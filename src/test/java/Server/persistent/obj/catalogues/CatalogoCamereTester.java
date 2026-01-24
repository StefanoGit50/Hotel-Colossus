package Server.persistent.obj.catalogues;

import it.unisa.Common.Camera;
import it.unisa.Server.ObserverCamereInterface;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.util.Stato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogoCamereTester
{
    @Mock
    Camera mockCamera1;

    @Mock
    Camera mockCamera2;

    @Mock
    ObserverCamereInterface mockObserver;

    @InjectMocks
    private CatalogoCamere catalogo;

    @BeforeEach
    void setUp()
    {
        catalogo = new CatalogoCamere();
        CatalogoCamere.getListaCamere().clear();
    }

    // getCamera

    @Test
    @DisplayName("getCamera: trovata")
    void getCameraTest_Trovata() throws CloneNotSupportedException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);
        when(mockCamera1.clone()).thenReturn(mockCamera1);

        CatalogoCamere.getListaCamere().add(mockCamera1);

        Camera risultato = catalogo.getCamera(101);

        assertNotNull(risultato);
        verify(mockCamera1).clone();
    }

    @Test
    @DisplayName("getCamera: non trovata")
    void getCameraTest_NonTrovata() throws CloneNotSupportedException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);

        CatalogoCamere.getListaCamere().add(mockCamera1);

        Camera risultato = catalogo.getCamera(999);

        assertNull(risultato);
    }

    // aggiornaStatoCamera

    @Test
    @DisplayName("aggiornaStatoCamera: stato modificato con successo")
    void aggiornaStatoCameraTest_StatoModificatoConSuccesso() throws RemoteException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);
        when(mockCamera1.getStatoCamera()).thenReturn(Stato.Libera);
        when(mockCamera2.getNumeroCamera()).thenReturn(101);
        when(mockCamera2.getStatoCamera()).thenReturn(Stato.Occupata);

        CatalogoCamere.getListaCamere().add(mockCamera1);
        catalogo.attach(mockObserver);

        boolean risultato = catalogo.aggiornaStatoCamera(mockCamera2);

        assertTrue(risultato);
        verify(mockCamera1).setStatoCamera(Stato.Occupata);
        verify(mockObserver).update();
    }

    @Test
    @DisplayName("aggiornaStatoCamera: stato non modificato (stesso stato e camera non trovata)")
    void aggiornaStatoCameraTest_StatoNonModificato() throws RemoteException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);
        when(mockCamera1.getStatoCamera()).thenReturn(Stato.Libera);

        CatalogoCamere.getListaCamere().add(mockCamera1);
        catalogo.attach(mockObserver);

        // Test 1: stesso stato (condizione !cam.getStatoCamera().equals(c.getStatoCamera()) = false)
        when(mockCamera2.getNumeroCamera()).thenReturn(101);
        when(mockCamera2.getStatoCamera()).thenReturn(Stato.Libera);

        boolean risultato1 = catalogo.aggiornaStatoCamera(mockCamera2);

        assertFalse(risultato1);
        verify(mockCamera1, never()).setStatoCamera(any());
        verify(mockObserver, never()).update();

        // Test 2: camera non trovata (condizione cam.getNumeroCamera()==c.getNumeroCamera() = false)
        Camera mockCamera3 = mock(Camera.class);
        when(mockCamera3.getNumeroCamera()).thenReturn(999);
        when(mockCamera3.getStatoCamera()).thenReturn(Stato.Occupata);

        boolean risultato2 = catalogo.aggiornaStatoCamera(mockCamera3);

        assertFalse(risultato2);
        verify(mockCamera1, never()).setStatoCamera(any());
        verify(mockObserver, never()).update();
    }

    // attach e detach

    @Test
    @DisplayName("attach: observer aggiunto e notificato")
    void attachTest_ObserverAggiuntoENotificato() throws RemoteException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);
        when(mockCamera1.getStatoCamera()).thenReturn(Stato.Libera);
        when(mockCamera2.getNumeroCamera()).thenReturn(101);
        when(mockCamera2.getStatoCamera()).thenReturn(Stato.Occupata);

        CatalogoCamere.getListaCamere().add(mockCamera1);
        catalogo.attach(mockObserver);

        catalogo.aggiornaStatoCamera(mockCamera2);

        verify(mockObserver, times(1)).update();
    }

    @Test
    @DisplayName("detach: observer rimosso e non notificato")
    void detachTest_ObserverRimossoENonNotificato() throws RemoteException
    {
        when(mockCamera1.getNumeroCamera()).thenReturn(101);
        when(mockCamera1.getStatoCamera()).thenReturn(Stato.Libera);
        when(mockCamera2.getNumeroCamera()).thenReturn(101);
        when(mockCamera2.getStatoCamera()).thenReturn(Stato.Occupata);

        CatalogoCamere.getListaCamere().add(mockCamera1);
        catalogo.attach(mockObserver);
        catalogo.detach(mockObserver);

        catalogo.aggiornaStatoCamera(mockCamera2);

        verify(mockObserver, never()).update();
    }
}