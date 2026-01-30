package it.unisa.Server;

import com.mysql.cj.protocol.a.SqlDateValueEncoder;
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

import javax.xml.catalog.CatalogResolver;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    /*
    static void main(){
        CameraDAO cameraDAO = new CameraDAO();
        CatalogoCamere catalogoCamere = new CatalogoCamere();
        try{
            ArrayList<Camera> cameras = catalogoCamere.getListaCamere();
            ArrayList<Camera> cameras1 = new ArrayList<>();
            cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
            catalogoCamere.addCamere(cameras1);
            System.out.println(cameras);
            System.out.println(catalogoCamere.getListaCamere());
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }


        try{
            cameraDAO.doDelete(catalogoCamere.getListaCamere().getFirst());
            ArrayList<Camera> cameras = new ArrayList<>();
            cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
            catalogoCamere.addCamere(cameras);
            System.out.println(cameras);
            System.out.println(catalogoCamere.getListaCamere());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        try{
            ArrayList<Cliente> clientes =(ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
            CatalogoClienti.setListaClienti(clientes);
            System.out.println(clientes);
            System.out.println(CatalogoClienti.getListaClienti());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            clienteDAO.doDelete(CatalogoClienti.getListaClienti().getFirst());
            ArrayList<Cliente> clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("crescente");
            CatalogoClienti.setListaClienti(clientes);
            System.out.println(clientes);
            System.out.println(CatalogoClienti.getListaClienti());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
        try{
            ArrayList<Impiegato> impiegatoes = (ArrayList<Impiegato>) impiegatoDAO.doRetriveAll("decrescente");
            CatalogoImpiegati.setListaImpiegati(impiegatoes);
            System.out.println(impiegatoes);
            System.out.println(CatalogoImpiegati.getListaImpiegati());
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            impiegatoDAO.doDelete(CatalogoImpiegati.getListaImpiegati().getFirst());
            ArrayList<Impiegato> impiegatoes = (ArrayList<Impiegato>) impiegatoDAO.doRetriveAll("descrescente");
            CatalogoImpiegati.setListaImpiegati(impiegatoes);
            System.out.println(impiegatoes);
            System.out.println(CatalogoImpiegati.getListaImpiegati());
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        PrenotazioneDAO prenotazioneDAO = null;
        prenotazioneDAO = new PrenotazioneDAO();
        try{
            ArrayList<Prenotazione> prenotaziones = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveAll("IDPrenotazione");
            CatalogoPrenotazioni.addPrenotazioni(prenotaziones);
            System.out.println(prenotaziones);
            System.out.println(CatalogoPrenotazioni.getListaPrenotazioni());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
           prenotazioneDAO.doDelete(CatalogoPrenotazioni.getListaPrenotazioni().getFirst());
           ArrayList<Prenotazione> prenotaziones = (ArrayList<Prenotazione>) prenotazioneDAO.doRetriveAll("IDPrenotazione");
           CatalogoPrenotazioni.addPrenotazioni(prenotaziones);
           System.out.println(prenotaziones);
           System.out.println(CatalogoPrenotazioni.getListaPrenotazioni());
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }*/
}
