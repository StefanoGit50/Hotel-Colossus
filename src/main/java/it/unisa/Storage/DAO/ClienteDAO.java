package it.unisa.Storage.DAO;

import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class ClienteDAO implements FrontDeskStorage<Cliente> {

    private Connection con;
    private Cliente cliente=null;
    private  ResultSet resultSet;


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

    /**
     * @param o;
     * @throws SQLException;
     */
    public synchronized void doSave(Cliente o) throws SQLException{

        try{
                con= ConnectionStorage.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO CLIENTE(\n" +
                        "CF, nome, cognome, Cap, comune, civico, provincia, via,\n" +
                        "Email, Sesso, telefono, Cittadinanza,\n" +
                        "DataDiNascita, IsBackListed\n" +
                        ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
                preparedStatement.setString(12,o.getCittadinanza());
                Date date = Date.valueOf(o.getDataNascita());
                preparedStatement.setDate(13,date);
                preparedStatement.setBoolean(14,o.isBlacklisted());
                preparedStatement.executeUpdate();
            }finally {
                if(con != null){
                    ConnectionStorage.releaseConnection(con);
                }
            }
    }

    /**
     * @param oggetto è un oggetto (rappresenta la chiave del );
     * @return Cliente;
     * @throws SQLException;
     */

    @Override
    public synchronized Cliente doRetriveByKey(Object oggetto) throws SQLException{
        if (oggetto instanceof String) {
            String cf = (String) oggetto;
            con = ConnectionStorage.getConnection();
            String cf1 = null,nome = null,cognome = null,comune = null,provincia = null,via = null,email = null,sesso = null,cittadinanza = null,telefono = null , cap = null;
            Integer civico = null;
            LocalDate date = null;
            Boolean isBlackListed = false;
            try(PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Cliente WHERE CF = ?")){
                preparedStatement.setString(1,cf);
                preparedStatement.executeQuery();
                resultSet =  preparedStatement.getResultSet();

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
                    cittadinanza = (String) resultSet.getObject(12);
                    Date date1 = (Date) resultSet.getObject(13);
                    date = date1.toLocalDate();
                    isBlackListed = (Boolean) resultSet.getObject(14);
                }
                resultSet.close();

            }finally{
                if(con != null){
                    ConnectionStorage.releaseConnection(con);
                }
            }

            Pattern regex = Pattern.compile("^[0-9]*$");
            System.out.println(cap);

                if(cap!=null) {
                    if (regex.matcher(cap).matches()) {
                        cliente = new Cliente(nome, cognome, cittadinanza, provincia, comune, via, civico, Integer.parseInt(cap), telefono, sesso, date, cf1, email);
                        cliente.setBlacklisted(isBlackListed);
                    } else {
                        cliente = new Cliente(nome, cognome, cittadinanza, provincia, comune, via, civico, null, telefono, sesso, date, cf1, email);
                        cliente.setBlacklisted(isBlackListed);
                    }

                }else{
                    cliente = new Cliente(nome, cognome, cittadinanza, provincia, comune, via, civico, null, telefono, sesso, date, cf1, email);
                    cliente.setBlacklisted(isBlackListed);
                }

                return cliente;
        }else{
            throw new SQLException();
        }
    }

    @Override
    public synchronized void doDelete(Cliente o) throws SQLException {

        if(o != null && o.getCf() != null){
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Cliente WHERE CF = ?")){
                preparedStatement.setString(1,o.getCf());
                if(preparedStatement.executeUpdate()==0)
                    throw new NoSuchElementException("elemento non trovato");
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NoSuchElementException("elemento non trovato");
        }
    }

    @Override
    public synchronized Collection<Cliente> doRetriveAll(String order) throws SQLException {
        con = ConnectionStorage.getConnection();
        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql =  "SELECT * FROM Cliente ORDER BY ? ";
        if(order.equalsIgnoreCase("decrescente")){
            sql += "DESC";
        }else{
            sql+= "ASC";
        }

        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
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
                String  cittadinazione = (String) resultSet.getObject(12);
                Date date1 = (Date) resultSet.getObject(13);
                LocalDate date = date1.toLocalDate();
                Boolean  isBackListed = (Boolean) resultSet.getObject(14);

                cliente = new Cliente(nome,cognome,cittadinazione,provincia,comune,via,civico,Integer.parseInt(cap),telefono,sesso,date,cf1,email);
                cliente.setBlacklisted(isBackListed);
                clientes.add(cliente);
            }
            resultSet.close();
        }finally{
            if(con != null){
                ConnectionStorage.releaseConnection(con);
            }
        }
        return clientes;
    }


    /**
     * Aggiorna i dati di un cliente esistente nel database.
     *
     * @param o Il cliente con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NoSuchElementException Se il parametro o è null o non trovato.
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
        if(o != null && o.getCf() != null){
            con = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE Cliente SET nome = ?, cognome = ?, Cap = ?, comune = ?, " +
                            "civico = ?, provincia = ?, via = ?, Email = ?, Sesso = ?, " +
                            "telefono = ?, Cittadinanza = ?, " +
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
                preparedStatement.setString(11, o.getCittadinanza());
                preparedStatement.setDate(12, Date.valueOf(o.getDataNascita()));
                preparedStatement.setBoolean(13, o.isBlacklisted());
                preparedStatement.setString(14, o.getCf());
                preparedStatement.executeUpdate();
            }
            finally
            {
                if(con != null)
                {
                    ConnectionStorage.releaseConnection(con);
                }
            }
        }
        else
        {
            throw new NoSuchElementException("elemento non trovato");
        }
    }

    @Override
    public synchronized Collection<Cliente> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        PreparedStatement preparedStatement = null;
        ArrayList<Cliente> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            con= ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM Cliente WHERE " + attribute + " = ?";
            try{
                preparedStatement = con.prepareStatement(selectSQL);
                preparedStatement.setObject(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    cliente = new Cliente();
                    cliente.setCf(resultSet.getString("CF"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setCognome(resultSet.getString("cognome"));
                    cliente.setNumeroCivico(resultSet.getInt("civico"));
                    cliente.setCAP(resultSet.getInt("Cap"));
                    cliente.setComune(resultSet.getString("Comune"));
                    cliente.setCittadinanza(resultSet.getString("Cittadinanza"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setVia(resultSet.getString("Via"));
                    cliente.setEmail(resultSet.getString("Email"));
                    cliente.setSesso(resultSet.getString("Sesso"));
                    cliente.setNumeroTelefono(resultSet.getString("telefono"));
                    cliente.setBlacklisted(resultSet.getBoolean("IsBackListed"));
                    cliente.setDataNascita(resultSet.getDate("DataDiNascita").toLocalDate());
                    lista.add(cliente);
                }

            }finally{
                if(con!= null){
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    ConnectionStorage.releaseConnection(con);
                }
            }
        }else{
            throw new RuntimeException("Attributo e/o valore non valido/i");
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessun cliente con " + attribute + " = " + value + "!");

        return lista;
    }


    // Filtro clienti
    @Override
    public List<Cliente> doFilter(String nome, String cognome, String nazionalita, LocalDate dataNascita, Boolean blackListed, String orderBy) {
        con = null;
        PreparedStatement preparedStatement = null;
        List<Cliente> lista = new ArrayList<>();
        String selectSQL = "";

        boolean[] params = new boolean[5];
        params[0] = nome != null && !nome.isEmpty();
        params[1] = cognome != null && !cognome.isEmpty();
        params[2] = nazionalita != null && !nazionalita.isEmpty();
        params[3] = dataNascita != null && dataNascita.isBefore(LocalDate.now());
        params[4] = blackListed != null;

        // Se tutti i parametri sono nulli allora lancia l'eccezione
        if (!params[0] && !params[1] && !params[2] && !params[3] && !params[4]) {
            throw new RuntimeException("Nessun parametro inserito!");
        }

        // Conta il numero di parametri che sono veri
        int count = -1;
        for (boolean b : params) {
            if(b) count++;
        }
        // Il numero di AND della query è pari a count - 1

        selectSQL += "SELECT * FROM Cliente WHERE";
        // Il cliclo serve per inserire gli AND correttamente
        for (int i = 0, j = count; i < params.length; i++) {
            if (i == 0 && params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la query di ricerca
                selectSQL += " nome = ?";
            }
            if (i == 1 && params[1]) {
                selectSQL += " cognome = ?";
            }
            if (i == 2 && params[2]) {
                selectSQL += " Cittadinanza = ?";
            }
            if (i == 3 && params[3]) {
                selectSQL += " DataDiNascita = ?";
            }
            if (i == 4 && params[4]) {
                selectSQL += " IsBlackListed = ?";
            }
            if (j != 0 && params[i]){
                selectSQL += " AND ";
                j--;
            }
        }

        if(orderBy != null && !orderBy.isEmpty()) {
            if(DaoUtils.checkWhitelist(whitelist, orderBy))
                selectSQL +=  " ORDER BY " + orderBy;
        }
        System.out.println(selectSQL);
        try{
            con = ConnectionStorage.getConnection();
            ResultSet resultSet = null;

            try{
                preparedStatement = con.prepareStatement(selectSQL);
                int counter = 1;
                if (params[0]) {
                    preparedStatement.setString(counter, nome);
                    counter++;
                }
                if (params[1]) {
                    preparedStatement.setString(counter, cognome);
                    counter++;
                }
                if (params[2]) {
                    preparedStatement.setString(counter, nazionalita);
                    counter++;
                }
                if (params[3]) {
                    preparedStatement.setDate(counter, Date.valueOf(dataNascita));
                    counter++;
                }
                if (params[4]) {
                    preparedStatement.setBoolean(counter, blackListed);
                }
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
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
                    cliente.setCittadinanza(resultSet.getString("Cittadinanza"));

                    lista.add(cliente);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null)
                    preparedStatement.close();
                ConnectionStorage.releaseConnection(con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}