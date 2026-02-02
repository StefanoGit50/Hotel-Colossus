package WhiteBox.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.*;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.RemovePrenotazioneCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemovePrenotazioneCommandTester
{

    private RemovePrenotazioneCommand command;
    private CatalogoPrenotazioni catalogue;
    private Prenotazione prenotazioneTest;

    @BeforeEach
    void setUp()
    {
        catalogue = mock(CatalogoPrenotazioni.class);

        Trattamento mezzaPensione = new Trattamento("Mezza Pensione", 45.50);
        Camera camera201 = new Camera(201, Stato.Libera, 2, 45.5, "Nessuna","");
        Cliente cliente = new Cliente("Valeria", "Bianchi", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana",new Camera());
        ArrayList<Cliente> ospiti = new ArrayList<>(List.of(cliente, new Cliente("Valeria", "Bianchi", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana",new Camera())));

        // Istanza di Prenotazione
        prenotazioneTest = new Prenotazione(
                1001,
                LocalDate.now(),
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2024, 6, 17),
                mezzaPensione,
                "Carta d'Identità",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2030, 1, 1),
                "Mario Rossi",
                "camera silenziosa",
                new ArrayList<>(List.of(camera201)),
                new ArrayList<>(List.of(new Servizio("Aria Condizionata", 5.0))),
                ospiti,
                "12345678"
        );

        command = new RemovePrenotazioneCommand(catalogue, prenotazioneTest);
    }

    // testo costruttore con parametri

    @Test
    void testConstructorWithParametersSetsCatalogue()
    {
        // Assert
        assertNotNull(command.getCatalogue());
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testConstructorWithParametersSetsPrenotazione()
    {
        // Assert
        //assertNotNull(command.getPrenotazione());
        //assertEquals(prenotazioneTest, command.getPrenotazione());
    }

    // testo costruttore vuoto

    @Test
    void testEmptyConstructorCreatesInstance()
    {
        // Act
        RemovePrenotazioneCommand emptyCommand = new RemovePrenotazioneCommand();

        // Assert
        assertNotNull(emptyCommand);
    }

    // testo getter e setter

    @Test
    void testGetCatalogueReturnsCorrectValue()
    {
        // Assert
        assertEquals(catalogue, command.getCatalogue());
    }

    @Test
    void testSetCatalogueUpdatesValue()
    {
        // Arrange
        CatalogoPrenotazioni newCatalogue = mock(CatalogoPrenotazioni.class);

        // Act
        command.setCatalogue(newCatalogue);

        // Assert
        assertEquals(newCatalogue, command.getCatalogue());
    }

    @Test
    void testGetPrenotazioneReturnsCorrectValue()
    {
        // Assert
        //assertEquals(prenotazioneTest, command.getPrenotazione());
    }

    @Test
    void testSetPrenotazioneUpdatesValue()
    {
        // Arrange
        Trattamento mezzaPensione = new Trattamento("Mezza Pensione", 45.50);
        Camera camera201 = new Camera(201, Stato.Libera, 2, 45.5, "Nessuna","");
        Cliente cliente = new Cliente("Valeria", "Bianchi", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 2), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana",new Camera());
        ArrayList<Cliente> ospiti = new ArrayList<>(List.of(cliente, new Cliente("Valeria", "Bianchi", "MI", "Milano", "Via Dante", 42, 20121, "3479876543", "F", LocalDate.of(1985, 12, 02), "BNCVLR85T42F205Z", "valeria.b@provider.it","Italiana",new Camera())));

        // Istanza di Prenotazione
        Prenotazione newPrenotazione = new Prenotazione(
                1001,
                LocalDate.now(),
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2024, 6, 17),
                mezzaPensione,
                "Carta d'Identità",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2030, 1, 1),
                "Mario Rossi",
                "camera silenziosa",
                new ArrayList<>(List.of(camera201)),
                new ArrayList<>(List.of(new Servizio("Aria Condizionata", 5.0))),
                ospiti,
                "12345678"
        );

        // Act
        command.setPrenotazione(newPrenotazione);

        // Assert
        //assertEquals(newPrenotazione, command.getPrenotazione());
    }

    // testo execute

    @Test
    void testExecuteRemovesPrenotazioneFromList() throws CloneNotSupportedException
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
        listaPrenotazioni.add(prenotazioneTest);
        //CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        when(catalogue.getPrenotazione(prenotazioneTest.getIDPrenotazione())).thenReturn(prenotazioneTest);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class)) {
            // Act
            command.execute();

            // Assert
            assertFalse(listaPrenotazioni.contains(prenotazioneTest));
            assertEquals(0, listaPrenotazioni.size());
        }
    }

    @Test
    void testExecuteDoDelete() throws SQLException, CloneNotSupportedException
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
        listaPrenotazioni.add(prenotazioneTest);
       // CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        when(catalogue.getPrenotazione(prenotazioneTest.getIDPrenotazione())).thenReturn(prenotazioneTest);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class)) {
            // Act
            command.execute();

            // Assert
            PrenotazioneDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doDelete(prenotazioneTest);
        }
    }

    @Test
    void testExecuteCloneNotSupportedException() throws CloneNotSupportedException
    {
        // Arrange
        when(catalogue.getPrenotazione(anyInt())).thenThrow(new CloneNotSupportedException("Clone error"));

        // Act e Assert
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    void testExecuteSQLException() throws SQLException, CloneNotSupportedException
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
        listaPrenotazioni.add(prenotazioneTest);
        //CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        when(catalogue.getPrenotazione(prenotazioneTest.getIDPrenotazione())).thenReturn(prenotazioneTest);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doDelete(any());
                })) {

            // Act e Assert
            assertDoesNotThrow(() -> command.execute());
        }
    }

    // testo undo

    @Test
    void testUndoAddsPrenotazioneToList()
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
       // CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class)) {
            // Act
            command.undo();

            // Assert
            assertTrue(listaPrenotazioni.contains(prenotazioneTest));
            assertEquals(1, listaPrenotazioni.size());
        }
    }

    @Test
    void testUndoDoSave() throws SQLException
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
        //CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class)) {
            // Act
            command.undo();

            // Assert
            PrenotazioneDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(prenotazioneTest);
        }
    }

    @Test
    void testUndoSQLException() throws SQLException
    {
        // Arrange
        ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();
        //CatalogoPrenotazioni.setListaPrenotazioni(listaPrenotazioni);

        try (MockedConstruction<PrenotazioneDAO> mockedDAO = mockConstruction(PrenotazioneDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doSave(any());
                })) {

            // Act e Assert
            assertDoesNotThrow(() -> command.undo());
        }
    }
}