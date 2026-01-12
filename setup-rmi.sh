#!/bin/bash


#==============================================================================
# Script di Setup RMI per Hotel Colossus
# Descrizione: Configura l'ambiente RMI e crea le directory necessarie
#==============================================================================


set -e  # Exit on error


# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color


# Variabili configurazione
RMI_PORT=1099
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BIN_DIR="$PROJECT_ROOT/bin"
LIB_DIR="$PROJECT_ROOT/lib"
LOGS_DIR="$PROJECT_ROOT/logs"
PID_DIR="$PROJECT_ROOT/pids"
CONFIG_FILE="$PROJECT_ROOT/rmi.properties"


echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
echo -e "${BLUE}     Hotel Colossus - RMI Setup Script             ${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════${NC}"
echo ""

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


# Crea directory necessarie
print_info "Creazione directory necessarie..."


mkdir -p "$BIN_DIR"
mkdir -p "$LIB_DIR"
mkdir -p "$LOGS_DIR"
mkdir -p "$PID_DIR"


print_success "Directory create: bin/, lib/, logs/, pids/"


# Crea file di configurazione
print_info "Creazione file di configurazione RMI..."


cat > "$CONFIG_FILE" << EOF
# RMI Configuration for Hotel Colossus
# Generated: $(date)

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
db.password=1uno2due

# Services
service.frontdesk.name=GestionePrenotazioni
service.governante.name=GestoreCamere
service.manager.name=GestioneImpiegati
EOF


print_success "File di configurazione creato: $CONFIG_FILE"


# Controlla se la porta RMI è disponibile
print_info "Verifica disponibilità porta $RMI_PORT..."

if lsof -Pi :$RMI_PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
    print_warning "Porta $RMI_PORT già in uso!"
    echo -e "   Processo: $(lsof -Pi :$RMI_PORT -sTCP:LISTEN | tail -n 1)"
    echo -e "   Eseguire: ${YELLOW}./stop-rmi.sh${NC} per terminare i servizi RMI"
else
    print_success "Porta $RMI_PORT disponibile"
fi


# Verifica compilazione classi
print_info "Verifica compilazione progetto..."


if [ -d "target/classes" ]; then
    print_success "Classi trovate in target/classes"
    CLASSPATH="$PROJECT_ROOT/target/classes"
elif [ -d "out/production" ]; then
    print_success "Classi trovate in out/production"
    CLASSPATH="$PROJECT_ROOT/out/production"
elif [ -d "$BIN_DIR" ]; then
    print_warning "Classi in bin/ - assicurarsi che siano aggiornate"
    CLASSPATH="$BIN_DIR"
else
    print_error "Classi non trovate! Compilare il progetto prima."
    echo -e "   ${YELLOW}Eseguire: mvn compile${NC} oppure compilare manualmente"
    exit 1
fi


# Aggiorna .gitignore
print_info "Aggiornamento .gitignore..."

if [ ! -f ".gitignore" ]; then
    touch .gitignore
fi


# Aggiungi entries se non presenti
grep -qxF 'logs/' .gitignore || echo 'logs/' >> .gitignore
grep -qxF 'pids/' .gitignore || echo 'pids/' >> .gitignore
grep -qxF '*.log' .gitignore || echo '*.log' >> .gitignore


print_success ".gitignore aggiornato"


# Crea script di ambiente
print_info "Creazione script di ambiente..."


cat > "$PROJECT_ROOT/env.sh" << EOF
#!/bin/bash
# Environment variables for RMI services

export RMI_PORT=$RMI_PORT
export RMI_HOST=localhost
export PROJECT_ROOT="$PROJECT_ROOT"
export CLASSPATH="$CLASSPATH"
export LOGS_DIR="$LOGS_DIR"
export PID_DIR="$PID_DIR"

# MySQL JDBC Driver (aggiungere se necessario)
# export CLASSPATH="\$CLASSPATH:$LIB_DIR/mysql-connector-java.jar"

# Bcrypt library
# export CLASSPATH="\$CLASSPATH:$LIB_DIR/bcrypt.jar"

# Java RMI options
export JAVA_OPTS="-Djava.rmi.server.hostname=localhost"
export JAVA_OPTS="\$JAVA_OPTS -Djava.security.policy=$PROJECT_ROOT/rmi.policy"
export JAVA_OPTS="\$JAVA_OPTS -Djava.rmi.server.codebase=file://$CLASSPATH/"

echo "✓ Environment variables loaded"
EOF


chmod +x "$PROJECT_ROOT/env.sh"
print_success "Script ambiente creato: env.sh"


# Verifica Java
print_info "Verifica installazione Java..."


if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    print_success "Java trovato: $JAVA_VERSION"
else
    print_error "Java non trovato! Installare JDK 11 o superiore"
    exit 1
fi


# Summary
echo ""
echo -e "${GREEN}════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}     Setup completato con successo!                 ${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo ""


print_info "Prossimi passi:"
echo -e "  1. ${YELLOW}./start-rmi.sh${NC}        - Avvia tutti i servizi RMI"
echo -e "  2. ${YELLOW}./status-rmi.sh${NC}       - Verifica stato servizi"
echo -e "  3. ${YELLOW}./stop-rmi.sh${NC}         - Ferma tutti i servizi"
echo ""


print_info "File creati:"
echo -e "  • rmi.properties    - Configurazione"
echo -e "  • env.sh            - Variabili ambiente"
echo -e "  • logs/             - Directory log"
echo -e "  • pids/             - File PID processi"
echo ""