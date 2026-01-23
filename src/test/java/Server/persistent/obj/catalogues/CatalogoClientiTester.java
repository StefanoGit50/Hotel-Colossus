package Server.persistent.obj.catalogues;

import it.unisa.Common.Cliente;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
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

    @InjectMocks
    private CatalogoClienti catalogo;

    @BeforeEach
    void setUp() {
        catalogo = new CatalogoClienti();
        // Pulisce le liste statiche prima di ogni test
        CatalogoClienti.getListaClienti().clear();
        CatalogoClienti.getListaClientiBannati().clear();
    }

    @Test
    @DisplayName("cercaClienti: tutti parametri null")
    void cercaClientiTest_TuttiParametriNull() throws CloneNotSupportedException
    {
        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, null, null);

        assertNull(risultato);
    }

    @Test
    @DisplayName("cercaClienti: almeno un parametro non null, lista vuota")
    void cercaClientiTest_AlmenoUnParametroNonNull_ListaVuota() throws CloneNotSupportedException
    {
        ArrayList<Cliente> risultato = catalogo.cercaClienti("Mario", null, null, null, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro nome trovato")
    void cercaClientiTest_ParametroNomeTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getNome()).thenReturn("Mario");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti("Mario", null, null, null, null);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("cercaClienti: parametro nome non trovato")
    void cercaClientiTest_ParametroNomeNonTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getNome()).thenReturn("Luigi");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti("Mario", null, null, null, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro cognome trovato")
    void cercaClientiTest_ParametroCognomeTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getCognome()).thenReturn("Rossi");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, "Rossi", null, null, null);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("cercaClienti: parametro cognome non trovato")
    void cercaClientiTest_ParametroCognomeNonTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getCognome()).thenReturn("Verdi");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, "Rossi", null, null, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro sesso trovato")
    void cercaClientiTest_ParametroSessoTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getSesso()).thenReturn("M");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, null, "M");

        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("cercaClienti: parametro sesso non trovato")
    void cercaClientiTest_ParametroSessoNonTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getSesso()).thenReturn("F");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, null, "M");

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro dataNascita non trovato (dopo)")
    void cercaClientiTest_ParametroDataNascitaNonTrovato_Dopo() throws CloneNotSupportedException
    {
        LocalDate dataCerca = LocalDate.of(1990, 1, 1);
        LocalDate dataCliente = LocalDate.of(1995, 1, 1);

        when(mockCliente1.getDataNascita()).thenReturn(dataCliente);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, dataCerca, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro dataNascita non match (uguale)")
    void cercaClientiTest_ParametroDataNascitaNonTrovato_Uguale() throws CloneNotSupportedException
    {
        LocalDate dataCerca = LocalDate.of(1990, 1, 1);
        LocalDate dataCliente = LocalDate.of(1990, 1, 1);

        when(mockCliente1.getDataNascita()).thenReturn(dataCliente);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, dataCerca, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("cercaClienti: parametro dataNascita trovato (prima)")
    void cercaClientiTest_ParametroDataNascitaTrovato_prima() throws CloneNotSupportedException
    {
        LocalDate dataCerca = LocalDate.of(1990, 1, 1);
        LocalDate dataCliente = LocalDate.of(1985, 1, 1);

        when(mockCliente1.getDataNascita()).thenReturn(dataCliente);
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, null, dataCerca, null);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("cercaClienti: parametro nazionalità trovato")
    void cercaClientiTest_ParametroNazionalitaTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getNazionalita()).thenReturn("Italiana");
        when(mockCliente1.clone()).thenReturn(mockCliente1);

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, "Italiana", null, null);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("cercaClienti: parametro nazionalità non trovato")
    void cercaClientiTest_ParametroNazionalitaNonTrovato() throws CloneNotSupportedException
    {
        when(mockCliente1.getNazionalita()).thenReturn("Francese");

        CatalogoClienti.getListaClienti().add(mockCliente1);

        ArrayList<Cliente> risultato = catalogo.cercaClienti(null, null, "Italiana", null, null);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
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
    @DisplayName("getCliente: non trovato in listaClienti, trovato in listaClientiBannati")
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
}