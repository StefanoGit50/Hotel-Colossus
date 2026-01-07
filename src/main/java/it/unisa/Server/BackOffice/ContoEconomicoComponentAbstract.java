package it.unisa.Server.BackOffice;

import java.util.ArrayList;
import java.util.List;

public abstract class ContoEconomicoComponentAbstract {
    private String nomevoce;
    private String descrizione;
    private double prezzo;
    protected List<ContoEconomicoComponentAbstract> figli = new ArrayList<>();


    public String getNomeComponente(){
        return nomevoce;
    }
    public double getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public ContoEconomicoComponentAbstract(){

    }

    public ContoEconomicoComponentAbstract(String nomevoce) {
        this.nomevoce = nomevoce;
    }

    public abstract double getImportoTotale();


    public void addChild(ContoEconomicoComponentAbstract child) {
        throw new UnsupportedOperationException("Foglia non supporta operazione");
    }

    public void removeChild(ContoEconomicoComponentAbstract child) {
        throw new UnsupportedOperationException("Foglia non supporta operazione");
    }

    public abstract double getTotalePerTipo(TipoVoce tipo);

    public abstract void stampaAlbero(String indent, boolean ultimo);
}
