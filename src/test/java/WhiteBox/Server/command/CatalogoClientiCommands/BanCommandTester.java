package WhiteBox.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.CatalogoClientiCommands.BanCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.util.Stato;
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
class BanCommandTester {

    private BanCommand command;
    private CatalogoClienti catalogue;
    private Cliente clienteTest;
    private String CFCliente;

    @BeforeEach
    void setUp() {
        catalogue = mock(CatalogoClienti.class);
        CFCliente = "RSSMRA90E15H501Z";

        clienteTest = new Cliente("Mario",                // nome
                "Rossi",                // cognome
                "Roma",                 // provincia (Qui avevi messo "Italiana")
                "Roma",                 // comune
                "Via del Corso",        // via
                10,                     // numeroCivico (Integer)
                18600,                  // CAP (Integer)
                "3331234567",           // numeroTelefono
                "M",                    // sesso
                LocalDate.of(1990, 5, 15), // dataNascita
                "RSSMRA90E15H501Z",     // cf
                "mario.rossi@email.com",// email
                "Italiana",             // nazionalità (Qui va bene)
                new Camera(101, Stato.Libera,1, 50.0,"", "posillipo"));

        command = new BanCommand(CFCliente);
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
        BanCommand emptyCommand = new BanCommand();

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
    void testExecuteBanCliente() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertTrue(clienteTest.isBlacklisted());
        } catch (IllegalAccess e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExecuteRemovesClienteFromNormalList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClienti.contains(clienteTest));
        } catch (IllegalAccess e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExecuteAddsClienteToBannedList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertTrue(listaClientiBannati.contains(clienteTest));
        } catch (IllegalAccess e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExecuteDoUpdate() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(clienteTest);
        } catch (IllegalAccess e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExecuteCloneNotSupportedException() throws CloneNotSupportedException {
        // Arrange
        when(catalogue.getCliente(anyString())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act & Assert - non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    void testExecuteSQLException() throws SQLException, CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(CFCliente)).thenReturn(clienteTest);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doUpdate(any());
                })) {

            // Act & Assert - non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo

    @Test
    void testUndoUnbansCliente() throws CloneNotSupportedException, SQLException {
        // Arrange
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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
            assertFalse(clienteTest.isBlacklisted());
        }
    }

    @Test
    void testUndoRemovesClienteFromBannedList() throws CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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
            assertFalse(listaClientiBannati.contains(clienteTest));
        }
    }

    @Test
    void testUndoAddsClienteToNormalList() throws CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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
            assertTrue(listaClienti.contains(clienteTest));
        }
    }

    @Test
    void testUndoDoRetriveByKey() throws SQLException, CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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

        // Act & Assert - non dovrebbe lanciare eccezione
        assertDoesNotThrow(() -> command.undo());
    }

    @Test
    void testUndoSQLException() throws SQLException, CloneNotSupportedException {
        // Arrange
        clienteTest.setBlacklisted(true);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteTest);

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