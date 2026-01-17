#!/bin/bash


#==============================================================================
# Script di Visualizzazione Log RMI per Hotel Colossus
# Descrizione: Visualizza i log dei servizi RMI in tempo reale con opzioni
#              avanzate di filtraggio e ricerca
# Autore: Hotel Colossus Team
# Versione: 2.0
#==============================================================================


set -e


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


# ============================================================================
# CONFIGURAZIONE COLORI
# ============================================================================


RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
GRAY='\033[0;90m'
NC='\033[0m' # No Color


# ============================================================================
# FUNZIONI DI UTILITÀ
# ============================================================================


print_header()
{
    clear
    echo -e "${BLUE}════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}     Hotel Colossus - Visualizzazione Log          ${NC}"
    echo -e "${BLUE}════════════════════════════════════════════════════${NC}"
    echo ""
}

print_menu()
{
    print_header
    echo -e "${CYAN}Seleziona il servizio di cui visualizzare i log:${NC}"
    echo ""
    echo -e "  ${GREEN}1)${NC} RMI Registry"
    echo -e "  ${GREEN}2)${NC} FrontDesk Server"
    echo -e "  ${GREEN}3)${NC} Governante Server"
    echo -e "  ${GREEN}4)${NC} Manager Server"
    echo ""
    echo -e "${CYAN}Visualizzazione multipla:${NC}"
    echo -e "  ${GREEN}5)${NC} Tutti i servizi (split screen)"
    echo -e "  ${GREEN}6)${NC} Tutti i servizi (interleaved)"
    echo ""
    echo -e "${CYAN}Filtri e ricerca:${NC}"
    echo -e "  ${YELLOW}7)${NC} Cerca nel log (grep)"
    echo -e "  ${YELLOW}8)${NC} Solo ERRORI"
    echo -e "  ${YELLOW}9)${NC} Solo WARNING"
    echo -e "  ${YELLOW}10)${NC} Solo INFO"
    echo ""
    echo -e "${CYAN}Utilità:${NC}"
    echo -e "  ${MAGENTA}11)${NC} Statistiche log"
    echo -e "  ${MAGENTA}12)${NC} Pulisci log"
    echo -e "  ${MAGENTA}13)${NC} Esporta log"
    echo ""
    echo -e "  ${RED}0)${NC} Esci"
    echo ""
    echo -ne "${WHITE}Scelta: ${NC}"
}


# Verifica esistenza log file
check_log_exists()
{
    local log_file="$1"

    if [ ! -f "$log_file" ]; then
        echo -e "${RED}✗ Log non trovato: $log_file${NC}"
        echo -e "${YELLOW}  Il servizio potrebbe non essere stato ancora avviato.${NC}"
        echo ""
        echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
        read
        return 1
    fi
    return 0
}


# Mostra dimensione file
show_file_info()
{
    local log_file="$1"
    local service_name="$2"

    if [ -f "$log_file" ]; then
        local size=$(du -h "$log_file" | cut -f1)
        local lines=$(wc -l < "$log_file")
        local modified=$(stat -f "%Sm" -t "%Y-%m-%d %H:%M:%S" "$log_file" 2>/dev/null || stat -c "%y" "$log_file" 2>/dev/null | cut -d'.' -f1)

        echo -e "${GRAY}┌────────────────────────────────────────────────────┐${NC}"
        echo -e "${GRAY}│${NC} ${CYAN}Servizio:${NC} $service_name"
        echo -e "${GRAY}│${NC} ${CYAN}File:${NC}     $log_file"
        echo -e "${GRAY}│${NC} ${CYAN}Dimensione:${NC} $size (${lines} righe)"
        echo -e "${GRAY}│${NC} ${CYAN}Modificato:${NC} $modified"
        echo -e "${GRAY}└────────────────────────────────────────────────────┘${NC}"
        echo ""
    fi
}


# ============================================================================
# FUNZIONI DI VISUALIZZAZIONE
# ============================================================================


# Visualizza log singolo con tail -f e colorazione
view_log_single()
{
    local service_name="$1"
    local log_file="$LOGS_DIR/${service_name}.log"

    print_header
    show_file_info "$log_file" "$service_name"

    check_log_exists "$log_file" || return

    echo -e "${GREEN}▶ Visualizzazione log in tempo reale${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per uscire)${NC}"
    echo ""
    echo -e "${GRAY}════════════════════════════════════════════════════${NC}"
    echo ""

    # Tail con colorazione automatica
    tail -f "$log_file" | while IFS= read -r line; do
        # Colora in base al livello di log
        if echo "$line" | grep -qi "error\|exception\|failed"; then
            echo -e "${RED}$line${NC}"
        elif echo "$line" | grep -qi "warning\|warn"; then
            echo -e "${YELLOW}$line${NC}"
        elif echo "$line" | grep -qi "info"; then
            echo -e "${GREEN}$line${NC}"
        elif echo "$line" | grep -qi "debug"; then
            echo -e "${GRAY}$line${NC}"
        elif echo "$line" | grep -qi "success\|✓\|completed"; then
            echo -e "${CYAN}$line${NC}"
        else
            echo "$line"
        fi
    done
}


# Visualizza tutti i log con split (richiede tmux o screen)
view_all_logs_split()
{
    print_header

    # Verifica se tmux è disponibile
    if ! command -v tmux &> /dev/null; then
        echo -e "${RED}✗ tmux non installato${NC}"
        echo -e "${YELLOW}  Installa tmux per usare questa funzione:${NC}"
        echo -e "    ${WHITE}sudo apt-get install tmux${NC}  (Debian/Ubuntu)"
        echo -e "    ${WHITE}brew install tmux${NC}          (macOS)"
        echo ""
        echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
        read
        return
    fi

    echo -e "${GREEN}▶ Avvio visualizzazione split con tmux${NC}"
    echo -e "${YELLOW}  Comandi tmux utili:${NC}"
    echo -e "    Ctrl+B poi %  = Split verticale"
    echo -e "    Ctrl+B poi \"  = Split orizzontale"
    echo -e "    Ctrl+B poi o  = Switch tra pane"
    echo -e "    Ctrl+B poi d  = Detach (ritorna al menu)"
    echo ""
    echo -e "${CYAN}  Avvio tra 3 secondi...${NC}"
    sleep 3

    # Crea sessione tmux con layout predefinito
    tmux new-session -d -s hotelcolossus "tail -f $LOGS_DIR/rmiregistry.log"
    tmux split-window -h "tail -f $LOGS_DIR/FrontDesk.log"
    tmux split-window -v "tail -f $LOGS_DIR/Governante.log"
    tmux select-layout tiled
    tmux attach-session -t hotelcolossus
}


# Visualizza tutti i log interleaved
view_all_logs_interleaved()
{
    print_header

    echo -e "${GREEN}▶ Visualizzazione tutti i log (interleaved)${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per uscire)${NC}"
    echo ""

    # Usa tail multipli con prefix
    tail -f \
        "$LOGS_DIR/rmiregistry.log" \
        "$LOGS_DIR/FrontDesk.log" \
        "$LOGS_DIR/Governante.log" \
        2>/dev/null | \
    while IFS= read -r line; do
        # Aggiungi timestamp se mancante
        if [[ ! "$line" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2} ]]; then
            line="[$(date '+%H:%M:%S')] $line"
        fi

        # Identifica la sorgente dal path
        if echo "$line" | grep -q "rmiregistry"; then
            echo -e "${BLUE}[REGISTRY]${NC} $line"
        elif echo "$line" | grep -q "FrontDesk"; then
            echo -e "${GREEN}[FRONTDESK]${NC} $line"
        elif echo "$line" | grep -q "Governante"; then
            echo -e "${MAGENTA}[GOVERNANTE]${NC} $line"
        else
            # Colora in base al livello
            if echo "$line" | grep -qi "error"; then
                echo -e "${RED}$line${NC}"
            elif echo "$line" | grep -qi "warn"; then
                echo -e "${YELLOW}$line${NC}"
            else
                echo "$line"
            fi
        fi
    done
}


# ============================================================================
# FUNZIONI DI FILTRO E RICERCA
# ============================================================================


# Cerca nel log
search_in_logs()
{
    print_header

    echo -e "${CYAN}Ricerca nei log${NC}"
    echo ""
    echo -ne "${WHITE}Inserisci termine di ricerca: ${NC}"
    read search_term

    if [ -z "$search_term" ]; then
        echo -e "${RED}✗ Termine di ricerca vuoto${NC}"
        sleep 2
        return
    fi

    echo ""
    echo -e "${GREEN}▶ Ricerca di '${search_term}' in tutti i log...${NC}"
    echo ""

    local found=0

    for log in "$LOGS_DIR"/*.log; do
        if [ -f "$log" ]; then
            local matches=$(grep -i "$search_term" "$log" 2>/dev/null)

            if [ -n "$matches" ]; then
                found=1
                echo -e "${CYAN}═══ $(basename $log) ═══${NC}"
                echo "$matches" | while IFS= read -r line; do
                    # Evidenzia il termine cercato
                    echo "$line" | sed "s/$search_term/${YELLOW}${search_term}${NC}/gi"
                done
                echo ""
            fi
        fi
    done

    if [ $found -eq 0 ]; then
        echo -e "${YELLOW}Nessun risultato trovato per '${search_term}'${NC}"
    fi

    echo ""
    echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
    read
}


# Filtra per livello di log
filter_by_level()
{
    local level="$1"
    local level_name="$2"
    local color="$3"

    print_header

    echo -e "${color}▶ Visualizzazione ${level_name}${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per uscire)${NC}"
    echo ""

    # Tail tutti i log e filtra per livello
    tail -f "$LOGS_DIR"/*.log 2>/dev/null | \
    grep -i "$level" | \
    while IFS= read -r line; do
        echo -e "${color}$line${NC}"
    done
}


# Solo errori
view_errors_only()
{
    filter_by_level "error\|exception\|failed" "ERRORI" "$RED"
}


# Solo warning
view_warnings_only()
{
    filter_by_level "warning\|warn" "WARNING" "$YELLOW"
}


# Solo info
view_info_only()
{
    filter_by_level "info" "INFO" "$GREEN"
}


# ============================================================================
# FUNZIONI UTILITÀ
# ============================================================================


# Statistiche log
show_log_statistics()
{
    print_header

    echo -e "${CYAN}Statistiche Log${NC}"
    echo ""

    for log in "$LOGS_DIR"/*.log; do
        if [ -f "$log" ]; then
            local name=$(basename "$log" .log)
            local size=$(du -h "$log" | cut -f1)
            local lines=$(wc -l < "$log")
            local errors=$(grep -ci "error\|exception" "$log" 2>/dev/null || echo 0)
            local warnings=$(grep -ci "warning\|warn" "$log" 2>/dev/null || echo 0)
            local infos=$(grep -ci "info" "$log" 2>/dev/null || echo 0)

            echo -e "${WHITE}━━━ $name ━━━${NC}"
            echo -e "  Dimensione:    ${size}"
            echo -e "  Righe totali:  ${lines}"
            echo -e "  ${RED}Errori:${NC}        ${errors}"
            echo -e "  ${YELLOW}Warning:${NC}       ${warnings}"
            echo -e "  ${GREEN}Info:${NC}          ${infos}"
            echo ""
        fi
    done

    # Spazio totale occupato
    local total_size=$(du -sh "$LOGS_DIR" 2>/dev/null | cut -f1)
    echo -e "${CYAN}Spazio totale occupato: ${total_size}${NC}"
    echo ""

    echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
    read
}


# Pulisci log
clean_logs()
{
    print_header

    echo -e "${RED}⚠ ATTENZIONE: Questa operazione cancellerà tutti i log!${NC}"
    echo ""

    # Mostra dimensione attuale
    local total_size=$(du -sh "$LOGS_DIR" 2>/dev/null | cut -f1)
    echo -e "Spazio da liberare: ${total_size}"
    echo ""

    echo -ne "${WHITE}Sei sicuro? (yes/no): ${NC}"
    read confirm

    if [ "$confirm" == "yes" ]; then
        echo ""
        echo -e "${YELLOW}Pulizia in corso...${NC}"

        # Backup prima di cancellare (opzionale)
        local backup_dir="$LOGS_DIR/backup_$(date +%Y%m%d_%H%M%S)"
        mkdir -p "$backup_dir"

        for log in "$LOGS_DIR"/*.log; do
            if [ -f "$log" ]; then
                cp "$log" "$backup_dir/"
            fi
        done

        echo -e "${GREEN}✓ Backup creato in: $backup_dir${NC}"

        # Svuota i log (non cancellare, per evitare problemi con servizi attivi)
        for log in "$LOGS_DIR"/*.log; do
            if [ -f "$log" ]; then
                > "$log"  # Truncate file
                echo -e "${GREEN}✓ Pulito: $(basename $log)${NC}"
            fi
        done

        echo ""
        echo -e "${GREEN}✓ Pulizia completata!${NC}"
        sleep 2
    else
        echo -e "${YELLOW}Operazione annullata${NC}"
        sleep 1
    fi
}


# Esporta log
export_logs()
{
    print_header

    echo -e "${CYAN}Esportazione Log${NC}"
    echo ""

    local export_dir="$PROJECT_ROOT/exports"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local export_file="$export_dir/logs_${timestamp}.tar.gz"

    mkdir -p "$export_dir"

    echo -e "${YELLOW}Creazione archivio...${NC}"

    tar -czf "$export_file" -C "$PROJECT_ROOT" logs/ 2>/dev/null

    if [ $? -eq 0 ]; then
        local size=$(du -h "$export_file" | cut -f1)
        echo ""
        echo -e "${GREEN}✓ Esportazione completata!${NC}"
        echo ""
        echo -e "  File:       $export_file"
        echo -e "  Dimensione: $size"
        echo ""
    else
        echo -e "${RED}✗ Errore durante l'esportazione${NC}"
    fi

    echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
    read
}


# ============================================================================
# MAIN LOOP
# ============================================================================


# Verifica che la directory log esista
if [ ! -d "$LOGS_DIR" ]; then
    echo -e "${RED}✗ Directory log non trovata: $LOGS_DIR${NC}"
    echo -e "${YELLOW}  Esegui prima: ./setup-rmi.sh${NC}"
    exit 1
fi


# Loop principale
while true; do
    print_menu
    read choice

    case $choice in
        1)
            view_log_single "rmiregistry"
            ;;
        2)
            view_log_single "FrontDesk"
            ;;
        3)
            view_log_single "Governante"
            ;;
        4)
            view_log_single "Manager"
            ;;
        5)
            view_all_logs_split
            ;;
        6)
            view_all_logs_interleaved
            ;;
        7)
            search_in_logs
            ;;
        8)
            view_errors_only
            ;;
        9)
            view_warnings_only
            ;;
        10)
            view_info_only
            ;;
        11)
            show_log_statistics
            ;;
        12)
            clean_logs
            ;;
        13)
            export_logs
            ;;
        0)
            clear
            echo -e "${GREEN}Arrivederci!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}✗ Scelta non valida${NC}"
            sleep 1
            ;;
    esac
done