package it.unisa.Common;

import it.unisa.Server.persistent.util.Stato;
import it.unisa.Server.persistent.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Indica lo stato della prenotazione
     */
    private boolean statoPrenotazione;
    /**
     * Dice se è stato fatto il check-In della prenotazione
     */
    private boolean checkIn;

    /**
     * Costruttore completo per creare una nuova istanza di {@code Prenotazione}.
     *
     * @param IDPrenotazione Codice univoco della prenotazione.
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
     * @param numeroDocumento il numero del documento.
     */
    public Prenotazione(int IDPrenotazione, LocalDate dataCreazionePrenotazione, LocalDate dataInizio, LocalDate dataFine,LocalDate dataEmissioneRicevuta,
                        Trattamento trattamento, String tipoDocumento, LocalDate dataRilascio, LocalDate dataScadenza,
                        String intestatario, String noteAggiuntive, ArrayList<Camera> listaCamere, ArrayList<Servizio> listaServizi,
                        ArrayList<Cliente> listaClienti ,String numeroDocumento, String metodoPagamento) {
        this.IDPrenotazione = IDPrenotazione;
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
        this.statoPrenotazione = true;
        this.checkIn = false;
        this.dataEmissioneRicevuta = dataEmissioneRicevuta;
        this.metodoDiPagamento= metodoPagamento;
    }

    public Prenotazione() {
        this.IDPrenotazione = 0;
        this.numeroDocumento = "";
        this.dataCreazionePrenotazione = LocalDate.now();
        this.dataFine = LocalDate.now();
        this.dataInizio = LocalDate.now();
        this.dataRilascio = LocalDate.now();
        this.dataScadenza = LocalDate.now();
        this.listaCamere = new ArrayList<>();
        this.listaClienti = new ArrayList<>();
        this.listaServizi = new ArrayList<>();
        this.noteAggiuntive = "";
        this.trattamento = new Trattamento();
        this.intestatario = "";
        this.statoPrenotazione = false;
    }

     /**
     * Aggiunge un cliente alla lista dei clienti.
     *
      * @pre cliente != null
      * @post listaClienti.contains(cliente)
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
     * @pre cliente != null
     * @post result == (not listaClienti.contains(cliente))
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
     * @pre servizio != null
     * @post listaServizi.contains(servizio)
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
     * @pre servizio != null
     * @post result == (not listaServizi.contains(servizio))
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


    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoDiPagamento = metodoPagamento;
    }

    public String getMetodoPagamento() {
        return metodoDiPagamento;
    }

    public LocalDate getDataEmissioneRicevuta() {
        return dataEmissioneRicevuta;
    }

    /**
     * Imposta il valore di IDPrenotazione.
     *
     * @pre IDPrenotazione != null
     * @post IDPrenotazione == IDPrenotazione
     */
    public void setIDPrenotazione(Integer IDPrenotazione) {
        this.IDPrenotazione = IDPrenotazione;
    }


    /**
     * Restituisce il valore di iDPrenotazione.
     *
     * @post result == IDPrenotazione
     */
    public Integer getIDPrenotazione() {
        return IDPrenotazione;
    }


    /**
     * Restituisce il valore di dataCreazionePrenotazione.
     *
     * @post result == dataCreazionePrenotazione
     */
    public LocalDate getDataCreazionePrenotazione() {
        return dataCreazionePrenotazione;
    }


    /**
     * Imposta il valore di dataCreazionePrenotazione.
     *
     * @pre dataCreazionePrenotazione != null
     * @post this.dataCreazionePrenotazione == dataCreazionePrenotazione
     */
    public void setDataCreazionePrenotazione(LocalDate dataCreazionePrenotazione) {
        this.dataCreazionePrenotazione = dataCreazionePrenotazione;
    }


    /**
     * Restituisce il valore di statoPrenotazione.
     *
     * @post result == statoPrenotazione
     */
    public boolean getStatoPrenotazione(){
        return this.statoPrenotazione;
    }


    /**
     * Imposta il valore di statoPrenotazione.
     *
     * @post statoPrenotazione == st
     */
    public  void setStatoPrenotazione(boolean st){
        this.statoPrenotazione = st;
    }


    /**
     * Restituisce il valore di dataInizio.
     *
     * @post result == dataInizio
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }


    /**
     * Imposta il valore di dataInizio.
     *
     * @pre dataInizio != null
     * @post this.dataInizio == dataInizio
     */
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Restituisce il valore di dataFine.
     *
     * @post result == dataFine
     */
    public LocalDate getDataFine() {
        return dataFine;
    }


    /**
     * Imposta il valore di dataFine.
     *
     * @pre dataFine != null
     * @post this.dataFine == dataFine
     */
    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Restituisce il valore di trattamento.
     *
     * @post result == trattamento
     */
    public Trattamento getTrattamento() {
        return trattamento;
    }


    /**
     * Imposta il valore di trattamento.
     *
     * @pre trattamento != null
     * @post this.trattamento == trattamento
     */
    public void setTrattamento(Trattamento trattamento) {
        this.trattamento = trattamento;
    }


    /**
     * Restituisce il valore di tipoDocumento.
     *
     * @post result == tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }


    /**
     * Imposta il valore di tipoDocumento.
     *
     * @pre tipoDocumento != null && tipoDocumento != ""
     * @post this.tipoDocumento == tipoDocumento
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    /**
     * Restituisce il valore di dataRilascio.
     *
     * @post result == dataRilascio
     */
    public LocalDate getDataRilascio() {
        return dataRilascio;
    }


    /**
     * Imposta il valore di dataRilascio.
     *
     * @pre dataRilascio != null
     * @post this.dataRilascio == dataRilascio
     */
    public void setDataRilascio(LocalDate dataRilascio) {
        this.dataRilascio = dataRilascio;
    }


    /**
     * Restituisce il valore di dataScadenza.
     *
     * @post result == dataScadenza
     */
    public LocalDate getDataScadenza() {
        return dataScadenza;
    }


    /**
     * Imposta il valore di dataScadenza.
     *
     * @pre dataScadenza != null
     * @post this.dataScadenza == dataScadenza
     */
    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }


    /**
     * Restituisce il valore di intestatario.
     *
     * @post result == intestatario
     */
    public String getIntestatario() {
        return intestatario;
    }


    /**
     * Imposta il valore di intestatario.
     *
     * @pre intestatario != null && intestatario != ""
     * @post this.intestatario == intestatario
     */
    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }


    /**
     * Restituisce il valore di noteAggiuntive.
     *
     * @post result == noteAggiuntive
     */
    public String getNoteAggiuntive() {
        return noteAggiuntive;
    }


    /**
     * Imposta il valore di noteAggiuntive.
     *
     * @post this.noteAggiuntive == noteAggiuntive
     */
    public void setNoteAggiuntive(String noteAggiuntive) {
        this.noteAggiuntive = noteAggiuntive;
    }


    /**
     * Imposta il valore di numeroDocumento.
     *
     * @pre numeroDocumento != null
     * @post this.numeroDocumento == numeroDocumento
     */
    public void setNumeroDocumento(String numeroDocumento){
        this.numeroDocumento = numeroDocumento;
    }


    /**
     * Restituisce il valore di numeroDocumento.
     *
     * @post result == numeroDocumento
     */
    public String getNumeroDocumento(){
        return numeroDocumento;
    }


    /**
     * Restituisce una deep copy della lista delle camere.
     *
     * @post result.stream().allMatch(c | listaCamere.contains(c))
     *
     * @return Una nuova ArrayList contenente i cloni delle camere.
     */
    public ArrayList<Camera> getListaCamere() {
        return Util.deepCopyArrayList(listaCamere);
    }

    /**
     * Imposta la lista delle camere, creando una deep copy della lista fornita.
     *
     * @pre listaCamere != null
     * @post this.listaCamere.stream().allMatch(c | listaCamere.contains(c))
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


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @post result == checkIn
     */
    public boolean isCheckIn() {
        return checkIn;
    }



    /**
     * Imposta il valore di checkIn.
     *
     * @post this.checkIn == checkIn
     */
    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    // --- Metodi Standard di Object ---

    @Override
    public String toString() {
        return "Prenotazione{" +
                "IDPrenotazione=" + IDPrenotazione +
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
                ", listaCamere=" + listaCamere +
                ", listaServizi=" + listaServizi +
                ", listaClienti=" + listaClienti +
                ", statoPrenotazione=" + statoPrenotazione +
                ", checkIn=" + checkIn +
                ", metodoPagamento=" + metodoDiPagamento + '\'' +
                ", dataemissione=" + dataEmissioneRicevuta +
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
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(this == o){
            return true;
        }
        if(getClass() != o.getClass()){
            return false;
        }
        Prenotazione prenotazione = (Prenotazione) o;
        if(!isNullAll(prenotazione) && !isNullAll(this)){
                ArrayList<Cliente> clientes = prenotazione.getListaClienti();
                ArrayList<Camera> cameras = prenotazione.getListaCamere();
                ArrayList<Servizio> servizios = prenotazione.getListaServizi();

                if(isNull(clientes)){
                     if(!isNull(listaClienti)){
                        return false;
                     }
                }else {
                    if(isNull(listaClienti)){
                        return false;
                    }
                }

                if(isNull(cameras)){
                    if(!isNull(listaCamere)){
                        return false;
                    }
                }else{
                    if(isNull(listaCamere)){
                        return false;
                    }
                }

                if(isNull(servizios)){
                    if(!isNull(listaServizi)){
                        return false;
                    }
                }else{
                    if(isNull(listaServizi)){
                        return false;
                    }
                }

                Boolean clien = null;
                Boolean came = null;
                Boolean servi = null;
                int i = 0;
                if(clientes.size() == listaClienti.size()){
                    for(Cliente cliente : clientes){
                        if(!listaClienti.get(i).equals(cliente)){
                            clien = false;
                            break;
                        }
                        i++;
                    }

                    if(clien == null){
                        clien = true;
                    }
                }else{
                    clien = false;
                }
                i = 0;
                if(servizios.size() == listaServizi.size()){
                    for(Servizio servizio : servizios){
                        if(!listaServizi.get(i).equals(servizio)){
                            servi = false;
                            break;
                        }
                        i++;
                    }

                    if(servi == null){
                        servi =true;
                    }
                }else{
                    servi = false;
                }
                i = 0;
                if(cameras.size() == listaCamere.size()){
                    for(Camera camera : cameras){
                        if(!listaCamere.get(i).equals(camera)){
                            came = false;
                            break;
                        }
                        i++;
                    }

                    if(came == null){
                        came = true;
                    }
                }else{
                    came = false;
                }

                if(isNull(trattamento)){
                    if(!isNull(prenotazione.trattamento)){
                        return false;
                    }

                }else{
                    if(isNull(prenotazione.trattamento)){
                        return false;
                    }
                }

                if(isNull(IDPrenotazione)){
                    if(!isNull(prenotazione.IDPrenotazione)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.IDPrenotazione)){
                        return false;
                    }
                }

                if(isNull(dataCreazionePrenotazione)){
                    if(!isNull(prenotazione.dataCreazionePrenotazione)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.dataCreazionePrenotazione)){
                        return false;
                    }
                }

                if(isNull(dataFine)){
                    if(!isNull(prenotazione.dataFine)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.dataFine)){
                        return false;
                    }
                }

                if(isNull(dataInizio)){
                    if(!isNull(prenotazione.dataInizio)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.dataInizio)){
                        return false;
                    }
                }

                if(isNull(dataRilascio)){
                    if(!isNull(prenotazione.dataRilascio)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.dataRilascio)){
                        return false;
                    }
                }

                if(isNull(dataScadenza)){
                    if(!isNull(prenotazione.dataScadenza)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.dataScadenza)){
                        return false;
                    }
                }

                if(isNull(tipoDocumento)){
                    if(!isNull(prenotazione.tipoDocumento)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.tipoDocumento)){
                        return false;
                    }
                }

                if(isNull(numeroDocumento)){
                    if(!isNull(prenotazione.numeroDocumento)){
                        return false;
                    }
                }else{
                    if(isNull(prenotazione.numeroDocumento)){
                        return false;
                    }
                }

                if(isNull(intestatario)){
                    if(!isNull(prenotazione.intestatario)){
                       return false;
                    }
                }else{
                    if(isNull(prenotazione.intestatario)){
                        return false;
                    }
                }

                if(isNull(noteAggiuntive)){
                    if(!isNull(prenotazione.noteAggiuntive)){
                        return false;
                    }
                }else{
                    if(isNull(noteAggiuntive)){
                        return false;
                    }
                }
                return came && servi && clien && trattamento.equals(prenotazione.getTrattamento()) && IDPrenotazione.equals(prenotazione.getIDPrenotazione())
                        && dataCreazionePrenotazione.equals(prenotazione.getDataCreazionePrenotazione()) && dataInizio.equals(prenotazione.getDataInizio()) &&
                        dataFine.equals(prenotazione.getDataFine()) && dataRilascio.equals(prenotazione.getDataRilascio()) && dataScadenza.equals(prenotazione.getDataScadenza())
                        && tipoDocumento.equalsIgnoreCase(prenotazione.getTipoDocumento()) && numeroDocumento.equalsIgnoreCase(prenotazione.getNumeroDocumento())
                        && intestatario.equalsIgnoreCase(prenotazione.getIntestatario()) && noteAggiuntive.equalsIgnoreCase(prenotazione.getNoteAggiuntive()) && checkIn == prenotazione.isCheckIn()
                        && statoPrenotazione == prenotazione.getStatoPrenotazione() && metodoDiPagamento == prenotazione.getMetodoPagamento() && dataEmissioneRicevuta.equals(prenotazione.getDataEmissioneRicevuta());

        }else{
           if(isNullAll(prenotazione) && isNullAll(this)){
               return true;
           }else{
               return false;
           }
        }

    }
    private boolean isNullAll(Prenotazione prenotazione) {
        //renato fatti ricoverare
        return prenotazione.trattamento == null && prenotazione.noteAggiuntive == null && prenotazione.intestatario == null &&
                prenotazione.numeroDocumento == null && prenotazione.tipoDocumento == null && prenotazione.IDPrenotazione == null &&
                prenotazione.dataRilascio == null && prenotazione.dataScadenza == null && prenotazione.dataInizio == null &&
                prenotazione.dataFine == null && prenotazione.listaCamere == null && prenotazione.listaClienti == null &&
                prenotazione.listaServizi == null && prenotazione.dataCreazionePrenotazione == null && metodoDiPagamento==null;
    }


    private boolean isNull(Object o ){
        if(o == null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDPrenotazione, dataCreazionePrenotazione, dataInizio, dataFine, trattamento, tipoDocumento, dataRilascio, dataScadenza, intestatario, noteAggiuntive, numeroDocumento, listaCamere, listaServizi, listaClienti, statoPrenotazione, checkIn);
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