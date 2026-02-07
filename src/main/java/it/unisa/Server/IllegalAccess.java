package it.unisa.Server;

import java.io.Serializable;

public class IllegalAccess extends Exception implements Serializable {
    public IllegalAccess(String message) {
        super(message);
    }
}
