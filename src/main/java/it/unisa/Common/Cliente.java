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
        if(nome != null){
            this.nome = nome;
        }else{
            this.nome = "";
        }

        if(cognome != null){
            this.cognome = cognome;
        }else{
            this.cognome = "";
        }

        if(provincia != null){
            this.provincia = provincia;
        }else{
            this.provincia = "";
        }
        if(comune != null){
            this.comune = comune;
        }else{
            this.comune = "";
        }
        if(via != null){
            this.via = via;
        }else{
            this.via = "";
        }
        if(numeroCivico != null){
            this.numeroCivico = numeroCivico;
        } else{
            this.numeroCivico = 0;
        }

        if(CAP != null){
           this.CAP = CAP;
        }else{
            this.CAP = 0;
        }

        if(numeroCivico != null){
            this.numeroTelefono = numeroTelefono;
        }else{
            this.numeroTelefono = "";
        }

        if(sesso != null){
            this.sesso = sesso;
        }else{
            this.sesso = "";
        }

        if(dataNascita != null){
            this.dataNascita = dataNascita;
        }else{
            this.dataNascita = LocalDate.now();
        }

        if(cf != null){
            this.cf = cf;
        }else{
            this.cf = "";
        }

        if(email != null){
            this.email = email;
        }else{
            this.email = "";
        }

        if(nazionalità != null){
            this.nazionalità = nazionalità;
        }else{
            this.nazionalità = "";
        }
        if(camera != null){
            this.camera = camera;
        }else{
            this.camera = new Camera();
        }
    }

    /**
     * Costruttore vuoto.
     */
    public Cliente() {
        this.nome = "";
        this.cognome = "";
        this.provincia = "";
        this.comune = "";
        this.via = "";
        this.numeroCivico = 0;
        this.CAP = 0;
        this.numeroTelefono = "";
        this.isBlacklisted = false;
        this.sesso = "";
        this.dataNascita = LocalDate.now();
        this.cf = "";
        this.email = "";
        this.nazionalità = "";
        this.camera = new Camera();
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
    public Integer getNumeroCivico() {
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
    public Integer getCAP() {
        return CAP;
    }

    /**
     * Imposta il CAP.
     *
     * @param CAP Il nuovo CAP.
     */
    public void setCAP(Integer CAP) {
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