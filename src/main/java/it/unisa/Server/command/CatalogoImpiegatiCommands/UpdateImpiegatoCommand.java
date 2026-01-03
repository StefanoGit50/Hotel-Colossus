package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegatiPublisher;

import java.util.ArrayList;

/**
 * Comando per modificare i dati di registrazione di un impiegato (eccetto il codice fiscale, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdateImpiegatoCommand implements Command {

    private CatalogoImpiegatiPublisher catalogue;
    private Impiegato impiegato;
    private Impiegato impiegatoNonModificato;
    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei impiegati per poter completare il comando.
     * @param impiegato   Impiegato da modificare.
     */
    public UpdateImpiegatoCommand(CatalogoImpiegatiPublisher catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdateImpiegatoCommand() {
    }

    public CatalogoImpiegatiPublisher getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoImpiegatiPublisher catalogue) {
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
            Impiegato p = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = catalogue.getListaImpiegati();

            for (Impiegato imp : li) {
                impiegatoNonModificato = imp;
                li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                li.add(p); // aggiungi l'impiegato 'modificato' alla lista dei impiegati
            }

            catalogue.setListaImpiegati(li);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Impiegato c = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = catalogue.getListaImpiegati();

            // Ricerca in entrambe le liste (impiegati bannati e non)
            for (Impiegato imp : li) {
                if(imp.getCodiceFiscale().equals(c.getCodiceFiscale())) {
                    li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                    li.add(impiegatoNonModificato); // aggiungi l'impiegato 'modificato' alla lista dei impiegati
                }
            }

            catalogue.setListaImpiegati(li);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
