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
    /**
     * Restituisce il valore di prenotazioni.
     *
     * @post result != null
     */
    List<Prenotazione> getPrenotazioni() throws RemoteException;


    /**
     * Aggiorna lo stato di statoCamera.
     *
     * @pre c != null
     * @post result == true || result == false
     */
    boolean aggiornaStatoCamera(Camera c) throws RemoteException;


    /**
     * Restituisce il valore di camere.
     *
     * @post result != null
     */
    List<Camera> getCamere() throws RemoteException;

    // Comandi prenotazione
    /**
     * Aggiunge prenotazione alla collezione.
     *
     * @pre p != null
     * @post Prenotazione..contains(p)
     */
    void addPrenotazione(Prenotazione p) throws RemoteException;


    /**
     * Rimuove prenotazione dalla collezione.
     *
     * @pre p != null
     * @post not Prenotazione..contains(p)
     */
    void removePrenotazione(Prenotazione p) throws RemoteException;

    /**
     * Aggiorna i dati di prenotazione.
     *
     * @pre p != null
     * @post Prenotazione..contains(p)
     */
    void updatePrenotazione(Prenotazione p) throws RemoteException;
    // Filtro Prenotazioni

    // Comandi cliente
    /**
     * Aggiunge cliente alla collezione.
     *
     * @pre c != null
     * @post Cliente..contains(c)
     */
    void addCliente(Cliente c) throws RemoteException;


    /**
     * Rimuove cliente dalla collezione.
     *
     * @pre c != null
     * @post not Cliente..contains(c)
     */
    void removeCliente(Cliente c) throws RemoteException;


    /**
     * Aggiorna i dati di cliente.
     *
     * @pre c != null
     * @post Cliente..contains(c)
     */
    void updateCliente(Cliente c) throws RemoteException;


    /**
     * Aggiunge il cliente alla blacklist.
     *
     * @pre c != null
     * @post c.isBlacklisted == true
     */
    void banCliente(Cliente c) throws RemoteException;


    /**
     * Rimuove il cliente dalla blacklist.
     *
     * @pre c != null
     * @post c.isBlacklisted == false
     */
    void unBanCliente(Cliente c) throws RemoteException;

    //autenticazione
    Impiegato authentication(String username, String password,String pwd2) throws RemoteException, IllegalAccess;

    // Comando undo
    /**
     * Annulla il comando precedentemente eseguito.
     */
    void undoCommand() throws RemoteException;

    // Comando redo
    /**
     * Effettua redo.
     */
    void redoCommand() throws RemoteException;


    // MetodiFindSingoli
    /**
     * Restituisce prenotazione in base all'id.
     *
     * @pre id != null
     * @post result != null
     */
    Prenotazione getPrenotazioneById(int id) throws RemoteException;


    /**
     * Restituisce cliente in base al cf.
     *
     * @pre cf != null
     * @post result != null
     */
    Cliente getClienteByCf(String cf) throws RemoteException;
}
