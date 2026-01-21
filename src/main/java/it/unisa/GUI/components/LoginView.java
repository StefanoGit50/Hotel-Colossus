package it.unisa.GUI.components;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
     * LoginView - Login elegante con sfondo full-screen e card glass morphism
     */
    public class LoginView extends StackPane {

        // ===== COMPONENTI =====
        private TextField usernameField;
        private PasswordField passwordField;
        private Button loginButton;

        // ===== CALLBACK =====
        private LoginCallback loginCallback;

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
            HBox loginCard = createLoginCard();

            // Aggiungi tutto allo StackPane
            this.getChildren().addAll(background, loginCard);
            this.setAlignment(Pos.CENTER);
        }

        /**
         * Crea lo sfondo full-screen
         */
        private ImageView createBackground() {
            ImageView bgImage = new ImageView();

            try {
                // Prova a caricare l'immagine dal resources
                Image image = new Image(getClass().getResourceAsStream("/hotel-background.png"));
                bgImage.setImage(image);
            } catch (Exception e) {
                System.out.println("⚠️ Immagine non trovata, uso colore di fallback");
                // Fallback: usa un rettangolo colorato
                this.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
            }

            // Imposta dimensioni
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(this.widthProperty());
            bgImage.fitHeightProperty().bind(this.heightProperty());

            // Effetto blur leggero
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

            // ===== LATO SINISTRO - LOGO E INFO (Glass Morphism Rosso) =====
            VBox leftSide = createLeftSide();

            // ===== LATO DESTRO - FORM =====
            VBox rightSide = createFormSide();

            // Aggiungi i lati alla card
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

            // Logo immagine
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

            // Dimensioni logo
            logoView.setFitWidth(220);
            logoView.setFitHeight(220);
            logoView.setPreserveRatio(false);
            logoView.setSmooth(true);

            // Aggiungi ombra per staccare dal fondo
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

            // Intestazione
            Label welcomeLabel = new Label("Benvenuto");
            welcomeLabel.getStyleClass().add("login-welcome-glass");

            Label subtitleLabel = new Label("Accedi al tuo account");
            subtitleLabel.getStyleClass().add("login-subtitle-glass");

            VBox header = new VBox(5, welcomeLabel, subtitleLabel);
            header.setAlignment(Pos.CENTER);

            // Spaziatore
            Region spacer1 = new Region();
            spacer1.setPrefHeight(5);

            // Form groups
            VBox usernameGroup = createFormGroup("Username", usernameField);
            VBox passwordGroup = createFormGroup("Password", passwordField);

            // Bottone login
            loginButton.setMaxWidth(Double.MAX_VALUE);

            // Spaziatore
            Region spacer2 = new Region();
            spacer2.setPrefHeight(3);

            // Link password dimenticata
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
                loginCallback.onLogin(username, password);
            }
        }

        private void handleForgotPassword() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Dimenticata");
            alert.setHeaderText("Recupero Password");
            alert.setContentText("Contatta l'amministratore di sistema per recuperare la password.");
            alert.showAndWait();
        }

        private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore di Login");
            alert.setContentText(message);
            alert.showAndWait();
        }

        public void showSuccess(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText("Login Effettuato");
            alert.setContentText(message);
            alert.showAndWait();
        }

        public void setLoginCallback(LoginCallback callback) {
            this.loginCallback = callback;
        }

        public void clearFields() {
            usernameField.clear();
            passwordField.clear();
        }

        @FunctionalInterface
        public interface LoginCallback {
            void onLogin(String username, String password);
        }
    }


