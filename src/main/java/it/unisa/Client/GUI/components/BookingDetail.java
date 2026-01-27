package it.unisa.Client.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

/**
 * BookingDetailView - Vista dettaglio prenotazione
 * Stile: V2 Wide Layout + V3 Hover Effect
 *
 * @author Team Hotel Colossus
 * @version 1.0
 */
public class BookingDetail extends VBox {

    // ===== DATI =====
    private String bookingCode;
    private String clientName;
    private String fiscalCode;
    private boolean checkinCompleted;

    // ===== COMPONENTI =====
    private Button checkinStatusBtn;
    private TextArea notesArea;
    private GridPane servicesGrid;

    // ===== COSTRUTTORE =====
    public BookingDetail() {
        this("TXAA504554", "CLAUDIO MINERVA", "MNRCLD85M01H501Z", true);
    }

    public BookingDetail(String bookingCode, String clientName, String fiscalCode, boolean checkinCompleted) {
        this.bookingCode = bookingCode;
        this.clientName = clientName;
        this.fiscalCode = fiscalCode;
        this.checkinCompleted = checkinCompleted;

        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        // Check-in button
        checkinStatusBtn = new Button();
        updateCheckinButton();
        checkinStatusBtn.setOnAction(e -> toggleCheckin());

        // Notes area
        notesArea = new TextArea("Cliente celebra anniversario - preparare champagne in camera");
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(5);
        notesArea.getStyleClass().add("booking-notes-area");
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(20);
        this.setPadding(new Insets(30, 40, 30, 40));

        // Header
        HBox header = createHeader();

        // Client Info Inline
        HBox clientInfo = createClientInfoInline();

        // Main Content Grid (Left: Tables + Services | Right: Summary + Notes)
        GridPane mainGrid = createMainGrid();

        // Action Buttons
        HBox actionBar = createActionBar();

        this.getChildren().addAll(header, clientInfo, mainGrid, actionBar);
        VBox.setVgrow(mainGrid, Priority.ALWAYS);
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("booking-detail-view");
    }

    // ===== HEADER =====

    /**
     * Crea l'header con titolo, codice e check-in status
     */
    private HBox createHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(25, 35, 25, 35));
        header.getStyleClass().add("booking-header");

        // Left: Title
        VBox headerLeft = new VBox(5);
        headerLeft.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("GESTIONE PRENOTAZIONE");
        title.getStyleClass().add("booking-header-title");

        Label subtitle = new Label("Visualizza e modifica i dettagli della prenotazione");
        subtitle.getStyleClass().add("booking-header-subtitle");

        headerLeft.getChildren().addAll(title, subtitle);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right: Booking Code
        VBox codeBox = new VBox(3);
        codeBox.setAlignment(Pos.CENTER);
        codeBox.getStyleClass().add("booking-code-box");
        codeBox.setPadding(new Insets(10, 20, 10, 20));

        Label codeLabel = new Label("Codice Prenotazione");
        codeLabel.getStyleClass().add("booking-code-label");

        Label codeValue = new Label(bookingCode);
        codeValue.getStyleClass().add("booking-code-value");

        codeBox.getChildren().addAll(codeLabel, codeValue);

        // Check-in Status Button
        checkinStatusBtn.getStyleClass().add("booking-checkin-btn");
        checkinStatusBtn.setPadding(new Insets(12, 24, 12, 24));

        header.getChildren().addAll(headerLeft, spacer, codeBox, checkinStatusBtn);

        return header;
    }

    // ===== CLIENT INFO =====

    /**
     * Crea la barra informazioni cliente inline
     */
    private HBox createClientInfoInline() {
        HBox clientBox = new HBox(25);
        clientBox.setAlignment(Pos.CENTER_LEFT);
        clientBox.setPadding(new Insets(18, 30, 18, 30));
        clientBox.getStyleClass().add("booking-client-info");

        // Icon
        Label icon = new Label("👤");
        icon.setStyle("-fx-font-size: 32px;");

        // Client Name
        VBox nameBox = createInfoBox("Intestato a", clientName);

        // Separator
        Region separator1 = createSeparator();

        // Fiscal Code
        VBox fiscalBox = createInfoBox("Codice Fiscale", fiscalCode);

        // Separator
        Region separator2 = createSeparator();

        // Check-in Date
        VBox checkinBox = createInfoBox("Check-in", "20/07/2025");

        // Separator
        Region separator3 = createSeparator();

        // Check-out Date
        VBox checkoutBox = createInfoBox("Check-out", "24/07/2025");

        clientBox.getChildren().addAll(
                icon, nameBox, separator1, fiscalBox,
                separator2, checkinBox, separator3, checkoutBox
        );

        return clientBox;
    }

    /**
     * Crea un box informazione
     */
    private VBox createInfoBox(String label, String value) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("client-info-label");

        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("client-info-value");

        box.getChildren().addAll(labelNode, valueNode);

        return box;
    }

    /**
     * Crea un separatore verticale
     */
    private Region createSeparator() {
        Region separator = new Region();
        separator.setPrefWidth(2);
        separator.setPrefHeight(40);
        separator.setStyle("-fx-background-color: #f5e6d3;");
        return separator;
    }

    // ===== MAIN GRID =====

    /**
     * Crea la griglia principale (2 colonne)
     */
    private GridPane createMainGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(20);

        // Column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        grid.getColumnConstraints().addAll(col1, col2);

        // Left Column
        VBox leftColumn = createLeftColumn();
        grid.add(leftColumn, 0, 0);
        GridPane.setVgrow(leftColumn, Priority.ALWAYS);

        // Right Column (Sidebar)
        VBox rightColumn = createRightColumn();
        grid.add(rightColumn, 1, 0);
        GridPane.setVgrow(rightColumn, Priority.ALWAYS);

        return grid;
    }

    // ===== LEFT COLUMN =====

    /**
     * Crea la colonna sinistra (Tabella + Servizi)
     */
    private VBox createLeftColumn() {
        VBox leftCol = new VBox(20);

        // Rooms Table
        VBox roomsSection = createRoomsSection();

        // Services Grid
        VBox servicesSection = createServicesSection();

        leftCol.getChildren().addAll(roomsSection, servicesSection);
        VBox.setVgrow(servicesSection, Priority.ALWAYS);

        return leftCol;
    }

    /**
     * Crea la sezione camere con tabella
     */
    private VBox createRoomsSection() {
        VBox section = new VBox(15);

        // Section Title
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("🛏️");
        icon.setStyle("-fx-font-size: 18px;");

        Label title = new Label("Camere Prenotate");
        title.getStyleClass().add("section-title-booking");

        titleBox.getChildren().addAll(icon, title);

        // Table
        TableView<RoomBooking> table = createRoomsTable();

        section.getChildren().addAll(titleBox, table);

        return section;
    }

    /**
     * Crea la tabella camere con effetto hover V3
     */
    private TableView<RoomBooking> createRoomsTable() {
        TableView<RoomBooking> table = new TableView<>();
        table.getStyleClass().add("booking-rooms-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns
        TableColumn<RoomBooking, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        nameCol.setPrefWidth(80);

        TableColumn<RoomBooking, String> surnameCol = new TableColumn<>("Cognome");
        surnameCol.setCellValueFactory(data -> data.getValue().surnameProperty());
        surnameCol.setPrefWidth(90);

        TableColumn<RoomBooking, String> roomCol = new TableColumn<>("Camera");
        roomCol.setCellValueFactory(data -> data.getValue().roomNumberProperty());
        roomCol.setPrefWidth(60);

        TableColumn<RoomBooking, String> arrivalCol = new TableColumn<>("Arrivo");
        arrivalCol.setCellValueFactory(data -> data.getValue().arrivalProperty());
        arrivalCol.setPrefWidth(75);

        TableColumn<RoomBooking, String> departureCol = new TableColumn<>("Partenza");
        departureCol.setCellValueFactory(data -> data.getValue().departureProperty());
        departureCol.setPrefWidth(75);

        TableColumn<RoomBooking, String> typeCol = new TableColumn<>("Tipologia");
        typeCol.setCellValueFactory(data -> data.getValue().roomTypeProperty());
        typeCol.setPrefWidth(90);

        TableColumn<RoomBooking, String> treatmentCol = new TableColumn<>("Trattamento");
        treatmentCol.setCellValueFactory(data -> data.getValue().treatmentProperty());
        treatmentCol.setPrefWidth(120);

        TableColumn<RoomBooking, String> nightPriceCol = new TableColumn<>("€ Notte");
        nightPriceCol.setCellValueFactory(data -> data.getValue().nightPriceProperty());
        nightPriceCol.setPrefWidth(70);

        TableColumn<RoomBooking, String> treatmentPriceCol = new TableColumn<>("€ Tratt.");
        treatmentPriceCol.setCellValueFactory(data -> data.getValue().treatmentPriceProperty());
        treatmentPriceCol.setPrefWidth(70);

        TableColumn<RoomBooking, String> anagraficaCol = new TableColumn<>("Anagrafica");
        anagraficaCol.setCellValueFactory(data -> data.getValue().anagraficaProperty());
        anagraficaCol.setCellFactory(col -> new TableCell<RoomBooking, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("booking-badge-complete");
                    setGraphic(badge);
                }
            }
        });
        anagraficaCol.setPrefWidth(100);

        table.getColumns().addAll(
                nameCol, surnameCol, roomCol, arrivalCol, departureCol,
                typeCol, treatmentCol, nightPriceCol, treatmentPriceCol, anagraficaCol
        );

        // ✅ V3 HOVER EFFECT - Custom row factory
        table.setRowFactory(tv -> {
            TableRow<RoomBooking> row = new TableRow<>() {
                @Override
                protected void updateItem(RoomBooking item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else {
                        // Default style
                        setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");
                    }
                }
            };

            // ✅ Hover effect con animazione
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f5e6d3; -fx-background-radius: 8; -fx-translate-x: 5;");
                }
            });

            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-translate-x: 0;");
                }
            });

            return row;
        });

        // Mock data
        table.getItems().addAll(
                new RoomBooking("CLAUDIO", "MINERVA", "123", "20/07/25", "24/07/25",
                        "DOPPIA", "MEZZA PENSIONE", "89.5 €", "50.5 €", "✓ COMPLETA"),
                new RoomBooking("BARBARA", "D'ORSO", "123", "20/07/25", "24/07/25",
                        "DOPPIA", "MEZZA PENSIONE", "89.5 €", "50.5 €", "✓ COMPLETA")
        );

        return table;
    }

    /**
     * Crea la sezione servizi
     */
    private VBox createServicesSection() {
        VBox section = new VBox(15);

        // Section Title
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("✨");
        icon.setStyle("-fx-font-size: 18px;");

        Label title = new Label("Servizi Aggiuntivi");
        title.getStyleClass().add("section-title-booking");

        titleBox.getChildren().addAll(icon, title);

        // Services Grid
        servicesGrid = createServicesGrid();

        section.getChildren().addAll(titleBox, servicesGrid);

        return section;
    }

    /**
     * Crea la griglia servizi (3 colonne)
     */
    private GridPane createServicesGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        // Mock services
        String[][] services = {
                {"🏊", "Accesso Piscina", "80 €", "2"},
                {"🛏️", "Camera Extra", "35 €", "3"},
                {"💆", "Spa & Wellness", "85 €", "1"},
                {"🚗", "Transfer Aeroporto", "50 €", "0"},
                {"🍽️", "Colazione in Camera", "15 €", "4"},
                {"🅿️", "Parcheggio Coperto", "20 €", "4"},
                {"🍷", "Bottiglia Vino", "45 €", "2"},
                {"🧺", "Lavanderia", "25 €", "1"},
                {"📞", "Late Check-out", "40 €", "0"}
        };

        int col = 0;
        int row = 0;

        for (String[] service : services) {
            VBox serviceCard = createServiceCard(service[0], service[1], service[2], Integer.parseInt(service[3]));
            grid.add(serviceCard, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        // Column constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(33.33);
            grid.getColumnConstraints().add(colConstraint);
        }

        return grid;
    }

    /**
     * Crea una card servizio
     */
    private VBox createServiceCard(String emoji, String name, String price, int quantity) {
        VBox card = new VBox(12);
        card.getStyleClass().add("booking-service-card");
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.TOP_LEFT);

        // Header (Icon + Name | Price)
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(emoji);
        iconLabel.setStyle("-fx-font-size: 22px;");

        VBox infoBox = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("service-name");
        Label descLabel = new Label(getServiceDescription(name));
        descLabel.getStyleClass().add("service-desc");
        infoBox.getChildren().addAll(nameLabel, descLabel);

        nameBox.getChildren().addAll(iconLabel, infoBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label(price);
        priceLabel.getStyleClass().add("service-price");

        header.getChildren().addAll(nameBox, spacer, priceLabel);

        // Controls (Qty buttons)
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);

        Button minusBtn = new Button("−");
        minusBtn.getStyleClass().add("qty-btn");
        minusBtn.setOnAction(e -> updateQuantity(card, -1));

        Label qtyLabel = new Label(String.valueOf(quantity));
        qtyLabel.getStyleClass().add("qty-value");
        qtyLabel.setMinWidth(30);
        qtyLabel.setAlignment(Pos.CENTER);

        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("qty-btn");
        plusBtn.setOnAction(e -> updateQuantity(card, 1));

        controls.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        card.getChildren().addAll(header, controls);

        return card;
    }

    /**
     * Aggiorna quantità servizio
     */
    private void updateQuantity(VBox card, int change) {
        HBox controls = (HBox) card.getChildren().get(1);
        Label qtyLabel = (Label) controls.getChildren().get(1);

        int currentQty = Integer.parseInt(qtyLabel.getText());
        int newQty = Math.max(0, currentQty + change);

        qtyLabel.setText(String.valueOf(newQty));
    }

    /**
     * Ottieni descrizione servizio
     */
    private String getServiceDescription(String serviceName) {
        switch (serviceName) {
            case "Accesso Piscina": return "Accesso giornaliero";
            case "Camera Extra": return "Letto aggiuntivo";
            case "Spa & Wellness": return "Massaggio 60min";
            case "Transfer Aeroporto": return "Andata/Ritorno";
            case "Colazione in Camera": return "Servizio in stanza";
            case "Parcheggio Coperto": return "Al giorno";
            case "Bottiglia Vino": return "Selezione premium";
            case "Lavanderia": return "Servizio rapido";
            case "Late Check-out": return "Fino alle 18:00";
            default: return "";
        }
    }

    // ===== RIGHT COLUMN (SIDEBAR) =====

    /**
     * Crea la colonna destra (Summary + Notes)
     */
    private VBox createRightColumn() {
        VBox rightCol = new VBox(20);

        // Summary
        VBox summaryBox = createSummaryBox();

        // Notes
        VBox notesBox = createNotesBox();

        rightCol.getChildren().addAll(summaryBox, notesBox);

        return rightCol;
    }

    /**
     * Crea il box riepilogo
     */
    private VBox createSummaryBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("booking-summary-box");
        box.setPadding(new Insets(25));

        // Title
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("💰");
        icon.setStyle("-fx-font-size: 18px;");

        Label title = new Label("Riepilogo Costi");
        title.getStyleClass().add("section-title-booking");

        titleBox.getChildren().addAll(icon, title);

        // Summary lines
        VBox lines = new VBox(0);

        lines.getChildren().addAll(
                createSummaryLine("Camere (4 notti)", "716.00 €", false),
                createSummaryLine("Trattamenti", "404.00 €", false),
                createSummaryLine("Servizi Extra", "605.00 €", false),
                createSummaryLine("TOTALE", "1.725,00 €", true)
        );

        box.getChildren().addAll(titleBox, lines);

        return box;
    }

    /**
     * Crea una riga riepilogo
     */
    private HBox createSummaryLine(String label, String value, boolean isTotal) {
        HBox line = new HBox();
        line.setAlignment(Pos.CENTER);
        line.setPadding(new Insets(12, 0, 12, 0));

        if (isTotal) {
            line.getStyleClass().add("summary-line-total");
        } else {
            line.getStyleClass().add("summary-line");
        }

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("summary-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueNode = new Label(value);
        valueNode.getStyleClass().add(isTotal ? "summary-value-total" : "summary-value");

        line.getChildren().addAll(labelNode, spacer, valueNode);

        return line;
    }

    /**
     * Crea il box note
     */
    private VBox createNotesBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("booking-notes-box");
        box.setPadding(new Insets(20));

        // Title
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("📝");
        icon.setStyle("-fx-font-size: 18px;");

        Label title = new Label("Note per la Prenotazione");
        title.getStyleClass().add("section-title-booking");

        titleBox.getChildren().addAll(icon, title);

        box.getChildren().addAll(titleBox, notesArea);

        return box;
    }

    // ===== ACTION BAR =====

    /**
     * Crea la barra azioni
     */
    private HBox createActionBar() {
        HBox actionBar = new HBox(12);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(20, 0, 0, 0));
        actionBar.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 2 0 0 0;");

        // Left: Back button
        Button backBtn = new Button("← INDIETRO");
        backBtn.getStyleClass().add("booking-btn-back");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right: Delete + Save
        Button deleteBtn = new Button("🗑️ ELIMINA");
        deleteBtn.getStyleClass().add("booking-btn-delete");

        Button saveBtn = new Button("💾 SALVA MODIFICHE");
        saveBtn.getStyleClass().add("booking-btn-save");

        actionBar.getChildren().addAll(backBtn, spacer, deleteBtn, saveBtn);

        return actionBar;
    }

    // ===== CHECK-IN LOGIC =====

    /**
     * Toggle check-in status
     */
    private void toggleCheckin() {
        checkinCompleted = !checkinCompleted;
        updateCheckinButton();
    }

    /**
     * Aggiorna il bottone check-in
     */
    private void updateCheckinButton() {
        if (checkinCompleted) {
            checkinStatusBtn.setText("✓ CHECK-IN COMPLETATO");
            checkinStatusBtn.getStyleClass().removeAll("booking-checkin-pending");
            checkinStatusBtn.getStyleClass().add("booking-checkin-complete");
        } else {
            checkinStatusBtn.setText("⏳ CHECK-IN PENDENTE");
            checkinStatusBtn.getStyleClass().removeAll("booking-checkin-complete");
            checkinStatusBtn.getStyleClass().add("booking-checkin-pending");
        }
    }

    // ===== INNER CLASS: RoomBooking =====

    /**
     * Classe per rappresentare una prenotazione camera
     */
    public static class RoomBooking {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty surname;
        private final javafx.beans.property.SimpleStringProperty roomNumber;
        private final javafx.beans.property.SimpleStringProperty arrival;
        private final javafx.beans.property.SimpleStringProperty departure;
        private final javafx.beans.property.SimpleStringProperty roomType;
        private final javafx.beans.property.SimpleStringProperty treatment;
        private final javafx.beans.property.SimpleStringProperty nightPrice;
        private final javafx.beans.property.SimpleStringProperty treatmentPrice;
        private final javafx.beans.property.SimpleStringProperty anagrafica;

        public RoomBooking(String name, String surname, String roomNumber, String arrival,
                           String departure, String roomType, String treatment,
                           String nightPrice, String treatmentPrice, String anagrafica) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.surname = new javafx.beans.property.SimpleStringProperty(surname);
            this.roomNumber = new javafx.beans.property.SimpleStringProperty(roomNumber);
            this.arrival = new javafx.beans.property.SimpleStringProperty(arrival);
            this.departure = new javafx.beans.property.SimpleStringProperty(departure);
            this.roomType = new javafx.beans.property.SimpleStringProperty(roomType);
            this.treatment = new javafx.beans.property.SimpleStringProperty(treatment);
            this.nightPrice = new javafx.beans.property.SimpleStringProperty(nightPrice);
            this.treatmentPrice = new javafx.beans.property.SimpleStringProperty(treatmentPrice);
            this.anagrafica = new javafx.beans.property.SimpleStringProperty(anagrafica);
        }

        public javafx.beans.property.SimpleStringProperty nameProperty() { return name; }
        public javafx.beans.property.SimpleStringProperty surnameProperty() { return surname; }
        public javafx.beans.property.SimpleStringProperty roomNumberProperty() { return roomNumber; }
        public javafx.beans.property.SimpleStringProperty arrivalProperty() { return arrival; }
        public javafx.beans.property.SimpleStringProperty departureProperty() { return departure; }
        public javafx.beans.property.SimpleStringProperty roomTypeProperty() { return roomType; }
        public javafx.beans.property.SimpleStringProperty treatmentProperty() { return treatment; }
        public javafx.beans.property.SimpleStringProperty nightPriceProperty() { return nightPrice; }
        public javafx.beans.property.SimpleStringProperty treatmentPriceProperty() { return treatmentPrice; }
        public javafx.beans.property.SimpleStringProperty anagraficaProperty() { return anagrafica; }
    }
}