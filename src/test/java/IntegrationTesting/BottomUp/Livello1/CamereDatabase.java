package IntegrationTesting.BottomUp.Livello1;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Common.Camera;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CamereDatabase {
    private static CatalogoCamere catalogoCamere;
    private static FrontDeskStorage<Camera> fds;
    private static Camera camera;


    @BeforeAll
    public static void setUp() throws RemoteException, SQLException {
         camera = new Camera();
         catalogoCamere = new CatalogoCamere();
         fds = new CameraDAO();
    }
    @AfterEach
    public void tearDown()   {
        CatalogoCamere.aggiornalista();
        camera = new Camera();
    }

    @Test
    @DisplayName("Bottom up TC1: CameraDAO e aggiornamento stato camera da catalogo")
    @Tag("integration")
    public void aggiornaStatoCameraDB() throws RemoteException {
        DBPopulator.cancel();
        DBPopulator.populator();
        camera = CatalogoCamere.getCamera(101);
        Camera camera1;
        try {
           camera1 = fds.doRetriveByKey(camera.getNumeroCamera());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        camera.setStatoCamera(Stato.Occupata);
        assertTrue(catalogoCamere.aggiornaStatoCamera(camera));

        //controllo se nel DB è cambiata la camera
        try {
           camera = fds.doRetriveByKey(camera.getNumeroCamera());
        }catch(SQLException e){
                e.printStackTrace();
        }
        assertNotEquals(camera1.getStatoCamera(),camera.getStatoCamera());
        assertEquals(camera.getNumeroCamera(), catalogoCamere.getLastModified().getNumeroCamera());

    }

    @Test
    @DisplayName("Bottom up TC2: CameraDao aggiuntaCamera da catalogo")
    @Tag("integration")
    public void aggiuntaCameraDB() {
        ArrayList<Camera> camere = new ArrayList<>();
        camere.add(new Camera(114,Stato.Libera,3,89,"","Posillipo"));
        camere.add(new Camera(104,Stato.Libera,2,114,"","Amalfi"));
        CatalogoCamere.addCamere(camere); // fa la chiamata al db

        Camera c1=null;
        Camera c2=null;
            // controllo se le camere sono state aggiunte al DB
        try{
           c1=fds.doRetriveByKey(camere.getFirst().getNumeroCamera());
           c2=fds.doRetriveByKey(camere.getLast().getNumeroCamera());
        }catch (SQLException s){
            s.printStackTrace();
        }
        assertDoesNotThrow(()-> fds.doRetriveByKey(camere.getFirst().getNumeroCamera()));
        assertDoesNotThrow(()-> fds.doRetriveByKey(camere.getLast().getNumeroCamera()));
        assertNotNull(c1);
        assertNotNull(c2);
    }

    @Test
    @DisplayName("Bottom up TC3: ottenere le camere dal DB")
    @Tag("integration")
    public void getCamereFromDb(){
        assertTrue(CatalogoCamere.aggiornalista());
        List<Camera>cameraList=CatalogoCamere.getListaCamere();
        List<Camera> cameraList2 = List.of();
        try{
            cameraList2= (List<Camera>) fds.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(cameraList2,cameraList);
    }
}
