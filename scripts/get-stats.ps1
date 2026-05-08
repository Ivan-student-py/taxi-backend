$baseUrl = "http://127.0.0.1:8080"
Write-Host "[INFO] Fetching trip statistics..." -ForegroundColor Cyan
try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/stats/trips" -Method Get
    Write-Host "[SUCCESS] Stats:" -ForegroundColor Green
    $resp | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}