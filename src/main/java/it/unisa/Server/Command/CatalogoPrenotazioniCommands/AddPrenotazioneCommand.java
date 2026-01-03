package it.unisa.Server.Command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.Command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioniPublisher;

import java.util.ArrayList;

/**
 * Comando che permette di registrare una prenotazione (aggiungerla al catalogo delle prenotazioni).
 */
public class AddPrenotazioneCommand implements Command {

    private CatalogoPrenotazioniPublisher catalogue;
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param catalogue     Catalogo delle prenotazioni, necessario per completare il comando.
     * @param prenotazione  Oggetto {@code Prenotazione} da registrare.
     */
    public AddPrenotazioneCommand(CatalogoPrenotazioniPublisher catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public AddPrenotazioneCommand() {
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
        ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();
        lp.add(prenotazione);
        catalogue.setListaPrenotazioni(lp);
    }

    @Override
    public void undo() {
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getCodicePrenotazione());
            ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();
            lp.remove(p);
            catalogue.setListaPrenotazioni(lp);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
