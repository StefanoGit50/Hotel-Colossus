package it.unisa.Server.gestioneClienti;

import java.io.Serializable;

public class Cliente  implements Serializable
{
    private static final long serialVersionUID = 324234L;
    
    private String nome;
    
    public Cliente() {}
    
    public Cliente(String nome){
        this.nome = nome;
    }
    
    // GETTERS AND SETTERS

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    
}
