package it.unisa.Client.GUI.components;

import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Componente TopBar - Barra superiore dell'applicazione.
 */
public class TopBar extends HBox {
    private ImageView logoImage;
    private Label titleLabel;
    private Label userNameLabel;
    private Label userRoleLabel;
    private VBox avatarBox;
    private ContextMenu userMenu;

    private Runnable logoutCallback;

    // ===== COSTRUTTORI =====

    public TopBar() {
        this("Receptionist1", "Front Desk", null);
    }

    public TopBar(String username, String role, Runnable logoutCallback) {
        this.logoutCallback = logoutCallback;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();

        setUserName(username);
        setUserRole(role);
        updateAvatar(username);
    }

    /**
     * Inizializza tutti i componenti UI
     */
    private void initializeComponents() {
        // Logo Image
        logoImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/logo3.png"));
            logoImage.setImage(image);
            logoImage.setFitHeight(120);
            logoImage.setFitWidth(120);
            logoImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("⚠️ Impossibile caricare il logo: " + e.getMessage());
        }

        // ✅ Title Label - GRANDE E CENTRATO
        titleLabel = new Label("HOTEL COLOSSUS");
        titleLabel.getStyleClass().add("logo");
        titleLabel.setStyle(
                "-fx-font-size: 42px; " +           // ✅ Più grande
                        "-fx-font-weight: bolder; " +
                        "-fx-text-fill: #660f04; " +        // ✅ Colore richiesto
                        "-fx-letter-spacing: 4px;"
        );

        // User Info
        userNameLabel = new Label("Receptionist1");
        userNameLabel.getStyleClass().add("user-name");

        userRoleLabel = new Label("Front Desk");
        userRoleLabel.getStyleClass().add("user-role");

        // Avatar
        avatarBox = new VBox();
        avatarBox.setAlignment(Pos.CENTER);
        avatarBox.setPrefSize(40, 40);
        avatarBox.setMaxSize(40, 40);
        avatarBox.setMinSize(40, 40);
        avatarBox.getStyleClass().add("avatar");

        Label avatarText = new Label("R1");
        avatarText.getStyleClass().add("avatar-text");
        avatarBox.getChildren().add(avatarText);

        // Context Menu
        userMenu = createUserMenu();
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(15);
        this.setPadding(new Insets(21, 30, 21, 30));
        this.setAlignment(Pos.CENTER);

        // ✅ Spacer sinistro per centrare il titolo
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        // ✅ Spacer destro per centrare il titolo
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // User Box
        HBox userBox = createUserBox();

        // ✅ NUOVO ORDINE: logo - spacer - TITOLO CENTRATO - spacer - userBox
        this.getChildren().addAll(logoImage, leftSpacer, titleLabel, rightSpacer, userBox);
    }

    /**
     * Crea il box con le informazioni utente
     */
    private HBox createUserBox() {
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.getStyleClass().add("user-box");
        userBox.setPadding(new Insets(0, 20, 0, 0));

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        userInfo.getChildren().addAll(userNameLabel, userRoleLabel);

        userBox.getChildren().addAll(userInfo, avatarBox);

        return userBox;
    }

    /**
     * Setup dello styling CSS
     */
    private void setupStyling() {
        this.getStyleClass().add("top-bar");
    }

    /**
     * Crea il context menu dell'utente
     */
    private ContextMenu createUserMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem profileItem = new MenuItem("👤 Profilo");
        MenuItem logoutItem = new MenuItem("🚪 Logout");

        profileItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");
        logoutItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");

        profileItem.setOnAction(e -> onProfileClick());
        logoutItem.setOnAction(e -> onLogoutClick());

        contextMenu.getItems().addAll(profileItem, logoutItem);

        return contextMenu;
    }

    /**
     * Setup degli event handlers
     */
    private void setupEventHandlers() {
        // ✅ Aggiornato indice - userBox è ora il 5° elemento (indice 4)
        HBox userBox = (HBox) this.getChildren().get(4);

        userBox.setOnMouseClicked(e -> {
            if (!userMenu.isShowing()) {
                double x = avatarBox.localToScreen(avatarBox.getBoundsInLocal()).getMinX();
                double y = avatarBox.localToScreen(avatarBox.getBoundsInLocal()).getMaxY();
                userMenu.show(avatarBox, x, y);
            } else {
                userMenu.hide();
            }
        });
    }

    // ===== EVENT HANDLERS =====

    protected void onProfileClick() {
        System.out.println("📋 Profilo cliccato");
    }

    protected void onLogoutClick() {
        System.out.println("🚪 Logout cliccato");

        if (logoutCallback != null) {
            logoutCallback.run();
        } else {
            System.out.println("⚠️ Nessun callback di logout impostato");
        }
    }

    // ===== GETTER/SETTER =====

    public void setUserName(String name) {
        this.userNameLabel.setText(name);
    }

    public void setUserRole(String role) {
        this.userRoleLabel.setText(role);
    }

    public String getUserName() {
        return userNameLabel.getText();
    }

    private void updateAvatar(String username) {
        if (username == null || username.isEmpty()) {
            username = "?";
        }

        if (avatarBox.getChildren().size() > 0) {
            Label avatarText = (Label) avatarBox.getChildren().get(0);

            String initials = username.length() >= 2
                    ? username.substring(0, 2).toUpperCase()
                    : username.toUpperCase();

            avatarText.setText(initials);
        }
    }

    public void setLogoutCallback(Runnable callback) {
        this.logoutCallback = callback;
    }
}