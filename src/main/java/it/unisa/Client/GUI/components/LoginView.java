package it.unisa.Client.GUI.components;

import it.unisa.Server.Eccezioni.IllegalAccess;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * LoginView - Login elegante con sfondo full-screen e card glass morphism
 * + Overlay per credenziali temporanee
 */
public class LoginView extends StackPane {

    // ===== COMPONENTI =====
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    // Overlay per credenziali temporanee
    private StackPane overlayPane;
    private VBox loginCard;

    // ===== CALLBACK =====
    private LoginCallback loginCallback;
    private TempCredentialsCallback tempCredentialsCallback;
    private List<String> params = new ArrayList<>();

    public List<String> getParams() {
        return params;
    }
    /**
     * Costruttore
     */
    public LoginView() {
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        usernameField = new TextField();
        usernameField.setPromptText("Il tuo username");
        usernameField.getStyleClass().add("login-input");

        passwordField = new PasswordField();
        passwordField.setPromptText("La tua password");
        passwordField.getStyleClass().add("login-input");

        loginButton = new Button("ACCEDI");
        loginButton.getStyleClass().add("login-btn");
        loginButton.setOnAction(e -> handleLogin());

        // Enter key per login
        passwordField.setOnAction(e -> handleLogin());
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        // ===== SFONDO FULL-SCREEN =====
        ImageView background = createBackground();

        // ===== CARD CENTRALE CON GLASS MORPHISM =====
        HBox card = createLoginCard();

        // Aggiungi tutto allo StackPane
        this.getChildren().addAll(background, card);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Crea lo sfondo full-screen
     */
    private ImageView createBackground() {
        ImageView bgImage = new ImageView();

        try {
            Image image = new Image(getClass().getResourceAsStream("/hotel-background.png"));
            bgImage.setImage(image);
        } catch (Exception e) {
            System.out.println("⚠️ Immagine non trovata, uso colore di fallback");
            this.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        }

        bgImage.setPreserveRatio(false);
        bgImage.fitWidthProperty().bind(this.widthProperty());
        bgImage.fitHeightProperty().bind(this.heightProperty());

        GaussianBlur blur = new GaussianBlur(2);
        bgImage.setEffect(blur);

        return bgImage;
    }

    /**
     * Crea la card di login con effetto glass
     */
    private HBox createLoginCard() {
        HBox card = new HBox(0);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(650);
        card.setMaxHeight(400);
        card.setAlignment(Pos.CENTER);

        VBox leftSide = createLeftSide();
        VBox rightSide = createFormSide();

        card.getChildren().addAll(leftSide, rightSide);

        return card;
    }

    /**
     * Crea il lato sinistro con logo (Glass Morphism)
     */
    private VBox createLeftSide() {
        VBox leftSide = new VBox(15);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.getStyleClass().add("login-left-glass");
        leftSide.setPadding(new Insets(40, 30, 40, 30));
        leftSide.setPrefWidth(280);

        ImageView logo = createLogo();
        leftSide.getChildren().add(logo);

        return leftSide;
    }

    /**
     * Crea il logo
     */
    private ImageView createLogo() {
        ImageView logoView = new ImageView();

        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo2.png"));
            logoView.setImage(logo);
            logoView.setFitWidth(220);
            logoView.setFitHeight(220);
            logoView.setPreserveRatio(false);
            logoView.setSmooth(true);

            logoView.setEffect(new javafx.scene.effect.DropShadow(
                    20,
                    javafx.scene.paint.Color.rgb(0, 0, 0, 0.5)
            ));

        } catch (Exception e) {
            System.out.println("⚠️ Logo non trovato");
        }

        return logoView;
    }

    /**
     * Crea il lato destro con form
     */
    private VBox createFormSide() {
        VBox formSide = new VBox(15);
        formSide.setAlignment(Pos.CENTER);
        formSide.getStyleClass().add("login-form-glass");
        formSide.setPadding(new Insets(40, 35, 40, 30));
        formSide.setPrefWidth(370);

        Label welcomeLabel = new Label("Benvenuto");
        welcomeLabel.getStyleClass().add("login-welcome-glass");

        Label subtitleLabel = new Label("Accedi al tuo account");
        subtitleLabel.getStyleClass().add("login-subtitle-glass");

        VBox header = new VBox(5, welcomeLabel, subtitleLabel);
        header.setAlignment(Pos.CENTER);

        Region spacer1 = new Region();
        spacer1.setPrefHeight(5);

        VBox usernameGroup = createFormGroup("Username", usernameField);
        VBox passwordGroup = createFormGroup("Password", passwordField);

        loginButton.setMaxWidth(Double.MAX_VALUE);

        Region spacer2 = new Region();
        spacer2.setPrefHeight(3);

        Hyperlink forgotPassword = new Hyperlink("Password dimenticata?");
        forgotPassword.getStyleClass().add("login-link-glass");
        forgotPassword.setOnAction(e -> handleForgotPassword());

        formSide.getChildren().addAll(
                header,
                spacer1,
                usernameGroup,
                passwordGroup,
                loginButton,
                spacer2,
                forgotPassword
        );

        return formSide;
    }

    /**
     * Crea un gruppo form
     */
    private VBox createFormGroup(String labelText, Control input) {
        VBox group = new VBox(8);

        Label label = new Label(labelText);
        label.getStyleClass().add("login-label-glass");

        group.getChildren().addAll(label, input);

        return group;
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("login-root");
    }

    // ===== HANDLERS =====

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Per favore inserisci username e password");
            return;
        }

        if (loginCallback != null) {
            try {
                loginCallback.onLogin(username, password);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccess e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Dimenticata");
        alert.setHeaderText("Recupero Password");
        alert.setContentText("Contatta l'amministratore di sistema per recuperare la password.");

        // Aggiungi pulsante custom
        ButtonType tempCredBtn = new ButtonType("Inserisci credenziali temporanee", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(tempCredBtn, cancelBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == tempCredBtn) {
                showTempCredentialsOverlay();
            }
        });
    }

    /**
     * Mostra l'overlay per credenziali temporanee
     */
    private void showTempCredentialsOverlay() {
        // Crea overlay se non esiste
        if (overlayPane == null) {
            createOverlay();
        }

        // Mostra overlay
        if (!this.getChildren().contains(overlayPane)) {
            this.getChildren().add(overlayPane);
        }
        overlayPane.setVisible(true);
    }

    /**
     * Crea l'overlay panel
     */
    private void createOverlay() {
        overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlayPane.setAlignment(Pos.CENTER);

        // Card overlay
        VBox overlayCard = new VBox(20);
        overlayCard.setMaxWidth(480);
        overlayCard.setMaxHeight(500);
        overlayCard.setPadding(new Insets(35, 40, 35, 40));
        overlayCard.setAlignment(Pos.TOP_CENTER);
        overlayCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 30, 0, 0, 10);"
        );

        // Header
        Label overlayTitle = new Label("Credenziali Temporanee");
        overlayTitle.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: #6d1331;"
        );

        Label overlaySubtitle = new Label("Inserisci le credenziali temporanee fornite dall'amministratore");
        overlaySubtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #666;" +
                        "-fx-wrap-text: true;" +
                        "-fx-text-alignment: center;"
        );
        overlaySubtitle.setMaxWidth(400);

        VBox headerBox = new VBox(8, overlayTitle, overlaySubtitle);
        headerBox.setAlignment(Pos.CENTER);

        // Fields
        TextField tempUsernameField = new TextField();
        tempUsernameField.setPromptText("Receptionist1");
        tempUsernameField.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12px 15px;" +
                        "-fx-font-size: 14px;"
        );

        PasswordField tempPasswordField = new PasswordField();
        tempPasswordField.setPromptText("PWDTMP-231WDiSeV");
        tempPasswordField.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12px 15px;" +
                        "-fx-font-size: 14px;"
        );

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("HCrec231");
        newPasswordField.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12px 15px;" +
                        "-fx-font-size: 14px;"
        );

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("HCrec231");
        confirmPasswordField.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12px 15px;" +
                        "-fx-font-size: 14px;"
        );

        VBox usernameBox = createOverlayField("Username", tempUsernameField);
        VBox tempPwdBox = createOverlayField("Password Temporanea", tempPasswordField);
        VBox newPwdBox = createOverlayField("Nuova Password", newPasswordField);
        VBox confirmPwdBox = createOverlayField("Conferma Password", confirmPasswordField);

        // Buttons
        Button confirmBtn = new Button("Conferma nuove credenziali");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);
        confirmBtn.setStyle(
                "-fx-background-color: #2c2c2c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-padding: 14px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        confirmBtn.setOnAction(e -> {
            handleTempCredentialsSubmit(tempUsernameField, tempPasswordField, newPasswordField, confirmPasswordField);
        });

        Button cancelBtn = new Button("Annulla");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #666;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        );
        cancelBtn.setOnAction(e -> hideOverlay());

        overlayCard.getChildren().addAll(
                headerBox,
                usernameBox,
                tempPwdBox,
                newPwdBox,
                confirmPwdBox,
                confirmBtn,
                cancelBtn
        );

        overlayPane.getChildren().add(overlayCard);
    }

    private VBox createOverlayField(String label, Control control) {
        VBox box = new VBox(8);

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: #333;"
        );

        box.getChildren().addAll(lbl, control);
        return box;
    }

    /**
     * Gestisce l'invio delle credenziali temporanee
     */
    private void handleTempCredentialsSubmit(TextField usernameField, PasswordField tempPasswordField,
                                                     PasswordField newPasswordField, PasswordField confirmPasswordField) {
        // Recupera tutti i valori
        String username = usernameField.getText().trim();
        String tempPassword = tempPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validazione
        if (username.isEmpty() || tempPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Compila tutti i campi");
            return ;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Le password non corrispondono");
            return;
        }

        // Se esiste callback, chiama il callback
        if (tempCredentialsCallback != null) {
            try {
                tempCredentialsCallback.onTempCredentials(username, tempPassword, newPassword);

                // Success
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Successo");
                success.setHeaderText("Password Aggiornata");
                success.setContentText("La password è stata aggiornata correttamente. Usa le nuove credenziali per accedere.");
                success.showAndWait();

                hideOverlay();
            } catch (Exception e) {
                showError("Errore durante l'aggiornamento: " + e.getMessage());

            }

            params.add(username);
            params.add(tempPassword);
            params.add(newPassword);


        } else {
            // Se non c'è callback, mostra solo messaggio di successo (per testing)
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Successo");
            success.setHeaderText("Password Aggiornata");
            success.setContentText("Username: " + username + "\nPassword aggiornata correttamente.");
            success.showAndWait();


            params.add(username);
            params.add(tempPassword);
            params.add(newPassword);

            hideOverlay();

        }
    }

    private void hideOverlay() {
        if (overlayPane != null) {
            overlayPane.setVisible(false);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Errore di Login");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setLoginCallback(LoginCallback callback) {
        this.loginCallback = callback;
    }

    public void setTempCredentialsCallback(TempCredentialsCallback callback) {
        this.tempCredentialsCallback = callback;
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    @FunctionalInterface
    public interface LoginCallback {
        void onLogin(String username, String password) throws RemoteException, IllegalAccess;
    }

    @FunctionalInterface
    public interface TempCredentialsCallback {
        void onTempCredentials(String username, String tempPassword, String newPassword) throws Exception;
    }
}