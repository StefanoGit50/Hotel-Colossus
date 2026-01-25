#==============================================================================
# Script di Restart RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Riavvia tutti i servizi RMI
#==============================================================================

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Banner
Write-Host "===================================================" -ForegroundColor Blue
Write-Host "     Hotel Colossus - Restart Servizi RMI         " -ForegroundColor Blue
Write-Host "===================================================" -ForegroundColor Blue
Write-Host ""

# Stop
Write-Host "[1/2] Fermo servizi esistenti..." -ForegroundColor Blue
Write-Host ""

$stopScript = Join-Path $ScriptDir "stop-rmi.ps1"
if (Test-Path $stopScript) {
    & $stopScript
} else {
    Write-Host "ERRORE: stop-rmi.ps1 non trovato!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Attendo 3 secondi prima del riavvio..." -ForegroundColor Blue
Start-Sleep -Seconds 3
Write-Host ""

# Start
Write-Host "[2/2] Avvio servizi..." -ForegroundColor Blue
Write-Host ""

$startScript = Join-Path $ScriptDir "start-rmi.ps1"
if (Test-Path $startScript) {
    & $startScript
} else {
    Write-Host "ERRORE: start-rmi.ps1 non trovato!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Restart completato!" -ForegroundColor Blue
Write-Host ""