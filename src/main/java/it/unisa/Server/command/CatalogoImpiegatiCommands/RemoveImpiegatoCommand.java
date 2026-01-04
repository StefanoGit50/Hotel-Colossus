package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegatiPublisher;

import java.util.ArrayList;

/**
 * Comando per eliminare un impiegato (ovvero rimuoverlo dalla lista dei impiegati).
 */
public class RemoveImpiegatoCommand implements Command {

    private CatalogoImpiegatiPublisher catalogue;
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei impiegati per poter completare il comando.
     * @param impiegato   Impiegato da eliminare.
     */
    public RemoveImpiegatoCommand(CatalogoImpiegatiPublisher catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveImpiegatoCommand() {
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
            // Riprendi l'impiegato per com'è attualmente nel catalogo
            Impiegato i = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = catalogue.getListaImpiegati();
            li.remove(i);
            catalogue.setListaImpiegati(li);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Impiegato> li;
            li = catalogue.getListaImpiegati();
            li.add(impiegato);
            catalogue.setListaImpiegati(li);
    }
}
