package it.unisa.Server.commandPattern.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.Server.Eccezioni.DuplicateKeyEntry;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per registrare un nuovo impiegato (aggiungerlo al catalogo degli impiegati).
 */
public class AddImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue = new  CatalogoImpiegati();
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param impiegato   Impiegato da registrare.
     */
    public AddImpiegatoCommand(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public AddImpiegatoCommand() {
    }

    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     */
    public CatalogoImpiegati getCatalogue() {
        return catalogue;
    }

    /**
     * Imposta il valore di catalogue.
     *
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoImpiegati catalogue) {
        this.catalogue = catalogue;
    }

    /**
     * Restituisce il valore di impiegato.
     *
     * @post result == impiegato
     */
    public Impiegato getImpiegato() {
        return impiegato;
    }

    /**
     * Imposta il valore di impiegato.
     *
     * @pre impiegato != null
     * @post this.impiegato == impiegato
     */
    public void setImpiegato(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    /**
     * Esegue il comando.
     *
     * @post CatalogoImpiegati.listaImpiegati.contains(impiegato)
     */
    @Override
    public void execute() throws IllegalAccess{
        ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
        li.add(impiegato);
        try{
            BackofficeStorage<Impiegato> impiegatoFrontDeskStorage = new ImpiegatoDAO();
            impiegatoFrontDeskStorage.doSave(impiegato);
        } catch (SQLException e){
            if (e.getErrorCode() == 1062)
                throw new DuplicateKeyEntry();
            else
                throw new IllegalAccess("ERROR: l'impiegato non è stato aggiunto");
        }
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post not CatalogoImpiegati.listaImpiegati.contains(impiegato)
     */
    @Override
    public void undo() {
        try {
            Impiegato i = CatalogoImpiegati.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
            li.remove(i);
            BackofficeStorage<Impiegato> impiegatoBackofficeStorage = new ImpiegatoDAO();
            impiegatoBackofficeStorage.doDelete(i);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
