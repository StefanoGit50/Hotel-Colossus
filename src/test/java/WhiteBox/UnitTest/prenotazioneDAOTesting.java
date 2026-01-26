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
    @InjectMocks
    private PrenotazioneDAO prenotazioneDAO;

    private MockedStatic<ConnectionStorage> connectionStorageMockedStatic;

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Stefano","Santoro","Italiana","Salerno","mercato san severino","Corso Armando Diaz",10,84085,"3249554018","Maschio",LocalDate.of(2004,12, 16),"SNTSFN04T16H703T","stefano.santoro.2004@gmail.com"));
        clientes.add(new Cliente("Andrea","Squitieri","Italiana","Avvelino","Tufino","Via Roma",13,80030,"3567893421","Maschio",LocalDate.of(2004,11,30),"SNTSFN04T16H703T","andrea.Squitieri.2002@gmail.com"));

        cameras.add(new Camera(10,Stato.Libera,2,20.0,"Stefano è celiaco"));
        cameras.add(new Camera(11,Stato.Libera,3,40.0,"Squittiri è allergico alle noci"));

        servizios.add(new Servizio("Piscina",30.0));
        servizios.add(new Servizio("Palestra",35.0));
        prenotazione = new Prenotazione(1,LocalDate.of(2003,12,11),LocalDate.of(2004,03,12),LocalDate.of(2004,05,12),new Trattamento("Mezza Pensione",40.0),"Patente", LocalDate.of(1998 , 12 ,11), LocalDate.of(2008,12,11),"Stefano Santoro","",cameras , servizios , clientes , 112 , true , false);
        connectionStorageMockedStatic = mockStatic(ConnectionStorage.class);
        connectionStorageMockedStatic.when(ConnectionStorage::getConnection).thenReturn(connection);
    }

    @AfterEach
    public void setAfter(){
        connectionStorageMockedStatic.close();
    }

    @Test
    public void doSave(){

    }

}