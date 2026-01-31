package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Comando per modificare i dati di una prenotazione (eccetto il codice identificativo, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdatePrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue;
    private Prenotazione prenotazione;
    private Prenotazione prenotazioneNonModificata;
    /**
     * Costruttore del comando.
     * @param catalogue     Catalogo delle prenotazioni per poter completare il comando.
     * @param prenotazione  Prenotazione da modificare.
     */
    public UpdatePrenotazioneCommand(CatalogoPrenotazioni catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdatePrenotazioneCommand() {
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
       prenotazioneNonModificata = null;

        ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();

        for (int i = 0; i < lp.size(); i++) {
            Prenotazione corrente = lp.get(i);

            if (corrente.getIDPrenotazione() == prenotazione.getIDPrenotazione()) {
                try {
                    prenotazioneNonModificata = corrente.clone();
                }catch (CloneNotSupportedException c) {
                    c.printStackTrace();
                    return;
                }
                catalogue.UpdatePrenotazioni(prenotazione);
            }
        }

        throw new NoSuchElementException("Prenotazione non trovata");
    }


    @Override
    public void undo() {

        if(prenotazioneNonModificata != null) {
            ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();

            for (int i = 0; i < lp.size(); i++) {
                if (Objects.equals(lp.get(i).getIDPrenotazione(), prenotazioneNonModificata.getIDPrenotazione())) {
                    catalogue.UpdatePrenotazioni(prenotazioneNonModificata); // UNDO
                    return;
                }
            }
        }

    }

}
