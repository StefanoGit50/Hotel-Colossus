package it.unisa.Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ConnectionStorage{
    private static List<Connection> freeDbConnections;
    static {
        freeDbConnections = new LinkedList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("DB driver not found:"+ e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }

    private static synchronized Connection createDBConnection() throws SQLException {
        Connection newConnection;
        String ip = "localhost";
        String port = "3306";
        String db = "hotelcolossus";
        String username = "root";
        String password = "";

        newConnection = DriverManager.getConnection("jdbc:mysql://"+ ip+":"+ port+"/"+db+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        return newConnection;
    }


    public static synchronized Connection getConnection() throws SQLException {
        Connection connection;

        if (!freeDbConnections.isEmpty()) {
            connection =  freeDbConnections.get(0); // restituisce la prima connessione disponibile
            freeDbConnections.remove(0); // fa in modo che il prossimo thread non possa usare la stessa connessione del thread attuale
                try {
                    if (connection.isValid(4))
                        connection = getConnection(); // richiama se stesso fin quando non trova una connessione valida
                } catch (SQLException e) {
                    connection.close();
                    connection = getConnection(); // richiama se stesso fin quando non trova una connessione valida
                }

        } else {
            connection = createDBConnection();
        }

        return connection;
    }

    public static synchronized void releaseConnection(Connection connection) throws SQLException {
        if(connection != null) freeDbConnections.add(connection);
    }
    public static synchronized void shutdown() {
        System.out.println("Chiusura connessioni...");
        for (Connection conn : freeDbConnections) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        freeDbConnections.clear();
        System.out.println("✓ Connessioni chiuse");
    }


}
