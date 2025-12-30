package it.unisa.Client.Governante;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.util.Stato;
import it.unisa.interfacce.GovernanteInterface;
import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Client per la gestione delle operazioni della governante.
 * Permette di modificare lo stato delle camere e visualizzare la lista delle stanze.
 */

public class GovernanteClient 
{
    static Logger logger = Logger.getLogger("global");

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
                        List<Camera> camere = governanteInterface.getListCamere();

                        for(Camera c: camere)
                        {
                            System.out.println("Stanza: " + governanteInterface.getCamera(c.getNumeroCamera()).getNumeroCamera() + "   \n\tStato: " + governanteInterface.getCamera(c.getNumeroCamera()).getStatoCamera());
                        }
                        break;
                    }
                    case 2: 
                    {
                        System.out.println("Inserisci numero camera da rendendere libera: ");
                        Scanner sc2 = new Scanner(System.in);
                        governanteInterface.setStatoLibera(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 3:
                    {
                        System.out.println("Inserisci numero camera da rendere out of order: ");
                        Scanner sc2 = new Scanner(System.in);
                        governanteInterface.setStatoOutOfOrder(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 4:
                    {
                        System.out.println("Inserisci numero camera da rendere in servizio: ");
                        Scanner sc2 = new Scanner(System.in);
                        governanteInterface.setStatoInServizio(governanteInterface.getCamera(sc2.nextInt()));
                        break;
                    }
                    case 5:
                    {
                        System.out.println("Inserisci numero camera da pulire: ");
                        Scanner sc2 = new Scanner(System.in);
                        governanteInterface.setStatoInPulizia(governanteInterface.getCamera(sc2.nextInt()));
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
