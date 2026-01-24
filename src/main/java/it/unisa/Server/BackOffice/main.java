package it.unisa.Server.BackOffice;

import it.unisa.Common.*;
import it.unisa.Server.persistent.obj.catalogues.CatalogoPrenotazioni;
import it.unisa.Server.persistent.util.Ruolo;
import it.unisa.Server.persistent.util.Stato;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class main {

    private static <T> ContoEconomicoComposite creaNodoeFoglie(String nomeComposite,
                                                      List<T> sottolista,
                                                      Function<T,String> nomeVoce,
                                                      Function<T,Double> importo,
                                                      Function<T,TipoVoce> tipo)
    {
        ContoEconomicoComposite contoEconomico = new ContoEconomicoComposite(nomeComposite);

        if(sottolista!=null && !sottolista.isEmpty() ){
            sottolista.forEach(e-> contoEconomico.
                    addChild(new ContoEconomicoLeaf(nomeVoce.apply(e), importo.apply(e), tipo.apply(e))));
        }
        return contoEconomico;
    }


    public static void main(String[] args) {

        ArrayList<Servizio> listaServizi = new ArrayList<Servizio>();
        listaServizi.add(new Servizio("Spa",325));
        ArrayList<Camera> c = new ArrayList<Camera>();
        c.add(new Camera(112, Stato.Occupata,2,50.5,"o babba"));
        ArrayList<Cliente> clist= new ArrayList<>();
        Cliente cliente = new Cliente("Luca",
                "Marole",
                "Italiana",
                "Napoli",
                "NA",
                "via barbone",
                2,
                9076,
                "3233452",
                "m",
                LocalDate.of(2022, 1, 6),
                "SDFGANNSOLF","Libero@asfnai");
        clist.add(cliente);

        Prenotazione p1 = new Prenotazione(1,
                LocalDate.now(),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 15),
                new Trattamento("MEZZA PENSIONE",24 ),
                "PASSAPORTO",
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2030, 5, 20),
                cliente.getNome()+" "+cliente.getCognome(),
                "renato ti massaggiA",
                c,listaServizi,clist,34569);
        p1.setStatoPrenotazione(false);

        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();

        List<ContoEconomicoComposite> ComponentList =  new ArrayList<ContoEconomicoComposite>();

        ContoEconomicoComposite prenotazioni = new ContoEconomicoComposite("PRENOTAZIONI");

        CatalogoPrenotazioni.getListaPrenotazioni().add(p1);
        //mostra solo le prenotazioni completate
         for(Prenotazione p : CatalogoPrenotazioni.getListaPrenotazioni()){
             if(!p.getStatoPrenotazione()){
                ContoEconomicoComposite prenotazioneComposite = new ContoEconomicoComposite("PRENOTAZIONE"+p.getIDPrenotazione()+" "+p.getIntestatario());

                // creazione composite CAMERE
                 if(!p.getListaCamere().isEmpty() && p.getListaCamere()!= null) {
                   ContoEconomicoComposite camere= creaNodoeFoglie("CAMERE",p.getListaCamere(),room->"CAMERA"+ room.getNumeroCamera(),
                               room-> room.getPrezzoCamera(),room-> TipoVoce.CAMERA);
                     prenotazioneComposite.addChild(camere);
                 }
                   //creazione composite SERVIZI
                 if (!p.getListaServizi().isEmpty() && p.getListaServizi()!= null) {
                     ContoEconomicoComposite servizi = creaNodoeFoglie("SERVIZI", p.getListaServizi(), s -> s.getNome(),
                             s -> s.getPrezzo(), s -> TipoVoce.SERVIZIO);
                     prenotazioneComposite.addChild(servizi);
                 }
                 if (p.getTrattamento() != null) {
                     prenotazioneComposite.addChild(new ContoEconomicoLeaf(
                             p.getTrattamento().getNome(),
                             p.getTrattamento().getPrezzo(),
                             TipoVoce.TRATTAMENTO
                     ));
                 }

                 // aggiunta della prenotazione al nodo prenotazioni
                 prenotazioni.addChild(prenotazioneComposite);
             }
         }

// RICAVI
        ContoEconomicoComponentAbstract  ricavi = new ContoEconomicoComposite("RICAVI");
        ricavi.addChild(prenotazioni);

// PASSIVITA
        List<Impiegato> listaImpiegati = new ArrayList<>();
        listaImpiegati.add(new Impiegato(
                "mrossi", "hashedPassword123", "Mario", "Rossi", "M", "Carta d'identità", "AB13334", 83734, "ViaRoma",
                "NA", "Napoli", 10, "RSSMRA80A01H501X", "3331234567", Ruolo.FrontDesk, 4000,
                LocalDate.of(2022, 6, 1),            // dataAssunzione
                LocalDate.of(2022, 6, 1),            // dataRilascio documento
                "mario.rossi@hotel.it",              // emailAziendale
                "Italiana",                           // cittadinanza
                LocalDate.of(2021, 3, 15)            // dataScadenza documento
        ));

        listaImpiegati.add(new Impiegato(
                "lbianchi",
                "hashedPassword456",
                "Laura",
                "Bianchi",
                "F",
                "Passaporto",
                "CD9876543",
                20100,
                "Via Milano",
                "MI",
                "Milano",
                25,
                "BNCLRA90B55F205Y",
                "3497654321",
                Ruolo.FrontDesk,
                2800.75,
                LocalDate.of(2023, 3, 15),          // dataAssunzione
                LocalDate.of(2023, 3, 15),          // dataRilascio documento
                "laura.bianchi@hotel.it",           // emailAziendale
                "Italiana",                          // cittadinanza
                LocalDate.of(2033, 3, 15)           // dataScadenza documento
        ));

// creo il composite stipendi
        ContoEconomicoComposite stipendi = creaNodoeFoglie(
                "Stipendi Personale",
                listaImpiegati,
                Impiegato::getNome,  //uso di method reference syntax
                Impiegato::getStipendio,
                i -> TipoVoce.STIPENDI
        );

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
