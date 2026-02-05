package IntegrationTesting.BottomUp.Livello1;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClientiDatabase {


    private FrontDeskStorage <Cliente> fds;
    Cliente cliente = new Cliente("nome","cognome","milano","milano","via milano",23,12453,"32345672","m", LocalDate.of(2001,12,13),"CSDFHWSDO209u8y6","luca@email","italiana",new Camera());

    @BeforeEach
    public void setUp() {
        fds = new ClienteDAO();
        CatalogoClienti.aggiornalista();
    }
    @AfterEach
    public void tearDown() {
        DBPopulator.cancel();
        DBPopulator.populator();
        CatalogoClienti.aggiornalista();
    }


    @Test
    @DisplayName("Bottom up: simulazione della chiamata update tramite catalogo")
    @Tag("integration")
    public void updateCatalogoClienti() throws CloneNotSupportedException {
        CatalogoClienti.aggiungiCliente(cliente);
        Cliente copia = cliente.clone();
        cliente.setCognome("Nicolussi");
        CatalogoClienti.updateCliente(cliente); // fa la chiamata al db

        //verifica se i cambiamenti sono effettivi nel db
        try{
            System.out.println(cliente);
            System.out.println(copia);
            assertNotEquals(copia,fds.doRetriveByKey(cliente.getCf()));
        }catch (Exception e){
             e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Bottom up: simulazione della chiamata save tramite catalogo")
    @Tag("integration")
    public void aggiungiCliente() {
        CatalogoClienti.aggiungiCliente(cliente);
        Cliente c2=null;
        try{
            c2=fds.doRetriveByKey(cliente.getCf());
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(c2,cliente);
    }

    @Test
    @DisplayName("Bottom up: simulazione della chiamata di retrieve dal DB")
    @Tag("integration")
    public void retrievelistaCliente() {
        ArrayList<Cliente> testarray = new ArrayList<>();

        try{
           testarray= (ArrayList<Cliente>) fds.doRetriveAll("decrescente");
        }catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(testarray,CatalogoClienti.getListaClienti());
    }

    @Test
    @DisplayName("Bottom up: simulazione della chiamata di eliminazione dal DB")
    @Tag("integration")
    public void eliminazioneCliente() {
        CatalogoClienti.aggiungiCliente(cliente);
        ArrayList<Cliente> prova=null;
        try{
            prova= (ArrayList<Cliente>) fds.doRetriveAll("decrescente");
        }catch (Exception e){
            e.printStackTrace();
        }
        CatalogoClienti.removeCliente(cliente);
        assertNotEquals(prova,CatalogoClienti.getListaClienti());
    }

}
