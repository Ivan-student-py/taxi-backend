#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_passenger"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Passenger token not found."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Rating trip #1 with 5 stars..."

curl -s -X PATCH "$BASE_URL/trips/1/rate" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rating": 5}' | jq . 2>/dev/null || echo "$RESPONSE"