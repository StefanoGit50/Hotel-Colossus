package it.unisa.Server.commandPattern.Others;

import it.unisa.Common.Servizio;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Storage.DAO.ServizioDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class RetrieveAllServiziCommand implements Command {

    /**
     * Lista di servizi di ritorno.
     */
    private ArrayList<Servizio> servizi;


    /**
     * Costruttore vuoto del comando.
     */
    public RetrieveAllServiziCommand() {
        servizi = new ArrayList<>(0);
    }
    
    /**
     * Restituisce la lista di servizi (indipendentemente dall'esecuzione del comando o meno).
     *
     * @post    this.servizi.size() == 0, se non è stato ancora eseguito il metodo execute()
     *          this.servizi.size() >= 0 && this.servizi.size() <= # servizi presenti nel sistema.
     * @return
     */
    public ArrayList<Servizio> getServizi() {
        return servizi;
    }

    /**
     * Esegue il comando.
     *
     * @post servizi = lista di tutti i servizi presenti nel sistema
     */
    @Override
    public void execute() throws IllegalAccess {
        try {
            servizi = (ArrayList<Servizio>) new ServizioDAO().doRetriveAll("id disc");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalAccess("ERROR: nessun servizio recuperato");
        }
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post servizi.size() = 0
     */
    @Override
    public void undo() {
        servizi = new ArrayList<>(0);
    }

}
