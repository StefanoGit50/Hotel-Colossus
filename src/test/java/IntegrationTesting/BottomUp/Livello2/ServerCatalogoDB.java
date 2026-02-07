package IntegrationTesting.BottomUp.Livello2;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Common.*;
import it.unisa.Server.command.Invoker;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerCatalogoDB {

    private Invoker invoker;
    private static CatalogoPrenotazioni catPrenotazioni;
    private static CatalogoClienti catClienti;
    private static CatalogoCamere catCamere;
    private static FrontDesk frontDesk;
    private static ArrayList<Camera> arraycamera = new ArrayList<>();
    private static Prenotazione prenotazione;
    private static ArrayList<Servizio>  servizio= new ArrayList<>();
    private static ArrayList<Cliente>cliente = new ArrayList<>();
    private static FrontDeskStorage frontDeskStorage;


    @BeforeAll
    static void setAllup() throws RemoteException, SQLException {


        //inizializzazione variabili di default
        //si suppone che il DB contenga queste variabili per il corretto funzionamento del test

        frontDeskStorage = new CameraDAO();
        arraycamera.add(CatalogoCamere.getListaCamere().getFirst());
        arraycamera.add(CatalogoCamere.getListaCamere().getLast());
        arraycamera.add(CatalogoCamere.getListaCamere().get(2));

        Invoker invoker = new Invoker();
        CatalogoCamere catCamere = new CatalogoCamere();
        frontDesk = new FrontDesk(invoker,catCamere);


        ServizioDAO servizioDAO = new ServizioDAO();
        ArrayList<Servizio>  servizioDB;

        servizioDB= (ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome","Spa e Benessere");
        servizio.add(servizioDB.getFirst());
        servizio.getFirst().setPrezzo(30);
        servizioDB=(ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome", "Parcheggio");
        servizio.add(servizioDB.getFirst());
        servizio.get(1).setPrezzo(20);

        cliente = new ArrayList<>(CatalogoClienti.getListaClienti().subList(0,3));
        cliente.getFirst().setCamera(arraycamera.getFirst());
        cliente.get(1).setCamera(arraycamera.get(1));
        cliente.get(2).setCamera(arraycamera.get(2));
        System.out.println(cliente);


        prenotazione =new Prenotazione(
                LocalDate.now(),                        // dataCreazionePrenotazione (Oggi)
                LocalDate.of(2026, 8, 10),              // dataInizio
                LocalDate.of(2026, 8, 20),              // dataFine
                null,
                new Trattamento("Pensione Completa",45),           // trattamento (Assumendo sia un Enum)
                45.0,
                "Carta d'Identità",                     // tipoDocumento
                LocalDate.of(2020, 5, 20),              // dataRilascio (documento)
                LocalDate.of(2030, 5, 19),              // dataScadenza (documento)
                cliente.getFirst().getNome()+" "+cliente.getFirst().getCognome(),                          // intestatario
                "Richiesta culla in camera",            // noteAggiuntive
                servizio,                          // listaServizi
                 cliente,                                 // listaClienti
                "CA123YZ",                           // numeroDocumento
                "",
                "Italiano"
        );
    }
    @BeforeEach
    void setUp() throws RemoteException {

    }

    @AfterEach
    void tearDown(){
        DBPopulator.cancel();
        DBPopulator.populator();
        CatalogoPrenotazioni.aggiornalista();
        CatalogoClienti.aggiornalista();
        CatalogoCamere.aggiornalista();
    }

    @Test
    @DisplayName("Bottom up 3.1 TC13: LV2 aggiornamento dello stato camera dalla classe server con pattern Observer")
    @Tag("integration-LV2")
    public void AggiornaStatoCamereCatalogoDB(){
         frontDeskStorage = new CameraDAO();
        System.out.println("Array camere: "+arraycamera);
        arraycamera.getFirst().setStatoCamera(Stato.Prenotata);
        assertDoesNotThrow(()->frontDesk.aggiornaStatoCamera(arraycamera.getFirst()));

        Camera c = null;
        //controllo nel DB se è stata cambiata lo stato della camera
        c= (Camera) assertDoesNotThrow(()->frontDeskStorage.doRetriveByKey(arraycamera.getFirst().getNumeroCamera()));

        assertEquals(arraycamera.getFirst(),c);
    }

    @Test
    @DisplayName("Bottom up 3.2 TC14: LV2 Test se le camere nel front desk sono le stesse nel DB ")
    @Tag("integration-LV2")
    public void getCamereTest(){
        frontDeskStorage = new CameraDAO();
       List<Camera> c=frontDesk.getCamere();
       Collection<?> listDB= null;

        //controllo nel DB se le liste sono le stesse
        listDB = assertDoesNotThrow(() -> frontDeskStorage.doRetriveAll("decrescente"));

        assertEquals(listDB, c);
    }

    @Test
    @DisplayName("Bottom up 3.3 TC15: LV2 Test se le prenotazioni nel front desk sono le stesse nel DB")
    @Tag("integration-LV2")
    public void getPrenotazioniTest() throws RemoteException {
        frontDeskStorage = new PrenotazioneDAO();

        List<Prenotazione> p = frontDesk.getPrenotazioni();
        Collection<?> listDB= null;

        // controllo nel DB se le liste sono le stesse
        listDB=  assertDoesNotThrow(()-> frontDeskStorage.doRetriveAll("decrescente"));

        assertEquals(listDB, p);
    }

    @Test
    @DisplayName("Bottom up 3.4 TC16: LV2 Test controllo dell'interazione sull aggiunta fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void addPrenotazioneTest() throws RemoteException{
        frontDeskStorage = new PrenotazioneDAO();

        ArrayList<Camera> arrcam= new ArrayList<>();
        arrcam.add(CatalogoCamere.getCamera(102));
        ArrayList<Cliente> clienti= new ArrayList<>();
        Cliente c =CatalogoClienti.getListaClienti().getFirst();
        c.setCamera(arrcam.getFirst());
        clienti.add(c);
        ArrayList<Servizio> servizio= new ArrayList<>();
        ServizioDAO servizioDAO = new ServizioDAO();
        ArrayList<Servizio> servizios=((ArrayList<Servizio>) assertDoesNotThrow(()->servizioDAO.doRetriveByAttribute("Nome","Spa e Benessere")));
        servizio.add(servizios.getFirst());

        Prenotazione p = new Prenotazione(        // IDPrenotazione
                LocalDate.now(),                        // dataCreazionePrenotazione
                LocalDate.now(),                        // dataInizio
                LocalDate.now().plusDays(5),             // dataFine
                null,
                new Trattamento("MEZZA PENSIONE", 60),
                60.0,
                "Passaporto",                           // tipoDocumento
                LocalDate.of(2012, 03, 11),             // dataRilascio
                LocalDate.of(2044, 12, 11),             // dataScadenza
                clienti.getFirst().getNome()+" "+ clienti.getFirst().getCognome(),                         // intestatario
                "",                                     // noteAggiuntive
                servizio,                               // listaServizi (Assicurati che sia un ArrayList!)
                clienti,                                // listaClienti
                "3453M",                             // numeroDocumento
                "",
                "italiana"
        );

        frontDesk.addPrenotazione(p);
        Prenotazione p1= null;
        // controllo nel DB se la prenotazione è presente
        try{
            p1= (Prenotazione) frontDeskStorage.doRetriveByKey(p.getIDPrenotazione());
        }catch (SQLException e){
            e.printStackTrace();
         }
        assertEquals(p, p1);
    }

    @Test
    @DisplayName("Bottom up 3.5 TC17 : LV2 Test controllo dell'interazione sulla rimozione fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void removePrenotazioneTest() throws RemoteException {
        frontDesk.addPrenotazione(prenotazione);
        List<Prenotazione> p = frontDesk.getPrenotazioni();
        frontDesk.removePrenotazione(prenotazione);

        Collection<?> listDB= null;
        // controllo nel DB se le liste sono le stesse
        try{
            listDB= frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertNotEquals(listDB, p);
    }

    @Test
    @DisplayName("Bottom up 3.6 TC18: LV2 Test controllo dell interazione sull update fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void updatePrenotazioneTest() throws  CloneNotSupportedException {
        frontDeskStorage = new PrenotazioneDAO();
        Prenotazione p3 = CatalogoPrenotazioni.getPrenotazione(1);
        Prenotazione p2 = CatalogoPrenotazioni.getPrenotazione(1);
        assertNotNull(p2);
        assertNotNull(p3);
        p2.setCheckIn(false);
        assertDoesNotThrow(()->frontDesk.updatePrenotazione(p2));

        prenotazione= (Prenotazione) assertDoesNotThrow(()->frontDeskStorage.doRetriveByKey(p3.getIDPrenotazione()));

        assertNotEquals(prenotazione, p3);
        assertNotEquals(CatalogoPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()), p3); //verifico se il catalogo ha preso la modifica
    }

    @Test
    @DisplayName("Bottom up 3.7 TC19: LV2 Test controllo dell interazione sull'aggiunta del cliente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void addClienteTest() throws RemoteException {
        frontDeskStorage = new ClienteDAO();
        Cliente c = new Cliente("Roberto","Rossi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana",new Camera());
        frontDesk.addCliente(c);

        Collection <?> clienti;

        assertDoesNotThrow(()->frontDeskStorage.doRetriveByKey(c.getCf()));
        clienti= assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));


        assertTrue(CatalogoClienti.getListaClienti().contains(c));
        assertTrue(CatalogoClienti.getListaClienti().containsAll(clienti));  // controlla se la lista passata è contenuta ed è uguale come elementi a quella expected

    }

    @Test
    @DisplayName("Bottom up 3.8 TC20: LV2 Test controllo dell interazione sulla rimozione del cliente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void removeClienteTest() throws RemoteException {
        frontDeskStorage = new ClienteDAO();
        assertFalse(CatalogoClienti.getListaClienti().isEmpty());
        ArrayList<Cliente> catalogoCopia = CatalogoClienti.getListaClienti();

        Cliente c = CatalogoClienti.getListaClienti().get(0);
        frontDesk.removeCliente(CatalogoClienti.getListaClienti().getFirst());

        Collection<?> listDB;

        assertThrows(Exception.class,()->frontDeskStorage.doRetriveByKey(c.getCf()));
        listDB= assertDoesNotThrow(()->frontDeskStorage.doRetriveAll("decrescente"));

        assertNotNull(listDB);
        assertNotEquals(catalogoCopia, listDB);
        assertEquals(CatalogoClienti.getListaClienti(),listDB);
    }

    @Test
    @DisplayName("Bottom up 3.9 TC21: LV2 Test controllo dell interazione sull update fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void updateClienteTest() throws RemoteException {
        Cliente c= cliente.getFirst();
        cliente.getFirst().setVia("via giovanni muratore");
        System.out.println("XDFDFS"+c);
        frontDesk.updateCliente(cliente.getFirst());

        Cliente c2= null;

        c2= (Cliente) assertDoesNotThrow(()->frontDeskStorage.doRetriveByKey(cliente.getFirst()));

        assertNotEquals(c,c2);
    }

    @Test
    @DisplayName("Bottom up 4.0 TC22: LV2 Test controllo dell interazione sul Ban dell utente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void BanClienteTest() throws RemoteException, CloneNotSupportedException {
        Cliente c= cliente.getFirst().clone();
        cliente.getFirst().setBlacklisted(false);

        frontDesk.banCliente(cliente.getFirst());
        assertNotEquals(cliente.getFirst(),c);
    }

    @Test
    @DisplayName("Bottom up 4.1 TC 23: LV2 Test controllo dell interazione sul UNBan dell utente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void UnBanClienteTest() throws RemoteException, CloneNotSupportedException {
        frontDeskStorage = new ClienteDAO();

        Cliente c= CatalogoClienti.getListaClienti().get(0);
        assertNotNull(c);
        frontDesk.unBanCliente(c);
        assertFalse(CatalogoClienti.getlistaBannati().contains(c));
        assertNotEquals(CatalogoClienti.getCliente(c.getCf()), c);
    }

}
