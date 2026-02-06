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

    private CatalogoClienti catalogue = new CatalogoClienti();
    private Cliente cliente;

    /**
     * Costruttore del comando.
     * @param cliente   Cliente da eliminare.
     */
    public RemoveClienteCommand(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public RemoveClienteCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     * @return catalogue
     */
    public CatalogoClienti getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @param catalogue
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
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }


    /**
     * Imposta il valore di cliente.
     *
     * @param cliente
     * @pre cliente != null
     * @post this.cliente == cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    /**
     * Esegue il comando.
     *
     * @post not CatalogoClienti.listaClienti.contains(cliente) && !CatalogoClienti.listaClientiBannati.contains(cliente)
     */
    @Override
    public void execute() {
        CatalogoClienti.removeCliente(cliente);

    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post (cliente.isBlacklisted == true && CatalogoClienti.listaClientiBannati.contains(cliente)) || (cliente.isBlacklisted == false && CatalogoClienti.listaClienti.contains(cliente))
     */
    @Override
    public void undo() {
        ArrayList<Cliente> lc;
        if(cliente.isBlacklisted()) {
            lc = catalogue.getListaClientiBannati();
            lc.add(cliente);
            try{
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.doSave(cliente);
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }
        } else {
            lc = catalogue.getListaClienti();
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
