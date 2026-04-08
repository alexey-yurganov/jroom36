#!/bin/bash

source "./scripts/cd-project-dir.sh"
echo "🛑 Stopping Postgres, MinIO, Kafka..."
docker compose -f docker/docker-compose.datasources.yml down
echo "✅ Done"
