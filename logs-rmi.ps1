#==============================================================================
# Script di Visualizzazione Log RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Visualizza i log dei servizi RMI con menu interattivo
# Versione: 2.0 - Menu Interattivo
#==============================================================================

# Carica environment
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
. "$ScriptDir\env.ps1"

# ============================================================================
# FUNZIONI DI UTILITÀ
# ============================================================================

function Colorize-Line {
    param([string]$line)

    if ($line -match "error|exception|failed") {
        Write-Host $line -ForegroundColor Red
    }
    elseif ($line -match "warning|warn") {
        Write-Host $line -ForegroundColor Yellow
    }
    elseif ($line -match "info") {
        Write-Host $line -ForegroundColor Green
    }
    elseif ($line -match "debug") {
        Write-Host $line -ForegroundColor Gray
    }
    elseif ($line -match "success|completed") {
        Write-Host $line -ForegroundColor Cyan
    }
    else {
        Write-Host $line
    }
}


function Show-Header {
    Clear-Host
    Write-Host "====================================================" -ForegroundColor Cyan
    Write-Host "     Hotel Colossus - Visualizzazione Log          " -ForegroundColor Cyan
    Write-Host "====================================================" -ForegroundColor Cyan
    Write-Host ""
}

function Show-Menu {
    Show-Header
    Write-Host "Seleziona il servizio di cui visualizzare i log:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  " -NoNewline
    Write-Host "1)" -ForegroundColor Green -NoNewline
    Write-Host " RMI Registry"

    Write-Host "  " -NoNewline
    Write-Host "2)" -ForegroundColor Green -NoNewline
    Write-Host " FrontDesk Server"

    Write-Host "  " -NoNewline
    Write-Host "3)" -ForegroundColor Green -NoNewline
    Write-Host " Governante Server"

    Write-Host "  " -NoNewline
    Write-Host "4)" -ForegroundColor Green -NoNewline
    Write-Host " Manager Server"

    Write-Host ""
    Write-Host "Visualizzazione log Statici" -ForegroundColor Cyan
    Write-Host "  " -NoNewline
    Write-Host "5)" -ForegroundColor Green -NoNewline
    Write-Host " FrontDesk.log"
    Write-Host "  " -NoNewline
    Write-Host "6)" -ForegroundColor Green -NoNewline
    Write-Host " Governante.log"
    Write-Host "  " -NoNewline
    Write-Host "7)" -ForegroundColor Green -NoNewline
    Write-Host " Manager.log"

    Write-Host ""
    Write-Host "Visualizzazione multipla:" -ForegroundColor Cyan
    Write-Host "  " -NoNewline
    Write-Host "8)" -ForegroundColor Green -NoNewline
    Write-Host " Tutti i servizi (interleaved)"

    Write-Host ""
    Write-Host "Filtri e ricerca:" -ForegroundColor Cyan
    Write-Host "  " -NoNewline
    Write-Host "9)" -ForegroundColor Yellow -NoNewline
    Write-Host " Cerca nel log (grep)"

    Write-Host "  " -NoNewline
    Write-Host "10)" -ForegroundColor Yellow -NoNewline
    Write-Host " Solo ERRORI"

    Write-Host "  " -NoNewline
    Write-Host "11)" -ForegroundColor Yellow -NoNewline
    Write-Host " Solo WARNING"

    Write-Host "  " -NoNewline
    Write-Host "12)" -ForegroundColor Yellow -NoNewline
    Write-Host " Solo INFO"

    Write-Host ""
    Write-Host "Utilita':" -ForegroundColor Cyan
    Write-Host "  " -NoNewline
    Write-Host "13)" -ForegroundColor Magenta -NoNewline
    Write-Host " Statistiche log"

    Write-Host "  " -NoNewline
    Write-Host "14)" -ForegroundColor Magenta -NoNewline
    Write-Host " Pulisci log"

    Write-Host "  " -NoNewline
    Write-Host "15)" -ForegroundColor Magenta -NoNewline
    Write-Host " Esporta log"

    Write-Host ""
    Write-Host "  " -NoNewline
    Write-Host "0)" -ForegroundColor Red -NoNewline
    Write-Host " Esci"

    Write-Host ""
    Write-Host "Scelta: " -ForegroundColor White -NoNewline
}

function Test-LogExists {
    param([string]$LogFile)

    if (-not (Test-Path $LogFile)) {
        Write-Host "X Log non trovato: $LogFile" -ForegroundColor Red
        Write-Host "  Il servizio potrebbe non essere stato ancora avviato." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Premi INVIO per continuare..." -ForegroundColor White -NoNewline
        Read-Host
        return $false
    }
    return $true
}

function Show-FileInfo {
    param(
        [string]$LogFile,
        [string]$ServiceName
    )

    if (Test-Path $LogFile) {
        $file = Get-Item $LogFile
        $size = [math]::Round($file.Length / 1KB, 2)
        $lines = (Get-Content $LogFile | Measure-Object -Line).Lines
        $modified = $file.LastWriteTime.ToString("yyyy-MM-dd HH:mm:ss")

        Write-Host "+----------------------------------------------------+" -ForegroundColor Gray
        Write-Host "| " -ForegroundColor Gray -NoNewline
        Write-Host "Servizio: " -ForegroundColor Cyan -NoNewline
        Write-Host "$ServiceName"

        Write-Host "| " -ForegroundColor Gray -NoNewline
        Write-Host "File:     " -ForegroundColor Cyan -NoNewline
        Write-Host "$LogFile"

        Write-Host "| " -ForegroundColor Gray -NoNewline
        Write-Host "Dimensione: " -ForegroundColor Cyan -NoNewline
        Write-Host "${size}KB ($lines righe)"

        Write-Host "| " -ForegroundColor Gray -NoNewline
        Write-Host "Modificato: " -ForegroundColor Cyan -NoNewline
        Write-Host "$modified"

        Write-Host "+----------------------------------------------------+" -ForegroundColor Gray
        Write-Host ""
    }
}

# ============================================================================
# FUNZIONI DI VISUALIZZAZIONE
# ============================================================================

function Show-LogSingle
{
    param([string]$ServiceName)

    $logFile = Join-Path $LOGS_DIR "$ServiceName.log"

    Show-Header
    Show-FileInfo $logFile $ServiceName

    if (-not (Test-LogExists $logFile))
    {
        return
    }

    Write-Host "> Visualizzazione log in tempo reale" -ForegroundColor Green
    Write-Host "  (Premi Ctrl+C per uscire)" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "====================================================" -ForegroundColor Gray
    Write-Host ""

    # Tail con colorazione
    Get-Content $logFile -Wait -Tail 20 | ForEach-Object {
        $line = $_

        if ($line -match "error|exception|failed")
        {
            Write-Host $line -ForegroundColor Red
        }
        elseif ($line -match "warning|warn")
        {
            Write-Host $line -ForegroundColor Yellow
        }
        elseif ($line -match "info")
        {
            Write-Host $line -ForegroundColor Green
        }
        elseif ($line -match "debug")
        {
            Write-Host $line -ForegroundColor Gray
        }
        elseif ($line -match "success|completed")
        {
            Write-Host $line -ForegroundColor Cyan
        }
        else
        {
            Write-Host $line
        }
    }
}

function Show-LogFile {
    param([string]$ServiceName)

    $logs = Get-ChildItem $LOGS_DIR -Filter "$ServiceName*.log"

    if ($logs.Count -eq 0) {
        Write-Host "Nessun log trovato per $ServiceName" -ForegroundColor Red
        Start-Sleep -Seconds 2
        return
    }

    Show-Header
    Write-Host "Log disponibili per ${ServiceName}:" -ForegroundColor Cyan
    for ($i=0; $i -lt $logs.Count; $i++) {
        $name = $logs[$i].Name
        if ($name -match "error") { Write-Host " [$($i+1)] $name" -ForegroundColor Red }
        elseif ($name -match "warn") { Write-Host " [$($i+1)] $name" -ForegroundColor Yellow }
        else { Write-Host " [$($i+1)] $name" }
    }

    $choice = Read-Host "Seleziona log"
    $index = [int]$choice - 1
    if ($index -lt 0 -or $index -ge $logs.Count) {
        Write-Host "Scelta non valida" -ForegroundColor Red
        Start-Sleep -Seconds 1
        return
    }

    $filePath = $logs[$index].FullName
    Show-Header
    Write-Host "Visualizzazione log statico: $filePath" -ForegroundColor Cyan
    Write-Host "====================================================" -ForegroundColor Gray
    Write-Host ""

    Get-Content $filePath | ForEach-Object {
        $line = $_
        if ($line -match "error|exception|failed") {
            Write-Host $line -ForegroundColor Red
        }
        elseif ($line -match "warning|warn") {
            Write-Host $line -ForegroundColor Yellow
        }
        elseif ($line -match "info") {
            Write-Host $line -ForegroundColor Green
        }
        elseif ($line -match "debug") {
            Write-Host $line -ForegroundColor Gray
        }
        elseif ($line -match "success|completed") {
            Write-Host $line -ForegroundColor Cyan
        }
        else {
            Write-Host $line
        }
    }

    Write-Host ""
    Read-Host "Premi INVIO per tornare al menu..."
}




function Show-AllLogsInterleaved {
    Show-Header

    Write-Host "> Visualizzazione tutti i log (interleaved)" -ForegroundColor Green
    Write-Host "  (Premi Ctrl+C per uscire)" -ForegroundColor Yellow
    Write-Host ""

    $logFiles = @(
        (Join-Path $LOGS_DIR "rmiregistry.log"),
        (Join-Path $LOGS_DIR "FrontDesk.log"),
        (Join-Path $LOGS_DIR "Governante.log"),
        (Join-Path $LOGS_DIR "Manager.log")
    ) | Where-Object { Test-Path $_ }

    if ($logFiles.Count -eq 0) {
        Write-Host "Nessun file log trovato!" -ForegroundColor Red
        Write-Host ""
        Write-Host "Premi INVIO per continuare..." -NoNewline
        Read-Host
        return
    }

    Get-Content $logFiles -Wait -Tail 10 | ForEach-Object {
        $line = $_

        # Identifica sorgente
        if ($line -match "rmiregistry") {
            Write-Host "[REGISTRY] " -ForegroundColor Blue -NoNewline
        }
        elseif ($line -match "FrontDesk") {
            Write-Host "[FRONTDESK] " -ForegroundColor Green -NoNewline
        }
        elseif ($line -match "Governante") {
            Write-Host "[GOVERNANTE] " -ForegroundColor Magenta -NoNewline
        }
        elseif ($line -match "Manager") {
            Write-Host "[MANAGER] " -ForegroundColor Cyan -NoNewline
        }

        # Colora per livello
        if ($line -match "error") {
            Write-Host $line -ForegroundColor Red
        }
        elseif ($line -match "warn") {
            Write-Host $line -ForegroundColor Yellow
        }
        else {
            Write-Host $line
        }
    }
}

# ============================================================================
# FUNZIONI DI FILTRO E RICERCA
# ============================================================================

function Search-InLogs {
    Show-Header

    Write-Host "Ricerca nei log" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Inserisci termine di ricerca: " -ForegroundColor White -NoNewline
    $searchTerm = Read-Host

    if ([string]::IsNullOrWhiteSpace($searchTerm)) {
        Write-Host "X Termine di ricerca vuoto" -ForegroundColor Red
        Start-Sleep -Seconds 2
        return
    }

    Write-Host ""
    Write-Host "> Ricerca di '$searchTerm' in tutti i log..." -ForegroundColor Green
    Write-Host ""

    $found = $false

    Get-ChildItem $LOGS_DIR -Filter "*.log" | ForEach-Object {
        $matches = Select-String -Path $_.FullName -Pattern $searchTerm -AllMatches

        if ($matches) {
            $found = $true
            Write-Host "=== $($_.Name) ===" -ForegroundColor Cyan

            foreach ($match in $matches) {
                Write-Host $match.Line -ForegroundColor Yellow
            }
            Write-Host ""
        }
    }

    if (-not $found) {
        Write-Host "Nessun risultato trovato per '$searchTerm'" -ForegroundColor Yellow
    }

    Write-Host ""
    Write-Host "Premi INVIO per continuare..." -ForegroundColor White -NoNewline
    Read-Host
}

function Show-FilteredLogs {
    param(
        [string]$Pattern,
        [string]$LevelName,
        [ConsoleColor]$Color
    )

    Show-Header

    Write-Host "> Visualizzazione $LevelName" -ForegroundColor $Color
    Write-Host "  (Premi Ctrl+C per uscire)" -ForegroundColor Yellow
    Write-Host ""

    $logFiles = Get-ChildItem $LOGS_DIR -Filter "*.log" | Where-Object { Test-Path $_.FullName }

    if ($logFiles.Count -eq 0) {
        Write-Host "Nessun file log trovato!" -ForegroundColor Red
        return
    }

    Get-Content $logFiles.FullName -Wait -Tail 20 | Where-Object { $_ -match $Pattern } | ForEach-Object {
        Write-Host $_ -ForegroundColor $Color
    }
}

function Show-ErrorsOnly {
    Show-FilteredLogs "error|exception|failed" "ERRORI" Red
}

function Show-WarningsOnly {
    Show-FilteredLogs "warning|warn" "WARNING" Yellow
}

function Show-InfoOnly {
    Show-FilteredLogs "info" "INFO" Green
}

# ============================================================================
# FUNZIONI UTILITÀ
# ============================================================================

function Show-LogStatistics {
    Show-Header

    Write-Host "Statistiche Log" -ForegroundColor Cyan
    Write-Host ""

    Get-ChildItem $LOGS_DIR -Filter "*.log" | ForEach-Object {
        $name = $_.BaseName
        $size = [math]::Round($_.Length / 1KB, 2)
        $content = Get-Content $_.FullName
        $lines = $content.Count
        $errors = ($content | Select-String -Pattern "error|exception" -AllMatches).Count
        $warnings = ($content | Select-String -Pattern "warning|warn" -AllMatches).Count
        $infos = ($content | Select-String -Pattern "info" -AllMatches).Count

        Write-Host "--- $name ---" -ForegroundColor White
        Write-Host "  Dimensione:    ${size}KB"
        Write-Host "  Righe totali:  $lines"
        Write-Host "  " -NoNewline
        Write-Host "Errori:        $errors" -ForegroundColor Red
        Write-Host "  " -NoNewline
        Write-Host "Warning:       $warnings" -ForegroundColor Yellow
        Write-Host "  " -NoNewline
        Write-Host "Info:          $infos" -ForegroundColor Green
        Write-Host ""
    }

    # Spazio totale
    $totalSize = (Get-ChildItem $LOGS_DIR -Filter "*.log" | Measure-Object -Property Length -Sum).Sum
    $totalSizeMB = [math]::Round($totalSize / 1MB, 2)
    Write-Host "Spazio totale occupato: ${totalSizeMB}MB" -ForegroundColor Cyan
    Write-Host ""

    Write-Host "Premi INVIO per continuare..." -ForegroundColor White -NoNewline
    Read-Host
}

function Clear-Logs {
    Show-Header

    Write-Host "! ATTENZIONE: Questa operazione cancellera' tutti i log!" -ForegroundColor Red
    Write-Host ""

    $totalSize = (Get-ChildItem $LOGS_DIR -Filter "*.log" | Measure-Object -Property Length -Sum).Sum
    $totalSizeMB = [math]::Round($totalSize / 1MB, 2)
    Write-Host "Spazio da liberare: ${totalSizeMB}MB"
    Write-Host ""

    Write-Host "Sei sicuro? (yes/no): " -ForegroundColor White -NoNewline
    $confirm = Read-Host

    if ($confirm -eq "yes") {
        Write-Host ""
        Write-Host "Pulizia in corso..." -ForegroundColor Yellow

        # Backup
        $backupDir = Join-Path $LOGS_DIR "backup_$(Get-Date -Format 'yyyyMMdd_HHmmss')"
        New-Item -ItemType Directory -Path $backupDir -Force | Out-Null

        Get-ChildItem $LOGS_DIR -Filter "*.log" | ForEach-Object {
            Copy-Item $_.FullName -Destination $backupDir
        }

        Write-Host "v Backup creato in: $backupDir" -ForegroundColor Green

        # Svuota log
        Get-ChildItem $LOGS_DIR -Filter "*.log" | ForEach-Object {
            Clear-Content $_.FullName
            Write-Host "v Pulito: $($_.Name)" -ForegroundColor Green
        }

        Write-Host ""
        Write-Host "v Pulizia completata!" -ForegroundColor Green
        Start-Sleep -Seconds 2
    }
    else {
        Write-Host "Operazione annullata" -ForegroundColor Yellow
        Start-Sleep -Seconds 1
    }
}

function Export-Logs {
    Show-Header

    Write-Host "Esportazione Log" -ForegroundColor Cyan
    Write-Host ""

    $exportDir = Join-Path $PROJECT_ROOT "exports"
    New-Item -ItemType Directory -Path $exportDir -Force | Out-Null

    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    $exportFile = Join-Path $exportDir "logs_$timestamp.zip"

    Write-Host "Creazione archivio..." -ForegroundColor Yellow

    try {
        Compress-Archive -Path $LOGS_DIR -DestinationPath $exportFile -Force

        $size = [math]::Round((Get-Item $exportFile).Length / 1MB, 2)

        Write-Host ""
        Write-Host "v Esportazione completata!" -ForegroundColor Green
        Write-Host ""
        Write-Host "  File:       $exportFile"
        Write-Host "  Dimensione: ${size}MB"
        Write-Host ""
    }
    catch {
        Write-Host "X Errore durante l'esportazione" -ForegroundColor Red
        Write-Host "Dettagli: $_" -ForegroundColor Red
    }

    Write-Host ""
    Write-Host "Premi INVIO per continuare..." -ForegroundColor White -NoNewline
    Read-Host
}

# ============================================================================
# MAIN LOOP
# ============================================================================

# Verifica directory log
if (-not (Test-Path $LOGS_DIR)) {
    Write-Host "X Directory log non trovata: $LOGS_DIR" -ForegroundColor Red
    Write-Host "  Esegui prima: .\setup-rmi.ps1" -ForegroundColor Yellow
    exit 1
}

# Loop principale
while ($true) {
    Show-Menu
    $choice = Read-Host

    switch ($choice) {
        "1" { Show-LogSingle "rmiregistry" }
        "2" { Show-LogSingle "FrontDesk" }
        "3" { Show-LogSingle "Governante" }
        "4" { Show-LogSingle "Manager" }
        "5" { Show-LogFile "FrontDesk"}
        "6" { Show-LogFile "Governante"}
        "7" { Show-LogFile "Manager"}
        "8" { Show-AllLogsInterleaved }
        "9" { Search-InLogs }
        "10" { Show-ErrorsOnly }
        "11" { Show-WarningsOnly }
        "12" { Show-InfoOnly }
        "13" { Show-LogStatistics }
        "14" { Clear-Logs }
        "15" { Export-Logs }
        "0" {
            Clear-Host
            Write-Host "Arrivederci!" -ForegroundColor Green
            exit 0
        }
        default {
            Write-Host "X Scelta non valida" -ForegroundColor Red
            Start-Sleep -Seconds 1
        }
    }
}