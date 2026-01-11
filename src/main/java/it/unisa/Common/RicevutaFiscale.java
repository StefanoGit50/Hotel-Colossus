package it.unisa.Common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta una ricevuta fiscale emessa per il soggiorno e i servizi
 * usufruiti da un cliente presso la struttura.
 */
public class RicevutaFiscale implements Serializable, Cloneable {

    /** Identificativo univoco della ricevuta fiscale. */
    private int IDRicevutaFiscale;

    /** Identificativo della prenotazione a cui la ricevuta fa riferimento. */
    Prenotazione IDPrenotazione;

    /** Importo complessivo della ricevuta (somma delle voci). */
    private double Totale;

    /** Data in cui il documento fiscale è stato emesso. */
    private LocalDate dataEmissione;

    /**
     * Inizializza una nuova ricevuta con i dati identificativi e il totale.
     * @param IDRicevutaFiscale l'identificativo univoco della ricevuta.
     * @param IDPrenotazione l'identificativo della prenotazione associata.
     * @param totale l'importo complessivo del documento.
     * @param dataEmissione la data di emissione della ricevuta.
     */
    public RicevutaFiscale(int IDRicevutaFiscale, Prenotazione IDPrenotazione, double totale, LocalDate dataEmissione) {
        this.IDRicevutaFiscale = IDRicevutaFiscale;
        this.IDPrenotazione = IDPrenotazione;
        this.Totale = totale;
        this.dataEmissione = dataEmissione;
    }

    /**
     * Costruttore vuoto.
     */
    public RicevutaFiscale() {};

    /**
     * @return l'identificativo univoco della ricevuta.
     */
    public int getIDRicevutaFiscale() {
        return IDRicevutaFiscale;
    }

    /**
     * @param IDRicevutaFiscale l'identificativo da assegnare.
     */
    public void setIDRicevutaFiscale(int IDRicevutaFiscale) {
        this.IDRicevutaFiscale = IDRicevutaFiscale;
    }

    /**
     * @return l'importo totale della ricevuta.
     */
    public double getTotale() {
        return Totale;
    }

    /**
     * @param totale l'importo complessivo da impostare.
     */
    public void setTotale(double totale) {
        Totale = totale;
    }

    /**
     * @return la data di emissione del documento.
     */
    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    /**
     * @param dataEmissione la data di emissione da impostare.
     */
    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    /**
     * @return l'id univoco della prenotazione.
     */
    public int getIDPrenotazione() {
        return IDPrenotazione.getIDPrenotazione();
    }

    /**
     * @param IDPrenotazione l'id univoco della prenotazione.
     */
    public void setIDPrenotazione(int IDPrenotazione) {
        this.IDPrenotazione.setIDPrenotazione(IDPrenotazione);
    }

    /**
     * Crea e restituisce una copia di questo oggetto.
     */
    @Override
    public RicevutaFiscale clone() {
        try {
            return (RicevutaFiscale) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Confronta l'oggetto con quello passato per verificarne l'uguaglianza.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RicevutaFiscale that = (RicevutaFiscale) o;
        return IDRicevutaFiscale == that.IDRicevutaFiscale && IDPrenotazione == that.IDPrenotazione &&
                Double.compare(Totale, that.Totale) == 0 && Objects.equals(dataEmissione, that.dataEmissione);
    }

    /**
     * Restituisce una rappresentazione testuale della ricevuta.
     */
    @Override
    public String toString() {
        return "RicevutaFiscale{" +
                "IDRicevutaFiscale=" + IDRicevutaFiscale +
                ", IDPrenotazione=" + IDPrenotazione +
                ", Totale=" + Totale +
                ", dataEmissione=" + dataEmissione +
                '}';
    }
}