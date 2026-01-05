package it.unisa.Server.BackOffice;

public class ContoEconomicoLeaf extends ContoEconomicoComponentAbstract {

    private double importo;
    private TipoVoce tipo;
    public ContoEconomicoLeaf(String nomeVoce, double importo, TipoVoce tipo) {
        super(nomeVoce);
        this.importo = importo;
        this.tipo = tipo;
    }
    @Override
    public double getImportoTotale() {
        return importo;
    }


    // nuovo metodo minimo per tipo
    public double getTotalePerTipo(TipoVoce t) {
        return tipo == t ? importo : 0;
    }


    @Override
    public void stampaAlbero(String indent, boolean ultimo) {

        System.out.println(indent + getNomevoce() + " (Totale: " + getImportoTotale() + ")");
    }


}
