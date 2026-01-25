package WhiteBox.UnitTest;


import it.unisa.Common.Servizio;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ServizioDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServizioDAOTesting{

    @Mock
    private ResultSet resultSet;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private Connection connection;

    private Servizio servizio;

    private MockedStatic<ConnectionStorage> connectionSM;

    @Mock
    private Statement statement;

    @InjectMocks
    private ServizioDAO servizioDAO;



    @BeforeEach
    public void setUP(){
        servizio = new Servizio("Piscina",20.0);
        connectionSM = mockStatic(ConnectionStorage.class);
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
    }

    @AfterEach
    public void setAfter(){
        connectionSM.close();
    }
    @DisplayName("doDelete(Servizio servizio) quando va tutto bene")
    @Test
    @Tag("True")
    public void doDeleteAllTrue() throws SQLException {
        when(connection.prepareStatement("DELETE FROM Servizio WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(()->servizioDAO.doDelete(servizio));
    }
     @DisplayName("doDelete(Servizio servizio) quando o la chiave di servizio o servizio sono uguale a null")
     @Test
     @Tags({@Tag("Exception"),@Tag("Error")})
     public void doDeleteException() throws SQLException {
        assertThrows(SQLException.class,()->servizioDAO.doDelete(null));
    }

    @Test
    @DisplayName("doRetriveByKey(Object nome) quando vado tutto a buon fine")
    @Tag("True")
    public void doRetriveByKeyAllTrue() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM Servizio WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1,"Piscina");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("Nome")).thenReturn("Piscina");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);

        assertEquals(new Servizio("Piscina",20.0),servizioDAO.doRetriveByKey("Piscina"));
    }
    @DisplayName("doRetriveByKey(Object nome) quando resultSet.next() ritorno false")
    @Tags({@Tag("Error"),@Tag("Exception"),@Tag("False")})
    @Test
    public void doRetriveByKeyResultSetIsFalse() throws SQLException{
        when(connection.prepareStatement("SELECT * FROM Servizio WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1,"Piscina");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class,()->servizioDAO.doRetriveByKey("Piscina"));
    }

    @DisplayName("doRetriByKey(Object nome) quando il nome non è una stringa")
    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    public void doRetriveByKeyNomeNonèUnaStringa(){
        assertThrows(SQLException.class,()->servizioDAO.doRetriveByKey(servizio));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveAll(String order) quando va tutto bene")
    public void doRetriveAllAllTrue() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM Servizio  ORDER BY Nome DESC ")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,true,true,false);
        when(resultSet.getString("Nome")).thenReturn("Piscina");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);

        ArrayList<Servizio> servizios = new ArrayList<>();
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));

        assertEquals(servizios,servizioDAO.doRetriveAll("decrescente"));
    }
    @Tag("False")
    @Test
    @DisplayName("doRetriveAll(String order) quando resultSet.next() restituisce uguale a false")
    public void doRetriveAllResultSetIsFalse() throws SQLException{
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM Servizio  ORDER BY Nome DESC ")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertEquals(new ArrayList<>(),servizioDAO.doRetriveAll("decrescente"));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveAll(String order) quando va tutto bene pero è crescente")
    public void doRetriveAllCrescente() throws SQLException{
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM Servizio  ORDER BY Nome ASC")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,true,true,false);
        when(resultSet.getString("Nome")).thenReturn("Piscina");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);

        ArrayList<Servizio> servizios = new ArrayList<>();
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));

        assertEquals(servizios,servizioDAO.doRetriveAll("crescente"));
    }
    @Tag("True")
    @DisplayName("doUpdate(Servizio servizio) quando va tutto bene")
    @Test
    public void doUpdateAllTrue() throws SQLException {
        when(connection.prepareStatement("UPDATE Servizio SET Prezzo = ? WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setDouble(1,20.0);
        doNothing().when(preparedStatement).setString(2,"Piscina");
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(()->servizioDAO.doUpdate(servizio));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doUpdate(Servizio servizio) quando servizio è uguale a null")
    public void doUpdatException(){
        assertThrows(SQLException.class,()->servizioDAO.doUpdate(null));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveByAttribute(String attribute , Object value) quando va tutto bene ")
    public void doRetriveByAttributeAllTrue() throws SQLException {
        lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(anyInt(),anyString());
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true , true ,true ,false);
        when(resultSet.getString("Nome")).thenReturn("Piscina");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);

        ArrayList<Servizio> servizios = new ArrayList<>();
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));
        servizios.add(new Servizio("Piscina",20.0));
        assertEquals(servizios,servizioDAO.doRetriveByAttribute("Nome","Piscina"));
    }

    @Tags({@Tag("Exception"),@Tag("Error"),@Tag("False")})
    @Test
    @DisplayName("doRetriveByAttribute() quando resultSet.next() ritorna false")
    public void doRetriveByAttributeResultSetIsFalse() throws SQLException {
        lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(anyInt(),anyString());
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class,()->servizioDAO.doRetriveByAttribute("Nome","Piscina"));
    }
    @Test
    @DisplayName("doRetriveByAttribute() quando da un Eccezione ")
    @Tags({@Tag(""),@Tag("")})
    public void doRetriveByAttributeException(){
        assertThrows(RuntimeException.class,()->servizioDAO.doRetriveByAttribute(null,"Piscina"));
    }
}
