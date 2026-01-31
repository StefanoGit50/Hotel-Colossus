#!/bin/bash

#==============================================================================

set +e

# Carica environment
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ===========================================
# CONFIGURAZIONE COLORI
# ===============================================

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
GRAY='\033[0;90m'
NC='\033[0m'

# ==================================================
# CONFIGURAZIONE FILE
# ===========================================

CONFIG_FILE="rmi.properties"
ENV_FILE="env.sh"
TEMP_FILE="/tmp/rmi_config_temp.$$"

# ================================================
# FUNZIONI UTILITY
# ============================================

clear_screen()
{
    clear
}

print_header()
{
    local title="$1"
    clear_screen
    echo -e "${BLUE}============================================================${NC}"
    echo -e "${BLUE}     $title${NC}"
    echo -e "${BLUE}============================================================${NC}"
    echo ""
}

print_info()
{
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success()
{
    echo -e "${GREEN}[OK]${NC} $1"
}

print_warning()
{
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error()
{
    echo -e "${RED}[ERROR]${NC} $1"
}

return_to_menu()
{
    echo ""
    echo -ne "${WHITE}Premi INVIO per continuare...${NC}"
    read
}

# ==============================================
# FUNZIONI DI CARICAMENTO E SALVATAGGIO
# =============================================

load_configuration()
{
    if [ ! -f "$CONFIG_FILE" ]
    then
        print_warning "File di configurazione non trovato: $CONFIG_FILE"
        create_default_configuration
        return 1
    fi

    print_success "Configurazione caricata: $CONFIG_FILE"
    return 0
}

create_default_configuration()
{
    print_info "Creazione configurazione predefinita..."

    cat > "$CONFIG_FILE" << 'EOF'
# Hotel Colossus Configuration
# Auto-generated configuration file

# RMI Registry Configuration
rmi.registry.port=1099
rmi.registry.host=localhost
rmi.server.hostname=localhost

# RMI Connection Settings
rmi.connection.timeout=10000
rmi.read.timeout=5000

# Logging Configuration
log.level=INFO
log.directory=logs

# Database Configuration
db.host=localhost
db.port=3306
db.name=hotelcolossus
db.user=root
db.password=

# Service Names
service.frontdesk.name=FrontDeskService
service.governante.name=GovernanteService
service.manager.name=ManagerService
EOF

    if [ $? -eq 0 ]
    then
        print_success "Configurazione predefinita creata"
        generate_env_script
        return 0
    else
        print_error "Impossibile creare configurazione predefinita"
        return 1
    fi
}

save_configuration()
{
    local backup_file="${CONFIG_FILE}.backup.$(date +%Y%m%d_%H%M%S)"

    # Backup della configurazione attuale
    if [ -f "$CONFIG_FILE" ]
    then
        cp "$CONFIG_FILE" "$backup_file"
        print_info "Backup salvato: $backup_file"
    fi

    # Salva le modifiche
    if [ -f "$TEMP_FILE" ]
    then
        mv "$TEMP_FILE" "$CONFIG_FILE"
        print_success "Configurazione salvata: $CONFIG_FILE"

        # Rigenera il file env.sh
        generate_env_script
        return 0
    else
        print_error "File temporaneo non trovato"
        return 1
    fi
}

# ===========================================
# GENERAZIONE ENV.SH
# ==============================================

generate_env_script()
{
    print_info "Generazione file env.sh..."

    cat > "$ENV_FILE" << 'ENVEOF'
#!/bin/bash
# Environment variables for RMI services
# Auto-generated from rmi.properties
ENVEOF

    echo "# Generated: $(date)" >> "$ENV_FILE"
    echo "" >> "$ENV_FILE"

    # Directory paths
    cat >> "$ENV_FILE" << 'ENVEOF'
# Directory paths
export PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export LOGS_DIR="${LOGS_DIR:-$PROJECT_ROOT/logs}"
export PID_DIR="$PROJECT_ROOT/pids"

ENVEOF

    # Leggi il file properties e converti in variabili bash
    echo "# Configuration from rmi.properties" >> "$ENV_FILE"

    while IFS='=' read -r key value
    do
        # Salta commenti e righe vuote
        if [[ "$key" =~ ^#.*$ ]] || [ -z "$key" ]
        then
            continue
        fi

        # Rimuovi spazi bianchi
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        # Converti chiave in formato variabile bash
        local env_var=$(convert_property_to_env "$key")

        if [ -n "$env_var" ]
        then
            # Se il valore contiene spazi o caratteri speciali, usa virgolette
            if [[ "$value" =~ [[:space:]] ]] || [[ "$value" =~ [\$] ]]
            then
                echo "export $env_var=\"$value\"" >> "$ENV_FILE"
            else
                echo "export $env_var=$value" >> "$ENV_FILE"
            fi
        fi

    done < "$CONFIG_FILE"

    # Aggiungi configurazioni aggiuntive
    cat >> "$ENV_FILE" << 'ENVEOF'

# Classpath configuration
if [ -d "target/classes" ]; then
    export CLASSPATH="$PROJECT_ROOT/target/classes"
elif [ -d "out/production" ]; then
    export CLASSPATH="$PROJECT_ROOT/out/production"
elif [ -d "bin" ]; then
    export CLASSPATH="$PROJECT_ROOT/bin"
fi

# Add libraries to classpath if present
if [ -d "$PROJECT_ROOT/lib" ]; then
    for jar in "$PROJECT_ROOT/lib"/*.jar; do
        export CLASSPATH="$CLASSPATH:$jar"
    done
fi

# Java RMI options
export JAVA_OPTS="-Djava.rmi.server.hostname=${RMI_SERVER_HOSTNAME:-localhost}"
export JAVA_OPTS="$JAVA_OPTS -Djava.security.policy=$PROJECT_ROOT/rmi.policy"
export JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.codebase=file://$CLASSPATH/"

echo "✓ Environment variables loaded from rmi.properties"
ENVEOF

    chmod +x "$ENV_FILE"
    print_success "File env.sh generato con successo"
}

convert_property_to_env()
{
    local prop_key="$1"
    local env_var=""

    case "$prop_key" in
        "rmi.registry.port")
            env_var="RMI_PORT"
            ;;
        "rmi.registry.host")
            env_var="RMI_HOST"
            ;;
        "rmi.server.hostname")
            env_var="RMI_SERVER_HOSTNAME"
            ;;
        "rmi.connection.timeout")
            env_var="RMI_CONNECTION_TIMEOUT"
            ;;
        "rmi.read.timeout")
            env_var="RMI_READ_TIMEOUT"
            ;;
        "log.level")
            env_var="LOG_LEVEL"
            ;;
        "log.directory")
            env_var="LOGS_DIR"
            ;;
        "db.host")
            env_var="DB_HOST"
            ;;
        "db.port")
            env_var="DB_PORT"
            ;;
        "db.name")
            env_var="DB_NAME"
            ;;
        "db.user")
            env_var="DB_USER"
            ;;
        "db.password")
            env_var="DB_PASSWORD"
            ;;
        "service.frontdesk.name")
            env_var="SERVICE_FRONTDESK_NAME"
            ;;
        "service.governante.name")
            env_var="SERVICE_GOVERNANTE_NAME"
            ;;
        "service.manager.name")
            env_var="SERVICE_MANAGER_NAME"
            ;;
    esac

    echo "$env_var"
}

# ==============================================
# VISUALIZZAZIONE CONFIGURAZIONE
# ============================================

show_current_configuration()
{
    print_header "Configurazione Attuale"

    print_info "Configurazione corrente:"
    echo -e "${GRAY}------------------------------------------------------------${NC}"

    # Leggi e mostra tutte le configurazioni ordinate
    local count=0

    while IFS='=' read -r key value
    do
        # Salta commenti e righe vuote
        if [[ "$key" =~ ^#.*$ ]] || [ -z "$key" ]
        then
            continue
        fi

        # Rimuovi spazi bianchi
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        ((count++))
        echo -e "${GRAY}|${NC}" "${CYAN}$key${NC}" "= $value"

    done < <(sort "$CONFIG_FILE" | grep -v '^#' | grep -v '^$')

    echo -e "${GRAY}------------------------------------------------------------${NC}"
    echo ""
    echo -e "${CYAN}Totale parametri: $count${NC}"
    echo ""
}

# ==================================================
# MODIFICA CONFIGURAZIONE - FUNZIONI HELPER
# =====================================================

get_config_value()
{
    local key="$1"
    local value=""

    while IFS='=' read -r config_key config_value
    do
        # Rimuovi spazi
        config_key=$(echo "$config_key" | xargs)

        if [ "$config_key" = "$key" ]
        then
            value=$(echo "$config_value" | xargs)
            break
        fi
    done < "$CONFIG_FILE"

    echo "$value"
}

set_config_value()
{
    local key="$1"
    local new_value="$2"
    local found=0

    # Crea file temporaneo
    > "$TEMP_FILE"

    while IFS= read -r line
    do
        # Se è un commento o riga vuota, copia così com'è
        if [[ "$line" =~ ^#.*$ ]] || [ -z "$line" ]
        then
            echo "$line" >> "$TEMP_FILE"
            continue
        fi

        # Estrai chiave
        local current_key=$(echo "$line" | cut -d'=' -f1 | xargs)

        if [ "$current_key" = "$key" ]
        then
            # Sostituisci con nuovo valore
            echo "$key=$new_value" >> "$TEMP_FILE"
            found=1
        else
            # Mantieni riga originale
            echo "$line" >> "$TEMP_FILE"
        fi

    done < "$CONFIG_FILE"

    if [ $found -eq 0 ]
    then
        # Chiave non trovata, aggiungila
        echo "$key=$new_value" >> "$TEMP_FILE"
    fi

    return 0
}

validate_config_value()
{
    local key="$1"
    local value="$2"

    # Validazione base: non vuoto
    if [ -z "$value" ]
    then
        return 1
    fi

    # Validazioni specifiche per tipo
    case "$key" in
        *port*)
            # Deve essere un numero tra 1 e 65535
            if ! [[ "$value" =~ ^[0-9]+$ ]]
            then
                print_error "Il valore deve essere un numero"
                return 1
            fi

            if [ "$value" -lt 1 ] || [ "$value" -gt 65535 ]
            then
                print_error "Il valore deve essere tra 1 e 65535"
                return 1
            fi
            ;;

        *timeout*)
            # Deve essere un numero positivo
            if ! [[ "$value" =~ ^[0-9]+$ ]]
            then
                print_error "Il valore deve essere un numero"
                return 1
            fi

            if [ "$value" -le 0 ]
            then
                print_error "Il valore deve essere maggiore di 0"
                return 1
            fi
            ;;

        *host*)
            # Validazione base hostname/IP
            if ! [[ "$value" =~ ^[a-zA-Z0-9.-]+$ ]]
            then
                print_error "Il valore contiene caratteri non validi"
                return 1
            fi
            ;;
    esac

    return 0
}

# ============================================
# FUNZIONE MIGLIORATA: RICERCA PARAMETRI CON SELEZIONE
# ===========================================

find_matching_parameters()
{
    local search_term="$1"
    local -n results_ref=$2  # reference to array
    local -n values_ref=$3   # reference to array

    results_ref=()
    values_ref=()

    while IFS='=' read -r key value
    do
        # Salta commenti e righe vuote
        if [[ "$key" =~ ^#.*$ ]] || [ -z "$key" ]
        then
            continue
        fi

        # Rimuovi spazi
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        # Cerca nel nome della chiave (case insensitive)
        if echo "$key" | grep -qi "$search_term"
        then
            results_ref+=("$key")
            values_ref+=("$value")
        fi

    done < "$CONFIG_FILE"
}

# ==================================
# MODIFICA PARAMETRO
# =======================================

modify_parameter()
{
    print_header "Modifica Parametro"

    echo -ne "${WHITE}Inserisci il nome (o parte del nome) del parametro: ${NC}"
    read search_term

    # Rimuovi spazi
    search_term=$(echo "$search_term" | xargs)

    if [ -z "$search_term" ]
    then
        print_error "Nome parametro vuoto"
        return_to_menu
        return 1
    fi

    # Cerca parametri corrispondenti
    declare -a matching_keys
    declare -a matching_values

    find_matching_parameters "$search_term" matching_keys matching_values

    local num_matches=${#matching_keys[@]}

    if [ $num_matches -eq 0 ]
    then
        print_error "Nessun parametro trovato per '$search_term'"
        echo ""
        echo -e "${YELLOW}Vuoi crearlo come nuovo parametro? (yes/no)${NC}"
        read -p "> " create_new

        if [ "$create_new" = "yes" ]
        then
            # Crea nuovo parametro con il nome esatto inserito
            echo ""
            echo -ne "${WHITE}Inserisci il valore per il nuovo parametro '$search_term': ${NC}"
            read new_value

            new_value=$(echo "$new_value" | xargs)

            if [ -n "$new_value" ]
            then
                if set_config_value "$search_term" "$new_value"
                then
                    if save_configuration
                    then
                        print_success "Nuovo parametro '$search_term' creato con successo"
                    fi
                fi
            fi
        fi

        return_to_menu
        return 1
    fi

    # Se trovato un solo match, usalo direttamente
    local param_key
    local old_value

    if [ $num_matches -eq 1 ]
    then
        param_key="${matching_keys[0]}"
        old_value="${matching_values[0]}"

        echo ""
        print_success "Parametro trovato: ${GREEN}$param_key${NC}"
    else
        # Mostra la lista e chiedi all'utente di scegliere
        echo ""
        print_info "Trovati $num_matches parametri corrispondenti:"
        echo ""

        for i in "${!matching_keys[@]}"
        do
            local idx=$((i + 1))
            echo -e "  ${CYAN}$idx)${NC} ${GREEN}${matching_keys[i]}${NC} = ${matching_values[i]}"
        done

        echo ""
        echo -ne "${WHITE}Seleziona il numero del parametro da modificare (1-$num_matches, 0 per annullare): ${NC}"
        read selection

        if ! [[ "$selection" =~ ^[0-9]+$ ]] || [ "$selection" -lt 1 ] || [ "$selection" -gt $num_matches ]
        then
            print_warning "Selezione annullata"
            return_to_menu
            return 1
        fi

        local array_index=$((selection - 1))
        param_key="${matching_keys[$array_index]}"
        old_value="${matching_values[$array_index]}"

        echo ""
        print_success "Parametro selezionato: ${GREEN}$param_key${NC}"
    fi

    echo ""
    echo -e "Valore attuale: ${YELLOW}$old_value${NC}"
    echo ""

    echo -ne "${WHITE}Inserisci il nuovo valore: ${NC}"
    read new_value

    # Rimuovi spazi
    new_value=$(echo "$new_value" | xargs)

    if [ -z "$new_value" ]
    then
        print_error "Valore vuoto non permesso"
        return_to_menu
        return 1
    fi

    # Valida il valore
    if ! validate_config_value "$param_key" "$new_value"
    then
        print_error "Valore non valido per il parametro '$param_key'"
        return_to_menu
        return 1
    fi

    # Conferma modifica
    echo ""
    echo -e "${CYAN}Riepilogo modifica:${NC}"
    echo -e "  Parametro:      ${YELLOW}$param_key${NC}"
    echo -e "  Valore vecchio: $old_value"
    echo -e "  Valore nuovo:   ${GREEN}$new_value${NC}"
    echo ""

    echo -ne "${WHITE}Confermi la modifica? (yes/no): ${NC}"
    read confirm

    if [ "$confirm" != "yes" ]
    then
        print_warning "Modifica annullata"
        return_to_menu
        return 1
    fi

    # Applica la modifica
    if set_config_value "$param_key" "$new_value"
    then
        if save_configuration
        then
            print_success "Parametro '$param_key' aggiornato con successo"
            echo ""
            print_info "Vecchio valore: $old_value"
            print_info "Nuovo valore:   $new_value"
        else
            print_error "Errore durante il salvataggio della configurazione"
        fi
    else
        print_error "Errore durante la modifica del parametro"
    fi

    return_to_menu
}

# ========================
# RICERCA PARAMETRI
# =========================

search_parameters()
{
    print_header "Ricerca Parametri"

    echo -ne "${WHITE}Inserisci termine di ricerca: ${NC}"
    read search_term

    if [ -z "$search_term" ]
    then
        print_error "Termine di ricerca vuoto"
        return_to_menu
        return
    fi

    echo ""
    print_info "Ricerca di '$search_term' nella configurazione..."
    echo ""

    local found=0

    while IFS='=' read -r key value
    do
        # Salta commenti e righe vuote
        if [[ "$key" =~ ^#.*$ ]] || [ -z "$key" ]
        then
            continue
        fi

        # Rimuovi spazi
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        # Cerca nel nome della chiave o nel valore
        if echo "$key" | grep -qi "$search_term" || echo "$value" | grep -qi "$search_term"
        then
            ((found++))
            echo -e "${CYAN}[$found]${NC} ${GREEN}$key${NC} = $value"
        fi

    done < "$CONFIG_FILE"

    if [ $found -eq 0 ]
    then
        print_warning "Nessun parametro trovato per '$search_term'"
    else
        echo ""
        echo -e "${CYAN}Trovati $found parametri${NC}"
    fi

    return_to_menu
}

# ========================================
# ESPORTAZIONE/IMPORTAZIONE
# =========================================

export_configuration()
{
    print_header "Esporta Configurazione"

    local export_dir="exports"
    mkdir -p "$export_dir"

    local timestamp=$(date +%Y%m%d_%H%M%S)
    local export_file="$export_dir/config_$timestamp.properties"

    if cp "$CONFIG_FILE" "$export_file"
    then
        print_success "Configurazione esportata con successo"
        echo ""
        echo -e "  File: ${GREEN}$export_file${NC}"

        local size=$(du -h "$export_file" | cut -f1)
        echo -e "  Dimensione: $size"
    else
        print_error "Errore durante l'esportazione"
    fi

    return_to_menu
}

import_configuration()
{
    print_header "Importa Configurazione"

    echo -ne "${WHITE}Inserisci il path del file da importare: ${NC}"
    read import_file

    if [ ! -f "$import_file" ]
    then
        print_error "File non trovato: $import_file"
        return_to_menu
        return
    fi

    echo ""
    echo -e "${RED}ATTENZIONE: Questa operazione sovrascriverà la configurazione attuale!${NC}"
    echo ""
    echo -ne "${WHITE}Confermi l'importazione? (yes/no): ${NC}"
    read confirm

    if [ "$confirm" != "yes" ]
    then
        print_warning "Importazione annullata"
        return_to_menu
        return
    fi

    # Backup configurazione corrente
    local backup_file="${CONFIG_FILE}.backup.$(date +%Y%m%d_%H%M%S)"
    cp "$CONFIG_FILE" "$backup_file"
    print_info "Backup salvato: $backup_file"

    # Importa
    if cp "$import_file" "$CONFIG_FILE"
    then
        print_success "Configurazione importata con successo"
        generate_env_script
    else
        print_error "Errore durante l'importazione"
    fi

    return_to_menu
}

# ============================
# RESET CONFIGURAZIONE
# ===========================

reset_configuration()
{
    print_header "Reset Configurazione"

    echo -e "${RED}ATTENZIONE: Questa operazione ripristinerà la configurazione predefinita!${NC}"
    echo -e "${RED}Tutti i parametri personalizzati saranno persi.${NC}"
    echo ""
    echo -ne "${WHITE}Confermi il reset? (yes/no): ${NC}"
    read confirm

    if [ "$confirm" != "yes" ]
    then
        print_warning "Reset annullato"
        return_to_menu
        return
    fi

    # Backup configurazione corrente
    local backup_file="${CONFIG_FILE}.backup.$(date +%Y%m%d_%H%M%S)"
    cp "$CONFIG_FILE" "$backup_file"
    print_info "Backup salvato: $backup_file"

    # Crea configurazione predefinita
    if create_default_configuration
    then
        print_success "Configurazione ripristinata ai valori predefiniti"
    else
        print_error "Errore durante il reset"
    fi

    return_to_menu
}

# ==========================
# MENU PRINCIPALE
# =======================

print_menu()
{
    print_header "Hotel Colossus - Configuration Manager"

    echo -e "${CYAN}Visualizzazione:${NC}"
    echo -e "  ${GREEN}1)${NC} Mostra configurazione corrente"
    echo -e "  ${GREEN}2)${NC} Cerca parametri"
    echo ""

    echo -e "${CYAN}Modifica:${NC}"
    echo -e "  ${YELLOW}3)${NC} Modifica parametro"
    echo -e "  ${YELLOW}4)${NC} Reset configurazione"
    echo ""

    echo -e "${CYAN}Import/Export:${NC}"
    echo -e "  ${MAGENTA}5)${NC} Esporta configurazione"
    echo -e "  ${MAGENTA}6)${NC} Importa configurazione"
    echo ""

    echo -e "${CYAN}Utility:${NC}"
    echo -e "  ${BLUE}7)${NC} Rigenera env.sh"
    echo ""

    echo -e "  ${RED}0)${NC} Esci"
    echo ""
    echo -ne "${WHITE}Scelta: ${NC}"
}

# ====================
# MAIN LOOP
# =====================

main()
{
    # Carica configurazione
    load_configuration

    while true
    do
        print_menu
        read choice

        case $choice in
            1)
                show_current_configuration
                return_to_menu
                ;;
            2)
                search_parameters
                ;;
            3)
                modify_parameter
                ;;
            4)
                reset_configuration
                ;;
            5)
                export_configuration
                ;;
            6)
                import_configuration
                ;;
            7)
                print_header "Rigenera env.sh"
                if generate_env_script
                then
                    print_success "File env.sh rigenerato con successo"
                else
                    print_error "Errore durante la rigenerazione"
                fi
                return_to_menu
                ;;
            0)
                clear_screen
                exit 0
                ;;
            *)
                print_error "Scelta non valida"
                sleep 1
                ;;
        esac
    done
}

# Avvia il programma
main