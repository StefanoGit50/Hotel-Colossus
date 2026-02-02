package it.unisa.Storage.SQL;

import it.unisa.Common.Prenotazione;
import it.unisa.Common.Servizio;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class Test {

    static void main() throws SQLException, ClassNotFoundException {
        PrenotazioneDAO dao = new PrenotazioneDAO();
        Prenotazione p = dao.doRetriveByKey(1);
        System.out.println(p);
    }
}