package WhiteBox.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.command.CatalogoClientiCommands.UpdateClienteCommand;
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
class UpdateClienteCommandTester {

    private UpdateClienteCommand command;
    private CatalogoClienti catalogue;
    private Cliente clienteTest;
    private Cliente clienteModificato;
    private Cliente clienteBannatoTest;
    private Cliente clienteBannatoModificato;

    @BeforeEach
    void setUp() {
        catalogue = mock(CatalogoClienti.class);


       clienteTest = new Cliente(
               "Maria", "Rosso",
               "Roma", "Roma", "Via del Corso",
               10, 18600,
               "3331234561", "F",
               LocalDate.of(1990, 5, 15), "RSSMRA90E55H501G",
               "maria.rossi@email.com", "Italiana",
               new Camera(101, Stato.Libera,1,50.1,"","Minolo"));

       clienteModificato = new Cliente("Luigi", "Verdi",
               "Milano", "Milano", "Corso Buenos Aires",
               24, 20124,
               "3391122334", "M",
               LocalDate.of(1985, 10, 20), "VRDLGU85R20F205X",
               "luigi.verdi@test.it", "Italiana",
               new Camera(202,Stato.Libera, 2, 50,"", "Renato"));

       clienteBannatoTest = new Cliente("John", "Doe",
               "Londra", "Londra", "Baker Street",
               221, 1000,
               "+4477009000", "M",
               LocalDate.of(1978, 3, 12), "DOEJOHN78C12Z114A",
               "john.doe@ukmail.com", "Inglese",
               new Camera(305,Stato.Libera, 3, 70,"", "Sasa"));

       clienteBannatoModificato = new Cliente("Anna", "Bianchi",
               "Napoli", "Napoli", "Via Toledo",
               45, 80134,
               "3289876543", "F",
               LocalDate.of(2001, 8, 5), "BNCNNA01M45F839K",
               "anna.b@libero.it", "Italiana",
               new Camera(104, Stato.Libera,2,98, "", "asfsaf"));

        command = new UpdateClienteCommand(clienteModificato);
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
        assertEquals(clienteModificato, command.getCliente());
    }

    // testo costruttore vuoto

    @Test
    void testEmptyConstructorCreatesInstance() {
        // Act
        UpdateClienteCommand emptyCommand = new UpdateClienteCommand();

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
        assertEquals(clienteModificato, command.getCliente());
    }

    @Test
    void testSetClienteUpdatesValue() {
        // Arrange
        Cliente newCliente = clienteBannatoModificato;

        // Act
        command.setCliente(newCliente);

        // Assert
        assertEquals(newCliente, command.getCliente());
    }

    // testo execute con cliente non bannato

    @Test
    void testExecuteUpdatesNonBannedClienteInList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteModificato.getCf())).thenReturn(clienteModificato);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClienti.contains(clienteTest));
            assertTrue(listaClienti.contains(clienteModificato));
        }
    }

    @Test
    void testExecuteRemovesOldClienteFromList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteModificato.getCf())).thenReturn(clienteModificato);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertEquals(1, listaClienti.size());
        }
    }

    // testo execute con cliente bannato

    @Test
    void testExecuteUpdatesBannedClienteInList() throws CloneNotSupportedException {
        // Arrange
        command.setCliente(clienteBannatoModificato);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoModificato.getCf())).thenReturn(clienteBannatoModificato);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaClientiBannati.contains(clienteBannatoTest));
            assertTrue(listaClientiBannati.contains(clienteBannatoModificato));
        }
    }

    @Test
    void testExecuteDoUpdateForBannedCliente() throws SQLException, CloneNotSupportedException {
        // Arrange
        command.setCliente(clienteBannatoModificato);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoModificato.getCf())).thenReturn(clienteBannatoModificato);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.execute();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(clienteBannatoModificato);
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
        command.setCliente(clienteBannatoModificato);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoModificato.getCf())).thenReturn(clienteBannatoModificato);

        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doUpdate(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo con cliente non bannato

    @Test
    void testUndoRestoresOriginalNonBannedCliente() throws CloneNotSupportedException, SQLException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteModificato);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteModificato.getCf())).thenReturn(clienteModificato);

        // Prima esegue execute per salvare il cliente non modificato
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            command.execute();
        }

        // Poi esegue undo
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert verifica che doUpdate sia chiamato
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(any(Cliente.class));
        }
    }

    @Test
    void testUndoRemovesModifiedClienteFromList() throws CloneNotSupportedException {
        // Arrange
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteModificato.getCf())).thenReturn(clienteModificato);

        // Prima esegui execute
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            command.execute();
        }

        // Poi esegui undo
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            listaClienti.clear();
            listaClienti.add(clienteModificato);

            // Act
            command.undo();

            // Assert
            assertFalse(listaClienti.contains(clienteModificato));
        }
    }

    // testo undo con cliente bannato

    @Test
    void testUndoRestoresOriginalBannedCliente() throws CloneNotSupportedException, SQLException {
        // Arrange
        command.setCliente(clienteBannatoModificato);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoModificato.getCf())).thenReturn(clienteBannatoModificato);

        // Prima esegue execute
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            command.execute();
        }

        // Poi esegue undo
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(any(Cliente.class));
        }
    }

    @Test
    void testUndoDoUpdateForBannedCliente() throws SQLException, CloneNotSupportedException {
        // Arrange
        command.setCliente(clienteBannatoModificato);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        listaClientiBannati.add(clienteBannatoTest);

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteBannatoModificato.getCf())).thenReturn(clienteBannatoModificato);

        // Prima esegui execute
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            command.execute();
        }

        // Poi esegui undo
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            listaClientiBannati.clear();
            listaClientiBannati.add(clienteBannatoModificato);

            // Act
            command.undo();

            // Assert
            ClienteDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(any(Cliente.class));
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
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(clienteTest);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

        CatalogoClienti.setListaClienti(listaClienti);
        CatalogoClienti.setListaClientiBannati(listaClientiBannati);

        when(catalogue.getCliente(clienteModificato.getCf())).thenReturn(clienteModificato);

        // Prima esegue execute
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class)) {
            command.execute();
        }

        // Poi esegue undo con SQLException
        try (MockedConstruction<ClienteDAO> mockedDAO = mockConstruction(ClienteDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doUpdate(any());
                })) {

            listaClienti.clear();
            listaClienti.add(clienteModificato);

            // Act e Assert non dovrebbe lanciare eccezione
            assertDoesNotThrow(() -> command.undo());
        }
    }
}