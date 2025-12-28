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
    }

    private static synchronized Connection createDBConnection() throws SQLException {
        java.sql.Connection newConnection;
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
            connection =  freeDbConnections.get(0);
            freeDbConnections.remove(0);

            try {
                if (connection.isClosed())
                    connection = getConnection();
            } catch (SQLException e) {
                connection.close();
                connection = getConnection();
            }
        } else {
            connection = createDBConnection();
        }

        return connection;
    }

    public static synchronized void releaseConnection(java.sql.Connection connection) throws SQLException {
        if(connection != null) freeDbConnections.add(connection);
    }
}
