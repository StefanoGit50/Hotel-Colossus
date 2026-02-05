
package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.SubjectCamereInterface;
import it.unisa.Server.persistent.util.Util;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
    private static FrontDeskStorage<Camera> fds;
    private static List<ObserverCamereInterface> observers = new ArrayList<>();

    /**
     * Lista interna contenente tutti gli oggetti {@link it.unisa.Common.Camera} del catalogo.
     * La lista viene gestita tramite deep copy per garantire l'incapsulamento.
     */
    private static ArrayList<Camera> camereList;

    static {
        fds = new CameraDAO();
        try {
            camereList = new ArrayList<>(fds.doRetriveAll("decrescente"));
            System.out.println(camereList.size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static Camera lastModified = null;


    /**
     * Restituisce l'ultima camera modificata.
     *
     * @return Ultima camera modificata, null se nessuna modifica
     *
     * @pre Nessuna
     * @post result = lastModified
     */
    public static Camera getLastModified() {
        return lastModified;
    }

    /**
     * Costruttore per inizializzare il catalogo con una lista di camere.
     * Viene eseguita una deep copy della lista fornita per isolare lo stato interno.
     *
     * @param listaCamere L'ArrayList di oggetti Camera da copiare nel catalogo.
     */
    public CatalogoCamere(ArrayList<Camera> listaCamere) {
        this.camereList = Util.deepCopyArrayList(listaCamere);
    }

    public CatalogoCamere(){}

    /**
     * Restituisce la lista completa delle camere.
     *
     * @return ArrayList delle camere
     *
     * @pre Nessuna
     * @post result != null
     * @post result è sincronizzato (thread-safe)
     */
    public synchronized static ArrayList<Camera> getListaCamere() {
        return camereList;
    }


    /**
     * Aggiunge camere al catalogo e le persiste.
     *
     * @param camere ArrayList di camere da aggiungere
     * @throws CloneNotSupportedException Se le camere non supportano la clonazione
     * @throws SQLException Se si verifica un errore di database
     *
     * @pre camere != null
     * @pre camere.size() > 0
     *
     * @post ∀ camera ∈ camere: camereList.contains(camera.clone())
     * @post Camere salvate nel database
     */
    public synchronized static void addCamere(ArrayList<Camera> camere) {

        try {
            fds.doSaveAll(camere);
            for (Camera camera : camere) {
                camereList.add(camera.clone());
            }
        } catch (CloneNotSupportedException | SQLException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        }
    }


    public synchronized static boolean aggiornalista(){
        fds = new CameraDAO();
        try{
            camereList= (ArrayList<Camera>) fds.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return !camereList.isEmpty();
    }




    /**
     * Verifica se un cliente corrisponde a una camera nel catalogo.
     *
     * @param c Cliente da verificare
     * @return true se trovata corrispondenza, false altrimenti
     *
     * @pre Nessuna
     * @post result = false se c == null
     * @post result = true ⟺ ∃ camera ∈ camereList: camera.equals(c)
     */
    public static boolean cameraIsEquals(Cliente c){
        if(c==null)
            return false;
        if(!camereList.contains(c))
            return false;
        else return true;

    }


    /**
     * Cerca una camera per numero e ne restituisce una copia.
     *
     * @param numeroCamera Numero identificativo della camera
     * @return Clone della camera trovata, null se non esiste
     *
     * @pre numeroCamera > 0
     *
     * @post result != null ⟺ ∃ camera ∈ camereList: camera.getNumeroCamera() = numeroCamera
     * @post result != camera originale (è un clone)
     */
    public static Camera getCamera(int numeroCamera) {
        try{
            System.out.println(camereList);
            for(Camera c : camereList) {
                if(c.getNumeroCamera() == numeroCamera)
                    return c.clone();
            }
        }catch (CloneNotSupportedException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        }
        return null;
    }


    /**
     * Aggiorna lo stato di una camera.
     *
     * @param c Camera con il nuovo stato
     * @return true se aggiornamento riuscito, false altrimenti
     * @throws RemoteException Se si verifica un errore remoto
     *
     * @pre c != null
     * @pre c.getNumeroCamera() > 0
     *
     * @post result = true ⟺ stato camera aggiornato nel catalogo e nel database
     * @post result = true ⟹ lastModified = camera aggiornata
     * @post result = true ⟹ observers notificati
     * @post result = false se stato non modificabile o camera non presente
     */
    public boolean aggiornaStatoCamera(Camera c) throws RemoteException {

            for(Camera cam : camereList){
                if(cam.getNumeroCamera()==c.getNumeroCamera() && !cam.getStatoCamera().equals(c.getStatoCamera())){
                    cam.setStatoCamera(c.getStatoCamera());
                    FrontDeskStorage<Camera> fds = new CameraDAO();
                    try {
                        fds.doUpdate(cam);
                    }catch (SQLException e) {
                        e.printStackTrace();
                        Logger.getLogger(FrontDeskStorage.class.getName()).log(Level.SEVERE, null, e);
                    }
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


    /**
     * Iscrive un observer al catalogo.
     *
     * @param observer Observer da iscrivere
     *
     * @pre observer != null
     * @post observers.contains(observer)
     */
    @Override
    public void attach(ObserverCamereInterface observer) {
        observers.add(observer);
    }


    /**
     * Disiscrive un observer dal catalogo.
     *
     * @param observer Observer da disiscrivere
     *
     * @pre observer != null
     * @post !observers.contains(observer)
     */
    @Override
    public void detach(ObserverCamereInterface observer) {
        observers.remove(observer);

    }


    /**
     * Notifica tutti gli observer di un aggiornamento.
     *
     * @throws RemoteException Se si verifica un errore nella notifica remota
     *
     * @pre Nessuna
     * @post ∀ observer ∈ observers: observer.update() chiamato
     */
    @Override
    public void notifyObservers() throws RemoteException {  // notifico agli observer che una camera è stata cambiata
        for (ObserverCamereInterface observer : observers) {
            observer.update();
        }
    }
}
