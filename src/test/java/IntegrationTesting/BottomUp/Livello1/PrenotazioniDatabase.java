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
    
    private FrontDeskStorage<Prenotazione> fds = new PrenotazioneDAO();

    @AfterEach
    public void setup()  {
        CatalogoPrenotazioni.aggiornalista();
    }
    @AfterEach
    public void tearDown()  {
        CatalogoPrenotazioni.getListaPrenotazioni().clear();
    }

    @Test
    @DisplayName("Bottom up: Retrieve della lista nel catalogo prenotazioni")
    @Tag("Integration")
    public void RetrievePrenotazioni()  {

        List<Prenotazione> prenotazioni= null;
          try{
            prenotazioni= (List<Prenotazione>) fds.doRetriveAll("decrescente");
          }catch (SQLException e){
              e.printStackTrace();
          }

          assertNotNull(prenotazioni);
          assertEquals(CatalogoPrenotazioni.getListaPrenotazioni().size(), prenotazioni.size());
          for(int i=0;i<prenotazioni.size();i++){
               assertEquals(CatalogoPrenotazioni.getListaPrenotazioni().get(i), prenotazioni.get(i));
          }
    }

    @Test
    @DisplayName("Bottom up: Aggiunta alla lista del catalogo e successivamente al DB")
    @Tag("integration")
    public void addPrenotazione() throws  SQLException {

        ArrayList<Camera> listcamera=new ArrayList<>();
        listcamera.add(new Camera(112, Stato.Occupata,2,45.50,"","Pino"));
        ArrayList<Servizio> servizio=new ArrayList<>();
        servizio.add(new Servizio("Piscina",20));
        ArrayList<Cliente> cliente=new ArrayList<>();
        cliente.add(new Cliente("mario","Rossi","napoli","napoli","via manzo",12,45,"323425","M",LocalDate.of(1998,12,1),"CF234rdfcfg","luca@gmail.com","italiana",listcamera.getFirst()));
        Prenotazione prenotazione =new Prenotazione(
                LocalDate.now(),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20),
                LocalDate.of(2025, 12, 1),
                new Trattamento("Pensione Completa", 100.0),
                100.0,
                "Passaporto",
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2032, 10, 10),
                cliente.getFirst().getNome()+" "+cliente.getFirst().getCognome(),
                "Privacy assoluta",
                servizio, cliente,
                "UK123456789",
                "carta di credito",
                "Albanese"
        );
        CatalogoPrenotazioni.addPrenotazioni(prenotazione);
        // prendo dal database la prenotazione appena inserita
        Prenotazione p =fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(p,prenotazione);
    }

    @Test
    @DisplayName("Bottom up: Rimozione dalla lista e dal database")
    @Tag("integration")
    @Tag("exception")
    public void removePrenotazione() throws CloneNotSupportedException {

        ArrayList<Prenotazione> arrayList= CatalogoPrenotazioni.getListaPrenotazioni();
        Prenotazione p1=CatalogoPrenotazioni.getPrenotazione(arrayList.getFirst().getIDPrenotazione());

        CatalogoPrenotazioni.removePrenotazioni(p1);

        assertThrows(SQLException.class,()-> fds.doRetriveByKey(p1.getIDPrenotazione()));
        assertNotEquals(CatalogoPrenotazioni.getListaPrenotazioni().size(), arrayList.size());
    }

    @Test
    @DisplayName("Bottom up: Update della lista del catalogo e del database")
    @Tag("integration")
    public void updatePrenotazione() throws SQLException, CloneNotSupportedException {

        ArrayList<Prenotazione> catalogop= CatalogoPrenotazioni.getListaPrenotazioni();
        Prenotazione prenotazione = catalogop.getFirst();
        prenotazione.setCheckIn(true);

        CatalogoPrenotazioni.UpdatePrenotazioni(prenotazione);

        fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(prenotazione,CatalogoPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()));
    }
}
