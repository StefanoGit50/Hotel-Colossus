package it.unisa.Storage.DAO;

import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ClienteDAO implements FrontDeskStorage<Cliente> {
    public void doSave(Cliente o) throws SQLException {
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1,o.get);
    }

    @Override
    public Cliente doRetriveByKey(int index) throws SQLException{

    }

    @Override
    public void doDelete(Cliente o) throws SQLException {

    }

    @Override
    public Collection<Cliente> doRetriveAll(String order) throws SQLException {
        return List.of();
    }

}
