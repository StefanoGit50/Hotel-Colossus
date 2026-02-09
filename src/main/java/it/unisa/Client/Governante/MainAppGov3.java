package it.unisa.Client.Governante;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Client.GUI.components.*;
import it.unisa.Common.*;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.persistent.util.Stato;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainAppGov3 extends Application {

    private static final Logger log = LogManager.getLogger(MainAppGov3.class);
    private FrontDeskClient frontDeskClient =  new FrontDeskClient();
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
    private BookingCreation bookingCreationView;

    // ===== DATI =====
    private String currentUsername;
    private String currentRole;
    public static List<Camera> camere;
    public static List<Servizio> servizio;
    public static List<Trattamento> trattamenti;
    public static List<Cliente> clienti;


    @Override
    public void start(Stage stage) throws IllegalAccess, RemoteException {
        this.primaryStage = stage;

        camere = frontDeskClient.getCamere();
        servizio = frontDeskClient.getServizi();
        trattamenti= frontDeskClient.getTrattamenti();
        clienti = frontDeskClient.getListaClienti();

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
            loginView.setTempCredentialsCallback((username, tempPassword, newPassword) -> {
                handleTempCredentialsValidation(username, tempPassword, newPassword);
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
     *  AUTENTICAZIONE ( chiamata server)
     */
    private boolean authenticateUser(String username, String password, String pwd2) throws RemoteException {
        try{
            imp = frontDeskClient.DoAuthentication(username,password,pwd2);
           // return imp != null;
        } catch (IllegalAccess e) {
            exception=e.getMessage();
        }
        return true;
    }

    private boolean handleTempCredentialsValidation(String username, String tempPassword, String newPassword) throws Exception {
        try {
            System.out.println(" Validazione credenziali temporanee...");
            System.out.println("   Username: " + username);
            System.out.println("   Temp Password: " + tempPassword);
            System.out.println("   New Password: " + newPassword);

           Impiegato imp= frontDeskClient.DoAuthentication(username,newPassword,tempPassword);
           return imp!=null;

        } catch (RemoteException e) {
            System.err.println(" Errore RMI: " + e.getMessage());
            throw new Exception("Errore di connessione al server: " + e.getMessage());
        } catch (IllegalAccess e) {
            System.err.println(" Accesso negato: " + e.getMessage());
            throw new Exception("Accesso negato: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Errore validazione: " + e.getMessage());
            throw e;
        }
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
        int size = BookingFilter.initializeSampleBookings();
        System.out.println("Caricate " + size + " prenotazioni");
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
        log.info(" Logout: " + currentUsername);

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
        log.info(" Navigazione verso: " + destination);

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
            case SideBar.BOOKING:
                showBookingCreation();
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
            dashboardView.setBookings(BookingFilter.getAllBookings());
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
        Label placeholder = new Label("💰 Check-out - In sviluppo");
        contentArea.getChildren().add(placeholder);
    }

    private BookingDetail bookingDetail;

    private void showBookingDetail() {
        contentArea.getChildren().clear();

        BookingDetail bookingDetail = new BookingDetail(
                creaPrenotazioneDiProva(),
                creaCatalogoServizi()
        );

        contentArea.getChildren().add(bookingDetail);
        VBox.setVgrow(bookingDetail, Priority.ALWAYS);
    }
/*
    private void showContoEconomico() {
        contentArea.getChildren().clear();

        if (contoEconomicoView == null) {
            contoEconomicoView = new ContoEconomico();
        }

        contentArea.getChildren().add(contoEconomicoView);
        VBox.setVgrow(contoEconomicoView, Priority.ALWAYS);
    }*/
    private void showBookingCreation(){
        contentArea.getChildren().clear();

        if (bookingCreationView == null) {
            bookingCreationView = new BookingCreation(frontDeskClient);
        }
        contentArea.getChildren().add(bookingCreationView);
        VBox.setVgrow(bookingCreationView, Priority.ALWAYS);
    }


    private List<Servizio> creaCatalogoServizi() {
        return frontDeskClient.getServizi();
    }

    private Prenotazione creaPrenotazioneDiProva() {
        // Camera
        Camera camera101 = new Camera(101, Stato.Occupata, 2, 89.50, "Piano Terra","");

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


        Prenotazione p = new Prenotazione(
                LocalDate.now(),             // Data creazione
                LocalDate.now().plusDays(3),             // Data inizio
                LocalDate.now().plusDays(7),             // Data fine (2 notti)
                null,
                trattamento,
                55.5,
                "Patente",                             // Tipo documento
                LocalDate.of(2021, 2, 13),             // Data rilascio
                LocalDate.of(2028, 2, 13),             // Data scadenza
                alessio.getNome() + " " + alessio.getCognome(), // Intestatario
                "champagne in camera",                 // Note
                serviziPrenotati,
                arrayCliente,
                "SAGD65",
                "in natura",
                "bangladina"
        );

        p.setCheckIn(false); // Check-in non ancora fatto

        return p;

    }

    public static void main(String[] args) {
        System.out.println(" Avvio Hotel Colossus...");
        launch(args);
    }
}