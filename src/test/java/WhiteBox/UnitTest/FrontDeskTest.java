package WhiteBox.UnitTest;

import it.unisa.Common.*;
import it.unisa.Server.Autentication.Autentication;
import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.Server.commandPattern.CatalogoClientiCommands.*;
import it.unisa.Server.commandPattern.CatalogoPrenotazioniCommands.*;
import it.unisa.Server.commandPattern.Invoker;
import it.unisa.Server.commandPattern.Others.RetrieveAllCamereCommand;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.obj.catalogues.CatalogueUtils;
import it.unisa.Server.persistent.util.Stato;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FrontDeskTest {

    private FrontDesk frontDesk;
    private Invoker invokerMock;
    private CatalogoCamere catalogoCamereMock;

    // Oggetti per gestire i mock statici
    private MockedStatic<CatalogoPrenotazioni> catalogoPrenotazioniStatic;
    private MockedStatic<CatalogueUtils> catalogueUtilsStatic;
    private MockedStatic<Autentication> autenticationStatic;
    private MockedStatic<CatalogoCamere> catalogoCamereStatic;

    @BeforeEach
    void setUp() throws RemoteException {
        // 1. Mock delle dipendenze iniettabili
        invokerMock = mock(Invoker.class);
        catalogoCamereMock = mock(CatalogoCamere.class);

        // 2. Inizializzazione dei Mock Statici
        catalogoPrenotazioniStatic = mockStatic(CatalogoPrenotazioni.class);
        catalogueUtilsStatic = mockStatic(CatalogueUtils.class);
        autenticationStatic = mockStatic(Autentication.class);
        catalogoCamereStatic = mockStatic(CatalogoCamere.class);

        // 3. Istanziazione FrontDesk usando il costruttore di testing
        frontDesk = new FrontDesk(invokerMock, catalogoCamereMock);
    }

    @AfterEach
    void tearDown() {
        // 4. Chiusura dei mock statici
        if (catalogoPrenotazioniStatic != null) catalogoPrenotazioniStatic.close();
        if (catalogueUtilsStatic != null) catalogueUtilsStatic.close();
        if (autenticationStatic != null) autenticationStatic.close();
        if (catalogoCamereStatic != null) catalogoCamereStatic.close();
    }

    // --- TEST METODI BASATI SU INVOKER (Comandi void) --- //

    @Test
    void testAddPrenotazione() throws RemoteException, IllegalAccess {
        Prenotazione p = mock(Prenotazione.class);

        catalogueUtilsStatic.when(() -> CatalogueUtils.checkNull(p)).thenAnswer(i -> null);
        catalogoPrenotazioniStatic.when(() -> CatalogoPrenotazioni.checkPrenotazione(p)).thenAnswer(i -> null);

        frontDesk.addPrenotazione(p);

        catalogueUtilsStatic.verify(() -> CatalogueUtils.checkNull(p));
        catalogoPrenotazioniStatic.verify(() -> CatalogoPrenotazioni.checkPrenotazione(p));

        verify(invokerMock, times(1)).executeCommand(any(AddPrenotazioneCommand.class));
    }

    @Test
    void testAddCliente() throws RemoteException, IllegalAccess {
        Cliente c = mock(Cliente.class);

        frontDesk.addCliente(c);

        verify(invokerMock).executeCommand(any(AddClienteCommand.class));
    }

    @Test
    void testBanCliente() throws RemoteException, IllegalAccess {
        Cliente c = mock(Cliente.class);
        when(c.getCf()).thenReturn("CF12345");

        frontDesk.banCliente(c);

        verify(invokerMock).executeCommand(any(BanCommand.class));
    }

    // --- TEST METODI DI RECUPERO DATI (Comandi con return) --- //

    @Test
    void testGetListaPrenotazioni() throws RemoteException, IllegalAccess {
        ArrayList<Prenotazione> expectedList = new ArrayList<>();
        expectedList.add(mock(Prenotazione.class));

        try (MockedConstruction<RetrieveAllPCommand> mockedCmd = Mockito.mockConstruction(RetrieveAllPCommand.class,
                (mock, context) -> {
                    when(mock.getPrenotazioni()).thenReturn(expectedList);
                })) {

            ArrayList<Prenotazione> result = frontDesk.getListaPrenotazioni();

            assertEquals(expectedList, result);

            RetrieveAllPCommand createdCommand = mockedCmd.constructed().get(0);
            verify(invokerMock).executeCommand(createdCommand);
        }
    }

    @Test
    void testGetListaCamere() throws RemoteException, IllegalAccess {
        ArrayList<Camera> expectedList = new ArrayList<>();

        try (MockedConstruction<RetrieveAllCamereCommand> mockedCmd = Mockito.mockConstruction(RetrieveAllCamereCommand.class,
                (mock, context) -> {
                    when(mock.getCamere()).thenReturn(expectedList);
                })) {

            ArrayList<Camera> result = frontDesk.getListaCamere();

            assertEquals(expectedList, result);
            verify(invokerMock).executeCommand(mockedCmd.constructed().get(0));
        }
    }

    // --- TEST METODI DIRETTI SUI CATALOGHI --- //

    @Test
    void testGetPrenotazioniDiretto() throws RemoteException, IllegalAccess {
        // Questo testa il metodo "getPrenotazioni" (senza "Lista") che chiama direttamente il statico
        ArrayList<Prenotazione> expectedList = new ArrayList<>();
        catalogoPrenotazioniStatic.when(CatalogoPrenotazioni::getListaPrenotazioni).thenReturn(expectedList);

        ArrayList<Prenotazione> result = frontDesk.getListaPrenotazioni();

        assertEquals(expectedList, result);
    }

    @Test
    void testAggiornaStatoCamera() throws RemoteException {
        Camera c = mock(Camera.class);
        when(c.getNumeroCamera()).thenReturn(101);
        when(c.getStatoCamera()).thenReturn(Stato.Libera);

        when(catalogoCamereMock.aggiornaStatoCamera(c)).thenReturn(true);

        boolean result = frontDesk.aggiornaStatoCamera(c);

        assertTrue(result);
        verify(catalogoCamereMock).aggiornaStatoCamera(c);
    }

    @Test
    void testUpdate() throws RemoteException {
        Camera expectedCamera = mock(Camera.class);
        catalogoCamereStatic.when(CatalogoCamere::getLastModified).thenReturn(expectedCamera);

        Camera result = frontDesk.update();

        assertEquals(expectedCamera, result);
    }

    // --- TEST AUTENTICAZIONE --- //

    @Test
    void testAuthenticationSuccess() throws RemoteException, IllegalAccess {
        String user = "user";
        String pass = "pass";
        String pass2 = "pass";
        Impiegato expectedImp = mock(Impiegato.class);

        autenticationStatic.when(() -> Autentication.checkaccount(user, pass, pass2)).thenReturn(true);
        autenticationStatic.when(Autentication::getImpiegato).thenReturn(expectedImp);

        Impiegato result = frontDesk.authentication(user, pass, pass2);

        assertNotNull(result);
        assertEquals(expectedImp, result);
    }

    @Test
    void testAuthenticationFail() throws RemoteException, IllegalAccess {
        String user = "user";
        String pass = "hashedPassword1!";
        String pass2 = "hashedPassword1!";

        autenticationStatic.when(() -> Autentication.checkaccount(user, pass, pass2)).thenReturn(false);

        Impiegato result = frontDesk.authentication(user, pass, pass2);

        assertNull(result);
    }
}