package it.unisa.Client.FrontDesk;

import it.unisa.Common.Camera;
import it.unisa.Server.gestionePrenotazioni.FrontDesk;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;
import it.unisa.interfacce.FrontDeskInterface;
import it.unisa.Server.gestioneClienti.Cliente;
import it.unisa.Server.gestionePrenotazioni.Prenotazione;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class FrontDeskClient 
{
    static Logger logger = Logger.getLogger("global");
    
    public static void main(String[] args)
    {
        try 
        {
            logger.info("Sto cercando gli oggetti remoti GestionePrenotazioni e Gestionecamere...");
            
            FrontDeskInterface frontDeskInterface = (FrontDeskInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
            logger.info("Trovato GestionePrenotazioni! RMI REGISTRY ...");
            
           // GovernanteInterface governanteInterface = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
            //logger.info("Trovato GestioneCamere! ...");
            
            int x=0;
            
            while(x==0)
            {
                System.out.println("Benvenuto nel Menu front desk! \nScegli un opzione:");
                System.out.println("1. Effettua prenotazione\n2. Rimuovi prenotazione\n3. Ottieni lista prenotazioni \n4. modifica stato camera \n0. Esci");
                
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
                        Cliente c = new Cliente(sc3.nextLine());
                        
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
                        Cliente c = new Cliente(sc3.nextLine());
                        
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
                        System.out.println("Inserisci numero camera da modificare: ");
                        List<Camera> camList= frontDeskInterface.getCamere();
                        IO.println(camList.toString());
                        Scanner sc2 = new Scanner(System.in);
                        int n=sc2.nextInt();

                        if (camList.isEmpty()){
                            System.out.println("Inserisci numero camera esistente");
                            break;
                        }
                        else{

                            for(Camera c: camList){
                                if(c.getNumeroCamera()== n){
                                    c.setStatoCamera(Stato.Occupata);
                                    boolean b = frontDeskInterface.aggiornaStatoCamera(c);
                                    IO.println("Camera result : "+b);
                                    frontDeskInterface.up
                                }
                            }
                        }
                        break;
                    }
                    case 0:
                        x++;
                        break;
                        
                    default:
                    {
                        System.err.println("Scelta errata!");
                    }
                }
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
