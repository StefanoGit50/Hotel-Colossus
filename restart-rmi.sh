#!/bin/bash


#==============================================================================
# Script di Restart RMI per Hotel Colossus
# Descrizione: Riavvia tutti i servizi RMI
#==============================================================================


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
BLUE='\033[0;34m'
NC='\033[0m'


echo -e "${BLUE}════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}     Hotel Colossus - Restart Servizi RMI           ${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════${NC}"
echo ""


# Stop
echo -e "${BLUE}[1/2] Fermo servizi esistenti...${NC}"
"$SCRIPT_DIR/stop-rmi.sh"


echo ""
echo -e "${BLUE}Attendo 3 secondi prima del riavvio...${NC}"
sleep 3
echo ""


# Start
echo -e "${BLUE}[2/2] Avvio servizi...${NC}"
"$SCRIPT_DIR/startup-rmi.sh"


echo ""
echo -e "${BLUE}Restart completato!${NC}"