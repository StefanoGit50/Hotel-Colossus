package it.unisa.Storage.DAO.Eccezioni.Cliente;

public class ViaException extends RuntimeException{
    public ViaException(){}
    public ViaException(String message) {
        super(message);
    }
}
