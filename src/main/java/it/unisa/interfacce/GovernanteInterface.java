package it.unisa.interfacce;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GovernanteInterface extends Remote
{
    List<Camera> getListCamere() throws RemoteException;

    Camera getCamera(int numero) throws RemoteException;

    void setStatoInPulizia(Camera c) throws RemoteException;

    void setStatoOutOfOrder(Camera c) throws RemoteException;

    void setStatoInServizio(Camera c) throws RemoteException;

    void setStatoLibera(Camera c) throws RemoteException;
}
