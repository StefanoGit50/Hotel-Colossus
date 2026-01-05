package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class SessoException extends RuntimeException {
    public SessoException(){}
    public SessoException(String message) {
        super(message);
    }
}
