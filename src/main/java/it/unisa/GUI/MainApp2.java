package it.unisa.GUI;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

    public class MainApp2 extends Application {

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
            VBox.setVgrow(mainArea, Priority.ALWAYS);
            root.getChildren().add(mainArea);

            // Scene
            Scene scene = new Scene(root, 1400, 800);

            // Load CSS
            scene.getStylesheets().add(
                    getClass().getResource("/hotel2.css").toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private HBox createTopBar() {
            HBox topBar = new HBox(15);
            topBar.setPadding(new Insets(20, 30, 20, 30));
            topBar.setAlignment(Pos.CENTER_LEFT);
            topBar.getStyleClass().add("top-bar");

            // Logo Text a SINISTRA
            Label logo = new Label("HOTEL COLOSSUS");
            logo.getStyleClass().add("logo");

            // Spacer per spingere user info a destra
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // User info a DESTRA
            HBox userBox = createUserBox();

            topBar.getChildren().addAll(logo, spacer, userBox);

            return topBar;
        }

        private HBox createUserBox() {
            HBox userBox = new HBox(12);
            userBox.setAlignment(Pos.CENTER_RIGHT);
            userBox.getStyleClass().add("user-box");

            // User info (nome + ruolo)
            VBox userInfo = new VBox(2);
            userInfo.setAlignment(Pos.CENTER_RIGHT);

            Label userName = new Label("Receptionist1");
            userName.getStyleClass().add("user-name");

            Label userRole = new Label("Front Desk");
            userRole.getStyleClass().add("user-role");

            userInfo.getChildren().addAll(userName, userRole);

            // Avatar
            VBox avatar = new VBox();
            avatar.setAlignment(Pos.CENTER);
            avatar.setPrefSize(45, 45);
            avatar.setMaxSize(45, 45);
            avatar.setMinSize(45, 45);
            avatar.getStyleClass().add("avatar");

            Label avatarText = new Label("R1");
            avatarText.getStyleClass().add("avatar-text");
            avatar.getChildren().add(avatarText);

            userBox.getChildren().addAll(userInfo, avatar);

            // Context Menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem profileItem = new MenuItem("👤 Profilo");
            MenuItem settingsItem = new MenuItem("⚙️ Impostazioni");
            MenuItem logoutItem = new MenuItem("🚪 Logout");

            profileItem.setOnAction(e -> System.out.println("Profilo cliccato"));
            settingsItem.setOnAction(e -> System.out.println("Impostazioni cliccato"));
            logoutItem.setOnAction(e -> System.out.println("Logout cliccato"));

            contextMenu.getItems().addAll(profileItem, settingsItem, logoutItem);

            // Menu appare esattamente sotto l'avatar
            userBox.setOnMouseClicked(e -> {
                if (!contextMenu.isShowing()) {
                    double x = avatar.localToScreen(avatar.getBoundsInLocal()).getMinX();
                    double y = avatar.localToScreen(avatar.getBoundsInLocal()).getMaxY();
                    contextMenu.show(avatar, x, y);
                } else {
                    contextMenu.hide();
                }
            });

            return userBox;
        }

        private HBox createMainArea() {
            HBox mainArea = new HBox(20);
            HBox.setHgrow(mainArea, Priority.ALWAYS);

            // Sidebar
            VBox sidebar = createSidebar();

            // Content Area
            VBox contentArea = createContentArea();
            HBox.setHgrow(contentArea, Priority.ALWAYS);

            mainArea.getChildren().addAll(sidebar, contentArea);
            return mainArea;
        }

        private VBox createSidebar() {
            VBox sidebar = new VBox(8);
            sidebar.setPrefWidth(250);
            sidebar.setPadding(new Insets(20));
            sidebar.getStyleClass().add("sidebar");

            // Menu items
            VBox dashboardItem = createNavItem("📊", "Dashboard", true);
            VBox guestItem = createNavItem("👥", "Clienti", false);
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
            navItem.setPadding(new Insets(14, 18, 14, 18));
            navItem.getStyleClass().add("nav-item");

            HBox content = new HBox(12);
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
            VBox contentArea = new VBox(25);
            contentArea.setPadding(new Insets(30));
            contentArea.getStyleClass().add("content-area");
            VBox.setVgrow(contentArea, Priority.ALWAYS);

            // Stats Grid
            GridPane statsGrid = createStatsGrid();

            // Section Title
            Label sectionTitle = new Label("Prossime Prenotazioni");
            sectionTitle.getStyleClass().add("section-title");

            // Placeholder text
            Label placeholder = new Label("Lista prenotazioni in arrivo...");
            placeholder.getStyleClass().add("placeholder-text");

            contentArea.getChildren().addAll(statsGrid, sectionTitle, placeholder);
            return contentArea;
        }

        private GridPane createStatsGrid() {
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.getStyleClass().add("stats-grid");

            // Stat cards SENZA EMOJI
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
            VBox card = new VBox(15);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(25));
            card.setPrefHeight(140);
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

