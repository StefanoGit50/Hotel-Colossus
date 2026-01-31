package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;
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
import java.util.Random;

public class Test {
    public static FrontDeskInterface frontDesk;
    public static ArrayList<Camera> listaCamere = new ArrayList<>();
    public static ArrayList<Servizio> listaServizi = new ArrayList<>();
    public static ArrayList<Cliente> listaClienti = new ArrayList<>();
    public static Trattamento trattamento = new Trattamento("Mezza Pensione", 125.00);



    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost:1099/GestionePrenotazioni");

        // Lista clienti

        Cliente cliente1 = new Cliente(
                "Giulia", "Verdi", "Italiana", "Milano", "Milano",
                "Corso Vittorio Emanuele", 22, 20121, "3479876543", "F",
                LocalDate.of(1992, 8, 15), "VRDGLI92M55F205W",
                "giulia.verdi@gmail.com",
                "Italiana"
        );

        Cliente cliente2 = new Cliente(
                "Marco", "Neri", "Italiana", "Roma", "Roma",
                "Via dei Condotti", 5, 11187, "3351122334", "M",
                LocalDate.of(1978, 11, 3), "NREMRA78S03H501U",
                "m.neri@provider.it","Italiana"
        );

        Cliente cliente3 = new Cliente(
                "Sofia", "Bruno", "Italiana", "Firenze", "Firenze",
                "Piazza della Signoria", 1, 50122, "3294455667", "F",
                LocalDate.of(2001, 1, 25), "BRNSFO01A65D612Y",
                "sofia.bruno@studio.it","Italiana"
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
        Prenotazione nuovaPrenotazione = new Prenotazione(
                1,                                      // IDPrenotazione
                LocalDate.now(),                        // dataCreazionePrenotazione (Oggi)
                LocalDate.of(2026, 8, 10),              // dataInizio (Check-in)
                LocalDate.of(2026, 8, 17),              // dataFine (Check-out)
                trattamento,                            // Oggetto Trattamento (già istanziato sopra)
                "Carta di Identità",                    // tipoDocumento
                LocalDate.of(2020, 5, 20),              // dataRilascio documento
                LocalDate.of(2030, 5, 19),              // dataScadenza documento
                "Giulia Verdi",                         // intestatario (Coerente con cliente1 della lista)
                "Richiesta culla in camera",            // noteAggiuntive
                listaCamere,                            // ArrayList<Camera> (già istanziata)
                listaServizi,                           // ArrayList<Servizio> (già istanziata)
                listaClienti,                           // ArrayList<Cliente> (già istanziata)
                "VRDGL"                      // numeroDocumento (Coerente con cliente1)
        );
        return nuovaPrenotazione;
    }

    static void main()  {
        PrenotazioneDAO dao = new PrenotazioneDAO();

        Prenotazione p = Test.createBasePrenotazione();
        if(p != null) {
            System.out.println(1);
        }
        if (p.getTrattamento() != null) {
            System.out.println(2);
        }
    }
}
