package blackbox.FiltroClienti;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

@Tag("filtroClienti")
public class TestFiltroClienti {
    private static FrontDeskInterface frontDesk;

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
    }

    /* *************************** CASI DI SUCCESSO *************************** */

    @Nested
    @DisplayName("TESTING: FiltroClienti")
    @Tag("success")
    class TestPassFiltroClienti {

        @Test
        @DisplayName("TC1: [success] Ricerca per Nome - Ordinamento Nome")
        public void testCase1() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("Mario", "", "", LocalDate.of(1800, 5, 12), null, "Nome ASC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC2: [success] Ricerca parametri multipli (tutto eccetto il nome) - Ordinamento Cognome")
        public void testCase2() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("", "Rossi", "Italiana", null, true, "Cognome DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC3: [success] Ricerca parametri completi - Ordinamento Nome")
        public void testCase3() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("Luca", "Bianchi", "Italiana", LocalDate.of(1990, 1, 20), true, "Nome DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC3: [success] Ricerca parametri completi - Ordinamento Nazionalità")
        public void testCas3() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("Luca", "Bianchi", "Italiana", LocalDate.of(1990, 1, 20), true, "Nazionalità DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC4: [success] Ricerca parametri completi - Ordinamento Data di Nascita")
        public void testCase4() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("Anna", "Verdi", "Francese", LocalDate.of(1995, 3, 15), false, "Data di Nascita DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC5: [success] Ricerca parametri completi - Ordinamento Sesso")
        public void testCase5() throws RemoteException {
            List<Cliente> result = frontDesk.filterClienti("Giulia", "Neri", "Spagnola", LocalDate.of(1998, 10, 10), false, "Sesso DESC");
            Assertions.assertNotNull(result);
        }
    }

    /* ***************************** CASI DI ERRORE **************************** */

    @Nested
    @DisplayName("TESTING: FiltroClienti - [error]")
    @Tag("error")
    class TestFailFiltroClienti {

        @Test
        @DisplayName("TC6: [error] Formato nome non valido")
        public void testCase6() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario123", "Rossi", "Italiana", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC7: [error] Nome oltre 50 caratteri")
        public void testCase7() {
            String nomeLungo = "Uvuvwevwevwe onyetenvewve ugwemubwem ugwemubwem ossas";
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti(nomeLungo, "Rossi", "Italiana", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC8: [error] Formato cognome non valido")
        public void testCase8() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi_!", "Italiana", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC9: [error] Cognome oltre 50 caratteri")
        public void testCase9() {
            String cognomeLungo = "Uvuvwevwevwe onyetenvewve ugwemubwem ugwemubwem ossas";
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", cognomeLungo, "Italiana", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC10: [error] Formato nazionalità non valido")
        public void testCase10() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi", "It4li4!", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC11: [error] Nazionalità oltre 50 caratteri")
        public void testCase11() {
            String nazLunga = "NazionalitaConUnTestoTroppoEstesoPerEssereAccettatoDalSistema";
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi", nazLunga, LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC12: [error] Formato data non valido (yyyy/mm/dd)")
        public void testCase12() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi", "Italiana", null, true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC13: [error] Data di nascita futura")
        public void testCase13() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi", "Italiana", LocalDate.of(2030, 1, 1), true, "Nome DESC"));
        }

        @Test
        @DisplayName("TC14: [error] Valore blackListed non riconosciuto")
        public void testCase14() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterClienti("Mario", "Rossi", "Italiana", LocalDate.of(1990, 1, 1), null, "Nome DESC"));
        }

        @Test
        @DisplayName("TC15: [error] Nessun criterio inserito (tutti nulli)")
        public void testCase15() {
            Assertions.assertThrows(NullPointerException.class, () ->
                    frontDesk.filterClienti("", "", "", null, null, "Nome DESC"));
        }

        @Test
        @DisplayName("TC16: [error] Database offline")
        public void testCase16() {
            // Simulazione errore di connessione
            Assertions.assertThrows(NullPointerException.class /*La connessione è nulla quando tenta di creare un preparedStatement*/, () ->
                    frontDesk.filterClienti("Mario", "Rossi", "Italiana", LocalDate.of(1990, 1, 1), true, "Nome DESC"));
        }
    }
}
