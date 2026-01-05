package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Il catalogo degli impiegati permette di modificare, accedere e cercare gli impiegati presenti nel sistema.
 */
public class CatalogoImpiegati implements Serializable {

    /**
     * Lista di tutti gli impiegati del sistema.
     */
    private ArrayList<Impiegato> listaImpiegati;


    /**
     * Costruttore del catalogo.
     * @param listaImpiegati    {@code ArrayList<Impiegato>} usata come lista del catalogo.
     */
    public CatalogoImpiegati(ArrayList<Impiegato> listaImpiegati) {
        this.listaImpiegati = listaImpiegati;
    }

    /**
     * Costruttore vuoto.
     */
    public CatalogoImpiegati() {
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

    /**
     * Esegue una ricerca flessibile all'interno del catalogo impiegati basandosi su vari criteri.
     * La ricerca prende almeno un parametro in input mentre gli altri possono essere nulli.
     * Un impiegato viene selezionato solo se rispetta tutti i parametri non nulli della ricerca.
     *
     * @param nome Il nome dell'impiegato da cercare (può essere {@code null}).
     * @param cognome Il cognome dell'impiegato da cercare (può essere {@code null}).
     * @param sesso La nazionalità (o cittadinanza) dell'impiegato (può essere {@code null}).
     * @param ruolo {@code Ruolo} Ruolo dell'impiegato (può essere {@code null}).
     * @return Una deep copy dell'ArrayList contenente tutti gli impiegati che corrispondono ai criteri di ricerca.
     * @throws CloneNotSupportedException Se il metodo clone non è supportato dalla classe {@code Impiegato}
     */
    public ArrayList<Impiegato> cercaimpiegati(String nome, String cognome, String sesso, Ruolo ruolo)
            throws CloneNotSupportedException{
        ArrayList<Impiegato> risultati = new ArrayList<>();

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[4];
        params[0] = nome != null && !nome.isEmpty();
        params[1] = cognome != null && !cognome.isEmpty();
        params[2] = sesso != null  && !sesso.isEmpty();
        params[3] = ruolo != null;

        // Tutti i parametri sono nulli
        if( !params[0] && !params[1] && !params[2] && !params[3] ) {return null;}

        for (Impiegato impiegato : listaImpiegati) {

            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la ricerca
                if (!Objects.equals(impiegato.getNome(), nome)) { // Il criterio non è rispettato
                    continue; // L'oggetto Impiegato non viene aggiunto
                }
            }
            if (params[1]) {
                if (!Objects.equals(impiegato.getCognome(), cognome)) {
                    continue;
                }
            }
            if (params[2]) {
                if (!Objects.equals(impiegato.getSesso(), sesso)) {
                    continue;
                }
            }
            if (params[3]) {
                if (!impiegato.getRuolo().equals(ruolo)) {
                    continue;
                }
            }

            risultati.add(impiegato.clone());
        }

        return risultati;
    }

    // Metodi della classe Object

    @Override
    public String toString() {
        return "CatalogoImpiegati{" +
                "listaImpiegati=" + listaImpiegati +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CatalogoImpiegati that = (CatalogoImpiegati) o;
        return Objects.equals(listaImpiegati, that.listaImpiegati);
    }
}
