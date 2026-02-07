package it.unisa.Server.command.Others;

import it.unisa.Common.Trattamento;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.Command;
import it.unisa.Storage.DAO.TrattamentoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class RetrieveAllTrattamentiCommand implements Command {

    /**
     * Lista di trattamenti di ritorno.
     */
    private ArrayList<Trattamento> trattamenti;


    /**
     * Costruttore vuoto del comando.
     */
    public RetrieveAllTrattamentiCommand() {
        trattamenti = new ArrayList<>(0);
    }

    /**
     * Restituisce la lista di trattamenti (indipendentemente dall'esecuzione del comando o meno).
     *
     * @post    this.trattamenti.size() == 0, se non è stato ancora eseguito il metodo execute()
     *          this.trattamenti.size() >= 0 && this.trattamenti.size() <= # trattamenti presenti nel sistema.
     * @return
     */
    public ArrayList<Trattamento> getTrattamenti() {
        return trattamenti;
    }

    /**
     * Esegue il comando.
     *
     * @post trattamenti = lista di tutti i trattamenti presenti nel sistema
     */
    @Override
    public void execute() throws IllegalAccess {
        try {
            trattamenti = (ArrayList<Trattamento>) new TrattamentoDAO().doRetriveAll("nome asc");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalAccess("ERROR: nessun servizio recuperato");
        }
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post trattamenti.size() = 0
     */
    @Override
    public void undo() {
        trattamenti = new ArrayList<>(0);
    }
    
}
