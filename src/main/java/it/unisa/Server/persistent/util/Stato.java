package it.unisa.Server.persistent.util;

import java.io.Serializable;

/**
 * Elenco degli stati possibili di una camera
 */
public enum Stato implements Serializable {
    OutOfOrder,
    InPulizia,
    InServizio,
    Occupata,
    Libera,
    Prenotata
}
