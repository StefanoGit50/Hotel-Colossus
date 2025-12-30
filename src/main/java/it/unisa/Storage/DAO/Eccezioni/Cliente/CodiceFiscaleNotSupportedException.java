package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class CodiceFiscaleNotSupportedException extends RuntimeException{
    public CodiceFiscaleNotSupportedException(){}
    public CodiceFiscaleNotSupportedException(String message) {
        super(message);
    }
}
