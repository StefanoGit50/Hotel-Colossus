package blackbox.CambiaStatoCamera;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

//TODO: DA COMPLETARE --> METODO PER INIZIALIZZARE LA LISTA DELLE CAMERE DAL DATABASE

@DisplayName("TESTING: cambia stato camere")
@Tag("cambiaStatoCamera")
public class TestCambiaStatoCamera {

    private static GovernanteInterface governante;

    @BeforeAll
    public static void istantiateGovernante() throws RemoteException, NotBoundException, MalformedURLException {
        governante = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
    }

    @Test
    @DisplayName("TC1: [success] Out Of Order -> In pulizia")
    public void testCase1() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.InPulizia);
        governante.aggiornaStatoCamera(camera);
        Assertions.assertEquals(Stato.InPulizia, camera.getStatoCamera());
    }

    @Test
    @DisplayName("TC2: [success] Out Of Order -> In servizio")
    public void testCase2() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.InServizio);
        governante.aggiornaStatoCamera(camera);
        Assertions.assertEquals(Stato.InPulizia, camera.getStatoCamera());
    }

    @Test
    @DisplayName("TC3: [success] In pulizia -> Out of order")
    public void testCase3() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.OutOfOrder);
        governante.aggiornaStatoCamera(camera);
        Assertions.assertEquals(Stato.InPulizia, camera.getStatoCamera());
    }

    @Test
    @DisplayName("TC4: [success] In pulizia -> In servizio")
    public void testCase4() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.InServizio);
        governante.aggiornaStatoCamera(camera);
    }

    @Test
    @DisplayName("TC5: [success] In servizio -> Out of Order")
    public void testCase5() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.OutOfOrder);
        governante.aggiornaStatoCamera(camera);
    }

    @Test
    @DisplayName("TC6: [success] In servizio -> In pulizia")
    public void testCase6() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.InPulizia);
        governante.aggiornaStatoCamera(camera);
    }

    /* ***************************** CASI DI ERRORE **************************** */

    @Test
    @DisplayName("TF7: [error] Database offline - Lo stato non deve cambiare")
    public void testCaseTF7() throws RemoteException {
        Camera camera = governante.getListCamere().getFirst();
        camera.setStatoCamera(Stato.OutOfOrder);
        governante.aggiornaStatoCamera(camera);
    }
}
