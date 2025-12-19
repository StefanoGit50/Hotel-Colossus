package it.unisa.Server.gestionePrenotazioni.gestioneCamere;

import java.io.Serializable;

public class Stanza implements Serializable
{
    private static final long serialVersionUID = 324234L;
    
    private int numero;
    private String statoGlobale;
    private String statoPulizie;

    public Stanza() {
    }

    public Stanza(int numero) {
        this.numero = numero;
        this.statoGlobale = "libera";
        this.statoPulizie = "libera";
    }
    
    // GETTERS AND SETTERS

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getStatoGlobale() {
        return statoGlobale;
    }

    public void setStatoGlobale(String statoGlobale) {
        this.statoGlobale = statoGlobale;
    }

    public String getStatoPulizie() {
        return statoPulizie;
    }

    public void setStatoPulizie(String statoPulizie) {
        this.statoPulizie = statoPulizie;
    }
    
    
    
}
