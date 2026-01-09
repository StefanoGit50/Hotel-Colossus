package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per eliminare un cliente (ovvero rimuoverlo dalla lista dei clienti).
 */
public class RemoveClienteCommand implements Command {

    private CatalogoClienti catalogue;
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti per poter completare il comando.
     * @param cliente   Cliente da eliminare.
     */
    public RemoveClienteCommand(CatalogoClienti catalogue, Cliente cliente) {
        this.catalogue = catalogue;
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveClienteCommand() {
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
        try {
            // Riprendi il cliente come lo è attualmente nel catalogo
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc;
            if(c.isBlacklisted()) {
                lc = CatalogoClienti.getListaClientiBannati();
                lc.remove(c);
            } else {
                lc = CatalogoClienti.getListaClienti();
                lc.remove(c);
            }

            ClienteDAO clienteDAO = new ClienteDAO();
            clienteDAO.doDelete(c);
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Override
    public void undo() {
        ArrayList<Cliente> lc;
        if(cliente.isBlacklisted()) {
            lc = CatalogoClienti.getListaClientiBannati();
            lc.add(cliente);
            try{
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.doSave(cliente);
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }
        } else {
            lc = CatalogoClienti.getListaClienti();
            lc.add(cliente);
            try{
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.doSave(cliente);
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
    }
}
