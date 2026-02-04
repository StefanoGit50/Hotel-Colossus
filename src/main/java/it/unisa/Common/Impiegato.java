
package it.unisa.Common;

import it.unisa.Server.persistent.util.Ruolo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un Impiegato dell'hotel, che eredita le funzionalità di {@code Utente}.
 * Contiene i dati anagrafici, i dettagli lavorativi e le informazioni di contatto dell'impiegato.
 */
public class Impiegato implements Cloneable, Serializable {
    private static final Logger log = LogManager.getLogger(Impiegato.class);
    private String username;
    private String password;
    private Instant expires;
    private boolean change;
    /**
     * Nome dell'impiegato.
     */
    private String nome;


    private String hashPassword;

    private boolean isTemporary;



    private int id;

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
    public Impiegato(int id,String username, String hashedPassword,boolean isTemporary,Instant dataScadenzaToken, String nome, String cognome, String sesso, String tipoDocumento,
                     String numeroDocumento, int CAP, String via, String provincia, String comune, int numeroCivico,
                     String codiceFiscale, String telefono, Ruolo ruolo, double stipendio, LocalDate dataAssunzione,
                     LocalDate dataRilascio, String emailAziendale, String cittadinanza , LocalDate dataScadenza) {
        this.id = id;
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
        this.isTemporary = isTemporary;
        this.expires = dataScadenzaToken;
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
        this.isTemporary = false;
        this.expires = null;
    }

    // --- Getter e Setter ---


    /**
     * Imposta il nome utente.
     *
     * @param username Nome utente
     *
     * @pre username != null
     * @post this.username = username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Imposta la password.
     *
     * @param password Password
     *
     * @pre password != null
     * @post this.password = password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Imposta la data di scadenza del token.
     *
     * @param expires Data scadenza
     *
     * @pre expires != null
     * @post this.expires = expires
     */
    public void setExpires(Instant expires) {
        this.expires = expires;
    }


    /**
     * Imposta il flag di cambio password.
     *
     * @param change Flag cambio
     *
     * @pre Nessuna
     * @post this.change = change
     */
    public void setChange(boolean change) {
        this.change = change;
    }


    /**
     * Restituisce il nome utente.
     *
     * @return Nome utente
     *
     * @pre Nessuna
     * @post result = this.username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Restituisce la password.
     *
     * @return Password
     *
     * @pre Nessuna
     * @post result = this.password
     */
    public String getPassword() {
        return password;
    }


    /**
     * Restituisce la data di scadenza del token.
     *
     * @return Data scadenza token
     *
     * @pre Nessuna
     * @post result = this.expires
     */
    public Instant getExpires() {
        return expires;
    }


    /**
     * Verifica se è richiesto il cambio password.
     *
     * @return true se cambio richiesto, false altrimenti
     *
     * @pre Nessuna
     * @post result = this.change
     */
    public boolean isChange() {
        return change;
    }


    /**
     * Imposta l'identificativo.
     *
     * @param id Identificativo
     *
     * @pre id >= 0
     * @post this.id = id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Imposta il flag temporaneo.
     *
     * @param temporary Flag temporaneo
     *
     * @pre Nessuna
     * @post this.isTemporary = temporary
     */
    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }


    /**
     * Restituisce l'identificativo.
     *
     * @return Identificativo
     *
     * @pre Nessuna
     * @post result = this.id
     * @post result >= 0
     */
    public int getId() {
        return id;
    }


    /**
     * Verifica se l'impiegato è temporaneo.
     *
     * @return true se temporaneo, false altrimenti
     *
     * @pre Nessuna
     * @post result = this.isTemporary
     */
    public boolean isTemporary() {
        return isTemporary;
    }


    /**
     * Restituisce la password hashata.
     *
     * @return Password hashata
     *
     * @pre Nessuna
     * @post result = this.hashPassword
     */
    public String getHashPassword() {
        return hashPassword;
    }


    /**
     * Imposta la password hashata.
     *
     * @param hashPassword Password hashata
     *
     * @pre hashPassword != null
     * @post this.hashPassword = hashPassword
     */
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }


    /**
     * Restituisce il nome.
     *
     * @return Nome dell'impiegato
     *
     * @pre Nessuna
     * @post result = this.nome
     */
    public String getNome() {
        return nome;
    }


    /**
     * Imposta il nome.
     *
     * @param nome Nome dell'impiegato
     *
     * @pre nome != null
     * @post this.nome = nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Restituisce il cognome.
     *
     * @return Cognome dell'impiegato
     *
     * @pre Nessuna
     * @post result = this.cognome
     */
    public String getCognome() {
        return cognome;
    }


    /**
     * Imposta il cognome.
     *
     * @param cognome Cognome dell'impiegato
     *
     * @pre cognome != null
     * @post this.cognome = cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    /**
     * Restituisce il sesso.
     *
     * @return Sesso
     *
     * @pre Nessuna
     * @post result = this.sesso
     */
    public String getSesso() {
        return sesso;
    }


    /**
     * Imposta il sesso.
     *
     * @param sesso Sesso
     *
     * @pre sesso != null
     * @post this.sesso = sesso
     */
    public void setSesso(String sesso) {
        this.sesso = sesso;
    }


    /**
     * Restituisce il tipo di documento.
     *
     * @return Tipo documento
     *
     * @pre Nessuna
     * @post result = this.tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }


    /**
     * Imposta il tipo di documento.
     *
     * @param tipoDocumento Tipo documento
     *
     * @pre tipoDocumento != null
     * @post this.tipoDocumento = tipoDocumento
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    /**
     * Restituisce il numero del documento.
     *
     * @return Numero documento
     *
     * @pre Nessuna
     * @post result = this.numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }


    /**
     * Imposta il numero del documento.
     *
     * @param numeroDocumento Numero documento
     *
     * @pre numeroDocumento != null
     * @post this.numeroDocumento = numeroDocumento
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }


    /**
     * Restituisce il CAP.
     *
     * @return CAP
     *
     * @pre Nessuna
     * @post result = this.CAP
     * @post result >= 0 && result <= 99999
     */
    public int getCAP() {
        return CAP;
    }


    /**
     * Imposta il CAP.
     *
     * @param CAP Codice Avviamento Postale
     *
     * @pre CAP >= 0 && CAP <= 99999
     * @post this.CAP = CAP
     */
    public void setCAP(int CAP) {
        this.CAP = CAP;
    }


    /**
     * Restituisce la via.
     *
     * @return Via
     *
     * @pre Nessuna
     * @post result = this.via
     */
    public String getVia() {
        return via;
    }


    /**
     * Imposta la via.
     *
     * @param via Via
     *
     * @pre via != null
     * @post this.via = via
     */
    public void setVia(String via) {
        this.via = via;
    }


    /**
     * Restituisce la provincia.
     *
     * @return Provincia
     *
     * @pre Nessuna
     * @post result = this.provincia
     */
    public String getProvincia() {
        return provincia;
    }


    /**
     * Imposta la provincia.
     *
     * @param provincia Provincia
     *
     * @pre provincia != null
     * @post this.provincia = provincia
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    /**
     * Restituisce il comune.
     *
     * @return Comune
     *
     * @pre Nessuna
     * @post result = this.comune
     */
    public String getComune() {
        return comune;
    }


    /**
     * Imposta il comune.
     *
     * @param comune Comune
     *
     * @pre comune != null
     * @post this.comune = comune
     */
    public void setComune(String comune) {
        this.comune = comune;
    }


    /**
     * Restituisce il numero civico.
     *
     * @return Numero civico
     *
     * @pre Nessuna
     * @post result = this.numeroCivico
     * @post result > 0
     */
    public int getNumeroCivico() {
        return numeroCivico;
    }


    /**
     * Imposta il numero civico.
     *
     * @param numeroCivico Numero civico
     *
     * @pre numeroCivico > 0
     * @post this.numeroCivico = numeroCivico
     */
    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }


    /**
     * Restituisce il codice fiscale.
     *
     * @return Codice fiscale
     *
     * @pre Nessuna
     * @post result = this.codiceFiscale
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Imposta il codice fiscale.
     *
     * @param codiceFiscale Codice fiscale
     *
     * @pre codiceFiscale != null
     * @post this.codiceFiscale = codiceFiscale
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Restituisce il numero di telefono.
     *
     * @return Telefono
     *
     * @pre Nessuna
     * @post result = this.telefono
     */
    public String getTelefono() {
        return telefono;
    }


    /**
     * Imposta il numero di telefono.
     *
     * @param telefono Telefono
     *
     * @pre telefono != null
     * @post this.telefono = telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    /**
     * Restituisce il ruolo lavorativo.
     *
     * @return Ruolo
     *
     * @pre Nessuna
     * @post result = this.ruolo
     * @post result != null
     */
    public Ruolo getRuolo() {
        return ruolo;
    }


    /**
     * Imposta il ruolo lavorativo.
     *
     * @param ruolo Ruolo
     *
     * @pre ruolo != null
     * @post this.ruolo = ruolo
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }


    /**
     * Restituisce lo stipendio.
     *
     * @return Stipendio mensile
     *
     * @pre Nessuna
     * @post result = this.stipendio
     * @post result >= 0
     */
    public double getStipendio() {
        return stipendio;
    }


    /**
     * Imposta lo stipendio.
     *
     * @param stipendio Stipendio mensile
     *
     * @pre stipendio >= 0
     * @post this.stipendio = stipendio
     */
    public void setStipendio(double stipendio) {
        this.stipendio = stipendio;
    }


    /**
     * Restituisce la data di assunzione.
     *
     * @return Data assunzione
     *
     * @pre Nessuna
     * @post result = this.dataAssunzione
     * @post result != null
     */
    public LocalDate getDataAssunzione() {
        return dataAssunzione;
    }


    /**
     * Imposta la data di assunzione.
     *
     * @param dataAssunzione Data assunzione
     *
     * @pre dataAssunzione != null
     * @post this.dataAssunzione = dataAssunzione
     */
    public void setDataAssunzione(LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }


    /**
     * Restituisce la data di rilascio del documento.
     *
     * @return Data rilascio
     *
     * @pre Nessuna
     * @post result = this.dataRilascio
     */
    public LocalDate getDataRilascio() {
        return dataRilascio;
    }


    /**
     * Imposta la data di rilascio del documento.
     *
     * @param dataRilascio Data rilascio
     *
     * @pre dataRilascio != null
     * @post this.dataRilascio = dataRilascio
     */
    public void setDataRilascio(LocalDate dataRilascio) {
        this.dataRilascio = dataRilascio;
    }


    /**
     * Restituisce l'email aziendale.
     *
     * @return Email aziendale
     *
     * @pre Nessuna
     * @post result = this.emailAziendale
     */
    public String getEmailAziendale() {
        return emailAziendale;
    }


    /**
     * Imposta l'email aziendale.
     *
     * @param emailAziendale Email aziendale
     *
     * @pre emailAziendale != null
     * @post this.emailAziendale = emailAziendale
     */
    public void setEmailAziendale(String emailAziendale) {
        this.emailAziendale = emailAziendale;
    }


    /**
     * Restituisce la cittadinanza.
     *
     * @return Cittadinanza
     *
     * @pre Nessuna
     * @post result = this.cittadinanza
     */
    public String getCittadinanza() {
        return cittadinanza;
    }


    /**
     * Imposta la cittadinanza.
     *
     * @param cittadinanza Cittadinanza
     *
     * @pre cittadinanza != null
     * @post this.cittadinanza = cittadinanza
     */
    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }


    /**
     * Restituisce la data di scadenza del documento.
     *
     * @return Data scadenza
     *
     * @pre Nessuna
     * @post result = this.dataScadenza
     */
    public LocalDate getDataScadenza() {
        return dataScadenza;
    }


    /**
     * Imposta la data di scadenza del documento.
     *
     * @param localDate Data scadenza
     *
     * @pre localDate != null
     * @post this.dataScadenza = localDate
     */
    public void setDataScadenza(LocalDate localDate){
        this.dataScadenza = localDate;
    }




    // --- Metodi Standard di Object ---


    @Override
    public String toString() {
        return "Impiegato{" +
                "nome='" + nome + '\'' +
                ", userName='" + username + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", isTemporary=" + isTemporary +
                ", dataScadenzaToken=" + expires+
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
