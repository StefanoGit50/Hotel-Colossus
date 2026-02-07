package IntegrationTesting.BottomUp.Livello3;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Client.GUI.components.BookingCreation;
import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientRMIServer {
    private FrontDeskClient frontDeskClient = new FrontDeskClient();

    public ClientRMIServer() throws MalformedURLException, NotBoundException, RemoteException {
    }

    @Test
    @DisplayName("Bottom Up TC24: LV3 FrontDeskClientRMI +FrontDeskServer+DB ")
    @Tag("integration-LV3")
    public void FrontDeskCompleteTest(){
        ArrayList<Camera> camere = null;
        try {
            frontDeskClient.FrontDeskController(new FrontDesk());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
