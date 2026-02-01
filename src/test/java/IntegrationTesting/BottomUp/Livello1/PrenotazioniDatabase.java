package IntegrationTesting.BottomUp.Livello1;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
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
<<<<<<< Updated upstream
        cliente.add(new Cliente("mario","Rossi","Burundi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana",new Camera()));
=======
        cliente.add(new Cliente("mario","Rossi","Burundi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana", new Camera()));
>>>>>>> Stashed changes
        Prenotazione prenotazione =new Prenotazione(1234, LocalDate.now(),LocalDate.now(),LocalDate.of(2026,02,01),new Trattamento("MEZZA PENSIONE", 60),"Passaporto",LocalDate.of(2012,03,11),LocalDate.of(2044,12,11),"Mario Biondi","",listcamera,servizio,cliente,"34532MC2");
        catprenotazioni.addPrenotazioni(prenotazione);
        // prendo dal database la prenotazione appena inserita
        Prenotazione p =fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(p,prenotazione);
    }

    @Test
    @DisplayName("Bottom up: Rimozione dalla lista e dal database")
    @Tag("integration")
    @Tag("exception")
    public void removePrenotazione() throws CloneNotSupportedException {

        ArrayList<Prenotazione> arrayList= catprenotazioni.getListaPrenotazioni();
        Prenotazione p1=catprenotazioni.getPrenotazione(arrayList.getFirst().getIDPrenotazione());

        catprenotazioni.removePrenotazioni(p1);

        assertThrows(SQLException.class,()-> fds.doRetriveByKey(p1.getIDPrenotazione()));
        assertNotEquals(catprenotazioni.getListaPrenotazioni().size(), arrayList.size());
    }

    @Test
    @DisplayName("Bottom up: Update della lista del catalogo e del database")
    @Tag("integration")
    public void updatePrenotazione() throws SQLException, CloneNotSupportedException {

        ArrayList<Prenotazione> catalogop= catprenotazioni.getListaPrenotazioni();
        Prenotazione prenotazione = catalogop.getFirst();
        prenotazione.setCheckIn(true);

        catprenotazioni.UpdatePrenotazioni(prenotazione);

        fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(prenotazione,catprenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()));
    }
}
