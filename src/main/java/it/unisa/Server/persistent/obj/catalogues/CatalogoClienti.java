package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.util.Util;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Gestisce il catalogo dei clienti dell'hotel.
 * La classe mantiene una lista di tutti i clienti e una lista separata per i clienti
 * inseriti nella blacklist (bannati). Garantisce l'incapsulamento.
 */
public class CatalogoClienti implements Serializable {

    /**
     * Lista interna contenente tutti gli oggetti {@link Cliente}.
     */
    private static FrontDeskStorage<Cliente> frontDeskStorage = new ClienteDAO();
    private static ArrayList<Cliente> listaClienti ;

    /**
     * Lista interna contenente gli oggetti {@link Cliente} bannati.
     */
    private static ArrayList<Cliente> listaClientiBannati = new ArrayList<>();

    static {
        try {
            listaClienti= (ArrayList<Cliente>)frontDeskStorage.doRetriveAll("decrescente");
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
        for( Cliente c : listaClienti){
            if(c.isBlacklisted())
                listaClientiBannati.add(c);
        }
    }


    private boolean updateCliente(Cliente cliente){
         boolean flag=false;
         if(listaClienti.contains(cliente)){
            Iterator<Cliente> it = listaClienti.iterator();
            while (it.hasNext()) {
                Cliente c = it.next();
                if (c.getCf().equals(cliente.getCf()) && !c.equals(cliente)) {
                    try{
                        frontDeskStorage.doUpdate(cliente);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    it.remove();                  // rimuove in sicurezza
                    listaClienti.add(cliente);  // aggiunge cliente modificato
                    flag=true;
                        if(cliente.isBlacklisted())
                            listaClientiBannati.add(cliente);
                }
            }
        }
         return flag;
    }

    public boolean aggiungiCliente(Cliente cliente){
        if(!listaClienti.contains(cliente)){
            try{
                frontDeskStorage.doSave(cliente);
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
            listaClienti.add(cliente);
            return true;
        }
        return false;
    }

    public boolean removeCliente(Cliente cliente){
        if(listaClienti.contains(cliente)){
            try {
                frontDeskStorage.doDelete(cliente);
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
            listaClienti.remove(cliente);
            listaClientiBannati.remove(cliente);
        }
        return true;
    }








    /**
     * Restituisce una deep copy dell'elenco completo dei clienti.
     *
     * @return Un nuovo ArrayList contenente copie (cloni) di tutti gli oggetti Cliente.
     */
    public synchronized static ArrayList<Cliente> getListaClienti() {
        return listaClienti;
    }

    /**
     * Restituisce una deep copy dell'elenco dei clienti bannati.
     *
     * @return Uno nuovo ArrayList contenente copie (cloni) dei clienti bannati.
     */
    public synchronized static ArrayList<Cliente> getListaClientiBannati() {
        return listaClientiBannati;
    }


    /**
     * Imposta una deep copy della lista clienti.
     * @param listaClienti1 {@code ArrayList<Cliente>} da impostare.
     */
    public synchronized static void setListaClienti(ArrayList<Cliente> listaClienti1){
        while(!listaClienti.isEmpty()){
            listaClienti.removeFirst();
        }

        for(Cliente cliente: listaClienti1){
            try{
                listaClienti.add(cliente.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Imposta una deep copy della lista clienti bannati.
     * @param listaClientiBannati1 {@code ArrayList<Cliente>} da impostare.
     */
    public synchronized static void setListaClientiBannati(ArrayList<Cliente> listaClientiBannati1) {
        listaClientiBannati = listaClientiBannati1;
    }

    //
    //  METODI INTERFACCIA PUBLICA
    //

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

        // Tutti i parametri sono nulli
        if( !params[0] && !params[1] && !params[2] && !params[3] && !params[4] ){return null;}

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
                if (cliente.getDataNascita().isAfter(dataNascita) || cliente.getDataNascita().isEqual(dataNascita)) {
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
     * Cerca un cliente specifica tramite il suo codice fiscale e ne restituisce una copia.
     *
     * @param CFCliente Il codice fiscale del cliente da cercare da cercare.
     * @return Una deep copy dell'oggetto Cliente trovato, o {@code null} se non esiste nessun cliente con quel CF.
     * @throws CloneNotSupportedException Se l'oggetto Cliente non supporta la clonazione.
     */
    public Cliente getCliente(String CFCliente) throws CloneNotSupportedException{
        for (Cliente c : listaClienti) {
            if (c.getCf().equals(CFCliente))
                return c.clone();
        }
        for (Cliente c : listaClientiBannati) {
            if (c.getCf().equals(CFCliente))
                return c.clone();
        }
        return null;
    }

    public static void checkCliente(String nome, String cognome, String nazionalita, LocalDate dataNascita, Boolean blackListed) throws InvalidInputException{
        Pattern namePattern = Pattern.compile("^[A-Za-z\\s]{0,49}$");

        // Verifica se tutti i campi sono nulli / vuoti (stringhe)
        if ( (nome == null || nome.isBlank()) && (cognome == null || cognome.isBlank()) && (nazionalita == null || nazionalita.isBlank()) && (blackListed == null)){
            throw new NullPointerException("Tutti i campi sono nulli o vuoti");
        }

        // 1. Nome
        if (nome != null && !namePattern.matcher(nome).matches()) {
            throw new InvalidInputException("[Nome] errato");
        }

        // 2. Cognome
        if (cognome != null && !namePattern.matcher(cognome).matches()) {
            throw new InvalidInputException("[Cognome] errato");
        }

        // 3. Nazionalità
        if (nazionalita != null && !namePattern.matcher(nazionalita).matches()) {
            throw new InvalidInputException("[Nazionalità] errato");
        }

        // 4. Data di Nascita
        if (dataNascita != null && (dataNascita.isAfter(LocalDate.now()) || dataNascita.isEqual(LocalDate.now()))) {
            throw new InvalidInputException("[Data di Nascita] errato");
        }
    }
}


