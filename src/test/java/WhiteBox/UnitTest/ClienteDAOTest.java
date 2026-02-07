package WhiteBox.UnitTest;


import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.ClienteDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClienteDAOTest {

    private ClienteDAO clienteDAO;
    private Cliente cliente;

    @BeforeAll
    public static void setBeforeAll(){
        DBPopulator.cancel();
    }


    @BeforeEach
    public void setUp() throws SQLException {
        DBPopulator.populator();
        clienteDAO = new ClienteDAO();
        cliente = new Cliente("Laura","Bruno","Torino","Torino","Via Roma",34,10100,"+393367890123","F",LocalDate.of(1992,6,25),"BRNLRA92F25L219P","laura.bruno@email.it","Italiana",new Camera(101, Stato.Libera,2,20.0,"",""));
    }

    @AfterEach
    public void setAfter(){
        DBPopulator.cancel();
    }


    @Test
    @Tag("True")
    @DisplayName("TC11: doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
        assertDoesNotThrow(() -> clienteDAO.doSave(cliente));
    }

    @Test
    @Tag("True")
    @DisplayName("TC12: doRetriveByKey() quando va tutto bene")
    public void doRetriveByKeyAllTrue() throws SQLException{
        Cliente cliente1 = clienteDAO.doRetriveByKey("RSSMRA80A01H501U");
        assertEquals(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera()),cliente1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC13: doRetriveByKey() quando tira un eccezione")
    public void doRetriveByKeyException(){
        assertThrows(SQLException.class,()->clienteDAO.doRetriveByKey(cliente));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC14: doDelete() quando cliente è uguale null")
    public void doDeleteException() throws SQLException{
       assertThrows(NoSuchElementException.class,()->clienteDAO.doDelete(null));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC15: doDelete() quando non cancella nulla")
    public void doDelete(){
        DBPopulator.cancel();
        assertThrows(NoSuchElementException.class,()->clienteDAO.doDelete(cliente));
    }

    @Test
    @Tag("True")
    @DisplayName("TC16: doRetriveAll() quando True e quindi è decrescente")
    public void doRetriveAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Cliente> clientes;
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
        clientes1.add(new Cliente("Lucia","Bianchi","Palermo","Palermo","Via Roma",5,90100,"3381122334","F", LocalDate.of(1985,3,3),"BNCLCU85C03G273Z","lucia.bianchi@email.com","Italiana",new Camera()));
        clientes1.add(new Cliente("Hans","Muller","Berlin","Berlino","Alexanderplatz",1,10115,"+4915123456","M",LocalDate.of(1988,5,5),"MULLER88E05Z112K","hans.muller@de-mail.de","Tedesca",new Camera()));
        clientes1.add(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera()));
        clientes1.add(new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",20,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera()));
        clientes1.getFirst().setBlacklisted(true);

        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("False")
    @DisplayName("TC17: doRetriveAll() quando viene inserito in senso crescente")
    public void doRetriveAllFalse() throws SQLException{
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Cliente> clientes;
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("crescente");
        clientes1.add(new Cliente("Lucia","Bianchi","Palermo","Palermo","Via Roma",5,90100,"3381122334","F", LocalDate.of(1985,3,3),"BNCLCU85C03G273Z","lucia.bianchi@email.com","Italiana",new Camera()));
        clientes1.add(new Cliente("Hans","Muller","Berlin","Berlino","Alexanderplatz",1,10115,"+4915123456","M",LocalDate.of(1988,5,5),"MULLER88E05Z112K","hans.muller@de-mail.de","Tedesca",new Camera()));
        clientes1.add(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera()));
        clientes1.add(new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",20,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera()));
        clientes1.getFirst().setBlacklisted(true);

        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("False")
    @DisplayName("TC18: doRetriveAll() quando il resultSet.next() restituisce false")
    public void doRetriveResultSet() throws SQLException {
        DBPopulator.cancel();
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        ArrayList<Cliente> clientes;

        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("True")
    @DisplayName("TC19: doUpdate() quando va tutto bene")
    public void doUpdateAllTrue(){
        DBPopulator.cancel();
        DBPopulator.populator();
      Cliente cliente1  = new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",27,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera(202,Stato.Occupata,2,350.00,"Jacuzzi privata","Suite Presidenziale"));
        assertDoesNotThrow(()->clienteDAO.doUpdate(cliente1));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC20: doUpdate() quando lancia una eccezione")
    public void doUpdateAllFalse(){
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(NoSuchElementException.class,()->clienteDAO.doUpdate(null));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC21: doRetrivaByAttribute() quando viene mandata una eccezione")
    public void doRetriveByAttributeException(){
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(RuntimeException.class,()->clienteDAO.doRetriveByAttribute(null,null));
    }

    @Test
    @Tag("True")
    @DisplayName("TC22: doRetriveByAttribute() quando va tutto bene")
    public void doRetriveByAttributeAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("Hans","Muller","Berlin","Berlino","Alexanderplatz",1,10115,"+4915123456","M",LocalDate.of(1988,5,5),"MULLER88E05Z112K","hans.muller@de-mail.de","Tedesca",new Camera()));
        clientes.add(new Cliente("Mario","Rossi","Roma","Roma","Via del Corso",10,10000,"3331234567","M",LocalDate.of(1980,1,1),"RSSMRA80A01H501U","mario.rossi@email.com","Italiana",new Camera()));
        clientes.add(new Cliente("Luigi","Verdi","Milano","Milano","Corso Buenos Aires",20,20100,"3339876543","M",LocalDate.of(1990,2,2),"VRDLGI90B02F205K","luigi.verdi@email.com","Italiana",new Camera()));

        Object s = "M";
        ArrayList<Cliente> clientes1 = (ArrayList<Cliente>) clienteDAO.doRetriveByAttribute("Sesso",s);
        assertEquals(clientes,clientes1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC23: doRetriveByAttribute() quando va in eccezione")
    public void doRetriveByAttribute() throws SQLException{
        DBPopulator.cancel();
        DBPopulator.populator();
        Object s = "Maria";
        assertThrows(NoSuchElementException.class,()->clienteDAO.doRetriveByAttribute("Nome",s));
    }

}