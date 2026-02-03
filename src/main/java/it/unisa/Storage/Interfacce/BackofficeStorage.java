package it.unisa.Storage.Interfacce;

import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.Collection;

public interface BackofficeStorage<T>
{

    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre o != null
     */
    void doSave(T o) throws SQLException;


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre o != null
     */
    void doDelete(T o) throws SQLException;


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre index != null
     * @post result == null || result != null
     */
    T doRetriveByKey(Object index) throws SQLException;


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre order != null && order != ""
     * @post result != null
     */
    Collection<T> doRetriveAll(String order) throws SQLException;


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre o != null
     */
    void doUpdate(T o) throws SQLException;


    /**
     * Permette di recuperare tutte le entità sul database che hanno un attributo di nome {@code attribute}
     * con un valore uguale a {@code valore}.
     * @param attribute nome dell'attributo.
     * @param value valore dell'attributo.
     * @return  {@code Collection<T>} di oggetti per il quale {@code attribute} {@code =value}.
     * @throws SQLException se c'è errore di accesso/connessione/retrieval col database.
     *
     * Esegue un'operazione specifica del metodo.
     *
     * @pre attribute != null && attribute != "" && value != null
     * @post result != null
     */
    Collection<T> doRetriveByAttribute(String attribute, Object value) throws SQLException;


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre nome != null || sesso != null || ruolo != null || orderBy != null
     * @post result != null
     */
    Collection<T> doFilter(String nome, String sesso, Ruolo ruolo, String orderBy) throws  SQLException;
}
