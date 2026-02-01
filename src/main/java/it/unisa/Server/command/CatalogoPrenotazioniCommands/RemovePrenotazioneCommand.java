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

    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
    }

    public Prenotazione getPrenotazioni() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    @Override
    public void execute() {
        catalogue.removePrenotazioni(prenotazione);
    }

    @Override
    public void undo() {
        catalogue.getListaPrenotazioni();
        catalogue.addPrenotazioni(prenotazione);
    }
}
