package it.unisa.interfacce;

import it.unisa.Common.*;

import it.unisa.Server.IllegalAccess;
import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface FrontDeskInterface extends Remote, ObserverCamereInterface
{
    // Comandi camere
    List<Prenotazione> getPrenotazioni() throws RemoteException;
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;
    List<Camera> getCamere() throws RemoteException;

    // Comandi prenotazione
    void addPrenotazione(Prenotazione p) throws RemoteException;
    void removePrenotazione(Prenotazione p) throws RemoteException;
    void updatePrenotazione(Prenotazione p) throws RemoteException;
    // Filtro Prenotazioni
    List<Prenotazione> filterPrenotazioni(String nome, String cognome, LocalDate dataInizioSoggiorno,
                                     LocalDate dataFineSoggiorno, String orderBy) throws RemoteException;

    // Comandi cliente
    void addCliente(Cliente c) throws RemoteException;
    void removeCliente(Cliente c) throws RemoteException;
    void updateCliente(Cliente c) throws RemoteException;
    void banCliente(Cliente c) throws RemoteException;
    void unBanCliente(Cliente c) throws RemoteException;
    // Filtro clienti
    List<Cliente> filterClienti(String nome, String cognome, String nazionalita, LocalDate dataNascita, Boolean blackListed, String orderBy)
            throws RemoteException;

    //autenticazione
    Impiegato authentication(String username, String password,String pwd2) throws RemoteException, IllegalAccess;

    // Comando undo
    void undoCommand() throws RemoteException;

    // Comando redo
    void redoCommand() throws RemoteException;

    // MetodiFindSingoli
    Prenotazione getPrenotazioneById(int id) throws RemoteException;
    Cliente getClienteByCf(String cf) throws RemoteException;
}
