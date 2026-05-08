$baseUrl = "http://127.0.0.1:8080"
$email = "driver@test.com"
$pass  = "pass123"

Write-Host "[INFO] Logging in driver: $email..." -ForegroundColor Cyan

$body = @{ email = $email; password = $pass } | ConvertTo-Json

try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body

    Write-Host "[SUCCESS] Auth response:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 3

    if ($resp.token) {
        $resp.token | Out-File -FilePath "scripts/.token_driver" -NoNewline
        Write-Host "[INFO] Driver token saved to scripts/.token_driver" -ForegroundColor Green
    }
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "[ERROR] Status: $($_.Exception.Response.StatusCode.Value__)" -ForegroundColor Red
    }
}