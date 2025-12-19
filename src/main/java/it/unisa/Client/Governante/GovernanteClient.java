package it.unisa.gestionePrenotazioni.clients;

import it.unisa.gestionePrenotazioni.Interfacce.GestioneCamereInterface;
import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class GovernanteClient 
{
    static Logger logger = Logger.getLogger("global");
    
    public static void main(String[] args)
    {
        try 
        {
            logger.info("Sto cercando l' oggetto remoto Gestionecamere...");
            
            GestioneCamereInterface gestioneCamereInterface = (GestioneCamereInterface) Naming.lookup("rmi://localhost/GestoreCamere");
            logger.info("Trovato GestioneCamere! ...");
            
            int x=0;
            
            while(x==0)
            {
                System.out.println("Benvenuto nel Menu governante! \nScegli un opzione:");
                System.out.println("1. Pulisci camera\n2. Rendi libera camera\n3. Ottieni lista stanze\n0. Esci");
                
                Scanner sc = new Scanner(System.in);
                int scelta = sc.nextInt();
                
                switch(scelta)
                {
                    case 1: 
                    {
                        System.out.println("Inserisci numero camera da pulire: ");
                        Scanner sc2 = new Scanner(System.in);
                        gestioneCamereInterface.setOccupataPulizie(new Stanza(sc2.nextInt()));
                        break;
                    }
                    case 2: 
                    {
                        System.out.println("Inserisci numero camera da liberare: ");
                        Scanner sc2 = new Scanner(System.in);
                        gestioneCamereInterface.setLiberaPulizie(new Stanza(sc2.nextInt()));
                        break;
                    }
                    case 3:
                    {
                        List<Stanza> stanze = gestioneCamereInterface.getStanze();
                        
                        for(Stanza s: stanze)
                        {
                            System.out.println("Stanza: " + s.getNumero() + "   \n\tStato globale: " + s.getStatoGlobale() + "\n\tStato pulizie: " + s.getStatoPulizie());
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
