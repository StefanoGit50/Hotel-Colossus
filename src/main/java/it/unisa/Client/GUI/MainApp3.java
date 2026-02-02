package it.unisa.Client.GUI;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Client.GUI.components.*;
import it.unisa.Common.*;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.persistent.util.Stato;
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

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainApp3 extends Application {

    private FrontDeskClient frontDeskClient;
    private Impiegato imp;
    private String exception;
    // ===== STAGE E SCENE =====
    private Stage primaryStage;
    private Scene loginScene;
    private Scene mainScene;

    // ===== COMPONENTI =====
    private TopBar topBar;
    private SideBar sidebar;
    private VBox contentArea;
    private LoginView loginView;

    // ===== VISTE =====
    private Dashboard dashboardView;
    private GuestManagement guestManagement;
    private Planning planning;
    private RoomManagementView roomManagementView;
    private ContoEconomico contoEconomicoView;

    // ===== DATI =====
    private ObservableList<BookingFilter> allBookings = FXCollections.observableArrayList();
    private String currentUsername;
    private String currentRole;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // CONFIGURAZIONE FINESTRA (una volta sola)
        primaryStage.setTitle("Hotel Colossus");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);

        // PARTE CON IL LOGIN
        showLoginScreen();
    }

    /**
     *  Mostra schermata login
     */
    private void showLoginScreen() {
        String pwd2= null;
        // CREA LoginView SOLO UNA VOLTA
        if (loginView == null) {
            loginView = new LoginView();

            loginView.setLoginCallback((username, password) -> {
                if (authenticateUser(username, password,pwd2)) {
                    System.out.println(" Login riuscito: " + username);
                    currentUsername = username;
                    currentRole = getRoleFromUsername(username);
                    loginView.clearFields();
                    showMainInterface();
                } else {
                    System.out.println(" Login fallito");
                    loginView.clearFields();
                }
            });
        } else {
            loginView.clearFields();
        }

        // CREA LA SCENA SOLO SE NON ESISTE
        if (loginScene == null) {
            loginScene = new Scene(loginView, 1200, 700);
            loginScene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/hotel.css")).toExternalForm()
            );

            loginScene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F11) {
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
                }
            });
        }

        // CAMBIA SCENA
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Hotel Colossus - Login");

        // SOLO AL PRIMO AVVIO
        if (!primaryStage.isShowing()) {
            primaryStage.setMaximized(true);
            primaryStage.show();
        }
    }

    /**
     *  AUTENTICAZIONE (MOCK - sostituire con chiamata server)
     */
    private boolean authenticateUser(String username, String password, String pwd2) throws RemoteException {
        try{
            imp = frontDeskClient.DoAuthentication(username,password,pwd2);
            if(imp==null){
                return false;
            }else return true;
        } catch (IllegalAccess e) {
            exception=e.getMessage();
        }
        return false;
    }

    /**
     *  Determina ruolo da username
     */
    private String getRoleFromUsername(String username) {
        String lowerName = username.toLowerCase();

        if (lowerName.startsWith("reception")) {
            return "Receptionist";
        } else if (lowerName.startsWith("manager")) {
            return "Manager";
        } else if (lowerName.startsWith("governante")) {
            return "Governante";
        } else {
            return "Utente";
        }
    }

    /**
     * STEP 2: Mostra interfaccia principale
     */
    private void showMainInterface() {
        initializeData();
        VBox root = createMainLayout();

        if (mainScene == null) {
            //  USA LE DIMENSIONI DELLA FINESTRA CORRENTE (già massimizzata dal login)
            double width = primaryStage.getWidth() > 0 ? primaryStage.getWidth() : 1400;
            double height = primaryStage.getHeight() > 0 ? primaryStage.getHeight() : 900;

            mainScene = new Scene(root, width, height);
            mainScene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/hotel.css")).toExternalForm()
            );

            mainScene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F11) {
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
                }
                if (e.getCode() == KeyCode.ESCAPE && primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                }
            });
        } else {
            mainScene.setRoot(root);
        }

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Hotel Colossus - " + currentRole);

        System.out.println("Interfaccia caricata per: " + currentUsername + " (" + currentRole + ")");
    }

    /**
     * Inizializza i dati
     */
    private void initializeData() {
        BookingFilter.initializeSampleBookings(allBookings);
        System.out.println("Caricate " + allBookings.size() + " prenotazioni");
    }

    /**
     * Crea il layout principale
     */
    private VBox createMainLayout() {
        VBox root = new VBox(14);
        root.getStyleClass().add("root-container");
        root.setPadding(new Insets(8, 8, 8, 3));

        //  TopBar con username, role e callback logout
        topBar = new TopBar(currentUsername, currentRole, this::handleLogout);

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

        // Content Area
        contentArea = new VBox();
        contentArea.getStyleClass().add("content-area-container");
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Carica dashboard di default
        showDashboard();

        mainArea.getChildren().addAll(sidebar, contentArea);

        return mainArea;
    }

    /**
     *  LOGOUT - Torna al login
     */
    private void handleLogout() {
        System.out.println("👋 Logout: " + currentUsername);

        // Reset
        currentUsername = null;
        currentRole = null;
        dashboardView = null;
        guestManagement = null;
        planning = null;
        roomManagementView = null;
        contoEconomicoView = null;

        //SOLUZIONE: Temporaneamente disabilita maximized
        primaryStage.setMaximized(false);

        // Torna al login
        showLoginScreen();

        // Dopo aver cambiato scena, riabilita maximized
        javafx.application.Platform.runLater(() -> {
            primaryStage.setMaximized(true);
        });
    }

    // ===== NAVIGAZIONE =====

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
            case SideBar.BOOKING_DETAILS:
                showBookingDetail();
                break;
        }
    }

    private void showDashboard() {
        contentArea.getChildren().clear();

        if (dashboardView == null) {
            dashboardView = new Dashboard();
            dashboardView.setBookings(allBookings);
        }

        contentArea.getChildren().add(dashboardView);
        VBox.setVgrow(dashboardView, Priority.ALWAYS);
    }

    private void showGuestManagement() {
        contentArea.getChildren().clear();

        if (guestManagement == null) {
            guestManagement = new GuestManagement();
        }

        contentArea.getChildren().add(guestManagement);
        VBox.setVgrow(guestManagement, Priority.ALWAYS);
    }

    private void showPlanning() {
        contentArea.getChildren().clear();

        if (planning == null) {
            planning = new Planning();
        }

        contentArea.getChildren().add(planning);
        VBox.setVgrow(planning, Priority.ALWAYS);
    }

    private void showRooms() {
        contentArea.getChildren().clear();

        if (roomManagementView == null) {
            roomManagementView = new RoomManagementView();
        }

        contentArea.getChildren().add(roomManagementView);
        VBox.setVgrow(roomManagementView, Priority.ALWAYS);
    }

    private void showCheckout() {
        contentArea.getChildren().clear();
        // TODO: Implementare
        Label placeholder = new Label("💰 Check-out - In sviluppo");
        contentArea.getChildren().add(placeholder);
    }

    private BookingDetail bookingDetail;

    private void showBookingDetail() {
        contentArea.getChildren().clear();

        BookingDetail bookingDetail = new BookingDetail(
                creaPrenotazioneDiProva(),
                creaCatalogo()
        );

        contentArea.getChildren().add(bookingDetail);
        VBox.setVgrow(bookingDetail, Priority.ALWAYS);
    }

    private void showContoEconomico() {
        contentArea.getChildren().clear();

        if (contoEconomicoView == null) {
            contoEconomicoView = new ContoEconomico();
        }

        contentArea.getChildren().add(contoEconomicoView);
        VBox.setVgrow(contoEconomicoView, Priority.ALWAYS);
    }


    private List<Servizio> creaCatalogo() {
        // Catalogo completo servizi disponibili
        return Arrays.asList(
                new Servizio("Accesso Piscina", 80.0),
                new Servizio("Spa & Wellness", 85.0),
                new Servizio("Bottiglia Vino", 45.0),
                new Servizio("Transfer Aeroporto", 50.0),
                new Servizio("Colazione in Camera", 15.0)
        );
    }

    private Prenotazione creaPrenotazioneDiProva() {
        // Camera
        Camera camera101 = new Camera(101, Stato.Occupata, 2, 89.50, "Piano Terra","");
        ArrayList<Camera> camere = new ArrayList<>(List.of(camera101));

        // Clienti
        Cliente alessio = new Cliente(
                "Alessio", "Colardi", "napoli", "caserta",
                "via fas", 234, 234, "3243543", "M",
                LocalDate.of(2001, 1, 30), "23rtygfds2",
                "luca@smdb", "italiana", camera101
        );
        //alessio.setIntestatario(true); //  Intestatario della prenotazione

        ArrayList<Cliente> arrayCliente = new ArrayList<>(List.of(alessio));

        // Servizi prenotati (quello che il cliente ha già ordinato)
        ArrayList<Servizio> serviziPrenotati = new ArrayList<>(List.of(
                new Servizio("Bottiglia Vino", 45.0)
        ));

        // Trattamento
        Trattamento trattamento = new Trattamento("MEZZA PENSIONE", 55.5);

        // Prenotazione
        Prenotazione p = new Prenotazione(
                1234,                                   // ID
                LocalDate.of(2026, 1, 31),             // Data creazione
                LocalDate.of(2026, 2, 11),             // Data inizio
                LocalDate.of(2026, 2, 13),             // Data fine (2 notti)
                trattamento,
                "Patente",                             // Tipo documento
                LocalDate.of(2021, 2, 13),             // Data rilascio
                LocalDate.of(2028, 2, 13),             // Data scadenza
                alessio.getNome() + " " + alessio.getCognome(), // Intestatario
                "champagne in camera",                 // Note
                camere,
                serviziPrenotati,
                arrayCliente,
                "safnsdj08"
        );

        p.setCheckIn(false); // Check-in non ancora fatto

        return p;
    }

    public static void main(String[] args) {
        System.out.println(" Avvio Hotel Colossus...");
        launch(args);
    }
}