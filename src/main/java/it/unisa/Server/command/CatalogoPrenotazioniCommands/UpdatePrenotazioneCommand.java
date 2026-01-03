package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioniPublisher;

import java.util.ArrayList;

/**
 * Comando per modificare i dati di una prenotazione (eccetto il codice identificativo, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdatePrenotazioneCommand implements Command {

    private CatalogoPrenotazioniPublisher catalogue;
    private Prenotazione prenotazione;
    private Prenotazione prenotazioneNonModificata;
    /**
     * Costruttore del comando.
     * @param catalogue     Catalogo delle prenotazioni per poter completare il comando.
     * @param prenotazione  Prenotazione da modificare.
     */
    public UpdatePrenotazioneCommand(CatalogoPrenotazioniPublisher catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdatePrenotazioneCommand() {
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

            // Ricerca in entrambe le liste (prenotazioni bannati e non)
            for (Prenotazione pren : lp) {
                if(pren.getCodicePrenotazione() ==  p.getCodicePrenotazione()) {
                    prenotazioneNonModificata = pren;
                    lp.remove(pren); // rimuovi la prenotazione 'non modificata' dalla lista delle prenotazioni
                    lp.add(p); // aggiungi la prenotazione 'modificata' alla lista delle prenotazioni
                }
            }

            catalogue.setListaPrenotazioni(lp);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getCodicePrenotazione());
            ArrayList<Prenotazione> lc = catalogue.getListaPrenotazioni();

            // Ricerca in entrambe le liste (prenotazioni bannati e non)
            for (Prenotazione pren : lc) {
                if(pren.getCodicePrenotazione() == p.getCodicePrenotazione()) {
                    lc.remove(pren); // rimuovi il prenotazione 'non modificato' dalla lista dei prenotazioni
                    lc.add(prenotazioneNonModificata); // aggiungi il prenotazione 'modificato' alla lista dei prenotazioni
                }
            }

            catalogue.setListaPrenotazioni(lc);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
