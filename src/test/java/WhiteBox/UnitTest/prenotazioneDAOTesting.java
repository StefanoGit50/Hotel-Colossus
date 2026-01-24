package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
* da finire
* */
@ExtendWith(MockitoExtension.class)
public class prenotazioneDAOTesting {
    @Mock
    private static DataSource source;
    private static Trattamento trattamento;
    private static Servizio servizio;
    @Mock
    private static Connection connection;
    private static String sql = "INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
            "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
            "numeroDocumento, DataRilascio, TipoDocumento) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    @Mock
    private static PreparedStatement preparedStatement;
    @Mock
    private static PreparedStatement preparedStatement1;
    @Mock
    private static PreparedStatement preparedStatement2;
    @Mock
    private static PreparedStatement preparedStatement3;
    @Mock
    private static PreparedStatement preparedStatement4;
    @Mock
    private static ResultSet resultSet;
    @Mock
    private static ResultSet resultSet1;
    @Mock
    private static ResultSet resultSet2;
    @Mock
    private static ResultSet resultSet3;
    @Mock
    private static ResultSet resultSet4;
    private static Prenotazione prenotazione;
    @Mock
    private static ClienteDAO clienteDAO;
    @InjectMocks
    private static PrenotazioneDAO prenotazioneDAO;


    @BeforeEach
    public void setUp() throws SQLException {
        prenotazione = new Prenotazione();
        trattamento = new Trattamento("D'asporto", 22);
        ArrayList<Servizio> servizios = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        servizios.add(new Servizio("Mensa", 11));
        servizios.add(new Servizio("Piscina", 12));
        servizios.add(new Servizio("ijjknk", 15));
        clientes.add(new Cliente("Mario", "Masceri", "Italiana", "Salerno", "Mercato San Severino", "Corso Armando diaz", 12, 84085, "3249552034", "Maschio", Date.valueOf("1899-12-04").toLocalDate(), "MSCMRA00T04H703M", "Mario.masceri@gmail.com"));
        clientes.add(new Cliente("Mario", "Masceri", "Italiana", "Salerno", "Mercato San Severino", "Corso Armando diaz", 12, 84085, "3249552034", "Maschio", Date.valueOf("1899-12-04").toLocalDate(), "MSCMRA00T04H703H", "Mario.masceri@gmail.com"));
        cameras.add(new Camera(101, Stato.Libera, 2, 120, "Non ce la faccio piu"));
        cameras.add(new Camera(102, Stato.Libera, 3, 110, "Non ce la faccio piu"));
        prenotazione.setDataCreazionePrenotazione(Date.valueOf("1992-12-20").toLocalDate());
        prenotazione.setDataInizio(Date.valueOf("1994-11-30").toLocalDate());
        prenotazione.setDataFine(Date.valueOf("1990-09-20").toLocalDate());
        prenotazione.setDataScadenza(Date.valueOf("2004-12-2").toLocalDate());
        prenotazione.setDataRilascio(Date.valueOf("2000-12-2").toLocalDate());
        prenotazione.setNumeroDocumento(112);
        prenotazione.setTipoDocumento("Carta d'identita");
        prenotazione.setListaClienti(clientes);
        prenotazione.setListaServizi(servizios);
        prenotazione.setListaCamere(cameras);
        prenotazione.setTrattamento(trattamento);
        prenotazione.setIDPrenotazione(6);
    }
    @DisplayName("doSave() ")
    @Test
    public void doSaveAllTrue() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement1.executeUpdate()).thenReturn(1);
        when(preparedStatement2.executeUpdate()).thenReturn(1);
        when(preparedStatement3.executeUpdate()).thenReturn(1);
        prenotazioneDAO = new PrenotazioneDAO();

        prenotazioneDAO.doSave(prenotazione);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        preparedStatement.executeUpdate();
        preparedStatement1.executeUpdate();
        preparedStatement2.executeUpdate();

        preparedStatement3.executeUpdate();
        preparedStatement3.executeUpdate();
        preparedStatement3.executeUpdate();
        preparedStatement3.executeUpdate();
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement1, times(1)).executeUpdate();
        verify(preparedStatement2, times(1)).executeUpdate();
        verify(preparedStatement3, times(4)).executeUpdate();
    }

    @Test
    public void doSaveException() throws SQLException {
        prenotazione.setListaCamere(new ArrayList<>());
        prenotazione.setListaServizi(new ArrayList<>());
        prenotazione.setListaClienti(new ArrayList<>());
        prenotazione.setTrattamento(null);
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertNull(prenotazione.getTrattamento());
        assertNotNull(prenotazione.getListaServizi());
        assertEquals(0, prenotazione.getListaServizi().size());
        assertNotNull(prenotazione.getListaCamere());
        assertEquals(0, prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0, prenotazione.getListaClienti().size());

        assertThrows(Exception.class, () -> {
            prenotazioneDAO.doSave(prenotazione);
        });

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        verify(preparedStatement, never()).executeUpdate();
        verify(preparedStatement1, never()).executeUpdate();
        verify(preparedStatement2, never()).executeUpdate();
        verify(preparedStatement3, never()).executeUpdate();
    }

    @Test
    public void doSaveAllFalse() throws SQLException {
        prenotazione.setListaCamere(new ArrayList<>());
        prenotazione.setListaServizi(new ArrayList<>());
        prenotazione.setListaClienti(new ArrayList<>());
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertNotNull(prenotazione.getTrattamento());
        assertNotNull(prenotazione.getListaServizi());
        assertEquals(0, prenotazione.getListaServizi().size());
        assertNotNull(prenotazione.getListaCamere());
        assertEquals(0, prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0, prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        preparedStatement.executeUpdate();

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement1, never()).executeUpdate();
        verify(preparedStatement2, never()).executeUpdate();
        verify(preparedStatement3, never()).executeUpdate();
    }

    @Test
    public void doSaveFalseCliente() throws SQLException {
        prenotazione.setListaClienti(new ArrayList<>());
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertNotNull(prenotazione.getTrattamento());
        assertNotNull(prenotazione.getListaServizi());
        assertNotEquals(0, prenotazione.getListaServizi().size());
        assertNotNull(prenotazione.getListaCamere());
        assertNotEquals(0, prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0, prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        preparedStatement.executeUpdate();
        preparedStatement1.executeUpdate();
        preparedStatement2.executeUpdate();

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement1, times(1)).executeUpdate();
        verify(preparedStatement2, times(1)).executeUpdate();
        verify(preparedStatement3, never()).executeUpdate();
    }

    @Test
    public void doDeleteException() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);

        prenotazione.setIDPrenotazione(null);

        assertThrows(NullPointerException.class, () -> {
            prenotazioneDAO.doDelete(prenotazione);
        });

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?");

        verify(preparedStatement, never()).executeUpdate();
    }

    @Test
    public void doDeleteAllTrue() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        prenotazioneDAO.doDelete(prenotazione);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?");

        preparedStatement.executeUpdate();

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void doRetryByKeyNull() throws SQLException {
        prenotazione = prenotazioneDAO.doRetriveByKey("ciao");
        assertNull(prenotazione);
    }

    @Test
    public void doRetryByKeyResultSetFalse() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?");
        resultSet = preparedStatement.executeQuery();

        assertThrows(NoSuchElementException.class, () -> {
            prenotazioneDAO.doRetriveByKey(7);
        });

        assertFalse(resultSet.next());

    }

    @Test
    public void doRetryByKeyResultSetTrue() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT * FROM Trattamento WHERE IDPrenotazione = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("SELECT * FROM Servizio WHERE IDPrenotazione = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("SELECT DISTINCT c.* FROM Camera c " +
                "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement3);
        when(connection.prepareStatement("SELECT DISTINCT cl.* FROM Cliente cl " +
                "JOIN Associato_a a ON cl.CF = a.CF " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement4);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        when(preparedStatement3.executeQuery()).thenReturn(resultSet3);
        when(preparedStatement4.executeQuery()).thenReturn(resultSet4);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?");
        preparedStatement1 = connection.prepareStatement("SELECT * FROM Trattamento WHERE IDPrenotazione = ?");
        preparedStatement2 = connection.prepareStatement("SELECT * FROM Servizio WHERE IDPrenotazione = ?");
        preparedStatement3 = connection.prepareStatement("SELECT DISTINCT c.* FROM Camera c " +
                "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                "WHERE a.IDPrenotazione = ?");
        preparedStatement4 = connection.prepareStatement("SELECT DISTINCT cl.* FROM Cliente cl " +
                "JOIN Associato_a a ON cl.CF = a.CF " +
                "WHERE a.IDPrenotazione = ?");

        resultSet = preparedStatement.executeQuery();
        resultSet1 = preparedStatement1.executeQuery();
        resultSet2 = preparedStatement2.executeQuery();
        resultSet3 = preparedStatement3.executeQuery();
        resultSet4 = preparedStatement4.executeQuery();

        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();

        clientes.add(new Cliente("John", "Smith", "Inglese", "Estero", "Londra", "Baker Street", 45, 121, "447123456", "Maschio", Date.valueOf("1975-04-15").toLocalDate(), "SMIJHN75D15Z114B", "john.smith@uk.com"));
        servizios.add(new Servizio("Transfer Aeroporto", 40));
        cameras.add(new Camera(202, Stato.InPulizia, 1, 85, "Singola business"));
        assertEquals(new Prenotazione(6, Date.valueOf("2023-11-15").toLocalDate(), Date.valueOf("2024-02-14").toLocalDate(), Date.valueOf("2024-02-16").toLocalDate(), new Trattamento("Massaggio Sportivo", 70), "Passaporto", Date.valueOf("2022-12-10").toLocalDate(), Date.valueOf("2032-12-10").toLocalDate(), "John Smith", "Anniversario", cameras, servizios, clientes, 901234, true, false), prenotazioneDAO.doRetriveByKey(6));

        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement1, times(1)).executeQuery();
        verify(preparedStatement2, times(1)).executeQuery();
        verify(preparedStatement3, times(1)).executeQuery();
        verify(preparedStatement4, times(1)).executeQuery();

        verify(connection, times(5)).prepareStatement(anyString());
    }

    @Test
    public void doRetriveAllTrue() throws SQLException {
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Prenotazione ORDER BY ?")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT * FROM Trattamento WHERE IDPrenotazione = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("SELECT * FROM Servizio WHERE IDPrenotazione = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("SELECT DISTINCT c.* FROM Camera c " +
                "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement3);
        when(connection.prepareStatement("SELECT DISTINCT cl.* FROM Cliente cl " +
                "JOIN Associato_a a ON cl.CF = a.CF " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement4);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        when(preparedStatement3.executeQuery()).thenReturn(resultSet3);
        when(preparedStatement4.executeQuery()).thenReturn(resultSet4);
        ArrayList<Cliente> clientes = prenotazione.getListaClienti();
        doNothing().when(clienteDAO).doSave(clientes.get(0));



        // PRENOTAZIONE 1
        ArrayList<Camera> camere1 = new ArrayList<>();
        camere1.add(new Camera(101, Stato.Prenotata, 2, 150.0, "Vista mare"));

        ArrayList<Servizio> servizi1 = new ArrayList<>();
        servizi1.add(new Servizio("Colazione", 15.0));

        ArrayList<Cliente> clienti1 = new ArrayList<>();
        clienti1.add(new Cliente("Mario", "Rossi", "Italiana", "NA", "Napoli", "Via Roma", 12, 80100,
                "3331234567", "M", LocalDate.of(1985, 3, 15), "RSSMRA85C15F839K", "mario.rossi@email.com"));

        Trattamento trattamento1 = new Trattamento("Bed & Breakfast", 25.0);

        Prenotazione p1 = new Prenotazione(1, LocalDate.of(2026, 1, 15), LocalDate.of(2026, 2, 10),
                LocalDate.of(2026, 2, 15), trattamento1, "Carta d'Identità", LocalDate.of(2020, 5, 10),
                LocalDate.of(2030, 5, 10), "Mario Rossi", "Cliente VIP", camere1, servizi1, clienti1, 123456789);

// PRENOTAZIONE 2
        ArrayList<Camera> camere2 = new ArrayList<>();
        camere2.add(new Camera(205, Stato.Prenotata, 4, 220.0, "Suite familiare"));

        ArrayList<Servizio> servizi2 = new ArrayList<>();
        servizi2.add(new Servizio("Spa", 50.0));
        servizi2.add(new Servizio("Minibar", 30.0));

        ArrayList<Cliente> clienti2 = new ArrayList<>();
        clienti2.add(new Cliente("Laura", "Bianchi", "Italiana", "RM", "Roma", "Via Nazionale", 45, 185,
                "3339876543", "F", LocalDate.of(1990, 7, 20), "BNCLRA90L60H501Z", "laura.bianchi@email.com"));
        clienti2.add(new Cliente("Giuseppe", "Bianchi", "Italiana", "RM", "Roma", "Via Nazionale", 45, 185,
                "3339876544", "M", LocalDate.of(1988, 11, 5), "BNCGPP88S05H501W", "giuseppe.bianchi@email.com" ));

        Trattamento trattamento2 = new Trattamento("Mezza Pensione", 45.0);

        Prenotazione p2 = new Prenotazione(2, LocalDate.of(2026, 1, 20), LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7), trattamento2, "Passaporto", LocalDate.of(2019, 8, 15),
                LocalDate.of(2029, 8, 15), "Laura Bianchi", "Anniversario di matrimonio", camere2, servizi2, clienti2, 987654321);

// PRENOTAZIONE 3
        ArrayList<Camera> camere3 = new ArrayList<>();
        camere3.add(new Camera(310, Stato.Prenotata, 2, 180.0, "Vista panoramica"));

        ArrayList<Servizio> servizi3 = new ArrayList<>();
        servizi3.add(new Servizio("Cena romantica", 80.0));

        ArrayList<Cliente> clienti3 = new ArrayList<>();
        clienti3.add(new Cliente("Anna", "Verdi", "Italiana", "MI", "Milano", "Corso Buenos Aires", 78, 20124,
                "3347891234", "F", LocalDate.of(1992, 4, 12), "VRDNNA92D52F205X", "anna.verdi@email.com"));

        Trattamento trattamento3 = new Trattamento("Pensione Completa", 65.0);

        Prenotazione p3 = new Prenotazione(3, LocalDate.of(2026, 1, 18), LocalDate.of(2026, 2, 14),
                LocalDate.of(2026, 2, 16), trattamento3, "Carta d'Identità", LocalDate.of(2021, 2, 20),
                LocalDate.of(2031, 2, 20), "Anna Verdi", "San Valentino", camere3, servizi3, clienti3, 456789123);

// PRENOTAZIONE 4
        ArrayList<Camera> camere4 = new ArrayList<>();
        camere4.add(new Camera(102, Stato.Prenotata, 3, 190.0, "Camera tripla standard"));

        ArrayList<Servizio> servizi4 = new ArrayList<>();
        servizi4.add(new Servizio("Parcheggio", 20.0));
        servizi4.add(new Servizio("Lavanderia", 25.0));

        ArrayList<Cliente> clienti4 = new ArrayList<>();
        clienti4.add(new Cliente("Francesco", "Neri", "Italiana", "TO", "Torino", "Via Po", 23, 10123,
                "3356781234", "M", LocalDate.of(1978, 9, 8), "NREFNC78P08L219Y", "francesco.neri@email.com"));
        clienti4.add(new Cliente("Giulia", "Neri", "Italiana", "TO", "Torino", "Via Po", 23, 10123,
                "3356781235", "F", LocalDate.of(1980, 12, 3), "NREGLI80T43L219K", "giulia.neri@email.com"));
        clienti4.add(new Cliente("Luca", "Neri", "Italiana", "TO", "Torino", "Via Po", 23, 10123,
                "3356781236", "M", LocalDate.of(2010, 5, 15), "NRELCU10E15L219M", "luca.neri@email.com"));

        Trattamento trattamento4 = new Trattamento("Bed & Breakfast", 25.0);

        Prenotazione p4 = new Prenotazione(4, LocalDate.of(2026, 1, 22), LocalDate.of(2026, 4, 5),
                LocalDate.of(2026, 4, 12), trattamento4, "Patente", LocalDate.of(2018, 6, 10),
                LocalDate.of(2028, 6, 10), "Francesco Neri", "Vacanza famiglia", camere4, servizi4, clienti4, 741852963);

// PRENOTAZIONE 5
        ArrayList<Camera> camere5 = new ArrayList<>();
        camere5.add(new Camera(401, Stato.Prenotata, 2, 250.0, "Suite deluxe"));

        ArrayList<Servizio> servizi5 = new ArrayList<>();
        servizi5.add(new Servizio("Room Service", 40.0));
        servizi5.add(new Servizio("Transfer aeroporto", 60.0));

        ArrayList<Cliente> clienti5 = new ArrayList<>();
        clienti5.add(new Cliente("Roberto", "Gialli", "Italiana", "FI", "Firenze", "Via dei Calzaiuoli", 15, 50122,
                "3312345678", "M", LocalDate.of(1975, 1, 25), "GLLRRT75A25D612P", "roberto.gialli@email.com"));

        Trattamento trattamento5 = new Trattamento("All Inclusive", 95.0);

        Prenotazione p5 = new Prenotazione(5, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 5, 20),
                LocalDate.of(2026, 5, 27), trattamento5, "Passaporto", LocalDate.of(2022, 3, 5),
                LocalDate.of(2032, 3, 5), "Roberto Gialli", "Viaggio d'affari", camere5, servizi5, clienti5, 159753486);

// PRENOTAZIONE 6
        ArrayList<Camera> camere6 = new ArrayList<>();
        camere6.add(new Camera(150, Stato.Prenotata, 2, 165.0, "Camera matrimoniale"));

        ArrayList<Servizio> servizi6 = new ArrayList<>();
        servizi6.add(new Servizio("Palestra", 15.0));

        ArrayList<Cliente> clienti6 = new ArrayList<>();
        clienti6.add(new Cliente("Valentina", "Blu", "Italiana", "BO", "Bologna", "Via Indipendenza", 88, 40121,
                "3398765432", "F", LocalDate.of(1995, 6, 30), "BLUVNT95H70A944L", "valentina.blu@email.com"));

        Trattamento trattamento6 = new Trattamento("Solo Pernottamento", 0.0);

        Prenotazione p6 = new Prenotazione(6, LocalDate.of(2026, 1, 25), LocalDate.of(2026, 3, 15),
                LocalDate.of(2026, 3, 18), trattamento6, "Carta d'Identità", LocalDate.of(2023, 9, 12),
                LocalDate.of(2033, 9, 12), "Valentina Blu", "Weekend relax", camere6, servizi6, clienti6, 852963741);

// PRENOTAZIONE 7
        ArrayList<Camera> camere7 = new ArrayList<>();
        camere7.add(new Camera(201, Stato.Prenotata, 2, 200.0, "Vista città"));
        camere7.add(new Camera(202, Stato.Prenotata, 2, 200.0, "Vista città"));

        ArrayList<Servizio> servizi7 = new ArrayList<>();
        servizi7.add(new Servizio("Escursione guidata", 70.0));

        ArrayList<Cliente> clienti7 = new ArrayList<>();
        clienti7.add(new Cliente("Marco", "Viola", "Italiana", "VE", "Venezia", "Calle Lunga", 33, 30100,
                "3345678901", "M", LocalDate.of(1987, 8, 18), "VLAMRC87M18L736N", "marco.viola@email.com"));
        clienti7.add(new Cliente("Stefania", "Rosa", "Italiana", "VE", "Venezia", "Calle Corta", 12, 30100,
                "3345678902", "F", LocalDate.of(1989, 2, 14), "RSOSFN89B54L736R", "stefania.rosa@email.com"));

        Trattamento trattamento7 = new Trattamento("Mezza Pensione", 45.0);

        Prenotazione p7 = new Prenotazione(7, LocalDate.of(2026, 1, 12), LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 8), trattamento7, "Passaporto", LocalDate.of(2020, 11, 22),
                LocalDate.of(2030, 11, 22), "Marco Viola", "Viaggio di gruppo", camere7, servizi7, clienti7, 369258147);

// PRENOTAZIONE 8
        ArrayList<Camera> camere8 = new ArrayList<>();
        camere8.add(new Camera(305, Stato.Prenotata, 1, 120.0, "Camera singola economica"));

        ArrayList<Servizio> servizi8 = new ArrayList<>();

        ArrayList<Cliente> clienti8 = new ArrayList<>();
        clienti8.add(new Cliente("Alessio", "Arancio", "Italiana", "GE", "Genova", "Via XX Settembre", 67, 16121,
                "3387654321", "M", LocalDate.of(1993, 10, 5), "RNCLS93R05D969Q", "alessio.arancio@email.com"));

        Trattamento trattamento8 = new Trattamento("Solo Pernottamento", 0.0);

        Prenotazione p8 = new Prenotazione(8, LocalDate.of(2026, 1, 28), LocalDate.of(2026, 2, 20),
                LocalDate.of(2026, 2, 22), trattamento8, "Carta d'Identità", LocalDate.of(2024, 1, 8),
                LocalDate.of(2034, 1, 8), "Alessio Arancio", "Breve sosta", camere8, servizi8, clienti8, 258369147);

// PRENOTAZIONE 9
        ArrayList<Camera> camere9 = new ArrayList<>();
        camere9.add(new Camera(450, Stato.Prenotata, 4, 280.0, "Suite presidenziale"));

        ArrayList<Servizio> servizi9 = new ArrayList<>();
        servizi9.add(new Servizio("Butler service", 100.0));
        servizi9.add(new Servizio("Champagne", 120.0));
        servizi9.add(new Servizio("Massaggio", 90.0));

        ArrayList<Cliente> clienti9 = new ArrayList<>();
        clienti9.add(new Cliente("Davide", "Marrone", "Italiana", "PA", "Palermo", "Via Maqueda", 150, 90133,
                "3321234567", "M", LocalDate.of(1982, 3, 28), "MRRDVD82C28G273S", "davide.marrone@email.com"));
        clienti9.add(new Cliente("Elisa", "Marrone", "Italiana", "PA", "Palermo", "Via Maqueda", 150, 90133,
                "3321234568", "F", LocalDate.of(1984, 7, 11), "MRRLS84L51G273V", "elisa.marrone@email.com"));

        Trattamento trattamento9 = new Trattamento("All Inclusive", 95.0);

        Prenotazione p9 = new Prenotazione(9, LocalDate.of(2026, 1, 5), LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 20), trattamento9, "Passaporto", LocalDate.of(2021, 4, 15),
                LocalDate.of(2031, 4, 15), "Davide Marrone", "Luna di miele", camere9, servizi9, clienti9, 147258369);

// PRENOTAZIONE 10
        ArrayList<Camera> camere10 = new ArrayList<>();
        camere10.add(new Camera(120, Stato.Prenotata, 2, 175.0, "Camera comfort"));

        ArrayList<Servizio> servizi10 = new ArrayList<>();
        servizi10.add(new Servizio("Late check-out", 35.0));
        servizi10.add(new Servizio("Noleggio bici", 25.0));

        ArrayList<Cliente> clienti10 = new ArrayList<>();
        clienti10.add(new Cliente("Chiara", "Grigio", "Italiana", "SA", "Salerno", "Corso Vittorio Emanuele", 200, 84100,
                "3369871234", "F", LocalDate.of(1991, 11, 22), "GRGCHR91S62H703B", "chiara.grigio@email.com"));

        Trattamento trattamento10 = new Trattamento("Bed & Breakfast", 25.0);

        Prenotazione p10 = new Prenotazione(10, LocalDate.of(2026, 1, 30), LocalDate.of(2026, 4, 25),
                LocalDate.of(2026, 4, 28), trattamento10, "Patente", LocalDate.of(2022, 8, 30),
                LocalDate.of(2032, 8, 30), "Chiara Grigio", "Weekend mare", camere10, servizi10, clienti10, 963852741);

        Prenotazione[] prenotaziones = new Prenotazione[10];
        prenotaziones[0] = p1;
        prenotaziones[1] = p2;
        prenotaziones[2] = p3;
        prenotaziones[3] = p4;
        prenotaziones[4] = p5;
        prenotaziones[5] = p6;
        prenotaziones[6] = p7;
        prenotaziones[7] = p8;
        prenotaziones[8] = p9;
        prenotaziones[9] = p10;

        assertArrayEquals(prenotaziones, prenotazioneDAO.doRetriveAll("").toArray());
    }
}