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
                Integer integer = (Integer) oggetto;
                Connection connection = ConnectionStorage.getConnection();
                try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Camera WHERE NumeroCamera = ?")){
                 preparedStatement.setInt(1,integer);
                ResultSet resultSet = preparedStatement.executeQuery();
                    Integer numeroCamera = null,numeroMaxOcc = null,piano = null;
                    String noteCamera = null, TipologiaCamera = null;
                    Stato stato = null;
                    Double prezzo = null;
                    if(resultSet.next()){
                    numeroCamera = (Integer) resultSet.getObject(1);
                    numeroMaxOcc = (Integer) resultSet.getObject(2);
                    noteCamera = (String) resultSet.getObject(3);
                    String c = resultSet.getString(4);
                    stato =  Stato.valueOf(c);
                    prezzo = (Double) resultSet.getObject(5);
                }

                return new Camera(numeroCamera , stato , numeroMaxOcc , prezzo , noteCamera);
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
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CAMERA VALUES (?,?,?,?,?)")){
            preparedStatement.setInt(1,o.getNumeroCamera());
            preparedStatement.setInt(2,o.getCapacità());
            preparedStatement.setString(3,o.getNoteCamera());
            preparedStatement.setString(4,o.getStatoCamera().name());
            preparedStatement.setDouble(5,o.getPrezzoCamera());
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
               Integer numeroMaxOcc = (Integer) resultSet.getObject(2);
                String noteCamera = (String) resultSet.getObject(3);
                String c = resultSet.getString(4);
               Stato stato =  Stato.valueOf(c);
               Double prezzo = (Double) resultSet.getObject(5);
               cameras.add(new Camera(numeroCamera,stato, numeroMaxOcc ,prezzo,noteCamera));
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
