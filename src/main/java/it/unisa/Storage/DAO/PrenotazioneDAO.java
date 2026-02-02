package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.ErrorInputException;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrenotazioneDAO implements FrontDeskStorage<Prenotazione> {
    private Connection connection;
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
    private Pattern injection =Pattern.compile("^('{2,}|--|;)$");
    private Matcher matcher;
    private ClienteDAO clienteDAO;

    public PrenotazioneDAO(){
        this.clienteDAO =  new ClienteDAO();
    }

    public PrenotazioneDAO(ClienteDAO clienteDAO){this.clienteDAO = clienteDAO;
    }

    @Override
    public synchronized void doSave(Prenotazione p) throws SQLException {
        if(p != null && p.getTrattamento() != null){
            connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) " +
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

            // Salva il trattamento associato
            if(p.getTrattamento().getNome() != null){
                String query = "UPDATE trattamento SET IDPrenotazione = ? WHERE Nome = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, p.getIDPrenotazione());
                    stmt.setString(2, p.getTrattamento().getNome());
                    stmt.executeUpdate();
                }
            }

            // Salva i servizi associati
            for (Servizio servizio : p.getListaServizi()){
                String query = "UPDATE servizio SET IDPrenotazione = ? WHERE Nome = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, p.getIDPrenotazione());
                    stmt.setString(2, servizio.getNome());
                    stmt.executeUpdate();
                }
            }


            // Salva le associazioni clienti-camere
            for(Cliente cliente : p.getListaClienti()){
                clienteDAO.doSave(cliente);
            }

            for (Camera camera : p.getListaCamere()){
                for (Cliente cliente : p.getListaClienti()){
                    System.out.println("ciao");
                    String query = "INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                            "VALUES (?, ?, ?, ?)";
                    System.out.println(cliente);
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
                connection = ConnectionStorage.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) " +
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

                // Salva i servizi associati
                for (Servizio servizio : p.getListaServizi()){
                    String query = "UPDATE servizio SET IDPrenotazione = ? WHERE Nome = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, p.getIDPrenotazione());
                        stmt.setString(2, servizio.getNome());
                        stmt.executeUpdate();
                    }
                }


                // Salva le associazioni clienti-camere
                for(Cliente cliente : p.getListaClienti()){
                    clienteDAO.doSave(cliente);
                }

                for (Camera camera : p.getListaCamere()){
                    for (Cliente cliente : p.getListaClienti()) {
                        String query = "INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                                "VALUES (?, ?, ?, ?)";
                        System.out.println(cliente);
                        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
       connection = ConnectionStorage.getConnection();

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

        connection = ConnectionStorage.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, (Integer) codicePrenotazione);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()){
                    int idPrenotazione = rs.getInt("IDPrenotazione");

                    // Recupera il trattamento
                    Trattamento trattamento = null;
                    String query = "SELECT * FROM trattamento WHERE IDPrenotazione = ?";

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
                    String query2 = "SELECT * FROM servizio WHERE IDPrenotazione = ?";
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
                                        rs3.getString("NoteCamera")
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
                            p = connection.prepareStatement(query5);
                            while (rs4.next()) {
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
                                        rs4.getString("Nazionalità"),
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
         connection = ConnectionStorage.getConnection();

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
                String query1 = "SELECT * FROM trattamento WHERE IDPrenotazione = ?";

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
                String query2 = "SELECT * FROM servizio WHERE IDPrenotazione = ?";
                Collection<Servizio> servizi = new ArrayList<>();

                try (PreparedStatement stmt3 = connection.prepareStatement(query2)) {
                    stmt3.setInt(1, idPrenotazione);

                    try (ResultSet rs2 = stmt3.executeQuery()) {
                        while (rs2.next()) {
                            System.out.println("efwkjf");
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
                                    rs3.getString("NoteCamera")
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
                                    if(rs5.next()){
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
        String [] attributi = new String [21];

        CameraDAO cameraDAO = new CameraDAO();
        ServizioDAO serviziDAO = new ServizioDAO();
        if (p != null) {
            connection = ConnectionStorage.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE prenotazione SET IDPrenotazione = ? , NomeIntestatario = ? , DataCreazionePrenotazione = ? , DataArrivoCliente = ? , DataPartenzaCliente = ?, DataEmissioneRicevuta = ? , " +
                            "NumeroDocumento = ? , DataRilascioDocumento = ? , DataScadenzaDocumento = ? , NomeTrattamento = ? , NoteAggiuntive = ? ," +
                            "TipoDocumento = ? , Stato = ? , CheckIn = ? , PrezzoAcquistoTrattamento = ? , MetodoPagamento = ? , CF = ?, NumeroCamera = ? ,PrezzoAcquisto = ? , NomeServizio = ?," +
                            "PrezzoAcquistoServizio = ? Where IDPrenotazione = ?")) {


                preparedStatement.setInt(1,p.getIDPrenotazione());
                attributi[0] = String.valueOf(p.getIDPrenotazione());
                preparedStatement.setString(2,p.getIntestatario());
                attributi[1] = p.getIntestatario();
                preparedStatement.setDate(3, Date.valueOf(p.getDataCreazionePrenotazione()));
                attributi[2] = p.getDataCreazionePrenotazione().toString();
                preparedStatement.setDate(4,Date.valueOf(p.getDataInizio()));
                attributi[3] = p.getDataInizio().toString();
                preparedStatement.setDate(5, Date.valueOf(p.getDataFine()));
                attributi[4] = p.getDataFine().toString();
                preparedStatement.setDate(6, Date.valueOf(p.getDataEmissioneRicevuta()));
                attributi[5] = p.getDataEmissioneRicevuta().toString();
                preparedStatement.setString(7, p.getNumeroDocumento());
                attributi[6] = p.getNumeroDocumento();
                preparedStatement.setDate(8, Date.valueOf(p.getDataRilascio()));
                attributi[7] = p.getDataRilascio().toString();
                preparedStatement.setDate(9, Date.valueOf(p.getDataScadenza()));
                attributi[8] = p.getDataScadenza().toString();
                preparedStatement.setString(10, p.getTrattamento().getNome().trim());
                attributi[9] = p.getTrattamento().getNome().trim();
                preparedStatement.setString(11, p.getNoteAggiuntive().trim());
                attributi[10] = p.getNoteAggiuntive().trim();
                preparedStatement.setString(12, p.getTipoDocumento().trim());
                attributi[11] = p.getTipoDocumento().trim();
                preparedStatement.setBoolean(13,p.getStatoPrenotazione());
                attributi[12]= String.valueOf(p.getStatoPrenotazione());
                preparedStatement.setBoolean(14,p.isCheckIn());
                attributi[13]= String.valueOf(p.isCheckIn());
                preparedStatement.setDouble(15,p.getTrattamento().getPrezzo());
                attributi[14] = String.valueOf(p.getTrattamento().getPrezzo());
                preparedStatement.setString(16,p.getMetodoPagamento());
                attributi[15] = String.valueOf(p.getMetodoPagamento());

                if(p.getListaClienti()!=null|| !p.getListaClienti().isEmpty() ) {  // da testare chiedere a me se danno problemi
                    for (Cliente c : p.getListaClienti()) {
                        if(!CatalogoClienti.clienteIsEquals(c)) {
                            clienteDAO.doUpdate(c);
                        }
                        if(!CatalogoCamere.cameraIsEquals(c)) {
                            cameraDAO.doUpdate(c.getCamera());
                        }
                    }
                }
                if (p.getListaServizi()!=null|| !p.getListaServizi().isEmpty()) {
                    for (Servizio s : p.getListaServizi()) {
                        serviziDAO.doUpdate(s);
                    }
                }

                //controllo per sql injection
                int i=1;
                for(String s : attributi){
                    i++;
                    if(injection.matcher(s).matches()){
                        throw new  ErrorInputException("valore non ammesso trovato nella stringa "+i);
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

    @Override
    public Collection<Prenotazione> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        connection = ConnectionStorage.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM prenotazione WHERE " + attribute + " = ?")) {
            preparedStatement.setObject(1, value);
            Collection<Prenotazione> prenotaziones = new ArrayList<>();
            try (ResultSet rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    int idPrenotazione = rs.getInt("IDPrenotazione");

                    // Recupera il trattamento
                    Trattamento trattamento = null;
                    String query = "SELECT * FROM trattamento WHERE IDPrenotazione = ?";

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
                    String query2 = "SELECT * FROM servizio WHERE IDPrenotazione = ?";
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
                                        rs3.getString("NoteCamera")
                                ));
                            }
                        }
                    }

                    // Recupera i clienti
                    String query4 = "SELECT DISTINCT cl.* FROM cliente cl " +
                            "JOIN associato_a a ON cl.CF = a.CF " +
                            "WHERE a.IDPrenotazione = ?";

                    Collection<Cliente> clienti = new ArrayList<>();
                    String query5 = "SELECT c.* FROM (associato_a join camera c on associato_a.NumeroCamera = c.NumeroCamera" +
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

}
