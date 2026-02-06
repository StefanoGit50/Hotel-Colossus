package IntegrationTesting.BottomUp.Livello3;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Server.IllegalAccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientRMIServer {
    private FrontDeskClient frontDeskClient = new FrontDeskClient();

    public ClientRMIServer() throws MalformedURLException, NotBoundException, RemoteException {
    }

    @Test
    @DisplayName("Bottom Up TC24: LV3 FrontDeskClientRMI +FrontDeskServer+DB ")
    @Tag("integration-LV3")
    public void FrontDeskCompleteTest() throws IllegalAccess, RemoteException {

       //frontDeskClient

    }
}
