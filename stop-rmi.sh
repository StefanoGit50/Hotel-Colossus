#!/bin/bash


#==============================================================================
# Script di Stop RMI per Hotel Colossus
# Descrizione: Ferma tutti i servizi RMI in esecuzione
#==============================================================================


# Carica environment
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/env.sh"


# Colori
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


echo -e "${RED}═══════════════════════════════════════════════════${NC}"
echo -e "${RED}     Hotel Colossus - Stop Servizi RMI             ${NC}"
echo -e "${RED}═══════════════════════════════════════════════════${NC}"
echo ""


# Funzione per fermare un processo via PID file
stop_service()
{
    local service_name="$1"
    local pid_file="$PID_DIR/${service_name}.pid"

    if [ ! -f "$pid_file" ]; then
        print_info "$service_name non è in esecuzione (nessun PID file)"
        return 0
    fi

    local pid=$(cat "$pid_file")

    if ! ps -p "$pid" > /dev/null 2>&1; then
        print_warning "$service_name non è in esecuzione (PID stale)"
        rm -f "$pid_file"
        return 0
    fi

    print_info "Fermo $service_name (PID: $pid)..."

    # Tentativo graceful shutdown (SIGTERM)
    kill -TERM "$pid" 2>/dev/null

    # Attendi fino a 10 secondi
    local count=0
    while ps -p "$pid" > /dev/null 2>&1 && [ $count -lt 10 ]; do
        sleep 1
        count=$((count + 1))
        echo -n "."
    done
    echo ""

    # Se ancora attivo, forza (SIGKILL)
    if ps -p "$pid" > /dev/null 2>&1; then
        print_warning "$service_name non risponde, forzatura terminazione..."
        kill -KILL "$pid" 2>/dev/null
        sleep 1
    fi

    if ps -p "$pid" > /dev/null 2>&1; then
        print_error "Impossibile fermare $service_name!"
        return 1
    else
        print_success "$service_name fermato"
        rm -f "$pid_file"
        return 0
    fi
}


# Funzione per fermare tutti i processi Java RMI
kill_all_rmi_processes()
{
    print_info "Ricerca processi RMI rimasti..."

    # Trova tutti i processi Java con RMI
    local pids=$(ps aux | grep java | grep -E "(FrontDesk|Governante|rmiregistry)" | grep -v grep | awk '{print $2}')

    if [ -z "$pids" ]; then
        print_info "Nessun processo RMI trovato"
        return 0
    fi

    print_warning "Processi RMI trovati: $pids"
    echo -n "Fermare tutti? [y/N] "
    read -r response

    if [[ "$response" =~ ^[Yy]$ ]]; then
        for pid in $pids; do
            print_info "Fermo PID $pid..."
            kill -KILL "$pid" 2>/dev/null
        done
        print_success "Tutti i processi RMI fermati"
    fi
}


# ============================================================================
# MAIN EXECUTION
# ============================================================================



# Ferma servizi in ordine inverso rispetto all'avvio

# 1. Ferma Manager
stop_service "Manager"


# 2. Ferma Governante
stop_service "Governante"


# 3. Ferma FrontDesk
stop_service "FrontDesk"


# 4. Ferma RMI Registry
stop_service "rmiregistry"


# Verifica che la porta sia liberata
sleep 2

if lsof -Pi :$RMI_PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
    print_error "Porta $RMI_PORT ancora occupata!"
    echo -e "   Processo: $(lsof -Pi :$RMI_PORT -sTCP:LISTEN | tail -n 1)"
    kill_all_rmi_processes
else
    print_success "Porta $RMI_PORT liberata"
fi


# Cleanup PID files
print_info "Cleanup file PID..."
rm -f "$PID_DIR"/*.pid
print_success "Cleanup completato"


echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo -e "${GREEN}     Tutti i servizi RMI sono stati fermati!       ${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo ""


print_info "Per riavviare i servizi:"
echo -e "  ${YELLOW}./start-rmi.sh${NC}"
echo ""