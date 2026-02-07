package it.unisa.Server.Autentication;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.unisa.Common.Impiegato;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import java.util.Base64;

public class CredenzialiUtils {

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();


    /**
     * Effettua l'hash della password.
     *
     * @pre password != null && password != ""
     * @post result != null && result != "" && result != password
     */
    public static String HashPassword(String password) {
        password=password.trim();
        return BCrypt.withDefaults().hashToString(8, password.toCharArray());
    }

    /**
     * Verifica la validità di password.
     *
     * @pre password != null && password != "" && HashedPassword != null && HashedPassword != ""
     * @post result == true || result == false
     */
    public static boolean checkPassword(String password, String HashedPassword) {
        password=password.trim();
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), HashedPassword.toCharArray());
        if(result.verified){
            return true;
        }
        return false;
    }


    //TODO : chiamare il doUpdate di Impiegato sul valore di ritorno
    // TODO: bisogna ricordarsi prima di mettere la nuova password di rendere null sia campo expire sia il campo token nel DB
}
