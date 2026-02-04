package it.unisa.Server.Autentication;


import it.unisa.Common.Impiegato;
import it.unisa.Storage.DAO.ImpiegatoDAO;

public class main {

    public static void main(String[] args) {
        Autentication a = new Autentication("asdadfh12","Receptionist1", null);

        System.out.println(CredenzialiUtils.HashPassword("Receptionist123!"));
        System.out.println(CredenzialiUtils.HashPassword("Governante123!"));

        Impiegato impiegato = new Impiegato();

        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();
        try{
            impiegato =  impiegatoDAO.doRetriveByKey("VRDLCU90B45F205Z");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(CredenzialiUtils.checkPassword("Receptionist123!",impiegato.getHashPassword()));
    }
}

