package it.unisa.Client.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Planning extends VBox {
    // ===== CONFIGURAZIONE =====
    private static final int DAYS_TO_SHOW = 13;
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEE");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM");

    // ===== DATI =====
    private LocalDate currentStartDate = LocalDate.now();
    private final LocalDate today = LocalDate.now();
    private Map<Integer, List<Booking>> bookingsData;
    private List<Room> rooms;

    // ===== COMPONENTI UI =====
    private Label currentPeriodLabel;
    private GridPane calendarGrid;

    // ===== COSTRUTTORE =====
    public Planning() {
        initializeData();
        initializeComponents();
        setupLayout();
        setupStyling();
        renderCalendar();
    }

    /**
     * Inizializza i dati mock
     */
    private void initializeData() {
        // Inizializza camere
        rooms = Arrays.asList(
                new Room(101, "Singola"),
                new Room(102, "Singola"),
                new Room(103, "Singola"),
                new Room(104, "Doppia"),
                new Room(105, "Doppia"),
                new Room(106, "Doppia"),
                new Room(107, "Doppia"),
                new Room(108, "Suite"),
                new Room(109, "Suite"),
                new Room(110, "Suite")
        );

        // Inizializza prenotazioni mock
        bookingsData = new HashMap<>();

        bookingsData.put(101, Arrays.asList(
                new Booking("Mario Rossi", LocalDate.now(), LocalDate.now().plusDays(3), BookingType.OCCUPIED),
                new Booking("Sofia Loren", LocalDate.now().plusMonths(1).plusDays(5), LocalDate.now().plusMonths(1).plusDays(11), BookingType.OCCUPIED)
        ));

        bookingsData.put(102, Arrays.asList(
                new Booking("Barbara d'Orso", LocalDate.now().plusDays(2), LocalDate.now().plusDays(5), BookingType.RESERVED),
                new Booking("Luca Bianchi", LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3), BookingType.RESERVED)
        ));

        bookingsData.put(103, Arrays.asList(
                new Booking("Paolo Verdi", LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(1).plusDays(6), BookingType.OCCUPIED)
        ));

        bookingsData.put(104, Arrays.asList(
                new Booking("Giorgio Ventura", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), BookingType.OCCUPIED)
        ));

        bookingsData.put(107, Arrays.asList(
                new Booking("Antonio Verdi", LocalDate.now(), LocalDate.now().plusDays(6), BookingType.OCCUPIED)
        ));

        bookingsData.put(110, Arrays.asList(
                new Booking("Laura Bianchi", LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), BookingType.RESERVED)
        ));
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        // Calendar grid
        calendarGrid = new GridPane();
        calendarGrid.setHgap(0);
        calendarGrid.setVgap(0);
        calendarGrid.getStyleClass().add("planning-calendar-grid");

        // Period label
        currentPeriodLabel = new Label();
        currentPeriodLabel.getStyleClass().add("planning-period-label");
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Navigation
        HBox navigation = createCalendarNavigation();

        // === WRAPPER CON COLONNA FISSA ===
        HBox calendarContainer = new HBox();
        calendarContainer.setStyle("-fx-background-color: white;");

        // Colonna camere separata (fissa, non scrolla)
        VBox roomsColumn = createFixedRoomsColumn();

        // Scroll pane per le date (scrolla orizzontalmente)
        ScrollPane datesScrollPane = new ScrollPane(calendarGrid);
        datesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        datesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        HBox.setHgrow(datesScrollPane, Priority.ALWAYS);
        datesScrollPane.setFitToWidth(true);
        datesScrollPane.setFitToHeight(true);

        calendarContainer.getChildren().addAll(roomsColumn, datesScrollPane);

        ScrollPane verticalScroll = new ScrollPane(calendarContainer);
        verticalScroll.setFitToWidth(true);
        verticalScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        verticalScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(verticalScroll, Priority.ALWAYS);

        // Legend
        HBox legend = createLegend();

        this.getChildren().addAll(navigation, verticalScroll, legend);
    }

    private VBox createFixedRoomsColumn() {
        VBox column = new VBox();
        column.setStyle("-fx-background-color: white; -fx-border-color: #f5e6d3; -fx-border-width: 1 0 1 1;");

        // Header
        VBox roomHeader = new VBox();
        roomHeader.setAlignment(Pos.CENTER);
        roomHeader.setPadding(new Insets(15));
        roomHeader.setMinHeight(57);
        roomHeader.setMaxHeight(57);
        roomHeader.getStyleClass().add("planning-room-header");

        Label roomLabel = new Label("Camera");
        roomLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        roomHeader.getChildren().add(roomLabel);

        column.getChildren().add(roomHeader);

        // Room cells
        for (Room room : rooms) {
            VBox roomCell = new VBox(5);
            roomCell.setAlignment(Pos.CENTER);
            roomCell.setPadding(new Insets(15));
            roomCell.getStyleClass().add("planning-room-cell");
            roomCell.setMinHeight(80);
            roomCell.setMinWidth(120);
            roomCell.setMaxWidth(120);

            Label roomNumber = new Label(String.valueOf(room.number));
            roomNumber.getStyleClass().add("planning-room-number");

            Label roomType = new Label(room.type);
            roomType.getStyleClass().add("planning-room-type");

            roomCell.getChildren().addAll(roomNumber, roomType);
            column.getChildren().add(roomCell);
        }

        return column;
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("content-area");
    }

    /**
     * Crea la navigazione del calendario
     */
    private HBox createCalendarNavigation() {
        StackPane navStack = new StackPane();
        navStack.setPadding(new Insets(10, 15, 10, 15));
        navStack.getStyleClass().add("planning-calendar-nav");


        HBox leftContainer = new HBox();
        leftContainer.setAlignment(Pos.CENTER_LEFT);
        Label planningLabel = new Label("Planning Camere");
        planningLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 500; -fx-text-fill: #6d1331; -fx-letter-spacing: 1px;");
        leftContainer.getChildren().add(planningLabel);

        // HBox CENTRATO con i controlli di navigazione
        HBox navControls = new HBox(15);
        navControls.setAlignment(Pos.CENTER);

        // Previous buttons
        HBox prevButtons = new HBox(10);
        Button prevWeekBtn = createNavButton("←");
        Button prevDayBtn = createNavButton("‹");
        prevWeekBtn.setOnAction(e -> navigateWeek(-1));
        prevDayBtn.setOnAction(e -> navigateDay(-1));
        prevButtons.getChildren().addAll(prevWeekBtn, prevDayBtn);

        // Current period
        currentPeriodLabel.setMinWidth(200);
        currentPeriodLabel.setAlignment(Pos.CENTER);
        updatePeriodLabel();

        // Next buttons and today
        HBox nextButtons = new HBox(10);
        Button todayBtn = new Button("Oggi");
        todayBtn.getStyleClass().add("planning-today-btn");
        todayBtn.setOnAction(e -> navigateToToday());

        Button nextDayBtn = createNavButton("›");
        Button nextWeekBtn = createNavButton("→");
        nextDayBtn.setOnAction(e -> navigateDay(1));
        nextWeekBtn.setOnAction(e -> navigateWeek(1));

        nextButtons.getChildren().addAll(todayBtn, nextDayBtn, nextWeekBtn);

        navControls.getChildren().addAll(prevButtons, currentPeriodLabel, nextButtons);

        // Bottone a destra (sovrapposto)
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        buttonContainer.setPickOnBounds(false);
        Button newBookingBtn = new Button("+ Nuova Prenotazione");
        newBookingBtn.setMouseTransparent(false);
        newBookingBtn.getStyleClass().add("planning-btn-primary");
        newBookingBtn.setOnAction(e -> onNewBookingClick());

        buttonContainer.getChildren().add(newBookingBtn);

        // Sovrapponi: controlli centrati + bottone a destra
        navStack.getChildren().addAll(leftContainer,navControls, buttonContainer);
        StackPane.setAlignment(leftContainer, Pos.CENTER_LEFT);
        StackPane.setAlignment(navControls, Pos.CENTER);
        StackPane.setAlignment(buttonContainer, Pos.CENTER_RIGHT);

        // Wrap in HBox per compatibilità
        HBox wrapper = new HBox(navStack);
        HBox.setHgrow(navStack, Priority.ALWAYS);

        return wrapper;
    }

    /**
     * Crea un bottone di navigazione
     */
    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("planning-nav-button");
        return btn;
    }

    /**
     * Crea la legenda
     */
    private HBox createLegend() {
        HBox legend = new HBox(25);
        legend.setPadding(new Insets(15));
        legend.getStyleClass().add("planning-legend");

        legend.getChildren().addAll(
                createLegendItem("Occupato", "planning-legend-occupied"),
                createLegendItem("Prenotato", "planning-legend-reserved"),
                createLegendItem("Disponibile", "planning-legend-available")
        );

        return legend;
    }

    /**
     * Crea un item della legenda
     */
    private HBox createLegendItem(String text, String colorClass) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);

        Region colorBox = new Region();
        colorBox.setPrefSize(30, 20);
        colorBox.getStyleClass().add(colorClass);

        Label label = new Label(text);
        label.getStyleClass().add("planning-legend-text");

        item.getChildren().addAll(colorBox, label);

        return item;
    }

    /**
     * Renderizza il calendario
     */
    private void renderCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear();
        calendarGrid.getColumnConstraints().clear();

        // Generate dates
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < DAYS_TO_SHOW; i++) {
            dates.add(currentStartDate.plusDays(i));
        }

        // Create header row
        createCalendarHeader(dates);

        // Create room rows
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            createRoomRow(room, dates, i + 1);
        }

        // Update period label
        updatePeriodLabel();
    }

    /**
     * Crea l'header del calendario
     */
    private void createCalendarHeader(List<LocalDate> dates) {
        // SOLO date columns (NO room header)
        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            VBox dateHeader = createDateHeader(date);
            calendarGrid.add(dateHeader, i, 0);
        }
    }

    /**
     * Crea un header di data
     */
    private VBox createDateHeader(LocalDate date) {
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(12));
        header.getStyleClass().add("planning-date-header");

        // Day name
        Label dayName = new Label(date.format(DAY_FORMATTER).toUpperCase());
        dayName.getStyleClass().add("planning-day-name");

        // Date value
        Label dateValue = new Label(date.format(DATE_FORMATTER));
        dateValue.getStyleClass().add("planning-date-value");

        // Highlight today
        if (date.equals(today)) {
            dateValue.getStyleClass().add("planning-date-today");
        }

        header.getChildren().addAll(dayName, dateValue);

        return header;
    }

    /**
     * Crea una riga per una camera
     */
    private void createRoomRow(Room room, List<LocalDate> dates, int rowIndex) {
        // SOLO day cells (NO room cell)
        boolean[] occupied = new boolean[dates.size()];

        for (int i = 0; i < dates.size(); i++) {
            if (occupied[i]) continue; // Salta se già occupata

            LocalDate date = dates.get(i);
            BookingInfo bookingInfo = getBookingForRoomAndDate(room.number, date, dates);

            StackPane dayCell = createDayCell(room, date, dates);

            if (bookingInfo != null && bookingInfo.isStart && bookingInfo.span > 1) {
                //  IMPOSTA COLUMN SPAN per celle multiple
                GridPane.setColumnSpan(dayCell, bookingInfo.span);

                // Marca le colonne successive come occupate
                for (int j = 0; j < bookingInfo.span && (i + j) < dates.size(); j++) {
                    occupied[i + j] = true;
                }
            }

            calendarGrid.add(dayCell, i, rowIndex);
        }
    }

    /**
     * Crea una cella giorno
     */
    private StackPane createDayCell(Room room, LocalDate date, List<LocalDate> visibleDates) {
        StackPane cell = new StackPane();
        cell.setMinSize(150, 80);
        cell.getStyleClass().add("planning-day-cell");

        // Check for booking
        BookingInfo bookingInfo = getBookingForRoomAndDate(room.number, date, visibleDates);

        if (bookingInfo != null && bookingInfo.isStart) {
            // Create booking block
            VBox bookingBlock = createBookingBlock(bookingInfo);
            cell.getChildren().add(bookingBlock);
            StackPane.setAlignment(bookingBlock, Pos.CENTER_LEFT);
        } else if (bookingInfo == null) {
            // Empty cell - clickable
            cell.setOnMouseClicked(e -> onEmptyCellClick(room, date));
            cell.setOnMouseEntered(e -> cell.setStyle("-fx-background-color: #f0f8ff;"));
            cell.setOnMouseExited(e -> cell.setStyle(""));
        }

        return cell;
    }

    /**
     * Crea un blocco prenotazione
     */
    private VBox createBookingBlock(BookingInfo bookingInfo) {
        VBox block = new VBox(3);
        block.setAlignment(Pos.CENTER_LEFT);
        block.setPadding(new Insets(10, 15, 10, 15));
        block.setMaxHeight(60);

        String styleClass = bookingInfo.booking.type == BookingType.OCCUPIED
                ? "planning-booking-occupied"
                : "planning-booking-reserved";
        block.getStyleClass().add(styleClass);

        // Guest name
        Label guestName = new Label(bookingInfo.booking.guestName);
        guestName.getStyleClass().add("planning-guest-name");

        // Booking dates
        String datesText = bookingInfo.booking.startDate.format(DATE_FORMATTER) +
                " - " +
                bookingInfo.booking.endDate.format(DATE_FORMATTER);
        Label bookingDates = new Label(datesText);
        bookingDates.getStyleClass().add("planning-booking-dates");

        block.getChildren().addAll(guestName, bookingDates);

        // Set width based on span
        if (bookingInfo.span > 1) {
            block.setMinWidth(150 * bookingInfo.span + (bookingInfo.span - 1) * 0);
        }

        // Click handler
        block.setOnMouseClicked(e -> onBookingClick(bookingInfo.booking));
        block.setOnMouseEntered(e -> block.setStyle(block.getStyle() + "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);"));
        block.setOnMouseExited(e -> block.setStyle(""));

        return block;
    }

    /**
     * Ottiene la prenotazione per camera e data
     */
    private BookingInfo getBookingForRoomAndDate(int roomNumber, LocalDate date, List<LocalDate> visibleDates) {
        List<Booking> roomBookings = bookingsData.get(roomNumber);
        if (roomBookings == null) return null;

        for (Booking booking : roomBookings) {
            if (!date.isBefore(booking.startDate) && !date.isAfter(booking.endDate)) {
                // Check if this is the start date
                if (date.equals(booking.startDate)) {
                    // Calculate span
                    long totalDays = java.time.temporal.ChronoUnit.DAYS.between(booking.startDate, booking.endDate) + 1;
                    long visibleDays = visibleDates.stream()
                            .filter(d -> !d.isBefore(booking.startDate) && !d.isAfter(booking.endDate))
                            .count();

                    return new BookingInfo(booking, true, (int) visibleDays);
                }
                return new BookingInfo(booking, false, 0);
            }
        }

        return null;
    }

    /**
     * Aggiorna la label del periodo
     */
    private void updatePeriodLabel() {
        LocalDate endDate = currentStartDate.plusDays(DAYS_TO_SHOW - 1);
        String text = currentStartDate.format(DATE_FORMATTER) + " - " +
                endDate.format(DATE_FORMATTER) + " " +
                currentStartDate.getYear();
        currentPeriodLabel.setText(text);
    }

    // ===== NAVIGAZIONE =====

    private void navigateDay(int direction) {
        currentStartDate = currentStartDate.plusDays(direction);
        renderCalendar();
    }

    private void navigateWeek(int direction) {
        currentStartDate = currentStartDate.plusWeeks(direction);
        renderCalendar();
    }

    private void navigateToToday() {
        currentStartDate = today;
        renderCalendar();
    }

    // ===== EVENT HANDLERS =====

    private void onNewBookingClick() {
        System.out.println("Nuova prenotazione richiesta");
        // TODO: Aprire dialog per nuova prenotazione
    }

    private void onBookingClick(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli Prenotazione");
        alert.setHeaderText("Prenotazione di " + booking.guestName);
        alert.setContentText(
                "Check-in: " + booking.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Check-out: " + booking.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Tipo: " + booking.type
        );
        alert.showAndWait();
    }

    private void onEmptyCellClick(Room room, LocalDate date) {
        System.out.println("Click su cella vuota: Camera " + room.number + ", Data: " + date);
        // TODO: Aprire dialog per nuova prenotazione
    }

    // ===== CLASSI INTERNE =====

    private static class Room {
        int number;
        String type;

        Room(int number, String type) {
            this.number = number;
            this.type = type;
        }
    }

    private static class Booking {
        String guestName;
        LocalDate startDate;
        LocalDate endDate;
        BookingType type;

        Booking(String guestName, LocalDate startDate, LocalDate endDate, BookingType type) {
            this.guestName = guestName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.type = type;
        }
    }

    private enum BookingType {
        OCCUPIED, RESERVED
    }

    private static class BookingInfo {
        Booking booking;
        boolean isStart;
        int span;

        BookingInfo(Booking booking, boolean isStart, int span) {
            this.booking = booking;
            this.isStart = isStart;
            this.span = span;
        }
    }
}