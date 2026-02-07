package WhiteBox.UnitTest;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CameraDaoTest {

    private CameraDAO cameraDAO;
    private ResultSet resultSet;
    private Camera camera;

    @BeforeAll
    public static void setBeforeAll(){
        DBPopulator.cancel();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Inizializza DAO e oggetto Camera
        DBPopulator.populator();
        cameraDAO = new CameraDAO();
        camera = new Camera(104, Stato.Libera, 2, 150.0,"Camera prova","ho sole mio");
    }

    @AfterEach
    public void after(){
        DBPopulator.cancel();
    }

    @Test
    @Tag("True")
    @DisplayName("TC1: doDelete() quando va tutto")
    public void doDelete(){
       assertDoesNotThrow(()->cameraDAO.doDelete(camera));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC2: doDelete() quando non va a buon fine")
    public void doDeleteException(){
        assertThrows(NoSuchElementException.class,()->cameraDAO.doDelete(null));
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC3: doRetriveByKey() quando lancia l'eccezione")
    public void doRetriveByKeyException(){
        assertThrows(SQLException.class,()->cameraDAO.doRetriveByKey(new Cliente()));
    }

    @Test
    @Tag("True")
    @DisplayName("TC4: doRetriveByKey() quando va tutto a buon fine")
    public void doRetriveByKeyAllTrue() throws SQLException{
      Camera camera2 = new Camera(101,Stato.Libera,2,80.0,"Vista interna","Camera Standard");
      Camera camera1 = assertDoesNotThrow(()->cameraDAO.doRetriveByKey(101));
      assertEquals(camera2,camera1);
    }

    @Test
    @Tag("False")
    @DisplayName("TC5: doRetriveByKey() quando il resultSet non va a buon fine")
    public void doRetriveByKeyResulSetFalse(){
        Camera camera =  new Camera();
        Camera camera1 = assertDoesNotThrow(()->cameraDAO.doRetriveByKey(104));
        assertEquals(camera,camera1);
    }

    @Test
    @Tag("True")
    @DisplayName("TC6: doSaveAll() quando è tutto vero")
    public void doSaveAllTrue(){
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        cameras.add(new Camera(105,Stato.Libera,3,155.0,"GiorgioVanni è celiaco","Camera con balcone"));
        cameras.add(new Camera(106,Stato.Libera,4,160.0,"Annapia ha le lentigini","Camera con 2 letti"));
        assertDoesNotThrow(()->cameraDAO.doSaveAll(cameras));
    }

    @Test
    @Tags({@Tag("Error"),@Tag("Exception")})
    @DisplayName("TC7: doSave() quando è tutto falso")
   public void doSaveAllFalse() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Camera> cameras = new ArrayList<>();
       assertThrows(NullPointerException.class,()->cameraDAO.doSaveAll(cameras));
   }

   @Test
   @Tag("True")
   @DisplayName("TC8: doRetriveAll() quando è tutto vero")
   public void doRetriveAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(new Camera(101,Stato.Libera,2,80,"Vista interna","Camera Standard"));
        cameras.add(new Camera(102,Stato.Occupata,2,80,"Vista strada","Camera Standard"));
        cameras.add(new Camera(201,Stato.Prenotata,3,180,"Vista mare laterale","Junior Suite"));
        cameras.add(new Camera(202,Stato.Occupata,2,350,"Jacuzzi privata","Suite Presidenziale"));
        cameras.add(new Camera(301,Stato.InPulizia,1,60,"Piccola ma accogliente","Camera Singola"));
        ArrayList<Camera> cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
        assertEquals(cameras1,cameras);
    }




    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("TC9: doRetriveAll() quando order è null")
    public void doRetriveAllOrderNull() throws SQLException{
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(RuntimeException.class,()->cameraDAO.doRetriveAll(null));
    }

    @Test
    @Tag("False")
    @DisplayName("TC10: doRetriveAll() quando è tutto false")
    public void doRetriveAllFalseTranneOrder() throws SQLException {
        DBPopulator.cancel();
        ArrayList<Camera> cameras;

        cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("crescente");
        assertEquals(new ArrayList<>(),cameras);
    }

    @Test
    @Tag("False")
    @DisplayName("TC11: doUpdate() quando è False che camere è uguale a null")
    public void doUpdateCameraUgualeANull(){
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(NoSuchElementException.class,()->cameraDAO.doUpdate(null));
    }

    @Test
    @Tag("True")
    @DisplayName("TC12: doUpdate() quando è tutto vero")
    public void doUpdateAllTrue() throws SQLException{
        DBPopulator.cancel();
        DBPopulator.populator();
        camera.setNumeroCamera(101);
        assertDoesNotThrow(()->cameraDAO.doUpdate(camera));
    }

    @Test
    @Tags({@Tag("Error"),@Tag("Exception")})
    @DisplayName("TC13: doRetriveAttribute() quando manda l'eccezione")
    public void doRetriveAttribute(){
        DBPopulator.cancel();
        DBPopulator.populator();
        assertThrows(RuntimeException.class,()->cameraDAO.doRetriveByAttribute(null,null));
    }

    @Test
    @Tag("True")
    @DisplayName("TC14: doRetriveAttribute() quando va tutto bene")
    public void doRetriveAttributeAllTrue() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        ArrayList<Camera>cameras = new ArrayList<>();
         cameras.add(new Camera(102,Stato.Occupata,2,80,"Vista strada","Camera Standard"));
         cameras.add(new Camera(202,Stato.Occupata,2,350,"Jacuzzi privata","Suite Presidenziale"));

        Object og = Stato.Occupata.name();
        ArrayList<Camera> cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveByAttribute("Stato",og);

        assertEquals(cameras,cameras1);
    }

    @Test
    @Tag("False")
    @DisplayName("TC15: doRetriveAttribute() quando va male")
    public void doRetriveAttributeException() throws SQLException {
        DBPopulator.cancel();
        DBPopulator.populator();
        Object o = Stato.OutOfOrder.name();
        assertThrows(NoSuchElementException.class,()->cameraDAO.doRetriveByAttribute("Stato",o));
    }
}
