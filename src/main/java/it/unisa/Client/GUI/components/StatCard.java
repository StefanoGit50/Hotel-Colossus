package it.unisa.Client.GUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Componente StatCard - Card per visualizzare statistiche.
 * Mostra un numero e una label descrittiva.
 *
 * @author Team Hotel Colossus
 * @version 1.0
 */
public class StatCard extends VBox {

    // ===== COMPONENTI =====
    private Label numberLabel;
    private Label descriptionLabel;

    // ===== COSTRUTTORE =====

    /**
     * Crea una stat card con numero e descrizione
     *
     * @param number numero da visualizzare
     * @param description descrizione della statistica
     */
    public StatCard(String number, String description) {
        initializeComponents(number, description);
        setupLayout();
        setupStyling();
    }


    /**
     * Setup del layout
     */
    private void setupLayout() {
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(20));
        this.setPrefHeight(120);
    }

    /**
     * Costruttore con valori numerici
     *
     * @param number numero intero da visualizzare
     * @param description descrizione della statistica
     */
    public StatCard(int number, String description) {
        this(String.valueOf(number), description);
    }

    /**
     * Inizializza i componenti
     */
    private void initializeComponents(String number, String description) {
        // Number Label
        numberLabel = new Label(number);
        numberLabel.getStyleClass().add("stat-number");

        // Description Label
        descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("stat-label");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setAlignment(Pos.CENTER);

        this.getChildren().addAll(numberLabel, descriptionLabel);
    }


    /**
     * Setup dello styling
     */
    private void setupStyling() {
        this.getStyleClass().add("stat-card");
    }

    // ===== METODI PUBBLICI =====

    /**
     * Aggiorna il numero visualizzato
     *
     * @param number nuovo numero
     */
    public void setNumber(String number) {
        this.numberLabel.setText(number);
    }

    /**
     * Aggiorna il numero visualizzato (int)
     *
     * @param number nuovo numero
     */
    public void setNumber(int number) {
        setNumber(String.valueOf(number));
    }

    /**
     * Aggiorna la descrizione
     *
     * @param description nuova descrizione
     */
    public void setDescription(String description) {
        this.descriptionLabel.setText(description);
    }

    /**
     * Ottiene il numero attuale
     *
     * @return numero come stringa
     */
    public String getNumber() {
        return numberLabel.getText();
    }

    /**
     * Ottiene la descrizione attuale
     *
     * @return descrizione
     */
    public String getDescription() {
        return descriptionLabel.getText();
    }

    /**
     * Imposta un click handler sulla card
     *
     * @param handler azione da eseguire al click
     */
    public void setOnCardClick(Runnable handler) {
        this.setOnMouseClicked(e -> handler.run());
    }
}