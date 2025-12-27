package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Gestisce il catalogo dei clienti dell'hotel.
 * La classe mantiene una lista di tutti i clienti e una lista separata per i clienti
 * inseriti nella blacklist (bannati). Garantisce l'incapsulamento.
 */
public class CatalogoClientiPublisher implements Cloneable {

    /**
     * Lista interna contenente tutti gli oggetti {@link Cliente}.
     */
    private ArrayList<Cliente> listaClienti;

    /**
     * Lista interna contenente gli oggetti {@link Cliente} bannati.
     */
    private ArrayList<Cliente> listaClientiBannati;

    /**
     * Costruttore per creare un nuovo catalogo clienti.
     * Viene eseguita una deep copy delle liste fornite.
     *
     * @param listaClienti L'ArrayList di clienti da copiare nel catalogo.
     */
    public CatalogoClientiPublisher(ArrayList<Cliente> listaClienti) {
        this.listaClienti = Util.deepCopyArrayList(listaClienti);
        ArrayList<Cliente> listaClientiBannati = new ArrayList<>();
        for (Cliente cliente : listaClienti) {
            if (cliente.isBlacklisted()) {
                listaClientiBannati.add(cliente);
            }
        }
        this.listaClientiBannati = Util.deepCopyArrayList(listaClientiBannati);
    }

    /**
     * Restituisce una deep copy dell'elenco completo dei clienti.
     *
     * @return Un nuovo ArrayList contenente copie (cloni) di tutti gli oggetti Cliente.
     */
    public ArrayList<Cliente> getListaClienti() {
        return Util.deepCopyArrayList(listaClienti);
    }

    /**
     * Restituisce una deep copy dell'elenco dei clienti bannati.
     *
     * @return Uno nuovo ArrayList contenente copie (cloni) dei clienti bannati.
     */
    public ArrayList<Cliente> getListaClientiBannati() {
        return Util.deepCopyArrayList(listaClientiBannati);
    }


    /**
     * Esegue una ricerca flessibile all'interno del catalogo clienti basandosi su vari criteri.
     * La ricerca prende almeno un parametro in input mentre gli altri possono essere nulli.
     * Un cliente viene selezionato solo se rispetta tutti i parametri non nulli della ricerca.
     *
     * @param nome Il nome del cliente da cercare (può essere {@code null}).
     * @param cognome Il cognome del cliente da cercare (può essere {@code null}).
     * @param nazionalita La nazionalità (o cittadinanza) del cliente (può essere {@code null}).
     * @param dataNascita La data di nascita del cliente (può essere {@code null}).
     * @param sesso Il sesso del cliente (può essere {@code null}).
     * @return Una deep copy dell'ArrayList contenente tutti i clienti che corrispondono ai criteri di ricerca.
     * @throws CloneNotSupportedException Se il metodo clone non è supportato dalla classe {@code Client}
     */
    public ArrayList<Cliente> cercaClienti(String nome, String cognome, String nazionalita, LocalDate dataNascita, String sesso)
            throws CloneNotSupportedException{
        ArrayList<Cliente> risultati = new ArrayList<>();

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[5];
        params[0] = nome != null;
        params[1] = cognome != null;
        params[2] = nazionalita != null;
        params[3] = dataNascita != null;
        params[4] = sesso != null;

        // ///////////////////////////////////////////////////////////////////////
        // Controllo: tutti i parametri sono nulli
        boolean flag = false;
        for (boolean b : params) {
            if (b) {
                flag = true;
            }
        }
        // Se tutti i parametri sono nulli, restituisci lista vuota
        if (!flag) {
            return null;
        }
        // ////////////////////////////////////////////////////////////////////////

        for (Cliente cliente : listaClienti) {

            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la ricerca
                if (!Objects.equals(cliente.getNome(), nome)) { // Il criterio non è rispettato
                    continue; // L'oggetto cliente non viene aggiunto
                }
            }
            if (params[1]) {
                if (!Objects.equals(cliente.getCognome(), cognome)) {
                    continue;
                }
            }
            if (params[2]) {
                if (!Objects.equals(cliente.getSesso(), sesso)) {
                    continue;
                }
            }
            if (params[3]) {
                if (!Objects.equals(cliente.getDataNascita(), dataNascita)) {
                    continue;
                }
            }
            if (params[4]) {
                if (!Objects.equals(cliente.getNazionalita(),  nazionalita)) {
                    continue;
                }
            }

            risultati.add(cliente.clone());
        }
        return risultati;
    }

    /**
     * Chiamare questo metodo ha l'effetto di bannare il cliente passato come parametro.
     * "Bannare" un cliente significa impostare l'attributo {@code isBanned} a {@code true}
     * e rimuove il cliente dalla lista {@code listaClienti} e lo aggiunge alla lista
     * {@code listaClientiBannati}.
     * @param cliente Cliente da bannare.
     */
    public void ban(Cliente cliente) {
        // Cliente già presente nella lista dei clienti bannati
        if(listaClientiBannati.contains(cliente)) {
            return; // cliente già bannato
        } else {
            // Cliente non presente nella lista bannati AND presente nella lista clienti
            if(listaClienti.contains(cliente)) {
                listaClienti.remove(cliente);
                cliente.setBlacklisted(true);
                listaClientiBannati.add(cliente);
            } else {
                return; // Cliente non registrato
            }
        }
    }

    /**
     * Chiamare questo metodo ha l'effetto di unbannare il cliente passato come parametro.
     * "Unbannare" un cliente significa impostare l'attributo {@code isBanned} a {@code false}
     * e rimuove il cliente dalla lista {@code listaClientiBannati} e lo aggiunge alla lista
     * {@code listaClienti}.
     * @param cliente Cliente a cui rimuovere il ban.
     */
    public void unBan(Cliente cliente) {
        // Cliente già presente nella lista dei clienti (non bannati)
        if(listaClienti.contains(cliente)) {
            return; // cliente non bannato
        } else {
            // Cliente non presente nella lista dei clienti AND presente nella lista clienti bannati
            if(listaClientiBannati.contains(cliente)) {
                listaClientiBannati.remove(cliente);
                cliente.setBlacklisted(false);
                listaClienti.add(cliente);
            } else {
                return; // Cliente non registrato
            }
        }
    }

    public void aggiornaDatiCliente(Cliente cliente) {
        if(listaClienti.contains(cliente)) {
            listaClienti.remove(cliente);
            listaClienti.add(cliente);
        }
    }

}