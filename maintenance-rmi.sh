#!/bin/bash


# ==================================================
# Hotel Colossus - Sistema di Manutenzione Programmata
# ======================================================


# Colori per il terminale
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
GRAY='\033[0;90m'
BOLD='\033[1m'
RESET='\033[0m'


# File di configurazione
MAINTENANCE_FILE="maintenance.status"
MAINTENANCE_FLAG="pids/maintenance.flag"
LOG_DIR="logs"
NOTIFY_LOG="$LOG_DIR/maintenance.notify"


# ================================
# FUNZIONI UTILITY
# ================================


# Pulisce lo schermo
clear_screen() {
    clear
}


# Stampa header
print_header()
{
    local title="$1"
    echo -e "${BLUE}============================================================${RESET}"
    echo -e "${BLUE}  ${BOLD}$title${RESET}"
    echo -e "${BLUE}============================================================${RESET}"
    echo ""
}


# Messaggi colorati
print_info()
{
    echo -e "${BLUE}[INFO]${RESET} $1"
}


print_success()
{
    echo -e "${GREEN}[OK]${RESET} $1"
}


print_warning()
{
    echo -e "${YELLOW}[WARN]${RESET} $1"
}


print_error()
{
    echo -e "${RED}[ERROR]${RESET} $1"
}


# ============================
# FUNZIONI PRINCIPALI
# ========================================


# Crea le directory necessarie
setup_directories()
{
    mkdir -p "$LOG_DIR"
    mkdir -p "pids"
    mkdir -p "backups"
}


# Richiede i minuti di manutenzione
get_maintenance_minutes()
{
    while true; do
        echo "" >&2
        echo -e "${CYAN}Inserisci i minuti prima della manutenzione (1-120):${RESET}" >&2
        read -p "> " minutes

        # Verifica che sia un numero
        if ! [[ "$minutes" =~ ^[0-9]+$ ]]; then
            print_error "Devi inserire un numero valido!" >&2
            continue
        fi

        # Verifica il range
        if [ "$minutes" -lt 1 ] || [ "$minutes" -gt 120 ]; then
            print_error "Il valore deve essere tra 1 e 120 minuti!" >&2
            continue
        fi

        # Input valido - stampa SOLO il numero su stdout
        echo "$minutes"
        return 0
    done
}


# Richiede conferma
ask_confirmation()
{
    local minutes=$1

    echo ""
    echo -e "${RED}*** ATTENZIONE ***${RESET}"
    echo -e "  Il sistema andrà in manutenzione tra ${YELLOW}$minutes minuti${RESET}"
    echo -e "  Tutti i servizi riceveranno una notifica"
    echo -e "  Al termine verrà eseguito lo shutdown automatico"
    echo ""

    while true; do
        read -p "Confermi la manutenzione programmata? (yes/no): " confirm

        case "$confirm" in
            yes|YES|y|Y)
                return 0
                ;;
            no|NO|n|N)
                return 1
                ;;
            *)
                print_error "Rispondi 'yes' o 'no'"
                ;;
        esac
    done
}


# Crea il file di stato manutenzione con tutte le informazioni
create_maintenance_status()
{
    local minutes=$1
    local start_time=$(date +%s)
    local shutdown_time=$((start_time + minutes * 60))

    cat > "$MAINTENANCE_FILE" << EOF
# ==============================================================================
# Hotel Colossus - Maintenance Status File
# ==============================================================================
# Questo file contiene tutte le informazioni sulla manutenzione programmata
# I client e i servizi possono leggere questo file per conoscere lo stato
# ==============================================================================

# === INFORMAZIONI GENERALI ===
maintenance.active=true
maintenance.mode=SCHEDULED_SHUTDOWN
maintenance.version=1.0
maintenance.protocol=GRACEFUL_SHUTDOWN

# === TIMING ===
maintenance.scheduled.minutes=$minutes
maintenance.scheduled.seconds=$((minutes * 60))
maintenance.started.timestamp=$start_time
maintenance.started.datetime=$(date -d "@$start_time" "+%Y-%m-%d %H:%M:%S")
maintenance.shutdown.timestamp=$shutdown_time
maintenance.shutdown.datetime=$(date -d "@$shutdown_time" "+%Y-%m-%d %H:%M:%S")
maintenance.timezone=$(date +%Z)
maintenance.timezone.offset=$(date +%z)

# === STATO CORRENTE ===
maintenance.current.status=IN_PROGRESS
maintenance.current.phase=COUNTDOWN
maintenance.remaining.seconds=$((minutes * 60))
maintenance.remaining.minutes=$minutes
maintenance.elapsed.seconds=0
maintenance.elapsed.minutes=0
maintenance.progress.percentage=0

# === MOTIVO E DESCRIZIONE ===
maintenance.reason=Manutenzione programmata del sistema
maintenance.description=Shutdown programmato per aggiornamento e manutenzione ordinaria
maintenance.priority=HIGH
maintenance.type=PLANNED
maintenance.category=SYSTEM_UPDATE

# === AZIONI PROGRAMMATE ===
maintenance.actions.backup=ENABLED
maintenance.actions.graceful_shutdown=ENABLED
maintenance.actions.client_notification=ENABLED
maintenance.actions.data_sync=ENABLED
maintenance.actions.log_rotation=ENABLED

# === BACKUP ===
maintenance.backup.enabled=true
maintenance.backup.location=backups/
maintenance.backup.timestamp=$(date +%Y%m%d_%H%M%S)
maintenance.backup.includes=logs,config,data
maintenance.backup.format=tar.gz
maintenance.backup.compression=gzip

# === SISTEMA ===
maintenance.system.hostname=$(hostname)
maintenance.system.user=$(whoami)
maintenance.system.os=$(uname -s)
maintenance.system.kernel=$(uname -r)
maintenance.system.arch=$(uname -m)
maintenance.system.pid=$$
maintenance.system.ppid=$PPID
maintenance.system.shell=$SHELL

# === MESSAGGI PER UTENTI ===
maintenance.message.it=Sistema in manutenzione programmata. Shutdown tra $minutes minuti.
maintenance.message.en=System under scheduled maintenance. Shutdown in $minutes minutes.
maintenance.message.display=ATTENZIONE: Manutenzione in corso - Salva il tuo lavoro

# === CHECKPOINT ===
maintenance.checkpoint.backup=PENDING
maintenance.checkpoint.shutdown=PENDING
maintenance.checkpoint.cleanup=PENDING

# ==============================================================================
# Fine del file di stato - Ultimo aggiornamento: $(date)
# ==============================================================================
EOF

    print_success "File di stato creato: $MAINTENANCE_FILE"
}


# Crea il file di notifica per i server RMI
create_maintenance_flag()
{
    local minutes=$1

    cat > "$MAINTENANCE_FLAG" << EOF
MAINTENANCE_MODE
MINUTES=$minutes
TIMESTAMP=$(date +%s)
DATETIME=$(date "+%Y-%m-%d %H:%M:%S")
INITIATED_BY=$(whoami)
HOSTNAME=$(hostname)
PID=$$
EOF

    print_success "Flag di manutenzione creato: $MAINTENANCE_FLAG"
}


# Inizializza il log di notifica
init_notification_log()
{
    cat > "$NOTIFY_LOG" << EOF
# ==============================================================================
# Hotel Colossus - Maintenance Notification Log
# Avviato: $(date)
# ==============================================================================

EOF
}


# Aggiunge una notifica al log
log_notification()
{
    local message="$1"
    echo "[$(date "+%Y-%m-%d %H:%M:%S")] $message" >> "$NOTIFY_LOG"
}


# Aggiorna il file di stato con il tempo rimanente
update_maintenance_status()
{
    local remaining_seconds=$1
    local total_seconds=$2
    local elapsed_seconds=$((total_seconds - remaining_seconds))
    local progress=$((elapsed_seconds * 100 / total_seconds))

    # Aggiorna solo le righe del tempo rimanente
    sed -i "s/^maintenance.remaining.seconds=.*/maintenance.remaining.seconds=$remaining_seconds/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.remaining.minutes=.*/maintenance.remaining.minutes=$((remaining_seconds / 60))/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.elapsed.seconds=.*/maintenance.elapsed.seconds=$elapsed_seconds/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.elapsed.minutes=.*/maintenance.elapsed.minutes=$((elapsed_seconds / 60))/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.progress.percentage=.*/maintenance.progress.percentage=$progress/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.current.status=.*/maintenance.current.status=IN_PROGRESS/" "$MAINTENANCE_FILE"
}


# Formatta il tempo in formato leggibile
format_time()
{
    local seconds=$1
    local hours=$((seconds / 3600))
    local minutes=$(((seconds % 3600) / 60))
    local secs=$((seconds % 60))

    printf "%02d:%02d:%02d" $hours $minutes $secs
}


# Invia notifica in base al tempo rimanente
send_notification()
{
    local remaining=$1
    local message=""
    local color=""

    if [ $remaining -ge 300 ]; then
        # >= 5 minuti
        local mins=$((remaining / 60))
        message="Manutenzione tra $mins minuti"
        color="$YELLOW"
    elif [ $remaining -ge 60 ]; then
        # >= 1 minuto
        local mins=$((remaining / 60))
        message="ATTENZIONE: Manutenzione tra $mins minuti!"
        color="$YELLOW"
    elif [ $remaining -ge 30 ]; then
        message="ATTENZIONE: Manutenzione tra $remaining secondi!"
        color="$RED"
    else
        message="SHUTDOWN IMMINENTE: $remaining secondi!"
        color="$RED"
    fi

    # Log della notifica
    log_notification "$message"

    # Incrementa contatore notifiche
    local count=$(grep "^maintenance.notifications.count=" "$MAINTENANCE_FILE" | cut -d= -f2)
    ((count++))
    sed -i "s/^maintenance.notifications.count=.*/maintenance.notifications.count=$count/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.notifications.last.sent=.*/maintenance.notifications.last.sent=$(date +%s)/" "$MAINTENANCE_FILE"

    # Stampa notifica
    echo ""
    echo -e "${color}[!] $message${RESET}"
}


# Disegna la progress bar
draw_progress_bar()
{
    local current=$1
    local total=$2
    local width=50

    local percentage=$((current * 100 / total))
    local filled=$((current * width / total))
    local empty=$((width - filled))

    local bar=""
    for ((i=0; i<filled; i++)); do bar="${bar}#"; done
    for ((i=0; i<empty; i++)); do bar="${bar}-"; done

    echo "$bar"
}


# Countdown principale
run_countdown()
{
    local total_minutes=$1
    local total_seconds=$((total_minutes * 60))
    local remaining=$total_seconds

    # Intervalli di notifica (in secondi)
    local notify_at=(1800 900 600 300 180 60 30 10)

    # Header del countdown
    clear_screen
    print_header "Manutenzione in Corso - Hotel Colossus"

    print_info "Countdown iniziato - Tempo totale: $total_minutes minuti"
    echo -e "${GRAY}------------------------------------------------------------${RESET}"
    echo ""

    while [ $remaining -gt 0 ]; do
        # Verifica se è il momento di notificare
        for interval in "${notify_at[@]}"; do
            if [ $remaining -eq $interval ]; then
                send_notification $remaining
                break
            fi
        done

        # Aggiorna il file di stato
        update_maintenance_status $remaining $total_seconds

        # Calcola il colore in base al tempo rimanente
        local time_color="$GREEN"
        if [ $remaining -le 60 ]; then
            time_color="$RED"
        elif [ $remaining -le 300 ]; then
            time_color="$YELLOW"
        fi

        # Calcola la progress bar
        local elapsed=$((total_seconds - remaining))
        local progress_bar=$(draw_progress_bar $elapsed $total_seconds)
        local percentage=$((elapsed * 100 / total_seconds))

        # Formatta il tempo
        local time_str=$(format_time $remaining)

        # Stampa la riga di countdown (sovrascrive la precedente)
        printf "\r${time_color}Tempo rimanente: %s${RESET} [%s] %3d%%" \
            "$time_str" "$progress_bar" "$percentage"

        sleep 1
        ((remaining--))
    done

    echo ""
    echo ""
}


# Esegue il backup
perform_backup()
{
    print_info "Esecuzione backup pre-manutenzione..."

    local timestamp=$(date +%Y%m%d_%H%M%S)
    local backup_file="backups/maintenance_backup_$timestamp.tar.gz"

    if tar -czf "$backup_file" "$LOG_DIR/" "pids/" "$MAINTENANCE_FILE" 2>/dev/null; then
        print_success "Backup creato: $backup_file"

        # Aggiorna il file di stato
        sed -i "s/^maintenance.checkpoint.backup=.*/maintenance.checkpoint.backup=COMPLETED/" "$MAINTENANCE_FILE"
        sed -i "s|^maintenance.backup.timestamp=.*|maintenance.backup.timestamp=$timestamp|" "$MAINTENANCE_FILE"
    else
        print_warning "Impossibile creare backup completo"
    fi
}


# Esegue lo shutdown
perform_shutdown()
{
    print_info "Avvio procedura di shutdown..."

    # Aggiorna stato
    sed -i "s/^maintenance.current.phase=.*/maintenance.current.phase=SHUTDOWN/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.checkpoint.shutdown=.*/maintenance.checkpoint.shutdown=IN_PROGRESS/" "$MAINTENANCE_FILE"


    bash stop-rmi.sh

    print_success "Shutdown completato"
    sed -i "s/^maintenance.checkpoint.shutdown=.*/maintenance.checkpoint.shutdown=COMPLETED/" "$MAINTENANCE_FILE"
}


# Cleanup finale
cleanup()
{
    print_info "Pulizia file temporanei..."

    # Aggiorna stato finale
    sed -i "s/^maintenance.current.status=.*/maintenance.current.status=COMPLETED/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.current.phase=.*/maintenance.current.phase=CLEANUP/" "$MAINTENANCE_FILE"
    sed -i "s/^maintenance.checkpoint.cleanup=.*/maintenance.checkpoint.cleanup=COMPLETED/" "$MAINTENANCE_FILE"

    # Rimuovi flag (opzionale, può essere utile mantenerlo per audit)
    # rm -f "$MAINTENANCE_FLAG"

    print_success "Cleanup completato"
}


# =================================
# MAIN
# ==============================


main()
{
    clear_screen
    print_header "Hotel Colossus - Sistema di Manutenzione Programmata"

    # Setup
    setup_directories

    # Richiedi minuti
    minutes=$(get_maintenance_minutes)

    # Chiedi conferma
    if ! ask_confirmation "$minutes"; then
        echo ""
        print_warning "Manutenzione annullata dall'utente"
        echo ""
        exit 0
    fi

    echo ""
    print_success "Manutenzione confermata: $minutes minuti"
    echo ""

    # Inizializza i file
    print_info "Inizializzazione sistema di manutenzione..."
    init_notification_log
    create_maintenance_status "$minutes"
    create_maintenance_flag "$minutes"
    echo ""

    # Countdown
    run_countdown "$minutes"

    # Operazioni finali
    echo ""
    print_info "Tempo scaduto! Esecuzione operazioni finali..."
    echo ""

    perform_backup
    echo ""

    perform_shutdown
    echo ""

    cleanup
    echo ""

    # Messaggio finale
    echo -e "${GREEN}============================================================${RESET}"
    echo -e "${GREEN}  ${BOLD}Manutenzione completata con successo${RESET}"
    echo -e "${GREEN}============================================================${RESET}"
    echo ""

    print_info "Log disponibile in: $NOTIFY_LOG"
    print_info "Stato finale in: $MAINTENANCE_FILE"
    echo ""
}


# Avvia lo script
main