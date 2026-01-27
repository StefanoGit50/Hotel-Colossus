package blackbox.RegistraImpiegato;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.DuplicateKeyEntry;
import it.unisa.interfacce.ManagerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Random;

@DisplayName("TESTING: RegistraImpiegato")
@Tag("registraImpiegato")
public class TestCasesRegistraImpiegato {

    private static final Logger log = LogManager.getLogger(TestCasesRegistraImpiegato.class);
    public static ManagerInterface manager;

    @BeforeAll
    public static void istantiateManager() throws RemoteException, NotBoundException, MalformedURLException {
        manager = (ManagerInterface) Naming.lookup("rmi://localhost/GestioneImpiegati");
    }

    /* ************************************************************************************************************** */

    /**
     * Crea un impiegato con dati di base. Su questo si costruiscono tutti gli altri.
     *
     * @return {@code Impiegato} base.
     */
    public Impiegato createBaseImpiegato() {
        return new Impiegato(
                // username generato casualmente per evitare DuplicateKey
                "mario.rossi",
                "passwordSicura123!",        // hashedPassword (dedotto)
                "Mario",                     // nome
                "Rossi",                     // cognome
                "Maschio",                   // sesso
                "CID",                   // tipoDocumento
                "AB1234567",                 // numeroDocumento
                80100,                       // CAP
                "Via Roma",                  // via
                "Napoli",                    // provincia
                "Napoli",                    // comune
                10,                          // numeroCivico
                generateRandomCF(),          // codiceFiscale
                "1",                        // telefono
                Ruolo.Manager,             // ruolo (mappato dal testo "Front desk")
                2500.00,                     // stipendio
                LocalDate.of(2024, 1, 15),   // dataAssunzione
                LocalDate.of(2020, 1, 20),   // dataRilascio
                "mario.rossi@HotelColossus.it", // emailAziendale
                "Italiana",                  // cittadinanza
                LocalDate.of(2099, 1, 20)    // dataScadenza
        );
    }

    public static String generateRandomCF() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rn =  new Random();

        // Aggiunge len caratteri random
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(rn.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /* ************************************************************************************************************** */

    @Nested
    @DisplayName("TESTING: RegistraImpiegato")
    @Tag("success")
    class TestRegistraImpiegato {
        @Test
        @DisplayName("TC1: [success] Registrazione impiegato effettuata con successo")
        public void testCase1() {
            Impiegato i = createBaseImpiegato();
            Assertions.assertDoesNotThrow( () -> manager.addImpiegato(i));
        }
    }
    /* ***************************** CASI DI ERRORE **************************** */

    @Nested
    @DisplayName("TESTING: RegistraImpiegato - [error]")
    @Tag("error")
    class TestFailRegistraImpiegato {
        @Test
        @DisplayName("TC2: [error] Codice fiscale deve essere di 16 caratteri")
        public void testCase2() {
            Impiegato i = createBaseImpiegato();
            i.setCodiceFiscale("RSSMRA85M0"); // 10 caratteri
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC3: [error] Codice fiscale già presente nel sistema")
        public void testCase3() {
            Impiegato i = createBaseImpiegato();
            // Provo ad inserire lo stesso impiegato (con lo stesso CF) più volte
            try {
                manager.addImpiegato(i);
            } catch (RemoteException | DuplicateKeyEntry e) {
                e.printStackTrace();
            }
            Assertions.assertThrows(DuplicateKeyEntry.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC4: [error] Codice fiscale non valido: [error] CF contiene caratteri speciali")
        public void testCase4() {
            Impiegato i = createBaseImpiegato();
            i.setCodiceFiscale("RSS5M*");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC5: [error] Codice fiscale non valido: [error] CF vuoto")
        public void testCase5() {
            Impiegato i = createBaseImpiegato();
            i.setCodiceFiscale("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC6: [error] Stipendio deve essere un valore positivo")
        public void testCase6() {
            Impiegato i = createBaseImpiegato();
            i.setStipendio(-1500.00);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC7: [error] Campo stipendio non deve essere vuoto")
        public void testCase7() {
            Impiegato i = createBaseImpiegato();
            // In Java double non può essere "", testiamo il valore di default nullo/zero se gestito
            i.setStipendio(0);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC8: [error] Nome deve contenere solo lettere e spazi")
        public void testCase8() {
            Impiegato i = createBaseImpiegato();
            i.setNome("Mario123");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC9: [error] Nome non deve essere vuoto")
        public void testCase9() {
            Impiegato i = createBaseImpiegato();
            i.setNome("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC10: [error] Cognome deve contenere solo lettere e spazi")
        public void testCase10() {
            Impiegato i = createBaseImpiegato();
            i.setCognome("Rossi!");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC11: [error] Cognome non deve essere vuoto")
        public void testCase11() {
            Impiegato i = createBaseImpiegato();
            i.setCognome("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC12: [error] CAP deve essere di 5 cifre")
        public void testCase12() {
            Impiegato i = createBaseImpiegato();
            i.setCAP(801);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC13: [error] CAP non deve essere vuoto")
        public void testCase13() {
            Impiegato i = createBaseImpiegato();
            i.setCAP(0);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC14: [error] Formato data assunzione non corretto")
        public void testCase14() {
            Impiegato i = createBaseImpiegato();
            // La gestione del formato è solitamente a livello di GUI
            i.setDataAssunzione(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC15: [error] Data assunzione non può essere futura")
        public void testCase15() {
            Impiegato i = createBaseImpiegato();
            i.setDataAssunzione(LocalDate.of(2027, 12, 15));
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC16: [error] Data assunzione non può essere vuota")
        public void testCase16() {
            Impiegato i = createBaseImpiegato();
            i.setDataAssunzione(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC17: [error] Numero telefono deve essere di massimo 15 cifre")
        public void testCase17() {
            Impiegato i = createBaseImpiegato();
            i.setTelefono("393331234567890123"); // > 15 cifre
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC18: [error] Numero telefono deve contenere solo cifre")
        public void testCase18() {
            Impiegato i = createBaseImpiegato();
            i.setTelefono("393331234567ABC");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC19: [error] Numero telefono non deve essere vuoto")
        public void testCase19() {
            Impiegato i = createBaseImpiegato();
            i.setTelefono("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC20: [error] Cittadinanza deve contenere solo lettere e spazi")
        public void testCase20() {
            Impiegato i = createBaseImpiegato();
            i.setCittadinanza("Italiana123");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC21: [error] Cittadinanza non deve essere vuoto")
        public void testCase21() {
            Impiegato i = createBaseImpiegato();
            i.setCittadinanza("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC22: [error] Email aziendale non corretta")
        public void testCase22() {
            Impiegato i = createBaseImpiegato();
            i.setEmailAziendale("mario.rossi.at.hotelcolossus.it");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC23: [error] Email aziendale non deve essere vuota")
        public void testCase23() {
            Impiegato i = createBaseImpiegato();
            i.setEmailAziendale("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC24: [error] Valore sesso non valido")
        public void testCase24() {
            Impiegato i = createBaseImpiegato();
            i.setSesso("Tanto");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC25: [error] Valore sesso non deve essere nullo")
        public void testCase25() {
            Impiegato i = createBaseImpiegato();
            i.setSesso("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC26: [error] Ruolo non valido")
        public void testCase26() {
            Impiegato i = createBaseImpiegato();
            i.setRuolo(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC27: [error] Ruolo non deve essere vuoto")
        public void testCase27() {
            Impiegato i = createBaseImpiegato();
            i.setRuolo(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC28: [error] Formato data di rilascio documento non corretta")
        public void testCase28() {
            Impiegato i = createBaseImpiegato();
            i.setDataRilascio(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC29: [error] Data di rilascio del documento non può essere futura")
        public void testCase29() {
            Impiegato i = createBaseImpiegato();
            i.setDataRilascio(LocalDate.now().plusYears(1));
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC30: [error] Data di rilascio del documento non può essere nulla")
        public void testCase30() {
            Impiegato i = createBaseImpiegato();
            i.setDataRilascio(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC31: [error] Tipo documento non valido")
        public void testCase31() {
            Impiegato i = createBaseImpiegato();
            i.setTipoDocumento("Tessera sanitaria");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC32: [error] Tipo documento non deve essere vuoto")
        public void testCase32() {
            Impiegato i = createBaseImpiegato();
            i.setTipoDocumento("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC33: [error] Via deve contenere solo lettere e spazi")
        public void testCase33() {
            Impiegato i = createBaseImpiegato();
            i.setVia("Via Roma #10");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC34: [error] Via non deve essere vuoto")
        public void testCase34() {
            Impiegato i = createBaseImpiegato();
            i.setVia("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC35: [error] Provincia deve contenere solo lettere e spazi")
        public void testCase35() {
            Impiegato i = createBaseImpiegato();
            i.setProvincia("NAH1");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC36: [error] Provincia non deve essere vuoto")
        public void testCase36() {
            Impiegato i = createBaseImpiegato();
            i.setProvincia("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC37: [error] Comune deve contenere solo lettere e spazi")
        public void testCase37() {
            Impiegato i = createBaseImpiegato();
            i.setComune("Napoli_1");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC38: [error] Comune non deve essere vuoto")
        public void testCase38() {
            Impiegato i = createBaseImpiegato();
            i.setComune("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC39: [error] Numero civico deve essere un numero intero positivo")
        public void testCase39() {
            Impiegato i = createBaseImpiegato();
            i.setNumeroCivico(-2);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC40: [error] Numero civico non deve essere nullo")
        public void testCase40() {
            Impiegato i = createBaseImpiegato();
            i.setNumeroCivico(0); // Assumendo 0
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC41: [error] Numero documento deve contenere solo numeri e lettere")
        public void testCase41() {
            Impiegato i = createBaseImpiegato();
            i.setNumeroDocumento("AB123-456?");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC42: [error] Numero documento non deve essere nullo")
        public void testCase42() {
            Impiegato i = createBaseImpiegato();
            i.setNumeroDocumento("");
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC43: [error] Formato data di scadenza del documento non corretto")
        public void testCase43() {
            Impiegato i = createBaseImpiegato();
            i.setDataScadenza(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC44: [error] Data scadenza deve essere futura")
        public void testCase44() {
            Impiegato i = createBaseImpiegato();
            i.setDataScadenza(LocalDate.now().minusYears(1));
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC45: [error] Campo data scadenza non deve essere vuoto")
        public void testCase45() {
            Impiegato i = createBaseImpiegato();
            i.setDataScadenza(null);
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }

        @Test
        @DisplayName("TC46: [error] Database non disponibile")
        public void testCase46() {
            Impiegato i = createBaseImpiegato();
            Assertions.assertThrows(InvalidInputException.class, () -> manager.addImpiegato(i));
        }
    }

}
