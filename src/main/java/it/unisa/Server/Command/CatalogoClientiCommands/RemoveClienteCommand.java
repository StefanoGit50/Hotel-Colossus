package it.unisa.Server.Command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.Command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClientiPublisher;

import java.util.ArrayList;

/**
 * Comando per eliminare un cliente (ovvero rimuoverlo dalla lista dei clienti).
 */
public class RemoveClienteCommand implements Command {

    private CatalogoClientiPublisher catalogue;
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti per poter completare il comando.
     * @param cliente   Cliente da eliminare.
     */
    public RemoveClienteCommand(CatalogoClientiPublisher catalogue, Cliente cliente) {
        this.catalogue = catalogue;
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveClienteCommand() {
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
        try {
            // Riprendi il cliente come lo è attualmente nel catalogo
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc;
            if(c.isBlacklisted()) {
                lc = catalogue.getListaClientiBannati();
                lc.remove(c);
                catalogue.setListaClienti(lc);
            } else {
                lc = catalogue.getListaClienti();
                lc.remove(c);
                catalogue.setListaClienti(lc);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Cliente> lc;
        if(cliente.isBlacklisted()) {
            lc = catalogue.getListaClientiBannati();
            lc.add(cliente);
            catalogue.setListaClienti(lc);
        } else {
            lc = catalogue.getListaClienti();
            lc.add(cliente);
            catalogue.setListaClienti(lc);
        }
    }
}
