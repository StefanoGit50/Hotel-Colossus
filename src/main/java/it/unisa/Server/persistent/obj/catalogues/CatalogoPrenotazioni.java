package it.unisa.Server.persistent.obj.catalogues;
import it.unisa.Common.Camera;
import it.unisa.Common.Prenotazione;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Gestisce l'elenco complessivo delle prenotazioni, permettendo la registrazione,
 * la ricerca e la rimozione delle stesse.
 */
public class CatalogoPrenotazioni implements Serializable {
    private static ArrayList<Prenotazione> listaPrenotazioni;

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
    public synchronized static ArrayList<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta o sostituisce l'intera lista delle prenotazioni.
     */
    public synchronized static void addPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni1) {
        try {
            for (Prenotazione p : listaPrenotazioni1) {
                listaPrenotazioni.add(p.clone());
            }
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        }
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
        // 1. Data Inizio (Arrivo)
        if (inizio.isBefore(LocalDate.now())) {
            throw new InvalidInputException("[Data Inizio] errato");
        }

        // 2. Data Fine (Partenza)
        if (fine.isBefore(inizio) || fine.isEqual(inizio) || fine.isBefore(LocalDate.now())) {
            throw new InvalidInputException("[Data Fine] errato");
        }

        // 3. Lista Camere e Posti
        if (prenotazione.getListaCamere().isEmpty() || nPostiCamere == 0) {
            throw new InvalidInputException("[Lista Camere] errato");
        }

        // 4. Lista Clienti
        if (prenotazione.getListaClienti().isEmpty() || nClienti == 0) {
            throw new InvalidInputException("[Lista Clienti] errato");
        }

        // 5. Corrispondenza Clienti/Posti (Capacità)
        if (nClienti != nPostiCamere) {
            throw new InvalidInputException("[Capacità Camere] errato");
        }

        // 6. Tipo Documento
        if (!documento.equalsIgnoreCase("carta d'identità") &&
                        !documento.equalsIgnoreCase("passaporto") &&
                        !documento.equalsIgnoreCase("patente") ) {
            throw new InvalidInputException("[Tipo Documento] errato");
        }

        // 7. Data Rilascio Documento
        if (rilascio.isAfter(LocalDate.now())) {
            throw new InvalidInputException("[Data Rilascio Documento] errato");
        }

        // 8. Data Scadenza Documento
        if (scadenza.isBefore(LocalDate.now()) || scadenza.isBefore(rilascio)) {
            throw new InvalidInputException("[Data Scadenza Documento] errato");
        }
    }
}
