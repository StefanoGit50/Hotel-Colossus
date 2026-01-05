package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class EmailException extends RuntimeException{
    public EmailException(){}
    public EmailException(String message) {
        super(message);
    }
}
