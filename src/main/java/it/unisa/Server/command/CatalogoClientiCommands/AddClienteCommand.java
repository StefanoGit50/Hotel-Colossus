package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
       catalogue.aggiungiCliente(cliente);
    }

    @Override
    public void undo() {
            if(catalogue.getListaClienti().contains(cliente)) {
                catalogue.removeCliente(cliente);
            }
    }
}
