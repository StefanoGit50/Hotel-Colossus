package it.unisa.Server;

import it.unisa.Common.Cliente;
import it.unisa.Storage.DAO.Eccezioni.*;

public class Controller{
    public static boolean controllaCliente(Cliente o){
        if(o.getCf().length() != 16){
            return false;
        }

        if(o.getNome().length() > 50 && o.getNome().isEmpty()){
            return false;
        }

        if(o.getCognome().length() > 50 && o.getCognome().isEmpty()){
            return false;
        }

        Integer cap = o.getCAP();

        if(cap.compareTo(10000) < 0 && cap.compareTo(99999) > 0){
            return false;
        }

        if(o.getComune().length() > 50 && o.getComune().isEmpty()){
            return false;
        }

        if(o.getProvincia().isEmpty() && o.getProvincia().length() > 50){
            return false;
        }

        if(o.getVia().isEmpty() && o.getVia().length() > 50){
            return false;
        }

        if(o.getEmail().isEmpty() && o.getEmail().length() > 100){
            return false;
        }

        if(o.getSesso().isEmpty() && o.getSesso().length() > 40){
            return false;
        }

        Integer intero = o.getNumeroTelefono();

        if(intero.compareTo(100000000) < 0 && intero.compareTo(999999999) > 0){
            return false;
        }

        if(o.getMetodoDiPagamento().isEmpty() && o.getMetodoDiPagamento().length() > 50){
            return false;
        }

        if(o.getCittadinanza().isEmpty() && o.getCittadinanza().length() > 50){
            return false;
        }

        return true;
    }



}
