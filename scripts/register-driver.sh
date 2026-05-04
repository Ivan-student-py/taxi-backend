BASE_URL="http://127.0.0.1:8080"

echo "[INFO] Sending driver registration request..."

RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register/driver" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alex Driver",
    "email": "driver@test.com",
    "phone": "+79997654321",
    "licenseNumber": "LIC-123456",
    "status": "FREE",
    "password": "pass123"
  }')

echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"

TOKEN=$(echo "$RESPONSE" | jq -r '.token' 2>/dev/null)
if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo "$TOKEN" > scripts/.token_driver
    echo "[INFO] Token saved to scripts/.token_driver"
fi