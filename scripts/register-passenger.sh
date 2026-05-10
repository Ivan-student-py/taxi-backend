#!/bin/bash
BASE_URL="http://127.0.0.1:8080"

echo "[INFO] Sending passenger registration request..."

RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register/passenger" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ivan",
    "email": "ivan@test.com",
    "phone": "+79991234567",
    "password": "pass123"
  }')

echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"

TOKEN=$(echo "$RESPONSE" | jq -r '.token' 2>/dev/null)
if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo "$TOKEN" > scripts/.token_passenger
    echo "[INFO] Token saved to scripts/.token_passenger"
fi