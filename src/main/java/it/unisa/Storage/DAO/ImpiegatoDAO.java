package it.unisa.Storage.DAO;

import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.BackofficeStorage;
import it.unisa.Storage.ConnectionStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class ImpiegatoDAO implements BackofficeStorage<Impiegato>
{
    private static final String TABLE_NAME = "Impiegato";
    private static final String[] whitelist = {
            "CF",
            "Stipedio",
            "Nome",
            "Cognome",
            "Cap",
            "DataAssunzione",
            "Telefono",
            "Cittadinanza",
            "EmailAziendale",
            "Sesso",
            "Ruolo",
            "DataRilascio",
            "TipoDocumento",
            "Via",
            "Provincia",
            "Comune",
            "Civico",
            "NumeroDocumento",
            "DataScadenza",
            "CF1"
    };

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
                    throw new NoSuchElementException ("impiegato non trovato");
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
    public Collection<Impiegato> doRetriveByAttribute(String attribute, String value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Impiegato> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null && !value.isEmpty()) {
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM " + ImpiegatoDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();
                Impiegato impiegato;

                while (resultSet.next()) {
                    impiegato = new Impiegato();
                    impiegato.setUsername(resultSet.getString("username"));
                    impiegato.setNewHashedPassword(resultSet.getString("password"));
                    impiegato.setNome(resultSet.getString("nome"));
                    impiegato.setCognome(resultSet.getString("cognome"));
                    impiegato.setSesso(resultSet.getString("sesso"));
                    impiegato.setTipoDocumento(resultSet.getString("tipoDocumento"));
                    impiegato.setNumeroDocumento(resultSet.getString("numeroDocumento"));
                    impiegato.setCAP(resultSet.getInt("CAP"));
                    impiegato.setVia(resultSet.getString("via"));
                    impiegato.setProvincia(resultSet.getString("provincia"));
                    impiegato.setComune(resultSet.getString("comune"));
                    impiegato.setNumeroCivico(resultSet.getInt("numeroCivico"));
                    impiegato.setCodiceFiscale(resultSet.getString("codiceFiscale"));
                    impiegato.setTelefono(resultSet.getString("telefono"));
                    impiegato.setRuolo(resultSet.getObject("Ruolo", Ruolo.class));
                    impiegato.setStipendio(resultSet.getDouble("Stipendio"));
                    impiegato.setDataAssunzione(resultSet.getDate("DataAssunzione").toLocalDate());
                    impiegato.setDataRilascio(resultSet.getDate("DataRilascio").toLocalDate());
                    impiegato.setEmailAziendale(resultSet.getString("emailAziendale"));
                    impiegato.setCittadinanza(resultSet.getString("cittadinanza"));
                    impiegato.setDataScadenza(resultSet.getDate("DataScadenza").toLocalDate());
                    lista.add(impiegato);
                }
            } finally {
                if (connection != null) {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    ConnectionStorage.releaseConnection(connection);
                }
            }
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessun immpiegato trovato!");

        return lista;
    }

    @Override
    public Collection<Impiegato> doFilter(String nome, String sesso, Ruolo ruolo, String orderBy) throws  SQLException {
        Connection conn = ConnectionStorage.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;
        List<Impiegato> lista = new ArrayList<>();

        String selectSQL = "select * FROM " + ImpiegatoDAO.TABLE_NAME +
                " where nome = ? AND sesso = ? AND ruolo = ?";

        if(orderBy != null && !orderBy.isEmpty()) {
            if (DaoUtils.checkWhitelist(whitelist, orderBy))
                selectSQL +=  " ORDER BY " + orderBy + ";";
        }

        try {
            ps = conn.prepareStatement(selectSQL);
            rs = ps.executeQuery();

            while(rs.next()){
                Impiegato impiegato;
                impiegato = new Impiegato();
                impiegato.setCodiceFiscale(rs.getString("CF"));
                impiegato.setStipendio(rs.getDouble("Stipedio"));
                impiegato.setNome(rs.getString("nome"));
                impiegato.setCognome(rs.getString("Cognome"));
                impiegato.setCAP(rs.getInt("Cap"));
                impiegato.setSesso(rs.getString("sesso"));
                impiegato.setDataAssunzione(rs.getDate("date").toLocalDate());
                impiegato.setTelefono(rs.getString("Telefono"));
                impiegato.setCittadinanza(rs.getString("Cittadinanza"));
                impiegato.setEmailAziendale(rs.getString("EmailAziendale"));
                impiegato.setRuolo(rs.getObject("Ruolo", Ruolo.class));
                impiegato.setDataRilascio(rs.getDate("DataRilascio").toLocalDate());
                impiegato.setComune(rs.getString("Comune"));
                impiegato.setProvincia(rs.getString("Provincia"));
                impiegato.setTipoDocumento(rs.getString("TipoDocumento"));
                impiegato.setVia(rs.getString("Via"));
                impiegato.setNumeroCivico(rs.getInt("Civico"));
                impiegato.setNumeroDocumento(rs.getString("NumeroDocumento"));
                impiegato.setDataScadenza(rs.getDate("DataScadenza").toLocalDate());

                lista.add(impiegato);
            }

        }finally{
            if(conn != null){
                if (ps != null) {
                    ps.close();
                }
                ConnectionStorage.releaseConnection(conn);
            }
        }

        if(lista.isEmpty()) throw new NoSuchElementException("Nessun immpiegato {" + nome + ", " + sesso + "," + ruolo + "," + orderBy + "}!");

        return lista;
    }
}
