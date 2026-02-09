package it.unisa.Server.gestioneCamere;

import it.unisa.Common.Camera;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.ObserverPattern.ObserverCamereInterface;
import it.unisa.Server.commandPattern.Invoker;
import it.unisa.Server.commandPattern.Others.RetrieveAllCamereCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.interfacce.GovernanteInterface;
import org.apache.logging.log4j.LogManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class Governante extends UnicastRemoteObject implements GovernanteInterface, ObserverCamereInterface {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Governante.class);

    private static final long serialVersionUID = -34234234L;
    private static final int RMI_PORT = 1099;
    private static CatalogoCamere catalogoCamere = new CatalogoCamere();

    private static Invoker invoker;
    private Camera camera;


    public Governante() throws RemoteException {
        super();
        invoker = new Invoker();
    }

    @Override
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {
        log.info("camera passata = "+c.getNumeroCamera()+ c.getStatoCamera());
        return catalogoCamere.aggiornaStatoCamera(c);
    }

    // manda al client la camera ricevuta dall'update e il client dovrà aggiornare la sua lista da mostrare.
    @Override
    public Camera update() throws RemoteException {
        return CatalogoCamere.getLastModified();
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

    public static void main(String[] args) throws RemoteException {
        log.info("Ottengo le camere...");

            log.info("Camere ottenute!");

            try {
                // IMPORTANTE: Avvia l'RMI registry programmaticamente
                log.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");
                Registry registry;
                try {
                    // Prova a creare un nuovo registry
                    registry = LocateRegistry.createRegistry(RMI_PORT);
                    log.info("RMI Registry creato con successo!");
                } catch (RemoteException e) {
                    // Se esiste già, ottieni il riferimento
                    registry = LocateRegistry.getRegistry(RMI_PORT);
                    log.info("Connesso a RMI Registry esistente");
                }

                log.info("Sto creando il gestore camere...");
                Governante gc = new Governante();
                log.info("Gestore camere creato... ");

                log.info("Effettuo il rebind del gestore camere...");
                Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestoreCamere", gc);
                log.info("Il gestore camere è pronto e registrato!");
                log.info("Server in attesa di connessioni...");

                // Mantieni il server in esecuzione
                Thread.currentThread().join();
            } catch (Exception e) {
                log.info("Errore durante l'avvio del server RMI: " + e.getMessage());
                e.printStackTrace();
            }

        }

}