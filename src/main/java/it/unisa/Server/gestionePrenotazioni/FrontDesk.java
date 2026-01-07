package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Common.Camera;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.gestioneClienti.Cliente;

import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.FrontDeskStorage;
import it.unisa.interfacce.GovernanteInterface;
import it.unisa.interfacce.FrontDeskInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface {
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    private List<Prenotazione> prenotazioni = new ArrayList<>();
    private CatalogoCamere camList = new CatalogoCamere();

    public FrontDesk() throws RemoteException {
        super();
    }


    public List<Camera> getCamere(){
       return CatalogoCamere.getListaCamere();
    }

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        Logger.getLogger("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return camList.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return CatalogoCamere.getLastModified();
    }

    @Override
    public void effettuaPrenotazione(String id, Cliente cliente, Camera stanza) throws RemoteException, SQLException {
        prenotazioni.add(new Prenotazione(Integer.parseInt(id), LocalDate.now() , LocalDate.now()));
        FrontDeskStorage<Prenotazione> prenotazioneFrontDeskStorage = new PrenotazioneDAO();

        while(!prenotazioni.isEmpty()){
            prenotazioneFrontDeskStorage.doSave(prenotazioni.getFirst());
            prenotazioni.removeFirst();
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
            if(p2.getCodicePrenotazione() == p.getCodicePrenotazione())
            {
                prenotazioni.remove(p2);
                logger.info("Prenotazione " + p.getCodicePrenotazione() + " cancellata");
                trovata = true;
                return;
            }
        }
        if(!trovata) {
            logger.warning("Prenotazione " + p.getCodicePrenotazione() + " non trovata");
            System.err.println("Prenotazione non trovata");
        }
    }

    @Override
    public Prenotazione getPrenotazione(String id) throws RemoteException
    {
        for(Prenotazione p2: prenotazioni)
        {
            if(p2.getCodicePrenotazione() == Integer.getInteger(id))
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