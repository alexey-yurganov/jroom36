.DEFAULT_GOAL := help

COMPOSE_FILE := docker/docker-compose.yml
COMPOSE := docker compose -f $(COMPOSE_FILE)

help:
	@echo "Available commands:"
	@echo "  make re-build   - rebuild and redeploy services"
	@echo "  make full-rebuild-rust-client - full re-build rust client(include docker images)"
	@echo "  make build-oas3-gen - build oas3-gen image (required for rust client generation)"
	@echo "  make build-rust-client - build rust client"
	@echo "  make build   - Compile Java and Build Docker image"
	@echo "  make start   - Start all services"
	@echo "  make stop    - Stop all services"
	@echo "  make logs    - View logs"
	@echo "  make clean   - Clean everything"
	@echo "  make status  - Check application status(test endpoint pinged to check status)"
	@echo "  make update-postgres  - Update Postgres"
	@echo "  make drop-postgres  - Drop Postgres"
	@echo "  make reset-datasources  - Reset Postgres, MinIO, Kafka"
	@echo "  make start-datasources  - Start Postgres, MinIO, Kafka"
	@echo "  make stop-datasources  - Stop Postgres, MinIO, Kafka"

.PHONY: build start stop status logs clean reset-datasources start-datasources stop-datasources

build-oas3-gen:
	@echo "🚀 Building oas3-gen image..."
	${COMPOSE} --profile build-oas3-gen up -d
	@echo "✅ oas3-gen image build completed successfully!"

build-rust-client:
	@echo "🚀 Building rust client..."
	${COMPOSE} --profile build-rust-client run --rm --remove-orphans build-rust-client
	${COMPOSE} --profile build-rust-types run --rm --remove-orphans build-rust-types
	@echo "✅ Rust client build completed successfully!"

full-rebuild-rust-client: build-oas3-gen build-rust-client

reset-datasources:
	@./scripts/reset-datasources.sh

start-datasources:
	@echo "🚀 Starting Postgres, MinIO, Kafka..."
	${COMPOSE} --profile datasources up -d
	@echo "✅ Started successfully!"

stop-datasources:
	@echo "🛑 Stopping Postgres, MinIO, Kafka..."
	${COMPOSE} --profile datasources down
	@echo "✅ Done"

re-build: stop build start

build:
	@./scripts/build-services.sh

start:
	@echo "🚀 Starting jroom36..."
	${COMPOSE} --profile app up -d
	@echo "✅ Started successfully!"

stop:
	@echo "🛑 Stopping jroom36..."
	${COMPOSE} --profile app down
	@echo "✅ Done"

status:
	@./scripts/status.sh

logs:
	${COMPOSE} --profile app logs -f

clean:
	${COMPOSE} --profile app down -v
	docker rmi jroom36:latest || true
	docker rmi oas3-gen-jroom36:0.25.3 || true
	rm -rf */target/

update-postgres:
	@./scripts/update-postgres.sh

drop-postgres:
	@./scripts/drop-postgres.sh
