package it.unisa.Client.GUI.components;



import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.*;

/**
 * ContoEconomico - Vista del Conto Economico per il Manager
 */
public class ContoEconomico extends ScrollPane {

    // ===== SPESE FISSE MENSILI (HARDCODED) =====
    private static final double SPESA_STIPENDI = 6800.75;
    private static final double SPESA_MANUTENZIONE = 540.00;
    private static final double SPESA_UTENZE = 300.00;
    private static final double SPESE_FISSE_TOTALI = SPESA_STIPENDI + SPESA_MANUTENZIONE + SPESA_UTENZE;

    // ===== COMPONENTI =====
    private VBox contentContainer;
    private ComboBox<String> comboPeriodo;
    private Label lblRicaviTotali, lblPassivitaTotali, lblUtileNetto, lblTotaleFinale;
    private VBox cardRicavi, cardPassivita, cardUtile;
    private Button btnTabella, btnWaterfall, btnExportPDF;
    private VBox tabellaView, waterfallView;
    private TreeTableView<VoceEconomica> treeRicavi, treePassivita;
    private HBox waterfallChart;
    private StackPane contentSwitcher;
    private Label lblInfoRicavi, lblInfoPassivita, lblInfoUtile, lblInfoMargine;

    // ===== DATI =====
    private List<RicevutaFiscale> ricevuteDalDatabase;
    private Map<String, DatiPeriodo> datiPerPeriodo;
    private DatiPeriodo datiCorrente;
    /**
     * Classe per contenere i dati di un periodo
     */


    // ===== COSTRUTTORE =====
    public ContoEconomico() {
        caricaDatiMock();
        preparaDatiPerPeriodi();
        initializeComponents();
        setupLayout();
        setupStyling();
        loadData();
        setupEventHandlers();
    }

    /**
     * ✅ MOCK - Simula ricevute dal database
     */
    private void caricaDatiMock() {
        ricevuteDalDatabase = new ArrayList<>();

        ricevuteDalDatabase.add(new RicevutaFiscale(
                1, "Luca Marole",
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2026, 1, 18),
                50.50, 325.00, 24.00
        ));

        ricevuteDalDatabase.add(new RicevutaFiscale(
                2, "Mario Rossi",
                LocalDate.of(2026, 1, 20),
                LocalDate.of(2026, 1, 25),
                1500.00, 1200.00, 150.00
        ));

        ricevuteDalDatabase.add(new RicevutaFiscale(
                3, "Barbara d'Orso",
                LocalDate.of(2026, 1, 28),
                LocalDate.of(2026, 2, 2),
                1200.00, 0, 0
        ));

        ricevuteDalDatabase.add(new RicevutaFiscale(
                4, "Giorgio Ventura",
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 14),
                3000.00, 0, 0
        ));

        ricevuteDalDatabase.add(new RicevutaFiscale(
                5, "Anna Verdi",
                LocalDate.of(2026, 3, 5),
                LocalDate.of(2026, 3, 12),
                3000.00, 2000.00, 200.00
        ));
    }

    /**
     * ✅ Metodo pubblico per ricevere ricevute dal server
     */
    public void setRicevuteDalServer(List<RicevutaFiscale> ricevute) {
        this.ricevuteDalDatabase = ricevute;
        preparaDatiPerPeriodi();
        loadData();
    }

    /**
     * ✅ Pre-calcola dati per tutti i periodi
     */
    private void preparaDatiPerPeriodi() {
        datiPerPeriodo = new HashMap<>();

        String[] periodi = {"Gennaio 2026", "Febbraio 2026", "Marzo 2026", "Aprile 2026",
                "Maggio 2026", "Giugno 2026", "Q1 2026", "Anno 2026"};

        for (String periodo : periodi) {
            calcolaDatiPeriodo(periodo);
        }
    }

    /**
     * ✅ Calcola dati per un periodo
     */
    private void calcolaDatiPeriodo(String periodo) {
        List<RicevutaFiscale> ricevutePeriodo = filtraRicevutePerPeriodo(periodo);

        if (ricevutePeriodo.isEmpty()) {
            return;
        }

        double totaleCamere = 0;
        double totaleServizi = 0;
        double totaleTrattamenti = 0;

        for (RicevutaFiscale ricevuta : ricevutePeriodo) {
            totaleCamere += ricevuta.getCostoCamera();
            totaleServizi += ricevuta.getCostoServizi();
            totaleTrattamenti += ricevuta.getCostoTrattamento();
        }

        double ricavi = totaleCamere + totaleServizi + totaleTrattamenti;
        double passivita = SPESE_FISSE_TOTALI;

        TreeItem<VoceEconomica> alberoRicavi = buildRicaviTree(ricevutePeriodo);
        TreeItem<VoceEconomica> alberoPassivita = buildPassivitaTreeFisse();

        // ✅ PASSA I VALORI AL COSTRUTTORE
        datiPerPeriodo.put(periodo, new DatiPeriodo(
                ricavi, passivita, alberoRicavi, alberoPassivita,
                totaleCamere, totaleServizi, totaleTrattamenti
        ));
    }


    /**
     * ✅ Filtra ricevute per periodo
     */
    private List<RicevutaFiscale> filtraRicevutePerPeriodo(String periodo) {
        List<RicevutaFiscale> risultato = new ArrayList<>();

        String[] parti = periodo.split(" ");
        String nomeMese = parti[0];
        int anno = Integer.parseInt(parti[1]);
        int mese = getMeseNumero(nomeMese);

        for (RicevutaFiscale ricevuta : ricevuteDalDatabase) {
            LocalDate dataInizio = ricevuta.getDataInizio();

            if (dataInizio.getYear() == anno && dataInizio.getMonthValue() == mese) {
                risultato.add(ricevuta);
            }
        }

        return risultato;
    }

    /**
     * ✅ Costruisce albero ricavi da ricevute
     */
    private TreeItem<VoceEconomica> buildRicaviTree(List<RicevutaFiscale> ricevute) {
        TreeItem<VoceEconomica> root = new TreeItem<>(new VoceEconomica("Root", ""));
        root.setExpanded(true);

        double totaleRicavi = 0;
        for (RicevutaFiscale r : ricevute) {
            totaleRicavi += r.getTotale();
        }

        TreeItem<VoceEconomica> prenotazioniNode = new TreeItem<>(
                new VoceEconomica("PRENOTAZIONI", String.format("€ %.2f", totaleRicavi))
        );
        prenotazioniNode.setExpanded(true);

        for (RicevutaFiscale ricevuta : ricevute) {
            TreeItem<VoceEconomica> ricevutaNode = new TreeItem<>(
                    new VoceEconomica(
                            String.format("  PRENOTAZIONE %d - %s", ricevuta.getId(), ricevuta.getNomeCliente()),
                            String.format("€ %.2f", ricevuta.getTotale())
                    )
            );
            ricevutaNode.setExpanded(true);

            TreeItem<VoceEconomica> camereNode = new TreeItem<>(
                    new VoceEconomica("    CAMERE", String.format("€ %.2f", ricevuta.getCostoCamera()))
            );

            TreeItem<VoceEconomica> serviziNode = new TreeItem<>(
                    new VoceEconomica("    SERVIZI", String.format("€ %.2f", ricevuta.getCostoServizi()))
            );

            TreeItem<VoceEconomica> trattamentoNode = new TreeItem<>(
                    new VoceEconomica("    TRATTAMENTO", String.format("€ %.2f", ricevuta.getCostoTrattamento()))
            );

            ricevutaNode.getChildren().addAll(camereNode, serviziNode, trattamentoNode);
            prenotazioniNode.getChildren().add(ricevutaNode);
        }

        root.getChildren().add(prenotazioniNode);
        return root;
    }

    /**
     * ✅ Costruisce albero passività FISSE
     */
    private TreeItem<VoceEconomica> buildPassivitaTreeFisse() {
        TreeItem<VoceEconomica> root = new TreeItem<>(new VoceEconomica("Root", ""));
        root.setExpanded(true);

        TreeItem<VoceEconomica> stipendiNode = new TreeItem<>(
                new VoceEconomica("Stipendi Personale", String.format("€ %.2f", SPESA_STIPENDI))
        );
        stipendiNode.setExpanded(true);
        stipendiNode.getChildren().addAll(
                new TreeItem<>(new VoceEconomica("  Mario Rossi (Front Desk)", "€ 4.000,00")),
                new TreeItem<>(new VoceEconomica("  Laura Bianchi (Front Desk)", "€ 2.800,75"))
        );

        TreeItem<VoceEconomica> manutenzioneNode = new TreeItem<>(
                new VoceEconomica("Manutenzione Camere", String.format("€ %.2f", SPESA_MANUTENZIONE))
        );

        TreeItem<VoceEconomica> utenzeNode = new TreeItem<>(
                new VoceEconomica("Utenze (Luce, Gas, Acqua)", String.format("€ %.2f", SPESA_UTENZE))
        );

        root.getChildren().addAll(stipendiNode, manutenzioneNode, utenzeNode);

        return root;
    }

    /**
     * Converte nome mese in numero
     */
    private int getMeseNumero(String mese) {
        switch (mese.toLowerCase()) {
            case "gennaio":
                return 1;
            case "febbraio":
                return 2;
            case "marzo":
                return 3;
            case "aprile":
                return 4;
            case "maggio":
                return 5;
            case "giugno":
                return 6;
            case "luglio":
                return 7;
            case "agosto":
                return 8;
            case "settembre":
                return 9;
            case "ottobre":
                return 10;
            case "novembre":
                return 11;
            case "dicembre":
                return 12;
            default:
                return 1;
        }
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(30));
        contentContainer.getStyleClass().add("conto-economico-container");

        contentContainer.getChildren().add(createHeader());
        contentContainer.getChildren().add(createSummaryCards());
        contentContainer.getChildren().add(createToggleButtons());

        contentSwitcher = new StackPane();

        tabellaView = createTabellaView();
        waterfallView = createWaterfallView();
        waterfallView.setVisible(false);
        waterfallView.setManaged(false);

        contentSwitcher.getChildren().addAll(tabellaView, waterfallView);
        contentContainer.getChildren().add(contentSwitcher);
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setContent(contentContainer);
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.getStyleClass().addAll("bookings-scroll", "content-area");
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        // Già gestito
    }

    // ===== CREAZIONE COMPONENTI =====

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("conto-economico-header");

        Label title = new Label("Conto Economico");
        title.getStyleClass().add("conto-economico-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox controls = createHeaderControls();

        header.getChildren().addAll(title, spacer, controls);

        return header;
    }

    private HBox createHeaderControls() {
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Label lblPeriodo = new Label("Periodo:");

        comboPeriodo = new ComboBox<>();
        comboPeriodo.getItems().addAll(
                "Gennaio 2026", "Febbraio 2026", "Marzo 2026", "Q1 2026", "Anno 2026"
        );
        comboPeriodo.setValue("Gennaio 2026");
        comboPeriodo.getStyleClass().add("conto-economico-combo");

        btnExportPDF = new Button("📄 Esporta PDF");
        btnExportPDF.getStyleClass().add("conto-economico-btn-export");

        controls.getChildren().addAll(lblPeriodo, comboPeriodo, btnExportPDF);

        return controls;
    }

    private HBox createSummaryCards() {
        HBox cards = new HBox(20);

        cardRicavi = createSummaryCard("Ricavi Totali", lblRicaviTotali = new Label(),
                "conto-economico-card-ricavi");

        cardPassivita = createSummaryCard("Passività Totali", lblPassivitaTotali = new Label(),
                "conto-economico-card-passivita");

        cardUtile = createSummaryCard("Utile Netto", lblUtileNetto = new Label(),
                "conto-economico-card-utile");

        HBox.setHgrow(cardRicavi, Priority.ALWAYS);
        HBox.setHgrow(cardPassivita, Priority.ALWAYS);
        HBox.setHgrow(cardUtile, Priority.ALWAYS);

        cards.getChildren().addAll(cardRicavi, cardPassivita, cardUtile);

        return cards;
    }

    private VBox createSummaryCard(String title, Label valueLabel, String cssClass) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().addAll("conto-economico-card", cssClass);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("conto-economico-card-title");

        valueLabel.getStyleClass().add("conto-economico-card-value");

        card.getChildren().addAll(titleLabel, valueLabel);

        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return card;
    }

    private HBox createToggleButtons() {
        HBox toggleBox = new HBox(10);
        toggleBox.setAlignment(Pos.CENTER);

        btnTabella = new Button("📊 Tabella");
        btnTabella.getStyleClass().add("conto-economico-toggle-active");

        btnWaterfall = new Button("📈 Waterfall");
        btnWaterfall.getStyleClass().add("conto-economico-toggle-inactive");

        toggleBox.getChildren().addAll(btnTabella, btnWaterfall);

        return toggleBox;
    }

    private VBox createTabellaView() {
        VBox view = new VBox(20);

        view.getChildren().add(createRicaviSection());
        view.getChildren().add(createPassivitaSection());
        view.getChildren().add(createTotaleFinale());
        view.getChildren().add(createLegend());

        return view;
    }

    private VBox createRicaviSection() {
        VBox section = new VBox();
        section.getStyleClass().add("conto-economico-section");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("conto-economico-section-header-ricavi");

        Label titleLabel = new Label("RICAVI");
        titleLabel.getStyleClass().add("conto-economico-section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label totalLabel = new Label("€ 0,00");
        totalLabel.getStyleClass().add("conto-economico-section-title");

        header.getChildren().addAll(titleLabel, spacer, totalLabel);

        treeRicavi = createRicaviTree();

        section.getChildren().addAll(header, treeRicavi);

        return section;
    }

    private TreeTableView<VoceEconomica> createRicaviTree() {
        TreeTableView<VoceEconomica> tree = new TreeTableView<>();
        tree.setShowRoot(false);
        tree.setMinHeight(400);
        tree.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tree.setMaxHeight(Double.MAX_VALUE);
        tree.getStyleClass().add("conto-economico-tree");

        TreeTableColumn<VoceEconomica, String> colDescrizione = new TreeTableColumn<>("Descrizione");
        colDescrizione.setCellValueFactory(param -> param.getValue().getValue().descrizioneProperty());
        colDescrizione.setPrefWidth(600);

        TreeTableColumn<VoceEconomica, String> colImporto = new TreeTableColumn<>("Importo");
        colImporto.setCellValueFactory(param -> param.getValue().getValue().importoProperty());
        colImporto.setPrefWidth(200);

        colImporto.setCellFactory(column -> new TreeTableCell<VoceEconomica, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item);
                    getStyleClass().clear();
                    getStyleClass().add("conto-economico-importo-positivo");
                }
            }
        });

        tree.getColumns().addAll(colDescrizione, colImporto);

        return tree;
    }

    private VBox createPassivitaSection() {
        VBox section = new VBox();
        section.getStyleClass().add("conto-economico-section");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("conto-economico-section-header-passivita");

        Label titleLabel = new Label("PASSIVITÀ");
        titleLabel.getStyleClass().add("conto-economico-section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label totalLabel = new Label("€ 0,00");
        totalLabel.getStyleClass().add("conto-economico-section-title");

        header.getChildren().addAll(titleLabel, spacer, totalLabel);

        treePassivita = createPassivitaTree();

        section.getChildren().addAll(header, treePassivita);

        return section;
    }

    private TreeTableView<VoceEconomica> createPassivitaTree() {
        TreeTableView<VoceEconomica> tree = new TreeTableView<>();
        tree.setShowRoot(false);
        tree.setMinHeight(200);
        tree.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tree.setMaxHeight(Double.MAX_VALUE);
        tree.getStyleClass().add("conto-economico-tree");

        TreeTableColumn<VoceEconomica, String> colDescrizione = new TreeTableColumn<>("Descrizione");
        colDescrizione.setCellValueFactory(param -> param.getValue().getValue().descrizioneProperty());
        colDescrizione.setPrefWidth(600);

        TreeTableColumn<VoceEconomica, String> colImporto = new TreeTableColumn<>("Importo");
        colImporto.setCellValueFactory(param -> param.getValue().getValue().importoProperty());
        colImporto.setPrefWidth(200);

        colImporto.setCellFactory(column -> new TreeTableCell<VoceEconomica, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item);
                    getStyleClass().clear();
                    getStyleClass().add("conto-economico-importo-negativo");
                }
            }
        });

        tree.getColumns().addAll(colDescrizione, colImporto);

        return tree;
    }

    private VBox createTotaleFinale() {
        VBox section = new VBox();
        section.getStyleClass().add("conto-economico-section");

        HBox totale = new HBox();
        totale.setAlignment(Pos.CENTER_LEFT);
        totale.getStyleClass().add("conto-economico-totale-finale");

        Label titleLabel = new Label("UTILE/PERDITA NETTO");
        titleLabel.getStyleClass().add("conto-economico-totale-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lblTotaleFinale = new Label();
        lblTotaleFinale.getStyleClass().add("conto-economico-totale-value");

        totale.getChildren().addAll(titleLabel, spacer, lblTotaleFinale);
        section.getChildren().add(totale);

        return section;
    }

    private HBox createLegend() {
        HBox legend = new HBox(30);
        legend.setAlignment(Pos.CENTER);
        legend.getStyleClass().add("conto-economico-legend");

        legend.getChildren().addAll(
                createLegendItem("conto-economico-legend-box-ricavi", "Ricavi (Entrate)"),
                createLegendItem("conto-economico-legend-box-passivita", "Passività (Uscite)"),
                createLegendItem("conto-economico-legend-box-utile", "Utile Netto")
        );

        return legend;
    }

    private HBox createLegendItem(String boxCssClass, String text) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER);

        StackPane colorBox = new StackPane();
        colorBox.setMinSize(30, 20);
        colorBox.setMaxSize(30, 20);
        colorBox.getStyleClass().addAll("conto-economico-legend-box", boxCssClass);

        Label label = new Label(text);

        item.getChildren().addAll(colorBox, label);

        return item;
    }

    private VBox createWaterfallView() {
        VBox container = new VBox(25);
        container.getStyleClass().add("conto-economico-waterfall-container");

        Label title = new Label("Andamento Conto Economico");
        title.getStyleClass().add("conto-economico-waterfall-title");

        Label subtitle = new Label("Visualizzazione progressiva da ricavi a utile netto");
        subtitle.getStyleClass().add("conto-economico-waterfall-subtitle");
        VBox.setMargin(subtitle, new Insets(0, 0, 30, 0));

        waterfallChart = new HBox(15);
        waterfallChart.setAlignment(Pos.BOTTOM_CENTER);
        waterfallChart.setMinHeight(500);
        waterfallChart.setMaxHeight(500);
        waterfallChart.getStyleClass().add("conto-economico-waterfall-chart");

        HBox waterfallLegend = createWaterfallLegend();
        HBox infoBox = createInfoBox();

        container.getChildren().addAll(title, subtitle, waterfallChart, waterfallLegend, infoBox);

        return container;
    }

    private HBox createWaterfallLegend() {
        HBox legend = new HBox(30);
        legend.setAlignment(Pos.CENTER);
        legend.getStyleClass().add("conto-economico-legend");

        legend.getChildren().addAll(
                createLegendItem("conto-economico-legend-box-ricavi", "Ricavi (Entrate)"),
                createLegendItem("conto-economico-legend-box-passivita", "Passività (Uscite)"),
                createLegendItem("conto-economico-legend-box-utile", "Totali")
        );

        return legend;
    }

    private HBox createInfoBox() {
        HBox infoBox = new HBox(15);
        infoBox.getStyleClass().add("conto-economico-info-box");

        // ✅ Inizializza le label
        lblInfoRicavi = new Label("€ 0,00");
        lblInfoPassivita = new Label("€ 0,00");
        lblInfoUtile = new Label("€ 0,00");
        lblInfoMargine = new Label("0%");

        // ✅ Crea gli item
        VBox item1 = new VBox(8);
        item1.setAlignment(Pos.TOP_LEFT);
        item1.getStyleClass().add("conto-economico-info-item");
        HBox.setHgrow(item1, Priority.ALWAYS);
        Label label1 = new Label("📊 Ricavi Totali:");
        label1.getStyleClass().add("conto-economico-info-label");
        lblInfoRicavi.getStyleClass().addAll("conto-economico-info-value", "conto-economico-info-value-positivo");
        item1.getChildren().addAll(label1, lblInfoRicavi);

        VBox item2 = new VBox(8);
        item2.setAlignment(Pos.TOP_LEFT);
        item2.getStyleClass().add("conto-economico-info-item");
        HBox.setHgrow(item2, Priority.ALWAYS);
        Label label2 = new Label("📉 Passività Totali:");
        label2.getStyleClass().add("conto-economico-info-label");
        lblInfoPassivita.getStyleClass().addAll("conto-economico-info-value", "conto-economico-info-value-negativo");
        item2.getChildren().addAll(label2, lblInfoPassivita);

        VBox item3 = new VBox(8);
        item3.setAlignment(Pos.TOP_LEFT);
        item3.getStyleClass().add("conto-economico-info-item");
        HBox.setHgrow(item3, Priority.ALWAYS);
        Label label3 = new Label("💰 Utile Netto:");
        label3.getStyleClass().add("conto-economico-info-label");
        lblInfoUtile.getStyleClass().addAll("conto-economico-info-value", "conto-economico-info-value-utile");
        item3.getChildren().addAll(label3, lblInfoUtile);

        VBox item4 = new VBox(8);
        item4.setAlignment(Pos.TOP_LEFT);
        item4.getStyleClass().add("conto-economico-info-item");
        HBox.setHgrow(item4, Priority.ALWAYS);
        Label label4 = new Label("📈 Margine:");
        label4.getStyleClass().add("conto-economico-info-label");
        lblInfoMargine.getStyleClass().addAll("conto-economico-info-value");
        item4.getChildren().addAll(label4, lblInfoMargine);

        infoBox.getChildren().addAll(item1, item2, item3, item4);

        return infoBox;
    }


    private VBox createWaterfallBar(String label, double value, double heightPercent, String cssClass, boolean isHighlight) {
        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.BOTTOM_CENTER);
        wrapper.setPrefWidth(100);
        VBox.setVgrow(wrapper, Priority.ALWAYS);

        if (isHighlight) {
            wrapper.setStyle("-fx-background-color: rgba(102, 126, 234, 0.05); -fx-background-radius: 8;");
        }

        StackPane bar = new StackPane();
        bar.setPrefWidth(80);
        bar.setMaxWidth(80);
        bar.setMinHeight(heightPercent * 4);
        bar.getStyleClass().add(cssClass);

        Label valueLabel = new Label(String.format("€ %.0f", value));
        valueLabel.getStyleClass().add("conto-economico-bar-value");
        valueLabel.setRotate(-90);

        bar.getChildren().add(valueLabel);

        Label labelText = new Label(label);
        labelText.getStyleClass().add(isHighlight ? "conto-economico-bar-label-highlight" : "conto-economico-bar-label");
        labelText.setWrapText(true);
        labelText.setMaxWidth(80);
        labelText.setAlignment(Pos.CENTER);
        VBox.setMargin(labelText, new Insets(10, 0, 0, 0));

        wrapper.getChildren().addAll(bar, labelText);

        bar.setOnMouseEntered(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), bar);
            tt.setToY(-5);
            tt.play();
        });

        bar.setOnMouseExited(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), bar);
            tt.setToY(0);
            tt.play();
        });

        return wrapper;
    }

    // ===== CARICAMENTO DATI =====

    private void loadData() {
        String periodoIniziale = comboPeriodo.getValue();
        DatiPeriodo dati = datiPerPeriodo.get(periodoIniziale);

        if (dati != null) {
            mostraDati(dati);
        } else {
            svuotaDati();
        }
    }

    /**
     * ✅ Mostra i dati già calcolati
     */
    private void mostraDati(DatiPeriodo dati) {
        double utile = dati.ricaviTotali - dati.passivitaTotali;
        this.datiCorrente = dati;
        lblRicaviTotali.setText(String.format("€ %.2f", dati.ricaviTotali));
        lblPassivitaTotali.setText(String.format("€ %.2f", dati.passivitaTotali));
        lblUtileNetto.setText(String.format("€ %.2f", utile));
        lblTotaleFinale.setText(String.format("€ %.2f", utile));

        treeRicavi.setRoot(dati.alberoRicavi);
        treePassivita.setRoot(dati.alberoPassivita);

        loadWaterfallChart();
        aggiornaInfoBox();
    }

    /**
     * Svuota tutti i dati
     */
    private void svuotaDati() {
        lblRicaviTotali.setText("€ 0,00");
        lblPassivitaTotali.setText("€ 0,00");
        lblUtileNetto.setText("€ 0,00");
        lblTotaleFinale.setText("€ 0,00");

        TreeItem<VoceEconomica> emptyRoot = new TreeItem<>(new VoceEconomica("", ""));
        treeRicavi.setRoot(emptyRoot);
        treePassivita.setRoot(emptyRoot);

        waterfallChart.getChildren().clear();
    }

    private void loadWaterfallChart() {
        waterfallChart.getChildren().clear();

        if (datiCorrente == null) {
            return;
        }

        // Calcola percentuali per l'altezza delle barre
        double maxValue = Math.max(datiCorrente.ricaviTotali, datiCorrente.passivitaTotali);

        double percentCamere = (datiCorrente.totaleCamere / maxValue) * 100;
        double percentServizi = (datiCorrente.totaleServizi / maxValue) * 100;
        double percentTrattamenti = (datiCorrente.totaleTrattamenti / maxValue) * 100;
        double percentRicaviTotali = (datiCorrente.ricaviTotali / maxValue) * 100;

        double percentStipendi = (SPESA_STIPENDI / maxValue) * 100;
        double percentManutenzione = (SPESA_MANUTENZIONE / maxValue) * 100;
        double percentUtenze = (SPESA_UTENZE / maxValue) * 100;
        double percentPassivitaTotali = (datiCorrente.passivitaTotali / maxValue) * 100;

        double utile = datiCorrente.ricaviTotali - datiCorrente.passivitaTotali;
        double percentUtile = (Math.abs(utile) / maxValue) * 100;

        waterfallChart.getChildren().addAll(
                createWaterfallBar("Camere", datiCorrente.totaleCamere, percentCamere, "conto-economico-bar-ricavi", false),
                createWaterfallBar("Servizi", datiCorrente.totaleServizi, percentServizi, "conto-economico-bar-ricavi", false),
                createWaterfallBar("Trattamenti", datiCorrente.totaleTrattamenti, percentTrattamenti, "conto-economico-bar-ricavi", false),
                createWaterfallBar("RICAVI\nTOTALI", datiCorrente.ricaviTotali, percentRicaviTotali, "conto-economico-bar-totale-ricavi", true),
                createWaterfallBar("Stipendi", SPESA_STIPENDI, percentStipendi, "conto-economico-bar-passivita", false),
                createWaterfallBar("Manut.", SPESA_MANUTENZIONE, percentManutenzione, "conto-economico-bar-passivita", false),
                createWaterfallBar("Utenze", SPESA_UTENZE, percentUtenze, "conto-economico-bar-passivita", false),
                createWaterfallBar("PASSIVITÀ\nTOTALI", datiCorrente.passivitaTotali, percentPassivitaTotali, "conto-economico-bar-totale-passivita", true),
                createWaterfallBar("UTILE\nNETTO", utile, percentUtile, utile >= 0 ? "conto-economico-bar-utile-finale" : "conto-economico-bar-perdita-finale", true)
        );
    }

    // ===== EVENT HANDLERS =====

    private void setupEventHandlers() {
        btnTabella.setOnAction(e -> showTabellaView());
        btnWaterfall.setOnAction(e -> showWaterfallView());
        btnExportPDF.setOnAction(e -> exportToPDF());
        comboPeriodo.setOnAction(e -> onPeriodoChanged());
    }

    /**
     * ✅ Cambio periodo - semplice!
     */
    private void onPeriodoChanged() {
        String periodoSelezionato = comboPeriodo.getValue();
        System.out.println("📅 Cambio periodo: " + periodoSelezionato);

        DatiPeriodo dati = datiPerPeriodo.get(periodoSelezionato);

        if (dati == null) {
            svuotaDati();
            System.out.println("⚠️ Nessun dato per " + periodoSelezionato);
        } else {
            mostraDati(dati);
            System.out.println("✅ Dati caricati per " + periodoSelezionato);
        }
    }

    private void showTabellaView() {
        tabellaView.setVisible(true);
        tabellaView.setManaged(true);
        waterfallView.setVisible(false);
        waterfallView.setManaged(false);

        btnTabella.getStyleClass().clear();
        btnTabella.getStyleClass().add("conto-economico-toggle-active");

        btnWaterfall.getStyleClass().clear();
        btnWaterfall.getStyleClass().add("conto-economico-toggle-inactive");

        System.out.println("📊 Vista Tabella attivata");
    }

    private void showWaterfallView() {
        tabellaView.setVisible(false);
        tabellaView.setManaged(false);
        waterfallView.setVisible(true);
        waterfallView.setManaged(true);

        btnTabella.getStyleClass().clear();
        btnTabella.getStyleClass().add("conto-economico-toggle-inactive");

        btnWaterfall.getStyleClass().clear();
        btnWaterfall.getStyleClass().add("conto-economico-toggle-active");

        aggiornaInfoBox();

        animateWaterfallBars();

        System.out.println(" Vista Waterfall attivata");
    }

    private void animateWaterfallBars() {
        for (int i = 0; i < waterfallChart.getChildren().size(); i++) {
            VBox wrapper = (VBox) waterfallChart.getChildren().get(i);
            StackPane bar = (StackPane) wrapper.getChildren().get(0);

            double originalHeight = bar.getMinHeight();
            bar.setMinHeight(0);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(bar.minHeightProperty(), 0)),
                    new KeyFrame(Duration.millis(500), new KeyValue(bar.minHeightProperty(), originalHeight))
            );
            timeline.setDelay(Duration.millis(i * 150));
            timeline.play();
        }
    }

    private void aggiornaInfoBox() {
        if (datiCorrente == null) {
            return;
        }

        double utile = datiCorrente.ricaviTotali - datiCorrente.passivitaTotali;
        double margine = datiCorrente.ricaviTotali > 0 ? (utile / datiCorrente.ricaviTotali) * 100 : 0;

        lblInfoRicavi.setText(String.format("€ %.2f", datiCorrente.ricaviTotali));
        lblInfoPassivita.setText(String.format("€ %.2f", datiCorrente.passivitaTotali));
        lblInfoUtile.setText(String.format("€ %.2f", utile));
        lblInfoMargine.setText(String.format("%.2f%%", margine));
    }

    private void exportToPDF() {
        System.out.println("📄 Esportazione PDF...");
        // TODO: Implementare esportazione
    }

    // ===== CLASSE INTERNA =====

    private static class DatiPeriodo {
        double ricaviTotali;
        double passivitaTotali;
        TreeItem<VoceEconomica> alberoRicavi;
        TreeItem<VoceEconomica> alberoPassivita;
        double totaleCamere;
        double totaleServizi;
        double totaleTrattamenti;
        DatiPeriodo(double ricavi, double passivita,
                    TreeItem<VoceEconomica> alberoRicavi,
                    TreeItem<VoceEconomica> alberoPassivita, double totaleCamere, double totaleServizi, double totaleTrattamenti) {
            this.ricaviTotali = ricavi;
            this.passivitaTotali = passivita;
            this.alberoRicavi = alberoRicavi;
            this.alberoPassivita = alberoPassivita;
            this.totaleCamere = totaleCamere;
            this.totaleServizi = totaleServizi;
            this.totaleTrattamenti = totaleTrattamenti;
        }
    }


    public static class VoceEconomica {
        private final SimpleStringProperty descrizione;
        private final SimpleStringProperty importo;

        public VoceEconomica(String descrizione, String importo) {
            this.descrizione = new SimpleStringProperty(descrizione);
            this.importo = new SimpleStringProperty(importo);
        }

        public SimpleStringProperty descrizioneProperty() {
            return descrizione;
        }

        public SimpleStringProperty importoProperty() {
            return importo;
        }
    }
}