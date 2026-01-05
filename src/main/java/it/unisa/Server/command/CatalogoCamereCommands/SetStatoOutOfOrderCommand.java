package it.unisa.Server.command.CatalogoCamereCommands;

import it.unisa.Common.Camera;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.util.Stato;

import java.util.ArrayList;

/**
 * Comando per impostare lo stato di una camera ad "OutOfOrder".
 */
public class SetStatoOutOfOrderCommand implements Command {

    private CatalogoCamere catalogue;
    private int numeroCamera;
    private Stato statoPrecedente;


    /**
     * Costruttore del comando.
     * @param catalogue     Catalogo delle camere usato per completare il comando.
     * @param numeroCamera  Numero della camera a cui si deve modificare lo stato.
     */
    public SetStatoOutOfOrderCommand(int numeroCamera, CatalogoCamere catalogue) {
        this.numeroCamera = numeroCamera;
        this.catalogue = catalogue;
    }

    /**
     * Costruttore vuoto.
     */
    public SetStatoOutOfOrderCommand() {
    }

    public CatalogoCamere getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoCamere catalogue) {
        this.catalogue = catalogue;
    }

    public int getNumeroCamera() {
        return numeroCamera;
    }

    public void setNumeroCamera(int numeroCamera) {
        this.numeroCamera = numeroCamera;
    }

    public Stato getStatoPrecedente() {
        return statoPrecedente;
    }


    @Override
    public void execute() {
        try {
            Camera c = catalogue.getCamera(numeroCamera);
            ArrayList<Camera> lc = catalogue.getListaCamere();
            statoPrecedente = c.getStatoCamera();

            for (Camera cam : lc) {
                if(cam.equals(c))
                    cam.setStatoCamera(Stato.OutOfOrder);
            }

           // catalogue.setListaCamere(lc);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Camera c = catalogue.getCamera(numeroCamera);
            ArrayList<Camera> lc = catalogue.getListaCamere();
            statoPrecedente = c.getStatoCamera();

            for (Camera cam : lc) {
                if(cam.equals(c))
                    cam.setStatoCamera(statoPrecedente);
            }

            //catalogue.setListaCamere(lc);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
