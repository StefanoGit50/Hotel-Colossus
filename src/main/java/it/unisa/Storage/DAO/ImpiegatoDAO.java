package it.unisa.Storage.DAO;


import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Storage.Interfacce.BackofficeStorage;
import it.unisa.Storage.ConnectionStorage;

import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImpiegatoDAO implements BackofficeStorage<Impiegato> {
    private static final String TABLE_NAME = "Impiegato";
    String[] whitelist = {
            "CF",
            "nome",
            "cognome",
            "Cap",
            "comune",
            "civico",
            "provincia",
            "via",
            "Email",
            "Sesso",
            "telefono",
            "MetodoDiPagamento",
            "Cittadinanza",
            "DataDiNascita",
            "IsBackListed"
    };

    @Override
    public synchronized void doSave(Impiegato impiegato) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Impiegato(CF,Stipendio,UserName,HashPasword,isTemporary,dataScadenzaToken,Nome," +
                                                                                    "Cognome,Cap,DataAssunzione, Telefono, Cittadinanza, EmailAziendale, Sesso, Ruolo," +
                                                                                    "TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento," +
                                                                                    "DataScadenzaDocumento,DataRilascioDocumento)" +
                                                                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            preparedStatement.setString(1, impiegato.getCodiceFiscale());
            preparedStatement.setDouble(2, impiegato.getStipendio());
            preparedStatement.setString(3, impiegato.getUserName());
            preparedStatement.setString(4, impiegato.getHashPassword());
            preparedStatement.setBoolean(5, impiegato.isTempurali());
            preparedStatement.setTimestamp(6, Timestamp.from(impiegato.getDataScadenzaToken()));
            preparedStatement.setString(7, impiegato.getNome());
            preparedStatement.setString(8, impiegato.getCognome());
            preparedStatement.setString(9, "" + impiegato.getCAP());
            preparedStatement.setDate(10, Date.valueOf(impiegato.getDataAssunzione()));
            preparedStatement.setString(11, impiegato.getTelefono());
            preparedStatement.setString(12, impiegato.getCittadinanza());
            preparedStatement.setString(13, impiegato.getEmailAziendale());
            preparedStatement.setString(14, impiegato.getSesso());
            preparedStatement.setString(15, "" + impiegato.getRuolo());
            preparedStatement.setDate(16, Date.valueOf(impiegato.getDataRilascio()));
            preparedStatement.setString(17, impiegato.getTipoDocumento());
            preparedStatement.setString(18, impiegato.getVia());
            preparedStatement.setString(19, impiegato.getProvincia());
            preparedStatement.setString(20, impiegato.getComune());
            preparedStatement.setInt(21, impiegato.getNumeroCivico());
            preparedStatement.setString(22, impiegato.getNumeroDocumento());
            preparedStatement.setDate(23, Date.valueOf(impiegato.getDataScadenza()));
            preparedStatement.setDate(24,Date.valueOf(impiegato.getDataRilascio()));
            preparedStatement.executeUpdate();
        }finally{
            if (connection != null) {
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized void doDelete(Impiegato impiegato) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM impiegato WHERE CF = ?")) {
            preparedStatement.setString(1, impiegato.getCodiceFiscale());
            preparedStatement.executeUpdate();
        } finally {
            if (connection != null) {
                ConnectionStorage.releaseConnection(connection);
            }
        }
    }

    @Override
    public synchronized Impiegato doRetriveByKey(Object index) throws SQLException {
        String f = "";
        if (index instanceof String) {
            f = (String) index;
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher matcher = p.matcher(f);
            if (matcher.matches()) {
                Integer integer = Integer.parseInt(f);
                Connection connection = ConnectionStorage.getConnection();
                Impiegato impiegato;
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT CF,Nome,Cognome,UserName,HashPasword,isTemporary,dataScadenzaToken,Sesso , TipoDocumento " +
                                    "                                                                   , NumeroDocumento , Cap,Via ,Provincia , Comune ,Civico," +
                                                                                            "Telefono,Ruolo,Stipendio,DataAssunzione,DataScadenzaDocumento,DataRilascioDocumento," +
                                                                                            "EmailAziendale,Cittadinanza FROM impiegato WHERE IDImpiegato = ?")) {
                    preparedStatement.setString(1, f);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String cf = resultSet.getString("CF");
                        String nome = resultSet.getString("Nome");
                        String cognome = resultSet.getString("Cognome");
                        String userName = resultSet.getString("UserName");
                        String hashPassword = resultSet.getString("HashPasword");
                        Boolean isTempurali = resultSet.getBoolean("isTempurali");
                        Instant dataScadenzaToken = resultSet.getTimestamp("").toInstant();
                        String sesso = resultSet.getString("Sesso");
                        String tipoDocumento = resultSet.getString("TipoDocumento");
                        String i = resultSet.getString("NumeroDocumento");
                        Integer Cap = resultSet.getInt("Cap");
                        String via = resultSet.getString("Via");
                        String provincia = resultSet.getString("Provincia");
                        String comune = resultSet.getString("Comune");
                        Integer numeroCivico = resultSet.getInt("Civico");
                        String telefono = resultSet.getString("Telefono");
                        String ruolo = resultSet.getString("Ruolo");
                        Double stipedio = resultSet.getDouble("Stipedio");
                        LocalDate dataAssunzione = resultSet.getDate("DataAssunzione").toLocalDate();
                        LocalDate dataScadenza = resultSet.getDate("DataScadenzaDocumento").toLocalDate();
                        LocalDate dataRilascio = resultSet.getDate("DataRilascioDocumento").toLocalDate();
                        String emailAziendale = resultSet.getString("EmailAziendale");
                        String cittadinanza = resultSet.getString("Cittadinanza");


                        impiegato = new Impiegato(userName, hashPassword, isTempurali, dataScadenzaToken, nome, cognome, sesso, tipoDocumento, i, Cap, via, provincia, comune, numeroCivico, cf, telefono, Ruolo.valueOf(ruolo), stipedio, dataAssunzione, dataRilascio, emailAziendale, cittadinanza, dataScadenza);
                    } else {
                        impiegato = null;
                        throw new NoSuchElementException("impiegato non trovato");
                    }
                }finally {
                    if (connection != null) {
                        ConnectionStorage.releaseConnection(connection);
                    }
                }
                return impiegato;
            } else {
                throw new RuntimeException();
            }

        }else{
            Connection connection = ConnectionStorage.getConnection();
            Impiegato impiegato = null;
            try(PreparedStatement pr = connection.prepareStatement("SELECT CF , Nome , Cognome , UserName , HashPasword , isTemporary , dataScadenzaToken , Sesso ," +
                                                                        "TipoDocumento , NumeroDocumento , Cap,Via ,Provincia " +
                                                                        ", Comune ,Civico,Telefono,Ruolo,Stipendio,DataAssunzione,DataScadenzaDocumento" +
                                                                        ",DataRilascioDocumento,EmailAziendale,Cittadinanza FROM impiegato WHERE CF = ?")){
                pr.setString(1,f);
                ResultSet resultSet = pr.executeQuery();
                if (resultSet.next()){
                    String cf = resultSet.getString("CF");
                    String nome = resultSet.getString("Nome");
                    String cognome = resultSet.getString("Cognome");
                    String userName = resultSet.getString("UserName");
                    String hashPassword = resultSet.getString("HashPasword");
                    Boolean isTempurali = resultSet.getBoolean("isTempurali");
                    Instant dataScadenzaToken = resultSet.getTimestamp("").toInstant();
                    String sesso = resultSet.getString("Sesso");
                    String tipoDocumento = resultSet.getString("TipoDocumento");
                    String i = resultSet.getString("NumeroDocumento");
                    Integer Cap = resultSet.getInt("Cap");
                    String via = resultSet.getString("Via");
                    String provincia = resultSet.getString("Provincia");
                    String comune = resultSet.getString("Comune");
                    Integer numeroCivico = resultSet.getInt("Civico");
                    String telefono = resultSet.getString("Telefono");
                    String ruolo = resultSet.getString("Ruolo");
                    Double stipedio = resultSet.getDouble("Stipedio");
                    LocalDate dataAssunzione = resultSet.getDate("DataAssunzione").toLocalDate();
                    LocalDate dataScadenza = resultSet.getDate("DataScadenzaDocumento").toLocalDate();
                    LocalDate dataRilascio = resultSet.getDate("DataRilascioDocumento").toLocalDate();
                    String emailAziendale = resultSet.getString("EmailAziendale");
                    String cittadinanza = resultSet.getString("Cittadinanza");

                    impiegato = new Impiegato(userName,hashPassword,isTempurali,dataScadenzaToken,nome,cognome,sesso,tipoDocumento,i,Cap,via,provincia,comune,numeroCivico,cf,telefono,Ruolo.valueOf(ruolo),stipedio,dataAssunzione,dataRilascio,emailAziendale,cittadinanza,dataScadenza);
                }else{
                    throw new NoSuchElementException("impiegato non trovato");
                }
            }
            return  impiegato;
        }
    }

    @Override
    public synchronized Collection<Impiegato> doRetriveAll(String order) throws SQLException{
        if(order != null){
            Connection connection = ConnectionStorage.getConnection();
            ArrayList<Impiegato> impiegatoes = new ArrayList<>();
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT CF , Nome , Cognome , UserName , HashPasword , isTemporary, dataScadenzaToken ," +
                                                                                      "Sesso , TipoDocumento , NumeroDocumento , Cap,Via ,Provincia , Comune ,Civico,Telefono," +
                                                                                      "Ruolo,Stipendio,DataAssunzione,DataScadenzaDocumento,DataRilascioDocumento,EmailAziendale" +
                                                                                      ",Cittadinanza FROM impiegato"); ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Impiegato impiegato;
                    String cF = resultSet.getString("CF");
                    String nome = resultSet.getString("Nome");
                    String cognome = resultSet.getString("Cognome");
                    String userName = resultSet.getString("UserName");
                    String HashPassword = resultSet.getString("HashPasword");
                    boolean isTempurali = resultSet.getBoolean("isTempurali");
                    Instant dataScadenzaToken = resultSet.getTimestamp("dataScadenzaToken").toInstant();
                    String sesso = resultSet.getString("Sesso");
                    String tipoDocumento = resultSet.getString("TipoDocumento");
                    String NumeroDocumento = resultSet.getString("NumeroDocumento");
                    int cap = Integer.parseInt(resultSet.getString("Cap"));
                    String via = resultSet.getString("Via");
                    String provincia = resultSet.getString("Provincia");
                    String comune = resultSet.getString("Comune");
                    int civico = resultSet.getInt("Civico");
                    String telefono = resultSet.getString("Telefono");
                    String ruolo = resultSet.getString("Ruolo");
                    double stipedio = resultSet.getDouble("Stipedio");
                    LocalDate dataAssunzione = resultSet.getDate("DataAssunzione").toLocalDate();
                    LocalDate dataScadenzio = resultSet.getDate("DataScadenzaDocumento").toLocalDate();
                    LocalDate dataRilascio = resultSet.getDate("DataRilascioDocumento").toLocalDate();
                    String emailAziendale = resultSet.getString("EmailAziendale");
                    String Cittadinanza = resultSet.getString("Cittadinanza");

                    impiegato = new Impiegato(userName , HashPassword , isTempurali , dataScadenzaToken , nome , cognome , sesso , tipoDocumento , NumeroDocumento , cap , via , provincia , comune , civico , cF , telefono , Ruolo.valueOf(ruolo) , stipedio , dataAssunzione , dataRilascio ,emailAziendale , Cittadinanza , dataScadenzio);

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
                    "UPDATE Impiegato SET Nome = ? , Cognome = ? , UserName = ? ,isTemporary = ? , HashPasword = ?  , dataScadenzaToken = ? , Cap = ?" +
                            " , DataAssunzione = ? , Telefono = ? , Cittadinanza = ? , EmailAziendale = ? , Sesso = ? , Ruolo = ? , DataRilascioDocumento = ? , TipoDocumento = ? ," +
                            " Via = ? , Provincia = ? , Comune = ? ,Civico = ? , NumeroDocumento = ? , DataScadenzaDocumento = ?  WHERE CF = ?")){

                        preparedStatement.setString(1,impiegato.getNome());
                        preparedStatement.setString(2,impiegato.getCognome());
                        preparedStatement.setString(3,impiegato.getUserName());
                        preparedStatement.setBoolean(4,impiegato.isTempurali());
                        preparedStatement.setString(5,impiegato.getHashPassword());
                        preparedStatement.setTimestamp(6,Timestamp.from(impiegato.getDataScadenzaToken()));
                        preparedStatement.setString(7,"" + impiegato.getCAP());
                        preparedStatement.setDate(8,Date.valueOf(impiegato.getDataAssunzione()));
                        preparedStatement.setString(9,impiegato.getTelefono());
                        preparedStatement.setString(10,impiegato.getCittadinanza());
                        preparedStatement.setString(11,impiegato.getEmailAziendale());
                        preparedStatement.setString(12,impiegato.getSesso());
                        preparedStatement.setString(13,impiegato.getRuolo().name());
                        preparedStatement.setDate(14,Date.valueOf(impiegato.getDataRilascio()));
                        preparedStatement.setString(15,impiegato.getTipoDocumento());
                        preparedStatement.setString(16,impiegato.getVia());
                        preparedStatement.setString(17,impiegato.getProvincia());
                        preparedStatement.setInt(18,impiegato.getNumeroCivico());
                        preparedStatement.setString(19,impiegato.getNumeroDocumento());
                        preparedStatement.setDate(20,Date.valueOf(impiegato.getDataScadenza()));
                        preparedStatement.setString(21,impiegato.getCodiceFiscale());
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
    public Collection<Impiegato> doRetriveByAttribute(String attribute, Object value) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ArrayList<Impiegato> lista = new ArrayList<>();
        String selectSQL;

        if(attribute != null && !attribute.isEmpty() && value != null) {
            connection = ConnectionStorage.getConnection();
            selectSQL = "SELECT * FROM " + ImpiegatoDAO.TABLE_NAME + " WHERE " + attribute + " = ?";
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setObject(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();
                Impiegato impiegato;

                while (resultSet.next()) {
                    impiegato = new Impiegato();
                    impiegato.setUserName(resultSet.getString("Username"));
                    impiegato.setHashPassword(resultSet.getString("HashPasword"));
                    impiegato.setNome(resultSet.getString("Nome"));
                    impiegato.setCognome(resultSet.getString("Cognome"));
                    impiegato.setSesso(resultSet.getString("Sesso"));
                    impiegato.setTempurali(resultSet.getBoolean("isTempurali"));
                    impiegato.setDataScadenzaToken(resultSet.getTimestamp("DataScadenzaToken").toInstant());
                    impiegato.setTipoDocumento(resultSet.getString("TipoDocumento"));
                    impiegato.setNumeroDocumento(resultSet.getString("NumeroDocumento"));
                    impiegato.setCAP(resultSet.getInt("CAP"));
                    impiegato.setVia(resultSet.getString("Via"));
                    impiegato.setProvincia(resultSet.getString("Provincia"));
                    impiegato.setComune(resultSet.getString("Comune"));
                    impiegato.setNumeroCivico(resultSet.getInt("NumeroCivico"));
                    impiegato.setCodiceFiscale(resultSet.getString("CodiceFiscale"));
                    impiegato.setTelefono(resultSet.getString("Telefono"));
                    impiegato.setRuolo(Ruolo.valueOf((String) resultSet.getObject("Ruolo")));
                    impiegato.setStipendio(resultSet.getDouble("Stipendio"));
                    impiegato.setDataAssunzione(resultSet.getDate("DataAssunzione").toLocalDate());
                    impiegato.setDataRilascio(resultSet.getDate("DataRilascioDocumento").toLocalDate());
                    impiegato.setEmailAziendale(resultSet.getString("EmailAziendale"));
                    impiegato.setCittadinanza(resultSet.getString("Cittadinanza"));
                    impiegato.setDataScadenza(resultSet.getDate("DataScadenzaDocumento").toLocalDate());
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
    public Collection<Impiegato> doFilter(String nome, String sesso, Ruolo ruolo, String orderBy) throws  SQLException{
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs;
                List<Impiegato> lista = new ArrayList<>();
                String selectSQL = "";

                // Flags per verificare se almeno un parametro è stato fornito
                boolean[] params = new boolean[3];
                 params[0] = nome != null && !nome.isEmpty();
                 params[1] = sesso != null && !sesso.isEmpty();
                 params[2] = ruolo != null;

            // Se tutti i parametri sono nulli allora lancia l'eccezione
                if (!params[0] && !params[1] && !params[2]) {
            throw new RuntimeException("Nessun parametro inserito!");
            }

            // Conta il numero di parametri che sono veri
            int count = -1;
                for (boolean b : params) {
                    if(b) count++;
                }
            // Il numero di AND della query è pari a count - 1
            System.out.println(count);
            selectSQL += "SELECT * FROM " + ImpiegatoDAO.TABLE_NAME + " WHERE ";
                for(int i = 0; i < params.length; i++) {
            if (i == 0 && params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la query di ricerca
                selectSQL += " nome = ? ";
            }
            if (i == 1 && params[1]) {
                selectSQL += " sesso = ? ";
            }
            if (i == 2 && params[2]) {
                selectSQL += " ruolo = ? ";
            }
            if (count != 0 && params[i]){
                selectSQL += "AND";
                count--;
            }
        }

                if(orderBy != null && !orderBy.isEmpty()) {
            if (DaoUtils.checkWhitelist(whitelist, orderBy))
                selectSQL +=  " ORDER BY " + orderBy;
        }

                try {
            conn = ConnectionStorage.getConnection();
            System.out.println(selectSQL);
            ps = conn.prepareStatement(selectSQL);
            int counter = 1;
            if (params[0]) { // Se la flag è vera allora il parametro è presente ed è usato come criterio per la query di ricerca
                ps.setString(counter, nome);
                counter++;
            }
            if (params[1]) {
                ps.setString(counter, sesso);
                counter++;
            }
            if (params[2]) {
                ps.setString(counter, ruolo.name());
            }

            rs = ps.executeQuery();

            while(rs.next()){
                Impiegato impiegato;
                impiegato = new Impiegato();

                impiegato.setCodiceFiscale(rs.getString("CF"));
                impiegato.setStipendio(rs.getDouble("Stipedio"));
                impiegato.setUserName(rs.getString("UserName"));
                impiegato.setHashPassword(rs.getString("HashPasword"));
                impiegato.setTempurali(rs.getBoolean("isTempurali"));
                impiegato.setDataScadenzaToken(rs.getTimestamp("dataScadenzaToken").toInstant());
                impiegato.setNome(rs.getString("nome"));
                impiegato.setCognome(rs.getString("Cognome"));
                impiegato.setCAP(rs.getInt("Cap"));
                impiegato.setSesso(rs.getString("sesso"));
                impiegato.setDataAssunzione(rs.getDate("DataAssunzione").toLocalDate());
                impiegato.setTelefono(rs.getString("Telefono"));
                impiegato.setCittadinanza(rs.getString("Cittadinanza"));
                impiegato.setEmailAziendale(rs.getString("EmailAziendale"));
                impiegato.setRuolo(Ruolo.valueOf(rs.getString("Ruolo")));
                impiegato.setDataRilascio(rs.getDate("DataRilascioDocumento").toLocalDate());
                impiegato.setComune(rs.getString("Comune"));
                impiegato.setProvincia(rs.getString("Provincia"));
                impiegato.setTipoDocumento(rs.getString("TipoDocumento"));
                impiegato.setVia(rs.getString("Via"));
                impiegato.setNumeroCivico(rs.getInt("Civico"));
                impiegato.setNumeroDocumento(rs.getString("NumeroDocumento"));
                impiegato.setDataScadenza(rs.getDate("DataScadenzaDocumento").toLocalDate());

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
