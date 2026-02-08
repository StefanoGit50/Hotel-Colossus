package it.unisa.interfacce;

import it.unisa.Common.Camera;

import it.unisa.Server.IllegalAccess;
import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GovernanteInterface extends Remote, ObserverCamereInterface {

    /**
     * Aggiorna lo stato di statoCamera.
     *
     * @pre c != null
     * @post result == true || result == false
     */
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;

    /**
     * Recupera la lista {@code list} di tutte le camere presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcuna camere) e # camere.
     * @throws RemoteException .
     */
    ArrayList<Camera> getListaCamere() throws RemoteException, IllegalAccess;

}
