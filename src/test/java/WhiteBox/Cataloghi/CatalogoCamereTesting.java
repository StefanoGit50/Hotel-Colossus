package WhiteBox.Cataloghi;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogoCamereTesting{

    @Mock
    private FrontDeskStorage<Camera> cameraDAO;
    private Camera camera;

    @InjectMocks
    private CatalogoCamere catalogoCamere;

    @BeforeEach
    public void setBefore(){
        camera = new Camera(110, Stato.Libera,2,100.0,"");
    }

    @Test
    @Tag("True")
    @DisplayName("addCamera() quando va tutto a buon fine")
    public void addCameraAllTrue(){
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        cameras.add(camera);

        assertDoesNotThrow(()->catalogoCamere.addCamere(cameras));
    }

    @Test
    @Tag("False")
    @DisplayName("addCamera() quando la condizione del for è falsa")
    public void addCameraForIsFalse(){
        assertDoesNotThrow(()->catalogoCamere.addCamere(new ArrayList<>()));
    }
    @Test
    @Tag("True")
    @DisplayName("getCamera() quando va tutto a buon fine")
    public void getCameraAllTrue() throws CloneNotSupportedException {
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        cameras.add(new Camera(111,Stato.Libera,2,110.0,""));
        cameras.add(new Camera(112,Stato.Libera,3,112.0,""));
        catalogoCamere.addCamere(cameras);
        assertNotNull(catalogoCamere.getCamera(110));
    }

    @Test
    @Tag("False")
    @DisplayName("getCamera() quando la lista è vuota")
    public void getCameraQuandoIlCatalogoCameraèvuoto() throws CloneNotSupportedException {
        assertNull(catalogoCamere.getCamera(110));
    }

    @Test
    @Tag("False")
    @DisplayName("getCamera() quando non trova la camera")
    public void getCameraQuandoNonTrova() throws CloneNotSupportedException {
        ArrayList<Camera> cameras = new ArrayList<>();
        cameras.add(camera);
        cameras.add(new Camera(111,Stato.Libera,2,110.0,""));
        cameras.add(new Camera(112,Stato.Libera,3,112.0,""));
        assertNull(catalogoCamere.getCamera(113));
    }

    @Test
    @Tag("True")
    @DisplayName("aggiornaStatoCamera(Camera c) quando è tutto vero")
    public void AggiornaStatoCameraAllTrue() throws SQLException, RemoteException {
       // catalogoCamere = new CatalogoCamere(cameraDAO);
        ArrayList<Camera> cameras = new ArrayList<>();
        Camera camera1 = new Camera(111, Stato.Libera,2,100.0,"");
        doNothing().when(cameraDAO).doUpdate(camera1);
        cameras.add(camera);
        cameras.add(new Camera(111,Stato.Occupata,2,110.0,""));
        cameras.add(new Camera(112,Stato.Libera,3,112.0,""));

        catalogoCamere.addCamere(cameras);
        assertTrue(catalogoCamere.aggiornaStatoCamera(camera1));
    }

    @Test
    @Tag("False")
    @DisplayName("aggiornaStatoCamera() quando tutto Falso")
    public void AggiornaStatoCameraAllFalse() throws RemoteException {
        //catalogoCamere = new CatalogoCamere(cameraDAO);
        Camera camera1 = new Camera(111, Stato.Libera,2,100.0,"");
        assertFalse(catalogoCamere.aggiornaStatoCamera(camera1));
    }


}
