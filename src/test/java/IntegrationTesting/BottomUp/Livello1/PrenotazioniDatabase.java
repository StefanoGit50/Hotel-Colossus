package IntegrationTesting.BottomUp.Livello1;

import WhiteBox.UnitTest.DBPopulator;
import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
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
          for(int i=0;i<prenotazioni.size();i++){ // verifica che ogni elemento è uguale
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
                LocalDate.of(2025, 12, 1),
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
                "UK123456789",
                "carta di credito",
                "italiana"
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
    public void removePrenotazione()  {

        ArrayList<Prenotazione> arrayList= CatalogoPrenotazioni.getListaPrenotazioni();
        Prenotazione p1=CatalogoPrenotazioni.getPrenotazione(arrayList.getFirst().getIDPrenotazione());

        CatalogoPrenotazioni.removePrenotazioni(p1);

        assertThrows(SQLException.class,()-> {
            assertNotNull(p1);
            fds.doRetriveByKey(p1.getIDPrenotazione());
        });
        assertNotEquals(CatalogoPrenotazioni.getListaPrenotazioni().size(), arrayList.size());
    }

    @Test
    @DisplayName("Bottom up: Update della lista del catalogo e del database")
    @Tag("integration")
    public void updatePrenotazione() throws SQLException {

        Cliente c1 = new Cliente("luca","pizzuto","cagliari","cagliari","via minerva",123,123,"12334","m",LocalDate.of(2001,12,30),"asdafahfasf","luca@email","italiana",
                CatalogoCamere.getCamera(101));
        Cliente c2 = new Cliente("pagliaro","minosto","milano","milano","via andrea ", 1234,1234,"234567","m",LocalDate.of(2001,12,30),"ajhfdbhdafba","asdadsf","nigeria",
                CatalogoCamere.getCamera(102));
        ArrayList<Cliente> clienteArray=new ArrayList<>();
        clienteArray.add(c1); clienteArray.add(c2);
        ArrayList<Prenotazione> catalogop= CatalogoPrenotazioni.getListaPrenotazioni();
        Prenotazione prenotazione = catalogop.getFirst();
        prenotazione.setListaClienti(clienteArray);

        CatalogoPrenotazioni.UpdatePrenotazioni(prenotazione);

        fds.doRetriveByKey(prenotazione.getIDPrenotazione());

        assertEquals(prenotazione,CatalogoPrenotazioni.getPrenotazione(prenotazione.getIDPrenotazione()));
    }
}
