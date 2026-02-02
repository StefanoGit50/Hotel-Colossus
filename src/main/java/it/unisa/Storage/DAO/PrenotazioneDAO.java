package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PrenotazioneDAO implements FrontDeskStorage<Prenotazione> {
    private static final String [] whitelist = {
        "IDPrenotazione",
        "DataPrenotazione",
        "DataArrivoCliente",
        "DataPartenzaCliente",
        "NomeTrattamento",
        "NoteAggiuntive",
        "Intestatario",
        "dataScadenza",
        "numeroDocumento",
        "DataRilascio",
        "TipoDocumento",
        "Stato"
    };

    private static final String [] draList = {
            "IDPrenotazione"
    };


    private static final String VIEW_TABLE_NAME = "prenotazioneIS";


    private ClienteDAO clienteDAO;

    private synchronized void createView() throws SQLException {
        String createView =
                "CREATE or REPLACE VIEW prenotazioneIS AS ( "
                + " SELECT * "
                + " FROM Prenotazione p "
                + "  JOIN Associato_a a using(IDPrenotazione) "
                + "  JOIN Ha s Using(IDPrenotazione) ); ";

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(createView);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
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


    public PrenotazioneDAO(){
        this.clienteDAO =  new ClienteDAO();
    }

    public PrenotazioneDAO(ClienteDAO clienteDAO){this.clienteDAO = clienteDAO;
    }

    @Override
    public synchronized void doSave(Prenotazione p) throws SQLException {
        if(p != null && p.getTrattamento() != null){
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataCreazionePrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, NomeIntestatario, DataScadenzaDocumento, numeroDocumento, DataRilascioDocumento, TipoDocumento, Stato, CheckIn) " +
                                                                                  "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?)");

            preparedStatement.setDate(1,Date.valueOf(p.getDataCreazionePrenotazione()));
            preparedStatement.setDate(2,Date.valueOf(p.getDataInizio()));
            preparedStatement.setDate(3,Date.valueOf(p.getDataFine()));
            preparedStatement.setString(4, p.getTrattamento().getNome());
            preparedStatement.setString(5,p.getNoteAggiuntive());
            preparedStatement.setString(6 , p.getIntestatario());
            preparedStatement.setDate(7, Date.valueOf(p.getDataScadenza()));
            preparedStatement.setString(8,p.getNumeroDocumento());
            preparedStatement.setDate(9,Date.valueOf(p.getDataRilascio()));
            preparedStatement.setString(10,p.getTipoDocumento());
            preparedStatement.setBoolean(11,p.getStatoPrenotazione());
            preparedStatement.setBoolean(12,p.isCheckIn());
            preparedStatement.executeUpdate();
            // Salva i servizi associati

            String query = "Insert into ha values(?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query);

            for(Servizio servizio: p.getListaServizi()){
                preparedStatement1.setString(2,servizio.getNome());
                preparedStatement1.setInt(1,p.getIDPrenotazione());
                preparedStatement1.setDouble(3,servizio.getPrezzo());
                preparedStatement1.executeUpdate();
            }

            // Salva le associazioni clienti-camere
            for(Cliente cliente : p.getListaClienti()){
                clienteDAO.doSave(cliente);
            }

            for (Camera camera : p.getListaCamere()){
                for (Cliente cliente : p.getListaClienti()){
                    System.out.println("ciao");
                    String query1 = "INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                            "VALUES (?, ?, ?, ?)";
                    System.out.println(cliente);
                    try (PreparedStatement stmt = connection.prepareStatement(query1)) {
                        stmt.setString(1, cliente.getCf());
                        stmt.setInt(2, camera.getNumeroCamera());
                        stmt.setInt(3, p.getIDPrenotazione());
                        stmt.setDouble(4, camera.getPrezzoCamera());
                        stmt.executeUpdate();
                    }
                }
            }

            preparedStatement.close();
            connection.close();
        }else{
            if(p != null && p.getTrattamento() == null){
                Connection connection = ConnectionStorage.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataCreazionePrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, NomeIntestatario, DataScadenzaDocumento, numeroDocumento, DataRilascioDocumento, TipoDocumento, Stato, CheckIn) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?)");

                preparedStatement.setDate(1,Date.valueOf(p.getDataCreazionePrenotazione()));
                preparedStatement.setDate(2,Date.valueOf(p.getDataInizio()));
                preparedStatement.setDate(3,Date.valueOf(p.getDataFine()));
                preparedStatement.setString(4, null);
                preparedStatement.setString(5,p.getNoteAggiuntive());
                preparedStatement.setString(6 , p.getIntestatario());
                preparedStatement.setDate(7, Date.valueOf(p.getDataScadenza()));
                preparedStatement.setString(8,p.getNumeroDocumento());
                preparedStatement.setDate(9,Date.valueOf(p.getDataRilascio()));
                preparedStatement.setString(10,p.getTipoDocumento());
                preparedStatement.setBoolean(11,p.getStatoPrenotazione());
                preparedStatement.setBoolean(12,p.isCheckIn());
                preparedStatement.executeUpdate();

                String query = "Insert into ha values(?,?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query);

                for(Servizio servizio: p.getListaServizi()){
                    preparedStatement1.setString(2,servizio.getNome());
                    preparedStatement1.setInt(1,p.getIDPrenotazione());
                    preparedStatement1.setDouble(3,servizio.getPrezzo());
                    preparedStatement1.executeUpdate();
                }

                // Salva le associazioni clienti-camere
                for(Cliente cliente : p.getListaClienti()){
                    clienteDAO.doSave(cliente);
                }

                for (Camera camera : p.getListaCamere()){
                    for (Cliente cliente : p.getListaClienti()) {
                        String query1 = "INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                                "VALUES (?, ?, ?, ?)";
                        System.out.println(cliente);
                        try (PreparedStatement stmt = connection.prepareStatement(query1)) {
                            stmt.setString(1, cliente.getCf());
                            stmt.setInt(2, camera.getNumeroCamera());
                            stmt.setInt(3, p.getIDPrenotazione());
                            stmt.setDouble(4, camera.getPrezzoCamera());
                            stmt.executeUpdate();
                        }
                    }
                }

                preparedStatement.close();
                connection.close();
            }else{
                throw new SQLException();
            }
        }

    }

    /**
     * @param list
     * @throws SQLException
     */

    public void doSaveAll(List<Prenotazione> list) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void doDelete(Prenotazione p) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, p.getIDPrenotazione());
            preparedStatement.executeUpdate();
        }
        connection.close();
    }

    /**
     * @param codicePrenotazione {@code Integer} codice della prenotazione.
     * @return oggetto {@code Prenotazione} con codicePrenotazione corrispondente al parametro esplicito,
     * {@code null} altrimenti.
     * @throws SQLException .
     */
    @Override
    public synchronized Prenotazione doRetriveByKey(Object codicePrenotazione) throws SQLException {
        if (!(codicePrenotazione instanceof Integer)) {
            throw new SQLException("ERRORE: chiave non valida");
        }

        createView();

        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        Prenotazione p = null;
        int key = (int) codicePrenotazione;
        ArrayList<Camera> camere = new ArrayList<>();
        ArrayList<Servizio> servizi = new ArrayList<>();
        ArrayList<Cliente> clienti = new ArrayList<>();
        Trattamento trattamento = new Trattamento();

        String[] sql = {
                "SELECT NumeroCamera, PrezzoAcquisto FROM " + VIEW_TABLE_NAME + " WHERE IDPrenotazione = ?",
                "SELECT NomeTrattamento, PrezzoAcquistoTrattamento FROM " + VIEW_TABLE_NAME + " WHERE IDPrenotazione = ?",
                "SELECT NomeServizio, PrezzoAcquistoServizio FROM " + VIEW_TABLE_NAME + " WHERE IDPrenotazione = ?",
                "SELECT CF  FROM " + VIEW_TABLE_NAME + " WHERE IDPrenotazione = ?"
        };

        String selectSql = "SELECT * FROM " + VIEW_TABLE_NAME + " WHERE IDPrenotazione = ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql[0]);
            ps.setInt(1, key);
            rs = ps.executeQuery();
            CameraDAO dao = new CameraDAO();

            if (rs.next()) {
                try {
                    Camera camera = null;
                    do {
                        camera = new Camera();
                        camera = dao.doRetriveByKey(rs.getInt("NumeroCamera"));
                        camera.setPrezzoCamera(rs.getDouble("PrezzoAcquisto"));
                        camere.add(camera);
                    } while (rs.next());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                throw new SQLException("ERRORE: prenotazione ID="+key+"non contiene camere");
            }

            ps = conn.prepareStatement(sql[1]);
            ps.setInt(1, key);
            rs = ps.executeQuery();
            TrattamentoDAO dao2 = new TrattamentoDAO();

            if (rs.next()) {
                trattamento = dao2.doRetriveByKey(rs.getString("NomeTrattamento"));
                trattamento.setPrezzo(rs.getDouble("PrezzoAcquistoTrattamento"));
            } else {
                throw new SQLException("ERRORE: prenotazione ID="+key+"non contiene nessun trattamento");
            }

            ps = conn.prepareStatement(sql[2]);
            ps.setInt(1, key);
            rs = ps.executeQuery();
            ServizioDAO dao3 = new ServizioDAO();

            if (rs.next()) {
                try {
                    Servizio servizio = null;
                    do {
                        servizio = new Servizio();
                        servizio = dao3.doRetriveByKey(rs.getString("NomeServizio"));
                        servizio.setPrezzo(rs.getDouble("PrezzoAcquistoServizio"));
                    } while (rs.next());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            ps = conn.prepareStatement(sql[3]);
            ps.setInt(1, key);
            rs = ps.executeQuery();
            ClienteDAO dao4 = new ClienteDAO();

            if (rs.next()) {
                try {
                    do {
                        clienti.add(dao4.doRetriveByKey(rs.getString("CF")));
                    } while (rs.next());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            } else {
                throw new SQLException("ERRORE: prenotazione ID="+key+"non contiene clienti");
            }

            ps = conn.prepareStatement(selectSql);
            ps.setInt(1, key);
            rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    p = new Prenotazione();
                    p.setIDPrenotazione(key);
                    p.setTrattamento(trattamento);
                    p.setListaServizi(servizi);
                    p.setListaClienti(clienti);
                    p.setListaCamere(camere);
                    p.setIntestatario(rs.getString("NomeIntestatario"));
                    p.setDataCreazionePrenotazione(rs.getDate("DataCreazionePrenotazione").toLocalDate());
                    p.setDataFine(rs.getDate("DataPartenzaCliente").toLocalDate());
                    p.setDataInizio(rs.getDate("DataArrivoCliente").toLocalDate());
                    p.setDataRilascio(rs.getDate("DataRilascioDocumento").toLocalDate());
                    p.setDataScadenza(rs.getDate("DataScadenzaDocumento").toLocalDate());
                    p.setStatoPrenotazione(rs.getBoolean("Stato"));
                    p.setCheckIn(rs.getBoolean("CheckIn"));
                    p.setNoteAggiuntive(rs.getString("NoteAggiuntive"));
                    p.setNumeroDocumento(rs.getString("NumeroDocumento"));
                    p.setMetodoPagamento(rs.getString("MetodoPagamento"));
                    p.setTipoDocumento(rs.getString("TipoDocumento"));
                } else {
                    p = null;
                }
            } catch (SQLException ex) {
                throw new SQLException(ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (conn != null)
                    conn.close();
            }
        }

        return p;
    }

    /**
     * @param order unici valori ammissibili {"IDPrenotazione ASC", "IDPrenotazione DESC"} case INsenitive.
     * @return {@code List<Prenotazione>} tutte le prenotazioni presenti nel sistema, {@code null} in caso di errore.
     * @throws SQLException .
     */
    @Override
    public synchronized Collection<Prenotazione> doRetriveAll(String order) throws SQLException {

        createView();
        String query = "SELECT DISTINCT IDPrenotazione FROM " + VIEW_TABLE_NAME ;
        if (order != null && !order.trim().isEmpty() && DaoUtils.checkWhitelist(draList, order)) {
            query += " ORDER BY " + order;
        }

        Connection connection = null;
        PreparedStatement ps = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        try {
            connection = ConnectionStorage.getConnection();
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                try {
                    do {
                        prenotazioni.add(doRetriveByKey(rs.getInt("IDPrenotazione")));
                    } while (rs.next());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return prenotazioni;
    }


    /**
     * Aggiorna i dati di una prenotazione esistente nel database e aggiorna i collegamenti
     * al trattamento e ai servizi associati.
     *
     * @param p La prenotazione con i dati aggiornati da persistere.
     * @throws SQLException         Se si verifica un errore durante l'accesso al database.
     * @throws NullPointerException Se il parametro p è null.
     *                              Precondizioni:
     *                              p != null
     *                              p.getIDPrenotazione() deve corrispondere a una prenotazione esistente nel database
     *                              Tutte le date (DataCreazionePrenotazione, DataInizio, DataFine, DataRilascio, DataScadenza)
     *                              devono essere valorizzate e valide
     *                              Se p.getTrattamento() != null, il trattamento deve esistere nel database
     *                              Tutti i servizi in p.getListaServizi() devono esistere nel database
     *                              p.getNumeroDocumento() deve essere maggiore di 0
     *                              <p>
     *                              Postcondizioni:
     *                              Il record della prenotazione nel database viene aggiornato con i nuovi valori
     *                              Il IDPrenotazione (chiave primaria) rimane invariato
     *                              Se presente, il trattamento viene collegato alla prenotazione
     *                              Tutti i servizi nella lista vengono collegati alla prenotazione
     *                              Le associazioni clienti-camere nella tabella Associato_a NON vengono modificate
     */
    @Override
    public synchronized void doUpdate(Prenotazione p) throws SQLException {
        if (p != null) {
            Connection connection = ConnectionStorage.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE prenotazione SET DataPrenotazione = ? , DataArrivoCliente = ?, DataPartenzaCliente = ? , " +
                            "NoteAggiuntive = ? ,Intestatario = ? , dataScadenza = ? , numeroDocumento = ? , DataRilascio = ? , TipoDocumento = ?," +
                            "Stato = ? , CheckIn = ? Where IDPrenotazione = ?")) {

                preparedStatement.setDate(1, Date.valueOf(p.getDataCreazionePrenotazione()));
                preparedStatement.setDate(2, Date.valueOf(p.getDataInizio()));
                preparedStatement.setDate(3, Date.valueOf(p.getDataFine()));
                preparedStatement.setString(4, p.getNoteAggiuntive());
                preparedStatement.setString(5, p.getIntestatario());
                preparedStatement.setDate(6, Date.valueOf(p.getDataScadenza()));
                preparedStatement.setString(7, p.getNumeroDocumento());
                preparedStatement.setDate(8, Date.valueOf(p.getDataRilascio()));
                preparedStatement.setString(9, p.getTipoDocumento());
                preparedStatement.setBoolean(10,p.getStatoPrenotazione());
                preparedStatement.setBoolean(11,p.isCheckIn());
                preparedStatement.setInt(12, p.getIDPrenotazione());
                preparedStatement.executeUpdate();

                // Aggiorna il trattamento associato
                if (p.getTrattamento() != null) {
                    String query = "";

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, p.getIDPrenotazione());
                        stmt.setString(2, p.getTrattamento().getNome());
                        stmt.executeUpdate();
                    }
                }

                // Aggiorna i servizi associati
                for (Servizio servizio : p.getListaServizi()) {
                    String query = "";

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, p.getIDPrenotazione());
                        stmt.setString(2, servizio.getNome());
                        stmt.executeUpdate();
                    }
                }
            } finally {
                if (connection != null) {
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Non è supportata una implementazione
     *
     */
    @Override
    public Collection<Prenotazione> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blackListed, String orderBy) throws SQLException {
        return List.of();
    }

    /**
     * @param attribute;
     * @param value;
     * @return Collection<Prenotazione>;
     * @throws SQLException;
     */
    @Override
    public Collection<Prenotazione> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM prenotazione WHERE " + attribute + " = ?")) {
            preparedStatement.setObject(1, value);
            Collection<Prenotazione> prenotaziones = new ArrayList<>();
            try (ResultSet rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    int idPrenotazione = rs.getInt("IDPrenotazione");

                    // Recupera il trattamento
                    Trattamento trattamento = null;
                    String query = "";

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs1 = stmt.executeQuery()) {
                            if (rs1.next()) {
                                trattamento = new Trattamento(
                                        rs1.getString("Nome"),
                                        rs1.getDouble("Prezzo")
                                );
                            }
                        }
                    }

                    // Recupera i servizi
                    String query2 = "";
                    Collection<Servizio> servizi = new ArrayList<>();

                    try (PreparedStatement stmt = connection.prepareStatement(query2)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs2 = stmt.executeQuery()) {
                            while (rs2.next()) {
                                servizi.add(new Servizio(
                                        rs2.getString("Nome"),
                                        rs2.getDouble("Prezzo")
                                ));
                            }
                        }
                    }

                    // Recupera le camere
                    String query3 = "SELECT DISTINCT c.* FROM camera c " +
                            "JOIN associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                            "WHERE a.IDPrenotazione = ?";

                    Collection<Camera> camere = new ArrayList<>();

                    try (PreparedStatement stmt = connection.prepareStatement(query3)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs3 = stmt.executeQuery()) {
                            while (rs3.next()) {
                                camere.add(new Camera(
                                        rs3.getInt("NumeroCamera"),
                                        Stato.valueOf(rs3.getString("Stato")),
                                        rs3.getInt("NumeroMaxOcc"),
                                        rs3.getDouble("Prezzo"),
                                        rs3.getString("NoteCamera"),
                                        rs3.getString("NomeCamera")
                                ));
                            }
                        }
                    }

                    // Recupera i clienti
                    String query4 = "SELECT DISTINCT cl.* FROM cliente cl " +
                            "JOIN associato_a a ON cl.CF = a.CF " +
                            "WHERE a.IDPrenotazione = ?";

                    Collection<Cliente> clienti = new ArrayList<>();
                    String query5 = "SELECT camera.* FROM (associato_a join camera c on associato_a.NumeroCamera = c.NumeroCamera" +
                            ")where CF = ?";
                    PreparedStatement preparedStatement1;
                    Camera camera = new Camera();
                    try (PreparedStatement stmt = connection.prepareStatement(query4)) {
                        stmt.setInt(1, idPrenotazione);

                        preparedStatement1 = connection.prepareStatement(query5);

                        try (ResultSet rs4 = stmt.executeQuery()){
                            preparedStatement1.setString(1,rs4.getString("CF"));

                            try(ResultSet resultSet = preparedStatement1.executeQuery()){
                                camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
                                camera.setNoteCamera(resultSet.getString("NoteCamera"));
                                camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
                                camera.setPrezzoCamera(resultSet.getDouble("Prezzo"));
                                camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
                            }


                            while (rs4.next()) {
                                clienti.add(new Cliente(
                                        rs4.getString("nome"),
                                        rs4.getString("cognome"),
                                        rs4.getString("provincia"),
                                        rs4.getString("comune"),
                                        rs4.getString("via"),
                                        rs4.getInt("civico"),
                                        rs4.getInt("Cap"),
                                        rs4.getString("telefono"),
                                        rs4.getString("Sesso"),
                                        rs4.getDate("DataDiNascita") != null ? rs4.getDate("DataDiNascita").toLocalDate() : null,
                                        rs4.getString("CF"),
                                        rs4.getString("Email"),
                                        rs4.getString("Nazionalità"),
                                        camera
                                ));
                            }
                        }
                    }

                    prenotaziones.add(new Prenotazione(
                                idPrenotazione,
                                rs.getDate("DataPrenotazione").toLocalDate(),
                                rs.getDate("DataArrivoCliente").toLocalDate(),
                                rs.getDate("DataPartenzaCliente").toLocalDate(),
                                trattamento,
                                rs.getString("TipoDocumento"),
                                rs.getDate("DataRilascio").toLocalDate(),
                                rs.getDate("DataScadenza").toLocalDate(),
                                rs.getString("Intestatario"),
                                rs.getString("NoteAggiuntive"),
                            (ArrayList<Camera>) camere,
                            (ArrayList<Servizio>) servizi,
                            (ArrayList<Cliente>) clienti,
                                rs.getString("NumeroDocumento")
                    ));
                }

            }
            return prenotaziones;
        }

    }


    /**
     * Metodo per recuperare tramite filtro delle prenotazioni.
     * @param nome  nome del cliente intestatario.
     * @param cognome  cognome del cliente intestatario.
     * @param dataInizio data di inizio del soggiorno del cliente.
     * @param dataFine data di fine del soggiorno del cliente.
     * @param elementOrder ordine dei risultati per data.
     * @return lista di elementi che rispettano di parametri
     */
    public ArrayList<Prenotazione> doFilter(String nome, String cognome, LocalDate dataInizio, LocalDate dataFine, String elementOrder)
    {
            try{
                        Connection connection = ConnectionStorage.getConnection();
                        boolean [] booleans = new boolean[4];
                        PreparedStatement preparedStatement = connection.prepareStatement("CREATE or replace view PrenotaIS as\n" +
                                "SELECT  p.*, t.Nome as TrattamentoNome, t.Prezzo as TrattamentoPrezzo,\n" +
                                "        c.CF, c.nome as ClienteNome, c.cognome as ClienteCognome,   c.Email, c.telefono, c.Sesso, c.DataDiNascita, c.Cittadinanza,\n" +
                                "        c.via, c.civico, c.comune, c.provincia, c.Cap, c.IsBackListed,\n" +
                                "        cam.NumeroCamera, cam.NumeroMaxOcc, cam.NoteCamera, cam.Stato as CameraStato, cam.Prezzo\n" +
                                "            as CameraPrezzo, as ServizioNome, s.Prezzo as ServizioPrezzo\n" +
                                "From  prenotazione p\n" +
                                "          join trattamento t ON p.NomeTrattamento = t.Nome\n" +
                                "          join associato_a a ON p.IDPrenotazione = a.IDPrenotazione\n" +
                                "          JOIN cliente c ON a.CF = c.CF\n" +
                                "          JOIN camera cam ON a.NumeroCamera = cam.NumeroCamera\n");

                        preparedStatement.executeUpdate();
                        String sqlBello = "SELECT * FROM prenotais WHERE ";
                        booleans[0] = nome != null && !nome.isEmpty();
                        booleans[1] = cognome != null && !cognome.isEmpty();
                        booleans[2] = dataInizio != null;
                        booleans[3] = dataFine != null;

                        int count = -1;
                        for(int i = 0;i < booleans.length; i++){
                            if(booleans[i]){
                                count++;
                            }
                        }

                        for(int i = 0;i < booleans.length; i++){
                            boolean f = false;
                            if(i == 0 && booleans[i]){
                                sqlBello += " ClienteNome = ?";
                                f = true;
                            }
                            if(i == 1 && booleans[i]){
                                sqlBello += " ClienteCognome = ? ";
                                f = true;
                            }
                            if(i == 2 && booleans[i]){
                                sqlBello += " DataArrivoCliente >= ? ";
                                f = true;
                            }
                            if(i == 3 && booleans[i]) {
                                sqlBello += " DataPartenzaCliente <= ? ";
                                f = true;
                            }
                            if(f && count != 0){
                                sqlBello += " AND ";
                                count--;
                            }
                        }
                        if(elementOrder != null && !elementOrder.isBlank()){
                            sqlBello += " ORDER BY DataArrivoCliente " + elementOrder;
                        }
                        PreparedStatement preparedStatement1 = connection.prepareStatement(sqlBello);
                        int counter = 1;
                        if(booleans[0]){
                            preparedStatement1.setString(counter,nome);
                            counter++;
                        }
                        if(booleans[1]){
                            preparedStatement1.setString(counter,cognome);
                            counter++;
                        }
                        if(booleans[2]){
                            preparedStatement1.setDate(counter,Date.valueOf(dataInizio));
                            counter++;
                        }
                        if(booleans[3]){
                            preparedStatement1.setDate(counter,Date.valueOf(dataFine));
                            counter++;
                        }
                        if(elementOrder != null){
                            preparedStatement1.setString(counter,elementOrder);
                        }

                        ResultSet resultSet = preparedStatement1.executeQuery();
                Map<Integer, PrenotazioneBuilder> prenotazioneBuilderMap = new LinkedHashMap<>();

                while(resultSet.next()){
                    Integer integer =  resultSet.getInt("IDPrenotazione");
                    PrenotazioneBuilder builder = prenotazioneBuilderMap.get(integer);
                    if(builder == null){
                        builder = new PrenotazioneBuilder(resultSet);
                        prenotazioneBuilderMap.put(integer,builder);
                    }
                    builder.addCliente(resultSet);
                    builder.addCamera(resultSet);
                    builder.addServizio(resultSet);
                }

                ArrayList<Prenotazione> prenotaziones = new ArrayList<>(prenotazioneBuilderMap.size());
                for(PrenotazioneBuilder prenotazione: prenotazioneBuilderMap.values()){
                    prenotaziones.add(prenotazione.build());
                }
                return prenotaziones;
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        throw new NoSuchElementException("NON ci sono gli elementi");
    }
}
