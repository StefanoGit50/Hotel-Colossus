package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
* da finire
* */

@ExtendWith(MockitoExtension.class)
public class prenotazioneDAOTesting{

    private PrenotazioneDAO prenotazioneDAO;
    private Prenotazione prenotazione;

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        ServizioDAO dao = new ServizioDAO();
        Servizio servizio = null;
        try {
            servizio = dao.doRetriveByKey("Spa e Benessere");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(servizio);
        clientes.add(new Cliente("Mario","Rossi","Napoli","Napoli","Via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera(101,Stato.Occupata,2,120,"Camera matrimoniale vista mare","")));
        servizios.add(servizio);
        cameras.add(new Camera(101,Stato.Occupata,2,120,"Camera matrimoniale vista mare",""));
        Prenotazione prenotazione = new Prenotazione(
            LocalDate.o

        );
        prenotazioneDAO = new PrenotazioneDAO();
    }

    @Test
    @Tag("True")
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException{
       assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tag("False")
    @DisplayName("doSave() quando è tutto False")
    public void doSaveAllFalse() throws SQLException{
        prenotazione.setListaServizi(new ArrayList<>());
       // prenotazione.setListaCamere(new ArrayList<>());
        prenotazione.setListaClienti(new ArrayList<>());
        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doSave() quando tira una eccezione")
    public void doSaveException(){
       assertThrows(SQLException.class,()->prenotazioneDAO.doSave(null));
    }

    @Test
    @Tag("False")
    @DisplayName("doSave() quando la condizione del for del cliente è falsa")
    public void doSaveSecondoForCliente() throws SQLException {
        prenotazione.setListaClienti(new ArrayList<>());
        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByKey() quando è tutto true")
    public void doRetriveByKeyAllTrue() throws SQLException {
        Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey(1);
        assertEquals(prenotazione,prenotazione1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando va in errore")
    public void doRetriveByKeyReturnNull() throws SQLException {
        assertNull(prenotazioneDAO.doRetriveByKey(" "));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando resultSet.next() restituisce false")
    public void doRetriveByKeyResultSetIsFalse(){
        assertThrows(NoSuchElementException.class,()->prenotazioneDAO.doRetriveByKey(7));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveByKey() quando resultSet.next() è true ma gli altri false")
    public void doRetriveByKeyAllFalseTranneIlPrimoResultSet() throws SQLException {
       Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey(1);
       System.out.println(prenotazione1);
       prenotazione.setListaClienti(new ArrayList<>());
       //prenotazione.setListaCamere(new ArrayList<>());
       prenotazione.setListaServizi(new ArrayList<>());
       prenotazione.setTrattamento(null);
       assertEquals(prenotazione,prenotazione1);
    }
    @Test
    @Tag("True")
    @DisplayName("doRetriveAll() quando va tutto bene")
    public void doRetriveAllAllTrue() throws SQLException {
        ArrayList<Prenotazione> prenotaziones;
        prenotaziones = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveAll("IDPrenotazione");
        ArrayList<Prenotazione> prenotaziones1 = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras  = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Laura","Verdi","Roma","Roma","Via Milano",23,100,"3339876543","Femmina",LocalDate.of(1990,4,5),"VRDLRA90D45F839Y","laura.verdi@email.it","Italiana",new Camera(102,Stato.Libera,4,180.0,"Camera familiare con balcone","")));
        cameras.add(new Camera(102,Stato.Libera,4,180.0,"Camera familiare con balcone",""));
        servizios.add(new Servizio("Spa e Benessere",40.0));
        prenotaziones1.add(prenotazione);
        /*
        prenotaziones1.add(new Prenotazione(1,                                          // ID
                LocalDate.of(2026, 6, 1),                   // Data Creazione
                LocalDate.of(2026, 8, 10),                  // Inizio
                LocalDate.of(2026, 8, 20),                  // Fine
                LocalDate.of(2026, 6, 1),                   // Data Ricevuta (Pagato subito)
                new Trattamento("All Inclusive", 1500.0),   // Trattamento
                "Carta Identità",                           // Doc Tipo
                LocalDate.of(2020, 5, 10),                  // Doc Rilascio
                LocalDate.of(2030, 5, 10),                  // Doc Scadenza
                "Marco Verdi",                              // Intestatario
                "Bambino di 2 anni",                        // Note
                cameras, servizios, clientes,                // Liste
                "CA123456",                                 // Num Doc
                "Carta di Credito"                          // Pagamento
                 ));
        prenotaziones1.add(new Prenotazione(2,
                LocalDate.now(),
                LocalDate.now().plusDays(2),                // Arriva tra 2 giorni
                LocalDate.now().plusDays(4),
                null,                                       // NULL: Non ha ancora pagato
                new Trattamento("Pernottamento e Colazione", 120.0),
                "Patente",
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2028, 1, 1),
                "Tech Solutions SRL",                       // Intestatario Azienda
                "Fattura elettronica richiesta",
                cameras, servizios, clientes,
                "PAT98765",
                "Bonifico Bancario"));
        prenotaziones1.add(new Prenotazione(3,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 14),                  // San Valentino
                LocalDate.of(2026, 2, 16),
                LocalDate.of(2026, 2, 1),
                new Trattamento("Mezza Pensione", 300.0),
                "Passaporto",
                LocalDate.of(2019, 6, 15),
                LocalDate.of(2029, 6, 14),
                "Luca Bianchi",
                "Bottiglia di vino in camera",
                cameras, servizios, clientes,
                "YA556677",
                "Paypal"));
        prenotaziones1.add(new Prenotazione(4,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 8),
                null,                                       // Acconto versato ma saldo al check-in
                new Trattamento("Solo Pernottamento", 800.0),
                "Carta Identità",
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2031, 1, 1),
                "Anna Gialli",
                "Camere vicine se possibile",
                cameras, servizios, clientes,
                "CA998877",
                "Contanti"));
        prenotaziones1.add(new Prenotazione(5,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20),
                LocalDate.of(2025, 12, 1),
                new Trattamento("Pensione Completa Deluxe", 5000.0),
                "Passaporto",
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2032, 10, 10),
                "Mr. John Smith",
                "Privacy assoluta",
                cameras, servizios, clientes,
                "UK123456789",
                "Amex Black"));
         */
        assertEquals(prenotaziones1,prenotaziones);
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando va tutto male")
    public void doRetriveAll() throws SQLException{
        ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
        assertEquals(prenotaziones,prenotazioneDAO.doRetriveAll("IDPrenotazione"));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando")
    public void doRetriveAllAllFalseTranneilPrimoResultSet() throws SQLException {
        ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
        Prenotazione prenotazione1 = new Prenotazione();
        prenotazione1.setIDPrenotazione(1);
        prenotazione1.setDataCreazionePrenotazione(LocalDate.of(2026,1,15));
        prenotazione1.setDataInizio(LocalDate.of(2026,2,1));
        prenotazione1.setDataFine(LocalDate.of(2026,2,5));
        prenotazione1.setDataRilascio(LocalDate.of(2020,1,15));
        prenotazione1.setDataScadenza(LocalDate.of(2030,1,1));
        prenotazione1.setTrattamento(null);
        prenotazione1.setNumeroDocumento("AX123456");
        prenotazione1.setListaClienti(new ArrayList<>());
        //prenotazione1.setListaCamere(new ArrayList<>());
        prenotazione1.setListaServizi(new ArrayList<>());
        prenotazione1.setNoteAggiuntive("Richiesta camera silenziosa");
        prenotazione1.setIntestatario("Mario Rossi");
        prenotazione1.setTipoDocumento("Carta Identità");
        prenotazione1.setStatoPrenotazione(true);
        prenotazione1.setCheckIn(false);
        Prenotazione prenotazione2 = new Prenotazione();
        prenotazione2.setIDPrenotazione(2);
        prenotazione2.setTipoDocumento("Passaporto");
        prenotazione2.setDataRilascio(LocalDate.of(2019,5,10));
        prenotazione2.setDataScadenza(LocalDate.of(2029,5,10));
        prenotazione2.setDataFine(LocalDate.of(2026,2,15));
        prenotazione2.setDataInizio(LocalDate.of(2026,2,10));
        prenotazione2.setDataCreazionePrenotazione(LocalDate.of(2026,1,20));
        prenotazione2.setTrattamento(null);
        prenotazione2.setNoteAggiuntive("Nessuna nota particolare");
        prenotazione2.setIntestatario("Laura Verdi");
        prenotazione2.setNumeroDocumento("BC789012");
        prenotazione2.setListaServizi(new ArrayList<>());
       //prenotazione2.setListaCamere(new ArrayList<>());
        prenotazione2.setListaClienti(new ArrayList<>());
        prenotazione2.setStatoPrenotazione(true);
        prenotazione2.setCheckIn(false);
        prenotaziones.add(prenotazione1);
        prenotaziones.add(prenotazione2);

        ArrayList<Prenotazione> prenotaziones1 = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveAll("IDPrenotazione");
        assertEquals(prenotaziones,prenotaziones1);
    }


    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doUpdate() quando da l'eccezzione")
    public void doUpdateException() throws SQLException {
    }

    @Test
    @Tag("True")
    @DisplayName("doUpdate() quando va tutto bene")
    public void doUpdateAllTrue() throws SQLException {

    }

    @Test
    @Tag("False")
    @DisplayName("doUpdate() quando non va bene")
    public void doUpdateAllFalse() throws SQLException {

    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByAttribute() quando va bene")
    public void doRetriveByAttribute() throws SQLException {


    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveByAttribute() quando va male")
    public void doRetriveByAttributeAllFalse() throws SQLException {
       Prenotazione or = new Prenotazione();
       Object c = "Nessuna nota";
       ArrayList<Prenotazione> prenotazione1 = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveByAttribute("NoteAggiuntive",c);
       ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
       prenotaziones.add(new Prenotazione());
       assertEquals();
    }





}