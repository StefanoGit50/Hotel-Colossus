package it.unisa.Storage.SQL;

import it.unisa.Common.Prenotazione;
import it.unisa.Common.Servizio;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Storage.DAO.PrenotazioneDAO;
import it.unisa.Storage.DAO.ServizioDAO;
import it.unisa.interfacce.FrontDeskInterface;

import java.rmi.Naming;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class Test {

    static void main() throws Exception {
//        FrontDeskInterface frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost:1099/GestionePrenotazioni");

//        Prenotazione p = frontDesk.getPrenotazioneById(1);
        PrenotazioneDAO dao = new PrenotazioneDAO();
        Prenotazione p = dao.doRetriveByKey(3);
        System.out.println(p);
//        frontDesk.addPrenotazione(p);
        System.out.println(p);
    }
}