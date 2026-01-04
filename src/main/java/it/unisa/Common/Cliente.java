package it.unisa.Common;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un Cliente dell'hotel.
 * Memorizza i dati anagrafici, l'indirizzo e le informazioni di contatto
 * del cliente, oltre a un flag per indicare se è inserito nella blacklist.
 */
public class Cliente implements Cloneable {

    /**
     * Il nome del cliente.
     */
    private String nome;

    /**
     * Il codice fiscale del cliente
     */

    private String cf;

    /**
     *  email del cliente
     */

    private String email;

    /**
     *  Il metodo di pagamento di cui ha scelto il cliente
     */
    private String metodoDiPagamento;

    /**
     * Il cognome del cliente.
     */
    private String cognome;

    /**
     * La cittadinanza del cliente.
     */
    private String cittadinanza;

    /**
     * La provincia di residenza.
     */
    private String provincia;

    /**
     * Il comune di residenza.
     */
    private String comune;

    /**
     * La via di residenza.
     */
    private String via;

    /**
     * Il numero civico dell'abitazione.
     */
    private int numeroCivico;

    /**
     * Il Codice di Avviamento Postale (CAP).
     */
    private int CAP;

    /**
     * Il numero di telefono per i contatti.
     */
    private String numeroTelefono;

    /**
     * Flag che indica se il cliente è inserito nella blacklist.
     */
    private boolean isBlacklisted;

    /**
     * Sesso del cliente.
     */
    private String sesso;

    /**
     * Data di nascita del cliente.
     */
    private LocalDate dataNascita;
    /**
     * La nazionalità del cliente.
     */
    private String nazionalità;

    /**
     * Costruttore completo per creare una nuova istanza di {@code Cliente}
     * (l'attributo {@code isBlacklisted} Flag che indica se il cliente è in lista nera di default {@code false}).
     * @param nome Il nome del cliente.
     * @param cognome Il cognome del cliente.
     * @param cittadinanza La cittadinanza del cliente.
     * @param provincia La provincia di residenza.
     * @param comune Il comune di residenza.
     * @param via La via di residenza.
     * @param numeroCivico Il numero civico.
     * @param CAP Il CAP.
     * @param numeroTelefono Il numero di telefono.
     * @param sesso Il sesso del cliente.
     * @param dataNascita La data di nascita del cliente.
     * @param cf il codice fiscale del cliente
     * @param email l'email del cliente
     */
    public Cliente(String nome, String cognome, String cittadinanza, String provincia, String comune, String via, int numeroCivico, int CAP, String numeroTelefono, String sesso, LocalDate dataNascita ,String cf , String email,String metodoDiPagamento) {
        this.nome = nome;
        this.cognome = cognome;
        this.cittadinanza = cittadinanza;
        this.provincia = provincia;
        this.comune = comune;
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.CAP = CAP;
        this.numeroTelefono = numeroTelefono;
        this.isBlacklisted = false;
        this.sesso = sesso;
        this.dataNascita = dataNascita;
        this.cf = cf;
        this.email = email;
        this.metodoDiPagamento = metodoDiPagamento;
    }

    /**
     * Restituisce il nome del cliente.
     *
     * @return Il nome del cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del cliente.
     *
     * @param nome Il nuovo nome del cliente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome del cliente.
     *
     * @return Il cognome del cliente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome del cliente.
     *
     * @param cognome Il nuovo cognome del cliente.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce la cittadinanza del cliente.
     *
     * @return La cittadinanza del cliente.
     */
    public String getCittadinanza() {
        return cittadinanza;
    }

    /**
     * Imposta la cittadinanza del cliente.
     *
     * @param cittadinanza La nuova cittadinanza del cliente.
     */
    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    /**
     * Restituisce la provincia di residenza.
     *
     * @return La provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta la provincia di residenza.
     *
     * @param provincia La nuova provincia.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Restituisce il comune di residenza.
     *
     * @return Il comune.
     */
    public String getComune() {
        return comune;
    }

    /**
     * Imposta il comune di residenza.
     *
     * @param comune Il nuovo comune.
     */
    public void setComune(String comune) {
        this.comune = comune;
    }

    /**
     * Restituisce la via di residenza.
     *
     * @return La via.
     */
    public String getVia() {
        return via;
    }

    /**
     * Imposta la via di residenza.
     *
     * @param via La nuova via.
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Restituisce il numero civico.
     *
     * @return Il numero civico.
     */
    public int getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * Imposta il numero civico.
     *
     * @param numeroCivico Il nuovo numero civico.
     */
    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * Restituisce il CAP.
     *
     * @return Il CAP.
     */
    public int getCAP() {
        return CAP;
    }

    /**
     * Imposta il CAP.
     *
     * @param CAP Il nuovo CAP.
     */
    public void setCAP(int CAP) {
        this.CAP = CAP;
    }

    /**
     * Restituisce il numero di telefono.
     *
     * @return Il numero di telefono.
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * Imposta il numero di telefono.
     *
     * @param numeroTelefono Il nuovo numero di telefono.
     */
    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    /**
     * Restituisce lo stato di blacklisting del cliente.
     *
     * @return {@code true} se il cliente è in lista nera, {@code false} altrimenti.
     */
    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    /**
     * Imposta lo stato di blacklisting del cliente.
     *
     * @param blacklisted Lo stato di blacklisting da impostare.
     */
    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    /**
     * Restituisce il sesso del cliente.
     *
     * @return Il sesso del cliente.
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il sesso del cliente.
     *
     * @param sesso Il nuovo sesso del cliente.
     */
    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    /**
     * Restituisce la data di nascita del cliente.
     *
     * @return La data di nascita.
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta la data di nascita del cliente.
     *
     * @param dataNascita La nuova data di nascita.
     */
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce la nazionalità del cliente.
     *
     * @return La nazionalità del cliente.
     */
    public String getNazionalita() {
        return nazionalità;
    }

    /**
     * Imposta la nazionalità del cliente.
     *
     * @param nazionalita La nuova nazionalità del cliente.
     */
    public void setNazionalita(String nazionalita) {
        this.nazionalità = nazionalita;
    }
    /**
     * Imposta il codice fiscale del cliente
     *
     * @param cf il nuovo codice fiscale
     */
    public void setCf(String cf){
        this.cf = cf;
    }

    /**
     * Mostra il codice fiscale del cliente
     *
     * @return cf il codice fiscale del cliente
     */
    public String getCf() {
        return cf;
    }

    /**
     * Setta l'email del cliente
     *
     * @param email l'email nuova del cliente
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Mostra l'email di un cliente
     *
     * @return email l'email del cliente
     */

    public String getEmail() {
        return email;
    }

    /**
     * Mostra i metodoDiPagamento
     *
     * @return metodoDiPagamento il metodo di pagamento scelto dal cliente
     */

    public String getMetodoDiPagamento() {
        return metodoDiPagamento;
    }

    /**
     * Setta il metodo di pagamento del cliente
     *
     * @param metodoDiPagamento il metodo di pagamento del cliente
     */

    public void setMetodoDiPagamento(String metodoDiPagamento) {
        this.metodoDiPagamento = metodoDiPagamento;
    }

    /**
     * Indica se un altro oggetto è "uguale a" questo cliente.
     * Il confronto è basato su tutti i campi del cliente.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se i due oggetti Cliente sono uguali, {@code false} altrimenti.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return numeroCivico == cliente.numeroCivico && CAP == cliente.CAP && isBlacklisted == cliente.isBlacklisted
                && Objects.equals(nome, cliente.nome) && Objects.equals(cognome, cliente.cognome)
                && Objects.equals(cittadinanza, cliente.cittadinanza) && Objects.equals(provincia, cliente.provincia)
                && Objects.equals(comune, cliente.comune) && Objects.equals(via, cliente.via)
                && Objects.equals(numeroTelefono, cliente.numeroTelefono) && Objects.equals(sesso, cliente.sesso)
                && Objects.equals(dataNascita, cliente.dataNascita) && Objects.equals(nazionalità, cliente.nazionalità) &&
                Objects.equals(metodoDiPagamento,cliente.metodoDiPagamento) && Objects.equals(email , cliente.email) &&
                Objects.equals(cf,cliente.cf);
    }
    /**
     * Crea e restituisce una copia dell'oggetto (clone).
     * Poiché la classe contiene solo tipi primitivi e stringhe (immutabili),
     * un clone superficiale è sufficiente e agisce come una deep copy.
     *
     * @return Una copia (clone) dell'oggetto {@code Cliente}.
     * @throws CloneNotSupportedException Se l'oggetto non implementa l'interfaccia {@code Cloneable}.
     */
    @Override
    public Cliente clone() throws CloneNotSupportedException{
        return (Cliente) super.clone(); // LocalDate immutabile
    }
}