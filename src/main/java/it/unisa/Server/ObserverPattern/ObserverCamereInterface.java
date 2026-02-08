package it.unisa.Server.ObserverPattern;

import it.unisa.Common.Camera;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverCamereInterface extends Remote {


    /**
     * Aggiorna i dati di l'elemento.
     *
     * @post result != null
     * @return Camera
     * @throws RemoteException
     */
    Camera update() throws RemoteException;
}
