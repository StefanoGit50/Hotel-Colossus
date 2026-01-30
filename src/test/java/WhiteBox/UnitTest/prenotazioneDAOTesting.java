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
import java.lang.invoke.SwitchPoint;
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
public class prenotazioneDAOTesting {

    private PrenotazioneDAO prenotazioneDAO;
    private Prenotazione prenotazione;

    @BeforeEach
    public void setUp(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        clientes.add(new Cliente("Mario","Rossi","Italiana","Napoli","Napoli","via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana"));
        servizios.add(new Servizio("Spa",50));
        cameras.add(new Camera(101,Stato.Libera,2,120,"Camera matrimoniale vista mare"));
        prenotazione = new Prenotazione(1,LocalDate.of(2026,2,1),LocalDate.of(2026,2,10),LocalDate.of(2026,2,15),new Trattamento("Pensione Completa",300),"CartaIDentità", LocalDate.of(2020,1,1),LocalDate.of(2026,2,9),"Mario Rossi","Nessuna nota",cameras,servizios,clientes,"AA123456");
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
        Prenotazione prenotazione1 = prenotazioneDAO.doRetriveByKey();
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando va in errore")
    public void doRetriveByKeyReturnNull() throws SQLException {
        assertNull(prenotazioneDAO.doRetriveByKey(" "));
    }

    public void doRetriveByKeyAllFalseTranne(){

    }

}