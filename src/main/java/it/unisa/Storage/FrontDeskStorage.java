package it.unisa.Storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public interface FrontDeskStorage<T>{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object oggetto) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
}
