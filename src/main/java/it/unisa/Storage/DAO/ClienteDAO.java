package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class ClienteDAO implements FrontDeskStorage<Cliente> {

    private static final Logger log = LogManager.getLogger(ClienteDAO.class);
    private Connection con;
    private Cliente cliente=null;
    private  ResultSet resultSet;
    private static final String TABLE_NAME = "cliente";

    private PreparedStatement pst;

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
     *
     * o non null PRECONDIZIONE
     *
     * @param o;
     * @throws SQLException;
     */
    public synchronized void doSave(Cliente o) throws SQLException{
        String insertSQL= "INSERT INTO " +  TABLE_NAME +" (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, Nazionalita,DataDiNascita, IsBackListed)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
                con = ConnectionStorage.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
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
                preparedStatement.setString(12,o.getNazionalita());
                preparedStatement.setDate(13,Date.valueOf(o.getDataNascita()));
                preparedStatement.setBoolean(14,o.isBlacklisted());
                preparedStatement.executeUpdate();
            }finally{
                if(con != null){
                    ConnectionStorage.releaseConnection(con);
                }
            }
    }

    /**
     * @param list
     * @throws SQLException
     */
    @Override
    public void doSaveAll(List<Cliente> list) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param oggetto è un oggetto (rappresenta la chiave del );
     * @return Cliente;
     * @throws SQLException;
     */

    @Override
    public synchronized Cliente doRetriveByKey(Object oggetto) throws SQLException{
        if (oggetto instanceof String){
            String cf = (String) oggetto;
            con = ConnectionStorage.getConnection();
            String sql = "Select * From (associato_a join camera on associato_a.NumeroCamera = camera.NumeroCamera)" +
                    "where CF = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql);
            Camera camera = new Camera();
            Cliente cliente = new Cliente();
            try(PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM cliente WHERE CF = ?")){
                preparedStatement.setString(1,cf);
                resultSet = preparedStatement.executeQuery();
                preparedStatement1.setString(1,cf);

                if(resultSet.next()){
                    cliente.setCf(resultSet.getString("CF"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setCognome(resultSet.getString("cognome"));
                    cliente.setCAP( resultSet.getInt("Cap"));
                    cliente.setComune(resultSet.getString("comune"));
                    cliente.setNumeroCivico(resultSet.getInt("civico"));
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setVia(resultSet.getString("via"));
                    cliente.setEmail(resultSet.getString("email"));
                    cliente.setSesso(resultSet.getString("sesso"));
                    cliente.setNumeroTelefono(resultSet.getString("telefono"));
                    cliente.setNazionalita(resultSet.getString("nazionalita"));
                    cliente.setDataNascita(resultSet.getDate("DatadiNascita").toLocalDate());
                    cliente.setBlacklisted(resultSet.getBoolean("IsBackListed"));
                    try(ResultSet resultSet1 = preparedStatement1.executeQuery()){
                        if(resultSet1.next()){
                            camera.setNomeCamera(resultSet1.getString("NomeCamera"));
                            camera.setNumeroCamera(resultSet1.getInt("NumeroCamera"));
                            camera.setCapacità(resultSet1.getInt("NumeroMaxOcc"));
                            camera.setPrezzoCamera(resultSet1.getDouble("PrezzoAcquisto"));
                            camera.setStatoCamera(Stato.valueOf(resultSet1.getString("Stato")));
                            camera.setNoteCamera(resultSet1.getString("NoteCamera"));
                        }
                    }
                }
                cliente.setCamera(camera);
                resultSet.close();

            }finally{
                if(con != null){
                    ConnectionStorage.releaseConnection(con);
                }
            }
                return cliente;
        }else{
            throw new SQLException();
        }
    }

    @Override
    public synchronized void doDelete(Cliente o) throws SQLException {

        if(o != null && o.getCf() != null){
            con = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM cliente WHERE CF = ?")){
                preparedStatement.setString(1,o.getCf());

                if(preparedStatement.executeUpdate()==0)
                    throw new NoSuchElementException("elemento non trovato");
            }finally{
                if(con != null){
                    ConnectionStorage.releaseConnection(con);
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

        String sql =  "SELECT * FROM cliente ORDER BY ? ";
        String sql1 = "Select * From (associato_a join camera on associato_a.NumeroCamera = camera.NumeroCamera)" +
                "where CF = ?";
        if(order != null){
            if(order.equalsIgnoreCase("decrescente")){
                sql += "DESC";
            }else{

                sql+= "ASC";
            }
        }
        PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
        Camera camera = new Camera();
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1,"CF");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String cf1 = (String) resultSet.getObject("CF");
                String nome = (String) resultSet.getObject("Nome");
                String cognome = (String) resultSet.getObject("Cognome");
                String  cap = (String) resultSet.getObject("Cap");
                String comune = (String) resultSet.getObject("comune");
                Integer civico = (Integer) resultSet.getObject("civico");
                String provincia = (String) resultSet.getObject("provincia");
                String via = (String) resultSet.getObject("via");
                String email = (String) resultSet.getObject("email");
                String  sesso = (String) resultSet.getObject("Sesso");
                String  telefono = (String) resultSet.getObject("telefono");
                Date date1 = (Date) resultSet.getObject("DataDiNascita");
                LocalDate date = date1.toLocalDate();
                Boolean  isBackListed = (Boolean) resultSet.getObject("IsBackListed");
                String nazionalità = resultSet.getString("Nazionalita");
                preparedStatement1.setString(1,resultSet.getString("CF"));
                try(ResultSet resultSet1 = preparedStatement1.executeQuery()){
                        if(resultSet1.next()){
                            camera.setNomeCamera(resultSet1.getString("NomeCamera"));
                            camera.setNumeroCamera(resultSet1.getInt("NumeroCamera"));
                            camera.setNoteCamera(resultSet1.getString("NoteCamera"));
                            camera.setStatoCamera(Stato.valueOf(resultSet1.getString("Stato")));
                            camera.setCapacità(resultSet1.getInt("NumeroMaxOcc"));
                            camera.setPrezzoCamera(resultSet1.getDouble("PrezzoAcquisto"));
                        }
                }
                cliente = new Cliente(nome,cognome,provincia,comune,via,civico,Integer.parseInt(cap),telefono,sesso,date,cf1,email,nazionalità,camera);
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

        String query = "UPDATE cliente LEFT JOIN associato_a USING (CF) SET " +
            "nome = ?, cognome = ?, Cap = ?, comune = ?, " +
            "civico = ?, provincia = ?, via = ?, Email = ?, Sesso = ?, " +
            "telefono = ?, Nazionalita = ?, " +
            "DataDiNascita = ?, IsBackListed = ?, " +
            "NumeroCamera = ?, NumeroCameraStorico = ?, PrezzoAcquisto = ? " +
            "WHERE cliente.CF = ?";
        if(o != null && o.getCf() != null){
            con = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){

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
                preparedStatement.setString(11, o.getNazionalita());
                preparedStatement.setDate(12, Date.valueOf(o.getDataNascita()));
                preparedStatement.setBoolean(13, o.isBlacklisted());
                preparedStatement.setInt(14,o.getCamera().getNumeroCamera());
                preparedStatement.setInt(15,o.getCamera().getNumeroCamera());
                preparedStatement.setDouble(16,o.getCamera().getPrezzoCamera());
                preparedStatement.setString(17, o.getCf());
                int rowsAffected = preparedStatement.executeUpdate();
                log.debug(rowsAffected+" rows affected");
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
            selectSQL = "SELECT * FROM cliente WHERE " + attribute + " = ?";
            Camera camera = new Camera();
            PreparedStatement preparedStatement1 = con.prepareStatement("Select * From (associato_a join camera on associato_a.NumeroCamera = camera.NumeroCamera)" +
                    "where CF = ?");
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
                    cliente.setProvincia(resultSet.getString("provincia"));
                    cliente.setVia(resultSet.getString("Via"));
                    cliente.setEmail(resultSet.getString("Email"));
                    cliente.setSesso(resultSet.getString("Sesso"));
                    cliente.setNumeroTelefono(resultSet.getString("telefono"));
                    cliente.setBlacklisted(resultSet.getBoolean("IsBackListed"));
                    cliente.setDataNascita(resultSet.getDate("DataDiNascita").toLocalDate());
                    cliente.setNazionalita(resultSet.getString("Nazionalita"));

                    preparedStatement1.setString(1,resultSet.getString("CF"));

                    try(ResultSet resultSet1 = preparedStatement1.executeQuery()){
                        if(resultSet1.next()){
                            camera.setNomeCamera(resultSet1.getString("NomeCamera"));
                            camera.setNumeroCamera(resultSet1.getInt("NumeroCamera"));
                            camera.setNoteCamera(resultSet1.getString("NoteCamera"));
                            camera.setPrezzoCamera(resultSet1.getDouble("PrezzoAcquisto"));
                            camera.setCapacità(resultSet1.getInt("NumeroMaxOcc"));
                            camera.setStatoCamera(Stato.valueOf(resultSet1.getString("Stato")));
                        }
                    }
                    cliente.setCamera(camera);

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

}