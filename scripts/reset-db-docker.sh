#!/bin/bash
echo "[INFO] Resetting database inside Docker container..."

if ! docker compose ps | grep -q "postgres.*Up"; then
    echo "[ERROR] PostgreSQL container is not running. Start it first: docker compose up -d"
    exit 1
fi

docker compose exec postgres psql -U taxi_user -d taxi_db -c \
"TRUNCATE TABLE notification_tasks, trips, drivers, passengers RESTART IDENTITY CASCADE;"

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Database cleaned. IDs reset to 1."
else
    echo "[ERROR] Cleanup failed. Check container logs."
    exit 1
fi