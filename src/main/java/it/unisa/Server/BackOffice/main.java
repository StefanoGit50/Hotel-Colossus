package it.unisa.Server.BackOffice;

public class main {
    public static void main(String[] args) {

       // Prenotazioni
        ContoEconomicoComponentAbstract prenotazione1 = new ContoEconomicoComposite("Prenotazione 1");
        prenotazione1.addChild(new ContoEconomicoLeaf("Camera 101", 60, TipoVoce.CAMERA));
        prenotazione1.addChild(new ContoEconomicoLeaf("Servizio SPA", 30, TipoVoce.SERVIZIO));
        prenotazione1.addChild(new ContoEconomicoLeaf("Massaggio", 10,TipoVoce.SERVIZIO));

        ContoEconomicoComponentAbstract  prenotazione2 = new ContoEconomicoComposite("Prenotazione 2");
        prenotazione2.addChild(new ContoEconomicoLeaf("Camera 102", 120, TipoVoce.CAMERA));
        prenotazione2.addChild(new ContoEconomicoLeaf("Colazione", 50,TipoVoce.SERVIZIO));
        prenotazione2.addChild(new ContoEconomicoLeaf("Sauna", 30, TipoVoce.SERVIZIO));

// Composite prenotazioni
        ContoEconomicoComponentAbstract  prenotazioni = new ContoEconomicoComposite("Prenotazioni");
        prenotazioni.addChild(prenotazione1);
        prenotazioni.addChild(prenotazione2);

// RICAVI
        ContoEconomicoComponentAbstract  ricavi = new ContoEconomicoComposite("RICAVI");
        ricavi.addChild(prenotazioni);

// PASSIVITA
        ContoEconomicoComponentAbstract  stipendi = new ContoEconomicoComposite("Stipendi Personale");
        stipendi.addChild(new ContoEconomicoLeaf("Mario Rossi", 4000,TipoVoce.STIPENDI));
        stipendi.addChild(new ContoEconomicoLeaf("Laura Bianchi", 4000,TipoVoce.STIPENDI));

        ContoEconomicoLeaf manutenzione = new ContoEconomicoLeaf("Manutenzione Camere", 540, TipoVoce.ALTRO);

        ContoEconomicoComponentAbstract  passivita = new ContoEconomicoComposite("PASSIVITA");
        passivita.addChild(stipendi);
        passivita.addChild(manutenzione);

// CONTO ECONOMICO COMPLETO
        ContoEconomicoComponentAbstract  contoEconomico = new ContoEconomicoComposite("CONTO ECONOMICO");
        contoEconomico.addChild(ricavi);
        contoEconomico.addChild(passivita);
        contoEconomico.stampaAlbero("", true);

        double totalePrenotazioni = prenotazioni.getImportoTotale();
        double totaleCamere = prenotazioni.getTotalePerTipo(TipoVoce.CAMERA);
        double totaleServizi = prenotazioni.getTotalePerTipo(TipoVoce.SERVIZIO);
        double totaleTrattamenti = prenotazioni.getTotalePerTipo(TipoVoce.TRATTAMENTO);

        double totalePassivita = passivita.getImportoTotale();
        double totaleContoEconomico = contoEconomico.getImportoTotale();
        double totaleRicavi = ricavi.getImportoTotale();


        // ⚠ CORRETTO: conto economico = ricavi - passività
        double totaleVeroContoEconomico = totaleRicavi - totalePassivita;

        // -------------------
        // RIEPILOGO CONTABILE
        // -------------------
        System.out.println("\n===== RIEPILOGO CONTABILE =====");
        System.out.println("Totale Prenotazioni = " + totalePrenotazioni);
        System.out.println("Totale Camere = " + totaleCamere);
        System.out.println("Totale Servizi = " + totaleServizi);
        System.out.println("Totale Trattamenti = " + totaleTrattamenti);
        System.out.println("Totale Passività = " + totalePassivita);
        System.out.println("Totale Ricavi = " + totaleRicavi);
        System.out.println("Totale Conto Economico = " + totaleVeroContoEconomico);
    }
}
