package WhiteBox.TestingDB.RicevutaFiscaleDAO;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.RicevutaFiscaleDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RicevutaFiscaleDAOTesting{

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private PreparedStatement preparedStatement1;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet resultSet1;

    private MockedStatic<ConnectionStorage> connectionSM;

    private RicevutaFiscale ricevutaFiscale;

    @InjectMocks
    private RicevutaFiscaleDAO ricevutaFiscaleDAO;

    @BeforeEach
    public void setUp(){
        ricevutaFiscale = new RicevutaFiscale(12,1,30.0, LocalDate.of(2006,12,20),"Carta di credito" , LocalDate.of(2009,11,30) , 25 , "sanificazione");
        connectionSM = mockStatic(ConnectionStorage.class);
    }
    @AfterEach
    public void setAfter(){
        connectionSM.close();
    }

    @Test
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("insert into RicevutaFiscale " +
                    "VALUES (?,?,?,?,?,?,?)")).thenReturn(preparedStatement);

            when(preparedStatement.executeUpdate()).thenReturn(1);

            ricevutaFiscaleDAO.doSave(ricevutaFiscale);

    }

    @Test
    @DisplayName("doDelete() quando fa l'eccezione")
    public void doDeleteAllTrue() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
            when(connection.prepareStatement("delete from ricevutafiscale" +
                    " where IDRicevutaFiscale = ? and IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);
            ricevutaFiscaleDAO.doDelete(ricevutaFiscale);
    }
    @Test
    @DisplayName("doRetriveByKey() quando è tutto vero")
    public void doRetriveByKeyAlltrue() throws SQLException {

            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("select * FROM ricevutafiscale" +
                    " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(connection.prepareStatement("SELECT PrezzoCamera FROM (ricevutafiscale join hotelcolossus.cameraricevuta c on ricevutafiscale.IDRicevutaFiscale = c.IDRicevutaFiscale) where c.IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
            when(preparedStatement1.executeQuery()).thenReturn(resultSet);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true,true,false);
            when(resultSet.getInt("IDPrenotazione")).thenReturn(2);
            when(resultSet.getInt("IDRicevutaFiscale")).thenReturn(4);
            when(resultSet.getDate("DataPrenotazione")).thenReturn(Date.valueOf(LocalDate.of(2007,12,11)));
            when(resultSet.getDate("DataEmissione")).thenReturn(Date.valueOf(LocalDate.of(2008,12,11)));
            when(resultSet.getString("TipoTrattamento")).thenReturn("pernottamento e colazione");
            when(resultSet.getDouble("PrezzoTrattamento")).thenReturn(40.0);
            when(resultSet.getString("metodoDiPagamento")).thenReturn("Carta D'identità");
            when(resultSet.getDouble("PrezzoCamera")).thenReturn(22.90);


            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(4);
            arrayList.add(3);

            ricevutaFiscaleDAO.doRetriveByKey(arrayList);
    }
    @Test
    @DisplayName("doRetiveByKey() quando ResultSet è false")
    public void doRetriveByKeyResultSetFalse() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("select * FROM ricevutafiscale" +
                    " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(4);
            arrayList.add(3);
            assertThrows(NoSuchElementException.class,()->ricevutaFiscaleDAO.doRetriveByKey(arrayList));
    }

    @Test
    @DisplayName("doRetriveByKey() quando il primo ResultSet va e il secondo No")
    public void doRetriveByKeyResultSetSecondoFalse() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
            when(connection.prepareStatement("select * FROM ricevutafiscale" +
                    " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(connection.prepareStatement("SELECT PrezzoCamera FROM (ricevutafiscale join hotelcolossus.cameraricevuta c on ricevutafiscale.IDRicevutaFiscale = c.IDRicevutaFiscale) where c.IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
            when(preparedStatement1.executeQuery()).thenReturn(resultSet);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true,true,false);
            when(resultSet1.next()).thenReturn(false);
            when(resultSet.getInt("IDPrenotazione")).thenReturn(2);
            when(resultSet.getInt("IDRicevutaFiscale")).thenReturn(4);
            when(resultSet.getDate("DataPrenotazione")).thenReturn(Date.valueOf(LocalDate.of(2007,12,11)));
            when(resultSet.getDate("DataEmissione")).thenReturn(Date.valueOf(LocalDate.of(2008,12,11)));
            when(resultSet.getString("TipoTrattamento")).thenReturn("pernottamento e colazione");
            when(resultSet.getDouble("PrezzoTrattamento")).thenReturn(40.0);
            when(resultSet.getString("metodoDiPagamento")).thenReturn("Carta D'identità");
            when(resultSet.getDouble("PrezzoCamera")).thenReturn(22.90);

            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(4);
            arrayList.add(3);
            ricevutaFiscaleDAO.doRetriveByKey(arrayList);
    }

    @Test
    @DisplayName("doRetriveAll() ")
    public void doRetriveAllAllTrue(){

    }

}
