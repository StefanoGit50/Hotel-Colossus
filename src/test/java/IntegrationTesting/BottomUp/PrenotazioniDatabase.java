package IntegrationTesting.BottomUp;

import it.unisa.Common.*;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.AddPrenotazioneCommand;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.RemovePrenotazioneCommand;
import it.unisa.Server.command.CatalogoPrenotazioniCommands.UpdatePrenotazioneCommand;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrenotazioniDatabase {

    private CatalogoPrenotazioni catprenotazioni;

    private FrontDeskStorage<Prenotazione> fds = new PrenotazioneDAO();

    @AfterEach
    public void setup()  {
        catprenotazioni = new CatalogoPrenotazioni();
    }
    @AfterEach
    public void tearDown()  {
        catprenotazioni.getListaPrenotazioni().clear();
    }

    @Test
    @DisplayName("Bottom up: Retrieve della lista nel catalogo prenotazioni")
    @Tag("Integration")
    public void Prenotazioni()  {
        catprenotazioni.getListaPrenotazioni();
        List<Prenotazione> prenotazioni= null;
          try{
            prenotazioni= (List<Prenotazione>) fds.doRetriveAll("discendente");
          }catch (SQLException e){
              e.printStackTrace();
          }

          assertNotNull(prenotazioni);
          assertEquals(catprenotazioni.getListaPrenotazioni().size(), prenotazioni.size());
          for(int i=0;i<prenotazioni.size();i++){
               assertEquals(catprenotazioni.getListaPrenotazioni().get(i), prenotazioni.get(i));
          }
    }

    @Test
    @DisplayName("Bottom up: Aggiunta alla lista del catalogo e successivamente al DB")
    @Tag("integration")
    public void addPrenotazione() throws  SQLException {

        ArrayList<Camera> listcamera=new ArrayList<>();
        listcamera.add(new Camera(112, Stato.Occupata,2,45.50,""));
        ArrayList<Servizio> servizio=new ArrayList<>();
        servizio.add(new Servizio("Piscina",20));
        ArrayList<Cliente> cliente=new ArrayList<>();
        cliente.add(new Cliente("mario","Rossi","Burundi","napoli","napoli","via napoli",12,45,"23453332","m",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com"));
        ArrayList<Prenotazione> prenotazioni=new ArrayList<>();

        prenotazioni.add( new Prenotazione(1234, LocalDate.now(),LocalDate.now(),LocalDate.of(2026,02,01),new Trattamento("MEZZA PENSIONE", 60),"Passaporto",LocalDate.of(2012,03,11),LocalDate.of(2044,12,11),"Mario Biondi","",listcamera,servizio,cliente,"34532MC2"));
        catprenotazioni.addPrenotazioni(prenotazioni);
        // prendo dal database la prenotazione appena inserita
        Prenotazione p =fds.doRetriveByKey(prenotazioni.getFirst().getIDPrenotazione());

        assertEquals(p,prenotazioni.get(0));
    }

    @Test
    @DisplayName("Bottom up: Rimozione dalla lista e dal database")
    @Tag("integration")
    @Tag("exception")
    public void removePrenotazione()  {

        ArrayList<Prenotazione> p= catprenotazioni.getListaPrenotazioni();
        ArrayList<Prenotazione> prenotazioni=new ArrayList<>();
        prenotazioni.addFirst(p.getFirst());

        catprenotazioni.removePrenotazioni(prenotazioni);

        assertThrows(SQLException.class,()-> fds.doRetriveByKey(prenotazioni.getFirst().getIDPrenotazione()));
    }

    @Test
    @DisplayName("Bottom up: Update della lista del catalogo e del database")
    @Tag("integration")
    public void updatePrenotazione() throws SQLException, CloneNotSupportedException {

        ArrayList<Prenotazione> catalogop= catprenotazioni.getListaPrenotazioni();
        ArrayList<Prenotazione> prenotazioni=new ArrayList<>();
        prenotazioni.addFirst(catalogop.getFirst());
        prenotazioni.getFirst().setCheckIn(true);

        catprenotazioni.UpdatePrenotazioni(prenotazioni);

        Prenotazione prenotazione =fds.doRetriveByKey(prenotazioni.getFirst().getIDPrenotazione());

        assertEquals(prenotazione,catprenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()));
    }
}
