package it.unisa.Storage;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.Collection;

public interface BackofficeStorage<T>
{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object index) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;

    /**
     * Permette di recuperare tutte le entità sul database che hanno un attributo di nome {@code attribute}
     * con un valore uguale a {@code valore}.
     * @param attribute nome dell'attributo.
     * @param value valore dell'attributo.
     * @return  {@code Collection<T>} di oggetti per il quale {@code attribute} {@code =value}.
     * @throws SQLException se c'è errore di accesso/connessione/retrieval col database.
     */
    Collection<T> doRetriveByAttribute(String attribute, Object value) throws SQLException;
    Collection<T> doFilter(String nome, String sesso, Ruolo ruolo, String orderBy) throws  SQLException;
}
