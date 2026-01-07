INSERT INTO Cliente (
    CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso,
    telefono, MetodoDiPagamento, Cittadinanza, DataDiNascita, IsBackListed
) VALUES
      ('RSSMRA90A01H501X','Mario','Rossi','00100','Roma',12,'RM','Via Nazionale','mario.rossi@email.it','Maschio','3331234567','Carta di Credito','Italiana','1990-01-01',FALSE),
      ('BNCLGU85C15F839Z','Giulia','Bianchi','20100','Milano',45,'MI','Corso Buenos Aires','giulia.bianchi@email.it','Femmina','3209876543','PayPal','Italiana','1985-03-15',FALSE),
      ('VRDLCA78L22F205Q','Luca','Verdi','80100','Napoli',7,'NA','Via Toledo','luca.verdi@email.it','Maschio','3495558899','Bonifico Bancario','Italiana','1978-07-22',TRUE),
      ('DMLFRN92E11D612A','Francesca','Damiani','50100','Firenze',23,'FI','Via dei Calzaiuoli','francesca.damiani@email.it','Femmina','3281122334','Carta di Debito','Italiana','1992-05-11',FALSE);

INSERT INTO Camera (NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo) VALUES
     (101, 2, 'Camera matrimoniale con vista giardino', 'Libera', 95.50),
     (102, 3, 'Camera tripla con balcone', 'Occupata', 120.00),                                                   (103, 1, 'Camera singola, silenziosa', 'Prenotata', 70.00),
     (104, 2, 'Camera doppia con aria condizionata', 'InPulizia', 100.00);

INSERT INTO Prenotazione (
    DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NoteAggiuntive,
    Intestatario, dataScadenza, numeroDocumento, DataRilascio, Tipo
) VALUES
      ('2026-01-01','2026-02-10','2026-02-15','Nessuna nota','Mario Rossi','2030-01-01',12345678,'2015-06-10','Carta Identità'),
      ('2026-01-03','2026-02-12','2026-02-14','Richiesta culla','Giulia Bianchi','2028-05-20',87654321,'2016-08-05','Passaporto'),
      ('2026-01-05','2026-03-01','2026-03-05','Arrivo tardivo','Luca Verdi','2032-03-10',11223344,'2010-12-15','Carta Identità'),
      ('2026-01-06','2026-01-20','2026-01-25','Colazione inclusa','Francesca Damiani','2027-07-11',44332211,'2014-04-20','Passaporto');

INSERT INTO Impiegato (
    CF, Stipedio, Nome, Cognome, Cap, DataAssunzione, Telefono, Cittadinanza, EmailAziendale,
    Sesso, Ruolo, DataRilascio, TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento, DataScadenza, CF1
) VALUES
      ('VLDMRA85A01H501X',2500,'Marco','Valdini','00100','2020-05-01','3312233445','Italiana','marco.valdini@hotelcolossus.it','Maschio','FrontDesk','2018-01-15','Carta Identità','Via del Corso','RM','Roma',10,'AB1234567','2028-01-01',NULL),
      ('TNRGLI90B12F205X',2800,'Lina','Tonarelli','20100','2019-03-10','3285566778','Italiana','lina.tonarelli@hotelcolossus.it','Femmina','Manager','2017-11-20','Passaporto','Corso Vittorio','MI','Milano',12,'CD9876543','2027-03-10',NULL),
      ('BRGFRS92C15D612Y',2200,'Francesco','Borgiani','80100','2021-07-15','3391122334','Italiana','francesco.borgiani@hotelcolossus.it','Maschio','Governante','2019-06-25','Carta Identità','Via Chiaia','NA','Napoli',5,'EF1122334','2031-07-15',NULL),
      ('DMNFRN88D22H501Z',2300,'Francesca','Damiani','50100','2022-02-01','3374455667','Italiana','francesca.damiani@hotelcolossus.it','Femmina','FrontDesk','2020-03-05','Passaporto','Via Roma','FI','Firenze',7,'GH4455667','2030-02-01',NULL);


INSERT INTO Associato_a (
    CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto
) VALUES
      ('RSSMRA90A01H501X',101,1,95.50),
      ('BNCLGU85C15F839Z',102,2,120.00),
      ('VRDLCA78L22F205Q',103,3,70.00),
      ('DMLFRN92E11D612A',104,4,100.00);


INSERT INTO RicevutaFiscale (
    IDRicevutaFiscale, IDPrenotazione, Totale, DataEmissione
) VALUES
      (1,1,95.50,'2026-02-10'),
      (2,2,120.00,'2026-02-12'),
      (3,3,70.00,'2026-03-01'),
      (4,4,100.00,'2026-01-20');


INSERT INTO Servizio (
    Nome, Prezzo, IDPrenotazione
) VALUES
      ('Colazione',10.00,1),
      ('Spa',50.00,2),
      ('Parcheggio',15.00,3),
      ('NoleggioBici',8.00,4);


INSERT INTO Trattamento (
    Nome, Prezzo, IDPrenotazione
) VALUES
      ('Mezza Pensione',30.00,1),
      ('Pensione Completa',50.00,2),
      ('All Inclusive',70.00,3),
      ('Bed & Breakfast',15.00,4);

