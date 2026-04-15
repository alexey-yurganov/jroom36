# Jroom36

File Storage Service with REST API, WebSocket and MinIO

Demo UI(Figma make): https://help-marsh-18266473.figma.site/

<img width="1842" height="1118" alt="Weyland-Yutani" src="https://github.com/user-attachments/assets/a62663c3-ad50-4904-b532-1912b2fa9626" />


## Tech Stack
### App
- Java 25
- Spring Boot 4.0.5
- PostgreSql 18.3
- MinIO(+tux for big file processing)
- Maven
- Docker Compose

### For chat part
- **REST API** — send new messages, get message history per room
- **WebSocket (STOMP) over RabbitMQ** — real-time updates: typing status, online/offline presence, room changes
- **Redis** — cache active WebSocket connections and user sessions
- **Outbox Pattern + Debezium CDC** — capture message changes from PostgreSQL WAL (exactly-once delivery)
- **Apache Kafka** — distribute messages and events to consumers (WebSocket push, analytics, storage)
- **OpenTelemetry + Prometheus + Grafana** — monitor system health, subscribed to Kafka metrics

```mermaid
%%{
  init: {
    'theme': 'base',
    'themeVariables': {
      'background': '#0a0f1a',
      'primaryColor': '#1a2a4f',
      'primaryTextColor': '#e2e8f0',
      'primaryBorderColor': '#3b82f6',
      'lineColor': '#3b82f6',
      'secondaryColor': '#1e3a8a',
      'tertiaryColor': '#0f2b3d',
      'clusterBkg': '#0f172a',
      'clusterBorder': '#3b82f6',
      'titleColor': '#ffffff',
      'edgeLabelBackground': '#1e293b'
    },
    'flowchart': {
      'curve': 'basis',
      'padding': 20,
      'nodeSpacing': 60,
      'rankSpacing': 60
    }
  }
}%%
graph TB
    subgraph "CLIENT LAYER"
        CLIENT["Web/Mobile Client<br/>REST + WebSocket STOMP"]
    end

    subgraph "EDGE LAYER"
        API_GW["API Gateway<br/>Rate Limiting + Auth"]
    end

    subgraph "APPLICATION LAYER (Spring Boot 4.0.5)"
        REST_API["REST API<br/>POST /messages | GET /history"]
        OUTBOX["Outbox Service<br/>@Transactional"]
        WS_HANDLER["WebSocket Handler<br/>STOMP over RabbitMQ"]
    end

    subgraph "STORAGE LAYER"
        PG[("PostgreSQL 18.3<br/>outbox + messages + WAL")]
        REDIS[("Redis<br/>WS Sessions + Presence")]
    end

    subgraph "STREAMING LAYER"
        DEBEZIUM["Debezium CDC<br/>WAL Reader + Outbox"]
        KAFKA["Kafka<br/>chat.messages | typing | presence"]
    end

    subgraph "MESSAGE BROKER"
        RABBITMQ["RabbitMQ<br/>STOMP Broker"]
    end

    subgraph "OBSERVABILITY"
        OTEL["OpenTelemetry"]
        PROM["Prometheus"]
        GRAFANA["Grafana"]
    end

    CLIENT -->|REST/WS| API_GW
    API_GW -->|POST /messages| REST_API
    API_GW -->|WebSocket upgrade| WS_HANDLER
    
    REST_API -->|create event| OUTBOX
    OUTBOX -->|INSERT| PG
    
    PG -->|WAL stream| DEBEZIUM
    DEBEZIUM -->|publish| KAFKA
    
    KAFKA -->|consume| WS_HANDLER
    
    WS_HANDLER -->|broadcast| RABBITMQ
    WS_HANDLER -->|cache| REDIS
    
    OTEL -->|metrics| PROM
    PROM -->|visualize| GRAFANA
    KAFKA -.->|metrics| OTEL
```

  
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
