package it.unisa.gestionePrenotazioni.clients;

import it.unisa.gestionePrenotazioni.gestioneCamere.Stanza;
import it.unisa.gestionePrenotazioni.gestionePrenotazioni.Cliente;
import it.unisa.gestionePrenotazioni.gestionePrenotazioni.Prenotazione;

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
            
            GestionePrenotazioniInterface gestionePrenotazioniInterface = (GestionePrenotazioniInterface) Naming.lookup("rmi://localhost/GestionePrenotazioni");
            logger.info("Trovato GestionePrenotazioni! ...");
            
            GestioneCamereInterface gestioneCamereInterface = (GestioneCamereInterface) Naming.lookup("rmi://localhost/GestoreCamere");
            logger.info("Trovato GestioneCamere! ...");
            
            int x=0;
            
            while(x==0)
            {
                System.out.println("Benvenuto nel Menu front desk! \nScegli un opzione:");
                System.out.println("1. Effettua prenotazione\n2. Rimuovi prenotazione\n3. Ottieni lista prenotazioni\n0. Esci");
                
                Scanner sc = new Scanner(System.in);
                int scelta = sc.nextInt();
                
                switch(scelta)
                {
                    case 1: 
                    {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                        Stanza s = new Stanza(sc2.nextInt());
                        
                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                        Cliente c = new Cliente(sc3.nextLine());
                        
                        String id = c.getNome() + "" + s.getNumero();
                        
                        gestionePrenotazioniInterface.effettuaPrenotazione(id, c, s);
                        
                        break;
                    }
                    case 2: 
                    {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                        Stanza s = new Stanza(sc2.nextInt());
                        
                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                        Cliente c = new Cliente(sc3.nextLine());
                        
                        String id = c.getNome() + "" + s.getNumero();
                        
                        gestionePrenotazioniInterface.cancellaPrenotazione(new Prenotazione(id, c, s));
                        
                        break;
                    }
                    case 3:
                    {
                        List<Prenotazione> prenotazioni = gestionePrenotazioniInterface.getPrenotazioni();
                        
                        for(Prenotazione p: prenotazioni)
                        {
                            System.out.println("Id: " + p.getId() + "   \n\tNumero stanza: " + p.getStanza().getNumero() + "\n\tNome cliente: " + p.getCliente().getNome());
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
