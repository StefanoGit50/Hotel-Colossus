package it.unisa.interfacce;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.rmi.Remote;
import java.util.List;

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
    List<Double> calcolaContoHotel();
}
