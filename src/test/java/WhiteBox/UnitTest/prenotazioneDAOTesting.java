package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.*;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
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
        clientes.add(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera(101,Stato.Libera,2,80,"Camera matrimoniale vista mare","")));
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
        servizios.add(new Servizio("Colazione in Camera",12.0));
        Prenotazione prenotazione2 = new Prenotazione(LocalDate.of(2024,2,1),LocalDate.of(2024,2,20),LocalDate.of(2024,2,25),null,new Trattamento("Pensione Completa",55.0),55.0,"Patente",LocalDate.of(2019,5,5),LocalDate.of(2029,5,5),"Luigi Verdi","Intolleranza Lattosio",servizios,clientes,"PT123456","Contanti","italiana");
        prenotazione2.setIDPrenotazione(2);
        prenotazione2.setCheckIn(true);
        assertEquals(prenotazione2,prenotazione1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC29: doRetriveByKey() quando va in errore e il DB ")
    public void doRetriveByKeyException() throws SQLException{
        assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(" "));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC30: quando viene lancia l'eccezione SQLException ")
    public void doRetriveByKeyResultSetIsFalse() throws SQLException{
        DBPopulator.cancel();
        assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(1));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC31: doRetriveByKey() quando non trova la camera")
    public void doRetriveByKeyIlPrimoResultSetDaExcpetion() throws SQLException{
        DBPopulator.cancel();
       assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(1));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC32: doRetriveByKey() quando non trova il cliente")
    public void doRetriveByKeyIlSecondoResultSetDaException() throws SQLException{
        PrenotazioneDAO prenotazioneDAO1 = new PrenotazioneDAO();
        prenotazione.setTrattamento(null);
        prenotazioneDAO1.doSave(prenotazione);
        assertThrows(SQLException.class,()->prenotazioneDAO.doRetriveByKey(prenotazione.getIDPrenotazione()));
    }

    @Test
    @Tag("True")
    @DisplayName("TC33: doRetriveAll() quando va tutto bene")
    public void doRetriveAllAllTrue() throws SQLException {
        //prenotazioneDAO.doDelete();
        ArrayList<Prenotazione> prenotaziones = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveAll("IDPrenotazione");
        ArrayList<Prenotazione> prenotaziones1 = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Servizio> servizios = new ArrayList<>();
        System.out.println(prenotaziones);
        clientes.add(new Cliente("Hans","Muller","Berlin","Berlino","Alexanderplatz",1,10115,"+4915123456","M",LocalDate.of(1988,5,5),"MULLER88E05Z112K","hans.muller@de-mail.de","Tedesca",new Camera(201,Stato.Prenotata,3,180.0,"Vista mare laterale","Junior Suite")));
        servizios.add(new Servizio("Transfer Aeroporto",35.0));
        Prenotazione prenotazione1 = new Prenotazione(LocalDate.of(2024,2,15),LocalDate.of(2024,3,1), LocalDate.of(2024,3,7),null,new Trattamento("All Inclusive",80.0),80.0,"Passaporto",LocalDate.of(2021,1,1),LocalDate.of(2031,1,1),"Hans Muller","Late arrival expected",servizios,clientes,"PASSDE99","Bonifico","italiana");
        prenotazione1.setIDPrenotazione(3);
        servizios = new ArrayList<>();
        clientes = new ArrayList<>();
        clientes.add(new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",20,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera(202,Stato.Occupata,2,350.0,"Jacuzzi privata","Suite Presidenziale")));
        servizios.add(new Servizio("Spa e Benessere",45.0));
        servizios.add(new Servizio("Colazione in Camera",12.0));
        Prenotazione prenotazione2 = new Prenotazione(LocalDate.of(2024,2,1),LocalDate.of(2024,2,20),LocalDate.of(2024,2,25),null,new Trattamento("Pensione Completa",55.0),55.0,"Patente",LocalDate.of(2019,5,5),LocalDate.of(2029,5,5),"Luigi Verdi","Intolleranza Lattosio",servizios,clientes,"PT123456","Contanti","italiana");
        prenotazione2.setIDPrenotazione(2);
        prenotazione2.setCheckIn(true);

        prenotaziones1.add(prenotazione2);
        prenotaziones1.add(prenotazione1);
        assertEquals(prenotaziones1,prenotaziones);
    }

    @Test
    @Tag("False")
    @DisplayName("TC34: doRetriveAll() quando va tutto male")
    public void doRetriveAll() throws SQLException{
        DBPopulator.cancel();
        ArrayList<Prenotazione> prenotaziones = new ArrayList<>();
        assertEquals(prenotaziones,prenotazioneDAO.doRetriveAll("IDPrenotazione"));
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