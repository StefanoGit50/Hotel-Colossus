package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class ComuneException extends RuntimeException {
    public ComuneException(){}
    public ComuneException(String message) {
        super(message);
    }
}
