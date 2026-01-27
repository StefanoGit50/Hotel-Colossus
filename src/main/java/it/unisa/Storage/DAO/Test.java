package it.unisa.Storage.DAO;

import it.unisa.Common.Camera;
import it.unisa.Common.Impiegato;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {
    static void main() throws SQLException, ClassNotFoundException {
        List<Camera> listaCamere = new ArrayList<>(5);

        // Aggiunta di 5 istanze
        listaCamere.add(new Camera(101, Stato.Libera, 5, 454, "Vista mare, letto matrimoniale"));
        listaCamere.add(new Camera(102, Stato.Libera, 5, 454, "Vista mare, letto matrimoniale"));
        listaCamere.add(new Camera(103, Stato.Libera, 5, 454, "Vista mare, letto matrimoniale"));
        listaCamere.add(new Camera(104, Stato.Libera, 5, 454, "Vista mare, letto matrimoniale"));
        listaCamere.add(new Camera(105, Stato.Libera, 5, 454, "Vista mare, letto matrimoniale"));

        CameraDAO cameraDAO = new CameraDAO();
        cameraDAO.doSaveAll(listaCamere);
    }
}
