package it.unisa.Common;

import it.unisa.Server.persistent.util.Stato;

import java.util.Objects;

/**
 * Rappresenta una Camera d'hotel.
 * Ogni camera è identificata da un numero univoco e possiede una tipologia, uno stato
 * corrente e una capacità massima di persone.
 */
public class Camera implements Cloneable{
    /**
     * Numero di camera, univoco per ogni camera all'interno dell'hotel.
     */
    private int numeroCamera;
    /**
     * Tipologia di camera (es. Singola, Doppia, Suite).
     */
    private String tipologia;
    /**
     * Stato corrente della camera (es. Libera, Occupata, In Manutenzione),
     * utilizzando l'enumerazione {@link Stato}.
     */
    private Stato statoCamera;
    /**
     * Numero massimo di clienti che possono soggiornare nella camera.
     */
    private int capacità;

    private String noteCamera;

    /**
     * Costruttore per creare una nuova istanza di {@code Camera}.
     * @param numeroCamera Il numero identificativo univoco della camera.
     * @param tipologia La tipologia della camera (es. Singola, Doppia).
     * @param statoCamera Lo stato iniziale della camera (es. Libera).
     * @param capacità Il numero massimo di persone che può ospitare.
     */
    public Camera(int numeroCamera, String tipologia, Stato statoCamera, int capacità) {
        this.numeroCamera = numeroCamera;
        this.tipologia = tipologia;
        this.statoCamera = statoCamera;
        this.capacità = capacità;
    }

    /**
     * Restituisce il numero identificativo della camera.
     * @return Il numero di camera.
     */
    public int getNumeroCamera() {
        return numeroCamera;
    }

    /**
     * Imposta un nuovo numero identificativo per la camera.
     * @param numeroCamera Il nuovo numero di camera da assegnare.
     */
    public void setNumeroCamera(int numeroCamera) {
        this.numeroCamera = numeroCamera;
    }

    /**
     * Restituisce la tipologia della camera.
     * @return La tipologia della camera.
     */
    public String getTipologia() {
        return tipologia;
    }

    /**
     * Imposta una nuova tipologia per la camera.
     * @param tipologia La nuova tipologia della camera.
     */
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    /**
     * Restituisce lo stato corrente della camera.
     * @return Lo stato attuale della camera, come valore dell'enumerazione {@link Stato}.
     */
    public Stato getStatoCamera() {
        return statoCamera;
    }

    /**
     * Imposta un nuovo stato per la camera.
     * @param statoCamera Il nuovo stato della camera.
     */
    public void setStatoCamera(Stato statoCamera) {
        this.statoCamera = statoCamera;
    }

    /**
     * Restituisce la capacità massima di persone che la camera può ospitare.
     * @return La capacità massima della camera.
     */
    public int getCapacità() {
        return capacità;
    }

    /**
     * Imposta una nuova capacità massima di persone per la camera.
     * @param capacità La nuova capacità massima della camera.
     */
    public void setCapacità(int capacità) {
        this.capacità = capacità;
    }

    public void setNoteCamera(String noteCamera){
        this.noteCamera = noteCamera;
    }

    public String getNoteCamera(){
        return this.noteCamera;
    }
    /**
     * @param obj   L'oggetto da confrontare.
     * @return {@code true} se l'oggetto obj è un'istanza di Camera, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Camera camera = (Camera) obj;
        return numeroCamera == camera.numeroCamera && capacità == camera.capacità && Objects.equals(tipologia, camera.tipologia) && statoCamera == camera.statoCamera;
    }

    /**
     * @return Deep copy di un oggetto camera.
     * @throws CloneNotSupportedException Se l'interfaccia {@code Cloneable} è assente.
     */
    @Override
    public Camera clone() throws CloneNotSupportedException {
        return (Camera) super.clone();
    }
}