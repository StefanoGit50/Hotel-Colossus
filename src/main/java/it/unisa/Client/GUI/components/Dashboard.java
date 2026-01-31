package it.unisa.Client.GUI.components;

import it.unisa.Client.GUI.BookingFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * DashboardView - Vista principale della dashboard.
 */
public class Dashboard extends VBox {

    // ===== COMPONENTI UI =====
    private GridPane statsGrid;
    private VBox filtersSection;
    private VBox bookingsContainer;
    private ScrollPane bookingsScrollPane;

    // ===== STAT CARDS =====
    private StatCard occupiedRoomsCard;
    private StatCard checkinsCard;
    private StatCard checkoutsCard;
    private StatCard availableRoomsCard;

    // ===== FILTRI =====
    private TextField nameFilter;
    private DatePicker startDateFilter;
    private DatePicker endDateFilter;
    private Button sortAscBtn;
    private Button sortDescBtn;
    private Button resetBtn;
    private boolean isAscending = true;

    // ===== DATI =====
    private ObservableList<BookingFilter> allBookings = FXCollections.observableArrayList();
    private ObservableList<BookingFilter> filteredBookings = FXCollections.observableArrayList();

    // ===== COSTRUTTORE =====
    public Dashboard() {
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Inizializza tutti i componenti
     */
    private void initializeComponents() {
        // Stats Grid
        statsGrid = createStatsGrid();

        // Section Title
        HBox sectionTitleBox = createSectionTitle();

        // Filters Section
        filtersSection = createFiltersSection();

        // Bookings ScrollPane
        bookingsScrollPane = createBookingsScrollPane();
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        // ✅ Ridotto spacing generale da 20 a 12
        this.setSpacing(12);
        // ✅ Ridotto padding da 30 a 20
        this.setPadding(new Insets(20));

        HBox sectionTitleBox = createSectionTitle();
        // ✅ Ridotti i margini
        VBox.setMargin(sectionTitleBox, new Insets(7, 0, 2, 0));  // ← Aumentato margine sopra da 5 a 15
        VBox.setMargin(filtersSection, new Insets(-4, 0, 5, 0));

        this.getChildren().addAll(
                statsGrid,
                sectionTitleBox,
                filtersSection,
                bookingsScrollPane
        );

        VBox.setVgrow(bookingsScrollPane, Priority.ALWAYS);
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("content-area");
    }

    // ===== CREAZIONE COMPONENTI =====

    /**
     * Crea la griglia delle statistiche
     */
    private GridPane createStatsGrid() {
        GridPane grid = new GridPane();
        // ✅ Ridotto gap da 15 a 10
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("stats-grid");

        // Crea le stat cards
        occupiedRoomsCard = new StatCard(45, "Camere Occupate");
        checkinsCard = new StatCard(12, "Check-in Oggi");
        checkoutsCard = new StatCard(8, "Check-out Oggi");
        availableRoomsCard = new StatCard(15, "Camere Libere");

        // ✅ Ridotto margine da 15 a 8
        GridPane.setMargin(occupiedRoomsCard, new Insets(0, 0, 10, 0));
        GridPane.setMargin(checkinsCard, new Insets(0, 0, 10, 0));
        GridPane.setMargin(checkoutsCard, new Insets(0, 0, 10, 0));
        GridPane.setMargin(availableRoomsCard, new Insets(0, 0, 10, 0));

        // Aggiungi alla grid
        grid.add(occupiedRoomsCard, 0, 0);
        grid.add(checkinsCard, 1, 0);
        grid.add(checkoutsCard, 2, 0);
        grid.add(availableRoomsCard, 3, 0);

        // Imposta larghezze colonne uguali
        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }

        return grid;
    }

    /**
     * Crea il titolo della sezione con decorazioni
     */
    private HBox createSectionTitle() {
        HBox sectionTitleBox = new HBox(16);
        sectionTitleBox.setAlignment(Pos.CENTER);

        Label leftDecor = new Label("◈───           ");
        leftDecor.getStyleClass().add("section-decoration");

        Label sectionTitle = new Label("Prossime Prenotazioni");
        sectionTitle.getStyleClass().add("section-title");

        Label rightDecor = new Label("             ───◈");
        rightDecor.getStyleClass().add("section-decoration");

        sectionTitleBox.getChildren().addAll(leftDecor, sectionTitle, rightDecor);

        return sectionTitleBox;
    }

    /**
     * Crea la sezione filtri
     */
    private VBox createFiltersSection() {
        VBox section = new VBox(3);
        section.getStyleClass().add("filters-section");
        section.setPadding(new Insets(2, 20, 2, 20));

        // Titolo filtri
        Label filtersTitle = new Label("🔍 Filtri di Ricerca");
        filtersTitle.getStyleClass().add("filters-title");

        // Grid dei filtri
        GridPane filtersGrid = new GridPane();

        filtersGrid.setHgap(12);
        filtersGrid.setVgap(8);

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
        resetBtn = new Button("↺ Reset");
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

        section.getChildren().addAll(filtersTitle, filtersGrid);

        return section;
    }

    /**
     * Crea lo scroll pane per le prenotazioni
     */
    private ScrollPane createBookingsScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("bookings-scroll");

        bookingsContainer = new VBox(15);
        bookingsContainer.getStyleClass().add("bookings-container");
        // ✅ Ridotto padding da 10,0,10,0 a 5,0,5,0
        bookingsContainer.setPadding(new Insets(5, 0, 5, 0));

        scrollPane.setContent(bookingsContainer);

        return scrollPane;
    }

    // ===== LOGICA FILTRI =====

    private void applyFilters() {
        filteredBookings.clear();

        String nameQuery = nameFilter.getText().toLowerCase().trim();
        LocalDate startDate = startDateFilter.getValue();
        LocalDate endDate = endDateFilter.getValue();

        for (BookingFilter booking : allBookings) {
            boolean matchesName = nameQuery.isEmpty() ||
                    booking.getGuestName().toLowerCase().contains(nameQuery);

            boolean matchesStartDate = startDate == null ||
                    !booking.getCheckIn().isBefore(startDate);

            boolean matchesEndDate = endDate == null ||
                    !booking.getCheckIn().isAfter(endDate);

            if (matchesName && matchesStartDate && matchesEndDate) {
                filteredBookings.add(booking);
            }
        }

        if (isAscending) {
            filteredBookings.sort(Comparator.comparing(BookingFilter::getCheckIn));
        } else {
            filteredBookings.sort(Comparator.comparing(BookingFilter::getCheckIn).reversed());
        }

        updateBookingsList();
    }

    private void resetFilters() {
        nameFilter.clear();
        startDateFilter.setValue(null);
        endDateFilter.setValue(null);
        isAscending = true;
        sortAscBtn.getStyleClass().add("active");
        sortDescBtn.getStyleClass().remove("active");
        applyFilters();
    }

    private void updateBookingsList() {
        bookingsContainer.getChildren().clear();

        if (filteredBookings.isEmpty()) {
            Label noResults = new Label("Nessuna prenotazione trovata");
            noResults.getStyleClass().add("no-results");
            bookingsContainer.getChildren().add(noResults);
            return;
        }

        for (BookingFilter booking : filteredBookings) {
            BookingCard card = new BookingCard(booking);
            card.setOnCardClick(this::onBookingCardClick);
            bookingsContainer.getChildren().add(card);
        }
    }

    private void onBookingCardClick(BookingFilter booking) {
        System.out.println("🎯 Dettagli prenotazione: " + booking);
    }

    // ===== METODI PUBBLICI =====

    public void setBookings(ObservableList<BookingFilter> bookings) {
        this.allBookings = bookings;
        this.filteredBookings.clear();
        this.filteredBookings.addAll(bookings);
        applyFilters();
    }

    public void updateStats(int occupied, int checkins, int checkouts, int available) {
        occupiedRoomsCard.setNumber(occupied);
        checkinsCard.setNumber(checkins);
        checkoutsCard.setNumber(checkouts);
        availableRoomsCard.setNumber(available);
    }

    public ObservableList<BookingFilter> getAllBookings() {
        return allBookings;
    }
}