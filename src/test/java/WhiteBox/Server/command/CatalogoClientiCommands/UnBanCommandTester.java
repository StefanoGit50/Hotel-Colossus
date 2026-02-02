package WhiteBox.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.command.CatalogoClientiCommands.UnBanCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnBanCommandTester {

    private UnBanCommand command;
    private CatalogoClienti catalogue;
    private Cliente clienteTest;
    private String CFCliente;

    @BeforeEach
    void setUp() {
        catalogue = mock(CatalogoClienti.class);
        CFCliente = "RSSMRA90E15H501Z";

        clienteTest = new Cliente("Mario", "Rossi", "Roma", "Roma", "Via del Corso", 10, 18600, "3331234567", "M", LocalDate.of(1990, 5, 15), "RSSMRA90E15H501Z", "mario.rossi@email.com","Italiana",new Camera());
        command = new UnBanCommand(CFCliente);
    }

    // testo costruttore con parametri

    @Test
    void testConstructorWithParametersSetsCatalogue() {
        // Assert
        assertNotNull(command.getCatalogue());
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testConstructorWithParametersSetsCFCliente() {
        // Assert
        assertNotNull(command.getCFCliente());
        assertEquals(CFCliente, command.getCFCliente());
    }

    // testo costruttore vuoto

    @Test
    void testEmptyConstructorCreatesInstance() {
        // Act
        UnBanCommand emptyCommand = new UnBanCommand();

        // Assert
        assertNotNull(emptyCommand);
    }

    // testo getter e setter

    @Test
    void testGetCatalogueReturnsCorrectValue() {
        // Assert
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testSetCatalogueUpdatesValue() {
        // Arrange
        CatalogoClienti newCatalogue = mock(CatalogoClienti.class);

        // Act
        command.setCatalogue(newCatalogue);

        // Assert
        assertEquals(newCatalogue, command.getCatalogue());
    }

    @Test
    void testGetCFClienteReturnsCorrectValue() {
        // Assert
        assertEquals(CFCliente, command.getCFCliente());
    }

    @Test
    void testSetCFClienteUpdatesValue() {
        // Arrange
        String newCF = "BNCGPP85M15F205W";

        // Act
        command.setCFCliente(newCF);

        // Assert
        assertEquals(newCF, command.getCFCliente());
    }

    // testo execute

    @Test
    void testExecuteUnbansCliente() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(clienteTest.isBlacklisted());
        }
    }

    @Test
    void testExecuteRemovesClienteFromBannedList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClientiBannati.contains(clienteTest));
        }
    }

    @Test
    void testExecuteAddsClienteToNormalList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertTrue(listaClienti.contains(clienteTest));
        }
    }

    @Test
    void testExecuteCallsDoUpdate() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(clienteTest);
        }
    }

    @Test
    void testExecuteCloneNotSupportedException() throws CloneNotSupportedException {
        // Arrange
        when(catalogue.getCliente(anyString())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act e Assert non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    void testExecuteSQLException() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doUpdate(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo

    @Test
    void testUndoBansCliente() throws CloneNotSupportedException, SQLException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenReturn(clienteTest);
                })) {

            // Act
            command.undo();

            // Assert
            assertTrue(clienteTest.isBlacklisted());
        }
    }

    @Test
    void testUndoRemovesClienteFromNormalList() throws CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenReturn(clienteTest);
                })) {

            // Act
            command.undo();

            // Assert
            assertFalse(listaClienti.contains(clienteTest));
        }
    }

    @Test
    void testUndoAddsClienteToBannedList() throws CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenReturn(clienteTest);
                })) {

            // Act
            command.undo();

            // Assert
            assertTrue(listaClientiBannati.contains(clienteTest));
        }
    }

    @Test
    void testUndoDoRetriveByKey() throws SQLException, CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenReturn(clienteTest);
                })) {

            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doRetriveByKey(CFCliente);
        }
    }

    @Test
    void testUndoDoSave() throws SQLException, CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenReturn(clienteTest);
                })) {

            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(any(Cliente.class));
        }
    }

    @Test
    void testUndoCloneNotSupportedException() throws CloneNotSupportedException {
        // Arrange
        when(catalogue.getCliente(anyString())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act e Assert non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.undo());
    }

    @Test
    void testUndoSQLException() throws SQLException, CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(false);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(CFCliente)).thenThrow(new SQLException("Database error"));
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.undo());
        }
    }
}