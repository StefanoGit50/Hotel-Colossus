package WhiteBox.UnitTest;

import it.unisa.Storage.ConnectionStorage;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBPopulator{
    private Connection connection;
    public void populator(){
        this.connection = ConnectionStorage.getConnection();
        try {
            // Inizializza lo ScriptRunner
            ScriptRunner runner = new ScriptRunner(connection);

            // Impostazioni opzionali (ma utili)
            runner.setLogWriter(null); // Se non vuoi vedere l'output in console
            runner.setStopOnError(true); // Si ferma se c'è un errore SQL

            // Leggi il file
            // Assicurati che il path sia corretto!
            Reader reader = new BufferedReader(new FileReader("src/main/java/it/unisa/Storage/SQL/insertDB.sql"));
            // Esegui
            runner.runScript(reader);

            System.out.println("Script eseguito con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancel(){
        this.connection = ConnectionStorage.getConnection();
        try {
            // Inizializza lo ScriptRunner
            ScriptRunner runner = new ScriptRunner(connection);

            // Impostazioni opzionali (ma utili)
            runner.setLogWriter(null); // Se non vuoi vedere l'output in console
            runner.setStopOnError(true); // Si ferma se c'è un errore SQL

            // Leggi il file
            // Assicurati che il path sia corretto!
            Reader reader = new BufferedReader(new FileReader("src/main/java/it/unisa/Storage/SQL/DB1.sql"));
            // Esegui
            runner.runScript(reader);

            System.out.println("Script eseguito con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
