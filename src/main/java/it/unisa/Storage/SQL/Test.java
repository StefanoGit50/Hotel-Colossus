package it.unisa.Storage.SQL;

import it.unisa.Common.Prenotazione;
import it.unisa.Common.Servizio;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class Test {

    static void main() throws SQLException, ClassNotFoundException {
        PrenotazioneDAO dao = new PrenotazioneDAO();
        List<Prenotazione> p = (List<Prenotazione>) dao.doRetriveAll("idPrenotazione DESC");
        p.forEach(System.out::println);
    }
}