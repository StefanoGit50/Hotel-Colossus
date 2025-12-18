package it.unisa.gestionePrenotazioni.clients;

import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;
import it.unisa.gestionePrenotazioni.gestionePrenotazioni.Cliente;
import it.unisa.gestionePrenotazioni.gestionePrenotazioni.Prenotazione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GestionePrenotazioniInterface extends Remote
{
    public void effettuaPrenotazione(String id, Cliente cliente, Stanza stanza) throws RemoteException;
    public List<Prenotazione> getPrenotazioni() throws RemoteException;
    public void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    public Prenotazione getPrenotazione(String id) throws RemoteException;
}
