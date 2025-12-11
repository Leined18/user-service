# User Service - Script de Utilidades para Windows
# Facilita operaciones comunes con Docker Compose

param(
    [Parameter(Position=0)]
    [string]$Command = "help"
)

function Show-Help {
    Write-Host "User Service - Comandos disponibles:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  start         " -NoNewline -ForegroundColor Green
    Write-Host "Iniciar todos los servicios"
    Write-Host "  stop          " -NoNewline -ForegroundColor Green
    Write-Host "Detener todos los servicios"
    Write-Host "  restart       " -NoNewline -ForegroundColor Green
    Write-Host "Reiniciar todos los servicios"
    Write-Host "  logs          " -NoNewline -ForegroundColor Green
    Write-Host "Ver logs del servicio"
    Write-Host "  logs-db       " -NoNewline -ForegroundColor Green
    Write-Host "Ver logs de la base de datos"
    Write-Host "  status        " -NoNewline -ForegroundColor Green
    Write-Host "Estado de los servicios"
    Write-Host "  build         " -NoNewline -ForegroundColor Green
    Write-Host "Construir imagen Docker"
    Write-Host "  rebuild       " -NoNewline -ForegroundColor Green
    Write-Host "Reconstruir sin caché"
    Write-Host "  clean         " -NoNewline -ForegroundColor Green
    Write-Host "Limpiar contenedores y volúmenes"
    Write-Host "  health        " -NoNewline -ForegroundColor Green
    Write-Host "Verificar health del servicio"
    Write-Host "  shell         " -NoNewline -ForegroundColor Green
    Write-Host "Acceder al contenedor"
    Write-Host "  shell-db      " -NoNewline -ForegroundColor Green
    Write-Host "Acceder a PostgreSQL"
    Write-Host "  test          " -NoNewline -ForegroundColor Green
    Write-Host "Ejecutar tests"
    Write-Host "  package       " -NoNewline -ForegroundColor Green
    Write-Host "Empaquetar aplicación"
    Write-Host "  backup        " -NoNewline -ForegroundColor Green
    Write-Host "Crear backup de la BD"
    Write-Host ""
    Write-Host "Uso: .\manage.ps1 <comando>" -ForegroundColor Yellow
}

function Start-Services {
    Write-Host "Iniciando servicios..." -ForegroundColor Green
    docker-compose up -d
    Write-Host "Servicios iniciados. Disponible en: http://localhost:8083/api/v1" -ForegroundColor Green
}

function Stop-Services {
    Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
    docker-compose down
}

function Restart-Services {
    Stop-Services
    Start-Services
}

function Show-Logs {
    Write-Host "Mostrando logs (Ctrl+C para salir)..." -ForegroundColor Cyan
    docker-compose logs -f user-service
}

function Show-DbLogs {
    Write-Host "Mostrando logs de BD (Ctrl+C para salir)..." -ForegroundColor Cyan
    docker-compose logs -f user-service-db
}

function Show-Status {
    docker-compose ps
}

function Build-Image {
    Write-Host "Construyendo imagen..." -ForegroundColor Green
    docker-compose build user-service
}

function Rebuild-Image {
    Write-Host "Reconstruyendo imagen sin caché..." -ForegroundColor Green
    docker-compose build --no-cache user-service
    docker-compose up -d user-service
}

function Clean-All {
    Write-Host "Limpiando contenedores y volúmenes..." -ForegroundColor Red
    $confirmation = Read-Host "¿Estás seguro? Esto eliminará los datos (S/N)"
    if ($confirmation -eq 'S' -or $confirmation -eq 's') {
        docker-compose down -v
        docker system prune -f
        Write-Host "Limpieza completada" -ForegroundColor Green
    } else {
        Write-Host "Operación cancelada" -ForegroundColor Yellow
    }
}

function Check-Health {
    Write-Host "Verificando health del servicio..." -ForegroundColor Cyan
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/actuator/health"
        $response | ConvertTo-Json -Depth 10
        Write-Host "Estado: OK" -ForegroundColor Green
    } catch {
        Write-Host "Error: El servicio no está disponible" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
    }
}

function Enter-Shell {
    Write-Host "Accediendo al contenedor..." -ForegroundColor Cyan
    docker-compose exec user-service sh
}

function Enter-DbShell {
    Write-Host "Accediendo a PostgreSQL..." -ForegroundColor Cyan
    docker-compose exec user-service-db psql -U userservice_user -d userservice_db
}

function Run-Tests {
    Write-Host "Ejecutando tests..." -ForegroundColor Cyan
    .\mvnw.cmd test
}

function Build-Package {
    Write-Host "Empaquetando aplicación..." -ForegroundColor Cyan
    .\mvnw.cmd clean package
}

function Backup-Database {
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    $backupFile = "backup_$timestamp.sql"
    Write-Host "Creando backup: $backupFile" -ForegroundColor Cyan
    docker-compose exec -T user-service-db pg_dump -U userservice_user userservice_db > $backupFile
    Write-Host "Backup creado: $backupFile" -ForegroundColor Green
}

# Ejecutar comando
switch ($Command.ToLower()) {
    "start"    { Start-Services }
    "stop"     { Stop-Services }
    "restart"  { Restart-Services }
    "logs"     { Show-Logs }
    "logs-db"  { Show-DbLogs }
    "status"   { Show-Status }
    "build"    { Build-Image }
    "rebuild"  { Rebuild-Image }
    "clean"    { Clean-All }
    "health"   { Check-Health }
    "shell"    { Enter-Shell }
    "shell-db" { Enter-DbShell }
    "test"     { Run-Tests }
    "package"  { Build-Package }
    "backup"   { Backup-Database }
    default    { Show-Help }
}
