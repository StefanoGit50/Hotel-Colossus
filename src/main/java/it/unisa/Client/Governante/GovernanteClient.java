package it.unisa.Client.Governante;

import it.unisa.Common.Camera;

import it.unisa.Server.Eccezioni.IllegalAccess;
import it.unisa.interfacce.FrontDeskInterface;
import it.unisa.interfacce.GovernanteInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Client per la gestione delle operazioni della governante.
 * Permette di modificare lo stato delle camere e visualizzare la lista delle stanze.
 */

public class GovernanteClient {
    static Logger logger = Logger.getLogger("global");
    private static GovernanteInterface governanteInterface;
    private static final boolean flag= false;

    public GovernanteClient(){
        if(!flag)startRMI();
    }

    private static void startRMI()  {
        logger.info("Sto cercando gli oggetti remoti GestionePrenotazioni e Gestionecamere...");
        try {
            governanteInterface = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
            logger.info("oggetto trovato.");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("ATTENZIONE: Impossibile connettersi al Server RMI.");
            e.printStackTrace();
        }
    }

    /**
     * Metodo principale che avvia il client della governante.
     * Stabilisce la connessione RMI con il server e presenta un menu interattivo
     * che permette di:
     * Pulire una camera
     * Rendere libera una camera
     * Rendere fuori servizio (outOfOrder) una camera
     * Rendere in servizio una camera
     * Rendere in pulizia una camera
     * Visualizzare la lista delle stanze
     *
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args)
    {
        try 
        {
            logger.info("Sto cercando l' oggetto remoto Gestionecamere...");
            
            GovernanteInterface governanteInterface = (GovernanteInterface) Naming.lookup("rmi://localhost/GestoreCamere");
            logger.info("Trovato GestioneCamere! ...");
            
            int x=0;
            
            while(x==0)
            {
                System.out.println("Benvenuto nel Menu governante! \nScegli un opzione:");
                System.out.println("1. Visualizza lista camere\n" +
                        "2. Rendi libera camera\n" +
                        "3. Rendi outOfOrder camera\n" +
                        "4. Rendi in servizio camera\n" +
                        "5. Rendi in pulizia camera\n" +
                        "0. Esci");
                
                Scanner sc = new Scanner(System.in);
                int scelta = sc.nextInt();
                
                switch(scelta)
                {
                    case 1:
                    {

                        List<Camera> camere = governanteInterface.getListaCamere();
                       System.out.println("Inserisci 1 per visualizzare le camere: ");
                        Scanner sc2 = new Scanner(System.in);



                        for(Camera c: camere)
                        {
                          //  System.out.println("Stanza: " + governanteInterface.getCamera(c.getNumeroCamera()).getNumeroCamera() + "   \n\tStato: " + governanteInterface.getCamera(c.getNumeroCamera()).getStatoCamera());
                        }
                        break;
                    }
                    case 2: 
                    {
                        System.out.println("Inserisci numero camera da rendendere libera: ");
                        Scanner sc2 = new Scanner(System.in);
                      //  TODO: governanteInterface.setStatoLibera(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 3:
                    {
                        System.out.println("Inserisci numero camera da rendere out of order: ");
                        Scanner sc2 = new Scanner(System.in);
                        //TODO: governanteInterface.setStatoOutOfOrder(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 4:
                    {
                        System.out.println("Inserisci numero camera da rendere in servizio: ");
                        Scanner sc2 = new Scanner(System.in);
                        //TODO: governanteInterface.setStatoInServizio(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 5:
                    {
                        System.out.println("Inserisci numero camera da pulire: ");
                        Scanner sc2 = new Scanner(System.in);
                        // TODO: governanteInterface.setStatoInPulizia(governanteInterface.getCamera(sc2.nextInt()));
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

    public boolean aggiornaStatoCamera(Camera c) {
        if (c == null) return false;

        try {
            governanteInterface.aggiornaStatoCamera(c);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Camera> getListaCamere() throws RemoteException, IllegalAccess {
        return governanteInterface.getListaCamere();
    }

}
