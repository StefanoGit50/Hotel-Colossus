package it.unisa.Storage.DAO;

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
    private static final String[] whitelist = {
            "nome",
            "prezzo"
    };


    /**
     * Salva un oggetto nel db
     *
     * @param trattamento
     * @pre o != null
     * @post inserisce l'oggetto nel database
     * @throws SQLException
     */
    @Override
    public synchronized void doSave(Trattamento trattamento) throws SQLException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO " + TrattamentoDAO.TABLE_NAME
                + " (nome, prezzo) VALUES (?, ?)";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, trattamento.getNome());
            ps.setDouble(2, trattamento.getPrezzo());

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

    /**
     * Salva un insieme di oggetti nel db
     *
     * @pre list != null
     * @post inserisce gli oggetti nel database
     *
     * @param list
     * @throws SQLException
     */
    @Override
    public void doSaveAll(List<Trattamento> list) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Cancella l oggetto dal db.
     *
     * @param trattamento
     * @pre o != null
     * @post l'oggetto non c'è piu nel db
     * @throws SQLException
     */
    @Override
    public synchronized void doDelete(Trattamento trattamento) throws SQLException
    {
        if(trattamento == null || trattamento.getNome() == null || trattamento.getNome().isBlank())
            throw new SQLException("ERRORE: trattamento nullo oppure nomeServizio nullo/vuoto");

        Connection connection = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM " + TrattamentoDAO.TABLE_NAME + " WHERE nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, trattamento.getNome());
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


    /**
     * Ottiene  oggetto da chiave.
     *
     * @param nome
     * @pre nome != null
     * @post result == null || result != null
     * @return T
     * @throws SQLException
     */
    @Override
    public synchronized Trattamento doRetriveByKey(Object nome) throws SQLException
    {
        if(!(nome instanceof String) ) throw new SQLException("ERROR: chiave non valida");

        Connection connection = null;
        PreparedStatement ps = null;
        Trattamento trattamento = new Trattamento();
        String query = "SELECT * FROM " + TrattamentoDAO.TABLE_NAME +  " WHERE nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);

            ps.setString(1, (String) nome);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();


            if (rs.next()) {
                trattamento.setNome(rs.getString("nome"));
                trattamento.setPrezzo(rs.getDouble("prezzo"));
            } else {
                throw new NoSuchElementException("ERRORE: trattamento non trovato");
            }
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
        return trattamento;
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
    public synchronized Collection<Trattamento> doRetriveAll(String order) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM " + TrattamentoDAO.TABLE_NAME;

        if (order != null && !order.isBlank() && DaoUtils.checkWhitelist(whitelist, order)) {
            query += " ORDER BY " + order;
        }

        Collection<Trattamento> trattamenti = new ArrayList<>();
        Trattamento trattamento = null;

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();

            while(rs.next()){
                trattamento = new Trattamento();
                trattamento.setNome(rs.getString("nome"));
                trattamento.setPrezzo(rs.getDouble("prezzo"));
                trattamenti.add(trattamento);
            }
        } catch (SQLException e) {
            throw e;
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

        if(trattamento == null || trattamento.getNome() == null || trattamento.getNome().isBlank())
            throw new SQLException("ERRORE: trattamento nullo oppure nomeServizio nullo/vuoto");

        Connection connection = null;
        PreparedStatement ps = null;
        String query = "UPDATE " + TrattamentoDAO.TABLE_NAME + " SET Prezzo = ? WHERE Nome = ?";

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);
            ps.setDouble(1, trattamento.getPrezzo());
            ps.setString(2, trattamento.getNome());
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
    public synchronized Collection<Trattamento> doRetriveByAttribute(String attribute, Object value) throws SQLException {

        Connection connection = null;
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


}