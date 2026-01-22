package it.unisa.Server.gestioneImpiegati;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Logger;

/**
 * Classe di implememtazione del manager --TEST
 * //TODO: Spostare in package gestioneImpiegato -> sostituire con la classe Manager
 */
public class Manager {

    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    static void main(String[] args)
    {
        try
        {
            // IMPORTANTE: Avvia l'RMI registry programmaticamente
            logger.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");

            try {
                // Prova a creare un nuovo registry
                LocateRegistry.createRegistry(RMI_PORT);
                logger.info("✓ RMI Registry creato con successo!");
            } catch (RemoteException e) {
                // Se esiste già, ottieni il riferimento
                LocateRegistry.getRegistry(RMI_PORT);
                logger.info("✓ Connesso a RMI Registry esistente");
            }

            logger.info("Genero il gestore prenotazioni...");
            Manager man = new Manager();
            logger.info("✓ Gestore prenotazioni creato");

            logger.info("Effettuo il rebind di gestione prenotazioni...");
            //Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni", man);
            logger.info("✓ Gestore prenotazioni registrato con successo!");
            logger.info("✓ Servizio 'GestionePrenotazioni' pronto");
            logger.info("Server in attesa di connessioni...");

            // Mantieni il server in esecuzione
            Thread.currentThread().join();
        }
        catch(Exception e)
        {
            logger.severe("Errore durante l'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
