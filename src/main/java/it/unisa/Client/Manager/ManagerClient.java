package it.unisa.Client.Manager;

import it.unisa.Common.Cliente;
import it.unisa.Common.Prenotazione;
import it.unisa.interfacce.FrontDeskInterface;
import it.unisa.interfacce.GovernanteInterface;
import it.unisa.interfacce.ManagerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ManagerClient {

    static Logger logger = Logger.getLogger("global");
    private static ManagerInterface managerInterface;

    public static void main(String[] args) {
        try {
           startRMI();

            int x = 0;

            while (x == 0) {
                System.out.println("Benvenuto nel Menu Manager! \nScegli un opzione:");
                System.out.println("1. Effettua prenotazione\n2. Rimuovi prenotazione\n3. Ottieni lista prenotazioni\n0. Esci");

                Scanner sc = new Scanner(System.in);
                int scelta = sc.nextInt();

                switch (scelta) {
                    case 1: {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                        //  Camera camera = new Camera(sc2.nextInt());

                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                        //Cliente c = new Cliente(sc3.nextLine());

                        //  String id = c.getNome() + "" + camera.getNumeroCamera();

                        //frontDeskInterface.effettuaPrenotazione(id, c, camera);

                        break;
                    }
                    case 2: {
                        System.out.println("Inserisci numero camera: ");
                        Scanner sc2 = new Scanner(System.in);
                        // Camera s = new Camera(sc2.nextInt());

                        System.out.println("Inserisci nome cliente: ");
                        Scanner sc3 = new Scanner(System.in);
                        // Cliente c = new Cliente(sc3.nextLine());

                        //String id = c.getNome() + "" + s.getNumeroCamera();

                        //  frontDeskInterface.cancellaPrenotazione(new Prenotazione(id, c, s));

                        break;
                    }
                    case 3: {



                    }
                    case 0:
                        x++;
                        break;

                    default: {
                        System.err.println("Scelta errata!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startRMI() throws RemoteException, MalformedURLException, NotBoundException {
        logger.info("Sto cercando l'oggetto remoto GestoreImpiegati ...");

        ManagerInterface managerInterface = (ManagerInterface) Naming.lookup("rmi://localhost/gestioneImpiegati");
        logger.info("T\n" +
                "           // GovernanteInterface governanteInterface = (GovernanteInterface) Naming.lookup(\"rmi://localhost/GestoreImpiegati\");\n" +
                "           // logger.info(\"Trovato GestioneCamere! ...\");\n" +
                "\n" +
                "            int x=0;\n" +
                "            \n" +
                "            while(x==0)\n" +
                "            {trovato GestionePrenotazioni! RMI REGISTRY ...");

    }

    public static String doGeneratePassword() throws RemoteException {
       return managerInterface.generatePassword();
    }

}


