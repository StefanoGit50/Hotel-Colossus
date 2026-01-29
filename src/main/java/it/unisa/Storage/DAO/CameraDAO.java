package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import it.unisa.Storage.Interfacce.GovernanteStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class CameraDAO implements FrontDeskStorage<Camera>, GovernanteStorage<Camera>{

    public static final String TABLE_NAME = "Camera";
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void doSave(Camera o) throws SQLException {
        throw new NoSuchElementException("no");
    }

    @Override
    public synchronized void doDelete(Camera o) throws SQLException {
        if(o != null){
             connection = ConnectionStorage.getConnection();
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Camera WHERE NumeroCamera = ?");
                preparedStatement.setInt(1,o.getNumeroCamera());
                preparedStatement.executeUpdate();
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NoSuchElementException("elemento non trovato");
        }
    }

    @Override
    public synchronized Camera doRetriveByKey(Object oggetto) throws SQLException {
            if(oggetto instanceof Integer){
                Integer integer = (Integer) oggetto;
                connection = ConnectionStorage.getConnection();
                try{
                    preparedStatement = connection.prepareStatement("SELECT * FROM Camera WHERE NumeroCamera = ?");
                    preparedStatement.setInt(1,integer);
                    resultSet = preparedStatement.executeQuery();
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
    public synchronized void doSaveAll(List<Camera> listCamera) throws SQLException {
        StringBuilder insertSQL = new StringBuilder();
        String values = " (?, ?, ?, ?, ?) ";
        insertSQL.append("INSERT INTO " + CameraDAO.TABLE_NAME + " VALUES ");
        int numCamere = listCamera.size(); // numCamere * 5 = numCampi ?

        // Crea la query con i
        for(int i = 1; i <= numCamere; i++){
            insertSQL.append(values);
            if(i == numCamere){
                insertSQL.append(";");
            } else {
                insertSQL.append(",");
            }
        }

        System.out.println(insertSQL.toString());

        // Riempi la query
        connection = ConnectionStorage.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL.toString());
        for (int i = 0; i < numCamere; i++) {
            Camera c = listCamera.get(i);
            System.out.println(c.toString());
            preparedStatement.setInt(1 + 5*i, c.getNumeroCamera());
            preparedStatement.setInt(2 + 5*i, c.getCapacità());
            preparedStatement.setString(3 + 5*i, c.getNoteCamera());
            preparedStatement.setObject(4 + 5*i, c.getStatoCamera().name());
            preparedStatement.setDouble(5 + 5*i, c.getPrezzoCamera());
        }

        try{
            preparedStatement.executeUpdate();
        } finally {
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized Collection<Camera> doRetriveAll(String order) {
          connection = ConnectionStorage.getConnection();
          String sql = "SELECT * FROM Camera ORDER BY ? ";
          if (order != null) {
              if (order.equalsIgnoreCase("decrescente")) {
                  sql += "DESC";
              } else {
                  sql += "ASC";
              }
          } else {
              throw new RuntimeException();
          }

        ArrayList<Camera> cameras = new ArrayList<>();

        try {
            try{
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "NumeroCamera");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Integer numeroCamera = (Integer) resultSet.getObject(1);
                    Integer numeroMaxOcc = (Integer) resultSet.getObject(2);
                    String noteCamera = (String) resultSet.getObject(3);
                    String c = resultSet.getString(4);
                    Stato stato = Stato.valueOf(c);
                    Double prezzo = (Double) resultSet.getObject(5);
                    cameras.add(new Camera(numeroCamera, stato, numeroMaxOcc, prezzo, noteCamera));
                }
                resultSet.close();
            }catch(SQLException e){

            }
        }finally{
            if(connection != null){
                try {
                    ConnectionStorage.releaseConnection(connection);
                }catch (SQLException e){

                }
            }
        }
        return cameras;
    }


    /**
     * Aggiorna i dati di una camera esistente nel database.
     *
     * @param o La camera con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NoSuchElementException Se il parametro o è null quindi non trovato.
     * Precondizioni:
     *   o != null
     *   o.getNumeroCamera() deve corrispondere a una camera esistente nel database
     *   o.getStatoCamera() deve essere un valore valido dell'enum Stato
     *   o.getCapacità() deve essere maggiore di 0
     *   o.getPrezzoCamera() deve essere maggiore o uguale a 0
     * Postcondizioni:
     *   Il record della camera nel database viene aggiornato con i nuovi valori
     *   Il NumeroCamera (chiave primaria) rimane invariato
     */
    @Override
    public synchronized void doUpdate(Camera o) throws SQLException
    {
        if(o != null)
        {
            connection = ConnectionStorage.getConnection();
            try{
                preparedStatement = connection.prepareStatement(
                        "UPDATE Camera SET NumeroMaxOcc = ?, NoteCamera = ?, Stato = ?, " +
                                "Prezzo = ? WHERE NumeroCamera = ?");
                preparedStatement.setInt(1, o.getCapacità());
                preparedStatement.setString(2, o.getNoteCamera());
                preparedStatement.setString(3, o.getStatoCamera().name());
                preparedStatement.setDouble(4, o.getPrezzoCamera());
                preparedStatement.setInt(5, o.getNumeroCamera());
                preparedStatement.executeUpdate();
            }
            finally
            {
                if(connection != null)
                {
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }
        else
        {
            throw new NoSuchElementException("elemento non trovato");
        }
    }


    @Override
    public Collection<Camera> doRetriveByAttribute(String attribute, String value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * @param attribute;
     * @param value;
     * @return Collection<Camera>;
     * @throws SQLException;
     */
    @Override
    public synchronized Collection<Camera> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        ArrayList<Camera> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM "+ CameraDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try{
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setObject(1, value);
                 resultSet = preparedStatement.executeQuery();

                Camera camera;
                while (resultSet.next()) {
                    camera = new Camera();
                    camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
                    camera.setNoteCamera(resultSet.getString("NoteCamera"));
                    camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
                    camera.setPrezzoCamera(resultSet.getDouble("PrezzoCamera"));
                    camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
                    lista.add(camera);
                }
            }finally{
                if(connection != null){
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new RuntimeException("Attributo e/o valore non valido/i");
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessuna camera con " + attribute + " = " + value + "!");

        return lista;
    }

    /**
     * @param nome;
     * @param cognome;
     * @param nazionalita;
     * @param dataDiNascita;
     * @param orderBy;
     * @throws UnsupportedOperationException;
     */
    @Override
    public Collection<Camera> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blackListed, String orderBy)  throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
