package it.unisa.GUITests;

import it.unisa.GUITests.components.ContoEconomico;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class MainApp5 extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Colossus - Conto Economico");

        // Crea il Conto Economico
        ContoEconomico contoEconomico = new ContoEconomico();

        // Crea la scena
        Scene scene = new Scene(contoEconomico, 1600, 900);

        // Carica il CSS (opzionale, se hai il file CSS)
        try {
            scene.getStylesheets().add(
                    getClass().getResource("/hotel.css").toExternalForm()
            );
            System.out.println("✅ CSS caricato con successo");
        } catch (Exception e) {
            System.out.println("⚠️ CSS non trovato, uso stili inline");
        }

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.show();
        primaryStage.centerOnScreen();

        System.out.println("✅ Conto Economico avviato con successo!");
    }

    public static void main(String[] args) {
        System.out.println("🚀 Avvio Conto Economico Hotel Colossus...");
        launch(args);
    }
}

