package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Camera;
import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.SubjectCamereInterface;
import it.unisa.Server.persistent.util.Util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce il catalogo delle camere dell'hotel.
 * Questa classe è responsabile per la conservazione e l'accesso sicuro
 * (tramite deep copy) all'elenco delle camere disponibili.
 */
public class CatalogoCamerePublisher implements SubjectCamereInterface {

    private List<ObserverCamereInterface> observers = new ArrayList<>();

    /**
     * Lista interna contenente tutti gli oggetti {@link it.unisa.Common.Camera} del catalogo.
     * La lista viene gestita tramite deep copy per garantire l'incapsulamento.
     */
    private ArrayList<Camera> listaCamere;

    /**
     * Costruttore per inizializzare il catalogo con una lista di camere.
     * Viene eseguita una deep copy della lista fornita per isolare lo stato interno.
     *
     * @param listaCamere L'ArrayList di oggetti Camera da copiare nel catalogo.
     */
    public CatalogoCamerePublisher(ArrayList<Camera> listaCamere) {
        this.listaCamere = Util.deepCopyArrayList(listaCamere);
    }

    /**
     * Restituisce una deep copy dell'elenco completo delle camere.
     * Questo impedisce modifiche esterne alla lista interna del catalogo.
     *
     * @return Una nuova ArrayList contenente copie (cloni) di tutti gli oggetti Camera.
     */
    public ArrayList<Camera> getListaCamere() {
        return Util.deepCopyArrayList(listaCamere);
    }

    /**
     * Cerca una camera specifica tramite il suo numero e ne restituisce una copia.
     *
     * @param numeroCamera Il numero identificativo della camera da cercare.
     * @return Una deep copy dell'oggetto Camera trovato, o {@code null} se non esiste nessuna camera con quel numero.
     * @throws CloneNotSupportedException Se l'oggetto Camera non supporta la clonazione.
     */
    public Camera getCamera(int numeroCamera) throws CloneNotSupportedException{
        for (Camera c : listaCamere) {
            if (c.getNumeroCamera() == numeroCamera)
                // Restituiamo una copia per rispettare l'incapsulamento
                return c.clone();
        }
        return null;
    }


    @Override
    public void attach(ObserverCamereInterface observer) {
        observers.add(observer);
    }

    @Override
    public void detach(ObserverCamereInterface observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Camera camera) throws RemoteException {  // notifico agli observer che una camera è stata cambiata
        for (Camera c : listaCamere) {
            if (c.getNumeroCamera() == camera.getNumeroCamera() && c.getStatoCamera() != camera.getStatoCamera())
                c.setStatoCamera(camera.getStatoCamera());
        }
        for (ObserverCamereInterface observer : observers) {
            observer.update(camera);
        }
    }
}