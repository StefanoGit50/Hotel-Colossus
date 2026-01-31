#==============================================================================
# Script di Stop RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Ferma tutti i servizi RMI in esecuzione
#==============================================================================

$ErrorActionPreference = "Continue"

# Carica environment
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
. "$ScriptDir\env.ps1"

# --- FUNZIONI DI OUTPUT ---
function Write-Info {
    param($Message)
    Write-Host "[INFO] " -ForegroundColor Blue -NoNewline
    Write-Host $Message
}

function Write-Success {
    param($Message)
    Write-Host "[OK] " -ForegroundColor Green -NoNewline
    Write-Host $Message
}

function Write-Warning {
    param($Message)
    Write-Host "[WARN] " -ForegroundColor Yellow -NoNewline
    Write-Host $Message
}

function Write-ErrorMsg {
    param($Message)
    Write-Host "[ERROR] " -ForegroundColor Red -NoNewline
    Write-Host $Message
}

# Banner
Write-Host "===================================================" -ForegroundColor Red
Write-Host "     Hotel Colossus - Stop Servizi RMI            " -ForegroundColor Red
Write-Host "===================================================" -ForegroundColor Red
Write-Host ""

# --- FUNZIONI ---

# Ferma un servizio tramite PID file
function Stop-ServiceByPid {
    param(
        [string]$ServiceName,
        [string]$PidFile
    )

    if (-not (Test-Path $PidFile)) {
        Write-Info "$ServiceName non e' in esecuzione (nessun PID file)"
        return $true
    }

    $processId = Get-Content $PidFile

    try {
        $process = Get-Process -Id $processId -ErrorAction Stop
        Write-Info "Fermo $ServiceName (PID: $processId)..."

        # Tentativo graceful shutdown
        $process.CloseMainWindow() | Out-Null
        Start-Sleep -Seconds 2

        # Se ancora attivo, forza terminazione
        if (-not $process.HasExited) {
            Write-Warning "$ServiceName non risponde, forzatura terminazione..."
            Stop-Process -Id $processId -Force
            Start-Sleep -Seconds 1
        }

        # Verifica terminazione
        try {
            Get-Process -Id $processId -ErrorAction Stop | Out-Null
            Write-ErrorMsg "Impossibile fermare $ServiceName!"
            return $false
        } catch {
            Write-Success "$ServiceName fermato"
            Remove-Item $PidFile -Force
            return $true
        }
    } catch {
        Write-Warning "$ServiceName non e' in esecuzione (PID: $processId non trovato)"
        Remove-Item $PidFile -Force
        return $true
    }
}

# Ferma tutti i processi RMI rimasti
function Stop-AllRMIProcesses {
    Write-Info "Ricerca processi RMI rimasti..."

    $rmiProcesses = Get-Process -Name java -ErrorAction SilentlyContinue |
            Where-Object {
                $_.CommandLine -match "FrontDesk|Governante|Manager|rmiregistry"
            }

    if ($rmiProcesses) {
        Write-Warning "Processi RMI trovati: $($rmiProcesses.Count)"
        $response = Read-Host "Fermare tutti? [y/N]"

        if ($response -eq 'y' -or $response -eq 'Y') {
            foreach ($proc in $rmiProcesses) {
                Write-Info "Fermo PID $($proc.Id)..."
                Stop-Process -Id $proc.Id -Force
            }
            Write-Success "Tutti i processi RMI fermati"
        }
    } else {
        Write-Info "Nessun processo RMI trovato"
    }
}

# ============================================================================
# ESECUZIONE PRINCIPALE
# ============================================================================

# Ferma servizi in ordine inverso rispetto all'avvio

# 1. Ferma Manager
Stop-ServiceByPid "Manager" (Join-Path $PID_DIR "Manager.pid")

# 2. Ferma Governante
Stop-ServiceByPid "Governante" (Join-Path $PID_DIR "Governante.pid")

# 3. Ferma FrontDesk
Stop-ServiceByPid "FrontDesk" (Join-Path $PID_DIR "FrontDesk.pid")

# 4. Ferma RMI Registry
Stop-ServiceByPid "RMI Registry" (Join-Path $PID_DIR "rmiregistry.pid")

# Verifica che la porta sia liberata
Start-Sleep -Seconds 2

try {
    $connection = Get-NetTCPConnection -LocalPort $RMI_PORT -State Listen -ErrorAction Stop
    Write-ErrorMsg "Porta $RMI_PORT ancora occupata!"
    $process = Get-Process -Id $connection.OwningProcess -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   Processo: $($process.Name) (PID: $($process.Id))"
    }
    Stop-AllRMIProcesses
} catch {
    Write-Success "Porta $RMI_PORT liberata"
}

# Cleanup PID files
Write-Info "Cleanup file PID..."
Get-ChildItem -Path $PID_DIR -Filter "*.pid" -ErrorAction SilentlyContinue | Remove-Item -Force
Write-Success "Cleanup completato"

Write-Host ""
Write-Host "===================================================" -ForegroundColor Green
Write-Host "     Tutti i servizi RMI sono stati fermati!      " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green
Write-Host ""

Write-Info "Per riavviare i servizi:"
Write-Host "  .\start-rmi.ps1" -ForegroundColor Yellow
Write-Host ""