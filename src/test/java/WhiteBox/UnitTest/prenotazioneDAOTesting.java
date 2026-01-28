package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.lang.invoke.SwitchPoint;
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
    private  DataSource source;
    private  Trattamento trattamento;
    private Servizio servizio;
    @Mock
    private Connection connection;
    private String sql = "INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, " +
            "DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, " +
            "numeroDocumento, DataRilascio, TipoDocumento) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private PreparedStatement preparedStatement1;
    @Mock
    private PreparedStatement preparedStatement2;
    @Mock
    private PreparedStatement preparedStatement3;
    @Mock
    private PreparedStatement preparedStatement4;
    @Mock
    private ResultSet resultSet;
    @Mock
    private ResultSet resultSet1;
    @Mock
    private ResultSet resultSet2;
    @Mock
    private ResultSet resultSet3;
    @Mock
    private ResultSet resultSet4;
    private Prenotazione prenotazione;

    @Mock
    private ClienteDAO clienteDAO;
    @Mock
    private Cliente cliente;
    @InjectMocks
    private PrenotazioneDAO prenotazioneDAO;

    private MockedStatic<ConnectionStorage> connectionStorageMockedStatic;

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Stefano","Santoro","Italiana","Salerno","mercato san severino","Corso Armando Diaz",10,84085,"3249554018","Maschio",LocalDate.of(2004,12, 16),"SNTSFN04T16H703T","stefano.santoro.2004@gmail.com","Italiana"));

        cameras.add(new Camera(10,Stato.Libera,2,20.0,"Stefano è celiaco"));

        servizios.add(new Servizio("Piscina",30.0));

        prenotazione = new Prenotazione(1,LocalDate.of(2003,12,11),LocalDate.of(2004,3,12),LocalDate.of(2004,5,12),new Trattamento("Mezza Pensione",40.0),"Carta D'identità", LocalDate.of(1998 , 12 ,11), LocalDate.of(2008,12,11),"Stefano Santoro","",cameras , servizios , clientes , "CA12C459" , true , false);
        connectionStorageMockedStatic = mockStatic(ConnectionStorage.class);
        connectionStorageMockedStatic.when(ConnectionStorage::getConnection).thenReturn(connection);
    }

    @AfterEach
    public void setAfter(){
        connectionStorageMockedStatic.close();
    }

    @Test
    @Tag("True")
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
        prenotazioneDAO = new PrenotazioneDAO(clienteDAO);
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?)")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        doNothing().when(preparedStatement).setDate(1,Date.valueOf(LocalDate.of(2003,12,11)));
        doNothing().when(preparedStatement).setDate(2,Date.valueOf(LocalDate.of(2004,3,12)));
        doNothing().when(preparedStatement).setDate(3,Date.valueOf(LocalDate.of(2004,5,12)));
        doNothing().when(preparedStatement).setString(10,"Mezza Pensione");
        doNothing().when(preparedStatement).setString(4,"");
        doNothing().when(preparedStatement).setString(5,"Stefano Santoro");
        doNothing().when(preparedStatement).setDate(6,Date.valueOf(LocalDate.of(2008,12,11)));
        doNothing().when(preparedStatement).setString(7,"CA12C459");
        doNothing().when(preparedStatement).setDate(8,Date.valueOf(LocalDate.of(1998,12,11)));
        doNothing().when(preparedStatement).setString(9,"Carta D'identità");
        doNothing().when(preparedStatement).setBoolean(11,true);
        doNothing().when(preparedStatement).setBoolean(12,false);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        doNothing().when(preparedStatement1).setInt(1,1);
        doNothing().when(preparedStatement1).setString(2,"Mezza Pensione");
        when(preparedStatement1.executeUpdate()).thenReturn(1);
        doNothing().when(preparedStatement2).setInt(1,1);
        doNothing().when(preparedStatement2).setString(2,"Piscina");
        when(preparedStatement2.executeUpdate()).thenReturn(1);
        doNothing().when(preparedStatement3).setString(1,"SNTSFN04T16H703T");
        doNothing().when(preparedStatement3).setInt(2,10);
        doNothing().when(preparedStatement3).setInt(3,1);
        doNothing().when(preparedStatement3).setDouble(4,20.0);
        when(preparedStatement3.executeUpdate()).thenReturn(1);
        doNothing().when(clienteDAO).doSave(any(Cliente.class));

        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }
    @Test
    @Tag("False")
    @DisplayName("doSave() quando è tutto False")
    public void doSaveAllFalse() throws SQLException{
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?)")).thenReturn(preparedStatement);

        doNothing().when(preparedStatement).setDate(1,Date.valueOf(LocalDate.of(2003,12,11)));
        doNothing().when(preparedStatement).setDate(2,Date.valueOf(LocalDate.of(2004,3,12)));
        doNothing().when(preparedStatement).setDate(3,Date.valueOf(LocalDate.of(2004,5,12)));
        doNothing().when(preparedStatement).setString(10,null);
        doNothing().when(preparedStatement).setString(4,"");
        doNothing().when(preparedStatement).setString(5,"Stefano Santoro");
        doNothing().when(preparedStatement).setDate(6,Date.valueOf(LocalDate.of(2008,12,11)));
        doNothing().when(preparedStatement).setString(7,"CA12C459");
        doNothing().when(preparedStatement).setDate(8,Date.valueOf(LocalDate.of(1998,12,11)));
        doNothing().when(preparedStatement).setString(9,"Carta D'identità");
        doNothing().when(preparedStatement).setBoolean(11,true);
        doNothing().when(preparedStatement).setBoolean(12,false);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        prenotazione.setTrattamento(new Trattamento());
        prenotazione.setListaServizi(new ArrayList<>());
        prenotazione.setListaCamere(new ArrayList<>());
        prenotazione.setListaClienti(new ArrayList<>());

        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doSave() quando tira una eccezione")
    public void doSaveException(){
        assertThrows(SQLException.class,()->prenotazioneDAO.doSave(null));
    }

    @Test
    @Tag("False")
    @DisplayName("doSave() quando la condizione del for del cliente è falsa")
    public void doSaveSecondoForCliente() throws SQLException {
        when(connection.prepareStatement("INSERT INTO prenotazione(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?)")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        doNothing().when(preparedStatement).setDate(1,Date.valueOf(LocalDate.of(2003,12,11)));
        doNothing().when(preparedStatement).setDate(2,Date.valueOf(LocalDate.of(2004,3,12)));
        doNothing().when(preparedStatement).setDate(3,Date.valueOf(LocalDate.of(2004,5,12)));
        doNothing().when(preparedStatement).setString(10,"Mezza Pensione");
        doNothing().when(preparedStatement).setString(4,"");
        doNothing().when(preparedStatement).setString(5,"Stefano Santoro");
        doNothing().when(preparedStatement).setDate(6,Date.valueOf(LocalDate.of(2008,12,11)));
        doNothing().when(preparedStatement).setString(7,"CA12C459");
        doNothing().when(preparedStatement).setDate(8,Date.valueOf(LocalDate.of(1998,12,11)));
        doNothing().when(preparedStatement).setString(9,"Carta D'identità");
        doNothing().when(preparedStatement).setBoolean(11,true);
        doNothing().when(preparedStatement).setBoolean(12,false);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        doNothing().when(preparedStatement1).setInt(1,1);
        doNothing().when(preparedStatement1).setString(2,"Mezza Pensione");
        when(preparedStatement1.executeUpdate()).thenReturn(1);

        doNothing().when(preparedStatement2).setInt(1,1);
        doNothing().when(preparedStatement2).setString(2,"Piscina");
        when(preparedStatement2.executeUpdate()).thenReturn(1);

        prenotazione.setListaClienti(new ArrayList<>());

        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }
    @Test
    @Tag("True")
    @DisplayName("doDelete() quando è tutto vero")
    public void doDeleteAllTrue() throws SQLException {
        when(connection.prepareStatement("DELETE FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1,1);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(()->prenotazioneDAO.doDelete(prenotazione));
    }

    @Test
    @Tag("False")
    @DisplayName("doDelete() quando è tutto falso")
    public void doDeleteAllFalse() throws SQLException{
        assertDoesNotThrow(()->prenotazioneDAO.doDelete(null));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByKey() quando è tutto true")
    public void doRetriveByKeyAllTrue() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT * FROM Trattamento WHERE IDPrenotazione = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("SELECT * FROM Servizio WHERE IDPrenotazione = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement("SELECT DISTINCT c.* FROM Camera c " +
                "JOIN Associato_a a ON c.NumeroCamera = a.NumeroCamera " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement3);
        when(connection.prepareStatement("SELECT DISTINCT cl.* FROM Cliente cl " +
                "JOIN Associato_a a ON cl.CF = a.CF " +
                "WHERE a.IDPrenotazione = ?")).thenReturn(preparedStatement4);
       doNothing().when(preparedStatement).setInt(1,1);
       when(preparedStatement.executeQuery()).thenReturn(resultSet);
       when(resultSet.next()).thenReturn(true);
       when(resultSet.getInt("IDPrenotazione")).thenReturn(1);
       when(resultSet.getDate("DataPrenotazione")).thenReturn(Date.valueOf("2003-12-11"));
       when(resultSet.getDate("DataArrivoCliente")).thenReturn(Date.valueOf("2004-3-12"));
       when(resultSet.getDate("DataPartenzaCliente")).thenReturn(Date.valueOf("2004-5-12"));
       when(resultSet.getString("TipoDocumento")).thenReturn("Carta D'identità");
       when(resultSet.getDate("DataRilascio")).thenReturn(Date.valueOf("1998-12-11"));
       when(resultSet.getDate("dataScadenza")).thenReturn(Date.valueOf("2008-12-11"));
       when(resultSet.getString("Intestatario")).thenReturn("Stefano Santoro");
       when(resultSet.getString("NoteAggiuntive")).thenReturn("");
       when(resultSet.getString("numeroDocumento")).thenReturn("CA12C459");
       when(resultSet.getBoolean("Stato")).thenReturn(true);
       when(resultSet.getBoolean("CheckIn")).thenReturn(false);

       doNothing().when(preparedStatement1).setInt(1,1);
       when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
       when(resultSet1.next()).thenReturn(true);
       when(resultSet1.getString("Nome")).thenReturn("Mezza Pensione");
       when(resultSet1.getDouble("Prezzo")).thenReturn(40.0);

       doNothing().when(preparedStatement2).setInt(1,1);
       when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
       when(resultSet2.next()).thenReturn(true,true ,false);
       when(resultSet2.getString("Nome")).thenReturn("Piscina");
       when(resultSet2.getDouble("Prezzo")).thenReturn(30.0);

       doNothing().when(preparedStatement3).setInt(1,1);
       when(preparedStatement3.executeQuery()).thenReturn(resultSet3);
       when(resultSet3.next()).thenReturn(true , false);
       when(resultSet3.getInt("NumeroCamera")).thenReturn(10);
       when(resultSet3.getString("Stato")).thenReturn(Stato.Libera.name());
       when(resultSet3.getInt("NumeroMaxOcc")).thenReturn(2);
       when(resultSet3.getDouble("Prezzo")).thenReturn(20.0);
       when(resultSet3.getString("NoteCamera")).thenReturn("Stefano è celiaco");

       doNothing().when(preparedStatement4).setInt(1,1);
       when(preparedStatement4.executeQuery()).thenReturn(resultSet4);
       when(resultSet4.next()).thenReturn(true , false);
       when(resultSet4.getString("nome")).thenReturn("Stefano");
       when(resultSet4.getString("cognome")).thenReturn("Santoro");
       when(resultSet4.getString("Cittadinanza")).thenReturn("Italiana");
       when(resultSet4.getString("provincia")).thenReturn("Salerno");
       when(resultSet4.getString("comune")).thenReturn("mercato san severino");
       when(resultSet4.getString("via")).thenReturn("Corso Armando Diaz");
       when(resultSet4.getInt("civico")).thenReturn(10);
       when(resultSet4.getInt("Cap")).thenReturn(84085);
       when(resultSet4.getString("telefono")).thenReturn("3249554018");
       when(resultSet4.getString("Sesso")).thenReturn("Maschio");
       when(resultSet4.getDate("DataDiNascita")).thenReturn(Date.valueOf("2004-12-16"));
       when(resultSet4.getString("CF")).thenReturn("SNTSFN04T16H703T");
       when(resultSet4.getString("Email")).thenReturn("stefano.santoro.2004@gmail.com");
       when(resultSet4.getString("Nazionalità")).thenReturn("Italiana");

       ArrayList<Servizio> servizios = new ArrayList<>();
       servizios.add(new Servizio("Piscina",30.0));
       servizios.add(new Servizio("Piscina",30.0));

       prenotazione.setListaServizi(servizios);
       assertEquals(prenotazione,prenotazioneDAO.doRetriveByKey(1));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando va in errore")
    public void doRetriveByKey() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM Prenotazione WHERE IDPrenotazione = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1,1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class,()->prenotazioneDAO.doRetriveByKey(1));
    }

    public void doRetriveByKeyAllFalseTranne(){

    }

}