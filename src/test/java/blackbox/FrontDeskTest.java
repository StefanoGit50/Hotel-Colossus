package blackbox;


import it.unisa.Common.*;
import it.unisa.Server.command.Invoker;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamere;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Stato;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Nested
@DisplayName("addPrenotazione()")

public class FrontDeskTest{


        @Mock
        private Invoker mockInvoker;

        @Mock
        private CatalogoPrenotazioni mockCatalogoPrenotazioni;

        @Mock
        private CatalogoClienti mockCatalogoClienti;

        @Mock
        private CatalogoCamere mockCatalogoCamere;

        private FrontDesk frontDesk;

        @BeforeEach
        void setUp() throws RemoteException {
            frontDesk = new FrontDesk(
                    mockInvoker,
                    mockCatalogoPrenotazioni,
                    mockCatalogoClienti,
                    mockCatalogoCamere
            );
        }


        @Test
        @DisplayName(" Prenotazione valida  nessuna eccezione")
        @Tag("true")
        void prenotazioneValida_nessunErrore() throws RemoteException {
            Prenotazione p = creaPrenotazioneValida();
            assertDoesNotThrow(()->frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName("Prenotazione null eccezione")
        @Tag("exception")
        void prenotazioneNull_lanciaEccezione() {
            assertThrows(Exception.class,() -> frontDesk.addPrenotazione( null));
        }

        @Test
        @DisplayName(" dataFine < dataInizio → eccezione")
        void dateInvertite_lanciaEccezione() {
            Prenotazione p = creaPrenotazioneValida();
            p.setDataInizio(LocalDate.now().plusDays(10));
            p.setDataFine(LocalDate.now().plusDays(5));
            assertThrows(Exception.class,() -> frontDesk.addPrenotazione(p));
        }

        // BOUNDARY VALUE: dataInizio = oggi
        @Test
        @DisplayName(" dataInizio = oggi → valido")
        void dataInizioOggi_valido() {
            Prenotazione p = creaPrenotazioneValida();
            p.setDataInizio(LocalDate.now());
            p.setDataFine(LocalDate.now().plusDays(1));

            assertDoesNotThrow(() -> frontDesk.addPrenotazione(p));
        }

        // BOUNDARY VALUE: dataInizio = ieri
        @Test
        @DisplayName(" dataInizio = ieri → eccezione")
        void dataInizioIeri_lanciaEccezione() {
            Prenotazione p = creaPrenotazioneValida();
            p.setDataInizio(LocalDate.now().minusDays(1));

            assertThrows(Exception.class,() -> frontDesk.addPrenotazione(p));
        }

        @Test
        @DisplayName(" Ritorna lista di camere")
        void getCamere_restituisceLista() {
            // Setup: il mock restituisce dati (necessario per far funzionare)
            List<Camera> camereAttese = List.of(creaCamera(101), creaCamera(102));
            when(mockCatalogoCamere.getListaCamere()).thenReturn((ArrayList<Camera>) camereAttese);

            List<Camera> result = frontDesk.getCamere();

            // Verifico SOLO l'output
            assertEquals(result.size(),camereAttese.size());
            assertEquals(101,result.get(0).getNumeroCamera());
            assertEquals(102,result.get(1).getNumeroCamera());
        }

        @Test
        @DisplayName("Lista vuota → restituisce lista vuota")
        void getCamere_listaVuota_restituisceVuota() {
            when(mockCatalogoCamere.getListaCamere()).thenReturn(new ArrayList<>());

            List<Camera> result = frontDesk.getCamere();

            assertTrue(result.isEmpty());
        }



        @Test
        @DisplayName("Camera valida → restituisce true")
        void cameraValida_restituisceTrue() throws RemoteException {
            Camera c = creaCamera(101);
            c.setStatoCamera(Stato.Occupata);
            when(mockCatalogoCamere.aggiornaStatoCamera(c)).thenReturn(true);

            assertTrue(frontDesk.aggiornaStatoCamera(c));
        }

        @Test
        @DisplayName("CV2: Camera non esistente → restituisce false")
        void cameraNonEsiste_restituisceFalse() throws RemoteException {
            Camera c = creaCamera(999);
            when(mockCatalogoCamere.aggiornaStatoCamera(c)).thenReturn(false);

            assertFalse(frontDesk.aggiornaStatoCamera(c));

        }

        @Test
        @DisplayName("CNV1: Camera null → eccezione")
        void cameraNull_lanciaEccezione() {
            assertThrows(Exception.class,()-> frontDesk.aggiornaStatoCamera(null));
        }

        // BOUNDARY: numero camera = 0
        @Test
        @DisplayName("BVA1: numeroCamera = 0")
        void numeroCameraZero() throws RemoteException {
            Camera c = creaCamera(0);
            when(mockCatalogoCamere.aggiornaStatoCamera(c)).thenReturn(false);

            assertFalse(frontDesk.aggiornaStatoCamera(c));
        }

        // BOUNDARY: numero camera negativo
        @Test
        @DisplayName("BVA2: numeroCamera = -1")
        void numeroCameraNegativo() throws RemoteException {
            Camera c = creaCamera(-1);
            when(mockCatalogoCamere.aggiornaStatoCamera(c)).thenReturn(false);

            assertFalse(frontDesk.aggiornaStatoCamera(c));
        }


        // HELPER
        private Prenotazione creaPrenotazioneValida() {
            ArrayList<Camera> listcamera=new ArrayList<>();
            listcamera.add(new Camera(112, Stato.Occupata,2,45.50,"",""));
            ArrayList<Servizio> servizio=new ArrayList<>();
            servizio.add(new Servizio("Piscina",20));
            ArrayList<Cliente> cliente=new ArrayList<>();
            cliente.add(new Cliente("mario",
                    "Rossi",
                    "Napoli",
                    "Napoli",
                    "via manzo",
                    12,
                    45,
                    "323425",
                    "M",
                    LocalDate.of(1998,12,1),
                    "CF234rdfcfg",
                    "luca@gmail.com",
                    "italiana",
                    new Camera(101,Stato.Libera, 1, 56, "","papaBenedetto")));

                Prenotazione p= new Prenotazione(
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    LocalDate.now().plusDays(10),
                    LocalDate.of(2026, 02, 01),
                    new Trattamento("MEZZA PENSIONE", 60),
                    60.0,
                    "Passaporto",
                    LocalDate.of(2012, 03, 11),
                    LocalDate.of(2044, 12, 11),
                    "Mario Biondi",
                    "",
                    servizio,
                    cliente,
                    "34532MC2",
                    "Carta di Credito",
                    "Italiana");
            return p;
        }

        private Camera creaCamera(int numero) {
            Camera c = new Camera();
            c.setNumeroCamera(numero);
            c.setStatoCamera(Stato.Libera);
            return c;
        }

}
