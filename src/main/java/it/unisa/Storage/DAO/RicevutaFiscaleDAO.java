package it.unisa.Storage.DAO;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class RicevutaFiscaleDAO implements FrontDeskStorage<RicevutaFiscale> {

    private static final String TABLE_NAME = "RicevutaFiscale";

    private static final String[] attributes = {"IDRicevutaFiscale", "IDPrenotazione", "Totale", "DataEmissione"};

    @Override
    public void doSave(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String insertSQL = "insert into " + RicevutaFiscaleDAO.TABLE_NAME +
                " (IDRicevutaFiscale, IDPrenotazione, Totale, DataEmissione) VALUES (?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(insertSQL);
            ps.setInt(1, o.getIDRicevutaFiscale());
            ps.setInt(2, o.getIDPrenotazione());
            ps.setDouble(3, o.getTotale());
            ps.setDate(4, Date.valueOf(o.getDataEmissione()) );

            ps.executeUpdate();
        } finally {
            if (ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }
    }

    @Override
    public void doDelete(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String deleteSQL = "delete from " + RicevutaFiscaleDAO.TABLE_NAME +
                " where IDRicevutaFiscale = ? and IDPrenotazione = ?";

        try {
            ps = conn.prepareStatement(deleteSQL);
            ps.setInt(1, o.getIDRicevutaFiscale());
            ps.setInt(2, o.getIDPrenotazione());

            ps.executeUpdate();
        } finally {
            if (ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }
    }

    /**
     * NON SUPPORTATO
     */
    public RicevutaFiscale doRetriveByKey(Object oggetto) throws SQLException {
        // Attaccati al tram
        return null;
    }

    public RicevutaFiscale doRetriveByKey(ArrayList<Integer> keys) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;
        RicevutaFiscale ricevuta = null;

        String selectSQL = "select * FROM " + RicevutaFiscaleDAO.TABLE_NAME +
                " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?";

        try {
            ps = conn.prepareStatement(selectSQL);
            ps.setInt(1, keys.get(0));
            ps.setInt(2, keys.get(1));
            rs = ps.executeQuery();

            if (rs.next()) {
                ricevuta = new RicevutaFiscale();
                ricevuta.setIDRicevutaFiscale(rs.getInt("IDPrenotazione"));
                ricevuta.setIDPrenotazione(rs.getInt("IDRicevutaFiscale"));
                ricevuta.setTotale(rs.getDouble("Totale"));
                ricevuta.setDataEmissione(rs.getDate("DataEmissione").toLocalDate());
            }

        } finally {
            if (ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }

        if  (ricevuta == null)
            throw new NoSuchElementException("prenotazione non trovata");

        return ricevuta;
    }

    public Collection<RicevutaFiscale> doRetriveAll(String order) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectSQL = "select * FROM" + RicevutaFiscaleDAO.TABLE_NAME;

        ArrayList<RicevutaFiscale> results = new ArrayList<>();

        if (order != null && !order.trim().isEmpty() && DaoUtils.checkWhitelist(attributes, order)) {
            selectSQL += " ORDER BY " + order;
        }

        try {
            ps = conn.prepareStatement(selectSQL);
            rs = ps.executeQuery();

            RicevutaFiscale ricevuta;
            while (rs.next()) {
                ricevuta = new RicevutaFiscale();
                ricevuta.setIDRicevutaFiscale(rs.getInt("IDPrenotazione"));
                ricevuta.setIDPrenotazione(rs.getInt("IDRicevutaFiscale"));
                ricevuta.setTotale(rs.getDouble("Totale"));
                ricevuta.setDataEmissione(rs.getDate("DataEmissione").toLocalDate());
                results.add(ricevuta);

                results.add(ricevuta);
            }

        } finally {
            if (ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }

        return results;
    }
    
}
