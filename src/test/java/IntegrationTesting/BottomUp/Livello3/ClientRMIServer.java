package IntegrationTesting.BottomUp.Livello3;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ClientRMIServer {
    private FrontDeskClient frontDeskClient = new FrontDeskClient();

    @Test
    @DisplayName("Bottom Up: LV3 FrontDeskClientRMI +FrontDeskServer+DB ")
    @Tag("integration-LV3")
    public void FrontDeskCompleteTest(){
        frontDeskClient.
    }

}
