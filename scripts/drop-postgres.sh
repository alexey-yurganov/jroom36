#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SRC_DIR="$PROJECT_DIR/backend"

echo "🚀 Starting Liquibase database drop..."

cd "$PROJECT_DIR"
echo "📁 Changed to: $(pwd)"

echo "🔧 Executing Liquibase drop..."
docker compose -f docker/docker-compose.yml --profile drop-db run --rm liquibase-drop

echo "🎉 Database drop completed successfully!"