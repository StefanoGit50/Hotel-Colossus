package blackbox.FiltroClienti;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

@Tag("filtroClienti")
public class TestFiltroClienti {
    private static FrontDeskInterface frontDesk;

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
    }

    /* *************************** CASI DI SUCCESSO *************************** */


}
