package it.unisa.Client.FrontDesk;

import it.unisa.Common.Camera;

import it.unisa.Common.Cliente;
import it.unisa.Common.Impiegato;
import it.unisa.Common.Prenotazione;
import it.unisa.Server.IllegalAccess;
import it.unisa.Server.command.CatalogoClientiCommands.AddClienteCommand;
import it.unisa.Server.command.CatalogoClientiCommands.UnBanCommand;
import it.unisa.Server.persistent.obj.catalogues.CatalogoClienti;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.Storage.DAO.CameraDAO;
import it.unisa.interfacce.FrontDeskInterface;
//import it.unisa.Server.gestioneClienti.Cliente;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class FrontDeskClient
{
    static Logger logger = Logger.getLogger("global");
    private static FrontDeskInterface frontDeskInterface;


    public static void startRMI() throws RemoteException, MalformedURLException, NotBoundException {
        logger.info("Sto cercando gli oggetti remoti GestionePrenotazioni e Gestionecamere...");

        FrontDeskInterface frontDeskInterface = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
        logger.info("T\n" +
                "           // GovernanteInterface governanteInterface = (GovernanteInterface) Naming.lookup(\"rmi://localhost/GestoreCamere\");\n" +
                "           // logger.info(\"Trovato GestioneCamere! ...\");\n" +
                "\n" +
                "            int x=0;\n" +
                "            \n" +
                "            while(x==0)\n" +
                "            {trovato GestionePrenotazioni! RMI REGISTRY ...");

        System.out.println("Benvenuto nel Menu front desk! \nScegli un opzione:");
        System.out.println("1. Effettua prenotazione\n2. Rimuovi prenotazione\n3. Ottieni lista prenotazioni \n4. modifica stato camera \n5. Visualizza lista attuale delle camere \n0. Esci");

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

                        //Camera s = new Camera(sc2.nextInt());
                        
                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                      //  Cliente c = new Cliente(sc3.nextLine());

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
                        List<Prenotazione> prenotazioni = frontDeskInterface.getPrenotazioni();
                        
                        for(Prenotazione p: prenotazioni)
                        {
              //   System.out.println("Id: " + p.getId() + "   \n\tNumero stanza: " + p.getStanza().getNumero() + "\n\tNome cliente: " + p.getCliente().getNome());

                        }
                        break;
                    }
                    case 4:
                    {
                        List<Camera> camList= frontDeskInterface.getCamere();
                        IO.println(camList.toString());
                        System.out.println("Inserisci numero camera da modificare: ");
                        Scanner sc2 = new Scanner(System.in);
                        int n=sc2.nextInt();

                        boolean flag=false;

                        for(Camera c: camList){
                            if(c.getNumeroCamera()== n){
                                c.setStatoCamera(Stato.Occupata);
                                boolean b = frontDeskInterface.aggiornaStatoCamera(c);
                                IO.println("Camera result : "+b);
                                flag=true;
                            }
                        }

                        if(!flag){
                            IO.println("Camera non trovata inserire una camera nella lista");
                            break;
                        }
                        Camera c= frontDeskInterface.update();
                        IO.println("Camera mandata dal server = " +c.toString());
                        //aggiorna gui da qui

                        break;
                    }

                    case 5:
                    {
                        List<Camera> camList= frontDeskInterface.getCamere();
                        IO.println(camList.toString());
                        break;
                    }
                    case 6:{
                        CameraDAO cameraDAO = new CameraDAO();
                        ArrayList<Camera> cameras = (ArrayList<Camera>) frontDeskInterface.getCamere();
                        IO.println("Questo è l'oggetto" + cameras.getFirst());
                        IO.println("Sto salvando nel DB ...");
                        cameraDAO.doSave(cameras.getFirst());
                        IO.println("Salvataggio effettuato");
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
    public void FrontDeskController(FrontDeskInterface server) {
       //this.server = server;
    }

    public List<Camera> getCamere() throws RemoteException {
        //return server.getCamere();
        return null;
    }

    public boolean aggiornaStatoCamera(int numeroCamera, String nuovoStato) throws RemoteException {
        //List<Camera> camere = server.getCamere();

        //for (Camera c : camere) {
          //  if (c.getNumeroCamera() == numeroCamera) {
            //    c.setStatoCamera(nuovoStato);
              //  return server.aggiornaStatoCamera(c);
            //}
        //}
        return false;  // Camera non trovata
    }

    public Camera getUltimoAggiornamento() throws RemoteException {
        //return server.update();
        return null;
    }

    public List<Prenotazione> getPrenotazioni() throws RemoteException {
      //  return server.getPrenotazioni();
        return null;
    }

    public void addPrenotazione(Prenotazione p) throws RemoteException {
       // server.addPrenotazione(p);
    }

    public Impiegato DoAuthentication(String username, String password, String pwd2) throws RemoteException, IllegalAccess {
        return frontDeskInterface.authentication(username, password, pwd2);
    }
}
