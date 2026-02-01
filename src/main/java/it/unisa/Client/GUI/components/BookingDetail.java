package it.unisa.Client.GUI.components;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Common.Servizio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * BookingDetail - Vista dettaglio prenotazione PULITA E DINAMICA
 * Riceve dati → Mostra numeri. SEMPLICE.
 */
public class BookingDetail extends VBox {

    // ===== DATI =====
    private Prenotazione prenotazione;
    private List<Servizio> catalogoServizi;
    private List<ServiceItem> serviceItems;

    // ===== COMPONENTI UI (riferimenti diretti) =====
    private Button checkinStatusBtn;
    private TextArea notesArea;
    private TableView<RoomBooking> roomsTable;
    private GridPane servicesGrid;

    // Label dinamiche per totali
    private Label lblCode;
    private Label lblClientName;
    private Label lblFiscalCode;
    private Label lblCheckin;
    private Label lblCheckout;
    private Label lblRoomCost;
    private Label lblTreatmentCost;
    private Label lblServicesTotal;
    private Label lblGrandTotal;

    // ===== COSTRUTTORE - RICEVE DATI E BASTA =====
    public BookingDetail(Prenotazione prenotazione, List<Servizio> catalogoServizi) {
        this.prenotazione = prenotazione;
        this.catalogoServizi = catalogoServizi;
        this.serviceItems = new ArrayList<>();

        // 1. Prepara i dati dei servizi
        initializeServiceItems();

        // 2. Costruisci l'interfaccia
        initializeComponents();
        setupLayout();
        setupStyling();

        // 3. Carica i dati nella UI
        loadDataIntoUI();
    }

    /**
     * Prepara la lista dei servizi dal catalogo
     */
    private void initializeServiceItems() {
        // Per ogni servizio del catalogo
        for (Servizio catalogItem : catalogoServizi) {
            int count = 0;

            // Conta quante volte appare nella prenotazione
            if (prenotazione.getListaServizi() != null) {
                for (Servizio prenotatoItem : prenotazione.getListaServizi()) {
                    if (prenotatoItem.getNome().equals(catalogItem.getNome())) {
                        count++;
                    }
                }
            }

            // Aggiungi alla lista
            serviceItems.add(new ServiceItem(
                    getEmojiForService(catalogItem.getNome()),
                    catalogItem.getNome(),
                    catalogItem.getPrezzo(),
                    count
            ));
        }
    }

    private String getEmojiForService(String nome) {
        if (nome == null) return "✨";
        String n = nome.toLowerCase();
        if (n.contains("piscina")) return "🏊";
        if (n.contains("spa")) return "💆";
        if (n.contains("camera")) return "🛏️";
        if (n.contains("colazione")) return "🍽️";
        if (n.contains("transfer")) return "🚗";
        if (n.contains("vino")) return "🍷";
        if (n.contains("parcheggio")) return "🅿️";
        if (n.contains("lavanderia")) return "🧺";
        return "✨";
    }

    private void initializeComponents() {
        checkinStatusBtn = new Button();
        checkinStatusBtn.setOnAction(e -> toggleCheckin());
        updateCheckinButton();

        String noteText = (prenotazione.getNoteAggiuntive() != null && !prenotazione.getNoteAggiuntive().isEmpty())
                ? prenotazione.getNoteAggiuntive()
                : "Nessuna nota";
        notesArea = new TextArea(noteText);
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(5);
        notesArea.getStyleClass().add("booking-notes-area");

        roomsTable = createRoomsTable();
    }

    private void setupLayout() {
        this.setSpacing(15);
        this.setPadding(new Insets(11, 25, 11, 25));

        HBox header = createHeader();
        HBox clientInfo = createClientInfoInline();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        GridPane mainGrid = createMainGrid();
        scrollPane.setContent(mainGrid);

        HBox actionBar = createActionBar();

        this.getChildren().addAll(header, clientInfo, scrollPane, actionBar);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private void setupStyling() {
        this.getStyleClass().add("booking-detail-view");
    }

    /**
     * CARICA I DATI NELLA UI - Chiamato una volta sola
     */
    private void loadDataIntoUI() {
        // Header
        lblCode.setText(String.valueOf(prenotazione.getIDPrenotazione()));

        // Client info
        Cliente intestatario = null;
        for (Cliente c : prenotazione.getListaClienti()) {
            if (c.isIntestatario()) {
                intestatario = c;
                break;
            }
        }

        if (intestatario != null) {
            lblClientName.setText(intestatario.getNome() + " " + intestatario.getCognome());
            lblFiscalCode.setText(intestatario.getCf());
        }

        lblCheckin.setText(prenotazione.getDataInizio().toString());
        lblCheckout.setText(prenotazione.getDataFine().toString());

        // Tabella camere
        roomsTable.getItems().clear();
        for (Cliente c : prenotazione.getListaClienti()) {
            String numCamera = (c.getCamera() != null) ? String.valueOf(c.getCamera().getNumeroCamera()) : "-";
            String tipo = getTipoCamera(c.getCamera());
            String prezzoCamera = (c.getCamera() != null) ? String.format("%.2f €", c.getCamera().getPrezzoCamera()) : "0.00 €";
            String prezzoTrattamento = String.format("%.2f €", prenotazione.getTrattamento().getPrezzo());

            roomsTable.getItems().add(new RoomBooking(
                    c.getNome(),
                    c.getCognome(),
                    numCamera,
                    prenotazione.getDataInizio().toString(),
                    prenotazione.getDataFine().toString(),
                    tipo,
                    prenotazione.getTrattamento().getNome(),
                    prezzoCamera,
                    prezzoTrattamento,
                    "✓ COMPLETA"
            ));
        }

        // Calcola e mostra i totali
        recalculateTotals();
    }

    private String getTipoCamera(Camera camera) {
        if (camera == null) return "-";
        int capacita = camera.getCapacità();
        if (capacita == 1) return "Singola";
        if (capacita == 2) return "Doppia";
        if (capacita == 3) return "Tripla";
        return "Suite";
    }

    // ===== HEADER =====
    private HBox createHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 35, 15, 35));
        header.getStyleClass().add("booking-header");

        VBox headerLeft = new VBox(5);
        Label title = new Label("GESTIONE PRENOTAZIONE");
        title.getStyleClass().add("booking-header-title");
        Label subtitle = new Label("Visualizza e modifica i dettagli della prenotazione");
        subtitle.getStyleClass().add("booking-header-subtitle");
        headerLeft.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox codeBox = new VBox(3);
        codeBox.setAlignment(Pos.CENTER);
        codeBox.getStyleClass().add("booking-code-box");
        codeBox.setPadding(new Insets(10, 20, 10, 20));
        Label codeLabel = new Label("Codice Prenotazione");
        codeLabel.getStyleClass().add("booking-code-label");
        lblCode = new Label(""); // Sarà riempita dopo
        lblCode.getStyleClass().add("booking-code-value");
        codeBox.getChildren().addAll(codeLabel, lblCode);

        checkinStatusBtn.getStyleClass().add("booking-checkin-btn");
        checkinStatusBtn.setPadding(new Insets(12, 24, 12, 24));

        header.getChildren().addAll(headerLeft, spacer, codeBox, checkinStatusBtn);
        return header;
    }

    // ===== CLIENT INFO =====
    private HBox createClientInfoInline() {
        HBox clientBox = new HBox(25);
        clientBox.setAlignment(Pos.CENTER_LEFT);
        clientBox.setPadding(new Insets(18, 30, 18, 30));
        clientBox.getStyleClass().add("booking-client-info");

        Label icon = new Label("👤");
        icon.setStyle("-fx-font-size: 32px; -fx-text-fill: black");

        lblClientName = new Label("");
        lblFiscalCode = new Label("");
        lblCheckin = new Label("");
        lblCheckout = new Label("");

        VBox nameBox = createInfoBox("Intestato a", lblClientName);
        Region sep1 = createSeparator();
        VBox fiscalBox = createInfoBox("Codice Fiscale", lblFiscalCode);
        Region sep2 = createSeparator();
        VBox checkinBox = createInfoBox("Check-in", lblCheckin);
        Region sep3 = createSeparator();
        VBox checkoutBox = createInfoBox("Check-out", lblCheckout);

        clientBox.getChildren().addAll(icon, nameBox, sep1, fiscalBox, sep2, checkinBox, sep3, checkoutBox);
        return clientBox;
    }

    private VBox createInfoBox(String label, Label valueLabel) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER_LEFT);
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("client-info-label");
        valueLabel.getStyleClass().add("client-info-value");
        box.getChildren().addAll(labelNode, valueLabel);
        return box;
    }

    private Region createSeparator() {
        Region separator = new Region();
        separator.setPrefWidth(2);
        separator.setPrefHeight(40);
        separator.setStyle("-fx-background-color: #f5e6d3;");
        return separator;
    }

    // ===== MAIN GRID =====
    private GridPane createMainGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(20);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        grid.getColumnConstraints().addAll(col1, col2);

        VBox leftColumn = createLeftColumn();
        VBox rightColumn = createRightColumn();

        grid.add(leftColumn, 0, 0);
        grid.add(rightColumn, 1, 0);

        GridPane.setVgrow(leftColumn, Priority.ALWAYS);
        GridPane.setVgrow(rightColumn, Priority.ALWAYS);

        return grid;
    }

    // ===== LEFT COLUMN =====
    private VBox createLeftColumn() {
        VBox leftCol = new VBox(20);
        VBox roomsSection = createRoomsSection();
        VBox servicesSection = createServicesSection();
        leftCol.getChildren().addAll(roomsSection, servicesSection);
        VBox.setVgrow(servicesSection, Priority.ALWAYS);
        return leftCol;
    }

    private VBox createRoomsSection() {
        VBox section = new VBox(15);
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("🛏️");
        icon.setStyle("-fx-font-size: 18px;");
        Label title = new Label("Camere Prenotate");
        title.getStyleClass().add("section-title-booking");
        titleBox.getChildren().addAll(icon, title);
        section.getChildren().addAll(titleBox, roomsTable);
        return section;
    }

    private TableView<RoomBooking> createRoomsTable() {
        TableView<RoomBooking> table = new TableView<>();
        table.getStyleClass().add("booking-rooms-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(150);

        TableColumn<RoomBooking, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        nameCol.setStyle("-fx-background-radius: 12 0 0 0;");

        TableColumn<RoomBooking, String> surnameCol = new TableColumn<>("Cognome");
        surnameCol.setCellValueFactory(data -> data.getValue().surnameProperty());

        TableColumn<RoomBooking, String> roomCol = new TableColumn<>("Camera");
        roomCol.setCellValueFactory(data -> data.getValue().roomNumberProperty());

        TableColumn<RoomBooking, String> arrivalCol = new TableColumn<>("Arrivo");
        arrivalCol.setCellValueFactory(data -> data.getValue().arrivalProperty());

        TableColumn<RoomBooking, String> departureCol = new TableColumn<>("Partenza");
        departureCol.setCellValueFactory(data -> data.getValue().departureProperty());

        TableColumn<RoomBooking, String> typeCol = new TableColumn<>("Tipologia");
        typeCol.setCellValueFactory(data -> data.getValue().roomTypeProperty());

        TableColumn<RoomBooking, String> treatmentCol = new TableColumn<>("Trattamento");
        treatmentCol.setCellValueFactory(data -> data.getValue().treatmentProperty());

        TableColumn<RoomBooking, String> nightPriceCol = new TableColumn<>("€ Notte");
        nightPriceCol.setCellValueFactory(data -> data.getValue().nightPriceProperty());

        TableColumn<RoomBooking, String> treatmentPriceCol = new TableColumn<>("€ Tratt.");
        treatmentPriceCol.setCellValueFactory(data -> data.getValue().treatmentPriceProperty());

        TableColumn<RoomBooking, String> anagraficaCol = new TableColumn<>("Anagrafica");
        anagraficaCol.setCellValueFactory(data -> data.getValue().anagraficaProperty());
        anagraficaCol.setStyle("-fx-background-radius: 0 12 0 0;");
        anagraficaCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("booking-badge-complete");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        table.getColumns().addAll(nameCol, surnameCol, roomCol, arrivalCol, departureCol,
                typeCol, treatmentCol, nightPriceCol, treatmentPriceCol, anagraficaCol);

        // Hover effect
        table.setRowFactory(tv -> {
            TableRow<RoomBooking> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #f5e6d3; -fx-background-radius: 8; -fx-translate-x: 5;");
            });
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-translate-x: 0;");
            });
            return row;
        });

        return table;
    }

    // ===== SERVICES SECTION =====
    private VBox createServicesSection() {
        VBox section = new VBox(15);
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("✨");
        icon.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-text-fill: #e4c418; -fx-font-size: 22px;");
        Label title = new Label("Servizi Aggiuntivi");
        title.getStyleClass().add("section-title-booking");
        titleBox.getChildren().addAll(icon, title);

        servicesGrid = createServicesGrid();
        section.getChildren().addAll(titleBox, servicesGrid);
        return section;
    }

    private GridPane createServicesGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        int col = 0;
        int row = 0;

        for (ServiceItem item : serviceItems) {
            VBox serviceCard = createServiceCard(item);
            grid.add(serviceCard, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(33.33);
            grid.getColumnConstraints().add(colConstraint);
        }

        return grid;
    }

    private VBox createServiceCard(ServiceItem item) {
        VBox card = new VBox(12);
        card.getStyleClass().add("booking-service-card");
        card.setPadding(new Insets(16));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        HBox nameBox = new HBox(10);
        Label iconLabel = new Label(item.emoji);
        iconLabel.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-text-fill: black; -fx-font-size: 28px;");

        VBox infoBox = new VBox(2);
        Label nameLabel = new Label(item.name);
        nameLabel.getStyleClass().add("service-name");
        Label descLabel = new Label(getServiceDescription(item.name));
        descLabel.getStyleClass().add("service-desc");
        infoBox.getChildren().addAll(nameLabel, descLabel);
        nameBox.getChildren().addAll(iconLabel, infoBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label(String.format("%.0f €", item.unitPrice));
        priceLabel.getStyleClass().add("service-price");

        header.getChildren().addAll(nameBox, spacer, priceLabel);

        // Controls
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_LEFT);

        HBox qtyBox = new HBox(10);
        qtyBox.setAlignment(Pos.CENTER);

        Label qtyLabel = new Label(String.valueOf(item.quantity));
        qtyLabel.getStyleClass().add("qty-value");
        qtyLabel.setMinWidth(35);
        qtyLabel.setAlignment(Pos.CENTER);

        Label subtotalLabel = new Label(String.format("%.0f €", item.getTotalPrice()));
        subtotalLabel.getStyleClass().add("service-subtotal");

        Button minusBtn = new Button("−");
        minusBtn.getStyleClass().add("qty-btn");
        minusBtn.setOnAction(e -> updateQuantity(item, -1, qtyLabel, subtotalLabel));

        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("qty-btn");
        plusBtn.setOnAction(e -> updateQuantity(item, 1, qtyLabel, subtotalLabel));

        qtyBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        controls.getChildren().addAll(qtyBox, spacer2, subtotalLabel);
        card.getChildren().addAll(header, controls);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-border-color: #6d1331; -fx-border-width: 2; " +
                        "-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-border-radius: 10; -fx-translate-y: -3; " +
                        "-fx-effect: dropshadow(gaussian, rgba(109, 19, 49, 0.2), 20, 0, 0, 5);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-border-color: #f5e6d3; -fx-border-width: 2; " +
                        "-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-border-radius: 10; -fx-translate-y: 0;"
        ));

        return card;
    }

    private void updateQuantity(ServiceItem item, int change, Label qtyLabel, Label subtotalLabel) {
        int newQty = Math.max(0, item.quantity + change);
        item.quantity = newQty;

        qtyLabel.setText(String.valueOf(newQty));
        subtotalLabel.setText(String.format("%.0f €", item.getTotalPrice()));

        // Animazione
        qtyLabel.setScaleX(1.3);
        qtyLabel.setScaleY(1.3);
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(200));
        pause.setOnFinished(e -> {
            qtyLabel.setScaleX(1.0);
            qtyLabel.setScaleY(1.0);
        });
        pause.play();

        recalculateTotals();
    }

    private void recalculateTotals() {
        // Calcola notti
        long notti = ChronoUnit.DAYS.between(prenotazione.getDataInizio(), prenotazione.getDataFine());
        if (notti <= 0) notti = 1;

        // Calcola costo camere
        double roomCost = 0;
        for (Camera c : prenotazione.getListaCamere()) {
            roomCost += c.getPrezzoCamera();
        }
        roomCost *= notti;

        // Costo trattamento
        double treatmentCost = prenotazione.getTrattamento().getPrezzo() * prenotazione.getListaClienti().size();

        // Costo servizi
        double servicesTotal = 0;
        for (ServiceItem item : serviceItems) {
            servicesTotal += item.getTotalPrice();
        }

        // Totale
        double grandTotal = roomCost + treatmentCost + servicesTotal;

        // Aggiorna UI
        if (lblRoomCost != null) lblRoomCost.setText(String.format("%.2f €", roomCost));
        if (lblTreatmentCost != null) lblTreatmentCost.setText(String.format("%.2f €", treatmentCost));
        if (lblServicesTotal != null) lblServicesTotal.setText(String.format("%.2f €", servicesTotal));
        if (lblGrandTotal != null) lblGrandTotal.setText(String.format("%,.2f €", grandTotal));
    }

    private String getServiceDescription(String serviceName) {
        switch (serviceName) {
            case "Accesso Piscina": return "Accesso giornaliero";
            case "Spa & Wellness": return "Massaggio 60min";
            case "Transfer Aeroporto": return "Andata/Ritorno";
            case "Colazione in Camera": return "Servizio in stanza";
            case "Bottiglia Vino": return "Selezione premium";
            case "Parcheggio Coperto": return "Al giorno";
            case "Lavanderia": return "Servizio rapido";
            default: return "";
        }
    }

    // ===== RIGHT COLUMN =====
    private VBox createRightColumn() {
        VBox rightCol = new VBox(20);
        VBox summaryBox = createSummaryBox();
        VBox notesBox = createNotesBox();
        rightCol.getChildren().addAll(summaryBox, notesBox);
        return rightCol;
    }

    private VBox createSummaryBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("booking-summary-box");
        box.setPadding(new Insets(25));

        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("💰");
        icon.setStyle("-fx-font-size: 18px;");
        Label title = new Label("Riepilogo Costi");
        title.getStyleClass().add("section-title-booking");
        titleBox.getChildren().addAll(icon, title);

        VBox lines = new VBox(0);

        lblRoomCost = new Label();
        lblRoomCost.getStyleClass().add("summary-value");

        lblTreatmentCost = new Label();
        lblTreatmentCost.getStyleClass().add("summary-value");

        lblServicesTotal = new Label();
        lblServicesTotal.getStyleClass().add("summary-value");

        lblGrandTotal = new Label();
        lblGrandTotal.getStyleClass().add("summary-value-total");

        long notti = ChronoUnit.DAYS.between(prenotazione.getDataInizio(), prenotazione.getDataFine());
        if (notti <= 0) notti = 1;

        lines.getChildren().addAll(
                createSummaryLine("Camere (" + notti + " notti)", lblRoomCost),
                createSummaryLine("Trattamenti", lblTreatmentCost),
                createSummaryLine("Servizi Extra", lblServicesTotal),
                createSummaryLineTot("TOTALE", lblGrandTotal)
        );

        box.getChildren().addAll(titleBox, lines);
        return box;
    }

    private HBox createSummaryLine(String label, Label valueLabel) {
        HBox line = new HBox();
        line.setAlignment(Pos.CENTER);
        line.setPadding(new Insets(12, 0, 12, 0));
        line.getStyleClass().add("summary-line");

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("summary-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        line.getChildren().addAll(labelNode, spacer, valueLabel);
        return line;
    }

    private HBox createSummaryLineTot(String label, Label valueLabel) {
        HBox line = new HBox();
        line.setAlignment(Pos.CENTER);
        line.setPadding(new Insets(12, 0, 12, 0));
        line.getStyleClass().add("summary-line-total");

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("summary-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        line.getChildren().addAll(labelNode, spacer, valueLabel);
        return line;
    }

    private VBox createNotesBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("booking-notes-box");
        box.setPadding(new Insets(20));

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

    private HBox createActionBar() {
        HBox actionBar = new HBox(12);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(20, 0, 0, 0));
        actionBar.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 2 0 0 0;");

        Button backBtn = new Button("← INDIETRO");
        backBtn.getStyleClass().add("booking-btn-back");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteBtn = new Button("🗑️ ELIMINA");
        deleteBtn.getStyleClass().add("booking-btn-delete");

        Button saveBtn = new Button("💾 SALVA MODIFICHE");
        saveBtn.getStyleClass().add("booking-btn-save");

        actionBar.getChildren().addAll(backBtn, spacer, deleteBtn, saveBtn);
        return actionBar;
    }

    private void toggleCheckin() {
        prenotazione.setCheckIn(!prenotazione.isCheckIn());
        updateCheckinButton();
    }

    private void updateCheckinButton() {
        if (prenotazione.isCheckIn()) {
            checkinStatusBtn.setText("✓ CHECK-IN COMPLETATO");
            checkinStatusBtn.getStyleClass().removeAll("booking-checkin-pending");
            checkinStatusBtn.getStyleClass().add("booking-checkin-complete");
        } else {
            checkinStatusBtn.setText("⏳ CHECK-IN PENDENTE");
            checkinStatusBtn.getStyleClass().removeAll("booking-checkin-complete");
            checkinStatusBtn.getStyleClass().add("booking-checkin-pending");
        }
    }

    // ===== INNER CLASSES =====
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

    private static class ServiceItem {
        String emoji;
        String name;
        double unitPrice;
        int quantity;

        public ServiceItem(String emoji, String name, double unitPrice, int quantity) {
            this.emoji = emoji;
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public double getTotalPrice() {
            return unitPrice * quantity;
        }
    }
}