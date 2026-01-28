package WhiteBox.Server.command.CatalogoClientiCommands;


import it.unisa.Common.Cliente;
import it.unisa.Server.command.CatalogoClientiCommands.AddClienteCommand;
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
class AddClienteCommandTester{

    private AddClienteCommand command;
    private CatalogoClienti catalogue;
    private Cliente clienteTest;

    @BeforeEach
    void setUp() {
        catalogue = mock(CatalogoClienti.class);

        clienteTest = new Cliente("Valeria", "Bianchi", "Francese", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana");

        command = new AddClienteCommand(catalogue, clienteTest);
    }

    // testo costruttore con parametri


    @Test
     void testConstructorWithParametersSetCatalogue() {
        // Assert
        assertNotNull(command.getCatalogue());
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testConstructorWithParametersSetCliente() {
        // Assert
        assertNotNull(command.getCliente());
        assertEquals(clienteTest, command.getCliente());
    }

    // testo costruttore vuoto

    @Test
    void testEmptyConstructorCreatesInstance() {
        // Act
        AddClienteCommand emptyCommand = new AddClienteCommand();

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
    void testGetClienteReturnsCorrectValue() {
        // Assert
        assertEquals(clienteTest, command.getCliente());
    }

    @Test
    void testSetClienteUpdatesValue() {
        // Arrange
        Cliente newCliente = new Cliente("Mario", "Rossi", "Italiana", "Roma", "Roma", "Via del Corso", 10, 18600, "3331234567", "M", LocalDate.of(1990, 5, 15), "RSSMRA90E15H501Z", "mario.rossi@email.com","Italiana");

        // Act
        command.setCliente(newCliente);

        // Assert
        assertEquals(newCliente, command.getCliente());
    }

    // testo execute

    @Test
    void testExecuteAddsClienteToList() {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        CatalogoClienti.setListaClienti(listaClienti);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertTrue(listaClienti.contains(clienteTest));
            assertEquals(1, listaClienti.size());
        }
    }

    @Test
    void testExecuteCallsDoSave() throws SQLException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        CatalogoClienti.setListaClienti(listaClienti);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(clienteTest);
        }
    }

    @Test
    void testExecuteHandlesSQLException() throws SQLException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        CatalogoClienti.setListaClienti(listaClienti);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doSave(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo

    @Test
    void testUndoRemovesClienteFromList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        CatalogoClienti.setListaClienti(listaClienti);

        when(catalogue.getCliente(clienteTest.getCf())).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            assertFalse(listaClienti.contains(clienteTest));
            assertEquals(0, listaClienti.size());
        }
    }

    @Test
    void testUndoCallsDoDelete() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        CatalogoClienti.setListaClienti(listaClienti);

        when(catalogue.getCliente(clienteTest.getCf())).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doDelete(clienteTest);
        }
    }

    @Test
    void testUndoSQLException() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        CatalogoClienti.setListaClienti(listaClienti);

        when(catalogue.getCliente(clienteTest.getCf())).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doDelete(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.undo());
        }
    }

    @Test
    void testUndoHandlesCloneException() throws CloneNotSupportedException {
        // Arrange
        when(catalogue.getCliente(anyString())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act & Assert - non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.undo());
    }
}