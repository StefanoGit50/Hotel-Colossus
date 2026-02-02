package blackbox.FiltroPrenotazioni;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import it.unisa.interfacce.FrontDeskInterface;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

@Tag("filtroPrenotazioni")
public class TestFiltroPrenotazioni {
    public static FrontDeskInterface frontDesk;

    @BeforeAll
    public static void istantiateFrontDesk() throws RemoteException, NotBoundException, MalformedURLException {
        frontDesk = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
    }

    /* ************************************************************************************************************** */


    /* *************************** CASI DI SUCCESSO *************************** */

}
