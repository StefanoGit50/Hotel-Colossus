package it.unisa.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuestManagement extends VBox {
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker birthDatePicker;
    private ComboBox<String> documentTypeCombo;
    private TextField documentNumberField;
    private TextField addressField;
    private TextField cityField;
    private TextField zipCodeField;
    private TextField nationalityField;
    private TextField phoneField;
    private TextField emailField;

    // ===== BUTTONS =====
    private Button registerButton;
    private Button cancelButton;

    // ===== COSTRUTTORE =====
    public GuestManagement() {
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        // Header con titolo
        VBox headerBox = createHeaderBox();

        // Form Container
        VBox formContainer = createFormContainer();

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(headerBox, scrollPane);
    }


    /**
     * Inizializza tutti i componenti
     */
    private void initializeComponents() {
        // Inizializza i campi del form
        firstNameField = createTextField("Inserisci nome");
        lastNameField = createTextField("Inserisci cognome");
        birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("Seleziona data");

        documentTypeCombo = new ComboBox<>();
        documentTypeCombo.getItems().addAll(
                "Carta d'Identità",
                "Patente",
                "Passaporto"
        );
        documentTypeCombo.setPromptText("Seleziona tipo");

        documentNumberField = createTextField("Numero documento");
        addressField = createTextField("Via, Numero Civico");
        cityField = createTextField("Città");
        zipCodeField = createTextField("CAP");
        nationalityField = createTextField("Es: Italiana");
        phoneField = createTextField("Numero di telefono");
        emailField = createTextField("email@example.com");

        // Buttons
        registerButton = new Button("Registra Cliente");
        registerButton.getStyleClass().add("register-btn-primary");
        registerButton.setOnAction(e -> onRegisterClick());

        cancelButton = new Button("Annulla");
        cancelButton.getStyleClass().add("register-btn-secondary");
        cancelButton.setOnAction(e -> onCancelClick());
    }


    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("content-area");
    }

    // ===== CREAZIONE COMPONENTI =====

    /**
     * Crea l'header con titolo e decorazioni
     */
    private VBox createHeaderBox() {
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getStyleClass().add("registration-header");

        // Crown emoji
        Label crownLabel = new Label("👤");
        crownLabel.setStyle("-fx-font-size: 20px;");

        // Titolo
        Label titleLabel = new Label("REGISTRAZIONE CLIENTE");
        titleLabel.getStyleClass().add("registration-title");

        // Descrizione
        Label descLabel = new Label("Inserisci i dati del nuovo ospite");
        descLabel.getStyleClass().add("registration-description");

        headerBox.getChildren().addAll(crownLabel, titleLabel, descLabel);

        return headerBox;
    }

    /**
     * Crea il container del form
     */
    private VBox createFormContainer() {
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20, 30, 20, 30));
        formContainer.getStyleClass().add("registration-form-container");

        // Prima riga: Nome e Cognome
        GridPane row1 = createFormRow(
                "Nome", firstNameField,
                "Cognome", lastNameField
        );

        // Seconda riga: Data di nascita e Tipo documento
        GridPane row2 = createFormRow(
                "Data di Nascita", birthDatePicker,
                "Tipo Documento", documentTypeCombo
        );

        // Terza riga: Numero documento
        VBox row3 = createFormGroup("Numero Documento", documentNumberField, true);

        // Quarta riga: Indirizzo
        VBox row4 = createFormGroup("Indirizzo Completo", addressField, true);

        // Quinta riga: Città e CAP
        GridPane row5 = createFormRow(
                "Città", cityField,
                "CAP", zipCodeField
        );

        // Sesta riga: Nazionalità e Telefono
        GridPane row6 = createFormRow(
                "Nazionalità", nationalityField,
                "Telefono", phoneField
        );

        // Settima riga: Email
        VBox row7 = createFormGroup("Email", emailField, true);

        // Buttons row
        HBox buttonsRow = createButtonsRow();

        formContainer.getChildren().addAll(
                row1, row2, row3, row4, row5, row6, row7, buttonsRow
        );

        return formContainer;
    }

    /**
     * Crea una riga del form con due campi
     */
    private GridPane createFormRow(String label1, Control field1, String label2, Control field2) {
        GridPane grid = new GridPane();
        grid.setHgap(20);

        // Prima colonna
        VBox col1 = createFormGroup(label1, field1, false);

        // Seconda colonna
        VBox col2 = createFormGroup(label2, field2, false);

        grid.add(col1, 0, 0);
        grid.add(col2, 1, 0);

        // Imposta larghezze colonne uguali
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(50);
        grid.getColumnConstraints().addAll(colConstraints, colConstraints);

        return grid;
    }

    /**
     * Crea un singolo form group (label + campo)
     */
    private VBox createFormGroup(String labelText, Control field, boolean fullWidth) {
        VBox formGroup = new VBox(5);
        if (fullWidth) {
            formGroup.getStyleClass().add("form-group-full");
        } else {
            formGroup.getStyleClass().add("form-group");
        }

        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        field.getStyleClass().add("form-input");

        formGroup.getChildren().addAll(label, field);

        return formGroup;
    }

    /**
     * Crea la riga dei bottoni
     */
    private HBox createButtonsRow() {
        HBox buttonsRow = new HBox(15);
        buttonsRow.setAlignment(Pos.CENTER);
        buttonsRow.setPadding(new Insets(20, 0, 0, 0));

        HBox.setHgrow(cancelButton, Priority.ALWAYS);
        HBox.setHgrow(registerButton, Priority.ALWAYS);

        cancelButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setMaxWidth(Double.MAX_VALUE);

        buttonsRow.getChildren().addAll(cancelButton, registerButton);

        return buttonsRow;
    }

    // ===== UTILITY =====

    /**
     * Crea un TextField con prompt text
     */
    private TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        return field;
    }

    // ===== EVENT HANDLERS =====

    /**
     * Gestisce il click sul bottone Registra
     */
    private void onRegisterClick() {
        // Validazione
        if (!validateForm()) {
            showAlert("Errore", "Compila tutti i campi obbligatori", Alert.AlertType.ERROR);
            return;
        }

        // Raccolta dati
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        System.out.println("✅ Cliente registrato: " + firstName + " " + lastName);

        // TODO: Salva nel database
        // guestService.saveGuest(new Guest(...));

        showAlert("Successo", "Cliente registrato con successo!", Alert.AlertType.INFORMATION);
        clearForm();
    }

    /**
     * Gestisce il click sul bottone Annulla
     */
    private void onCancelClick() {
        clearForm();
        System.out.println("❌ Registrazione annullata");
    }

    /**
     * Valida il form
     */
    private boolean validateForm() {
        return !firstNameField.getText().trim().isEmpty() &&
                !lastNameField.getText().trim().isEmpty() &&
                birthDatePicker.getValue() != null &&
                documentTypeCombo.getValue() != null &&
                !documentNumberField.getText().trim().isEmpty() &&
                !phoneField.getText().trim().isEmpty();
    }

    /**
     * Pulisce il form
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.setValue(null);
        documentTypeCombo.setValue(null);
        documentNumberField.clear();
        addressField.clear();
        cityField.clear();
        zipCodeField.clear();
        nationalityField.clear();
        phoneField.clear();
        emailField.clear();
    }

    /**
     * Mostra un alert
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
