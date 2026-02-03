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
        try {
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            // Ricerca in entrambe le liste (clienti bannati e non)
            Iterator<Cliente> it = lc.iterator(); // Evita di modificare l'array metre lo si itera
            Cliente cli;
            while (it.hasNext()) {
                cli = it.next();

                if(cli.getCf().equals(c.getCf())) {
                    clienteNonModificato = cli;
                    lc.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lc.add(c); // aggiungi il cliente 'modificato' alla lista dei clienti
                    break;
                }
            }
            it = lcb.iterator();
            while (it.hasNext()) {
                cli = it.next();

                if(cli.getCf().equals(c.getCf())) {
                    clienteNonModificato = cli;
                    lcb.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lcb.add(c); // aggiungi il cliente 'modificato' alla lista dei clienti
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.doUpdate(c);
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


    /**
     * Annulla il comando precedentemente eseguito.
     *
     * @post CatalogoClienti.listaClienti.stream().anyMatch(c | c.cf == clienteNonModificato.cf) || CatalogoClienti.listaClientiBannati.stream().anyMatch(c | c.cf == clienteNonModificato.cf)
     */
    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            // Ricerca in entrambe le liste (clienti bannati e non)
            Iterator<Cliente> it = lc.iterator(); // Evita di modificare l'array metre lo si itera
            Cliente cli;
            while (it.hasNext()) {
                cli = it.next();

                if(cli.getCf().equals(c.getCf())) {
                    lc.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lc.add(clienteNonModificato); // aggiungi il cliente 'modificato' alla lista dei clienti
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.doUpdate(clienteNonModificato);
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                    break;
                }
            }
            it = lcb.iterator();
            while (it.hasNext()) {
                cli = it.next();

                if(cli.getCf().equals(c.getCf())) {
                    lcb.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lcb.add(clienteNonModificato); // aggiungi il cliente 'modificato' alla lista dei clienti
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.doUpdate(clienteNonModificato);
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
