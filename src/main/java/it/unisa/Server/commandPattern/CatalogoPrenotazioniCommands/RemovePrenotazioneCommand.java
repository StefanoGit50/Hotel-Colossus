package it.unisa.Server.commandPattern.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;

import java.util.ArrayList;


/**
 * Comando che permette di eliminare una prenotazione (eliminarla dal catalogo delle prenotazioni).
 */
public class RemovePrenotazioneCommand implements Command {

    private Prenotazione prenotazione;

    /**
     * Costruttore del comando
     * @param prenotazione  Oggetto {@code Prenotazione} da eliminare.
     */
    public RemovePrenotazioneCommand(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public RemovePrenotazioneCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public ArrayList<Prenotazione> getCatalogue() {
        return CatalogoPrenotazioni.getListaPrenotazioni();
    }


    /**
     * Restituisce il valore di prenotazione.
     *
     * @post result == prenotazione
     * @return prenotazione
     */
    public Prenotazione getPrenotazioni() {
        return prenotazione;
    }


    /**
     * Imposta il valore di prenotazione.
     *
     * @param prenotazione
     * @pre prenotazione != null
     * @post this.prenotazione == prenotazione
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    /**
     * Esegue il comando.
     *
     * @post not CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void execute() throws IllegalAccess {
        ArrayList<Prenotazione> p =CatalogoPrenotazioni.getListaPrenotazioni();
        CatalogoPrenotazioni.removePrenotazioni(prenotazione);
        if(p.equals(CatalogoPrenotazioni.getListaPrenotazioni()))throw  new IllegalAccess("Prenotazione non rimossa");
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.contains(prenotazione)
     */
    @Override
    public void undo() {
        CatalogoPrenotazioni.addPrenotazioni(prenotazione);
    }
}
