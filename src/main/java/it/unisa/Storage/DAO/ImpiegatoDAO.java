package it.unisa.Storage.DAO;

import it.unisa.Common.Impiegato;
import it.unisa.Storage.BackofficeStorage;

import java.sql.SQLException;
import java.util.Collection;

public class ImpiegatoDAO implements BackofficeStorage<Impiegato>
{
    @Override
    public void doSave(Impiegato impiegato) throws SQLException
    {

    }

    @Override
    public void doDelete(Impiegato impiegato) throws SQLException
    {

    }

    @Override
    public Impiegato doRetriveByKey(int index) throws SQLException
    {

    }

    @Override
    public Collection<Impiegato> doRetriveAll(String order) throws SQLException
    {

    }
}
