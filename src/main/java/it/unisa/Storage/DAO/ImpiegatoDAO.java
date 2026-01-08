package it.unisa.Storage.DAO;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.ConnectionStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class ImpiegatoDAO implements BackofficeStorage<Impiegato>
{
    @Override
    public synchronized void doSave(Impiegato impiegato) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Impiegato(CF, Stipedio, Nome, Cognome, Cap, DataAssunzione, Telefono, Cittadinanza, EmailAziendale, Sesso, Ruolo, DataRilascio, TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento, DataScadenza) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
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
            preparedStatement.executeUpdate();
        }finally{
            if(connection != null){
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized void doDelete(Impiegato impiegato) throws SQLException
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
    public synchronized Impiegato doRetriveByKey(Object index) throws SQLException{
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
                    String c =  resultSet.getString(11);
                    Ruolo ruolo = Ruolo.valueOf(c);
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
                    impiegato.setCAP(Integer.parseInt(cap));
                    impiegato.setDataAssunzione(date);
                    impiegato.setTelefono(telefono);
                    impiegato.setCittadinanza(cittadinanza);
                    impiegato.setEmailAziendale(emailAzienda);
                    impiegato.setSesso(sesso);
                    impiegato.setRuolo(ruolo);
                    impiegato.setDataRilascio(localDate);
                    impiegato.setTipoDocumento(tipoDocumento);
                    impiegato.setVia(via);
                    impiegato.setProvincia(provincia);
                    impiegato.setComune(comune);
                    impiegato.setNumeroCivico(civico);
                    impiegato.setNumeroDocumento(numeroDocumento);
                    impiegato.setDataScadenza(date1);
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
    public synchronized Collection<Impiegato> doRetriveAll(String order) throws SQLException{
        if(order != null){
            Connection connection = ConnectionStorage.getConnection();
            ArrayList<Impiegato> impiegatoes = new ArrayList<>();
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM impiegato"); ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Impiegato impiegato;
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
                    String c =  resultSet.getString(11);
                    Ruolo ruolo = Ruolo.valueOf(c);
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
                    impiegato.setCAP(Integer.parseInt(cap));
                    impiegato.setDataAssunzione(date);
                    impiegato.setTelefono(telefono);
                    impiegato.setCittadinanza(cittadinanza);
                    impiegato.setEmailAziendale(emailAzienda);
                    impiegato.setSesso(sesso);
                    impiegato.setRuolo(ruolo);
                    impiegato.setDataRilascio(localDate);
                    impiegato.setTipoDocumento(tipoDocumento);
                    impiegato.setVia(via);
                    impiegato.setProvincia(provincia);
                    impiegato.setComune(comune);
                    impiegato.setNumeroCivico(civico);
                    impiegato.setNumeroDocumento(numeroDocumento);
                    impiegato.setDataScadenza(date1);

                    try{
                        impiegatoes.add(impiegato.clone());
                    }catch(CloneNotSupportedException cloneNotSupportedException){
                        cloneNotSupportedException.getCause();
                        cloneNotSupportedException.getLocalizedMessage();
                        cloneNotSupportedException.getMessage();
                    }
                }
            }finally {
                if(connection != null){
                    ConnectionStorage.releaseConnection(connection);
                }
            }
            return impiegatoes;
        }else{
            throw new NullPointerException();
        }
    }


    /**
     * Aggiorna i dati di un impiegato esistente nel database.
     *
     * @param impiegato L'impiegato con i dati aggiornati da persistere.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws NullPointerException Se il parametro impiegato è null.
     *
     * Precondizioni:
     * impiegato != null
     * impiegato.getCodiceFiscale() deve corrispondere a un impiegato esistente nel database
     * impiegato.getRuolo() deve essere un valore valido dell'enum Ruolo
     * impiegato.getStipendio() deve essere maggiore o uguale a 0
     * Tutte le date (DataAssunzione, DataRilascio, DataScadenza) devono essere valorizzate
     *
     * Postcondizioni:
     * Il record dell'impiegato nel database viene aggiornato con i nuovi valori
     * Il CF (chiave primaria) rimane invariato
     */
    @Override
    public synchronized void doUpdate(Impiegato impiegato) throws SQLException
    {
        if(impiegato != null)
        {
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Impiegato SET Stipedio = ?, Nome = ?, Cognome = ?, Cap = ?, " +
                            "DataAssunzione = ?, Telefono = ?, Cittadinanza = ?, EmailAziendale = ?, " +
                            "Sesso = ?, Ruolo = ?, DataRilascio = ?, TipoDocumento = ?, Via = ?, " +
                            "Provincia = ?, Comune = ?, Civico = ?, NumeroDocumento = ?, " +
                            "DataScadenza = ? WHERE CF = ?")){

                preparedStatement.setDouble(1, impiegato.getStipendio());
                preparedStatement.setString(2, impiegato.getNome());
                preparedStatement.setString(3, impiegato.getCognome());
                preparedStatement.setString(4, "" + impiegato.getCAP());
                preparedStatement.setDate(5, Date.valueOf(impiegato.getDataAssunzione()));
                preparedStatement.setString(6, impiegato.getTelefono());
                preparedStatement.setString(7, impiegato.getCittadinanza());
                preparedStatement.setString(8, impiegato.getEmailAziendale());
                preparedStatement.setString(9, impiegato.getSesso());
                preparedStatement.setString(10, "" + impiegato.getRuolo());
                preparedStatement.setDate(11, Date.valueOf(impiegato.getDataRilascio()));
                preparedStatement.setString(12, impiegato.getTipoDocumento());
                preparedStatement.setString(13, impiegato.getVia());
                preparedStatement.setString(14, impiegato.getProvincia());
                preparedStatement.setString(15, impiegato.getComune());
                preparedStatement.setInt(16, impiegato.getNumeroCivico());
                preparedStatement.setString(17, impiegato.getNumeroDocumento());
                preparedStatement.setDate(18, Date.valueOf(impiegato.getDataScadenza()));
                preparedStatement.setString(19, impiegato.getCodiceFiscale());

                preparedStatement.executeUpdate();
            }
            finally
            {
                if(connection != null)
                {
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }
        else
        {
            throw new NullPointerException();
        }
    }

    @Override
    public Impiegato doRetriveByAttribute(String attribute, String value) throws SQLException {
        if(attribute != null && !attribute.isEmpty() && value != null && !value.isEmpty()){
            Connection connection = ConnectionStorage.getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM impiegato WHERE " + attribute + " = ?")){
                preparedStatement.setString(1,value);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(!resultSet.next()){
                    return null;
                }

                String CF = resultSet.getString("CF");
                Double stipedio = resultSet.getDouble("Stipedio");
                String nome = resultSet.getString("Nome");
                String cognome = resultSet.getString("Cognome");
                String Cap = resultSet.getString("Cap");
                Date date = resultSet.getDate("DataAssunzione");
                LocalDate date1 = date.toLocalDate();
                String telefono = resultSet.getString("Telefono");
                String cittadinanza = resultSet.getString("Cittadinanza");
                String emailAzienda = resultSet.getString("EmailAziendale");
                String sesso = resultSet.getString("Sesso");
                String ruolo = resultSet.getString("Ruolo");
                Ruolo ruolo1 = Ruolo.valueOf(ruolo);
                Date date2 = resultSet.getDate("DataRilascio");
                String tipoDocumeto = resultSet.getString("TipoDocumento");
                String via = resultSet.getString("Via");
                String provincia = resultSet.getString("Provincia");
                String comune = resultSet.getString("Comune");
                String civico = resultSet.getString("Civico");
                String numeroDocumento = resultSet.getString("NumeroDocumento");
                Date dataScadenza = resultSet.getDate("DataScadenza");
                LocalDate localDate = dataScadenza.toLocalDate();
            }finally{
                    if(connection != null){
                        ConnectionStorage.releaseConnection(connection);
                    }
            }
        }
    }
}
