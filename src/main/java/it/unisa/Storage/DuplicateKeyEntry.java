package it.unisa.Storage;

public class DuplicateKeyEntry extends RuntimeException {
    public DuplicateKeyEntry() {
        super();
    }
    public DuplicateKeyEntry(String message) {
        super(message);
    }
}
