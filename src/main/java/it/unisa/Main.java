package it.unisa;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String password = "password123";
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        Label label = new Label("Password hashata: " + hashed);
        VBox root = new VBox(label);
        root.setSpacing(10);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 450, 100);

        stage.setTitle("Test JavaFX + BCrypt");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}