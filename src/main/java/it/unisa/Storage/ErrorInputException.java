package it.unisa.Storage;

public class ErrorInputException extends RuntimeException{
    public ErrorInputException(){}
    public ErrorInputException(String message) {
        super(message);
    }
}
