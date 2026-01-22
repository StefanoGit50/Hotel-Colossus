//package it.unisa.Storage.Testing.PrenotazioneDAO;
//
//import it.unisa.Common.*;
//import it.unisa.Server.persistent.util.Stato;
//import it.unisa.Storage.DAO.PrenotazioneDAO;
//import javafx.fxml.LoadListener;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//public class PrenotazioneDAOTesting{
//
//    private int idPrenotazione;
//    private LocalDate dataCreazionePrenotazione;
//    private LocalDate dataInizio;
//    private LocalDate dataFine;
//    private Trattamento trattamento;
//    private String tipoDocumento;
//    private LocalDate dataRilascio;
//    private LocalDate dataScadenza;
//    private String intestatario;
//    private String noteAggiuntive;
//    private int numeroDocumento;
//    private ArrayList<Camera> listaCamera;
//    private ArrayList<Servizio> listaServizi;
//    private ArrayList<Cliente> listaClienti;
//    private boolean statoPrenotazione;
//    private PrenotazioneDAO prenotazione;
//
//    @BeforeTest
//    public void initPrenotazione(){
//        prenotazione = new PrenotazioneDAO();
//    }
//    @AfterTest
//    public void postPrenotazione(){
//        prenotazione = null;
//    }
//
//    @Test
//    public void doSaveSuccessTesting(){
//        idPrenotazione = 112;
//        dataCreazionePrenotazione = LocalDate.of(2018,12,30);
//        dataInizio = LocalDate.of(2020,12,30);
//        dataFine = LocalDate.of(2024,11,20);
//        trattamento = new Trattamento("",30);
//        tipoDocumento = "Patente";
//        dataRilascio = LocalDate.of(2001,5,12);
//        dataScadenza = LocalDate.of(2030 , 10 , 20);
//        intestatario = "Leonardo di caprio";
//        noteAggiuntive = "Leonardo di caprio in realtà è chuck noris";
//        numeroDocumento = 11;
//        listaCamera = new ArrayList<>();
//        listaClienti = new ArrayList<>();
//        listaServizi = new ArrayList<>();
//        listaClienti.add(new Cliente("Samuele","Valiante","Italiana","Salerno","Montoro","corso armando diaz",10,84085,"3249657893","Maschio",LocalDate.of(2004,12,16),"VLNSML04T16H703Y","Samuele.Valiante@gmail.com","Post"));
//        listaCamera.add(new Camera(130, Stato.Occupata,2,50,"il cliente è stronzo"));
//        listaServizi.add(new Servizio("palestra",20));
//        try {
//            //prenotazione.doSave(new Prenotazione(idPrenotazione,dataCreazionePrenotazione , dataInizio , dataFine , trattamento , tipoDocumento , dataRilascio , dataScadenza , ));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
