package it.unisa.Server.gestioneCamere;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementazione del servizio di gestione delle camere per il personale governante.
 * Questa classe gestisce lo stato delle camere dell'hotel e fornisce funzionalità
 * per il monitoraggio e l'aggiornamento dello stato delle camere tramite RMI.
 * Si parte dal presupposto che le camere, in uno stato iniziale del software,
 * siano già create e settate nei parametri di default. Per cui il programma ricava
 * le camere direttamente dal database
 *
 */
public class GovernanteImpl extends UnicastRemoteObject implements GovernanteInterface
{
    private static final long serialVersionUID = -34234234L;
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    private static List<Camera> camere = new ArrayList<>();


    /**
     * Costruttore della classe GovernanteImpl.
     *
     * @throws RemoteException se si verifica un errore nella creazione dell'oggetto remoto
     */
    public GovernanteImpl() throws RemoteException
    {
        // effettua una query dal db e ottiene tutte le camere, le salva nella lista camere

        // per testare il programma nel frattempo che venga inserito il db
        for(int x = 101; x <= 599; x++)
        {
            Camera c = new Camera(x);
            c.setStatoCamera(Stato.Libera);
            camere.add(c);
        }
    }

    /**
     * Restituisce una camera dato il suo numero identificativo.
     *
     * @param numero il numero identificativo della camera da cercare
     * @return la Camera corrispondente al numero specificato, o una Camera vuota se non trovata
     *
     * @pre numero > 0
     * @pre la lista camere deve essere inizializzata (non null)
     * @post restituisce la Camera con numeroCamera == numero se presente nella lista
     * @post restituisce una nuova istanza di Camera() se nessuna camera corrisponde al numero specificato
     * @post la lista camere non viene modificata
     */
    @Override
    public Camera getCamera(int numero)
    {
        for(Camera c: camere)
        {
            if(c.getNumeroCamera() == numero)
                return c;
        }
        return new Camera();
    }

    /**
     * Imposta lo stato di una camera a "In Pulizia".
     *
     * @param c la camera di cui modificare lo stato
     *
     * @pre c != null
     * @pre c deve essere presente nella lista camere
     * @post c.getStatoCamera() == Stato.InPulizia
     * @post lo stato precedente della camera viene sovrascritto
     */
    @Override
    public void setStatoInPulizia(Camera c)
    {
        c.setStatoCamera(Stato.InPulizia);

        // aggiorna i dati nel db

        // tramite una funzione notifica i cambiamenti al front desk
    }

    /**
     * Imposta lo stato di una camera a "Fuori Servizio" (Out Of Order).
     *
     * @param c la camera di cui modificare lo stato
     *
     * @pre c != null
     * @pre c deve essere presente nella lista camere
     * @post c.getStatoCamera() == Stato.OutOfOrder
     * @post lo stato precedente della camera viene sovrascritto
     */
    @Override
    public void setStatoOutOfOrder(Camera c)
    {
        c.setStatoCamera(Stato.OutOfOrder);

        // aggiorna i dati nel db

        // tramite una funzione notifica i cambiamenti al front desk
    }

    /**
     * Imposta lo stato di una camera a "In Servizio".
     *
     * @param c la camera di cui modificare lo stato
     *
     * @pre c != null
     * @pre c deve essere presente nella lista camere
     * @post c.getStatoCamera() == Stato.InServizio
     * @post lo stato precedente della camera viene sovrascritto
     */
    @Override
    public void setStatoInServizio(Camera c)
    {
        c.setStatoCamera(Stato.InServizio);

        // aggiorna i dati nel db

        // tramite una funzione notifica i cambiamenti al front desk
    }


    /**
     * Imposta lo stato di una camera a "Libera".
     *
     * @param c la camera di cui modificare lo stato
     *
     * @pre c != null
     * @pre c deve essere presente nella lista camere
     * @post c.getStatoCamera() == Stato.Libera
     * @post lo stato precedente della camera viene sovrascritto
     */
    @Override
    public void setStatoLibera(Camera c)
    {
        c.setStatoCamera(Stato.Libera);

        // aggiorna i dati nel db

        // tramite una funzione notifica i cambiamenti al front desk
    }

    /**
     * Restituisce la lista completa di tutte le camere gestite dal sistema.
     *
     * @return la lista di tutte le camere
     *
     * @pre la lista camere deve essere inizializzata (non null)
     * @post restituisce un riferimento alla lista camere
     * @post la lista camere non viene modificata
     * @post il valore restituito può essere una lista vuota se non ci sono camere nel sistema
     */
    @Override
    public List<Camera> getListCamere()
    {
        return camere;
    }

    /**
    * Metodo principale per l'avvio del server RMI di gestione camere.
    * Inizializza le camere dal database, crea il registry RMI e registra il servizio.
    *
    * @param args argomenti da linea di comando (non utilizzati)
    */
    public static void main(String[] args) throws RemoteException
    {
        logger.info("Ottengo le camere...");



        logger.info("Camere ottenute!");

        try
        {
            // IMPORTANTE: Avvia l'RMI registry programmaticamente
            logger.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");
            Registry registry;
            try {
                // Prova a creare un nuovo registry
                registry = LocateRegistry.createRegistry(RMI_PORT);
                logger.info("RMI Registry creato con successo!");
            } catch (RemoteException e) {
                // Se esiste già, ottieni il riferimento
                registry = LocateRegistry.getRegistry(RMI_PORT);
                logger.info("Connesso a RMI Registry esistente");
            }

            logger.info("Sto creando il gestore camere...");
            GovernanteImpl gc = new GovernanteImpl();
            logger.info("Gestore camere creato... ");

            logger.info("Effettuo il rebind del gestore camere...");
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestoreCamere", gc);
            logger.info("Il gestore camere è pronto e registrato!");
            logger.info("Server in attesa di connessioni...");

            // Mantieni il server in esecuzione
            Thread.currentThread().join();
        }
        catch(Exception e)
        {
            logger.severe("Errore durante l'avvio del server RMI: " + e.getMessage());
            e.printStackTrace();
        }

    }

}