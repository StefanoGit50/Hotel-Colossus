=========================================
       HOTEL COLOSSUS - RMI SCRIPTS
=========================================

Questo pacchetto contiene gli script necessari per la gestione delle
Boundary Condition (Condizioni al Contorno) del sistema Hotel Colossus,
come specificato nel documento SDD v1.1.

Script:
------------------------------------------------------------------------

1. SETUP E INIZIALIZZAZIONE (UC1: Startup del sistema)
   File: setup-rmi.sh / setup-rmi.ps1
   Descrizione: Risolvono la condizione di "Primo Avvio". Gestiscono la
   creazione delle directory di runtime (logs, pids) e la generazione
   del file di policy e dell'ambiente necessari all'esecuzione.
    setup-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
    setup-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).

2. AVVIO DEI SERVIZI (UC1: Startup del sistema)
   File: startup-rmi.sh / start-rmi.ps1
   (SI CONSIGLIA SEMPRE L'AVVIO DI setup-rmi.sh/setup-rmi.ps1 PRIMA DELL'AVVIO)
   Descrizione: Gestiscono la corretta sequenza di attivazione dei
   sottosistemi (Registry -> FrontDesk -> Governante -> Manager)
   garantendo che le dipendenze RMI siano soddisfatte all'avvio.
   startup-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
   start-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).

3. ARRESTO E RIAVVIO (UC2: Spegnimento del sistema)
   File: stop-rmi.sh / stop-rmi.ps1
   File: restart-rmi.sh / restart-rmi.ps1
   Descrizione: Gestiscono la terminazione pulita (graceful shutdown)
   dei processi RMI, assicurando il rilascio delle risorse e la
   pulizia dei file PID per evitare stati di inconsistenza.
   stop-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
   stop-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).

4. configurazione (uc3)

5. MANUTENZIONE PROGRAMMATA (UC4: Avviso di manutenzione al sistema)
   File: maintenance-rmi.sh / maintenance-rmi.ps1
   Descrizione: Gestisce la condizione di "Manutenzione", fornendo
   strumenti per il backup, il countdown di avviso agli utenti e lo
   spegnimento controllato dei servizi.
   maintenance-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
   maintenance-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).

6. MONITORAGGIO E LOGGING (UC6: Monitoraggio Sistema)
   File: status-rmi.sh / status-rmi.ps1
   File: logs-rmi.sh / logs-rmi.ps1
   Descrizione: Gestiscono la boundary condition relativa alla verifica
   dello stato di salute dei processi e dei log.
   status-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
   status-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).
   logs-rmi.sh: Da utilizzare in ambienti Linux/Unix (Bash).
   logs-rmi.ps1: Da utilizzare in ambienti Windows (PowerShell).

------------------------------------------------------------------------
Nota: Per ogni script Linux (.sh) esiste una controparte Windows (.ps1)
con logica equivalente per assicurare che le boundary condition siano
risolte indipendentemente dal sistema operativo ospite.
========================================================================