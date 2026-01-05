package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per poter bannare un cliente.
 */
public class BanCommand implements Command {

    private CatalogoClienti catalogue;
    private String CFCliente;

    /**
     * Costruttore del comando.
     * @param catalogue Catalogo dei clienti.
     * @param CFCliente Codice fiscale del cliente a cui si vuole rimuovere il ban.
     */
    public BanCommand(CatalogoClienti catalogue, String CFCliente) {
        this.catalogue = catalogue;
        this.CFCliente = CFCliente;
    }

    /**
     * Costruttore vuoto
     */
    public BanCommand() {
    }

    public CatalogoClienti getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogoClienti catalogue) {
        this.catalogue = catalogue;
    }

    public String getCFCliente() {
        return CFCliente;
    }

    public void setCFCliente(String CFCliente) {
        this.CFCliente = CFCliente;
    }

    /**
     * Banna il cliente.
     */
    @Override
    public void execute() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            Iterator<Cliente> it = lc.iterator(); // Evita di modificare l'array metre lo si itera
            Cliente cli;
            while (it.hasNext()) {
                cli = it.next();

                if(cli.equals(c)) {
                    cli.setBlacklisted(true); // ban
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

    /**
     * Rimuovi il ban al cliente.
     */
    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();


            Iterator<Cliente> it = lcb.iterator(); // Evita di modificare l'array metre lo si itera
            Cliente cli;
            while (it.hasNext()) {
                cli = it.next();

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

}
