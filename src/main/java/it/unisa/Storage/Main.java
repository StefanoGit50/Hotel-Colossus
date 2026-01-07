package it.unisa.Storage;

import com.mysql.cj.protocol.a.SqlDateValueEncoder;
import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Main{
    public static void main(String[] args){
     /*   CameraDAO cameraDAO = new CameraDAO();
        Camera camera = new Camera(
                101,       // numeroCamera
                Stato.Libera,           // statoCamera
                2,                      // capacità (numeroMaxOccupanti)
                95.50,                  // prezzoCamera
                "Check-in dalle 14:00"  // noteCamera
        );
        try{
            cameraDAO.doSave(camera);
        }catch(SQLException sqlException){
            sqlException.getMessage();
            sqlException.getErrorCode();
            sqlException.getCause();
        }
        Camera camera1 = new Camera(
                102,                    // numeroCamera
                Stato.Libera,           // statoCamera
                3,                      // capacità (numeroMaxOccupanti)
                100.50,                  // prezzoCamera
                "Check-in dalle 14:00"  // noteCamera
        );
        try{
            cameraDAO.doSave(camera1);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        Camera camera2 = new Camera(
                103,                    // numeroCamera
                Stato.Libera,           // statoCamera
                1,                      // capacità (numeroMaxOccupanti)
                60.4,                  // prezzoCamera
                "Check-in dalle 14:00"  // noteCamera
        );
        try{
            cameraDAO.doSave(camera2);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        Cliente c1 = new Cliente(
                "Mario",
                "Rossi",
                "Italiana",
                "SA",
                "Salerno",
                "Via Roma",
                10,
                84100,
                 "333123456728934",
                "M",
                LocalDate.of(1990, 5, 12),
                "RSSMGA90E12H703X",
                "mario.rossi@email.it",
                "Carta di credito"
        );

        Cliente c2 = new Cliente(
                "Anna",
                "Bianchi",
                "Italiana",
                "NA",
                "Napoli",
                "Via Toledo",
                22,
                80100,
                "339987654312345",
                "F",
                LocalDate.of(1992, 8, 20),
                "BNCNNA92M60F839Y",
                "anna.bianchi@email.it",
                "Contanti"
        );

        Servizio spa = new Servizio("SPA", 30.00);
        Servizio colazione = new Servizio("Colazione", 10.00);

        /*Trattamento pensioneCompleta = new Trattamento(
                "Pensione Completa",
                60.00
        );

        ArrayList<Cliente> clienti = new ArrayList<>();
        clienti.add(c1);
        clienti.add(c2);

        ArrayList<Camera> camere = new ArrayList<>();
        camere.add(camera);
        camere.add(camera1);

        ArrayList<Servizio> servizi = new ArrayList<>();
        servizi.add(spa);
        servizi.add(colazione);

        Prenotazione prenotazione = new Prenotazione(
                1,                                  // IDPrenotazione
                LocalDate.now(),                    // DataPrenotazione
                LocalDate.of(2026, 2, 10),           // DataArrivo
                LocalDate.of(2026, 2, 15),           // DataPartenza
              //  pensioneCompleta,                   // Trattamento
                "Carta d'identità",                 // Tipo documento
                LocalDate.of(2030, 12, 31),          // Data rilascio
                LocalDate.of(2030, 12, 31),          // Data scadenza
                "Mario Rossi",                      // Intestatario
                "Cliente celiaco",                  // Note
                camere,
                servizi,
                clienti,
                123456789                           // Numero documento
        );

        TrattamentoDAO trattamentoDAO = new TrattamentoDAO();

        try{
          //  trattamentoDAO.doSave(pensioneCompleta);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        ServizioDAO servizioDAO = new ServizioDAO();

        try{
            servizioDAO.doSave(spa);
            servizioDAO.doSave(colazione);
        }catch (SQLException sqlException){
            sqlException.getMessage();
            sqlException.getErrorCode();
            sqlException.getCause();
        }

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try{
            prenotazioneDAO.doSave(prenotazione);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
        try{
            impiegatoDAO.doSave( new Impiegato(
                    "mrossi",
                    "hashedPwd123",
                    "Mario",
                    "Rossi",
                    "M",
                    "Carta d'identità",
                    "AA1234567",
                    84084,
                    "Via Roma",
                    "SA",
                    "Fisciano",
                    10,
                    "AASMRA90A01H703X",
                    "3331234567",
                    Ruolo.FrontDesk,
                    1500.00,
                    LocalDate.of(2022, 3, 1),
                    LocalDate.of(2020, 2, 15),
                    "mario.rossi@hotel.it",
                    "Italiana",
                    LocalDate.of(2030, 2, 15)
            ));
            impiegatoDAO.doSave(new Impiegato(
                    "lbianchi",
                    "hashedPwd456",
                    "Luca",
                    "Bianchi",
                    "M",
                    "Passaporto",
                    "YA9876543",
                    80100,
                    "Via Toledo",
                    "NA",
                    "Napoli",
                    25,
                    "BNCLCU85C10F839Y",
                    "3399876543",
                    Ruolo.Manager,
                    2500.00,
                    LocalDate.of(2019, 6, 10),
                    LocalDate.of(2018, 5, 20),
                    "luca.bianchi@hotel.it",
                    "Italiana",
                    LocalDate.of(2028, 5, 20)
            ));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        Camera camera3 = null;
        try{
          camera3 = cameraDAO.doRetriveByKey(101);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        System.out.println(camera3);
        Servizio servizio = null;
        try{
            servizio = servizioDAO.doRetriveByKey("SPA");
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        System.out.println(servizio);
        Trattamento trattamento = null;

        try{
            trattamento = trattamentoDAO.doRetriveByKey("Pensione Completa");
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        System.out.println(trattamento);
        Cliente cliente2 = null;
        try{
            ClienteDAO clienteDAO = new ClienteDAO();
            cliente2 = clienteDAO.doRetriveByKey("BNCNNA92M60F839Y");
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        IO.println(cliente2);
        Prenotazione prenotazione1 = null;
        try{
            prenotazione1 = prenotazioneDAO.doRetriveByKey(1);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        Impiegato impiegato = null;
        try{
           impiegato = impiegatoDAO.doRetriveByKey("AASMRA90A01H703X");
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        System.out.println(impiegato);
        IO.println(prenotazione1);
        ClienteDAO clienteDAO = new ClienteDAO();

        IO.println("DO Retrive ALLLLLLLLLLLLLLLLLLLL:");

        try{
            ArrayList<Cliente> clientes = new ArrayList<>(clienteDAO.doRetriveAll("decrescente"));
            System.out.println(clientes);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            ArrayList<Impiegato> impiegatoes = new ArrayList<>(impiegatoDAO.doRetriveAll("decrescente"));
            System.out.println(impiegatoes);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            ArrayList<Camera> cameras = new ArrayList<>(cameraDAO.doRetriveAll("decrescente"));
            System.out.println(cameras);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            ArrayList<Prenotazione> prenotaziones = new ArrayList<>(prenotazioneDAO.doRetriveAll("IDPrenotazione"));
            System.out.println(prenotaziones);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            ArrayList<Servizio> servizios = new ArrayList<>(servizioDAO.doRetriveAll("Nome"));
            System.out.println(servizios);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            ArrayList<Trattamento> trattamentos = new ArrayList<>(trattamentoDAO.doRetriveAll("Nome"));
            System.out.println(trattamentos);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            cameraDAO.doDelete(camera);
            cameraDAO.doDelete(camera1);
            cameraDAO.doDelete(camera3);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            clienteDAO.doDelete(c1);
            clienteDAO.doDelete(c2);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            prenotazioneDAO.doDelete(prenotazione);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            servizioDAO.doDelete(spa);
            servizioDAO.doDelete(colazione);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
           // trattamentoDAO.doDelete(pensioneCompleta);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        try{
            impiegatoDAO.doDelete(new Impiegato("lbianchi",
                    "hashedPwd456",
                    "Luca",
                    "Bianchi",
                    "M",
                    "Passaporto",
                    "YA9876543",
                    80100,
                    "Via Toledo",
                    "NA",
                    "Napoli",
                    25,
                    "BNCLCU85C10F839Y",
                    "3399876543",
                    Ruolo.Manager,
                    2500.00,
                    LocalDate.of(2019, 6, 10),
                    LocalDate.of(2018, 5, 20),
                    "luca.bianchi@hotel.it",
                    "Italiana",
                    LocalDate.of(2028, 5, 20)));
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }*/
        CameraDAO cameraDAO = new CameraDAO();
        ArrayList<Camera> cameras;
        ArrayList<Servizio> servizios = null;
        ArrayList<Cliente> clientes = null;
        try {
            cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll("decrescente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ServizioDAO servizioDAO = new ServizioDAO();
        try{
            servizios = (ArrayList<Servizio>) servizioDAO.doRetriveAll("decrescente");
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        ClienteDAO clienteDAO = new ClienteDAO();
        try{
            clientes = (ArrayList<Cliente>) clienteDAO.doRetriveAll("decrescente");
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        TrattamentoDAO trattamentoDAO = new TrattamentoDAO();
        try {
            Trattamento trattamento = trattamentoDAO.doRetriveByAttribute("Nome","Mezza Pensione");
            System.out.println(t)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
