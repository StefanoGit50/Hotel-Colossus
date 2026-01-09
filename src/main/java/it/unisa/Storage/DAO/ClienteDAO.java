package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.RicevutaFiscale;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.net.ConnectException;
import java.rmi.RemoteException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ClienteDAO implements FrontDeskStorage<Cliente>
{
    public static final String TABLE_NAME = "Cliente";
    private static final String[] whitelist = {
            "CF",
            "Stipedio",
            "Nome",
            "Cognome",
            "Cap",
            "DataAssunzione",
            "Telefono",
            "Cittadinanza",
            "EmailAziendale",
            "Sesso",
            "Ruolo",
            "DataRilascio",
            "TipoDocumento",
            "Via",
            "Provincia",
            "Comune",
            "Civico",
            "NumeroDocumento",
            "DataScadenza",
            "CF1"
    };

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


    /**
     * Aggiorna i dati di un cliente esistente nel database.
     *
     * @param o Il cliente con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NullPointerException Se il parametro o è null.
     *
     * Precondizioni:
     * o != null
     * Tutti i campi obbligatori di o devono essere valorizzati correttamente
     *
     * Postcondizioni:
     * Il record del cliente nel database viene aggiornato con i nuovi valori
     * Il CF (chiave primaria) rimane invariato
     * La connessione al database viene rilasciata correttamente
     */
    @Override
    public synchronized void doUpdate(Cliente o) throws SQLException
    {
        if(o != null)
        {
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Cliente SET nome = ?, cognome = ?, Cap = ?, comune = ?, " +
                            "civico = ?, provincia = ?, via = ?, Email = ?, Sesso = ?, " +
                            "telefono = ?, MetodoDiPagamento = ?, Cittadinanza = ?, " +
                            "DataDiNascita = ?, IsBackListed = ? WHERE CF = ?")){

                preparedStatement.setString(1, o.getNome());
                preparedStatement.setString(2, o.getCognome());
                preparedStatement.setInt(3, o.getCAP());
                preparedStatement.setString(4, o.getComune());
                preparedStatement.setInt(5, o.getNumeroCivico());
                preparedStatement.setString(6, o.getProvincia());
                preparedStatement.setString(7, o.getVia());
                preparedStatement.setString(8, o.getEmail());
                preparedStatement.setString(9, o.getSesso());
                preparedStatement.setString(10, o.getNumeroTelefono());
                preparedStatement.setString(11, o.getMetodoDiPagamento());
                preparedStatement.setString(12, o.getCittadinanza());
                preparedStatement.setDate(13, Date.valueOf(o.getDataNascita()));
                preparedStatement.setBoolean(14, o.isBlacklisted());
                preparedStatement.setString(15, o.getCf());
                preparedStatement.executeUpdate();
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
            throw new NullPointerException();
        }
    }

    @Override
    public synchronized Collection<Cliente> doRetriveByAttribute(String attribute, String value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Cliente> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null && !value.isEmpty()){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM "+ ClienteDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try{
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();
                Cliente cliente;
                while (resultSet.next()) {
                    cliente = new Cliente();
                    cliente.setCf(resultSet.getString("CF"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setCognome(resultSet.getString("cognome"));
                    cliente.setCognome(resultSet.getString("civico"));
                    cliente.setCAP(resultSet.getInt("Cap"));
                    cliente.setComune(resultSet.getString("Comune"));
                    cliente.setCittadinanza(resultSet.getString("Cittadinanza"));
                    cliente.setNumeroCivico(resultSet.getInt("civico"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setVia(resultSet.getString("Via"));
                    cliente.setEmail(resultSet.getString("Email"));
                    cliente.setSesso(resultSet.getString("Sesso"));
                    cliente.setNumeroTelefono(resultSet.getString("telefono"));
                    cliente.setMetodoDiPagamento(resultSet.getString("MetodoDiPagamento"));
                    cliente.setBlacklisted(resultSet.getBoolean("IsBackListed"));
                    cliente.setDataNascita(resultSet.getDate("DataDiNascita").toLocalDate());
                    lista.add(cliente);
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

        if(lista.isEmpty()) throw new NoSuchElementException("Nessuna cliente con " + attribute + " = " + value + "!");

        return lista;
    }


    // TODO: FINIRE I METODI doFilter per le collezioni E POI FARE METODI doFilter e doRetrieveByAttribute per Prenotazione.
    // Filtro clienti
    List<Cliente> doFilter(String nome, String cognome, String nazionalita, LocalDate dataNascita, String sesso, String orderBy) throws RemoteException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Cliente> lista = new ArrayList<>();
        String selectSQL = "";

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[5];
        params[0] = nome != null && !nome.isEmpty();
        params[1] = cognome != null && !cognome.isEmpty();
        params[2] = nazionalita != null && !nazionalita.isEmpty();
        params[3] = dataNascita != null && dataNascita.isBefore(LocalDate.now());
        params[4] = sesso != null && !sesso.isEmpty();

        // Se tutti i parametri sono nulli allora lancia l'eccezione
        if (!params[0] && !params[1] && !params[2] && !params[3] && !params[4]) {
            throw new RuntimeException("Nessun parametro inserito!");
        }

        // Conta il numero di parametri che sono veri
        int count = 0;
        for (boolean b : params) {
            if(b) count++;
        }
        // Il numero di AND della query è pari a count - 1

        selectSQL += " SELECT * FROM " + ClienteDAO.TABLE_NAME + " WHERE ";
        for (int i = 0; i < params.length; i++) {
            if (i == 0 && params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la query di ricerca
                selectSQL += " nome = " + nome + " ";
            }
            if (i == 1 && params[1]) {
                selectSQL += " cognome = " + cognome + " ";
            }
            if (i == 2 && params[2]) {
                selectSQL += " Cittadinanza = " + nazionalita + " ";
            }
            if (i == 3 && params[3]) {
                selectSQL += " DataDiNascita = " + dataNascita + " ";
            }
            if (i == 4 && params[4]) {
                selectSQL += " sesso = " + sesso + " ";
            }
            if (count != 0) {
                selectSQL += " AND ";
                count--;
            }
        }

        if(orderBy != null && !orderBy.isEmpty()) {
            if (DaoUtils.checkWhitelist(whitelist, orderBy))
                selectSQL +=  " ORDER BY " + orderBy + ";";
        }

        ArrayList<Cliente> results = null;
        Connection conn = null;
        try {
            conn = ConnectionStorage.getConnection();

            PreparedStatement ps = null;
            ResultSet resultSet = null;

            try {
                ps = conn.prepareStatement(selectSQL);
                resultSet = ps.executeQuery();
                results = new ArrayList<Cliente>();
                Cliente cliente;
                while (resultSet.next()) {
                    cliente = new Cliente();
                    cliente.setCf(resultSet.getString("CF"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setCognome(resultSet.getString("cognome"));
                    cliente.setCAP(resultSet.getInt("Cap"));
                    cliente.setComune(resultSet.getString("comune"));
                    cliente.setNumeroCivico(resultSet.getInt("civico"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setVia(resultSet.getString("via"));
                    cliente.setEmail(resultSet.getString("Email"));
                    cliente.setSesso(resultSet.getString("Sesso"));
                    cliente.setNumeroTelefono(resultSet.getString("telefono"));
                    cliente.setMetodoDiPagamento(resultSet.getString("MetodoDiPagamento"));
                    cliente.setCittadinanza(resultSet.getString("Cittadinanza"));

                    results.add(cliente);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (ps != null)
                    ps.close();
                ConnectionStorage.releaseConnection(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

}
