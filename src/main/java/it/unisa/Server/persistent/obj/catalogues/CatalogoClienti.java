package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Util;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Gestisce il catalogo dei clienti dell'hotel.
 * La classe mantiene una lista di tutti i clienti e una lista separata per i clienti
 * inseriti nella blacklist (bannati). Garantisce l'incapsulamento.
 */
public class CatalogoClienti implements Serializable {

    /**
     * Lista interna contenente tutti gli oggetti {@link Cliente}.
     */
    private ArrayList<Cliente> listaClienti;

    /**
     * Lista interna contenente gli oggetti {@link Cliente} bannati.
     */
    private ArrayList<Cliente> listaClientiBannati;

    /**
     * Costruttore per creare un nuovo catalogo clienti.
     * Viene eseguita una deep copy delle liste fornite.
     *
     * @param listaClienti L'ArrayList di clienti da copiare nel catalogo.
     */
    public CatalogoClienti(ArrayList<Cliente> listaClienti) {
        this.listaClienti = Util.deepCopyArrayList(listaClienti);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        for (Cliente cliente : listaClienti) {
            if (cliente.isBlacklisted()) {
                listaClientiBannati.add(cliente);
            }
        }
        this.listaClientiBannati = Util.deepCopyArrayList(listaClientiBannati);
    }

    /**
     * Costruttore vuoto
     */
    public CatalogoClienti() {}

    /**
     * Restituisce una deep copy dell'elenco completo dei clienti.
     *
     * @return Un nuovo ArrayList contenente copie (cloni) di tutti gli oggetti Cliente.
     */
    public ArrayList<Cliente> getListaClienti() {
        return Util.deepCopyArrayList(listaClienti);
    }

    /**
     * Restituisce una deep copy dell'elenco dei clienti bannati.
     *
     * @return Uno nuovo ArrayList contenente copie (cloni) dei clienti bannati.
     */
    public ArrayList<Cliente> getListaClientiBannati() {
        return Util.deepCopyArrayList(listaClientiBannati);
    }


    /**
     * Imposta una deep copy della lista clienti.
     * @param listaClienti {@code ArrayList<Cliente>} da impostare.
     */
    public void setListaClienti(ArrayList<Cliente> listaClienti) {
        this.listaClienti = listaClienti;
    }

    /**
     * Imposta una deep copy della lista clienti bannati.
     * @param listaClientiBannati {@code ArrayList<Cliente>} da impostare.
     */
    public void setListaClientiBannati(ArrayList<Cliente> listaClientiBannati) {
        this.listaClientiBannati = listaClientiBannati;
    }

    //
    //  METODI INTERFACCIA PUBLICA
    //

    /**
     * Esegue una ricerca flessibile all'interno del catalogo clienti basandosi su vari criteri.
     * La ricerca prende almeno un parametro in input mentre gli altri possono essere nulli.
     * Un cliente viene selezionato solo se rispetta tutti i parametri non nulli della ricerca.
     *
     * @param nome Il nome del cliente da cercare (può essere {@code null}).
     * @param cognome Il cognome del cliente da cercare (può essere {@code null}).
     * @param nazionalita La nazionalità (o cittadinanza) del cliente (può essere {@code null}).
     * @param dataNascita La data di nascita del cliente (può essere {@code null}).
     * @param sesso Il sesso del cliente (può essere {@code null}).
     * @return Una deep copy dell'ArrayList contenente tutti i clienti che corrispondono ai criteri di ricerca.
     * @throws CloneNotSupportedException Se il metodo clone non è supportato dalla classe {@code Client}
     */
    public ArrayList<Cliente> cercaClienti(String nome, String cognome, String nazionalita, LocalDate dataNascita, String sesso)
            throws CloneNotSupportedException{
        ArrayList<Cliente> risultati = new ArrayList<>();

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[5];
        params[0] = nome != null;
        params[1] = cognome != null;
        params[2] = nazionalita != null;
        params[3] = dataNascita != null;
        params[4] = sesso != null;

        // Tutti i parametri sono nulli
        if( !params[0] && !params[1] && !params[2] && !params[3] && !params[4] ){return null;}

        for (Cliente cliente : listaClienti) {

            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la ricerca
                if (!Objects.equals(cliente.getNome(), nome)) { // Il criterio non è rispettato
                    continue; // L'oggetto cliente non viene aggiunto
                }
            }
            if (params[1]) {
                if (!Objects.equals(cliente.getCognome(), cognome)) {
                    continue;
                }
            }
            if (params[2]) {
                if (!Objects.equals(cliente.getSesso(), sesso)) {
                    continue;
                }
            }
            if (params[3]) {
                if (cliente.getDataNascita().isAfter(dataNascita) || cliente.getDataNascita().isEqual(dataNascita)) {
                    continue;
                }
            }
            if (params[4]) {
                if (!Objects.equals(cliente.getNazionalita(),  nazionalita)) {
                    continue;
                }
            }

            risultati.add(cliente.clone());
        }
        return risultati;
    }

    /**
     * Cerca un cliente specifica tramite il suo codice fiscale e ne restituisce una copia.
     *
     * @param CFCliente Il codice fiscale del cliente da cercare da cercare.
     * @return Una deep copy dell'oggetto Cliente trovato, o {@code null} se non esiste nessun cliente con quel CF.
     * @throws CloneNotSupportedException Se l'oggetto Cliente non supporta la clonazione.
     */
    public Cliente getCliente(String CFCliente) throws CloneNotSupportedException{
        for (Cliente c : listaClienti) {
            if (c.getCf().equals(CFCliente))
                return c.clone();
        }
        for (Cliente c : listaClientiBannati) {
            if (c.getCf().equals(CFCliente))
                return c.clone();
        }
        return null;
    }
}


