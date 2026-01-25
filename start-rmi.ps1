#==============================================================================
# Script di Avvio RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Avvia RMI Registry e tutti i server RMI
#==============================================================================

# Blocca l'esecuzione in caso di errore
$ErrorActionPreference = "Stop"

# --- CARICA ENVIRONMENT ---
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
Write-Host "===================================================" -ForegroundColor Blue
Write-Host "     Hotel Colossus - Avvio Servizi RMI           " -ForegroundColor Blue
Write-Host "===================================================" -ForegroundColor Blue
Write-Host ""

# --- FUNZIONI ---

# Verifica se un processo e' attivo
function Test-ServiceRunning {
    param($PidFile)

    if (Test-Path $PidFile) {
        $processId = Get-Content $PidFile
        try {
            $process = Get-Process -Id $processId -ErrorAction Stop
            return $true
        } catch {
            Remove-Item $PidFile -Force
            return $false
        }
    }
    return $false
}

# Attende che un servizio sia pronto
function Wait-ForService {
    param(
        [string]$ServiceName,
        [int]$Port,
        [int]$MaxWait = 10
    )

    Write-Info "Attendo che $ServiceName sia pronto..."

    for ($i = 0; $i -lt $MaxWait; $i++) {
        try {
            $connection = Test-NetConnection -ComputerName localhost -Port $Port -WarningAction SilentlyContinue
            if ($connection.TcpTestSucceeded) {
                Write-Success "$ServiceName e' pronto"
                return $true
            }
        } catch {
            # Continua ad attendere
        }

        Start-Sleep -Seconds 1
        Write-Host "." -NoNewline
    }

    Write-Host ""
    Write-ErrorMsg "$ServiceName non risponde dopo ${MaxWait}s"
    return $false
}

# Avvia un servizio Java
function Start-JavaService {
    param(
        [string]$ServiceName,
        [string]$MainClass
    )

    $pidFile = Join-Path $PID_DIR "$ServiceName.pid"
    $logFile = Join-Path $LOGS_DIR "$ServiceName.log"

    Write-Info "Avvio $ServiceName..."

    # Verifica se gia' in esecuzione
    if (Test-ServiceRunning $pidFile) {
        $processId = Get-Content $pidFile
        Write-Warning "$ServiceName e' gia' in esecuzione (PID: $processId)"
        return $true
    }

    # Prepara il comando Java
    $javaArgs = @(
        $JAVA_OPTS.Split(' ')
        '-cp'
        $CLASSPATH
        $MainClass
    )

    # Avvia il processo
    try {
        $errorLogFile = Join-Path $LOGS_DIR "$ServiceName-error.log"
        $process = Start-Process -FilePath "java" `
                                 -ArgumentList $javaArgs `
                                 -NoNewWindow `
                                 -PassThru `
                                 -RedirectStandardOutput $logFile `
                                 -RedirectStandardError $errorLogFile

        # Salva il PID
        $process.Id | Out-File -FilePath $pidFile -Encoding ASCII

        # Attendi che il processo sia avviato
        Start-Sleep -Seconds 2

        # Verifica che sia ancora in esecuzione
        if (Get-Process -Id $process.Id -ErrorAction SilentlyContinue) {
            Write-Success "$ServiceName avviato (PID: $($process.Id))"
            Write-Info "  Log: $logFile"
            return $true
        } else {
            Write-ErrorMsg "$ServiceName non e' partito! Controlla il log:"
            Get-Content $logFile -Tail 20
            Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
            return $false
        }
    } catch {
        Write-ErrorMsg "Errore avvio di $ServiceName"
        Write-Host "Dettagli: $_" -ForegroundColor Red
        Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
        return $false
    }
}

# ============================================================================
# ESECUZIONE PRINCIPALE
# ============================================================================

# 1. Verifica che nessun servizio sia gia' attivo
Write-Info "Verifica servizi esistenti..."

try {
    $connection = Get-NetTCPConnection -LocalPort $RMI_PORT -State Listen -ErrorAction Stop
    Write-Warning "Servizi RMI gia' attivi sulla porta $RMI_PORT"
    Write-Host "   Eseguire " -NoNewline
    Write-Host ".\stop-rmi.ps1" -ForegroundColor Yellow -NoNewline
    Write-Host " prima di riavviare"
    exit 1
} catch {
    # Porta libera, ok
}

# 2. Avvia RMI Registry
Write-Info "Avvio RMI Registry sulla porta $RMI_PORT..."

$rmiRegistryPid = Join-Path $PID_DIR "rmiregistry.pid"

if (Test-ServiceRunning $rmiRegistryPid) {
    Write-Warning "RMI Registry gia' in esecuzione"
} else {
    # Avvia rmiregistry
    $rmiLogFile = Join-Path $LOGS_DIR "rmiregistry.log"
    $rmiErrorLogFile = Join-Path $LOGS_DIR "rmiregistry-error.log"

    try {
        $rmiProcess = Start-Process -FilePath "rmiregistry" `
                                     -ArgumentList $RMI_PORT `
                                     -NoNewWindow `
                                     -PassThru `
                                     -RedirectStandardOutput $rmiLogFile `
                                     -RedirectStandardError $rmiErrorLogFile

        $rmiProcess.Id | Out-File -FilePath $rmiRegistryPid -Encoding ASCII

        # Attendi che sia pronto
        if (Wait-ForService "RMI Registry" $RMI_PORT) {
            Write-Success "RMI Registry avviato (PID: $($rmiProcess.Id))"
        } else {
            Write-ErrorMsg "RMI Registry non risponde"
            exit 1
        }
    } catch {
        Write-ErrorMsg "Impossibile avviare RMI Registry"
        Write-Host "Dettagli: $_" -ForegroundColor Red
        exit 1
    }
}

Start-Sleep -Seconds 2

# 3. Avvia FrontDesk Server
Start-JavaService "FrontDesk" "it.unisa.Server.gestionePrenotazioni.FrontDesk"
Start-Sleep -Seconds 3

# 4. Avvia Governante Server
Start-JavaService "Governante" "it.unisa.Server.gestioneCamere.Governante"
Start-Sleep -Seconds 3

# 5. Avvia Manager Server
Start-JavaService "Manager" "it.unisa.Server.gestioneImpiegati.Manager"

# Summary
Write-Host ""
Write-Host "===================================================" -ForegroundColor Green
Write-Host "     Tutti i servizi RMI sono stati avviati!      " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green
Write-Host ""
Write-Info "Servizi attivi:"

if (Test-ServiceRunning $rmiRegistryPid) {
    $processId = Get-Content $rmiRegistryPid
    Write-Host "  [*] RMI Registry    (PID: $processId)" -ForegroundColor Green
}

$frontDeskPid = Join-Path $PID_DIR "FrontDesk.pid"
if (Test-ServiceRunning $frontDeskPid) {
    $processId = Get-Content $frontDeskPid
    Write-Host "  [*] FrontDesk       (PID: $processId)" -ForegroundColor Green
}

$governantePid = Join-Path $PID_DIR "Governante.pid"
if (Test-ServiceRunning $governantePid) {
    $processId = Get-Content $governantePid
    Write-Host "  [*] Governante      (PID: $processId)" -ForegroundColor Green
}

$managerPid = Join-Path $PID_DIR "Manager.pid"
if (Test-ServiceRunning $managerPid) {
    $processId = Get-Content $managerPid
    Write-Host "  [*] Manager         (PID: $processId)" -ForegroundColor Green
}

Write-Host ""
Write-Info "Comandi utili:"
Write-Host "  .\status-rmi.ps1   - Verifica stato servizi" -ForegroundColor Yellow
Write-Host "  .\logs-rmi.ps1     - Visualizza log" -ForegroundColor Yellow
Write-Host "  .\stop-rmi.ps1     - Ferma tutti i servizi" -ForegroundColor Yellow
Write-Host ""