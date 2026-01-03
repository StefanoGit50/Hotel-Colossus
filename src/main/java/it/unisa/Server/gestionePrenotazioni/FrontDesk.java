package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Common.Camera;
import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamerePublisher;
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

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface {
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    private List<Prenotazione> prenotazioni = new ArrayList<>();
    private CatalogoCamerePublisher camList = new CatalogoCamerePublisher();

    public FrontDesk() throws RemoteException {
        super();
    }

    @Override
    public List<Camera> getCamere(){
       return CatalogoCamerePublisher.getListaCamere();
    }

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        Logger.getLogger("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return camList.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return CatalogoCamerePublisher.getLastModified();
    }

    @Override
    public void effettuaPrenotazione(String id, Cliente cliente, Camera stanza) throws RemoteException
    {
        //if(Camera.getStatoGlobale().equals("libera"))
        {
            //Prenotazione p = new Prenotazione(id, cliente, stanza);
            //prenotazioni.add(p);

            try{
                GovernanteInterface gs = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
                //gs.setOccupataGlobale(Canera);
          //      logger.info("Prenotazione effettuata per stanza " + stanza.getNumero());
            } catch(Exception e)
            {
                logger.severe("Errore nel contattare GestoreCamere: " + e.getMessage());
                e.printStackTrace();
            }
        }
       // else
        {
      //      logger.warning("Stanza " + stanza.getNumero() + " occupata!");
       //     System.err.println("Stanza " + stanza.getNumero() + " occupata!");
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


    public static void main(String[] args)
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