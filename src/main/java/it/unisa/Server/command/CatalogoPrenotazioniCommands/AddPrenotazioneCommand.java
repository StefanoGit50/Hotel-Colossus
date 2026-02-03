package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;


/**
 * Comando che permette di registrare una prenotazione (aggiungerla al catalogo delle prenotazioni).
 */
public class AddPrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue = new  CatalogoPrenotazioni();
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param prenotazione  Oggetto {@code Prenotazione} da registrare.
     */
    public AddPrenotazioneCommand(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public AddPrenotazioneCommand(){
    }

    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     */
    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }

    /**
     * Imposta il valore di catalogue.
     *
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
    }

    /**
     * Restituisce il valore di prenotazione.
     *
     * @post result == prenotazione
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Imposta il valore di prenotazione.
     *
     * @pre prenotazione != null
     * @post this.prenotazione == prenotazione
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Esegue il comando.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void execute() {
        catalogue.addPrenotazioni(prenotazione);
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post not CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void undo() {

            Prenotazione p = catalogue.getPrenotazione(prenotazione.getIDPrenotazione());
            catalogue.removePrenotazioni(p);

    }
}
