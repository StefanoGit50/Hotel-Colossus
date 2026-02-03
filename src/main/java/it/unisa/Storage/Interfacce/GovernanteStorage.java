package it.unisa.Storage.Interfacce;

import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

public interface GovernanteStorage<T>{

    /**
     * Salva un oggetto nel db
     *
     * @pre o != null
     * @post inserisce l'oggetto nel database
     */
    void doSave(T o) throws SQLException;


    /**
     * Cancella l oggetto dal db.
     *
     * @param o
     * @pre o != null
     * @post l'oggetto non c'è piu nel db
     */
    void doDelete(T o) throws SQLException;


    /**
     * Ottiene tutti gli oggetti dal DB.
     *
     * @pre order != null && order != ""
     * @post result != null
     */
    Collection<T> doRetriveAll(String order) throws SQLException;


    /**
     * Aggiorna i dati di un oggetto esistente nel database.
     *
     * @param o La camera con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NoSuchElementException Se il parametro o è null quindi non trovato.
     * @pre o != null
     * @post record nel database viene aggiornato con i nuovi valori
     * @post la chiave primaria rimane invariata
     */
    void doUpdate(T o) throws SQLException;


    /**
     *
     * Ottiene oggetto tramite attributo.
     *
     * @pre attribute != null && attribute != "" && value != null && value != ""
     * @post result != null
     *
     *
     * @param attribute;
     * @param value;
     * @return Collection<>;
     * @throws SQLException;
     */
    Collection<T> doRetriveByAttribute(String attribute , String value) throws SQLException;
}
