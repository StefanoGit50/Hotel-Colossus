package WhiteBox.TestingDB.CameraDao;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.CameraDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CameraDaoTesting {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private CameraDAO cameraDAO;
    private Camera camera;

    @BeforeEach
    public void setUp() {
        cameraDAO = new CameraDAO();
        camera = new Camera(112, Stato.Libera, 3, 300, "");
    }

    // --------------------- TEST DELETE ---------------------
    @Test
    @DisplayName("doDelete() tutte le condizioni vere")
    public void doDeleteAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);


            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            cameraDAO.doDelete(camera);

            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    @DisplayName("doDelete() quando null")
    public void doDeleteException() {
        assertThrows(NoSuchElementException.class, () -> cameraDAO.doDelete(null));
    }

    // --------------------- TEST RETRIEVE BY KEY ---------------------
    @Test
    @DisplayName("doRetriveByKey() va tutto bene")
    public void doRetrieveByKeyAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getObject(1)).thenReturn(112);
            when(resultSet.getObject(2)).thenReturn(3);
            when(resultSet.getObject(3)).thenReturn("");
            when(resultSet.getString(4)).thenReturn(Stato.Libera.name());
            when(resultSet.getObject(5)).thenReturn(300.0);

            Camera result = cameraDAO.doRetriveByKey(112);
            assertEquals(camera.getNumeroCamera(), result.getNumeroCamera());
            assertEquals(camera.getPrezzoCamera(), result.getPrezzoCamera());
        }
    }

    @Test
    @DisplayName("doRetriveByKey() quando eccezione")
    public void doRetrieveByKeyException() {
        assertThrows(SQLException.class, () -> cameraDAO.doRetriveByKey(null));
    }

    // --------------------- TEST SAVE ---------------------
    @Test
    @DisplayName("doSave() tutto bene")
    public void doSaveAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
            doNothing().when(preparedStatement).setString(anyInt(), anyString());
            doNothing().when(preparedStatement).setDouble(anyInt(), anyDouble());
            when(preparedStatement.executeUpdate()).thenReturn(1);

            cameraDAO.doSave(camera);

            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    // --------------------- TEST RETRIEVE ALL ---------------------
    @Test
    @DisplayName("doRetriveAll() tutto bene")
    public void doRetrieveAllAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, true, false);

            when(resultSet.getObject(1)).thenReturn(112);
            when(resultSet.getObject(2)).thenReturn(3);
            when(resultSet.getObject(3)).thenReturn("");
            when(resultSet.getString(4)).thenReturn(Stato.Libera.name());
            when(resultSet.getObject(5)).thenReturn(30.0);

            cameraDAO.doRetriveAll("desc");
        }
    }

    // --------------------- TEST UPDATE ---------------------
    @Test
    @DisplayName("doUpdate() tutto bene")
    public void doUpdateAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            cameraDAO.doUpdate(camera);

            verify(preparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    @DisplayName("doUpdate() eccezione")
    public void doUpdateException() {
        assertThrows(NoSuchElementException.class, () -> cameraDAO.doUpdate(null));
    }

    // --------------------- TEST RETRIEVE BY ATTRIBUTE ---------------------
    @Test
    @DisplayName("doRetriveByAttribute() tutto bene")
    public void doRetrieveByAttributeAllTrue() throws SQLException {
        try (MockedStatic<ConnectionStorage> mocked = Mockito.mockStatic(ConnectionStorage.class)) {
            mocked.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true, true, true, false);
            when(resultSet.getInt("NumeroCamera")).thenReturn(122);
            when(resultSet.getString("NoteCamera")).thenReturn("");
            when(resultSet.getString("Stato")).thenReturn(Stato.Prenotata.name());
            when(resultSet.getDouble("PrezzoCamera")).thenReturn(22.0);
            when(resultSet.getInt("NumeroMaxOcc")).thenReturn(2);

            cameraDAO.doRetriveByAttribute("NumeroCamera", 122);
        }
    }
    @Test
    @DisplayName("")
    public void doRetrieveryByAttributeException() throws SQLException {
        assertThrows(RuntimeException.class,()->cameraDAO.doRetriveByAttribute(null,null));
    }
}
