package it.unisa.Server.commandPattern.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.commandPattern.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;

import java.util.ArrayList;

public class RetrieveAllCCommand implements Command {

    /**
     * Catalogo dei clienti.
     */
    private CatalogoClienti catalogue;

    /**
     * Lista di clienti di ritorno.
     */
    private ArrayList<Cliente> clienti;

    /**
     * Costruttore vuoto del comando.
     */
    public RetrieveAllCCommand() {
        clienti = new ArrayList<>(0);
        catalogue = new CatalogoClienti();
    }

    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     */
    public CatalogoClienti getCatalogue() {
        return catalogue;
    }

    /**
     * Imposta il valore di catalogue.
     *
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoClienti catalogue) {
        this.catalogue = catalogue;
    }

    /**
     * Restituisce la lista di clienti (indipendentemente dall'esecuzione del comando o meno).
     *
     * @post    this.clienti.size() == 0, se non è stato ancora eseguito il metodo execute()
     *          this.clienti.size() >= 0 && this.clienti.size() <= # clienti presenti nel sistema nel sistema.
     * @return
     */
    public ArrayList<Cliente> getClienti() {
        return clienti;
    }

    /**
     * Esegue il comando.
     *
     * @post CatalogoClienti.listaClienti.contains(cliente)
     */
    @Override
    public void execute() {
        clienti = CatalogoClienti.getListaClientiBannati();
        clienti.addAll(CatalogoClienti.getListaClienti());
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post not CatalogoClienti.listaClienti.contains(cliente)
     */
    @Override
    public void undo() {
        clienti = new ArrayList<>(0);
    }

}
