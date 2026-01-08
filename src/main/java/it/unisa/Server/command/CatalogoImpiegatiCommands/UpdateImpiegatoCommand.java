package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per modificare i dati di registrazione di un impiegato (eccetto il codice fiscale, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdateImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue;
    private Impiegato impiegato;
    private Impiegato impiegatoNonModificato;
    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei impiegati per poter completare il comando.
     * @param impiegato   Impiegato da modificare.
     */
    public UpdateImpiegatoCommand(CatalogoImpiegati catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdateImpiegatoCommand() {
    }

    public CatalogoImpiegati getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoImpiegati catalogue) {
        this.catalogue = catalogue;
    }

    public Impiegato getImpiegato() {
        return impiegato;
    }

    public void setImpiegato(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    @Override
    public void execute() {
        try {
            Impiegato i = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();

            Iterator<Impiegato> it = li.iterator(); // Evita di modificare l'array metre lo si itera
            Impiegato imp;
            while (it.hasNext()) {
                imp = it.next();

                if(imp.getCodiceFiscale().equals(i.getCodiceFiscale())) {
                    impiegatoNonModificato = imp;
                    li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                    li.add(i); // aggiungi l'impiegato 'modificato' alla lista dei impiegati
                    break;
                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Impiegato c = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();

            Iterator<Impiegato> it = li.iterator(); // Evita di modificare l'array metre lo si itera
            Impiegato imp;
            while (it.hasNext()) {
                imp = it.next();

                if(imp.getCodiceFiscale().equals(c.getCodiceFiscale())) {
                    li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                    li.add(impiegatoNonModificato); // aggiungi l'impiegato 'modificato' alla lista dei impiegati
                    break;
                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
