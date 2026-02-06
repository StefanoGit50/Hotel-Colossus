package it.unisa.Server.persistent.obj.catalogues;
import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.persistent.util.Util;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DuplicateKeyEntry;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gestisce l'elenco complessivo delle prenotazioni, permettendo la registrazione,
 * la ricerca e la rimozione delle stesse.
 */
public class CatalogoPrenotazioni implements Serializable {

    private static final Logger log = LogManager.getLogger(CatalogoPrenotazioni.class);
    private static FrontDeskStorage<Prenotazione>fds;
    private static ArrayList<Prenotazione> listaPrenotazioni;

    static {
        try {
            fds = new PrenotazioneDAO();
            listaPrenotazioni = (ArrayList<Prenotazione>) fds.doRetriveAll("IDPrenotazione");
            log.debug("size iniziale :{}", listaPrenotazioni.size());
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Costruttore della classe CatalogoPrenotazioni.
     * @param listaPrenotazioni Lista di prenotazioni di tipo {@link Prenotazione} del catalogo.
     */
    public CatalogoPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni) {
        CatalogoPrenotazioni.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore vuoto.
     */
    public CatalogoPrenotazioni() {
        if(listaPrenotazioni==null) aggiornalista();
    }

    //  Getters / Setters

    /**
     * Restituisce la lista di tutte le prenotazioni nel catalogo.
     *
     * @post result == listaPrenotazioni
     * @return listaPrenotazioni || null
     */
    public synchronized static   ArrayList<Prenotazione> getListaPrenotazioni() {
        log.info("lista prenotazioni nel catalogo :{}", listaPrenotazioni);
        return Util.deepCopyArrayList(listaPrenotazioni);
    }

    /**
     * Restituisce una prenotazione.
     *
     * @pre codicePrenotazione != null
     * @post result == null || result.IDPrenotazione == codicePrenotazione
     */
    public static synchronized Prenotazione getPrenotazione(Integer ID){
        for(Prenotazione p : listaPrenotazioni){
            if(p.getIDPrenotazione().equals(ID))
                try {
                    return p.clone();
                }catch (CloneNotSupportedException e){
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * Aggiunge prenotazioni alla collezione.
     *
     * @param prenotazione
     * @pre listaPrenotazioni1 != null
     * @post listaPrenotazioni.containsAll(listaPrenotazioni1)
     * @return true || false
     */
    public static synchronized boolean addPrenotazioni(Prenotazione prenotazione) {
        if(prenotazione == null|| listaPrenotazioni.contains(prenotazione)) {
            return false;
        }
                FrontDeskStorage<Prenotazione> fd = new PrenotazioneDAO();
                try{
                    fd.doSave(prenotazione);
                } catch (SQLException e) {
                if (e.getErrorCode() == 1062)
                    throw new DuplicateKeyEntry();
                }
                listaPrenotazioni.add(prenotazione);
            return true;
    }


    /**
     * Rimuove una prenotazione dalla lista delle prenotazioni e dal database.
     *
     * @param prenotazione la prenotazione da rimuovere
     * @return true se la prenotazione è stata rimossa con successo, false se era null
     *         o non presente nella lista
     * @throws DuplicateKeyEntry se si verifica un errore SQL con codice 1062
     *
     * @pre prenotazione == null || listaPrenotazioni != null
     * @post se prenotazione == null allora result == false
     * @post se !listaPrenotazioni@pre.contains(prenotazione) allora result == false
     * @post se result == false allora listaPrenotazioni == listaPrenotazioni@pre
     * @post se result == true allora listaPrenotazioni@pre.contains(prenotazione)
     * @post se result == true allora !listaPrenotazioni.contains(prenotazione)
     * @post se result == true allora listaPrenotazioni.size() == listaPrenotazioni@pre.size() - 1
     * @post se result == true allora PrenotazioneDAO.doDelete(prenotazione) è stato invocato
     */
    public static synchronized boolean removePrenotazioni(Prenotazione prenotazione) {
        if(prenotazione == null|| !listaPrenotazioni.contains(prenotazione)) {
            return false;
        }

        FrontDeskStorage<Prenotazione> fd = new PrenotazioneDAO();
        try{
            fd.doDelete(prenotazione);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062)
                throw new DuplicateKeyEntry();
        }
        listaPrenotazioni.remove(prenotazione);
        return true;

    }


    /**
     * Aggiorna una prenotazione esistente nella lista sostituendola con una versione modificata.
     *
     * @param prenotazione la prenotazione con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false se la prenotazione
     *         è null, non è presente nella lista, o si è verificato un errore SQL
     *
     * @pre prenotazione può essere null && listaPrenotazioni != null && fds != null
     * @post se prenotazione == null allora result == false
     * @post se !listaPrenotazioni@pre.contains(prenotazione) allora result == false
     * @post se result == false allora listaPrenotazioni == listaPrenotazioni@pre
     * @post se result == true allora listaPrenotazioni@pre.stream().anyMatch(p ->
     *       p.getIDPrenotazione().equals(prenotazione.getIDPrenotazione()))
     * @post se result == true allora listaPrenotazioni.contains(prenotazione)
     * @post se result == true allora !listaPrenotazioni.stream().anyMatch(p ->
     *       p.getIDPrenotazione().equals(prenotazione.getIDPrenotazione()) && !p.equals(prenotazione))
     * @post se result == true allora listaPrenotazioni.size() == listaPrenotazioni@pre.size()
     * @post se result == true allora fds.doUpdate(prenotazione) è stato invocato con successo
     */
    public static synchronized boolean UpdatePrenotazioni(Prenotazione prenotazione) {
        if(prenotazione == null|| !listaPrenotazioni.contains(prenotazione)) {
            return false;
        }

        for (int i = 0; i < listaPrenotazioni.size(); i++) {
            Prenotazione p = listaPrenotazioni.get(i);

            if (p.getIDPrenotazione().equals(prenotazione.getIDPrenotazione())) {
                try {
                    FrontDeskStorage<Prenotazione> fds = new PrenotazioneDAO(); // O come lo istanzi tu
                    fds.doUpdate(prenotazione);
                    listaPrenotazioni.set(i, prenotazione);

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false; // Non trovato
    }


    /**
     * Aggiorna la lista delle prenotazioni recuperando tutti i dati dal database.
     *
     * @return true se l'aggiornamento è avvenuto con successo, false se si è verificato
     *         un errore SQL durante il recupero dei dati
     *
     * @pre listaPrenotazioni != null
     * @post se result == true allora fds != null && fds.oclIsKindOf(PrenotazioneDAO)
     * @post se result == true allora listaPrenotazioni != null
     * @post se result == true allora listaPrenotazioni contiene tutte le prenotazioni
     *       recuperate dal database tramite fds.doRetriveAll
     * @post se result == true allora le prenotazioni in listaPrenotazioni sono ordinate
     *       in modo decrescente
     * @post se result == false allora si è verificata una SQLException durante
     *       fds.doRetriveAll
     * @post se result == false allora listaPrenotazioni potrebbe essere modificata
     */
    public static synchronized boolean aggiornalista(){
        fds = new PrenotazioneDAO();
        try{
            listaPrenotazioni= (ArrayList<Prenotazione>) fds.doRetriveAll("decrescente");
            System.out.println("lista aggiornata in aggionra lista"+listaPrenotazioni);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Recupera lo storico delle prenotazioni effettuate da un cliente specifico.
     *
     * @pre cliente != null
     * @post result != null
     *
     */
    public static ArrayList<Prenotazione> getStoricoPrenotazioni(Object cliente) {
        return new ArrayList<>();
    }

    /**
     * Cerca una prenotazione specifica tramite il suo numero identificativo e ne restituisce una copia.
     *
     * @param codicePrenotazione Il numero identificativo della prenotazione da cercare.
     * @return Una deep copy dell'oggetto Prenotazione trovata, o {@code null} se non esiste nessuna prenotazione con quel numero.
     */
    public static Prenotazione getPrenotazione(int codicePrenotazione){
        for (Prenotazione p : listaPrenotazioni) {
            if (p.getIDPrenotazione() == codicePrenotazione)
                try {
                    return p.clone();
                } catch (CloneNotSupportedException e) {
                    return null;
                }
        }
        return null;
    }

    /**
     * Metodo usato per controllare la validità dei campi di una prenotazione.
     *
     * @pre prenotazione != null
     *
     * @param prenotazione prenotazione da controllare.
     * @throws InvalidInputException se un campo presenta un valore errato.
     */
    public static void checkPrenotazione(Prenotazione prenotazione) throws InvalidInputException {
        LocalDate inizio = prenotazione.getDataInizio(),  fine = prenotazione.getDataFine(),
                rilascio = prenotazione.getDataRilascio(), scadenza = prenotazione.getDataScadenza();

        Pattern numeroDocumento = Pattern.compile("^[0-9A-Z]{0,9}$");
        String documento = prenotazione.getTipoDocumento();

        int nClienti = prenotazione.getListaClienti().size();
        int nPostiCamere = 0;

        List<Camera> duplicati = new ArrayList<>();
        Camera camera;
        for (Cliente c : prenotazione.getListaClienti()) {
            camera = c.getCamera();
            if (!duplicati.contains(camera)) {
                duplicati.add(camera);
                nPostiCamere +=  c.getCamera().getCapacità();
            }
        }

        // Lista documenti accettabili
        List<String> documentiValidi = List.of(
                "carta d'identità", "passaporto", "patente", "cid"
        );

        // Lista di condizioni che possono lanciare un errore
        // 1. Data Arrivo Passata
        if (inizio.isBefore(LocalDate.now()))
            throw new InvalidInputException("Data di arrivo non può essere passata");

        // 2. Data Partenza precedente alla data di Arrivo
        if (fine.isBefore(inizio))
            throw new InvalidInputException("Data di partenza deve essere successiva alla data di arrivo");

        // 3. Data Partenza uguale alla data di Arrivo
        if (fine.isEqual(inizio))
            throw new InvalidInputException("Data di partenza deve essere successiva alla data di arrivo");

        // 4. Data Partenza passata
        if (fine.isBefore(LocalDate.now()))
            throw new InvalidInputException("Data di partenza non può essere passata");

        // 5. Nessuna Camera selezionata
        boolean flag = true; // True: ogni camera è selezionata
        for (Cliente c : prenotazione.getListaClienti()) {
            if (c.getCamera() == null || c.getCamera().equals(new Camera())) {
                flag = false; // False: almeno una camera è nulla / vuota
            }
        }
        if (!flag)
            throw new InvalidInputException("Almeno una camera deve essere selezionata");

        // 6. Nessun Cliente selezionato
        if (prenotazione.getListaClienti().isEmpty() || nClienti == 0)
            throw new InvalidInputException("Almeno un cliente deve essere selezionato");

        // 7. Tipo Documento non valido
        if (!documentiValidi.contains(prenotazione.getTipoDocumento().toLowerCase())) {
            throw new InvalidInputException("Tipo documento non valido");
        }

        // 8. Mismatch capacità (Clienti vs Posti Camera)
        if (nClienti > nPostiCamere)
            throw new InvalidInputException("Numero clienti non corrisponde alla capacità totale delle camere");

        // 9. Data Rilascio Documento futura
        if (rilascio.isAfter(LocalDate.now()))
            throw new InvalidInputException("Data rilascio documento non può essere futura");

        // 10. Data Scadenza Documento passata
        if (scadenza.isBefore(LocalDate.now()))
            throw new InvalidInputException("Data scadenza documento non può essere passata");

        // 11. Data Scadenza antecedente o uguale alla data di Rilascio
        if (scadenza.isBefore(rilascio) || scadenza.isEqual(rilascio))
            throw new InvalidInputException("Data scadenza documento deve essere successi");

        // 12. Numero documento formato solo da numeri e lettere
        if ( !numeroDocumento.matcher(prenotazione.getNumeroDocumento()).matches() ) {
            throw new InvalidInputException("Il numero documento deve contenere solo lettere (maiuscole) e numeri");
        }

        // 13. Lunghezza numero documento maggiore di 9
        if (prenotazione.getNumeroDocumento().length() > 9) {
            throw new InvalidInputException("Il documento deve avere una lunghezza <= 9");
        }

    }

}
