package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class CognomeException extends RuntimeException {
    public CognomeException(){}
    public CognomeException(String message) {
        super(message);
    }
}
