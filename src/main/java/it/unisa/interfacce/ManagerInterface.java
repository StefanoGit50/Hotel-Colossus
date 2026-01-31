package it.unisa.interfacce;

import it.unisa.Common.Impiegato;
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
    void addImpiegato(Impiegato i) throws RemoteException;
    void removeImpiegato(Impiegato i) throws RemoteException;
    void updateImpiegato(Impiegato i) throws RemoteException;

    // Comando undo
    void undoCommand() throws RemoteException;

    // Comando redo
    void redoCommand() throws RemoteException;

    /**
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
     * @return {@code Map<String, Double>} ricavi-passività conto economico.
     * @throws RemoteException .
     */
    Map<String, Double> calcolaContoHotel() throws RemoteException;
}
