package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Common.Trattamento;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class TrattamentoDAO implements FrontDeskStorage<Trattamento>{
    private static final String TABLE_NAME = "Trattamento";

    @Override
    public synchronized void doSave(Trattamento trattamento) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        String query = "INSERT INTO Trattamento(Nome, Prezzo, IDPrenotazione) VALUES (?,?,?) ";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, trattamento.getNome());
            stmt.setDouble(2, trattamento.getPrezzo());
            stmt.setNull(3, Types.INTEGER); // gestito altrove

            stmt.executeUpdate();
        }finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }
    @Override
    public synchronized void doDelete(Trattamento trattamento) throws SQLException
    {
        if(trattamento != null && trattamento.getNome() != null){
            Connection connection = ConnectionStorage.getConnection();
            String query = "DELETE FROM Trattamento WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query))
            {
                stmt.setString(1, trattamento.getNome());
                if(stmt.executeUpdate() == 0){
                    throw new NoSuchElementException("elemento non trovato");
                }
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new SQLException("");
        }
    }

    @Override
    public synchronized Trattamento doRetriveByKey(Object nome) throws SQLException
    {
        if(nome instanceof String){
            Connection connection = ConnectionStorage.getConnection();
            String query = "SELECT * FROM Trattamento WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query))
            {
                stmt.setString(1, (String) nome);

                try (ResultSet rs = stmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return new Trattamento(
                                rs.getString("Nome"),
                                rs.getDouble("Prezzo")
                        );
                    }
                }
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }
        throw new NoSuchElementException("Trattamento non trovato");
    }

    @Override
    public synchronized Collection<Trattamento> doRetriveAll(String order) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        String query = "SELECT * FROM Trattamento";
        if (order.equalsIgnoreCase("decrescente"))
        {
            query += " ORDER BY Nome DESC ";
        }else{
            query += " ORDER BY Nome ASC";
        }

        Collection<Trattamento> trattamenti = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Trattamento t = new Trattamento(
                        rs.getString("Nome"),
                        rs.getDouble("Prezzo")
                );

                trattamenti.add(t);
            }
        }finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
        return trattamenti;
    }


    /**
     * Aggiorna il prezzo di un trattamento esistente nel database.
     *
     * @param trattamento Il trattamento con il prezzo aggiornato da persistere.
     * @throws SQLException Se il parametro è null o si verifica un errore durante l'accesso al database.
     *
     * Precondizioni:
     *   trattamento != null
     *   trattamento.getNome() deve corrispondere a un trattamento esistente nel database
     *   trattamento.getPrezzo() deve essere maggiore o uguale a 0
     *
     * Postcondizioni:
     *   Il prezzo del trattamento nel database viene aggiornato con il nuovo valore
     *   Il Nome (chiave primaria) rimane invariato
     *   L'eventuale IDPrenotazione associato rimane invariato
     */
    @Override
    public synchronized void doUpdate(Trattamento trattamento) throws SQLException{
        if(trattamento != null)
        {
            Connection connection = ConnectionStorage.getConnection();
            String query = "UPDATE Trattamento SET Prezzo = ? WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query))
            {
                stmt.setDouble(1, trattamento.getPrezzo());
                stmt.setString(2, trattamento.getNome());

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

    public synchronized Collection<Trattamento> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Trattamento> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM " + TrattamentoDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try{
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setObject(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();

                Trattamento trattamento;

                while (resultSet.next()) {
                    trattamento = new Trattamento();
                    trattamento.setNome(resultSet.getString("Nome"));
                    trattamento.setPrezzo(resultSet.getDouble("Prezzo"));

                    lista.add(trattamento);
                }

            }finally{
                if(connection != null){
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new RuntimeException("Attributo e/o valore non valido/i");
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessun trattamento con " + attribute + " = " + value + "!");

        return lista;
    }

    @Override
    public Collection<Trattamento> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blackListed, String orderBy) throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doSaveAll(List<Camera> listCamera) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}