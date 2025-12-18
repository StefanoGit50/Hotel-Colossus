package it.unisa.gestionePrenotazioni.gestionePrenotazioni;

import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;

import java.io.Serializable;

public class Prenotazione implements Serializable
{
    private final static long serialVersionUID = 32434L; 
    
    private String id;
    private Cliente cliente;
    private Stanza stanza;

    public Prenotazione() {
    }

    public Prenotazione(String id, Cliente cliente, Stanza stanza) {
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

    public Stanza getStanza() {
        return stanza;
    }
    
    // SETTERS

    public void setId(String id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }
    
}
