package it.unisa.Client.FrontDesk;

import it.unisa.Common.*;

import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.CatalogoClientiCommands.AddClienteCommand;
import it.unisa.Server.command.CatalogoClientiCommands.UnBanCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.Storage.DAO.ImpiegatoDAO;
import it.unisa.interfacce.FrontDeskInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import it.unisa.Server.gestioneClienti.Cliente;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FrontDeskClient
{
    private static Logger logger = LogManager.getLogger(FrontDeskClient.class);
    private static FrontDeskInterface frontDeskInterface;


    public FrontDeskClient(){
            startRMI();
    }


    private static void startRMI()  {
        logger.info("Sto cercando gli oggetti remoti GestionePrenotazioni e Gestionecamere...");
        try {
        frontDeskInterface = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
            logger.info("oggetto trovato.");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("ATTENZIONE: Impossibile connettersi al Server RMI.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try 
        {

                startRMI();
                Scanner sc = new Scanner(System.in);
                int scelta = sc.nextInt();
                
                switch(scelta)
                {
                    case 1: 
                    {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);

                        //String id = c.getNome() + "" + s.getNumero();
                        
                        //frontDeskInterface.effettuaPrenotazione(id, c, s);
                        
                        break;
                    }
                    case 2: 
                    {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                      //  Stanza s = new Stanza(sc2.nextInt());

                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                       // Cliente c = new Cliente(sc3.nextLine());

                       // String id = c.getNome() + "" + s.getNumero();
                        
                        //frontDeskInterface.cancellaPrenotazione(new Prenotazione(id, c, s));
                        
                        break;
                    }
                    case 3:
                    {
                        List<Prenotazione> prenotazioni = frontDeskInterface.getListaPrenotazioni();
                        
                        for(Prenotazione p: prenotazioni)
                        {
                     //System.out.println("Id: " + p.getId() + "   \n\tNumero stanza: " + p.getStanza().getNumero() + "\n\tNome cliente: " + p.getCliente().getNome());

                        }
                        break;
                    }
                    case 4:
                    {
                        List<Camera> camList= frontDeskInterface.getListaCamere();
                        System.out.println(camList.toString());
                        System.out.println("Inserisci numero camera da modificare: ");
                        Scanner sc2 = new Scanner(System.in);
                        int n=sc2.nextInt();

                        boolean flag=false;

                        for(Camera c: camList){
                            if(c.getNumeroCamera()== n){
                                c.setStatoCamera(Stato.Occupata);
                                boolean b = frontDeskInterface.aggiornaStatoCamera(c);
                                System.out.println("Camera result : "+b);
                                flag=true;
                            }
                        }

                        if(!flag){
                            System.out.println("Camera non trovata inserire una camera nella lista");
                            break;
                        }
                        Camera c= frontDeskInterface.update();
                        System.out.println("Camera mandata dal server = " +c.toString());
                        //aggiorna gui da qui

                        break;
                    }

                    case 5:
                    {
                        List<Camera> camList= frontDeskInterface.getListaCamere();
                        System.out.println(camList.toString());
                        break;
                    }
                    case 6:{
                        CameraDAO cameraDAO = new CameraDAO();
                        ArrayList<Camera> cameras = (ArrayList<Camera>) frontDeskInterface.getListaCamere();
                        System.out.println("Questo è l'oggetto" + cameras.getFirst());
                        System.out.println("Sto salvando nel DB ...");
                        cameraDAO.doSave(cameras.getFirst());
                        System.out.println("Salvataggio effettuato");
                        break;
                    }
                    case 7:{
                        //frontDeskInterface.effettuaPrenotazione("7" , new Cliente("Stefano") , new Camera(4 ,Stato.Libera,4, 200.0 , ""));
                        AddClienteCommand addClienteCommand =
                                new AddClienteCommand(new it.unisa.Common.Cliente("Francesco" , "Moretto", "Salerno" , "Capezzano" , "corso armando diaz " , 10 , 84054 , "324677894018" , "Maschio" , LocalDate.of(2016,12,30) , "MRTFCS16T30H703C"  ,"Cartaceo","Italiana",new Camera()));
                        addClienteCommand.execute();
                        //BanCommand banCommand = new BanCommand(new CatalogoClienti() , "MRTFCS16T30H703C");
                        UnBanCommand unBanCommand = new UnBanCommand("MRTFCS16T30H703C");
                        unBanCommand.execute();
                    }
                    case 0:{
                        //x++;
                        break;
                    } default: {
                        System.err.println("Scelta errata!");
                    }
                }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Camera> getListaCamere() throws RemoteException, IllegalAccess {
        return frontDeskInterface.getListaCamere();
    }

    public boolean aggiornaStatoCamera(int numeroCamera, String nuovoStato) throws RemoteException, IllegalAccess {
        List<Camera> cameras = null;
        try{
            cameras = frontDeskInterface.getListaCamere();
        }catch (RemoteException remoteException){
            remoteException.getStackTrace();
            remoteException.getMessage();
        }


        Camera camera1 = null;

        for(Camera camera : cameras){
                if(camera.getNumeroCamera() == numeroCamera){
                    try {
                        camera1 = camera.clone();
                    }catch(CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }
        }

        if(camera1 != null){
            return frontDeskInterface.aggiornaStatoCamera(camera1);
        }else{
            return false;
        }
    }

    /*
    public Camera getUltimoAggiornamento() throws RemoteException {

    }*/

    public List<Prenotazione> getListaPrenotazioni() {
        List<Prenotazione> prenotaziones = null;
        try{
           prenotaziones = frontDeskInterface.getListaPrenotazioni();
        }catch (RemoteException | IllegalAccess exception){
            exception.getStackTrace();
            exception.getMessage();
        }
        return prenotaziones;
    }

    public String addPrenotazione(Prenotazione p)  {
        String message="";
        try{
            frontDeskInterface.addPrenotazione(p);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }
    public String removePrenotazione(Prenotazione p) {
        String message="";
        try{
            frontDeskInterface.removePrenotazione(p);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;

    }

    public String updatePrenotazione(Prenotazione p)  {
        String message="";
        try{
            frontDeskInterface.updatePrenotazione(p);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public String addCliente(Cliente c) {
        String message="";
        try{
            frontDeskInterface.addCliente(c);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public String removeCliente(Cliente c) {
        String message="";
        try{
            frontDeskInterface.removeCliente(c);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public String updateCliente(Cliente c) {
        String message="";
        try{
            frontDeskInterface.updateCliente(c);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public List<Servizio>getServizi() {
        List<Servizio> servizi = null;
        try{
            servizi=frontDeskInterface.getListaServizi();
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            throw  new RuntimeException(illegalAccessException.getMessage());
        }
        return servizi;
    }

    public List<Trattamento> getTrattamenti() {
        List<Trattamento> trattamento = null;
        try{
            trattamento=frontDeskInterface.getListaTrattamenti();
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            throw  new RuntimeException(illegalAccessException.getMessage());
        }
        return trattamento;
    }

    public List<Cliente> getListaClienti(){

        List<Cliente> clienti = null;
        try{
            clienti=frontDeskInterface.getListaClienti();
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            throw new RuntimeException(illegalAccessException.getMessage());
        }
        return clienti;
    }


    public String banCliente(Cliente c){
        String message="";
        try{
            frontDeskInterface.banCliente(c);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public String unBanCliente(Cliente c){
        String message="";
        try{
            frontDeskInterface.unBanCliente(c);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }catch (IllegalAccess illegalAccessException){
            message = illegalAccessException.getMessage();
        }
        return message;
    }

    public Impiegato DoAuthentication(String username, String password, String pwd2) throws RemoteException, IllegalAccess {
        Impiegato imp = null;
        try{
            if (frontDeskInterface == null) {
                logger.warn("Errore: Non sei connesso al server.");
                return null;
            }

            System.out.println("nel client username e password" + username+" " +password);
             imp =frontDeskInterface.authentication(username, password, pwd2);
            System.out.println(imp);
        }catch (RemoteException remoteException){
            remoteException.getMessage();
            remoteException.getStackTrace();
        }
        return imp;
    }



}
