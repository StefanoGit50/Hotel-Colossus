package it.unisa.Client.GUI.components;


import it.unisa.Common.Prenotazione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Componente BookingCard - Card per visualizzare una prenotazione.
 * Mostra nome ospite, date, camera e piano pasti.
 *
 * Design Pattern: Component (riutilizzabile) + Observer (notifica click)
 *
 * @author Team Hotel Colossus
 * @version 1.0
 */
public class BookingCard extends HBox {

    // ===== COSTANTI =====
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===== COMPONENTI =====
    private Label nameLabel;
    private Label detailsLabel;
    private Label arrowLabel;

    // ===== DATI =====
    private Prenotazione booking;
    private Consumer<Prenotazione> onCardClick;

    // ===== COSTRUTTORE =====

    /**
     * Crea una booking card per una prenotazione
     *
     * @param booking prenotazione da visualizzare
     */
    public BookingCard(Prenotazione booking) {
        this.booking = booking;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents() {
        // Info Box
        VBox infoBox = new VBox(6);

        // Nome cliente
        nameLabel = new Label(booking.getIntestatario());
        nameLabel.getStyleClass().add("booking-name");

        // Dettagli (date + camera + piano pasti)
        String dates = booking.getDataInizio().format(DATE_FORMATTER) +
                " - " +
                booking.getDataFine().format(DATE_FORMATTER);
        String details = dates + " • Camera " + booking.getListaClienti().getFirst().getCamera() + " • " + booking.getTrattamento();

        detailsLabel = new Label(details);
        detailsLabel.getStyleClass().add("booking-details");

        infoBox.getChildren().addAll(nameLabel, detailsLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Arrow
        arrowLabel = new Label("→");
        arrowLabel.getStyleClass().add("booking-arrow");

        // Aggiungi tutto alla card
        this.getChildren().addAll(infoBox, spacer, arrowLabel);
    }

    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(12, 18, 12, 18));
    }

    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("booking-card");
    }

    /**
     * Setup degli event handlers
     */
    private void setupEventHandlers() {
        this.setOnMouseClicked(e -> {
            if (onCardClick != null) {
                onCardClick.accept(booking);
            }
            System.out.println("📋 Prenotazione selezionata: " + booking.getIntestatario());
        });
    }

    // ===== METODI PUBBLICI =====

    /**
     * Imposta il callback per il click sulla card
     *
     * @param callback funzione che riceve la prenotazione cliccata
     */
    public void setOnCardClick(Consumer<Prenotazione> callback) {
        this.onCardClick = callback;
    }

    /**
     * Ottiene la prenotazione associata a questa card
     *
     * @return prenotazione
     */
    public Prenotazione getBooking() {
        return booking;
    }

    /**
     * Aggiorna la card con una nuova prenotazione
     *
     * @param booking nuova prenotazione
     */
    public void setBooking(Prenotazione booking) {
        this.booking = booking;
        updateDisplay();
    }

    /**
     * Aggiorna la visualizzazione con i dati attuali
     */
    private void updateDisplay() {
        nameLabel.setText(booking.getIntestatario());

        String dates = booking.getDataInizio().format(DATE_FORMATTER) +
                " - " +
                booking.getDataFine().format(DATE_FORMATTER);
        String details = dates + " • Camera " + booking.getListaClienti().getFirst().getCamera() + " • " + booking.getTrattamento();

        detailsLabel.setText(details);
    }

    /**
     * Evidenzia la card (utile per ricerche o selezioni)
     */
    public void highlight() {
        this.setStyle("-fx-border-color: #6d1331; -fx-border-width: 3;");
    }

    /**
     * Rimuove l'evidenziazione
     */
    public void removeHighlight() {
        this.setStyle("");
    }

}