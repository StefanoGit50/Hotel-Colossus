package it.unisa.interfacce;

import it.unisa.Common.Camera;

import java.rmi.Remote;
import java.util.List;

public interface GovernanteInterface extends Remote {
    List<Camera> getListCamere();
    boolean aggiornaStatoCamera(Camera c);
}
