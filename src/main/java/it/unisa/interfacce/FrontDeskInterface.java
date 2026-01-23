package it.unisa.interfacce;

import it.unisa.Common.*;

import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



    public interface FrontDeskInterface extends Remote, ObserverCamereInterface
    {
        List<Prenotazione> getPrenotazioni() throws RemoteException;
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

        // Comando undo
        void undoCommand() throws RemoteException;

        // Comando redo
        void redoCommand() throws RemoteException;
    }


