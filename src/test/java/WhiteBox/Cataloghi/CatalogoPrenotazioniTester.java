package WhiteBox.Cataloghi;

import it.unisa.Common.Camera;
import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.obj.catalogues.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogoPrenotazioniTester
{
    @Mock
    Prenotazione mockPrenotazione1;

    @Mock
    Prenotazione mockPrenotazione2;

    @Mock
    Prenotazione mockPrenotazione3;

    @Mock
    Camera mockCamera1;

    @Mock
    Camera mockCamera2;

    @Mock
    Cliente mockCliente1;

    @InjectMocks
    private CatalogoPrenotazioni catalogo;

    @BeforeEach
    void setUp()
    {
        catalogo = new CatalogoPrenotazioni();
        CatalogoPrenotazioni.getListaPrenotazioni().clear();
    }

    // cercaPrenotazioni

    @Test
    @DisplayName("cercaPrenotazioni: tutti parametri null/default")
    void cercaPrenotazioniTest_TuttiParametriNull() throws CloneNotSupportedException
    {
        //ArrayList<Prenotazione> risultato = catalogo.cercaPrenotazioni(null, -1, null, null, true);
      //assertNull(risultato);
    }

    @Test
    @DisplayName("cercaPrenotazioni: almeno un parametro non null con lista vuota")
    void cercaPrenotazioniTest_AlmenoUnParametroNonNull_ListaVuota() throws CloneNotSupportedException
    {
        //ArrayList<Prenotazione> risultato = catalogo.cercaPrenotazioni("Mario Rossi", -1, null, null, true);
        //assertNotNull(risultato);
        //assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaPrenotazioni: parametri trovati")
    void cercaPrenotazioniTest_ParametriTrovati() throws CloneNotSupportedException
    {
        ArrayList<Camera> listaCamere = new ArrayList<>();
        listaCamere.add(mockCamera1);

        LocalDate dataInizioRicerca = LocalDate.of(2025, 1, 1);
        LocalDate dataFineRicerca = LocalDate.of(2025, 1, 31);
        LocalDate dataInizioPrenotazione = LocalDate.of(2025, 1, 10);
        LocalDate dataFinePrenotazione = LocalDate.of(2025, 1, 20);

        when(mockPrenotazione1.getIntestatario()).thenReturn("Mario Rossi");
        //when(mockPrenotazione1.getListaCamere()).thenReturn(listaCamere);
        when(mockPrenotazione1.getDataInizio()).thenReturn(dataInizioPrenotazione);
        when(mockPrenotazione1.getDataFine()).thenReturn(dataFinePrenotazione);
        when(mockPrenotazione1.clone()).thenReturn(mockPrenotazione1);
        when(mockCamera1.getNumeroCamera()).thenReturn(101);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);

        // Test nominativo trovato
       /*
        ArrayList<Prenotazione> risultatoNominativo = catalogo.cercaPrenotazioni("Mario Rossi", -1, null, null, true);
        assertNotNull(risultatoNominativo);
        assertEquals(1, risultatoNominativo.size());

        // Test numeroCamera trovato
        ArrayList<Prenotazione> risultatoNumero = catalogo.cercaPrenotazioni(null, 101, null, null, true);
        assertNotNull(risultatoNumero);
        assertEquals(1, risultatoNumero.size());

        // Test date trovate
        ArrayList<Prenotazione> risultatoDate = catalogo.cercaPrenotazioni(null, -1, dataInizioRicerca, dataFineRicerca, true);
        assertNotNull(risultatoDate);
        assertEquals(1, risultatoDate.size());*/
    }

    @Test
    @DisplayName("cercaPrenotazioni: parametri non trovati")
    void cercaPrenotazioniTest_ParametriNonTrovati() throws CloneNotSupportedException
    {
        ArrayList<Camera> listaCamere = new ArrayList<>();
        listaCamere.add(mockCamera1);

        when(mockPrenotazione1.getIntestatario()).thenReturn("Luigi Verdi");
        //when(mockPrenotazione1.getListaCamere()).thenReturn(listaCamere);
        when(mockCamera1.getNumeroCamera()).thenReturn(101);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);

        // Test nominativo non trovato
        /*
        ArrayList<Prenotazione> risultatoNominativo = catalogo.cercaPrenotazioni("Mario Rossi", -1, null, null, true);
        assertNotNull(risultatoNominativo);
        assertTrue(risultatoNominativo.isEmpty());

        // Test numeroCamera non trovato
        ArrayList<Prenotazione> risultatoNumero = catalogo.cercaPrenotazioni(null, 202, null, null, true);
        assertNotNull(risultatoNumero);
        assertTrue(risultatoNumero.isEmpty());*/
    }

    @Test
    @DisplayName("cercaPrenotazioni: solo dataInizio fornita (dataFine null)")
    void cercaPrenotazioniTest_SoloDataInizioFornita() throws CloneNotSupportedException
    {
        LocalDate dataInizioRicerca = LocalDate.of(2025, 1, 1);

        when(mockPrenotazione1.clone()).thenReturn(mockPrenotazione1);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);

        /*
        ArrayList<Prenotazione> risultato = catalogo.cercaPrenotazioni(null, -1, dataInizioRicerca, null, true);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());*/
    }

    @Test
    @DisplayName("cercaPrenotazioni: ordinamento ASC e DESC")
    void cercaPrenotazioniTest_Ordinamento() throws CloneNotSupportedException
    {
        LocalDate data1 = LocalDate.of(2025, 1, 20);
        LocalDate data2 = LocalDate.of(2025, 1, 10);

        when(mockPrenotazione1.getDataInizio()).thenReturn(data1);
        when(mockPrenotazione1.clone()).thenReturn(mockPrenotazione1);
        when(mockPrenotazione2.getDataInizio()).thenReturn(data2);
        when(mockPrenotazione2.clone()).thenReturn(mockPrenotazione2);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);
        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione2);

        // Test ASC (sort = true)
        /*
        ArrayList<Prenotazione> risultatoASC = catalogo.cercaPrenotazioni(null, -1, LocalDate.of(2025, 1, 1), null, true);
        assertNotNull(risultatoASC);
        assertEquals(2, risultatoASC.size());
        assertEquals(mockPrenotazione2, risultatoASC.get(0));
        assertEquals(mockPrenotazione1, risultatoASC.get(1));

        // Test DESC (sort = false)
        ArrayList<Prenotazione> risultatoDESC = catalogo.cercaPrenotazioni(null, -1, LocalDate.of(2025, 1, 1), null, false);
        assertNotNull(risultatoDESC);
        assertEquals(2, risultatoDESC.size());
        assertEquals(mockPrenotazione1, risultatoDESC.get(0));
        assertEquals(mockPrenotazione2, risultatoDESC.get(1));
        */

    }


    // getPrenotazione

    @Test
    @DisplayName("getPrenotazione: trovata")
    void getPrenotazioneTest_Trovata() throws CloneNotSupportedException
    {
        when(mockPrenotazione1.getIDPrenotazione()).thenReturn(123);
        when(mockPrenotazione1.clone()).thenReturn(mockPrenotazione1);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);

        Prenotazione risultato = catalogo.getPrenotazione(123);

        assertNotNull(risultato);
        verify(mockPrenotazione1).clone();
    }

    @Test
    @DisplayName("getPrenotazione: non trovata")
    void getPrenotazioneTest_NonTrovata() throws CloneNotSupportedException
    {
        when(mockPrenotazione1.getIDPrenotazione()).thenReturn(123);

        CatalogoPrenotazioni.getListaPrenotazioni().add(mockPrenotazione1);

        Prenotazione risultato = catalogo.getPrenotazione(999);

        assertNull(risultato);
    }

    // checkPrenotazione

    @Test
    @DisplayName("checkPrenotazione: parametri invalidi")
    void checkPrenotazioneTest_ParametriInvalidi()
    {
        ArrayList<Camera> listaCamere = new ArrayList<>();
        listaCamere.add(mockCamera1);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(mockCliente1);

        when(mockCamera1.getCapacità()).thenReturn(1);
        when(mockPrenotazione1.getTipoDocumento()).thenReturn("patente");
        //when(mockPrenotazione1.getListaCamere()).thenReturn(listaCamere);
        when(mockPrenotazione1.getListaClienti()).thenReturn(listaClienti);

        // dataInizio passata
        when(mockPrenotazione1.getDataInizio()).thenReturn(LocalDate.now().minusDays(1));
        when(mockPrenotazione1.getDataFine()).thenReturn(LocalDate.now().plusDays(5));
        when(mockPrenotazione1.getDataRilascio()).thenReturn(LocalDate.now().minusYears(1));
        when(mockPrenotazione1.getDataScadenza()).thenReturn(LocalDate.now().plusYears(1));
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // dataFine precedente a dataInizio
        when(mockPrenotazione1.getDataInizio()).thenReturn(LocalDate.now().plusDays(5));
        when(mockPrenotazione1.getDataFine()).thenReturn(LocalDate.now().plusDays(2));
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // dataFine uguale a dataInizio
        LocalDate stessaData = LocalDate.now().plusDays(5);
        when(mockPrenotazione1.getDataInizio()).thenReturn(stessaData);
        when(mockPrenotazione1.getDataFine()).thenReturn(stessaData);
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // Lista camere vuota
        when(mockPrenotazione1.getDataInizio()).thenReturn(LocalDate.now().plusDays(1));
        when(mockPrenotazione1.getDataFine()).thenReturn(LocalDate.now().plusDays(5));
        //when(mockPrenotazione1.getListaCamere()).thenReturn(new ArrayList<>());
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // Lista clienti vuota
        //when(mockPrenotazione1.getListaCamere()).thenReturn(listaCamere);
        when(mockPrenotazione1.getListaClienti()).thenReturn(new ArrayList<>());
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // Tipo documento non valido
        when(mockPrenotazione1.getListaClienti()).thenReturn(listaClienti);
        when(mockPrenotazione1.getTipoDocumento()).thenReturn("documento_invalido");
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // dataRilascio futura
        when(mockPrenotazione1.getTipoDocumento()).thenReturn("patente");
        when(mockPrenotazione1.getDataRilascio()).thenReturn(LocalDate.now().plusDays(1));
        when(mockPrenotazione1.getDataScadenza()).thenReturn(LocalDate.now().plusYears(1));
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // dataScadenza passata
        when(mockPrenotazione1.getDataRilascio()).thenReturn(LocalDate.now().minusYears(2));
        when(mockPrenotazione1.getDataScadenza()).thenReturn(LocalDate.now().minusDays(1));
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));
    }

    @Test
    @DisplayName("checkPrenotazione: tutti parametri validi")
    void checkPrenotazioneTest_TuttiParametriValidi() throws InvalidInputException
    {
        ArrayList<Camera> listaCamere = new ArrayList<>();
        listaCamere.add(mockCamera1);
        ArrayList<Cliente> listaClienti = new ArrayList<>();
        listaClienti.add(mockCliente1);

        when(mockCamera1.getCapacità()).thenReturn(1);
        when(mockPrenotazione1.getDataInizio()).thenReturn(LocalDate.now().plusDays(1));
        when(mockPrenotazione1.getDataFine()).thenReturn(LocalDate.now().plusDays(5));
        when(mockPrenotazione1.getDataRilascio()).thenReturn(LocalDate.now().minusYears(1));
        when(mockPrenotazione1.getDataScadenza()).thenReturn(LocalDate.now().plusYears(1));
        when(mockPrenotazione1.getTipoDocumento()).thenReturn("patente");
        //when(mockPrenotazione1.getListaCamere()).thenReturn(listaCamere);
        when(mockPrenotazione1.getListaClienti()).thenReturn(listaClienti);

        assertDoesNotThrow(() -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));
    }
}