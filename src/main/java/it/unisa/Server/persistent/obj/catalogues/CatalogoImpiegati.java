package it.unisa.Server.persistent.obj.catalogues;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Il catalogo degli impiegati permette di modificare, accedere e cercare gli impiegati presenti nel sistema.
 */
public class CatalogoImpiegati implements Serializable {

    /**
     * Lista di tutti gli impiegati del sistema.
     */
    private static ArrayList<Impiegato> listaImpiegati = new ArrayList<>();

    // Getters - Setters

    public synchronized static ArrayList<Impiegato> getListaImpiegati() {
        return listaImpiegati;
    }

    public synchronized static void setListaImpiegati(ArrayList<Impiegato> listaImpiegati1) {
        while(!listaImpiegati.isEmpty()){
            listaImpiegati.removeFirst();
        }

        for(Impiegato impiegato : listaImpiegati1){
            try{
                listaImpiegati.add(impiegato.clone());
            }catch(CloneNotSupportedException cloneNotSupportedException){
                cloneNotSupportedException.printStackTrace();
            }

        }

    }

    // Metodi interfaccia publica

    /**
     * Cerca un impiegato specifica tramite il suo codice fiscale e ne restituisce una copia.
     *
     * @param CFImpiegato Il codice fiscale dell'impiegato da cercare da cercare.
     * @return Una deep copy dell'oggetto Impiegato trovato, o {@code null} se non ne esiste nessuno con quel CF.
     * @throws CloneNotSupportedException Se l'oggetto Impiegato non supporta la clonazione.
     */
    public Impiegato getImpiegato(String CFImpiegato) throws CloneNotSupportedException{
        for (Impiegato i : listaImpiegati) {
            if (i.getCodiceFiscale().equals(CFImpiegato))
                return i.clone();
        }
        return null;
    }

    /**
     * Esegue una ricerca flessibile all'interno del catalogo impiegati basandosi su vari criteri.
     * La ricerca prende almeno un parametro in input mentre gli altri possono essere nulli.
     * Un impiegato viene selezionato solo se rispetta tutti i parametri non nulli della ricerca.
     *
     * @param nome Il nome dell'impiegato da cercare (può essere {@code null}).
     * @param cognome Il cognome dell'impiegato da cercare (può essere {@code null}).
     * @param sesso La nazionalità (o cittadinanza) dell'impiegato (può essere {@code null}).
     * @param ruolo {@code Ruolo} Ruolo dell'impiegato (può essere {@code null}).
     * @return Una deep copy dell'ArrayList contenente tutti gli impiegati che corrispondono ai criteri di ricerca.
     * @throws CloneNotSupportedException Se il metodo clone non è supportato dalla classe {@code Impiegato}
     */
    public ArrayList<Impiegato> cercaimpiegati(String nome, String cognome, String sesso, Ruolo ruolo) {
        ArrayList<Impiegato> risultati = new ArrayList<>();

        // Flags per verificare se almeno un parametro è stato fornito
        boolean[] params = new boolean[4];
        params[0] = nome != null && !nome.isEmpty();
        params[1] = cognome != null && !cognome.isEmpty();
        params[2] = sesso != null  && !sesso.isEmpty();
        params[3] = ruolo != null;

        // Tutti i parametri sono nulli
        if( !params[0] && !params[1] && !params[2] && !params[3] ) {return null;}

        for (Impiegato impiegato : listaImpiegati) {

            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la ricerca
                if (!Objects.equals(impiegato.getNome(), nome)) { // Il criterio non è rispettato
                    continue; // L'oggetto Impiegato non viene aggiunto
                }
            }
            if (params[1]) {
                if (!Objects.equals(impiegato.getCognome(), cognome)) {
                    continue;
                }
            }
            if (params[2]) {
                if (!Objects.equals(impiegato.getSesso(), sesso)) {
                    continue;
                }
            }
            if (params[3]) {
                if (!impiegato.getRuolo().equals(ruolo)) {
                    continue;
                }
            }

            try {
                risultati.add(impiegato.clone());
            } catch (CloneNotSupportedException e) { // Non succede perchè impiegato supporta clone
                e.printStackTrace();
            }
        }

        return risultati;
    }

    /**
     * Metodo usato per controllare la validità dei campi di un impiegato.
     * @param impiegato impiegato da controllare.
     * @throws InvalidInputException se un campo presenta un valore errato.
     */
    public static void checkImpiegato(Impiegato impiegato) throws InvalidInputException {
        Pattern cfPattern = Pattern.compile("^[A-Z0-9]{16}$"),
                numletterPattern = Pattern.compile("^[A-Za-z0-9]*$"),
                namePattern = Pattern.compile("^[A-Za-z\\s]*$"),
                telPattern = Pattern.compile("^[0-9]{0,15}$"),
                emailPattern = Pattern.compile("^[A-Za-z]*\\.[A-Za-z]*[0-9]*@HotelColossus\\.it$");

        String[] listaRuolo = {Ruolo.FrontDesk.toString(), Ruolo.Manager.toString(), Ruolo.Governante.toString()},
            listaSesso = {"Maschio", "Femmina", "Altro"},
            listaDocumenti = {"Patente", "CID", "Passaporto"};

        LocalDate assunzione = impiegato.getDataAssunzione(),
            rilascioDocumento = impiegato.getDataRilascio(),
            scadenzaDocumento = impiegato.getDataScadenza();

        // Condizioni che possono lanciare un errore
        // 1. Codice Fiscale
        if (!cfPattern.matcher(impiegato.getCodiceFiscale()).matches())
            throw new InvalidInputException("[Codice Fiscale] errato");

        // 2. Stipendio
        if (impiegato.getStipendio() <= 0)
            throw new InvalidInputException("[Stipendio] errato");

        // 3. Nome
        if (!namePattern.matcher(impiegato.getNome()).matches())
            throw new InvalidInputException("[Nome] errato");

        // 4. Cognome
        if (!namePattern.matcher(impiegato.getCognome()).matches())
            throw new InvalidInputException("[Cognome] errato");

        // 5. CAP
        if (impiegato.getCAP() < 10000 || impiegato.getCAP() > 99999)
            throw new InvalidInputException("[CAP] errato");

        // 6. Data Assunzione
        if (assunzione.isAfter(LocalDate.now()))
            throw new InvalidInputException("[Data Assunzione] errato");

        // 7. Lunghezza Telefono e 8. Formato Telefono
        if (impiegato.getTelefono().length() > 15 || !telPattern.matcher(impiegato.getTelefono()).matches())
            throw new InvalidInputException("[Telefono] errato");

        // 9. Cittadinanza
        if (!namePattern.matcher(impiegato.getCittadinanza()).matches())
            throw new InvalidInputException("[Cittadinanza] errato");

        // 10. Email Aziendale
        if (!emailPattern.matcher(impiegato.getEmailAziendale()).matches())
            throw new InvalidInputException("[Email Aziendale] errato");

        // 11. Ruolo
        if (!Arrays.asList(listaRuolo).contains(impiegato.getRuolo().toString().trim()))
            throw new InvalidInputException("[Ruolo] errato");

        // 12. Sesso
        if (!Arrays.asList(listaSesso).contains(impiegato.getSesso().trim()))
            throw new InvalidInputException("[Sesso] errato");

        // 13. Data Rilascio Documento
        if (rilascioDocumento.isAfter(LocalDate.now()))
            throw new InvalidInputException("[Data Rilascio Documento] errato");

        // 14. Tipo Documento
        if (!Arrays.asList(listaDocumenti).contains(impiegato.getTipoDocumento().trim()))
            throw new InvalidInputException("[Tipo Documento] errato");

        // 15. Via
        if (!namePattern.matcher(impiegato.getVia()).matches())
            throw new InvalidInputException("[Via] errato");

        // 16. Provincia
        if (!namePattern.matcher(impiegato.getProvincia()).matches())
            throw new InvalidInputException("[Provincia] errato");

        // 17. Comune
        if (!namePattern.matcher(impiegato.getComune()).matches())
            throw new InvalidInputException("[Comune] errato");

        // 18. Numero Civico
        if (impiegato.getNumeroCivico() <= 0)
            throw new InvalidInputException("[Numero Civico] errato");

        // 19. Numero Documento
        if (!numletterPattern.matcher(impiegato.getNumeroDocumento()).matches())
            throw new InvalidInputException("[Numero Documento] errato");

        // 20, 21, 22. Validazione Data Scadenza Documento
        if (scadenzaDocumento.isBefore(LocalDate.now()) ||
                scadenzaDocumento.isBefore(rilascioDocumento) ||
                scadenzaDocumento.isEqual(rilascioDocumento)) {
            throw new InvalidInputException("[Data Scadenza Documento] errato");
        }

    }
    
    // Metodi della classe Object

    @Override
    public String toString() {
        return "CatalogoImpiegati{" +
                "listaImpiegati=" + listaImpiegati +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CatalogoImpiegati that = (CatalogoImpiegati) o;
        return Objects.equals(listaImpiegati, that.listaImpiegati);
    }
}
