package it.unisa.Server.command.CatalogoClientiCommands;

import it.unisa.Common.Cliente;
import it.unisa.Server.command.Command;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Comando per poter rimuovere il ban da un cliente.
 */
public class UnBanCommand implements Command {

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



    /**
     * Restituisce il valore di cFCliente.
     *
     * @post result == CFCliente
     * @return cFCliente
     */
    public String getCFCliente() {
        return CFCliente;
    }


    /**
     * Imposta il valore di cFCliente.
     *
     * @param CFCliente
     * @pre CFCliente != null && CFCliente != ""
     * @post this.CFCliente == CFCliente
     */
    public void setCFCliente(String CFCliente) {
        this.CFCliente = CFCliente;
    }

    /**
     * Rimuovi il ban al cliente.
     *
     * @post CatalogoClienti.listaClienti.stream().anyMatch(c | c.cf == CFCliente && c.isBlacklisted == false)
     *
     */
    private Cliente c = null;

    @Override
    public void execute() {
        boolean bool=false;
        try {
             c = CatalogoClienti.getCliente(CFCliente);
            if(c!=null) {
                for (int i = 0; i < CatalogoClienti.getListaClienti().size(); i++) {
                    if (CatalogoClienti.getListaClienti().get(i).getCf().equals(CFCliente)) {
                        bool = CatalogoClienti.getlistaBannati().remove(c);
                        c.setBlacklisted(false);
                        CatalogoClienti.updateCliente(c);
                    }
                }
                if (!bool) {
                    throw new RuntimeException("Unban non disponibile");
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimetti il ban cl cliente.
     *
     * @post CatalogoClienti.listaClientiBannati.stream().anyMatch(c | c.cf == CFCliente && c.isBlacklisted == true)
     *
     */
    @Override
    public void undo() {
        boolean bool= false;
        if(c!=null) {
            for (int i = 0; i < CatalogoClienti.getListaClienti().size(); i++) {
                if (CatalogoClienti.getListaClienti().get(i).getCf().equals(CFCliente)) {
                    bool = CatalogoClienti.getlistaBannati().add(c);
                    CatalogoClienti.updateCliente(c);
                }
            }
            if (!bool) {
                throw new RuntimeException("Undo Unban non disponibile");
            }
        }

    }
}
