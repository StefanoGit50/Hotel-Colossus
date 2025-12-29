package it.unisa.Storage;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Questa interfaccia serve per il caricamento dei dati/Cancellazione/Aggiornamento dei
 * dati nel database oppure nel recupero delle informazioni nel DB di HotelCollosus
 * @param <T>
 */
public interface BackofficeStorage<T>{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(int index) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
}
