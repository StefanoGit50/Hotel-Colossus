package it.unisa.Common;

import it.unisa.Server.persistent.util.Stato;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta una Camera d'hotel.
 * Ogni camera è identificata da un numero univoco e possiede una tipologia, uno stato
 * corrente e una capacità massima di persone.
 */
public class Camera implements Cloneable, Serializable {

    private final static double EPSILON = 1e10;
 

    /**
     * Numero di camera, univoco per ogni camera all'interno dell'hotel.
     */
     private int numeroCamera;

    /**
     *  Il Nome della camera
     */
    private String nomeCamera;

     /**
     * Il prezzo della camera
     */
    private double prezzoCamera;
    /**
     * Stato corrente della camera (es. Libera, Occupata, In Manutenzione),
     * utilizzando l'enumerazione {@link Stato}.
     */
    private Stato statoCamera;
    /**
     * Numero massimo di clienti che possono soggiornare nella camera.
     */
    private Integer numeroMaxOccupanti;

    /**
     * Le note della camera (ad esempio check-in ore 14 , il cliente è celiaco , ecc.)
     */
    private String noteCamera;

    /**
     * Costruttore per creare una nuova istanza di {@code Camera}.
     *
     * @param numeroCamera    Il numero identificativo univoco della camera.
     * @param statoCamera     Lo stato iniziale della camera (es. Libera).
     * @param capacità        Il numero massimo di persone che può ospitare.
     * @param prezzoCamera    il prezzo della camera in quel momento
     * @param noteCamera      le informazioni aggiunti
     * @param nomeCamera
     */
    public Camera(int numeroCamera, Stato statoCamera, int capacità , double prezzoCamera , String noteCamera , String nomeCamera) {
        this.numeroCamera = numeroCamera;
        this.statoCamera = statoCamera;
        this.numeroMaxOccupanti = capacità;
        this.prezzoCamera = prezzoCamera;
        this.noteCamera = noteCamera;
        this.nomeCamera = nomeCamera;
    }

    /**
     * Costruttore per creare una nuova istanza di {@code Camera} senza parametri.
     */
    public Camera(){
        this.numeroCamera = 0;
        this.numeroMaxOccupanti = 0;
        this.statoCamera = Stato.Libera;
        this.noteCamera = "";
        this.prezzoCamera = 0;
    }

    /**
     * Costruttore per creare una nuova istanza di {@code Camera} con il solo parametro del numero camera.
     *  @param numeroCamera Il nuovo numero di camera da assegnare.
     */
    public Camera(int numeroCamera){
        this.numeroCamera = numeroCamera;
    }

    /**
     * Restituisce il numero identificativo della camera.
     *
     * @pre numeroCamera != null
     * @post this.numeroCamera == numeroCamera
     *
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
     * Restituisce lo stato corrente della camera.
     *
     * @post result == statoCamera
     *
     * @return Lo stato attuale della camera, come valore dell'enumerazione {@link Stato}.
     */
    public Stato getStatoCamera() {
        return statoCamera;
    }

    /**
     * Imposta un nuovo stato per la camera.
     *
     * @pre statoCamera != null
     * @post this.statoCamera == statoCamera
     *
     * @param statoCamera Il nuovo stato della camera.
     */
    public void setStatoCamera(Stato statoCamera) {
        this.statoCamera = statoCamera;
    }

    /**
     * Restituisce la capacità massima di persone che la camera può ospitare.
     * @post result == numeroMaxOccupanti
     * @return La capacità massima della camera.
     */
    public int getCapacità() {
        return numeroMaxOccupanti;
    }

    /**
     * Imposta una nuova capacità massima di persone per la camera.
     *
     * @pre capacità != null
     * @post numeroMaxOccupanti == capacità
     *
     * @param capacità La nuova capacità massima della camera.
     */
    public void setCapacità(int capacità) {
        this.numeroMaxOccupanti = capacità;
    }

    /**
     * Imposta una nota alla camera
     *
     * @post this.noteCamera == noteCamera
     *
     * @param noteCamera
     */
    public void setNoteCamera(String noteCamera){
        this.noteCamera = noteCamera;
    }

    /**
     * restituisce le note della camera
     *
     * @post result == noteCamera
     *
     * @return notecamera
     */
    public String getNoteCamera(){
        return this.noteCamera;
    }

    /**
     * Restituisce il prezzo della camera
     *
     * @post result == prezzoCamera
     *
     * @return prezzoCamera
     */
    public double getPrezzoCamera() {
        return prezzoCamera;
    }


    /**
     * Restituisce il nome della camera
     *
     * @post result == nomeCamera
     *
     * @return nomeCamera
     */
    public String getNomeCamera(){
        return nomeCamera;
    }

    /**
     * Imposta il nome della camera.
     *
     * @param nomeCamera
     *
     * @pre nomeCamera != null
     * @post this.nomeCamera == nomeCamera
     *
     */
    public void setNomeCamera(String nomeCamera){
        this.nomeCamera = nomeCamera;
    }


    /**
     * Imposta il prezzo della camera
     *
     * @pre prezzoCamera != null
     * @post this.prezzoCamera == prezzoCamera
     *
     * @param prezzoCamera il nuovo prezzo della camera
     */
    public void setPrezzoCamera(double prezzoCamera) {
        this.prezzoCamera = prezzoCamera;
    }


    /**
     * @param obj   L'oggetto da confrontare.
     * @return {@code true} se l'oggetto obj è un'istanza di Camera, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Camera camera = (Camera) obj;
        return numeroCamera == camera.numeroCamera && numeroMaxOccupanti.equals(camera.numeroMaxOccupanti) &&
                statoCamera.equals(camera.statoCamera) && Math.abs(prezzoCamera - camera.prezzoCamera) < EPSILON &&
                noteCamera.equalsIgnoreCase(camera.noteCamera) && nomeCamera.equalsIgnoreCase(nomeCamera);
    }

    @Override
    public String toString() {
        return "Camera{" +
                "numeroCamera=" + numeroCamera +
                ", nomeCamera='" + nomeCamera + '\'' +
                ", prezzoCamera=" + prezzoCamera +
                ", statoCamera=" + statoCamera +
                ", numeroMaxOccupanti=" + numeroMaxOccupanti +
                ", noteCamera='" + noteCamera + '\'' +
                '}';
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

