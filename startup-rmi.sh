#!/bin/bash


#==============================================================================
# Script di Avvio RMI per Hotel Colossus
# Descrizione: Avvia RMI Registry e tutti i server RMI
#==============================================================================


set -e  # Blocca l'esecuzione dello script in caso di errore in qualsiasi comando


# Carica environment
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# METODO 1: Usa il nuovo script load-config.sh
if [ -f "$SCRIPT_DIR/load-config.sh" ]; then
    source "$SCRIPT_DIR/load-config.sh"

# METODO 2: Fallback su env.sh generato da SystemManager
elif [ -f "$SCRIPT_DIR/env.sh" ]; then
    source "$SCRIPT_DIR/env.sh"

# METODO 3: Configurazione manuale (compatibilità all'indietro)
else
    echo "⚠ File di configurazione non trovato, uso valori predefiniti"

    # Valori di default
    export RMI_PORT=1099
    export RMI_HOST=localhost
    export PROJECT_ROOT="$SCRIPT_DIR"
    export LOGS_DIR="$PROJECT_ROOT/logs"
    export PID_DIR="$PROJECT_ROOT/pids"

    # Classpath discovery
    if [ -d "target/classes" ]; then
        export CLASSPATH="$PROJECT_ROOT/target/classes"
    elif [ -d "out/production" ]; then
        export CLASSPATH="$PROJECT_ROOT/out/production"
    elif [ -d "bin" ]; then
        export CLASSPATH="$PROJECT_ROOT/bin"
    fi

    export JAVA_OPTS="-Djava.rmi.server.hostname=localhost"
fi


# --- CONFIGURAZIONE COLORI PER OUTPUT TERMINALE ---
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Funzione per stampare messaggi
print_info()
{
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success()
{
    echo -e "${GREEN}[✓]${NC} $1"
}

print_warning()
{
    echo -e "${YELLOW}[⚠]${NC} $1"
}

print_error()
{
    echo -e "${RED}[✗]${NC} $1"
}

echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
echo -e "${BLUE}     Hotel Colossus - Avvio Servizi RMI            ${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
echo ""


# Funzione per verificare se un processo è attivo
is_running()
{
    local pid_file="$1"
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p "$pid" > /dev/null 2>&1; then
            return 0  # Running
        else
            rm -f "$pid_file"  # Cleanup stale PID
        fi
    fi
    return 1  # Not running
}


# Funzione per aspettare che un servizio sia pronto
wait_for_service()
{
    local service_name="$1"
    local max_wait=10
    local count=0

    print_info "Attendo che $service_name sia pronto..."

    while [ $count -lt $max_wait ]; do
        if rmiregistry -h 2>&1 | grep -q "usage" || \
           nc -z localhost $RMI_PORT 2>/dev/null; then
            print_success "$service_name è pronto"
            return 0
        fi
        sleep 1
        count=$((count + 1))
        echo -n "."
    done

    echo ""
    print_error "$service_name non risponde dopo ${max_wait}s"
    return 1
}


# Funzione per avviare un servizio Java
start_service()
{
    local service_name="$1"
    local main_class="$2"
    local pid_file="$PID_DIR/${service_name}.pid"
    local log_file="$LOGS_DIR/${service_name}.log"

    print_info "Avvio $service_name..."

    if is_running "$pid_file"; then
        print_warning "$service_name è già in esecuzione (PID: $(cat $pid_file))"
        return 0
    fi

    # Avvia il servizio in background
    nohup mvn exec:java $JAVA_OPTS \
        -Dexec.mainClass="$main_class" \
        >> "$log_file" 2>&1 &

    local pid=$!
    echo $pid > "$pid_file"

    # Attendi che il servizio sia avviato
    sleep 2

    if ps -p $pid > /dev/null; then
        print_success "$service_name avviato (PID: $pid)"
        print_info "  Log: $log_file"
        return 0
    else
        print_error "$service_name non è partito! Controlla il log:"
        tail -n 20 "$log_file"
        rm -f "$pid_file"
        return 1
    fi
}


# ============================================================================
# MAIN EXECUTION
# ============================================================================


# 1. Verifica che nessun servizio sia già attivo
print_info "Verifica servizi esistenti..."


if lsof -Pi :$RMI_PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
    print_warning "Servizi RMI già attivi sulla porta $RMI_PORT"
    echo -e "   Eseguire ${YELLOW}./stop-rmi.sh${NC} prima di riavviare"
    exit 1
fi


# 2. Avvia RMI Registry
print_info "Avvio RMI Registry sulla porta $RMI_PORT..."

RMI_REGISTRY_PID="$PID_DIR/rmiregistry.pid"

if is_running "$RMI_REGISTRY_PID"; then
    print_warning "RMI Registry già in esecuzione"
else
    # Avvia rmiregistry
    rmiregistry $RMI_PORT > "$LOGS_DIR/rmiregistry.log" 2>&1 &
    RMI_PID=$!
    echo $RMI_PID > "$RMI_REGISTRY_PID"

    # Attendi che sia pronto
    if wait_for_service "RMI Registry"; then
        print_success "RMI Registry avviato (PID: $RMI_PID)"
    else
        print_error "RMI Registry non risponde"
        exit 1
    fi
fi



sleep 2


# 3. Avvia FrontDesk Server
start_service "FrontDesk" "it.unisa.Server.gestionePrenotazioni.FrontDesk"
sleep 3

# 4. Avvia Governante Server
start_service "Governante" "it.unisa.Server.gestioneCamere.Governante"
sleep 3


# 5. Avvia Manager Server
start_service "Manager" "it.unisa.Server.BackOffice.ManagerImpl"


# Summary
echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo -e "${GREEN}     Tutti i servizi RMI sono stati avviati!       ${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo ""
print_info "Servizi attivi:"


if is_running "$RMI_REGISTRY_PID"; then
    echo -e "  ${GREEN}●${NC} RMI Registry    (PID: $(cat $RMI_REGISTRY_PID))"
fi


if is_running "$PID_DIR/FrontDesk.pid"; then
    echo -e "  ${GREEN}●${NC} FrontDesk       (PID: $(cat $PID_DIR/FrontDesk.pid))"
fi


if is_running "$PID_DIR/Governante.pid"; then
    echo -e "  ${GREEN}●${NC} Governante      (PID: $(cat $PID_DIR/Governante.pid))"
fi

if is_running "$PID_DIR/Manager.pid"; then
    echo -e "  ${GREEN}●${NC} Manager         (PID: $(cat $PID_DIR/Manager.pid))"
fi


echo ""
print_info "Comandi utili:"
echo -e "  ${YELLOW}./status-rmi.sh${NC}            - Verifica stato servizi"
echo -e "  ${YELLOW}./logs-rmi.sh${NC}              - Visualizza log in tempo reale"
echo -e "  ${YELLOW}./configuration-rmi.sh${NC}     - Permette di modificare la configurazione"
echo -e "  ${YELLOW}./load-config.sh${NC}           - Carica la configurazione"
echo -e "  ${YELLOW}./maintenance-rmi.sh${NC}       - Programma la manutenzione"
echo -e "  ${YELLOW}./stop-rmi.sh${NC}              - Ferma tutti i servizi"















































































































































































































































































































































































































































































































echo ""