package it.unisa.Server.commandPattern.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;

import java.util.ArrayList;

public class RetrieveAllPCommand implements Command {

    /**
     * Catalogo delle prenotazioni.
     */
    private CatalogoPrenotazioni catalogue;

    /**
     * Lista di prenotazioni di ritorno.
     */
    private ArrayList<Prenotazione> prenotazioni;

    /**
     * Costruttore vuoto del comando.
     */
    public RetrieveAllPCommand() {
        prenotazioni = new ArrayList<>(0);
        catalogue = new CatalogoPrenotazioni();
    }

    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     */
    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }

    /**
     * Imposta il valore di catalogue.
     *
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
    }

    /**
     * Restituisce la lista di prenotazioni (indipendentemente dall'esecuzione del comando o meno).
     *
     * @post    this.prenotazioni.size() == 0, se non è stato ancora eseguito il metodo execute()
     *          this.prenotazioni.size() >= 0 && this.prenotazioni.size() <= # prenotazioni presenti nel sistema nel sistema.
     * @return
     */
    public ArrayList<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Esegue il comando.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void execute() {
        prenotazioni = CatalogoPrenotazioni.getListaPrenotazioni();
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post not CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void undo() {
        prenotazioni = new ArrayList<>(0);
    }

}
