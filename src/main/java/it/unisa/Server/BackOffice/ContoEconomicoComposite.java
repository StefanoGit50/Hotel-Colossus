package it.unisa.Server.BackOffice;

import java.util.List;

public class ContoEconomicoComposite extends ContoEconomicoComponentAbstract {

    public ContoEconomicoComposite(String nome) {
        super(nome);
    }

    @Override
    public void addChild(ContoEconomicoComponentAbstract child) {
        figli.add(child);
    }

    @Override
    public void removeChild(ContoEconomicoComponentAbstract child) {
        figli.remove(child);
    }


    @Override
    public double getImportoTotale() {
        double totale = 0;
        for (ContoEconomicoComponentAbstract c : figli) {
            totale += c.getImportoTotale(); // ricorsione
        }
        return totale;
    }

    public List<ContoEconomicoComponentAbstract> getFigli() {
        return figli;
    }

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