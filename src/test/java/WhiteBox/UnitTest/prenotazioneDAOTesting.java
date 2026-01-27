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
        clientes.add(new Cliente("Stefano","Santoro","Italiana","Salerno","mercato san severino","Corso Armando Diaz",10,84085,"3249554018","Maschio",LocalDate.of(2004,12, 16),"SNTSFN04T16H703T","stefano.santoro.2004@gmail.com"));

        cameras.add(new Camera(10,Stato.Libera,2,20.0,"Stefano è celiaco"));

        servizios.add(new Servizio("Piscina",30.0));

        prenotazione = new Prenotazione(1,LocalDate.of(2003,12,11),LocalDate.of(2004,03,12),LocalDate.of(2004,05,12),new Trattamento("Mezza Pensione",40.0),"Carta D'identità", LocalDate.of(1998 , 12 ,11), LocalDate.of(2008,12,11),"Stefano Santoro","",cameras , servizios , clientes , "CA12C459" , true , false);
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
        when(connection.prepareStatement(contains("CLIENTE"))).thenReturn(preparedStatement);
        when(connection.prepareStatement(contains("Trattamento"))).thenReturn(preparedStatement1);
        when(connection.prepareStatement(contains("Servizio"))).thenReturn(preparedStatement2);
        when(connection.prepareStatement(contains("Associato_a"))).thenReturn(preparedStatement3);
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
        doNothing().when(clienteDAO).doSave(cliente);

        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

}