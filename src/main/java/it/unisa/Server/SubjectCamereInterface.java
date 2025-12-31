package it.unisa.Server;

import it.unisa.Common.Camera;

import java.rmi.RemoteException;

public interface SubjectCamereInterface {
    void attach(ObserverCamereInterface observer);
    void detach(ObserverCamereInterface observer);
    void notifyObservers(Camera camera) throws RemoteException;
}
