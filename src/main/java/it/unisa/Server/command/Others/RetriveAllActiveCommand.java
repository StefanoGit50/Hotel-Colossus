package it.unisa.Server.command.Others;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;

import java.time.LocalDate;
import java.util.ArrayList;

public class RetriveAllActiveCommand implements Command {

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
    public RetriveAllActiveCommand() {
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
        prenotazioni.removeIf(
                prenotazione -> prenotazione.getDataInizio().isAfter(LocalDate.now()) ||
                prenotazione.getDataFine().isBefore(LocalDate.now()) || prenotazione.getDataFine().isEqual(LocalDate.now()));
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
