package Server.persistent.obj.catalogues;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
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
public class CatalogoClientiTester
{

    @Mock
    Cliente mockCliente1;

    @Mock
    Cliente mockCliente2;

    @Mock
    Cliente mockCliente3;

    @InjectMocks
    private CatalogoClienti catalogo;

    @BeforeEach
    void setUp()
    {
        catalogo = new CatalogoClienti();
        CatalogoClienti.getListaClienti().clear();
        CatalogoClienti.getListaClientiBannati().clear();
    }

    // test per cerca clienti

    @Test
    @DisplayName("cercaClienti: tutti parametri null")
    void cercaClientiTest_TuttiParametriNull() throws CloneNotSupportedException
    {
        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, null, null);
        assertNull(risultato);
    }

    @Test
    @DisplayName("cercaClienti: almeno un parametro non null con lista vuota")
    void cercaClientiTest_AlmenoUnParametroNonNull_ListaVuota() throws CloneNotSupportedException
    {
        ArrayList<Cliente> risultato = catalogo.cercaClienti("Mario", null, null, null, null);
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametri trovati")
    void cercaClientiTest_ParametriTrovati() throws CloneNotSupportedException
    {
        // Setup mock per tutti i parametri
        when(mockCliente1.getNome()).thenReturn("Mario");
        when(mockCliente1.getCognome()).thenReturn("Rossi");
        when(mockCliente1.getNazionalita()).thenReturn("Italiana");
        when(mockCliente1.getSesso()).thenReturn("M");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        // Test nome trovato
        ArrayList<Cliente> risultatoNome = catalogo.cercaClienti("Mario", null, null, null, null);
        assertNotNull(risultatoNome);
        assertEquals(1, risultatoNome.size());

        // Test cognome trovato
        ArrayList<Cliente> risultatoCognome = catalogo.cercaClienti(null, "Rossi", null, null, null);
        assertNotNull(risultatoCognome);
        assertEquals(1, risultatoCognome.size());

        // Test nazionalità trovata
        ArrayList<Cliente> risultatoNazionalita = catalogo.cercaClienti(null, null, "Italiana", null, null);
        assertNotNull(risultatoNazionalita);
        assertEquals(1, risultatoNazionalita.size());

        // Test sesso trovato
        ArrayList<Cliente> risultatoSesso = catalogo.cercaClienti(null, null, null, null, "M");
        assertNotNull(risultatoSesso);
        assertEquals(1, risultatoSesso.size());
    }

    @Test
    @DisplayName("cercaClienti: parametri non trovati")
    void cercaClientiTest_ParametriNonTrovati() throws CloneNotSupportedException
    {
        // Setup mock con valori diversi da quelli cercati
        when(mockCliente1.getNome()).thenReturn("Luigi");
        when(mockCliente1.getCognome()).thenReturn("Verdi");
        when(mockCliente1.getNazionalita()).thenReturn("Francese");
        when(mockCliente1.getSesso()).thenReturn("F");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        // Test nome non trovato
        ArrayList<Cliente> risultatoNome = catalogo.cercaClienti("Mario", null, null, null, null);
        assertNotNull(risultatoNome);
        assertTrue(risultatoNome.isEmpty());

        // Test cognome non trovato
        ArrayList<Cliente> risultatoCognome = catalogo.cercaClienti(null, "Rossi", null, null, null);
        assertNotNull(risultatoCognome);
        assertTrue(risultatoCognome.isEmpty());

        // Test nazionalità non trovata
        ArrayList<Cliente> risultatoNazionalita = catalogo.cercaClienti(null, null, "Italiana", null, null);
        assertNotNull(risultatoNazionalita);
        assertTrue(risultatoNazionalita.isEmpty());

        // Test sesso non trovato
        ArrayList<Cliente> risultatoSesso = catalogo.cercaClienti(null, null, null, null, "M");
        assertNotNull(risultatoSesso);
        assertTrue(risultatoSesso.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: dataNascita prima trovato, dopo e uguale non trovati")
    void cercaClientiTest_DataNascita() throws CloneNotSupportedException
    {
        LocalDate dataCerca = LocalDate.of(1990, 1, 1);

        // Test before (trovato)
        LocalDate dataBefore = LocalDate.of(1985, 1, 1);
        when(mockCliente1.getDataNascita()).thenReturn(dataBefore);
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultatoBefore = catalogo.cercaClienti(null, null, null, dataCerca, null);
        assertNotNull(risultatoBefore);
        assertEquals(1, risultatoBefore.size());

        // Test after (non trovato)
        LocalDate dataAfter = LocalDate.of(1995, 1, 1);
        when(mockCliente2.getDataNascita()).thenReturn(dataAfter);
        CatalogoClienti.getListaClienti().clear();
        CatalogoClienti.getListaClienti().add(mockCliente2);

        ArrayList<Cliente> risultatoAfter = catalogo.cercaClienti(null, null, null, dataCerca, null);
        assertNotNull(risultatoAfter);
        assertTrue(risultatoAfter.isEmpty());

        // Test equal (non trovato)
        LocalDate dataEqual = LocalDate.of(1990, 1, 1);
        when(mockCliente3.getDataNascita()).thenReturn(dataEqual);
        CatalogoClienti.getListaClienti().clear();
        CatalogoClienti.getListaClienti().add(mockCliente3);

        ArrayList<Cliente> risultatoEqual = catalogo.cercaClienti(null, null, null, dataCerca, null);
        assertNotNull(risultatoEqual);
        assertTrue(risultatoEqual.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: for con più elementi")
    void cercaClientiTest_ForMultipliElementi() throws CloneNotSupportedException
    {
        when(mockCliente1.getNome()).thenReturn("Mario");
        when(mockCliente1.clone()).thenReturn(mockCliente1);
        when(mockCliente2.getNome()).thenReturn("Luigi");
        when(mockCliente3.getNome()).thenReturn("Mario");
        when(mockCliente3.clone()).thenReturn(mockCliente3);

        CatalogoClienti.getListaClienti().add(mockCliente1);
        CatalogoClienti.getListaClienti().add(mockCliente2);
        CatalogoClienti.getListaClienti().add(mockCliente3);

        ArrayList<Cliente> risultato = catalogo.cercaClienti("Mario", null, null, null, null);

        assertNotNull(risultato);
        assertEquals(2, risultato.size());
    }

    @Test
    @DisplayName("getCliente: trovato in listaClienti")
    void getClienteTest_TrovatoInListaClienti() throws CloneNotSupportedException
    {
        when(mockCliente1.getCf()).thenReturn("RSSMRA90E15F839X");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        Cliente risultato = catalogo.getCliente("RSSMRA90E15F839X");

        assertNotNull(risultato);
        verify(mockCliente1).clone();
    }

    @Test
    @DisplayName("getCliente: trovato in listaClientiBannati")
    void getClienteTest_TrovatoInListaBannati() throws CloneNotSupportedException
    {
        when(mockCliente1.getCf()).thenReturn("RSSMRA90E15F839X");
        when(mockCliente2.getCf()).thenReturn("VRDNNA85C20F839Y");
        when(mockCliente2.clone()).thenReturn(mockCliente2);

        CatalogoClienti.getListaClienti().add(mockCliente1);
        CatalogoClienti.getListaClientiBannati().add(mockCliente2);

        Cliente risultato = catalogo.getCliente("VRDNNA85C20F839Y");

        assertNotNull(risultato);
        verify(mockCliente2).clone();
    }

    @Test
    @DisplayName("getCliente: non trovato")
    void getClienteTest_NonTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getCf()).thenReturn("RSSMRA90E15F839X");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        Cliente risultato = catalogo.getCliente("CFINESISTENTE123");

        assertNull(risultato);
    }

    @Test
    @DisplayName("checkCliente: tutti parametri null o vuoti")
    void checkClienteTest_TuttiParametriNullOVuoti()
    {
        // Tutti null
        assertThrows(NullPointerException.class, () ->
                CatalogoClienti.checkCliente(null, null, null, null, null));

        // Tutti blank
        assertThrows(NullPointerException.class, () ->
                CatalogoClienti.checkCliente("", "", "", null, null));
    }

    @Test
    @DisplayName("checkCliente: parametri invalidi")
    void checkClienteTest_ParametriStringaInvalidi()
    {
        // Nome invalido (numeri)
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente("Mario123", null, null, null, null));

        // Nome troppo lungo
        String nomeLungo = "a".repeat(50);
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(nomeLungo, null, null, null, null));

        // Cognome invalido (numeri)
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(null, "Rossi123", null, null, null));

        // Cognome invalido (caratteri speciali)
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(null, "Rossi@", null, null, null));

        // Nazionalità invalida (numeri)
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(null, null, "Italiana123", null, null));
    }

    @Test
    @DisplayName("checkCliente: dataNascita futura o uguale a oggi (invalida)")
    void checkClienteTest_DataNascitaInvalida()
    {
        // Data futura
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(null, null, null, dataFutura, null));

        // Data uguale a oggi
        LocalDate oggi = LocalDate.now();
        assertThrows(InvalidInputException.class, () ->
                CatalogoClienti.checkCliente(null, null, null, oggi, null));
    }

    @Test
    @DisplayName("checkCliente: dataNascita passata (valida)")
    void checkClienteTest_DataNascitaPassata() throws InvalidInputException
    {
        LocalDate dataPassata = LocalDate.now().minusYears(25);
        assertDoesNotThrow(() ->
                CatalogoClienti.checkCliente(null, null, null, dataPassata, null));
    }

    @Test
    @DisplayName("checkCliente: blacklisted fornito")
    void checkClienteTest_BlacklistedFornito() throws InvalidInputException
    {
        // Blacklisted true
        assertDoesNotThrow(() ->
                CatalogoClienti.checkCliente(null, null, null, null, true));

        // Blacklisted false
        assertDoesNotThrow(() ->
                CatalogoClienti.checkCliente(null, null, null, null, false));
    }

    @Test
    @DisplayName("checkCliente: tutti parametri validi")
    void checkClienteTest_TuttiParametriValidi() throws InvalidInputException
    {
        LocalDate dataPassata = LocalDate.now().minusYears(30);
        assertDoesNotThrow(() ->
                CatalogoClienti.checkCliente("Mario", "Rossi", "Italiana", dataPassata, false));
    }
}