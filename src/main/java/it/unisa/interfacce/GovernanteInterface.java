package it.unisa.interfacce;

import it.unisa.Server.gestioneCamere.Stanza;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GovernanteInterface extends Remote
{
    Stanza getStanza(int numero) throws RemoteException;
    List<Stanza> getStanze() throws RemoteException;
    void setOccupataGlobale(Stanza s) throws RemoteException;
    void setLiberaGlobale(Stanza s) throws RemoteException;
    void setOccupataPulizie(Stanza s) throws RemoteException;
    void setLiberaPulizie(Stanza s) throws RemoteException;
}
