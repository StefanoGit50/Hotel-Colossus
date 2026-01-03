package it.unisa.Storage.DAO;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.ConnectionStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;

public class ImpiegatoDAO implements BackofficeStorage<Impiegato>
{
    @Override
    public void doSave(Impiegato impiegato) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Impiegato VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
            preparedStatement.setString(1,impiegato.getCodiceFiscale());
            preparedStatement.setDouble(2,impiegato.getStipendio());
            preparedStatement.setString(3,impiegato.getNome());
            preparedStatement.setString(4,impiegato.getCognome());
            preparedStatement.setString(5,"" + impiegato.getCAP());
            preparedStatement.setDate(6, Date.valueOf(impiegato.getDataAssunzione()));
            preparedStatement.setString(7,impiegato.getTelefono());
            preparedStatement.setString(8,impiegato.getCittadinanza());
            preparedStatement.setString(9,impiegato.getEmailAziendale());
            preparedStatement.setString(10,impiegato.getSesso());
            preparedStatement.setString(11,"" + impiegato.getRuolo());
            preparedStatement.setDate(12,Date.valueOf(impiegato.getDataRilascio()));
            preparedStatement.setString(13,impiegato.getTipoDocumento());
            preparedStatement.setString(14,impiegato.getVia());
            preparedStatement.setString(15,impiegato.getProvincia());
            preparedStatement.setString(16,impiegato.getComune());
            preparedStatement.setInt(17,impiegato.getNumeroCivico());
            preparedStatement.setString(18,impiegato.getNumeroDocumento());
            preparedStatement.setDate(19,Date.valueOf(impiegato.getDataScadenza()));
            preparedStatement.setString(20, null);
            preparedStatement.executeUpdate();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Impiegato impiegato) throws SQLException
    {
        Connection connection = ConnectionStorage.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM impiegato WHERE CF = ?")){
            preparedStatement.setString(1,impiegato.getCodiceFiscale());
            preparedStatement.executeUpdate();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public Impiegato doRetriveByKey(Object index) throws SQLException{
        if(index instanceof String){
            String f = (String) index;
           Connection connection = ConnectionStorage.getConnection();
            Impiegato impiegato;
           try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM impiegato WHERE CF = ?")){
                preparedStatement.setString(1,f);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    String cf = (String) resultSet.getObject(1);
                    Double stipedio = (Double) resultSet.getObject(2);
                    String nome = (String) resultSet.getObject(3);
                    String cognome = (String) resultSet.getObject(4);
                    String cap = (String) resultSet.getObject(5);
                    Date dataAssunzione = (Date) resultSet.getObject(6);
                    LocalDate date = dataAssunzione.toLocalDate();
                    String telefono = (String) resultSet.getObject(7);
                    String cittadinanza = (String) resultSet.getObject(8);
                    String emailAzienda = (String) resultSet.getObject(9);
                    String sesso = (String) resultSet.getObject(10);
                    Ruolo ruolo = (Ruolo) resultSet.getObject(11);
                    Date  dataRilascio = (Date) resultSet.getObject(12);
                    LocalDate localDate = dataRilascio.toLocalDate();
                    String tipoDocumento = (String) resultSet.getObject(13);
                    String via = (String) resultSet.getObject(14);
                    String provincia = (String) resultSet.getObject(15);
                    String comune = (String) resultSet.getObject(16);
                    Integer civico = (Integer) resultSet.getObject(17);
                    String numeroDocumento = (String) resultSet.getObject(18);
                    Date dataScadenza = (Date) resultSet.getObject(19);
                    LocalDate date1 = dataScadenza.toLocalDate();

                    impiegato = new Impiegato();
                    impiegato.setCodiceFiscale(cf);
                    impiegato.setNome(nome);
                    impiegato.setCognome(cognome);
                    impiegato.setCAP(Integer.getInteger(cap));
                    impiegato.setDataAssunzione(date);
                    impiegato.setTelefono(telefono);
                    impiegato.setCittadinanza(cittadinanza);
                    impiegato.setEmailAziendale(emailAzienda);
                    impiegato.setSesso(sesso);
                    impiegato.setRuolo(ruolo);
                    impiegato.setDataRilascio(localDate);
                    impiegato.setTipoDocumento(tipoDocumento);
                    impiegato.set
                }else{
                    impiegato = null;
                }
            }finally {
                if (connection != null) {
                    ConnectionStorage.releaseConnection(connection);
                }
            }
           return impiegato;
        }else{
            throw new RuntimeException();
        }
    }

    @Override
    public Collection<Impiegato> doRetriveAll(String order) throws SQLException{

    }
}
