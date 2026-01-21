package it.unisa.interfacce;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ManagerInterface extends Remote
{
    void aggiungiImpiegato(Impiegato E)throws RemoteException;
    List<Impiegato> ottieniImpiegatiTutti()throws RemoteException;
    Impiegato ottieniImpiegatoDaId(String codiceFiscale)throws RemoteException;
    // i valori omessi vanno inseriti come null
    List<Impiegato> ottieniImpiegatiDaFiltro(String nome, String cognome, Ruolo ruolo, String sesso)throws RemoteException;
    boolean eliminaImpiegato(Impiegato E)throws RemoteException;
    String generatePassword()throws RemoteException;
    void modificaDatiImpiegato(Impiegato E)throws RemoteException;

    /**
     * Restituisce il conto economico sottoforma di entry di una mappa {@code Map<String, Double>}
     * Le chiavi e le corrispettive entry sono:
     * - prenotazioni: ricavi delle prenotazioni
     * - camere: ricavi derivanti dall'affitto delle camere
     * - servizi = ricavi derivanti dai servizi
     * - trattamenti = ricavi derivanti dai trattamenti
     * - passivita = totale ricavi dalle passività
     * @return {@code Map<String, Double>} ricavi-passività conto economico.
     */
    Map<String, Double> calcolaContoHotel()throws RemoteException;
}
