package it.unisa.GUI.components;

import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * ContoEconomico - Vista del Conto Economico per il Manager
 *
 * @author Team Hotel Colossus
 * @version 1.0
 */
public class ContoEconomico extends VBox {

    // ===== COMPONENTI =====
    private ComboBox<String> comboPeriodo;
    private Label lblRicaviTotali, lblPassivitaTotali, lblUtileNetto, lblTotaleFinale;
    private VBox cardRicavi, cardPassivita, cardUtile;
    private Button btnTabella, btnWaterfall, btnExportPDF;
    private VBox tabellaView, waterfallView;
    private TreeTableView<VoceEconomica> treeRicavi, treePassivita;
    private HBox waterfallChart;
    private StackPane contentSwitcher;

    // ===== DATI =====
    private double ricaviTotali = 7450.50;
    private double passivitaTotali = 7340.75;
    private double utileNetto = 109.75;

    // ===== COSTRUTTORE =====

    /**
     * Costruttore
     */
    public ContoEconomico() {
        initializeComponents();
        setupLayout();
        setupStyling();
        loadData();
        setupEventHandlers();
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        // Header
        this.getChildren().add(createHeader());

        // Summary Cards
        this.getChildren().add(createSummaryCards());

        // Toggle Buttons
        this.getChildren().add(createToggleButtons());

        // Content Switcher
        contentSwitcher = new StackPane();

        tabellaView = createTabellaView();
        waterfallView = createWaterfallView();
        waterfallView.setVisible(false);
        waterfallView.setManaged(false);

        contentSwitcher.getChildren().addAll(tabellaView, waterfallView);
        this.getChildren().add(contentSwitcher);
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(25);
        this.setPadding(new Insets(30));
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().addAll("content-area", "conto-economico-container");
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
        VBox card = new VBox(10);
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

        Label totalLabel = new Label("€ 7.450,50");
        totalLabel.getStyleClass().add("conto-economico-section-title");

        header.getChildren().addAll(titleLabel, spacer, totalLabel);

        treeRicavi = createRicaviTree();

        section.getChildren().addAll(header, treeRicavi);

        return section;
    }

    private TreeTableView<VoceEconomica> createRicaviTree() {
        TreeTableView<VoceEconomica> tree = new TreeTableView<>();
        tree.setShowRoot(false);
        tree.setPrefHeight(400);
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

        Label totalLabel = new Label("€ 7.340,75");
        totalLabel.getStyleClass().add("conto-economico-section-title");

        header.getChildren().addAll(titleLabel, spacer, totalLabel);

        treePassivita = createPassivitaTree();

        section.getChildren().addAll(header, treePassivita);

        return section;
    }

    private TreeTableView<VoceEconomica> createPassivitaTree() {
        TreeTableView<VoceEconomica> tree = new TreeTableView<>();
        tree.setShowRoot(false);
        tree.setPrefHeight(200);
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
        VBox view = new VBox(25);

        VBox container = new VBox();
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
        view.getChildren().add(container);

        return view;
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

        infoBox.getChildren().addAll(
                createInfoItem("📊 Ricavi Totali:", "€ 7.450,50", "conto-economico-info-value-positivo"),
                createInfoItem("📉 Passività Totali:", "€ 7.340,75", "conto-economico-info-value-negativo"),
                createInfoItem("💰 Utile Netto:", "€ 109,75", "conto-economico-info-value-utile"),
                createInfoItem("📈 Margine:", "1,47%", "conto-economico-info-value")
        );

        return infoBox;
    }

    private VBox createInfoItem(String labelText, String valueText, String valueCssClass) {
        VBox item = new VBox(8);
        item.setAlignment(Pos.TOP_LEFT);
        item.getStyleClass().add("conto-economico-info-item");
        HBox.setHgrow(item, Priority.ALWAYS);

        Label label = new Label(labelText);
        label.getStyleClass().add("conto-economico-info-label");

        Label value = new Label(valueText);
        value.getStyleClass().addAll("conto-economico-info-value", valueCssClass);

        item.getChildren().addAll(label, value);

        return item;
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
        lblRicaviTotali.setText(String.format("€ %.2f", ricaviTotali));
        lblPassivitaTotali.setText(String.format("€ %.2f", passivitaTotali));
        lblUtileNetto.setText(String.format("€ %.2f", utileNetto));
        lblTotaleFinale.setText(String.format("€ %.2f", utileNetto));

        loadRicaviData();
        loadPassivitaData();
        loadWaterfallChart();
    }

    private void loadRicaviData() {
        TreeItem<VoceEconomica> root = new TreeItem<>(new VoceEconomica("Root", ""));

        TreeItem<VoceEconomica> prenotazioni = new TreeItem<>(new VoceEconomica("PRENOTAZIONI", "€ 7.450,50"));
        prenotazioni.setExpanded(true);

        TreeItem<VoceEconomica> pren1 = new TreeItem<>(new VoceEconomica("  PRENOTAZIONE 1 - Luca Marole", "€ 400,50"));

        TreeItem<VoceEconomica> camere1 = new TreeItem<>(new VoceEconomica("    CAMERE", "€ 50,50"));
        camere1.getChildren().add(new TreeItem<>(new VoceEconomica("      Camera 112", "€ 50,50")));

        TreeItem<VoceEconomica> servizi1 = new TreeItem<>(new VoceEconomica("    SERVIZI", "€ 325,00"));
        servizi1.getChildren().add(new TreeItem<>(new VoceEconomica("      Spa", "€ 325,00")));

        TreeItem<VoceEconomica> trattamento1 = new TreeItem<>(new VoceEconomica("    MEZZA PENSIONE", "€ 24,00"));

        pren1.getChildren().addAll(camere1, servizi1, trattamento1);

        TreeItem<VoceEconomica> pren2 = new TreeItem<>(new VoceEconomica("  PRENOTAZIONE 2 - Mario Rossi", "€ 2.850,00"));
        TreeItem<VoceEconomica> pren3 = new TreeItem<>(new VoceEconomica("  PRENOTAZIONE 3 - Barbara d'Orso", "€ 1.200,00"));
        TreeItem<VoceEconomica> pren4 = new TreeItem<>(new VoceEconomica("  PRENOTAZIONE 4 - Giorgio Ventura", "€ 3.000,00"));

        prenotazioni.getChildren().addAll(pren1, pren2, pren3, pren4);
        root.getChildren().add(prenotazioni);

        treeRicavi.setRoot(root);
    }

    private void loadPassivitaData() {
        TreeItem<VoceEconomica> root = new TreeItem<>(new VoceEconomica("Root", ""));

        TreeItem<VoceEconomica> stipendi = new TreeItem<>(new VoceEconomica("Stipendi Personale", "€ 6.800,75"));
        stipendi.getChildren().addAll(
                new TreeItem<>(new VoceEconomica("  Mario Rossi (Front Desk)", "€ 4.000,00")),
                new TreeItem<>(new VoceEconomica("  Laura Bianchi (Front Desk)", "€ 2.800,75"))
        );

        TreeItem<VoceEconomica> manutenzione = new TreeItem<>(new VoceEconomica("Manutenzione Camere", "€ 540,00"));

        root.getChildren().addAll(stipendi, manutenzione);

        treePassivita.setRoot(root);
    }

    private void loadWaterfallChart() {
        waterfallChart.getChildren().clear();

        waterfallChart.getChildren().addAll(
                createWaterfallBar("Camere", 1100, 15, "conto-economico-bar-ricavi", false),
                createWaterfallBar("Servizi", 3350, 45, "conto-economico-bar-ricavi", false),
                createWaterfallBar("Trattamenti", 3000, 40, "conto-economico-bar-ricavi", false),
                createWaterfallBar("RICAVI\nTOTALI", 7450, 100, "conto-economico-bar-totale-ricavi", true),
                createWaterfallBar("Stipendi", 6800, 91, "conto-economico-bar-passivita", false),
                createWaterfallBar("Manut.", 540, 7, "conto-economico-bar-passivita", false),
                createWaterfallBar("PASSIVITÀ\nTOTALI", 7340, 98, "conto-economico-bar-totale-passivita", true),
                createWaterfallBar("UTILE\nNETTO", 109.75, 2, "conto-economico-bar-utile-finale", true)
        );
    }

    // ===== EVENT HANDLERS =====

    private void setupEventHandlers() {
        btnTabella.setOnAction(e -> showTabellaView());
        btnWaterfall.setOnAction(e -> showWaterfallView());
        btnExportPDF.setOnAction(e -> exportToPDF());
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

        animateWaterfallBars();

        System.out.println("📈 Vista Waterfall attivata");
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

    private void exportToPDF() {
        System.out.println("📄 Esportazione PDF...");
        // TODO: Implementare esportazione
    }

    // ===== METODI PUBBLICI =====

    /**
     * Imposta i dati del conto economico
     *
     * @param ricavi ricavi totali
     * @param passivita passività totali
     */
    public void setDatiConto(double ricavi, double passivita) {
        this.ricaviTotali = ricavi;
        this.passivitaTotali = passivita;
        this.utileNetto = ricavi - passivita;
        loadData();
    }

    // ===== CLASSE INTERNA =====

    /**
     * Classe interna VoceEconomica
     */
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