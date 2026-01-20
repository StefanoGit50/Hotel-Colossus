package WhiteBox.TestingDB.PrenotazioneDAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class prenotazioneDAOTesting {
    @Mock
    private static DataSource source;
    private static Trattamento trattamento;
    private static Servizio servizio;
    @Mock
    private static Connection connection;
    @Mock
    private static ResultSet resultSet;
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
    private static Prenotazione prenotazione;
    @InjectMocks
    private static PrenotazioneDAO prenotazioneDAO;


    @BeforeEach
    public void setUp() throws SQLException {
        prenotazione = new Prenotazione();
        trattamento = new Trattamento("D'asporto",22);
        ArrayList<Servizio> servizios = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        servizios.add(new Servizio("Mensa",11));
        servizios.add(new Servizio("Piscina",12));
        servizios.add(new Servizio("ijjknk",15));
        clientes.add(new Cliente("Mario","Masceri","Italiana","Salerno","Mercato San Severino","Corso Armando diaz" , 12 , 84085 , "3249552034" , "Maschio" , Date.valueOf("1899-12-04").toLocalDate(),"MSCMRA00T04H703M","Mario.masceri@gmail.com","Bancomat"));
        clientes.add(new Cliente("Mario","Masceri","Italiana","Salerno","Mercato San Severino","Corso Armando diaz" , 12 , 84085 , "3249552034" , "Maschio" , Date.valueOf("1899-12-04").toLocalDate(),"MSCMRA00T04H703H","Mario.masceri@gmail.com","carta"));
        cameras.add(new Camera(101, Stato.Libera,2,120,"Non ce la faccio piu"));
        cameras.add(new Camera(102, Stato.Libera,3,110,"Non ce la faccio piu"));
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

    @Test
    public void doSaveAllTrue() throws SQLException{
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

        assertNotNull(prenotazione.getTrattamento());
        assertNotNull(prenotazione.getListaServizi());
        assertNotEquals(0, prenotazione.getListaServizi().size());
        assertNotNull(prenotazione.getListaCamere());
        assertNotEquals(0,prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertNotEquals(0,prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

       connection =  source.getConnection();
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
        verify(preparedStatement1,times(1)).executeUpdate();
        verify(preparedStatement2,times(1)).executeUpdate();
        verify(preparedStatement3,times(4)).executeUpdate();
    }

    @Test
    public void doSaveException() throws SQLException{
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
        assertEquals(0,prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0,prenotazione.getListaClienti().size());

        assertThrows(Exception.class,()->{prenotazioneDAO.doSave(prenotazione);});

        connection =  source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        verify(preparedStatement, never()).executeUpdate();
        verify(preparedStatement1,never()).executeUpdate();
        verify(preparedStatement2,never()).executeUpdate();
        verify(preparedStatement3,never()).executeUpdate();
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
        assertEquals(0,prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0,prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

        connection =  source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
                "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
                "numeroDocumento, DataRilascio, TipoDocumento,NomeTrattamento,Stato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ?) ");
        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");

        preparedStatement.executeUpdate();

        verify(preparedStatement,times(1)).executeUpdate();
        verify(preparedStatement1,never()).executeUpdate();
        verify(preparedStatement2,never()).executeUpdate();
        verify(preparedStatement3,never()).executeUpdate();
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
        assertNotEquals(0,prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertEquals(0,prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

        connection =  source.getConnection();
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

        verify(preparedStatement,times(1)).executeUpdate();
        verify(preparedStatement1,times(1)).executeUpdate();
        verify(preparedStatement2,times(1)).executeUpdate();
        verify(preparedStatement3,never()).executeUpdate();
    }

    @Test
    public void doDeleteException() throws SQLException{
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);

        prenotazione.setIDPrenotazione(null);

        assertThrows(NullPointerException.class , ()->{prenotazioneDAO.doDelete(prenotazione);});

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?");

        verify(preparedStatement,never()).executeUpdate();
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

        verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    public void doRetryByKeyNull() throws SQLException {
       prenotazione = prenotazioneDAO.doRetriveByKey("ciao");
        assertNull(prenotazione);
    }

    @Test
    public void doRetryByKeyResultSetFalse()throws SQLException{
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class,()-> {prenotazioneDAO.doRetriveByKey(6);});

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?");
        resultSet = preparedStatement.executeQuery();
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
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?");
        preparedStatement1 = connection.prepareStatement("SELECT * FROM Trattamento WHERE IDPrenotazione = ?");
        resultSet = preparedStatement.executeQuery();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("John","Smith","Inglese","Estero","Londra","Baker Street",45,00121,"447123456","Maschio",Date.valueOf("1975-04-15").toLocalDate(),"SMIJHN75D15Z114B","john.smith@uk.com","Carta di Credito"));
        servizios.add(new Servizio("Transfer Aeroporto",40));
        cameras.add(new Camera(202,Stato.InPulizia,1,85,"Singola business"));

        assertEquals(new Prenotazione(4,Date.valueOf("2023-11-15").toLocalDate(),Date.valueOf("2024-02-14").toLocalDate(),Date.valueOf("2024-02-16").toLocalDate(),new Trattamento("Massaggio Sportivo",70),"Passaporto",Date.valueOf("2022-12-10").toLocalDate(),Date.valueOf("2032-12-10").toLocalDate(),"John Smith","Anniversario",cameras,servizios,clientes,901234),prenotazioneDAO.doRetriveByKey(4));



    }
}