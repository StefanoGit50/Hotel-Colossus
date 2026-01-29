package it.unisa.Storage.DAO;

import it.unisa.Common.*;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.Interfacce.FrontDeskStorage;

import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class RicevutaFiscaleDAO implements FrontDeskStorage<RicevutaFiscale>
{

    private static final String[] attributes = {"IDRicevutaFiscale", "IDPrenotazione", "Totale", "DataEmissione"};

    @Override
    public synchronized void doSave(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String insertSQL = "insert into RicevutaFiscale VALUES (?,?,?,?,?,?,?)";
        String insertSQl1 = "insert into CameraRicevuta Values(?,?,?)";
        String insertSQL2 = "insert into clientiricevuta values(?,?,?,?,?)";

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        String intestatario = o.getPrenotazione().getIntestatario();
        try{
            ps2 = conn.prepareStatement(insertSQL2);
            ps1 = conn.prepareStatement(insertSQl1);
            ps = conn.prepareStatement(insertSQL);
            ps.setInt(1, o.getIDRicevutaFiscale());
            ps.setInt(2, o.getPrenotazione().getIDPrenotazione());
            ps.setDate(3, Date.valueOf(o.getDataEmissione()));
            ps.setString(4,o.getMetodoPagamento());
            ps.setDate(5,Date.valueOf(o.getDataPrenotazione()));
            ps.setDouble(6,o.getPrezzoTrattamento());
            ps.setString(7,o.getTipoTrattamento());
            ps.executeUpdate();

            for(Camera camera: o.getCameras()){
                ps1.setInt(1,o.getIDRicevutaFiscale());
                ps1.setInt(2,camera.getNumeroCamera());
                ps1.setDouble(3,camera.getPrezzoCamera());
                ps1.executeUpdate();
            }

            for(Cliente cliente: o.getClientes()){
                ps2.setInt(2,o.getIDRicevutaFiscale());
                ps2.setString(1,cliente.getCf());
                ps2.setString(3,cliente.getNome());
                ps2.setString(4,cliente.getCognome());
                if(intestatario.equalsIgnoreCase(cliente.getCf())){
                    ps2.setBoolean(5,true);
                }else{
                    ps2.setBoolean(5,false);
                }
                ps2.executeUpdate();
            }
            ArrayList<Servizio> occorenze = new ArrayList<>();

            for(Servizio servizio: o.getServizi()){
                if(!occorenze.contains(servizio)){
                    occorenze.add(servizio);
                    int n = Collections.frequency(o.getServizi(),servizio);
                    ServizioRicevutaDAO servizioRicevutaDAO = new ServizioRicevutaDAO();
                    servizioRicevutaDAO.doSave(servizio,o.getIDRicevutaFiscale(),n);
                }
            }

        }finally{
            if(ps != null)
                ps.close();
            ConnectionStorage.releaseConnection(conn);
        }
    }

    /**
     * @param list
     * @throws SQLException
     */
    @Override
    public void doSaveAll(List<RicevutaFiscale> list) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void doDelete(RicevutaFiscale o) throws SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        String deleteSQL = "delete from ricevutafiscale" +
                " where IDRicevutaFiscale = ? and IDPrenotazione = ?";
        String deleteSQL1 = "delete from serviziricevuta where IDRicevutaFiscale = ?";
        String dele = "delete from clientiricevuta where IDRicevutaFiscale = ?";
        String delet = "delete from cameraricevuta where IDRicevutaFiscale = ?";
        PreparedStatement preparedStatem = null;
        PreparedStatement preparedStateme1 = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatem = conn.prepareStatement(deleteSQL1);
            preparedStateme1 = conn.prepareStatement(dele);
            preparedStatement = conn.prepareStatement(delet);
            ps = conn.prepareStatement(deleteSQL);
            ps.setInt(1, o.getIDRicevutaFiscale());
            ps.setInt(2, o.getPrenotazione().getIDPrenotazione());
            preparedStatem.setInt(1,o.getIDRicevutaFiscale());
            preparedStateme1.setInt(1,o.getIDRicevutaFiscale());
            preparedStatement.setInt(1,o.getIDRicevutaFiscale());
            preparedStatem.executeUpdate();
            ps.executeUpdate();
            preparedStateme1.executeUpdate();
            preparedStatement.executeUpdate();
        }finally{
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
        PreparedStatement pre = null;
        PreparedStatement pre1 = null;
        PreparedStatement pre2 = null;
        ResultSet rs;
        ResultSet resultSet;
        ResultSet resultSet1;
        ResultSet resultSet2;
        RicevutaFiscale ricevuta = null;

        String selectSQL = "SELECT * FROM ricevutafiscale Where IDRicevutaFiscale = ? and IDPrenotazione = ?";
        String selectSQL1 = "SELECT * FROM cameraricevuta Where IDRicevutaFiscale = ?";
        String selectSQL2 = "Select * From serviziricevuta where IDRicevutaFiscale = ?";
        String selectSQL3 = "Select * FROM clientiricevuta where IDRicevutaFiscale = ?";
        try{
            ps = conn.prepareStatement(selectSQL);
            ps.setInt(1,keys.get(1));
            ps.setInt(2,keys.get(2));
            rs = ps.executeQuery();

            pre = conn.prepareStatement(selectSQL1);
            pre.setInt(1, keys.get(1));
            resultSet = pre.executeQuery();

            pre1 = conn.prepareStatement(selectSQL2);
            pre1.setInt(1,keys.get(1));
            resultSet1 = pre1.executeQuery();

            pre2 = conn.prepareStatement(selectSQL3);
            pre2.setInt(1,keys.get(1));
            resultSet2 = pre2.executeQuery();

            if(rs.next()){
                ricevuta = new RicevutaFiscale();
                ricevuta.setPrenotazione(new Prenotazione());
                ricevuta.setIDRicevutaFiscale(rs.getInt("IDRicevutaFiscale"));
                ricevuta.getPrenotazione().setIDPrenotazione(rs.getInt("IDPrenotazione"));
                ricevuta.setTipoTrattamento(rs.getString("TipoTrattamento"));
                ricevuta.setPrezzoTrattamento(rs.getDouble("PrezzoTrattamento"));
                ricevuta.setDataPrenotazione(rs.getDate("DataPrenotazione").toLocalDate());
                ricevuta.setDataEmissione(rs.getDate("DataEmissione").toLocalDate());
                ricevuta.setMetodoPagamento(rs.getString("metodoPagamento"));
                double totPrezzoCamera = 0;
                ArrayList<Camera> cameras = new ArrayList<>();

                while(resultSet.next()){
                    Camera camera = new Camera();
                    camera.setNumeroCamera(resultSet.getInt("NumeroCamera"));
                    camera.setPrezzoCamera(resultSet.getDouble("PrezzoCamera"));
                    totPrezzoCamera += camera.getPrezzoCamera();
                    cameras.add(camera);
                }

                ricevuta.setCameras(cameras);
                double totPrezzoServizi = 0;
                ArrayList<Servizio> servizios = new ArrayList<>();

                while(resultSet1.next()){
                    Servizio servizio = new Servizio();
                    servizio.setNome(resultSet1.getString("NomeServizio"));
                    servizio.setPrezzo(resultSet1.getDouble("PrezzoServizio"));
                    totPrezzoServizi += resultSet1.getInt("Quantità") * servizio.getPrezzo();
                    servizios.add(servizio);
                }

                ricevuta.setServizios(servizios);
                ricevuta.setTotale(totPrezzoCamera + totPrezzoServizi);
                ArrayList<Cliente> clientes = new ArrayList<>();

                while(resultSet2.next()){
                    Cliente cliente = new Cliente();
                    cliente.setCf(resultSet2.getString("CFCliente"));
                    cliente.setNome(resultSet2.getString("NomeCliente"));
                    cliente.setCognome(resultSet2.getString("CognomeCliente"));
                    clientes.add(cliente);
                }

                ricevuta.setClientes(clientes);

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
        String selectSQL = "SELECT ricevutafiscale.IDRicevutaFiscale,IDPrenotazione,DataEmissione,metodoPagamento,DataPrenotazione,PrezzoTrattamento,TipoTrattamento,cameraricevuta.PrezzoCamera,c.NumeroCamera,clientiricevuta.CFCliente,clientiricevuta.NomeCliente,clientiricevuta.CognomeCliente,clientiricevuta.isIntestatario,serviziricevuta.NomeServizio,serviziricevuta.PrezzoServizio,serviziricevuta.Quantità " +
                " FROM (ricevutafiscale join cameraricevuta on ricevutafiscale.IDRicevutaFiscale = cameraricevuta.IDRicevutaFiscale" +
                " join clientiricevuta on ricevutafiscale.IDRicevutaFiscale = clientiricevuta.IDRicevutaFiscale" +
                " join serviziricevuta on ricevutafiscale.IDRicevutaFiscale = serviziricevuta.IDRicevutaFiscale)" ;

        ArrayList<RicevutaFiscale> results = new ArrayList<>();

        if (order != null && !order.trim().isEmpty() && DaoUtils.checkWhitelist(attributes, order)) {
            selectSQL += " ORDER BY " + order;
        }

        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<Integer,RicevutaFiscaleBuilder> ricevutaFiscale = new LinkedHashMap<>();

        try{
            while(resultSet.next()){
                Integer integer = resultSet.getInt("IDRicevutaFiscale");
                RicevutaFiscaleBuilder re = ricevutaFiscale.get(integer);
                if(re == null){
                    re = new RicevutaFiscaleBuilder(resultSet);
                    ricevutaFiscale.put(integer,re);
                }
                re.addClienti(resultSet);
                re.addCamere(resultSet);
                re.addServizi(resultSet);
            }

            ArrayList<RicevutaFiscale> ricevutaFiscales = new ArrayList<>(ricevutaFiscale.size());

            for(RicevutaFiscaleBuilder fiscaleBuilder: ricevutaFiscale.values()){
                fiscaleBuilder.totalePrezzo();
                ricevutaFiscales.add(fiscaleBuilder.build());
            }

            return ricevutaFiscales;
        }finally{
            if (ps != null){
                ps.close();
                ConnectionStorage.releaseConnection(conn);
            }
        }
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
            String updateSQL1 = "Update cameraricevuta set PrezzoCamera = ? where IDRicevutaFiscale = ?";
            String updateSQL2 = "Update serviziricevuta set PrezzoServizio = ? where IDRicevutaFiscale = ?";
            String updateSQL3 = "Update clientiricevuta set NomeCliente = ? , CognomeCliente = ? where IDRicevutaFiscale = ?";



            try
            {
                ps = conn.prepareStatement(updateSQL);
                PreparedStatement preparedStatement = conn.prepareStatement(updateSQL1);
                PreparedStatement preparedStatement1 = conn.prepareStatement(updateSQL2);
                PreparedStatement preparedStatement2 = conn.prepareStatement(updateSQL3);

                ps.setString(1,o.getMetodoPagamento());
                ps.setDate(2,Date.valueOf(o.getDataPrenotazione()));
                ps.setDouble(3,o.getPrezzoTrattamento());
                ps.setString(4,o.getTipoTrattamento());
                ps.setDate(5,Date.valueOf(o.getDataPrenotazione()));
                ps.setInt(6,o.getIDRicevutaFiscale());
                ps.setInt(7,o.getPrenotazione().getIDPrenotazione());

                ps.executeUpdate();

                for(Camera camera: o.getCameras()){
                    preparedStatement.setDouble(1,camera.getPrezzoCamera());
                    preparedStatement.setInt(2,o.getIDRicevutaFiscale());
                    preparedStatement.executeUpdate();
                }

                for(Servizio servizio: o.getServizi()){
                    preparedStatement1.setDouble(1,servizio.getPrezzo());
                    preparedStatement1.setInt(2,o.getIDRicevutaFiscale());
                    preparedStatement1.executeUpdate();
                }

                for(Cliente cliente: o.getClientes()){
                    preparedStatement2.setString(1,cliente.getNome());
                    preparedStatement2.setString(2,cliente.getCognome());
                    preparedStatement2.setInt(3,o.getIDRicevutaFiscale());
                    preparedStatement2.executeUpdate();
                }

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
        throw new UnsupportedOperationException("Not supported yet");
    }
    /**
     *
     * NON è SUPPORTATA
     */
    @Override
    public Collection<RicevutaFiscale> doFilter(String nome, String cognome, String nazionalita, LocalDate dataDiNascita, Boolean blacklisted, String orderBy) throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doSaveAll(List<Camera> listCamera) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
