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
        listcamera.add(new Camera(112, Stato.Occupata,2,45.50,"",""));
        ArrayList<Servizio> servizio=new ArrayList<>();
        servizio.add(new Servizio("Piscina",20));
        ArrayList<Cliente> cliente=new ArrayList<>();
        cliente.add(new Cliente("mario","Rossi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana",new Camera()));
        Prenotazione prenotazione =new Prenotazione(5,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20),
                LocalDate.of(2025, 12, 1),
                new Trattamento("Pensione Completa Deluxe", 5000.0),
                "Passaporto",
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2032, 10, 10),
                "Mr. John Smith",
                "Privacy assoluta",
                listcamera, servizio, cliente,
                "UK123456789",
                "Amex Black"
        );
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
