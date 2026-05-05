$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_passenger"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Passenger token not found. Run register-passenger.ps1 first." -ForegroundColor Red
    exit 1
}

$token = Get-Content $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

$body = @{ rating = 5 } | ConvertTo-Json

Write-Host "[INFO] Rating trip #1 with 5 stars..." -ForegroundColor Cyan

try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/trips/1/rate" `
        -Method Patch `
        -Headers $headers `
        -ContentType "application/json" `
        -Body $body

    Write-Host "[SUCCESS] Rating added:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}