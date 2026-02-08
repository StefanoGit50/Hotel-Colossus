package Cataloghi;


import WhiteBox.UnitTest.DBPopulator;
import com.sun.javafx.geom.AreaOp;
import it.unisa.Common.Camera;
import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Server.persistent.util.Util;
import it.unisa.Storage.Interfacce.FrontDeskStorage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatologoCamereTest{

    private static MockedStatic<Util> utilMockedStatic;

    private CatalogoCamere catalogoCamere;

    @BeforeAll
    public static void before(){
        DBPopulator.cancel();
    }

    @BeforeEach
    public void beforeEach(){
        DBPopulator.populator();
    }

    @AfterEach
    public void afterEach(){
        DBPopulator.cancel();
    }

    @Test
    @Tag("True")
    @DisplayName("getListaCamera() restituisce la lista delle camere")
    public void getListaCamera(){

    }

}
