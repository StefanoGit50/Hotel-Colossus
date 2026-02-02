package it.unisa.Storage;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.interfacce.FrontDeskInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

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
                "Giulia", "Verdi", "Milano", "Milano",
                "Corso Vittorio Emanuele", 22, 20121, "3479876543", "F",
                LocalDate.of(1992, 8, 15), "VRDGLI92M55F205W",
                "giulia.verdi@gmail.com",
                "Italiana", new Camera()
        );

        Cliente cliente2 = new Cliente(
                "Marco", "Neri", "Roma", "Roma",
                "Via dei Condotti", 5, 11187, "3351122334", "M",
                LocalDate.of(1978, 11, 3), "NREMRA78S03H501U",
                "m.neri@provider.it", "Italiana", new Camera()
        );

        Cliente cliente3 = new Cliente(
                "Sofia", "Bruno", "Firenze", "Firenze",
                "Piazza della Signoria", 1, 50122, "3294455667", "F",
                LocalDate.of(2001, 1, 25), "BRNSFO01A65D612Y",
                "sofia.bruno@studio.it", "Italiana", new Camera()
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
                "Singola lato giardino, molto silenziosa",
                ""
        );

        Camera camera205 = new Camera(
                205,
                Stato.Occupata,
                2,
                150.0,
                "Deluxe con idromassaggio e minibar rifornito",
                ""
        );

        Camera camera404 = new Camera(
                404,
                Stato.Libera,
                4,
                250.0,
                "Suite Familiare, due stanze comunicanti, balcone ampio",
                ""
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

        trattamento = new Trattamento("Mezza Pensione", 180);
    }
}
    /* ************************************************************************************************************** */

    /**
     * Crea una prenotazione con dati di base. Su questa prenotazione si costruiscono tutte le altre.
     *
     * @return {@code Prenotazione} base.
     */
   /* public static Prenotazione createBasePrenotazione() {
       /* ArrayList<Camera> camere = new ArrayList<>();
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

    public static Impiegato createBaseImpiegato() {
        return new Impiegato(
                "mario.rossi",                  // username
                "passwordSicura123!",      // hashedPassword
                true,
                Instant.now().plusSeconds(3600),
                "Mario",               // nome
                "Rossi",                     // cognome
                "Maschio",                   // sesso
                "CID",                       // tipoDocumento
                "AB1234567",                 // numeroDocumento
                80100,                       // CAP
                "Via Roma",                  // via
                "Napoli",                    // provincia
                "Napoli",                    // comune
                10,                          // numeroCivico
                generateRandomCF(),          // codiceFiscale
                "1",                         // telefono
                Ruolo.Manager,               // ruolo (mappato dal testo "Front desk")
                2500.00,                     // stipendio
                LocalDate.of(2024, 1, 15),   // dataAssunzione
                LocalDate.of(2020, 1, 20),   // dataRilascio
                "mario.rossi@HotelColossus.it", // emailAziendale
                "Italiana",                  // cittadinanza
                LocalDate.of(2099, 1, 20)    // dataScadenza
        );
    }

    public static String generateRandomCF() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rn =  new Random();

        // Aggiunge len caratteri random
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(rn.nextInt(chars.length())));
        }

        return sb.toString();
    }

    public static void main() throws Exception {
        istantiateFrontDesk();
        PrenotazioneDAO dao = new PrenotazioneDAO();
        Prenotazione p = createBasePrenotazione(), campione = null;
        try {
            System.out.println(p);
           //dao.doSave(p);
            campione = dao.doRetriveByKey(p.getIDPrenotazione());
            System.out.println(campione);
            System.out.println(Objects.equals(campione, p));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/