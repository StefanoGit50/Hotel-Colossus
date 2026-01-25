package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.Storage.DuplicateKeyEntry;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per registrare un nuovo impiegato (aggiungerlo al catalogo degli impiegati).
 */
public class AddImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue;
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo degli impiegati per poter completare il comando.
     * @param impiegato   Impiegato da registrare.
     */
    public AddImpiegatoCommand(CatalogoImpiegati catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public AddImpiegatoCommand() {
    }

    public CatalogoImpiegati getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoImpiegati catalogue) {
        this.catalogue = catalogue;
    }

    public Impiegato getImpiegato() {
        return impiegato;
    }

    public void setImpiegato(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    @Override
    public void execute() {
        ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
        li.add(impiegato);
        try{
            BackofficeStorage<Impiegato> impiegatoFrontDeskStorage = new ImpiegatoDAO();
            impiegatoFrontDeskStorage.doSave(impiegato);
        } catch (SQLException e){
            if (e.getErrorCode() == 1062)
                throw new DuplicateKeyEntry();
        }
    }

    @Override
    public void undo() {
        try {
            Impiegato i = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
            li.remove(i);
            BackofficeStorage<Impiegato> impiegatoBackofficeStorage = new ImpiegatoDAO();
            impiegatoBackofficeStorage.doDelete(i);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
