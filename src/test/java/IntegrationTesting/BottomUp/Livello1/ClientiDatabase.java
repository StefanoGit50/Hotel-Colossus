package IntegrationTesting.BottomUp.Livello1;

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


    private FrontDeskStorage <Cliente>  fds;
    private CatalogoClienti catalogoClienti;
    Cliente cliente = new Cliente("nome","cognome","italiana","milano","milano","via milano",23,12453,"32345672","m", LocalDate.of(2001,12,13),"CSDFHWSDO2","luca@email","italiana",new Camera());

    @BeforeEach
    public void setUp() {
        fds = new ClienteDAO();
        catalogoClienti = new CatalogoClienti();
    }
    @AfterEach
    public void tearDown() {
        catalogoClienti.getListaClienti().clear();
    }


    @Test
    @DisplayName("Bottom up: simulazione della chiamata update tramite catalogo")
    @Tag("integration")
    public void updateCatalogoClienti() throws CloneNotSupportedException {
        catalogoClienti.aggiungiCliente(cliente);
        Cliente copia = cliente.clone();
        cliente.setCognome("Nicolussi");
        catalogoClienti.updateCliente(cliente);

        //verifica se i cambiamenti sono effettivi nel db
        try{
            assertNotEquals(copia,fds.doRetriveByKey(cliente.getCf()));
        }catch (Exception e){
             e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Bottom up: simulazione della chiamata save tramite catalogo")
    @Tag("integration")
    public void aggiungiCliente() {
        catalogoClienti.aggiungiCliente(cliente);
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
        CatalogoClienti cat = new CatalogoClienti();
        ArrayList<Cliente> prova = new ArrayList<>();

        try{
            prova= (ArrayList<Cliente>) fds.doRetriveAll("decrescente");
        }catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(prova,cat.getListaClienti());
    }

    @Test
    @DisplayName("Bottom up: simulazione della chiamata di eliminazione dal DB")
    @Tag("integration")
    public void eliminazioneCliente() {
        catalogoClienti.aggiungiCliente(cliente);
        ArrayList<Cliente> prova =catalogoClienti.getListaClienti();
        try{
            prova= (ArrayList<Cliente>) fds.doRetriveAll("decrescente");
        }catch (Exception e){
            e.printStackTrace();
        }
        catalogoClienti.removeCliente(cliente);
        assertNotEquals(prova,catalogoClienti.getListaClienti());
    }

}
