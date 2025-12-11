# 👤 User Service - Microservicio Independiente

Microservicio independiente y reutilizable para la gestión de usuarios y autenticación, siguiendo los principios de arquitectura hexagonal.

## 🎯 Características

- ✅ **Completamente Independiente**: Sin dependencias de módulos externos
- ✅ **Gestión de Usuarios**: CRUD completo de usuarios
- ✅ **Autenticación Segura**: Codificación BCrypt de contraseñas
- ✅ **Base de Datos Propia**: PostgreSQL independiente
- ✅ **API RESTful**: Endpoints documentados
- ✅ **Health Checks**: Monitoreo integrado con Spring Actuator
- ✅ **Arquitectura Hexagonal**: Clean Architecture y DDD
- ✅ **Totalmente Dockerizado**: Docker y Docker Compose configurados
- ✅ **Producción Ready**: Configuración optimizada para despliegue

## 🚀 Inicio Rápido

### Requisitos Previos

- Docker y Docker Compose
- Java 21 (solo para desarrollo local)
- Maven 3.9+ (solo para desarrollo local)

### Ejecutar con Docker Compose (Recomendado)

```bash
# Clonar o copiar este directorio
cd user-service

# Copiar y configurar variables de entorno (opcional)
cp .env.example .env

# Iniciar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f user-service

# Detener servicios
docker-compose down
```

El servicio estará disponible en: `http://localhost:8083/api/v1`

### Verificar el Servicio

```bash
# Health check
curl http://localhost:8083/api/v1/actuator/health

# Info del servicio
curl http://localhost:8083/api/v1/actuator/info
```

## 📁 Estructura del Proyecto

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/health/tools/userservice/
│   │   │   ├── UserServiceApplication.java
│   │   │   ├── domain/
│   │   │   │   ├── model/              # Modelos de dominio (DDD)
│   │   │   │   │   ├── UserAccount.java
│   │   │   │   │   ├── UserId.java
│   │   │   │   │   ├── Email.java
│   │   │   │   │   └── UserRole.java
│   │   │   │   ├── port/               # Interfaces (Puertos hexagonales)
│   │   │   │   │   ├── UserRepositoryPort.java
│   │   │   │   │   └── PasswordEncoderPort.java
│   │   │   │   └── shared/             # Clases base DDD
│   │   │   │       ├── AggregateRoot.java
│   │   │   │       ├── ValueObject.java
│   │   │   │       └── DomainEvent.java
│   │   │   └── infrastructure/         # Adaptadores
│   │   │       ├── adapter/            # Implementaciones de puertos
│   │   │       ├── entity/             # Entidades JPA
│   │   │       └── mapper/             # Mappers entre capas
│   │   └── resources/
│   │       └── application.yml
│   └── test/                           # Tests unitarios e integración
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .env.example
└── README.md
```

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Puertos y Adaptadores)

```
┌─────────────────────────────────────────────┐
│           API REST (Adaptador)              │
│         controllers / endpoints             │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│          DOMAIN (Núcleo de Negocio)         │
│  ┌──────────────────────────────────────┐   │
│  │  UserAccount (Aggregate Root)        │   │
│  │  UserId, Email (Value Objects)       │   │
│  │  UserRole (Enum)                     │   │
│  └──────────────────────────────────────┘   │
│                                              │
│  ┌──────────────────────────────────────┐   │
│  │  Ports (Interfaces)                  │   │
│  │  - UserRepositoryPort                │   │
│  │  - PasswordEncoderPort               │   │
│  └──────────────────────────────────────┘   │
└──────────────────┬───────────────────────────┘
                   │
┌──────────────────▼───────────────────────────┐
│    INFRASTRUCTURE (Adaptadores)              │
│  ┌────────────────┐    ┌─────────────────┐   │
│  │ JPA Repository │    │ BCrypt Encoder  │   │
│  │   (Postgres)   │    │   (Security)    │   │
│  └────────────────┘    └─────────────────┘   │
└──────────────────────────────────────────────┘
```

### Modelo de Dominio

El núcleo del negocio está modelado usando DDD:

```java
// Crear usuario
UserId userId = UserId.generate();
Email email = Email.of("user@example.com");
UserAccount user = new UserAccount(userId, email, "John Doe");

// Asignar contraseña
user.setEncodedPassword(encodedPassword);

// Gestionar roles
user.assignRole(UserRole.ADMIN);
user.revokeRole(UserRole.USER);

// Control de estado
user.activate();
user.deactivate();
```

## ⚙️ Configuración

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `USERSERVICE_DB_NAME` | Nombre de la base de datos | `userservice_db` |
| `USERSERVICE_DB_USER` | Usuario de PostgreSQL | `userservice_user` |
| `USERSERVICE_DB_PASSWORD` | Contraseña de PostgreSQL | `userservice_secret` |
| `USERSERVICE_DB_PORT` | Puerto externo de PostgreSQL | `5433` |
| `USERSERVICE_PORT` | Puerto externo del servicio | `8083` |
| `SPRING_PROFILES_ACTIVE` | Perfil de Spring activo | `docker` |
| `LOG_LEVEL` | Nivel de logging | `INFO` |
| `JAVA_OPTS` | Opciones JVM | `-Xms256m -Xmx512m` |

### Perfiles de Spring

- **`default`**: Desarrollo local con PostgreSQL en localhost:5433
- **`docker`**: Ejecución en contenedores Docker
- **`test`**: Tests con base de datos H2 en memoria

## 🔧 Desarrollo Local

### Compilar el Proyecto

```bash
# Con Maven wrapper (recomendado)
./mvnw clean package

# Con Maven instalado
mvn clean package
```

### Ejecutar Tests

```bash
./mvnw test
```

### Ejecutar Localmente (sin Docker)

```bash
# Asegurar que PostgreSQL esté corriendo en localhost:5433
# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Build de Docker

```bash
# Build de la imagen
docker build -t user-service:latest .

# Ejecutar solo el contenedor
docker run -p 8083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/userservice_db \
  user-service:latest
```

## 📡 API Endpoints

### Actuator (Monitoreo)

```bash
GET /api/v1/actuator/health      # Estado del servicio
GET /api/v1/actuator/info        # Información del servicio
GET /api/v1/actuator/metrics     # Métricas
```

## 🔒 Seguridad

- **Contraseñas**: Codificadas con BCrypt (factor 10)
- **Usuario no-root**: Contenedor ejecuta con usuario `appuser`
- **Secrets**: Usar variables de entorno, nunca hardcodear
- **PostgreSQL**: Credenciales configurables via `.env`

## 📊 Monitoreo y Logs

### Ver Logs del Contenedor

```bash
# Logs en tiempo real
docker-compose logs -f user-service

# Últimas 100 líneas
docker-compose logs --tail=100 user-service
```

### Logs Persistentes

Los logs se almacenan en el volumen `user_service_logs`:

```bash
# Inspeccionar volumen
docker volume inspect user_service_logs

# Acceder a logs
docker run --rm -v user_service_logs:/logs alpine ls -la /logs
```

## 🐳 Docker Compose

El archivo `docker-compose.yml` incluye:

- **user-service-db**: PostgreSQL 16 con healthcheck
- **user-service**: Aplicación Spring Boot
- **Redes**: Red aislada `user-service-network`
- **Volúmenes**: Persistencia de datos y logs
- **Health Checks**: Monitoreo automático
- **Resource Limits**: Control de CPU y memoria

## 🚀 Despliegue en Producción

### Consideraciones

1. **Variables de Entorno**: Configurar todas las variables sensibles
2. **Volúmenes**: Usar volúmenes nombrados o bind mounts para persistencia
3. **Límites de Recursos**: Ajustar según necesidades
4. **Logs**: Configurar rotación y centralización
5. **Backup**: Implementar estrategia de backup de PostgreSQL
6. **Secrets**: Usar Docker Secrets o servicios de gestión de secretos

### Ejemplo de Despliegue

```bash
# Producción con configuración personalizada
export USERSERVICE_DB_PASSWORD=$(cat /run/secrets/db_password)
export SPRING_PROFILES_ACTIVE=prod
export LOG_LEVEL=WARN
export JAVA_OPTS="-Xms512m -Xmx1024m"

docker-compose up -d
```

## 🔄 Integración con Otros Servicios

Este microservicio puede integrarse fácilmente:

```yaml
# En otro docker-compose.yml
services:
  my-app:
    image: my-app:latest
    environment:
      USER_SERVICE_URL: http://user-service:8083/api/v1
    networks:
      - user-service-network

networks:
  user-service-network:
    external: true
```

## 🛠️ Troubleshooting

### El servicio no arranca

```bash
# Verificar logs
docker-compose logs user-service

# Verificar health de la base de datos
docker-compose ps
```

### Base de datos no conecta

```bash
# Verificar que PostgreSQL esté saludable
docker-compose exec user-service-db pg_isready -U userservice_user

# Verificar conectividad desde el contenedor
docker-compose exec user-service wget -qO- http://user-service-db:5432
```

### Limpiar y reiniciar

```bash
# Detener y eliminar contenedores
docker-compose down

# Eliminar volúmenes (¡CUIDADO: borra datos!)
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache

# Iniciar de nuevo
docker-compose up -d
```

## 📝 Licencia

Este proyecto es parte de Health Tools y está diseñado para ser reutilizable en diferentes contextos.

## 🤝 Contribuir

Para contribuir a este microservicio:

1. Fork el proyecto
2. Crear una rama de feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

---

**Nota**: Este es un microservicio completamente independiente que puede ser desplegado y ejecutado sin dependencias externas más allá de su base de datos PostgreSQL.


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
