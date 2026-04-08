#!/bin/bash

source "./scripts/cd-project-dir.sh"
echo "🛑 Stopping jroom36..."
docker compose -f docker/docker-compose.services.yml down

echo "✅ Done"
