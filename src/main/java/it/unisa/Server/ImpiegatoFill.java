package it.unisa.Server;

import it.unisa.Common.Impiegato;
import it.unisa.Server.Autentication.Autentication;
import it.unisa.Server.Autentication.CredenzialiUtils;
import it.unisa.Server.Autentication.TokenGenerator;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.DAO.ImpiegatoDAO;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

public class ImpiegatoFill {
     static void main(String[] args) throws SQLException {
         String password = "manno";
         TokenGenerator token = new TokenGenerator(1);
         String password2= token.getToken();
         //CredenzialiUtils.HashPassword(password);
        Impiegato imp = new Impiegato(0,"",password2,false, Instant.parse("2000-01-01T00:00:00Z"),"Luca","Pignolo","M","Patente","sdjnfs987",123,"sdasf","via pollo","acascom",3455,"Csfsag","4567742", Ruolo.FrontDesk,1500.00, LocalDate.of(2001,03,12),LocalDate.of(2011,02,03),"pollo@luca","italiana",LocalDate.of(2028,03,12));
        ImpiegatoDAO dao = new ImpiegatoDAO();
        //dao.doSave(imp);
         imp =dao.doRetriveByKey(imp.getCodiceFiscale());
         System.out.println(imp);
         if(imp.getExpires().isBefore(Instant.now())){
             throw new RuntimeException();
         }
/*
         boolean v =CredenzialiUtils.checkPassword(password,password2);
         System.out.println(v);

        imp.setTemporary(true);
        imp.setHashPassword(password2);
        imp.setExpires(token.getExpiresAt());
        dao.doUpdate(imp);
         System.out.println(imp);*/
    }
}
