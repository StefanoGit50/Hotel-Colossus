package it.unisa.Server.BackOffice;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.interfacce.ManagerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManagerImpl extends UnicastRemoteObject implements ManagerInterface {

    protected ManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public void aggiungiImpiegato(Impiegato E) {
        //TODO: chiamata al DAO per gli impiegati
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

            for(Impiegato i: impiegati) {
                if (nome != null && nome.equals(i.getNome())) {
                    if (cognome != null && cognome.equals(i.getCognome())) {
                        if (ruolo != null && ruolo == i.getRuolo()) {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        } else if (ruolo == null) {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                    } else if (cognome == null) {
                        if (ruolo != null && ruolo == i.getRuolo()) {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        } else if (ruolo == null) {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        }
                    }
                } else if (nome == null) {
                    if (cognome != null && cognome.equals(i.getCognome())) {
                        if (ruolo != null && ruolo == i.getRuolo()) {
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                        } else if (ruolo == null)
                            if (sesso != null && sesso.equals(i.getSesso()))
                                impiegati2.add(i);
                            else if (sesso == null)
                                impiegati2.add(i);
                    }
                } else if (cognome == null) {
                    if (ruolo != null && ruolo == i.getRuolo()) {
                        if (sesso != null && sesso.equals(i.getSesso()))
                            impiegati2.add(i);
                        else if (sesso == null)
                            impiegati2.add(i);
                    } else if (ruolo == null) {
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
    public boolean eliminaImpiegato(Impiegato E) {
        //TODO: chiamata al DAO
        return false;
    }

    @Override
    public String generatePassword() {
        return "";
    }

    @Override
    public void modificaDatiImpiegato(Impiegato E) {
        //TODO: chiamata al DAO verrà passato l'impiegato già modificato
    }

    @Override
    public List<Double> calcolaContoHotel() {
        return List.of();
    }
}
