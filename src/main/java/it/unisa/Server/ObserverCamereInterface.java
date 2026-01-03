package it.unisa.Server;

import it.unisa.Common.Camera;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverCamereInterface extends Remote {
    Camera update() throws RemoteException;
}
