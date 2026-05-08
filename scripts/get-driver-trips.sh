#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_driver"
DRIVER_ID="${1:-1}"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Driver token not found. Run register-driver.sh first."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")
echo "[INFO] Fetching trips for driver #$DRIVER_ID..."
curl -s -X GET "$BASE_URL/trips/driver/$DRIVER_ID" \
  -H "Authorization: Bearer $TOKEN" | jq . 2>/dev/null || curl -s -X GET "$BASE_URL/trips/driver/$DRIVER_ID" -H "Authorization: Bearer $TOKEN"