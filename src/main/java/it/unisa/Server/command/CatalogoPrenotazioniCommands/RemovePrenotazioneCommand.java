package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;


/**
 * Comando che permette di eliminare una prenotazione (eliminarla dal catalogo delle prenotazioni).
 */
public class RemovePrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue = new CatalogoPrenotazioni();
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param prenotazione  Oggetto {@code Prenotazione} da eliminare.
     */
    public RemovePrenotazioneCommand(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public RemovePrenotazioneCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
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
     * @return prenotazione
     */
    public Prenotazione getPrenotazioni() {
        return prenotazione;
    }


    /**
     * Imposta il valore di prenotazione.
     *
     * @param prenotazione
     * @pre prenotazione != null
     * @post this.prenotazione == prenotazione
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    /**
     * Esegue il comando.
     *
     * @post not CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void execute() {
        catalogue.removePrenotazioni(prenotazione);
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void undo() {
        catalogue.getListaPrenotazioni();
        catalogue.addPrenotazioni(prenotazione);
    }
}
