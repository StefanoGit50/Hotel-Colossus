package it.unisa.Server.Eccezioni;

import java.io.Serial;
import java.io.Serializable;

public class IllegalAccess extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = -118359401881935472L;

    public IllegalAccess(String message) {
        super(message);
    }
}
