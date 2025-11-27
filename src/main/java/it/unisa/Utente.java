package it.unisa;

public class Utente {
    private String nomeUtente;
    private String password;
    public Utente(){
        nomeUtente = "";
        password = "";
    }
    public Utente(String nomeUtente,String password){
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }
}
