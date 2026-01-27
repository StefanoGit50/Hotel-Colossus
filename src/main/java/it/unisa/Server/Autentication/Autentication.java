package it.unisa.Server.Autentication;

import it.unisa.Common.Impiegato;
import it.unisa.Server.IllegalAccess;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.DAO.ImpiegatoDAO;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Autentication {
    String password;
    String userName;
    String pwd2;

    public Autentication(String password, String userName, String pwd2) {
        this.password = password;
        this.userName = userName;
        this.pwd2 = pwd2;
    }

    public boolean checkaccount() {
        Impiegato impiegato = null;
        BackofficeStorage<Impiegato> bo=null;

        Pattern p = Pattern.compile("^Receptionist(\\d+)$");
        Matcher matcher = p.matcher(userName);

        if (!matcher.matches())
            return false;

        if (password.contains("PWD-TMP-") && pwd2!=null) {
            bo = new ImpiegatoDAO();
            try {
                String soloNumeri = matcher.group(1);
                int numeroId = Integer.parseInt(soloNumeri);
                impiegato = bo.doRetriveByKey(numeroId);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            if (!TokenGenerator.isExpired(impiegato.getExpires())) {
                throw new IllegalAccess("la password è scaduta contattare il manager di riferimento");
            } else {
                //controllo se la password hashata corrisponde alla password passata
                if(!CredenzialiUtils.checkPassword(password, impiegato.getPassword()))
                    return false;
                impiegato.setPassword(CredenzialiUtils.HashPassword(pwd2));
                try {
                    bo.doSave(impiegato);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }else if(password.contains("PWD-TMP-") || pwd2!=null){
            return false;
        }
        else {
            bo = new ImpiegatoDAO();
            try {
                String soloNumeri = matcher.group(1);
                int numeroId = Integer.parseInt(soloNumeri);
                impiegato = bo.doRetriveByKey(numeroId);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            if(CredenzialiUtils.checkPassword(password, impiegato.getPassword())) {
                if (impiegato.getUsername().equals(userName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
