#!/bin/bash

source "./scripts/cd-project-dir.sh"
echo "🔄 Restarting Postgres, MinIO, Kafka..."
docker compose -f docker/docker-compose.datasources.yml restart
echo "✅ Restarted successfully!"