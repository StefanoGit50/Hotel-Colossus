package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per eliminare un impiegato (ovvero rimuoverlo dalla lista dei impiegati).
 */
public class RemoveImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue;
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei impiegati per poter completare il comando.
     * @param impiegato   Impiegato da eliminare.
     */
    public RemoveImpiegatoCommand(CatalogoImpiegati catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveImpiegatoCommand() {
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
        try {
            // Riprendi l'impiegato per com'è attualmente nel catalogo
            Impiegato i = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
            li.remove(i);
            BackofficeStorage<Impiegato> backofficeStorage = new ImpiegatoDAO();
            backofficeStorage.doDelete(i);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
        li.add(impiegato);
        try{
            BackofficeStorage<Impiegato> impiegatoBackofficeStorage = new ImpiegatoDAO();
            impiegatoBackofficeStorage.doSave(impiegato);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
