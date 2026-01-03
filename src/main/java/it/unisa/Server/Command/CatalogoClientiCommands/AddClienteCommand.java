package it.unisa.Server.Command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.Command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClientiPublisher;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Comando per registrare un cliente (ovvero aggiungerlo alla lista dei clienti).
 */
public class AddClienteCommand implements Command {

    private CatalogoClientiPublisher catalogue;
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti per poter completare il comando.
     * @param cliente   Cliente da registrare.
     */
    public AddClienteCommand(CatalogoClientiPublisher catalogue, Cliente cliente) {
        this.catalogue = catalogue;
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public AddClienteCommand() {
    }

    public CatalogoClientiPublisher getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoClientiPublisher catalogue) {
        this.catalogue = catalogue;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void execute() {
        ArrayList<Cliente> lc = catalogue.getListaClienti();
        lc.add(cliente);
        catalogue.setListaClienti(lc);
    }

    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = catalogue.getListaClienti();
            lc.remove(c);
            catalogue.setListaClienti(lc);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
