package it.unisa.Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class    ConnectionStorage{
    private static List<Connection> freeDbConnections;
    private static final Logger log = Logger.getLogger(ConnectionStorage.class.getName());

    static {
        freeDbConnections = new LinkedList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.warning("DB driver not found:"+ e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown(); //Quando l'applicazione si spegne, il codice viene eseguito
        }));
    }


    /**
     * Crea connessione al db
     *
     * @post result != null
     */
    private static synchronized Connection createDBConnection() throws SQLException {
        Connection newConnection;
        String ip = "localhost";
        String port = "3306";
        String db = "Hotelcolossus";
        String username = "root";
        String password = "_MySqlServer2024";  //_MySqlServer2024 E' mia --Giovanni
                                       //1uno2due
        newConnection = DriverManager.getConnection("jdbc:mysql://"+ ip+":"+ port+"/"+db+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        return newConnection;
    }


    /**
     * Restituisce il valore di connection.
     *
     * @post result != null && result.isClosed() == false
     * @return connection
     */
    public static synchronized Connection getConnection(){
        Connection connection = null;

        if (!freeDbConnections.isEmpty()) {
            connection =  freeDbConnections.getFirst(); // restituisce la prima connessione disponibile
            freeDbConnections.removeFirst(); // fa in modo che il prossimo thread non possa usare la stessa connessione del thread attuale
                try {
                    if (connection.isClosed()|| !connection.isValid(4)) {
                        connection.close();
                        return getConnection(); // richiama se stesso fin quando non trova una connessione valida
                    }
                } catch (SQLException e) {
                    try{
                        connection.close();
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                    return getConnection(); // richiama se stesso fin quando non trova una connessione valida
                }

        } else {
            try{
                connection = createDBConnection();
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
        return connection;
    }


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @param connection
     * @pre connection != null
     * @post freeDbConnections.contains(connection) || connection.isClosed() == true
     * @throws SQLException
     */
    public static synchronized void releaseConnection(Connection connection) throws SQLException {
        if(connection!=null && freeDbConnections.size()<60){
            freeDbConnections.add(connection);
        }else {
            try{
                connection.close();
                log.info("🗑️ Pool pieno, connessione chiusa");
            }catch (SQLException e){
                log.warning("⚠️ Errore chiusura connessione: " + e.getMessage());
            }
        }
    }


    /**
     * Esegue un'operazione specifica del metodo.
     *
     * @post freeDbConnections->isEmpty() && freeDbConnections.stream().allMatch(c | c.isClosed() == true)
     */
    public static synchronized void shutdown() {
        log.info("Chiusura connessioni...");
        for (Connection conn : freeDbConnections) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();  //logica per chiudere forzatamente tutte le connessioni in freeDbConnections
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        freeDbConnections.clear();
        log.info("✓ Connessioni chiuse");
    }
}
