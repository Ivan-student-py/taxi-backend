$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_driver"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Driver token not found. Run register-driver.ps1 first." -ForegroundColor Red
    exit 1
}

$token = Get-Content $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

Write-Host "[INFO] Completing trip #1 (Status: COMPLETED)..." -ForegroundColor Cyan

try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/trips/1/status?status=COMPLETED" `
        -Method Patch `
        -Headers $headers

    Write-Host "[SUCCESS] Trip completed:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}