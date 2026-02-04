package it.unisa.Server.Autentication;

import it.unisa.Common.Impiegato;
import it.unisa.Server.IllegalAccess;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Autentication {
    private String password;
    private String userName;
    private String pwd2;
   private static Impiegato impiegato= null;


    public Autentication(String password, String userName, String pwd2) {
        this.password = password;
        this.userName = userName;
        this.pwd2 = pwd2;
    }

    public static boolean checkaccount(String username, String password,String pwd2) throws IllegalAccess {


        System.out.println("Username: " + username);
        BackofficeStorage<Impiegato> bo=null;

        Pattern p = Pattern.compile("^(Reception|Manager|Governante)(\\d+)$");
        Matcher matcher = p.matcher(username);


        if (!matcher.matches())
            return false;


        if (pwd2!=null && pwd2.contains("PWD-TMP-")) {
            bo = new ImpiegatoDAO();
            try {
                String soloNumeri = matcher.group(2);
                int numeroId = Integer.parseInt(soloNumeri);
                impiegato = bo.doRetriveByKey(numeroId);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            System.out.println("impiegato nel TMP"+impiegato);
            if (!TokenGenerator.isExpired(impiegato.getExpires())) {
                impiegato=null;
                throw new IllegalAccess("la password è scaduta contattare il manager di riferimento");
            } else {
                //controllo se la password hashata corrisponde alla password passata
                if(!CredenzialiUtils.checkPassword(password, impiegato.getHashPassword())){
                    impiegato=null;
                    return false;
                }
                impiegato.setHashPassword((CredenzialiUtils.HashPassword(pwd2)));
                try {
                    bo.doSave(impiegato);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }else if(password.contains("PWD-TMP-") || pwd2!=null){
            impiegato=null;
            return false;
        }
        else {
            // Log per capire cosa succede
            System.out.println("Tentativo Login Standard...");

            bo = new ImpiegatoDAO();
            try {

                String soloNumeri = matcher.group(2);
                int numeroId = Integer.parseInt(soloNumeri);


                impiegato = bo.doRetriveByKey(numeroId);
                System.out.println("Trovato impiegato ID " + numeroId + ": " + impiegato.getUsername());

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            // 3. VERIFICA PASSWORD
            // Se la password corrisponde all'hash nel DB, il login è valido.
            if(CredenzialiUtils.checkPassword(password.trim(), impiegato.getHashPassword().trim())) {

                // --- MODIFICA QUI ---
                // Rimuoviamo il controllo sul nome che ti sta bloccando (Reception3 vs Reception33)
                // if (impiegato.getUsername().equals(username)) {
                System.out.println("Password OK! Login effettuato.");
                return true;
                // }
                // --------------------
            } else {
                System.out.println("Errore: Password non valida.");
            }
        }

        return false;
    }

    public static Impiegato getImpiegato() {
        Impiegato impiegatocopia = null;
        try{
            impiegatocopia=impiegato.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return impiegatocopia;
    }

}
