package it.unisa.Client.Governante;

public class Stanza {
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

    public String toString(){
        return ("numero="+this.numero);
    }

}
