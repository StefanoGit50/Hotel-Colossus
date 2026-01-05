package it.unisa.Storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public interface BackofficeStorage<T>
{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object index) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
}
