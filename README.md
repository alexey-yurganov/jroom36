# Jroom36

File Storage Service with REST API, WebSocket and MinIO

## Tech Stack

- Java 25
- Spring Boot 4.0.5
- PostgreSql 18.3
- MinIO
- Maven
- Docker Compose

## Quick Start

### Prerequisites

- Docker & Docker Compose
- Make
- sdkman is used to manage Java

### Run with Make
```bash
# Build and start all services
make build
make start

# Check application status
make status

# View logs
make logs

# Stop services
make stop

# Clean everything
make clean
```

### API Endpoints

#### Check version
curl http://localhost:8080/api/v1/version
