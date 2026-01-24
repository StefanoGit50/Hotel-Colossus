package it.unisa.Storage.DAO;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class Test {
    static void main() throws SQLException, ClassNotFoundException {
        ImpiegatoDAO dao = new ImpiegatoDAO();
        Collection<Impiegato> l = dao.doFilter("Lorenzo", "M", Ruolo.Manager, "");
        System.out.println(l.isEmpty());
    }
}
