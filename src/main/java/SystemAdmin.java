
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Sistema di Gestione Centralizzato per Hotel Colossus
 * Gestisce avvio, shutdown, configurazione e logging del sistema RMI
 */

public class SystemAdmin
{

    // Configurazione colori per terminale
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";


    private static final String CONFIG_FILE = "rmi.properties";
    private static final String SCRIPT_DIR = ".";


    private Properties config;
    private Scanner scanner;


    public SystemAdmin()
    {
        scanner = new Scanner(System.in);
        loadConfiguration();
    }


    public static void main(String[] args)
    {
        SystemAdmin manager = new SystemAdmin();
        manager.run();
    }


    /**
     * Loop principale del sistema
     */
    public void run()
    {
        while (true)
        {
            try
            {
                printMainMenu();
                int choice = getIntInput("Scelta");

                switch (choice)
                {
                    case 1:
                        startSystem();
                        break;
                    case 2:
                        shutdownSystem();
                        break;
                    case 3:
                        maintenanceMode();
                        break;
                    case 4:
                        manageLogging();
                        break;
                    case 5:
                        manageConfiguration();
                        break;
                    case 6:
                        showSystemStatus();
                        break;
                    case 0:
                        exitSystem();
                        return;
                    default:
                        printError("Scelta non valida!");
                        pause();
                }
            }
            catch (Exception e)
            {
                printError("Errore: " + e.getMessage());
                e.printStackTrace();
                pause();
            }
        }
    }


    /**
     * Stampa menu principale
     */
    private void printMainMenu()
    {
        clearScreen();
        printHeader("Hotel Colossus - System Manager");

        System.out.println(CYAN + "Gestione Sistema:" + RESET);
        System.out.println("  " + GREEN + "1)" + RESET + " Avvio Sistema (Setup + Startup)");
        System.out.println("  " + GREEN + "2)" + RESET + " Shutdown Sistema");
        System.out.println("  " + GREEN + "3)" + RESET + " Modalità Manutenzione");
        System.out.println();

        System.out.println(CYAN + "Monitoraggio:" + RESET);
        System.out.println("  " + YELLOW + "4)" + RESET + " Gestisci Logging");
        System.out.println("  " + YELLOW + "5)" + RESET + " Modifica Configurazione");
        System.out.println("  " + YELLOW + "6)" + RESET + " Status Sistema");
        System.out.println();

        System.out.println("  " + RED + "0)" + RESET + " Esci");
        System.out.println();
    }


    /**
     * Avvia il sistema completo (setup + startup)
     */
    private void startSystem()
    {
        clearScreen();
        printHeader("Avvio Sistema");

        printInfo("Fase 1/2: Esecuzione setup...");
        if (executeScript("setup-rmi.sh"))
        {
            printSuccess("Setup completato");
            System.out.println();

            printInfo("Fase 2/2: Avvio servizi RMI...");
            if (executeScript("startup-rmi.sh"))
            {
                printSuccess("Sistema avviato con successo!");
            }
            else
            {
                printError("Errore durante l'avvio dei servizi");
            }
        }
        else
        {
            printError("Errore durante il setup");
        }

        pause();
    }


    /**
     * Esegue lo shutdown del sistema
     */
    private void shutdownSystem()
    {
        clearScreen();
        printHeader("Shutdown Sistema");

        System.out.println(RED + "⚠ ATTENZIONE: Questo fermerà tutti i servizi RMI!" + RESET);
        System.out.println();

        System.out.print("Confermi? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if ("yes".equalsIgnoreCase(confirm))
        {
            printInfo("Shutdown in corso...");

            if (executeScript("stop-rmi.sh"))
            {
                printSuccess("Sistema fermato correttamente");
            }
            else
            {
                printError("Errore durante lo shutdown");
            }
        }
        else
        {
            printWarning("Shutdown annullato");
        }

        pause();
    }


    /**
     * Modalità manutenzione programmata
     */
    private void maintenanceMode() {
        clearScreen();
        printHeader("Modalità Manutenzione Programmata");

        System.out.println(YELLOW + "⚙ Questa modalità:" + RESET);
        System.out.println("  • Notifica tutti i client connessi");
        System.out.println("  • Blocca nuove connessioni");
        System.out.println("  • Completa le operazioni in corso");
        System.out.println("  • Esegue shutdown programmato");
        System.out.println();

        // Chiedi minuti per la manutenzione
        int minutes = getIntInput("Tempo prima dello shutdown (minuti)");

        if (minutes <= 0 || minutes > 120) {
            printError("Tempo non valido! Deve essere tra 1 e 120 minuti.");
            pause();
            return;
        }

        System.out.println();
        System.out.println(RED + "⚠ ATTENZIONE:" + RESET);
        System.out.println("  Il sistema andrà in manutenzione tra " + YELLOW + minutes + " minuti" + RESET);
        System.out.println("  Tutti i client riceveranno una notifica");
        System.out.println();

        System.out.print("Confermi? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (!"yes".equalsIgnoreCase(confirm)) {
            printWarning("Manutenzione annullata");
            pause();
            return;
        }

        // Avvia il processo di manutenzione
        scheduleMaintenance(minutes);
    }

    /**
     * Schedula ed esegue la manutenzione programmata
     */
    private void scheduleMaintenance(int minutes) {
        clearScreen();
        printHeader("Manutenzione in Corso");

        try {
            // 1. Crea file di stato manutenzione
            createMaintenanceStatusFile(minutes);
            printSuccess("File di stato manutenzione creato");

            // 2. Notifica i server RMI tramite file flag
            notifyServersMaintenanceMode(minutes);
            printSuccess("Notifica inviata ai server RMI");

            // 3. Conta alla rovescia con notifiche periodiche
            performCountdown(minutes);

            // 4. Shutdown finale
            printInfo("Tempo scaduto! Avvio shutdown...");
            Thread.sleep(2000);

            // Esegui backup prima dello shutdown
            printInfo("Esecuzione backup...");
            performBackup();

            // Shutdown del sistema
            if (executeScript("stop-rmi.sh")) {
                printSuccess("Sistema fermato correttamente");
            } else {
                printError("Errore durante lo shutdown");
            }

            // Cleanup
            cleanupMaintenanceFiles();

        } catch (Exception e) {
            printError("Errore durante la manutenzione: " + e.getMessage());
            cleanupMaintenanceFiles();
        }

        pause();
    }

    /**
     * Crea file di stato manutenzione che i client possono leggere
     */
    private void createMaintenanceStatusFile(int minutes) throws IOException {
        File maintenanceFile = new File("maintenance.status");

        try (PrintWriter writer = new PrintWriter(new FileWriter(maintenanceFile))) {
            long shutdownTime = System.currentTimeMillis() + (minutes * 60 * 1000);

            writer.println("# Hotel Colossus - Maintenance Status");
            writer.println("maintenance.active=true");
            writer.println("maintenance.message=Sistema in manutenzione programmata");
            writer.println("maintenance.shutdown.minutes=" + minutes);
            writer.println("maintenance.shutdown.timestamp=" + shutdownTime);
            writer.println("maintenance.started=" + System.currentTimeMillis());
            writer.println("maintenance.reason=Manutenzione programmata del sistema");
        }
    }

    /**
     * Notifica i server RMI della manutenzione imminente
     */
    private void notifyServersMaintenanceMode(int minutes) throws IOException {
        // Crea file che i server monitorano
        File notifyFile = new File("pids/maintenance.flag");
        notifyFile.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(notifyFile))) {
            writer.println("MAINTENANCE_MODE");
            writer.println("MINUTES=" + minutes);
            writer.println("TIMESTAMP=" + System.currentTimeMillis());
        }

        // Invia anche tramite file di log speciale
        File logNotify = new File("logs/maintenance.notify");
        try (PrintWriter writer = new PrintWriter(new FileWriter(logNotify, true))) {
            writer.println("[" + new Date() + "] MAINTENANCE SCHEDULED: " + minutes + " minutes");
        }
    }

    /**
     * Esegue countdown con notifiche periodiche
     */
    private void performCountdown(int totalMinutes) throws InterruptedException {
        int remainingSeconds = totalMinutes * 60;

        // Intervalli di notifica (in secondi)
        int[] notifyIntervals = {
                totalMinutes * 60,      // Inizio
                30 * 60,                // 30 minuti
                15 * 60,                // 15 minuti
                10 * 60,                // 10 minuti
                5 * 60,                 // 5 minuti
                3 * 60,                 // 3 minuti
                1 * 60,                 // 1 minuto
                30,                     // 30 secondi
                10                      // 10 secondi
        };

        System.out.println();
        printInfo("Countdown iniziato - Tempo totale: " + totalMinutes + " minuti");
        System.out.println(GRAY + "─".repeat(60) + RESET);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (totalMinutes * 60 * 1000);

        while (System.currentTimeMillis() < endTime) {
            long currentTime = System.currentTimeMillis();
            remainingSeconds = (int)((endTime - currentTime) / 1000);

            // Verifica se è il momento di notificare
            for (int interval : notifyIntervals) {
                if (remainingSeconds == interval) {
                    sendMaintenanceNotification(remainingSeconds);
                    break;
                }
            }

            // Aggiorna display ogni secondo
            updateCountdownDisplay(remainingSeconds);

            Thread.sleep(1000);
        }

        System.out.println();
    }

    /**
     * Invia notifica di manutenzione
     */
    private void sendMaintenanceNotification(int remainingSeconds) {
        try {
            String message;
            String color;

            if (remainingSeconds >= 300) { // >= 5 minuti
                int mins = remainingSeconds / 60;
                message = "Manutenzione tra " + mins + " minuti";
                color = YELLOW;
            } else if (remainingSeconds >= 60) { // >= 1 minuto
                int mins = remainingSeconds / 60;
                message = "ATTENZIONE: Manutenzione tra " + mins + " minuti!";
                color = YELLOW;
            } else if (remainingSeconds >= 30) {
                message = "ATTENZIONE: Manutenzione tra " + remainingSeconds + " secondi!";
                color = RED;
            } else {
                message = "SHUTDOWN IMMINENTE: " + remainingSeconds + " secondi!";
                color = RED;
            }

            // Log della notifica
            File notifyLog = new File("logs/maintenance.notify");
            try (PrintWriter writer = new PrintWriter(new FileWriter(notifyLog, true))) {
                writer.println("[" + new Date() + "] " + message);
            }

            // Aggiorna il file di stato
            updateMaintenanceStatus(remainingSeconds);

            // Stampa a video
            System.out.println();
            System.out.println(color + "🔔 " + message + RESET);

        } catch (IOException e) {
            printWarning("Impossibile inviare notifica: " + e.getMessage());
        }
    }

    /**
     * Aggiorna il file di stato con il tempo rimanente
     */
    private void updateMaintenanceStatus(int remainingSeconds) throws IOException {
        File statusFile = new File("maintenance.status");
        Properties status = new Properties();

        status.setProperty("maintenance.active", "true");
        status.setProperty("maintenance.remaining.seconds", String.valueOf(remainingSeconds));
        status.setProperty("maintenance.remaining.minutes", String.valueOf(remainingSeconds / 60));
        status.setProperty("maintenance.message",
                "Sistema in manutenzione. Shutdown tra " + formatTime(remainingSeconds));

        try (FileOutputStream fos = new FileOutputStream(statusFile)) {
            status.store(fos, "Maintenance Status - Updated: " + new Date());
        }
    }

    /**
     * Aggiorna il display del countdown
     */
    private void updateCountdownDisplay(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String timeStr = String.format("%02d:%02d:%02d", hours, minutes, secs);
        String color = seconds <= 60 ? RED : (seconds <= 300 ? YELLOW : GREEN);

        // Progress bar
        int totalBars = 50;
        int filledBars = Math.max(0, totalBars - (seconds / 60));
        String bar = "█".repeat(filledBars) + "░".repeat(totalBars - filledBars);

        System.out.print("\r" + color + "⏱ Tempo rimanente: " + timeStr + RESET +
                " [" + bar + "]");
    }

    /**
     * Formatta i secondi in stringa leggibile
     */
    private String formatTime(int seconds) {
        if (seconds >= 3600) {
            int hours = seconds / 3600;
            int mins = (seconds % 3600) / 60;
            return hours + "h " + mins + "m";
        } else if (seconds >= 60) {
            return (seconds / 60) + " minuti";
        } else {
            return seconds + " secondi";
        }
    }

    /**
     * Esegue backup prima dello shutdown
     */
    private void performBackup() {
        try {
            File backupDir = new File("backups");
            backupDir.mkdirs();

            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFile = "backups/maintenance_backup_" + timestamp + ".tar.gz";

            // Backup dei log
            ProcessBuilder pb = new ProcessBuilder(
                    "tar", "-czf", backupFile,
                    "logs/", "pids/", CONFIG_FILE
            );

            pb.directory(new File("."));
            Process process = pb.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                printSuccess("Backup creato: " + backupFile);
            } else {
                printWarning("Backup completato con avvisi");
            }

        } catch (Exception e) {
            printWarning("Impossibile creare backup: " + e.getMessage());
        }
    }

    /**
     * Pulisce i file di manutenzione
     */
    private void cleanupMaintenanceFiles() {
        try {
            new File("maintenance.status").delete();
            new File("pids/maintenance.flag").delete();
            printInfo("File di manutenzione rimossi");
        } catch (Exception e) {
            printWarning("Cleanup parziale: " + e.getMessage());
        }
    }


    /**
     * Gestione logging tramite script
     */
    private void manageLogging()
    {
        clearScreen();
        printHeader("Gestione Logging");

        printInfo("Avvio interfaccia logging...");
        executeScript("logs-rmi.sh");

        pause();
    }


    /**
     * Gestione configurazione interattiva
     */
    private void manageConfiguration()
    {
        while (true)
        {
            clearScreen();
            printHeader("Gestione Configurazione");

            // Mostra tutte le configurazioni
            printInfo("Configurazione attuale:");
            System.out.println(GRAY + "┌" + "─".repeat(60) + "┐" + RESET);

            List<String> keys = new ArrayList<>(config.stringPropertyNames());
            Collections.sort(keys);

            for (String key : keys)
            {
                String value = config.getProperty(key);
                System.out.printf(GRAY + "│" + RESET + " %-30s = %s\n",
                        CYAN + key + RESET, value);
            }

            System.out.println(GRAY + "└" + "─".repeat(60) + "┘" + RESET);
            System.out.println();


            modifyConfigParameter();
        }
    }


    /**
     * Modifica un parametro di configurazione
     */
    private void modifyConfigParameter()
    {
        System.out.println();
        System.out.print("Inserisci il nome del parametro da modificare: ");
        String key = scanner.nextLine().trim();

        if (!config.containsKey(key))
        {
            printError("Parametro '" + key + "' non trovato!");
            pause();
            return;
        }

        String oldValue = config.getProperty(key);
        System.out.println("Valore attuale: " + YELLOW + oldValue + RESET);
        System.out.println();

        System.out.print("Inserisci il nuovo valore: ");
        String newValue = scanner.nextLine().trim();

        if (!validateConfigValue(key, newValue))
        {
            printError("Valore non valido per il parametro '" + key + "'");
            pause();
            return;
        }

        config.setProperty(key, newValue);

        if (saveConfiguration())
        {
            printSuccess("Parametro '" + key + "' aggiornato con successo");
            printInfo("Vecchio valore: " + oldValue);
            printInfo("Nuovo valore:   " + newValue);
        }
        else
        {
            printError("Errore durante il salvataggio della configurazione");
        }

        pause();
    }


    /**
     * Valida un valore di configurazione
     */
    private boolean validateConfigValue(String key, String value)
    {
        if (value == null || value.isEmpty())
        {
            return false;
        }

        // Validazioni specifiche per tipo di parametro
        if (key.contains("port"))
        {
            try
            {
                int port = Integer.parseInt(value);
                return port > 0 && port <= 65535;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }

        if (key.contains("timeout"))
        {
            try
            {
                int timeout = Integer.parseInt(value);
                return timeout > 0;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }


        if (key.contains("host"))
        {
            // Validazione base hostname/IP
            return Pattern.matches("^[a-zA-Z0-9.-]+$", value);
        }

        return true;
    }


    /**
     * Mostra lo status del sistema
     */
    private void showSystemStatus()
    {
        clearScreen();
        printHeader("Status Sistema");

        executeScript("status-rmi.sh");

        pause();
    }


    /**
     * Carica la configurazione dal file
     */
    private void loadConfiguration()
    {
        config = new Properties();
        File configFile = new File(CONFIG_FILE);

        if (configFile.exists())
        {
            try (FileInputStream fis = new FileInputStream(configFile))
            {
                config.load(fis);
                printSuccess("Configurazione caricata: " + CONFIG_FILE);
            }
            catch (IOException e)
            {
                printWarning("Impossibile caricare configurazione: " + e.getMessage());
                createDefaultConfiguration();
            }
        }
        else
        {
            printWarning("File di configurazione non trovato");
            createDefaultConfiguration();
        }
    }


    /**
     * Crea configurazione predefinita
     */
    private void createDefaultConfiguration()
    {
        config.setProperty("rmi.registry.port", "1099");
        config.setProperty("rmi.registry.host", "localhost");
        config.setProperty("rmi.server.hostname", "localhost");
        config.setProperty("log.level", "INFO");
        config.setProperty("db.host", "localhost");
        config.setProperty("db.port", "3306");
        config.setProperty("db.name", "hotelcolossus");

        saveConfiguration();
    }


    /**
     * Salva la configurazione su file
     */
    private boolean saveConfiguration()
    {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE))
        {
            config.store(fos, "Hotel Colossus Configuration - Modified: " + new Date());

            // Genera anche il file env.sh per gli script bash
            generateEnvScript();

            return true;
        }
        catch (IOException e)
        {
            printError("Errore salvataggio: " + e.getMessage());
            return false;
        }
    }


    /**
     * Genera file env.sh con le variabili da rmi.properties
     * Questo permette agli script bash di usare le stesse configurazioni
     */
    private void generateEnvScript()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter("env.sh")))
        {
            writer.println("#!/bin/bash");
            writer.println("# Environment variables for RMI services");
            writer.println("# Auto-generated from " + CONFIG_FILE + " - " + new Date());
            writer.println();

            // Mappatura properties Java -> variabili bash
            Map<String, String> mapping = new LinkedHashMap<>();
            mapping.put("rmi.registry.port", "RMI_PORT");
            mapping.put("rmi.registry.host", "RMI_HOST");
            mapping.put("rmi.server.hostname", "RMI_SERVER_HOSTNAME");
            mapping.put("rmi.connection.timeout", "RMI_CONNECTION_TIMEOUT");
            mapping.put("rmi.read.timeout", "RMI_READ_TIMEOUT");
            mapping.put("log.level", "LOG_LEVEL");
            mapping.put("log.directory", "LOGS_DIR");
            mapping.put("db.host", "DB_HOST");
            mapping.put("db.port", "DB_PORT");
            mapping.put("db.name", "DB_NAME");
            mapping.put("db.user", "DB_USER");
            mapping.put("db.password", "DB_PASSWORD");
            mapping.put("service.frontdesk.name", "SERVICE_FRONTDESK_NAME");
            mapping.put("service.governante.name", "SERVICE_GOVERNANTE_NAME");
            mapping.put("service.manager.name", "SERVICE_MANAGER_NAME");

            // Esporta variabili base
            writer.println("# Directory paths");
            writer.println("export PROJECT_ROOT=\"$(cd \"$(dirname \"${BASH_SOURCE[0]}\")\" && pwd)\"");
            writer.println("export LOGS_DIR=\"${LOGS_DIR:-$PROJECT_ROOT/logs}\"");
            writer.println("export PID_DIR=\"$PROJECT_ROOT/pids\"");
            writer.println();

            // Esporta configurazioni da properties
            writer.println("# Configuration from " + CONFIG_FILE);
            for (Map.Entry<String, String> entry : mapping.entrySet())
            {
                String propKey = entry.getKey();
                String envVar = entry.getValue();
                String value = config.getProperty(propKey, "");

                if (!value.isEmpty())
                {
                    // Se il valore contiene spazi o caratteri speciali, mettilo tra virgolette
                    if (value.contains(" ") || value.contains("$"))
                    {
                        writer.println("export " + envVar + "=\"" + value + "\"");
                    }
                    else
                    {
                        writer.println("export " + envVar + "=" + value);
                    }
                }
            }

            writer.println();
            writer.println("# Classpath configuration");
            writer.println("if [ -d \"target/classes\" ]; then");
            writer.println("    export CLASSPATH=\"$PROJECT_ROOT/target/classes\"");
            writer.println("elif [ -d \"out/production\" ]; then");
            writer.println("    export CLASSPATH=\"$PROJECT_ROOT/out/production\"");
            writer.println("elif [ -d \"bin\" ]; then");
            writer.println("    export CLASSPATH=\"$PROJECT_ROOT/bin\"");
            writer.println("fi");
            writer.println();

            writer.println("# Add libraries to classpath if present");
            writer.println("if [ -d \"$PROJECT_ROOT/lib\" ]; then");
            writer.println("    for jar in \"$PROJECT_ROOT/lib\"/*.jar; do");
            writer.println("        export CLASSPATH=\"$CLASSPATH:$jar\"");
            writer.println("    done");
            writer.println("fi");
            writer.println();

            writer.println("# Java RMI options");
            writer.println("export JAVA_OPTS=\"-Djava.rmi.server.hostname=${RMI_SERVER_HOSTNAME:-localhost}\"");
            writer.println("export JAVA_OPTS=\"$JAVA_OPTS -Djava.security.policy=$PROJECT_ROOT/rmi.policy\"");
            writer.println("export JAVA_OPTS=\"$JAVA_OPTS -Djava.rmi.server.codebase=file://$CLASSPATH/\"");
            writer.println();

            writer.println("echo \"✓ Environment variables loaded from " + CONFIG_FILE + "\"");

            // Rendi eseguibile
            new File("env.sh").setExecutable(true);

        }
        catch (IOException e)
        {
            printWarning("Impossibile generare env.sh: " + e.getMessage());
        }
    }


    /**
     * Esegue uno script bash
     */
    private boolean executeScript(String scriptName)
    {
        try
        {
            String scriptPath = SCRIPT_DIR + "/" + scriptName;

            // Verifica che lo script esista
            File script = new File(scriptPath);
            if (!script.exists())
            {
                printError("Script non trovato: " + scriptPath);
                return false;
            }

            // Rendi eseguibile
            script.setExecutable(true);

            // Esegui lo script
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", scriptPath);
            pb.inheritIO(); // Eredita input/output
            pb.directory(new File(SCRIPT_DIR));

            Process process = pb.start();
            int exitCode = process.waitFor();

            return exitCode == 0;

        }
        catch (Exception e)
        {
            printError("Errore esecuzione script: " + e.getMessage());
            return false;
        }
    }


    /**
     * Uscita dal sistema
     */
    private void exitSystem()
    {
        clearScreen();
        System.out.println(GREEN + "╔═══════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║   Grazie per aver usato Hotel Colossus Admin      ║" + RESET);
        System.out.println(GREEN + "╚═══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }


    // ========================================================================
    // UTILITY METHODS
    // ========================================================================


    private void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    private void printHeader(String title)
    {
        System.out.println(BLUE + "═".repeat(55) + RESET);
        System.out.println(BLUE + "     " + title + RESET);
        System.out.println(BLUE + "═".repeat(55) + RESET);
        System.out.println();
    }


    private void printInfo(String msg)
    {
        System.out.println(BLUE + "[INFO] " + RESET + msg);
    }


    private void printSuccess(String msg)
    {
        System.out.println(GREEN + "[✓] " + RESET + msg);
    }


    private void printWarning(String msg)
    {
        System.out.println(YELLOW + "[⚠] " + RESET + msg);
    }


    private void printError(String msg)
    {
        System.out.println(RED + "[✗] " + RESET + msg);
    }


    private void pause()
    {
        System.out.println();
        System.out.print("Premi INVIO per continuare...");
        scanner.nextLine();
    }


    private int getIntInput(String prompt)
    {
        while (true)
        {
            try
            {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                printError("Inserisci un numero valido");
            }
        }
    }
}