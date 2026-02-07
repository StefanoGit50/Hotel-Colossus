package it.unisa.interfacce;

import it.unisa.Common.*;

import it.unisa.Server.IllegalAccess;
import it.unisa.Server.ObserverCamereInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
    void addPrenotazione(Prenotazione p) throws RemoteException, IllegalAccess;


    /**
     * Rimuove prenotazione dalla collezione.
     *
     * @pre p != null
     * @post not Prenotazione..contains(p)
     */
    void removePrenotazione(Prenotazione p) throws RemoteException, IllegalAccess;

    /**
     * Aggiorna i dati di prenotazione.
     *
     * @pre p != null
     * @post Prenotazione..contains(p)
     */
    void updatePrenotazione(Prenotazione p) throws RemoteException, IllegalAccess;
    // Filtro Prenotazioni

    // Comandi cliente
    /**
     * Aggiunge cliente alla collezione.
     *
     * @pre c != null
     * @post Cliente..contains(c)
     */
    void addCliente(Cliente c) throws RemoteException, IllegalAccess;


    /**
     * Rimuove cliente dalla collezione.
     *
     * @pre c != null
     * @post not Cliente..contains(c)
     */
    void removeCliente(Cliente c) throws RemoteException, IllegalAccess;

    //TODO: Aggiungere comando per retrieve del cliente (singolo / lista)

    /**
     * Aggiorna i dati di cliente.
     *
     * @pre c != null
     * @post Cliente..contains(c)
     */
    void updateCliente(Cliente c) throws RemoteException, IllegalAccess;

    //TODO: Aggiungere comando per retrieve delle prenotazione (singolo / lista)

    /**
     * Aggiunge il cliente alla blacklist.
     *
     * @pre c != null
     * @post c.isBlacklisted == true
     */
    void banCliente(Cliente c) throws RemoteException, IllegalAccess;


    /**
     * Rimuove il cliente dalla blacklist.
     *
     * @pre c != null
     * @post c.isBlacklisted == false
     */
    void unBanCliente(Cliente c) throws RemoteException, IllegalAccess;

    // Recuperare tutte le liste

    /**
     * Recupera la lista {@code list} di tutte le prenotazioni presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcuna prenotazione) e # prenotazioni.
     * @throws RemoteException .
     */
    ArrayList<Prenotazione> getListaPrenotazioni() throws RemoteException, IllegalAccess;

    /**
     * Recupera la lista {@code list} di tutti i clienti presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcun cliente) e # clienti.
     * @throws RemoteException .
     */
    ArrayList<Cliente> getListaClienti() throws RemoteException, IllegalAccess;

    /**
     * Recupera la lista {@code list} di tutti i servizi presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcun servizio) e # Servizi.
     * @throws RemoteException .
     * @throws IllegalAccess .
     */
    ArrayList<Servizio>  getListaServizi() throws RemoteException, IllegalAccess;

    /**
     * Recupera la lista {@code list} di tutti i trattamenti presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcun trattamento) e # Trattamenti.
     * @throws RemoteException .
     * @throws IllegalAccess .
     */
    ArrayList<Trattamento>  getListaTrattamenti() throws RemoteException, IllegalAccess;

    /**
     * Recupera la lista {@code list} di tutte le camere presenti nel sistema.
     *
     * @post {@code list} ha dimensioni tra 0 (se all'avvio non è presente alcuna camere) e # camere.
     * @throws RemoteException .
     */
    ArrayList<Camera> getListaCamere() throws RemoteException, IllegalAccess;


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


}
