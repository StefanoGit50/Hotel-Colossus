package it.unisa.gestionePrenotazioni.gestionePrenotazioni;

import it.unisa.gestionePrenotazioni.clients.GestioneCamereInterface;
import it.unisa.gestionePrenotazioni.clients.GestionePrenotazioniInterface;
import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GestionePrenotazioni extends UnicastRemoteObject implements GestionePrenotazioniInterface
{
    static Logger logger = Logger.getLogger("global");
    
    List<Prenotazione> prenotazioni = new ArrayList<>();
    
    public GestionePrenotazioni() throws RemoteException {}

    @Override
    public void effettuaPrenotazione(String id, Cliente cliente, Stanza stanza)
    {
        if(stanza.getStatoGlobale().equals("libera"))
        {
            Prenotazione p = new Prenotazione(id, cliente, stanza);
            prenotazioni.add(p);
            
            try{
                GestioneCamereInterface gs = (GestioneCamereInterface) Naming.lookup("rmi://localhost/GestoreCamere");
                gs.setOccupataGlobale(stanza);
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        } 
        else
        {
            System.err.println("Stanza " + stanza.getNumero() + " occupata!");
        }
    }
    
    @Override
    public List<Prenotazione> getPrenotazioni()
    {
        return prenotazioni;
    }
    
    @Override
    public void cancellaPrenotazione(Prenotazione p)
    {
        for(Prenotazione p2: prenotazioni)
        {
            if(p2.equals(p))
            {
                prenotazioni.remove(p);
                return;
            }
            else
                System.err.println("Prenotazione non trovata");
        }
    }
    
    @Override
    public Prenotazione getPrenotazione(String id)
    {
        for(Prenotazione p2: prenotazioni)
        {
            if(p2.getId().equals(id))
            {
                return p2;
            }   
        }
        System.err.println("Prenotazione non trovata");
        return null;
    }
    
    public static void main(String[] args)
    {
        try
        {
            logger.info("Genero il gestore prenotazioni...");
            GestionePrenotazioni gp = new GestionePrenotazioni();
            logger.info("Gestore prenotazioni ottenuto");
            
            logger.info("Effettuo il rebind di gestione prenotazioni...");
            Naming.rebind("GestionePrenotazioni", gp);
            logger.info("Gestore prenotazioni pronto...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
