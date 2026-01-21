package blackbox.RegistraPrenotazione;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Tag("registraPrenotazione")
public class TestCasesRegistraPrenotazione {

    private static FrontDeskInterface frontDesk;
    private static ArrayList<Camera> listaCamere = new ArrayList<>();
    private static ArrayList<Servizio> listaServizi = new ArrayList<>();
    private static ArrayList<Cliente> listaClienti = new ArrayList<>();
    private static Trattamento trattamento;

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");

        // Lista clienti

        Cliente cliente1 = new Cliente(
                "Giulia", "Verdi", "Italiana", "Milano", "Milano",
                "Corso Vittorio Emanuele", 22, 20121, "3479876543", "F",
                LocalDate.of(1992, 8, 15), "VRDGLI92M55F205W",
                "giulia.verdi@gmail.com", "Carta di Credito"
        );

        Cliente cliente2 = new Cliente(
                "Marco", "Neri", "Italiana", "Roma", "Roma",
                "Via dei Condotti", 5, 11187, "3351122334", "M",
                LocalDate.of(1978, 11, 3), "NREMRA78S03H501U",
                "m.neri@provider.it", "PayPal"
        );

        Cliente cliente3 = new Cliente(
                "Sofia", "Bruno", "Italiana", "Firenze", "Firenze",
                "Piazza della Signoria", 1, 50122, "3294455667", "F",
                LocalDate.of(2001, 1, 25), "BRNSFO01A65D612Y",
                "sofia.bruno@studio.it", "Bonifico"
        );

        listaClienti.add(cliente1);
        listaClienti.add(cliente2);
        listaClienti.add(cliente3);

        // Lista camere

        Camera camera102 = new Camera(
                102,
                Stato.Libera,
                1,
                85.0,
                "Singola lato giardino, molto silenziosa"
        );

        Camera camera205 = new Camera(
                205,
                Stato.Occupata,
                2,
                150.0,
                "Deluxe con idromassaggio e minibar rifornito"
        );

        Camera camera404 = new Camera(
                404,
                Stato.Libera,
                4,
                250.0,
                "Suite Familiare, due stanze comunicanti, balcone ampio"
        );

        listaCamere.add(camera102);
        listaCamere.add(camera205);
        listaCamere.add(camera404);

        // Lista servizi

        Servizio servizioMinibar = new Servizio("Consumazione Minibar", 15.50);
        Servizio servizioParcheggio = new Servizio("Parcheggio Coperto", 10.00);
        Servizio servizioColazione = new Servizio("Colazione in Camera", 12.00);

        listaServizi.add(servizioMinibar);
        listaServizi.add(servizioParcheggio);
        listaServizi.add(servizioColazione);

        // Trattamento

        trattamento = new Trattamento("Mezza Pensione", 200);
    }

    /* ************************************************************************************************************** */

    /**
     * Crea una prenotazione con dati di base. Su questa prenotazione si costruiscono tutte le altre.
     * @return  {@code Prenotazione} base.
     */
    private Prenotazione createBasePrenotazione() {
        return new Prenotazione(
                1,  // ID
                LocalDate.now(), // Data Creazione
                LocalDate.of(2026, 1, 17),  // Data arrivo
                LocalDate.of(2026, 1, 20),  // Data
                trattamento, "Patente",
                LocalDate.of(2020, 1, 10), LocalDate.of(2030, 1, 10),
                listaClienti.getFirst().getNome(), "",
                new ArrayList<>(listaCamere.subList(0, 1)),
                new ArrayList<>(),
                new ArrayList<>(listaClienti.subList(0, 1)),
                987654321
        );
    }
    /* ************************************************************************************************************** */

    @Test // TC5: Formato data arrivo non valido
    public void testCase5() {
        Prenotazione p = createBasePrenotazione();
        p.setDataInizio(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC6: Data arrivo nel passato
    public void testCase6() {
        Prenotazione p = createBasePrenotazione();
        p.setDataInizio(LocalDate.of(2020, 1, 10));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC7: Data di arrivo è obbligatoria
    public void testCase7() {
        Prenotazione p = createBasePrenotazione();
        p.setDataInizio(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC8: Formato data di partenza non valido
    public void testCase8() {
        Prenotazione p = createBasePrenotazione();
        p.setDataFine(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC9: Data partenza uguale (o precedente) a data arrivo
    public void testCase9() {
        Prenotazione p = createBasePrenotazione();
        p.setDataFine(p.getDataInizio());
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC10: Data partenza passata
    public void testCase10() {
        Prenotazione p = createBasePrenotazione();
        p.setDataFine(LocalDate.of(2024, 1, 10));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC11: Data di partenza obbligatoria
    public void testCase11() {
        Prenotazione p = createBasePrenotazione();
        p.setDataFine(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC13: Campo servizi è obbligatorio
    public void testCase12() {
        Prenotazione p = createBasePrenotazione();
        p.setListaServizi(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC13: Trattamento obbligatorio
    public void testCase13() {
        Prenotazione p = createBasePrenotazione();
        p.setTrattamento(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC14: Numero clienti = 0
    public void testCase14() {
        Prenotazione p = createBasePrenotazione();
        p.setListaClienti(new ArrayList<>());
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC15: Numero camere = 0
    public void testCase15() {
        Prenotazione p = createBasePrenotazione();
        p.setListaCamere(new ArrayList<>());
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC16: Tipo documento è obbligatorio
    public void testCase16() {
        Prenotazione p = createBasePrenotazione();
        p.setTipoDocumento("");
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC17: Tipo documento non valido
    public void testCase17() {
        Prenotazione p = createBasePrenotazione();
        p.setTipoDocumento("Tessera sanitaria");
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC18: No Match capacità (2 clienti in 1 camera singola)
    public void testCase18() {
        Prenotazione p = createBasePrenotazione();
        p.setListaClienti(new ArrayList<>(listaClienti.subList(0, 2))); // 2 Clienti
        // Assumendo che la camera in subList(0,1) abbia capacità 1
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC20: Data rilascio futura
    public void testCase20() {
        Prenotazione p = createBasePrenotazione();
        p.setDataRilascio(LocalDate.now().plusYears(1));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC21: Data rilascio documento obbligatoria
    public void testCase21() {
        Prenotazione p = createBasePrenotazione();
        p.setDataRilascio(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC22: Data scadenza documenti deve essere successiva alla data rilascio documento
        public void testCase22() {
        Prenotazione p = createBasePrenotazione();
        p.setDataRilascio(p.getDataRilascio().minusYears(1));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC23: Scadenza precedente a rilascio
    public void testCase23() {
        Prenotazione p = createBasePrenotazione();
        p.setDataRilascio(LocalDate.of(2020, 1, 10));
        p.setDataScadenza(LocalDate.of(2019, 1, 10));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC24: Data scadenza documento passata
    public void testCase24() {
        Prenotazione p = createBasePrenotazione();
        p.setDataScadenza(LocalDate.of(2024, 1, 10));
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }

    @Test // TC25: Data scadenza è obbligatoria
    public void testCase25() {
        Prenotazione p = createBasePrenotazione();
        p.setDataScadenza(null);
        Assertions.assertThrows(InvalidInputException.class, () -> frontDesk.addPrenotazione(p));
    }
}
