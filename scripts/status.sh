#!/bin/bash

check_jroom36() {
    STATUS=$(docker inspect --format='{{.State.Health.Status}}' jroom36-app 2>/dev/null)
    
    case "$STATUS" in
        "healthy")
            echo "🟢✅ jroom36 is healthy and running"
            ;;
        "unhealthy")
            echo "🔴❌ jroom36 is unhealthy - needs attention"
            ;;
        "starting")
            echo "🟡⏳ jroom36 is starting up..."
            ;;
        *)
            echo "⚪❓ jroom36 status unknown or not running"
            ;;
    esac
}

check_postgres() {
    STATUS=$(docker inspect --format='{{.State.Health.Status}}' jroom36-postgres 2>/dev/null)
    
    case "$STATUS" in
        "healthy")
            echo "🟢✅ Postgres is healthy and running"
            ;;
        "unhealthy")
            echo "🔴❌ Postgres is unhealthy - needs attention"
            ;;
        "starting")
            echo "🟡⏳ Postgres is starting up..."
            ;;
        *)
            echo "⚪❓ Postgres status unknown or not running"
            ;;
    esac
}

check_jroom36
check_postgres
