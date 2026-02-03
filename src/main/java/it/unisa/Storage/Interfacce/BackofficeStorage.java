package it.unisa.Storage.Interfacce;

import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.Collection;

public interface BackofficeStorage<T>
{

    /**
     * Salva un nuovo impiegato nel database.
     *
     * @param o L'impiegato da salvare
     * @throws SQLException Se si verifica un errore di database
     *
     * @pre impiegato != null
     * @pre impiegato.getCodiceFiscale() != null
     * @pre Tutti i campi obbligatori di impiegato sono valorizzati
     *
     * @post Impiegato inserito nel database
     * @post impiegato.getId() valorizzato
     * @post impiegato.getUsername() = "Manager" + id
     */
    void doSave(T o) throws SQLException;


    /**
     * Elimina un oggetto dal database.
     *
     * @param o L'impiegato da eliminare
     * @throws SQLException Se si verifica un errore di database
     *
     * @pre impiegato != null
     * @pre impiegato.getCodiceFiscale() != null
     *
     * @post Impiegato rimosso dal database (se esistente)
     */
    void doDelete(T o) throws SQLException;


    /**
     * Recupera un impiegato tramite chiave (CF o ID).
     *
     * @param index Chiave di ricerca (String per CF, Integer per ID)
     * @return L'impiegato trovato
     * @throws SQLException
     *
     * @pre index != null
     * @pre index instanceof String || index instanceof Integer
     *
     * @post result != null
     * @post result.getCodiceFiscale() = index || result.getId() = index
     */
    T doRetriveByKey(Object index) throws SQLException;


    /**
     * Recupera tutti gli impiegati dal database.
     *
     * @param order Campo per l'ordinamento (deve essere in whitelist)
     * @return Collection di tutti gli impiegati
     * @throws SQLException
     *
     * @pre orderBy == null || whitelist.contains(orderBy)
     *
     * @post result != null
     * @post result.size() >= 0
     */
    Collection<T> doRetriveAll(String order) throws SQLException;


    /**
     * Aggiorna i dati di un impiegato esistente.
     *
     * @param o L'impiegato con i dati aggiornati
     * @throws SQLException Se si verifica un errore di database
     *
     * @pre impiegato != null
     * @pre impiegato.getCodiceFiscale() != null
     *
     * @post Dati dell'impiegato aggiornati nel database
     */
    void doUpdate(T o) throws SQLException;


    /**
     * Permette di recuperare tutte le entità sul database che hanno un attributo di nome {@code attribute}
     * con un valore uguale a {@code valore}.
     * @param attribute nome dell'attributo.
     * @param value valore dell'attributo.
     * @return  {@code Collection<T>} di oggetti per il quale {@code attribute} {@code =value}.
     * @throws SQLException se c'è errore di accesso/connessione/retrieval col database.
     *
     * @pre attribute != null && attribute != "" && value != null
     * @post result != null
     */
    Collection<T> doRetriveByAttribute(String attribute, Object value) throws SQLException;


    /**
     * Filtra gli impiegati secondo criteri specificati.
     *
     * @param nome
     * @param sesso
     * @param ruolo
     * @param orderBy
     * @return Collection di impiegati filtrati
     * @throws SQLException
     *
     * @pre Almeno un parametro di filtro != null
     * @pre orderBy == null || whitelist.contains(orderBy)
     *
     * @post result != null
     */
    Collection<T> doFilter(String nome, String sesso, Ruolo ruolo, String orderBy) throws  SQLException;
}
