package IntegrationTesting.BottomUp.Livello2;

import it.unisa.Common.*;
import it.unisa.Server.command.Invoker;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
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
    private static ArrayList<Servizio>  servizio;
    private static ArrayList<Cliente> cliente;
    private static FrontDeskStorage frontDeskStorage;

    @BeforeAll
    static void setAllup() throws RemoteException {
        //inizializzazione variabili di default
        //si suppone che il DB contenga queste variabili per il corretto funzionamento del test
        Invoker invoker = new Invoker();
        CatalogoPrenotazioni catPrenotazioni = new CatalogoPrenotazioni();
        CatalogoClienti catClienti = new CatalogoClienti();
        CatalogoCamere catCamere = new CatalogoCamere();
        frontDesk = new FrontDesk(invoker,catPrenotazioni,catClienti,catCamere);

        servizio.add( new Servizio("Piscina",20));
        arraycamera.add(new Camera(112, Stato.Occupata,2,45.50,"",""));
        prenotazione =new Prenotazione(1234, LocalDate.now(),LocalDate.now(),LocalDate.of(2026,02,01),new Trattamento("MEZZA PENSIONE", 60),"Passaporto",LocalDate.of(2012,03,11),LocalDate.of(2044,12,11),"Mario Biondi","",arraycamera,servizio,cliente,"34532MC2");
        cliente.add( new Cliente("mario","Rossi","Burundi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana", new Camera()));
    }

    @AfterEach
    void tearDown(){
        catPrenotazioni.getListaPrenotazioni().clear();
        catClienti.getListaClienti().clear();
        catCamere.getListaCamere().clear();
    }

    @Test
    @DisplayName("Bottom up 3.1: LV2 aggiornamento dello stato camera dalla classe server con pattern Observer")
    @Tag("integration-LV2")
    public void AggiornaStatoCamereCatalogoDB(){
        assertDoesNotThrow(()->frontDesk.aggiornaStatoCamera(arraycamera.getFirst()));

        Camera c = null;
        //controllo nel DB se è stata cambiata lo stato della camera
        try {
             c= (Camera) frontDeskStorage.doRetriveByKey(arraycamera.getFirst());

        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(arraycamera.getFirst(),c);
    }

    @Test
    @DisplayName("Bottom up 3.2: LV2 Test se le camere nel front desk sono le stesse nel DB ")
    @Tag("integration-LV2")
    public void getCamereTest(){
       List<Camera> c=frontDesk.getCamere();
       Collection<?> listDB= null;

        //controllo nel DB se le liste sono le stesse
        try {
            listDB= frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(listDB, c);
    }

    @Test
    @DisplayName("Bottom up 3.3: LV2 Test se le prenotazioni nel front desk sono le stesse nel DB")
    @Tag("integration-LV2")
    public void getPrenotazioniTest() throws RemoteException {
        List<Prenotazione> p = frontDesk.getPrenotazioni();
        Collection<?> listDB= null;

        // controllo nel DB se le liste sono le stesse
        try{
            listDB= frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(listDB, p);
    }

    @Test
    @DisplayName("Bottom up 3.4: LV2 Test controllo dell'interazione sull aggiunta fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void addPrenotazioneTest() throws RemoteException, CloneNotSupportedException {
        ArrayList<Camera> arrcam= new ArrayList<>();
        arrcam.add(catCamere.getCamera(102));
        ArrayList<Cliente> clienti= new ArrayList<>();
        clienti.add(catClienti.getListaClienti().get(0));
        ArrayList<Servizio> servizio= new ArrayList<>();
        servizio.add(new Servizio("SPA",40));

        Prenotazione p = new Prenotazione(12124, LocalDate.now(),LocalDate.now(),LocalDate.of(2026,02,01),new Trattamento("MEZZA PENSIONE", 60),"Passaporto",LocalDate.of(2012,03,11),LocalDate.of(2044,12,11),"Mario Biondi","",arrcam,servizio,clienti,"34532MC2");
        frontDesk.addPrenotazione(p);
        Prenotazione p1= null;
        // controllo nel DB se la prenotazione è presente
        try{
            p1= (Prenotazione) frontDeskStorage.doRetriveByKey(prenotazione);
        }catch (SQLException e){
            e.printStackTrace();
         }
        assertEquals(p1, p);
    }

    @Test
    @DisplayName("Bottom up 3.5: LV2 Test controllo dell'interazione sulla rimozione fra frontdeskServer command pattern e DB")
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
    @DisplayName("Bottom up 3.6: LV2 Test controllo dell interazione sull update fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void updatePrenotazioneTest() throws RemoteException{
        Prenotazione p2 = catPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione());
        prenotazione.setCheckIn(true);
        frontDesk.updatePrenotazione(prenotazione);

        try{
            prenotazione= (Prenotazione) frontDeskStorage.doRetriveByKey(prenotazione);
        }catch (SQLException e){
            e.printStackTrace();
        }

        assertNotEquals(prenotazione, p2);
        assertNotEquals(catPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()), p2);
    }

    @Test
    @DisplayName("Bottom up 3.7: LV2 Test controllo dell interazione sull'aggiunta del cliente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void addClienteTest() throws RemoteException {
        Cliente c = new Cliente("Roberto","Rossi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana",new Camera());
        frontDesk.addCliente(c);

        Collection <?> clienti= new ArrayList<>();
        try{
            assertDoesNotThrow(()->frontDeskStorage.doRetriveByKey(c.getCf()));
            clienti= frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e ){
            e.printStackTrace();
        }
        assertEquals(catClienti,clienti );
    }

    @Test
    @DisplayName("Bottom up 3.8: LV2 Test controllo dell interazione sulla rimozione del cliente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void removeClienteTest() throws RemoteException {
        frontDesk.removeCliente(cliente.getFirst());
        Collection<?> listDB= null;

        assertThrows(SQLException.class,()->frontDeskStorage.doRetriveByKey(cliente.getFirst()));
        try{
            listDB=frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(catClienti,listDB);
    }

    @Test
    @DisplayName("Bottom up 3.9: LV2 Test controllo dell interazione sull update fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void updateClienteTest() throws RemoteException {
        Cliente c= cliente.getFirst();
        cliente.getFirst().setVia("via giovanni muratore");
        frontDesk.updateCliente(cliente.getFirst());

        Cliente c2= null;
        try{
           c2= (Cliente) frontDeskStorage.doRetriveByKey(cliente.getFirst());
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertNotEquals(c,c2);
    }

    @Test
    @DisplayName("Bottom up 4.0: LV2 Test controllo dell interazione sul Ban dell utente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void BanClienteTest() throws RemoteException {
        Cliente c= cliente.getFirst();
        frontDesk.banCliente(cliente.getFirst());
        assertNotEquals(cliente.getFirst(),c);
    }

    @Test
    @DisplayName("Bottom up 4.1: LV2 Test controllo dell interazione sul UNBan dell utente fra frontdeskServer command pattern e DB")
    @Tag("integration-LV2")
    public void UnBanClienteTest() throws RemoteException {
      //TODO  ArrayList<Cliente> c1= frontDesk.getListClienti();
        for(Cliente c: cliente){ // TODO c1
            if(c.isBlacklisted()){
                frontDesk.banCliente(c);
            }
        }
        Collection <?> clienti= null;
        try{
            clienti= frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(clienti,catClienti.getListaClienti());
    }
}
