package it.unisa.Server.Command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.Command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioniPublisher;

import java.util.ArrayList;

/**
 * Comando che permette di eliminare una prenotazione (eliminarla dal catalogo delle prenotazioni).
 */
public class RemovePrenotazioneCommand implements Command {

    private CatalogoPrenotazioniPublisher catalogue;
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param catalogue     Catalogo delle prenotazioni, necessario per completare il comando.
     * @param prenotazione  Oggetto {@code Prenotazione} da eliminare.
     */
    public RemovePrenotazioneCommand(CatalogoPrenotazioniPublisher catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public RemovePrenotazioneCommand() {
    }

    public CatalogoPrenotazioniPublisher getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoPrenotazioniPublisher catalogue) {
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
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getCodicePrenotazione());
            ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();
            lp.remove(p);
            catalogue.setListaPrenotazioni(lp);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();
        lp.add(prenotazione);
        catalogue.setListaPrenotazioni(lp);
    }
}
