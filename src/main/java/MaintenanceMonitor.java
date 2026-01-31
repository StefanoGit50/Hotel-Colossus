import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

/**
 * Interfaccia per il monitoraggio della manutenzione del sistema.
 * Fornisce metodi per verificare lo stato di manutenzione e ottenere informazioni
 * rilevanti per notificare i client.
 */
public interface MaintenanceMonitor
{

    /**
     * Verifica se il sistema è attualmente in manutenzione.
     * Controlla sia l'esistenza del file di stato che la validità temporale.
     *
     * @return true se la manutenzione è attiva e valida, false altrimenti
     */
    boolean isMaintenanceActive();


    /**
     * Ottiene il tempo rimanente prima dello shutdown in secondi.
     *
     * @return secondi rimanenti, o -1 se non c'è manutenzione attiva
     */
    long getRemainingSeconds();


    /**
     * Ottiene il tempo rimanente prima dello shutdown in minuti.
     *
     * @return minuti rimanenti, o -1 se non c'è manutenzione attiva
     */
    long getRemainingMinutes();


    /**
     * Ottiene il timestamp dello shutdown programmato.
     *
     * @return timestamp Unix dello shutdown, o -1 se non c'è manutenzione attiva
     */
    long getShutdownTimestamp();


    /**
     * Ottiene la data/ora dello shutdown in formato leggibile.
     *
     * @return stringa con data e ora dello shutdown, o null se non c'è manutenzione attiva
     */
    String getShutdownDateTime();


    /**
     * Ottiene il messaggio di notifica per i client in base al tempo rimanente.
     * Il messaggio varia in base all'urgenza.
     *
     * @return messaggio di notifica appropriato, o null se non c'è manutenzione attiva
     */
    String getNotificationMessage();


    /**
     * Ottiene tutte le informazioni di manutenzione in un oggetto.
     *
     * @return oggetto MaintenanceInfo con tutti i dettagli, o null se non c'è manutenzione attiva
     */
    MaintenanceInfo getMaintenanceInfo();


    /**
     * Verifica se il client specificato dovrebbe ricevere una notifica ora.
     * Evita di inviare troppe notifiche allo stesso client.
     *
     * @param clientId identificativo del client
     * @return true se il client dovrebbe essere notificato ora
     */
    boolean shouldNotifyClient(String clientId);


    /**
     * Registra che un client è stato notificato.
     *
     * @param clientId identificativo del client
     */
    void markClientNotified(String clientId);


    /**
     * Classe contenente tutte le informazioni sulla manutenzione
     */
    class MaintenanceInfo
    {
        private final boolean active;
        private final long remainingSeconds;
        private final long remainingMinutes;
        private final long shutdownTimestamp;
        private final String shutdownDateTime;
        private final String message;
        private final String reason;
        private final String description;
        private final int totalMinutes;
        private final long startedTimestamp;


        public MaintenanceInfo(boolean active, long remainingSeconds, long remainingMinutes,
                               long shutdownTimestamp, String shutdownDateTime, String message,
                               String reason, String description, int totalMinutes, long startedTimestamp)
        {
            this.active = active;
            this.remainingSeconds = remainingSeconds;
            this.remainingMinutes = remainingMinutes;
            this.shutdownTimestamp = shutdownTimestamp;
            this.shutdownDateTime = shutdownDateTime;
            this.message = message;
            this.reason = reason;
            this.description = description;
            this.totalMinutes = totalMinutes;
            this.startedTimestamp = startedTimestamp;
        }


        // Getters

        public boolean isActive()
        {
            return active;
        }

        public long getRemainingSeconds()
        {
            return remainingSeconds;
        }

        public long getRemainingMinutes()
        {
            return remainingMinutes;
        }

        public long getShutdownTimestamp()
        {
            return shutdownTimestamp;
        }

        public String getShutdownDateTime()
        {
            return shutdownDateTime;
        }

        public String getMessage()
        {
            return message;
        }

        public String getReason()
        {
            return reason;
        }

        public String getDescription()
        {
            return description;
        }

        public int getTotalMinutes()
        {
            return totalMinutes;
        }

        public long getStartedTimestamp()
        {
            return startedTimestamp;
        }


        @Override
        public String toString()
        {
            return String.format(
                    "MaintenanceInfo{active=%s, remaining=%dm %ds, message='%s'}",
                    active, remainingMinutes, remainingSeconds % 60, message
            );
        }
    }
}


/**
 * Implementazione del MaintenanceManager che monitora il file di stato
 * creato dallo script di manutenzione.
 */
class MaintenanceManager implements MaintenanceMonitor
{

    private static final String MAINTENANCE_FILE = "maintenance.status";
    private static final String MAINTENANCE_FLAG = "pids/maintenance.flag";

    // Cache per le notifiche ai client
    private final Map<String, Long> clientNotifications = new HashMap<>();
    private static final long NOTIFICATION_COOLDOWN_MS = 30000; // 30 secondi tra notifiche

    // Cache del file di stato
    private Properties statusProperties;
    private long lastFileCheck = 0;
    private static final long FILE_CHECK_INTERVAL_MS = 1000; // Rileggi il file ogni secondo


    /**
     * Legge e aggiorna la cache del file di stato se necessario.
     */
    private synchronized Properties getStatusProperties()
    {
        long now = System.currentTimeMillis();

        // Rileggi il file solo se è passato abbastanza tempo
        if (statusProperties == null || (now - lastFileCheck) > FILE_CHECK_INTERVAL_MS)
        {
            statusProperties = loadMaintenanceFile();
            lastFileCheck = now;
        }

        return statusProperties;
    }


    /**
     * Carica il file di manutenzione.
     */
    private Properties loadMaintenanceFile()
    {
        Properties props = new Properties();

        File maintenanceFile = new File(MAINTENANCE_FILE);

        if (!maintenanceFile.exists())
        {
            return props; // Restituisce proprietà vuote
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(maintenanceFile)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                line = line.trim();

                // Salta commenti e righe vuote
                if (line.isEmpty() || line.startsWith("#"))
                {
                    continue;
                }

                // Parsing chiave=valore
                int equalsIndex = line.indexOf('=');

                if (equalsIndex > 0)
                {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    props.setProperty(key, value);
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Errore nella lettura del file di manutenzione: " + e.getMessage());
        }

        return props;
    }


    @Override
    public boolean isMaintenanceActive()
    {
        Properties props = getStatusProperties();

        // Verifica che il file esista e sia marcato come attivo
        String active = props.getProperty("maintenance.active");

        if (!"true".equalsIgnoreCase(active))
        {
            return false;
        }

        // Verifica che non sia già scaduto
        long remaining = getRemainingSeconds();

        if (remaining <= 0)
        {
            return false;
        }

        // Verifica che lo stato sia IN_PROGRESS
        String status = props.getProperty("maintenance.current.status");

        return "IN_PROGRESS".equalsIgnoreCase(status);
    }


    @Override
    public long getRemainingSeconds()
    {
        Properties props = getStatusProperties();
        String remaining = props.getProperty("maintenance.remaining.seconds");

        if (remaining == null)
        {
            return -1;
        }

        try
        {
            return Long.parseLong(remaining);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }


    @Override
    public long getRemainingMinutes()
    {
        long seconds = getRemainingSeconds();
        return seconds >= 0 ? (seconds / 60) : -1;
    }


    @Override
    public long getShutdownTimestamp()
    {
        Properties props = getStatusProperties();
        String timestamp = props.getProperty("maintenance.shutdown.timestamp");

        if (timestamp == null)
        {
            return -1;
        }

        try
        {
            return Long.parseLong(timestamp);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }


    @Override
    public String getShutdownDateTime()
    {
        Properties props = getStatusProperties();
        return props.getProperty("maintenance.shutdown.datetime");
    }


    @Override
    public String getNotificationMessage()
    {
        if (!isMaintenanceActive())
        {
            return null;
        }

        long remainingSeconds = getRemainingSeconds();
        long remainingMinutes = remainingSeconds / 60;

        if (remainingSeconds < 30)
        {
            return String.format("SHUTDOWN IMMINENTE: %d secondi!", remainingSeconds);
        }
        else if (remainingSeconds < 60)
        {
            return String.format("ATTENZIONE: Manutenzione tra %d secondi!", remainingSeconds);
        }
        else if (remainingMinutes < 5)
        {
            return String.format("ATTENZIONE: Manutenzione tra %d minuti!", remainingMinutes);
        }
        else
        {
            return String.format("Sistema in manutenzione. Shutdown previsto tra %d minuti.", remainingMinutes);
        }
    }


    @Override
    public MaintenanceInfo getMaintenanceInfo()
    {
        if (!isMaintenanceActive())
        {
            return null;
        }

        Properties props = getStatusProperties();

        long remainingSeconds = getRemainingSeconds();
        long remainingMinutes = getRemainingMinutes();
        long shutdownTimestamp = getShutdownTimestamp();
        String shutdownDateTime = getShutdownDateTime();
        String message = getNotificationMessage();

        String reason = props.getProperty("maintenance.reason", "Manutenzione programmata");
        String description = props.getProperty("maintenance.description", "");

        int totalMinutes = 0;

        try
        {
            totalMinutes = Integer.parseInt(props.getProperty("maintenance.scheduled.minutes", "0"));
        }
        catch (NumberFormatException e)
        {
            // Ignore
        }

        long startedTimestamp = 0;

        try
        {
            startedTimestamp = Long.parseLong(props.getProperty("maintenance.started.timestamp", "0"));
        }
        catch (NumberFormatException e)
        {
            // Ignore
        }

        return new MaintenanceInfo
                (
                true,
                remainingSeconds,
                remainingMinutes,
                shutdownTimestamp,
                shutdownDateTime,
                message,
                reason,
                description,
                totalMinutes,
                startedTimestamp
        );
    }


    @Override
    public boolean shouldNotifyClient(String clientId)
    {
        if (!isMaintenanceActive())
        {
            return false;
        }

        Long lastNotification = clientNotifications.get(clientId);

        if (lastNotification == null)
        {
            return true; // Prima notifica
        }

        long now = System.currentTimeMillis();
        long timeSinceLastNotification = now - lastNotification;

        // Aumenta la frequenza di notifica in base al tempo rimanente
        long remainingSeconds = getRemainingSeconds();
        long cooldown = NOTIFICATION_COOLDOWN_MS;

        if (remainingSeconds < 60)
        {
            cooldown = 10000; // 10 secondi se critico
        }
        else if (remainingSeconds < 300)
        {
            cooldown = 20000; // 20 secondi se urgente
        }
        else if (remainingSeconds < 600)
        {
            cooldown = 30000; // 30 secondi se medio
        }
        else
        {
            cooldown = 60000; // 60 secondi se basso
        }

        return timeSinceLastNotification >= cooldown;
    }


    @Override
    public void markClientNotified(String clientId)
    {
        clientNotifications.put(clientId, System.currentTimeMillis());
    }


    /**
     * Metodo di utilità per formattare il tempo rimanente in modo leggibile.
     */
    public String formatRemainingTime()
    {
        long seconds = getRemainingSeconds();

        if (seconds < 0)
        {
            return "N/A";
        }

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0)
        {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
        else
        {
            return String.format("%02d:%02d", minutes, secs);
        }
    }


    /**
     * Pulisce la cache delle notifiche dai client non più rilevanti.
     * Dovrebbe essere chiamato periodicamente.
     */
    public void cleanupNotificationCache()
    {
        long now = System.currentTimeMillis();
        long maxAge = 3600000; // 1 ora

        clientNotifications.entrySet().removeIf(
                entry -> (now - entry.getValue()) > maxAge
        );
    }
}