package it.unisa.Server.Command.CatalogoCamereCommands;

import it.unisa.Common.*;
import it.unisa.Server.Command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamerePublisher;
import it.unisa.Server.persistent.util.Stato;

import java.util.ArrayList;

/**
 * Comando per impostare lo stato della camera a "InPulizia"
 */
public class SetStatoInPuliziaCommand implements Command {

    private CatalogoCamerePublisher catalogue;
    private int numeroCamera;
    private Stato statoPrecedente;

    /**
     * Costruttore del comando.
     * @param catalogue     Catalogo delle camere usato per completare il comando.
     * @param numeroCamera  Numero della camera a cui si deve modificare lo stato.
     */
    public SetStatoInPuliziaCommand(CatalogoCamerePublisher catalogue, int numeroCamera) {
        this.catalogue = catalogue;
        this.numeroCamera = numeroCamera;
        this.statoPrecedente = null;
    }

    /**
     * Costruttore vuoto.
     */
    public SetStatoInPuliziaCommand() {}


    public CatalogoCamerePublisher getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoCamerePublisher catalogue) {
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
                    cam.setStatoCamera(Stato.InPulizia);
            }

            catalogue.setListaCamere(lc);
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

            catalogue.setListaCamere(lc);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}