package it.unisa.Storage.DAO;

import it.unisa.Common.Servizio;
import it.unisa.Storage.ConnectionStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class ServizioRicevutaDAO {

    public void doSave(Servizio o , int IDRicevutaFiscale , int quantinta) throws SQLException {
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO serviziricevuta values(?,?,?,?)");
            preparedStatement.setInt(1,IDRicevutaFiscale);
            preparedStatement.setString(2,o.getNome());
            preparedStatement.setInt(3,quantinta);
            preparedStatement.setDouble(4,o.getPrezzo());
            preparedStatement.executeUpdate();
    }

    /**
     * @param o
     * @throws SQLException
     */

    public void doDelete(Object o) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param oggetto
     * @return
     * @throws SQLException
     */

    public Object doRetriveByKey(Object oggetto) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param order
     * @return
     * @throws SQLException
     */

    public Collection doRetriveAll(String order) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param o
     * @throws SQLException
     */

    public void doUpdate(Object o) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param attribute
     * @param value
     * @return
     * @throws SQLException
     */

    public Collection doRetriveByAttribute(String attribute, Object value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param nome
     * @param cognome
     * @param nazionalita
     * @param dataDiNascita
     * @param blackListed
     * @param orderBy
     * @return
     * @throws SQLException
     */

    public Collection doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blackListed, String orderBy) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
