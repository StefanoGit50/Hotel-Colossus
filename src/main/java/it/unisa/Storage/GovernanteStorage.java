package it.unisa.Storage;

import java.sql.SQLException;
import java.util.Collection;

public interface GovernanteStorage<T>{

    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(int index) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;
    T doRetriveByAttribute(String attribute , String value);
}
