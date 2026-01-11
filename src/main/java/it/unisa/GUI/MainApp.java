package it.unisa.GUI;// HotelColossusApp.java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.logging.Logger;


public class MainApp extends Application {

        private VBox selectedNavItem = null;

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Hotel Colossus - Management System");

            // Root layout
            VBox root = new VBox(20);
            root.getStyleClass().add("root-container");
            root.setPadding(new Insets(30));

            // Top Bar
            HBox topBar = createTopBar();
            root.getChildren().add(topBar);

            // Main Content Area (Sidebar + Content)
            HBox mainArea = createMainArea();
            root.getChildren().add(mainArea);

            // Scene
            Scene scene = new Scene(root, 1400, 800);

            // Load CSS
            scene.getStylesheets().add(
                    getClass().getResource("/hotel.css").toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.show();
        }


        private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(21,30,21,30));
        topBar.setAlignment(Pos.CENTER);
        topBar.getStyleClass().add("top-bar");

        // Logo Image PICCOLO a SINISTRA
        ImageView logoImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/hotelcolossus_logo.png"));
            logoImage.setImage(image);
            logoImage.setFitHeight(120);  // ✅ Ridotto da 180 a 60
            logoImage.setFitWidth(120);   // ✅ Ridotto da 180 a 60
            logoImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Impossibile caricare il logo: " + e.getMessage());
        }

        // Logo Text GRANDE al CENTRO
        Label logo = new Label("HOTEL COLOSSUS");
        logo.getStyleClass().add("logo");


            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

        // Spacer PRIMA della scritta (centra la scritta)
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);


        // Spacer DOPO la scritta (mantiene centrata)
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        // User info
        HBox userBox = createUserBox();

        // ✅ LAYOUT: Logo piccolo | Spacer | SCRITTA GRANDE | Spacer | User
        topBar.getChildren().addAll(  logo, spacer, userBox);

        return topBar;
    }

        private HBox createUserBox() {
            HBox userBox = new HBox(10);
            userBox.setAlignment(Pos.CENTER_RIGHT);
            userBox.getStyleClass().add("user-box");
            userBox.setPadding(new Insets(0, 20, 0, 0));

            Label userName = new Label("Receptionist1");
            userName.getStyleClass().add("user-name");

            Label userRole = new Label("Front Desk");
            userRole.getStyleClass().add("user-role");

            // Avatar
            VBox avatar = new VBox();
            avatar.setAlignment(Pos.CENTER);
            avatar.setPrefSize(40, 40);
            avatar.setMaxSize(40, 40);
            avatar.setMinSize(40, 40);
            avatar.getStyleClass().add("avatar");

            Label avatarText = new Label("R1");
            avatarText.getStyleClass().add("avatar-text");
            avatar.getChildren().add(avatarText);

            userBox.getChildren().addAll(userName, avatar);

            ContextMenu contextMenu = new ContextMenu();

            MenuItem profileItem = new MenuItem("👤 Profilo");
            MenuItem settingsItem = new MenuItem("⚙️ Impostazioni");
            MenuItem logoutItem = new MenuItem("🚪 Logout");

            // Styling per i menu items
            profileItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");
            logoutItem.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");

            // Actions
            profileItem.setOnAction(e -> System.out.println("Profilo cliccato"));
            logoutItem.setOnAction(e -> System.out.println("Logout cliccato"));

            contextMenu.getItems().addAll(profileItem, logoutItem);

            // Mostra il menu quando clicchi sullo userBox
            userBox.setOnMouseClicked(e -> {
                if (!contextMenu.isShowing()) {
                    // Calcola la posizione esatta sotto l'avatar
                    double x = userBox.localToScreen(userBox.getBoundsInLocal()).getMinX();
                    double y = userBox.localToScreen(userBox.getBoundsInLocal()).getMaxY();

                    contextMenu.show(userBox, x, y);
                } else {
                    contextMenu.hide();
                }
            });


            return userBox;
        }

        private HBox createMainArea() {
            HBox mainArea = new HBox(20);

            // Sidebar
            VBox sidebar = createSidebar();

            // Content Area
            VBox contentArea = createContentArea();
            HBox.setHgrow(contentArea, Priority.ALWAYS);

            mainArea.getChildren().addAll(sidebar, contentArea);
            return mainArea;
        }

        private VBox createSidebar() {
            VBox sidebar = new VBox(5);
            sidebar.setPrefWidth(250);
            sidebar.setPadding(new Insets(20));
            sidebar.getStyleClass().add("sidebar");

            // Menu items
            VBox dashboardItem = createNavItem("📊", "Dashboard", true);
            VBox guestItem = createNavItem("👥", "Guest Management", false);
            VBox planningItem = createNavItem("📅", "Planning", false);
            VBox roomsItem = createNavItem("🛏️", "Camere", false);
            VBox checkoutItem = createNavItem("💰", "Check-out", false);

            sidebar.getChildren().addAll(
                    dashboardItem,
                    guestItem,
                    planningItem,
                    roomsItem,
                    checkoutItem
            );

            return sidebar;
        }

        private VBox createNavItem(String icon, String text, boolean active) {
            VBox navItem = new VBox();
            navItem.setAlignment(Pos.CENTER_LEFT);
            navItem.setPadding(new Insets(12, 15, 12, 15));
            navItem.getStyleClass().add("nav-item");

            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);

            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("nav-icon");

            Label textLabel = new Label(text);
            textLabel.getStyleClass().add("nav-text");

            content.getChildren().addAll(iconLabel, textLabel);
            navItem.getChildren().add(content);

            if (active) {
                navItem.getStyleClass().add("nav-item-active");
                selectedNavItem = navItem;
            }

            // Click effect
            navItem.setOnMouseClicked(e -> {
                if (selectedNavItem != null) {
                    selectedNavItem.getStyleClass().remove("nav-item-active");
                }

                navItem.getStyleClass().add("nav-item-active");
                selectedNavItem = navItem;
            });

            return navItem;
        }

        private VBox createContentArea() {
            VBox contentArea = new VBox(20);
            contentArea.setPadding(new Insets(30));
            contentArea.getStyleClass().add("content-area");

            // Stats Grid
            GridPane statsGrid = createStatsGrid();

            // Section Title
            Label sectionTitle = new Label("Prossime Prenotazioni");
            sectionTitle.getStyleClass().add("section-title");

            // Placeholder text
            Label placeholder = new Label("Lista prenotazioni con timeline interattiva...");
            placeholder.getStyleClass().add("placeholder-text");

            contentArea.getChildren().addAll(statsGrid, sectionTitle, placeholder);
            return contentArea;
        }

        private GridPane createStatsGrid() {
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.getStyleClass().add("stats-grid");

            // Stat cards
            VBox stat1 = createStatCard("45", "Camere Occupate");
            VBox stat2 = createStatCard("12", "Check-in Oggi");
            VBox stat3 = createStatCard("8", "Check-out Oggi");
            VBox stat4 = createStatCard("15", "Camere Libere");

            grid.add(stat1, 0, 0);
            grid.add(stat2, 1, 0);
            grid.add(stat3, 2, 0);
            grid.add(stat4, 3, 0);

            // Make columns grow equally
            for (int i = 0; i < 4; i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setPercentWidth(25);
                grid.getColumnConstraints().add(col);
            }

            return grid;
        }

        private VBox createStatCard(String number, String label) {
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(20));
            card.setPrefHeight(120);
            card.getStyleClass().add("stat-card");

            Label numberLabel = new Label(number);
            numberLabel.getStyleClass().add("stat-number");

            Label labelText = new Label(label);
            labelText.getStyleClass().add("stat-label");
            labelText.setWrapText(true);
            labelText.setAlignment(Pos.CENTER);

            card.getChildren().addAll(numberLabel, labelText);

            return card;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }




/*
 * ISTRUZIONI PER L'USO:
 *
 * 1. Salva questo file come: HotelColossusApp.java
 *
 * 2. Compila con:
 *    javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls HotelColossusApp.java
 *
 * 3. Esegui con:
 *    java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls HotelColossusApp
 *
 * NOTA: Sostituisci /path/to/javafx-sdk con il percorso della tua installazione JavaFX
 *
 * CARATTERISTICHE IMPLEMENTATE:
 * - Top bar con logo e avatar utente
 * - Sidebar con navigazione interattiva (click per cambiare selezione)
 * - 4 stat cards con effetto hover
 * - Colori e styling fedeli al concept HTML
 * - Effetti di hover e transizioni
 * - Layout responsive con GridPane e HBox
 *
 * Per usare i colori burgundy del logo, sostituisci:
 * BURGUNDY_PRIMARY = "#6d1331"
 * BURGUNDY_GRADIENT_END = "#8b1538"
 */