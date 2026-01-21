package it.unisa.GUI;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * TEST APP - Per diagnosticare problemi con la finestra
 * Sostituisci temporaneamente MainApp con questa classe per testare
 */
public class TestWindowApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("=== DIAGNOSTIC INFO ===");
        System.out.println("🔍 OS: " + System.getProperty("os.name"));
        System.out.println("🔍 Java Version: " + System.getProperty("java.version"));
        System.out.println("🔍 JavaFX Version: " + System.getProperty("javafx.version"));

        // Crea finestra minimale
        primaryStage.setTitle("TEST WINDOW - Hotel Colossus");

        // Prova diversi style
        try {
            primaryStage.initStyle(StageStyle.DECORATED);
            System.out.println("✅ initStyle(DECORATED) impostato");
        } catch (Exception e) {
            System.err.println("❌ Errore initStyle: " + e.getMessage());
        }

        // Info sullo stage
        System.out.println("🔍 Stage Style: " + primaryStage.getStyle());
        System.out.println("🔍 Resizable: " + primaryStage.isResizable());
        System.out.println("🔍 Always On Top: " + primaryStage.isAlwaysOnTop());
        System.out.println("🔍 Full Screen: " + primaryStage.isFullScreen());
        System.out.println("🔍 Iconified: " + primaryStage.isIconified());
        System.out.println("🔍 Maximized: " + primaryStage.isMaximized());

        // Proprietà finestra
        primaryStage.setResizable(true);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setFullScreen(false);

        // Layout semplice
        VBox root = new VBox(20);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("🪟 TEST FINESTRA");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label info = new Label(
                "Se vedi questa finestra SENZA barra del titolo,\n" +
                        "c'è un problema con il window manager.\n\n" +
                        "Dovresti vedere:\n" +
                        "- Barra del titolo con 'TEST WINDOW'\n" +
                        "- Bottone X per chiudere\n" +
                        "- Bottoni minimize/maximize\n" +
                        "- Bordi per ridimensionare"
        );
        info.setStyle("-fx-font-size: 14px;");

        Button closeBtn = new Button("CHIUDI APP (se X non funziona)");
        closeBtn.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-color: #d32f2f; " +
                        "-fx-text-fill: white; " +
                        "-fx-cursor: hand;"
        );
        closeBtn.setOnAction(e -> {
            System.out.println("👋 Chiusura tramite bottone");
            primaryStage.close();
            System.exit(0);
        });

        Button printInfoBtn = new Button("STAMPA INFO FINESTRA");
        printInfoBtn.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        printInfoBtn.setOnAction(e -> printWindowInfo(primaryStage));

        root.getChildren().addAll(title, info, closeBtn, printInfoBtn);

        // Scena
        Scene scene = new Scene(root, 600, 400);

        // Shortcut emergenza
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                System.out.println("⌨️ ESC premuto - chiudo");
                primaryStage.close();
                System.exit(0);
            }
            if ((e.isControlDown() || e.isMetaDown()) &&
                    e.getCode() == javafx.scene.input.KeyCode.Q) {
                System.out.println("⌨️ Ctrl+Q premuto - chiudo");
                primaryStage.close();
                System.exit(0);
            }
        });

        primaryStage.setScene(scene);

        // Stampa info PRIMA di show
        System.out.println("\n=== PRIMA DI SHOW ===");
        printWindowInfo(primaryStage);

        primaryStage.show();

        // Stampa info DOPO show
        System.out.println("\n=== DOPO SHOW ===");
        printWindowInfo(primaryStage);

        System.out.println("\n=== TEST COMPLETATO ===");
        System.out.println("Guarda la finestra e dimmi cosa vedi!");
    }

    private void printWindowInfo(Stage stage) {
        System.out.println("📊 Window Info:");
        System.out.println("  Title: " + stage.getTitle());
        System.out.println("  Style: " + stage.getStyle());
        System.out.println("  Width: " + stage.getWidth());
        System.out.println("  Height: " + stage.getHeight());
        System.out.println("  X: " + stage.getX());
        System.out.println("  Y: " + stage.getY());
        System.out.println("  Resizable: " + stage.isResizable());
        System.out.println("  Showing: " + stage.isShowing());
        System.out.println("  Focused: " + stage.isFocused());
        System.out.println("  FullScreen: " + stage.isFullScreen());
        System.out.println("  Maximized: " + stage.isMaximized());
        System.out.println("  AlwaysOnTop: " + stage.isAlwaysOnTop());
    }

    public static void main(String[] args) {
        System.out.println("🚀 Avvio TEST WINDOW...");
        launch(args);
    }
}