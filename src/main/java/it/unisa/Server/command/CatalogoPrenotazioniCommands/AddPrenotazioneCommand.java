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

    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    @Override
    public void execute() {
        catalogue.addPrenotazioni(prenotazione);
    }

    @Override
    public void undo() {

            Prenotazione p = catalogue.getPrenotazione(prenotazione.getIDPrenotazione());
            catalogue.removePrenotazioni(p);

    }
}
