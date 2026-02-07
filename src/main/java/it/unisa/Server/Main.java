package it.unisa.Server;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Impiegato;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoImpiegati;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.Storage.DAO.PrenotazioneDAO;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main() throws RemoteException, SQLException, IllegalAccess {
        System.out.println("QUI IL MAIN ");
        FrontDesk frontDesk = new FrontDesk();
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente c =clienteDAO.doRetriveByKey("BNCLCU85C03G273Z");
        c.setBlacklisted(false);

        frontDesk.unBanCliente(c);

        c= clienteDAO.doRetriveByKey("BNCLCU85C03G273Z");
        System.out.println(c);
    }
}

