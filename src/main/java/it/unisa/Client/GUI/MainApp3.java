package it.unisa.Client.GUI;


import it.unisa.Client.GUI.components.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * MainApp - Applicazione principale Hotel Colossus.
 *
 * @author Team Hotel Colossus
 * @version 1.0
 */
public class MainApp3 extends Application {

    // ===== COMPONENTI PRINCIPALI =====
    private TopBar topBar;
    private SideBar sidebar;
    private VBox contentArea;
    private RoomManagementView roomManagementView;
    // ===== VISTE =====
    private Dashboard dashboardView;
    private GuestManagement guestManagement;
    private Planning planning;
    private ContoEconomico contoEconomicoView;

    // ===== DATI =====
    private ObservableList<BookingFilter> allBookings = FXCollections.observableArrayList();

    /**
     * Punto di ingresso dell'applicazione
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Colossus - Management System");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setFullScreen(false);
        primaryStage.setMaximized(false);
        primaryStage.setResizable(true);           // Permetti ridimensionamento
        primaryStage.setMaximized(false);          // Non partire massimizzata
        primaryStage.setMinWidth(1200);            // Larghezza minima
        primaryStage.setMinHeight(700);            // Altezza minima

        // ✅ GESTIONE CHIUSURA
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("👋 Chiusura applicazione...");
            // Qui puoi aggiungere conferma o salvataggio dati
        });

        // Inizializza i dati
        initializeData();

        // Crea il layout principale
        VBox root = createMainLayout();

        // Crea la scena
        Scene scene = new Scene(root, 1200, 700);


        // SHORTCUT DA TASTIERA
        scene.setOnKeyPressed(e -> {

            if ((e.isAltDown() || e.isMetaDown()) && e.getCode() == KeyCode.F4) {
                System.out.println("⌨️ Chiusura tramite shortcut (altf4)");
                primaryStage.close();
            }

            // ESC per uscire da fullscreen
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                }
            }

            // F11 per toggle fullscreen
            if (e.getCode() == javafx.scene.input.KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
                System.out.println("🖥️ Fullscreen: " + primaryStage.isFullScreen());
            }
        });
        // Carica il CSS
        scene.getStylesheets().add(
                getClass().getResource("/hotel.css").toExternalForm()
        );

        primaryStage.setScene(scene);

        primaryStage.show();
        primaryStage.centerOnScreen();  // ← Questo fa la differenza!
        primaryStage.toFront();
        primaryStage.requestFocus();
        System.out.println("✅ Hotel Colossus avviato con successo!");
    }

    /**
     * Inizializza i dati dell'applicazione
     */
    private void initializeData() {
        // Carica le prenotazioni di esempio
        BookingFilter.initializeSampleBookings(allBookings);
        System.out.println("📊 Caricate " + allBookings.size() + " prenotazioni");
    }

    /**
     * Crea il layout principale dell'applicazione
     */
    private VBox createMainLayout() {
        VBox root = new VBox(14);
        root.getStyleClass().add("root-container");
        root.setPadding(new Insets(8,8,8,3));

        // Top Bar
        topBar = new TopBar();

        // Main Area (Sidebar + Content)
        HBox mainArea = createMainArea();
        VBox.setVgrow(mainArea, Priority.ALWAYS);

        root.getChildren().addAll(topBar, mainArea);

        return root;
    }

    /**
     * Crea l'area principale (sidebar + contenuto)
     */
    private HBox createMainArea() {
        HBox mainArea = new HBox(8);

        // Sidebar
        sidebar = new SideBar();
        sidebar.setOnNavigationChange(this::handleNavigation);

        // Content Area (container dinamico)
        contentArea = new VBox();
        contentArea.getStyleClass().add("content-area-container");
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Carica la dashboard di default
        showDashboard();

        mainArea.getChildren().addAll(sidebar, contentArea);

        return mainArea;
    }

    // ===== GESTIONE NAVIGAZIONE =====

    /**
     * Gestisce il cambio di navigazione
     *
     * @param destination nome della destinazione
     */
    private void handleNavigation(String destination) {
        System.out.println("🧭 Navigazione verso: " + destination);

        switch (destination) {
            case SideBar.DASHBOARD:
                showDashboard();
                break;
            case SideBar.GUEST_MANAGEMENT:
                showGuestManagement();
                break;
            case SideBar.PLANNING:
                showPlanning();
                break;
            case SideBar.ROOMS:
                showRooms();
                break;
            case SideBar.CHECKOUT:
                showCheckout();
                break;
            case SideBar.CONTO_ECONOMICO:
                showContoEconomico();
                break;
            default:
                System.err.println("⚠️ Destinazione sconosciuta: " + destination);
        }
    }

    /**
     * Mostra la vista Dashboard
     */
    private void showDashboard() {
        contentArea.getChildren().clear();

        // Crea o riusa la dashboard view
        if (dashboardView == null) {
            dashboardView = new Dashboard();
            dashboardView.setBookings(allBookings);
        }

        contentArea.getChildren().add(dashboardView);
        VBox.setVgrow(dashboardView, Priority.ALWAYS);

        System.out.println("📊 Dashboard caricata");
    }

    /**
     * Mostra la vista Guest Management
     */
    private void showGuestManagement() {
        contentArea.getChildren().clear();

        if (guestManagement == null) {
            guestManagement = new GuestManagement();
        }

        VBox.setVgrow(guestManagement, Priority.ALWAYS);
        contentArea.getChildren().add(guestManagement);
        System.out.println("👥 Guest Management caricata");
    }

    /**
     * Mostra la vista Planning
     */
    private void showPlanning() {
        contentArea.getChildren().clear();

        if (planning == null) {
            planning = new Planning();
        }

        contentArea.getChildren().add(planning);
        VBox.setVgrow(planning, Priority.ALWAYS);

        System.out.println("📅 Planning caricata");    }

    /**
     * Mostra la vista Camere
     */
    private void showRooms() {
        contentArea.getChildren().clear();
        contentArea.getChildren().clear();

        if (roomManagementView == null) {
            roomManagementView = new RoomManagementView();
        }

        contentArea.getChildren().add(roomManagementView);
        VBox.setVgrow(roomManagementView, Priority.ALWAYS);

        System.out.println("🛏️ Room Management caricata");
    }

    /**
     * Mostra la vista Check-out
     */
    private void showCheckout() {
        contentArea.getChildren().clear();

        // TODO: Implementare CheckoutView
        VBox placeholder = createPlaceholder(
                "💰 Check-out",
                "Vista per i check-out"
        );

        contentArea.getChildren().add(placeholder);

        System.out.println("💰 Check-out - TODO");
    }

    /**
     * Mostra la vista ContoEconomico
     */
    private void showContoEconomico() {
        contentArea.getChildren().clear();
        if (contoEconomicoView == null) {
            contoEconomicoView = new ContoEconomico();
        }
        contentArea.getChildren().add(contoEconomicoView);
        VBox.setVgrow(contoEconomicoView, Priority.ALWAYS);
    }


    // ===== UTILITY =====

    /**
     * Crea un placeholder per viste non ancora implementate
     */
    private VBox createPlaceholder(String title, String description) {
        VBox placeholder = new VBox(20);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPadding(new Insets(50));
        placeholder.getStyleClass().add("content-area");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #6d1331;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");

        Label todoLabel = new Label("🚧 In fase di sviluppo");
        todoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #999999; -fx-padding: 20 0 0 0;");

        placeholder.getChildren().addAll(titleLabel, descLabel, todoLabel);

        return placeholder;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        System.out.println("🚀 Avvio Hotel Colossus...");
        launch(args);
    }
}