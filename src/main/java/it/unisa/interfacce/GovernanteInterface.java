package it.unisa.interfacce;

import it.unisa.Common.Camera;

import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



public interface GovernanteInterface extends Remote, ObserverCamereInterface {
    List<Camera> getListCamere() throws RemoteException;
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
}
