package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Comando per modificare i dati di una prenotazione (eccetto il codice identificativo, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdatePrenotazioneCommand implements Command {

    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazione;
    private Prenotazione prenotazioneNonModificata;
    /**
     * Costruttore del comando.
     * @param prenotazione  Prenotazione da modificare.
     */
    public UpdatePrenotazioneCommand(ArrayList<Prenotazione> list,Prenotazione prenotazione) {
        this.listaPrenotazioni = list;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdatePrenotazioneCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public ArrayList<Prenotazione> getCatalogue() {
        return listaPrenotazioni;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(ArrayList<Prenotazione> catalogue) {
        this.listaPrenotazioni = catalogue;
    }


    /**
     * Restituisce prenotazione.
     *
     * @post result == prenotazione
     * @return prenotazione
     */
    public Prenotazione getPrenotazione() {
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
     * @post CatalogoPrenotazioni.listaPrenotazioni.stream().anyMatch(p | p.IDPrenotazione == prenotazione.IDPrenotazione)
     */
    @Override
    public void execute() {
       prenotazioneNonModificata= prenotazione;
       boolean bool =CatalogoPrenotazioni.UpdatePrenotazioni(prenotazione);
        if(!bool){
            throw new NoSuchElementException("Prenotazione non trovata");
        }

    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.stream().anyMatch(p | p.IDPrenotazione == prenotazioneNonModificata.IDPrenotazione)
     */
    @Override
    public void undo() {

        if(prenotazioneNonModificata != null) {
            for(Prenotazione p : CatalogoPrenotazioni.getListaPrenotazioni()){
                CatalogoPrenotazioni.removePrenotazioni(p);
                CatalogoPrenotazioni.addPrenotazioni(prenotazioneNonModificata);
            }
        }

    }

}
