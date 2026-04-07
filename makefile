.DEFAULT_GOAL := help

help:
	@echo "Available commands:"
	@echo "  make build   - Compile Java and Build Docker image"
	@echo "  make start   - Start all services"
	@echo "  make stop    - Stop all services"
	@echo "  make logs    - View logs"
	@echo "  make clean   - Clean everything"
	@echo "  make status  - Check application status(test endpoint pinged to check status)"

.PHONY: build start stop status logs clean

build:
	@./scripts/build.sh

start:
	@./scripts/start.sh

stop:
	@./scripts/stop.sh

status:
	@./scripts/status.sh

logs:
	docker compose -f docker/docker-compose.yml logs -f

clean:
	docker compose -f docker/docker-compose.yml down -v
	docker rmi jroom36:latest || true
	rm -rf */target/
