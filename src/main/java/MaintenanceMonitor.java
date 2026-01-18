import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitor per lo stato di manutenzione del sistema
 * Questa classe può essere integrata nei client per:
 * - Rilevare quando il sistema va in manutenzione
 * - Mostrare notifiche agli utenti
 * - Gestire la disconnessione automatica
 */

public class MaintenanceMonitor
{

    private static final String STATUS_FILE = "maintenance.status";
    private static final long CHECK_INTERVAL = 5000; // 5 secondi

    private Timer monitorTimer;
    private MaintenanceListener listener;
    private boolean maintenanceActive = false;


    /**
     * Interfaccia per ricevere notifiche di manutenzione
     */
    public interface MaintenanceListener
    {

        /**
         * Chiamato quando viene rilevata una manutenzione imminente
         * @param minutes minuti rimanenti prima dello shutdown
         * @param message messaggio di manutenzione
         */
        void onMaintenanceScheduled(int minutes, String message);


        /**
         * Chiamato periodicamente con aggiornamenti sul tempo rimanente
         * @param remainingSeconds secondi rimanenti
         */
        void onMaintenanceUpdate(int remainingSeconds);


        /**
         * Chiamato quando lo shutdown è imminente
         */
        void onShutdownImminent();

    }


    /**
     * Costruttore
     * @param listener listener per ricevere le notifiche
     */
    public MaintenanceMonitor(MaintenanceListener listener)
    {
        this.listener = listener;
    }


    /**
     * Avvia il monitoraggio
     */
    public void startMonitoring()
    {
        if (monitorTimer != null)
        {
            monitorTimer.cancel();
        }

        monitorTimer = new Timer("MaintenanceMonitor", true);
        monitorTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                checkMaintenanceStatus();
            }
        }, 0, CHECK_INTERVAL);
    }


    /**
     * Ferma il monitoraggio
     */
    public void stopMonitoring()
    {
        if (monitorTimer != null)
        {
            monitorTimer.cancel();
            monitorTimer = null;
        }
    }


    /**
     * Verifica lo stato di manutenzione
     */
    private void checkMaintenanceStatus()
    {
        File statusFile = new File(STATUS_FILE);

        if (!statusFile.exists())
        {
            // Nessuna manutenzione attiva
            if (maintenanceActive)
            {
                maintenanceActive = false;
            }
            return;
        }

        try
        {
            Properties status = new Properties();

            try (FileInputStream fis = new FileInputStream(statusFile))
            {
                status.load(fis);
            }

            boolean isActive = Boolean.parseBoolean(
                    status.getProperty("maintenance.active", "false")
            );

            if (!isActive)
            {
                if (maintenanceActive)
                {
                    maintenanceActive = false;
                }
                return;
            }

            // Manutenzione attiva
            if (!maintenanceActive)
            {
                // Prima notifica
                maintenanceActive = true;
                int minutes = Integer.parseInt(
                        status.getProperty("maintenance.shutdown.minutes", "0")
                );
                String message = status.getProperty("maintenance.message",
                        "Sistema in manutenzione");

                if (listener != null)
                {
                    listener.onMaintenanceScheduled(minutes, message);
                }
            }

            // Aggiornamento continuo
            int remainingSeconds = Integer.parseInt(
                    status.getProperty("maintenance.remaining.seconds", "0")
            );

            if (listener != null)
            {
                listener.onMaintenanceUpdate(remainingSeconds);

                // Notifica quando manca poco
                if (remainingSeconds <= 60 && remainingSeconds > 0)
                {
                    listener.onShutdownImminent();
                }
            }

        }
        catch (Exception e)
        {
            System.err.println("Errore lettura stato manutenzione: " + e.getMessage());
        }
    }


    /**
     * Verifica se la manutenzione è attiva
     */
    public static boolean isMaintenanceActive()
    {
        File statusFile = new File(STATUS_FILE);
        if (!statusFile.exists())
        {
            return false;
        }

        try
        {
            Properties status = new Properties();
            try (FileInputStream fis = new FileInputStream(statusFile))
            {
                status.load(fis);
            }
            return Boolean.parseBoolean(
                    status.getProperty("maintenance.active", "false")
            );
        }
        catch (Exception e)
        {
            return false;
        }
    }


    /**
     * Ottiene il messaggio di manutenzione (metodo statico)
     */
    public static String getMaintenanceMessage()
    {
        File statusFile = new File(STATUS_FILE);
        if (!statusFile.exists())
        {
            return null;
        }

        try
        {
            Properties status = new Properties();
            try (FileInputStream fis = new FileInputStream(statusFile))
            {
                status.load(fis);
            }
            return status.getProperty("maintenance.message");
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * Ottiene i secondi rimanenti
     */
    public static int getRemainingSeconds()
    {
        File statusFile = new File(STATUS_FILE);
        if (!statusFile.exists())
        {
            return -1;
        }

        try
        {
            Properties status = new Properties();
            try (FileInputStream fis = new FileInputStream(statusFile))
            {
                status.load(fis);
            }
            return Integer.parseInt(
                    status.getProperty("maintenance.remaining.seconds", "-1")
            );
        }
        catch (Exception e)
        {
            return -1;
        }
    }


    /**
     * Esempio di utilizzo
     */
    public static void main(String[] args)
    {
        System.out.println("=== MaintenanceMonitor ===\n");

        MaintenanceMonitor monitor = new MaintenanceMonitor(new MaintenanceListener()
        {
            @Override
            public void onMaintenanceScheduled(int minutes, String message)
            {
                System.out.println("\n🔔 MANUTENZIONE PROGRAMMATA!");
                System.out.println("   Messaggio: " + message);
                System.out.println("   Tempo: " + minutes + " minuti");
                System.out.println("   ⚠ Salva il tuo lavoro!\n");
            }

            @Override
            public void onMaintenanceUpdate(int remainingSeconds)
            {
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;

                // Mostra solo agli intervalli significativi
                if (remainingSeconds % 60 == 0 || remainingSeconds <= 10)
                {
                    System.out.printf("⏱ Tempo rimanente: %02d:%02d\n", minutes, seconds);
                }
            }

            @Override
            public void onShutdownImminent()
            {
                System.out.println("\n⚠⚠⚠ SHUTDOWN IMMINENTE! ⚠⚠⚠");
                System.out.println("Chiudi tutte le operazioni!\n");
            }
        });

        monitor.startMonitoring();

        System.out.println("Monitoraggio attivo...");
        System.out.println("Premi Ctrl+C per uscire\n");

        // Mantieni il programma attivo
        try
        {
            Thread.sleep(Long.MAX_VALUE);
        }
        catch (InterruptedException e)
        {
            monitor.stopMonitoring();
        }
    }
}