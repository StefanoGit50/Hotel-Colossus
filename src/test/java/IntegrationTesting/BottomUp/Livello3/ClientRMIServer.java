package IntegrationTesting.BottomUp.Livello3;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Client.GUI.components.BookingCreation;
import it.unisa.Common.*;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Storage.DAO.*;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ClientRMIServer {
    private static final Logger log = LogManager.getLogger(ClientRMIServer.class);
    private static FrontDesk frontDesk;
    private static FrontDeskClient frontDeskClient;
    private static FrontDeskStorage frontDeskStorage;
    private static Thread serverThread;
    private static final int PORT = 1099;
    private static final String SERVICE_NAME = "GestionePrenotazioni";

    private static List<Camera> cameras;
    private static List<Prenotazione> plist;
    private static List<Cliente> clientes;

    @BeforeAll
    public static void setUp() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
            log.info("--- 1. AVVIO SERVER IN BACKGROUND ---");

            //  Lanciamo il server su un thread separato
            serverThread = new Thread(() -> {
                try {
                    // chiamerà il tuo costruttore che contiene il .join()
                    new FrontDesk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverThread.setDaemon(true); //  Muore quando finisce il test
            serverThread.start();

            ClientRMIServer.log.info("--- 2. ATTENDO CHE IL SERVER SIA PRONTO ---");
            waitForServerToStart();

            // C. Ora che il server è attivo collego il client
            log.info("--- 3. AVVIO CLIENT ---");
            frontDeskClient = new FrontDeskClient();
            log.info("✓ Client connesso e pronto per i test.");
        }

        // Metodo che blocca il test finché il server non risponde
        private static void waitForServerToStart() {
            boolean isConnected = false;
            int tentativi = 0;
            int maxTentativi = 20; // Aspetta massimo 10 secondi (20 * 500ms)

            while (!isConnected && tentativi < maxTentativi) {
                try {
                    // Proviamo a vedere se "GestionePrenotazioni" esiste nel registro

                    String url = "rmi://localhost:" + PORT + "/GestionePrenotazioni";
                    Naming.lookup(url);

                    isConnected = true;
                    log.info("✓ Server rilevato online al tentativo " + (tentativi + 1));

                } catch (Exception e) {
                    // Il server non è ancora pronto
                    tentativi++;
                    System.out.println("... server non ancora pronto (tentativo " + tentativi + ")...");
                    try {
                        Thread.sleep(500); // Aspetta mezzo secondo
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (!isConnected) {
                throw new RuntimeException("TIMEOUT: Il Server RMI non si è avviato in tempo.");
            }
        }

@AfterEach
public void tearDown() {
    DBPopulator.cancel();
    DBPopulator.populator();
}

    @Test
    @DisplayName("Bottom Up TC24: LV3 get camera dal DB attraverso il server RMI ")
    @Tag("integration-LV3")
    @SuppressWarnings("unchecked")
    public void getCamereTest() {
        frontDeskStorage= new CameraDAO();
        List<Camera> cameraList = assertDoesNotThrow(()->frontDeskClient.getCamere());

        //verifica se le camere sono le stesse del DB

        cameras = (List<Camera>) assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));
        assertEquals(cameras,cameraList);
    }

    @Test
    @DisplayName("Bottom Up TC25: lV3 FrontDesk getPrenotazioni DB attraverso il server RMI")
    @Tag("integration-LV3")
    @SuppressWarnings("unchecked")
    public void getPrenotazioneTest() {
        frontDeskStorage= new PrenotazioneDAO();
        List<Prenotazione> plist1 = assertDoesNotThrow(()-> frontDeskClient.getPrenotazioni());

        plist = (List<Prenotazione>) assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));
        System.out.println(plist.size()+"  "+plist);
        System.out.println(plist.size()+"  "+plist);
        assertEquals(plist1,plist);
    }

    @Test
    @DisplayName("Bottom up TC25 : LV3 FrontDesk addPrenotazioni DB attraverso il server RMI")
    @Tag("integration-LV3")
    public void addPrenotazioneTest() {
        clientes =  assertDoesNotThrow(()->frontDeskClient.getListaClienti());
        List<Prenotazione> prenotaziones = assertDoesNotThrow(()-> frontDeskClient.getPrenotazioni());
        List<Camera> cameraList = assertDoesNotThrow(()->frontDeskClient.getCamere());

        List<Cliente> clist = new ArrayList<>();
        clist.add(clientes.get(0));
        clist.add(clientes.get(1));
        clist.add(clientes.get(2));
        clist.getFirst().setCamera(cameraList.get(0));
        clist.get(1).setCamera(cameraList.get(1));
        clist.get(2).setCamera(cameraList.get(2));

        List<Servizio> servizioList = assertDoesNotThrow(()-> frontDeskClient.getServizi());
        List<Servizio> serviziop= new ArrayList<>();
        serviziop.add(servizioList.get(0));
        serviziop.add(servizioList.get(0));
        serviziop.add(servizioList.get(1));

        Prenotazione p = new Prenotazione(LocalDate.now(),LocalDate.now(),LocalDate.now().plusDays(5),  null,new Trattamento("Mezza Pensione",87),
                87.0,"Patente",LocalDate.of(2020,04,02),LocalDate.of(2030,05,02),
                "Mario Biondi","",(ArrayList<Servizio>) serviziop, (ArrayList<Cliente>) clist,"SADAS34","","italiana");

        assertDoesNotThrow(()->frontDeskClient.addPrenotazione(p));
        assertNotEquals(prenotaziones,assertDoesNotThrow(()->frontDeskClient.getPrenotazioni()));

    }

    @Test
    @DisplayName("Bottom up TC26 : LV3 FrontDesk rimozione prenotazione attraverso la chiamata RMI")
    @Tag("integration-LV3")
    @SuppressWarnings("unchecked")
    public void removePrenotazioneTest() {
        frontDeskStorage= new PrenotazioneDAO();
        plist = (List<Prenotazione>) assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));
        assertDoesNotThrow(()->frontDeskClient.removePrenotazione(plist.getLast()));
        List<Prenotazione>plist2 = (List<Prenotazione>) assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));
        assertNotEquals(plist2,plist);
    }

    @Test
    @DisplayName("Bottom up TC27 : LV3 FrontDesk update prenotazione attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void updatePrenotazioneTest() {
        frontDeskStorage = new PrenotazioneDAO();
        plist = (List<Prenotazione>) assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));
        plist.getLast().setMetodoPagamento("carta dello zio peppe");
        assertDoesNotThrow(()->frontDeskClient.updatePrenotazione(plist.getLast()));
    }
    @Test
    @DisplayName("Bottom up TC28 : LV3 FrontDesk aggiunta cliente attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void addClienteTest(){
        frontDeskStorage= new ClienteDAO();
        Cliente c = new Cliente("luca","mannolo","caserta","caserta","via via",123,9832,"34564322","m",LocalDate.of(2001,3,30),"SDFGFDRTHJNBVFDF",
                "luca@misafnfai","italiana",new Camera());
        assertDoesNotThrow(()->frontDeskClient.addCliente(c));
    }
   @Test
    @DisplayName("Bottom up TC28 : LV3 FrontDesk rimozione cliente attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void removeClienteTest(){
        frontDeskStorage= new ClienteDAO();
        List<Cliente> clientes1 = assertDoesNotThrow(()-> frontDeskClient.getListaClienti());
        Cliente c= clientes1.getFirst();
        assertDoesNotThrow(()->frontDeskClient.removeCliente(c));
        assertEquals(clientes1,assertDoesNotThrow(()->frontDeskClient.getListaClienti()));
    }

    @Test
    @DisplayName("Bottom up TC29: LV3 FrontDesk update cliente attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void updateClienteTest(){
        frontDeskStorage= new ClienteDAO();
        clientes= assertDoesNotThrow(()->frontDeskClient.getListaClienti());
        Cliente c = clientes.getFirst();
        c.setCAP(11111);
        assertDoesNotThrow(()->frontDeskClient.updateCliente(c));
        assertNotEquals(clientes,assertDoesNotThrow(()->frontDeskClient.getListaClienti()));
    }

    @Test
    @DisplayName("Bottom up TC30: LV3 FrontDesk banCliente attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void banClienteTest(){
        frontDeskStorage= new ClienteDAO();
        clientes= assertDoesNotThrow(()->frontDeskClient.getListaClienti());
        Cliente c = clientes.getFirst();
        c.setBlacklisted(true);
        assertDoesNotThrow(()->frontDeskClient.banCliente(c));
    }

    @Test
    @DisplayName("Bottom up TC31: LV3 FrontDesk unBanCliente attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void unBanClienteTest(){
        frontDeskStorage= new ClienteDAO();
        clientes= assertDoesNotThrow(()->frontDeskClient.getListaClienti());
        Cliente c = clientes.get(1);
        assertDoesNotThrow(()->frontDeskClient.unBanCliente(c));
    }

    @Test
    @DisplayName("Bottom up TC32: LV3 Frontdesk getServizi attraverso la chiamata RMI")
    @SuppressWarnings("unchecked")
    public void getServiziTest(){
        frontDeskStorage= new ServizioDAO();
        List<Servizio> servizios =assertDoesNotThrow(()->frontDeskClient.getServizi());
        assertFalse(servizios.isEmpty());
        assertTrue(servizios.size()>3);
        assertEquals(servizios,assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente")));
    }

    @Test
    @DisplayName("Bottom up TC33 : LV3 Frontdesk getTrattamento attraverso la chiamta RMI")
    public void getTrattamentoTest(){
        frontDeskStorage= new TrattamentoDAO();
        List<Trattamento> trattamenti =assertDoesNotThrow(()->frontDeskClient.getTrattamenti());
        assertFalse(trattamenti.isEmpty());
        assertTrue(trattamenti.size()>3);
        assertEquals(trattamenti,assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente")));
    }

}
