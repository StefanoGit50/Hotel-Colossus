package blackbox.RegistraPrenotazione;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.*;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Tag("registraPrenotazione")
public class TestCasesRegistraPrenotazione {

    public static FrontDeskInterface frontDesk;
    public static ArrayList<Camera> listaCamere = new ArrayList<>();
    public static ArrayList<Servizio> listaServizi = new ArrayList<>();
    public static ArrayList<Cliente> listaClienti = new ArrayList<>();
    public static Trattamento trattamento = new Trattamento("Mezza Pensione", 125.00);
    public static int autoIncrement; // simula l'autoincrement del DB

    @BeforeAll
    public static void istantiateNumberOfInstances(){
        // Numero di prenotazioni presenti nel sistema + 1 = prenotazione successiva ad essere memorizzata
        try {
            autoIncrement = new PrenotazioneDAO().doRetriveAll("").size() + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost:1099/GestionePrenotazioni");

        // Lista clienti

        Cliente cliente1 = new Cliente(
                "Luigi", "Verdi", "Milano", "Milano",
                "Corso Buenos Aires", 20, 20100, "3339876543", "M",
                LocalDate.of(1990, 2, 2), "VRDLGI90B02F205K",
                "luigi.verdi@email.com",
                "Italiana",
                new Camera()
        );

        Cliente cliente2 = new Cliente(
                "Mario", "Rossi", "Roma", "Roma",
                "Via del Corso", 10, 10000, "3331234567", "M",
                LocalDate.of(1920, 1, 1), "RSSMRA80A01H501U",
                "mario.rossi@email.com","Italiana",
                new Camera()
        );

        Cliente cliente3 = new Cliente(
                "Lucia", "Bianchi", "Palermo", "Palermo",
                "Via Roma", 5, 90100, "3381122334", "F",
                LocalDate.of(1985, 3, 3), "BNCLCU85C03G273Z",
                "lucia.bianchi@email.com","Italiana",
                new Camera()
        );

        listaClienti.add(cliente1);
        listaClienti.add(cliente2);
        listaClienti.add(cliente3);

        // Lista camere

        Camera camera101 = new Camera(
                101,
                Stato.Libera,
                2,
                80.0,
                "Vista interna",
                "Camera Standard"
        );

        Camera camera102 = new Camera(
                102,
                Stato.Occupata,
                2,
                80,
                "Vista strada",
                "Camera Standard"
        );

        Camera camera201 = new Camera(
                201,
                Stato.Prenotata,
                3,
                180.0,
                "Vista mare laterale",
                "Junior Suite"
        );

        listaCamere.add(camera101);
        listaCamere.add(camera102);
        listaCamere.add(camera201);

        // Lista servizi

        Servizio servizioMinibar = new Servizio("Parcheggio", 10.00);
        Servizio servizioParcheggio = new Servizio("WiFi Premium", 5.00);
        Servizio servizioColazione = new Servizio("Colazione in Camera", 12.00);

        listaServizi.add(servizioMinibar);
        listaServizi.add(servizioParcheggio);
        listaServizi.add(servizioColazione);

        // Trattamento

        trattamento = new Trattamento("Mezza Pensione", 35);
    }

    /* ************************************************************************************************************** */

    /**
     * Crea una prenotazione con dati di base. Su questa prenotazione si costruiscono tutte le altre.
     *
     * @return {@code Prenotazione} base.
     */
    public Prenotazione createBasePrenotazione() {
        ArrayList<Camera> camere = new ArrayList<>();
        ArrayList<Cliente> clienti = new ArrayList<>();
        camere.add(listaCamere.getFirst());
        clienti.add(listaClienti.getFirst());
        String nome = clienti.getFirst().getNome();

        return new Prenotazione(
                LocalDate.now(),                // 2. Data Creazione
                LocalDate.of(2026, 3, 17),    // 3. Data Inizio
                LocalDate.of(2026, 3, 20),    // 4. Data Fine
                null,                           // 5. DataEmissioneRicevuta (MANCAVA QUESTO)
                null,                    // 6. Trattamento
                trattamento.getPrezzo(),                      // 7. Tipo Documento
                "Patente",
                LocalDate.of(2020, 1, 10),      // 8. Data Rilascio
                LocalDate.of(2030, 1, 10),      // 9. Data Scadenza
                clienti.getFirst().getNome(),                          // 10. Intestatario
                "-",                             // 11. Note Aggiuntive
                camere,                         // 12. Lista Camere
                listaServizi,                   // 13. Lista Servizi
                clienti,                        // 14. Lista Clienti
                "CA123AA",                     // 15. Numero Documento
                "",
                "Italiana"// 16. Metodo Pagamento (MANCAVA QUESTO)
        );
    }
    /* ************************************************************************************************************** */

    /* ***************************** CASI DI SUCCESSO **************************** */

    @Nested
    @DisplayName("TESTING: RegistraPrenotazione")
    @Tag("success")
    class TestPassRegistraPrenotazione {
        @Test
        @DisplayName("TC1: [Success] Registrazione con servizi: nessuno")
        void testCase1() throws RemoteException{
            Prenotazione p = createBasePrenotazione(), campione;
            p.setTrattamento(null);
            p.setListaServizi(new ArrayList<>(listaServizi));
            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
            System.out.println(p);
            System.out.println(autoIncrement);
            try {
                campione = frontDesk.getPrenotazioneById(autoIncrement);
                Assertions.assertEquals(p, campione);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("TC2: [Success] Registrazione con servizi: 1")
        void testCase2() {
            Prenotazione p = createBasePrenotazione();
            p.setListaServizi(new ArrayList<>(listaServizi.subList(0, 1)));
            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));

           /* try {
                Assertions.assertEquals(p, frontDesk.getPrenotazioneById(p.getIDPrenotazione()));
            } catch (RemoteException e) {
                Assertions.fail(e.getMessage());
            }*/
        }

        @Test
        @DisplayName("TC3: [Success] Registrazione con servizi: 3 e CID")
        void testCase3() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("CID");
            p.setTrattamento(new Trattamento("Pensione Completa", 500));
            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
          /*  try {
                Assertions.assertEquals(p, frontDesk.getPrenotazioneById(p.getIDPrenotazione()));
            } catch (RemoteException e) {
                Assertions.fail(e.getMessage());
            }*/
        }

        @Test
        @DisplayName("TC4: [Success] Clienti multipli e camere multiple")
        void testCase4() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("Passaporto");
            p.setTrattamento(new Trattamento("Solo pernottamento", 80));
            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
            /*try {
                Assertions.assertEquals(p, frontDesk.getPrenotazioneById(p.getIDPrenotazione()));
            } catch (RemoteException e) {
                Assertions.fail(e.getMessage());
            }*/
        }
    }

    /* ***************************** CASI DI ERRORE **************************** */

    @Nested
    @DisplayName("TESTING: RegistraPrenotazione - [error]")
    @Tag("error")
    class TestFailRegistraPrenotazione {
        @Test
        @DisplayName("TC5: [error] Formato data arrivo non valido")
        public void testCase5() {
            Prenotazione p = createBasePrenotazione();
            p.setDataInizio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC6: [error] Data arrivo nel passato")
        public void testCase6() {
            Prenotazione p = createBasePrenotazione();
            p.setDataInizio(LocalDate.of(2020, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC7: [error] Data di arrivo è obbligatoria")
        public void testCase7() {
            Prenotazione p = createBasePrenotazione();
            p.setDataInizio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC8: [error] Formato data di partenza non valido")
        public void testCase8() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC9: [error] Data di partenza uguale (o precedente) alla data di arrivo")
        public void testCase9() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(p.getDataInizio());
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC10: [error] Data di partenza passata")
        public void testCase10() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(LocalDate.of(2024, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC11: [error] Data di partenza obbligatoria")
        public void testCase11() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC13: [error] Trattamento obbligatorio")
        public void testCase13() {
            Prenotazione p = createBasePrenotazione();
            p.setTrattamento(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC14: [error] Numero clienti = 0")
        public void testCase14() {
            Prenotazione p = createBasePrenotazione();
            p.setListaClienti(new ArrayList<>());
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC15: [error] Numero camere = 0")
        public void testCase15() {
            Prenotazione p = createBasePrenotazione();
            p.setListaCamere(new ArrayList<>());
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC16: [error] Tipo di documento è obbligatorio")
        public void testCase16() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC17: [error] Tipo di documento non valido")
        public void testCase17() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("Tessera sanitaria");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC18: [error] No Match capacità (2 clienti in 1 camera singola)")
        public void testCase18() {
            Prenotazione p = createBasePrenotazione();
            p.setListaClienti(new ArrayList<>(listaClienti.subList(0, 2))); // 2 Clienti
            // Assumendo che la camera in subList(0,1) abbia capacità 1
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC19: [error] Formato data rilascio")
        public void testCase19() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC20: [error] Data di rilascio futura")
        public void testCase20() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(LocalDate.now().plusYears(1));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC21: [error] Data di rilascio documento obbligatoria")
        public void testCase21() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC22: [error] Data di scadenza del documento successiva alla data di rilascio")
        public void testCase22() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(p.getDataScadenza());
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC23: [error] Data di scadenza del documento precedente alla data di rilascio")
        public void testCase23() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(LocalDate.of(2020, 1, 10));
            p.setDataScadenza(LocalDate.of(2019, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC24: [error] Data di scadenza del documento passata")
        public void testCase24() {
            Prenotazione p = createBasePrenotazione();
            p.setDataScadenza(LocalDate.of(2024, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC25: [error] Data di scadenza del documento è obbligatoria")
        public void testCase25() {
            Prenotazione p = createBasePrenotazione();
            p.setDataScadenza(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }
    }
}
