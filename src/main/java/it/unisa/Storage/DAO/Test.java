package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {
    static void main(){
       /*
        ImpiegatoDAO dao = new ImpiegatoDAO();
        Collection<Impiegato> l = dao.doFilter("Lorenzo", "M", Ruolo.Manager, "");
        System.out.println(l.isEmpty());
        */
        CameraDAO cameraDAO = new CameraDAO();
        ArrayList<Camera> cameras = new ArrayList<>();
        try{
            cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        System.out.println(cameras);
    }
}
