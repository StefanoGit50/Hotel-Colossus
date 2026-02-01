package it.unisa.Client.GUI;

import it.unisa.Client.GUI.components.BookingDetail;
import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainApp4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Colossus - Booking Detail Test");

        // ✅ UNA SOLA CHIAMATA - Dati passati al costruttore
        BookingDetail bookingDetail = new BookingDetail(
                creaPrenotazioneDiProva(),
                creaCatalogo()
        );

        Scene scene = new Scene(bookingDetail, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/hotel.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private List<Servizio> creaCatalogo() {
        // Catalogo completo servizi disponibili
        return Arrays.asList(
                new Servizio("Accesso Piscina", 80.0),
                new Servizio("Spa & Wellness", 85.0),
                new Servizio("Bottiglia Vino", 45.0),
                new Servizio("Transfer Aeroporto", 50.0),
                new Servizio("Colazione in Camera", 15.0)
        );
    }

    private Prenotazione creaPrenotazioneDiProva() {
        ArrayList<Camera> camere = new ArrayList<>(List.of(new Camera(101, Stato.Occupata,2,35,"asdasd")));
        ArrayList<Cliente> arrayCliente = new ArrayList<>(List.of(new Cliente("Alessio","Colardi","italiana","napoli","caserta","via fas",234,234,"3243543","M", LocalDate.of(2001,01,30),"23rtygfds2",
                "luca@smdb", "Italiana", camere.getFirst())));
        ArrayList<Servizio> servizio = new ArrayList<>(List.of(new Servizio("Bottiglia Vino",60)));

        // Clienti
        Cliente alessio = new Cliente(
                "Alessio", "Colardi", "italiana", "napoli", "caserta",
                "via fas", 234, 234, "3243543", "M",
                LocalDate.of(2001, 1, 30), "23rtygfds2",
                "luca@smdb", "italiana", camere.getFirst()
        );
        alessio.setIntestatario(true); // ✅ Intestatario della prenotazione

        arrayCliente = new ArrayList<>(List.of(alessio));

        // Servizi prenotati (quello che il cliente ha già ordinato)
        ArrayList<Servizio> serviziPrenotati = new ArrayList<>(List.of(
                new Servizio("Bottiglia Vino", 45.0)
        ));

        // Trattamento
        Trattamento trattamento = new Trattamento("MEZZA PENSIONE", 55.5);

        // Prenotazione
        Prenotazione p = new Prenotazione(
                1234,                                   // ID
                LocalDate.of(2026, 1, 31),             // Data creazione
                LocalDate.of(2026, 2, 11),             // Data inizio
                LocalDate.of(2026, 2, 13),             // Data fine (2 notti)
                trattamento,
                "Patente",                             // Tipo documento
                LocalDate.of(2021, 2, 13),             // Data rilascio
                LocalDate.of(2028, 2, 13),             // Data scadenza
                alessio.getNome() + " " + alessio.getCognome(), // Intestatario
                "champagne in camera",                 // Note
                camere,
                serviziPrenotati,
                arrayCliente,
                "safnsdj08"                           // IBAN
        );

        p.setCheckIn(false); // Check-in non ancora fatto

        return p;
    }

    public static void main(String[] args) {
        launch(args);
    }
}