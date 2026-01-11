package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando che permette di eliminare una prenotazione (eliminarla dal catalogo delle prenotazioni).
 */
public class RemovePrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue;
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param catalogue     Catalogo delle prenotazioni, necessario per completare il comando.
     * @param prenotazione  Oggetto {@code Prenotazione} da eliminare.
     */
    public RemovePrenotazioneCommand(CatalogoPrenotazioni catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
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

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    @Override
    public void execute() {
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getIDPrenotazione());
            ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();
            lp.remove(p);
            FrontDeskStorage<Prenotazione> frontDeskStorage = new PrenotazioneDAO();
            frontDeskStorage.doDelete(p);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();
        lp.add(prenotazione);
        try{
            PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
            prenotazioneDAO.doSave(prenotazione);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
