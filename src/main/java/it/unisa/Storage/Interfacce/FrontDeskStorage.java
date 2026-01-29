package it.unisa.Storage.Interfacce;

import it.unisa.Common.Camera;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface FrontDeskStorage<T>
{
    void doSave(T o) throws SQLException;
    void doDelete(T o) throws SQLException;
    T doRetriveByKey(Object oggetto) throws SQLException;
    Collection<T> doRetriveAll(String order) throws SQLException;
    void doUpdate(T o) throws SQLException;
    Collection<T> doRetriveByAttribute(String attribute , Object value) throws SQLException;
    Collection<T> doFilter(String nome , String cognome , String nazionalita , LocalDate dataDiNascita , Boolean blackListed, String orderBy) throws SQLException;
    void doSaveAll(List<Camera> listCamera) throws SQLException ;
}