.PHONY: help build up down restart logs clean test package install

# Variables
SERVICE_NAME=user-service
DB_SERVICE=user-service-db

help: ## Muestra esta ayuda
	@echo "User Service - Comandos disponibles:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Construir la imagen Docker
	docker-compose build $(SERVICE_NAME)

up: ## Iniciar todos los servicios
	docker-compose up -d

down: ## Detener todos los servicios
	docker-compose down

restart: down up ## Reiniciar todos los servicios

logs: ## Ver logs del servicio
	docker-compose logs -f $(SERVICE_NAME)

logs-db: ## Ver logs de la base de datos
	docker-compose logs -f $(DB_SERVICE)

clean: ## Limpiar contenedores y volúmenes
	docker-compose down -v
	docker system prune -f

clean-all: clean ## Limpiar todo incluyendo imágenes
	docker-compose down --rmi all -v
	docker system prune -af

test: ## Ejecutar tests
	./mvnw test

package: ## Empaquetar la aplicación
	./mvnw clean package

install: ## Instalar en repositorio Maven local
	./mvnw clean install

run-local: ## Ejecutar localmente (sin Docker)
	./mvnw spring-boot:run

ps: ## Mostrar estado de los servicios
	docker-compose ps

shell: ## Acceder al shell del contenedor
	docker-compose exec $(SERVICE_NAME) sh

shell-db: ## Acceder al shell de PostgreSQL
	docker-compose exec $(DB_SERVICE) psql -U userservice_user -d userservice_db

health: ## Verificar el estado del servicio
	@curl -s http://localhost:8083/api/v1/actuator/health | jq '.'

db-backup: ## Crear backup de la base de datos
	docker-compose exec -T $(DB_SERVICE) pg_dump -U userservice_user userservice_db > backup_$(shell date +%Y%m%d_%H%M%S).sql

db-restore: ## Restaurar base de datos (requiere BACKUP_FILE=nombre.sql)
	@if [ -z "$(BACKUP_FILE)" ]; then echo "Error: Especifica BACKUP_FILE=nombre.sql"; exit 1; fi
	docker-compose exec -T $(DB_SERVICE) psql -U userservice_user -d userservice_db < $(BACKUP_FILE)

rebuild: ## Reconstruir la imagen sin caché
	docker-compose build --no-cache $(SERVICE_NAME)
	docker-compose up -d $(SERVICE_NAME)
