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

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    /**
     * Cancella la camera dal db.
     *
     * @pre o != null
     */
    @Override
    public synchronized void doDelete(Camera o) throws SQLException {
        if(o != null){
             connection = ConnectionStorage.getConnection();
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM camera WHERE NumeroCamera = ?");
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


    /**
     * Ottiene  oggetto da chiave.
     *
     * @pre oggetto != null
     * @post result == null || (result != null && result.numeroCamera == oggetto)
     */
    @Override
    public synchronized Camera doRetriveByKey(Object oggetto) throws SQLException {
            if(oggetto instanceof Integer){
                Integer integer = (Integer) oggetto;
                connection = ConnectionStorage.getConnection();
                Camera camera = new Camera();
                try{
                    preparedStatement = connection.prepareStatement("SELECT * FROM camera WHERE NumeroCamera = ?");
                    preparedStatement.setInt(1,integer);
                    resultSet = preparedStatement.executeQuery();


                    if(resultSet.next()){
                        camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
                        camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
                        camera.setNoteCamera( resultSet.getString("NoteCamera"));
                        camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
                        camera.setPrezzoCamera( resultSet.getDouble("Prezzo"));
                        camera.setNomeCamera( resultSet.getString("NomeCamera"));
                    }

                    return camera;
                }finally{
                     if(connection != null){
                         ConnectionStorage.releaseConnection(connection);
                     }
             }

        }else{
           throw new SQLException();
       }
    }


    /**
     * Salva oggetto nel db.
     *
     * @pre o != null
     */
    @Override
    public synchronized void doSave(Camera o) throws SQLException {
        connection = ConnectionStorage.getConnection();
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO camera(NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo) VALUES (?,?,?,?,?,?)");
            preparedStatement.setInt(1,o.getNumeroCamera());
            preparedStatement.setString(2,o.getNomeCamera());
            preparedStatement.setInt(3,o.getCapacità());
            preparedStatement.setString(4,o.getNoteCamera());
            preparedStatement.setString(5,o.getStatoCamera().name());
            preparedStatement.setDouble(6,o.getPrezzoCamera());
            preparedStatement.executeUpdate();

        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }
    // Mock front desk, mock oggetti che chiamano il frontDesk e che vengono chiamati dal frontDesk
    // Oggetti che passati come parametri NON devono essere mock-ati


    /**
     * Salva tutti gli oggetti nel DB.
     *
     * @pre listCamera != null && listCamera.size() > 0
     */
    @Override
    public synchronized void doSaveAll(List<Camera> listCamera) throws SQLException{
        if(!listCamera.isEmpty()){
            StringBuilder insertSQL = new StringBuilder();
            String values = " (?,?,?,?,?,?) ";
            insertSQL.append("INSERT INTO camera VALUES ");
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

            // Riempi la query
            connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL.toString());
            for(int i = 0; i < numCamere; i++){
                Camera c = listCamera.get(i);
                preparedStatement.setInt(1 + 6*i, c.getNumeroCamera());
                preparedStatement.setString(1 + 6*i, c.getNomeCamera());
                preparedStatement.setInt(2 + 6*i, c.getCapacità());
                preparedStatement.setString(3 + 6*i, c.getNoteCamera());
                preparedStatement.setObject(4 + 6*i, c.getStatoCamera().name());
                preparedStatement.setDouble(5 + 6*i, c.getPrezzoCamera());
            }

            try{
                preparedStatement.executeUpdate();
            } finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NullPointerException("la lista è null oppure la dimensione è uguale a 0");
        }

    }


    /**
     * Ottiene tutte le camere dal DB.
     *
     * @pre order != null && order != ""
     * @post result != null
     */
    @Override
    public synchronized Collection<Camera> doRetriveAll(String order) throws SQLException{
        connection = ConnectionStorage.getConnection();
        String sql = "SELECT * FROM camera ORDER BY ? ";
            if(order != null){
                if(order.equalsIgnoreCase("decrescente")){
                    sql += "DESC";
                }else{
                    sql += "ASC";
                }
            }else{
                throw new RuntimeException();
            }

        ArrayList<Camera> cameras = new ArrayList<>();
        
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"NumeroCamera");
            resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                   Integer numeroCamera = (Integer) resultSet.getObject("NumeroCamera");
                   String nomeCamera = resultSet.getString("NomeCamera");
                   Integer numeroMaxOcc = (Integer) resultSet.getObject("NumeroMaxOcc");
                    String noteCamera = (String) resultSet.getObject("NoteCamera");
                    String c = resultSet.getString("Stato");
                   Stato stato =  Stato.valueOf(c);
                   Double prezzo = (Double) resultSet.getObject("Prezzo");
                   cameras.add(new Camera(numeroCamera,stato, numeroMaxOcc ,prezzo,noteCamera,nomeCamera));
                }
            resultSet.close();

        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
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
                        "UPDATE camera SET NumeroMaxOcc = ?, NoteCamera = ?, Stato = ?, " +
                                "Prezzo = ?,NomeCamera = ? WHERE NumeroCamera = ?");
                preparedStatement.setInt(1, o.getCapacità());
                preparedStatement.setString(2, o.getNoteCamera());
                preparedStatement.setString(3, o.getStatoCamera().name());
                preparedStatement.setDouble(4, o.getPrezzoCamera());
                preparedStatement.setString(5, o.getNomeCamera());
                preparedStatement.setInt(6,o.getNumeroCamera());
                preparedStatement.executeUpdate();
            }finally{
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }else{
            throw new NoSuchElementException("elemento non trovato");
        }
    }


    @Override
    public Collection<Camera> doRetriveByAttribute(String attribute, String value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     *
     *
     * Ottiene oggetto tramite attributo.
     *
     * @pre attribute != null && attribute != "" && value != null && value != ""
     * @post result != null
     *
     *
     * @param attribute;
     * @param value;
     * @return Collection<>;
     * @throws SQLException;
     */
    @Override
    public synchronized Collection<Camera> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        ArrayList<Camera> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM camera WHERE " + attribute + " = ?";
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
                    camera.setPrezzoCamera(resultSet.getDouble("Prezzo"));
                    camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
                    camera.setNomeCamera(resultSet.getString("NomeCamera"));
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
}


