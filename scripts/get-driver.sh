#!/bin/bash
ID="${1:-1}"
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_driver"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Driver token not found. Run register-driver.sh first."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Fetching driver #$ID..."

curl -s -X GET "$BASE_URL/users/drivers/$ID" \
  -H "Authorization: Bearer $TOKEN" | jq . 2>/dev/null