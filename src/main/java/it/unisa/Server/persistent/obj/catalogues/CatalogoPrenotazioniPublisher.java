package it.unisa.Server.persistent.obj.catalogues;
import it.unisa.Common.Prenotazione;

import java.util.ArrayList;

/**
 * Gestisce l'elenco complessivo delle prenotazioni, permettendo la registrazione,
 * la ricerca e la rimozione delle stesse.
 */
public class CatalogoPrenotazioniPublisher {
    private ArrayList<Prenotazione> listaPrenotazioni;

    /**
     * Costruttore della classe CatalogoPrenotazioniPublisher.
     * @param listaPrenotazioni Lista di prenotazioni di tipo {@link Prenotazione} del catalogo.
     */
    public CatalogoPrenotazioniPublisher(ArrayList<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore vuoto.
     */
    public CatalogoPrenotazioniPublisher() {}

    /**
     * Cerca prenotazioni in base a criteri specifici.
     * @return Una lista di prenotazioni filtrate.
     */
    public ArrayList<Prenotazione> cercaPrenotazioni(Object datiPrenotazioni) {
        // Logica prenotazione
        return null;
    }

    /**
     * Restituisce la lista di tutte le prenotazioni nel catalogo.
     */
    public ArrayList<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta o sostituisce l'intera lista delle prenotazioni.
     */
    public void setListaPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Recupera lo storico delle prenotazioni effettuate da un cliente specifico.
     */
    public ArrayList<Prenotazione> getStoricoPrenotazioni(Object cliente) {
        return new ArrayList<>();
    }

    /**
     * Cerca una prenotazione specifica tramite il suo numero identificativo e ne restituisce una copia.
     *
     * @param codicePrenotazione Il numero identificativo della prenotazione da cercare.
     * @return Una deep copy dell'oggetto Prenotazione trovata, o {@code null} se non esiste nessuna prenotazione con quel numero.
     * @throws CloneNotSupportedException Se l'oggetto Prenotazione non supporta la clonazione.
     */
    public Prenotazione getPrenotazione(int codicePrenotazione) throws CloneNotSupportedException{
        for (Prenotazione p : listaPrenotazioni) {
            if (p.getCodicePrenotazione() == codicePrenotazione)
                return p.clone();
        }
        return null;
    }
    // --- Getter e Setter ---


}
