$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_driver"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Driver token not found. Run register-driver.ps1 first." -ForegroundColor Red
    exit 1
}

$token = Get-Content $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

Write-Host "[INFO] Fetching trips for driver #1..." -ForegroundColor Cyan
try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/trips/driver/1" -Method Get -Headers $headers
    Write-Host "[SUCCESS] Driver trips:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}