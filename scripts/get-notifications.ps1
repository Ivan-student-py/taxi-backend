param(
    [string]$trip_id = ""
)

$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_passenger"

$headers = @{}
if (Test-Path $tokenFile) {
    $token = Get-Content $tokenFile -Raw
    $headers["Authorization"] = "Bearer $token"
}

$uri = if ($trip_id) { "$baseUrl/notifications?trip_id=$trip_id" } else { "$baseUrl/notifications" }

Write-Host "[INFO] Fetching notifications..." -ForegroundColor Cyan

try {
    $resp = Invoke-RestMethod -Uri $uri -Method Get -Headers $headers
    Write-Host "[SUCCESS] Notifications:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}