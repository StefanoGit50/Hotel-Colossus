package it.unisa.Server.gestionePrenotazioni;

import it.unisa.Common.Camera;
import it.unisa.Server.gestioneClienti.Cliente;

import java.io.Serializable;

public class Prenotazione implements Serializable
{
    private final static long serialVersionUID = 32434L; 
    
    private String id;
    private Cliente cliente;
    private Camera stanza;

    public Prenotazione() {
    }

    public Prenotazione(String id, Cliente cliente, Camera stanza) {
        this.id = id;
        this.cliente = cliente;
        this.stanza = stanza;
    }
    
    // GETTERS

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Camera getStanza() {
        return stanza;
    }
    
    // SETTERS

    public void setId(String id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setStanza(Camera stanza) {
        this.stanza = stanza;
    }

}
