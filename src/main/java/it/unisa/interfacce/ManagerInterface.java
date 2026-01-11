package it.unisa.interfacce;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

public interface ManagerInterface extends Remote
{
    void aggiungiImpiegato(Impiegato E);
    List<Impiegato> ottieniImpiegatiTutti();
    Impiegato ottieniImpiegatoDaId(String codiceFiscale);
    // i valori omessi vanno inseriti come null
    List<Impiegato> ottieniImpiegatiDaFiltro(String nome, String cognome, Ruolo ruolo, String sesso);
    boolean eliminaImpiegato(Impiegato E);
    String generatePassword();
    void modificaDatiImpiegato(Impiegato E);

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
    Map<String, Double> calcolaContoHotel();
}
