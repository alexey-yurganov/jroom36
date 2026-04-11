#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SRC_DIR="$PROJECT_DIR/src"

source "$SCRIPT_DIR/activate-java-25-env.sh"

cd "$SRC_DIR"

echo "📦 Building JAR..."
mvn clean package -DskipTests -pl jroom36-api -am

cd "$PROJECT_DIR"

echo "🐳 Building Docker image..."
docker compose -f docker/docker-compose.yml --profile app build

echo "✅ Done"
