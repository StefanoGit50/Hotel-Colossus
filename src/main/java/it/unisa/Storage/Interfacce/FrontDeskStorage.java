package it.unisa.Storage.Interfacce;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public interface FrontDeskStorage<T>
{

    /**
     * Salva un oggetto nel db
     *
     * @param o
     * @pre o != null
     * @post inserisce l'oggetto nel database
     * @throws SQLException
     */
    void doSave(T o) throws SQLException;


    /**
     * Salva un insieme di oggetti nel db
     *
     * @param list
     * @pre list != null
     * @post inserisce gli oggetti nel database
     * @throws SQLException
     */
    void doSaveAll(List<T> list) throws SQLException;


    /**
     * Cancella l oggetto dal db.
     *
     * @param o
     * @pre o != null
     * @post l'oggetto non c'è piu nel db
     * @throws SQLException
     */
    void doDelete(T o) throws SQLException;


    /**
     * Ottiene  oggetto da chiave.
     *
     * @param oggetto
     * @pre oggetto != null
     * @post result == null || result != null
     * @return T
     * @throws SQLException
     */
    T doRetriveByKey(Object oggetto) throws SQLException;


    /**
     * Ottiene tutti gli oggetti dal DB.
     *
     * @param order
     * @pre order != null && order != ""
     * @post result != null
     * @return {@link Collection}
     * @throws SQLException
     */
    Collection<T> doRetriveAll(String order) throws SQLException;


    /**
     * Aggiorna i dati di un oggetto esistente nel database.
     *
     * @param o La camera con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NoSuchElementException Se il parametro o è null quindi non trovato.
     * @pre o != null
     * @post record nel database viene aggiornato con i nuovi valori
     * @post la chiave primaria rimane invariata
     */
    void doUpdate(T o) throws SQLException;

    /**
     *
     * Ottiene oggetto tramite attributo.
     *
     * @pre attribute != null && attribute != "" && value != null && value != ""
     * @post result != null
     *
     *
     * @param attribute;
     * @param value;
     * @return Collection<>;
     * @throws SQLException;
     */
    Collection<T> doRetriveByAttribute(String attribute , Object value) throws SQLException;
}