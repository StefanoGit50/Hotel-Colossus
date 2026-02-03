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


public class ServizioDAO implements FrontDeskStorage<Servizio> {
    private static final String TABLE_NAME= "Servizio";
    private static final String[] whitelist = {
            "nome",
            "prezzo"
    };
    private Connection connection;


    /**
     * Salva un oggetto nel db
     *
     * @param servizio
     * @pre o != null
     * @post inserisce l'oggetto nel database
     * @throws SQLException
     */
    @Override
    public synchronized void doSave(Servizio servizio) throws SQLException    {
        connection = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + ServizioDAO.TABLE_NAME +
                " (IDServizio,Nome, Prezzo) " +
                " VALUES (?,?) ";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, servizio.getNome());
            ps.setDouble(2, servizio.getPrezzo());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);
                    servizio.setId(idGenerato);
                }
            }


        } catch(SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    ConnectionStorage.releaseConnection(connection);

            }
        }
    }
    /**
     * @param list
     *
     * @pre list != null
     * @post inserisce gli oggetti nel database
     *
     * @throws SQLException
     */
    @Override
    public void doSaveAll(List<Servizio> list) throws SQLException {
        throw new  UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Cancella l oggetto dal db.
     *
     * @param servizio
     * @pre o != null
     * @post l'oggetto non c'è piu nel db
     * @throws SQLException
     */
    @Override
    public synchronized void doDelete(Servizio servizio) throws SQLException
    {
        if(servizio == null || servizio.getNome() == null || servizio.getNome().isBlank())
            throw new SQLException("ERRORE: servizio nullo oppure nomeServizio nullo/vuoto");


        PreparedStatement ps = null;
        connection = ConnectionStorage.getConnection();

        String query = "DELETE FROM " + ServizioDAO.TABLE_NAME
                + " WHERE IDServizio = ?";

        try {
            connection =  ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setInt(1, servizio.getId());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        }finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    ConnectionStorage.releaseConnection(connection);

            }
        }
    }


    /**
     * Ottiene  oggetto da chiave.
     *
     * @param nome
     * @pre oggetto != null
     * @post result == null || result != null
     * @return Servizio
     * @throws SQLException
     */
    @Override
    public synchronized Servizio doRetriveByKey(Object nome) throws SQLException
    {
        if ( !(nome instanceof String)) throw new SQLException("ERROR: chiave non valida");

         connection = null;
        PreparedStatement ps = null;
        Servizio servizio = new Servizio();
        String query = "SELECT * FROM " + ServizioDAO.TABLE_NAME + " WHERE Nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, (String) nome);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                servizio.setId(rs.getInt("IDServizio"));
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
                    ConnectionStorage.releaseConnection(connection);

            }
        }
        return servizio;
    }


    /**
     * Ottiene tutti gli oggetti dal DB.
     *
     * @param order
     * @pre order != null && order != ""
     * @post result != null
     * @return {@link Collection}
     * @throws SQLException
     */
    @Override
    public synchronized Collection<Servizio> doRetriveAll(String order) throws SQLException
    {
         connection = null;
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
                    servizio.setId(rs.getInt("IDServizio"));
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
        if(servizio == null || servizio.getNome() == null || servizio.getNome().isBlank())
            throw new SQLException("ERRORE: servizio nullo oppure nomeServizio nullo/vuoto");

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
                    ConnectionStorage.releaseConnection(connection);
            }
        }
    }


    /**
     *
     * Ottiene oggetto tramite attributo.
     *
     * @pre attribute != null && attribute != "" && value != null && value != ""
     * @post result != null
     *
     *
     * @param attribute;
     * @param value;
     * @return Collection<>;
     * @throws SQLException;
     */
    public synchronized Collection<Servizio> doRetriveByAttribute(String attribute, Object value) throws SQLException {

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
                    servizio.setId(resultSet.getInt("IDServizio"));
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


}
