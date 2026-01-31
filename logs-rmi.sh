#!/bin/bash


#==============================================================================
# Script di Visualizzazione Log RMI per Hotel Colossus
# Descrizione: Visualizza i log dei servizi RMI in tempo reale con opzioni
#              avanzate di filtraggio e ricerca
# Autore: Hotel Colossus Team
#==============================================================================


# NON uscire in caso di errori - gestiamo tutto manualmente
set +e


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


# =============================
# GESTIRE CTRL+C
# ===================================

# Variabile globale per il PID del processo tail
TAIL_PID=""

# Funzione chiamata quando si preme Ctrl+C
handle_interrupt()
{
    if [ -n "$TAIL_PID" ]; then
        kill $TAIL_PID 2>/dev/null
        wait $TAIL_PID 2>/dev/null
        TAIL_PID=""
    fi
    echo ""
    echo -e "${YELLOW}Tornando al menu...${NC}"
    sleep 1
}

# Imposta il trap per SIGINT (Ctrl+C)
trap handle_interrupt SIGINT


# ==================================
# FUNZIONI DI UTILITÀ
# ========================================


print_header()
{
    clear
    echo -e "${BLUE}============================================================${NC}"
    echo -e "${BLUE}     Hotel Colossus - Visualizzazione Log          ${NC}"
    echo -e "${BLUE}============================================================${NC}"
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
    echo -e "  ${GREEN}5)${NC} Maintenance (manutenzione)"
    echo ""
    echo -e "${CYAN}Visualizzazione multipla:${NC}"
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
    echo -e "  ${MAGENTA}14)${NC} Mostra file log disponibili"
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
        echo -ne "${WHITE}Premi INVIO per tornare al menu...${NC}"
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
        local modified=$(stat -c "%y" "$log_file" 2>/dev/null | cut -d'.' -f1 || stat -f "%Sm" -t "%Y-%m-%d %H:%M:%S" "$log_file" 2>/dev/null)

        echo -e "${GRAY}----------------------------------------------------${NC}"
        echo -e "${GRAY}|${NC} ${CYAN}Servizio:${NC} $service_name"
        echo -e "${GRAY}|${NC} ${CYAN}File:${NC}     $log_file"
        echo -e "${GRAY}|${NC} ${CYAN}Dimensione:${NC} $size (${lines} righe)"
        echo -e "${GRAY}|${NC} ${CYAN}Modificato:${NC} $modified"
        echo -e "${GRAY}----------------------------------------------------${NC}"
        echo ""
    fi
}


# Torna al menu
return_to_menu()
{
    echo ""
    echo -ne "${WHITE}Premi INVIO per tornare al menu...${NC}"
    read
}


# ============================================================================
# FUNZIONI DI VISUALIZZAZIONE
# ============================================================================


# Visualizza log singolo con tail -f e colorazione
view_log_single()
{
    local service_name="$1"
    local log_file="$LOGS_DIR/${service_name}"

    # Se non ha estensione, aggiungi .log
    if [[ ! "$log_file" =~ \.(log|notify)$ ]]; then
        log_file="${log_file}.log"
    fi

    print_header
    show_file_info "$log_file" "$service_name"

    check_log_exists "$log_file" || return

    echo -e "${GREEN}▶ Visualizzazione log in tempo reale${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per tornare al menu)${NC}"
    echo ""
    echo -e "${GRAY}============================================================${NC}"
    echo ""

    # Tail con colorazione automatica
    tail -f "$log_file" 2>/dev/null | while IFS= read -r line; do
        # Colora in base al livello di log
        if echo "$line" | grep -qi "error\|exception\|failed"; then
            echo -e "${RED}$line${NC}"
        elif echo "$line" | grep -qi "warning\|warn"; then
            echo -e "${YELLOW}$line${NC}"
        elif echo "$line" | grep -qi "info"; then
            echo -e "${GREEN}$line${NC}"
        elif echo "$line" | grep -qi "debug"; then
            echo -e "${GRAY}$line${NC}"
        elif echo "$line" | grep -qi "success\|completed"; then
            echo -e "${CYAN}$line${NC}"
        else
            echo "$line"
        fi
    done &

    TAIL_PID=$!
    wait $TAIL_PID 2>/dev/null
    TAIL_PID=""
}


# Visualizza tutti i log interleaved
view_all_logs_interleaved()
{
    print_header

    echo -e "${GREEN}▶ Visualizzazione tutti i log (interleaved)${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per tornare al menu)${NC}"
    echo ""

    # Usa tail multipli con prefix
    tail -f \
        "$LOGS_DIR/rmiregistry.log" \
        "$LOGS_DIR/FrontDesk.log" \
        "$LOGS_DIR/Governante.log" \
        "$LOGS_DIR/maintenance.notify" \
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
        elif echo "$line" | grep -q "maintenance"; then
            echo -e "${CYAN}[MAINTENANCE]${NC} $line"
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
    done &

    TAIL_PID=$!
    wait $TAIL_PID 2>/dev/null
    TAIL_PID=""
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
        return_to_menu
        return
    fi

    echo ""
    echo -e "${GREEN}▶ Ricerca di '${search_term}' in tutti i log...${NC}"
    echo ""

    local found=0

    for log in "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify "$LOGS_DIR"/*.status; do
        if [ -f "$log" ]; then
            local matches=$(grep -i "$search_term" "$log" 2>/dev/null)

            if [ -n "$matches" ]; then
                found=1
                echo -e "${CYAN}=== $(basename $log) ===${NC}"
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

    return_to_menu
}


# Filtra per livello di log
filter_by_level()
{
    local level="$1"
    local level_name="$2"
    local color="$3"

    print_header

    echo -e "${color}▶ Visualizzazione ${level_name}${NC}"
    echo -e "${YELLOW}  (Premi Ctrl+C per tornare al menu)${NC}"
    echo ""

    # Tail tutti i log e filtra per livello
    tail -f "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify 2>/dev/null | \
    grep -i "$level" | \
    while IFS= read -r line; do
        echo -e "${color}$line${NC}"
    done &

    TAIL_PID=$!
    wait $TAIL_PID 2>/dev/null
    TAIL_PID=""
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


# ===========================================
# FUNZIONI UTILITÀ
# ====================================


# Mostra file log disponibili
show_log_files()
{
    while true; do
        print_header

        echo -e "${CYAN}File di Log Disponibili${NC}"
        echo ""

        local count=0
        declare -a log_files

        for log in "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify "$LOGS_DIR"/*.status; do
            if [ -f "$log" ]; then
                ((count++))
                log_files[$count]="$log"

                local name=$(basename "$log")
                local size=$(du -h "$log" | cut -f1)
                local lines=$(wc -l < "$log")
                local modified=$(stat -c "%y" "$log" 2>/dev/null | cut -d'.' -f1 || stat -f "%Sm" -t "%Y-%m-%d %H:%M:%S" "$log" 2>/dev/null)

                echo -e "${WHITE}[$count]${NC} ${GREEN}$name${NC}"
                echo -e "    Path:       $log"
                echo -e "    Dimensione: $size ($lines righe)"
                echo -e "    Modificato: $modified"
                echo ""
            fi
        done

        if [ $count -eq 0 ]; then
            echo -e "${YELLOW}Nessun file di log trovato${NC}"
            echo ""
            return_to_menu
            return
        fi

        echo -e "${CYAN}Totale file: $count${NC}"
        echo ""
        echo -e "${YELLOW}Opzioni:${NC}"
        echo -e "  ${GREEN}1-$count)${NC} Visualizza contenuto file"
        echo -e "  ${RED}0)${NC} Torna al menu principale"
        echo ""
        echo -ne "${WHITE}Scelta: ${NC}"

        read file_choice

        # Torna al menu principale
        if [ "$file_choice" = "0" ]; then
            return
        fi

        # Verifica che sia un numero valido
        if ! [[ "$file_choice" =~ ^[0-9]+$ ]] || [ "$file_choice" -lt 1 ] || [ "$file_choice" -gt $count ]; then
            echo -e "${RED}✗ Scelta non valida${NC}"
            sleep 1
            continue
        fi

        # Visualizza il file selezionato
        display_log_file "${log_files[$file_choice]}"
    done
}

# Visualizza il contenuto di un file di log
display_log_file()
{
    local log_file="$1"
    local log_name=$(basename "$log_file")

    print_header

    echo -e "${CYAN}Contenuto di: ${GREEN}$log_name${NC}"
    echo -e "${GRAY}Path: $log_file${NC}"
    echo ""

    local size=$(du -h "$log_file" | cut -f1)
    local lines=$(wc -l < "$log_file")

    echo -e "${CYAN}Dimensione:${NC} $size (${lines} righe)"
    echo ""
    echo -e "${YELLOW}Modalità di visualizzazione:${NC}"
    echo -e "  ${GREEN}1)${NC} Visualizza tutto (cat)"
    echo -e "  ${GREEN}2)${NC} Visualizza in tempo reale (tail -f)"
    echo -e "  ${GREEN}3)${NC} Visualizza con scorrimento (less)"
    echo -e "  ${RED}0)${NC} Torna alla lista file"
    echo ""
    echo -ne "${WHITE}Scelta: ${NC}"

    read view_choice

    case $view_choice in
        1)
            # Visualizza tutto il contenuto
            print_header
            echo -e "${CYAN}=== Contenuto completo di $log_name ===${NC}"
            echo ""
            echo -e "${GRAY}----------------------------------------------------${NC}"

            cat "$log_file" | while IFS= read -r line; do
                # Colora in base al contenuto
                if echo "$line" | grep -qi "error\|exception\|failed"; then
                    echo -e "${RED}$line${NC}"
                elif echo "$line" | grep -qi "warning\|warn"; then
                    echo -e "${YELLOW}$line${NC}"
                elif echo "$line" | grep -qi "info"; then
                    echo -e "${GREEN}$line${NC}"
                elif echo "$line" | grep -qi "success\|completed"; then
                    echo -e "${CYAN}$line${NC}"
                else
                    echo "$line"
                fi
            done

            echo ""
            echo -e "${GRAY}----------------------------------------------------${NC}"
            echo ""
            echo -ne "${WHITE}Premi INVIO per tornare...${NC}"
            read
            ;;

        2)
            # Visualizza in tempo reale
            print_header
            echo -e "${CYAN}=== Visualizzazione in tempo reale di $log_name ===${NC}"
            echo -e "${YELLOW}(Premi Ctrl+C per tornare)${NC}"
            echo ""
            echo -e "${GRAY}----------------------------------------------------${NC}"
            echo ""

            tail -f "$log_file" 2>/dev/null | while IFS= read -r line; do
                if echo "$line" | grep -qi "error\|exception\|failed"; then
                    echo -e "${RED}$line${NC}"
                elif echo "$line" | grep -qi "warning\|warn"; then
                    echo -e "${YELLOW}$line${NC}"
                elif echo "$line" | grep -qi "info"; then
                    echo -e "${GREEN}$line${NC}"
                else
                    echo "$line"
                fi
            done &

            TAIL_PID=$!
            wait $TAIL_PID 2>/dev/null
            TAIL_PID=""
            ;;

        3)
            # Visualizza con less
            less "$log_file"
            ;;

        0)
            # Torna alla lista
            return
            ;;

        *)
            echo -e "${RED}✗ Scelta non valida${NC}"
            sleep 1
            ;;
    esac
}


# Statistiche log
show_log_statistics()
{
    print_header

    echo -e "${CYAN}Statistiche Log${NC}"
    echo ""

    for log in "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify; do
        if [ -f "$log" ]; then
            local name=$(basename "$log" .log)
            name=$(basename "$name" .notify)
            local size=$(du -h "$log" | cut -f1)
            local lines=$(wc -l < "$log")
            local errors=$(grep -ci "error\|exception" "$log" 2>/dev/null || echo 0)
            local warnings=$(grep -ci "warning\|warn" "$log" 2>/dev/null || echo 0)
            local infos=$(grep -ci "info" "$log" 2>/dev/null || echo 0)

            echo -e "${WHITE}=== $name ===${NC}"
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

    return_to_menu
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

        for log in "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify; do
            if [ -f "$log" ]; then
                cp "$log" "$backup_dir/" 2>/dev/null
            fi
        done

        echo -e "${GREEN}✓ Backup creato in: $backup_dir${NC}"

        # Svuota i log (non cancellare, per evitare problemi con servizi attivi)
        for log in "$LOGS_DIR"/*.log "$LOGS_DIR"/*.notify; do
            if [ -f "$log" ]; then
                > "$log"  # Truncate file
                echo -e "${GREEN}✓ Pulito: $(basename $log)${NC}"
            fi
        done

        echo ""
        echo -e "${GREEN}✓ Pulizia completata!${NC}"
    else
        echo -e "${YELLOW}Operazione annullata${NC}"
    fi

    return_to_menu
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

    return_to_menu
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
            view_log_single "maintenance.notify"
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
        14)
            show_log_files
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