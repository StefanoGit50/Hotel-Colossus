package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Impiegato;
import it.unisa.Common.Impiegato;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Il catalogo degli impiegati permette di modificare, accedere e cercare gli impiegati presenti nel sistema.
 */
public class CatalogoImpiegatiPublisher {

    /**
     * Lista di tutti gli impiegati del sistema.
     */
    private ArrayList<Impiegato> listaImpiegati;


    /**
     * Costruttore del catalogo.
     * @param listaImpiegati    {@code ArrayList<Impiegato>} usata come lista del catalogo.
     */
    public CatalogoImpiegatiPublisher(ArrayList<Impiegato> listaImpiegati) {
        this.listaImpiegati = listaImpiegati;
    }

    /**
     * Costruttore vuoto.
     */
    public CatalogoImpiegatiPublisher() {
    }

    // Getters - Setters

    public ArrayList<Impiegato> getListaImpiegati() {
        return listaImpiegati;
    }

    public void setListaImpiegati(ArrayList<Impiegato> listaImpiegati) {
        this.listaImpiegati = listaImpiegati;
    }

    // Metodi interfaccia publica

    /**
     * Cerca un impiegato specifica tramite il suo codice fiscale e ne restituisce una copia.
     *
     * @param CFImpiegato Il codice fiscale dell'impiegato da cercare da cercare.
     * @return Una deep copy dell'oggetto Impiegato trovato, o {@code null} se non ne esiste nessuno con quel CF.
     * @throws CloneNotSupportedException Se l'oggetto Impiegato non supporta la clonazione.
     */
    public Impiegato getImpiegato(String CFImpiegato) throws CloneNotSupportedException{
        for (Impiegato i : listaImpiegati) {
            if (i.getCodiceFiscale().equals(CFImpiegato))
                return i.clone();
        }
        return null;
    }
    
    // Metodi della classe Object

    @Override
    public String toString() {
        return "CatalogoImpiegatiPublisher{" +
                "listaImpiegati=" + listaImpiegati +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CatalogoImpiegatiPublisher that = (CatalogoImpiegatiPublisher) o;
        return Objects.equals(listaImpiegati, that.listaImpiegati);
    }
}
