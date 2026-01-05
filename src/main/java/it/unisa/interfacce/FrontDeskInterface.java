package it.unisa.interfacce;

import it.unisa.Common.Camera;
import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.gestionePrenotazioni.Prenotazione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FrontDeskInterface extends Remote
{
    void effettuaPrenotazione(String id, Cliente cliente, Stanza stanza) throws RemoteException;
    List<Prenotazione> getPrenotazioni() throws RemoteException;
    void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    Prenotazione getPrenotazione(String id) throws RemoteException;
    boolean aggiungiCliente(Cliente c);

}
