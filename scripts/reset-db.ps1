$pgUser = "taxi_user"
$pgDb   = "taxi_db"
$sqlFile = "scripts/reset-db.sql"

Write-Host "[INFO] Cleaning database..." -ForegroundColor Cyan
try {
    psql -U $pgUser -d $pgDb -f $sqlFile 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[SUCCESS] Database cleaned. IDs reset." -ForegroundColor Green
    } else {
        throw "psql execution failed"
    }
} catch {
    Write-Host "[WARN] psql not found or connection failed." -ForegroundColor Yellow
    Write-Host "[INFO] Please run scripts/reset-db.sql manually in pgAdmin/DBeaver." -ForegroundColor Gray
}