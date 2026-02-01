package WhiteBox.Server.Autentication;

import it.unisa.Common.Impiegato;
import it.unisa.Server.Autentication.CredenzialiUtils;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredenzialiUtilsTest
{

    @Mock
    private BackofficeStorage<Impiegato> impiegatoStorage;

    @Mock
    private Impiegato impiegato;

    private String testPassword;
    private String testUsername;
    private String hashedPassword;

    @BeforeEach
    void setUp()
    {
        testPassword = "password123";
        testUsername = "testUser";
        // Genera una password hashata reale per i test
        hashedPassword = CredenzialiUtils.HashPassword(testPassword);
    }

    // Test per HashPassword

    // valore valido
    @Test
    void testHashPasswordReturnNotNull()
    {
        String password = "testPassword";
        String hashed = CredenzialiUtils.HashPassword(password);

        assertNotNull(hashed);
    }

    // Test per checkPassword

    // valore valido
    @Test
    void testCheckPasswordCorrectPassword()
    {
        String password = "correctPassword";
        String hashed = CredenzialiUtils.HashPassword(password);

        boolean result = CredenzialiUtils.checkPassword(password, hashed);

        assertTrue(result);
    }

    // password errata
    @Test
    void testCheckPasswordIncorrectPassword()
    {
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String hashed = CredenzialiUtils.HashPassword(correctPassword);

        boolean result = CredenzialiUtils.checkPassword(wrongPassword, hashed);

        assertFalse(result);
    }


    // Test per checkAccount

    // credenziali valide
    @Test
    void testCheckAccountValidCredentials() throws Exception
    {
        // Arrange
        when(impiegatoStorage.doRetriveByKey(testUsername)).thenReturn(impiegato);
        when(impiegato.getHashPassword()).thenReturn(hashedPassword);
        when(impiegato.getUserName()).thenReturn(testUsername);

        // Act
       // boolean result = CredenzialiUtils.checkAccount(testUsername, testPassword, impiegatoStorage);

        // Assert
       // assertTrue(result);
        verify(impiegatoStorage).doRetriveByKey(testUsername);
        verify(impiegato).getHashPassword();
        verify(impiegato).getUserName();
    }


    // errore nel db
    @Test
    void testCheckAccountDatabaseException() throws Exception
    {
        // Arrange
        when(impiegatoStorage.doRetriveByKey(anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        //boolean result = CredenzialiUtils.checkAccount(testUsername, testPassword, impiegatoStorage);

        // Assert
        //assertFalse(result);
        verify(impiegatoStorage).doRetriveByKey(testUsername);
    }

    // password errata
    @Test
    void testCheckAccountWrongPassword() throws Exception
    {
        // Arrange
        String wrongPassword = "passwordErrata";
        when(impiegatoStorage.doRetriveByKey(testUsername)).thenReturn(impiegato);
        when(impiegato.getHashPassword()).thenReturn(hashedPassword);

        // Act
       // boolean result = CredenzialiUtils.checkAccount(testUsername, wrongPassword, impiegatoStorage);

        // Assert
       // assertFalse(result);
        verify(impiegatoStorage).doRetriveByKey(testUsername);
        verify(impiegato).getHashPassword();
    }


    // username errato
    @Test
    void testCheckAccountWrongUsername() throws Exception
    {
        // Arrange
        String wrongUsername = "usernameDiverso";
        when(impiegatoStorage.doRetriveByKey(testUsername)).thenReturn(impiegato);
        when(impiegato.getHashPassword()).thenReturn(hashedPassword);
        when(impiegato.getUserName()).thenReturn(wrongUsername);

        // Act
        //boolean result = CredenzialiUtils.checkAccount(testUsername, testPassword, impiegatoStorage);

        // Assert
        //assertFalse(result);
        verify(impiegatoStorage).doRetriveByKey(testUsername);
        verify(impiegato).getHashPassword();
        verify(impiegato).getUserName();
    }


    // campo password vuoto
    @Test
    void testCheckAccountPasswordIsNull() throws Exception
    {
        // Arrange
        when(impiegatoStorage.doRetriveByKey(testUsername)).thenReturn(impiegato);
        when(impiegato.getHashPassword()).thenReturn(null);

        // Act e Assert
        assertThrows(NullPointerException.class, () ->
        {
           // CredenzialiUtils.checkAccount(testUsername, testPassword, impiegatoStorage);
        });
    }

    // Test per HashToken

    // token valido
    @Test
    void testHashTokenNonNullHash()
    {
        String token = "testToken123";

        //String hashed = CredenzialiUtils.HashToken(token);

      //assertNotNull(hashed);
    }
}