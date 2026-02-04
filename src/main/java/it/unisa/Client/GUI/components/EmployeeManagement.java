package it.unisa.Client.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeManagement - Gestione Personale (List Style - V2)
 */
public class EmployeeManagement extends VBox {

    private List<Employee> employees;
    private VBox employeeListContainer;
    private Label resultsCountLabel;
    private TextField nameFilter;
    private TextField surnameFilter;
    private ComboBox<String> roleFilter;
    private ComboBox<String> genderFilter;
    private Integer currentEmployeeId = null;

    public EmployeeManagement() {
        this.employees = createMockEmployees();
        setupLayout();
        setupStyling();
        loadEmployees();
    }

    private void setupLayout() {
        HBox header = createHeader();
        HBox searchBar = createSearchBar();
        VBox resultsSection = createResultsSection();

        this.getChildren().addAll(header, searchBar, resultsSection);
        VBox.setVgrow(resultsSection, Priority.ALWAYS);
    }

    private void setupStyling() {
        this.getStyleClass().add("employee-management-view");
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(25, 35, 25, 35));
        header.getStyleClass().add("employee-header");

        VBox headerLeft = new VBox(5);
        Label title = new Label("GESTIONE PERSONALE");
        title.getStyleClass().add("employee-header-title");
        Label subtitle = new Label("Ricerca e gestisci gli impiegati dell'hotel");
        subtitle.getStyleClass().add("employee-header-subtitle");
        headerLeft.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button newEmployeeBtn = new Button("➕ NUOVO IMPIEGATO");
        newEmployeeBtn.getStyleClass().add("btn-new-employee");
        newEmployeeBtn.setOnAction(e -> openEmployeeDialog(null));

        header.getChildren().addAll(headerLeft, spacer, newEmployeeBtn);
        return header;
    }

    private HBox createSearchBar() {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(25, 35, 25, 35));
        searchBar.getStyleClass().add("employee-search-bar");

        VBox nameBox = createSearchField("Nome", nameFilter = new TextField());
        nameFilter.setPromptText("Mario");

        VBox surnameBox = createSearchField("Cognome", surnameFilter = new TextField());
        surnameFilter.setPromptText("Rossi");

        roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("Tutti", "Cameriere", "Maitre", "Receptionist", "Addetto Pulizie", "Chef", "Manager");
        roleFilter.setValue("Tutti");
        VBox roleBox = createSearchField("Ruolo", roleFilter);

        genderFilter = new ComboBox<>();
        genderFilter.getItems().addAll("Tutti", "M", "F");
        genderFilter.setValue("Tutti");
        VBox genderBox = createSearchField("Sesso", genderFilter);

        Button searchBtn = new Button("🔍 CERCA");
        searchBtn.getStyleClass().add("btn-search-employee");
        searchBtn.setOnAction(e -> filterEmployees());
        VBox.setMargin(searchBtn, new Insets(17, 0, 0, 0));

        HBox.setHgrow(nameBox, Priority.ALWAYS);
        HBox.setHgrow(surnameBox, Priority.ALWAYS);
        HBox.setHgrow(roleBox, Priority.ALWAYS);
        HBox.setHgrow(genderBox, Priority.ALWAYS);

        searchBar.getChildren().addAll(nameBox, surnameBox, roleBox, genderBox, searchBtn);
        return searchBar;
    }

    private VBox createSearchField(String label, Control control) {
        VBox box = new VBox(6);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("search-field-label");
        control.getStyleClass().add("search-field-input");
        box.getChildren().addAll(lbl, control);
        return box;
    }

    private VBox createResultsSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25, 35, 25, 35));
        section.getStyleClass().add("employee-results-section");

        HBox resultsHeader = new HBox();
        resultsHeader.setAlignment(Pos.CENTER_LEFT);
        resultsHeader.setPadding(new Insets(0, 0, 15, 0));
        resultsHeader.setStyle("-fx-border-color: #f5e6d3; -fx-border-width: 0 0 2 0;");

        Label resultsTitle = new Label("📋 RISULTATI RICERCA");
        resultsTitle.getStyleClass().add("results-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        resultsCountLabel = new Label("0 impiegati trovati");
        resultsCountLabel.getStyleClass().add("results-count-badge");

        resultsHeader.getChildren().addAll(resultsTitle, spacer, resultsCountLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        employeeListContainer = new VBox(12);
        scrollPane.setContent(employeeListContainer);

        section.getChildren().addAll(resultsHeader, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return section;
    }

    private HBox createEmployeeRow(Employee emp) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20, 25, 20, 25));
        row.getStyleClass().add("employee-row");

        Label icon = new Label("👤");
        icon.getStyleClass().add("employee-icon");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoGrid, Priority.ALWAYS);

        for (int i = 0; i < 5; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            infoGrid.getColumnConstraints().add(col);
        }

        infoGrid.add(createInfoBlock("Nome e Cognome", emp.getFullName(), true), 0, 0);
        infoGrid.add(createInfoBlock("Codice Fiscale", emp.cf, false), 1, 0);
        infoGrid.add(createInfoBlock("Data Nascita", emp.birthDate.toString(), false), 2, 0);
        infoGrid.add(createRoleBadge(emp.role), 3, 0);
        infoGrid.add(createSalaryBadge(""+emp.stipendio), 4, 0);

        Button infoBtn = new Button("📋 INFO");
        infoBtn.getStyleClass().add("employee-action-btn");
        infoBtn.setOnAction(e -> openEmployeeDialog(emp));

        row.getChildren().addAll(icon, infoGrid, infoBtn);

        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-border-color: #6d1331; -fx-border-width: 2; -fx-translate-x: 5;");
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-border-color: #e5e5e5; -fx-border-width: 2; -fx-translate-x: 0;");
        });

        return row;
    }

    private VBox createInfoBlock(String label, String value, boolean primary) {
        VBox box = new VBox(4);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("info-label-small");
        Label val = new Label(value);
        val.getStyleClass().add(primary ? "info-value-main" : "info-value-secondary");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private VBox createRoleBadge(String role) {
        VBox box = new VBox(4);
        Label lbl = new Label("Ruolo");
        lbl.getStyleClass().add("info-label-small");
        Label badge = new Label(role);
        badge.getStyleClass().add("role-badge");
        box.getChildren().addAll(lbl, badge);
        return box;
    }
    private VBox createSalaryBadge(String salary) {
        VBox box = new VBox(4);


        Label lbl = new Label("Stipendio");
        lbl.getStyleClass().add("info-label-small");


        Label badge = new Label("€ "+salary);

        // Stile inline per renderlo verde e "a pillola" (simile al ruolo)
        // Sfondo verde chiaro, testo verde scuro, bordi arrotondati
        badge.setStyle("-fx-background-color: #d1fae5; " +
                "-fx-text-fill: #065f46; " +
                "-fx-padding: 5px 12px; " +
                "-fx-background-radius: 15; " +
                "-fx-font-weight: 800; " +
                "-fx-font-size: 12px; " +
                "-fx-border-color: #34d399; " +
                "-fx-border-radius: 15; -fx-border-width: 1;");

        box.getChildren().addAll(lbl, badge);
        return box;
    }

    private void loadEmployees() {
        employeeListContainer.getChildren().clear();
        for (Employee emp : employees) {
            employeeListContainer.getChildren().add(createEmployeeRow(emp));
        }
        updateResultsCount();
    }

    private void filterEmployees() {
        String name = nameFilter.getText().toLowerCase();
        String surname = surnameFilter.getText().toLowerCase();
        String role = roleFilter.getValue();
        String gender = genderFilter.getValue();

        employeeListContainer.getChildren().clear();

        for (Employee emp : employees) {
            boolean matches = true;

            if (!name.isEmpty() && !emp.nome.toLowerCase().contains(name)) matches = false;
            if (!surname.isEmpty() && !emp.cognome.toLowerCase().contains(surname)) matches = false;
            if (!role.equals("Tutti") && !emp.role.equals(role)) matches = false;
            if (!gender.equals("Tutti") && !emp.gender.equals(gender)) matches = false;

            if (matches) {
                employeeListContainer.getChildren().add(createEmployeeRow(emp));
            }
        }

        updateResultsCount();
    }

    private void updateResultsCount() {
        int count = employeeListContainer.getChildren().size();
        resultsCountLabel.setText(count + " impiegati trovati");
    }

    // ===== EMPLOYEE DIALOG (V4 - CON USERNAME READ-ONLY) =====
    private void openEmployeeDialog(Employee emp) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(emp == null ? "Nuovo Impiegato" : "Anagrafica Impiegato");

        VBox dialogContent = new VBox();
        dialogContent.getStyleClass().add("employee-dialog");

        // --- HEADER ---
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.getStyleClass().add("dialog-header");

        Label logo = new Label("👑 HOTEL COLOSSUS");
        logo.setStyle("-fx-text-fill: #d4af37; -fx-font-size: 16px; -fx-font-weight: 700; -fx-letter-spacing: 2;");

        Label titleLabel = new Label("SCHEDA ANAGRAFICA");
        titleLabel.getStyleClass().add("dialog-header-title");

        header.getChildren().addAll(logo, titleLabel);

        // --- BODY ---
        ScrollPane bodyScroll = new ScrollPane();
        bodyScroll.setFitToWidth(true);
        bodyScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        bodyScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        bodyScroll.setPadding(new Insets(30, 40, 30, 40));

        VBox body = new VBox(25);

        // -- Definizione Campi --
        TextField nomeField = createDialogField();
        TextField cognomeField = createDialogField();

        // CAMPO USERNAME (Read Only)
        TextField usernameField = new TextField();
        usernameField.setEditable(false);
        usernameField.setPromptText("Generato dal sistema...");
        usernameField.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-border-color: #ced4da; -fx-padding: 10px 12px; -fx-font-size: 13px; -fx-background-radius: 5; -fx-font-weight: bold;");

        ComboBox<String> sessoCombo = new ComboBox<>();
        sessoCombo.getItems().addAll("M", "F");
        sessoCombo.setValue("M");
        sessoCombo.getStyleClass().add("dialog-field-input");

        TextField indirizzoField = createDialogField();
        ComboBox<String> tipoDocCombo = new ComboBox<>();
        tipoDocCombo.getItems().addAll("Patente", "Carta d'Identità", "Passaporto");
        tipoDocCombo.setValue("Patente");
        tipoDocCombo.getStyleClass().add("dialog-field-input");

        DatePicker dataRilascioPicker = new DatePicker();
        dataRilascioPicker.getStyleClass().add("dialog-field-input");
        DatePicker dataScadenzaPicker = new DatePicker();
        dataScadenzaPicker.getStyleClass().add("dialog-field-input");
        TextField emailField = createDialogField();
        TextField cittadinanzaField = createDialogField();
        TextField cfField = createDialogField();
        DatePicker dataNascitaPicker = new DatePicker();
        dataNascitaPicker.getStyleClass().add("dialog-field-input");
        TextField comuneField = createDialogField();
        TextField provinciaField = createDialogField();
        TextField capField = createDialogField();
        TextField telefonoField = createDialogField();
        ComboBox<String> ruoloCombo = new ComboBox<>();
        ruoloCombo.getItems().addAll("Receptionist", "Cameriere", "Maitre", "Chef", "Addetto Pulizie", "Manager");
        ruoloCombo.setValue("Receptionist");
        ruoloCombo.getStyleClass().add("dialog-field-input");
        TextField stipendioField = createDialogField();

        // Popolamento dati
        if (emp != null) {
            nomeField.setText(emp.nome);
            cognomeField.setText(emp.cognome);
            usernameField.setText(emp.userName);
            sessoCombo.setValue(emp.gender);
            indirizzoField.setText(emp.via + " " + emp.numeroCivico);
            tipoDocCombo.setValue(emp.docType);
            dataRilascioPicker.setValue(emp.docIssueDate);
            dataScadenzaPicker.setValue(emp.docExpiryDate);
            emailField.setText(emp.emailAziendale);
            cittadinanzaField.setText(emp.cittadinanza);
            cfField.setText(emp.cf);
            dataNascitaPicker.setValue(emp.birthDate);
            comuneField.setText(emp.comune);
            provinciaField.setText(emp.provincia);
            capField.setText(String.valueOf(emp.CAP));
            telefonoField.setText(emp.telefono);
            ruoloCombo.setValue(emp.role);
            stipendioField.setText(String.valueOf(emp.stipendio));
        }

        // -- Layout Grid --
        Label anagraficaTitle = new Label("DATI PERSONALI");
        anagraficaTitle.getStyleClass().add("dialog-section-divider");

        GridPane anagraficaGrid = new GridPane();
        anagraficaGrid.setHgap(20);
        anagraficaGrid.setVgap(15);
        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(50);
        anagraficaGrid.getColumnConstraints().addAll(col1, col2);

        int row = 0;
        anagraficaGrid.add(createDialogFieldBox("NOME", nomeField), 0, row);
        anagraficaGrid.add(createDialogFieldBox("CITTADINANZA", cittadinanzaField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("COGNOME", cognomeField), 0, row);
        anagraficaGrid.add(createDialogFieldBox("CODICE FISCALE", cfField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("SESSO", sessoCombo), 0, row);
        anagraficaGrid.add(createDialogFieldBox("DATA NASCITA", dataNascitaPicker), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("INDIRIZZO COMPLETO", indirizzoField), 0, row);
        anagraficaGrid.add(createDialogFieldBox("COMUNE", comuneField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("TIPO DOC.", tipoDocCombo), 0, row);
        anagraficaGrid.add(createDialogFieldBox("PROVINCIA", provinciaField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("DATA RILASCIO", dataRilascioPicker), 0, row);
        anagraficaGrid.add(createDialogFieldBox("CAP", capField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("DATA SCADENZA", dataScadenzaPicker), 0, row);
        anagraficaGrid.add(createDialogFieldBox("TELEFONO", telefonoField), 1, row++);
        anagraficaGrid.add(createDialogFieldBox("E-MAIL AZIENDALE", emailField), 0, row);
        anagraficaGrid.add(createDialogFieldBox("USERNAME (SISTEMA)", usernameField), 1, row++);

        Label salarioTitle = new Label("CONTRATTO E RUOLO");
        salarioTitle.getStyleClass().add("dialog-section-divider");

        GridPane salarioGrid = new GridPane();
        salarioGrid.setHgap(20);
        salarioGrid.setVgap(15);
        salarioGrid.getColumnConstraints().addAll(col1, col2);

        salarioGrid.add(createDialogFieldBox("RUOLO AZIENDALE", ruoloCombo), 0, 0);
        salarioGrid.add(createDialogFieldBox("SALARIO MENSILE (€)", stipendioField), 1, 0);

        body.getChildren().addAll(anagraficaTitle, anagraficaGrid, salarioTitle, salarioGrid);
        bodyScroll.setContent(body);

        // --- FOOTER ---
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(20, 30, 20, 30));
        footer.getStyleClass().add("dialog-footer");

        Button rimuoviBtn = new Button("ELIMINA");
        rimuoviBtn.getStyleClass().add("dialog-btn-delete");
        rimuoviBtn.setVisible(emp != null);
        rimuoviBtn.setOnAction(e -> {
            if (emp != null) {
                employees.remove(emp);
                filterEmployees();
                dialog.close();
            }
        });

        Button resetBtn = new Button("RESET FORM");
        resetBtn.setStyle("-fx-background-color: #8b4513; -fx-text-fill: white; -fx-padding: 13px 20px; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 8; -fx-cursor: hand;");
        resetBtn.setOnAction(e -> {
            // Pulisce solo i campi modificabili dall'utente
            nomeField.clear(); cognomeField.clear(); indirizzoField.clear();
            emailField.clear(); cittadinanzaField.clear(); cfField.clear();
            comuneField.clear(); provinciaField.clear(); capField.clear();
            telefonoField.clear(); stipendioField.clear();

            // IMPORTANTE: NON puliamo usernameField qui!

            dataNascitaPicker.setValue(null);
            dataRilascioPicker.setValue(null);
            dataScadenzaPicker.setValue(null);
            sessoCombo.setValue("M");
            tipoDocCombo.setValue("Patente");
            ruoloCombo.setValue("Receptionist");
        });

        Button generaPasswordBtn = new Button("GENERA CREDENZIALI");
        generaPasswordBtn.setStyle("-fx-background-color: #8b4513; -fx-text-fill: white; -fx-padding: 13px 20px; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 8; -fx-cursor: hand;");
        generaPasswordBtn.setOnAction(e -> {
            String n = nomeField.getText().replaceAll("\\s+", "").toLowerCase();
            String c = cognomeField.getText().replaceAll("\\s+", "").toLowerCase();

            if (n.isEmpty()) n = "user";
            if (c.isEmpty()) c = "temp";

            String generatedUser = n + "." + c;
            String generatedPass = "Temp" + (int)(Math.random() * 10000) + "!";

            usernameField.setText(generatedUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Credenziali Generate");
            alert.setHeaderText("Nuove credenziali di accesso");
            alert.setContentText("Username: " + generatedUser + "\nPassword: " + generatedPass);
            alert.showAndWait();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button salvaBtn = new Button("SALVA DATI");
        salvaBtn.getStyleClass().add("dialog-btn-save");
        salvaBtn.setOnAction(e -> {
            dialog.close();
        });

        footer.getChildren().addAll(rimuoviBtn, resetBtn, generaPasswordBtn, spacer, salvaBtn);

        dialogContent.getChildren().addAll(header, bodyScroll, footer);
        VBox.setVgrow(bodyScroll, Priority.ALWAYS);

        javafx.scene.Scene dialogScene = new javafx.scene.Scene(dialogContent, 950, 750);
        dialogScene.getStylesheets().add(getClass().getResource("/hotel.css").toExternalForm());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // 1. Questo mancava: gestisce la chiamata senza parametri
    private TextField createDialogField() {
        return createDialogField("", "");
    }

    // 2. Questo è quello principale che avevi già
    private TextField createDialogField(String prompt, String value) {
        // Se value è null, mettiamo stringa vuota per evitare errori
        TextField field = new TextField(value == null ? "" : value);
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent; -fx-padding: 10px 12px; -fx-font-size: 13px; -fx-background-radius: 5;");
        return field;
    }

    // 3. Questo è il metodo principale per i box
    private VBox createDialogFieldBox(String label, Control control) {
        VBox box = new VBox(5);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #333;");
        box.getChildren().addAll(lbl, control);
        return box;
    }

    // 4. Questo mancava: serve perché nel codice usi "createFieldBox" invece di "createDialogFieldBox"
    private VBox createFieldBox(String label, Control control) {
        return createDialogFieldBox(label, control);
    }

    private List<Employee> createMockEmployees() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("pbianchi", "hash123", "Paola", "Bianchi", "BNCPLA85M41H501Z", LocalDate.of(1985, 8, 1),
                "F", "Cameriere", "Carta d'Identità", "AB1234567", LocalDate.of(2020, 1, 15), LocalDate.of(2030, 1, 15),
                "Via Roma", 123, "Napoli", "NA", 80100, "+39 333 1111111", "paola.bianchi@hotel.com", "Italiana", 1500.00, LocalDate.of(2020, 3, 1)));

        list.add(new Employee("fverdi", "hash456", "Francesco", "Verdi", "VRDFNC80B15F205W", LocalDate.of(1980, 2, 15),
                "M", "Cameriere", "Patente", "CD7890123", LocalDate.of(2019, 5, 10), LocalDate.of(2029, 5, 10),
                "Via Garibaldi", 45, "Roma", "RM", 00100, "+39 333 2222222", "francesco.verdi@hotel.com", "Italiana", 1500.00, LocalDate.of(2019, 6, 15)));

        list.add(new Employee("arossi", "hash789", "Alberto", "Rossi", "RSSLRT75C20H501K", LocalDate.of(1975, 3, 20),
                "M", "Cameriere", "Passaporto", "EF4567890", LocalDate.of(2021, 8, 1), LocalDate.of(2031, 8, 1),
                "Via Dante", 67, "Milano", "MI", 20100, "+39 333 3333333", "alberto.rossi@hotel.com", "Italiana", 1500.00, LocalDate.of(2018, 1, 10)));

        list.add(new Employee("vazzurri", "hash101", "Valerio", "Azzurri", "ZZRVLR82D10L219M", LocalDate.of(1982, 4, 10),
                "M", "Maitre", "Carta d'Identità", "GH1234567", LocalDate.of(2018, 12, 5), LocalDate.of(2028, 12, 5),
                "Via Manzoni", 89, "Torino", "TO", 10100, "+39 333 4444444", "valerio.azzurri@hotel.com", "Italiana", 2200.00, LocalDate.of(2017, 9, 1)));

        list.add(new Employee("fceleste", "hash202", "Francesca Luca", "Celeste", "CLSFNC88E45F205X", LocalDate.of(1988, 5, 5),
                "F", "Cameriere", "Patente", "IJ7890123", LocalDate.of(2020, 3, 20), LocalDate.of(2030, 3, 20),
                "Via Verdi", 34, "Firenze", "FI", 50100, "+39 333 5555555", "francesca.celeste@hotel.com", "Italiana", 1500.00, LocalDate.of(2020, 4, 1)));

        return list;
    }

    public static class Employee {
        String userName, hashPassword, nome, cognome, cf, gender, role, docType, docNumber;
        String via, comune, provincia, telefono, emailAziendale, cittadinanza;
        LocalDate birthDate, docIssueDate, docExpiryDate, dataAssunzione;
        int numeroCivico, CAP;
        double stipendio;

        public Employee(String userName, String hashPassword, String nome, String cognome, String cf, LocalDate birthDate,
                        String gender, String role, String docType, String docNumber, LocalDate docIssueDate, LocalDate docExpiryDate,
                        String via, int numeroCivico, String comune, String provincia, int CAP, String telefono,
                        String emailAziendale, String cittadinanza, double stipendio, LocalDate dataAssunzione) {
            this.userName = userName;
            this.hashPassword = hashPassword;
            this.nome = nome;
            this.cognome = cognome;
            this.cf = cf;
            this.birthDate = birthDate;
            this.gender = gender;
            this.role = role;
            this.docType = docType;
            this.docNumber = docNumber;
            this.docIssueDate = docIssueDate;
            this.docExpiryDate = docExpiryDate;
            this.via = via;
            this.numeroCivico = numeroCivico;
            this.comune = comune;
            this.provincia = provincia;
            this.CAP = CAP;
            this.telefono = telefono;
            this.emailAziendale = emailAziendale;
            this.cittadinanza = cittadinanza;
            this.stipendio = stipendio;
            this.dataAssunzione = dataAssunzione;
        }

        public String getFullName() {
            return nome + " " + cognome;
        }
    }
}