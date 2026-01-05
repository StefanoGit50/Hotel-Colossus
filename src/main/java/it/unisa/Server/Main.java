package it.unisa.Server;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main(){
        CameraDAO cameraDAO = new CameraDAO();

        try{
            ArrayList<Camera> cameras = CatalogoCamere.getListaCamere();
            ArrayList<Camera> cameras1 = new ArrayList<>();
            cameras1 = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
            CatalogoCamere.setListaCamere(cameras1);
            System.out.println(cameras);
            System.out.println(CatalogoCamere.getListaCamere());
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }


        try{
            cameraDAO.doDelete(CatalogoCamere.getListaCamere().getFirst());
            ArrayList<Camera> cameras = new ArrayList<>();
            cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
            CatalogoCamere.setListaCamere(cameras);
            System.out.println(cameras);
            System.out.println(CatalogoCamere.getListaCamere());
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        try{
            ArrayList<Cliente> clientes = new ArrayList<>();
            clientes =(ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
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

    }
}
