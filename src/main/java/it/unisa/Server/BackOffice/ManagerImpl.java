package it.unisa.Server.BackOffice;

import it.unisa.Common.Impiegato;
import it.unisa.interfacce.ManagerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ManagerImpl extends UnicastRemoteObject implements ManagerInterface {

    protected ManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public void aggiungiImpiegato(Impiegato E) {
        //TODO: chiamata al DAO per gli impiegati
    }

    @Override
    public boolean eliminaImpiegato(Impiegato E) {
        //TODO: chiamata al DAO
        return false;
    }

    @Override
    public String generatePassword() {
        return "";
    }

    @Override
    public void modificaDatiImpiegato(Impiegato E) {
        //TODO: chiamata al DAO verrà passato l'impiegato già modificato
    }

    @Override
    public List<Double> calcolaContoHotel() {
        return List.of();
    }
}
