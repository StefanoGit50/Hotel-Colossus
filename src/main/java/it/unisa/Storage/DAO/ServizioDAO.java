package it.unisa.Storage.DAO;

import it.unisa.Common.Servizio;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;


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
       throw new NoSuchElementException("servizio non trovato");
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

    /**
     * Aggiorna il prezzo di un servizio esistente nel database.
     *
     * @param servizio Il servizio con il prezzo aggiornato da persistere.
     * @throws SQLException Se il parametro è null o si verifica un errore durante l'accesso al database.
     *
     * Precondizioni:
     *   servizio != null
     *   servizio.getNome() deve corrispondere a un servizio esistente nel database
     *   servizio.getPrezzo() deve essere maggiore o uguale a 0
     *
     * Postcondizioni:
     *   Il prezzo del servizio nel database viene aggiornato con il nuovo valore
     *   Il Nome (chiave primaria) rimane invariato
     *   L'eventuale IDPrenotazione associato rimane invariato
     */
    @Override
    public synchronized void doUpdate(Servizio servizio) throws SQLException
    {
        if(servizio != null)
        {
            Connection connection = ConnectionStorage.getConnection();
            String query = "UPDATE Servizio SET Prezzo = ? WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query))
            {
                stmt.setDouble(1, servizio.getPrezzo());
                stmt.setString(2, servizio.getNome());

                stmt.executeUpdate();
            }
            finally
            {
                if(connection != null)
                {
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }
        else
        {
            throw new SQLException();
        }
    }

    @Override
    public ArrayList<Servizio> doRetriveByAttribute(String attribute, String value) throws SQLException {
        if(attribute != null && !attribute.isEmpty() && value != null && !value.isEmpty()){
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM servizio WHERE " + attribute + " = ?")){

                preparedStatement.setString(1,value);
                ResultSet resultSet = preparedStatement.getResultSet();

                    if(!resultSet.next()){
                        return null;
                    }
               ArrayList<Servizio> servizios = new ArrayList<>();
                while(resultSet.next()){
                    String nome = resultSet.getString(1);
                    Double prezzo = resultSet.getDouble(2);
                servizios.add(new Servizio(nome , prezzo));
                }

                return servizios;
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new RuntimeException();
        }
    }
}
