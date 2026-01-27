package it.unisa.Common;

import it.unisa.Server.persistent.util.Ruolo;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un Impiegato dell'hotel, che eredita le funzionalità di {@code Utente}.
 * Contiene i dati anagrafici, i dettagli lavorativi e le informazioni di contatto dell'impiegato.
 */
public class Impiegato implements Cloneable, Serializable {

    /**
     * Nome dell'impiegato.
     */
    private String nome;

    private String userName;

    private String hashPassword;

    private boolean isTempurali;

    private Instant dataScadenzaToken;

    /**
     * Cognome dell'impiegato.
     */
    private String cognome;

    /**
     * Sesso dell'impiegato.
     */
    private String sesso;

    /**
     * Tipo di documento di identità.
     */
    private String tipoDocumento;

    /**
     * Numero identificativo del documento.
     */
    private String numeroDocumento;

    /**
     * Codice di Avviamento Postale (CAP) di residenza.
     */
    private int CAP;

    /**
     * Via di residenza.
     */
    private String via;

    /**
     * Provincia di residenza.
     */
    private String provincia;

    /**
     * Comune di residenza.
     */
    private String comune;

    /**
     * Numero civico dell'abitazione.
     */
    private int numeroCivico;

    /**
     * Codice Fiscale dell'impiegato.
     */
    private String codiceFiscale;

    /**
     * Numero di telefono per i contatti.
     */
    private String telefono;

    /**
     * Ruolo o posizione lavorativa (es. "Receptionist", "Manager").
     */
    private Ruolo ruolo;

    /**
     * Stipendio mensile dell'impiegato.
     */
    private double stipendio;

    /**
     * Data di assunzione in azienda.
     */
    private LocalDate dataAssunzione;

    /**
     * Data di scadenza del documento di identità.
     */

    private LocalDate dataScadenza;
    /**
     * Data di rilascio del documento di identità.
     */
    private LocalDate dataRilascio;

    /**
     * Indirizzo email aziendale dell'impiegato.
     */
    private String emailAziendale;

    /**
     * Cittadinanza dell'impiegato.
     */
    private String cittadinanza;

    /**
     * Costruttore completo per creare una nuova istanza di {@code Impiegato}.
     * Chiama il costruttore della superclasse {@code Utente} per le credenziali.
     *
     * @param username Nome utente.
     * @param hashedPassword Password cifrata.
     * @param nome Nome dell'impiegato.
     * @param cognome Cognome dell'impiegato.
     * @param sesso Sesso.
     * @param tipoDocumento Tipo di documento.
     * @param numeroDocumento Numero del documento.
     * @param CAP CAP.
     * @param via Via.
     * @param provincia Provincia.
     * @param comune Comune.
     * @param numeroCivico Numero civico.
     * @param codiceFiscale Codice Fiscale.
     * @param telefono Numero di telefono.
     * @param ruolo Ruolo lavorativo.
     * @param stipendio Stipendio.
     * @param dataAssunzione Data di assunzione.
     * @param dataRilascio Data di rilascio del documento.
     * @param emailAziendale Email aziendale.
     * @param cittadinanza Cittadinanza.
     * @param dataScadenza Data di scadenza del documento
     */
    public Impiegato(String username, String hashedPassword,boolean isTempurali,Instant dataScadenzaToken, String nome, String cognome, String sesso, String tipoDocumento,
                     String numeroDocumento, int CAP, String via, String provincia, String comune, int numeroCivico,
                     String codiceFiscale, String telefono, Ruolo ruolo, double stipendio, LocalDate dataAssunzione,
                     LocalDate dataRilascio, String emailAziendale, String cittadinanza , LocalDate dataScadenza) {
        this.userName = username;
        this.hashPassword = hashedPassword;
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.CAP = CAP;
        this.via = via;
        this.provincia = provincia;
        this.comune = comune;
        this.numeroCivico = numeroCivico;
        this.codiceFiscale = codiceFiscale;
        this.telefono = telefono;
        this.ruolo = ruolo;
        this.stipendio = stipendio;
        this.dataAssunzione = dataAssunzione;
        this.dataRilascio = dataRilascio;
        this.emailAziendale = emailAziendale;
        this.cittadinanza = cittadinanza;
        this.dataScadenza = dataScadenza;
    }

    public Impiegato(){
        this.userName = "";
        this.hashPassword = "";
        this.nome = "";
        this.cognome = "";
        this.sesso = "";
        this.tipoDocumento = "";
        this.numeroDocumento = "";
        this.CAP = 0;
        this.via = "";
        this.provincia = "";
        this.comune = "";
        this.numeroCivico = 0;
        this.codiceFiscale = "";
        this.telefono = "";
        this.ruolo = Ruolo.FrontDesk;
        this.stipendio = 0;
        this.dataAssunzione = null;
        this.dataRilascio = null;
        this.emailAziendale = "";
        this.cittadinanza = "";
        this.dataScadenza = null;
    }

    // --- Getter e Setter ---

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getCAP() {
        return CAP;
    }

    public void setCAP(int CAP) {
        this.CAP = CAP;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public int getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public double getStipendio() {
        return stipendio;
    }

    public void setStipendio(double stipendio) {
        this.stipendio = stipendio;
    }

    public LocalDate getDataAssunzione() {
        return dataAssunzione;
    }

    public void setDataAssunzione(LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    public LocalDate getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(LocalDate dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public String getEmailAziendale() {
        return emailAziendale;
    }

    public void setEmailAziendale(String emailAziendale) {
        this.emailAziendale = emailAziendale;
    }

    public String getCittadinanza() {
        return cittadinanza;
    }

    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }
    public void setDataScadenza(LocalDate localDate){
        this.dataScadenza = localDate;
    }

    public boolean isTempurali() {
        return isTempurali;
    }

    public void setTempurali(boolean tempurali) {
        isTempurali = tempurali;
    }

    public Instant getDataScadenzaToken() {
        return dataScadenzaToken;
    }

    public void setDataScadenzaToken(Instant dataScadenzaToken) {
        this.dataScadenzaToken = dataScadenzaToken;
    }

    // --- Metodi Standard di Object ---


    @Override
    public String toString() {
        return "Impiegato{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", sesso='" + sesso + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", CAP=" + CAP +
                ", via='" + via + '\'' +
                ", provincia='" + provincia + '\'' +
                ", comune='" + comune + '\'' +
                ", numeroCivico=" + numeroCivico +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ruolo=" + ruolo +
                ", stipendio=" + stipendio +
                ", dataAssunzione=" + dataAssunzione +
                ", dataScadenza=" + dataScadenza +
                ", dataRilascio=" + dataRilascio +
                ", emailAziendale='" + emailAziendale + '\'' +
                ", cittadinanza='" + cittadinanza + '\'' +
                '}';
    }

    /**
     * Indica se un altro oggetto è "uguale a" questo impiegato.
     * Il confronto è basato sull'uguaglianza della superclasse {@code Utente}
     * e di tutti gli attributi specifici dell'impiegato.
     *
     * @param o L'oggetto da confrontare.
     * @return {@code true} se i due oggetti Impiegato sono uguali, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Impiegato impiegato = (Impiegato) o;
        return CAP == impiegato.CAP && numeroCivico == impiegato.numeroCivico && Double.compare(stipendio, impiegato.stipendio) == 0 && Objects.equals(nome, impiegato.nome) && Objects.equals(cognome, impiegato.cognome) && Objects.equals(sesso, impiegato.sesso) && Objects.equals(tipoDocumento, impiegato.tipoDocumento) && Objects.equals(numeroDocumento, impiegato.numeroDocumento) && Objects.equals(via, impiegato.via) && Objects.equals(provincia, impiegato.provincia) && Objects.equals(comune, impiegato.comune) && Objects.equals(codiceFiscale, impiegato.codiceFiscale) && Objects.equals(telefono, impiegato.telefono) && ruolo == impiegato.ruolo && Objects.equals(dataAssunzione, impiegato.dataAssunzione) && Objects.equals(dataScadenza, impiegato.dataScadenza) && Objects.equals(dataRilascio, impiegato.dataRilascio) && Objects.equals(emailAziendale, impiegato.emailAziendale) && Objects.equals(cittadinanza, impiegato.cittadinanza);
    }


    /**
     * Crea e restituisce una copia dell'oggetto (clone).
     *
     * @return Una copia (clone) dell'oggetto {@code Impiegato}.
     * @throws CloneNotSupportedException Se l'oggetto non implementa l'interfaccia {@code Cloneable}.
     */
    @Override
    public Impiegato clone() throws CloneNotSupportedException {
        // La chiamata a super.clone() gestisce il clone degli attributi di Impiegato e Utente
        return (Impiegato) super.clone();
    }
}