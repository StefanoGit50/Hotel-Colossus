#!/bin/bash


#==============================================================================
# Script di Status RMI per Hotel Colossus
# Descrizione: Mostra lo stato di tutti i servizi RMI
#==============================================================================


# Carica environment

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Carica configurazione
if [ -f "$SCRIPT_DIR/load-config.sh" ]; then
    source "$SCRIPT_DIR/load-config.sh"
elif [ -f "$SCRIPT_DIR/env.sh" ]; then
    source "$SCRIPT_DIR/env.sh"
else
    echo "⚠ Configurazione non trovata"
    exit 1
fi


# Colori
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
GRAY='\033[0;90m'
NC='\033[0m'


print_header()
{
    echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}     Hotel Colossus - Status Servizi RMI           ${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
    echo ""
}


# Funzione per verificare status di un servizio
check_service_status()
{
    local service_name="$1"
    local pid_file="$PID_DIR/${service_name}.pid"
    local log_file="$LOGS_DIR/${service_name}.log"

    printf "%-20s" "$service_name:"

    if [ ! -f "$pid_file" ]; then
        echo -e "${GRAY}●${NC} Non in esecuzione"
        return 1
    fi

    local pid=$(cat "$pid_file")

    if ps -p "$pid" > /dev/null 2>&1; then
        # Calcola uptime
        local start_time=$(ps -o lstart= -p "$pid")
        local uptime=$(ps -o etime= -p "$pid" | tr -d ' ')

        echo -e "${GREEN}●${NC} Running (PID: $pid, Uptime: $uptime)"

        # Mostra memoria usata
        local mem=$(ps -o rss= -p "$pid" | tr -d ' ')
        local mem_mb=$((mem / 1024))
        echo -e "                      Memory: ${mem_mb}MB"

        # Mostra ultime righe del log
        if [ -f "$log_file" ]; then
            local log_size=$(du -h "$log_file" | cut -f1)
            echo -e "                      Log: $log_size ($log_file)"
        fi

        return 0
    else
        echo -e "${RED}●${NC} Dead (PID stale: $pid)"
        rm -f "$pid_file"
        return 1
    fi
}


# Funzione per verificare porta RMI
check_rmi_port()
{
    printf "%-20s" "RMI Port $RMI_PORT:"

    if lsof -Pi :$RMI_PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${GREEN}●${NC} Listening"
        local process=$(lsof -Pi :$RMI_PORT -sTCP:LISTEN | tail -n 1 | awk '{print $1 " (PID: " $2 ")"}')
        echo -e "                      Process: $process"
    else
        echo -e "${RED}●${NC} Not listening"
    fi
}


# Funzione per testare connessione RMI
test_rmi_connection()
{
    printf "%-20s" "RMI Registry:"

    # Tenta di listare i servizi registrati
    local services=$(rmiregistry -h 2>&1 || true)

    # Verifica con netcat
    if nc -z localhost $RMI_PORT 2>/dev/null; then
        echo -e "${GREEN}●${NC} Reachable"

        # Tenta di listare servizi (richiede compilazione tool)
    else
        echo -e "${RED}●${NC} Unreachable"
    fi
}


# Funzione per mostrare servizi registrati
list_rmi_services()
{
    echo ""
    echo -e "${BLUE}Servizi registrati nel Registry:${NC}"

    # Crea un semplice Java tool per listare servizi
    local list_tool="$PID_DIR/ListRMIServices.java"

    cat > "$list_tool" << 'EOF'
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ListRMIServices {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            String[] services = registry.list();

            if (services.length == 0) {
                System.out.println("  (nessun servizio registrato)");
            } else {
                for (String service : services) {
                    System.out.println("  • " + service);
                }
            }
        } catch (Exception e) {
            System.out.println("  ✗ Errore connessione: " + e.getMessage());
        }
    }
}
EOF

    # Compila ed esegui
    javac -d "$PID_DIR" "$list_tool" 2>/dev/null
    java -cp "$PID_DIR" ListRMIServices 2>/dev/null || echo "  ✗ Registry non disponibile"

    # Cleanup
    rm -f "$list_tool" "$PID_DIR/ListRMIServices.class"
}


# ============================================================================
# MAIN EXECUTION
# ============================================================================


print_header


echo -e "${BLUE}System Information:${NC}"
echo -e "  Hostname:   $(hostname)"
echo -e "  Java:       $(java -version 2>&1 | head -n 1)"
echo -e "  Project:    $PROJECT_ROOT"
echo ""


echo -e "${BLUE}Network Status:${NC}"
check_rmi_port
test_rmi_connection
echo ""


echo -e "${BLUE}Service Status:${NC}"
check_service_status "rmiregistry"
check_service_status "FrontDesk"
check_service_status "Governante"
check_service_status "Manager"


list_rmi_services


# Conta servizi attivi
echo ""
active_count=0
for service in rmiregistry FrontDesk Governante Manager; do
    if [ -f "$PID_DIR/${service}.pid" ]; then
        pid=$(cat "$PID_DIR/${service}.pid")
        if ps -p "$pid" > /dev/null 2>&1; then
            active_count=$((active_count + 1))
        fi
    fi
done


echo -e "${BLUE}Summary:${NC} $active_count/4 servizi attivi"






























































































































































































































































































































































































































# Suggerimenti
echo ""
echo -e "${BLUE}Comandi disponibili:${NC}"
echo -e "  ${YELLOW}./start-rmi.sh${NC}             - Avvia servizi"
echo -e "  ${YELLOW}./stop-rmi.sh${NC}              - Ferma servizi"
echo -e "  ${YELLOW}./restart-rmi.sh${NC}           - Riavvia servizi"
echo -e "  ${YELLOW}./logs-rmi.sh${NC}              - Visualizza log"
echo -e "  ${YELLOW}./configuration-rmi.sh${NC}     - Permette di modificare la configurazione"
echo -e "  ${YELLOW}./load-config.sh${NC}           - Carica la configurazione"
echo -e "  ${YELLOW}./maintenance-rmi.sh${NC}       - Programma la manutenzione"
echo ""