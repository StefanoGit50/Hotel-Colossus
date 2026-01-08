package it.unisa.Storage.DAO;

import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class ClienteDAO implements FrontDeskStorage<Cliente>
{
    public synchronized void doSave(Cliente o) throws SQLException{
        Connection connection = null;
        try{
                connection = ConnectionStorage.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTE(\n" +
                        "    CF, nome, cognome, Cap, comune, civico, provincia, via,\n" +
                        "    Email, Sesso, telefono, MetodoDiPagamento, Cittadinanza,\n" +
                        "    DataDiNascita, IsBackListed\n" +
                        ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
                preparedStatement.setString(11,o.getNumeroTelefono());
                preparedStatement.setString(12,o.getMetodoDiPagamento());
                preparedStatement.setString(13,o.getCittadinanza());
                Date date = Date.valueOf(o.getDataNascita());
                preparedStatement.setDate(14,date);
                preparedStatement.setBoolean(15,o.isBlacklisted());
                preparedStatement.executeUpdate();
            }finally {
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
            String cf1 = null,nome = null,cognome = null,comune = null,provincia = null,via = null,email = null,sesso = null,metodoDiPagamento = null,cittadinazione = null,telefono = null , cap = null;
            Integer civico = null;
            LocalDate date = null;
            Boolean isBackListed = null;
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Cliente WHERE CF = ?")){
                preparedStatement.setString(1,cf);
                preparedStatement.executeQuery();
                ResultSet resultSet =  preparedStatement.getResultSet();
                if(resultSet.next()){
                    cf1 = (String) resultSet.getObject(1);
                    nome = (String) resultSet.getObject(2);
                    cognome = (String) resultSet.getObject(3);
                    cap = (String) resultSet.getObject(4);
                    comune = (String) resultSet.getObject(5);
                    civico = (Integer) resultSet.getObject(6);
                    provincia = (String) resultSet.getObject(7);
                    via = (String) resultSet.getObject(8);
                    email = (String) resultSet.getObject(9);
                    sesso = (String) resultSet.getObject(10);
                    telefono = (String) resultSet.getObject(11);
                    metodoDiPagamento = (String) resultSet.getObject(12);
                    cittadinazione = (String) resultSet.getObject(13);
                    Date date1 = (Date) resultSet.getObject(14);
                    date = date1.toLocalDate();
                    isBackListed = (Boolean) resultSet.getObject(15);
                }
                resultSet.close();
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
            Cliente cliente = null;

            if(nome != null && cognome != null && cittadinazione != null && provincia != null && comune != null && via != null && civico != null && cap != null && telefono != null && sesso != null && date != null && cf1 != null && email != null && metodoDiPagamento != null && isBackListed != null){
                System.out.println("ciao");
                cliente = new Cliente(nome,cognome,cittadinazione,provincia,comune,via,civico,Integer.parseInt(cap),telefono,sesso,date,cf1,email,metodoDiPagamento);
                cliente.setBlacklisted(isBackListed);
            }

            if( cliente == null )
                throw new NoSuchElementException("cliente non trovato");

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
                preparedStatement.executeUpdate();
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
              String  cap = (String) resultSet.getObject(4);
               String comune = (String) resultSet.getObject(5);
               Integer civico = (Integer) resultSet.getObject(6);
               String provincia = (String) resultSet.getObject(7);
               String via = (String) resultSet.getObject(8);
               String email = (String) resultSet.getObject(9);
              String  sesso = (String) resultSet.getObject(10);
              String  telefono = (String) resultSet.getObject(11);
               String metodoDiPagamento = (String) resultSet.getObject(12);
              String  cittadinazione = (String) resultSet.getObject(13);
                Date date1 = (Date) resultSet.getObject(14);
               LocalDate date = date1.toLocalDate();
              Boolean  isBackListed = (Boolean) resultSet.getObject(15);
                clientes.add(new Cliente(nome,cognome,cittadinazione,provincia,comune,via,civico,Integer.parseInt(cap),telefono,sesso,date,cf1,email,metodoDiPagamento));
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
