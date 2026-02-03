package it.unisa.Common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Rappresenta una ricevuta fiscale emessa per il soggiorno e i servizi
 * usufruiti da un cliente presso la struttura.
 */
public class RicevutaFiscale implements Serializable, Cloneable {

    /** Identificativo univoco della ricevuta fiscale. */
    private int IDRicevutaFiscale;

    /** Identificativo della prenotazione a cui la ricevuta fa riferimento. */
    private Prenotazione prenotazione;

    /** Importo complessivo della ricevuta (somma delle voci). */
    private double Totale;

    /** Data in cui il documento fiscale è stato emesso. */
    private LocalDate dataEmissione;

    /** Metodo di pagamento scelto dal Cliente */
    private String metodoPagamento;

    /** Data della Prenotazione */
    private LocalDate dataPrenotazione;

    /** Prezzo del trattamento*/
    private double prezzoTrattamento;

    /** la tipologia del trattamento*/
    private String tipoTrattamento;
    /** la lista delle camere */
    private ArrayList<Camera> cameras;
    /** la lista dei clienti */
    private ArrayList<Cliente> clientes;
    /** la lista dei servizi */
    private ArrayList<Servizio> servizios;
    /**
     * Inizializza una nuova ricevuta con i dati identificativi e il totale.
     * @param IDRicevutaFiscale l'identificativo univoco della ricevuta.
     * @param prenotazione l'identificativo della prenotazione associata.
     * @param totale l'importo complessivo del documento.
     * @param dataEmissione la data di emissione della ricevuta.
     * @param metodoPagamento il metodo di pagamento
     *
     *
     */
    public RicevutaFiscale(int IDRicevutaFiscale, Prenotazione prenotazione, double totale, LocalDate dataEmissione ,String metodoPagamento,LocalDate dataPrenotazione ,double prezzoTrattamento , String tipoTrattamento){
        this.IDRicevutaFiscale = IDRicevutaFiscale;
        this.prenotazione = prenotazione;
        this.Totale = totale;
        this.dataEmissione = dataEmissione;
        this.metodoPagamento = metodoPagamento;
        this.dataPrenotazione = dataPrenotazione;
        this.prezzoTrattamento = prezzoTrattamento;
        this.tipoTrattamento = tipoTrattamento;
    }

    /**
     * Costruttore vuoto.
     */
    public RicevutaFiscale() {};

    /**
     *
     * @post result == IDRicevutaFiscale
     *
     * @return l'identificativo univoco della ricevuta.
     */
    public int getIDRicevutaFiscale() {
        return IDRicevutaFiscale;
    }

    /**
     *
     * @pre IDRicevutaFiscale != null
     * @post this.IDRicevutaFiscale == IDRicevutaFiscale
     *
     * @param IDRicevutaFiscale l'identificativo da assegnare.
     */
    public void setIDRicevutaFiscale(int IDRicevutaFiscale) {
        this.IDRicevutaFiscale = IDRicevutaFiscale;
    }

    /**
     *
     * @post result == Totale
     *
     * @return l'importo totale della ricevuta.
     */
    public double getTotale() {
        return Totale;
    }

    /**
     *
     * @pre totale != null
     * @post Totale == totale
     *
     * @param totale l'importo complessivo da impostare.
     */
    public void setTotale(double totale) {
        Totale = totale;
    }

    /**
     *
     * @post result == dataEmissione
     *
     * @return la data di emissione del documento.
     */
    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    /**
     *
     * @pre dataEmissione != null
     * @post this.dataEmissione == dataEmissione
     *
     * @param dataEmissione la data di emissione da impostare.
     */
    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    /**
     *
     * @post result == prenotazione
     *
     * @return l'id univoco della prenotazione.
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     *
     * @pre prenotazione != null
     * @post this.prenotazione == prenotazione
     *
     * @param prenotazione l'id univoco della prenotazione.
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    /**
     *
     * @post result == metodoPagamento
     *
     * @return metodoPagamento ritorna il metodo di pagamento scelto dal Cliente
     */
    public String getMetodoPagamento() {
        return metodoPagamento;
    }


    /**
     *
     * @pre metodoPagamento != null && metodoPagamento != ""
     * @post this.metodoPagamento == metodoPagamento
     *
     * @param metodoPagamento è il metodo di pagamento scelto dal cliente
     * */
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }


    /**
     *
     * @post result == dataPrenotazione
     *
     * @return datPrenotazione restituisce il la data della prenotazione
     * */
    public LocalDate getDataPrenotazione() {
        return dataPrenotazione;
    }
    /**
     *
     * @pre dataPrenotazione != null
     * @post this.dataPrenotazione == dataPrenotazione
     *
     * @param dataPrenotazione la data della prenotazione
     * */
    public void setDataPrenotazione(LocalDate dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }
    /**
     *
     * @post result == prezzoTrattamento
     *
     * @return prezzoTrattamento il prezzo del trattamento
     * */
    public double getPrezzoTrattamento() {
        return prezzoTrattamento;
    }

    /**
     *
     * @pre prezzoTrattamento != null
     * @post this.prezzoTrattamento == prezzoTrattamento
     *
     * @param prezzoTrattamento il prezzo del trattamento
     * */
    public void setPrezzoTrattamento(double prezzoTrattamento) {
        this.prezzoTrattamento = prezzoTrattamento;
    }


    /**
     *
     * @post result == tipoTrattamento
     *
     * @return tipoTrattamento il tipo di trattamento
     * */
    public String getTipoTrattamento() {
        return tipoTrattamento;
    }

    /**
     *
     * @pre tipoTrattamento != null && tipoTrattamento != ""
     * @post this.tipoTrattamento == tipoTrattamento
     *
     * @param tipoTrattamento il tipo di trattamento
     * */
    public void setTipoTrattamento(String tipoTrattamento) {
        this.tipoTrattamento = tipoTrattamento;
    }


    /**
     * Imposta servizios.
     *
     * @param servizios
     * @pre servizios != null
     * @post this.servizios.stream().allMatch(s | servizios.contains(s))
     */
    public void setServizios(ArrayList<Servizio> servizios){
        for(Servizio servizio: servizios){
            try{
                this.servizios.add(servizio.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException){
                throw new RuntimeException();
            }
        }
    }


    /**
     * Imposta cameras.
     *
     * @param cameras
     * @pre cameras != null
     * @post this.cameras.stream().allMatch(c | cameras.contains(c))
     */
    public void setCameras(ArrayList<Camera> cameras) {
        for(Camera camera: cameras){
            try{
                this.cameras.add(camera.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException){
                throw new RuntimeException();
            }
        }
    }


    /**
     * Imposta clientes.
     *
     * @param clientes
     * @pre clientes != null
     * @post this.clientes.stream().allMatch(c | clientes.contains(c))
     */
    public void setClientes(ArrayList<Cliente> clientes){
        for (Cliente cliente: clientes){
            try{
                this.clientes.add(cliente.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new RuntimeException(cloneNotSupportedException);
            }
        }
    }


    /**
     * Restituisce il valore di clientes.
     *
     * @post result.stream().allMatch(c | clientes.contains(c))
     * @return clientes
     */
    public ArrayList<Cliente> getClientes(){
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        for(Cliente cliente: clientes){
            try{
                clientes1.add(cliente.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException){
                throw new RuntimeException();
            }
        }
        return clientes1;
    }


    /**
     * Restituisce il valore di cameras.
     *
     * @post result.stream().allMatch(c | cameras.contains(c))
     * @return cameras
     */
    public ArrayList<Camera> getCameras(){
        ArrayList<Camera> cameras1 = new ArrayList<>();
        for(Camera camera: cameras){
            try{
                cameras1.add(camera.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException){
                throw new RuntimeException();
            }
        }
        return cameras1;
    }


    /**
     * Restituisce il valore di servizi.
     *
     * @post result.stream().allMatch(s | servizios.contains(s))
     * @return servizi
     */
    public ArrayList<Servizio> getServizi(){
        ArrayList<Servizio> servizios1 = new ArrayList<>();
        for(Servizio servizio: servizios){
            try{
                servizios1.add(servizio.clone());
            }catch (CloneNotSupportedException cloneNotSupportedException){
                throw new RuntimeException();
            }
        }
        return servizios1;
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
        return IDRicevutaFiscale == that.IDRicevutaFiscale && prenotazione.equals(that.prenotazione) &&
                Double.compare(Totale, that.Totale) == 0 && Objects.equals(dataEmissione, that.dataEmissione);
    }

    /**
     * Restituisce una rappresentazione testuale della ricevuta.
     */
    @Override
    public String toString() {
        return "RicevutaFiscale{" +
                "IDRicevutaFiscale=" + IDRicevutaFiscale +
                ", prenotazione=" + prenotazione +
                ", Totale=" + Totale +
                ", dataEmissione=" + dataEmissione +
                '}';
    }
}