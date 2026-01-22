package WhiteBox.TestingDB.PrenotazioneDAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import javafx.scene.chart.PieChart;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
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
    @Mock
    private static Prenotazione prenotazione;
    @InjectMocks
    private static PrenotazioneDAO prenotazioneDAO;

    @Test
    public void doSaveAllTrue() throws SQLException{
        when(source.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO prenotazione " +
                "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?, ?) ")).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement1);
        when(connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?")).thenReturn(preparedStatement2);
        when(connection.prepareStatement( "INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)")).thenReturn(preparedStatement3);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement1.executeUpdate()).thenReturn(1);
        when(preparedStatement2.executeUpdate()).thenReturn(1);
        when(preparedStatement3.executeUpdate()).thenReturn(1);
        when(preparedStatement4.executeUpdate()).thenReturn(1);

        when(prenotazione.getDataScadenza()).thenReturn(LocalDate.of(2004,12,16));
        when(prenotazione.getDataInizio()).thenReturn(LocalDate.of(1990,11,19));
        when(prenotazione.getDataCreazionePrenotazione()).thenReturn(LocalDate.of(2013,11,20));
        when(prenotazione.getDataFine()).thenReturn(LocalDate.of(2012,11,20));
        when(prenotazione.getDataRilascio()).thenReturn(LocalDate.of(2003,12,14));
        when(prenotazione.getTrattamento()).thenReturn(new Trattamento("In Camera",30));
        when(prenotazione.getNoteAggiuntive()).thenReturn("");
        when(prenotazione.getIntestatario()).thenReturn("Mario Masceri");
        when(prenotazione.getStatoPrenotazione()).thenReturn(true);
        when(prenotazione.getTipoDocumento()).thenReturn("Carta D'Identità");
        when(prenotazione.isCheckIn()).thenReturn(false);
        when(prenotazione.getNumeroDocumento()).thenReturn(112);
        when(prenotazione.getIDPrenotazione()).thenReturn(1);

        connection = source.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO prenotazione " + "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?, ?) ");
        int e = preparedStatement.executeUpdate();

        preparedStatement1 = connection.prepareStatement("UPDATE Trattamento SET IDPrenotazione = ? WHERE Nome = ?");
        int y = preparedStatement1.executeUpdate();

        preparedStatement2 = connection.prepareStatement("UPDATE Servizio SET IDPrenotazione = ? WHERE Nome = ?");
        int z = preparedStatement2.executeUpdate();

        preparedStatement3 = connection.prepareStatement("INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) " +
                "VALUES (?, ?, ?, ?)");
        int o = preparedStatement3.executeUpdate();

       prenotazioneDAO.doSave(prenotazione);

        assertEquals(e,y);
        assertEquals(e,z);
        assertEquals(e,o);

    }

}