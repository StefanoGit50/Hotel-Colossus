package it.unisa.interfacce;

import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.gestionePrenotazioni.Prenotazione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FrontDeskInterface extends Remote, ObserverCamereInterface
{
    public void effettuaPrenotazione(String id, Cliente cliente, Stanza stanza) throws RemoteException;
    public List<Prenotazione> getPrenotazioni() throws RemoteException;
    public void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    public Prenotazione getPrenotazione(String id) throws RemoteException;
}
