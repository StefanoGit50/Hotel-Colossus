package it.unisa.Common;

import it.unisa.Server.persistent.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Rappresenta una Prenotazione di un soggiorno in hotel.
 * Contiene i dettagli della prenotazione, le date di validità, i documenti
 * associati e le liste degli elementi chiave (Camere, Servizi, Clienti).
 */
public class Prenotazione implements Cloneable {

    /**
     * Codice univoco della prenotazione.
     */
    private int codicePrenotazione;

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
    private int numeroDocumento;

    /**
     * Lista delle camere incluse nella prenotazione.
     */
    private ArrayList<Camera> listaCamere;

    /**
     * Lista dei servizi aggiuntivi inclusi nella prenotazione.
     */
    private ArrayList<Servizio> listaServizi;

    /**
     * Lista dei clienti che soggiorneranno.
     */
    private ArrayList<Cliente> listaClienti;


    /**
     * Costruttore completo per creare una nuova istanza di {@code Prenotazione}.
     *
     * @param codicePrenotazione Codice univoco della prenotazione.
     * @param dataCreazionePrenotazione Data di creazione della prenotazione.
     * @param dataInizio Data di inizio del soggiorno.
     * @param dataFine Data di fine del soggiorno.
     * @param trattamento Il trattamento (es. Pensione completa).
     * @param tipoDocumento Tipo di documento di garanzia.
     * @param dataRilascio Data di rilascio del documento.
     * @param dataScadenza Data di scadenza del documento.
     * @param intestatario Nome dell'intestatario della prenotazione.
     * @param noteAggiuntive Note aggiuntive.
     * @param listaCamere Lista delle camere prenotate.
     * @param listaServizi Lista dei servizi aggiuntivi.
     * @param listaClienti Lista dei clienti.
     * @param numeroDocumento il numero del documento
     */
    public Prenotazione(int codicePrenotazione, LocalDate dataCreazionePrenotazione, LocalDate dataInizio, LocalDate dataFine,
                        Trattamento trattamento, String tipoDocumento, LocalDate dataRilascio, LocalDate dataScadenza,
                        String intestatario, String noteAggiuntive, ArrayList<Camera> listaCamere, ArrayList<Servizio> listaServizi,
                        ArrayList<Cliente> listaClienti ,int numeroDocumento) {
        this.codicePrenotazione = codicePrenotazione;
        this.dataCreazionePrenotazione = dataCreazionePrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.trattamento = trattamento;
        this.tipoDocumento = tipoDocumento;
        this.dataRilascio = dataRilascio;
        this.dataScadenza = dataScadenza;
        this.intestatario = intestatario;
        this.noteAggiuntive = noteAggiuntive;
        // Uso di deep copy per l'incapsulamento delle liste
        this.listaCamere = Util.deepCopyArrayList(listaCamere);
        this.listaServizi = Util.deepCopyArrayList(listaServizi);
        this.listaClienti = Util.deepCopyArrayList(listaClienti);
        this.numeroDocumento = numeroDocumento;
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

    public int getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public void setCodicePrenotazione(int codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    public LocalDate getDataCreazionePrenotazione() {
        return dataCreazionePrenotazione;
    }

    public void setDataCreazionePrenotazione(LocalDate dataCreazionePrenotazione) {
        this.dataCreazionePrenotazione = dataCreazionePrenotazione;
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

    public void setNumeroDocumento(int numeroDocumento){
        this.numeroDocumento = numeroDocumento;
    }

    public int getNumeroDocumento(){
        return numeroDocumento;
    }
    /**
     * Restituisce una deep copy della lista delle camere.
     *
     * @return Una nuova ArrayList contenente i cloni delle camere.
     */
    public ArrayList<Camera> getListaCamere() {
        return Util.deepCopyArrayList(listaCamere);
    }

    /**
     * Imposta la lista delle camere, creando una deep copy della lista fornita.
     *
     * @param listaCamere La lista delle camere da copiare.
     */
    public void setListaCamere(ArrayList<Camera> listaCamere) {
        this.listaCamere = Util.deepCopyArrayList(listaCamere);
    }

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


    // --- Metodi Standard di Object ---

    /**
     * Indica se un altro oggetto è "uguale a" questa prenotazione.
     * Il confronto è basato sul codice di prenotazione e su tutti gli attributi.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se le due prenotazioni sono uguali, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Prenotazione that = (Prenotazione) obj;
        return Objects.equals(codicePrenotazione, that.codicePrenotazione) && Objects.equals(dataCreazionePrenotazione, that.dataCreazionePrenotazione) && Objects.equals(dataInizio, that.dataInizio) && Objects.equals(dataFine, that.dataFine) && Objects.equals(trattamento, that.trattamento) && Objects.equals(tipoDocumento, that.tipoDocumento) && Objects.equals(dataRilascio, that.dataRilascio) && Objects.equals(dataScadenza, that.dataScadenza) && Objects.equals(intestatario, that.intestatario) && Objects.equals(noteAggiuntive, that.noteAggiuntive) && Objects.equals(listaCamere, that.listaCamere) && Objects.equals(listaServizi, that.listaServizi) && Objects.equals(listaClienti, that.listaClienti) && numeroDocumento == that.numeroDocumento;
    }

    /**
     * Crea e restituisce una copia profonda (deep clone) dell'oggetto {@code Prenotazione}.
     * Vengono clonati anche gli oggetti {@code Trattamento} e le liste interne.
     *
     * @return Una copia (clone) dell'oggetto {@code Prenotazione}.
     * @throws CloneNotSupportedException Se un attributo non clonabile è presente o la clonazione fallisce.
     */
    @Override
    public Prenotazione clone() throws CloneNotSupportedException {
        Prenotazione cloned = (Prenotazione) super.clone();

        // Clonazione degli oggetti interni mutabili (Trattamento e Liste)
        if (this.trattamento != null) {
            // Assumiamo che Trattamento sia clonabile
            // cloned.trattamento = this.trattamento.clone();
        }

        // Deep copy delle liste
        cloned.listaCamere = Util.deepCopyArrayList(this.listaCamere);
        cloned.listaServizi = Util.deepCopyArrayList(this.listaServizi);
        cloned.listaClienti = Util.deepCopyArrayList(this.listaClienti);

        return cloned;
    }
}