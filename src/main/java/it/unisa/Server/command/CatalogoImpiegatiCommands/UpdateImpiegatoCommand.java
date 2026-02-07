package it.unisa.Server.command.CatalogoImpiegatiCommands;

import it.unisa.Common.Impiegato;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Storage.DAO.ImpiegatoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per modificare i dati di registrazione di un impiegato (eccetto il codice fiscale, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdateImpiegatoCommand implements Command {

    private CatalogoImpiegati catalogue = new CatalogoImpiegati();
    private Impiegato impiegato;
    private Impiegato impiegatoNonModificato;
    /**
     * Costruttore del comando.
     * @param impiegato   Impiegato da modificare.
     */
    public UpdateImpiegatoCommand(Impiegato impiegato) {
        this.impiegato = impiegato;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdateImpiegatoCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public CatalogoImpiegati getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoImpiegati catalogue) {
        this.catalogue = catalogue;
    }


    /**
     * Restituisce il valore di impiegato.
     *
     * @post result == impiegato
     * @return impiegato
     */
    public Impiegato getImpiegato() {
        return impiegato;
    }


    /**
     * Imposta il valore di impiegato.
     *
     * @param impiegato
     * @pre impiegato != null
     * @post this.impiegato == impiegato
     */
    public void setImpiegato(Impiegato impiegato) {
        this.impiegato = impiegato;
    }


    /**
     * Esegue il comando.
     *
     * @post CatalogoImpiegati.listaImpiegati.stream().anyMatch(i | i.codiceFiscale == impiegato.codiceFiscale)
     */
    @Override
    public void execute() throws IllegalAccess {
        try {
            Impiegato i = CatalogoImpiegati.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();

            Iterator<Impiegato> it = li.iterator(); // Evita di modificare l'array metre lo si itera
            Impiegato imp;

            while (it.hasNext()) {
                imp = it.next();

                if(imp.getCodiceFiscale().equals(i.getCodiceFiscale())) {
                    impiegatoNonModificato = imp;
                    li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                    li.add(i);// aggiungi l'impiegato 'modificato' alla lista dei impiegati
                    try{
                        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
                        impiegatoDAO.doUpdate(i);
                    }catch(SQLException sqlException){
                        sqlException.printStackTrace();
                        throw new IllegalAccess("ERROR: l'impiegato non è stato modificato");
                    }
                    break;
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoImpiegati.listaImpiegati.stream().anyMatch(i | i.codiceFiscale == impiegatoNonModificato.codiceFiscale)
     */
    @Override
    public void undo() {
        try {
            Impiegato c = CatalogoImpiegati.getImpiegato(impiegato.getCodiceFiscale());
            ArrayList<Impiegato> li = CatalogoImpiegati.getListaImpiegati();

            Iterator<Impiegato> it = li.iterator(); // Evita di modificare l'array metre lo si itera
            Impiegato imp;
            while (it.hasNext()) {
                imp = it.next();

                if(imp.getCodiceFiscale().equals(c.getCodiceFiscale())) {
                    li.remove(imp); // rimuovi l'impiegato 'non modificato' dalla lista dei impiegati
                    li.add(impiegatoNonModificato); // aggiungi l'impiegato 'modificato' alla lista dei impiegati
                    try{
                        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
                        impiegatoDAO.doUpdate(impiegatoNonModificato);
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                    break;
                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
