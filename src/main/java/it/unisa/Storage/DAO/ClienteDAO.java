package it.unisa.Storage.DAO;

import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.Eccezioni.*;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class ClienteDAO implements FrontDeskStorage<Cliente>
{
    public synchronized void doSave(Cliente o) throws SQLException{
        Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTE VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
                preparedStatement.setString(1,o.getCf());
                preparedStatement.setString(2,o.getNome());
                preparedStatement.setString(3,o.getCognome());
                preparedStatement.setInt(4,o.getCAP());
                preparedStatement.setString(5,o.getComune());
                preparedStatement.setInt(6,o.getNumeroCivico());
                preparedStatement.setString(7,o.getProvincia());
                preparedStatement.setString(8,o.getVia());
                preparedStatement.setString(9,o.getEmail());
                preparedStatement.setString(10,o.getSesso());
                preparedStatement.setInt(11,o.getNumeroTelefono());
                preparedStatement.setString(12,o.getMetodoDiPagamento());
                preparedStatement.setString(13,o.getCittadinanza());
                preparedStatement.setBoolean(14,o.isBlacklisted());
                preparedStatement.executeUpdate();
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
    }

    public synchronized static void doSaveStatic(Cliente o) throws SQLException{
        Connection connection = ConnectionStorage.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTE VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
            preparedStatement.setString(1,o.getCf());
            preparedStatement.setString(2,o.getNome());
            preparedStatement.setString(3,o.getCognome());
            preparedStatement.setInt(4,o.getCAP());
            preparedStatement.setString(5,o.getComune());
            preparedStatement.setInt(6,o.getNumeroCivico());
            preparedStatement.setString(7,o.getProvincia());
            preparedStatement.setString(8,o.getVia());
            preparedStatement.setString(9,o.getEmail());
            preparedStatement.setString(10,o.getSesso());
            preparedStatement.setInt(11,o.getNumeroTelefono());
            preparedStatement.setString(12,o.getMetodoDiPagamento());
            preparedStatement.setString(13,o.getCittadinanza());
            preparedStatement.setBoolean(14,o.isBlacklisted());
            preparedStatement.executeUpdate();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized Cliente doRetriveByKey(Object oggetto) throws SQLException{
        if(oggetto instanceof String){
            String cf = (String) oggetto;
            Connection connection = ConnectionStorage.getConnection();
            String cf1,nome,cognome,comune,provincia,via,email,sesso,metodoDiPagamento,cittadinazione;
            Integer cap , civico , telefono;
            LocalDate date;
            Boolean isBackListed;
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Cliente WHERE CF = ?");
                ResultSet resultSet =  preparedStatement.getResultSet())  {
                preparedStatement.setString(1,cf);
                preparedStatement.executeQuery();
                cf1 = (String) resultSet.getObject(1);
                nome = (String) resultSet.getObject(2);
                cognome = (String) resultSet.getObject(3);
                cap = (Integer) resultSet.getObject(4);
                comune = (String) resultSet.getObject(5);
                civico = (Integer) resultSet.getObject(6);
                provincia = (String) resultSet.getObject(7);
                via = (String) resultSet.getObject(8);
                email = (String) resultSet.getObject(9);
                sesso = (String) resultSet.getObject(10);
                telefono = (Integer) resultSet.getObject(11);
                metodoDiPagamento = (String) resultSet.getObject(12);
                cittadinazione = (String) resultSet.getObject(13);
                date = (LocalDate) resultSet.getObject(14);
                isBackListed = (Boolean) resultSet.getObject(15);
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
            Cliente cliente = new Cliente(nome,cognome,cittadinazione,provincia,comune,via,civico,cap,telefono,sesso,date,cf1,email,metodoDiPagamento);
            cliente.setBlacklisted(isBackListed);
            return cliente;
        }else{
            throw new SQLException();
        }
    }

    @Override
    public synchronized void doDelete(Cliente o) throws SQLException {
        if(o != null){
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Cliente WHERE CF = ?")){
                preparedStatement.setString(1,o.getCf());
                preparedStatement.executeQuery();
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NullPointerException();
        }
    }

    @Override
    public synchronized Collection<Cliente> doRetriveAll(String order) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql =  "SELECT * FROM Cliente ORDER BY ? ";
        if(order.equalsIgnoreCase("decrescente")){
            sql += "DESC";
        }else{
            sql+= "ASC";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,"CF");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String cf1 = (String) resultSet.getObject(1);
                String nome = (String) resultSet.getObject(2);
                String cognome = (String) resultSet.getObject(3);
                Integer cap = (Integer) resultSet.getObject(4);
                String comune = (String) resultSet.getObject(5);
                Integer civico = (Integer) resultSet.getObject(6);
                String provincia = (String) resultSet.getObject(7);
                String via = (String) resultSet.getObject(8);
                String email = (String) resultSet.getObject(9);
                String sesso = (String) resultSet.getObject(10);
                Integer telefono = (Integer) resultSet.getObject(11);
                String metodoDiPagamento = (String) resultSet.getObject(12);
                String cittadinazione = (String) resultSet.getObject(13);
                LocalDate date = (LocalDate) resultSet.getObject(14);
                Boolean isBackListed = (Boolean) resultSet.getObject(15);
                clientes.add(new Cliente(nome,cognome,cittadinazione,provincia,comune,via,civico,cap,telefono,sesso,date,cf1,email,metodoDiPagamento));
            }
            resultSet.close();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }

        return clientes;
    }

}
