package it.unisa.Storage;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public interface BackofficeStorage<T>
{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object index) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;
    Collection<T> doRetriveByAttribute(String attribute , String value) throws SQLException;
    Collection<T> doFilter(String nome, String sesso, Ruolo ruolo) throws  SQLException;
}
