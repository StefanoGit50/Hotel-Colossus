package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClientiPublisher;

import java.util.ArrayList;

/**
 * Comando per modificare i dati di registrazione di un cliente (eccetto il codice fiscale, il quale viene
 * usato come chiave per recuperare un'entità).
 */
public class UpdateClienteCommand implements Command {

    private CatalogoClientiPublisher catalogue;
    private Cliente cliente;
    private Cliente clienteNonModificato;
    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti per poter completare il comando.
     * @param cliente   Cliente da modificare.
     */
    public UpdateClienteCommand(CatalogoClientiPublisher catalogue, Cliente cliente) {
        this.catalogue = catalogue;
        this.cliente = cliente;
    }

    /**
     * Costruttore vuoto.
     */
    public UpdateClienteCommand() {
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
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            // Ricerca in entrambe le liste (clienti bannati e non)
            for (Cliente cli : lc) {
                if(cli.getCf().equals(c.getCf())) {
                    clienteNonModificato = cli;
                    lc.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lc.add(c); // aggiungi il cliente 'modificato' alla lista dei clienti
                }
            }
            for (Cliente cli : lcb) {
                if(cli.getCf().equals(c.getCf())) {
                    clienteNonModificato = cli;
                    lcb.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lcb.add(c); // aggiungi il cliente 'modificato' alla lista dei clienti
                }
            }

            catalogue.setListaClienti(lc);
            catalogue.setListaClientiBannati(lcb);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(cliente.getCf());
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            // Ricerca in entrambe le liste (clienti bannati e non)
            for (Cliente cli : lc) {
                if(cli.getCf().equals(c.getCf())) {
                    lc.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lc.add(clienteNonModificato); // aggiungi il cliente 'modificato' alla lista dei clienti
                }
            }
            for (Cliente cli : lcb) {
                if(cli.getCf().equals(c.getCf())) {
                    lcb.remove(cli); // rimuovi il cliente 'non modificato' dalla lista dei clienti
                    lcb.add(clienteNonModificato); // aggiungi il cliente 'modificato' alla lista dei clienti
                }
            }

            catalogue.setListaClienti(lc);
            catalogue.setListaClientiBannati(lcb);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
