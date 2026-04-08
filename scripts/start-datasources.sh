#!/bin/bash

source "./scripts/cd-project-dir.sh"
echo "🚀 Starting Postgres, MinIO, Kafka..."
docker compose -f docker/docker-compose.datasources.yml up -d
echo "✅ Started successfully!"
