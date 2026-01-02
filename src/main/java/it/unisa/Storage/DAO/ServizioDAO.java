package it.unisa.Storage.DAO;

import it.unisa.Common.Servizio;
import it.unisa.Storage.FrontDeskStorage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class ServizioDAO implements FrontDeskStorage<Servizio>
{
    private Connection connection;

    public ServizioDAO(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void doSave(Servizio servizio) throws SQLException
    {
        String query = "INSERT INTO Servizio (Nome, Prezzo, IDPrenotazione) VALUES (?, ?, ?) ";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, servizio.getNome());
            stmt.setDouble(2, servizio.getPrezzo());
            stmt.setNull(3, Types.INTEGER); // gestito altrove

            stmt.executeUpdate();
        }
    }

    @Override
    public void doDelete(Servizio servizio) throws SQLException
    {
        String query = "DELETE FROM Servizio WHERE Nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, servizio.getNome());
            stmt.executeUpdate();
        }
    }

    @Override
    public Servizio doRetriveByKey(Object nome) throws SQLException
    {
        String query = "SELECT * FROM Servizio WHERE Nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, (String) nome);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new Servizio(
                            rs.getString("Nome"),
                            rs.getDouble("Prezzo"));
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Servizio> doRetriveAll(String order) throws SQLException
    {
        String query = "SELECT * FROM Servizio WHERE IDPrenotazione IS NULL";
        if (order != null && !order.trim().isEmpty())
        {
            query += " ORDER BY " + order;
        }

        Collection<Servizio> servizi = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Servizio s = new Servizio(
                        rs.getString("Nome"),
                        rs.getDouble("Prezzo"));

                servizi.add(s);
            }
        }

        return servizi;
    }
}
