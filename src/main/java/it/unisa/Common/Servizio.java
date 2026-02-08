package it.unisa.Common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta un Servizio offerto in generale,
 * identificato da un nome e un prezzo.
 * Questa classe può essere utilizzata per definire voci di costo in un contesto
 * alberghiero (es. Tassa Soggiorno, Minibar, Penale, etc.).
 */
public class Servizio implements Cloneable, Serializable {

    /**
     * Il nome identificativo del servizio (es. "Tassa Soggiorno", "Minibar").
     */
    private String nome;

    /**
     * Il prezzo associato al servizio.
     */
    private double prezzo;

    private  int quantita;
    private int id;
    /**
     * Costruttore per creare una nuova istanza di {@code Servizio}.
     *
     * @param nome Il nome del servizio.
     * @param prezzo Il prezzo del servizio.
     */
    public Servizio(String nome, double prezzo) {
        this.id=0;  //l'id viene settato quando fatto il retrieve
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = 0;
    }

    public int getQuantita(){
        return this.quantita;
    }
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }


    /**
     * Costruttore vuoto
     */
    public Servizio() {}

    /**
     * Restituisce il nome del servizio.
     *
     * @post result == nome
     *
     * @return Il nome del servizio.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del servizio.
     *
     * @pre nome != null && nome != ""
     * @post this.nome == nome
     *
     * @param nome Il nuovo nome da assegnare al servizio.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il prezzo del servizio.
     *
     * @post result == prezzo
     *
     * @return Il prezzo del servizio.
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo del servizio.
     *
     * @pre prezzo != null
     * @post this.prezzo == prezzo
     *
     * @param prezzo Il nuovo prezzo da assegnare al servizio.
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Servizio{" +
                "nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                '}';
    }

    /**
     * Indica se un altro oggetto è "uguale a" questo servizio.
     * Due servizi sono considerati uguali se hanno lo stesso nome e lo stesso prezzo.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se questa servizio è uguale a {@code obj}, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Servizio servizio = (Servizio) obj;
        return Double.compare(servizio.prezzo, prezzo) == 0 && nome.equalsIgnoreCase(servizio.getNome());
    }

    /**
     * Crea e restituisce una copia dell'oggetto (clone).
     * Poiché la classe contiene solo tipi primitivi e una stringa immutabile,
     * un clone superficiale è sufficiente e agisce come una deep copy.
     *
     * @return Una copia (clone) dell'oggetto {@code Servizio}.
     * @throws CloneNotSupportedException Se l'oggetto non implementa l'interfaccia {@code Cloneable}.
     */
    @Override
    public Servizio clone() throws CloneNotSupportedException {
        return (Servizio) super.clone();
    }
}