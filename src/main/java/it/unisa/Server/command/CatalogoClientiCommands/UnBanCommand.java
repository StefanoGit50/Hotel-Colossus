package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClientiPublisher;

import java.util.ArrayList;

/**
 * Comando per poter rimuovere il ban da un cliente.
 */
public class UnBanCommand implements Command {

    private CatalogoClientiPublisher catalogue;
    private String CFCliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti.
     * @param CFCliente Codice fiscale del cliente a cui si vuole rimuovere il ban.
     */
    public UnBanCommand(CatalogoClientiPublisher catalogue, String CFCliente) {
        this.catalogue = catalogue;
        this.CFCliente = CFCliente;
    }

    /**
     * Costruttore vuoto.
     */
    public UnBanCommand() {}

    public CatalogoClientiPublisher getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoClientiPublisher catalogue) {
        this.catalogue = catalogue;
    }

    public String getCFCliente() {
        return CFCliente;
    }

    public void setCFCliente(String CFCliente) {
        this.CFCliente = CFCliente;
    }

    /**
     * Rimuovi il ban al cliente.
     */
    @Override
    public void execute() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            for (Cliente cli : lcb) {
                if(cli.equals(c)) {
                    cli.setBlacklisted(false); // annulla il ban
                    lcb.remove(cli); // rimuovi il cliente dalla lista dei clienti bannati
                    lc.add(cli); // aggiungilo alla lista dei clienti (non bannati)
                }
            }

            catalogue.setListaClienti(lc);
            catalogue.setListaClientiBannati(lcb);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimetti il ban cl cliente.
     */
    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            for (Cliente cli : lc) {
                if(cli.equals(c)) {
                    cli.setBlacklisted(true); // rimetti il ban
                    lc.remove(cli); // rimuovi il cliente dalla lista dei clienti (non bannati)
                    lcb.add(cli); // aggiungilo alla lista dei clienti (bannati)
                }
            }

            catalogue.setListaClienti(lc);
            catalogue.setListaClientiBannati(lcb);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
