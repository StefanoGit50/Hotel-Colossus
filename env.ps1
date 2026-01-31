#==============================================================================
# Environment Configuration - Hotel Colossus (Windows)
#==============================================================================

# Directory del progetto
$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$PROJECT_ROOT = $SCRIPT_DIR

# Configurazione RMI (da rmi.properties)
$RMI_PORT = 1099
$RMI_HOST = "localhost"

# Directory

$LOGS_DIR = Join-Path $PROJECT_ROOT "logs"
$PID_DIR = Join-Path $PROJECT_ROOT "pids"
$CONFIG_DIR = Join-Path $PROJECT_ROOT "config"

# Crea le directory se non esistono
@($LOGS_DIR, $PID_DIR) | ForEach-Object {
    if (-not (Test-Path $_)) {
        New-Item -ItemType Directory -Path $_ -Force | Out-Null
    }
}

# Classpath
if (Test-Path (Join-Path $PROJECT_ROOT "target\classes")) {
    $CLASSPATH = Join-Path $PROJECT_ROOT "target\classes"
} elseif (Test-Path (Join-Path $PROJECT_ROOT "out\production")) {
    $CLASSPATH = Join-Path $PROJECT_ROOT "out\production"
} else {
    Write-Warning "Classi compilate non trovate!"
    $CLASSPATH = Join-Path $PROJECT_ROOT "target\classes"
}

# Opzioni JVM
$JAVA_OPTS = "-Djava.rmi.server.hostname=$RMI_HOST -Xmx512m -Xms256m"

# Carica configurazione da rmi.properties se esiste
$propsFile = Join-Path $CONFIG_DIR "rmi.properties"
if (Test-Path $propsFile) {
    Get-Content $propsFile | ForEach-Object {
        if ($_ -match '^([^#].+?)=(.+)$') {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()

            switch ($key) {
                "rmi.registry.port" { $RMI_PORT = [int]$value }
                "rmi.registry.host" { $RMI_HOST = $value }
                "rmi.server.hostname" {
                    $JAVA_OPTS = $JAVA_OPTS -replace '-Djava.rmi.server.hostname=\S+', "-Djava.rmi.server.hostname=$value"
                }
            }
        }
    }
}

# Esporta le variabili globali
$global:PROJECT_ROOT = $PROJECT_ROOT
$global:RMI_PORT = $RMI_PORT
$global:RMI_HOST = $RMI_HOST
$global:BUILD_DIR = $BUILD_DIR
$global:LIB_DIR = $LIB_DIR
$global:LOGS_DIR = $LOGS_DIR
$global:PID_DIR = $PID_DIR
$global:CONFIG_DIR = $CONFIG_DIR
$global:CLASSPATH = $CLASSPATH
$global:JAVA_OPTS = $JAVA_OPTS

# Debug info (decommenta per vedere le variabili caricate)
# Write-Host "Environment loaded:" -ForegroundColor Cyan
# Write-Host "  PROJECT_ROOT: $PROJECT_ROOT"
# Write-Host "  RMI_PORT: $RMI_PORT"
# Write-Host "  RMI_HOST: $RMI_HOST"
# Write-Host "  CLASSPATH: $CLASSPATH"
# Write-Host "  LOGS_DIR: $LOGS_DIR"
# Write-Host "  JAVA_OPTS: $JAVA_OPTS"
