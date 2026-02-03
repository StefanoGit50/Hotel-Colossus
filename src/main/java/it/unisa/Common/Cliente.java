package it.unisa.Common;

import java.awt.font.TextHitInfo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un Cliente dell'hotel.
 * Memorizza i dati anagrafici, l'indirizzo e le informazioni di contatto
 * del cliente, oltre a un flag per indicare se è inserito nella blacklist.
 */
public class Cliente implements Cloneable, Serializable {

    /**
     * Il nome del cliente.
     */
    private String nome;

    /**
     * Il cognome del cliente.
     */
    private String cognome;

    /**
     * Il codice fiscale del cliente
     */

    private String cf;

    /**
     *  email del cliente
     */
    private String email;



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
    private Integer numeroCivico;

    /**
     * Il Codice di Avviamento Postale (CAP).
     */
    private Integer CAP;

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

    private Camera camera;

    /**
     * Data di nascita del cliente.
     */
    private LocalDate dataNascita;
    /**
     * La nazionalità del cliente.
     */
    private String nazionalità;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setNazionalità(String nazionalità) {
        this.nazionalità = nazionalità;
    }

    public String getNazionalità() {
        return nazionalità;
    }

    public Camera getCamera() {
        try{
            camera.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * Costruttore completo per creare una nuova istanza di {@code Cliente}
     * (l'attributo {@code isBlacklisted} Flag che indica se il cliente è in lista nera di default {@code false}).
     * @param nome Il nome del cliente.
     * @param cognome Il cognome del cliente.
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
    public Cliente(String nome, String cognome, String provincia, String comune, String via, Integer numeroCivico, Integer CAP, String numeroTelefono, String sesso, LocalDate dataNascita ,String cf , String email,String nazionalità,Camera camera) {
        this.nome = nome;
        this.cognome = cognome;
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
        this.nazionalità = nazionalità;
        this.camera = camera;
    }

    /**
     * Costruttore vuoto.
     */
    public Cliente() {}

    /**
     * Restituisce il nome del cliente.
     *
     * @post result == nome
     *
     * @return Il nome del cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del cliente.
     *
     * @pre nome != null && nome != ""
     * @post this.nome == nome
     *
     * @param nome Il nuovo nome del cliente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome del cliente.
     *
     * @post result == cognome
     *
     * @return Il cognome del cliente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome del cliente.
     *
     * @pre cognome != null && cognome != ""
     * @post this.cognome == cognome
     *
     * @param cognome Il nuovo cognome del cliente.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce la provincia di residenza.
     *
     * @post result == provincia
     *
     * @return La provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta la provincia di residenza.
     *
     * @pre provincia != null && provincia != ""
     * @post this.provincia == provincia
     *
     * @param provincia La nuova provincia.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Restituisce il comune di residenza.
     *
     * @post result == comune
     *
     * @return Il comune.
     */
    public String getComune() {
        return comune;
    }

    /**
     * Imposta il comune di residenza.
     *
     * @pre comune != null && comune != ""
     * @post this.comune == comune
     *
     * @param comune Il nuovo comune.
     */
    public void setComune(String comune) {
        this.comune = comune;
    }

    /**
     * Restituisce la via di residenza.
     *
     * @post result == via
     *
     * @return La via.
     */
    public String getVia() {
        return via;
    }

    /**
     * Imposta la via di residenza.
     *
     * @pre via != null && via != ""
     * @post this.via == via
     *
     * @param via La nuova via.
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Restituisce il numero civico.
     *
     * @post result == numeroCivico
     *
     * @return Il numero civico.
     */
    public Integer getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * Imposta il numero civico.
     *
     * @pre numeroCivico != null
     * @post this.numeroCivico == numeroCivico
     *
     * @param numeroCivico Il nuovo numero civico.
     */
    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * Restituisce il CAP.
     *
     * @post result == CAP
     *
     * @return Il CAP.
     */
    public Integer getCAP() {
        return CAP;
    }

    /**
     * Imposta il CAP.
     *
     * @pre CAP != null
     * @post this.CAP == CAP
     *
     * @param CAP Il nuovo CAP.
     */
    public void setCAP(Integer CAP) {
        this.CAP = CAP;
    }

    /**
     * Restituisce il numero di telefono.
     *
     * @post result == numeroTelefono
     *
     * @return Il numero di telefono.
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * Imposta il numero di telefono.
     *
     * @pre numeroTelefono != null && numeroTelefono != ""
     * @post this.numeroTelefono == numeroTelefono
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
     * @post result == sesso
     *
     * @return Il sesso del cliente.
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il sesso del cliente.
     *
     * @pre sesso != null && sesso != ""
     * @post this.sesso == sesso
     *
     * @param sesso Il nuovo sesso del cliente.
     */
    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    /**
     * Restituisce la data di nascita del cliente.
     *
     * @post result == dataNascita
     *
     * @return La data di nascita.
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta la data di nascita del cliente.
     *
     * @pre dataNascita != null
     * @post this.dataNascita == dataNascita
     *
     * @param dataNascita La nuova data di nascita.
     */
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce la nazionalità del cliente.
     *
     * @post result == nazionalità
     *
     * @return La nazionalità del cliente.
     */
    public String getNazionalita() {
        return nazionalità;
    }

    /**
     * Imposta la nazionalità del cliente.
     *
     * @pre nazionalita != null && nazionalita != ""
     * @post nazionalità == nazionalita
     *
     * @param nazionalita La nuova nazionalità del cliente.
     */
    public void setNazionalita(String nazionalita) {
        this.nazionalità = nazionalita;
    }

    /**
     * Imposta il codice fiscale del cliente
     *
     * @pre cf != null && cf != ""
     * @post this.cf == cf
     *
     * @param cf il nuovo codice fiscale
     */
    public void setCf(String cf){
        this.cf = cf;
    }

    /**
     * Mostra il codice fiscale del cliente
     *
     * @post result == cf
     *
     * @return cf il codice fiscale del cliente
     */
    public String getCf() {
        return cf;
    }

    /**
     * Setta l'email del cliente
     *
     * @pre email != null && email != ""
     * @post this.email == email
     *
     * @param email l'email nuova del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Mostra l'email di un cliente
     *
     * @post result == email
     *
     * @return email l'email del cliente
     */
    public String getEmail() {
        return email;
    }



    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cf='" + cf + '\'' +
                ", email='" + email + '\'' +
                ", cognome='" + cognome + '\'' +
                ", provincia='" + provincia + '\'' +
                ", comune='" + comune + '\'' +
                ", via='" + via + '\'' +
                ", numeroCivico=" + numeroCivico +
                ", CAP=" + CAP +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", isBlacklisted=" + isBlacklisted +
                ", sesso='" + sesso + '\'' +
                ", dataNascita=" + dataNascita +
                ", nazionalità='" + nazionalità + '\'' +
                ",Camera = " + camera + "}";
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
        return numeroCivico.equals(cliente.getNumeroCivico()) && nome.equalsIgnoreCase(cliente.getNome()) && cognome.equalsIgnoreCase(cliente.getCognome()) &&
                cf.equalsIgnoreCase(cliente.getCf()) && email.equalsIgnoreCase(cliente.getEmail()) && provincia.equalsIgnoreCase(cliente.getProvincia())
                && comune.equalsIgnoreCase(cliente.getComune()) && via.equalsIgnoreCase(cliente.getVia()) && CAP.equals(cliente.getCAP()) && numeroTelefono.equalsIgnoreCase(cliente.getNumeroTelefono())
                && isBlacklisted == cliente.isBlacklisted() && sesso.equalsIgnoreCase(cliente.getSesso()) && camera.equals(cliente.getCamera()) && dataNascita.equals(cliente.getDataNascita())
                && nazionalità.equalsIgnoreCase(cliente.getNazionalita());
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