package it.unisa.interfacce;

import it.unisa.Common.Camera;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.ObserverCamereInterface;

import it.unisa.Server.gestioneClienti.Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface FrontDeskInterface extends Remote, ObserverCamereInterface
{
    void effettuaPrenotazione(String id, Cliente cliente, Camera camera) throws RemoteException, SQLException;
    List<Prenotazione> getPrenotazioni() throws RemoteException;
    void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    Prenotazione getPrenotazione(String id) throws RemoteException;
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
    List<Camera> getCamere() throws RemoteException;
}
