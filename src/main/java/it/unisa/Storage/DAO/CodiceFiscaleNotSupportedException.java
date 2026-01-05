package it.unisa.Storage.DAO;

public class CodiceFiscaleNotSupportedException extends RuntimeException{
    public CodiceFiscaleNotSupportedException(){}
    public CodiceFiscaleNotSupportedException(String message) {
        super(message);
    }
}
