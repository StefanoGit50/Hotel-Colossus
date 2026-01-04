package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegatiPublisher;

import java.util.ArrayList;

/**
 * Comando per registrare un nuovo impiegato (aggiungerlo al catalogo degli impiegati).
 */
public class AddImpiegatoCommand implements Command {

    private CatalogoImpiegatiPublisher catalogue;
    private Impiegato impiegato;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo degli impiegati per poter completare il comando.
     * @param impiegato   Impiegato da registrare.
     */
    public AddImpiegatoCommand(CatalogoImpiegatiPublisher catalogue, Impiegato impiegato) {
        this.catalogue = catalogue;
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public AddImpiegatoCommand() {
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
        ArrayList<Impiegato> li = catalogue.getListaImpiegati();
        li.add(impiegato);
        catalogue.setListaImpiegati(li);
    }

    @Override
    public void undo() {
        try {
            Impiegato i = catalogue.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = catalogue.getListaImpiegati();
            li.remove(i);
            catalogue.setListaImpiegati(li);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
