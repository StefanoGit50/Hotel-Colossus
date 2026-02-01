package WhiteBox.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.command.CatalogoClientiCommands.RemoveClienteCommand;
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
class RemoveClienteCommandTester {

    private RemoveClienteCommand command;
    private CatalogoClienti catalogue;
    private Cliente clienteTest;
    private Cliente clienteBannatoTest;

    @BeforeEach
    void setUp() {
        catalogue = mock(CatalogoClienti.class);

<<<<<<< Updated upstream
        clienteTest = new Cliente("Mario", "Rossi", "Italiana", "Roma", "Roma", "Via del Corso", 10, 18600, "3331234567", "M", LocalDate.of(1990, 5, 15), "RSSMRA90E15H501Z", "mario.rossi@email.com","Italiana",new Camera());

        clienteBannatoTest = new Cliente("Valeria", "Bianchi", "Francese", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana",new Camera());
=======
//        clienteTest = new Cliente("Mario", "Rossi", "Italiana", "Roma", "Roma", "Via del Corso", 10, 18600, "3331234567", "M", LocalDate.of(1990, 5, 15), "RSSMRA90E15H501Z", "mario.rossi@email.com","Italiana");

//        clienteBannatoTest = new Cliente("Valeria", "Bianchi", "Francese", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana");
>>>>>>> Stashed changes

//        command = new RemoveClienteCommand(catalogue, clienteTest);
    }

    // testo costruttore con parametri

    @Test
    void testConstructorWithParametersSetsCatalogue() {
        // Assert
        assertNotNull(command.getCatalogue());
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testConstructorWithParametersSetsCliente() {
        // Assert
        assertNotNull(command.getCliente());
        assertEquals(clienteTest, command.getCliente());
    }

    // testo costruttore vuoto

    @Test
    void testEmptyConstructorCreatesInstance() {
        // Act
        RemoveClienteCommand emptyCommand = new RemoveClienteCommand();

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
        Cliente newCliente = clienteBannatoTest;

        // Act
        command.setCliente(newCliente);

        // Assert
        assertEquals(newCliente, command.getCliente());
    }

    // testo execute con cliente non bannato

    @Test
    void testExecuteRemovesNonBannedClienteFromList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        CatalogoClienti.setListaClienti(listaClienti);

        when(catalogue.getCliente(clienteTest.getCf())).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClienti.contains(clienteTest));
            assertEquals(0, listaClienti.size());
        }
    }

    @Test
    void testExecuteCallsDoDeleteForNonBannedCliente() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        CatalogoClienti.setListaClienti(listaClienti);

        when(catalogue.getCliente(clienteTest.getCf())).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doDelete(clienteTest);
        }
    }

    // testo execute con cliente bannato

    @Test
    void testExecuteRemovesBannedClienteFromList() throws CloneNotSupportedException {
        // Arrange
        command.setCliente(clienteBannatoTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoTest.getCf())).thenReturn(clienteBannatoTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClientiBannati.contains(clienteBannatoTest));
            assertEquals(0, listaClientiBannati.size());
        }
    }

    @Test
    void testExecuteCallsDoDeleteForBannedCliente() throws SQLException, CloneNotSupportedException {
        // Arrange
        command.setCliente(clienteBannatoTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoTest.getCf())).thenReturn(clienteBannatoTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doDelete(clienteBannatoTest);
        }
    }

    @Test
    void testExecuteHandlesCloneNotSupportedException() throws CloneNotSupportedException {
        // Arrange
        when(catalogue.getCliente(anyString())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act e Assert non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    void testExecuteHandlesSQLException() throws SQLException, CloneNotSupportedException {
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
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo con cliente non bannato

    @Test
    void testUndoAddsNonBannedClienteToList() {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        CatalogoClienti.setListaClienti(listaClienti);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            assertTrue(listaClienti.contains(clienteTest));
            assertEquals(1, listaClienti.size());
        }
    }

    @Test
    void testUndoCallsDoSaveForNonBannedCliente() throws SQLException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        CatalogoClienti.setListaClienti(listaClienti);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(clienteTest);
        }
    }

    // testo undo con cliente bannato

    @Test
    void testUndoAddsBannedClienteToList() {
        // Arrange
        command.setCliente(clienteBannatoTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            assertTrue(listaClientiBannati.contains(clienteBannatoTest));
            assertEquals(1, listaClientiBannati.size());
        }
    }

    @Test
    void testUndoDoSaveForBannedCliente() throws SQLException {
        // Arrange
        command.setCliente(clienteBannatoTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(clienteBannatoTest);
        }
    }
}