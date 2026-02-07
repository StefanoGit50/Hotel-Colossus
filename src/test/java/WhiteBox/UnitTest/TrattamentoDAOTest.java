package WhiteBox.UnitTest;


import it.unisa.Common.Cliente;
import it.unisa.Common.Trattamento;
import it.unisa.Storage.DAO.TrattamentoDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TrattamentoDAOTest {

    private Trattamento trattamento;
    private TrattamentoDAO trattamentoDAO;


    @BeforeEach
    public void setUP(){
        trattamento = new Trattamento("Mezza Pensione",40);
        trattamentoDAO = new TrattamentoDAO();
    }

    @Test
    @Tag("False")
    @DisplayName("TC51: doRetriveAll(String order) quando resultSet ritorna false")
    public void doRetriveAllResultSetFalse() throws SQLException{
        DBPopulator.cancel();
        ArrayList<Trattamento>  trattamento1 = new ArrayList<>();
        ArrayList<Trattamento> tramentos = (ArrayList<Trattamento>) trattamentoDAO.doRetriveAll("nome");
        assertEquals(trattamento1,tramentos);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC52: doRetriveByKey(Object nome) quando resultSet restituisce false")
    public void doRetriveByKeyResultSetFalse() throws SQLException {
        DBPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByKey("Mezza Pensione"));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC53: doRetriveByAttribute() se resultSet.next() ritorna false")
    public void doRetriveByAttributeResultSetReturnFalse() throws SQLException {
        DBPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByAttribute("Nome","Quarto di Pensione"));
    }

    @Test
    @Tag("True")
    @DisplayName("TC54: doDelete(Trattamento trattamento) quando va tutto a buon fine")
    public void doDeleteAllTrue() throws SQLException {
      DBPopulator.cancel();
      DBPopulator.populator();
      assertDoesNotThrow(()->trattamentoDAO.doDelete(trattamento));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC55: doDelete(Trattamento trattamento) quando va in eccezione")
    public void doDeleteExecuteUpdate(){
      DBPopulator.cancel();
      assertThrows(SQLException.class,()->trattamentoDAO.doDelete(null));
    }


    @Test
    @Tag("True")
    @DisplayName("TC56: doRetriveByKey(Object nome) quando va tutto a buon fine")
    public void doRetriveByKeyAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        Trattamento trattamento1 = new Trattamento("Pensione Completa",55);
        Trattamento trattamento2 = trattamentoDAO.doRetriveByKey("Pensione Completa");
        assertEquals(trattamento1,trattamento2);
    }



    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC57: doRetriveByKey(Object nome) quando passi un parametro diverso da una stringa")
    public void doRetriveByKeyParametroDiversoStringa(){
        DBPopulator.cancel();
        assertThrows(SQLException.class,()->trattamentoDAO.doRetriveByKey(new Cliente()));
    }

    @Tag("True")
    @Test
    @DisplayName("TC58: doRetriveAll(String order) quando va tutto bene ")
    public void doRetriveAllAllTrue() throws SQLException{
      DBPopulator.cancel();
      DBPopulator.populator();
      ArrayList<Trattamento> trattamento1 = (ArrayList<Trattamento>) trattamentoDAO.doRetriveAll("nome");
      ArrayList<Trattamento> trattamentos = new ArrayList<>();
      trattamentos.add(new Trattamento("All Inclusive",80.00));
        trattamentos.add(new Trattamento("Bed & Breakfast",15.00));
        trattamentos.add(new Trattamento("Mezza Pensione",35.00));
        trattamentos.add(new Trattamento("Pensione Completa",55.00));
        trattamentos.add(new Trattamento("Solo Pernottamento",1.00));
      assertEquals(trattamento1,trattamentos);
    }


    @Tag("False")
    @Test
    @DisplayName("TC59: doRetriveAll(String order) quando tutto è falso")
    public void doRetriveAllFalse() throws SQLException {
        DBPopulator.cancel();
        assertEquals(new ArrayList<>(),trattamentoDAO.doRetriveAll("nome"));
    }




    @Tag("True")
    @Test
    @DisplayName("TC60: doUpdate(Trattamento trattamento) quando va tutto bene")
    public void doUpdate() throws SQLException {
        DBPopulator.cancel();
        assertDoesNotThrow(()->trattamentoDAO.doUpdate(trattamento));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC61: doUpdate(Trattamento trattamento) quando tira una eccezione")
    public void doUpdateException(){
        assertThrows(SQLException.class,()->trattamentoDAO.doUpdate(null));
    }

    @Tag("True")
    @Test
    @DisplayName("TC62: doRetriveByAttribute() quando va tutto bene")
    public void doRetriveByAttributeAllTrue() throws SQLException{
      Object o = 35;
      DBPopulator.populator();
      ArrayList<Trattamento> trattamentos = (ArrayList<Trattamento>) trattamentoDAO.doRetriveByAttribute("Prezzo",o);
      ArrayList<Trattamento> trattamentos1 = new ArrayList<>();
      trattamentos1.add(new Trattamento("Mezza Pensione",35));
      assertEquals(trattamentos,trattamentos1);
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("TC63: doRetriveByAttribute(String attribute,Object value) se attribute è uguale a false e se value è uguale a null")
    public void doRetriveByAttributeAttributeisNull(){
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(RuntimeException.class,()->trattamentoDAO.doRetriveByAttribute(null,null));
    }
}
