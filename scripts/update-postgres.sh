#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SRC_DIR="$PROJECT_DIR/src"

echo "🚀 Starting Liquibase database migration..."

cd "$SRC_DIR/jroom36-db"
echo "📁 Changed to: $(pwd)"

if [ -f ../../.env ]; then
    echo "📄 Loading .env file..."
    set -a
    source ../../.env
    set +a

    # Build POSTGRES_URL with optional parameters
    POSTGRES_HOST="${POSTGRES_HOST:-localhost}"
    POSTGRES_PORT="${POSTGRES_PORT:-5432}"

    # Base URL
    POSTGRES_URL="jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB_NAME}"

    # Add SSL parameters if needed
    if [ -n "${POSTGRES_SSL_MODE}" ]; then
        POSTGRES_URL="${POSTGRES_URL}?ssl=${POSTGRES_SSL:-false}&sslmode=${POSTGRES_SSL_MODE}"
    fi

    echo "✅ Environment variables loaded"
    echo "   POSTGRES_URL: ${POSTGRES_URL}"
    echo "   POSTGRES_USERNAME: ${POSTGRES_USERNAME}"
else
    echo "⚠️  Warning: .env file not found at ../../.env"
    exit 1
fi

echo "🔧 Executing Liquibase update..."
mvn liquibase:update \
    -Dliquibase.url="${POSTGRES_URL}" \
    -Dliquibase.username="${POSTGRES_USERNAME}" \
    -Dliquibase.password="${POSTGRES_PASSWORD}" \
    -Dliquibase.driver=org.postgresql.Driver \
    -Dliquibase.changeLogFile=src/main/resources/db/changelog/db.changelog-master.yaml

echo "🎉 Database migration completed successfully!"