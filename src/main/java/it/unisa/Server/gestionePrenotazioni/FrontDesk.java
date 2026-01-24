package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Server.command.*;
import it.unisa.Common.*;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.CatalogoClientiCommands.*;
import it.unisa.Server.command.CatalogoImpiegatiCommands.*;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.*;
import it.unisa.Server.persistent.obj.catalogues.*;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.interfacce.FrontDeskInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
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
    public List<Prenotazione> getPrenotazioni() throws RemoteException
    {
        return CatalogoPrenotazioni.getListaPrenotazioni();
    }


    public static void main(String[] args)
    {
        try
        {
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
        AddPrenotazioneCommand command = new AddPrenotazioneCommand(catalogoPrenotazioni, p);
        invoker.executeCommand(command);
    }

    @Override
    public void removePrenotazione(Prenotazione p) throws RemoteException {
        RemovePrenotazioneCommand command = new RemovePrenotazioneCommand(catalogoPrenotazioni, p);
        invoker.executeCommand(command);
    }

    @Override
    public void updatePrenotazione(Prenotazione p) throws RemoteException {
        UpdatePrenotazioneCommand command = new UpdatePrenotazioneCommand(catalogoPrenotazioni, p);
        invoker.executeCommand(command);
    }

    // COMANDI CLIENTE
    @Override
    public void addCliente(Cliente c) throws RemoteException {
        AddClienteCommand command = new AddClienteCommand(catalogoClienti, c);
        invoker.executeCommand(command);
        if(!CatalogoClienti.getListaClienti().isEmpty()){
            CatalogoClienti.getListaClienti().forEach(o -> System.out.println(o.toString()));
        } else {
            System.out.println("lista cliente vuoto");
        }
    }

    @Override
    public void removeCliente(Cliente c) throws RemoteException {
        RemoveClienteCommand command = new RemoveClienteCommand(catalogoClienti, c);
        invoker.executeCommand(command);
    }

    @Override
    public void updateCliente(Cliente c) throws RemoteException {
        UpdateClienteCommand command = new UpdateClienteCommand(catalogoClienti, c);
        invoker.executeCommand(command);
    }

    @Override
    public void banCliente(Cliente c) throws RemoteException {
        BanCommand command = new BanCommand(catalogoClienti, c.getCf());
        invoker.executeCommand(command);
    }

    @Override
    public void unBanCliente(Cliente c) throws RemoteException {
        UnBanCommand command = new UnBanCommand(catalogoClienti, c.getCf());
        invoker.executeCommand(command);
    }

    /**
     * @param nome nome del cliente.
     * @param cognome cognome del cliente.
     * @param nazionalita nazionalità del cliente.
     * @param dataNascita data di nascita (gg/mm/aaaa) del cliente.
     * @param blackListed stato di ban del ciente.
     * @param orderBy criterio di ordinamento [attributo][ASC/DESC].
     * @return lista di oggetti {@code Cliente} che rispettano i parametri della ricerca.
     * @throws RemoteException .
     */
    @Override
    public List<Cliente> filterClienti(String nome, String cognome, String nazionalita, LocalDate dataNascita, Boolean blackListed, String orderBy)
            throws RemoteException {

        CatalogoClienti.checkCliente(nome, cognome, nazionalita,  dataNascita, blackListed);
        ClienteDAO clienteDAO = null;
        try {
            clienteDAO = new ClienteDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clienteDAO.doFilter(nome, cognome, nazionalita, dataNascita, blackListed, orderBy);
    }


    // COMANDO UNDO
    @Override
    public void undoCommand() throws RemoteException {
        invoker.undoCommand();
        if(!CatalogoClienti.getListaClienti().isEmpty()){
            System.out.println("UNDO");
            CatalogoClienti.getListaClienti().forEach(o -> System.out.println(o.toString()));
        } else {
            System.out.println("UNDO: lista cliente vuoto");
        }
    }

    // COMANDO REDO
    @Override
    public void redoCommand() throws RemoteException {
        invoker.redo();
        if(!CatalogoClienti.getListaClienti().isEmpty()){
            System.out.println("REDO");
            CatalogoClienti.getListaClienti().forEach(o -> System.out.println(o.toString()));
        } else {
            System.out.println("REDO: lista cliente vuoto");
        }
    }
}