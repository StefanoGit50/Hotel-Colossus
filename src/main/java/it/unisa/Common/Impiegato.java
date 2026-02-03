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
    private String username;
    private String password;
    private Instant expires;
    private boolean change;
    /**
     * Nome dell'impiegato.
     */
    private String nome;

    private String hashPassword;

    private boolean isTemporaly;

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
    public Impiegato(String username, String hashedPassword,boolean isTemporaly,Instant dataScadenzaToken, String nome, String cognome, String sesso, String tipoDocumento,
                     String numeroDocumento, int CAP, String via, String provincia, String comune, int numeroCivico,
                     String codiceFiscale, String telefono, Ruolo ruolo, double stipendio, LocalDate dataAssunzione,
                     LocalDate dataRilascio, String emailAziendale, String cittadinanza , LocalDate dataScadenza) {
        this.username = username;
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
        this.isTemporaly = isTemporaly;
        this.dataScadenzaToken = dataScadenzaToken;
    }

    public Impiegato(){
        this.username = "";
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
        this.isTemporaly = false;
        this.dataScadenzaToken = null;
    }

    // --- Getter e Setter ---


    /**
     * Imposta il valore di usename.
     *
     * @pre username != null && username != ""
     * @post this.username == username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Imposta il valore di password.
     *
     * @pre password != null && password != ""
     * @post this.password == password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Imposta il valore di expires.
     *
     * @pre expires != null && expires != ""
     * @post this.expires == expires
     */
    public void setExpires(Instant expires) {
        this.expires = expires;
    }


    /**
     * Imposta il valore di change.
     *
     * @pre change != null && change != ""
     * @post this.nome == nome
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    /**
     * Restituisce il valore di username.
     *
     * @post result == username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password.
     *
     * @post result == password
     */
    public String getPassword() {
        return password;
    }


    /**
     * Restituisce la scadenza.
     *
     * @post result == expires
     */
    public Instant getExpires() {
        return expires;
    }


    /**
     * Verifica valore variabile isChange.
     *
     * @post true o false
     */
    public boolean isChange() {
        return change;
    }


    /**
     * Restituisce lo hashPassword.
     *
     * @post result == hashPassword
     */
    public String getHashPassword() {
        return hashPassword;
    }


    /**
     * Imposta il valore di hashPassword.
     *
     * @pre hashPassword != null && hashPassword != ""
     * @post this.hashPassword == hashPassword
     */
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }


    /**
     * Restituisce il valore di nome.
     *
     * @post result == nome
     */
    public String getNome() {
        return nome;
    }


    /**
     * Imposta il valore di nome.
     *
     * @pre nome != null && nome != ""
     * @post this.nome == nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Restituisce il valore di cognome.
     *
     * @post result == cognome
     */
    public String getCognome() {
        return cognome;
    }


    /**
     * Imposta il valore di cognome.
     *
     * @pre cognome != null && cognome != ""
     * @post this.cognome == cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    /**
     * Restituisce il valore di sesso.
     *
     * @post result == sesso
     */
    public String getSesso() {
        return sesso;
    }


    /**
     * Imposta il valore di sesso.
     *
     * @pre sesso != null && sesso != ""
     * @post this.sesso == sesso
     */
    public void setSesso(String sesso) {
        this.sesso = sesso;
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
     * Restituisce il valore di numeroDocumento.
     *
     * @post result == numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }


    /**
     * Imposta il valore di numeroDocumento.
     *
     * @pre numeroDocumento != null && numeroDocumento != ""
     * @post this.numeroDocumento == numeroDocumento
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }


    /**
     * Restituisce il valore di cAP.
     *
     * @post result == CAP
     */
    public int getCAP() {
        return CAP;
    }


    /**
     * Imposta il valore di cAP.
     *
     * @pre CAP != null
     * @post this.CAP == CAP
     */
    public void setCAP(int CAP) {
        this.CAP = CAP;
    }


    /**
     * Restituisce il valore di via.
     *
     * @post result == via
     */
    public String getVia() {
        return via;
    }


    /**
     * Imposta il valore di via.
     *
     * @pre via != null && via != ""
     * @post this.via == via
     */
    public void setVia(String via) {
        this.via = via;
    }


    /**
     * Restituisce il valore di provincia.
     *
     * @post result == provincia
     */
    public String getProvincia() {
        return provincia;
    }


    /**
     * Imposta il valore di provincia.
     *
     * @pre provincia != null && provincia != ""
     * @post this.provincia == provincia
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    /**
     * Restituisce il valore di comune.
     *
     * @post result == comune
     */
    public String getComune() {
        return comune;
    }


    /**
     * Imposta il valore di comune.
     *
     * @pre comune != null && comune != ""
     * @post this.comune == comune
     */
    public void setComune(String comune) {
        this.comune = comune;
    }


    /**
     * Restituisce il valore di numeroCivico.
     *
     * @post result == numeroCivico
     */
    public int getNumeroCivico() {
        return numeroCivico;
    }


    /**
     * Imposta il valore di numeroCivico.
     *
     * @pre numeroCivico != null
     * @post this.numeroCivico == numeroCivico
     */
    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }


    /**
     * Restituisce il valore di codiceFiscale.
     *
     * @post result == codiceFiscale
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Imposta il valore di codiceFiscale.
     *
     * @pre codiceFiscale != null && codiceFiscale != ""
     * @post this.codiceFiscale == codiceFiscale
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Restituisce il valore di telefono.
     *
     * @post result == telefono
     */
    public String getTelefono() {
        return telefono;
    }


    /**
     * Imposta il valore di telefono.
     *
     * @pre telefono != null && telefono != ""
     * @post this.telefono == telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    /**
     * Restituisce il valore di ruolo.
     *
     * @post result == ruolo
     */
    public Ruolo getRuolo() {
        return ruolo;
    }


    /**
     * Imposta il valore di ruolo.
     *
     * @pre ruolo != null
     * @post this.ruolo == ruolo
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }


    /**
     * Restituisce il valore di stipendio.
     *
     * @post result == stipendio
     */
    public double getStipendio() {
        return stipendio;
    }


    /**
     * Imposta il valore di stipendio.
     *
     * @pre stipendio != null
     * @post this.stipendio == stipendio
     */
    public void setStipendio(double stipendio) {
        this.stipendio = stipendio;
    }


    /**
     * Restituisce il valore di dataAssunzione.
     *
     * @post result == dataAssunzione
     */
    public LocalDate getDataAssunzione() {
        return dataAssunzione;
    }


    /**
     * Imposta il valore di dataAssunzione.
     *
     * @pre dataAssunzione != null
     * @post this.dataAssunzione == dataAssunzione
     */
    public void setDataAssunzione(LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
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
     * Restituisce il valore di emailAziendale.
     *
     * @post result == emailAziendale
     */
    public String getEmailAziendale() {
        return emailAziendale;
    }


    /**
     * Imposta il valore di emailAziendale.
     *
     * @pre emailAziendale != null && emailAziendale != ""
     * @post this.emailAziendale == emailAziendale
     */
    public void setEmailAziendale(String emailAziendale) {
        this.emailAziendale = emailAziendale;
    }

    /**
     * Restituisce il valore di cittadinanza.
     *
     * @post result == cittadinanza
     */
    public String getCittadinanza() {
        return cittadinanza;
    }

    /**
     * Imposta il valore di cittadinanza.
     *
     * @pre cittadinanza != null && cittadinanza != ""
     * @post this.cittadinanza == cittadinanza
     */
    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
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
     * @pre localDate != null
     * @post dataScadenza == localDate
     */
    public void setDataScadenza(LocalDate localDate){
        this.dataScadenza = localDate;
    }


    /**
     * Verifica valore variabile isTemporaly.
     *
     * @post true o false
     */
    public boolean isTemporaly() {
        return isTemporaly;
    }


    /**
     * Imposta valore variabile isTemporaly.
     *
     * @pre temporaly != null
     */
    public void setTemporaly(boolean temporaly) {
        isTemporaly = temporaly;
    }


    /**
     * Restituisce il valore di DataScadenzaToken.
     *
     * @post result == dataScadenzaToken
     */
    public Instant getDataScadenzaToken() {
        return dataScadenzaToken;
    }


    /**
     * Imposta valore variabile DataScadenzaToken.
     *
     * @pre dataScadenzaToken != null
     * @post this.dataScadenzaToken = dataScadenzaToken
     */
    public void setDataScadenzaToken(Instant dataScadenzaToken) {
        this.dataScadenzaToken = dataScadenzaToken;
    }

    // --- Metodi Standard di Object ---


    @Override
    public String toString() {
        return "Impiegato{" +
                "nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", isTemporaly=" + isTemporaly +
                ", dataScadenzaToken=" + dataScadenzaToken +
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