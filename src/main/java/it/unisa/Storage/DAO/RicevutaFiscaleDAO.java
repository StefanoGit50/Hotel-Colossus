package it.unisa.Storage.DAO;

import it.unisa.Common.RicevutaFiscale;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class RicevutaFiscaleDAO implements FrontDeskStorage<RicevutaFiscale>
{

    private static final String[] attributes = {"IDRicevutaFiscale", "IDPrenotazione", "Totale", "DataEmissione"};

    @Override
    public synchronized void doSave(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String insertSQL = "insert into RicevutaFiscale VALUES (?,?,?,?,?,?,?)";

        try{
            ps = conn.prepareStatement(insertSQL);
            ps.setInt(1, o.getIDRicevutaFiscale());
            ps.setInt(2, o.getIDPrenotazione());
            ps.setDate(3, Date.valueOf(o.getDataEmissione()));
            ps.setString(4,o.getMetodoPagamento());
            ps.setDate(5,Date.valueOf(o.getDataPrenotazione()));
            ps.setDouble(6,o.getPrezzoTrattamento());
            ps.setString(7,o.getTipoTrattamento());
            ps.executeUpdate();
        }finally{
            if(ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }
    }

    @Override
    public synchronized void doDelete(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String deleteSQL = "delete from ricevutafiscale" +
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

        String selectSQL = "select * FROM ricevutafiscale" +
                " where IDRicevutaFiscale = ? AND  IDPrenotazione = ?";

        try {
            ps = conn.prepareStatement(selectSQL);
            ps.setInt(1, keys.get(0));
            ps.setInt(2, keys.get(1));
            rs = ps.executeQuery();

            if (rs.next()){
                ricevuta = new RicevutaFiscale();
                ricevuta.setIDRicevutaFiscale(rs.getInt("IDRicevutaFiscale"));
                ricevuta.setIDPrenotazione(rs.getInt("IDPrenotazione"));
                ricevuta.setDataPrenotazione(rs.getDate("DataPrenotazione").toLocalDate());
                ricevuta.setDataEmissione(rs.getDate("DataEmissione").toLocalDate());
                ricevuta.setMetodoPagamento(rs.getString("metodoDiPagamento"));
                ricevuta.setPrezzoTrattamento(rs.getDouble("PrezzoTrattamento"));
                ricevuta.setTipoTrattamento(rs.getString("TipoTrattamento"));
                double prezzoTrattamento = ricevuta.getPrezzoTrattamento();
                PreparedStatement preparedStatement = conn.prepareStatement("SELECT PrezzoCamera FROM (ricevutafiscale join hotelcolossus.cameraricevuta c on ricevutafiscale.IDRicevutaFiscale = c.IDRicevutaFiscale) where c.IDRicevutaFiscale = ?");
                preparedStatement.setInt(1,ricevuta.getIDRicevutaFiscale());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    ricevuta.setTotale(resultSet.getDouble("PrezzoCamera") + prezzoTrattamento);
                }else{
                    ricevuta.setTotale(prezzoTrattamento);
                }
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

    public synchronized Collection<RicevutaFiscale> doRetriveAll(String order) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectSQL = "SELECT * FROM ricevutafiscale" ;

        ArrayList<RicevutaFiscale> results = new ArrayList<>();

        if (order != null && !order.trim().isEmpty() && DaoUtils.checkWhitelist(attributes, order)) {
            selectSQL += " ORDER BY " + order;
        }

        try {
            ps = conn.prepareStatement(selectSQL);
            rs = ps.executeQuery();

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT PrezzoCamera FROM cameraricevuta where IDRicevutaFiscale = ?");
            double prezzoTrattamento = 0;
            double prezzoCamera = 0;
            RicevutaFiscale ricevuta;
            while (rs.next()){
                preparedStatement.setInt(1,rs.getInt("IDRicevutaFiscale"));
                ResultSet resultSet = preparedStatement.executeQuery();
                ricevuta = new RicevutaFiscale();
                prezzoTrattamento = rs.getDouble("PrezzoTrattamento");
                if(resultSet.next()){
                    prezzoCamera = resultSet.getDouble("PrezzoCamera");
                }
                ricevuta.setIDRicevutaFiscale(rs.getInt("IDRicevutaFiscale"));
                ricevuta.setIDPrenotazione(rs.getInt("IDPrenotazione"));
                ricevuta.setTotale(prezzoTrattamento + prezzoCamera);
                ricevuta.setDataEmissione(rs.getDate("DataEmissione").toLocalDate());
                ricevuta.setTipoTrattamento(rs.getString("TipoTrattamento"));
                ricevuta.setPrezzoTrattamento(rs.getDouble("PrezzoTrattamento"));
                ricevuta.setDataPrenotazione(rs.getDate("DataPrenotazione").toLocalDate());
                ricevuta.setMetodoPagamento(rs.getString("metodoPagamento"));
                results.add(ricevuta);
            }

        } finally {
            if (ps != null){
                ps.close();
                ConnectionStorage.releaseConnection(conn);
            }
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
            String updateSQL = "UPDATE ricevutafiscale" +
                    " SET metodoPagamento = ? , DataPrenotazione = ? , PrezzoTrattamento = ? , TipoTrattamento = ?, DataEmissione = ? " +
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

    public synchronized Collection<RicevutaFiscale> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<RicevutaFiscale> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null){
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM hotelcolossus.ricevutafiscale WHERE " + attribute + " = ?";
                try{
                    preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setObject(1, value);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    RicevutaFiscale ricevuta;
                    while (resultSet.next()) {
                        ricevuta = new RicevutaFiscale();
                        ricevuta.setIDRicevutaFiscale(resultSet.getInt("IDRicevutaFiscale"));
                        ricevuta.setIDPrenotazione(resultSet.getInt("IDPrenotazione"));
                        ricevuta.setTotale(resultSet.getDouble("Totale"));
                        ricevuta.setDataEmissione(resultSet.getDate("DataEmissione").toLocalDate());
                        lista.add(ricevuta);
                    }

                }finally{
                    if(connection != null){
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        ConnectionStorage.releaseConnection(connection);
                    }
                }
        }else{
            throw new RuntimeException("Attributo e/o valore non valido/i");
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessuna ricevuta fiscale con " + attribute + " = " + value + "!");

        return lista;
    }
    /**
     *
     * NON è SUPPORTATA
     */
    @Override
    public Collection<RicevutaFiscale> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blacklisted, String orderBy) throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
