package blackbox.FiltroPrenotazioni;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

@Tag("filtroPrenotazioni")
public class TestFiltroPrenotazioni {
    public static FrontDeskInterface frontDesk;

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
    }

    /* ************************************************************************************************************** */


    /* *************************** CASI DI SUCCESSO *************************** */

    @Nested
    @DisplayName("TESTING: FiltroPrenotazioni")
    @Tag("success")
    class TestPassFiltroPrenotazioni {
        @Test
        @DisplayName("TC1: Ricerca per Mario Santaniello - Ordinamento Nome Crescente")
        public void testCase1() throws RemoteException {
            List<Prenotazione> result = frontDesk.filterPrenotazioni("Mario", "Santaniello",
                    LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2),
                    "Nome ASC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC2: Ricerca per Luigi Verdi - Ordinamento Cognome Decrescente")
        public void testCase2() throws RemoteException {
            List<Prenotazione> result = frontDesk.filterPrenotazioni("Luigi", "Verdi",
                    LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 15),
                    "Cognome DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC3: Ricerca per Anna Neri - Ordinamento Nome Decrescente")
        public void testCase3() throws RemoteException {
            List<Prenotazione> result = frontDesk.filterPrenotazioni("Anna", "Neri",
                    LocalDate.of(2026, 3, 5), LocalDate.of(2026, 3, 12),
                    "Nome DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC4: Ricerca per Marco Bianchi - Ordinamento Data Inizio Decrescente")
        public void testCase4() throws RemoteException {
            List<Prenotazione> result = frontDesk.filterPrenotazioni("Marco", "Bianchi",
                    LocalDate.of(2026, 3, 15), LocalDate.of(2026, 3, 20),
                    "Data inizio soggiorno DESC");
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("TC5: Ricerca per Elena Rossi - Ordinamento Data Fine Decrescente")
        public void testCase5() throws RemoteException {
            List<Prenotazione> result = frontDesk.filterPrenotazioni("Elena", "Rossi",
                    LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 10),
                    "Data di fine soggiorno DESC");
            Assertions.assertNotNull(result);
        }
    }

    /* *************************** CASI DI ERROR *************************** */

    @Nested
    @DisplayName("TESTING: FiltroPrenotazioni - [error]")
    @Tag("error")
    class TestFailFiltroPrenotazioni {

        @Test
        @DisplayName("TC6: Nome con numeri")
        public void testCase6() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mar1o!", "Santaniello", LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2), "Nome DESC"));
        }

        @Test
        @DisplayName("TC7: Nome troppo lungo")
        public void testCase7() {
            String nomeLungo = "NomeEstremamenteLungoCheSuperaIlLimiteDiCinquantaCaratteri";
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni(nomeLungo, "Santaniello", LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2), "Nome DESC"));
            }

        @Test
        @DisplayName("TC8: Cognome con numeri")
        public void testCase8() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", "Santan1ell0", LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2), "Nome DESC"));
        }

        @Test
        @DisplayName("TC9: Cognome troppo lungo")
        public void testCase9() {
            String cognomeLungo = "CognomeMoltoLungoCheSuperaIlLimiteDiCinquantaCaratteri";
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", cognomeLungo, LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2), "Nome DESC"));
        }

        @Test
        @DisplayName("TC11: Data inizio passata")
        public void testCase11() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", "Santaniello", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 2, 2), "Nome DESC"));
        }

        @Test
        @DisplayName("TC13: Data fine precedente a inizio")
        public void testCase13() {
            Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", "Santaniello", LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 5), "Nome DESC"));
        }

        @Test
        @DisplayName("TC14: Data fine passata")
        public void testCase14() {
        Assertions.assertThrows(InvalidInputException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", "Santaniello", LocalDate.of(2026, 1, 27), LocalDate.of(2026, 1, 26), "Nome DESC"));
        }

        @Test
        @DisplayName("TC15: Parametri vuoti")
        public void testCase15() {
            Assertions.assertThrows(NullPointerException.class, () ->
                    frontDesk.filterPrenotazioni("", "", null, null, "Nome DESC"));
        }

        @Test
        @DisplayName("TC16: Database non disponibile")
        public void testCase16() {
            // Questo test presuppone una simulazione di errore DB lato server
            Assertions.assertThrows(RemoteException.class, () ->
                    frontDesk.filterPrenotazioni("Mario", "Santaniello", LocalDate.of(2026, 1, 27), LocalDate.of(2026, 2, 2), "Nome DESC"));
        }
    }
}
