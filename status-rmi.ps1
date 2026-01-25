#==============================================================================
# Script di Status RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Mostra lo stato di tutti i servizi RMI
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

# Banner
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "     Hotel Colossus - Status Servizi RMI          " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

# --- FUNZIONI ---

# Ottieni status di un servizio
function Get-ServiceStatus {
    param(
        [string]$ServiceName,
        [string]$PidFile
    )

    $status = @{
        Name = $ServiceName
        Running = $false
        PID = $null
        CPU = $null
        Memory = $null
        Uptime = $null
    }

    if (Test-Path $PidFile) {
        $processId = Get-Content $PidFile
        try {
            $process = Get-Process -Id $processId -ErrorAction Stop
            $status.Running = $true
            $status.PID = $processId
            $status.CPU = [math]::Round($process.CPU, 2)
            $status.Memory = [math]::Round($process.WorkingSet64 / 1MB, 2)

            # Calcola uptime
            $startTime = $process.StartTime
            $uptime = (Get-Date) - $startTime
            $status.Uptime = "{0:hh\:mm\:ss}" -f $uptime
        } catch {
            # Processo non trovato
        }
    }

    return $status
}

# Verifica porta RMI
function Test-RMIPort {
    Write-Info "Porta RMI: $RMI_PORT"
    Write-Host "  Porta $RMI_PORT`: " -NoNewline

    try {
        $connection = Get-NetTCPConnection -LocalPort $RMI_PORT -State Listen -ErrorAction Stop
        Write-Host "ATTIVA" -ForegroundColor Green

        $process = Get-Process -Id $connection.OwningProcess -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "                  Processo: $($process.Name) (PID: $($process.Id))"
        }
    } catch {
        Write-Host "LIBERA" -ForegroundColor Yellow
    }
}

# Testa connessione RMI
function Test-RMIConnection {
    Write-Host "RMI Registry:     " -NoNewline

    try {
        $connection = Test-NetConnection -ComputerName localhost -Port $RMI_PORT -WarningAction SilentlyContinue
        if ($connection.TcpTestSucceeded) {
            Write-Host "Reachable" -ForegroundColor Green
        } else {
            Write-Host "Unreachable" -ForegroundColor Red
        }
    } catch {
        Write-Host "Unreachable" -ForegroundColor Red
    }
}

# ============================================================================
# ESECUZIONE PRINCIPALE
# ============================================================================

# System Information
Write-Host "System Information:" -ForegroundColor Blue
Write-Host "  Hostname:   $env:COMPUTERNAME"
try {
    $javaVersion = (java -version 2>&1 | Select-Object -First 1)
    Write-Host "  Java:       $javaVersion"
} catch {
    Write-Host "  Java:       Non trovato!" -ForegroundColor Red
}
Write-Host "  Project:    $PROJECT_ROOT"
Write-Host ""

# Network Status
Write-Host "Network Status:" -ForegroundColor Blue
Test-RMIPort
Test-RMIConnection
Write-Host ""

# Service Status
Write-Host "Service Status:" -ForegroundColor Blue

$services = @(
    (Get-ServiceStatus "RMI Registry" (Join-Path $PID_DIR "rmiregistry.pid")),
    (Get-ServiceStatus "FrontDesk" (Join-Path $PID_DIR "FrontDesk.pid")),
    (Get-ServiceStatus "Governante" (Join-Path $PID_DIR "Governante.pid")),
    (Get-ServiceStatus "Manager" (Join-Path $PID_DIR "Manager.pid"))
)

# Tabella
Write-Host ""
Write-Host "Servizio        Status      PID      CPU(s)    Memoria(MB)  Uptime"
Write-Host "-----------------------------------------------------------------------"

foreach ($service in $services) {
    $name = $service.Name.PadRight(15)

    if ($service.Running) {
        $status = "Running".PadRight(10)
        $processId = $service.PID.ToString().PadRight(8)
        $cpu = $service.CPU.ToString().PadRight(9)
        $mem = $service.Memory.ToString().PadRight(12)
        $uptime = $service.Uptime

        Write-Host $name -NoNewline
        Write-Host " " -NoNewline
        Write-Host $status -ForegroundColor Green -NoNewline
        Write-Host " $processId $cpu $mem $uptime"
    } else {
        $status = "Stopped".PadRight(10)
        Write-Host $name -NoNewline
        Write-Host " " -NoNewline
        Write-Host $status -ForegroundColor Red -NoNewline
        Write-Host " -        -         -            -"
    }
}

Write-Host ""

# Conta servizi attivi
$runningCount = ($services | Where-Object { $_.Running }).Count
$totalCount = $services.Count

Write-Host "Summary: " -ForegroundColor Blue -NoNewline
Write-Host "$runningCount/$totalCount servizi attivi"
Write-Host ""

# Log files info
Write-Host "Log Files:" -ForegroundColor Blue
if (Test-Path $LOGS_DIR) {
    $logFiles = Get-ChildItem -Path $LOGS_DIR -Filter "*.log" -ErrorAction SilentlyContinue
    if ($logFiles) {
        foreach ($log in $logFiles) {
            $size = [math]::Round($log.Length / 1KB, 2)
            Write-Host "  $($log.Name.PadRight(30)) ${size}KB"
        }
    } else {
        Write-Host "  Nessun file log trovato"
    }
} else {
    Write-Host "  Directory log non trovata"
}

Write-Host ""

# Comandi disponibili
Write-Host "Comandi disponibili:" -ForegroundColor Yellow
Write-Host "  .\start-rmi.ps1    - Avvia servizi"
Write-Host "  .\stop-rmi.ps1     - Ferma servizi"
Write-Host "  .\restart-rmi.ps1  - Riavvia servizi"
Write-Host "  .\logs-rmi.ps1     - Visualizza log"
Write-Host ""