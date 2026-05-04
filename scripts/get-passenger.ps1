param(
    [int]$id = 1
)

$baseUrl = "http://127.0.0.1:8080"
$tokenFile = "scripts/.token_passenger"

if (-not (Test-Path $tokenFile)) {
    Write-Host "[ERROR] Token not found. Please run login.ps1 first." -ForegroundColor Red
    exit 1
}

$token = Get-Content -Path $tokenFile -Raw
$headers = @{ "Authorization" = "Bearer $token" }

Write-Host "[INFO] Fetching passenger #$id..." -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/users/passengers/$id" `
        -Method Get `
        -Headers $headers `
        -TimeoutSec 10

    Write-Host "[SUCCESS] Request completed!" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "[ERROR] Request failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 401) {
        Write-Host "[HINT] Token may be expired. Try running login.ps1 again." -ForegroundColor Yellow
    }
}