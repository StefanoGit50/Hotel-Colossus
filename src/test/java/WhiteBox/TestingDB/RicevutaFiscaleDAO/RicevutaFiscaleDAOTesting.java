package WhiteBox.TestingDB.RicevutaFiscaleDAO;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.RicevutaFiscaleDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
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
    @Tag("True")
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("insert into RicevutaFiscale " +
                    "VALUES (?,?,?,?,?,?,?)")).thenReturn(preparedStatement);

            when(preparedStatement.executeUpdate()).thenReturn(1);

            ricevutaFiscaleDAO.doSave(ricevutaFiscale);

    }

    @Test
    @Tag("Exception")
    @DisplayName("doDelete() quando fa l'eccezione")
    public void doDeleteAllTrue() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
            when(connection.prepareStatement("delete from ricevutafiscale" +
                    " where IDRicevutaFiscale = ? and IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);
            ricevutaFiscaleDAO.doDelete(ricevutaFiscale);
    }
    @Test
    @Tag("True")
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

           assertEquals(new RicevutaFiscale(4,2,62.90,LocalDate.of(2008,12,11),"Carta D'identità",LocalDate.of(2007,12,11),40.0,"pernottamento e colazione"),ricevutaFiscaleDAO.doRetriveByKey(arrayList));
    }
    @Test
    @Tag("False")
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
    @Tag("False")
    @DisplayName("doRetriveByKey() quando il primo ResultSet va e il secondo No")
    public void doRetriveByKeyResultSetSecondoFalse() throws SQLException {
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
            when(connection.prepareStatement("select * FROM ricevutafiscale" +
                    " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(connection.prepareStatement("SELECT PrezzoCamera FROM (ricevutafiscale join hotelcolossus.cameraricevuta c on ricevutafiscale.IDRicevutaFiscale = c.IDRicevutaFiscale) where c.IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
            when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
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


            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(4);
            arrayList.add(3);
           assertEquals(new RicevutaFiscale(4,2,40,LocalDate.of(2008,12,11),"Carta D'identità",LocalDate.of(2007,12,11),40.0,"pernottamento e colazione"),ricevutaFiscaleDAO.doRetriveByKey(arrayList));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveAll() quando va tutto bene")
    public void doRetriveAllAllTrue() throws SQLException {
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ricevutafiscale")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT PrezzoCamera FROM cameraricevuta where IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
       doNothing().when(preparedStatement1).setInt(anyInt(),anyInt());
        when(resultSet.next()).thenReturn(true,true,true,false);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDouble("PrezzoCamera")).thenReturn(22.0);
        when(resultSet.getInt("IDPrenotazione")).thenReturn(2);
        when(resultSet.getInt("IDRicevutaFiscale")).thenReturn(2);
        when(resultSet.getDate("DataEmissione")).thenReturn(Date.valueOf(LocalDate.of(2004,12,11)));
        when(resultSet.getString("TipoTrattamento")).thenReturn("MezzaPensione");
        when(resultSet.getDouble("PrezzoTrattamento")).thenReturn(22.0);
        when(resultSet.getDate("DataPrenotazione")).thenReturn(Date.valueOf(LocalDate.of(2003,12,11)));
        when(resultSet.getString("metodoPagamento")).thenReturn("Carta Di Credito");
        when(resultSet.getDouble("PrezzoTrattamento")).thenReturn(30.0);
        ArrayList<RicevutaFiscale> ricevutaFiscales = new ArrayList<>();

        ricevutaFiscales.add(new RicevutaFiscale(2,2,52.0,LocalDate.of(2004,12,11),"Carta Di Credito", LocalDate.of(2003,12,11), 30.0,"Mezza Pensione"));
        ricevutaFiscales.add(new RicevutaFiscale(2,2,52.0,LocalDate.of(2004,12,11),"Carta Di Credito", LocalDate.of(2003,12,11), 30.0,"Mezza Pensione"));
        ricevutaFiscales.add(new RicevutaFiscale(2,2,52.0,LocalDate.of(2004,12,11),"Carta Di Credito", LocalDate.of(2003,12,11), 30.0,"Mezza Pensione"));

        assertEquals(ricevutaFiscales,ricevutaFiscaleDAO.doRetriveAll("decrescente"));
    }
    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando il resultSet non ha tuple")
    public void doRetriveAllResultSetFalse() throws SQLException {
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ricevutafiscale")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT PrezzoCamera FROM cameraricevuta where IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertEquals(new ArrayList<RicevutaFiscale>() , ricevutaFiscaleDAO.doRetriveAll("decrescente"));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando il secondo resultSet non ha tuple")
    public void doRetriveAllResultSetTwisteFalse() throws SQLException {
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ricevutafiscale")).thenReturn(preparedStatement);
        when(connection.prepareStatement("SELECT PrezzoCamera FROM cameraricevuta where IDRicevutaFiscale = ?")).thenReturn(preparedStatement1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet.next()).thenReturn(true,true,true,false);
        when(resultSet1.next()).thenReturn(false);
        when(resultSet.getInt("IDPrenotazione")).thenReturn(2);
        when(resultSet.getInt("IDRicevutaFiscale")).thenReturn(2);
        when(resultSet.getDate("DataEmissione")).thenReturn(Date.valueOf(LocalDate.of(2004,12,11)));
        when(resultSet.getString("TipoTrattamento")).thenReturn("MezzaPensione");
        when(resultSet.getDouble("PrezzoTrattamento")).thenReturn(22.0);
        when(resultSet.getDate("DataPrenotazione")).thenReturn(Date.valueOf(LocalDate.of(2003,12,11)));
        when(resultSet.getString("metodoPagamento")).thenReturn("Carta Di Credito");


        ArrayList<RicevutaFiscale> ricevutaFiscales = new ArrayList<>();
        ricevutaFiscales.add(new RicevutaFiscale(2,2,22.0,LocalDate.of(2004,12,11),"Carta Di Credito",LocalDate.of(2003,12,11),22.0,"MezzaPensione"));
        ricevutaFiscales.add(new RicevutaFiscale(2,2,22.0,LocalDate.of(2004,12,11),"Carta Di Credito",LocalDate.of(2003,12,11),22.0,"MezzaPensione"));
        ricevutaFiscales.add(new RicevutaFiscale(2,2,22.0,LocalDate.of(2004,12,11),"Carta Di Credito",LocalDate.of(2003,12,11),22.0,"MezzaPensione"));
        assertEquals(ricevutaFiscales,ricevutaFiscaleDAO.doRetriveAll("decrescente"));
    }
    @Tag("True")
    @DisplayName("doUpdateAllTrue() quando sono tute vere")
    @Test
    public void doUpdateAllTrue() throws SQLException{
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
        when(connection.prepareStatement("UPDATE ricevutafiscale" +
                " SET metodoPagamento = ? , DataPrenotazione = ? , PrezzoTrattamento = ? , TipoTrattamento = ?, DataEmissione = ? " +
                "WHERE IDRicevutaFiscale = ? AND IDPrenotazione = ?")).thenReturn(preparedStatement);

        lenient().doNothing().when(preparedStatement).setDouble(anyInt(),anyDouble());
        lenient().doNothing().when(preparedStatement).setDate(eq(2), any(java.sql.Date.class));
        lenient().doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        lenient().doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(()->ricevutaFiscaleDAO.doUpdate(ricevutaFiscale));

    }


}
