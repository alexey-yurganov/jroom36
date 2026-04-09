.DEFAULT_GOAL := help

help:
	@echo "Available commands:"
	@echo "  make build   - Compile Java and Build Docker image"
	@echo "  make start   - Start all services"
	@echo "  make stop    - Stop all services"
	@echo "  make logs    - View logs"
	@echo "  make clean   - Clean everything"
	@echo "  make status  - Check application status(test endpoint pinged to check status)"
	@echo "  make update-postgres  - Update Postgres"
	@echo "  make reset-datasources  - Reset Postgres, MinIO, Kafka"
	@echo "  make start-datasources  - Start Postgres, MinIO, Kafka"
	@echo "  make stop-datasources  - Stop Postgres, MinIO, Kafka"

.PHONY: build start stop status logs clean reset-datasources start-datasources stop-datasources

reset-datasources:
	@./scripts/reset-datasources.sh

start-datasources:
	@./scripts/start-datasources.sh

stop-datasources:
	@./scripts/stop-datasources.sh

build:
	@./scripts/build-services.sh

start:
	@./scripts/start-services.sh

stop:
	@./scripts/stop-services.sh

status:
	@./scripts/print-status-of-services.sh

logs:
	docker compose -f docker/docker-compose.services.yml logs -f

clean:
	docker compose -f docker/docker-compose.services.yml down -v
	docker rmi jroom36:latest || true
	rm -rf */target/

update-postgres:
	@./scripts/update-postgres.sh
