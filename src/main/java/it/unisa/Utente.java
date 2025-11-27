package it.unisa;

import java.util.Objects;

public class Utente implements Cloneable{
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Utente[" +
                "password='" + password + '\'' +
                ", nomeUtente='" + nomeUtente + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(nomeUtente, utente.nomeUtente) && Objects.equals(password, utente.password);
    }

    @Override
    public Utente clone() {
        Utente utente = null;
        try {
            utente = (Utente) super.clone();
        } catch (CloneNotSupportedException e) {
            return  null;
        }

        return utente;
    }
}
