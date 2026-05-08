#!/bin/bash
BASE_URL="http://127.0.0.1:8080"
echo "[INFO] Checking Notification Worker metrics..."

get_metric() {
  local val
  val=$(curl -s "$BASE_URL/actuator/metrics/$1" | jq -r '.measurements[0].value' 2>/dev/null)
  if [ "$val" = "null" ] || [ -z "$val" ]; then echo "N/A"; else echo "$val"; fi
}

echo "[SUCCESS] Metrics:"
echo " Sent:    $(get_metric notifications.sent.total)"
echo " Failed:  $(get_metric notifications.failed.total)"
echo " Retries: $(get_metric notifications.retries.total)"

echo -e "\n[INFO] SQL for DB check (run in pgAdmin/psql):"
echo "SELECT id, message, status, attempts, created_at FROM notification_tasks ORDER BY id DESC LIMIT 5;"