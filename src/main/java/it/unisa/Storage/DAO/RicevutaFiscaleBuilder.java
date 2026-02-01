package it.unisa.Storage.DAO;

import it.unisa.Common.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RicevutaFiscaleBuilder{
    private final Map<String, Cliente> clientiMap = new LinkedHashMap<>();
    private final Map<Integer, Camera> camereMap = new LinkedHashMap<>();
    private final Map<String, Servizio> servizioMap = new LinkedHashMap<>();

    private final RicevutaFiscale ricevutaFiscale;

    public RicevutaFiscaleBuilder(ResultSet resultSet)throws SQLException{
        this.ricevutaFiscale = new RicevutaFiscale();
        ricevutaFiscale.setMetodoPagamento(resultSet.getString("metodoPagamento"));
        ricevutaFiscale.setIDRicevutaFiscale(resultSet.getInt("IDRicevuta"));
        ricevutaFiscale.setPrenotazione(new Prenotazione());
        ricevutaFiscale.getPrenotazione().setIDPrenotazione(resultSet.getInt("IDPrenotazione"));
        ricevutaFiscale.setDataEmissione(resultSet.getDate("DataEmissione").toLocalDate());
        ricevutaFiscale.setDataPrenotazione(resultSet.getDate("DataPrenotazione").toLocalDate());
        ricevutaFiscale.setTipoTrattamento(resultSet.getString("TipoTrattamento"));
        ricevutaFiscale.setPrezzoTrattamento(resultSet.getDouble("PrezzoTrattamento"));
    }

    public void addCamere(ResultSet resultSet) throws SQLException {
        int numeroCamera = resultSet.getInt("NumeroCamera");
        if(!camereMap.containsKey(numeroCamera)){
            Camera camera = new Camera();
            camera.setNumeroCamera(numeroCamera);
            camera.setPrezzoCamera(resultSet.getDouble("PrezzoCamera"));
            camera.setStatoCamera(null);
            camera.setNoteCamera(null);
            camereMap.put(numeroCamera,camera);
        }
    }

    public void addServizi(ResultSet resultSet) throws SQLException {
        String nomeServizio = resultSet.getString("NomeServizio");
        if(!servizioMap.containsKey(nomeServizio)){
            Servizio servizio = new Servizio();
            servizio.setNome(resultSet.getString("NomeServizio"));
            servizio.setPrezzo(resultSet.getDouble("PrezzoServizio"));
            servizioMap.put(nomeServizio,servizio);
        }
    }

    public void addClienti(ResultSet resultSet) throws SQLException {
        String cf = resultSet.getString("CFCliente");
        if(!servizioMap.containsKey(cf)){
            Cliente cliente = new Cliente();
            cliente.setNome(resultSet.getString("NomeCliente"));
            cliente.setCognome(resultSet.getString("CognomeCliente"));
            clientiMap.put(cf,cliente);
        }
    }

    public void totalePrezzo(){
        ArrayList<Camera> cameras = (ArrayList<Camera>) camereMap.values();
        double totale = 0;
        for(Camera camera: cameras){
            totale += camera.getPrezzoCamera();
        }

        ArrayList<Servizio> servizios = (ArrayList<Servizio>) servizioMap.values();

        for(Servizio servizio: servizios){
            totale += servizio.getPrezzo();
        }

        ricevutaFiscale.setTotale(totale);
    }

    public RicevutaFiscale build(){
        ricevutaFiscale.setClientes(new ArrayList<>());
        ricevutaFiscale.setCameras(new ArrayList<>());
        ricevutaFiscale.setServizios(new ArrayList<>());
        return ricevutaFiscale;
    }

}
