ID="${1:-2}"
BASE_URL="http://127.0.0.1:8080"
TOKEN_FILE="scripts/.token_passenger"

if [ ! -f "$TOKEN_FILE" ]; then
    echo "[ERROR] Token not found. Please run login.sh first."
    exit 1
fi

TOKEN=$(cat "$TOKEN_FILE")

echo "[INFO] Fetching passenger #$ID..."

curl -s -X GET "$BASE_URL/users/passengers/$ID" \
  -H "Authorization: Bearer $TOKEN" | jq . 2>/dev/null