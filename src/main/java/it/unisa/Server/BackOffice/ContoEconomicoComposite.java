package it.unisa.Server.BackOffice;

import java.util.List;

public class ContoEconomicoComposite extends ContoEconomicoComponentAbstract {

    public ContoEconomicoComposite(String nome) {
        super(nome);
    }

    /**
     * Aggiunge il child alla collezione.
     *
     * @pre child != null
     * @post figli.contains(child)
     */
    @Override
    public void addChild(ContoEconomicoComponentAbstract child) {
        figli.add(child);
    }


    /**
     * Rimuove il child dalla collezione.
     *
     * @pre child != null
     * @post not figli.contains(child)
     */
    @Override
    public void removeChild(ContoEconomicoComponentAbstract child) {
        figli.remove(child);
    }


    /**
     * Restituisce il valore di importoTotale.
     *
     * @post result == figli->iterate(c; acc: double == 0 | acc + c.getImportoTotale())
     */
    @Override
    public double getImportoTotale() {
        double totale = 0;
        for (ContoEconomicoComponentAbstract c : figli) {
            totale += c.getImportoTotale(); // ricorsione
        }
        return totale;
    }


    /**
     * Restituisce il valore di figli.
     *
     * @post result == figli
     */
    public List<ContoEconomicoComponentAbstract> getFigli() {
        return figli;
    }


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @pre indent != null
     */
    @Override
    public void stampaAlbero(String indent, boolean ultimo) {
        System.out.println(indent +
                (ultimo ? "└── " : "├── ") + getNomeComponente() + " = " + getImportoTotale());

        String nuovoPrefisso = indent + (ultimo ? "    " : "│   ");

        for (int i = 0; i < figli.size(); i++) {
            figli.get(i).stampaAlbero(
                    nuovoPrefisso,
                    i == figli.size() - 1
            );
        }
    }


    /**
     * Restituisce il valore di totalePerTipo.
     *
     * @pre tipo != null
     * @post result == figli->iterate(c; acc: double == 0 | acc + c.getTotalePerTipo(tipo))
     */
    @Override
    public double getTotalePerTipo(TipoVoce tipo) {
        double totale = 0;
        for (ContoEconomicoComponentAbstract c : figli) {
            if (c instanceof ContoEconomicoLeaf leaf) {
                totale += leaf.getTotalePerTipo(tipo);
            } else {
                totale += ((ContoEconomicoComposite) c).getTotalePerTipo(tipo);
            }
        }
        return totale;
    }
}