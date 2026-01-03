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
import java.util.ArrayList;
import java.util.Collection;

public class CameraDAO implements FrontDeskStorage<Camera>{
    @Override
    public synchronized void doDelete(Camera o) throws SQLException {
        if(o != null){
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Camera WHERE NumeroCamera = ?")){
                preparedStatement.setInt(1,o.getNumeroCamera());
                preparedStatement.executeUpdate();
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NullPointerException();
        }
    }

    @Override
    public synchronized Camera doRetriveByKey(Object oggetto) throws SQLException {
            if(oggetto instanceof Integer){
                Connection connection = ConnectionStorage.getConnection();
             try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Camera WHERE NumeroCamera = ?")){
                ResultSet resultSet = preparedStatement.executeQuery();

                Integer numeroCamera = (Integer) resultSet.getObject(1);
                Integer numeroMaxOcc = (Integer) resultSet.getObject(2);
                String noteCamera = (String) resultSet.getObject(3);
                Stato stato = (Stato) resultSet.getObject(4);
                Integer piano = (Integer) resultSet.getObject(5);
                String TipologiaCamera = (String) resultSet.getObject(6);
                Double prezzo = (Double) resultSet.getObject(7);

                return new Camera(numeroCamera , TipologiaCamera , stato , numeroMaxOcc , prezzo , piano , noteCamera);
            }finally{
                 if(connection != null){
                     ConnectionStorage.releaseConnection(connection);
                 }
             }
        }else{
           throw new SQLException();
       }
    }

    @Override
    public synchronized void doSave(Camera o) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CAMERA VALUES (?,?,?,?,?,?,?)")){
            preparedStatement.setInt(1,o.getNumeroCamera());
            preparedStatement.setInt(2,o.getCapacità());
            preparedStatement.setString(3,o.getNoteCamera());
            preparedStatement.setString(4,o.getStatoCamera().name());
            preparedStatement.setInt(5,o.getPiano());
            preparedStatement.setString(6,o.getTipologia());
            preparedStatement.setDouble(7,o.getPrezzoCamera());
            preparedStatement.executeUpdate();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized Collection<Camera> doRetriveAll(String order) throws SQLException{
        Connection connection = ConnectionStorage.getConnection();
        String sql = "SELECT * FROM Camera ORDER BY ? ";
        if(order.equalsIgnoreCase("decrescente")){
            sql += "DESC";
        }else{
            sql += "ASC";
        }
        ArrayList<Camera> cameras = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,"NumeroCamera");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Integer numeroCamera = (Integer) resultSet.getObject(1);
                Integer numeroMacOcc = (Integer) resultSet.getObject(2);
                String noteCamera = (String) resultSet.getObject(3);
                Stato stato = (Stato) resultSet.getObject(4);
                Integer piano = (Integer) resultSet.getObject(5);
                String tipologiaCamera = (String) resultSet.getObject(6);
                Double prezzo = (Double) resultSet.getObject(7);
                cameras.add(new Camera(numeroCamera,tipologiaCamera,stato, numeroMacOcc ,prezzo,piano,noteCamera));
            }
            resultSet.close();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
        return cameras;
    }
}
