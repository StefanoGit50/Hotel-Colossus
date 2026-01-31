package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.ConnectionStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PrenotazioneBuilder{

    private final Prenotazione prenotazione;
    // Map per evitare duplicati (lookup O(1) invece di O(n))
    private final Map<String, Cliente> clientiMap = new LinkedHashMap<>();
    private final Map<Integer, Camera> camereMap = new LinkedHashMap<>();
    private final Map<String, Servizio> serviziMap = new LinkedHashMap<>();

    /**
     * COSTRUTTORE - chiamato alla PRIMA riga di una prenotazione
     */
    public  PrenotazioneBuilder(ResultSet rs) throws SQLException {
        this.prenotazione = new Prenotazione();

        // Imposta i campi BASE (questi sono uguali in tutte le righe)
        prenotazione.setCodicePrenotazione(rs.getInt("IDPrenotazione"));
        prenotazione.setIntestatario(rs.getString("Intestatario"));
        prenotazione.setDataInizio(rs.getDate("DataArrivoCliente").toLocalDate());
        // ... altri campi base
        // Trattamento (uguale per tutte le righe)
        Trattamento trattamento = new Trattamento(
                rs.getString("TrattamentoNome"),
                rs.getDouble("TrattamentoPrezzo")
        );
        prenotazione.setTrattamento(trattamento);
    }

    /**
     * Aggiungi un cliente (se non c'è già)
     */
    public void addCliente(ResultSet rs) throws SQLException {
        String cf = rs.getString("CF");
        Connection connection = ConnectionStorage.getConnection();
        String q = "Select hot.camera2.* FROM (hot.associato_a2 join hot.camera2 " +
                "on associato_a2.NumeroCamera = camera2.NumeroCamera) where CF = ?";
        PreparedStatement p = connection.prepareStatement(q);
        // Controlla se il cliente è già stato aggiunto

        ResultSet resultSet = p.executeQuery();
        Camera camera = new Camera();
        if(resultSet.next()){
            camera.setCapacità(resultSet.getInt("NumeroMaxOcc"));
            camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
            camera.setNoteCamera(resultSet.getString("NoteCamera"));
            camera.setStatoCamera(Stato.valueOf(resultSet.getString("Stato")));
            camera.setPrezzoCamera(resultSet.getDouble("Prezzo"));
        }

        if (!clientiMap.containsKey(cf)){
            // Cliente nuovo - crealo e aggiungilo
            Cliente cliente = new Cliente();
            cliente.setCf(cf);
            cliente.setNome(rs.getString("ClienteNome"));
            cliente.setCognome(rs.getString("ClienteCognome"));
            cliente.setDataNascita(rs.getDate("DataDiNascita").toLocalDate());
            cliente.setBlacklisted(rs.getBoolean("IsBackListed"));
            cliente.setNumeroTelefono(rs.getString("telefono"));
            cliente.setEmail(rs.getString("Email"));
            cliente.setProvincia(rs.getString("provincia"));
            cliente.setVia(rs.getString("via"));
            cliente.setNumeroCivico(rs.getInt("civico"));
            cliente.setCognome(rs.getString("comune"));
            cliente.setCAP(Integer.parseInt(rs.getString("Cap")));
            cliente.setSesso(rs.getString("Sesso"));
            cliente.setCittadinanza(rs.getString("Cittadinanza"));
            cliente.setNazionalita(rs.getString("Nazionalità"));
            cliente.setCamere(camera);
            clientiMap.put(cf, cliente);
        }
        // Se il cliente c'è già, NON fare nulla (evita duplicati)
    }

    /**
     * Aggiungi una camera (se non c'è già)
     */
    public void addCamera(ResultSet rs) throws SQLException {
        Integer numeroCamera = rs.getInt("NumeroCamera");

        if (!camereMap.containsKey(numeroCamera)) {
            Camera camera = new Camera();
            camera.setNumeroCamera(numeroCamera);
            camera.setPrezzoCamera(rs.getDouble("CameraPrezzo"));
            // ... altri campi

            camereMap.put(numeroCamera, camera);
        }
    }

    /**
     * Aggiungi un servizio (se non c'è già)
     */
    public void addServizio(ResultSet rs) throws SQLException {
        String nomeServizio = rs.getString("ServizioNome");

        // Servizio può essere NULL (LEFT JOIN)
        if (nomeServizio != null && !serviziMap.containsKey(nomeServizio)) {
            Servizio servizio = new Servizio();
            servizio.setNome(nomeServizio);
            servizio.setPrezzo(rs.getDouble("ServizioPrezzo"));

            serviziMap.put(nomeServizio, servizio);
        }
    }

    /**
     * COSTRUISCI l'oggetto finale
     * Converte le Map in Liste e restituisce la Prenotazione completa
     */
    public Prenotazione build() {
        // Converti Map → List
        prenotazione.setListaClienti(new ArrayList<>(clientiMap.values()));
        prenotazione.setListaCamere(new ArrayList<>(camereMap.values()));
        prenotazione.setListaServizi(new ArrayList<>(serviziMap.values()));

        return prenotazione;
    }
}
