package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Comando per registrare un cliente (ovvero aggiungerlo alla lista dei clienti).
 */
public class AddClienteCommand implements Command {

    private CatalogoClienti catalogue;
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti per poter completare il comando.
     * @param cliente   Cliente da registrare.
     */
    public AddClienteCommand(CatalogoClienti catalogue, Cliente cliente) {
        this.catalogue = catalogue;
        this.cliente = cliente;
        Collection<Cliente> c = new  ArrayList<>();
    }

    /**
     * Costruttore vuoto.
     */
    public AddClienteCommand() {
    }

    public CatalogoClienti getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoClienti catalogue) {
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
        ArrayList<Cliente> lc = CatalogoClienti.getListaClienti();
        lc.add(cliente);
    }

    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = CatalogoClienti.getListaClienti();
            lc.remove(c);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
