package WhiteBox.TestingDB.PrenotazioneDAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PrenotazioneDAOTesting{
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
        prenotazione.setIDPrenotazione(12);
    }

    @Test
    public void doSaveAllTrue() throws SQLException{
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertNotNull(prenotazione.getTrattamento());
        assertNotNull(prenotazione.getListaServizi());
        assertNotEquals(0, prenotazione.getListaServizi().size());
        assertNotNull(prenotazione.getListaCamere());
        assertNotEquals(0,prenotazione.getListaCamere().size());
        assertNotNull(prenotazione.getListaClienti());
        assertNotEquals(0,prenotazione.getListaClienti().size());

        prenotazioneDAO.doSave(prenotazione);

        verify(preparedStatement.executeUpdate(), times(1));
    }

}