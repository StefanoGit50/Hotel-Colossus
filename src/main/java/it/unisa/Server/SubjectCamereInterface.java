package it.unisa.Server;

import java.rmi.RemoteException;

public interface SubjectCamereInterface {
    void attach(ObserverCamereInterface observer);
    void detach(ObserverCamereInterface observer);
    void notifyObservers() throws RemoteException;
}
