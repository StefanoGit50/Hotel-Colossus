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
            listaSesso = {"maschio", "femmina", "altro"},
            listaDocumenti = {"Patente", "CID", "Passaporto"};

        LocalDate assunzione = impiegato.getDataAssunzione(),
            rilascioDocumento = impiegato.getDataRilascio(),
            scadenzaDocumento = impiegato.getDataScadenza();



        // Lista di condizioni che possono lanciare un errore
        if (!cfPattern.matcher(impiegato.getCodiceFiscale()).matches() || // CF composto da caratteri diversi da numeri e lettere
                impiegato.getStipendio() <= 0 || // stipendio negativo o uguale a 0
                !namePattern.matcher(impiegato.getNome()).matches() || // nome composto da caratteri diversi da lettere e spazi
                !namePattern.matcher(impiegato.getCognome()).matches() || // cognome composto da caratteri diversi da lettere e spazi
                (impiegato.getCAP() < 10000 || impiegato.getCAP() > 99999) || // CAP deve essere di 5 cifre esatte
                assunzione.isAfter(LocalDate.now()) || // data di assunzione successiva alla data odierna
                impiegato.getTelefono().length() > 15 || // numero di telefono di più di 15 cifre
                !telPattern.matcher(impiegato.getTelefono()).matches() || // telefono composto da caratteri diversi da cifre
                !namePattern.matcher(impiegato.getCittadinanza()).matches() || // cittadinanza composta da caratteri diversi da lettere e spazi
                !emailPattern.matcher(impiegato.getEmailAziendale()).matches() || // email aziendale errata
                !Arrays.asList(listaRuolo).contains(impiegato.getRuolo().toString().toLowerCase().trim()) || // ruolo errato
                !Arrays.asList(listaSesso).contains(impiegato.getSesso().toLowerCase().trim()) || // sesso errato
                rilascioDocumento.isAfter(LocalDate.now()) || // data rilascio documento successiva alla data odierna
                !Arrays.asList(listaDocumenti).contains(impiegato.getTipoDocumento().toLowerCase().trim()) || // tipo documento errato
                !namePattern.matcher(impiegato.getVia()).matches() || // via composta da caratteri diversi da lettere e spazi
                !namePattern.matcher(impiegato.getProvincia()).matches() || // provincia composta da caratteri diversi da lettere e spazi
                !namePattern.matcher(impiegato.getComune()).matches() || // comune composto da caratteri diversi da lettere e spazi
                impiegato.getNumeroCivico() <= 0 || // numero civico negativo
                !numletterPattern.matcher(impiegato.getNumeroDocumento()).matches() || // nome composto da caratteri diversi da lettere e numeri
                scadenzaDocumento.isBefore(LocalDate.now()) || // data scadenza documento antecedente alla data odierna
                scadenzaDocumento.isBefore(rilascioDocumento) || // data scadenza documento antecedente alla data di rilascio del documento
                scadenzaDocumento.isEqual(rilascioDocumento) // data scadenza documento coincide con la data di rilascio del documento
        )
        {
            throw new InvalidInputException();
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
