package WhiteBox.UnitTest;


import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteDAOTesting {

    private ClienteDAO clienteDAO;
    private Cliente cliente;

    @BeforeEach
    void setUp() throws SQLException {
        clienteDAO = new ClienteDAO();
        cliente = new Cliente("Mario","Rossi","Italiana","Napoli","Napoli","via Roma",15,);
    }

    @AfterEach
    void shutdown() {

    }

    @Test
    @Tag("exeception")
    @DisplayName("Retrieve: Branch1: oggetto!=String")
    public void DoRetrievebyKeyBranch1TestException() {
        assertThrows(SQLException.class, () -> clienteDAO.doRetriveByKey());
    }

    @Test
    @Tag("true")
    @DisplayName("Retrieve: Branch2: oggetto ==String")
    public void DoRetrievebyKeyTesttrue() throws SQLException{

    }

    @Test
    @DisplayName("Retrieve: Branch 2 e branch 3 false")
    @Tags({@Tag("false"), @Tag("null")})

    public void DoRetrievebyKeyCapNull() throws SQLException {
    }


    @Test
    @DisplayName("Retrieve:branch 3 true cap not a number")
    @Tag("false")
    public void DoRetrievebyKeyTestCapFalse() throws SQLException {


    }


    @Test
    @DisplayName("Delete: Branch 1 cliente != null ")
    @Tag("true")
    public void DoDeleteTestTrue() throws SQLException{

    }

    @Test
    @DisplayName("Delete: Branch 1 cliente esiste elemento non trovato")
    @Tag("exception")
    public void DoDeleteTestNotFound() throws SQLException{

    }


    @Test
    @DisplayName("Delete: Branch 1 cliente == null ")
    @Tag("exception")
    public void DoDeleteTestFalse() throws SQLException{
        Cliente c= null;
        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doDelete(c));
    }

    @Test
    @DisplayName("RetrieveAll: branch 1 e branch 2 true")
    @Tag("true")
    public void DoRetrieveAll() throws SQLException{

    }


    @Test
    @DisplayName("RetrieveAll: branch 1 e branch 2 false")
    @Tag("false")
    public void testDoRetrieveAll_emptyResultSet() throws SQLException {

    }

    @Test
    @DisplayName("DoUpdate: branch 1 cliente != null o cliente cf !=null ")
    @Tag("true")
    public void testDoUpdateTrue() throws SQLException{


    }

    @Test
    @DisplayName("DoUpdate: branch 1 cliente == null o cliente cf ==null ")
    @Tags({@Tag("exception"),@Tag("false")})
    public void DoUpdateTestFalse() throws SQLException{
        Cliente c= null;
        assertThrows(NoSuchElementException.class, () ->  clienteDAO.doUpdate(c));
    }

    @Test
    @DisplayName("RetrieveAttribute: Branch 1 true Branch 2 true Branch 3 false")
    @Tag("true")
    public void testDoRetrieveAttributeTrue() throws SQLException{

    }

    @Test
    @DisplayName("RetrieveAttribute: branch2 false")
    @Tag("exception")@Tag("false")
    public void testDoRetrieveAttributeFalse() throws SQLException{
         }

    @Test
    @DisplayName("RetrieveAttribute: branch1 false")
    @Tag("exception")@Tag("false")
    public void testDoRetrieveAttributeFalse1() {

    }
}