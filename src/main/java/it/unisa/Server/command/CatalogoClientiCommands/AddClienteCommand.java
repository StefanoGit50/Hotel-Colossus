package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;

/**
 * Comando per registrare un cliente (ovvero aggiungerlo alla lista dei clienti).
 */
public class AddClienteCommand implements Command {

    private CatalogoClienti catalogue = new CatalogoClienti();
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param cliente   Cliente da registrare.
     */
    public AddClienteCommand(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public AddClienteCommand() {
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
     * Restituisce il valore di cliente.
     *
     * @post result == cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Imposta il valore di cliente.
     *
     * @pre cliente != null
     * @post this.cliente == cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Esegue il comando.
     *
     * @post CatalogoClienti.listaClienti.contains(cliente)
     */
    @Override
    public void execute() {
       CatalogoClienti.aggiungiCliente(cliente);
    }

    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post not CatalogoClienti.listaClienti.contains(cliente)
     */
    @Override
    public void undo() {
            if(CatalogoClienti.getListaClienti().contains(cliente)) {
                CatalogoClienti.removeCliente(cliente);
            }
    }
}
