package it.unisa.Storage;

import java.io.Serial;

public class DuplicateKeyEntry extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3382985647473234362L;

    public DuplicateKeyEntry() {
        super();
    }
    public DuplicateKeyEntry(String message) {
        super(message);
    }
}
