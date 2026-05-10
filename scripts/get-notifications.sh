#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
TRIP_ID="${1:-}"
TOKEN_FILE="scripts/.token_passenger"

AUTH_HEADER=""
if [ -f "$TOKEN_FILE" ]; then
    TOKEN=$(cat "$TOKEN_FILE")
    AUTH_HEADER="-H \"Authorization: Bearer $TOKEN\""
fi

echo "[INFO] Fetching notifications..."

if [ -n "$TRIP_ID" ]; then
    eval curl -s "$BASE_URL/notifications?trip_id=$TRIP_ID" $AUTH_HEADER | jq . 2>/dev/null || curl -s "$BASE_URL/notifications?trip_id=$TRIP_ID" $AUTH_HEADER
else
    eval curl -s "$BASE_URL/notifications" $AUTH_HEADER | jq . 2>/dev/null || curl -s "$BASE_URL/notifications" $AUTH_HEADER
fi