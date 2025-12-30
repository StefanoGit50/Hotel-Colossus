package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class CapException extends RuntimeException {
    public CapException(){}
    public CapException(String message) {
        super(message);
    }
}
