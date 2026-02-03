package WhiteBox.UnitTest;


import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.ClienteDAO;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
        cliente = new Cliente("Laura","Bruno","Torino","Torino","Via Roma",34,10100,"+393367890123","F",LocalDate.of(1992,6,25),"BRNLRA92F25L219P","laura.bruno@email.it","Italiana",new Camera(101, Stato.Libera,2,20.0,"",""));
    }

    @Test
    @Tag("True")
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
        assertDoesNotThrow(() -> clienteDAO.doSave(cliente));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByKey() quando va tutto bene")
    public void doRetriveByKeyAllTrue() throws SQLException{
        Cliente cliente1 = clienteDAO.doRetriveByKey("BRNLRA92F25L219P");
        assertEquals(cliente,cliente1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByKey() quando tira un eccezione")
    public void doRetriveByKeyException(){
        assertThrows(SQLException.class,()->clienteDAO.doRetriveByKey(cliente));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveByKey() quando non trova niente")
    public void doRetriveByKey() throws SQLException {
      Cliente cliente1 = new Cliente();
      final Cliente [] cliente2 = new Cliente[1];
      assertDoesNotThrow(()->{cliente2[0] = clienteDAO.doRetriveByKey("RSSMRA20T11H703F");});
      assertEquals(cliente1,cliente2[0]);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doDelete() quando cliente è uguale null")
    public void doDeleteException() throws SQLException{
       assertThrows(NoSuchElementException.class,()->clienteDAO.doDelete(null));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doDelete() quando non cancella nulla")
    public void doDelete(){
        assertThrows(NoSuchElementException.class,()->clienteDAO.doDelete(cliente));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveAll() quando True e quindi è decrescente")
    public void doRetriveAllTrue() throws SQLException {
        ArrayList<Cliente> clientes;
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
        Cliente cliente1 = new Cliente("Marco","Santi","Genova","Genova","Via Garibaldi",89,16100,"+393345678901","M",LocalDate.of(1987,7,30),"SNTMRC87G30G273N","marco.santi@email.it","Italiana",new Camera());
        cliente1.setBlacklisted(true);
        clientes1.add(cliente1);
        clientes1.add(new Cliente("Pietro","Ricci","Roma","Roma","Via Veneto",78,00100,"+393387654321","M",LocalDate.of(1990,3,15),"RCCPTR90C15L736Y","pietro.ricci@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Anna","Moretti","Firenze","Firenze","Via Tornabuoni",23,35100,"+393349876543","F",LocalDate.of(1985,2,2),"MRTANA85B42F205W","anna.moretti@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Andrea","Marini","Padova","Padova","Via del Santo",56,35100,"+393312345678","M", LocalDate.of(1991,7,20),"MRNAND91L20D612R","andrea.marini@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Sara","Lombardi","Firenze","Firenze","Borgo Pinti",12,50122,"+393323456789","F",LocalDate.of(1993,6,12),"LMBSRA93H12F205Q","sara.lombardi@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Francesco","Greco","Napoli","Napoli","Via Chiaia",67,80100,"+393323456789","F",LocalDate.of(1995,5,10),"GRCFNC95E10F839K","francesco.greco@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Leonardo","Ferrari","Milano","Milano","Via Dante",15,20100,"+393331234567","M",LocalDate.of(1980,1,1),"FRRLRD80A01H501Z","leonardo.ferrari@email.it","Italiana",new Camera(202,Stato.Libera,2,110,"Panoramica","Camera Superior")));
        clientes1.add(new Cliente("Claudia","Fontana","Milano","Milano","Viale Monza",91,20122,"+393390123456","F",LocalDate.of(1989,8,15),"FNTCLD89M15H501S","claudia.fontana@email.it","Italiana",new Camera()));
        clientes1.add(new Cliente("Giulia","Costa","Milano","Milano","Corso Buenos Aires",45,20121,"+393356789012","F",LocalDate.of(1988,4,20),"CSTGLT88D20H501M","giulia.costa@email.it","Italiana",new Camera()));
        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando viene inserito in senso crescente")
    public void doRetriveAllFalse() throws SQLException{
        ArrayList<Cliente> clientes;
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("crescente");

       clientes1.add(new Cliente("Mario","Rossi","Napoli","Napoli","Via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera()));
       clientes1.add(new Cliente("Laura","Verdi","Roma","Roma","Via Milano",23,100,"3339876543","Femmina",LocalDate.of(1990,4,5),"VRDLRA90D45F839Y","laura.verdi@email.it","Italiana",new Camera()));

        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando il resultSet.next() restituisce false")
    public void doRetriveResultSet() throws SQLException {
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        ArrayList<Cliente> clientes;

        clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
        assertEquals(clientes1,clientes);
    }

    @Test
    @Tag("True")
    @DisplayName("doUpdate() quando va tutto bene")
    public void doUpdateAllTrue(){
        assertDoesNotThrow(()->clienteDAO.doUpdate(cliente));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doUpdate() quando lancia una eccezione")
    public void doUpdateAllFalse(){
        assertThrows(NoSuchElementException.class,()->clienteDAO.doUpdate(null));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetrivaByAttribute() quando viene mandata una eccezione")
    public void doRetriveByAttributeException(){
      assertThrows(RuntimeException.class,()->clienteDAO.doRetriveByAttribute(null,null));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveByAttribute() quando va tutto bene")
    public void doRetriveByAttributeAllTrue() throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("Mario","Rossi","Napoli","Napoli","Via Roma",15,80100,"3331234567","Maschio",LocalDate.of(1985,8,1),"RSSMRA85M01H501Z","mario.rossi@email.it","Italiana",new Camera(101,Stato.Occupata,2, 120.00,"Camera matrimoniale vista mare","")));
        clientes.add(new Cliente("Laura","Verdi","Roma","Roma","Via Milano",23,100,"3339876543","Femmina",LocalDate.of(1990,4,5),"VRDLRA90D45F839Y","laura.verdi@email.it","Italiana",new Camera(102,Stato.Libera,3,130.0,"Camera per due persone","")));
        Object s = "Italiana" ;
        ArrayList<Cliente> clientes1 = (ArrayList<Cliente>) clienteDAO.doRetriveByAttribute("Cittadinanza",s);
        assertEquals(clientes,clientes1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveByAttribute() quando va in eccezzione")
    public void doRetriveByAttribute() throws SQLException{
        Object s = "Australia";
        assertThrows(NoSuchElementException.class,()->clienteDAO.doRetriveByAttribute("Cittadinanza",s));
    }

}