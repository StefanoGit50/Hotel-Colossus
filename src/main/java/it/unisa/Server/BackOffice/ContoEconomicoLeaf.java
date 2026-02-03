package it.unisa.Server.BackOffice;

public class ContoEconomicoLeaf extends ContoEconomicoComponentAbstract {

    private double importo;
    private TipoVoce tipo;
    public ContoEconomicoLeaf(String nomeVoce, double importo, TipoVoce tipo) {
        super(nomeVoce);
        this.importo = importo;
        this.tipo = tipo;
    }


    /**
     * Restituisce il valore di importoTotale.
     *
     * @post result == importo
     */
    @Override
    public double getImportoTotale() {
        return importo;
    }


    /**
     * Restituisce il valore di totalePerTipo.
     *
     * @pre t != null
     * @post result == (tipo == t implies result == importo) && (tipo != t implies result == 0)
     */
    // nuovo metodo minimo per tipo
    public double getTotalePerTipo(TipoVoce t) {
        return tipo == t ? importo : 0;
    }


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre indent != null
     */
    @Override
    public void stampaAlbero(String indent, boolean ultimo) {

        System.out.println(indent + getNomeComponente() + " (Totale: " + getImportoTotale() + ")");
    }


}
