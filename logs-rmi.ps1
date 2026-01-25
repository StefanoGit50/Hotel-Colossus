#==============================================================================
# Script Log Viewer per Hotel Colossus (Windows PowerShell)
# Descrizione: Visualizza i log dei servizi RMI in tempo reale
#==============================================================================

param(
    [string]$Service = "all",
    [int]$Lines = 50,
    [switch]$Watch,
    [switch]$Errors
)

# Carica environment
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
. "$ScriptDir\env.ps1"

# --- FUNZIONI ---
function Write-Info {
    param($Message)
    Write-Host "[INFO] " -ForegroundColor Blue -NoNewline
    Write-Host $Message
}

function Show-Log {
    param(
        [string]$ServiceName,
        [string]$LogFile,
        [int]$TailLines = 50,
        [switch]$ErrorLog
    )

    $logType = if ($ErrorLog) { "Error Log" } else { "Log" }

    if (-not (Test-Path $LogFile)) {
        Write-Host "$logType di $ServiceName non trovato: $LogFile" -ForegroundColor Yellow
        return
    }

    Write-Host ""
    Write-Host "===================================================" -ForegroundColor Cyan
    Write-Host "  $logType`: $ServiceName" -ForegroundColor Cyan
    Write-Host "===================================================" -ForegroundColor Cyan
    Write-Host ""

    $content = Get-Content $LogFile -Tail $TailLines
    if ($content) {
        $content | ForEach-Object { Write-Host $_ }
    } else {
        Write-Host "(vuoto)" -ForegroundColor Gray
    }
}

function Watch-Logs {
    param(
        [string[]]$LogFiles
    )

    Write-Host "Monitoraggio log in tempo reale..." -ForegroundColor Green
    Write-Host "Premi Ctrl+C per uscire" -ForegroundColor Yellow
    Write-Host ""

    # Filtra solo i log esistenti
    $existingLogs = $LogFiles | Where-Object { Test-Path $_ }

    if ($existingLogs.Count -eq 0) {
        Write-Host "Nessun file log trovato!" -ForegroundColor Red
        return
    }

    # Usa Get-Content -Wait per seguire i log
    Get-Content $existingLogs -Wait -Tail 10
}

# Banner
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "     Hotel Colossus - Log Viewer                  " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

# Determina quali log mostrare
$logSuffix = if ($Errors) { "-error.log" } else { ".log" }

if ($Watch) {
    # Modalita' watch: segui tutti i log in tempo reale
    $logFiles = @(
        (Join-Path $LOGS_DIR "rmiregistry$logSuffix"),
        (Join-Path $LOGS_DIR "FrontDesk$logSuffix"),
        (Join-Path $LOGS_DIR "Governante$logSuffix"),
        (Join-Path $LOGS_DIR "Manager$logSuffix")
    )
    Watch-Logs $logFiles
} else {
    # Modalita' normale: mostra ultimi N righe
    switch ($Service.ToLower()) {
        "registry" {
            Show-Log "RMI Registry" (Join-Path $LOGS_DIR "rmiregistry$logSuffix") $Lines -ErrorLog:$Errors
        }
        "frontdesk" {
            Show-Log "FrontDesk" (Join-Path $LOGS_DIR "FrontDesk$logSuffix") $Lines -ErrorLog:$Errors
        }
        "governante" {
            Show-Log "Governante" (Join-Path $LOGS_DIR "Governante$logSuffix") $Lines -ErrorLog:$Errors
        }
        "manager" {
            Show-Log "Manager" (Join-Path $LOGS_DIR "Manager$logSuffix") $Lines -ErrorLog:$Errors
        }
        default {
            # Mostra tutti i log
            Show-Log "RMI Registry" (Join-Path $LOGS_DIR "rmiregistry$logSuffix") $Lines -ErrorLog:$Errors
            Show-Log "FrontDesk" (Join-Path $LOGS_DIR "FrontDesk$logSuffix") $Lines -ErrorLog:$Errors
            Show-Log "Governante" (Join-Path $LOGS_DIR "Governante$logSuffix") $Lines -ErrorLog:$Errors
            Show-Log "Manager" (Join-Path $LOGS_DIR "Manager$logSuffix") $Lines -ErrorLog:$Errors

            Write-Host ""
            Write-Host "Comandi disponibili:" -ForegroundColor Yellow
            Write-Host "  .\logs-rmi.ps1 -Service frontdesk     - Solo log FrontDesk"
            Write-Host "  .\logs-rmi.ps1 -Watch                 - Segui log in tempo reale"
            Write-Host "  .\logs-rmi.ps1 -Errors                - Mostra solo log di errore"
            Write-Host "  .\logs-rmi.ps1 -Lines 100             - Mostra 100 righe"
            Write-Host "  .\logs-rmi.ps1 -Service frontdesk -Errors -Lines 20"
            Write-Host ""
        }
    }
}