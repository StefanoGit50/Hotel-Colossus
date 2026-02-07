package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per poter bannare un cliente.
 */
public class BanCommand implements Command {

    private CatalogoClienti catalogue = new CatalogoClienti();
    private String CFCliente;

    /**
     * Costruttore del comando.
     * @param CFCliente Codice fiscale del cliente a cui si vuole rimuovere il ban.
     */
    public BanCommand(String CFCliente) {
        this.CFCliente = CFCliente;
    }

    /**
     * Costruttore vuoto
     */
    public BanCommand() {
    }


    /**
     * Restituisce il valore di catalogue.
     *
     * @post result == catalogue
     */
    public CatalogoClienti getCatalogue() {
        return catalogue;
    }


    /**
     * Imposta il valore di catalogue.
     *
     * @pre catalogue != null
     * @post this.catalogue == catalogue
     */
    public void setCatalogue(CatalogoClienti catalogue) {
        this.catalogue = catalogue;
    }


    /**
     * Restituisce il valore di cFCliente.
     *
     * @post result == CFCliente
     */
    public String getCFCliente() {
        return CFCliente;
    }


    /**
     * Imposta il valore di cFCliente.
     *
     * @pre CFCliente != null && CFCliente != ""
     * @post this.CFCliente == CFCliente
     */
    public void setCFCliente(String CFCliente) {
        this.CFCliente = CFCliente;
    }


    /**
     * Esegue il comando.
     *
     * @post CatalogoClienti.listaClientiBannati.stream().anyMatch(c | c.cf == CFCliente && c.isBlacklisted == true)
     */
    @Override
    public void execute() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = CatalogoClienti.getListaClienti(), lcb = CatalogoClienti.getListaClientiBannati();

            Cliente cli;
            for(int i = 0; i < lc.size(); i++) {
                cli = lc.get(i);

                if(cli.equals(c)) {
                    cli.setBlacklisted(true); // ban
                    lc.remove(cli); // rimuovi il cliente dalla lista dei clienti (non bannati)
                    lc.add(cli); // aggiungilo alla lista dei clienti (bannati)
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.doUpdate(cli);
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
     * @post CatalogoClienti.listaClienti.stream().anyMatch(c | c.cf == CFCliente && c.isBlacklisted == false)
     */
    @Override
    public void undo() {
        try {
            Cliente c = catalogue.getCliente(CFCliente);
            ArrayList<Cliente> lc = CatalogoClienti.getListaClienti(), lcb = CatalogoClienti.getListaClientiBannati();

            Cliente cli;
            for (int i = 0; i < lcb.size(); i++) {
                cli = lcb.get(i);

                if(cli.equals(c)) {
                    cli.setBlacklisted(false); // annulla il ban
                    lcb.remove(cli); // rimuovi il cliente dalla lista dei clienti bannati
                    lc.add(cli); // aggiungilo alla lista dei clienti (non bannati)
                    try{
                        ClienteDAO clienteDAO = new ClienteDAO();
                        Cliente cliente = clienteDAO.doRetriveByKey(cli.getCf());
                        cliente.setBlacklisted(false);
                        clienteDAO.doSave(cliente);
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
