package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Gestisce il catalogo delle camere dell'hotel.
 * Questa classe è responsabile per la conservazione e l'accesso sicuro
 * (tramite deep copy) all'elenco delle camere disponibili.
 */
public class CatalogoCamere implements Serializable{
    /**
     * Lista interna contenente tutti gli oggetti {@link Camera} del catalogo.
     * La lista viene gestita tramite deep copy per garantire l'incapsulamento.
     */
    private static ArrayList<Camera> listaCamere = new ArrayList<>();
    /**
     * Restituisce una deep copy dell'elenco completo delle camere.
     * Questo impedisce modifiche esterne alla lista interna del catalogo.
     *
     * @return Una nuova ArrayList contenente copie (cloni) di tutti gli oggetti Camera.
     */
    public synchronized static ArrayList<Camera> getListaCamere() {
        return listaCamere;
    }

    public synchronized static void setListaCamere(ArrayList<Camera> camere){

        while (!listaCamere.isEmpty()) {
            listaCamere.removeFirst();
        }

        try{
            for(Camera camera: camere){
                listaCamere.add(camera.clone());
            }
        }catch (CloneNotSupportedException cloneNotSupportedException){
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
        for (Camera c : listaCamere) {
            if (c.getNumeroCamera() == numeroCamera)
                return c.clone();
        }
        return null;
    }
}