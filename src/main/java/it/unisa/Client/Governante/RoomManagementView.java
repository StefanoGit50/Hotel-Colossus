package it.unisa.Client.Governante;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.Eccezioni.IllegalAccess;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RoomManagementView extends VBox{

    private GridPane roomsGrid;
    protected List<Camera> camere = new ArrayList<>();
    protected FrontDeskClient frontDeskClient= new FrontDeskClient();
    private List<Prenotazione> plist= new  ArrayList<>();

    // ===== COSTRUTTORE =====
    public RoomManagementView() {
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        roomsGrid = new GridPane();
        roomsGrid.setHgap(19);
        roomsGrid.setVgap(19);
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        StackPane backgroundLayer = createBackground();

        VBox contentLayer = new VBox(18);
        contentLayer.setPadding(new Insets(15,30,30,30));

        // Header
        VBox header = createHeader();

        // Grid con scroll
        try{
            populateRoomsGrid();
        }catch (Exception e){
            e.printStackTrace();
        }


        ScrollPane scrollPane = new ScrollPane(roomsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("rooms-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        contentLayer.getChildren().addAll(header, scrollPane);
        //elemento per sovrapporre lo sfondo al contenuto
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundLayer, contentLayer);

        // aggiunta del contenuto alla root
        this.getChildren().add(root);
        VBox.setVgrow(root, Priority.ALWAYS);
    }



    private StackPane createBackground() {
        StackPane bgLayer = new StackPane();
        bgLayer.getStyleClass().add("dark-premium");

        // ImageView per lo sfondo
        ImageView bgImage = new ImageView();

        try {
            // Usa la stessa immagine del login o un'altra
            Image image = new Image(getClass().getResourceAsStream("/R.jpg"));
            bgImage.setImage(image);

            // Riempi tutto
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(bgLayer.widthProperty());
            bgImage.fitHeightProperty().bind(bgLayer.heightProperty());
            bgImage.setSmooth(true);

            // Blur leggero
            javafx.scene.effect.GaussianBlur blur = new javafx.scene.effect.GaussianBlur(3);
            bgImage.setEffect(blur);

        } catch (Exception e) {
            System.out.println("⚠️ Immagine sfondo non trovata");
        }

        // Overlay scuro
        Region overlay = new Region();
        overlay.prefWidthProperty().bind(bgLayer.widthProperty());
        overlay.prefHeightProperty().bind(bgLayer.heightProperty());

        bgLayer.getChildren().addAll(bgImage, overlay);

        return bgLayer;
    }


    /**
     * Crea l'header
     */
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.getStyleClass().add("dp-header");
        header.setPadding(new Insets(15, 25, 15, 25));

        // Titolo
        Label title = new Label("Gestione Camere");
        title.getStyleClass().add("dp-header-title");

        // Sottotitolo
        Label subtitle = new Label("Visualizzazione stato camere in tempo reale");
        subtitle.getStyleClass().add("dp-header-subtitle");

        header.getChildren().addAll(title, subtitle);

        return header;
    }

    private String helper (int capacità){
        String cap = "";
        switch (capacità) {
            case 1: {
                cap = "Singola";
                break;
            }
            case 2: {
                cap = "Doppia";
                break;
            }
            case 3: {
                cap = "Tripla";
                break;
            }
            default:cap = "";
        }
        return cap;
    }

    /**
     * Popola la griglia delle camere
     */
    private void populateRoomsGrid() throws IllegalAccess, RemoteException {
        FrontDeskClient frontDeskClient = new FrontDeskClient();
         camere = frontDeskClient.getCamere();

        String[][] rooms = new String[camere.size()][4];

        for (int i = 0; i < camere.size(); i++) {
            Camera c = camere.get(i);

            // Colonna 0: Numero (convertito in stringa)
            rooms[i][0] = String.valueOf(c.getNumeroCamera());

            // Colonna 1: Nome
            rooms[i][1] = String.valueOf(c.getNomeCamera());

            // Colonna 2: Stato (Es: "Occupata")
            rooms[i][2] = String.valueOf(helper(c.getCapacità()));


            rooms[i][3] = String.valueOf(helper(c.getCapacità())).toLowerCase();
        }

        int col = 0;
        int row = 0;

        for (Object[] roomData : rooms) {
            VBox roomCard = createRoomCard(
                    (Integer) roomData[0],
                    (String) roomData[1],
                    (String) roomData[2],
                    (String) roomData[3]
            );

            roomsGrid.add(roomCard, col, row);

            col++;
            if (col >= 6) { // 6 colonne
                col = 0;
                row++;
            }
        }
    }

    /**
     * Crea una card camera
     */
    private VBox createRoomCard(int roomNumber, String roomType, String status, String statusClass) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(12, 15, 12, 15));
        card.getStyleClass().add("dp-room-card");
        card.setMinWidth(200);
        card.setMinHeight(150);
        card.setCursor(javafx.scene.Cursor.HAND);

        // Numero camera (GOLD)
        Label numberLabel = new Label("Camera " + roomNumber);
        numberLabel.getStyleClass().add("dp-room-number");

        // Tipo camera
        Label typeLabel = new Label(roomType);
        typeLabel.getStyleClass().add("dp-room-type");

        // Spacer per spingere lo status in basso
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Status badge
        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().addAll("dp-room-status", "status-" + statusClass);

        card.getChildren().addAll(numberLabel, typeLabel, spacer, statusLabel);

        // Click handler
        card.setOnMouseClicked(e -> handleRoomClick(roomNumber, roomType, status));

        return card;
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        // Già gestito tramite CSS
    }

    // ===== EVENT HANDLERS =====

    /**
     * Gestisce il click su una camera
     */
    private void handleRoomClick(int roomNumber, String roomType, String status) {
        System.out.println("🚪 Click su Camera " + roomNumber);

        // Mostra dialog custom in overlay
        showRoomDetailDialog(roomNumber, roomType, status);
    }

    /**
     * Mostra il dialog di gestione camera
     */
    private void showRoomDetailDialog(int roomNumber, String roomType, String currentStatus) {
        // Crea overlay scuro
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setAlignment(Pos.CENTER);

        // Crea il dialog
        VBox dialogBox = createRoomDialog(roomNumber, roomType, currentStatus, overlay);

        overlay.getChildren().add(dialogBox);

        // Aggiungi overlay alla root
        StackPane root = (StackPane) this.getChildren().get(0);
        root.getChildren().add(overlay);

        // Click fuori dal dialog per chiudere
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) {
                root.getChildren().remove(overlay);
            }
        });
    }

    /**
     * Crea il dialog per i dettagli camera
     */
    private VBox createRoomDialog(int roomNumber, String roomType, String currentStatus, StackPane overlay) {
        VBox dialog = new VBox(20);
        dialog.setMaxWidth(500);
        dialog.setMaxHeight(600);
        dialog.getStyleClass().add("room-dialog");
        dialog.setPadding(new Insets(30));

        // Header del dialog
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("room-dialog-header");
        header.setPadding(new Insets(0, 0, 15, 0));
        header.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 0 0 2 0;");

        Label icon = new Label("🚪");
        icon.setStyle("-fx-font-size: 32px;");

        VBox headerText = new VBox(3);
        Label titleLabel = new Label("Camera " + roomNumber);
        titleLabel.getStyleClass().add("room-dialog-title");
        Label typeLabel = new Label(roomType);
        typeLabel.getStyleClass().add("room-dialog-subtitle");
        headerText.getChildren().addAll(titleLabel, typeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Bottone chiudi
        Label closeBtn = new Label("✕");
        closeBtn.getStyleClass().add("room-dialog-close");
        closeBtn.setOnMouseClicked(e -> {
            StackPane root = (StackPane) this.getChildren().get(0);
            root.getChildren().remove(overlay);
        });

        header.getChildren().addAll(icon, headerText, spacer, closeBtn);

        // Sezione stato
        VBox statusSection = createStatusSection(currentStatus, roomNumber);

        // Sezione ospite
        VBox guestSection = createGuestSection(roomNumber);

        // Sezione note
        VBox notesSection = createNotesSection();

        // Bottoni azione
        HBox actionButtons = createActionButtons(overlay, roomNumber);

        dialog.getChildren().addAll(header, statusSection, guestSection, notesSection, actionButtons);

        // Impedisce che il click sul dialog chiuda l'overlay
        dialog.setOnMouseClicked(e -> e.consume());

        return dialog;
    }

    /**
     * Crea la sezione stato camera
     */
    private VBox createStatusSection(String currentStatus, int roomNumber) {
        VBox section = new VBox(12);
        section.getStyleClass().add("room-dialog-section");

        Label sectionTitle = new Label("🔧 Stato Camera");
        sectionTitle.getStyleClass().add("room-dialog-section-title");

        // Radio buttons per lo stato
        VBox statusOptions = new VBox(10);

        ToggleGroup statusGroup = new ToggleGroup();

        RadioButton availableRadio = new RadioButton("In servizio");
        availableRadio.setToggleGroup(statusGroup);
        availableRadio.getStyleClass().add("room-status-radio");
        availableRadio.setSelected(currentStatus.equals("Disponibile"));

        RadioButton cleaningRadio = new RadioButton("In Pulizia");
        cleaningRadio.setToggleGroup(statusGroup);
        cleaningRadio.getStyleClass().add("room-status-radio");
        cleaningRadio.setSelected(currentStatus.equals("In Pulizia"));

        RadioButton outOfOrderRadio = new RadioButton("Out of Order");
        outOfOrderRadio.setToggleGroup(statusGroup);
        outOfOrderRadio.getStyleClass().add("room-status-radio");
        outOfOrderRadio.setSelected(currentStatus.equals("Out of Order"));


        statusOptions.getChildren().addAll(availableRadio, cleaningRadio, outOfOrderRadio);

        section.getChildren().addAll(sectionTitle, statusOptions);

        // Salva il gruppo per recuperarlo dopo
        section.setUserData(statusGroup);

        return section;
    }

    /**
     * Crea la sezione ospite
     */
    private VBox createGuestSection(int roomNumber) {
        VBox section = new VBox(12);
        section.getStyleClass().add("room-dialog-section");

        Label sectionTitle = new Label("👤 Intestatario");
        sectionTitle.getStyleClass().add("room-dialog-section-title");

        // Mock data - sostituire con dati reali
        String guestName = getGuestForRoom(roomNumber);

        Label guestLabel = new Label(guestName);
        guestLabel.getStyleClass().add("room-dialog-guest-name");

        section.getChildren().addAll(sectionTitle, guestLabel);

        return section;
    }

    /**
     * Crea la sezione note governante
     */
    private VBox createNotesSection() {
        VBox section = new VBox(12);
        section.getStyleClass().add("room-dialog-section");

        Label sectionTitle = new Label("📝 Note Governante");
        sectionTitle.getStyleClass().add("room-dialog-section-title");

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Inserisci note per la governante...");
        notesArea.setPrefRowCount(3);
        notesArea.setWrapText(true);
        notesArea.getStyleClass().add("room-dialog-notes");

        section.getChildren().addAll(sectionTitle, notesArea);

        return section;
    }

    /**
     * Crea i bottoni azione
     */
    private HBox createActionButtons(StackPane overlay, int roomNumber) {
        HBox buttons = new HBox(12);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(15, 0, 0, 0));
        buttons.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 2 0 0 0;");

        Button cancelBtn = new Button("ANNULLA");
        cancelBtn.getStyleClass().add("room-dialog-btn-cancel");
        cancelBtn.setOnAction(e -> {
            StackPane root = (StackPane) this.getChildren().get(0);
            root.getChildren().remove(overlay);
        });

        Button saveBtn = new Button("💾 SALVA");
        saveBtn.getStyleClass().add("room-dialog-btn-save");
        saveBtn.setOnAction(e -> {
            // Recupera lo stato selezionato
            VBox dialogBox = (VBox) overlay.getChildren().get(0);
            VBox statusSection = (VBox) dialogBox.getChildren().get(1);
            ToggleGroup statusGroup = (ToggleGroup) statusSection.getUserData();

            RadioButton selected = (RadioButton) statusGroup.getSelectedToggle();
            String newStatus = selected.getText();

            System.out.println("💾 Salvataggio stato camera " + roomNumber + ": " + newStatus);

            // Aggiorna lo stato
            updateRoomStatus(roomNumber, newStatus);

            // Chiudi dialog
            StackPane root = (StackPane) this.getChildren().get(0);
            root.getChildren().remove(overlay);

            // Ricarica griglia
            refreshRooms();
        });

        buttons.getChildren().addAll(cancelBtn, saveBtn);

        return buttons;
    }

    /**
     * Mock - Ottieni ospite per camera
     */
    private String getGuestForRoom(int roomNumber) {
      //plist= frontDeskClient.getPrenotazioniOdierne();
     for (Prenotazione p : plist) {
         for(Cliente c :p.getListaClienti()){
             if (c.getCamera().getNumeroCamera()== roomNumber){
                 return p.getIntestatario();
             }
         }
     }
     return "Non presente";
    }

    // ===== PUBLIC METHODS =====

    /**
     * Aggiorna lo stato di una camera
     */
    public void updateRoomStatus(int roomNumber, String newStatus) {
        System.out.println("🔄 Aggiornamento Camera " + roomNumber + " -> " + newStatus);
        // TODO: Implementare logica di aggiornamento
    }

    /**
     * Ricarica tutte le camere
     */
    public void refreshRooms() {
        roomsGrid.getChildren().clear();

        try{
            populateRoomsGrid();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("🔄 Camere ricaricate");
    }
}