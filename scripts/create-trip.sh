#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_passenger"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Passenger token not found. Run register-passenger.sh first."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Creating trip (distance: 35km, tariff: 15)..."

RESPONSE=$(curl -s -X POST "$BASE_URL/trips" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "passengerId": 1,
    "origin": "Moscow City",
    "destination": "Sheremetyevo",
    "distance": 35.0
  }')

echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"