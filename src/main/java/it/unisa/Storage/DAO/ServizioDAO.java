package it.unisa.Storage.DAO;

import it.unisa.Common.Servizio;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;


public class ServizioDAO implements FrontDeskStorage<Servizio>
{
    private static final String TABLE_NAME= "Servizio";
    private static final String[] whitelist = {
            "nome",
            "prezzo"
    };

    @Override
    public synchronized void doSave(Servizio servizio) throws SQLException
    {
        Connection connection = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + ServizioDAO.TABLE_NAME +
                " (Nome, Prezzo) " +
                " VALUES (?, ?, ?) ";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, servizio.getNome());
            ps.setDouble(2, servizio.getPrezzo());

            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }
    /**
     * @param list
     * @throws SQLException
     */
    @Override
    public void doSaveAll(List<Servizio> list) throws SQLException {
        throw new  UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void doDelete(Servizio servizio) throws SQLException
    {
        if(servizio == null || servizio.getNome() == null || servizio.getNome().isBlank())
            throw new SQLException("ERRORE: servizio nullo oppure nomeServizio nullo/vuoto");

        Connection connection = null;
        PreparedStatement ps = null;
        connection = ConnectionStorage.getConnection();

        String query = "DELETE FROM " + ServizioDAO.TABLE_NAME
                + " WHERE Nome = ?";

        try {
            connection =  ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, servizio.getNome());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        }finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    @Override
    public synchronized Servizio doRetriveByKey(Object nome) throws SQLException
    {
        if ( !(nome instanceof String)) throw new SQLException("ERROR: chiave non valida");

        Connection connection = null;
        PreparedStatement ps = null;
        Servizio servizio = new Servizio();
        String query = "SELECT * FROM " + ServizioDAO.TABLE_NAME + " WHERE Nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, (String) nome);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                servizio.setNome(rs.getString("Nome"));
                servizio.setPrezzo(rs.getDouble("Prezzo"));
            } else {
                throw new NoSuchElementException("ERRORE: servizio non trovato");
            }
        }finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
        return servizio;
    }

    @Override
    public synchronized Collection<Servizio> doRetriveAll(String order) throws SQLException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM " + ServizioDAO.TABLE_NAME;

        Collection<Servizio> servizi = new ArrayList<>();
        Servizio servizio = null;

        if (order != null && !order.isBlank() && DaoUtils.checkWhitelist(whitelist, order)) {
            query += " ORDER BY " + order;
        }

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                do {
                    servizio = new Servizio();
                    servizio.setNome(rs.getString("Nome"));
                    servizio.setPrezzo(rs.getDouble("Prezzo"));
                    servizi.add(servizio);
                } while (rs.next());
            } else {
                throw new NoSuchElementException("ERRORE: nessun servizio non trovato");
            }
        }finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    connection.close();
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
        if(servizio == null || servizio.getNome() == null || servizio.getNome().isBlank())
            throw new SQLException("ERRORE: servizio nullo oppure nomeServizio nullo/vuoto");

        Connection connection = null;
        PreparedStatement ps = null;
        String query = "UPDATE " + ServizioDAO.TABLE_NAME +  " SET Prezzo = ? WHERE Nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setDouble(1, servizio.getPrezzo());
            ps.setString(2, servizio.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    public synchronized Collection<Servizio> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Servizio> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM " + ServizioDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try{
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setObject(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();

                Servizio servizio;
                while (resultSet.next()) {
                    servizio = new Servizio();
                    servizio.setNome(resultSet.getString("Nome"));
                    servizio.setPrezzo(resultSet.getDouble("Prezzo"));

                    lista.add(servizio);
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

        if(lista.isEmpty()) throw new NoSuchElementException("Nessun servizio con " + attribute + " = " + value + "!");

        return lista;
    }

    @Override
    public Collection<Servizio> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blackListed, String orderBy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
