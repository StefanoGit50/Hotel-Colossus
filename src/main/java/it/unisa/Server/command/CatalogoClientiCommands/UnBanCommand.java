package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comando per poter rimuovere il ban da un cliente.
 */
public class UnBanCommand implements Command {

    private CatalogoClienti catalogue = new CatalogoClienti();
    private String CFCliente;

    /**
     * Costruttore del comando.
     * @param CFCliente Codice fiscale del cliente a cui si vuole rimuovere il ban.
     */
    public UnBanCommand(String CFCliente) {
        this.CFCliente = CFCliente;
    }

    /**
     * Costruttore vuoto.
     */
    public UnBanCommand() {}

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
     * Rimuovi il ban al cliente.
     */
    @Override
    public void execute() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            Cliente cli;
            for(int i = 0; i < lcb.size(); i++){
                cli = lcb.get(i);

                if(cli.equals(c)) {
                    cli.setBlacklisted(false); // annulla il ban
                    lcb.remove(cli); // rimuovi il cliente dalla lista dei clienti bannati
                    lc.add(cli); // aggiungilo alla lista dei clienti (non bannati)
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.doUpdate(cli);
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }

                }
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimetti il ban cl cliente.
     */
    @Override
    public void undo() {
        try{
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = catalogue.getListaClienti(), lcb = catalogue.getListaClientiBannati();

            Cliente cli;
            for(int i = 0; i < lc.size(); i++){
                cli = lcb.get(i);

                if(cli.equals(c)) {
                    cli.setBlacklisted(true); // rimetti il ban
                    lc.remove(cli); // rimuovi il cliente dalla lista dei clienti (non bannati)
                    lcb.add(cli); // aggiungilo alla lista dei clienti (bannati)
                    try {
                        ClienteDAO clienteDAO = new ClienteDAO();
                        Cliente cliente = clienteDAO.doRetriveByKey(cli.getCf());
                        cliente.setBlacklisted(true);
                        clienteDAO.doSave(cliente);
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                }
            }
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
