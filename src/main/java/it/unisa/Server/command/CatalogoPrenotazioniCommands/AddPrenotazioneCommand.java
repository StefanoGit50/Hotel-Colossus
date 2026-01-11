package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando che permette di registrare una prenotazione (aggiungerla al catalogo delle prenotazioni).
 */
public class AddPrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue;
    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param catalogue     Catalogo delle prenotazioni, necessario per completare il comando.
     * @param prenotazione  Oggetto {@code Prenotazione} da registrare.
     */
    public AddPrenotazioneCommand(CatalogoPrenotazioni catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
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
        ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();
        lp.add(prenotazione);
        FrontDeskStorage<Prenotazione> frontDeskStorage = new PrenotazioneDAO();
        try {
            frontDeskStorage.doSave(prenotazione);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getIDPrenotazione());
            ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();
            lp.remove(p);
            PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
            prenotazioneDAO.doDelete(p);
        } catch (CloneNotSupportedException | SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
