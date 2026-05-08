#!/bin/bash
ID="${1:-1}"
STATUS="${2:-BUSY}"
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_driver"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Driver token not found. Please register/login as driver first."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Updating driver #$ID status to $STATUS..."

curl -s -X PATCH "$BASE_URL/users/drivers/$ID/status?status=$STATUS" \
  -H "Authorization: Bearer $TOKEN" | jq . 2>/dev/null