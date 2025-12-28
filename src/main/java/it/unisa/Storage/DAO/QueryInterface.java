package it.unisa.Storage.DAO;

import java.sql.SQLException;
import java.util.Collection;

public interface QueryInterface<T>{
        void doSave(T o) throws SQLException;

        void doDelete(T o) throws SQLException;

        T doRetriveByKey(int index) throws SQLException;

        Collection<T> doRetriveAll(String order) throws SQLException;
}
