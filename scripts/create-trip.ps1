$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_passenger"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Passenger token not found. Run register-passenger.ps1 first." -ForegroundColor Red
    exit 1
}

$token = Get-Content $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

$body = @{
    passengerId = 1
    origin      = "Moscow City"
    destination = "Sheremetyevo"
    distance    = 35.0
} | ConvertTo-Json

Write-Host "[INFO] Creating trip (distance: 35km, tariff: 15)..." -ForegroundColor Cyan

try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/trips" `
        -Method Post `
        -Headers $headers `
        -ContentType "application/json" `
        -Body $body

    Write-Host "[SUCCESS] Trip created:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "[ERROR] Status: $($_.Exception.Response.StatusCode.Value__)" -ForegroundColor Red
    }
}