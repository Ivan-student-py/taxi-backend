param(
    [string]$email = "ivan@test.com",
    [string]$password = "pass123"
)

$baseUrl = "http://127.0.0.1:8080"

$body = @{
    email = $email
    password = $password
} | ConvertTo-Json

Write-Host "[INFO] Logging in: $email" -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -TimeoutSec 10

    Write-Host "[SUCCESS] Login completed!" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10

    if ($response.token) {
        $tokenFile = "scripts/.token_$($response.role.ToLower())"
        $response.token | Out-File -FilePath $tokenFile -Encoding utf8
        Write-Host "[INFO] Token saved to $tokenFile" -ForegroundColor Gray
    }
} catch {
    Write-Host "[ERROR] Login failed: $($_.Exception.Message)" -ForegroundColor Red
}