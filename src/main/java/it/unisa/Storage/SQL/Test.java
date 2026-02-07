package it.unisa.Storage.SQL;

import it.unisa.Common.*;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import it.unisa.interfacce.FrontDeskInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class Test {

    public static FrontDeskInterface frontDesk;
    public static ArrayList<Servizio> listaServizi = new ArrayList<>();
    public static ArrayList<Camera> listaCamere = new ArrayList<>();
    public static ArrayList<Cliente> listaClienti = new ArrayList<>();
    public static Trattamento trattamento = new Trattamento("Mezza Pensione", 125.00);
    public static int autoIncrement; // simula l'autoincrement del DB

    public static void istantiateNumberOfInstances(){
        // Numero di prenotazioni presenti nel sistema + 1 = prenotazione successiva ad essere memorizzata
        try {
            autoIncrement = new PrenotazioneDAO().doRetriveAll("").size() + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost:1099/GestionePrenotazioni");

        // Lista clienti

        Cliente cliente1 = new Cliente(
                "Luigi", "Verdi", "Milano", "Milano",
                "Corso Buenos Aires", 20, 20100, "3339876543", "M",
                LocalDate.of(1990, 2, 2), "VRDLGI90B02F205K",
                "luigi.verdi@email.com",
                "Italiana",
                new Camera()
        );

        Cliente cliente2 = new Cliente(
                "Mario", "Rossi", "Roma", "Roma",
                "Via del Corso", 10, 10000, "3331234567", "M",
                LocalDate.of(1920, 1, 1), "RSSMRA80A01H501U",
                "mario.rossi@email.com","Italiana",
                new Camera()
        );

        Cliente cliente3 = new Cliente(
                "Lucia", "Bianchi", "Palermo", "Palermo",
                "Via Roma", 5, 90100, "3381122334", "F",
                LocalDate.of(1985, 3, 3), "BNCLCU85C03G273Z",
                "lucia.bianchi@email.com","Italiana",
                new Camera()
        );

        // Lista camere

        Camera camera101 = new Camera(
                101,
                Stato.Libera,
                2,
                80.0,
                "Vista interna",
                "Camera Standard"
        );

        Camera camera102 = new Camera(
                102,
                Stato.Occupata,
                2,
                80,
                "Vista strada",
                "Camera Standard"
        );

        Camera camera201 = new Camera(
                201,
                Stato.Prenotata,
                3,
                180.0,
                "Vista mare laterale",
                "Junior Suite"
        );

        // lista camere

        listaCamere.add(camera101);
        listaCamere.add(camera102);
        listaCamere.add(camera201);
        cliente1.setCamera(camera101);
        cliente2.setCamera(camera102);
        cliente3.setCamera(camera201);

        // Lista clienti

        listaClienti.add(cliente1);
        listaClienti.add(cliente2);
        listaClienti.add(cliente3);

        // Lista servizi

        Servizio servizioMinibar = new Servizio("Parcheggio", 10.00);
        Servizio servizioParcheggio = new Servizio("WiFi Premium", 5.00);
        Servizio servizioColazione = new Servizio("Colazione in Camera", 12.00);
        servizioColazione.setId(1);
        servizioParcheggio.setId(2);
        servizioMinibar.setId(3);
        listaServizi.add(servizioMinibar);

        // Trattamento

        trattamento = new Trattamento("Mezza Pensione", 35);
    }

    /* ************************************************************************************************************** */

    /**
     * Crea una prenotazione con dati di base. Su questa prenotazione si costruiscono tutte le altre.
     *
     * @return {@code Prenotazione} base.
     */
    public static Prenotazione createBasePrenotazione() {
        ArrayList<Cliente> cliente = new ArrayList<>();
        cliente.add(listaClienti.getFirst());

        return new Prenotazione(
                LocalDate.now(),                // 2. Data Creazione
                LocalDate.of(2026, 3, 17),    // 3. Data Inizio
                LocalDate.of(2026, 3, 20),    // 4. Data Fine
                null,                           // 5. DataEmissioneRicevuta
                trattamento,                    // 6. Trattamento
                trattamento.getPrezzo(),                      // 7. Tipo Documento
                "Patente",
                LocalDate.of(2020, 1, 10),      // 8. Data Rilascio
                LocalDate.of(2030, 1, 10),      // 9. Data Scadenza
                cliente.getFirst().getNome() +" "+ cliente.getFirst().getCognome(),                          // 10. Intestatario
                null,                             // 11. Note Aggiuntive
                listaServizi,                   // 13. Lista Servizi
                cliente,                        // 14. Lista Clienti
                "CA123AA",                     // 15. Numero Documento
                null,
                "Italiana"// 16. Metodo Pagamento
        );
    }

    static void main() throws Exception {

        Test.istantiateNumberOfInstances();
        Test.istantiateFrontDesk();
//        PrenotazioneDAO dao = new PrenotazioneDAO();
//        Prenotazione p = Test.createBasePrenotazione(), campione;
//        p.setIDPrenotazione(autoIncrement);
//        dao.doSave(p);
//        campione = dao.doRetriveByKey(autoIncrement);
//
//        listaServizi = new ArrayList<>(1);
//  listaServizi.add(new Servizio("Colazione in Camera", 12.00));
//        campione.setListaServizi(listaServizi);
//        System.out.println(autoIncrement);
//        System.out.println(p);
//        System.out.println(campione);
//        System.out.println(p.equals(campione));

    }
}