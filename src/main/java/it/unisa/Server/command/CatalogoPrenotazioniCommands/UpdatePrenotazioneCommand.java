package it.unisa.Server.command.CatalogoPrenotazioniCommands;

import it.unisa.Common.Prenotazione;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per modificare i dati di una prenotazione (eccetto il codice identificativo, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdatePrenotazioneCommand implements Command {

    private CatalogoPrenotazioni catalogue;
    private Prenotazione prenotazione;
    private Prenotazione prenotazioneNonModificata;
    /**
     * Costruttore del comando.
     * @param catalogue     Catalogo delle prenotazioni per poter completare il comando.
     * @param prenotazione  Prenotazione da modificare.
     */
    public UpdatePrenotazioneCommand(CatalogoPrenotazioni catalogue, Prenotazione prenotazione) {
        this.catalogue = catalogue;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdatePrenotazioneCommand() {
    }

    public CatalogoPrenotazioni getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoPrenotazioni catalogue) {
        this.catalogue = catalogue;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    @Override
    public void execute() {
        try {
            Prenotazione p = catalogue.getPrenotazione(prenotazione.getIDPrenotazione());
            ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();
            Iterator<Prenotazione> it = lp.iterator(); // Evita di modificare l'array metre lo si itera
            Prenotazione pren;
            while (it.hasNext()) {
                pren = it.next();

                if(pren.getIDPrenotazione() ==  p.getIDPrenotazione()) {
                    prenotazioneNonModificata = pren;
                    lp.remove(p); // rimuovi la prenotazione 'non modificata' dalla lista delle prenotazioni
                    lp.add(prenotazione); // aggiungi la prenotazione 'modificata' alla lista delle prenotazioni
                    FrontDeskStorage<Prenotazione> prenotazioneFrontDeskStorage = new PrenotazioneDAO();
                    prenotazioneFrontDeskStorage.doUpdate(prenotazione);
                    break;
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }catch (SQLException sqlException){

        }
    }

    @Override
    public void undo() {
        try {
            ArrayList<Prenotazione> p =new ArrayList<>();
            p.add(catalogue.getPrenotazione(prenotazione.getIDPrenotazione()));
            ArrayList<Prenotazione> lp = catalogue.getListaPrenotazioni();

            Iterator<Prenotazione> it = lp.iterator(); // Evita di modificare l'array metre lo si itera
            Prenotazione pren;
            while (it.hasNext()) {
                pren = it.next();

                if(pren.getIDPrenotazione() == p.getFirst().getIDPrenotazione()) {
                    catalogue.removePrenotazioni(p); // rimuovi il prenotazione 'non modificato' dalla lista dei prenotazioni
                    ArrayList<Prenotazione> prenotazione =  new ArrayList<>();
                    prenotazione.add(prenotazioneNonModificata);
                    catalogue.addPrenotazioni(prenotazione); // aggiungi il prenotazione 'modificato' alla lista dei prenotazioni
                    break;
                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
