package WhiteBox.UnitTest;


import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteDAOTesting {

    private String attribute="";
    private Object object = new Object();

    private final String stringSQLRetrieveByKey = "SELECT * FROM Cliente WHERE CF = ?";
    private final String stringSQLDoDelete= "DELETE FROM Cliente WHERE CF = ?";
    private final String stringSQLDoUpdate= "UPDATE Cliente SET nome = ?, cognome = ?, Cap = ?, comune = ?, " +
            "civico = ?, provincia = ?, via = ?, Email = ?, Sesso = ?, " +
            "telefono = ?, Cittadinanza = ?, " +
            "DataDiNascita = ?, IsBackListed = ? WHERE CF = ?";
    private final String stringSQLDoUpdateAttribute = "SELECT * FROM Cliente WHERE " + attribute + " = ?";

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
    @DisplayName("Retrieve: Branch1: oggetto!=String")
    public void DoRetrievebyKeyBranch1TestException() {
        assertThrows(SQLException.class, () -> clienteDAO.doRetriveByKey(object));
    }

    @Test
    @Tag("true")
    @DisplayName("Retrieve: Branch2: oggetto ==String")
    public void DoRetrievebyKeyTesttrue() throws SQLException{
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
    @DisplayName("Retrieve: Branch 2 e branch 3 false")
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
    @DisplayName("Retrieve:branch 3 true cap not a number")
    @Tag("false")
    public void DoRetrievebyKeyTestCapFalse() throws SQLException {

        when(connection.prepareStatement(stringSQLRetrieveByKey)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

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
    @DisplayName("RetrieveAll: branch 1 e branch 2 true")
    @Tag("true")
    public void DoRetrieveAll() throws SQLException{
       String order = "decrescente";
        when(connection.prepareStatement(contains("Cliente ORDER BY"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getObject(1)).thenReturn("cf");
        when(resultSet.getObject(2)).thenReturn("viola");
        when(resultSet.getObject(3)).thenReturn("verdana");
        when(resultSet.getObject(4)).thenReturn("5678");
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

        Collection<Cliente> result = clienteDAO.doRetriveAll(order);
        verify(connection).prepareStatement(contains("DESC"));
        verify(preparedStatement).executeQuery();

        assertEquals(1, result.size());
        Cliente cliente = result.iterator().next();
        assertEquals("viola", cliente.getNome());
        assertEquals("verdana", cliente.getCognome());
        assertEquals("cf", cliente.getCf());
        assertEquals( 5678,cliente.getCAP());
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
    @DisplayName("RetrieveAll: branch 1 e branch 2 false")
    @Tag("false")
    public void testDoRetrieveAll_emptyResultSet() throws SQLException {
        String order = "crescente";
        when(connection.prepareStatement(contains("Cliente ORDER BY"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn( false);

        // Simula resultSet vuoto
        when(resultSet.next()).thenReturn(false);

        Collection<Cliente> result = clienteDAO.doRetriveAll(order);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("DoUpdate: branch 1 cliente != null o cliente cf !=null ")
    @Tag("true")
    public void testDoUpdateTrue() throws SQLException{
        when(connection.prepareStatement(stringSQLDoUpdate)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        when(cliente.getCf()).thenReturn("CF123");
        when(cliente.getNome()).thenReturn("Mario");
        when(cliente.getCognome()).thenReturn("Rossi");
        when(cliente.getCAP()).thenReturn(12345);
        when(cliente.getComune()).thenReturn("Roma");
        when(cliente.getNumeroCivico()).thenReturn(10);
        when(cliente.getProvincia()).thenReturn("RM");
        when(cliente.getVia()).thenReturn("Via Roma");
        when(cliente.getEmail()).thenReturn("mario@rossi.com");
        when(cliente.getSesso()).thenReturn("M");
        when(cliente.getNumeroTelefono()).thenReturn("123456789");
        when(cliente.getCittadinanza()).thenReturn("Italiana");
        when(cliente.getDataNascita()).thenReturn(LocalDate.of(2000,1,1));
        when(cliente.isBlacklisted()).thenReturn(false);

        clienteDAO.doUpdate(cliente);

        verify(preparedStatement).setString(1, "Mario");
        verify(preparedStatement).setString(2, "Rossi");
        verify(preparedStatement).setInt(3, 12345);
        verify(preparedStatement).setString(4, "Roma");
        verify(preparedStatement).setInt(5, 10);
        verify(preparedStatement).setString(6, "RM");
        verify(preparedStatement).setString(7, "Via Roma");
        verify(preparedStatement).setString(8, "mario@rossi.com");
        verify(preparedStatement).setString(9, "M");
        verify(preparedStatement).setString(10, "123456789");
        verify(preparedStatement).setString(11, "Italiana");
        verify(preparedStatement).setDate(12, Date.valueOf("2000-1-1"));
        verify(preparedStatement).setBoolean(13, false);
        verify(preparedStatement).setString(14,"CF123");

        verify(preparedStatement).executeUpdate();
        verify(connection).prepareStatement(contains("UPDATE Cliente SET"));

    }

    @Test
    @DisplayName("DoUpdate: branch 1 cliente == null o cliente cf ==null ")
    @Tags({@Tag("exception"),@Tag("false")})
    public void DoUpdateTestFalse() throws SQLException{
        Cliente c= null;
        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doUpdate(c));
    }

    @Test
    @DisplayName("RetrieveAttribute: Branch 1 true Branch 2 true Branch 3 false")
    @Tag("true")
    public void testDoRetrieveAttributeTrue() throws SQLException{
        Object value = "Rossi";
        attribute="cognome";
        when(connection.prepareStatement(contains(attribute))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,false);

        when(resultSet.getString("CF")).thenReturn("RSSMRA80A01F205X");
        when(resultSet.getString("nome")).thenReturn("Mario");
        when(resultSet.getString("cognome")).thenReturn("Rossi");
        when(resultSet.getInt("civico")).thenReturn(10);
        when(resultSet.getInt("Cap")).thenReturn(80100);
        when(resultSet.getString("Comune")).thenReturn("Napoli");
        when(resultSet.getString("Cittadinanza")).thenReturn("Italiana");
        when(resultSet.getString("provincia")).thenReturn("NA");
        when(resultSet.getString("Via")).thenReturn("Via Roma");
        when(resultSet.getString("Email")).thenReturn("mario@rossi.it");
        when(resultSet.getString("Sesso")).thenReturn("M");
        when(resultSet.getString("telefono")).thenReturn("3331234567");
        when(resultSet.getBoolean("IsBackListed")).thenReturn(false);
        when(resultSet.getDate("DataDiNascita"))
                .thenReturn(Date.valueOf(LocalDate.of(1980,1,1)));

        Collection<Cliente> result = assertDoesNotThrow(() ->
                clienteDAO.doRetriveByAttribute(attribute, value)
        );



        assertNotNull(result);
        assertEquals(1, result.size());
        cliente = result.iterator().next();
        assertEquals("RSSMRA80A01F205X", cliente.getCf());
        assertEquals("Mario", cliente.getNome());
        assertEquals("Rossi", cliente.getCognome());
        assertEquals(10, cliente.getNumeroCivico());
        assertEquals(80100, cliente.getCAP());
        assertEquals("Napoli", cliente.getComune());
        assertEquals("Italiana", cliente.getCittadinanza());
        assertEquals("NA", cliente.getProvincia());
        assertEquals("Via Roma", cliente.getVia());
        assertEquals("mario@rossi.it", cliente.getEmail());
        assertEquals("M", cliente.getSesso());
        assertEquals("3331234567", cliente.getNumeroTelefono());
        assertFalse(cliente.isBlacklisted());
        assertEquals(LocalDate.of(1980,1,1), cliente.getDataNascita());

        verify(preparedStatement).setObject(1, value);
        verify(preparedStatement).executeQuery();
    }

    @Test
    @DisplayName("RetrieveAttribute: branch2 false")
    @Tag("exception")@Tag("false")
    public void testDoRetrieveAttributeFalse() throws SQLException{
        Object value = "Mirko";
        attribute="nome";
        when(connection.prepareStatement(contains(attribute))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doRetriveByAttribute(attribute, value));
        verify(resultSet).next();
        verify(preparedStatement).setObject(1, value);
        verify(preparedStatement).executeQuery();
    }

    @Test
    @DisplayName("RetrieveAttribute: branch1 false")
    @Tag("exception")@Tag("false")
    public void testDoRetrieveAttributeFalse1() {
        attribute="";
        Object value= null;
        assertThrows(RuntimeException.class, () ->  clienteDAO.doRetriveByAttribute(attribute, value));
    }
}