package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class NomeException extends RuntimeException{
    public NomeException(){}
    public NomeException(String message) {
        super(message);
    }
}
