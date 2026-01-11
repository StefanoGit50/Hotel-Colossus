package it.unisa.Server.BackOffice;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.interfacce.ManagerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ManagerImpl extends UnicastRemoteObject implements ManagerInterface
{

    protected ManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public void aggiungiImpiegato(Impiegato E)
    {
        try {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
            impiegatoDAO.doSave(E);
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public List<Impiegato> ottieniImpiegatiTutti()
    {
        try {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
            return (List<Impiegato>)impiegatoDAO.doRetriveAll("null");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Impiegato ottieniImpiegatoDaId(String codiceFiscale)
    {
        try {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
            return impiegatoDAO.doRetriveByKey(codiceFiscale);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Impiegato> ottieniImpiegatiDaFiltro(String nome, String cognome, Ruolo ruolo, String sesso)
    {
        try
        {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
            List<Impiegato> impiegati = (List<Impiegato>)impiegatoDAO.doRetriveAll("null");

            List<Impiegato> impiegati2 = new ArrayList<>();

            for(Impiegato i: impiegati)
            {
                if (nome != null && nome.equals(i.getNome()))
                {
                    if (cognome != null && cognome.equals(i.getCognome()))
                    {
                        if (ruolo != null && ruolo == i.getRuolo())
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                        else if (ruolo == null)
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                    }
                    else if (cognome == null)
                    {
                        if (ruolo != null && ruolo == i.getRuolo())
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                        else if (ruolo == null)
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                    }
                }
                else if (nome == null)
                {
                    if (cognome != null && cognome.equals(i.getCognome()))
                    {
                        if (ruolo != null && ruolo == i.getRuolo())
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                        else if (ruolo == null)
                        {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                    }
                }
                else if (cognome == null)
                {
                    if (ruolo != null && ruolo == i.getRuolo())
                    {
                        if (sesso != null && sesso.equals(i.getSesso()))
                            impiegati2.add(i);
                        else if (sesso == null)
                            impiegati2.add(i);
                    }
                    else if (ruolo == null)
                    {
                        if (sesso != null && sesso.equals(i.getSesso()))
                            impiegati2.add(i);
                        else if (sesso == null)
                            impiegati2.add(i);
                    }
                }
            }

            return impiegati;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean eliminaImpiegato(Impiegato E)
    {
        try
        {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();

            impiegatoDAO.doDelete(E);

            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String generatePassword()
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

    //
    @Override
    public void modificaDatiImpiegato(Impiegato E)
    {
        try
        {
            ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();

            impiegatoDAO.doUpdate(E);
        }
        catch (SQLException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Double> calcolaContoHotel() {
        ArrayList<Servizio> listaServizi = new ArrayList<Servizio>();
        listaServizi.add(new Servizio("Spa", 325));
        ArrayList<Camera> c = new ArrayList<Camera>();
        c.add(new Camera(112, Stato.Occupata, 2, 50.5, "o babba"));
        ArrayList<Cliente> clist = new ArrayList<>();
        Cliente cliente = new Cliente("Luca",
                "Marole",
                "Italiana",
                "Napoli",
                "NA",
                "via barbone",
                2,
                9076,
                "3233452",
                "m",
                LocalDate.of(2022, 1, 6),
                "SDFGANNSOLF", "Libero@asfnai",
                "Carta");
        clist.add(cliente);

        Prenotazione p1 = new Prenotazione(1,
                LocalDate.now(),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 15),
                new Trattamento("MEZZA PENSIONE", 24),
                "PASSAPORTO",
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2030, 5, 20),
                cliente.getNome() + " " + cliente.getCognome(),
                "renato ti massaggiA",
                c, listaServizi, clist, 34569);
        p1.setStatoPrenotazione(false);

        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();

        List<ContoEconomicoComposite> ComponentList = new ArrayList<ContoEconomicoComposite>();

        ContoEconomicoComposite prenotazioni = new ContoEconomicoComposite("PRENOTAZIONI");

        CatalogoPrenotazioni.getListaPrenotazioni().add(p1);
        //mostra solo le prenotazioni completate
        for (Prenotazione p : CatalogoPrenotazioni.getListaPrenotazioni()) {
            if (p.getStatoPrenotazione() == false) {
                ContoEconomicoComposite prenotazioneComposite = new ContoEconomicoComposite("PRENOTAZIONE" + p.getIDPrenotazione() + " " + p.getIntestatario());

                // creazione composite CAMERE
                if (!p.getListaCamere().isEmpty() && p.getListaCamere() != null) {
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

                // aggiunta della prenotazione al nodo prenotazioni
                prenotazioni.addChild(prenotazioneComposite);
            }
        }

// RICAVI
        ContoEconomicoComponentAbstract ricavi = new ContoEconomicoComposite("RICAVI");
        ricavi.addChild(prenotazioni);

// PASSIVITA
        List<Impiegato> listaImpiegati = new ArrayList<>();
        listaImpiegati.add(new Impiegato(
                "mrossi", "hashedPassword123", "Mario", "Rossi", "M", "Carta d'identità", "AB13334", 83734, "ViaRoma",
                "NA", "Napoli", 10, "RSSMRA80A01H501X", "3331234567", Ruolo.FrontDesk, 4000,
                LocalDate.of(2022, 6, 1),            // dataAssunzione
                LocalDate.of(2022, 6, 1),            // dataRilascio documento
                "mario.rossi@hotel.it",              // emailAziendale
                "Italiana",                           // cittadinanza
                LocalDate.of(2021, 3, 15)            // dataScadenza documento
        ));

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

// creo il composite stipendi
        ContoEconomicoComposite stipendi = creaNodoeFoglie(
                "Stipendi Personale",
                listaImpiegati,
                Impiegato::getNome,  //uso di method reference syntax
                Impiegato::getStipendio,
                i -> TipoVoce.STIPENDI
        );

        ContoEconomicoLeaf manutenzione = new ContoEconomicoLeaf("Manutenzione Camere", 540, TipoVoce.ALTRO);

        ContoEconomicoComponentAbstract passivita = new ContoEconomicoComposite("PASSIVITA");
        passivita.addChild(stipendi);
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
        */

        return conto;
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
    public static final String CONTOECONOMICO_KEY = "totaleContoEconomico";
}
