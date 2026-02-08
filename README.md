# Hotel-Colossus
Development team of HotelColossus software

**ISTRUZIONI PER IL CORRETTO RUN DEL PROGETTO**
Il progetto per essere compilato ha bisogno dei seguenti prerequisiti
-JAVA VERSION 21 o superiore
-JDBC MYSQL : impostare una password nella classe connectorStorage deve essere la stessa che si usa nel DB, bisogna inoltre creare il pool di connessioni abbinando un datasource al progetto.

**ESECUZIONE**
-come prima condizione assicurarsi che il DB sia connesso e che tutte le create e insert nel pacchetto Server/Storage/SQL siano state eseguite   
-avviare la classe Server GestionePrenotazione--> FrontDesk. 
-Dopo che si sarà avviata impostare con maven EditConfiguration --> Commandline --> mvn javaFx:run
Quest ultimo comando avvia l'interfaccia grafica e chiama il client per connettersi tramite RMI al server
Se non ci sono errori il servizio di RMi sarà in running 
Per il login entrare con queste credenziali Reception5 password Receptionist123!

**ESECUZIONE TEST**
Questo progetto usa jacoco come tool di copertura per eseguire i test fare le seguenti operazioni
mvn Lifecicle --> clean --> compile --> test.
il plugin genererà la coverage nei file di compilazione di maven

**DIPENDENZE USATE**
-mybatisDB: permette di resettare il db dopo ogni test
-junit e mockito : test di unità , test di integrazione, test di sistema
-mysqlConnector: connessione driver jdbc
javaFX : framework grafico di java per interfacce avanzate. 
