package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per modificare i dati di registrazione di un cliente (eccetto il codice fiscale, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdateClienteCommand implements Command {

    private CatalogoClienti catalogue = new CatalogoClienti();
    private Cliente cliente;
    private Cliente clienteNonModificato;
    /**
     * Costruttore del comando.
     * @param cliente   Cliente da modificare.
     */
    public UpdateClienteCommand(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdateClienteCommand() {
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
     * @post CatalogoClienti.listaClienti.stream().anyMatch(c | c.cf == cliente.cf) || CatalogoClienti.listaClientiBannati.stream().anyMatch(c | c.cf == cliente.cf)
     */
    @Override
    public void execute() {
        clienteNonModificato=cliente;
        CatalogoClienti.updateCliente(cliente);
    }


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoClienti.listaClienti.stream().anyMatch(c | c.cf == clienteNonModificato.cf) || CatalogoClienti.listaClientiBannati.stream().anyMatch(c | c.cf == clienteNonModificato.cf)
     */
    @Override
    public void undo() {
        for (int i = 0 ; i<CatalogoClienti.getListaClienti().size() ; i++){
            if (CatalogoClienti.getListaClienti().get(i).getCf().equals(clienteNonModificato.getCf())) {

                CatalogoClienti.getListaClienti().set(i, clienteNonModificato);
                CatalogoClienti.updateCliente(clienteNonModificato);
                break;
            }
        }

    }
}
