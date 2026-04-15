#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SRC_DIR="$PROJECT_DIR/src"

echo "🚀 Starting Liquibase database migration..."

cd "$PROJECT_DIR"
echo "📁 Changed to: $(pwd)"

echo "🔧 Executing Liquibase update..."
docker compose -f docker/docker-compose.yml --profile migration run --rm liquibase

echo "🎉 Database migration completed successfully!"