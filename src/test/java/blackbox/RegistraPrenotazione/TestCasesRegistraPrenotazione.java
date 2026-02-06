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
    public static ArrayList<Servizio> listaServizi = new ArrayList<>();
    public static ArrayList<Camera> listaCamere = new ArrayList<>();
    public static ArrayList<Cliente> listaClienti = new ArrayList<>();
    public static Trattamento trattamento = new Trattamento("Mezza Pensione", 35);
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
                LocalDate.of(1980, 1, 1), "RSSMRA80A01H501U",
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

        // lista camere

        listaCamere.add(camera101);
        listaCamere.add(camera102);
        listaCamere.add(camera201);
        cliente1.setCamera(camera101);
        cliente2.setCamera(camera102);
        cliente3.setCamera(camera201);

        // Lista clienti

        listaClienti.add(cliente1);
        listaClienti.add(cliente2);
        listaClienti.add(cliente3);

        // Lista servizi

        Servizio servizioMinibar = new Servizio("Parcheggio", 10.00);
        servizioMinibar.setId(1);
        Servizio servizioParcheggio = new Servizio("WiFi Premium", 5.00);
        servizioParcheggio.setId(2);
        Servizio servizioColazione = new Servizio("Colazione in Camera", 12.00);
        servizioColazione.setId(3);

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
        ArrayList<Cliente> cliente = new ArrayList<>();
        cliente.add(listaClienti.getFirst());

        return new Prenotazione(
                LocalDate.now(),                // 2. Data Creazione
                LocalDate.of(2026, 3, 17),    // 3. Data Inizio
                LocalDate.of(2026, 3, 20),    // 4. Data Fine
                null,                           // 5. DataEmissioneRicevuta (MANCAVA QUESTO)
                trattamento,                    // 6. Trattamento
                trattamento.getPrezzo(),                      // 7. Tipo Documento
                "Patente",
                LocalDate.of(2020, 1, 10),      // 8. Data Rilascio
                LocalDate.of(2030, 1, 10),      // 9. Data Scadenza
                cliente.getFirst().getNome() + " " + cliente.getFirst().getCognome(),  // 10. Intestatario
                null,                             // 11. Note Aggiuntive
                listaServizi,                   // 13. Lista Servizi
                cliente,                        // 14. Lista Clienti
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
            p.setIDPrenotazione(autoIncrement);
            p.setTrattamento(null);
            p.setPrezzoAcquistoTrattamento(0.0);
            p.setListaServizi(null);

            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
            try {
                campione = frontDesk.getPrenotazioneById(autoIncrement);
                autoIncrement++;
                Assertions.assertEquals(p, campione);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("TC2: [Success] Registrazione con servizi: 1")
        void testCase2() {
            Prenotazione p = createBasePrenotazione(), campione;
            ArrayList<Servizio> c = new ArrayList<>();
            c.add(listaServizi.getFirst());
            p.setListaServizi(c);
            p.setIDPrenotazione(autoIncrement);
            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));

            try {
                campione = frontDesk.getPrenotazioneById(autoIncrement);
                autoIncrement++;
                Assertions.assertEquals(p, campione);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("TC3: [Success] Registrazione con servizi: 3 e tipo documento: CID")
        void testCase3() {
            Prenotazione p = createBasePrenotazione(), campione;
            p.setTipoDocumento("CID");
            p.setTrattamento(new Trattamento("Pensione Completa", 55.0));
            p.setPrezzoAcquistoTrattamento(55.0);
            ArrayList<Cliente> c = new ArrayList<>();
            c.add(listaClienti.get(1));
            p.setListaClienti(c);
            c.add(listaClienti.getFirst());
            p.setIntestatario(c.getFirst().getNome() + " " + c.getFirst().getCognome());
            p.setIDPrenotazione(autoIncrement);

            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
            try {
                campione = frontDesk.getPrenotazioneById(autoIncrement);
                autoIncrement++;
                Assertions.assertEquals(p, campione);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("TC4: [Success] Clienti multipli (2) e camere multiple (2) con tipo documento: Passaporto")
        void testCase4() {
            Prenotazione p = createBasePrenotazione(), campione;
            p.setTipoDocumento("Passaporto");
            p.setTrattamento(new Trattamento("Solo pernottamento", 0));
            p.setPrezzoAcquistoTrattamento(0.0);
            ArrayList<Cliente> c = new ArrayList<>();
            c.add(listaClienti.get(1));
            c.add(listaClienti.getFirst());  // 2 clienti -> 2 camere diverse
            p.setIntestatario(c.getFirst().getNome() + " " + c.getFirst().getCognome());
            p.setListaClienti(c);
            p.setIDPrenotazione(autoIncrement);

            Assertions.assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
            try {
                campione =  frontDesk.getPrenotazioneById(autoIncrement);
                Assertions.assertEquals(p, frontDesk.getPrenotazioneById(p.getIDPrenotazione()));
                autoIncrement++;
            } catch (RemoteException e) {
                Assertions.fail(e.getMessage());
            }
        }
    }

    /* ***************************** CASI DI ERRORE **************************** */

    @Nested
    @DisplayName("TESTING: RegistraPrenotazione - [error]")
    @Tag("error")
    class TestFailRegistraPrenotazione {
        @Test
        @DisplayName("TC5: [error] Data arrivo nel passato")
        public void testCase5() {
            Prenotazione p = createBasePrenotazione();
            p.setDataInizio(LocalDate.of(2020, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC6: [error] Data di arrivo è obbligatoria")
        public void testCase6() {
            Prenotazione p = createBasePrenotazione();
            p.setDataInizio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC7: [error] Data di partenza uguale (o precedente) alla data di arrivo")
        public void testCase7() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(p.getDataInizio());
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC8: [error] Data di partenza passata")
        public void testCas8e() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(LocalDate.of(2024, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC9: [error] Data di partenza obbligatoria")
        public void testCase9() {
            Prenotazione p = createBasePrenotazione();
            p.setDataFine(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC10: [error] Almeno un cliente obbligatorio")
        public void testCase10() {
            Prenotazione p = createBasePrenotazione();
            p.setListaClienti(new ArrayList<>(0));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC11: [error] Almeno una camera obbligatoria")
        public void testCase11() {
            Prenotazione p = createBasePrenotazione();
            p.setListaClienti(new ArrayList<>(0));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC12: [error] Tipo di documento è obbligatorio")
        public void testCase12() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC13: [error] Tipo di documento non valido")
        public void testCase13() {
            Prenotazione p = createBasePrenotazione();
            p.setTipoDocumento("Tessera sanitaria");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC14: [error] Numero clienti eccede la capacità totale delle camere")
        public void testCase14() throws CloneNotSupportedException{
            Prenotazione p = createBasePrenotazione();
            ArrayList<Cliente> clientes = new ArrayList<>(3);
            for (Cliente c : listaClienti) {
                clientes.add(c.clone());
            }
            for(Cliente c : clientes) {
                c.setCamera(listaCamere.getFirst().clone());
            }
            p.setListaClienti(clientes); // 4 Clienti - 2 posti camera
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC15: [error] Data di rilascio futura")
        public void testCase15() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(LocalDate.of(2026, 2, 20));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC16: [error] Data di rilascio documento obbligatoria")
        public void testCase16() {
            Prenotazione p = createBasePrenotazione();
            p.setDataRilascio(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC17: [error] Data di scadenza del documento precedente alla data di rilascio")
        public void testCase17() {
            Prenotazione p = createBasePrenotazione();
            p.setDataScadenza(LocalDate.of(2019, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC18: [error] Data di scadenza del documento passata")
        public void testCase18() {
            Prenotazione p = createBasePrenotazione();
            p.setDataScadenza(LocalDate.of(2024, 1, 10));
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC19: [error] Data di scadenza del documento è obbligatoria")
        public void testCase19() {
            Prenotazione p = createBasePrenotazione();
            p.setDataScadenza(null);
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC20: [error] Formato numero documento errato")
        public void testCase20() {
            Prenotazione p = createBasePrenotazione();
            p.setNumeroDocumento("CA123!");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC21: [error] Numero documento è obbligatorio")
        public void testCase21() {
            Prenotazione p = createBasePrenotazione();
            p.setNumeroDocumento("CA123!");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC22: [error] Lunghezza numero documento eccede lughezza massima")
        public void testCase22() {
            Prenotazione p = createBasePrenotazione();
            p.setNumeroDocumento("CA123456789AA");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC23: [error] Formato del numero documento deve contenere solo caratteri alfanumerici")
        public void testCase23() {
            Prenotazione p = createBasePrenotazione();
            p.setNumeroDocumento("CA123!!");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("TC24: [error] Campo numero documento obbligatorio")
        public void testCase24() {
            Prenotazione p = createBasePrenotazione();
            p.setNumeroDocumento("");
            assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
        }
    }
}
