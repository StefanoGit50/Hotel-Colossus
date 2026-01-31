package it.unisa.Client.GUI;

import it.unisa.Client.GUI.components.BookingDetail;
import it.unisa.Client.GUI.components.LoginView;
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

    // setting della scena principale

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Colossus - Login");
        BookingDetail bookingDetail = new BookingDetail();

        Scene scene = new Scene(bookingDetail, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private List<Servizio> creaCatalogo() {
        return Arrays.asList(
                new Servizio("Accesso Piscina", 80.0),
                new Servizio("Spa & Wellness", 85.0), // Presente nel catalogo
                new Servizio("Bottiglia Vino", 45.0), // Presente nel catalogo
                new Servizio("Transfer Aeroporto", 50.0),
                new Servizio("Colazione in Camera", 15.0)
        );
    }

    private Prenotazione creaPrenotazioneDiProva() {
        ArrayList<Camera> camere = new ArrayList<>(List.of(new Camera(101, Stato.Occupata,2,35,"asdasd")));
        ArrayList<Cliente> arrayCliente= new ArrayList<>(List.of(new Cliente("Alessio","Colardi","italiana","napoli","caserta","via fas",234,234,"3243543","M", LocalDate.of(2001,01,30),"23rtygfds2",
                "luca@smdb",camere.getFirst())));
        ArrayList<Servizio> servizio= new ArrayList<>(List.of(new Servizio("Bottiglia Vino",60)));

         Prenotazione p = new Prenotazione(1234,LocalDate.of(2026,01,31),LocalDate.of(2026,02,11),LocalDate.of(2026,02,13),
                 new Trattamento("MEZZA PENSIONE",55.5),"Patente",LocalDate.of(2021,02,13),LocalDate.of(2028,02,13),arrayCliente.getFirst().toString(),
                 "champagne in camera",camere,servizio,arrayCliente,"safnsdj08");

         return  p;
    }


    public static void main(String[] args) {
        launch(args);
    }
}