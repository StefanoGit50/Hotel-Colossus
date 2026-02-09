package it.unisa.Client.GUI.components;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Client.GUI.MainApp3;
import it.unisa.Common.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import org.jetbrains.annotations.UnknownNullability;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingCreation - Creazione Prenotazione (V3 Modified with Services)
 */
public class BookingCreation extends VBox {

    private List<Cliente> clientDatabase;
    private List<Cliente> selectedClients;
    private List<ServiceItem> serviceItems; // Lista dei servizi disponibili

    private FlowPane selectedClientsPane;
    private TabPane tabPane;
    private DatePicker checkinDate;
    private DatePicker checkoutDate;
    private TextField roomNumberField;
    private ComboBox<String> treatmentCombo;   // Selettore Trattamento (es. Mezza Pensione)
    private ComboBox<String> documentTypeCombo;
    private TextArea notesArea;
    private VBox roomsContainer; // Contenitore verticale per le righe delle camere
    private ScrollPane mainScrollPane;
    private TextField intestatario;

    private Label lblRoomTotal, lblTreatmentTotal, lblServiceTotal, lblGrandTotal; // Etichette Tab 3
    private Label lblSummaryNights;
    private FrontDeskClient frontDeskClient;


    public BookingCreation(FrontDeskClient frontDeskClient) {
        this.frontDeskClient = frontDeskClient;
        this.clientDatabase = MainApp3.clienti;
        this.serviceItems = createServices(); // Inizializza i servizi
        this.selectedClients = new ArrayList<>();
        setupLayout();
        setupStyling();
    }

    private void setupLayout() {
        HBox topBar = createTopBar();
        tabPane = createTabPane();
        this.getChildren().addAll(topBar, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void setupStyling() {
        this.getStyleClass().add("booking-creation-view");
        this.setStyle("-fx-background-color: white;");
    }


    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.getStyleClass().add("booking-top-bar");
        Label title = new Label("NUOVA PRENOTAZIONE");
        title.getStyleClass().add("booking-top-bar-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(title, spacer);
        return topBar;
    }

    private TabPane createTabPane() {
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("booking-tab-pane");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
                new Tab("🔍 Ricerca & Selezione", createTab1Content()),
                new Tab("📝 Dati Prenotazione", createTab2Content()),
                new Tab("✓ Riepilogo", createTab3Content())
        );

        tabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() == 2) {
                updateTotals();
            }
        });
        return tabs;
    }

    private VBox createTab1Content() {

        return createSearchPanelInternal();
    }

    // Helper temporaneo per non incollare tutto il codice TAB 1 che è già corretto
    private VBox createSearchPanelInternal() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30, 40, 30, 40));
        content.getStyleClass().add("full-search-panel");


        VBox searchSection = new VBox(20);
        Label searchTitle = new Label("🔍 RICERCA CLIENTE NEL SISTEMA");
        searchTitle.getStyleClass().add("search-section-title");

        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(20);
        searchGrid.setVgap(15);

        // Definizione colonne
        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            searchGrid.getColumnConstraints().add(col);
        }

        // Creazione Campi Input
        TextField nameField = new TextField();
        nameField.setPromptText("es. Mario");
        nameField.getStyleClass().add("search-full-input");

        TextField surnameField = new TextField();
        surnameField.setPromptText("es. Rossi");
        surnameField.getStyleClass().add("search-full-input");

        ComboBox<String> nationalityCombo = new ComboBox<>();
        nationalityCombo.getItems().addAll("Tutte", "Italiana", "Francese", "Tedesca", "USA");
        nationalityCombo.setValue("Tutte");
        nationalityCombo.getStyleClass().add("search-full-input");

        DatePicker birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("Data di nascita");
        birthDatePicker.getStyleClass().add("search-full-input");

        // Aggiunta alla griglia
        searchGrid.add(createSearchField("Nome", nameField), 0, 0);
        searchGrid.add(createSearchField("Cognome", surnameField), 1, 0);
        searchGrid.add(createSearchField("Nazionalità", nationalityCombo), 2, 0);
        searchGrid.add(createSearchField("Data di Nascita", birthDatePicker), 3, 0);

        HBox searchButtons = new HBox(15);
        searchButtons.setAlignment(Pos.CENTER_LEFT);

        Button searchBtn = new Button("🔍 CERCA");
        searchBtn.getStyleClass().add("btn-search-full");
        // Tasto Invio per cercare
        searchBtn.setDefaultButton(true);

        Button newClientBtn = new Button("➕ REGISTRA NUOVO CLIENTE");
        newClientBtn.getStyleClass().add("btn-new-client-full");
        newClientBtn.setOnAction(e -> openClientDialog(null));

        searchButtons.getChildren().addAll(searchBtn, newClientBtn);
        searchSection.getChildren().addAll(searchTitle, searchGrid, searchButtons);

        // --- SEZIONE 2: I RISULTATI ---
        VBox resultsSection = new VBox(20);
        HBox resultsHeader = new HBox();
        resultsHeader.setAlignment(Pos.CENTER_LEFT);
        resultsHeader.setPadding(new Insets(15, 0, 15, 0));
        resultsHeader.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 0 0 2 0;");

        Label resultsTitle = new Label("📋 RISULTATI RICERCA");
        resultsTitle.getStyleClass().add("results-section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label resultsCountLabel = new Label("0 clienti trovati");
        resultsCountLabel.getStyleClass().add("results-count-badge-full");

        resultsHeader.getChildren().addAll(resultsTitle, spacer, resultsCountLabel);

        ScrollPane resultsScroll = new ScrollPane();
        resultsScroll.setFitToWidth(true);
        resultsScroll.setStyle("-fx-background-color: transparent;");

        // Griglia dove metteremo le card
        GridPane resultsGrid = new GridPane();
        resultsGrid.setHgap(20);
        resultsGrid.setVgap(15);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            resultsGrid.getColumnConstraints().add(col);
        }

        resultsScroll.setContent(resultsGrid);
        resultsSection.getChildren().addAll(resultsHeader, resultsScroll);
        VBox.setVgrow(resultsSection, Priority.ALWAYS); // Espande i risultati


        //  LOGICA DI FILTRO

        searchBtn.setOnAction(e -> {
            //  Pulisci i risultati precedenti
            resultsGrid.getChildren().clear();

            // Prendi i valori dagli input
            String searchName = nameField.getText().trim().toLowerCase();
            String searchSurname = surnameField.getText().trim().toLowerCase();
            String searchNation = nationalityCombo.getValue();
            LocalDate searchDate = birthDatePicker.getValue();

            int count = 0;
            int col = 0;
            int row = 0;

            //  Itera sul database e filtra
            for (Cliente c : clientDatabase) {
                boolean match = true;

                // Filtro Nome
                if (!searchName.isEmpty() && !c.getNome().toLowerCase().contains(searchName)) {
                    match = false;
                }
                // Filtro Cognome
                if (!searchSurname.isEmpty() && !c.getCognome().toLowerCase().contains(searchSurname)) {
                    match = false;
                }
                // Filtro Nazionalità
                if (searchNation != null && !searchNation.equals("Tutte") && !c.getNazionalita().equalsIgnoreCase(searchNation)) {
                    match = false;
                }
                // Filtro Data Nascita
                if (searchDate != null && !c.getDataNascita().isEqual(searchDate)) {
                    match = false;
                }

                //  Se corrisponde, crea la card
                if (match) {
                    resultsGrid.add(createClientCard(c), col, row);
                    count++;

                    // Gestione griglia (3 colonne)
                    col++;
                    if (col >= 3) {
                        col = 0;
                        row++;
                    }
                }
            }

            //  Aggiorna il contatore
            resultsCountLabel.setText(count + " clienti trovati");

            if (count == 0) {
                Label noResults = new Label("Nessun risultato trovato.");
                noResults.setStyle("-fx-text-fill: #999; -fx-padding: 20;");
                resultsGrid.add(noResults, 0, 0);
            }
        });

        // Carica tutti i clienti all'avvio (Opzionale: simula un click su cerca)
        searchBtn.fire();

        content.getChildren().addAll(searchSection, resultsSection);
        return content;
    }

    // TAB 2: DATI PRENOTAZIONE
    private VBox createTab2Content() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(25, 30, 25, 30));
        content.getStyleClass().add("booking-main-panel");

        //  Sezione Clienti Selezionati (Rimane uguale)
        VBox selectedSection = createSection("👥 Clienti Selezionati (Pool)");
        selectedClientsPane = new FlowPane(10, 10);
        selectedClientsPane.setAlignment(Pos.CENTER_LEFT);
        updateSelectedClients(); // Popola inizialmente
        selectedSection.getChildren().add(selectedClientsPane);

        //  Sezione Date
        VBox dateSection = createSection("📅 Date e Trattamento"); // Rinominato
        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(15); dateGrid.setVgap(15); // Aggiunto Vgap
        ColumnConstraints dcol1 = new ColumnConstraints(); dcol1.setPercentWidth(33);
        ColumnConstraints dcol2 = new ColumnConstraints(); dcol2.setPercentWidth(33);
        ColumnConstraints dcol3 = new ColumnConstraints(); dcol3.setPercentWidth(33); // Colonna per trattamento
        dateGrid.getColumnConstraints().addAll(dcol1, dcol2, dcol3);

        checkinDate = new DatePicker(LocalDate.now());
        checkoutDate = new DatePicker(LocalDate.now().plusDays(4));

// LISTENER DATE: Se cambiano, ricalcola i totali
        checkinDate.valueProperty().addListener((obs, oldVal, newVal) -> updateTotals());
        checkoutDate.valueProperty().addListener((obs, oldVal, newVal) -> updateTotals());

// NUOVO: Selettore Trattamento
        treatmentCombo = new ComboBox<>();
        for (Trattamento t : MainApp3.trattamenti) {
            treatmentCombo.getItems().add(t.getNome());
        }

        treatmentCombo.setValue(MainApp3.trattamenti.getFirst().getNome());
        treatmentCombo.setMaxWidth(Double.MAX_VALUE);
// LISTENER TRATTAMENTO: Se cambia, ricalcola
        treatmentCombo.setOnAction(e -> updateTotals());

        dateGrid.add(createFormField("Data Check-in", checkinDate), 0, 0);
        dateGrid.add(createFormField("Data Check-out", checkoutDate), 1, 0);
        dateGrid.add(createFormField("Trattamento", treatmentCombo), 2, 0);

        dateSection.getChildren().add(dateGrid);


        VBox roomSection = new VBox(15);
        roomSection.getStyleClass().add("form-section");

        // Header con Titolo + CAMPO INTESTATARIO + Bottone Aggiungi
        HBox roomHeader = new HBox(15);
        roomHeader.setAlignment(Pos.CENTER_LEFT);

        // Titolo Sezione
        Label roomTitle = new Label("🛏️ Assegnazione Camere");
        roomTitle.getStyleClass().add("section-heading");

        // Spacer per spingere il resto a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Campo Intestatario
        HBox holderBox = new HBox(10);
        holderBox.setAlignment(Pos.CENTER_RIGHT); // Allineato a destra

        Label lblHolder = new Label("Intestatario:");
        lblHolder.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");

        intestatario = new TextField();
        intestatario.setPromptText("Nome Intestatario");
        intestatario.setPrefWidth(220);
        intestatario.getStyleClass().add("form-field-input");


        if (!selectedClients.isEmpty()) {
            intestatario.setText(selectedClients.get(0).getNome()+" "+selectedClients.get(0).getCognome());
        }

        holderBox.getChildren().addAll(lblHolder, intestatario);

        //  Bottone Aggiungi
        Button addRoomBtn = new Button("➕ Aggiungi Camera");
        addRoomBtn.setStyle("-fx-background-color: #e4c418; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        addRoomBtn.setOnAction(e -> addRoomRow());

        // Aggiungiamo tutto all'header: Titolo -> Spazio -> Intestatario -> Bottone
        roomHeader.getChildren().addAll(roomTitle, spacer, holderBox, addRoomBtn);


        // Contenitore delle righe
        roomsContainer = new VBox(10);
        addRoomRow(); // Aggiunge la prima riga di default

        roomSection.getChildren().addAll(roomHeader, roomsContainer);
        //  Servizi e Note (Rimangono uguali)
        VBox servicesSection = createServicesSection();

        VBox noteSection = createSection("📝 Note");
        notesArea = new TextArea();
        notesArea.setPrefRowCount(3);
        noteSection.getChildren().add(notesArea);

        // Setup ScrollPane finale
        mainScrollPane = new ScrollPane();
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setStyle("-fx-background-color: transparent;");

        VBox scrollContent = new VBox(20);
        scrollContent.getChildren().addAll(selectedSection, dateSection, roomSection, servicesSection, noteSection);
        mainScrollPane.setContent(scrollContent);

        content.getChildren().add(mainScrollPane);
        VBox.setVgrow(mainScrollPane, Priority.ALWAYS);

        return content;
    }

    private void addRoomRow() {
        HBox row = new HBox(15);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPadding(new Insets(15));
        row.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-background-radius: 5;");

        // ===== 1. COMBOBOX CAMERA (unico!) =====
        VBox cameraBox = new VBox(5);
        Label lblCamera = new Label("Camera");
        lblCamera.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        ComboBox<Camera> cameraCombo = new ComboBox<>();
        cameraCombo.getItems().addAll(MainApp3.camere); // Tutte le camere
        cameraCombo.setPromptText("Seleziona camera");
        cameraCombo.setPrefWidth(180);

        // ✅ Mostra "101 - Singola" nel ComboBox
        cameraCombo.setButtonCell(new javafx.scene.control.ListCell<Camera>() {
            @Override
            protected void updateItem(Camera camera, boolean empty) {
                super.updateItem(camera, empty);
                if (empty || camera == null) {
                    setText(null);
                } else {
                    setText(camera.getNumeroCamera() + " - " + camera.getNomeCamera());
                }
            }
        });

        cameraCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Camera>() {
            @Override
            protected void updateItem(Camera camera, boolean empty) {
                super.updateItem(camera, empty);
                if (empty || camera == null) {
                    setText(null);
                } else {
                    setText(camera.getNumeroCamera() + " - " + camera.getNomeCamera());
                }
            }
        });

        cameraBox.getChildren().addAll(lblCamera, cameraCombo);

        // ===== 2. LABEL TIPOLOGIA (solo visualizzazione) =====
        VBox typeBox = new VBox(5);
        Label lblType = new Label("Tipologia");
        lblType.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        Label typeLabel = new Label("-");
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #6d1331;");

        typeBox.getChildren().addAll(lblType, typeLabel);

        // ===== 3. CONTENITORE OSPITI =====
        VBox guestsWrapper = new VBox(5);
        Label lblGuests = new Label("Ospiti in camera");
        lblGuests.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        VBox guestsContainer = new VBox(5);
        guestsWrapper.getChildren().addAll(lblGuests, guestsContainer);
        HBox.setHgrow(guestsWrapper, Priority.ALWAYS);

        // ===== 4. LISTENER: Quando selezioni camera =====
        cameraCombo.setOnAction(e -> {
            Camera selectedCamera = cameraCombo.getValue();

            if (selectedCamera != null) {
                // Aggiorna label tipologia
                typeLabel.setText(selectedCamera.getNomeCamera());

                // Genera campi ospiti in base alla CAPACITÀ
                int capacita = selectedCamera.getCapacità();
                generateGuestFields(capacita, guestsContainer);

                updateTotals();
            }
        });

        // ===== 5. BOTTONE RIMUOVI =====
        Button btnRemove = new Button("✕");
        btnRemove.setStyle("-fx-text-fill: red; -fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        btnRemove.setOnAction(e -> {
            roomsContainer.getChildren().remove(row);
            updateTotals();
        });

        // Aggiungi tutto alla riga
        row.getChildren().addAll(cameraBox, typeBox, guestsWrapper, btnRemove);
        roomsContainer.getChildren().add(row);
        updateTotals();
    }

    private void generateGuestFields(int numGuests, VBox container) {
        container.getChildren().clear(); // Pulisce i campi precedenti

        for (int i = 0; i < numGuests; i++) {
            TextField guestField = new TextField();
            guestField.setPromptText("Nome Ospite " + (i + 1));
            guestField.setMaxWidth(Double.MAX_VALUE);

            HBox fieldBox = new HBox(5);
            fieldBox.setAlignment(Pos.CENTER_LEFT);
            Label icon = new Label("👤");
            fieldBox.getChildren().addAll(icon, guestField);
            HBox.setHgrow(guestField, Priority.ALWAYS);

            container.getChildren().add(fieldBox);
        }
    }


    // METODI PER I SERVIZI (INTEGRATI)

    private VBox createServicesSection() {
        VBox section = new VBox(15);
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("✨");
        icon.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-text-fill: #e4c418; -fx-font-size: 22px;");

        Label title = new Label("Servizi Aggiuntivi (Extra)");
        title.getStyleClass().add("section-heading"); // Uso la tua classe existing
        titleBox.getChildren().addAll(icon, title);

        GridPane servicesGrid = createServicesGrid();
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

        // Definizione colonne (3 colonne al 33%)
        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(33.33);
            grid.getColumnConstraints().add(colConstraint);
        }

        return grid;
    }

    private VBox createServiceCard(ServiceItem item) {
        VBox card = new VBox(12);
        //  stile inline/fallback
        card.getStyleClass().add("booking-service-card");
        card.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e5e7eb; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.setPadding(new Insets(16));

        // Header Card
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        HBox nameBox = new HBox(10);
        Label iconLabel = new Label(item.emoji);
        iconLabel.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-text-fill: black; -fx-font-size: 24px;");

        VBox infoBox = new VBox(2);
        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
        Label descLabel = new Label(getServiceDescription(item.name));
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        infoBox.getChildren().addAll(nameLabel, descLabel);
        nameBox.getChildren().addAll(iconLabel, infoBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label(String.format("€ %.0f", item.unitPrice));
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #d4af37;");

        header.getChildren().addAll(nameBox, spacer, priceLabel);

        // Controls (Qty & Subtotal)
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_LEFT);

        HBox qtyBox = new HBox(10);
        qtyBox.setAlignment(Pos.CENTER);

        Label qtyLabel = new Label(String.valueOf(item.quantity));
        qtyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        qtyLabel.setMinWidth(25);
        qtyLabel.setAlignment(Pos.CENTER);

        Label subtotalLabel = new Label(String.format("€ %.0f", item.getTotalPrice()));
        subtotalLabel.setStyle("-fx-font-weight: 800; -fx-text-fill: #059669;"); // Verde

        Button minusBtn = createQtyButton("−");
        minusBtn.setOnAction(e -> updateQuantity(item, -1, qtyLabel, subtotalLabel));

        Button plusBtn = createQtyButton("+");
        plusBtn.setOnAction(e -> updateQuantity(item, 1, qtyLabel, subtotalLabel));

        qtyBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        controls.getChildren().addAll(qtyBox, spacer2, subtotalLabel);
        card.getChildren().addAll(header, new Separator(), controls);

        // Hover effect inline
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-border-color: #6d1331; -fx-border-width: 1; " +
                        "-fx-background-color: white; -fx-background-radius: 8; " +
                        "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #f9fafb; -fx-border-color: #e5e7eb; -fx-border-radius: 8; -fx-background-radius: 8;"
        ));

        return card;
    }

    private Button createQtyButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db; -fx-border-radius: 4; -fx-min-width: 25px; -fx-min-height: 25px; -fx-cursor: hand;");
        return btn;
    }

    private void updateQuantity(ServiceItem item, int change, Label qtyLabel, Label subtotalLabel) {
        int newQty = Math.max(0, item.quantity + change);
        item.quantity = newQty;

        qtyLabel.setText(String.valueOf(newQty));
        subtotalLabel.setText(String.format("€ %.0f", item.getTotalPrice()));

        // Animazione
        qtyLabel.setScaleX(1.3);
        qtyLabel.setScaleY(1.3);
        PauseTransition pause = new PauseTransition(Duration.millis(200));
        pause.setOnFinished(e -> {
            qtyLabel.setScaleX(1.0);
            qtyLabel.setScaleY(1.0);
        });
        pause.play();

        updateTotals();

    }

    private String getServiceDescription(String name) {
        switch (name) {
            case "Spa & Wellness": return "Accesso giornaliero area relax";
            case "Parcheggio": return "Posto auto coperto sorvegliato";
            case "Colazione Camera": return "Servizio in camera al mattino";
            case "Tour Guidato": return "Tour centro storico (3h)";
            case "Champagne": return "Bottiglia in camera all'arrivo";
            case "Late Checkout": return "Camera fino alle ore 16:00";
            default: return "Servizio aggiuntivo";
        }
    }



    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("form-section");
        Label sectionTitle = new Label(title);
        sectionTitle.getStyleClass().add("section-heading");
        section.getChildren().add(sectionTitle);
        return section;
    }

    private VBox createFormField(String label, Control control) {
        VBox box = new VBox(6);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("form-field-label");
        box.getChildren().addAll(lbl, control);
        return box;
    }


    private VBox createSearchField(String label, Control control) {
        VBox box = new VBox(8);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("search-full-label");
        box.getChildren().addAll(lbl, control);
        return box;
    }

    private VBox createClientCard(@UnknownNullability Cliente client) {


        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("client-card-full");
        //  costruzione card
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("👤");
        icon.getStyleClass().add("client-card-icon");
        VBox nameBox = new VBox(3);
        Label name = new Label(client.getNome()+" "+client.getCognome());
        name.getStyleClass().add("client-card-name");
        Label cf = new Label(client.getCf());
        cf.getStyleClass().add("client-card-cf");
        nameBox.getChildren().addAll(name, cf);
        header.getChildren().addAll(icon, nameBox);

        VBox details = new VBox(8);
        details.getChildren().addAll(
                createDetailRow("Data Nascita", client.getDataNascita().toString()),
                createDetailRow("Nazionalità", client.getNazionalita())
        );

        HBox actions = new HBox(10);
        Button viewBtn = new Button("📋 INFO");
        viewBtn.getStyleClass().add("client-card-btn-view");
        viewBtn.setOnAction(e -> openClientDialog(client));
        Button selectBtn = new Button("✓ SELEZIONA");
        selectBtn.getStyleClass().add("client-card-btn-select");
        selectBtn.setOnAction(e -> {
            selectClient(client);
            tabPane.getSelectionModel().select(1);
        });
        HBox.setHgrow(viewBtn, Priority.ALWAYS);
        HBox.setHgrow(selectBtn, Priority.ALWAYS);
        viewBtn.setMaxWidth(Double.MAX_VALUE);
        selectBtn.setMaxWidth(Double.MAX_VALUE);
        actions.getChildren().addAll(viewBtn, selectBtn);
        card.getChildren().addAll(header, details, actions);
        return card;
    }

    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(label + ":");
        lbl.getStyleClass().add("client-card-detail-label");
        Label val = new Label(value);
        val.getStyleClass().add("client-card-detail-value");
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private VBox createTab3Content() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #f4f4f4;"); // Sfondo leggero

        //
        Label title = new Label("📋 Riepilogo e Conferma");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox mainLayout = new HBox(40); // Spazio tra le due colonne
        mainLayout.setAlignment(Pos.CENTER);


        // COLONNA SINISTRA: DATI SOGGIORNO E INTESTATARIO

        VBox detailsBox = new VBox(15);
        detailsBox.setPrefWidth(400);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label lblDetTitle = new Label("Dettagli Soggiorno");
        lblDetTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #6d1331;");

        // Label per il nome
        Label lblSummaryName = new Label("Intestatario: -");
        lblSummaryName.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        if (intestatario != null) {
            lblSummaryName.textProperty().bind(javafx.beans.binding.Bindings.concat("Intestatario: ", intestatario.textProperty()));
        }

        // Label per le notti (Aggiornata da updateTotals)
        lblSummaryNights = new Label("Durata soggiorno: -");
        lblSummaryNights.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Label per le date
        Label lblDates = new Label();
        if (checkinDate != null && checkoutDate != null) {
            // Binding per mostrare le date correnti
            lblDates.textProperty().bind(javafx.beans.binding.Bindings.concat(
                    "Dal: ", checkinDate.valueProperty(), "  Al: ", checkoutDate.valueProperty()
            ));
        }
        lblDates.setStyle("-fx-font-size: 13px; -fx-text-fill: #777;");

        detailsBox.getChildren().addAll(lblDetTitle, new Separator(), lblSummaryName, lblSummaryNights, lblDates);



        VBox costBox = new VBox(15);
        costBox.setPrefWidth(380);
        costBox.setPadding(new Insets(25));
        // Stile "Scontrino" elegante
        costBox.setStyle("-fx-background-color: white; -fx-border-color: #e4c418; -fx-border-width: 0 0 0 4; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        HBox costTitleBox = new HBox(10);
        Label icon = new Label("💰");
        icon.setStyle("-fx-font-size: 20px;");
        Label costTitle = new Label("Preventivo Finale");
        costTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #333;");
        costTitleBox.getChildren().addAll(icon, costTitle);


        // Queste variabili sono definite a livello di classe e usate da updateTotals()
        lblRoomTotal = new Label("0.00 €");
        lblTreatmentTotal = new Label("0.00 €");
        lblServiceTotal = new Label("0.00 €");

        lblGrandTotal = new Label("0.00 €");
        lblGrandTotal.setStyle("-fx-font-weight: 800; -fx-font-size: 26px; -fx-text-fill: #27ae60;"); // Verde grande

        // Creiamo le righe del preventivo
        VBox lines = new VBox(10);
        lines.getChildren().addAll(
                createSummaryRow("Totale Camere:", lblRoomTotal),
                createSummaryRow("Supplemento Trattamento:", lblTreatmentTotal),
                createSummaryRow("Servizi Extra:", lblServiceTotal),
                new Separator(), // Linea divisoria
                createSummaryRowTotal("TOTALE STIMATO:", lblGrandTotal)
        );

        costBox.getChildren().addAll(costTitleBox, new Separator(), lines);

        // Aggiungiamo le due colonne al layout principale
        mainLayout.getChildren().addAll(detailsBox, costBox);


        // BOTTONE CONFERMA

        Button btnConfirm = new Button("✅ CONFERMA E SALVA PRENOTAZIONE");
        btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 30; -fx-cursor: hand; -fx-background-radius: 30;");
        // Effetto: hover
        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle("-fx-background-color: #219150; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 30; -fx-cursor: hand; -fx-background-radius: 30;"));
        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 30; -fx-cursor: hand; -fx-background-radius: 30;"));

        // eveno: salvataggio nel DB
        btnConfirm.setOnAction(e -> {

            LocalDate dataCreazione = LocalDate.now();
            LocalDate dataInizio = checkinDate.getValue();
            LocalDate dataFine = checkoutDate.getValue();

            // Intestatario
            String intestatarioNome = intestatario.getText();

            // Note
            String note = notesArea.getText();

            String trattamentoScelto = treatmentCombo.getValue();
            // TODO: Converti in enum Trattamento se necessario

            // Servizi selezionati (solo quelli con quantity > 0)
            ArrayList<Servizio> listaServizi = new ArrayList<>();
            for (ServiceItem item : serviceItems) {
                if (item.quantity > 0) {
                    for (int i = 0; i < item.quantity; i++) {
                        Servizio s = new Servizio();
                        s.setNome(item.name);
                        s.setPrezzo(item.unitPrice);
                        listaServizi.add(s);
                    }
                }
            }

            // Clienti selezionati
            ArrayList<Cliente> listaClienti = new ArrayList<>(selectedClients);

            Prenotazione p = new Prenotazione(
                    dataCreazione,           // dataCreazionePrenotazione
                    dataInizio,              // dataInizioPrenotazione
                    dataFine,                // dataFinePrenotazione
                    null,                    // dataEmissioneRicevuta (null per ora)
                    null,                    // trattamento (TODO: converti da String a enum)
                    getPriceForTreatment(trattamentoScelto), // prezzoAcquistoTrattamento
                    "",                      // tipoDocumento (TODO: aggiungi al form)
                    null,                    // dataRilascio
                    null,                    // dataScadenza
                    intestatarioNome,        // intestatario
                    note,                    // noteAggiuntive
                    listaServizi,            // listaServizi
                    listaClienti,            // listaClienti
                    "",                      // numeroDocumento
                    "Carta",                 // metodoPagamento (TODO: aggiungi al form)
                    ""                       // cittadinanza
            );

            frontDeskClient.addPrenotazione(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prenotazione Salvata!\nTotale: " + lblGrandTotal.getText());
            alert.show();
        });

        content.getChildren().addAll(title, mainLayout, btnConfirm);


        updateTotals();

        return content;
    }

    // Metodo helper per creare una riga del preventivo (Label sinistra + Label destra)
    private HBox createSummaryRow(String labelText, Label valueLabel) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        valueLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333; -fx-font-size: 14px;");

        row.getChildren().addAll(lbl, spacer, valueLabel);
        return row;
    }

    // Metodo helper specifico per la riga del TOTALE con font più grande
    private HBox createSummaryRowTotal(String labelText, Label valueLabel) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 0, 0)); // Un po' di padding sopra

        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(lbl, spacer, valueLabel);
        return row;
    }

    private void updateTotals() {
        //  CALCOLO NOTTI
        LocalDate start = (checkinDate != null) ? checkinDate.getValue() : null;
        LocalDate end = (checkoutDate != null) ? checkoutDate.getValue() : null;
        long nights = 1;

        if (start != null && end != null) {
            nights = ChronoUnit.DAYS.between(start, end);
            if (nights <= 0) nights = 1; // Evitiamo prezzi negativi o zero
        }

        // Aggiorna la label delle notti nel riepilogo (se esiste)
        if (lblSummaryNights != null) {
            lblSummaryNights.setText("Durata soggiorno: " + nights + " notti");
        }

        // CALCOLO COSTO CAMERE E CONTEGGIO OSPITI
        double totalRoomCost = 0.0;
        int totalGuests = 0;


        if (roomsContainer != null) {
            for (Node node : roomsContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox row = (HBox) node;

                    // La struttura della riga è: [0:NumBox, 1:TypeBox, 2:GuestsWrapper, 3:BtnRemove]

                    try {
                        VBox typeBox = (VBox) row.getChildren().get(1);
                        ComboBox<String> combo = (ComboBox<String>) typeBox.getChildren().get(1);

                        String type = combo.getValue();

                        // Somma Costo Camera (Prezzo * Notti)
                        totalRoomCost += (getPriceForRoomType(type) * nights);

                        // Somma Ospiti (per calcolo trattamento)
                        totalGuests += getGuestsCountForType(type);

                    } catch (Exception e) {
                        System.err.println("Errore nel parsing della riga camera: " + e.getMessage());
                    }
                }
            }
        }

        // CALCOLO TRATTAMENTO
        // Formula: PrezzoTrattamento * NumeroPersone * Notti
        double treatmentPricePerPerson = 0.0;
        if (treatmentCombo != null && treatmentCombo.getValue() != null) {
            treatmentPricePerPerson = getPriceForTreatment(treatmentCombo.getValue());
        }

        double totalTreatmentCost = treatmentPricePerPerson * totalGuests * nights;

        // CALCOLO SERVIZI EXTRA
        double totalServicesCost = 0.0;
        if (serviceItems != null) {
            for (ServiceItem item : serviceItems) {
                totalServicesCost += item.getTotalPrice(); // item.price * item.quantity
            }
        }

        // TOTALE GENERALE
        double grandTotal = totalRoomCost + totalTreatmentCost + totalServicesCost;

        // AGGIORNAMENTO UI
        if (lblRoomTotal != null)
            lblRoomTotal.setText(String.format("%.2f €", totalRoomCost));

        if (lblTreatmentTotal != null)
            lblTreatmentTotal.setText(String.format("%.2f €", totalTreatmentCost));

        if (lblServiceTotal != null)
            lblServiceTotal.setText(String.format("%.2f €", totalServicesCost));

        if (lblGrandTotal != null)
            lblGrandTotal.setText(String.format("%,.2f €", grandTotal));
    }


    private double getPriceForRoomType(String type) {
        if (type == null) return 0.0;
        switch (type) {
            case "SINGOLA": return 80.00;
            case "DOPPIA / MATR.": return 120.00;
            case "TRIPLA": return 150.00;
            case "QUADRUPLA": return 190.00;
            default: return 100.00;
        }
    }

    private int getGuestsCountForType(String type) {
        if (type == null) return 1;
        String t = type.toUpperCase();
        if (t.contains("SINGOLA")) return 1;
        if (t.contains("DOPPIA") || t.contains("MATR")) return 2;
        if (t.contains("TRIPLA")) return 3;
        if (t.contains("QUADRUPLA")) return 4;
        if (t.contains("SUITE")) return 2;
        return 1;
    }

    /**
     * Restituisce il supplemento giornaliero A PERSONA per il trattamento scelto.
     */
    private double getPriceForTreatment(String treatmentName) {
        if (treatmentName == null) return 0.0;

        for (Trattamento t : MainApp3.trattamenti)
            if (treatmentName.equalsIgnoreCase(t.getNome()))
                return t.getPrezzo();
        throw new RuntimeException("Treatmento nome '" + treatmentName + "' invalido");
    }

    private void selectClient(Cliente client) {
        if (selectedClients.contains(client)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cliente già selezionato!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        selectedClients.add(client);
        updateSelectedClients();
    }

    private void updateSelectedClients() {
        selectedClientsPane.getChildren().clear();

        if (selectedClients.isEmpty()) {
            Label placeholder = new Label("Nessun cliente selezionato. Seleziona clienti dal Tab 1.");
            placeholder.getStyleClass().add("clients-placeholder");
            selectedClientsPane.getChildren().add(placeholder);
        } else {
            for (Cliente client : selectedClients) {
                selectedClientsPane.getChildren().add(createClientTag(client));
            }
        }
    }

    private HBox createClientTag(@UnknownNullability Cliente client) {
        HBox tag = new HBox(8);
        tag.setAlignment(Pos.CENTER_LEFT);
        tag.setPadding(new Insets(8, 14, 8, 14));
        tag.getStyleClass().add("client-tag");

        Label name = new Label("✓ " + client.getNome()+" "+client.getCognome());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: 600;");

        Button removeBtn = new Button("✕");
        removeBtn.getStyleClass().add("client-tag-remove");
        removeBtn.setOnAction(e -> {
            selectedClients.remove(client);
            updateSelectedClients();
        });

        tag.getChildren().addAll(name, removeBtn);
        return tag;
    }

    private void openClientDialog(@UnknownNullability Cliente client) {
        // ... [CODICE DIALOG RIMANE UGUALE] ...
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(client == null ? "Nuovo Cliente" : "Dettaglio Cliente");
        VBox dialogContent = new VBox();
        dialogContent.getStyleClass().add("client-dialog");
        // ... (resto della logica dialog che avevi già)


        Label title = new Label("Dettagli Cliente " + (client != null ? client.getCognome(): "Nuovo"));
        dialogContent.getChildren().add(title);
        javafx.scene.Scene dialogScene = new javafx.scene.Scene(dialogContent, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private VBox createInfoBox(String label, String value) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(12, 15, 12, 15));
        box.getStyleClass().add("client-info-box");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("client-info-box-label");
        Label val = new Label(value);
        val.getStyleClass().add("client-info-box-value");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    // === MOCK DATA GENERATION ===

    private List<ServiceItem> createServices() {

        Map<String, ServiceItem> serviceMap = new HashMap<>();

        for (Servizio servizio : MainApp3.servizi) {
            String nome = servizio.getNome();

            if (serviceMap.containsKey(nome)) {
                // Servizio già presente -> incrementa quantity
                ServiceItem existing = serviceMap.get(nome);
                existing.quantity++;
            } else {
                // Primo incontro -> crea nuovo ServiceItem
                ServiceItem item = new ServiceItem(
                        servizio.getNome(),
                        servizio.getPrezzo(),
                        getEmojiForService(servizio.getNome())  // Genera emoji in base al nome
                );
                item.quantity = 1;  // Prima occorrenza
                serviceMap.put(nome, item);
            }
        }

        //  Converti la Map in List e ritorna
        return new ArrayList<>(serviceMap.values());
    }

    /**
     * Assegna un emoji appropriato in base al nome del servizio
     */
    private String getEmojiForService(String serviceName) {
        if (serviceName == null) return "✨";

        String name = serviceName.toLowerCase();

        // Wellness & Relax
        if (name.contains("spa") || name.contains("wellness") || name.contains("massaggi") || name.contains("sauna")) {
            return "🧖";
        }

        // Trasporti
        if (name.contains("parcheggio") || name.contains("parking") || name.contains("garage")) {
            return "🚗";
        }
        if (name.contains("navetta") || name.contains("transfer") || name.contains("shuttle")) {
            return "🚐";
        }
        if (name.contains("taxi")) {
            return "🚕";
        }

        // Food & Beverage
        if (name.contains("colazione") || name.contains("breakfast")) {
            return "🥐";
        }
        if (name.contains("champagne") || name.contains("prosecco") || name.contains("spumante")) {
            return "🍾";
        }
        if (name.contains("vino") || name.contains("wine")) {
            return "🍷";
        }
        if (name.contains("cena") || name.contains("dinner") || name.contains("pranzo") || name.contains("lunch")) {
            return "🍽️";
        }

        // Servizi Hotel
        if (name.contains("tour") || name.contains("guida") || name.contains("escursion")) {
            return "🗺️";
        }
        if (name.contains("checkout") || name.contains("late") || name.contains("early")) {
            return "⏰";
        }
        if (name.contains("wifi") || name.contains("internet")) {
            return "📶";
        }
        if (name.contains("lavanderia") || name.contains("laundry")) {
            return "👔";
        }
        if (name.contains("animali") || name.contains("pet") || name.contains("dog") || name.contains("cat")) {
            return "🐕";
        }
        if (name.contains("baby") || name.contains("culla") || name.contains("passeggino")) {
            return "👶";
        }
        if (name.contains("bicicletta") || name.contains("bike")) {
            return "🚴";
        }
        if (name.contains("palestra") || name.contains("gym") || name.contains("fitness")) {
            return "💪";
        }

        // Default
        return "✨";
    }


    // Elemento grafico rappresentante i servizi
    public static class ServiceItem {
        String name;
        double unitPrice;
        String emoji;
        int quantity;

        public ServiceItem(String name, double unitPrice, String emoji) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.emoji = emoji;
            this.quantity = 0;
        }

        public double getTotalPrice() {
            return unitPrice * quantity;
        }
    }
}