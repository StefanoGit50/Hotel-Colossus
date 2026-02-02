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
    private ClienteDAO clienteDAO;

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

    @Override
    public synchronized Prenotazione doRetriveByKey(Object codicePrenotazione) throws SQLException {
        if (!(codicePrenotazione instanceof Integer)) {
            return null;
        }

        Connection connection = ConnectionStorage.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, (Integer) codicePrenotazione);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()){
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

                    try (PreparedStatement stmt = connection.prepareStatement(query2)){
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
                            while (rs3.next()){
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

                    String query5 = "Select camera.* FROM (associato_a join " +
                            "camera on associato_a.NumeroCamera = camera.NumeroCamera) where CF = ?";
                    PreparedStatement p;
                    Collection<Cliente> clienti = new ArrayList<>();
                    Camera camera = new Camera();
                    try (PreparedStatement stmt = connection.prepareStatement(query4)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs4 = stmt.executeQuery()){

                            while (rs4.next()) {
                            p = connection.prepareStatement(query5);
                            p.setString(1,rs4.getString("CF"));

                            try(ResultSet resultSet = p.executeQuery()){
                                resultSet.next();
                                camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
                                camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
                                camera.setNoteCamera(resultSet.getString("NoteCamera"));
                                camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
                                camera.setPrezzoCamera(resultSet.getDouble("Prezzo"));
                            }

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
                                        rs4.getString("Nazionalita"),
                                        camera
                                ));
                            }
                        }
                    }

                    return new Prenotazione(
                            idPrenotazione,
                            rs.getDate("DataPrenotazione").toLocalDate(),
                            rs.getDate("DataArrivoCliente").toLocalDate(),
                            rs.getDate("DataPartenzaCliente").toLocalDate(),
                            trattamento,
                            rs.getString("TipoDocumento"),
                            rs.getDate("DataRilascio").toLocalDate(),
                            rs.getDate("dataScadenza").toLocalDate(),
                            rs.getString("Intestatario"),
                            rs.getString("NoteAggiuntive"),
                            (ArrayList<Camera>) camere,
                            (ArrayList<Servizio>) servizi,
                            (ArrayList<Cliente>) clienti,
                            rs.getString("numeroDocumento")
                    );
                }
            }
        }
        throw new NoSuchElementException("prenotazione non trovata");
    }

    @Override
    public synchronized Collection<Prenotazione> doRetriveAll(String order) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();

        String query = "SELECT * FROM prenotazione";
        if (order != null && !order.trim().isEmpty()) {
            query += " ORDER BY " + order;
        }

        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                int idPrenotazione = rs.getInt("IDPrenotazione");
                // Recupera il trattamento
                Trattamento trattamento = null;
                String query1 = "";

                try(PreparedStatement stmt2 = connection.prepareStatement(query1)) {
                    stmt2.setInt(1, idPrenotazione);

                    try (ResultSet rs1 = stmt2.executeQuery()) {
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

                try (PreparedStatement stmt3 = connection.prepareStatement(query2)) {
                    stmt3.setInt(1, idPrenotazione);

                    try (ResultSet rs2 = stmt3.executeQuery()) {
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

                try (PreparedStatement stmt4 = connection.prepareStatement(query3)) {
                    stmt4.setInt(1, idPrenotazione);

                    try (ResultSet rs3 = stmt4.executeQuery()) {
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

                String query5 = "SELECT camera.* FROM (associato_a join " +
                        "camera on associato_a.NumeroCamera = camera.NumeroCamera) " +
                        "where CF = ?";

                Collection<Cliente> clienti = new ArrayList<>();
                PreparedStatement p1;
                Camera camera = new Camera();
                try (PreparedStatement stmt5 = connection.prepareStatement(query4)) {
                    stmt5.setInt(1, idPrenotazione);

                    try (ResultSet rs4 = stmt5.executeQuery()) {
                        p1 = connection.prepareStatement(query5);


                        while (rs4.next()) {
                            p1.setString(1,rs4.getString("CF"));

                            try(ResultSet rs5 = p1.executeQuery()){
                                while (rs5.next()) {
                                    camera.setNumeroCamera(rs5.getInt("NumeroCamera"));
                                    camera.setCapacità(rs5.getInt("NumeroMaxOcc"));
                                    camera.setNoteCamera(rs5.getString("NoteCamera"));
                                    camera.setStatoCamera(Stato.valueOf(rs5.getString("Stato")));
                                    camera.setPrezzoCamera(rs5.getDouble("Prezzo"));
                                }
                            }

                            clienti.add(new Cliente(
                                    rs4.getString("nome"),
                                    rs4.getString("cognome"),
                                    rs4.getString("provincia"),
                                    rs4.getString("comune"),
                                    rs4.getString("via"),
                                    rs4.getInt("civico"),
                                   Integer.parseInt(rs4.getString("Cap")),
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

                Prenotazione p = new Prenotazione(
                        idPrenotazione,
                        rs.getDate("DataPrenotazione").toLocalDate(),
                        rs.getDate("DataArrivoCliente").toLocalDate(),
                        rs.getDate("DataPartenzaCliente").toLocalDate(),
                        trattamento,
                        rs.getString("TipoDocumento"),
                        rs.getDate("DataRilascio").toLocalDate(),
                        rs.getDate("dataScadenza").toLocalDate(),
                        rs.getString("Intestatario"),
                        rs.getString("NoteAggiuntive"),
                        (ArrayList<Camera>) camere,
                        (ArrayList<Servizio>) servizi,
                        (ArrayList<Cliente>) clienti,
                        rs.getString("numeroDocumento")
                );
                prenotazioni.add(p);
            }
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
<<<<<<< HEAD
                        PreparedStatement preparedStatement = connection.prepareStatement("CREATE or replace view PrenotaIS as\n" +
=======
                        PreparedStatement preparedStatement = connection.prepareStatement("CREATE or replace view hotelcolossus.PrenotaIS as\n" +
>>>>>>> adf0e321d345f52dffea85f0929888aa93d23400
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
