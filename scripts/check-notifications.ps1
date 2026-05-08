# scripts/check-notifications.ps1
$baseUrl = "http://127.0.0.1:8080"

Write-Host "[INFO] Checking Notification Worker metrics..." -ForegroundColor Cyan

try {
    $sent   = (Invoke-RestMethod -Uri "$baseUrl/actuator/metrics/notifications.sent.total").measurements.value[0]
    $failed = (Invoke-RestMethod -Uri "$baseUrl/actuator/metrics/notifications.failed.total").measurements.value[0]
    $retry  = (Invoke-RestMethod -Uri "$baseUrl/actuator/metrics/notifications.retries.total").measurements.value[0]

    Write-Host "[SUCCESS] Metrics:" -ForegroundColor Green
    Write-Host " Sent:    $sent"
    Write-Host " Failed:  $failed"
    Write-Host " Retries: $retry"
} catch {
    Write-Host "[WARN] Could not fetch metrics. Ensure Actuator is exposed and app is running." -ForegroundColor Yellow
}

Write-Host "`n[INFO] SQL for DB check (run in pgAdmin):" -ForegroundColor Cyan
Write-Host "SELECT id, message, status, attempts, created_at FROM notification_tasks ORDER BY id DESC LIMIT 5;" -ForegroundColor Gray