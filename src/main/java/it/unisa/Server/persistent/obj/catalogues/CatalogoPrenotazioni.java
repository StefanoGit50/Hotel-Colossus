package it.unisa.Server.persistent.obj.catalogues;
import it.unisa.Common.Camera;
import it.unisa.Common.Prenotazione;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DuplicateKeyEntry;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Gestisce l'elenco complessivo delle prenotazioni, permettendo la registrazione,
 * la ricerca e la rimozione delle stesse.
 */
public class CatalogoPrenotazioni implements Serializable {

    private static FrontDeskStorage<Prenotazione>fds;
    private static Collection<Prenotazione> listaPrenotazioni;

    static {
        try {
            listaPrenotazioni = fds.doRetriveAll("decrescente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Costruttore della classe CatalogoPrenotazioni.
     * @param listaPrenotazioni Lista di prenotazioni di tipo {@link Prenotazione} del catalogo.
     */
    public CatalogoPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni) {
        CatalogoPrenotazioni.listaPrenotazioni = listaPrenotazioni;
         fds = new PrenotazioneDAO();
    }

    /**
     * Costruttore vuoto.
     */
    public CatalogoPrenotazioni() {
        listaPrenotazioni = new ArrayList<>();
    }

    /**
     * Esegue una ricerca flessibile all'interno del catalogo delle prenotazioni basandosi su vari criteri.
     * La ricerca prende almeno un parametro in input mentre gli altri possono essere nulli.
     * Una prenotazione viene selezionato solo se rispetta tutti i parametri non nulli della ricerca.
     *
     * @param nominativoCliente Nome del cliente che ha registrato la prenotazione.
     * @param numeroCamera Numero (una delle/a) camera registrata con la prenotazione.
     * @param dataInizio Data prevista per il check-in.
     * @param dataFine Data prevista per il check-out.
     * @param sort Parametro per indicare l'ordine rispetto la data (ASC=true or DESC=false). Per ASC si intende dalla
     *             data meno imminente a quella più imminente rispetto la data odierna metre il contrario vale per DESC.
     * @return Una deep copy dell'ArrayList contenente tutte le prenotazioni che corrispondono ai criteri di ricerca.
     * @throws CloneNotSupportedException Se il metodo clone non è supportato dalla classe {@code Prenotazione}
     */
    public ArrayList<Prenotazione> cercaPrenotazioni(String nominativoCliente, int numeroCamera,
                                           LocalDate dataInizio, LocalDate dataFine, boolean sort) throws CloneNotSupportedException{

        ArrayList<Prenotazione> risultati = new ArrayList<>();

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[4];
        params[0] = nominativoCliente != null;
        params[1] = numeroCamera >= 0;
        params[2] = dataInizio != null;
        params[3] = dataFine != null;

        // Tutti i parametri sono nulli
        if( !params[0] && !params[1] && !params[2] && !params[3] ){return null;}

        for (Prenotazione prenotazione : listaPrenotazioni) {

            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la ricerca
                if (!Objects.equals(prenotazione.getIntestatario(), nominativoCliente)) { // Il criterio non è rispettato
                    continue; // L'oggetto cliente non viene aggiunto
                }
            }
            if (params[1]) {
                boolean flag = false;
                for (Camera c : prenotazione.getListaCamere()) {
                    if (Objects.equals(c.getNumeroCamera(), numeroCamera)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }
            }
            if (params[2] && params[3]) {
                if ( !(prenotazione.getDataInizio().isAfter(dataInizio) && prenotazione.getDataFine().isBefore(dataFine)) ) {
                    continue;
                }
            }

            risultati.add(prenotazione.clone());
        }

        if (sort) {
            // ASC
            risultati.sort((p1, p2) -> p1.getDataInizio().compareTo(p2.getDataInizio()));
        } else {
            // DESC
            risultati.sort((p1, p2) -> p2.getDataInizio().compareTo(p1.getDataInizio()));
        }
        return risultati;
    }

    //  Getters / Setters

    /**
     * Restituisce la lista di tutte le prenotazioni nel catalogo.
     */
    public synchronized  ArrayList<Prenotazione> getListaPrenotazioni() {
        return (ArrayList<Prenotazione>) listaPrenotazioni;
    }

    public synchronized Prenotazione getPrenotazione(Integer ID){
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
     * Imposta o sostituisce l'intera lista delle prenotazioni.
     */
    public synchronized boolean addPrenotazioni(Prenotazione prenotazione) {
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

    public synchronized boolean removePrenotazioni(Prenotazione prenotazione) {
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

    public synchronized boolean UpdatePrenotazioni(Prenotazione prenotazione) {
        if(prenotazione == null|| !listaPrenotazioni.contains(prenotazione)) {
            return false;
        }
        Iterator<Prenotazione> it = listaPrenotazioni.iterator(); // Evita di modificare l'array metre lo si itera
        while(it.hasNext()) {
            Prenotazione p = it.next();
                if(p.getIDPrenotazione().equals(prenotazione.getIDPrenotazione())){
                    it.remove();
                    listaPrenotazioni.add(prenotazione);
                    try {
                        fds.doUpdate(prenotazione);
                    }catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        return true;
    }


    /**
     * Recupera lo storico delle prenotazioni effettuate da un cliente specifico.
     */
    public ArrayList<Prenotazione> getStoricoPrenotazioni(Object cliente) {
        return new ArrayList<>();
    }

    /**
     * Cerca una prenotazione specifica tramite il suo numero identificativo e ne restituisce una copia.
     *
     * @param codicePrenotazione Il numero identificativo della prenotazione da cercare.
     * @return Una deep copy dell'oggetto Prenotazione trovata, o {@code null} se non esiste nessuna prenotazione con quel numero.
     * @throws CloneNotSupportedException Se l'oggetto Prenotazione non supporta la clonazione.
     */
    public Prenotazione getPrenotazione(int codicePrenotazione) throws CloneNotSupportedException{
        for (Prenotazione p : listaPrenotazioni) {
            if (p.getIDPrenotazione() == codicePrenotazione)
                return p.clone();
        }
        return null;
    }

    /**
     * Metodo usato per controllare la validità dei campi di una prenotazione.
     * @param prenotazione prenotazione da controllare.
     * @throws InvalidInputException se un campo presenta un valore errato.
     */
    public static void checkPrenotazione(Prenotazione prenotazione) throws InvalidInputException {
        LocalDate inizio = prenotazione.getDataInizio(),  fine = prenotazione.getDataFine(),
                rilascio = prenotazione.getDataRilascio(), scadenza = prenotazione.getDataScadenza();

        String documento = prenotazione.getTipoDocumento();

        int nClienti = prenotazione.getListaClienti().size();
        int nPostiCamere = 0;

        for (Camera c : prenotazione.getListaCamere()) {
            nPostiCamere +=  c.getCapacità();
        }

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
        if (prenotazione.getListaCamere().isEmpty() || nPostiCamere == 0)
            throw new InvalidInputException("Almeno una camera deve essere selezionata");

        // 6. Nessun Cliente selezionato
        if (prenotazione.getListaClienti().isEmpty() || nClienti == 0)
            throw new InvalidInputException("Almeno un cliente deve essere selezionato");

        // 7. Tipo Documento non valido
        if (!documento.equalsIgnoreCase("carta d'identità") &&
                !documento.equalsIgnoreCase("passaporto") &&
                !documento.equalsIgnoreCase("patente"))
            throw new InvalidInputException("Tipo documento non valido");

        // 8. Mismatch capacità (Clienti vs Posti Camera)
        if (nClienti != nPostiCamere)
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
    }
    /**

     Metodo usato per controllare la validità dei campi passati al filtro delle prenotazioni.
     @throws InvalidInputException se un campo presenta un valore errato.*/
    public static void checkFiltroPrenotazione(String nome, String cognome, LocalDate dataInizioSoggiorno, LocalDate dataFineSoggiorno, String elementOrder) {
        Pattern namePattern = Pattern.compile("^[A-Za-z\s]{0,49}$");

        // Verifica se tutti i campi sono nulli / vuoti (stringhe)
        if ( (nome == null || nome.isBlank()) && (cognome == null ||
            cognome.isBlank()) && (dataInizioSoggiorno == null) && (dataFineSoggiorno == null)){
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

        // 3. Data di inizio del soggiorno
        if (dataInizioSoggiorno != null && dataInizioSoggiorno.isBefore(LocalDate.now())) {
            throw new InvalidInputException("[dataInizioSoggiorno] passata");
        }

        // 4. Data di fine del soggiorno
        if (dataFineSoggiorno != null && dataFineSoggiorno.isBefore(LocalDate.now())) {
            throw new InvalidInputException("[dataFineSoggiorno] passata");
        }

        if (dataFineSoggiorno != null && dataFineSoggiorno.isBefore(dataInizioSoggiorno)) {
            throw new InvalidInputException("[dataFineSoggiorno] precedente a dataInizioSoggiorno");
        }
    }

}
