package it.unisa.Storage;

import it.unisa.Common.Prenotazione;
import it.unisa.Storage.DAO.CameraDAO;

import java.sql.SQLException;
import java.util.Collection;

public interface FrontDeskStorage<T>
{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object oggetto) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;
    Collection<T> doRetriveByAttribute(String attribute , String value) throws SQLException;
}
