$baseUrl = "http://127.0.0.1:8080"

$body = @{
    name = "Ivan"
    email = "ivan2@test.com"
    phone = "+79991234567"
    password = "pass123"
} | ConvertTo-Json

Write-Host "[INFO] Sending passenger registration request..." -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/register/passenger" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -TimeoutSec 10

    Write-Host "[SUCCESS] Request completed!" -ForegroundColor Green
    Write-Host "[RESPONSE] Server response:" -ForegroundColor Yellow
    $response | ConvertTo-Json -Depth 10

    if ($response.token) {
        $response.token | Out-File -FilePath "scripts/.token_passenger" -Encoding utf8
        Write-Host "[INFO] Token saved to scripts/.token_passenger" -ForegroundColor Gray
    }
} catch {
    Write-Host "[ERROR] Request failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "[ERROR] HTTP Status: $($_.Exception.Response.StatusCode.Value__)" -ForegroundColor Red
    }
}