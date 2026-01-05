package it.unisa.interfacce;

import it.unisa.Common.Camera;

import it.unisa.Server.ObserverCamereInterface;

import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.gestionePrenotazioni.Prenotazione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FrontDeskInterface extends Remote, ObserverCamereInterface
{
    void effettuaPrenotazione(String id, Cliente cliente, Camera camera) throws RemoteException;
    List<Prenotazione> getPrenotazioni() throws RemoteException;
    void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    Prenotazione getPrenotazione(String id) throws RemoteException;
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
    List<Camera> getCamere() throws RemoteException;

}
