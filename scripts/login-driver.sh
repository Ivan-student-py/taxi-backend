#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
EMAIL="driver@test.com"
PASSWORD="pass123"

echo "[INFO] Logging in driver: $EMAIL..."

RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")

echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"

TOKEN=$(echo "$RESPONSE" | jq -r '.token' 2>/dev/null)
if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo "$TOKEN" > scripts/.token_driver
    echo "[INFO] Driver token saved to scripts/.token_driver"
fi