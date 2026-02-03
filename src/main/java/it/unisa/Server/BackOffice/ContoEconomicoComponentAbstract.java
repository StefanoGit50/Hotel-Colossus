package it.unisa.Server.BackOffice;

import java.util.ArrayList;
import java.util.List;

public abstract class ContoEconomicoComponentAbstract {
    private String nomevoce;
    private String descrizione;
    private double prezzo;
    protected List<ContoEconomicoComponentAbstract> figli = new ArrayList<>();


    /**
     * Restituisce il valore di nomeComponente.
     *
     * @post result == nomevoce
     * @return nomevoce
     */
    public String getNomeComponente(){
        return nomevoce;
    }


    /**
     * Restituisce il valore di prezzo.
     *
     * @post result == prezzo
     * @return prezzo
     */
    public double getPrezzo() {
        return prezzo;
    }


    /**
     * Imposta il valore di prezzo.
     *
     * @pre prezzo != null
     * @post this.prezzo == prezzo
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public ContoEconomicoComponentAbstract(){

    }

    public ContoEconomicoComponentAbstract(String nomevoce) {
        this.nomevoce = nomevoce;
    }

    public abstract double getImportoTotale();


    /**
     * Aggiunge il child alla collezione.
     *
     * @param child
     * @pre child != null
     * @post figli.contains(child)
     */
    public void addChild(ContoEconomicoComponentAbstract child) {
        throw new UnsupportedOperationException("Foglia non supporta operazione");
    }


    /**
     * Rimuove child dalla collezione.
     *
     * @param child
     * @pre child != null
     * @post not figli.contains(child)
     */
    public void removeChild(ContoEconomicoComponentAbstract child) {
        throw new UnsupportedOperationException("Foglia non supporta operazione");
    }



    public abstract double getTotalePerTipo(TipoVoce tipo);

    public abstract void stampaAlbero(String indent, boolean ultimo);
}
