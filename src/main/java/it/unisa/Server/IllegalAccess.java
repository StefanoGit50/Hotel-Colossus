package it.unisa.Server;

public class IllegalAccess extends RuntimeException {
    public IllegalAccess(String message) {
        super(message);
    }
}
