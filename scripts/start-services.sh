#!/bin/bash

source "./scripts/cd-project-dir.sh"
echo "🚀 Starting jroom36..."
docker compose -f docker/docker-compose.services.yml up -d

echo "✅ Started successfully!"
echo "📋 View logs manually: docker compose -f docker/docker-compose.services.yml logs -f jroom36"
