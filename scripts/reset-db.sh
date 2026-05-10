#!/bin/bash
PG_USER="taxi_user"
PG_PASS="taxi_password"
PG_DB="taxi_db"
PG_HOST="${1:-localhost}"
SQL_FILE="scripts/reset-db.sql"

echo "[INFO] Connecting to PostgreSQL..."
export PGPASSWORD="$PG_PASS"

if ! psql -U "$PG_USER" -h "$PG_HOST" -d "$PG_DB" -c "\q" 2>/dev/null; then
    echo "[ERROR] Connection failed. Check credentials or ensure PostgreSQL is running."
    exit 1
fi

echo "[INFO] Cleaning database and resetting sequences..."
psql -U "$PG_USER" -h "$PG_HOST" -d "$PG_DB" -f "$SQL_FILE"

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Database cleaned successfully."
else
    echo "[ERROR] Cleanup failed."
    exit 1
fi

unset PGPASSWORD