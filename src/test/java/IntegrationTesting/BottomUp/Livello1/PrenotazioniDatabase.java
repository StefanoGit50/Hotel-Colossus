package IntegrationTesting.BottomUp.Livello1;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrenotazioniDatabase {
    
    private FrontDeskStorage<Prenotazione> fds = new PrenotazioneDAO();

    @AfterEach
    public void setup()  {
        CatalogoPrenotazioni.aggiornalista();
    }
    @AfterEach
    public void tearDown()  {
        DBPopulator.cancel();
        DBPopulator.populator();
        CatalogoPrenotazioni.getListaPrenotazioni().clear();
    }

    @Test
    @DisplayName("Bottom up TC8: Retrieve della lista nel catalogo prenotazioni")
    @Tag("Integration")
    public void RetrievePrenotazioni(){

        List<Prenotazione> prenotazioni= null;
          try{
            prenotazioni= (List<Prenotazione>) fds.doRetriveAll("decrescente");
          }catch (SQLException e){
              e.printStackTrace();
          }

          assertNotNull(prenotazioni);
          assertEquals(CatalogoPrenotazioni.getListaPrenotazioni().size(), prenotazioni.size());
          for(int i=0;i<prenotazioni.size();i++){ // verifica che ogni elemento è uguale
               assertEquals(CatalogoPrenotazioni.getListaPrenotazioni().get(i), prenotazioni.get(i));
          }
    }

    @Test
    @DisplayName("Bottom up TC9: Aggiunta alla lista del catalogo e successivamente al DB")
    @Tag("integration")
    public void addPrenotazione() throws  SQLException {

        CatalogoClienti.aggiornalista();
        ClienteDAO clienteDAO=new ClienteDAO();
        Cliente cliente1 = clienteDAO.doRetriveByKey("BNCLCU85C03G273Z");
        assertTrue(CatalogoClienti.clienteIsEquals(cliente1)); // se ce un cliente contenuto nel catalogo

        ArrayList<Camera> listcamera=new ArrayList<>();
        CameraDAO cameraDAO = new CameraDAO();
        Camera camera =cameraDAO.doRetriveByKey(101);
        listcamera.add(camera);
        cliente1.setCamera(listcamera.getFirst());

        ArrayList<Servizio> servizio=new ArrayList<>();
        ServizioDAO servizioDAO=new ServizioDAO();
        ArrayList<Servizio> servizioDalDB = (ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome","Parcheggio");
        servizio.add(servizioDalDB.getFirst());
        servizioDalDB = (ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome","Spa e Benessere");
        servizio.add(servizioDalDB.getFirst());
        servizioDalDB = (ArrayList<Servizio>) servizioDAO.doRetriveByAttribute("Nome","Transfer Aeroporto");
        servizio.add(servizioDalDB.getFirst());

        ArrayList<Cliente> cliente=new ArrayList<>();


        cliente1.setCamera(listcamera.getFirst());
        cliente.add(cliente1);

        Prenotazione prenotazione =new Prenotazione(
                LocalDate.now(),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20),
                null,
                new Trattamento("Pensione Completa", 100.0),
                100.0,
                "Passaporto",
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2032, 10, 10),
                cliente.getFirst().getNome()+" "+cliente.getFirst().getCognome(),
                "Privacy assoluta",
                servizio, cliente,
                "UK1234567",
                null,
                "Albanese"
        );
        CatalogoPrenotazioni.addPrenotazioni(prenotazione);
        // prendo dal database la prenotazione appena inserita
        Prenotazione p =fds.doRetriveByKey(prenotazione.getIDPrenotazione());
        System.out.println(p);
        System.out.println(prenotazione);

        assertEquals(p,prenotazione);
    }

    @Test
    @DisplayName("Bottom up TC10: Rimozione dalla lista e dal database")
    @Tag("integration")
    @Tag("exception")
    public void removePrenotazione()  {

        int dimensioneIniziale = CatalogoPrenotazioni.getListaPrenotazioni().size();

        Prenotazione p1 = CatalogoPrenotazioni.getListaPrenotazioni().getFirst();

        CatalogoPrenotazioni.removePrenotazioni(p1);

        int dimensioneFinale = CatalogoPrenotazioni.getListaPrenotazioni().size();

        assertEquals(dimensioneIniziale - 1, dimensioneFinale);
    }

    @Test
    @DisplayName("Bottom up TC11: Update della lista del catalogo e del database")
    @Tag("integration")
    public void updatePrenotazione() throws SQLException {
        ClienteDAO clienteDAO=new ClienteDAO();
        Cliente c1= clienteDAO.doRetriveByKey("BNCLCU85C03G273Z");
        Cliente c2 = clienteDAO.doRetriveByKey("MULLER88E05Z112K");
        ArrayList<Cliente> clienteArray = new ArrayList<>();
        clienteArray.add(c1); clienteArray.add(c2);

        ArrayList<Prenotazione> catalogop= CatalogoPrenotazioni.getListaPrenotazioni();
        Prenotazione prenotazione = catalogop.getFirst();
        prenotazione.setListaClienti(clienteArray);

        CatalogoPrenotazioni.UpdatePrenotazioni(prenotazione);

        fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(prenotazione,CatalogoPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()));
    }
}
