package it.unisa.Server.commandPattern.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per eliminare un impiegato (ovvero rimuoverlo dalla lista dei impiegati).
 */
public class RemoveImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue = new CatalogoImpiegati();
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param impiegato   Impiegato da eliminare.
     */
    public RemoveImpiegatoCommand(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveImpiegatoCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public CatalogoImpiegati getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
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
     * @return impiegato
     */
    public Impiegato getImpiegato() {
        return impiegato;
    }


    /**
     * Imposta il valore di impiegato.
     *
     * @param impiegato
     * @pre impiegato != null
     * @post this.impiegato == impiegato
     */
    public void setImpiegato(Impiegato impiegato) {
        this.impiegato = impiegato;
    }


    /**
     * Esegue il comando.
     *
     * @post not CatalogoImpiegati.listaImpiegati.contains(impiegato)
     */
    @Override
    public void execute() throws IllegalAccess {
        try {
            // Riprendi l'impiegato per com'è attualmente nel catalogo
            Impiegato i = CatalogoImpiegati.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();
            li.remove(i);
            BackofficeStorage<Impiegato> backofficeStorage = new ImpiegatoDAO();
            backofficeStorage.doDelete(i);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
            throw new IllegalAccess("ERROR: l'impiegato non è stato rimosso");
        }
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoImpiegati.listaImpiegati.contains(impiegato)
     */
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
