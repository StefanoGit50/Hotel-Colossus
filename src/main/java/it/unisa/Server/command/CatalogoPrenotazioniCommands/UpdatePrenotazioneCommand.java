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

    private CatalogoPrenotazioni catalogue = new CatalogoPrenotazioni();
    private Prenotazione prenotazione;
    private Prenotazione prenotazioneNonModificata;
    /**
     * Costruttore del comando.
     * @param prenotazione  Prenotazione da modificare.
     */
    public UpdatePrenotazioneCommand(Prenotazione prenotazione) {
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
    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
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
       prenotazioneNonModificata = null;

        ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();

        for (int i = 0; i < lp.size(); i++) {
            Prenotazione corrente = lp.get(i);

            if (Objects.equals(corrente.getIDPrenotazione(), prenotazione.getIDPrenotazione())) {
                try {
                    prenotazioneNonModificata = corrente.clone();
                }catch (CloneNotSupportedException c) {
                    c.printStackTrace();
                    return;
                }
                CatalogoPrenotazioni.UpdatePrenotazioni(prenotazione);
            }
        }

        throw new NoSuchElementException("Prenotazione non trovata");
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoPrenotazioni.listaPrenotazioni.stream().anyMatch(p | p.IDPrenotazione == prenotazioneNonModificata.IDPrenotazione)
     */
    @Override
    public void undo() {

        if(prenotazioneNonModificata != null) {
            ArrayList<Prenotazione> lp = CatalogoPrenotazioni.getListaPrenotazioni();

            for (int i = 0; i < lp.size(); i++) {
                if (Objects.equals(lp.get(i).getIDPrenotazione(), prenotazioneNonModificata.getIDPrenotazione())) {
                    CatalogoPrenotazioni.UpdatePrenotazioni(prenotazioneNonModificata); // UNDO
                    return;
                }
            }
        }

    }

}
