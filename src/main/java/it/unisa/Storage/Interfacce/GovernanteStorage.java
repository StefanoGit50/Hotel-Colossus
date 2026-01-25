package it.unisa.Storage.Interfacce;

import java.sql.SQLException;
import java.util.Collection;

public interface GovernanteStorage<T>{

    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;
    Collection<T> doRetriveByAttribute(String attribute , String value) throws SQLException;
}
