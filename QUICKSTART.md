# 🚀 Guía de Inicio Rápido - User Service

## Para Usuarios de Windows

### 1. Prerrequisitos
- Docker Desktop instalado y ejecutándose
- (Opcional) Git Bash o PowerShell

### 2. Inicio Rápido con PowerShell

```powershell
# Navegar al directorio
cd user-service

# Iniciar todos los servicios
.\manage.ps1 start

# Verificar estado
.\manage.ps1 health

# Ver logs
.\manage.ps1 logs
```

### 3. Inicio Manual con Docker Compose

```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f user-service

# Detener
docker-compose down
```

### 4. Verificar el Servicio

Abrir en navegador o usar curl:
- Health: http://localhost:8083/api/v1/actuator/health
- Info: http://localhost:8083/api/v1/actuator/info

```powershell
# Con PowerShell
Invoke-RestMethod http://localhost:8083/api/v1/actuator/health

# Con curl (Git Bash o WSL)
curl http://localhost:8083/api/v1/actuator/health
```

---

## Para Usuarios de Linux/Mac

### 1. Prerrequisitos
- Docker y Docker Compose instalados
- Make (opcional, facilita comandos)

### 2. Inicio Rápido con Make

```bash
# Ver comandos disponibles
make help

# Iniciar servicios
make up

# Ver logs
make logs

# Verificar health
make health
```

### 3. Inicio Manual

```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f user-service

# Detener
docker-compose down
```

---

## Comandos Útiles

### Ver Estado de Servicios
```bash
docker-compose ps
```

### Acceder a la Base de Datos
```bash
# Desde Windows PowerShell
.\manage.ps1 shell-db

# Desde Linux/Mac
make shell-db

# Manual
docker-compose exec user-service-db psql -U userservice_user -d userservice_db
```

### Reiniciar Servicios
```bash
# Windows
.\manage.ps1 restart

# Linux/Mac
make restart

# Manual
docker-compose restart
```

### Backup de Base de Datos
```bash
# Windows
.\manage.ps1 backup

# Linux/Mac
make db-backup
```

---

## Configuración Personalizada

### 1. Crear archivo .env
```bash
cp .env.example .env
```

### 2. Editar variables (opcional)
```env
USERSERVICE_DB_PASSWORD=tu_password_seguro
USERSERVICE_PORT=8083
LOG_LEVEL=DEBUG
```

### 3. Reiniciar servicios
```bash
docker-compose down
docker-compose up -d
```

---

## Troubleshooting

### El servicio no inicia
```bash
# Ver logs detallados
docker-compose logs user-service

# Verificar que PostgreSQL esté healthy
docker-compose ps
```

### Puerto 8083 ya está en uso
```bash
# Cambiar puerto en .env
echo "USERSERVICE_PORT=8084" >> .env

# Reiniciar
docker-compose down
docker-compose up -d
```

### Limpiar y empezar de nuevo
```bash
# Windows
.\manage.ps1 clean

# Linux/Mac
make clean

# Manual
docker-compose down -v
docker-compose up -d
```

---

## Desarrollo Local (sin Docker)

### 1. Iniciar PostgreSQL
```bash
docker-compose up -d user-service-db
```

### 2. Ejecutar aplicación
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### 3. Ejecutar tests
```bash
# Windows
.\mvnw.cmd test

# Linux/Mac
./mvnw test
```

---

## Endpoints Disponibles

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/v1/actuator/health` | GET | Estado del servicio |
| `/api/v1/actuator/info` | GET | Información del servicio |
| `/api/v1/actuator/metrics` | GET | Métricas del servicio |

---

## Próximos Pasos

1. ✅ Servicios iniciados
2. ⬜ Implementar endpoints de API REST
3. ⬜ Agregar seguridad JWT
4. ⬜ Configurar CI/CD
5. ⬜ Desplegar en producción

---

**¿Necesitas ayuda?** Consulta el [README.md](README.md) para documentación completa.
