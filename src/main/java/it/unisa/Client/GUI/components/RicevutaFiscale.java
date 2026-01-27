package it.unisa.Client.GUI.components;

import java.time.LocalDate;

public class RicevutaFiscale {
    private int id;
    private String nomeCliente;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private double costoCamera;
    private double costoServizi;
    private double costoTrattamento;

    public RicevutaFiscale(int id, String nomeCliente, LocalDate dataInizio, LocalDate dataFine,
                           double costoCamera, double costoServizi, double costoTrattamento) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.costoCamera = costoCamera;
        this.costoServizi = costoServizi;
        this.costoTrattamento = costoTrattamento;
    }

    public int getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public LocalDate getDataInizio() { return dataInizio; }
    public LocalDate getDataFine() { return dataFine; }
    public double getCostoCamera() { return costoCamera; }
    public double getCostoServizi() { return costoServizi; }
    public double getCostoTrattamento() { return costoTrattamento; }

    public double getTotale() {
        return costoCamera + costoServizi + costoTrattamento;
    }
}