package WhiteBox.UnitTest;


import it.unisa.Common.Servizio;
import it.unisa.Storage.DAO.ServizioDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ServizioDAOTest {

    private Servizio servizio;

    private ServizioDAO servizioDAO;

    @BeforeEach
    public void setUP(){
        servizioDAO = new ServizioDAO();
        servizio = new Servizio("Minibar",30.0);
    }

    @DisplayName("TC38: doDelete(Servizio servizio) quando va tutto bene")
    @Test
    @Tag("True")
    public void doDeleteAllTrue() throws SQLException{
        DBPopulator.cancel();
        DBPopulator.populator();
        assertDoesNotThrow(()->servizioDAO.doDelete(new Servizio("WiFi Premium",5)));
    }
    @Test
    @DisplayName("TC39: doDelete(Servizio servizio) quando o la chiave di servizio o servizio sono uguale a null")
    @Tags({@Tag("Exception"),@Tag("Error")})
     public void doDeleteException() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(SQLException.class,()->servizioDAO.doDelete(null));
    }

    @Test
    @DisplayName("TC40: doRetriveByKey(Object nome) quando vado tutto a buon fine")
    @Tag("True")
    public void doRetriveByKeyAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        Servizio servizio1 = servizioDAO.doRetriveByKey("Spa e Benessere");
        Servizio servizio2 = new Servizio("Spa e Benessere",45);
        assertEquals(servizio2,servizio1);
    }
    @DisplayName("TC41: doRetriveByKey(Object nome) quando resultSet.next() ritorno false")
    @Tags({@Tag("Error"),@Tag("Exception"),@Tag("False")})
    @Test
    public void doRetriveByKeyResultSetIsFalse() throws SQLException{
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->servizioDAO.doRetriveByKey("Spa"));
    }

    @DisplayName("TC42: doRetriByKey(Object nome) quando il nome non è una stringa")
    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    public void doRetriveByKeyNomeNonèUnaStringa(){
        assertThrows(SQLException.class,()->servizioDAO.doRetriveByKey(servizio));
    }

    @Test
    @Tag("True")
    @DisplayName("TC43: doRetriveAll(String order) quando va tutto bene")
    public void doRetriveAllAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Servizio> servizios = new ArrayList<>();

        servizios.add(new Servizio("Parcheggio",10));
        servizios.add(new Servizio("WiFi Premium",5));
        servizios.add(new Servizio("Colazione in Camera",12));
        servizios.add(new Servizio("Late CheckOut",25));
        servizios.add(new Servizio("Spa e Benessere",45));
        servizios.add(new Servizio("Transfer Aeroporto",35));

        ArrayList<Servizio> servizios1 = (ArrayList<Servizio>) servizioDAO.doRetriveAll("nome");
        assertEquals(servizios,servizios1);
    }

    @Tag("False")
    @Test
    @DisplayName("TC44: doRetriveAll(String order) quando resultSet.next() restituisce uguale a false")
    public void doRetriveAllResultSetIsFalse() throws SQLException{
        DBPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->servizioDAO.doRetriveAll("nome"));
    }

    @Tag("True")
    @Test
    @DisplayName("TC45: doRetriveAll(String order) quando va tutto bene pero order = null")
    public void doRetriveAllCrescente() throws SQLException{
        ArrayList<Servizio> servizios = new ArrayList<>();
        DBPopulator.cancel();
        DBPopulator.populator();
        servizios.add(new Servizio("Parcheggio",10));
        servizios.add(new Servizio("WiFi Premium",5));
        servizios.add(new Servizio("Colazione in Camera",12));
        servizios.add(new Servizio("Late CheckOut",25));
        servizios.add(new Servizio("Spa e Benessere",45));
        servizios.add(new Servizio("Transfer Aeroporto",35));
        ArrayList<Servizio> servizios1 = (ArrayList<Servizio>) servizioDAO.doRetriveAll(null);
        assertEquals(servizios,servizios1);
    }

    @Tag("True")
    @DisplayName("TC46: doUpdate(Servizio servizio) quando va tutto bene")
    @Test
    public void doUpdateAllTrue() throws SQLException{
        servizio.setPrezzo(20);
        assertDoesNotThrow(()->servizioDAO.doUpdate(servizio));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC47: doUpdate(Servizio servizio) quando servizio è uguale a null")
    public void doUpdatException(){
        DBPopulator.cancel();
        assertThrows(SQLException.class,()->servizioDAO.doUpdate(null));
    }

    @Tag("True")
    @Test
    @DisplayName("TC48: doRetriveByAttribute(String attribute , Object value) quando va tutto bene ")
    public void doRetriveByAttributeAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Servizio> servizios = (ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome","Spa e Benessere");
        ArrayList<Servizio> servizios1 = new ArrayList<>();
        servizios1.add(new Servizio("Spa e Benessere",45));
        assertEquals(servizios,servizios1);
    }

    @Tags({@Tag("Exception"),@Tag("Error"),@Tag("False")})
    @Test
    @DisplayName("TC49: doRetriveByAttribute() quando resultSet.next() ritorna false")
    public void doRetriveByAttributeResultSetIsFalse() throws SQLException {
       DBPopulator.cancel();
       assertThrows(NoSuchElementException.class,()->servizioDAO.doRetriveByAttribute("Nome","Piscina"));
    }
    @Test
    @DisplayName("TC50: doRetriveByAttribute() quando da un Eccezione ")
    @Tags({@Tag("Error"),@Tag("Exception")})
    public void doRetriveByAttributeException(){
        assertThrows(RuntimeException.class,()->servizioDAO.doRetriveByAttribute(null,null));
    }
}