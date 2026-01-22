package it.unisa.Client.GUI.components;

import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Componente TopBar - Barra superiore dell'applicazione.
 * Contiene: Logo, Titolo, User Info e Menu utente.
 *
 *
 * @author Team Hotel Colossus
  */
public class TopBar extends HBox {
    private ImageView logoImage;
    private Label titleLabel;
    private Label userNameLabel;
    private Label userRoleLabel;
    private VBox avatarBox;
    private ContextMenu userMenu;

    // ===== COSTRUTTORE =====
    public TopBar() {
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(15);
        this.setPadding(new Insets(21, 30, 21, 30));
        this.setAlignment(Pos.CENTER);

        // Spacer per spingere user info a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User Box
        HBox userBox = createUserBox();

        // Aggiungi tutto alla TopBar
        this.getChildren().addAll(logoImage, titleLabel, spacer, userBox);
    }

    /**
     * Crea il box con le informazioni utente
     */
    private HBox createUserBox() {
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.getStyleClass().add("user-box");
        userBox.setPadding(new Insets(0, 20, 0, 0));

        userBox.getChildren().addAll(userNameLabel, avatarBox);

        return userBox;
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
            System.err.println(" Impossibile caricare il logo: " + e.getMessage());
        }

        // Title Label
        titleLabel = new Label("HOTEL COLOSSUS");
        titleLabel.getStyleClass().add("logo");

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

        // Actions
        profileItem.setOnAction(e -> onProfileClick());
        logoutItem.setOnAction(e -> onLogoutClick());

        contextMenu.getItems().addAll(profileItem, logoutItem);

        return contextMenu;
    }

    /**
     * Setup degli event handlers
     */
    private void setupEventHandlers() {
        // Click su user box mostra menu
        HBox userBox = (HBox) this.getChildren().get(3); // L'ultimo elemento

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

    // ===== EVENT HANDLERS (da sovrascrivere o collegare) =====

    /**
     * Chiamato quando si clicca su Profilo
     */
    protected void onProfileClick() {
        System.out.println("📋 Profilo cliccato");
        // Qui puoi aggiungere logica custom
    }


    /**
     * Chiamato quando si clicca su Logout
     */
    protected void onLogoutClick() {
        System.out.println("🚪 Logout cliccato");
        // Qui puoi aggiungere logica custom
    }

    //  GETTER/SETTER

    public void setUserName(String name) {
        this.userNameLabel.setText(name);
    }

    public void setUserRole(String role) {
        this.userRoleLabel.setText(role);
    }

    public String getUserName() {
        return userNameLabel.getText();
    }
}

