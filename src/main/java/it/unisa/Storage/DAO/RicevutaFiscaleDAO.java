package it.unisa.Storage.DAO;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class RicevutaFiscaleDAO implements FrontDeskStorage<RicevutaFiscale>
{

    private static final String TABLE_NAME = "RicevutaFiscale";

    private static final String[] attributes = {"IDRicevutaFiscale", "IDPrenotazione", "Totale", "DataEmissione"};

    @Override
    public synchronized void doSave(RicevutaFiscale o) throws SQLException {
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
    public synchronized void doDelete(RicevutaFiscale o) throws SQLException {
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

    public synchronized RicevutaFiscale doRetriveByKey(ArrayList<Integer> keys) throws SQLException {
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

        return ricevuta;
    }

    public synchronized Collection<RicevutaFiscale> doRetriveAll(String order) throws SQLException {
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

    @Override
    public synchronized void doUpdate(RicevutaFiscale o) throws SQLException
    {
        if(o != null)
        {
            Connection conn = ConnectionStorage.getConnection();
            PreparedStatement ps = null;
            String updateSQL = "UPDATE " + RicevutaFiscaleDAO.TABLE_NAME +
                    " SET Totale = ?, DataEmissione = ? " +
                    "WHERE IDRicevutaFiscale = ? AND IDPrenotazione = ?";

            try
            {
                ps = conn.prepareStatement(updateSQL);
                ps.setDouble(1, o.getTotale());
                ps.setDate(2, Date.valueOf(o.getDataEmissione()));
                ps.setInt(3, o.getIDRicevutaFiscale());
                ps.setInt(4, o.getIDPrenotazione());

                ps.executeUpdate();
            }
            finally
            {
                if (ps != null)
                    ps.close();
                ConnectionStorage.releaseConnection(conn);
            }
        }
        else
        {
            throw new SQLException();
        }
    }

    @Override
    public RicevutaFiscale doRetriveByAttribute(String attribute, String value) throws SQLException {
        if(attribute != null && !attribute.isEmpty() && value != null && !value.isEmpty()){
                Connection connection = ConnectionStorage.getConnection();
                try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hotelcolossus.ricevutafiscale WHERE " + attribute + " = ?")){

                    preparedStatement.setString(1,value);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if(resultSet.next()){
                        return null;
                    }
                    Date date = resultSet.getDate(4);
                    LocalDate localDate = date.toLocalDate();
                    RicevutaFiscale ricevutaFiscale = new RicevutaFiscale(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),localDate);
                    return ricevutaFiscale;
                }finally{
                    if(connection != null){
                        ConnectionStorage.releaseConnection(connection);
                    }
                }
        }else{
            throw new RuntimeException();
        }
    }

}
