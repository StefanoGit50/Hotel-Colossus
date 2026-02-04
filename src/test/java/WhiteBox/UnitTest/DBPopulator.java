package WhiteBox.UnitTest;

import it.unisa.Storage.ConnectionStorage;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBPopulator{
    private static Connection connection;
    public static void populator(){
        connection = ConnectionStorage.getConnection();
        try {

            ScriptRunner runner = new ScriptRunner(connection);


            runner.setLogWriter(null);
            runner.setStopOnError(true); // Si ferma se c'è un errore SQL

            // Leggi il file

            Reader reader = new BufferedReader(new FileReader("src/main/java/it/unisa/Storage/SQL/insertDB.sql"));

            runner.runScript(reader);

            System.out.println("Script eseguito con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cancel(){
        connection = ConnectionStorage.getConnection();
        try {

            ScriptRunner runner = new ScriptRunner(connection);

            // Impostazioni opzionali
            runner.setLogWriter(null);
            runner.setStopOnError(true); // Si ferma se c'è un errore SQL

           //lettura file
            Reader reader = new BufferedReader(new FileReader("src/main/java/it/unisa/Storage/SQL/re.sql"));

            runner.runScript(reader);

            System.out.println("Script eseguito con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
