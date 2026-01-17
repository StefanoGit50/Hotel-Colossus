#!/bin/bash

#==============================================================================
# Script di Caricamento Configurazione
# Descrizione: Carica le variabili da rmi.properties in formato bash
# Questo script può essere "source" da altri script per condividere la config
#==============================================================================

# Directory dello script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROPERTIES_FILE="$SCRIPT_DIR/rmi.properties"

# Colori
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Funzione per convertire property Java in variabile bash
# es: rmi.registry.port=1099 -> export RMI_PORT=1099
load_property() {
    local key="$1"
    local var_name="$2"

    # Leggi valore dal file properties (ignora commenti e righe vuote)
    local value=$(grep "^${key}=" "$PROPERTIES_FILE" 2>/dev/null | cut -d'=' -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')

    if [ -n "$value" ]; then
        export "$var_name"="$value"
        return 0
    fi
    return 1
}

# Verifica esistenza file properties
if [ ! -f "$PROPERTIES_FILE" ]; then
    echo -e "${RED}✗ File $PROPERTIES_FILE non trovato!${NC}" >&2
    echo -e "${YELLOW}  Esegui prima: java SystemManager${NC}" >&2
    return 1 2>/dev/null || exit 1
fi

# ============================================================================
# CARICA TUTTE LE CONFIGURAZIONI
# ============================================================================

# RMI Configuration
load_property "rmi.registry.port" "RMI_PORT"
load_property "rmi.registry.host" "RMI_HOST"
load_property "rmi.server.hostname" "RMI_SERVER_HOSTNAME"
load_property "rmi.connection.timeout" "RMI_CONNECTION_TIMEOUT"
load_property "rmi.read.timeout" "RMI_READ_TIMEOUT"

# Logging
load_property "log.level" "LOG_LEVEL"
load_property "log.directory" "LOGS_DIR"

# Database
load_property "db.host" "DB_HOST"
load_property "db.port" "DB_PORT"
load_property "db.name" "DB_NAME"
load_property "db.user" "DB_USER"
load_property "db.password" "DB_PASSWORD"

# Services
load_property "service.frontdesk.name" "SERVICE_FRONTDESK_NAME"
load_property "service.governante.name" "SERVICE_GOVERNANTE_NAME"
load_property "service.manager.name" "SERVICE_MANAGER_NAME"

# ============================================================================
# VARIABILI DERIVATE E DEFAULTS
# ============================================================================

# Project paths
export PROJECT_ROOT="$SCRIPT_DIR"
export LOGS_DIR="${LOGS_DIR:-$PROJECT_ROOT/logs}"
export PID_DIR="$PROJECT_ROOT/pids"

# Classpath discovery
if [ -d "$PROJECT_ROOT/target/classes" ]; then
    export CLASSPATH="$PROJECT_ROOT/target/classes"
elif [ -d "$PROJECT_ROOT/out/production" ]; then
    export CLASSPATH="$PROJECT_ROOT/out/production"
elif [ -d "$PROJECT_ROOT/bin" ]; then
    export CLASSPATH="$PROJECT_ROOT/bin"
else
    echo -e "${YELLOW}⚠ Classpath non trovato, usando directory corrente${NC}" >&2
    export CLASSPATH="$PROJECT_ROOT"
fi

# Aggiungi JAR libraries al classpath
if [ -d "$PROJECT_ROOT/lib" ]; then
    for jar in "$PROJECT_ROOT/lib"/*.jar; do
        if [ -f "$jar" ]; then
            export CLASSPATH="$CLASSPATH:$jar"
        fi
    done
fi

# Java RMI options
export JAVA_OPTS="-Djava.rmi.server.hostname=${RMI_SERVER_HOSTNAME:-localhost}"
export JAVA_OPTS="$JAVA_OPTS -Djava.security.policy=$PROJECT_ROOT/rmi.policy"
export JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.codebase=file://$CLASSPATH/"

# ============================================================================
# FUNZIONI UTILITY
# ============================================================================

# Funzione per stampare tutte le variabili caricate
print_config() {
    echo -e "${GREEN}Configurazione caricata:${NC}"
    echo "  RMI_PORT=$RMI_PORT"
    echo "  RMI_HOST=$RMI_HOST"
    echo "  RMI_SERVER_HOSTNAME=$RMI_SERVER_HOSTNAME"
    echo "  LOGS_DIR=$LOGS_DIR"
    echo "  DB_HOST=$DB_HOST"
    echo "  DB_PORT=$DB_PORT"
    echo "  DB_NAME=$DB_NAME"
    echo "  CLASSPATH=$CLASSPATH"
}

# Funzione per validare configurazione minima
validate_config() {
    local valid=true

    if [ -z "$RMI_PORT" ]; then
        echo -e "${RED}✗ RMI_PORT non configurato${NC}" >&2
        valid=false
    fi

    if [ -z "$RMI_HOST" ]; then
        echo -e "${RED}✗ RMI_HOST non configurato${NC}" >&2
        valid=false
    fi

    if [ -z "$CLASSPATH" ] || [ ! -d "$CLASSPATH" ]; then
        echo -e "${RED}✗ CLASSPATH non valido: $CLASSPATH${NC}" >&2
        valid=false
    fi

    if [ "$valid" = true ]; then
        echo -e "${GREEN}✓ Configurazione valida${NC}"
        return 0
    else
        return 1
    fi
}

# Mostra messaggio di conferma
echo -e "${GREEN}✓ Configurazione caricata da $PROPERTIES_FILE${NC}"

# Se eseguito direttamente (non con source), mostra configurazione
if [ "${BASH_SOURCE[0]}" = "${0}" ]; then
    echo ""
    print_config
    echo ""
    validate_config
fi