package it.unisa.Server.gestionePrenotazioni.persistent.util;

import it.unisa.Server.gestionePrenotazioni.persistent.obj.Camera;
import it.unisa.Server.gestionePrenotazioni.persistent.obj.Cliente;
import it.unisa.Server.gestionePrenotazioni.persistent.obj.Servizio;

import java.util.ArrayList;

public class Util {

    /**
     * Metodo di utilità per creare una copia profonda (deep copy) di un ArrayList di elementi clonabili.
     *
     * @param <T> Il tipo di elemento, che deve implementare {@code Cloneable}.
     * @param daCopiare L'ArrayList da copiare.
     * @return Un nuovo ArrayList contenente copie (cloni) degli elementi.
     */
    public static <T extends Cloneable> ArrayList<T> deepCopyArrayList(ArrayList<T> daCopiare) {
        if (daCopiare == null) return new ArrayList<>();

        ArrayList<T> copia = new ArrayList<>(daCopiare.size());
        try {
            for (T elemento : daCopiare) {
                // Si richiede il cast sicuro e si presume che T.clone() sia pubblico
                if (elemento instanceof Camera) {
                    copia.add((T) ((Camera) elemento).clone());
                } else if (elemento instanceof Servizio) {
                    copia.add((T) ((Servizio) elemento).clone());
                } else if (elemento instanceof Cliente) {
                    copia.add((T) ((Cliente) elemento).clone());
                }
            }
        } catch (CloneNotSupportedException e) {
            // Gestione dell'errore di clonazione
            throw new RuntimeException("Clonazione non supportata per gli elementi della lista", e);
        }
        return copia;
    }
}
