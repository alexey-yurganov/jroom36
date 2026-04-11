#!/bin/bash
source "./scripts/cd-project-dir.sh"

echo "⚠️  WARNING: HARD RESET will:"
echo "   • Stop and remove all containers"
echo "   • Delete all volumes (including ALL data)"
echo "   • Remove all images"
echo "   • Rebuild everything from scratch"
echo ""
echo "💾 This will PERMANENTLY DELETE:"
echo "   • All Postgres databases"
echo "   • All MinIO buckets and files"
echo "   • All Kafka topics and messages"
echo ""

read -r -p "❓ Are you absolutely sure? Type 'yes' to continue: " confirmation

if [ "$confirmation" != "yes" ]; then
    echo "❌ Reset cancelled."
    exit 0
fi

echo ""
echo "🛑 Stopping and removing containers, volumes, and images..."
docker compose -f docker/docker-compose.yml --profile datasources down -v --rmi all

echo "🔨 Rebuilding images without cache..."
docker compose -f docker/docker-compose.yml --profile datasources build --no-cache

echo "🚀 Starting fresh instances..."
docker compose -f docker/docker-compose.yml --profile datasources up -d

echo "✅ Hard reset completed successfully!"