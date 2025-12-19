package it.unisa.Server.gestionePrenotazioni.persistent.obj.catalogues;
import it.unisa.Server.gestionePrenotazioni.persistent.obj.Prenotazione;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'elenco complessivo delle prenotazioni, permettendo la registrazione,
 * la ricerca e la rimozione delle stesse.
 */
public class CatalogoPrenotazioniPublisher {
    private List<Prenotazione> listaPrenotazioni;

    /**
     * Costruttore della classe CatalogoPrenotazioniPublisher.
     * @param listaPrenotazioni Lista di prenotazioni di tipo {@link Prenotazione} del catalogo.
     */
    public CatalogoPrenotazioniPublisher(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Registra una nuova prenotazione nel sistema.
     * @param datiPrenotazione I dati necessari per la creazione.
     */
    public void registraPrenotazione(Object datiPrenotazione) {
        // Logica di creazione e aggiunta alla lista
    }

    /**
     * Aggiorna i dati di una prenotazione esistente.
     */
    public void aggiornaDatiPrenotazione(Prenotazione prenotazione) {
        // Logica di aggiornamento
    }

    /**
     * Rimuove una prenotazione dal catalogo.
     */
    public void eliminaPrenotazione(Prenotazione prenotazione) {
        this.listaPrenotazioni.remove(prenotazione);
    }

    /**
     * Avvia la procedura di checkout per una specifica prenotazione.
     */
    //public void effettuaCheckout(Prenotazione prenotazione) {
    //    prenotazione.checkout();
    //}

    /**
     * Cerca prenotazioni in base a criteri specifici.
     * @return Una lista di prenotazioni filtrate.
     */
    public List<Prenotazione> cercaPrenotazioni(Object datiPrenotazioni) {
        return new ArrayList<>(this.listaPrenotazioni);
    }


    /**
     * Recupera lo storico delle prenotazioni effettuate da un cliente specifico.
     */
    public List<Prenotazione> getStoricoPrenotazioni(Object cliente) {
        return new ArrayList<>();
    }

    // --- Getter e Setter ---

    /**
     * Restituisce la lista di tutte le prenotazioni nel catalogo.
     */
    public List<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta o sostituisce l'intera lista delle prenotazioni.
     */
    public void setListaPrenotazioni(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }
}
