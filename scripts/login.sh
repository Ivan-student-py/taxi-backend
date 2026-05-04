EMAIL="${1:-ivan2@test.com}"
PASSWORD="${2:-pass123}"
BASE_URL="http://127.0.0.1:8080"

echo "[INFO] Logging in: $EMAIL"

RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")

echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"

TOKEN=$(echo "$RESPONSE" | jq -r '.token' 2>/dev/null)
ROLE=$(echo "$RESPONSE" | jq -r '.role' 2>/dev/null | tr '[:upper:]' '[:lower:]')

if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo "$TOKEN" > "scripts/.token_$ROLE"
    echo "[INFO] Token saved to scripts/.token_$ROLE"
fi