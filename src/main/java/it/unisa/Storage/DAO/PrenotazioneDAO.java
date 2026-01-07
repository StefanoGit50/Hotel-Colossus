package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class PrenotazioneDAO implements FrontDeskStorage<Prenotazione> {

    // motodo che salva
    @Override
    public synchronized void doSave(Prenotazione p) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, Tipo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

        preparedStatement.setDate(1, Date.valueOf(p.getDataCreazionePrenotazione()));
        preparedStatement.setDate(2, Date.valueOf(p.getDataInizio()));
        preparedStatement.setDate(3, Date.valueOf(p.getDataFine()));
        preparedStatement.setString(4, p.getNoteAggiuntive());
        preparedStatement.setString(5, p.getIntestatario());
        preparedStatement.setDate(6, Date.valueOf(p.getDataScadenza()));
        preparedStatement.setInt(7, p.getNumeroDocumento());
        preparedStatement.setDate(8, Date.valueOf(p.getDataRilascio()));
        preparedStatement.setString(9, p.getTipoDocumento());

        preparedStatement.executeUpdate();

        // Salva il trattamento associato
        if (p.getTrattamento() != null) {
            String query = "UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, p.getCodicePrenotazione());
                stmt.setString(2, p.getTrattamento().getNome());
                stmt.executeUpdate();
            }
        }

        // Salva i servizi associati
        for (Servizio servizio : p.getListaServizi()) {
            String query = "UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, p.getCodicePrenotazione());
                stmt.setString(2, servizio.getNome());
                stmt.executeUpdate();
            }
        }
        ClienteDAO clienteDAO = new ClienteDAO();
        // Salva le associazioni clienti-camere
        for (Cliente cliente : p.getListaClienti()) {
            clienteDAO.doSave(cliente);
        }

        for (Camera camera : p.getListaCamere()) {
            for (Cliente cliente : p.getListaClienti()) {
                String query = "INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                        "VALUES (?, ?, ?, ?)";
                System.out.println(cliente);
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, cliente.getCf());
                    stmt.setInt(2, camera.getNumeroCamera());
                    stmt.setInt(3, p.getCodicePrenotazione());
                    stmt.setDouble(4, camera.getPrezzoCamera());
                    stmt.executeUpdate();
                }
            }
        }

        preparedStatement.close();
        connection.close();
    }

    @Override
    public synchronized void doDelete(Prenotazione p) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, p.getCodicePrenotazione());
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

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")) {
            preparedStatement.setInt(1, (Integer) codicePrenotazione);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int idPrenotazione = rs.getInt("IDPrenotazione");

                    // Recupera il trattamento
                    Trattamento trattamento = null;
                    String query = "SELECT * FROM Trattamento WHERE IDPrenotazione = ?";

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
                    String query2 = "SELECT * FROM Servizio WHERE IDPrenotazione = ?";
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
                    String query3 = "SELECT DISTINCT c.* FROM Camera c " +
                            "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
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
                    String query4 = "SELECT DISTINCT cl.* FROM Cliente cl " +
                            "JOIN Associato_a a ON cl.CF = a.CF " +
                            "WHERE a.IDPrenotazione = ?";

                    Collection<Cliente> clienti = new ArrayList<>();

                    try (PreparedStatement stmt = connection.prepareStatement(query4)) {
                        stmt.setInt(1, idPrenotazione);

                        try (ResultSet rs4 = stmt.executeQuery()) {
                            while (rs4.next()) {
                                clienti.add(new Cliente(
                                        rs4.getString("nome"),
                                        rs4.getString("cognome"),
                                        rs4.getString("Cittadinanza"),
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
                                        rs4.getString("MetodoDiPagamento")
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
                            rs.getString("Tipo"),
                            rs.getDate("DataRilascio").toLocalDate(),
                            rs.getDate("dataScadenza").toLocalDate(),
                            rs.getString("Intestatario"),
                            rs.getString("NoteAggiuntive"),
                            (ArrayList<Camera>) camere,
                            (ArrayList<Servizio>) servizi,
                            (ArrayList<Cliente>) clienti,
                            rs.getInt("numeroDocumento")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public synchronized Collection<Prenotazione> doRetriveAll(String order) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();

        String query = "SELECT * FROM Prenotazione";
        if (order != null && !order.trim().isEmpty()) {
            query += " ORDER BY " + order;
        }

        Collection<Prenotazione> prenotazioni = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idPrenotazione = rs.getInt("IDPrenotazione");

                // Recupera il trattamento
                Trattamento trattamento = null;
                String query1 = "SELECT * FROM Trattamento WHERE IDPrenotazione = ?";

                try (PreparedStatement stmt2 = connection.prepareStatement(query1)) {
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
                String query2 = "SELECT * FROM Servizio WHERE IDPrenotazione = ?";
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
                String query3 = "SELECT DISTINCT c.* FROM Camera c " +
                        "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
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
                String query4 = "SELECT DISTINCT cl.* FROM Cliente cl " +
                        "JOIN Associato_a a ON cl.CF = a.CF " +
                        "WHERE a.IDPrenotazione = ?";

                Collection<Cliente> clienti = new ArrayList<>();

                try (PreparedStatement stmt5 = connection.prepareStatement(query4)) {
                    stmt5.setInt(1, idPrenotazione);

                    try (ResultSet rs4 = stmt5.executeQuery()) {
                        while (rs4.next()) {
                            clienti.add(new Cliente(
                                    rs4.getString("nome"),
                                    rs4.getString("cognome"),
                                    rs4.getString("Cittadinanza"),
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
                                    rs4.getString("MetodoDiPagamento")
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
                        rs.getString("Tipo"),
                        rs.getDate("DataRilascio").toLocalDate(),
                        rs.getDate("dataScadenza").toLocalDate(),
                        rs.getString("Intestatario"),
                        rs.getString("NoteAggiuntive"),
                        (ArrayList<Camera>) camere,
                        (ArrayList<Servizio>) servizi,
                        (ArrayList<Cliente>) clienti,
                        rs.getInt("numeroDocumento")
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
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NullPointerException Se il parametro p è null.
     *
     * Precondizioni:
     *   p != null
     *   p.getCodicePrenotazione() deve corrispondere a una prenotazione esistente nel database
     *   Tutte le date (DataCreazionePrenotazione, DataInizio, DataFine, DataRilascio, DataScadenza)
     *       devono essere valorizzate e valide
     *   Se p.getTrattamento() != null, il trattamento deve esistere nel database
     *   Tutti i servizi in p.getListaServizi() devono esistere nel database
     *   p.getNumeroDocumento() deve essere maggiore di 0
     *
     * Postcondizioni:
     *   Il record della prenotazione nel database viene aggiornato con i nuovi valori
     *   Il IDPrenotazione (chiave primaria) rimane invariato
     *   Se presente, il trattamento viene collegato alla prenotazione
     *   Tutti i servizi nella lista vengono collegati alla prenotazione
     *   Le associazioni clienti-camere nella tabella Associato_a NON vengono modificate
     */
    @Override
    public synchronized void doUpdate(Prenotazione p) throws SQLException
    {
        if (p != null)
        {
            Connection connection = ConnectionStorage.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Prenotazione SET DataPrenotazione = ?, DataArrivoCliente = ?, " +
                            "DataPartenzaCliente = ?, NoteAggiuntive = ?, Intestatario = ?, " +
                            "dataScadenza = ?, numeroDocumento = ?, DataRilascio = ?, Tipo = ? " +
                            "WHERE IDPrenotazione = ?"))
            {

                preparedStatement.setDate(1, Date.valueOf(p.getDataCreazionePrenotazione()));
                preparedStatement.setDate(2, Date.valueOf(p.getDataInizio()));
                preparedStatement.setDate(3, Date.valueOf(p.getDataFine()));
                preparedStatement.setString(4, p.getNoteAggiuntive());
                preparedStatement.setString(5, p.getIntestatario());
                preparedStatement.setDate(6, Date.valueOf(p.getDataScadenza()));
                preparedStatement.setInt(7, p.getNumeroDocumento());
                preparedStatement.setDate(8, Date.valueOf(p.getDataRilascio()));
                preparedStatement.setString(9, p.getTipoDocumento());
                preparedStatement.setInt(10, p.getCodicePrenotazione());

                preparedStatement.executeUpdate();

                // Aggiorna il trattamento associato
                if (p.getTrattamento() != null)
                {
                    String query = "UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(query))
                    {
                        stmt.setInt(1, p.getCodicePrenotazione());
                        stmt.setString(2, p.getTrattamento().getNome());
                        stmt.executeUpdate();
                    }
                }

                // Aggiorna i servizi associati
                for (Servizio servizio : p.getListaServizi())
                {
                    String query = "UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(query))
                    {
                        stmt.setInt(1, p.getCodicePrenotazione());
                        stmt.setString(2, servizio.getNome());
                        stmt.executeUpdate();
                    }
                }
            }
            finally
            {
                if (connection != null)
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
}
