package WhiteBox.UnitTest;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.CameraDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CameraDaoTesting{

    private CameraDAO cameraDAO;
    private ResultSet resultSet;
    private Camera camera;

    @BeforeEach
    public void setUp() throws SQLException {
        // Inizializza DAO e oggetto Camera
        cameraDAO = new CameraDAO();
        camera = new Camera(104, Stato.Libera, 2, 150.0, "Camera prova");
    }



    @Test
    @Tag("True")
    @DisplayName("doSaveAll() quando è tutto vero")
    public void doSaveAllTrue(){
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        cameras.add(new Camera(105,Stato.Libera,3,155.0,"Sp"));
    }
    @Test
    @Tags({@Tag("Error"),@Tag("Exception")})
    @DisplayName("doSave() quando è tutto falso")
   public void doSaveAllFalse() throws SQLException {
        ArrayList<Camera> cameras = new ArrayList<>();
       assertThrows(NullPointerException.class,()->cameraDAO.doSaveAll(cameras));
   }
   @Test
   @Tag("True")
   @DisplayName("doRetriveAll() quando è tutto vero")

   public void doRetriveAllTrue() throws SQLException {
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(new Camera(101,Stato.Libera,2,120,"Camera doppia con vista mare"));
        cameras.add(new Camera(102,Stato.Libera,4,180,"Camera familiare con balcone"));
        cameras.add(new Camera(103,Stato.Libera,2,150,"Camera prova"));
        cameras.add(new Camera(104,Stato.Libera,2,150,"Camera prova"));

        ArrayList<Camera> cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
        assertEquals(cameras,cameras1);
    }

    @Test
    @Tags({@Tag("Exception"),@Tag("Error")})
    @DisplayName("doRetriveAll() quando order è null")
    public void doRetriveAllOrderNull() throws SQLException{
        assertThrows(RuntimeException.class,()->cameraDAO.doRetriveAll(null));
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAll() quando è tutto false")
    public void doRetriveAllFalseTranneOrder() throws SQLException {
        ArrayList<Camera> cameras;

        cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("crescente");
        assertEquals(new ArrayList<>(),cameras);
    }

    @Test
    @Tag("False")
    @DisplayName("doUpdate() quando è False che camere è uguale a null")
    public void doUpdateCameraUgualeANull(){
        assertThrows(NoSuchElementException.class,()->cameraDAO.doUpdate(null));
    }

    @Test
    @Tag("True")
    @DisplayName("doUpdate() quando è tutto vero")
    public void doUpdateAllTrue() throws SQLException{
        camera.setNumeroCamera(101);
        assertDoesNotThrow(()->cameraDAO.doUpdate(camera));
    }

    @Test
    @Tags({@Tag("Error"),@Tag("Exception")})
    @DisplayName("doRetriveAttribute() quando manda l'eccezione")
    public void doRetriveAttribute(){
        assertThrows(RuntimeException.class,()->cameraDAO.doRetriveByAttribute(null,null));
    }

    @Test
    @Tag("True")
    @DisplayName("doRetriveAttribute() quando va tutto bene")
    public void doRetriveAttributeAllTrue() throws SQLException {
        ArrayList<Camera>cameras = new ArrayList<>();
        cameras.add(new Camera(101 , Stato.Libera ,2 , 150 , "Camera prova"));
        cameras.add(new Camera(102,Stato.Libera,4,180,"Camera familiare con balcone"));
        Object og = Stato.Libera.name();
        ArrayList<Camera> cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveByAttribute("Stato",og);

        assertEquals(cameras,cameras1);
    }

    @Test
    @Tag("False")
    @DisplayName("doRetriveAttribute() quando va male")
    public void doRetriveAttributeException() throws SQLException {
        Object o = Stato.InPulizia.name();
        assertThrows(NoSuchElementException.class,()->cameraDAO.doRetriveByAttribute("Stato",o));
    }
}
