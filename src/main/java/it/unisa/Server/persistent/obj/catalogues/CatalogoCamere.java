package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Camera;
import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.SubjectCamereInterface;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Server.persistent.util.Util;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce il catalogo delle camere dell'hotel.
 * Questa classe è responsabile per la conservazione e l'accesso sicuro
 * (tramite deep copy) all'elenco delle camere disponibili.
 */

public class CatalogoCamere implements SubjectCamereInterface, Serializable {

    private static List<ObserverCamereInterface> observers = new ArrayList<>();

    /**
     * Lista interna contenente tutti gli oggetti {@link it.unisa.Common.Camera} del catalogo.
     * La lista viene gestita tramite deep copy per garantire l'incapsulamento.
     */
    private static ArrayList<Camera> camereList = new ArrayList<>();

    private static Camera lastModified;

    public static Camera getLastModified() {
        return lastModified;
    }

    /**
     * Costruttore per inizializzare il catalogo con una lista di camere.
     * Viene eseguita una deep copy della lista fornita per isolare lo stato interno.
     *
     * @param listaCamere L'ArrayList di oggetti Camera da copiare nel catalogo.
     */
    public  CatalogoCamere(ArrayList<Camera> listaCamere) {
        this.camereList = Util.deepCopyArrayList(listaCamere);

    }

    public CatalogoCamere(){}


    /**
     * Restituisce una deep copy dell'elenco completo delle camere.
     * Questo impedisce modifiche esterne alla lista interna del catalogo.
     *
     * @return Una nuova ArrayList contenente copie (cloni) di tutti gli oggetti Camera.
     */
    public synchronized static ArrayList<Camera> getListaCamere() {
        return camereList;
    }

    public synchronized static void addCamere(ArrayList<Camera> camere) {

        try {
            for (Camera camera : camere) {
                camereList.add(camera.clone());
            }
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        }
    }

    /**
     * Cerca una camera specifica tramite il suo numero e ne restituisce una copia.
     *
     * @param numeroCamera Il numero identificativo della camera da cercare.
     * @return Una deep copy dell'oggetto Camera trovato, o {@code null} se non esiste nessuna camera con quel numero.
     * @throws CloneNotSupportedException Se l'oggetto Camera non supporta la clonazione.
     */


    public Camera getCamera(int numeroCamera) throws CloneNotSupportedException{
        for (Camera c : camereList) {
            if (c.getNumeroCamera() == numeroCamera)
                return c.clone();
        }
        return null;
    }

    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {

            for(Camera cam : camereList){
                if(cam.getNumeroCamera()==c.getNumeroCamera() && !cam.getStatoCamera().equals(c.getStatoCamera())){
                    cam.setStatoCamera(c.getStatoCamera());
                    lastModified = cam;
                    notifyObservers();
                    return true;
                }
            }
        //lo stato della camera non è modificabile se è equivalente a quello attuale oppure se la camera non è presente nella lista globale
        Logger.getLogger("global").log(Level.INFO, "Stato camera"+c.getStatoCamera()+ "non modificabile");
        return false;
    }


    // metodi per fare l'iscrizione la disiscrizione al publisher e la notifica agli observer quando ce un update
    @Override
    public void attach(ObserverCamereInterface observer) {
        observers.add(observer);
    }

    @Override
    public void detach(ObserverCamereInterface observer) {
        observers.remove(observer);

    }

    @Override
    public void notifyObservers() throws RemoteException {  // notifico agli observer che una camera è stata cambiata
        for (ObserverCamereInterface observer : observers) {
            observer.update();
        }
    }
}