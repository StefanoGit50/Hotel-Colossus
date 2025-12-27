package it.unisa.interfacce;

import it.unisa.Common.Impiegato;

import java.rmi.Remote;
import java.util.List;

public interface ManagerInterface extends Remote {
    void aggiungiImpiegato(Impiegato E);
    boolean eliminaImpiegato(Impiegato E);
    String generatePassword();
    void modificaDatiImpiegato(Impiegato E);
    List<Double> calcolaContoHotel();
}
