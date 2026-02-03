package it.unisa.Common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta un Trattamento offerto in generale,
 * identificato da un nome e un prezzo.
 * Questa classe può essere utilizzata per definire voci di costo in un contesto
 * alberghiero (es. Mezza pensione, Pensione completa etc.).
 */
public class Trattamento implements Cloneable, Serializable {

    /**
     * Il nome identificativo del trattamento (es. "Mezza pensione").
     */
    private String nome;

    /**
     * Il prezzo associato al trattamento.
     */
    private double prezzo;

    /**
     * Il prezzo del trattamento all'acquisto
     */

    private final static double EPSILON = 1e10;

    /**
     * Costruttore per creare una nuova istanza di {@code Trattamento}.
     *
     * @param nome Il nome del trattamento.
     * @param prezzo Il prezzo del trattamento.
     */
    public Trattamento(String nome, double prezzo){
        this.nome = nome;
        this.prezzo = prezzo;
    }

    /**
     * Costruttore vuoto
     */
    public Trattamento(){}

    /**
     * Restituisce il nome del trattamento.
     *
     * @post result == nome
     *
     * @return Il nome del trattamento.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del trattamento.
     *
     * @pre nome != null && nome != ""
     * @post this.nome == nome
     *
     * @param nome Il nuovo nome da assegnare al trattamento.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il prezzo del trattamento.
     *
     * @post result == prezzo
     *
     * @return Il prezzo del trattamento.
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo del trattamento.
     *
     * @pre prezzo != null
     * @post this.prezzo == prezzo
     *
     * @param prezzo Il nuovo prezzo da assegnare al trattamento.
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "Trattamento{" +
                "nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                '}';
    }

    /**
     * Indica se un altro oggetto è "uguale a" questo trattamento.
     * Due servizi sono considerati uguali se hanno lo stesso nome e lo stesso prezzo.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se questa trattamento è uguale a {@code obj}, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        Trattamento trattamento = (Trattamento) obj;
        return Math.abs(trattamento.prezzo - prezzo) < EPSILON && nome.equalsIgnoreCase(trattamento.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, prezzo);
    }

    /**
     * Crea e restituisce una copia dell'oggetto (clone).
     * Poiché la classe contiene solo tipi primitivi e una stringa immutabile,
     * un clone superficiale è sufficiente e agisce come una deep copy.
     *
     * @return Una copia (clone) dell'oggetto {@code Trattamento}.
     * @throws CloneNotSupportedException Se l'oggetto non implementa l'interfaccia {@code Cloneable}.
     */
    @Override
    public Trattamento clone() throws CloneNotSupportedException {
        return (Trattamento) super.clone();
    }
}
