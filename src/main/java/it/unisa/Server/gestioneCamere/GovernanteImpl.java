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

public class Governante extends UnicastRemoteObject implements GovernanteInterface
{
    private static final long serialVersionUID = -34234234L;
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    private static List<Camera> camere = new ArrayList<>();

    public Governante() throws RemoteException {}

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

    @Override
    public void setStatoInPulizia(Camera c)
    {
        c.setStatoCamera(Stato.InPulizia);
    }

    @Override
    public void setStatoOutOfOrder(Camera c)
    {
        c.setStatoCamera(Stato.OutOfOrder);
    }

    @Override
    public List<Camera> getListCamere() {
        return camere;
    }

    static void main(String[] args) throws RemoteException
    {
        logger.info("Ottengo le camere...");

        for(int i=101; i<600; i++)
        {
            camere.add(new Camera(i));
            IO.println(camere.toString());
        }

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
            Governante gc = new Governante();
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