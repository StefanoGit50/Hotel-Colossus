package it.unisa.Storage.DAO;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CameraDAO implements FrontDeskStorage<Camera>{
    @Override
    public void doDelete(Camera o) throws SQLException {
        if(o != null){
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Camera WHERE NumeroCamera = ?");
            preparedStatement.setInt(1,o.getNumeroCamera());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }else{
            throw new NullPointerException();
        }
    }

    @Override
    public Camera doRetriveByKey(Object oggetto) throws SQLException {
        if(oggetto instanceof Integer){
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Camera WHERE NumeroCamera = ?");
            ResultSet resultSet = preparedStatement.executeQuery();

           Integer numeroCamera = (Integer) resultSet.getObject(1);
           Integer numeroMaxOcc = (Integer) resultSet.getObject(2);
           String noteCamera = (String) resultSet.getObject(3);
           Stato stato = (Stato) resultSet.getObject(4);
           Integer piano = (Integer) resultSet.getObject(5);
           String TipologiaCamera = (String) resultSet.getObject(6);
           Double prezzo = (Double) resultSet.getObject(7);

            return new Camera(numeroCamera , TipologiaCamera , stato , numeroMaxOcc , prezzo , piano , noteCamera);
        }else{
            throw new SQLException();
        }
    }

    @Override
    public void doSave(Camera o) throws SQLException {

    }

    @Override
    public Collection<Camera> doRetriveAll(String order) throws SQLException {
        return List.of();
    }
}
