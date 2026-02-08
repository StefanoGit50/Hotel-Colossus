package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Server.Autentication.Autentication;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.Others.RetrieveAllCamereCommand;
import it.unisa.Server.commandPattern.Others.RetrieveAllServiziCommand;
import it.unisa.Server.commandPattern.Others.RetrieveAllTrattamentiCommand;
import it.unisa.Server.commandPattern.Others.RetriveAllActiveCommand;
import it.unisa.interfacce.*;
import it.unisa.Server.commandPattern.*;
import it.unisa.Common.*;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.commandPattern.CatalogoClientiCommands.*;
import it.unisa.Server.commandPattern.CatalogoPrenotazioniCommands.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.obj.catalogues.CatalogueUtils;
import org.apache.logging.log4j.LogManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

public class FrontDesk extends UnicastRemoteObject implements FrontDeskInterface {
    private CatalogoCamere camList = new CatalogoCamere();
    private static final int RMI_PORT = 1099;
    private static boolean isServerRunning = false;

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FrontDesk.class);

    public FrontDesk() throws RemoteException {
        super();
        if(!isServerRunning){
            startServer(this);
            isServerRunning = true;
        }
    }

    //costruttore di Testing
    public FrontDesk(Invoker invoker,  CatalogoCamere catalogoCamere) throws RemoteException {
        super();
        this.invoker = invoker;
        this.camList = catalogoCamere;
    }


    private static void startServer(FrontDesk instance) throws RemoteException {
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



            log.info("Effettuo il rebind di gestione prenotazioni...");
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni", instance);
            log.info("✓ Gestore prenotazioni registrato con successo!");
            log.info("✓ Servizio 'GestionePrenotazioni' pronto");
            log.info("Server in attesa di connessioni...");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n--- SHUTDOWN IN CORSO ---");
                try {

                    Naming.unbind("rmi://localhost:" + RMI_PORT + "/GestionePrenotazioni");

                    java.rmi.server.UnicastRemoteObject.unexportObject(instance, true);
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

    public static void main(String[] args) throws RemoteException {
        FrontDesk instance = new FrontDesk();
    }

    /// ////////////////////////////////////////////////////////////////////////////////////

    // Invoker --> mantiene l'ordine delle chiamate ai comandi
    private Invoker invoker = new Invoker();
    // Cataloghi

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        log.info("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return camList.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return CatalogoCamere.getLastModified();
    }

    // COMANDI PRENOTAZIONE
    @Override
    public void addPrenotazione(Prenotazione p) throws RemoteException, IllegalAccess {
        CatalogueUtils.checkNull(p);                 // Lancia InvalidInputException
        CatalogoPrenotazioni.checkPrenotazione(p);   // Lancia InvalidInputException
        AddPrenotazioneCommand command = new AddPrenotazioneCommand(p);
        invoker.executeCommand(command);

    }

    @Override
    public void removePrenotazione(Prenotazione p) throws RemoteException, IllegalAccess {

        RemovePrenotazioneCommand command = new RemovePrenotazioneCommand(p);
        invoker.executeCommand(command);
    }

    @Override
    public void updatePrenotazione(Prenotazione p) throws RemoteException, IllegalAccess {
         UpdatePrenotazioneCommand command = new UpdatePrenotazioneCommand(p);
         invoker.executeCommand(command);
    }


    // COMANDI CLIENTE
    @Override
    public void addCliente(Cliente c) throws RemoteException, IllegalAccess {
        AddClienteCommand command = new AddClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void removeCliente(Cliente c) throws RemoteException, IllegalAccess {
        RemoveClienteCommand command = new RemoveClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void updateCliente(Cliente c) throws RemoteException, IllegalAccess {
        UpdateClienteCommand command = new UpdateClienteCommand(c);
        invoker.executeCommand(command);
    }

    @Override
    public void banCliente(Cliente c) throws RemoteException, IllegalAccess {
        BanCommand command = new BanCommand(c.getCf());
        invoker.executeCommand(command);
    }

    @Override
    public void unBanCliente(Cliente c) throws RemoteException, IllegalAccess {
        UnBanCommand command = new UnBanCommand(c.getCf());
        invoker.executeCommand(command);
    }

    @Override
    public ArrayList<Prenotazione> getListaPrenotazioni() throws RemoteException, IllegalAccess {
        RetrieveAllPCommand command = new RetrieveAllPCommand();
        invoker.executeCommand(command);
        return command.getPrenotazioni();
    }

    /**
     * Recupera la lista {@code list} di tutte le prenotazioni attive presenti nel sistema. Una prenotazione è considerata
     * "attiva" se la data di arrivo prevista per il cliente è antecedente o uguale alla data odierna e se la data di
     * partenza prevista per il cliente è successiva alla data odierna.
     *
     * @throws RemoteException .
     * @post {@code list} di oggetti {@code Prenotazione} p tale per cui p.dataInizio <= dataOrdierna && p.dataFine > dataOdierna.
     */
    @Override
    public ArrayList<Prenotazione> getListaPrenotazioniAttive() throws RemoteException, IllegalAccess {
        RetriveAllActiveCommand command = new RetriveAllActiveCommand();
        invoker.executeCommand(command);
        return command.getPrenotazioni();
    }

    @Override
    public ArrayList<Cliente> getListaClienti() throws RemoteException, IllegalAccess {
        RetrieveAllCCommand command = new RetrieveAllCCommand();
        invoker.executeCommand(command);
        return command.getClienti();
    }

    /**
     * Recupera la lista {@code list} di tutti i servizi presenti nel sistema.
     *
     * @throws RemoteException .
     * @throws IllegalAccess   .
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcun servizio) e # Servizi.
     */
    @Override
    public ArrayList<Servizio> getListaServizi() throws RemoteException, IllegalAccess {
        RetrieveAllServiziCommand command = new RetrieveAllServiziCommand();
        invoker.executeCommand(command);
        return command.getServizi();
    }

    /**
     * Recupera la lista {@code list} di tutti i trattamenti presenti nel sistema.
     *
     * @throws RemoteException .
     * @throws IllegalAccess   .
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcun trattamento) e # Trattamenti.
     */
    @Override
    public ArrayList<Trattamento> getListaTrattamenti() throws RemoteException, IllegalAccess {
        RetrieveAllTrattamentiCommand command = new RetrieveAllTrattamentiCommand();
        invoker.executeCommand(command);
        return command.getTrattamenti();
    }

    /**
     * Recupera la lista {@code list} di tutte le camere presenti nel sistema.
     *
     * @throws RemoteException .
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcuna camere) e # camere.
     */
    @Override
    public ArrayList<Camera> getListaCamere() throws RemoteException, IllegalAccess {
        RetrieveAllCamereCommand command = new RetrieveAllCamereCommand();
        invoker.executeCommand(command);
        return command.getCamere();
    }

    @Override
    public boolean checkOut(Prenotazione p)throws RemoteException{
        Prenotazione p1 =p.clone();
        p.setStatoPrenotazione(false);
        CatalogoPrenotazioni.UpdatePrenotazioni(p);
        Prenotazione p2= CatalogoPrenotazioni.getPrenotazione(p.getIDPrenotazione());
        if(!p1.equals(p2)) return true;
        else return false;
    }

    @Override
    public boolean checkIn(Prenotazione p) throws  RemoteException{
        Prenotazione p1 =p.clone();
        p.setCheckIn(true);
        CatalogoPrenotazioni.UpdatePrenotazioni(p);
        Prenotazione p2= CatalogoPrenotazioni.getPrenotazione(p.getIDPrenotazione());
        if(!p1.equals(p2)) return true;
        else return false;
    }

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
        return CatalogoPrenotazioni.getPrenotazione(id);
    }


}