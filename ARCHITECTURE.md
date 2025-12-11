# 🏛️ Arquitectura del User Service

## Vista General del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENTE / API CONSUMER                    │
│                     (Otros microservicios, UI, etc.)            │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP/REST
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                      DOCKER NETWORK                              │
│                   (user-service-network)                         │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐     │
│  │         USER SERVICE CONTAINER (Port 8083)             │     │
│  │                                                        │     │
│  │  ┌──────────────────────────────────────────────┐     │     │
│  │  │        Spring Boot Application               │     │     │
│  │  │  ┌────────────────────────────────────────┐  │     │     │
│  │  │  │   REST Controllers (API Layer)         │  │     │     │
│  │  │  │   - /actuator/health                   │  │     │     │
│  │  │  │   - /actuator/info                     │  │     │     │
│  │  │  │   - /actuator/metrics                  │  │     │     │
│  │  │  └────────────────┬───────────────────────┘  │     │     │
│  │  │                   │                           │     │     │
│  │  │  ┌────────────────▼───────────────────────┐  │     │     │
│  │  │  │      DOMAIN LAYER (Core Business)     │  │     │     │
│  │  │  │                                        │  │     │     │
│  │  │  │  Aggregates:                          │  │     │     │
│  │  │  │  └─ UserAccount                       │  │     │     │
│  │  │  │                                        │  │     │     │
│  │  │  │  Value Objects:                       │  │     │     │
│  │  │  │  ├─ UserId                            │  │     │     │
│  │  │  │  └─ Email                             │  │     │     │
│  │  │  │                                        │  │     │     │
│  │  │  │  Ports (Interfaces):                  │  │     │     │
│  │  │  │  ├─ UserRepositoryPort                │  │     │     │
│  │  │  │  └─ PasswordEncoderPort               │  │     │     │
│  │  │  └────────────────┬───────────────────────┘  │     │     │
│  │  │                   │                           │     │     │
│  │  │  ┌────────────────▼───────────────────────┐  │     │     │
│  │  │  │    INFRASTRUCTURE LAYER               │  │     │     │
│  │  │  │                                        │  │     │     │
│  │  │  │  Adapters:                            │  │     │     │
│  │  │  │  ├─ UserRepositoryAdapter (JPA)       │  │     │     │
│  │  │  │  └─ PasswordEncoderAdapter (BCrypt)   │  │     │     │
│  │  │  │                                        │  │     │     │
│  │  │  │  Entities:                            │  │     │     │
│  │  │  │  └─ UserEntity (JPA)                  │  │     │     │
│  │  │  └────────────────┬───────────────────────┘  │     │     │
│  │  └───────────────────┼──────────────────────────┘     │     │
│  │                      │ JDBC/JPA                       │     │
│  └──────────────────────┼────────────────────────────────┘     │
│                         │                                      │
│  ┌──────────────────────▼────────────────────────────────┐     │
│  │    POSTGRESQL CONTAINER (Port 5432 internal)          │     │
│  │                                                        │     │
│  │  Database: userservice_db                             │     │
│  │                                                        │     │
│  │  Tables:                                              │     │
│  │  ├─ users                                             │     │
│  │  └─ user_roles                                        │     │
│  │                                                        │     │
│  │  Volume: user_service_db_data (persistent)           │     │
│  └────────────────────────────────────────────────────────┘     │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

## Flujo de Datos

### 1. Operación de Lectura (Query)
```
Cliente
  │
  └─> REST Controller (GET /api/v1/users/{id})
        │
        └─> UserService (Application)
              │
              └─> UserRepositoryPort (Domain Interface)
                    │
                    └─> UserRepositoryAdapter (Infrastructure)
                          │
                          └─> UserRepositoryJpaPort (JPA Repository)
                                │
                                └─> PostgreSQL Database
                                      │
                                      └─> Retorna UserEntity
                                            │
                                            └─> UserMapper convierte a UserAccount
                                                  │
                                                  └─> Retorna al cliente
```

### 2. Operación de Escritura (Command)
```
Cliente
  │
  └─> REST Controller (POST /api/v1/users)
        │
        └─> UserService (Application)
              │
              ├─> PasswordEncoderPort.encode()
              │     └─> PasswordEncoderAdapter (BCrypt)
              │
              └─> UserAccount.create() (Domain)
                    │
                    └─> UserRepositoryPort.save()
                          │
                          └─> UserRepositoryAdapter
                                │
                                └─> UserMapper convierte a UserEntity
                                      │
                                      └─> UserRepositoryJpaPort.save()
                                            │
                                            └─> PostgreSQL Database
```

## Capas de Arquitectura Hexagonal

### 🎯 Domain (Núcleo)
**Responsabilidad**: Lógica de negocio pura
- **Aggregates**: Entidades raíz con invariantes
- **Value Objects**: Objetos inmutables sin identidad
- **Ports**: Interfaces que definen contratos
- **Domain Events**: Eventos del dominio

**Reglas**:
- ❌ NO puede depender de infraestructura
- ❌ NO puede depender de frameworks
- ✅ Define la lógica de negocio
- ✅ Define contratos (interfaces)

### 🔌 Infrastructure (Adaptadores)
**Responsabilidad**: Implementaciones técnicas
- **Adapters**: Implementan los ports del dominio
- **Entities**: Representación de persistencia (JPA)
- **Mappers**: Conversión entre domain y persistence

**Reglas**:
- ✅ Implementa ports del dominio
- ✅ Usa frameworks y bibliotecas
- ✅ Conoce el dominio
- ❌ NO define lógica de negocio

### 🌐 Application (Casos de Uso)
**Responsabilidad**: Orquestación
- **Services**: Coordinan operaciones
- **Controllers**: Exponen API REST
- **DTOs**: Objetos de transferencia

**Reglas**:
- ✅ Coordina domain e infrastructure
- ✅ Maneja transacciones
- ❌ NO contiene lógica de negocio compleja

## Patrones de Diseño Utilizados

### 1. Hexagonal Architecture (Ports & Adapters)
- Aísla el dominio de los detalles de implementación
- Facilita testing y cambios de tecnología

### 2. Domain-Driven Design (DDD)
- **Aggregates**: UserAccount
- **Value Objects**: UserId, Email
- **Repositories**: Abstracción de persistencia

### 3. Dependency Injection
- Spring Boot proporciona beans
- Inversión de control (IoC)

### 4. Repository Pattern
- Abstrae la capa de datos
- UserRepositoryPort → UserRepositoryAdapter

### 5. Mapper Pattern
- Separa modelos de dominio de entidades de persistencia
- UserAccount ↔ UserEntity

## Decisiones Arquitectónicas

### ¿Por qué Arquitectura Hexagonal?
✅ **Testabilidad**: Fácil crear mocks de puertos  
✅ **Flexibilidad**: Cambiar BD sin afectar dominio  
✅ **Independencia**: El dominio no depende de frameworks  
✅ **Mantenibilidad**: Lógica de negocio aislada y clara  

### ¿Por qué Value Objects?
✅ **Inmutabilidad**: No pueden cambiar después de crearse  
✅ **Validación**: Encapsulan reglas de validación  
✅ **Type Safety**: Email != String  

### ¿Por qué Agregados?
✅ **Consistencia**: Garantizan invariantes del dominio  
✅ **Límites Transaccionales**: UserAccount es la raíz  
✅ **Encapsulación**: Protegen estado interno  

## Escalabilidad y Rendimiento

### Optimizaciones Actuales
- **Connection Pooling**: HikariCP con 10 conexiones
- **Batch Operations**: Hibernate batch size 20
- **Índices DB**: email, active, user_id
- **JVM Tuning**: -Xms256m -Xmx512m

### Escalabilidad Horizontal
```
┌─────────────┐
│ Load        │
│ Balancer    │
└──────┬──────┘
       │
   ┌───┴───┬─────────┬─────────┐
   │       │         │         │
┌──▼──┐ ┌──▼──┐  ┌──▼──┐  ┌──▼──┐
│ US1 │ │ US2 │  │ US3 │  │ US4 │  (User Service Instances)
└──┬──┘ └──┬──┘  └──┬──┘  └──┬──┘
   └───────┴─────────┴─────────┘
              │
        ┌─────▼─────┐
        │ PostgreSQL│
        │ (Primary) │
        └───────────┘
```

### Monitoreo y Observabilidad
- **Health Checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Logs**: Rotación automática, JSON format
- **Prometheus**: Ready para integración

## Seguridad

### Capas de Seguridad
1. **Contraseñas**: BCrypt con factor 10
2. **Contenedor**: Usuario no-root (appuser:1001)
3. **Red**: Red Docker aislada
4. **Secrets**: Variables de entorno
5. **Validación**: En Value Objects y Aggregates

## Testing

### Estrategia de Testing
```
┌─────────────────────────────────────────┐
│  Unit Tests (Domain)                    │
│  - UserAccount, UserId, Email          │
│  - Sin dependencias externas            │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  Integration Tests (Infrastructure)     │
│  - Adapters con H2                      │
│  - Mappers                              │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  E2E Tests (Future)                     │
│  - REST API completa                    │
│  - Con Testcontainers                   │
└─────────────────────────────────────────┘
```

---

## Referencias

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/)
- [Spring Boot Best Practices](https://spring.io/guides)
- [PostgreSQL Performance](https://www.postgresql.org/docs/current/performance-tips.html)
