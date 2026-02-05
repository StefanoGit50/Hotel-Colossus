package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Server.Autentication.Autentication;
import it.unisa.Server.IllegalAccess;
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
import it.unisa.interfacce.FrontDeskInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface {
    static Logger logger = Logger.getLogger("global");
    private CatalogoCamere camList = new CatalogoCamere();
    private static final int RMI_PORT = 1099;

    public FrontDesk() throws RemoteException {
        super();
    }

    //costruttore di Testing
    public FrontDesk(Invoker invoker, CatalogoPrenotazioni catalogoPrenotazioni, CatalogoClienti catalogoClienti, CatalogoCamere catalogoCamere) throws RemoteException {
        super();
        this.invoker = invoker;
        this.catalogoPrenotazioni = catalogoPrenotazioni;
        this.catalogoClienti = catalogoClienti;
        this.camList = catalogoCamere;
    }

    public List<Camera> getCamere(){
       return camList.getListaCamere();
    }

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        Logger.getLogger("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return camList.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return camList.getLastModified();
    }

    @Override
    public List<Prenotazione> getPrenotazioni() throws RemoteException{
        return catalogoPrenotazioni.getListaPrenotazioni();
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
//        UpdatePrenotazioneCommand command = new UpdatePrenotazioneCommand(catalogoPrenotazioni, p);
//        invoker.executeCommand(command);
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
    public Impiegato authentication(String username, String password,String pwd2) throws RemoteException, IllegalAccess {
        if( Autentication.checkaccount(username, password, pwd2))
            return Autentication.getImpiegato();
        else  return null;
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