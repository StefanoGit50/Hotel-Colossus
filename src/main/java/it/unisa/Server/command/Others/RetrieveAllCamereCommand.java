package it.unisa.Server.command.Others;

import it.unisa.Common.Camera;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Storage.DAO.CameraDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class RetrieveAllCamereCommand implements Command {

    /**
     * Lista di camere di ritorno.
     */
    private ArrayList<Camera> camere;


    /**
     * Costruttore vuoto del comando.
     */
    public RetrieveAllCamereCommand() {
        camere = new ArrayList<>(0);
    }

    /**
     * Restituisce la lista di camere (indipendentemente dall'esecuzione del comando o meno).
     *
     * @post    this.camere.size() == 0, se non è stato ancora eseguito il metodo execute()
     *          this.camere.size() >= 0 && this.camere.size() <= # camere presenti nel sistema.
     * @return
     */
    public ArrayList<Camera> getCamere() {
        return camere;
    }

    /**
     * Esegue il comando.
     *
     * @post camere = lista di tutti i camere presenti nel sistema
     */
    @Override
    public void execute() throws IllegalAccess {
        camere = CatalogoCamere.getListaCamere();
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post camere.size() = 0
     */
    @Override
    public void undo() {
        camere = new ArrayList<>(0);
    }

}
