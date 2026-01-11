package it.unisa.interfacce;

import it.unisa.Common.*;

import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface FrontDeskInterface extends Remote, ObserverCamereInterface
{
    //void effettuaPrenotazione(String id, Cliente cliente, Camera camera) throws RemoteException, SQLException;
    List<Prenotazione> getPrenotazioni() throws RemoteException;
    void cancellaPrenotazione(Prenotazione p) throws RemoteException;
    Prenotazione getPrenotazione(String id) throws RemoteException;
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
    List<Camera> getCamere() throws RemoteException;

    // Comandi prenotazione
    void addPrenotazione(Prenotazione p) throws RemoteException;
    void removePrenotazione(Prenotazione p) throws RemoteException;
    void updatePrenotazione(Prenotazione p) throws RemoteException;

    // Comandi cliente
    void addCliente(Cliente c) throws RemoteException;
    void removeCliente(Cliente c) throws RemoteException;
    void updateCliente(Cliente c) throws RemoteException;
    void banCliente(Cliente c) throws RemoteException;
    void unBanCliente(Cliente c) throws RemoteException;

    // Comando impiegato
    void addImpiegato(Impiegato i) throws RemoteException;
    void removeImpiegato(Impiegato i) throws RemoteException;
    void updateImpiegato(Impiegato i) throws RemoteException;

    // Comando undo
    void undoCommand() throws RemoteException;

    // Comando redo
    void redoCommand() throws RemoteException;

}
