package it.unisa.GUI;// HotelColossusApp.java
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.logging.Logger;


public class MainApp extends Application {

        private VBox selectedNavItem = null;

        // ===== BOOKING DATA =====
        private ObservableList<BookingFilter> allBookings = FXCollections.observableArrayList();
        private ObservableList<BookingFilter> filteredBookings = FXCollections.observableArrayList();
        private VBox bookingsContainer;

        // ===== FILTRI =====
        private TextField nameFilter;
        private DatePicker startDateFilter;
        private DatePicker endDateFilter;
        private Button sortAscBtn;
        private Button sortDescBtn;
        private boolean isAscending = true;



        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Hotel Colossus - Management System");

            BookingFilter.initializeSampleBookings(allBookings);
            filteredBookings.addAll(allBookings);

            // Root layout
            VBox root = new VBox(20);
            root.getStyleClass().add("root-container");
            root.setPadding(new Insets(30));

            // Top Bar
            HBox topBar = createTopBar();
            root.getChildren().add(topBar);

            // Main Content Area (Sidebar + Content)
            HBox mainArea = createMainArea();
            root.getChildren().add(mainArea);

            // Scene
            Scene scene = new Scene(root, 1400, 900);

            // Load CSS
            scene.getStylesheets().add(
                    getClass().getResource("/hotel.css").toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.show();
        }



        private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(21,30,21,30));
        topBar.setAlignment(Pos.CENTER);
        topBar.getStyleClass().add("top-bar");

        // Logo Image PICCOLO a SINISTRA
        ImageView logoImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/logo3.png"));
            logoImage.setImage(image);
            logoImage.setFitHeight(120);  // ✅ Ridotto da 180 a 60
            logoImage.setFitWidth(120);   // ✅ Ridotto da 180 a 60
            logoImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Impossibile caricare il logo: " + e.getMessage());
        }

        // Logo Text GRANDE al CENTRO
        Label logo = new Label("HOTEL COLOSSUS");
        logo.getStyleClass().add("logo");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Spacer PRIMA della scritta (centra la scritta)
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);


        // Spacer DOPO la scritta (mantiene centrata)
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        // User info
        HBox userBox = createUserBox();

        // ✅ LAYOUT: Logo piccolo | Spacer | SCRITTA GRANDE | Spacer | User
        topBar.getChildren().addAll(logoImage,  logo, spacer, userBox);

        return topBar;
    }

        private HBox createUserBox() {
            HBox userBox = new HBox(10);
            userBox.setAlignment(Pos.CENTER_RIGHT);
            userBox.getStyleClass().add("user-box");
            userBox.setPadding(new Insets(0, 20, 0, 0));
            Label userName = new Label("Receptionist1");
            userName.getStyleClass().add("user-name");

            Label userRole = new Label("Front Desk");
            userRole.getStyleClass().add("user-role");

            // Avatar
            VBox avatar = new VBox();
            avatar.setAlignment(Pos.CENTER);
            avatar.setPrefSize(40, 40);
            avatar.setMaxSize(40, 40);
            avatar.setMinSize(40, 40);
            avatar.getStyleClass().add("avatar");

            Label avatarText = new Label("R1");
            avatarText.getStyleClass().add("avatar-text");
            avatar.getChildren().add(avatarText);

            userBox.getChildren().addAll(userName, avatar);

            ContextMenu contextMenu = new ContextMenu();

            MenuItem profileItem = new MenuItem("👤 Profilo");
            MenuItem logoutItem = new MenuItem("🚪 Logout");

            // Styling per i menu items
            profileItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");
            logoutItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");

            // Actions
            profileItem.setOnAction(e -> System.out.println("Profilo cliccato"));
            logoutItem.setOnAction(e -> System.out.println("Logout cliccato"));

            contextMenu.getItems().addAll(profileItem, logoutItem);

            // Mostra il menu quando clicchi sullo userBox
            userBox.setOnMouseClicked(e -> {
                if (!contextMenu.isShowing()) {
                    // Calcola la posizione esatta sotto l'avatar
                    double x = userBox.localToScreen(userBox.getBoundsInLocal()).getMinX();
                    double y = userBox.localToScreen(userBox.getBoundsInLocal()).getMaxY();

                    contextMenu.show(userBox, x, y);
                } else {
                    contextMenu.hide();
                }
            });


            return userBox;
        }

        private HBox createMainArea() {
            HBox mainArea = new HBox(20);

            // Sidebar
            VBox sidebar = createSidebar();

            // Content Area
            VBox contentArea = createContentArea();
            HBox.setHgrow(contentArea, Priority.ALWAYS);

            mainArea.getChildren().addAll(sidebar, contentArea);
            return mainArea;
        }

        private VBox createSidebar() {
            VBox sidebar = new VBox(5);
            sidebar.setPrefWidth(250);
            sidebar.setPadding(new Insets(20));
            sidebar.getStyleClass().add("sidebar");

            // Menu items
            VBox dashboardItem = createNavItem("📊", "Dashboard", true);
            VBox guestItem = createNavItem("👥", "Guest Management", true);
            VBox planningItem = createNavItem("📅", "Planning", false);
            VBox checkoutItem = createNavItem("💰", "Check-out", false);

            sidebar.getChildren().addAll(
                    dashboardItem,
                    guestItem,
                    planningItem,
                    checkoutItem
            );

            return sidebar;
        }

        private VBox createNavItem(String icon, String text, boolean active) {
            VBox navItem = new VBox();
            navItem.setAlignment(Pos.CENTER_LEFT);
            navItem.setPadding(new Insets(12, 15, 12, 15));
            navItem.getStyleClass().add("nav-item");

            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);

            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("nav-icon");

            Label textLabel = new Label(text);
            textLabel.getStyleClass().add("nav-text");

            content.getChildren().addAll(iconLabel, textLabel);
            navItem.getChildren().add(content);

            if (active) {
                navItem.getStyleClass().add("nav-item-active");
                selectedNavItem = navItem;
            }

            // Click effect
            navItem.setOnMouseClicked(e -> {
                if (selectedNavItem != null) {
                    selectedNavItem.getStyleClass().remove("nav-item-active");
                }

                navItem.getStyleClass().add("nav-item-active");
                selectedNavItem = navItem;
            });

            return navItem;
        }

        private VBox createContentArea() {
            VBox contentArea = new VBox(20);
            contentArea.setPadding(new Insets(30));
            contentArea.getStyleClass().add("content-area");

            // Stats Grid
            GridPane statsGrid = createStatsGrid();

            // Section Title con decorazioni - PIÙ VICINO e CENTRATO
            HBox sectionTitleBox = new HBox(123); // Container per titolo + decorazioni
            sectionTitleBox.setAlignment(Pos.CENTER); // ✅ CENTRA TUTTO
            VBox.setMargin(sectionTitleBox, new Insets(10, 0, 3, 0));

            // Decorazione sinistra
            Label leftDecor = new Label("◈───");
            leftDecor.getStyleClass().add("section-decoration");

            // Section Title
            Label sectionTitle = new Label("Prossime Prenotazioni");
            sectionTitle.getStyleClass().add("section-title");

            Label rightDecor = new Label("───◈");
            rightDecor.getStyleClass().add("section-decoration");

            sectionTitleBox.getChildren().addAll(leftDecor, sectionTitle, rightDecor);

            VBox filtersSection = createFiltersSection();
            VBox.setMargin(filtersSection, new Insets(0, 0, 2, 0));

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("bookings-scroll");


            bookingsContainer = new VBox(15);
            bookingsContainer.getStyleClass().add("bookings-container");
            bookingsContainer.setPadding(new Insets(10, 0, 10, 0));

            updateBookingsList();

            scrollPane.setContent(bookingsContainer);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            contentArea.getChildren().addAll(statsGrid, sectionTitleBox, filtersSection,scrollPane);
            return contentArea;
        }


    private VBox createFiltersSection() {
        VBox filtersSection = new VBox(3);
        filtersSection.getStyleClass().add("filters-section");
        filtersSection.setPadding(new Insets(12, 25, 2, 25));

        Label filtersTitle = new Label("🔍 Filtri di Ricerca");
        filtersTitle.getStyleClass().add("filters-title");

        GridPane filtersGrid = new GridPane();
        filtersGrid.setHgap(15);
        filtersGrid.setVgap(10);

        // Filtro Nome
        Label nameLabel = new Label("Nome Cliente");
        nameLabel.getStyleClass().add("filter-label");

        nameFilter = new TextField();
        nameFilter.setPromptText("Cerca per nome...");
        nameFilter.getStyleClass().add("filter-input");
        nameFilter.textProperty().addListener((obs, old, newVal) -> applyFilters());

        // Filtro Data Inizio
        Label startDateLabel = new Label("Check-in Da");
        startDateLabel.getStyleClass().add("filter-label");

        startDateFilter = new DatePicker();
        startDateFilter.setPromptText("Seleziona data");
        startDateFilter.getStyleClass().add("filter-input");
        startDateFilter.valueProperty().addListener((obs, old, newVal) -> applyFilters());

        // Filtro Data Fine
        Label endDateLabel = new Label("Check-in A");
        endDateLabel.getStyleClass().add("filter-label");

        endDateFilter = new DatePicker();
        endDateFilter.setPromptText("Seleziona data");
        endDateFilter.getStyleClass().add("filter-input");
        endDateFilter.valueProperty().addListener((obs, old, newVal) -> applyFilters());

        // Ordinamento
        Label sortLabel = new Label("Ordina per Data");
        sortLabel.getStyleClass().add("filter-label");

        HBox sortButtons = new HBox(10);

        sortAscBtn = new Button("↑ Crescente");
        sortAscBtn.getStyleClass().addAll("sort-btn", "active");
        sortAscBtn.setOnAction(e -> {
            isAscending = true;
            sortAscBtn.getStyleClass().add("active");
            sortDescBtn.getStyleClass().remove("active");
            applyFilters();
        });

        sortDescBtn = new Button("↓ Decrescente");
        sortDescBtn.getStyleClass().add("sort-btn");
        sortDescBtn.setOnAction(e -> {
            isAscending = false;
            sortDescBtn.getStyleClass().add("active");
            sortAscBtn.getStyleClass().remove("active");
            applyFilters();
        });

        sortButtons.getChildren().addAll(sortAscBtn, sortDescBtn);

        // Bottone Reset
        Button resetBtn = new Button("↺ Reset");
        resetBtn.getStyleClass().add("reset-btn");
        resetBtn.setOnAction(e -> resetFilters());

        // Aggiungi al grid
        filtersGrid.add(nameLabel, 0, 0);
        filtersGrid.add(nameFilter, 0, 1);
        filtersGrid.add(startDateLabel, 1, 0);
        filtersGrid.add(startDateFilter, 1, 1);
        filtersGrid.add(endDateLabel, 2, 0);
        filtersGrid.add(endDateFilter, 2, 1);
        filtersGrid.add(sortLabel, 3, 0);
        filtersGrid.add(sortButtons, 3, 1);
        filtersGrid.add(resetBtn, 4, 1);

        // Imposta larghezze colonne
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(18);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(18);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(24);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(15);

        filtersGrid.getColumnConstraints().addAll(col1, col2, col3, col4, col5);

        filtersSection.getChildren().addAll(filtersTitle, filtersGrid);
        return filtersSection;
    }

    // ✅ APPLICA FILTRI
    private void applyFilters() {
        filteredBookings.clear();

        String nameQuery = nameFilter.getText().toLowerCase().trim();
        LocalDate startDate = startDateFilter.getValue();
        LocalDate endDate = endDateFilter.getValue();

        for (BookingFilter booking : allBookings) {
            boolean matchesName = nameQuery.isEmpty() ||
                    booking.guestName.toLowerCase().contains(nameQuery);

            boolean matchesStartDate = startDate == null ||
                    !booking.checkIn.isBefore(startDate);

            boolean matchesEndDate = endDate == null ||
                    !booking.checkIn.isAfter(endDate);

            if (matchesName && matchesStartDate && matchesEndDate) {
                filteredBookings.add(booking);
            }
        }

        // Ordina
        if (isAscending) {
            filteredBookings.sort(Comparator.comparing(b -> b.checkIn));
        } else {
            filteredBookings.sort(Comparator.comparing((BookingFilter b) -> b.checkIn).reversed());
        }

        updateBookingsList();
    }

    // ✅ RESET FILTRI
    private void resetFilters() {
        nameFilter.clear();
        startDateFilter.setValue(null);
        endDateFilter.setValue(null);
        isAscending = true;
        sortAscBtn.getStyleClass().add("active");
        sortDescBtn.getStyleClass().remove("active");
        applyFilters();
    }

    // ✅ AGGIORNA LISTA PRENOTAZIONI
    private void updateBookingsList() {
        bookingsContainer.getChildren().clear();

        if (filteredBookings.isEmpty()) {
            Label noResults = new Label("Nessuna prenotazione trovata");
            noResults.getStyleClass().add("no-results");
            bookingsContainer.getChildren().add(noResults);
            return;
        }

        for (BookingFilter booking : filteredBookings) {
            HBox bookingCard = createBookingCard(booking);
            bookingsContainer.getChildren().add(bookingCard);
        }
    }

    // ✅ CREA CARD PRENOTAZIONE
    public HBox createBookingCard(BookingFilter booking) {
        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("booking-card");
        card.setPadding(new Insets(12, 18, 12, 18));

        VBox infoBox = new VBox(6);

        Label nameLabel = new Label(booking.guestName);
        nameLabel.getStyleClass().add("booking-name");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dates = booking.checkIn.format(formatter) + " - " + booking.checkOut.format(formatter);
        String details = dates + " • Camera " + booking.room + " • " + booking.mealPlan;

        Label detailsLabel = new Label(details);
        detailsLabel.getStyleClass().add("booking-details");

        infoBox.getChildren().addAll(nameLabel, detailsLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("→");
        arrow.getStyleClass().add("booking-arrow");

        card.getChildren().addAll(infoBox, spacer, arrow);

        card.setOnMouseClicked(e -> System.out.println("Prenotazione selezionata: " + booking.guestName));

        return card;
    }


        private GridPane createStatsGrid() {
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.getStyleClass().add("stats-grid");

            // Stat cards
            VBox stat1 = createStatCard("45", "Camere Occupate");
            VBox stat2 = createStatCard("12", "Check-in Oggi");
            VBox stat3 = createStatCard("8", "Check-out Oggi");
            VBox stat4 = createStatCard("15", "Camere Libere");

            GridPane.setMargin(stat1, new Insets(0, 0, 15, 0));
            GridPane.setMargin(stat2, new Insets(0, 0, 15, 0));
            GridPane.setMargin(stat3, new Insets(0, 0, 15, 0));
            GridPane.setMargin(stat4, new Insets(0, 0, 15, 0));

            grid.add(stat1, 0, 0);
            grid.add(stat2, 1, 0);
            grid.add(stat3, 2, 0);
            grid.add(stat4, 3, 0);

            // Make columns grow equally
            for (int i = 0; i < 4; i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setPercentWidth(25);
                grid.getColumnConstraints().add(col);
            }

            return grid;
        }

        private VBox createStatCard(String number, String label) {
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(20));
            card.setPrefHeight(120);
            card.getStyleClass().add("stat-card");
            VBox.setMargin(card,new Insets(0,0,30,0) );

            Label numberLabel = new Label(number);
            numberLabel.getStyleClass().add("stat-number");

            Label labelText = new Label(label);
            labelText.getStyleClass().add("stat-label");
            labelText.setWrapText(true);
            labelText.setAlignment(Pos.CENTER);

            card.getChildren().addAll(numberLabel, labelText);

            return card;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }




/*
 * ISTRUZIONI PER L'USO:
 *
 * 1. Salva questo file come: HotelColossusApp.java
 *
 * 2. Compila con:
 *    javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls HotelColossusApp.java
 *
 * 3. Esegui con:
 *    java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls HotelColossusApp
 *
 * NOTA: Sostituisci /path/to/javafx-sdk con il percorso della tua installazione JavaFX
 *
 * CARATTERISTICHE IMPLEMENTATE:
 * - Top bar con logo e avatar utente
 * - Sidebar con navigazione interattiva (click per cambiare selezione)
 * - 4 stat cards con effetto hover
 * - Colori e styling fedeli al concept HTML
 * - Effetti di hover e transizioni
 * - Layout responsive con GridPane e HBox
 *
 * Per usare i colori burgundy del logo, sostituisci:
 * BURGUNDY_PRIMARY = "#6d1331"
 * BURGUNDY_GRADIENT_END = "#8b1538"
 */