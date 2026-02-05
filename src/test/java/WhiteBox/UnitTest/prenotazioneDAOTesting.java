package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.*;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.ref.Cleaner;
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
        DBPopulator.populator();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        ServizioDAO dao = new ServizioDAO();
        Servizio servizio = null;
        try {
             servizio = dao.doRetriveByKey("Spa e Benessere");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        clientes.add(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera(101,Stato.Occupata,2,120,"Camera matrimoniale vista mare","")));
        servizios.add(servizio);
         prenotazione = new Prenotazione(
                LocalDate.of(2004,12,2),
                LocalDate.of(2004,12,14),
                LocalDate.of(2004,12,19),
                LocalDate.of(2004,12,24),
                new Trattamento("Mezza Pensione",35),
                35.0,
                "Carta d'identità",
                LocalDate.of(2002,12,11),
                LocalDate.of(2002,12,24),
                "Mario Mascheri",
                "Nessuna nota",
                servizios,
                clientes,
                "CA12346E",
                "Cripto",
                "Italiana"
        );
        prenotazioneDAO = new PrenotazioneDAO();
    }

    @AfterEach
    public void after(){
       DBPopulator.cancel();
    }

    @Test
    @Tag("True")
    @DisplayName("TC25: doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException{
        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tag("False")
    @DisplayName("TC26: doSave() quando è tutto False")
    public void doSaveAllFalse() throws SQLException{
        prenotazione.setListaServizi(new ArrayList<>());
        prenotazione.setListaClienti(new ArrayList<>());
        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tag("False")
    @DisplayName("TC27: doSave() quando la condizione del for del cliente è falsa")
    public void doSaveSecondoForCliente() throws SQLException {
        prenotazione.setListaClienti(new ArrayList<>());
        assertDoesNotThrow(()->prenotazioneDAO.doSave(prenotazione));
    }

    @Test
    @Tag("True")
    @DisplayName("TC28: doRetriveByKey() quando è tutto true")
    public void doRetriveByKeyAllTrue() throws SQLException{
        Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey(2);
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",20,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera(202,Stato.Occupata,2,350.0,"Jacuzzi privata","Suite Presidenziale")));
        servizios.add(new Servizio("Spa e Benessere",45.0));
        servizios.add(new Servizio("Colazione in camera",12.0));
        Prenotazione prenotazione2 = new Prenotazione(LocalDate.of(2024,2,1),LocalDate.of(2024,2,20),LocalDate.of(2024,2,25),null,new Trattamento("Pensione Completa",55.0),55.0,"Patente",LocalDate.of(2019,5,5),LocalDate.of(2029,5,5),"Luigi Verdi","Intolleranza Lattosio",servizios,clientes,"PT123456","Contanti","italiana");
        prenotazione2.setIDPrenotazione(2);
        prenotazione2.setCheckIn(true);
        System.out.println(prenotazione2);
        System.out.println(prenotazione1);
        assertEquals(prenotazione2,prenotazione1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC29: doRetriveByKey() quando va in errore")
    public void doRetriveByKeyException() throws SQLException {
        assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(" "));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC30: doRetriveByKey() quando resultSet.next() restituisce false")
    public void doRetriveByKeyResultSetIsFalse() throws SQLException {
        DBPopulator.cancel();
        assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(1));
    }

    @Test
    @Tag("False")
    @DisplayName("TC31: doRetriveByKey() quando resultSet.next() è true ma gli altri false")
    public void doRetriveByKeyAllFalseTranneIlPrimoResultSet() throws SQLException {
       Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey(1);
       System.out.println(prenotazione1);
       prenotazione.setListaClienti(new ArrayList<>());
       prenotazione.setListaServizi(new ArrayList<>());
       prenotazione.setTrattamento(null);
       assertEquals(prenotazione,prenotazione1);
    }
    @Test
    @Tag("True")
    @DisplayName("TC32: doRetriveAll() quando va tutto bene")
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
    @DisplayName("TC33: doRetriveAll() quando va tutto male")
    public void doRetriveAll() throws SQLException{
        ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
        assertEquals(prenotaziones,prenotazioneDAO.doRetriveAll("IDPrenotazione"));
    }

    @Test
    @Tag("False")
    @DisplayName("TC34: doRetriveAll() quando")
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
    @DisplayName("TC35: doUpdate() quando da l'eccezione")
    public void doUpdateException() throws SQLException {
        assertThrows(NullPointerException.class,()->prenotazioneDAO.doUpdate(null));
    }

    @Test
    @Tag("True")
    @DisplayName("TC36: doUpdate() quando va tutto bene")
    public void doUpdateAllTrue() throws SQLException {

    }

    @Test
    @Tag("False")
    @DisplayName("TC37: doUpdate() quando non va bene")
    public void doUpdateAllFalse() throws SQLException {

    }

    @Test
    @Tag("True")
    @DisplayName("TC38: doRetriveByAttribute() quando va bene")
    public void doRetriveByAttribute() throws SQLException {


    }

    @Test
    @Tag("False")
    @DisplayName("TC39: doRetriveByAttribute() quando va male")
    public void doRetriveByAttributeAllFalse() throws SQLException {
       ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
       ArrayList<Prenotazione> prenotaziones1 = new ArrayList<>();
       ArrayList<Cliente> clientes = new ArrayList<>();
       ArrayList<Cliente> clientes1 = new ArrayList<>();
       ArrayList<Servizio> servizios = new ArrayList<>();
       ArrayList<Servizio> servizios1 = new ArrayList<>();

       clientes.add(new Cliente("Hans","Muller","Berlin","Berlino","Alexanderplatz",1,10115,"+4915123456","M",LocalDate.of(1988,5,5),"MULLER88E05Z112K","hans.muller@de-mail.de","Tedesca",new Camera(201,Stato.Prenotata,3,180.0,"Vista mare laterale","Junior Suite")));
       //servizios.add(new Servizio(,));
      /*
       prenotaziones.add(new Prenotazione(
                    LocalDate.of(2024,2,15),
                    LocalDate.of(2024,3,1),
                    LocalDate.of(2024,3,7),
                        null,
                        new Trattamento("All Inclusive",),
               ));*/
    }
}