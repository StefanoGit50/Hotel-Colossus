package it.unisa.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Componente Sidebar - Menu di navigazione laterale.
 * @author Team Hotel Colossus
 *
 */

public class SideBar extends VBox{

    // ===== COSTANTI =====
    public static final String DASHBOARD = "Dashboard";
    public static final String GUEST_MANAGEMENT = "Guest Management";
    public static final String PLANNING = "Planning";
    public static final String ROOMS = "Camere";
    public static final String CHECKOUT = "Check-out";

    // ===== ATTRIBUTI =====
    private VBox selectedNavItem = null;
    private Map<String, VBox> navItems = new HashMap<>();
    private Consumer<String> onNavigationChange;

    // ===== COSTRUTTORE =====
    public SideBar() {
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(8);
        this.setPrefWidth(188);
        this.setPadding(new Insets(16));
    }



    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        // Crea i menu items
        VBox dashboardItem = createNavItem("📊", DASHBOARD, true);
        VBox guestItem = createNavItem("👥", GUEST_MANAGEMENT, true);
        VBox planningItem = createNavItem("📅", PLANNING, false);
        VBox roomsItem = createNavItem("🛏️", ROOMS, false);

        // Salva i riferimenti
        navItems.put(DASHBOARD, dashboardItem);
        navItems.put(GUEST_MANAGEMENT, guestItem);
        navItems.put(PLANNING, planningItem);
        navItems.put(ROOMS, roomsItem);

        // Aggiungi alla sidebar
        this.getChildren().addAll(
                dashboardItem,
                guestItem,
                planningItem,
                roomsItem
        );
    }


    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("sidebar");
    }

    /**
     * Crea un singolo navigation item
     *
     * @param icon emoji o icona
     * @param text testo del menu
     * @param active se è selezionato di default
     * @return VBox contenente il nav item
     */
    private VBox createNavItem(String icon, String text, boolean active) {
        VBox navItem = new VBox();
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPadding(new Insets(20, 18, 14, 18)); //20
        navItem.getStyleClass().add("nav-item");

        // Contenuto (icona + testo)
        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-icon");

        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("nav-text");

        content.getChildren().addAll(iconLabel, textLabel);
        navItem.getChildren().add(content);

        // Se è attivo, marca come selezionato
        if (active) {
            navItem.getStyleClass().add("nav-item-active");
            selectedNavItem = navItem;
        }

        // Setup click handler
        setupNavItemClickHandler(navItem, text);

        return navItem;
    }

    /**
     * Setup del click handler per un nav item
     */
    private void setupNavItemClickHandler(VBox navItem, String itemName) {
        navItem.setOnMouseClicked(e -> {
            // Rimuovi active dal precedente
            if (selectedNavItem != null) {
                selectedNavItem.getStyleClass().remove("nav-item-active");
            }

            // Aggiungi active al nuovo
            navItem.getStyleClass().add("nav-item-active");
            selectedNavItem = navItem;

            // Notifica il cambiamento
            if (onNavigationChange != null) {
                onNavigationChange.accept(itemName);
            }

            System.out.println("🔄 Navigazione verso: " + itemName);
        });
    }

    // ===== METODI PUBBLICI =====

    /**
     * Imposta il callback da chiamare quando cambia la navigazione
     *
     * @param callback funzione che riceve il nome del menu selezionato
     */
    public void setOnNavigationChange(Consumer<String> callback) {
        this.onNavigationChange = callback;
    }

    /**
     * Seleziona programmaticamente un menu item
     *
     * @param itemName nome del menu da selezionare
     */
    public void selectItem(String itemName) {
        VBox item = navItems.get(itemName);
        if (item != null) {
            // Simula un click
            item.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1,
                    false, false, false, false,
                    true, false, false, false, false, false,
                    null
            ));
        }
    }

    /**
     * Restituisce il nome del menu attualmente selezionato
     *
     * @return nome del menu selezionato
     */
    public String getSelectedItem() {
        for (Map.Entry<String, VBox> entry : navItems.entrySet()) {
            if (entry.getValue() == selectedNavItem) {
                return entry.getKey();
            }
        }
        return null;
    }
}
