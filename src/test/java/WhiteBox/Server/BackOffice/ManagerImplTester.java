package WhiteBox.Server.BackOffice;

import com.google.protobuf.Internal;
import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerImplTest {

    //private ManagerImpl manager;

    @Mock
    private ImpiegatoDAO mockImpiegatoDAO;

    private Impiegato impiegatoTest;
    private List<Impiegato> listaImpiegatiTest;

    @BeforeEach
    void setUp() throws RemoteException {
        //manager = new ManagerImpl();

        // Crea un impiegato di test
        impiegatoTest = new Impiegato(
                "mrossi",
                "hashedPassword123",
                false,
                Instant.now(),
                "Mario",
                "Rossi",
                "M",
                "Carta d'identità",
                "AB12345",
                80100,
                "Via Roma",
                "NA",
                "Napoli",
                10,
                "RSSMRA80A01H501X",
                "3331234567",
                Ruolo.FrontDesk,
                2500.00,
                LocalDate.of(2020, 1, 15),
                LocalDate.of(2020, 1, 1),
                "mario.rossi@hotel.it",
                "Italiana",
                LocalDate.of(2030, 1, 1)
        );

        // Crea una lista di impiegati di test
        listaImpiegatiTest = new ArrayList<>();
        listaImpiegatiTest.add(impiegatoTest);
        listaImpiegatiTest.add(new Impiegato(
                "lbianchi",
                "hashedPassword456",
                false,
                Instant.now(),
                "Laura",
                "Bianchi",
                "F",
                "Passaporto",
                "CD9876543",
                20100,
                "Via Milano",
                "MI",
                "Milano",
                25,
                "BNCLRA90B55F205Y",
                "3497654321",
                Ruolo.Manager,
                3000.00,
                LocalDate.of(2021, 3, 15),
                LocalDate.of(2021, 3, 1),
                "laura.bianchi@hotel.it",
                "Italiana",
                LocalDate.of(2031, 3, 1)
        ));
    }

    // testo aggiungiImpiegato

    @Test
    void testAggiungiImpiegatoCallDoSave() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class)) {

            // Act
           // manager.aggiungiImpiegato(impiegatoTest);

            // Assert verifica che doSave sia stato chiamato
            ImpiegatoDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doSave(impiegatoTest);
        }
    }

    // simula un'eccezione SQL
    @Test
    void testAggiungiImpiegatoShouldHandleSQLException() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doSave(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione, la gestisce internamente
           // assertDoesNotThrow(() -> manager.aggiungiImpiegato(impiegatoTest));
        }
    }

    // testo ottieniImpiegatiTutti

    @Test
    void testOttieniImpiegatiTuttiReturnList() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveAll("null")).thenReturn(listaImpiegatiTest);
                })) {

            // Act
            //List<Impiegato> result = manager.ottieniImpiegatiTutti();

            // Assert
           // assertNotNull(result);
           // assertEquals(2, result.size());
           // assertEquals("Mario", result.get(0).getNome());
           // assertEquals("Laura", result.get(1).getNome());
        }
    }

    // testo ottieniImpiegatoDaId

    @Test
    void testOttieniImpiegatoDaIdReturn() throws SQLException {
        // Arrange
        String codiceFiscale = "RSSMRA80A01H501X";
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveByKey(codiceFiscale)).thenReturn(impiegatoTest);
                })) {

            // Act
           // Impiegato result = manager.ottieniImpiegatoDaId(codiceFiscale);

            // Assert
           /*
            assertNotNull(result);
            assertEquals("Mario", result.getNome());
            assertEquals("Rossi", result.getCognome());
            assertEquals(codiceFiscale, result.getCodiceFiscale());
            */
        }

    }

    // testo ottieniImpiegatiDaFiltro

    @Test
    void testOttieniImpiegatiDaFiltroNoFilters() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveAll("null")).thenReturn(listaImpiegatiTest);
                })) {

            // Act tutti i filtri a null
           /*
            List<Impiegato> result = manager.ottieniImpiegatiDaFiltro(null, null, null, null);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            */
        }
    }

    @Test
    void testOttieniImpiegatiDaFiltroFilterByNome() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveAll("null")).thenReturn(listaImpiegatiTest);
                })) {

            // Act filtra per nome "Mario"
            /*
                List<Impiegato> result = manager.ottieniImpiegatiDaFiltro("Mario", null, null, null);

                // Assert
                assertNotNull(result);
             */

        }
    }

    @Test
    void testOttieniImpiegatiDaFiltroThrowException() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    when(mock.doRetriveAll("null")).thenThrow(new SQLException("Database error"));
                })) {

            // Act & Assert
           // assertThrows(RuntimeException.class,
              //() -> manager.ottieniImpiegatiDaFiltro("Mario", null, null, null));
        }
    }

    // testo eliminaImpiegato

    @Test
    void testEliminaImpiegatoReturnTrue() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class)) {

            // Act
           /*
            boolean result = manager.eliminaImpiegato(impiegatoTest);

            // Assert
            assertTrue(result);
            ImpiegatoDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doDelete(impiegatoTest);
            */

        }
    }

    @Test
    void testEliminaImpiegatoOnSQLException() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doDelete(any());
                })) {

            // Act
            //boolean result = manager.eliminaImpiegato(impiegatoTest);

            // Assert
            //assertFalse(result);
        }
    }

    // testo modificaDatiImpiegato

    @Test
    void testModificaDatiImpiegatoCallDoUpdate() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class)) {

            // Act
           //manager.modificaDatiImpiegato(impiegatoTest);

            // Assert
            ImpiegatoDAO dao = mockedDAO.constructed().get(0);
            verify(dao).doUpdate(impiegatoTest);
        }
    }

    @Test
    void testModificaDatiImpiegatoHandleSQLException() throws SQLException {
        // Arrange
        try (MockedConstruction<ImpiegatoDAO> mockedDAO = mockConstruction(ImpiegatoDAO.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).doUpdate(any());
                })) {

            // Act e Assert non dovrebbe lanciare eccezione
            //assertDoesNotThrow(() -> manager.modificaDatiImpiegato(impiegatoTest));
        }
    }

    // testo generatePassword

    @Test
    void testGeneratePasswordNonNullPassword() {
        // Act
        //String password = manager.generatePassword();

        // Assert
        //assertNotNull(password);
    }

    @Test
    void testGeneratePasswordCorrectLength() {
        // Act
      //  String password = manager.generatePassword();

        // Assert
        //assertEquals(10, password.length());
    }

    @Test
    void testGeneratePasswordContainUpperCaseLetter() {
        // Act
        //String password = manager.generatePassword();

        // Assert
        //assertTrue(password.matches(".*[A-Z].*"));
    }

    @Test
    void testGeneratePasswordContainLowerCaseLetter() {
        // Act
        //String password = manager.generatePassword();

        // Assert
        //assertTrue(password.matches(".*[a-z].*"));
    }

    @Test
    void testGeneratePassworddContainDigit() {
        // Act
       // String password = manager.generatePassword();

        // Assert
        //assertTrue(password.matches(".*[0-9].*"));
    }

    @Test
    void testGeneratePasswordContainSpecialCharacter() {
        // Act
        //String password = manager.generatePassword();

        // Assert
        //assertTrue(password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}<>?|].*"));
    }

    @Test
    void testGeneratePasswordDifferentPasswords() {
        // Act
       // String password1 = manager.generatePassword();
        //String password2 = manager.generatePassword();
        //String password3 = manager.generatePassword();

        // Assert
       // assertNotEquals(password1, password2);
       // assertNotEquals(password2, password3);
       // assertNotEquals(password1, password3);
    }

    @Test
    void testGeneratePasswordOnlyContainValidCharacters() {
        // Act
        //String password = manager.generatePassword();

        // Assert
        //assertTrue(password.matches("[A-Za-z0-9!@#$%^&*()\\-_=+\\[\\]{}<>?|]+"));
    }

    @Test
    void testGeneratePasswordProduceUniquePasswords() {
        // Arrange
        Set<String> passwords = new HashSet<>();
        int numberOfPasswords = 100;

        // Act
        for (int i = 0; i < numberOfPasswords; i++) {
           // passwords.add(manager.generatePassword());
        }

        // Assert
        assertEquals(numberOfPasswords, passwords.size());
    }

    // testo calcolaContoHotel

    @Test
    void testCalcolaContoHotelReturnNonNull() {
        // Act
      //  Map<String, Double> conto = manager.calcolaContoHotel();

        // Assert
        //assertNotNull(conto);
    }

    @Test
    void testCalcolaContoHotelContainAllRequiredKeys() {
        // Act
       // Map<String, Double> conto = manager.calcolaContoHotel();

        // Assert
        /*
            assertTrue(conto.containsKey(ManagerImpl.PRENOTAZIONE_KEY));
        assertTrue(conto.containsKey(ManagerImpl.CAMERA_KEY));
        assertTrue(conto.containsKey(ManagerImpl.SERVIZIO_KEY));
        assertTrue(conto.containsKey(ManagerImpl.TRATTAMENTO_KEY));
        assertTrue(conto.containsKey(ManagerImpl.PASSIVITA_KEY));
        assertTrue(conto.containsKey(ManagerImpl.RICAVI_KEY));
        assertTrue(conto.containsKey(ManagerImpl.CONTOECONOMICO_KEY));
         */

    }

    @Test
    void testCalcolaContoHotelHaveSevenEntries() {
        // Act
        //Map<String, Double> conto = manager.calcolaContoHotel();

        // Assert
        //assertEquals(7, conto.size());
    }

    @Test
    void testCalcolaContoHotelHaveNonNullValues() {
        // Act
        //Map<String, Double> conto = manager.calcolaContoHotel();

        //Assert
        /*
            assertNotNull(conto.get(ManagerImpl.PRENOTAZIONE_KEY));
            assertNotNull(conto.get(ManagerImpl.CAMERA_KEY));
            assertNotNull(conto.get(ManagerImpl.SERVIZIO_KEY));
            assertNotNull(conto.get(ManagerImpl.TRATTAMENTO_KEY));
            assertNotNull(conto.get(ManagerImpl.PASSIVITA_KEY));
            assertNotNull(conto.get(ManagerImpl.RICAVI_KEY));
            assertNotNull(conto.get(ManagerImpl.CONTOECONOMICO_KEY));
         */

    }

    @Test
    void testCalcolaContoHotelCalculateCorrectTotals() {
        // Act
        //Map<String, Double> conto = manager.calcolaContoHotel();

        // Assert
        /*
            assertEquals(conto.get(ManagerImpl.RICAVI_KEY), conto.get(ManagerImpl.PRENOTAZIONE_KEY));

        double sommaPrenotazioni = conto.get(ManagerImpl.CAMERA_KEY) +
                conto.get(ManagerImpl.SERVIZIO_KEY) +
                conto.get(ManagerImpl.TRATTAMENTO_KEY);
        assertEquals(sommaPrenotazioni, conto.get(ManagerImpl.PRENOTAZIONE_KEY), 0.01);
         */

    }
}