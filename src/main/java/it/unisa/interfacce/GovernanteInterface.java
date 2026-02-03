package it.unisa.interfacce;

import it.unisa.Common.Camera;

import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GovernanteInterface extends Remote, ObserverCamereInterface {


    /**
     * Restituisce il valore di listCamere.
     *
     * @post result != null
     */
    List<Camera> getListCamere() throws RemoteException;


    /**
     * Aggiorna lo stato di statoCamera.
     *
     * @pre c != null
     * @post result == true || result == false
     */
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
}
