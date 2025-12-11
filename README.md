# 👤 User Service - Servicio Independiente

Servicio independiente y reutilizable para la gestión de usuarios y autenticación.

## 🎯 Características

- ✅ Gestión completa de usuarios (CRUD)
- ✅ Autenticación y autorización
- ✅ Codificación segura de contraseñas (BCrypt)
- ✅ Base de datos PostgreSQL independiente
- ✅ API RESTful
- ✅ Health checks y monitoreo
- ✅ Arquitectura hexagonal (Clean Architecture)
- ✅ Completamente dockerizado

## 🏗️ Arquitectura

Este servicio sigue los principios de arquitectura hexagonal:

### `domain/model/`
- **`UserId.java`**: Value Object para identificador único de usuario
- **`UserAccount.java`**: Agregado raíz para cuenta de usuario
  - Contiene información de perfil básica
  - Gestiona contraseña y estado de cuenta
  - Valida invariantes de negocio
- **`UserRole.java`**: Enumeración de roles disponibles
- **`Email.java`**: Value Object para direcciones de correo

### `domain/port/`
- **`UserRepositoryPort`**: Contrato para persistencia de usuarios
- **`PasswordEncoderPort`**: Abstracción para codificación de contraseña

### `infrastructure/`
- **Adapters**: Implementaciones JPA y BCrypt
- **Entities**: Entidades de base de datos
- **Mappers**: Conversión entre capas

## Modelo de Dominio

```java
// Crear usuario
UserId userId = UserId.generate();
Email email = Email.of("user@example.com");
UserAccount user = new UserAccount(userId, email, "John Doe");

// Cambiar contraseña
user.changePassword("oldPassword", "newPassword", passwordEncoder);

// Asignar rol
user.assignRole(UserRole.THERAPIST);
```

## 🚀 Inicio Rápido

### Prerrequisitos

- Docker y Docker Compose
- Java 21 (solo para desarrollo local sin Docker)
- Maven 3.9+ (solo para desarrollo local sin Docker)

### Ejecución con Docker Compose (Recomendado)

1. **Configurar variables de entorno**
   ```bash
   cp .env.example .env
   # Editar .env según tus necesidades
   ```

2. **Iniciar los servicios**
   ```bash
   docker-compose up -d
   ```

3. **Verificar el estado**
   ```bash
   docker-compose ps
   curl http://localhost:8083/api/v1/actuator/health
   ```

### Ejecución Local (Desarrollo)

1. **Iniciar solo la base de datos**
   ```bash
   docker-compose up -d user-service-db
   ```

2. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

## 🔧 Configuración

### Variables de Entorno

| Variable | Descripción | Por Defecto |
|----------|-------------|-------------|
| `USERSERVICE_DB_NAME` | Nombre de la base de datos | `userservice_db` |
| `USERSERVICE_DB_USER` | Usuario de la base de datos | `userservice_user` |
| `USERSERVICE_DB_PASSWORD` | Contraseña de la base de datos | `userservice_secret` |
| `USERSERVICE_DB_PORT` | Puerto de la base de datos | `5433` |
| `USERSERVICE_PORT` | Puerto del servicio | `8083` |
| `LOG_LEVEL` | Nivel de logging | `INFO` |
| `JAVA_OPTS` | Opciones JVM | `-Xms256m -Xmx512m` |

### Perfiles de Spring

- `default`: Configuración para desarrollo local
- `docker`: Configuración para contenedores Docker
- `test`: Configuración para tests (H2 en memoria)

## 📡 API Endpoints

### Health Check
```bash
GET /api/v1/actuator/health
```

### Gestión de Usuarios
```bash
# Obtener todos los usuarios
GET /api/v1/users

# Obtener usuario por ID
GET /api/v1/users/{id}

# Crear usuario
POST /api/v1/users

# Actualizar usuario
PUT /api/v1/users/{id}

# Eliminar usuario
DELETE /api/v1/users/{id}
```

## 🧪 Testing

### Ejecutar todos los tests
```bash
mvn test
```

### Ejecutar tests con cobertura
```bash
mvn clean test jacoco:report
```

El reporte de cobertura estará en: `target/site/jacoco/index.html`

**Tests disponibles:**
- `UserAccountTest`: Tests de agregado raíz
- `UserIdTest`: Tests de value object
- `EmailTest`: Tests de value object de correo
- `UserRepositoryAdapterTest`: Tests de adaptador de persistencia

## 🐳 Docker

### Construir imagen
```bash
docker build -t health-tools/user-service:1.0.0 -f Dockerfile ../..
```

### Ejecutar contenedor standalone
```bash
docker run -d \
  --name user-service \
  -p 8083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/userservice_db \
  health-tools/user-service:1.0.0
```

## 🔄 Integración como Módulo

Si deseas integrar este servicio en un proyecto más grande:

1. **Como submódulo Git**
   ```bash
   git submodule add <user-service-repo-url> modules/user-service
   ```

2. **En docker-compose.yml principal** (orquestación)
   ```yaml
   services:
     user-service:
       extends:
         file: modules/user-service/docker-compose.yml
         service: user-service
       networks:
         - main-network
   ```

## 📦 Dependencias

- Common Module (ValueObject base, AggregateRoot)
- Spring Boot 3.5.7
- Spring Data JPA
- Spring Security (BCrypt)
- PostgreSQL Driver
- H2 (tests)

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto es de uso interno. Todos los derechos reservados.
