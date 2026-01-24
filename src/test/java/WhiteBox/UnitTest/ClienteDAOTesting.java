package WhiteBox.UnitTest;


import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import javafx.beans.binding.When;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.Suite;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension .class)
public class ClienteDAOTesting {

    private final String stringSQLRetrieveByKey = "SELECT * FROM Cliente WHERE CF = ?";
    private final String stringSQLDoDelete= "DELETE FROM Cliente WHERE CF = ?";
    private final String stringSQLDoSave= "INSERT INTO CLIENTE(\n" +
            "CF, nome, cognome, Cap, comune, civico, provincia, via,\n" +
            "Email, Sesso, telefono, Cittadinanza,\n" +
            "DataDiNascita, IsBackListed\n" +
            ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private Object object = new Object();

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Mock
    private Cliente cliente;

    private MockedStatic<ConnectionStorage> mockedConnectionStorage;

    @InjectMocks
    ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConnectionStorage = mockStatic(ConnectionStorage.class);
        mockedConnectionStorage.when(ConnectionStorage::getConnection).thenReturn(connection);
    }

    @AfterEach
    void shutdown() {
        mockedConnectionStorage.close();
        cliente = null;
    }

    @Test
    @Tag("exeception")
    @DisplayName("Branch1: oggetto!=String")
    public void DoRetrievebyKeyBranch1TestException() {
        assertThrows(SQLException.class, () -> clienteDAO.doRetriveByKey(object));
    }

    @Test
    @Tag("true")
    @DisplayName("Branch2: oggetto ==String")
    public void DoRetrievebyKeyTesttrue() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(connection.prepareStatement(stringSQLRetrieveByKey)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.getObject(1)).thenReturn("cf");
        when(resultSet.getObject(2)).thenReturn("viola");
        when(resultSet.getObject(3)).thenReturn("verdana");
        when(resultSet.getObject(4)).thenReturn("24432");
        when(resultSet.getObject(5)).thenReturn("Valle Lunga");
        when(resultSet.getObject(6)).thenReturn(234);
        when(resultSet.getObject(7)).thenReturn("Roma");
        when(resultSet.getObject(8)).thenReturn("via");
        when(resultSet.getObject(9)).thenReturn("email");
        when(resultSet.getObject(10)).thenReturn("M");
        when(resultSet.getObject(11)).thenReturn("235254545");
        when(resultSet.getObject(12)).thenReturn("marocchina");
        when(resultSet.getObject(13)).thenReturn(Date.valueOf("2009-12-13"));
        when(resultSet.getObject(14)).thenReturn(false);

        object = "pippo";
        cliente = clienteDAO.doRetriveByKey(object);
        assertEquals("viola", cliente.getNome());
        assertEquals("verdana", cliente.getCognome());
        assertEquals("cf", cliente.getCf());
        assertEquals(24432, cliente.getCAP());
        assertEquals("Valle Lunga", cliente.getComune());
        assertEquals(234, cliente.getNumeroCivico());
        assertEquals("Roma", cliente.getProvincia());
        assertEquals("via", cliente.getVia());
        assertEquals("email", cliente.getEmail());
        assertEquals("M", cliente.getSesso());
        assertEquals("235254545", cliente.getNumeroTelefono());
        assertEquals("marocchina", cliente.getCittadinanza());
        assertEquals(Date.valueOf("2009-12-13").toLocalDate(), cliente.getDataNascita());
        assertFalse(cliente.isBlacklisted());

    }

    @Test
    @DisplayName("Branch 2 e branch 3 false")
    @Tags({@Tag("false"), @Tag("null")})

    public void DoRetrievebyKeyCapNull() throws SQLException {
        object = "pippo";
        when(connection.prepareStatement(stringSQLRetrieveByKey)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        cliente = clienteDAO.doRetriveByKey(object);

        assertNull(cliente.getCAP());
    }


    @Test
    @DisplayName("branch 3 true cap not a number")
    @Tag("false")
    public void DoRetrievebyKeyTestCapFalse() throws SQLException {

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(connection.prepareStatement(stringSQLRetrieveByKey)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.getObject(1)).thenReturn("cf");
        when(resultSet.getObject(2)).thenReturn("viola");
        when(resultSet.getObject(3)).thenReturn("verdana");
        when(resultSet.getObject(4)).thenReturn("Cap no number");
        when(resultSet.getObject(5)).thenReturn("Valle Lunga");
        when(resultSet.getObject(6)).thenReturn(234);
        when(resultSet.getObject(7)).thenReturn("Roma");
        when(resultSet.getObject(8)).thenReturn("via");
        when(resultSet.getObject(9)).thenReturn("email");
        when(resultSet.getObject(10)).thenReturn("M");
        when(resultSet.getObject(11)).thenReturn("235254545");
        when(resultSet.getObject(12)).thenReturn("marocchina");
        when(resultSet.getObject(13)).thenReturn(Date.valueOf("2009-12-13"));
        when(resultSet.getObject(14)).thenReturn(false);

        object = "pippo";
        cliente = clienteDAO.doRetriveByKey(object);
        assertEquals("viola", cliente.getNome());
        assertEquals("verdana", cliente.getCognome());
        assertEquals("cf", cliente.getCf());
        assertNull( cliente.getCAP());
        assertEquals("Valle Lunga", cliente.getComune());
        assertEquals(234, cliente.getNumeroCivico());
        assertEquals("Roma", cliente.getProvincia());
        assertEquals("via", cliente.getVia());
        assertEquals("email", cliente.getEmail());
        assertEquals("M", cliente.getSesso());
        assertEquals("235254545", cliente.getNumeroTelefono());
        assertEquals("marocchina", cliente.getCittadinanza());
        assertEquals(Date.valueOf("2009-12-13").toLocalDate(), cliente.getDataNascita());
        assertFalse(cliente.isBlacklisted());
    }


    @Test
    @DisplayName("Delete: Branch 1 cliente != null ")
    @Tag("true")
    public void DoDeleteTestTrue() throws SQLException{
        when(cliente.getCf()).thenReturn("CF");
        when(connection.prepareStatement(stringSQLDoDelete)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        clienteDAO.doDelete(cliente);
        verify(preparedStatement).setString(1,"CF");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    @DisplayName("Delete: Branch 1 cliente esiste elemento non trovato")
    @Tag("exception")
    public void DoDeleteTestNotFound() throws SQLException{
        when(cliente.getCf()).thenReturn("CF");
        when(connection.prepareStatement(stringSQLDoDelete)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doDelete(cliente));
    }


    @Test
    @DisplayName("Delete: Branch 1 cliente == null ")
    @Tag("exception")
    public void DoDeleteTestFalse() throws SQLException{
        Cliente c= null;
        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doDelete(c));
    }

    @Test
    @DisplayName("Save:")
    @Tag("true")
    public void DoSaveTestTrue() throws SQLException{
        when(cliente.getCf()).thenReturn("CF");
        when(connection.prepareStatement(stringSQLDoSave)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(14);

        verify(preparedStatement).executeUpdate();
    }
}