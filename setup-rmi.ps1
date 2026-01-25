#==============================================================================
# Script di Setup RMI per Hotel Colossus (Windows PowerShell)
# Descrizione: Configura l'ambiente RMI e crea le directory necessarie
#==============================================================================

$ErrorActionPreference = "Stop"

# --- CONFIGURAZIONE ---
$RMI_PORT = 1099
$PROJECT_ROOT = Split-Path -Parent $MyInvocation.MyCommand.Path
$BUILD_DIR = Join-Path $PROJECT_ROOT "build\classes"
$LIB_DIR = Join-Path $PROJECT_ROOT "lib"
$LOGS_DIR = Join-Path $PROJECT_ROOT "logs"
$PID_DIR = Join-Path $PROJECT_ROOT "pids"
$CONFIG_DIR = Join-Path $PROJECT_ROOT "config"
$CONFIG_FILE = Join-Path $CONFIG_DIR "rmi.properties"

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
Write-Host "     Hotel Colossus - RMI Setup Script            " -ForegroundColor Blue
Write-Host "===================================================" -ForegroundColor Blue
Write-Host ""

# ============================================================================
# CREAZIONE DIRECTORY
# ============================================================================

Write-Info "Creazione directory necessarie..."

$directories = @($BUILD_DIR, $LIB_DIR, $LOGS_DIR, $PID_DIR, $CONFIG_DIR)

foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
}

Write-Success "Directory create: build\classes, lib, logs, pids, config"

# ============================================================================
# CREAZIONE FILE DI CONFIGURAZIONE
# ============================================================================

Write-Info "Creazione file di configurazione RMI..."

$configContent = @"
# RMI Configuration for Hotel Colossus
# Generated: $(Get-Date -Format "ddd MMM dd HH:mm:ss yyyy")

# RMI Registry
rmi.registry.port=$RMI_PORT
rmi.registry.host=localhost

# RMI Server Settings
rmi.server.hostname=localhost
rmi.connection.timeout=30000
rmi.read.timeout=60000

# Security (se necessario)
# java.security.policy=rmi.policy

# Logging
log.level=INFO
log.directory=$LOGS_DIR

# Database
db.host=localhost
db.port=3306
db.name=hotelcolossus
db.user=root
db.password=yourpassword

# Services
service.frontdesk.name=GestionePrenotazioni
service.governante.name=GestoreCamere
service.manager.name=GestioneImpiegati
"@

$configContent | Out-File -FilePath $CONFIG_FILE -Encoding UTF8
Write-Success "File di configurazione creato: $CONFIG_FILE"

# ============================================================================
# VERIFICA PORTA RMI
# ============================================================================

Write-Info "Verifica disponibilita' porta $RMI_PORT..."

try {
    $connection = Get-NetTCPConnection -LocalPort $RMI_PORT -State Listen -ErrorAction Stop
    Write-Warning "Porta $RMI_PORT gia' in uso!"

    $process = Get-Process -Id $connection.OwningProcess -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   Processo: $($process.Name) (PID: $($process.Id))"
    }
    Write-Host "   Eseguire: " -NoNewline
    Write-Host ".\stop-rmi.ps1" -ForegroundColor Yellow -NoNewline
    Write-Host " per terminare i servizi RMI"
} catch {
    Write-Success "Porta $RMI_PORT disponibile"
}

# ============================================================================
# VERIFICA COMPILAZIONE
# ============================================================================

Write-Info "Verifica compilazione progetto..."

$CLASSPATH = $null

if (Test-Path "$PROJECT_ROOT\target\classes") {
    Write-Success "Classi trovate in target\classes (Maven)"
    $CLASSPATH = "$PROJECT_ROOT\target\classes"
} elseif (Test-Path "$PROJECT_ROOT\out\production") {
    Write-Success "Classi trovate in out\production (IntelliJ)"
    $CLASSPATH = "$PROJECT_ROOT\out\production"
} elseif (Test-Path $BUILD_DIR) {
    $classFiles = Get-ChildItem -Path $BUILD_DIR -Recurse -Filter "*.class" -ErrorAction SilentlyContinue
    if ($classFiles) {
        Write-Success "Classi trovate in build\classes"
        $CLASSPATH = $BUILD_DIR
    } else {
        Write-Warning "Directory build\classes esiste ma e' vuota"
        $CLASSPATH = $BUILD_DIR
    }
} else {
    Write-ErrorMsg "Classi non trovate! Compilare il progetto prima."
    Write-Host ""
    Write-Host "   Compilazione manuale:" -ForegroundColor Yellow
    Write-Host "   javac -d build\classes -sourcepath src src\it\unisa\**\*.java"
    Write-Host ""
    exit 1
}

# ============================================================================
# AGGIORNAMENTO .gitignore
# ============================================================================

Write-Info "Aggiornamento .gitignore..."

$gitignorePath = Join-Path $PROJECT_ROOT ".gitignore"

if (-not (Test-Path $gitignorePath)) {
    New-Item -ItemType File -Path $gitignorePath | Out-Null
}

$gitignoreContent = Get-Content $gitignorePath -ErrorAction SilentlyContinue

$entriesToAdd = @(
    "logs/",
    "pids/",
    "*.log",
    "*.pid",
    "build/",
    "target/",
    ".idea/",
    "*.class"
)

$updated = $false
foreach ($entry in $entriesToAdd) {
    if ($gitignoreContent -notcontains $entry) {
        Add-Content -Path $gitignorePath -Value $entry
        $updated = $true
    }
}

if ($updated) {
    Write-Success ".gitignore aggiornato"
} else {
    Write-Success ".gitignore gia' configurato"
}

# ============================================================================
# VERIFICA JAVA
# ============================================================================

Write-Info "Verifica installazione Java..."

try {
    $javaVersion = (java -version 2>&1 | Select-Object -First 1)
    Write-Success "Java trovato: $javaVersion"
} catch {
    Write-Warning "Java non trovato da Powershell, controllare la variabile d'ambiente. Installare JDK 17 o superiore"
    Write-Host ""
    Write-Host "   Download JDK:" -ForegroundColor Yellow
    Write-Host "   https://adoptium.net/"
    Write-Host "   https://www.oracle.com/java/technologies/downloads/"
    Write-Host ""
}

try {
    $javacVersion = (javac -version 2>&1)
    Write-Success "Compilatore Java trovato: $javacVersion"
} catch {
    Write-Warning "javac non trovato - verificare installazione JDK"
}

try {
    rmiregistry -help 2>&1 | Out-Null
    Write-Success "rmiregistry trovato"
} catch {
    Write-Warning "rmiregistry non trovato - verificare PATH Java"
}

# ============================================================================
# VERIFICA SCRIPT RMI
# ============================================================================

Write-Info "Verifica script RMI..."

$requiredScripts = @(
    "start-rmi.ps1",
    "stop-rmi.ps1",
    "status-rmi.ps1",
    "restart-rmi.ps1",
    "logs-rmi.ps1",
    "env.ps1"
)

$missingScripts = @()
foreach ($script in $requiredScripts) {
    $scriptPath = Join-Path $PROJECT_ROOT $script
    if (-not (Test-Path $scriptPath)) {
        $missingScripts += $script
    }
}

if ($missingScripts.Count -eq 0) {
    Write-Success "Tutti gli script RMI presenti"
} else {
    Write-Warning "Script mancanti: $($missingScripts -join ', ')"
    Write-Host "   Scaricare da: https://github.com/StefanoGit50/Hotel-Colossus" -ForegroundColor Yellow
}

# ============================================================================
# SUMMARY
# ============================================================================

Write-Host ""
Write-Host "===================================================" -ForegroundColor Green
Write-Host "     Setup completato con successo!                " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green
Write-Host ""

Write-Info "Prossimi passi:"
Write-Host "  1. " -NoNewline
Write-Host ".\start-rmi.ps1" -ForegroundColor Yellow -NoNewline
Write-Host "        - Avvia tutti i servizi RMI"

Write-Host "  2. " -NoNewline
Write-Host ".\status-rmi.ps1" -ForegroundColor Yellow -NoNewline
Write-Host "       - Verifica stato servizi"

Write-Host "  3. " -NoNewline
Write-Host ".\stop-rmi.ps1" -ForegroundColor Yellow -NoNewline
Write-Host "         - Ferma tutti i servizi"

Write-Host ""

Write-Info "File creati:"
Write-Host "  * config\rmi.properties - Configurazione"
Write-Host "  * env.ps1               - Variabili ambiente"
Write-Host "  * logs\                 - Directory log"
Write-Host "  * pids\                 - File PID processi"
Write-Host "  * build\classes\        - Classi compilate"
Write-Host ""

Write-Info "Configurazione:"
Write-Host "  Porta RMI: $RMI_PORT"
Write-Host "  CLASSPATH: $CLASSPATH"
Write-Host "  Logs: $LOGS_DIR"
Write-Host ""

# ============================================================================
# SUGGERIMENTI
# ============================================================================

Write-Host "Suggerimenti:" -ForegroundColor Cyan
Write-Host ""

$classesFound = $false

if (Test-Path "$PROJECT_ROOT\target\classes\it") {
    $classesFound = $true
} elseif (Test-Path "$PROJECT_ROOT\out\production") {
    $classesFound = $true
} elseif (Test-Path "$BUILD_DIR\it") {
    $classesFound = $true
}

if (-not $classesFound) {
    Write-Host "  [!] Le classi non sono compilate" -ForegroundColor Yellow
    Write-Host "      Maven: mvn compile" -ForegroundColor Yellow
    Write-Host "      Manuale: javac -d build\classes -sourcepath src src\it\unisa\**\*.java"
    Write-Host ""
} else {
    Write-Host "  [OK] Classi compilate trovate in: $CLASSPATH" -ForegroundColor Green
    Write-Host ""
}