package it.unisa.Common;

import java.util.Objects;

/**
 * Rappresenta un Utente generico del sistema.
 * Contiene le credenziali di accesso di base.
 */
public class Utente implements Cloneable {

    /**
     * Nome utente univoco utilizzato per l'accesso.
     */
    private String username;

    /**
     * Password hash-ata per l'autenticazione.
     */
    private String hashedPassword;

    /**
     * Costruttore per creare una nuova istanza di {@code Utente}.
     *
     * @param username Il nome utente.
     * @param hashedPassword La password già hash-ata.
     */
    public Utente(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public Utente(){
        username = "";
        hashedPassword = "";
    }
    /**
     * Restituisce il nome utente.
     *
     * @return Lo username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta il nome utente.
     *
     * @param username Il nuovo nome utente.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce la password cifrata (hashed).
     *
     * @return La password in formato hash.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Imposta una nuova password hash-ata.
     *
     * @param newHashedPassword La nuova password hash-ata da assegnare.
     */
    public void setNewHashedPassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    /**
     * Indica se un altro oggetto è "uguale a" questo utente.
     * Il confronto è basato sul nome utente e sulla password hash-ata.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se i due oggetti Utente sono uguali, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utente utente = (Utente) obj;
        return Objects.equals(username, utente.username) && Objects.equals(hashedPassword, utente.hashedPassword);
    }

    /**
     * Crea e restituisce una copia dell'oggetto (clone).
     *
     * @return Una copia (clone) dell'oggetto {@code Utente}.
     * @throws CloneNotSupportedException Se l'oggetto non implementa l'interfaccia {@code Cloneable}.
     */
    @Override
    public Utente clone() throws CloneNotSupportedException {
        return (Utente) super.clone();
    }
}