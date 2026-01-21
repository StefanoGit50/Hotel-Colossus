package it.unisa.GUI;

import it.unisa.GUI.components.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp4 extends Application {

    // setting della scena principale

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Colossus - Login");

        LoginView loginView = new LoginView();

        loginView.setLoginCallback((username, password) -> {
            if (authenticateUser(username, password)) {
                System.out.println("✅ Login effettuato: " + username);
                loginView.showSuccess("Benvenuto, " + username + "!");
                // TODO: Passa a MainApp3
            } else {
                System.out.println("❌ Login fallito");
                loginView.clearFields();
            }
        });

        Scene scene = new Scene(loginView, 1200, 700);
        scene.getStylesheets().add(
                getClass().getResource("/hotel.css").toExternalForm()
        );


        primaryStage.setScene(scene);
        primaryStage.setResizable(true);  // ✅ ABILITA RESIZE
        primaryStage.setMinWidth(800);    // ✅ LARGHEZZA MINIMA
        primaryStage.setMinHeight(600);   // ✅ ALTEZZA MINIMA
        primaryStage.setMaximized(true);  // Parte massimizzata
        primaryStage.centerOnScreen();

        //abilita f11 per fare fullscreen
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        primaryStage.show();
    }


    private boolean authenticateUser(String username, String password) {
        // Mock: admin/admin
        return username.equals("admin") && password.equals("admin");
    }

    public static void main(String[] args) {
        launch(args);
    }
}