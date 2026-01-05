package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Common.Camera;
import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;
import it.unisa.interfacce.FrontDeskInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface
{
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    List<Prenotazione> prenotazioni = new ArrayList<>();

    public FrontDesk() throws RemoteException {
        super();
    }

    @Override
    public void effettuaPrenotazione(String id, Cliente cliente, Camera camera) throws RemoteException
    {
        if(camera.getStatoCamera() == Stato.Libera)
        {
            Prenotazione p = new Prenotazione(id, cliente, camera);
            prenotazioni.add(p);

            try{
                GovernanteInterface gs = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
                //gs.setOccupataGlobale(stanza);
                logger.info("Prenotazione effettuata per stanza " + stanza.getNumero());
            } catch(Exception e)
            {
                logger.severe("Errore nel contattare GestoreCamere: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            logger.warning("Stanza " + camera.getNumeroCamera() + " occupata!");
            System.err.println("Stanza " + camera.getNumeroCamera() + " occupata!");
        }
    }

    @Override
    public List<Prenotazione> getPrenotazioni() throws RemoteException
    {
        return prenotazioni;
    }

    @Override
    public void cancellaPrenotazione(Prenotazione p) throws RemoteException
    {
        boolean trovata = false;
        for(Prenotazione p2: prenotazioni)
        {
            if(p2.getId().equals(p.getId()))
            {
                prenotazioni.remove(p2);
                logger.info("Prenotazione " + p.getId() + " cancellata");
                trovata = true;
                return;
            }
        }
        if(!trovata) {
            logger.warning("Prenotazione " + p.getId() + " non trovata");
            System.err.println("Prenotazione non trovata");
        }
    }

    @Override
    public Prenotazione getPrenotazione(String id) throws RemoteException
    {
        for(Prenotazione p2: prenotazioni)
        {
            if(p2.getId().equals(id))
            {
                return p2;
            }
        }
        logger.warning("Prenotazione " + id + " non trovata");
        System.err.println("Prenotazione non trovata");
        return null;
    }

    @Override
    public boolean aggiungiCliente(Cliente c) {
        return false;
    }

    public static void main(String[] args)
    {
        try
        {
            // IMPORTANTE: Avvia l'RMI registry programmaticamente
            logger.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");
            Registry registry;
            try {
                // Prova a creare un nuovo registry
                registry = LocateRegistry.createRegistry(RMI_PORT);
                logger.info("✓ RMI Registry creato con successo!");
            } catch (RemoteException e) {
                // Se esiste già, ottieni il riferimento
                registry = LocateRegistry.getRegistry(RMI_PORT);
                logger.info("✓ Connesso a RMI Registry esistente");
            }

            logger.info("Genero il gestore prenotazioni...");
            FrontDesk gp = new FrontDesk();
            logger.info("✓ Gestore prenotazioni creato");

            logger.info("Effettuo il rebind di gestione prenotazioni...");
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni", gp);
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