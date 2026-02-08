package it.unisa.interfacce;

import it.unisa.Common.Impiegato;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.persistent.util.Ruolo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ManagerInterface extends Remote
{
    // I valori omessi vanno inseriti come null
    List<Impiegato> filtroImpiegati(String nome, String sesso, Ruolo ruolo, String orderBy) throws RemoteException;

    // Comandi impiegato


    /**
     * Aggiunge impiegato alla collezione.
     *
     * @pre i != null
     * @post Impiegato..contains(i)
     */
    void addImpiegato(Impiegato i) throws RemoteException, IllegalAccess;


    /**
     * Rimuove impiegato dalla collezione.
     *
     * @pre i != null
     * @post not Impiegato..contains(i)
     */
    void removeImpiegato(Impiegato i) throws RemoteException, IllegalAccess;


    /**
     * Aggiorna i dati di impiegato.
     *
     * @pre i != null
     * @post Impiegato..contains(i)
     */
    void updateImpiegato(Impiegato i) throws RemoteException, IllegalAccess;

    // Recupera singolo impiegato
    Impiegato getImpiegatoByCF(String Cf) throws RemoteException;

    /**
     * Annulla il comando precedentemente eseguito.
     *
     */
    void undoCommand() throws RemoteException;

    /**
     * Esegue redo
     *
     */
    void redoCommand() throws RemoteException;

    /**
     *
     * @post result != null && result != ""
     *
     * @return password temporanea.
     * @throws RemoteException .
     */
    String generatePassword() throws RemoteException;

    /**
     * Restituisce il conto economico sottoforma di entry di una mappa {@code Map<String, Double>}
     * Le chiavi e le corrispettive entry sono:
     * - prenotazioni: ricavi delle prenotazioni
     * - camere: ricavi derivanti dall'affitto delle camere
     * - servizi = ricavi derivanti dai servizi
     * - trattamenti = ricavi derivanti dai trattamenti
     * - passivita = totale ricavi dalle passività
     *
     * @post result != null &&
     * result.contains("prenotazioni") &&
     * result.contains("camere") &&
     * result.contains("servizi") &&
     * result.contains("trattamenti") &&
     * result.contains("passivita")
     *
     * @return {@code Map<String, Double>} ricavi-passività conto economico.
     * @throws RemoteException .
     */
    Map<String, Double> calcolaContoHotel() throws RemoteException;
}
