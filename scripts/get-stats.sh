#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
echo "[INFO] Fetching trip statistics..."
curl -s "$BASE_URL/stats/trips" | jq . 2>/dev/null || curl -s "$BASE_URL/stats/trips"