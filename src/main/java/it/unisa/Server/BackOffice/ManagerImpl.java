package it.unisa.Server.BackOffice;

import it.unisa.Common.*;
import it.unisa.Server.Autentication.TokenGenerator;
import it.unisa.Server.command.CatalogoImpiegatiCommands.AddImpiegatoCommand;
import it.unisa.Server.command.CatalogoImpiegatiCommands.RemoveImpiegatoCommand;
import it.unisa.Server.command.CatalogoImpiegatiCommands.UpdateImpiegatoCommand;
import it.unisa.Server.command.Invoker;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Server.persistent.obj.catalogues.CatalogueUtils;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.interfacce.ManagerInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class ManagerImpl extends UnicastRemoteObject implements ManagerInterface
{
    // Costruttore
    protected ManagerImpl() throws RemoteException {
        super();
    }

    // RMI
    static Logger logger = Logger.getLogger("global");
    private static final int RMI_PORT = 1099;

    static void main(String[] args) {

        try {
            logger.info("Avvio RMI Registry sulla porta " + RMI_PORT + "...");

            // Recupera gli impiegati e aggiungili al catalogo
            /*try {
                ImpiegatoDAO dao = new ImpiegatoDAO();
                CatalogoImpiegati.setListaImpiegati((ArrayList<Impiegato>) dao.doRetriveAll(""));
            } catch (SQLException ex) {
                logger.info("Recupero impiegati dal DB fallito!");
                throw new RemoteException("Try creating 'Manager' object again!");
            }*/

            try {
                // Prova a creare un nuovo registry
                LocateRegistry.createRegistry(RMI_PORT);
                logger.info("✓ RMI Registry creato con successo!");
            } catch (RemoteException e) {
                // Se esiste già, ottieni il riferimento
                LocateRegistry.getRegistry(RMI_PORT);
                logger.info("✓ Connesso a RMI Registry esistente");
            }

            logger.info("Genero il gestore degli impiegati...");
            ManagerImpl man = new ManagerImpl();
            logger.info("✓ Gestore impiegati creato");

            logger.info("Effettuo il rebind di gestione impiegati...");
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/GestioneImpiegati", man);
            logger.info("✓ Gestore impiegati registrato con successo!");
            logger.info("✓ Servizio 'GestioneImpiegati' pronto");
            logger.info("Server in attesa di connessioni...");

            // Mantieni il server in esecuzione
            Thread.currentThread().join();
        }
        catch(Exception e)
        {
            logger.severe("Errore durante l'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // COMANDI

    // Catalogo impiegati
    private static CatalogoImpiegati catalogoImpiegati = new CatalogoImpiegati();

    // Invoker --> mantiene l'ordine delle chiamate ai comandi
    private Invoker invoker = new Invoker();

    @Override
    public String generatePassword(){
        TokenGenerator token = new TokenGenerator(30);
        return token.getToken();
    }

    @Override
    public Map<String, Double> calcolaContoHotel() throws RemoteException {
        return Map.of();
    }

    @Override
    public List<Impiegato> filtroImpiegati(String nome, String sesso, Ruolo ruolo, String orderBy) throws RemoteException {
        ImpiegatoDAO impDao = new ImpiegatoDAO();
        List<Impiegato> impiegati = null;
        try{
            impiegati = (List<Impiegato>) impDao.doFilter(nome, sesso, ruolo, "Nome ASC");
        } catch (Exception e){
            e.printStackTrace();
        }
        return impiegati;
    }

    //  COMANDI IMPIEGATO
    @Override
    public void addImpiegato(Impiegato i) throws RemoteException {
        CatalogueUtils.checkNull(i);            // Lancia InvalidInputException
        CatalogoImpiegati.checkImpiegato(i);    // Lancia InvalidInputException
        AddImpiegatoCommand command = new AddImpiegatoCommand(i);
        invoker.executeCommand(command);
    }

    @Override
    public void removeImpiegato(Impiegato i) throws RemoteException {
        RemoveImpiegatoCommand command = new RemoveImpiegatoCommand(i);
        invoker.executeCommand(command);

    }

    @Override
    public void updateImpiegato(Impiegato i) throws RemoteException {
        UpdateImpiegatoCommand command = new UpdateImpiegatoCommand(i);
        invoker.executeCommand(command);
    }

    /**
     * @param Cf dell'impiegato.
     * @return l'oggetto {@code Impiegato} il cui Cf corrisponde a quello passato come parametro, {@code null} altrimenti.
     * @throws RemoteException .
     */
    @Override
    public Impiegato getImpiegatoByCF(String Cf) throws RemoteException {
        ImpiegatoDAO dao = new ImpiegatoDAO();
        Impiegato impiegato = null;
        try {
            impiegato =  dao.doRetriveByKey(Cf);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return impiegato;
    }

    // Comando undo
    public void undoCommand() throws RemoteException {
        invoker.undoCommand();
    }

    // Comando redo
    public void redoCommand() throws RemoteException {
        invoker.redo();
    }

    /**
     * @return nuova pssword temporanea
     */
   /* @Override
    public String generatePassword() throws RemoteException
    {
        // definiamo i set di caratteri
        int length = 10;
        final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL = "!@#$%^&*()-_=+[]{}<>?|";
        final String ALL_CHARS = ALPHA_UPPER + ALPHA_LOWER + DIGITS + SPECIAL;

        final SecureRandom random = new SecureRandom();

        // Assicuriamoci di avere almeno un carattere per ogni categoria
        StringBuilder password = new StringBuilder();
        password.append(ALPHA_UPPER.charAt(random.nextInt(ALPHA_UPPER.length())));
        password.append(ALPHA_LOWER.charAt(random.nextInt(ALPHA_LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Riempiamo il resto della lunghezza con caratteri casuali totali
        for (int i = 4; i < length; i++)
        {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Mescoliamo i caratteri per non avere sempre l'ordine (Maiusc, Minusc, Numero, Speciale) all'inizio
        String input = password.toString();

        List<Character> characters = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        Collections.shuffle(characters);

        StringBuilder result = new StringBuilder();

        for (char c : characters)
            result.append(c);

        return result.toString();
    }


    @Override
    public Map<String, Double> calcolaContoHotel() throws RemoteException {
        ArrayList<Servizio> listaServizi = new ArrayList<Servizio>();
        listaServizi.add(new Servizio("Spa", 325));
        ArrayList<Camera> c = new ArrayList<Camera>();
        c.add(new Camera(112, Stato.Occupata, 2, 50.5, "o babba", ""));
        ArrayList<Cliente> clist = new ArrayList<>();
        Cliente cliente = new Cliente("Luca",
                "Marole",
                "Napoli",
                "NA",
                "via barbone",
                2,
                9076,
                "3233452",
                "m",
                LocalDate.of(2022, 1, 6),
                "SDFGANNSOLF", "Libero@asfnai", "Italiana", new Camera());
        clist.add(cliente);

<<<<<<< HEAD
=======
        Prenotazione p1 = new Prenotazione(
                java.time.LocalDate.now(),                    // dataCreazionePrenotazione
                java.time.LocalDate.of(2025, 8, 15),          // dataInizioPrenotazione
                java.time.LocalDate.of(2025, 8, 22),          // dataFinePrenotazione
                null,                                         // dataEmissioneRicevuta (null se non emessa)
                new Trattamento("All Inclusive", 100.0),      // trattamento (Creato al volo)
                700.00,                                       // prezzoAcquistoTrattamento
                "Passaporto",                                 // tipoDocumento
                java.time.LocalDate.of(2018, 5, 20),          // dataRilascio
                java.time.LocalDate.of(2028, 5, 19),          // dataScadenza
                "Mario Rossi",                                // intestatario
                "Camera silenziosa, piano alto",              // noteAggiuntive
                listaServizi,
                clist,
                "YA1234567",                                  // numeroDocumento
                "Bancomat",                                   // metodoPagamento
                "Italiana"                                    // cittadinanza
        );
        p1.setStatoPrenotazione(false);
>>>>>>> cd6c0aa2842bc9d47936da18eb4183ba5ae110d0

        Prenotazione p1 = new Prenotazione();
/*
        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();

        List<ContoEconomicoComposite> ComponentList = new ArrayList<ContoEconomicoComposite>();

        ContoEconomicoComposite prenotazioni = new ContoEconomicoComposite("PRENOTAZIONI");


        ArrayList<Camera> listaCamere = new ArrayList<>();


        //int i = 0;
        //for (Prenotazione p : CatalogoPrenotazioni.getListaPrenotazioni()) {
            /*
            if (p.getStatoPrenotazione() == false) {
                ContoEconomicoComposite prenotazioneComposite = new ContoEconomicoComposite("PRENOTAZIONE" + p.getIDPrenotazione() + " " + p.getIntestatario());

                // creazione composite CAMERE
                if (!p.getListaClienti().isEmpty() ) {
                    ContoEconomicoComposite camere = creaNodoeFoglie("CAMERE", p.getListaCamere(), room -> "CAMERA" + room.getNumeroCamera(),
                            room -> room.getPrezzoCamera(), room -> TipoVoce.CAMERA);
                    prenotazioneComposite.addChild(camere);
                }
                //creazione composite SERVIZI
                if (!p.getListaServizi().isEmpty() && p.getListaServizi() != null) {
                    ContoEconomicoComposite servizi = creaNodoeFoglie("SERVIZI", p.getListaServizi(), s -> s.getNome(),
                            s -> s.getPrezzo(), s -> TipoVoce.SERVIZIO);
                    prenotazioneComposite.addChild(servizi);
                }
                if (p.getTrattamento() != null) {
                    prenotazioneComposite.addChild(new ContoEconomicoLeaf(
                            p.getTrattamento().getNome(),
                            p.getTrattamento().getPrezzo(),
                            TipoVoce.TRATTAMENTO
                    ));
                }
                    i++;
                // aggiunta della prenotazione al nodo prenotazioni
                prenotazioni.addChild(prenotazioneComposite);
            }
        }

// RICAVI
        ContoEconomicoComponentAbstract ricavi = new ContoEconomicoComposite("RICAVI");
        ricavi.addChild(prenotazioni);

// PASSIVITA
        //TODO da aggiornare
        /**
        List<Impiegato> listaImpiegati = new ArrayList<>();
        listaImpiegati.add(new Impiegato(
                "mrossi", "hashedPassword123", "Mario", "Rossi", "M", "Carta d'identità", "AB13334", 83734, "ViaRoma",
                "NA", "Napoli", 10, "RSSMRA80A01H501X", "3331234567", Ruolo.FrontDesk, 4000,
                LocalDate.of(2022, 6, 1),            // dataAssunzione
                LocalDate.of(2022, 6, 1),            // dataRilascio documento
                "mario.rossi@hotel.it",              // emailAziendale
                "Italiana",                           // cittadinanza
                LocalDate.of(2021, 3, 15)            // dataScadenza documento
        ));*/
    /*
        listaImpiegati.add(new Impiegato(
                "lbianchi",
                "hashedPassword456",
                "Laura",
                "Bianchi",
                "F",
                "Passaporto",
                "CD9876543",
                20100,
                "Via Milano",
                "MI",
                "Milano",
                25,
                "BNCLRA90B55F205Y",
                "3497654321",
                Ruolo.FrontDesk,
                2800.75,
                LocalDate.of(2023, 3, 15),          // dataAssunzione
                LocalDate.of(2023, 3, 15),          // dataRilascio documento
                "laura.bianchi@hotel.it",           // emailAziendale
                "Italiana",                          // cittadinanza
                LocalDate.of(2033, 3, 15)           // dataScadenza documento
        ));
     */


// creo il composite stipendi
        /* ContoEconomicoComposite stipendi = creaNodoeFoglie(
                "Stipendi Personale",
                listaImpiegati,
                Impiegato::getNome,  //uso di method reference syntax
                Impiegato::getStipendio,
                i -> TipoVoce.STIPENDI
<<<<<<< HEAD
        );*/
/*
=======
        );

>>>>>>> cd6c0aa2842bc9d47936da18eb4183ba5ae110d0
        ContoEconomicoLeaf manutenzione = new ContoEconomicoLeaf("Manutenzione Camere", 540, TipoVoce.ALTRO);

        ContoEconomicoComponentAbstract passivita = new ContoEconomicoComposite("PASSIVITA");
        //passivita.addChild(stipendi);
        passivita.addChild(manutenzione);

// CONTO ECONOMICO COMPLETO
        ContoEconomicoComponentAbstract contoEconomico = new ContoEconomicoComposite("CONTO ECONOMICO");
        contoEconomico.addChild(ricavi);
        contoEconomico.addChild(passivita);
        contoEconomico.stampaAlbero("", true);

        Map<String, Double> conto = new HashMap<>();

        conto.put(PRENOTAZIONE_KEY, prenotazioni.getImportoTotale());
        conto.put(CAMERA_KEY, prenotazioni.getTotalePerTipo(TipoVoce.CAMERA));
        conto.put(SERVIZIO_KEY, prenotazioni.getTotalePerTipo(TipoVoce.SERVIZIO));
        conto.put(TRATTAMENTO_KEY, prenotazioni.getTotalePerTipo(TipoVoce.TRATTAMENTO));
        conto.put(PASSIVITA_KEY, passivita.getImportoTotale());
        conto.put(RICAVI_KEY, ricavi.getImportoTotale());
        conto.put(CONTOECONOMICO_KEY, contoEconomico.getImportoTotale());

        // -------------------
        // RIEPILOGO CONTABILE
        // -------------------
        /*
        System.out.println("\n===== RIEPILOGO CONTABILE =====");
        System.out.println("Totale Prenotazioni = " + totalePrenotazioni);
        System.out.println("Totale Camere = " + totaleCamere);
        System.out.println("Totale Servizi = " + totaleServizi);
        System.out.println("Totale Trattamenti = " + totaleTrattamenti);
        System.out.println("Totale Passività = " + totalePassivita);
        System.out.println("Totale Ricavi = " + totaleRicavi);
        System.out.println("Totale Conto Economico = " + totaleVeroContoEconomico);


//        return conto;
        return null;
    }

    private static <T> ContoEconomicoComposite creaNodoeFoglie(String nomeComposite,
                                                               List<T> sottolista,
                                                               Function<T,String> nomeVoce,
                                                               Function<T,Double> importo,
                                                               Function<T,TipoVoce> tipo)
    {
        ContoEconomicoComposite contoEconomico = new ContoEconomicoComposite(nomeComposite);

        if(sottolista!=null && !sottolista.isEmpty() ){
            sottolista.forEach(e-> contoEconomico.
                    addChild(new ContoEconomicoLeaf(nomeVoce.apply(e), importo.apply(e), tipo.apply(e))));
        }
        return contoEconomico;
    }

    public static final String PRENOTAZIONE_KEY = "totalePrenotazioni";
    public static final String CAMERA_KEY = "totaleCamere";
    public static final String SERVIZIO_KEY = "totaleServizi";
    public static final String TRATTAMENTO_KEY = "totaleTrattamenti";
    public static final String PASSIVITA_KEY = "totalePassivita";
    public static final String RICAVI_KEY = "totaleRicavi";
    public static final String CONTOECONOMICO_KEY = "totaleContoEconomico";*/


}

