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

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();

        prenotazione = new Prenotazione();

    }

}