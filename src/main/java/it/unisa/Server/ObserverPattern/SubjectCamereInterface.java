package it.unisa.Server.ObserverPattern;

import java.rmi.RemoteException;

public interface SubjectCamereInterface {


    /**
     * Registra un observer per ricevere notifiche.
     *
     * @param observer
     * @pre observer != null
     * @post observers.contains(observer)
     */
    void attach(ObserverCamereInterface observer);


    /**
     * Rimuove un observer dalla lista di notifica.
     *
     * @pre observer != null
     * @post not observers.contains(observer)
     */
    void detach(ObserverCamereInterface observer);


    /**
     * Notifica tutti gli observer registrati.
     *
     */
    void notifyObservers() throws RemoteException;
}
