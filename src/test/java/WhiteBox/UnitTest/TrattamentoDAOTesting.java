package WhiteBox.UnitTest;


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

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private Trattamento trattamento;

    @Mock
    private Statement statement;

    @InjectMocks
    private TrattamentoDAO trattamentoDAO;

    private MockedStatic<ConnectionStorage> connectionSM;
    @BeforeEach
    public void setUP(){
        trattamento = new Trattamento("Mezza Pensione",30);
        connectionSM = mockStatic(ConnectionStorage.class);
        connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);
    }
    @AfterEach
    public void setAfter(){
        connectionSM.close();
    }

    @Test
    @Tag("True")
    @DisplayName("doDelete() quando va tutto a buon fine")
    public void doDeleteAllTrue() throws SQLException {
        when(connection.prepareStatement("DELETE FROM Trattamento WHERE Nome = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
       doNothing().when(preparedStatement).setString(1,"Mezza Pensione");

       assertDoesNotThrow(()->trattamentoDAO.doDelete(trattamento));
    }


    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doDelete() quando va in eccezione")
    public void doDeleteExecuteUpdateUgualeAZero()throws SQLException{
        when(connection.prepareStatement("DELETE FROM Trattamento WHERE Nome = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doDelete(trattamento));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doDelete() quando va in eccezione")
    public void doDeleteExecuteUpdate(){
        assertThrows(SQLException.class,()->trattamentoDAO.doDelete(null));
    }


    @Test
    @Tag("True")
    @DisplayName("doRetriveByKeyAllTrue() quando va tutto a buon fine")
    public void doRetriveByKeyAllTrue() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM Trattamento WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1,"Mezza Pensione");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("Nome")).thenReturn("Mezza Pensione");
        when(resultSet.getDouble("Prezzo")).thenReturn(30.0);

        assertEquals(new Trattamento("Mezza Pensione",30.0),trattamentoDAO.doRetriveByKey("Mezza Pensione"));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando resultSet restituisce false")
    public void doRetriveByKeyResultSetFalse() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM Trattamento WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(1,"Pensione Intera");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByKey("Pensione Intera"));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doRetriveByKey() quando passi un parametro diverso da una stringa")
    public void doRetriveByKeyParametroDiversoStringa(){
        assertThrows(NoSuchElementException.class,()->trattamentoDAO.doRetriveByKey(123));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveByKey() quando va tutto bene ")
    public void doRetriveAllAllTrue() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM Trattamento ORDER BY Nome DESC ")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true , true , true , false);
        when(resultSet.getString("Nome")).thenReturn("Mezza Pensione");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);
        ArrayList<Trattamento> trattamentos = new ArrayList<>();

        trattamentos.add(new Trattamento("Mezza Pensione",20.0));
        trattamentos.add(new Trattamento("Mezza Pensione",20.0));
        trattamentos.add(new Trattamento("Mezza Pensione",20.0));

        assertEquals(trattamentos,trattamentoDAO.doRetriveAll("decrescente"));
    }

    @Tag("False")
    @Test
    @DisplayName("doRetriveAll() quando tutto è falso")
    public void doRetriveAllAllFalse() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM Trattamento ORDER BY Nome ASC")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        ArrayList<Trattamento> trattamentos = new ArrayList<>();

        assertEquals(trattamentos,trattamentoDAO.doRetriveAll("crescente"));
    }


    @Tag("True")
    @Test
    @DisplayName("doUpdate() quando va tutto bene")
    public void doUpdate() throws SQLException {
        when(connection.prepareStatement("UPDATE Trattamento SET Prezzo = ? WHERE Nome = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setDouble(anyInt(),anyDouble());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        trattamentoDAO.doUpdate(trattamento);
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doUpdate() quando tira una eccezione")
    public void doUpdateException(){
        assertThrows(SQLException.class,()->trattamentoDAO.doUpdate(null));
    }

    @Tag("True")
    @Test
    @DisplayName("doRetriveByAttribute() quando va tutto bene")
    public void doRetriveByAttributeAllTrue() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM trattamento where " + anyString() + " = ?")).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setObject(1,"Mezza Pensione");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true , true , true , false);
        when(resultSet.getString("Nome")).thenReturn("Mezza Pensione");
        when(resultSet.getDouble("Prezzo")).thenReturn(20.0);

        ArrayList<Trattamento> trattamentos = new ArrayList<>();
        trattamentos.add(new Trattamento("Mezza Pensione",20.0));
        trattamentos.add(new Trattamento("Mezza Pensione",20.0));
        trattamentos.add(new Trattamento("Mezza Pensione",20.0));

        assertEquals(trattamentos,trattamentoDAO.doRetriveByAttribute("Nome","Mezza Pensione"));
    }

    @Tags({@Tag("Exception"),@Tag("Error")})
    @Test
    @DisplayName("doRetriveByAttribute() se resultSet.next() ritorna false")
    public void doRetirveByAttributeResultSetReturnFalse() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM trattamento where " + anyString() + " = ?")).thenReturn(preparedStatement);

    }

}
