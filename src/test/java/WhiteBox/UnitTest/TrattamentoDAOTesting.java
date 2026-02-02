package WhiteBox.UnitTest;


import it.unisa.Common.Cliente;
import it.unisa.Common.Trattamento;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.TrattamentoDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrattamentoDAOTesting{

    private Trattamento trattamento;
    private TrattamentoDAO trattamentoDAO;


    @BeforeEach
    public void setUP(){
        trattamento = new Trattamento("Mezza Pensione",40);
        trattamentoDAO = new TrattamentoDAO();
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll(String order) quando resultSet ritorna false")
    public void doRetriveAllResultSetFalse() throws SQLException{
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        ArrayList<Trattamento>  trattamento1 = new ArrayList<>();
        ArrayList<Trattamento> tramentos = (ArrayList<Trattamento>) trattamentoDAO.doRetriveAll("crescente");
        assertEquals(trattamento1,tramentos);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey(Object nome) quando resultSet restituisce false")
    public void doRetriveByKeyResultSetFalse() throws SQLException {
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByKey("Mezza Pensione"));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doRetriveByAttribute() se resultSet.next() ritorna false")
    public void doRetriveByAttributeResultSetReturnFalse() throws SQLException {
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByAttribute("Nome","Quarto di Pensione"));
    }



    @Test
    @Tag("True")
    @DisplayName("doDelete(Trattamento trattamento) quando va tutto a buon fine")
    public void doDeleteAllTrue() throws SQLException {
      DBPopulator dbPopulator = new DBPopulator();
      dbPopulator.populator();
      assertDoesNotThrow(()->trattamentoDAO.doDelete(trattamento));
    }


    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doDelete(Trattamento trattamento) quando va in eccezione")
    public void doDeleteExecuteUpdateUgualeAZero()throws SQLException{
      DBPopulator dbPopulator = new DBPopulator();
      dbPopulator.cancel();
      assertThrows( NoSuchElementException.class,()->trattamentoDAO.doDelete(new Trattamento("",20.0)));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doDelete(Trattamento trattamento) quando va in eccezione")
    public void doDeleteExecuteUpdate(){
      DBPopulator dbPopulator = new DBPopulator();
      dbPopulator.cancel();
      assertThrows(SQLException.class,()->trattamentoDAO.doDelete(null));
    }


    @Test
    @Tag("True")
    @DisplayName("doRetriveByKey(Object nome) quando va tutto a buon fine")
    public void doRetriveByKeyAllTrue() throws SQLException {
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.populator();
        Trattamento trattamento1 = new Trattamento("Pensione Completa",55);
        Trattamento trattamento2 = trattamentoDAO.doRetriveByKey("Pensione Completa");
        assertEquals(trattamento1,trattamento2);
    }



    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doRetriveByKey(Object nome) quando passi un parametro diverso da una stringa")
    public void doRetriveByKeyParametroDiversoStringa(){
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByKey(new Cliente()));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveAll(String order) quando va tutto bene ")
    public void doRetriveAllAllTrue() throws SQLException {
      DBPopulator dbPopulator = new DBPopulator();
      dbPopulator.populator();
      ArrayList<Trattamento> trattamento1 = (ArrayList<Trattamento>) trattamentoDAO.doRetriveAll("decrescente");
      ArrayList<Trattamento> trattamentos = new ArrayList<>();
      trattamentos.add(new Trattamento("Solo Pernottamento",0));
      trattamentos.add(new Trattamento("Pensione Completa",50.0));
      trattamentos.add(new Trattamento("Mezza Pensione",35));
      trattamentos.add(new Trattamento("Bed & Breakfast",15));
      trattamentos.add(new Trattamento("All Inclusive",80));

      assertEquals(trattamento1,trattamentos);
    }


    @Tag("False")
    @Test
    @DisplayName("doRetriveAll(String order) quando tutto è falso")
    public void doRetriveAllFalse() throws SQLException {
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.populator();
        ArrayList<Trattamento> trattamentos = new ArrayList<>();
        ArrayList<Trattamento> trattamentos1 = (ArrayList<Trattamento>) trattamentoDAO.doRetriveAll("crescente");
        trattamentos.add(new Trattamento("All Inclusive",80));
        trattamentos.add(new Trattamento("Bed & Breakfast",15));
        trattamentos.add(new Trattamento("Mezza Pensione",35));
        trattamentos.add(new Trattamento("Pensione Completa",50.0));
        trattamentos.add(new Trattamento("Solo Pernottamento",0));
        assertEquals(trattamentos,trattamentos1);
    }




    @Tag("True")
    @Test
    @DisplayName("doUpdate(Trattamento trattamento) quando va tutto bene")
    public void doUpdate() throws SQLException {
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.cancel();
        assertDoesNotThrow(()->trattamentoDAO.doUpdate(trattamento));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doUpdate(Trattamento trattamento) quando tira una eccezione")
    public void doUpdateException(){
        assertThrows(SQLException.class,()->trattamentoDAO.doUpdate(null));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveByAttribute() quando va tutto bene")
    public void doRetriveByAttributeAllTrue() throws SQLException{
      Object o = 35;
      DBPopulator dbPopulator = new DBPopulator();
      dbPopulator.populator();
      ArrayList<Trattamento> trattamentos = (ArrayList<Trattamento>) trattamentoDAO.doRetriveByAttribute("Prezzo",o);
      ArrayList<Trattamento> trattamentos1 = new ArrayList<>();
      trattamentos1.add(new Trattamento("Mezza Pensione",35));
      assertEquals(trattamentos,trattamentos1);
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doRetriveByAttribute(String attribute,Object value) se attribute è uguale a false e se value è uguale a null")
    public void doRetriveByAttributeAttributeisNull(){
        DBPopulator dbPopulator = new DBPopulator();
        dbPopulator.populator();
        assertThrows(RuntimeException.class,()->trattamentoDAO.doRetriveByAttribute(null,null));
    }
}
