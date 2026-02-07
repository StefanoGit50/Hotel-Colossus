package it.unisa.Common;

import it.unisa.Server.persistent.util.Stato;
import it.unisa.Server.persistent.util.*;
import org.apache.logging.log4j.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Rappresenta una Prenotazione di un soggiorno in hotel.
 * Contiene i dettagli della prenotazione, le date di validità, i documenti
 * associati e le liste degli elementi chiave (Camere, Servizi, Clienti).
 */
public class Prenotazione implements Cloneable, Serializable {

    private static final Logger log = LogManager.getLogger(Prenotazione.class);
    /**
     * Codice univoco della prenotazione.
     */
    private Integer IDPrenotazione;
    /**
     *  La cittadinanza del cliente
     */
    private String cittadinanza;

    /**
     *  il metodo Di Pagamento del cliente
     */
    private String metodoDiPagamento;

    /**
     * Data in cui è stata creata la prenotazione.
     */
    private LocalDate dataCreazionePrenotazione;

    /**
     * Data di inizio validità della prenotazione (check-in previsto).
     */
    private LocalDate dataInizio;

    /**
     * Data di fine validità della prenotazione (check-out previsto).
     */
    private LocalDate dataFine;

    /**
     * Oggetto che descrive il trattamento riservato (es. Mezza Pensione, Pensione Completa).
     */
    private Trattamento trattamento;

    /**
     * Tipo di documento fornito per la prenotazione (es. Carta di Credito, Voucher).
     */
    private String tipoDocumento;

    /**
     * Data di rilascio del documento/garanzia.
     */
    private LocalDate dataRilascio;

    /**
     * Data di scadenza del documento/garanzia.
     */
    private LocalDate dataScadenza;

    /**
     * Nome del cliente intestatario della prenotazione.
     */
    private String intestatario;

    /**
     * Note aggiuntive relative alla prenotazione.
     */
    private String noteAggiuntive;

    /**
     * Il numero del documento del cliente
     */
    private String numeroDocumento;

    private LocalDate dataEmissioneRicevuta;

    /**
     * Lista dei servizi aggiuntivi inclusi nella prenotazione.
     */
    private ArrayList<Servizio> listaServizi;

    /**
     * Lista dei clienti che soggiorneranno.
     */
    private ArrayList<Cliente> listaClienti;
    /**
     * Indica lo stato della prenotazione
     */
    private boolean statoPrenotazione;
    /**
     * Dice se è stato fatto il check-In della prenotazione
     */
    private boolean checkIn;

    private Double prezzoAcquistoTrattamento;

    /**
     * Costruttore completo per creare una nuova istanza di {@code Prenotazione}.
     *
     * @param dataCreazionePrenotazione Data di creazione della prenotazione.
     * @param dataInizioPrenotazione Data di inizio del soggiorno.
     * @param dataFinePrenotazione Data di fine del soggiorno.
     * @param trattamento Il trattamento (es. Pensione completa).
     * @param tipoDocumento Tipo di documento di garanzia.
     * @param dataRilascio Data di rilascio del documento.
     * @param dataScadenza Data di scadenza del documento.
     * @param intestatario Nome dell'intestatario della prenotazione.
     * @param noteAggiuntive Note aggiuntive.
     * @param listaServizi Lista dei servizi aggiuntivi.
     * @param listaClienti Lista dei clienti.
     * @param numeroDocumento il numero del documento.
     */
    public Prenotazione(LocalDate dataCreazionePrenotazione, LocalDate dataInizioPrenotazione, LocalDate dataFinePrenotazione,LocalDate dataEmissioneRicevuta,
                         Trattamento trattamento,Double prezzoAcquistoTrattamento, String tipoDocumento, LocalDate dataRilascio, LocalDate dataScadenza,
                         String intestatario, String noteAggiuntive, ArrayList<Servizio> listaServizi,
                         ArrayList<Cliente> listaClienti ,String numeroDocumento, String metodoPagamento, String cittadinanza) {
        this.IDPrenotazione = 0; //ID è AUTOINCREMENT
        this.dataCreazionePrenotazione = dataCreazionePrenotazione;
        this.dataInizio = dataInizioPrenotazione;
        this.dataFine = dataFinePrenotazione;
        this.trattamento = trattamento;
        this.tipoDocumento = tipoDocumento;
        this.dataRilascio = dataRilascio;
        this.dataScadenza = dataScadenza;
        this.intestatario = intestatario;
        this.noteAggiuntive = noteAggiuntive;
        // Uso di deep copy per l'incapsulamento delle liste
        this.listaServizi = Util.deepCopyArrayList(listaServizi);
        this.listaClienti = Util.deepCopyArrayList(listaClienti);
        this.numeroDocumento = numeroDocumento;
        this.statoPrenotazione = true;
        this.checkIn = false;
        this.dataEmissioneRicevuta = dataEmissioneRicevuta;
        this.metodoDiPagamento= metodoPagamento;
        this.prezzoAcquistoTrattamento = prezzoAcquistoTrattamento;
        this.cittadinanza= cittadinanza;
    }

    public Prenotazione() {
        this.IDPrenotazione = 0;
        this.numeroDocumento = "";
        this.dataCreazionePrenotazione = LocalDate.now();
        this.dataFine = LocalDate.now();
        this.dataInizio = LocalDate.now();
        this.dataRilascio = LocalDate.now();
        this.dataScadenza = LocalDate.now();
        this.listaClienti = new ArrayList<>();
        this.listaServizi = new ArrayList<>();
        this.noteAggiuntive = "";
        this.trattamento = new Trattamento();
        this.intestatario = "";
        this.statoPrenotazione = false;
        this.metodoDiPagamento= "";
    }

    /**
     * Aggiunge un cliente alla lista dei clienti.
     *
     * @param cliente Il {@code Cliente} da aggiungere.
     */
    public void aggiungiCliente(Cliente cliente) {
        if (cliente != null) {
            try {
                this.listaClienti.add(cliente.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cliente non clonabile", e);
            }
        }
    }

    /**
     * Rimuove un cliente dalla lista dei clienti basandosi sull'uguaglianza.
     *
     * @param cliente Il {@code Cliente} da rimuovere.
     * @return {@code true} se il cliente è stato rimosso, {@code false} altrimenti.
     */
    public boolean rimuoviCliente(Cliente cliente) {
        return this.listaClienti.remove(cliente);
    }

    /**
     * Aggiunge un servizio alla lista dei servizi.
     *
     * @param servizio Il {@code Servizio} da aggiungere.
     */
    public void aggiungiServizio(Servizio servizio) {
        if (servizio != null) {
            try {
                this.listaServizi.add(servizio.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Servizio non clonabile", e);
            }
        }
    }

    /**
     * Rimuove un servizio dalla lista dei servizi basandosi sull'uguaglianza.
     *
     * @param servizio Il {@code Servizio} da rimuovere.
     * @return {@code true} se il servizio è stato rimosso, {@code false} altrimenti.
     */
    public boolean rimuoviServizio(Servizio servizio) {
        return this.listaServizi.remove(servizio);
    }



    /**
     * Esegue l'operazione di check-out e genera la relativa ricevuta fiscale.
     *
     * @return L'oggetto {@code RicevutaFiscale2} generato.
     */
    //public RicevutaFiscale checkout() {
    // Implementazione logica del check-out
    //    return new RicevutaFiscale(); // Assumendo esista una classe RicevutaFiscale2
    //  }


    // --- Getter e Setter ---
    // I getter per le liste restituiscono deep copy per l'incapsulamento

    public void setDataEmissioneRicevuta(LocalDate dataEmissioneRicevuta) {
        this.dataEmissioneRicevuta = dataEmissioneRicevuta;
    }

    public void setPrezzoAcquistoTrattamento(Double prezzoAcquistoTrattamento) {
        this.prezzoAcquistoTrattamento = prezzoAcquistoTrattamento;
    }

    public void setMetodoDiPagamento(String metodoDiPagamento) {
        this.metodoDiPagamento = metodoDiPagamento;
    }

    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    public String getCittadinanza() {
        return cittadinanza;
    }

    public boolean isStatoPrenotazione() {
        return statoPrenotazione;
    }

    public Double getPrezzoAcquistoTrattamento() {
        return prezzoAcquistoTrattamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoDiPagamento = metodoPagamento;
    }

    public String getMetodoPagamento() {
        return metodoDiPagamento;
    }

    public LocalDate getDataEmissioneRicevuta() {
        return dataEmissioneRicevuta;
    }
    public void setIDPrenotazione(Integer IDPrenotazione) {
        this.IDPrenotazione = IDPrenotazione;
    }

    public Integer getIDPrenotazione() {
        return IDPrenotazione;
    }

    public LocalDate getDataCreazionePrenotazione() {
        return dataCreazionePrenotazione;
    }

    public void setDataCreazionePrenotazione(LocalDate dataCreazionePrenotazione) {
        this.dataCreazionePrenotazione = dataCreazionePrenotazione;
    }

    public boolean getStatoPrenotazione(){
        return this.statoPrenotazione;
    }

    public  void setStatoPrenotazione(boolean st){
        this.statoPrenotazione = st;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public Trattamento getTrattamento() {
        return trattamento;
    }

    public void setTrattamento(Trattamento trattamento) {
        this.trattamento = trattamento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(LocalDate dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }

    public String getNoteAggiuntive() {
        return noteAggiuntive;
    }

    public void setNoteAggiuntive(String noteAggiuntive) {
        this.noteAggiuntive = noteAggiuntive;
    }

    public void setNumeroDocumento(String numeroDocumento){
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroDocumento(){
        return numeroDocumento;
    }
    /**
     * Restituisce una deep copy della lista delle camere.
     *
     *
     * Imposta la lista delle camere, creando una deep copy della lista fornita.
     *
     * @param listaCamere La lista delle camere da copiare.
     */
    /**
     * Restituisce una deep copy della lista dei servizi.
     *
     * @return Una nuova ArrayList contenente i cloni dei servizi.
     */
    public ArrayList<Servizio> getListaServizi() {
        return Util.deepCopyArrayList(listaServizi);
    }

    /**
     * Imposta la lista dei servizi, creando una deep copy della lista fornita.
     *
     * @param listaServizi La lista dei servizi da copiare.
     */
    public void setListaServizi(ArrayList<Servizio> listaServizi) {
        this.listaServizi = Util.deepCopyArrayList(listaServizi);
    }

    /**
     * Restituisce una deep copy della lista dei clienti.
     *
     * @return Una nuova ArrayList contenente i cloni dei clienti.
     */
    public ArrayList<Cliente> getListaClienti() {
        return Util.deepCopyArrayList(listaClienti);
    }

    /**
     * Imposta la lista dei clienti, creando una deep copy della lista fornita.
     *
     * @param listaClienti La lista dei clienti da copiare.
     */
    public void setListaClienti(ArrayList<Cliente> listaClienti) {
        this.listaClienti = Util.deepCopyArrayList(listaClienti);
    }

    public boolean isCheckIn() {
        return checkIn;
    }


    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    // --- Metodi Standard di Object ---

    @Override
    public String toString() {
        return "Prenotazione{" +
                "IDPrenotazione=" + IDPrenotazione +
                ", cittadinanza='" + cittadinanza + '\'' +
                ", metodoDiPagamento='" + metodoDiPagamento + '\'' +
                ", dataCreazionePrenotazione=" + dataCreazionePrenotazione +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", trattamento=" + trattamento +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", dataRilascio=" + dataRilascio +
                ", dataScadenza=" + dataScadenza +
                ", intestatario='" + intestatario + '\'' +
                ", noteAggiuntive='" + noteAggiuntive + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", dataEmissioneRicevuta=" + dataEmissioneRicevuta +
                ", listaServizi=" + listaServizi +
                ", listaClienti=" + listaClienti +
                ", statoPrenotazione=" + statoPrenotazione +
                ", checkIn=" + checkIn +
                ", prezzoAcquistoTrattamento=" + prezzoAcquistoTrattamento +
                '}';
    }


    /**
     * Indica se un altro oggetto è "uguale a" questa prenotazione.
     * Il confronto è basato sul codice di prenotazione e su tutti gli attributi.
     *
     * @param o L'oggetto da confrontare.
     * @return {@code true} se le due prenotazioni sono uguali, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prenotazione that = (Prenotazione) o;

        // Compare primitive types
        if (statoPrenotazione != that.statoPrenotazione) return false;
        if (checkIn != that.checkIn) return false;
        System.out.println(prezzoAcquistoTrattamento);
        System.out.println(that.prezzoAcquistoTrattamento);
        if(prezzoAcquistoTrattamento != null && that.prezzoAcquistoTrattamento != null){
            if (Double.compare(that.prezzoAcquistoTrattamento, prezzoAcquistoTrattamento) != 0) return false;
        }else{
            return false;
        }
        // Compare objects using Objects.equals (handles null safely)
        if (!Objects.equals(IDPrenotazione, that.IDPrenotazione)) return false;
        if (!Objects.equals(cittadinanza, that.cittadinanza)) return false;
        if (!Objects.equals(metodoDiPagamento, that.metodoDiPagamento)) return false;
        if (!Objects.equals(dataCreazionePrenotazione, that.dataCreazionePrenotazione)) return false;
        if (!Objects.equals(dataInizio, that.dataInizio)) return false;
        if (!Objects.equals(dataFine, that.dataFine)) return false;
        if (!Objects.equals(trattamento, that.trattamento)) return false;
        if (!Objects.equals(tipoDocumento, that.tipoDocumento)) return false;
        if (!Objects.equals(dataRilascio, that.dataRilascio)) return false;
        if (!Objects.equals(dataScadenza, that.dataScadenza)) return false;
        if (!Objects.equals(intestatario, that.intestatario)) return false;
        if (!Objects.equals(noteAggiuntive, that.noteAggiuntive)) return false;
        if (!Objects.equals(numeroDocumento, that.numeroDocumento)) return false;
        if (!Objects.equals(dataEmissioneRicevuta, that.dataEmissioneRicevuta)) return false;

        // Compare lists
        if (! (listaServizi.containsAll(that.listaServizi) && listaServizi.size() == that.listaServizi.size())) return false;
        if (! (listaClienti.containsAll(that.listaClienti) && listaClienti.size() == that.listaClienti.size())) return false;

        return true;
    }

    /**
     * Helper method to compare two lists for equality, handling null cases
     */
    private <T> boolean compareListsEqual(ArrayList<T> list1, ArrayList<T> list2) {
        if (list1 == null && list2 == null) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;

        for (int i = 0; i < list1.size(); i++) {
            if (!Objects.equals(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDPrenotazione, dataCreazionePrenotazione, dataInizio, dataFine, trattamento, tipoDocumento, dataRilascio, dataScadenza, intestatario, noteAggiuntive, numeroDocumento,  listaServizi, listaClienti, statoPrenotazione, checkIn);
    }

    /**
     * Crea e restituisce una copia profonda (deep clone) dell'oggetto {@code Prenotazione}.
     * Vengono clonati anche gli oggetti {@code Trattamento} e le liste interne.
     *
     * @return Una copia (clone) dell'oggetto {@code Prenotazione}.
     * @throws CloneNotSupportedException Se un attributo non clonabile è presente o la clonazione fallisce.
     */
    @Override
    public Prenotazione clone(){
        try{
            Prenotazione cloned = (Prenotazione) super.clone();

        // Clonazione degli oggetti interni mutabili (Trattamento e Liste)
        if (this.trattamento != null) {
            // Assumiamo che Trattamento sia clonabile
            cloned.trattamento = this.trattamento.clone();
        }

        cloned.listaServizi = Util.deepCopyArrayList(this.listaServizi);
        cloned.listaClienti = Util.deepCopyArrayList(this.listaClienti);

            return cloned;
        }catch (CloneNotSupportedException cloneNotSupportedException){
            throw new RuntimeException(cloneNotSupportedException);
        }
    }
}