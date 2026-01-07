package it.unisa.Server.BackOffice;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.interfacce.ManagerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        // da implementare
    }

    @Override
    public List<Double> calcolaContoHotel() {
        return List.of();
    }
}
