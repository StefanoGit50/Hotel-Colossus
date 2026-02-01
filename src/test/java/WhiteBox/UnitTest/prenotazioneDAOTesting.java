package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.invoke.SwitchPoint;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
* da finire
* */
@ExtendWith(MockitoExtension.class)
public class prenotazioneDAOTesting {

    private PrenotazioneDAO prenotazioneDAO;
    private Prenotazione prenotazione;

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Mario","Rossi","Italiana","Napoli","Napoli","Via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera(101,Stato.Occupata,2,120,"Camera matrimoniale vista mare")));
        servizios.add(new Servizio("Spa",50.0));
        cameras.add(new Camera(101,Stato.Occupata,2,120,"Camera matrimoniale vista mare"));
        prenotazione = new Prenotazione(1,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),new Trattamento("Pensione Completa",300.0),"CartaIdentità", LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",cameras,servizios,clientes,"AA123456");
        prenotazioneDAO = new PrenotazioneDAO();
    }

    @Test
    @Tag("True")
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
        prenotazioneDAO.doSave(prenotazione);
    }
    @Test
    @Tag("False")
    @DisplayName("doSave() quando è tutto False")
    public void doSaveAllFalse() throws SQLException{
        prenotazione.setListaServizi(new ArrayList<>());
        prenotazione.setListaCamere(new ArrayList<>());
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
       // Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey();

      //  assertEquals(prenotazione,prenotazione1);
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
        clientes.add(new Cliente("Laura","Verdi","Italiana","Roma","Roma","Via Milano",23,100,"3339876543","Femmina",LocalDate.of(1990,4,5),"VRDLRA90D45F839Y","laura.verdi@email.it","Italiana",new Camera(102,Stato.Libera,4,180.0,"Camera familiare con balcone")));
        cameras.add(new Camera(102,Stato.Libera,4,180.0,"Camera familiare con balcone"));
        servizios.add(new Servizio("Spa e Benessere",40.0));
        prenotaziones1.add(prenotazione);
        prenotaziones1.add(new Prenotazione(2,LocalDate.of(2026,1,20),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),new Trattamento("Mezza Pensione",30.0),"Passaporto",LocalDate.of(2019,5,10),LocalDate.of(2029,5,10),"Laura Verdi","Nessuna nota particolare",cameras,servizios,clientes,"BC789012"));
        prenotaziones1.add(new Prenotazione(3,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),null,"CartaIDentità",LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),"AA123456"));
        prenotaziones1.add(new Prenotazione(4,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),null,"CartaIDentità",LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),"AA123456"));
        prenotaziones1.add(new Prenotazione(5,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),null,"CartaIDentità",LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),"AA123456"));
        prenotaziones1.add(new Prenotazione(6,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),null,"CartaIDentità",LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),"AA123456"));
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
        prenotazione1.setListaCamere(new ArrayList<>());
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
        prenotazione2.setListaCamere(new ArrayList<>());
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
      assertThrows(NullPointerException.class,()->prenotazioneDAO.doUpdate(null));
    }

    @Test
    @Tag("True")
    @DisplayName("doUpdate() quando va tutto bene")
    public void doUpdateAllTrue() throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Mario","Rossi","Italiana","Napoli","Napoli","via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera(101,Stato.Libera,2,120,"Camera doppia con vista mare")));
        servizios.add(new Servizio("Lavanderia",15.0));
        cameras.add(new Camera(101,Stato.Libera,2,120,"Camera doppia con vista mare"));
       assertDoesNotThrow(()->prenotazioneDAO.doUpdate(new Prenotazione(1,LocalDate.of(2026,1,15),LocalDate.of(2026,2,1),LocalDate.of(2026,2,5),new Trattamento("Pensione Completa",50.0),"Passaporto", LocalDate.of(2020,1,15),LocalDate.of(2030,1,1),"Maria Verdi","Richiesta camera silenziosa",cameras,servizios,clientes,"AX123456")));
    }

    @Test
    @Tag("False")
    @DisplayName("doUpdate() quando non va bene")
    public void doUpdateAllFalse() throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Mario","Rossi","Italiana","Napoli","Napoli","via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera(101,Stato.Libera,2,120,"Camera doppia con vista mare")));
        cameras.add(new Camera(101,Stato.Libera,2,120,"Camera doppia con vista mare"));
        assertDoesNotThrow(()->prenotazioneDAO.doUpdate(new Prenotazione(1,LocalDate.of(2026,1,15),LocalDate.of(2026,2,1),LocalDate.of(2026,2,5),null,"Passaporto", LocalDate.of(2020,1,15),LocalDate.of(2030,1,1),"Maria Verdi","Richiesta camera silenziosa",cameras,servizios,clientes,"AX123456")));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByAttribute() quando va bene")
    public void doRetriveByAttribute() throws SQLException {
        ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        servizios.add(new Servizio("Lavanderia",15.0));
        prenotaziones.add(new Prenotazione(1,LocalDate.of(2026,1,15),LocalDate.of(2026,2,1),LocalDate.of(2026,2,5),new Trattamento("Pensione Completa",50.0),"Carta Identità", LocalDate.of(2020,1,15),LocalDate.of(2030,1,1),"Mario Rossi","Richiesta camera silenziosa",cameras,servizios,clientes,"AX123456"));
        Object o = "AX123456";
        assertEquals(prenotaziones,prenotazioneDAO.doRetriveByAttribute("numeroDocumento",o));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveByAttribute() quando va male")
    public void doRetriveByAttributeAllFalse() throws SQLException {
       Prenotazione or = new Prenotazione();
       Object c = "";
       ArrayList<Prenotazione> prenotazione1 = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveByAttribute("Intestatario",c);

    }





}