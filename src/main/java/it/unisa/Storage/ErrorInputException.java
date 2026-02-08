package it.unisa.Storage;

import java.io.Serial;

public class ErrorInputException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1187086323373507413L;

    public ErrorInputException(){}
    public ErrorInputException(String message) {
        super(message);
    }
}
