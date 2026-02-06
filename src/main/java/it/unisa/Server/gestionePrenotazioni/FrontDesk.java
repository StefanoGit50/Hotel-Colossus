package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Server.Autentication.Autentication;
import it.unisa.Server.IllegalAccess;
import it.unisa.interfacce.*;
import it.unisa.Server.command.*;
import it.unisa.Common.*;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.CatalogoClientiCommands.*;
import it.unisa.Server.command.CatalogoImpiegatiCommands.*;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.obj.catalogues.CatalogueUtils;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import org.apache.logging.log4j.LogManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface {
    private CatalogoCamere camList = new CatalogoCamere();
    private static final int RMI_PORT = 1099;

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FrontDesk.class);

    public FrontDesk() throws RemoteException {
        super();
    }

    //costruttore di Testing
    public FrontDesk(Invoker invoker,  CatalogoCamere catalogoCamere) throws RemoteException {
        super();
        this.invoker = invoker;
        this.camList = catalogoCamere;
    }

    public List<Camera> getCamere(){
       return camList.getListaCamere();
    }

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        log.info("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return camList.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return camList.getLastModified();
    }

    @Override
    public ArrayList<Prenotazione> getPrenotazioni() throws RemoteException{
         ArrayList<Prenotazione> list =CatalogoPrenotazioni.getListaPrenotazioni();
        System.out.println("DENTRO LA CHIAMATA"+list);
        return list;
    }

    public static void main(String[] args)
    {
        try
        {
            // IMPORTANTE: Avvia l'RMI registry programmaticamente
            log.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");

            try {
                // Prova a creare un nuovo registry
                LocateRegistry.createRegistry(RMI_PORT);
                log.info("✓ RMI Registry creato con successo!");
            } catch (RemoteException e) {
                // Se esiste già, ottieni il riferimento
                LocateRegistry.getRegistry(RMI_PORT);
                log.info("✓ Connesso a RMI Registry esistente");
            }

            log.info("Genero il gestore prenotazioni...");
            FrontDesk gp = new FrontDesk();
            log.info("✓ Gestore prenotazioni creato");

            log.info("Effettuo il rebind di gestione prenotazioni...");
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni", gp);
            log.info("✓ Gestore prenotazioni registrato con successo!");
            log.info("✓ Servizio 'GestionePrenotazioni' pronto");
            log.info("Server in attesa di connessioni...");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n--- SHUTDOWN IN CORSO ---");
                try {
                    // 1. Rimuove il nome dal registro
                    Naming.unbind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni");
                    // 2. Smette di ascoltare (Forza la disconnessione)
                    // Questo è ciò che uccide veramente lo zombie lato RMI
                    java.rmi.server.UnicastRemoteObject.unexportObject(gp, true);
                    System.out.println("✓ Server disconnesso correttamente.");
                } catch (Exception e) {
                    // Ignora errori in chiusura
                }
            }));


            Thread.currentThread().join();
        }
        catch(Exception e)
        {
            System.out.println(e);
            log.error("ERRORE FATALE: Probabilmente c'è un vecchio server attivo sulla porta " + RMI_PORT);
            log.error("Soluzione: Ferma tutti i processi Java (pulsante Stop o Task Manager).");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /// ////////////////////////////////////////////////////////////////////////////////////

    // Invoker --> mantiene l'ordine delle chiamate ai comandi
    private Invoker invoker = new Invoker();
    // Cataloghi
    private CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();
    private CatalogoClienti catalogoClienti = new CatalogoClienti();


    // COMANDI PRENOTAZIONE
    @Override
    public void addPrenotazione(Prenotazione p) throws RemoteException {
        CatalogueUtils.checkNull(p);                 // Lancia InvalidInputException
        CatalogoPrenotazioni.checkPrenotazione(p);   // Lancia InvalidInputException
        AddPrenotazioneCommand command = new AddPrenotazioneCommand(p);
        invoker.executeCommand(command);

    }

    @Override
    public void removePrenotazione(Prenotazione p) throws RemoteException {

//        RemovePrenotazioneCommand command = new RemovePrenotazioneCommand(catalogoPrenotazioni, p);
//        invoker.executeCommand(command);
    }

    @Override
    public void updatePrenotazione(Prenotazione p) throws RemoteException {
         UpdatePrenotazioneCommand command = new UpdatePrenotazioneCommand(CatalogoPrenotazioni.getListaPrenotazioni(), p);
        invoker.executeCommand(command);
    }


    // COMANDI CLIENTE
    @Override
    public void addCliente(Cliente c) throws RemoteException {
        AddClienteCommand command = new AddClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void removeCliente(Cliente c) throws RemoteException {
        RemoveClienteCommand command = new RemoveClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void updateCliente(Cliente c) throws RemoteException {
        UpdateClienteCommand command = new UpdateClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void banCliente(Cliente c) throws RemoteException {
        BanCommand command = new BanCommand(c.getCf());
        invoker.executeCommand(command);
    }

    @Override
    public void unBanCliente(Cliente c) throws RemoteException {
        UnBanCommand command = new UnBanCommand(c.getCf());
        invoker.executeCommand(command);
    }
    //TODO: COMMAND PER RICEVERE LA LISTA UTENTI



    @Override
    public Impiegato authentication(String username, String password,String pwd2) throws RemoteException {
        log.info("SERVER: Username ricevuto: [" + username + "]");
        log.info("SERVER: Password ricevuta: [" + password + "]");

        try {
            if(Autentication.checkaccount(username, password, pwd2)){
                return Autentication.getImpiegato();
            }
        } catch (IllegalAccess e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // COMANDO UNDO
    @Override
    public void undoCommand() throws RemoteException {
        invoker.undoCommand();
    }

    // COMANDO REDO
    @Override
    public void redoCommand() throws RemoteException {
        invoker.redo();
    }

    /**
     * @param id della prenotazione.
     * @return l'oggetto {@code Prenotazione} tale per cui l'id corrisponde, {@code null} altrimenti.
     * @throws RemoteException .
     */
    @Override
    public Prenotazione getPrenotazioneById(int id) throws RemoteException {
        PrenotazioneDAO dao = new PrenotazioneDAO();
        Prenotazione prenotazione = null;
        try {
            prenotazione = dao.doRetriveByKey(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prenotazione;
    }

    /**
     * @param cf del cliente.
     * @return l'oggetto {@code Cliente} tale per cui il cf corrisponde, {@code null} altrimenti.
     * @throws RemoteException
     */
    @Override
    public Cliente getClienteByCf(String cf) throws RemoteException {
        if (cf == null || cf.isBlank())
            return null;
        ClienteDAO dao = new ClienteDAO();
        Cliente cliente = null;
        try {
            cliente = dao.doRetriveByKey(cf);
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
        return cliente;
    }
}