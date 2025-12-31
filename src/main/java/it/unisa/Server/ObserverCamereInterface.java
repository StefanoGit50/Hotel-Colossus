package it.unisa.Server;

import it.unisa.Common.Camera;

import java.rmi.RemoteException;

public interface ObserverCamereInterface {
    Camera update(Camera camera) throws RemoteException;
}
