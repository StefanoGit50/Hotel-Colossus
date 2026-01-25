package blackbox.CambiaStatoCamera;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

//TODO: DA COMPLETARE --> METODO PER INIZIALIZZARE LA LISTA DELLE CAMERE DAL DATABASE

@DisplayName("TESTING: cambia stato camere")
@Tag("cambiaStatoCamera")
public class TestCambiaStatoCamera {

    private static GovernanteInterface governante;

    /**
     * Restituisce il primo oggetto camera il cui stato è uguale al
     * parametro esplicito passato
     * @param stato uno stato
     * @return la prima camera con lo stato cercato, {@code null} altrimenti
     */
    public Camera getCameraByStato(Stato stato) throws RemoteException{
        List<Camera> camere = governante.getListCamere();
        for (Camera c : camere){
            if (c.getStatoCamera().equals(stato)){
                return c;
            }
        }
        return null;
    }

    /**
     * Restituisce l'oggetto camera il cui ID è uguale al
     * parametro esplicito.
     * @param idCamera chiave primaria.
     * @return la prima camera con l'id cercato, {@code null} altrimenti
     */
    public Camera getCameraById(int idCamera) throws RemoteException{
        List<Camera> camere = governante.getListCamere();
        for (Camera c : camere){
            if (c.getNumeroCamera() == idCamera){
                return c;
            }
        }
        return null;
    }

    @BeforeAll
    public static void istantiateGovernante() throws RemoteException, NotBoundException, MalformedURLException {
        governante = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
    }

    @Nested
    @DisplayName("TESTING: CambiaStatoCamere - PASSes")
    @Tag("pass")
    class TestPassCambiaStatoCamera {

        @Test
        @DisplayName("TC1: [success] Out Of Order -> In pulizia")
        public void testCase1() throws RemoteException {
            Camera camera = getCameraByStato(Stato.OutOfOrder), prova = null;
            Assertions.assertNotNull(camera);

            camera.setStatoCamera(Stato.InPulizia);
            governante.aggiornaStatoCamera(camera);
            prova = getCameraById(camera.getNumeroCamera());
            Assertions.assertNotNull(prova);

            Assertions.assertEquals(Stato.InPulizia, prova.getStatoCamera());
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
    }

    /* ***************************** CASI DI ERRORE **************************** */

    @Nested
    @DisplayName("TESTING: CambiaStatoCamera - FAILs")
    @Tag("fail")
    class TestFailCambioStatoCamera {

        @Test
        @DisplayName("TF7: [error] Database offline - Lo stato non deve cambiare")
        public void testCaseTF7() throws RemoteException {
            Camera camera = governante.getListCamere().getFirst();
            camera.setStatoCamera(Stato.OutOfOrder);
            governante.aggiornaStatoCamera(camera);
        }
    }
}
