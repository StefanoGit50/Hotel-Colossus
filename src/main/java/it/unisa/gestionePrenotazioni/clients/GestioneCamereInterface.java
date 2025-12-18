package it.unisa.gestionePrenotazioni.clients;

import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GestioneCamereInterface extends Remote
{
    public Stanza getStanza(int numero) throws RemoteException;
    public List<Stanza> getStanze() throws RemoteException;
    public void setOccupataGlobale(Stanza s) throws RemoteException;
    public void setLiberaGlobale(Stanza s) throws RemoteException;
    public void setOccupataPulizie(Stanza s) throws RemoteException;
    public void setLiberaPulizie(Stanza s) throws RemoteException;
}
