package it.unisa.Storage.DAO;

import it.unisa.Common.Cliente;
import it.unisa.Storage.ConnectionStorage;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ClienteDAO implements FrontDeskStorage<Cliente> {
    public void doSave(Cliente o) throws SQLException{
            Connection connection = ConnectionStorage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if(o.getCf().length() == 16){
                preparedStatement.setString(1,o.getCf());
            }else{
                throw new CodiceFiscaleNotSupportedException();
            }

            if(o.getNome().length() <= 50 && !o.getNome().isEmpty()){
                preparedStatement.setString(2,o.getNome());
            }else{
                throw new NomeException();
            }

            if(o.getNome().length() <= 50 && !o.getNome().isEmpty()){
                preparedStatement.setString(3,o.getCognome());
            }else{
                throw new CognomeException();
            }

            Integer cap = o.getCAP();

            if(cap.compareTo(10000) > 0 && cap.compareTo(99999) < 0){

            }



    }

    @Override
    public Cliente doRetriveByKey(int index) throws SQLException{

    }

    @Override
    public void doDelete(Cliente o) throws SQLException {

    }

    @Override
    public Collection<Cliente> doRetriveAll(String order) throws SQLException {
        return List.of();
    }

}
