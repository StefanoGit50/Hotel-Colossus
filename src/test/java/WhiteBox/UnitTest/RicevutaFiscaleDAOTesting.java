package WhiteBox.UnitTest;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.DAO.RicevutaFiscaleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RicevutaFiscaleDAOTesting{
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private RicevutaFiscale ricevutaFiscale;

    @InjectMocks
    private RicevutaFiscaleDAO ricevutaFiscaleDAO;

    @BeforeEach
    public void setUp(){
        ricevutaFiscale = new RicevutaFiscale(12,1,30.0, LocalDate.of(2006,12,20),"Carta di credito" , LocalDate.of(2009,11,30) , 25 , "sanificazione");
    }

    @Test
    @DisplayName("doSave() quando va tutto bene")
    public void doSaveAllTrue() throws SQLException {
        try(MockedStatic<ConnectionStorage> connectionSM = mockStatic(ConnectionStorage.class)){
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("insert into RicevutaFiscale " +
                    "VALUES (?,?,?,?,?,?,?)")).thenReturn(preparedStatement);

            when(preparedStatement.executeUpdate()).thenReturn(1);

            ricevutaFiscaleDAO.doSave(ricevutaFiscale);
        }
    }

    @Test
    @DisplayName("doDelete() quando fa l'eccezione")
    public void doDeleteAllTrue() throws SQLException {
        try(MockedStatic<ConnectionStorage> connectionSM = mockStatic(ConnectionStorage.class)){
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("delete from ricevutafiscale" +
                    " where IDRicevutaFiscale = ? and IDPrenotazione = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            ricevutaFiscaleDAO.doDelete(ricevutaFiscale);
        }

    }
/*
    public void doRetriveByKey() throws SQLException {
        try(MockedStatic<ConnectionStorage> connectionSM = mockStatic(ConnectionStorage.class)){
            connectionSM.when(ConnectionStorage::getConnection).thenReturn(connection);

            when(connection.prepareStatement("select * FROM ricevutafiscale" +
                    " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?")).thenReturn(preparedStatement);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true,true,false);
            when(resultSet.getInt("IDPrenotazione")).thenReturn(2);
            when(resultSet.getInt("IDRicevutaFiscale")).thenReturn(4);
            when(resultSet.getDouble()).thenReturn(20.0);

        }
    }
*/

}
