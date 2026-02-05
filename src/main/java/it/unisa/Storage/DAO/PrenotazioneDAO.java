package it.unisa.Storage.DAO;

import com.mysql.cj.protocol.x.XServerSession;
import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.ErrorInputException;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrenotazioneDAO implements FrontDeskStorage<Prenotazione> {
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(PrenotazioneDAO.class);
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

    private Pattern injection =Pattern.compile("^('{2,}|--|;)$");
    private Matcher matcher;
    private static final String VIEW_TABLE_NAME = "prenotazioneClientiCamereView";


    private ClienteDAO clienteDAO;

    private synchronized void createView() throws SQLException {
        String createView =
                "CREATE or REPLACE VIEW "+VIEW_TABLE_NAME+" AS "
                + "SELECT \n" +
                        "    p.*,\n" +
                        "    a.CF, a.NumeroCamera, a.PrezzoAcquisto, a.NumeroCameraStorico, \n" +
                        "    c.NomeCamera, c.NumeroMaxOcc, c.NoteCamera, c.Stato AS StatoCamera,\n" +
                        "    cl.nome, cl.cognome, cl.Email, cl.telefono, cl.DataDiNascita,\n" +
                        "    cl.Cap, cl.comune, cl.civico, cl.provincia, cl.via,\n" +
                        "    cl.Sesso, cl.Nazionalita, cl.IsBlackListed\n" +
                        "FROM Prenotazione p\n" +
                        "INNER JOIN Associato_a a ON p.IDPrenotazione = a.IDPrenotazione\n" +
                        "LEFT JOIN Camera c ON a.NumeroCamera = c.NumeroCamera\n" +
                        "LEFT JOIN Cliente cl ON a.CF = cl.CF;";

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
    public synchronized void doSave(Prenotazione p)  {
        PreparedStatement preparedStatement = null;
        ServizioDAO servizioDAO = new ServizioDAO();

        try {
            connection = ConnectionStorage.getConnection();
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO prenotazione(NomeIntestatario,DataCreazionePrenotazione, DataArrivoCliente, DataPartenzaCliente,numeroDocumento" +
                    ",DataRilascioDocumento, DataScadenzaDocumento,NomeTrattamento,NoteAggiuntive, TipoDocumento,PrezzoAcquistoTrattamento,Cittadinanza) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? ,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, p.getIntestatario());
            preparedStatement.setDate(2, Date.valueOf(p.getDataCreazionePrenotazione()));
            preparedStatement.setDate(3, Date.valueOf(p.getDataInizio()));
            preparedStatement.setDate(4, Date.valueOf(p.getDataFine()));
            preparedStatement.setString(5, p.getNumeroDocumento());
            preparedStatement.setDate(6, Date.valueOf(p.getDataRilascio()));
            preparedStatement.setDate(7, Date.valueOf(p.getDataScadenza()));
            if (p.getTrattamento() != null) {
                preparedStatement.setString(8, p.getTrattamento().getNome());
                preparedStatement.setDouble(11, p.getPrezzoAcquistoTrattamento());
            }else{
                preparedStatement.setString(8, null);
                preparedStatement.setDouble(11, 0);
            }
            preparedStatement.setString(9, p.getNoteAggiuntive());
            preparedStatement.setString(10, p.getTipoDocumento());
            preparedStatement.setString(12, p.getCittadinanza());

            preparedStatement.executeUpdate();


            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);
                    p.setIDPrenotazione(idGenerato);
                }
            }

            // Salva i servizi associati
            String query = "Insert into ha (IDPrenotazione,IDServizio,NomeServizioAcquistato,Quantità,PrezzoAcquistoServizio) values(?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query);

            HashMap<Integer, Integer> conteggioMapServizio = new HashMap<>(); // mappe per gestire il rapporto quantità - id
            HashMap<Integer, Servizio> infoMapServizio = new HashMap<>();
            int id;
            try {
                for (Servizio servizio : p.getListaServizi()) {
                    id = servizio.getId();
                    conteggioMapServizio.put(id, conteggioMapServizio.getOrDefault(id, 0) + 1);
                    logger.debug("conteggio servizio "+id);
                    if (!infoMapServizio.containsKey(id)) {
                        infoMapServizio.put(id, servizio);
                    }
                }

                for(Integer idServizio : conteggioMapServizio.keySet()) {
                    Servizio servizio = infoMapServizio.get(idServizio);
                    int quantità = conteggioMapServizio.get(idServizio);
                    logger.debug("Salvataggio servizio: " + servizio.getNome() + " con quantità: " + quantità);
                    preparedStatement1.setInt(1, p.getIDPrenotazione());
                    preparedStatement1.setInt(2, servizio.getId());
                    preparedStatement.setString(3, servizio.getNome());
                    preparedStatement1.setInt(4, quantità);
                    preparedStatement1.setDouble(5, servizio.getPrezzo());
                    preparedStatement1.executeUpdate();
                }
            }catch(SQLException e){
                    e.printStackTrace();
                    return;
            }

                for (Cliente cliente : p.getListaClienti()) {
                    logger.debug("Entra nel for per matchare camere e clienti della prenotazione");
                    String query1 = "INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto,NominativoCliente,NumeroCameraStorico ) " +
                            "VALUES (?, ?, ?, ?,?,?)";
                    try (PreparedStatement stmt = connection.prepareStatement(query1)) {
                        stmt.setString(1, cliente.getCf());
                        stmt.setInt(2, cliente.getCamera().getNumeroCamera());
                        stmt.setInt(3, p.getIDPrenotazione());
                        stmt.setDouble(4, cliente.getCamera().getPrezzoCamera());
                        stmt.setString(5,cliente.getNome()+" "+cliente.getCognome());
                        stmt.setInt(6,cliente.getCamera().getNumeroCamera());
                        stmt.executeUpdate();
                        logger.debug("query effettuata con successo");
                    }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // O logga l'errore, ma non interrompere il flusso
            }

            if (connection != null) {
                try {
                    ConnectionStorage.releaseConnection(connection);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param list
     * @throws UnsupportedOperationException
     */

    public void doSaveAll(List<Prenotazione> list)  {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void doDelete(Prenotazione p) throws SQLException {
        connection = ConnectionStorage.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, p.getIDPrenotazione());
            preparedStatement.executeUpdate();
        }
        ConnectionStorage.releaseConnection(connection);
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

        connection = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        Prenotazione p = null;
        int key = (int) codicePrenotazione;
        ArrayList<Camera> camere = new ArrayList<>();
        ArrayList<Servizio> servizi = new ArrayList<>();
        ArrayList<Cliente> clienti = new ArrayList<>();



        String selectSql = "SELECT * FROM "+VIEW_TABLE_NAME+" WHERE IDPrenotazione = ?";
        ResultSet rs = null; Camera camera = null   ; Cliente cliente = null;
        try{
            ps = connection.prepareStatement(selectSql);
            ps.setInt(1,key);
            rs = ps.executeQuery();

            if(rs.next()){
                try {
                    int i=0;
                    do{
                        camera = new Camera(rs.getInt("NumeroCameraStorico"),Stato.valueOf(rs.getString("StatoCamera")),
                                rs.getInt("NumeroMaxOcc"),rs.getDouble("PrezzoAcquisto"),rs.getString("NoteCamera"),rs.getString("NomeCamera"));
                        camere.add(camera);
                        String intestatarioCompleto = rs.getString("NomeIntestatario");
                        String nome = "";
                        String cognome = "";
                        if (intestatarioCompleto != null && !intestatarioCompleto.trim().isEmpty()) {
                            String[] parti = intestatarioCompleto.trim().split("\\s+", 2);

                            if (parti.length > 0) {
                                nome = parti[0]; // La prima parola è sempre il nome
                            }

                            if (parti.length > 1) {
                                cognome = parti[1]; // Tutto quello che c'è dopo il primo spazio è il cognome
                            }
                        }
                        logger.debug("Associazione clienti e camere");
                        cliente = new Cliente(nome,cognome,rs.getString("provincia"),rs.getString("comune"),rs.getString("via"),rs.getInt("civico"),rs.getInt("cap"),
                                rs.getString("telefono"),rs.getString("sesso"),rs.getDate("DataDiNascita").toLocalDate(),rs.getString("CF"),
                                rs.getString("email"),rs.getString("nazionalita"),camere.get(i));
                        cliente.setBlacklisted(rs.getBoolean("isBlacklisted"));
                        clienti.add(cliente);

                        i++;


                        String nomeTratt = rs.getString("NomeTrattamento");
                        double prezzoTratt = rs.getDouble("PrezzoAcquistoTrattamento");
                        Trattamento t = (nomeTratt == null) ? null : new Trattamento(nomeTratt, prezzoTratt);

                        p = new Prenotazione();
                        p.setIDPrenotazione(key);
                        p.setTrattamento(t);
                        p.setPrezzoAcquistoTrattamento(prezzoTratt);
                        p.setListaClienti(clienti);
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
                        p.setCittadinanza(rs.getString("Cittadinanza"));
                        p.setTipoDocumento(rs.getString("TipoDocumento"));


                    }
                    while(rs.next());

                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }else{
                throw new SQLException("ERRORE: prenotazione ID="+key+"non contiene camere o clienti");
            }

            ps = connection.prepareStatement("SELECT * FROM HA WHERE IDPrenotazione = ? ");
            ps.setInt(1, key);
            rs = ps.executeQuery();
            ServizioDAO dao3 = new ServizioDAO();

            if (rs.next()){
                try {
                    do {
                        int num_servizi =  rs.getInt("quantità");
                        Servizio servizio = null;
                        servizio = dao3.doRetriveByKey(rs.getString("NomeServizioAcquistato"));
                        servizio.setPrezzo(rs.getDouble("PrezzoAcquistoServizio"));
                        for(int i=0; i<num_servizi;i++){
                            servizi.add(servizio);
                        }

                    }while (rs.next());
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            if(p!= null) p.setListaServizi(servizi);

        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } finally {
                if (connection != null)
                    ConnectionStorage.releaseConnection(connection);
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

        connection = null;
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
        String[] attributi = new String[16];

        CameraDAO cameraDAO = new CameraDAO();
        ServizioDAO serviziDAO = new ServizioDAO();

        String[] sql = new String[2];
        sql[0] = " UPDATE prenotazione " + " SET IDPrenotazione = ? , NomeIntestatario = ? , DataCreazionePrenotazione = ? , DataArrivoCliente = ? , DataPartenzaCliente = ?, DataEmissioneRicevuta = ? , " +
                "NumeroDocumento = ? , DataRilascioDocumento = ? , DataScadenzaDocumento = ? , NomeTrattamento = ? , NoteAggiuntive = ? ," +
                "TipoDocumento = ? , Stato = ? , CheckIn = ? , PrezzoAcquistoTrattamento = ? , MetodoPagamento = ? , Cittadinanza = ?" +
                " Where IDPrenotazione = ?";

        sql[1] = "UPDATE HA SET NomeServizioAcquistato = ? , PrezzoAcquistoServizio = ? ,quantità =?" + " WHERE IDServizio = ? AND IDPrenotazione = ? ";
        connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql[0]);
            try {
                preparedStatement.setInt(1, p.getIDPrenotazione());
                attributi[0] = String.valueOf(p.getIDPrenotazione());
                preparedStatement.setString(2, p.getIntestatario());
                attributi[1] = p.getIntestatario();
                preparedStatement.setDate(3, Date.valueOf(p.getDataCreazionePrenotazione()));
                attributi[2] = p.getDataCreazionePrenotazione().toString();
                preparedStatement.setDate(4, Date.valueOf(p.getDataInizio()));
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

                if(p.getTrattamento()!=null){
                    preparedStatement.setString(10, p.getTrattamento().getNome().trim());
                    attributi[9] = p.getTrattamento().getNome().trim();
                }else{
                    preparedStatement.setNull(10, Types.VARCHAR);

                    attributi[9] = "";
                }

                preparedStatement.setString(11, p.getNoteAggiuntive().trim());
                attributi[10] = p.getNoteAggiuntive().trim();
                preparedStatement.setString(12, p.getTipoDocumento().trim());
                attributi[11] = p.getTipoDocumento().trim();
                preparedStatement.setBoolean(13, p.getStatoPrenotazione());
                attributi[12] = String.valueOf(p.getStatoPrenotazione());
                preparedStatement.setBoolean(14, p.isCheckIn());
                attributi[13] = String.valueOf(p.isCheckIn());
                preparedStatement.setDouble(15, p.getTrattamento().getPrezzo());
                attributi[14] = String.valueOf(p.getTrattamento().getPrezzo());
                preparedStatement.setString(16, p.getMetodoPagamento());
                attributi[15] = String.valueOf(p.getMetodoPagamento());
                preparedStatement.setString(17, p.getCittadinanza());
                attributi[16] = String.valueOf(p.getCittadinanza());

                preparedStatement.executeUpdate();

                if (p.getListaClienti() != null || !p.getListaClienti().isEmpty()) {
                    for (Cliente c : p.getListaClienti()) {
                        for (Cliente c2 : CatalogoClienti.getListaClienti()) {
                            if (!c2.getCf().equals(c.getCf())) {
                                throw new NoSuchElementException("registrare prima il cliente");
                            }
                        }
                        clienteDAO.doUpdate(c);
                    }
                }

                preparedStatement = connection.prepareStatement(sql[1]);

                ArrayList<Servizio> duplicate = new ArrayList<>();
                if (p.getListaServizi() != null || !p.getListaServizi().isEmpty()) {
                    for (Servizio s : p.getListaServizi()) {
                        if (!duplicate.contains(s)) {
                            duplicate.add(s);
                            preparedStatement.setString(1, s.getNome());
                            preparedStatement.setDouble(2, s.getPrezzo());
                            int quantity = Collections.frequency(p.getListaServizi(), s);
                            preparedStatement.setInt(3, quantity);
                            preparedStatement.executeUpdate();
                        }
                    }
                }

                //controllo per sql injection
                int i = 1;
                for (String s : attributi) {
                    i++;
                    if (injection.matcher(s).matches()) {
                        throw new ErrorInputException("valore non ammesso trovato nella stringa " + i);
                    }
                }

            }finally {
                if (connection != null) {
                        ConnectionStorage.releaseConnection(connection);
                }
            }
    }


    /**
     * @param attribute;
     * @param value;
     * @return Collection<Prenotazione>;
     * @throws SQLException;
     */
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
                    String query = "SELECT * FROM Prenotazione WHERE IDPrenotazione = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs1 = stmt.executeQuery()) {
                            if (rs1.next()){
                                trattamento = new Trattamento(
                                        rs1.getString("NomeTrattamento"),
                                        rs1.getDouble("PrezzoAcquistoTrattamento")
                                );
                            }
                        }
                    }

                    // Recupera i servizi
                    String query2 = "SELECT * FROM ha WHERE IDPrenotazione = ?";
                    ArrayList<Servizio> servizi = new ArrayList<>();

                    try (PreparedStatement stmt = connection.prepareStatement(query2)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs2 = stmt.executeQuery()) {
                            while (rs2.next()) {
                                int n= rs2.getInt("quantità");
                                String nome = rs2.getString("NomeServizioAcquistato");
                                Double prezzo = rs2.getDouble("PrezzoAcquistoServizio");
                                    for (int i = 0; i<n;i++){
                                        servizi.add(new Servizio(nome, prezzo));
                                    }
                            }
                        }
                    }

                    // Recupera le camere
                    String query3 = "SELECT c.* , a.NominativoCliente, a.NumeroCameraStorico , a.PrezzoAcquisto FROM camera c " +
                            "JOIN associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                            "WHERE a.IDPrenotazione = ?";

                    Collection<Camera> camere = new ArrayList<>();

                    try (PreparedStatement stmt = connection.prepareStatement(query3)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs3 = stmt.executeQuery()) {
                            while (rs3.next()) {
                                camere.add(new Camera(
                                        rs3.getInt("NumeroCameraStorico"),
                                        Stato.valueOf(rs3.getString("Stato")),
                                        rs3.getInt("NumeroMaxOcc"),
                                        rs3.getDouble("PrezzoAcquisto"),
                                        rs3.getString("NoteCamera"),
                                        rs3.getString("NomeCamera")
                                ));
                            }
                        }
                    }

                    // Recupera i clienti
                    String query4 = "SELECT  cl.* FROM cliente cl " +
                            "JOIN associato_a a ON cl.CF = a.CF " +
                            "WHERE a.IDPrenotazione = ?";

                    ArrayList<Cliente> clienti = new ArrayList<>();
                    String query5 = "SELECT cl.* FROM cliente cl JOIN associato_a a ON cl.CF = a.CF WHERE a.IDPrenotazione = ?";
                    PreparedStatement preparedStatement1;
                    Camera camera = new Camera();
                    preparedStatement1 = connection.prepareStatement(query5);
                    try (PreparedStatement stmt = connection.prepareStatement(query4)) {
                        stmt.setInt(1, idPrenotazione);
                        try(ResultSet rs4 = stmt.executeQuery()){
                            rs4.next();
                            preparedStatement1.setString(1,rs4.getString("CF"));
                            try(ResultSet resultSet = preparedStatement1.executeQuery()){
                                resultSet.next();
                                camera.setNumeroCamera(resultSet.getInt("NumeroCameraStorico"));
                                camera.setNoteCamera(resultSet.getString("NoteCamera"));
                                camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
                                camera.setPrezzoCamera(resultSet.getDouble("PrezzoAcquisto"));
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
                                Prenotazione p = new Prenotazione(
                                rs.getDate("DataCreazionePrenotazione").toLocalDate(),
                                rs.getDate("DataArrivoCliente").toLocalDate(),
                                rs.getDate("DataPartenzaCliente").toLocalDate(),
                                rs.getDate("DataEmissioneRicevuta") != null ? rs.getDate("DataEmissioneRicevuta").toLocalDate() : null,
                                trattamento,
                                rs.getDouble("PrezzoAcquistoTrattamento"),
                                rs.getString("TipoDocumento"),
                                rs.getDate("DataRilascio").toLocalDate(),
                                rs.getDate("DataScadenza").toLocalDate(),
                                rs.getString("Intestatario"),
                                rs.getString("NoteAggiuntive"),
                                servizi,
                                clienti,
                                rs.getString("NumeroDocumento"),
                                rs.getString("metodoPagamento"),
                                rs.getString("Cittadinanza"));
                                p.setIDPrenotazione(idPrenotazione);
                                p.setCheckIn(rs.getBoolean("CheckIn"));
                                p.setStatoPrenotazione(rs.getBoolean("StatoPrenotazione"));

                                prenotaziones.add(p);

            }
            return prenotaziones;
        }

    }
    }

}
