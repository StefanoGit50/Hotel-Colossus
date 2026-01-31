package it.unisa.Server.Autentication;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.unisa.Common.Impiegato;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import java.util.Base64;

public class CredenzialiUtils {

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String HashPassword(String password) {
        password=password.trim();
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean checkPassword(String password, String HashedPassword) {
        password=password.trim();
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), HashedPassword.toCharArray());
        if(result.verified) {
            return true;
        }
        return false;
    }

    public static boolean checkAccount(String username, String password) {

        Impiegato impiegato;
        BackofficeStorage<Impiegato> impiegatoStorage = new ImpiegatoDAO();
        try{
             impiegato=impiegatoStorage.doRetriveByKey(username);
        }catch (Exception e){
            return false;
        }

        if (checkPassword(password, impiegato.getHashPassword())) {
            if(impiegato.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static String HashToken(String token){
        token=token.trim();
        return BCrypt.withDefaults().hashToString(12, token.toCharArray());
        //TODO : chiamare il doUpdate di Impiegato sul valore di ritorno
    }
    // TODO: bisogna ricordarsi prima di mettere la nuova password di rendere null sia campo expire sia il campo token nel DB
}
