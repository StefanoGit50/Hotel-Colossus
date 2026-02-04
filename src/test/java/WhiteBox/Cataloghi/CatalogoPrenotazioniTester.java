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
        assertThrows(InvalidInputException.class, () -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));

        // Lista clienti vuota
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
        when(mockPrenotazione1.getListaClienti()).thenReturn(listaClienti);

        assertDoesNotThrow(() -> CatalogoPrenotazioni.checkPrenotazione(mockPrenotazione1));
    }
}