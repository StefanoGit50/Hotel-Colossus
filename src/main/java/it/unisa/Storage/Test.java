package it.unisa.Storage;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.interfacce.FrontDeskInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {

    public static FrontDeskInterface frontDesk;
    public static ArrayList<Camera> listaCamere = new ArrayList<>();
    public static ArrayList<Servizio> listaServizi = new ArrayList<>();
    public static ArrayList<Cliente> listaClienti = new ArrayList<>();
    public static Trattamento trattamento = new Trattamento("Mezza Pensione", 125.00);
    public static int autoIncrement = 3; // simula l'autoincrement del DB

    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost:1099/GestionePrenotazioni");

        // Lista clienti

        Cliente cliente1 = new Cliente(
                "Giulia", "Verdi", "Italiana", "Milano", "Milano",
                "Corso Vittorio Emanuele", 22, 20121, "3479876543", "F",
                LocalDate.of(1992, 8, 15), "VRDGLI92M55F205W",
                "giulia.verdi@gmail.com",
                "Italiana", new Camera()
        );

        Cliente cliente2 = new Cliente(
                "Marco", "Neri", "Italiana", "Roma", "Roma",
                "Via dei Condotti", 5, 11187, "3351122334", "M",
                LocalDate.of(1978, 11, 3), "NREMRA78S03H501U",
                "m.neri@provider.it","Italiana", new Camera()
        );

        Cliente cliente3 = new Cliente(
                "Sofia", "Bruno", "Italiana", "Firenze", "Firenze",
                "Piazza della Signoria", 1, 50122, "3294455667", "F",
                LocalDate.of(2001, 1, 25), "BRNSFO01A65D612Y",
                "sofia.bruno@studio.it","Italiana", new Camera()
        );

        listaClienti.add(cliente1);
        listaClienti.add(cliente2);
        listaClienti.add(cliente3);

        // Lista camere

        Camera camera102 = new Camera(
                102,
                Stato.Libera,
                1,
                85.0,
                "Singola lato giardino, molto silenziosa"
        );

        Camera camera205 = new Camera(
                205,
                Stato.Occupata,
                2,
                150.0,
                "Deluxe con idromassaggio e minibar rifornito"
        );

        Camera camera404 = new Camera(
                404,
                Stato.Libera,
                4,
                250.0,
                "Suite Familiare, due stanze comunicanti, balcone ampio"
        );

        listaCamere.add(camera102);
        listaCamere.add(camera205);
        listaCamere.add(camera404);

        // Lista servizi

        Servizio servizioMinibar = new Servizio("Consumazione Minibar", 15.50);
        Servizio servizioParcheggio = new Servizio("Parcheggio Coperto", 10.00);
        Servizio servizioColazione = new Servizio("Colazione in Camera", 12.00);

        listaServizi.add(servizioMinibar);
        listaServizi.add(servizioParcheggio);
        listaServizi.add(servizioColazione);

        // Trattamento

        trattamento = new Trattamento("Mezza Pensione", 200);
    }

    /* ************************************************************************************************************** */

    /**
     * Crea una prenotazione con dati di base. Su questa prenotazione si costruiscono tutte le altre.
     *
     * @return {@code Prenotazione} base.
     */
    public static Prenotazione createBasePrenotazione() {
        ArrayList<Camera> camere = new ArrayList<>();
        ArrayList<Cliente> clienti = new ArrayList<>();
        camere.add(listaCamere.getFirst());
        clienti.add(listaClienti.getFirst());
        String nome = clienti.getFirst().getNome();

        return new Prenotazione(
                autoIncrement,  // ID
                LocalDate.now(), // Data Creazione
                LocalDate.now().plusDays(1),            // Data arrivo
                LocalDate.now().plusDays(5),            // Data partenza
                trattamento, "Patente",
                LocalDate.of(2020, 1, 10),
                LocalDate.of(2030, 1, 10),
                "NNN", "",
                camere, // lista camere
                listaServizi, // lista servizi
                clienti, // lista clienti
                "12345678"
        );
    }

    public static void main() throws Exception {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        istantiateFrontDesk();
        Prenotazione prenotazione = createBasePrenotazione();
        Prenotazione prenotazione1;
        prenotazioneDAO.doSave(prenotazione);
        prenotazione1 = prenotazioneDAO.doRetriveByKey(prenotazione.getIDPrenotazione());
        System.out.println(prenotazione);
        System.out.println(prenotazione1);
    }
}
