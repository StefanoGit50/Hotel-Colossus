package it.unisa.Server;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Impiegato;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main() {
        CameraDAO cameraDAO = new CameraDAO();
        CatalogoCamere catalogoCamere = new CatalogoCamere();
        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();
        CatalogoClienti catalogoClienti = new CatalogoClienti();

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            Prenotazione p;
            p = prenotazioneDAO.doRetriveByKey(1);
            System.out.println(p);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}

