package it.unisa.Server.gestioneCamere;

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

    private static List<Stanza> stanze = new ArrayList<>();

    public Governante() throws RemoteException {}

    @Override
    public Stanza getStanza(int numero)
    {
        for(Stanza s: stanze)
        {
            if(s.getNumero() == numero)
                return s;
        }
        return new Stanza();
    }

    @Override
    public List<Stanza> getStanze()
    {
        return stanze;
    }

    @Override
    public void setOccupataGlobale(Stanza s)
    {
        for(Stanza s1: getStanze())
        {
            if(s.getNumero() == s1.getNumero())
                if(s1.getStatoGlobale().equals("libera"))
                    s1.setStatoGlobale("occupata");
        }
    }

    @Override
    public void setLiberaGlobale(Stanza s)
    {
        for(Stanza s1: getStanze())
        {
            if(s.getStatoGlobale().equals("occupata"))
                s.setStatoGlobale("libera");
        }
    }

    @Override
    public void setOccupataPulizie(Stanza s)
    {
        for(Stanza s1: getStanze())
        {
            if(s.getStatoPulizie().equals("libera"))
                s.setStatoPulizie("occupata");
        }
    }

    @Override
    public void setLiberaPulizie(Stanza s)
    {
        for(Stanza s1: getStanze())
        {
            if(s.getStatoPulizie().equals("occupata"))
                s.setStatoPulizie("libera");
        }
    }

    public static void main(String[] args) throws RemoteException
    {
        logger.info("Ottengo le camere...");

        for(int i=101; i<600; i++)
        {
            stanze.add(new Stanza(i));
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