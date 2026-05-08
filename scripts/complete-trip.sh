#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_driver"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Driver token not found."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Completing trip #1..."

curl -s -X PATCH "$BASE_URL/trips/1/status?status=COMPLETED" \
  -H "Authorization: Bearer $TOKEN" | jq . 2>/dev/null || echo "$RESPONSE"