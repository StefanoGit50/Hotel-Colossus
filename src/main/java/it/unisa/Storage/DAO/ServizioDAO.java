package it.unisa.Storage.DAO;

import it.unisa.Common.Servizio;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class ServizioDAO implements FrontDeskStorage<Servizio>
{
    @Override
    public synchronized void doSave(Servizio servizio) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        String query = "INSERT INTO Servizio(Nome, Prezzo, IDPrenotazione) VALUES (?, ?, ?) ";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, servizio.getNome());
            stmt.setDouble(2, servizio.getPrezzo());
            stmt.setNull(3, Types.INTEGER); // gestito altrove

            stmt.executeUpdate();
        }finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized void doDelete(Servizio servizio) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        String query = "DELETE FROM Servizio WHERE Nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, servizio.getNome());
            stmt.executeUpdate();
        }finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized Servizio doRetriveByKey(Object nome) throws SQLException
    {
        if(nome instanceof String){
            Connection connection = ConnectionStorage.getConnection();
            String query = "SELECT * FROM Servizio WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query))
            {
                stmt.setString(1, (String) nome);

                try (ResultSet rs = stmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return new Servizio(
                                rs.getString("Nome"),
                                rs.getDouble("Prezzo"));
                    }
                }
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new SQLException();
        }
        return null;
    }

    @Override
    public synchronized Collection<Servizio> doRetriveAll(String order) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        String query = "SELECT * FROM Servizio ";
        if(order.equalsIgnoreCase("decrescente")){
            query += " ORDER BY Nome DESC " ;
        }else{
            query += " ORDER BY Nome ASC" ;
        }

        Collection<Servizio> servizi = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {

                Servizio s = new Servizio(
                        rs.getString("Nome"),
                        rs.getDouble("Prezzo"));

                servizi.add(s);
            }
        }finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }

        return servizi;
    }
}
