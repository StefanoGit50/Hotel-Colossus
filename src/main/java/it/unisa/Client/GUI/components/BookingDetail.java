package it.unisa.Client.GUI.components;

import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Common.Servizio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

/**
 * BookingDetail - Vista dettaglio prenotazione
 * REFACTORING DINAMICO: I totali si aggiornano automaticamente.
 */
public class BookingDetail extends VBox {

    // ===== DATI & COSTANTI =====
    private String bookingCode;
    private String clientName;
    private String fiscalCode;
    private boolean checkinCompleted;

    // Costi Fissi (es. somma delle camere e dei trattamenti)
    private final double FIXED_ROOM_COST = 716.00;
    private final double FIXED_TREATMENT_COST = 404.00;

    // [DINAMICO] Lista "Master" dei servizi. Il programma legge da qui per fare i calcoli.
    private List<ServiceItem> serviceItems;

    // ===== COMPONENTI UI =====
    private Button checkinStatusBtn;
    private TextArea notesArea;
    private GridPane servicesGrid;

    // [DINAMICO] Le etichette del riepilogo sono variabili globali per poterle aggiornare
    private Label lblServicesTotal;
    private Label lblGrandTotal;

    // ===== COSTRUTTORE =====
    public BookingDetail() {
        this("TXAA504554", "CLAUDIO MINERVA", "MNRCLD85M01H501Z", true);
    }

    public BookingDetail(String bookingCode, String clientName, String fiscalCode, boolean checkinCompleted) {
        this.bookingCode = bookingCode;
        this.clientName = clientName;
        this.fiscalCode = fiscalCode;
        this.checkinCompleted = checkinCompleted;
        this.serviceItems = new ArrayList<>();

       // 1. Inizializziamo i dati PRIMA della grafica
        initializeComponents();
        setupLayout();
        setupStyling();

        // 2. Facciamo il primo calcolo appena aperta la finestra
        recalculateTotals();
    }

    public void setBookingData(Prenotazione p, List<Servizio> catalogoCompleto) {

        Label lblCode = (Label) ((VBox)((HBox)this.getChildren().get(0)).getChildren().get(2)).getChildren().get(1);
        lblCode.setText(String.valueOf(p.getIDPrenotazione()));

        // Svuota la tabella
        ScrollPane scroll = (ScrollPane) this.getChildren().get(2);
        GridPane grid = (GridPane) scroll.getContent();
        VBox leftCol = (VBox) grid.getChildren().get(0);
        TableView<RoomBooking> table = (TableView<RoomBooking>) ((VBox) leftCol.getChildren().get(0)).getChildren().get(1);

        table.getItems().clear();

        for (Cliente c : p.getListaClienti()) {
            String numCamera = (c.getCamera()!= null) ? String.valueOf(c.getCamera().getNumeroCamera()) : "-";
            String tipo = null;
            if( c.getCamera()!= null) {
                if (c.getCamera().getCapacità() == 1)
                    tipo= "Singola";
                else if (c.getCamera().getCapacità() == 2) {
                    tipo = "Doppia";
                } else if (c.getCamera().getCapacità() == 3) {
                    tipo = "Tripla";
                }
            }else {
                tipo = "-";
            }

            table.getItems().add(new RoomBooking(
                    c.getNome(), c.getCognome(), numCamera, "11/07/2026","12/07/2026",tipo,"Pensione Completa",""+ c.getCamera().getPrezzoCamera(),
                    ""+p.getTrattamento().getPrezzo(),"anagrafica")
            );
        }


        this.serviceItems.clear();



        for (Servizio voceCatalogo : catalogoCompleto) {
            int numeroOccorrenze = Collections.frequency(serviceItems, voceCatalogo);

            // Aggiungi alla lista grafica
            // Nota: getEmojiForService è un metodo helper (vedi punto 4 se non ce l'hai)
            this.serviceItems.add(new ServiceItem(
                    getEmojiForService(voceCatalogo.getNome()),
                    voceCatalogo.getNome(),
                    voceCatalogo.getPrezzo(),
                    numeroOccorrenze
            ));
        }

        // --- 4. Ridisegna la Griglia Servizi ---
        VBox servicesSection = (VBox) leftCol.getChildren().get(1);
        servicesSection.getChildren().remove(1); // Rimuovi vecchia griglia
        this.servicesGrid = createServicesGrid(); // Ricrea griglia con nuovi dati
        servicesSection.getChildren().add(this.servicesGrid);

        // Aggiorna i totali
        recalculateTotals();
    }

    // Aggiungi anche questo se non lo avevi copiato prima
    private String getEmojiForService(String nome) {
        if (nome == null) return "✨";
        String n = nome.toLowerCase();
        if (n.contains("piscina")) return "🏊";
        if (n.contains("spa")) return "💆";
        if (n.contains("camera")) return "🛏️";
        if (n.contains("colazione")) return "🍽️";
        if (n.contains("auto") || n.contains("transfer")) return "🚗";
        if (n.contains("vino") || n.contains("bar")) return "🍷";
        return "✨";
    }



    private void initializeComponents() {
        checkinStatusBtn = new Button();
        updateCheckinButton();
        checkinStatusBtn.setOnAction(e -> toggleCheckin());

        notesArea = new TextArea("Cliente celebra anniversario - preparare champagne in camera");
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(5);
        notesArea.getStyleClass().add("booking-notes-area");
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

    // ===== HEADER =====
    private HBox createHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 35, 15, 35));
        header.getStyleClass().add("booking-header");

        VBox headerLeft = new VBox(5);
        headerLeft.setAlignment(Pos.CENTER_LEFT);
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
        Label codeValue = new Label(bookingCode);
        codeValue.getStyleClass().add("booking-code-value");
        codeBox.getChildren().addAll(codeLabel, codeValue);

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
        icon.setStyle("-fx-font-size: 32px;");
        VBox nameBox = createInfoBox("Intestato a", clientName);
        Region separator1 = createSeparator();
        Region spacer = new Region();
        spacer.setMinWidth(0);
        VBox fiscalBox = createInfoBox("Codice Fiscale", fiscalCode);
        Region separator2 = createSeparator();
        VBox checkinBox = createInfoBox("Check-in", "20/07/2025");
        Region separator3 = createSeparator();
        VBox checkoutBox = createInfoBox("Check-out", "24/07/2025");

        clientBox.getChildren().addAll(icon, nameBox,spacer, separator1, fiscalBox, separator2, checkinBox, separator3, checkoutBox);
        return clientBox;
    }

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
        grid.add(leftColumn, 0, 0);
        GridPane.setVgrow(leftColumn, Priority.ALWAYS);

        VBox rightColumn = createRightColumn();
        grid.add(rightColumn, 1, 0);
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

        TableView<RoomBooking> table = createRoomsTable();
        section.getChildren().addAll(titleBox, table);
        return section;
    }

    private TableView<RoomBooking> createRoomsTable() {
        TableView<RoomBooking> table = new TableView<>();
        table.getStyleClass().add("booking-rooms-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(150);

        TableColumn<RoomBooking, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
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

        table.getColumns().addAll(nameCol, surnameCol, roomCol, arrivalCol, departureCol, typeCol, treatmentCol, nightPriceCol, treatmentPriceCol, anagraficaCol);

        table.setRowFactory(tv -> {
            TableRow<RoomBooking> row = new TableRow<>() {
                @Override
                protected void updateItem(RoomBooking item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setStyle("");
                    else setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-padding: 5;");
                }
            };
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #f5e6d3; -fx-background-radius: 8; -fx-translate-x: 5; -fx-padding: 5;");
            });
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-translate-x: 0; -fx-padding: 5;");
            });
            return row;
        });

        table.getItems().addAll(
                new RoomBooking("CLAUDIO", "MINERVA", "123", "20/07/25", "24/07/25", "DOPPIA", "MEZZA PENSIONE", "89.5 €", "50.5 €", "✓ COMPLETA"),
                new RoomBooking("BARBARA", "D'ORSO", "123", "20/07/25", "24/07/25", "DOPPIA", "MEZZA PENSIONE", "89.5 €", "50.5 €", "✓ COMPLETA")
        );

        return table;
    }

    // ===== SERVICES SECTION =====
    private VBox createServicesSection() {
        VBox section = new VBox(15);
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label icon =new Label("✨");
        icon.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-text-fill: #e4c418; -fx-font-size: 22px;");
        Label title = new Label("Servizi Aggiuntivi");
        title.getStyleClass().add("section-title-booking");
        titleBox.getChildren().addAll(icon, title);

        servicesGrid = createServicesGrid();
        section.getChildren().addAll(titleBox, servicesGrid);
        return section;
    }

    /**
     * [DINAMICO] Crea la griglia leggendo la lista di oggetti ServiceItem
     */
    private GridPane createServicesGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        int col = 0;
        int row = 0;

        for (ServiceItem item : serviceItems) {
            // Passiamo l'oggetto reale (item) alla funzione di creazione
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

    /**
     * [DINAMICO] Crea la card e collega i bottoni all'oggetto dati
     */
    private VBox createServiceCard(ServiceItem item) {
        VBox card = new VBox(12);
        card.getStyleClass().add("booking-service-card");
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
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

        // Label locali per quantità e subtotale (da aggiornare al click)
        Label subtotalLabel = new Label(String.format("%.0f €", item.getTotalPrice()));
        subtotalLabel.getStyleClass().add("service-subtotal");

        Label qtyLabel = new Label(String.valueOf(item.quantity));
        qtyLabel.getStyleClass().add("qty-value");
        qtyLabel.setMinWidth(35);
        qtyLabel.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("−");
        minusBtn.getStyleClass().add("qty-btn");
        // [EVENTO] Passiamo item, e le label locali
        minusBtn.setOnAction(e -> updateQuantity(item, -1, qtyLabel, subtotalLabel));

        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("qty-btn");
        // [EVENTO] Passiamo item, e le label locali
        plusBtn.setOnAction(e -> updateQuantity(item, 1, qtyLabel, subtotalLabel));

        qtyBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        controls.getChildren().addAll(qtyBox, spacer2, subtotalLabel);
        card.getChildren().addAll(header, controls);

        card.setOnMouseEntered(e -> card.setStyle("-fx-border-color: #6d1331; -fx-border-width: 2; -fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(109, 19, 49, 0.2), 20, 0, 0, 5); -fx-translate-y: -3;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 2; -fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-translate-y: 0;"));

        return card;
    }

    /**
     * [LOGICA] Aggiorna quantità singolo servizio e chiama il ricalcolo totale
     */
    private void updateQuantity(ServiceItem item, int change, Label qtyLabel, Label subtotalLabel) {
        int newQty = Math.max(0, item.quantity + change);

        // 1. Aggiorna il modello dati
        item.quantity = newQty;

        // 2. Aggiorna la card specifica (grafica locale)
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

        // 3. Ricalcola i totali generali (Riepilogo Costi)
        recalculateTotals();
    }

    /**
     * [LOGICA] Somma tutto e aggiorna le Label globali del Riepilogo
     */
    private void recalculateTotals() {
        double servicesTotal = 0.0;

        // Somma i prezzi di tutti i servizi nella lista
        for (ServiceItem item : serviceItems) {
            servicesTotal += item.getTotalPrice();
        }

        double grandTotal = FIXED_ROOM_COST + FIXED_TREATMENT_COST + servicesTotal;

        // Aggiorna le Label globali (se sono già state create)
        if (lblServicesTotal != null) {
            lblServicesTotal.setText(String.format("%.2f €", servicesTotal));
        }
        if (lblGrandTotal != null) {
            lblGrandTotal.setText(String.format("%,.2f €", grandTotal));
        }
    }

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

        // [DINAMICO] Inizializzazione Label Globali
        lblServicesTotal = new Label();
        lblServicesTotal.getStyleClass().add("summary-value");

        lblGrandTotal = new Label();
        lblGrandTotal.getStyleClass().add("summary-value-total");

        lines.getChildren().addAll(
                createSummaryLine("Camere (4 notti)", String.format("%.2f €", FIXED_ROOM_COST), false, null),
                createSummaryLine("Trattamenti", String.format("%.2f €", FIXED_TREATMENT_COST), false, null),
                // Passiamo le label globali per essere inserite nel layout
                createSummaryLine("Servizi Extra", null, false, lblServicesTotal),
                createSummaryLine("TOTALE", null, true, lblGrandTotal)
        );

        box.getChildren().addAll(titleBox, lines);
        return box;
    }

    private HBox createSummaryLine(String label, String staticValue, boolean isTotal, Label dynamicLabel) {
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

        Label valueNode;
        // Se c'è una label dinamica usiamo quella, altrimenti ne creiamo una statica
        if (dynamicLabel != null) {
            valueNode = dynamicLabel;
        } else {
            valueNode = new Label(staticValue);
            valueNode.getStyleClass().add(isTotal ? "summary-value-total" : "summary-value");
        }

        line.getChildren().addAll(labelNode, spacer, valueNode);
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
        checkinCompleted = !checkinCompleted;
        updateCheckinButton();
    }

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

    /**
     * [NUOVA CLASSE] Rappresenta un servizio con i suoi dati numerici
     */
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