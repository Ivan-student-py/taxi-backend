param(
    [int]$id = 1,
    [string]$status = "BUSY"
)

$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_driver"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Driver token not found. Please register/login as driver first." -ForegroundColor Red
    exit 1
}

$token = Get-Content -Path $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

Write-Host "[INFO] Updating driver #$id status to $status..." -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/users/drivers/$id/status?status=$status" `
        -Method Patch `
        -Headers $headers `
        -TimeoutSec 10

    Write-Host "[SUCCESS] Status updated!" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "[ERROR] Request failed: $($_.Exception.Message)" -ForegroundColor Red
}