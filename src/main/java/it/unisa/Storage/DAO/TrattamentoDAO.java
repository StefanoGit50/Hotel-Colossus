package it.unisa.Storage.DAO;

import it.unisa.Common.Trattamento;
import it.unisa.Storage.FrontDeskStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class TrattamentoDAO implements FrontDeskStorage<Trattamento>
{

    private Connection connection;

    public TrattamentoDAO(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void doSave(Trattamento trattamento) throws SQLException
    {
        String query = "INSERT INTO Trattamento(Nome, Prezzo, PrezzoAcquisto, IDPrenotazione) VALUES (?, ?, ?, ?) ";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, trattamento.getNome());
            stmt.setDouble(2, trattamento.getPrezzo());
            stmt.setDouble(3, trattamento.getPrezzoAcquisto());
            stmt.setNull(4, Types.INTEGER); // gestito altrove

            stmt.executeUpdate();
        }
    }

    @Override
    public void doDelete(Trattamento trattamento) throws SQLException
    {
        String query = "DELETE FROM Trattamento WHERE Nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, trattamento.getNome());
            stmt.executeUpdate();
        }
    }

    @Override
    public Trattamento doRetriveByKey(Object nome) throws SQLException
    {
        String query = "SELECT * FROM Trattamento WHERE Nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, (String) nome);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new Trattamento(
                            rs.getString("Nome"),
                            rs.getDouble("Prezzo"),
                            rs.getDouble("PrezzoAcquisto")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Trattamento> doRetriveAll(String order) throws SQLException
    {
        String query = "SELECT * FROM Trattamento WHERE IDPrenotazione IS NULL";
        if (order != null && !order.trim().isEmpty())
        {
            query += " ORDER BY " + order;
        }

        Collection<Trattamento> trattamenti = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Trattamento t = new Trattamento(
                        rs.getString("Nome"),
                        rs.getDouble("Prezzo"),
                        rs.getDouble("PrezzoAcquisto")
                );

                trattamenti.add(t);
            }
        }

        return trattamenti;
    }
}