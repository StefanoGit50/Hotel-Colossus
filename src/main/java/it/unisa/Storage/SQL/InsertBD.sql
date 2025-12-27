-- Inserimento clienti
INSERT INTO Cliente VALUES
('RSSMRA85M01H501X','Mario','Rossi','00100','Roma',12,'RM','Via Roma','mario.rossi@email.com','M','345678901','Carta di credito','Italiana',FALSE),
('VRDLGI90F45F205Y','Giulia','Verdi','20100','Milano',7,'MI','Corso Milano','giulia.verdi@email.com','F','345678902','Paypal','Italiana',FALSE),
('BNCLRD75D23B123Z','Luca','Bianchi','80100','Napoli',15,'NA','Via Napoli','luca.bianchi@email.com','M','345678903','Contanti','Italiana',FALSE);

-- Inserimento camere
INSERT INTO Camera VALUES
(101,2,'Camera matrimoniale con balcone','Disponibile',1,'Matrimoniale',120.0),
(102,3,'Camera tripla con vista mare','Occupata',1,'Tripla',150.0),
(201,4,'Suite con jacuzzi','Disponibile',2,'Suite',300.0);

-- Inserimento prenotazioni
INSERT INTO Prenotazione 
(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, Tipo)
VALUES
('2025-11-15','2025-12-20','2025-12-25','Nessuna','Mario Rossi','2025-12-01',123456,'2020-01-01','Carta Identità'),
('2025-11-20','2025-12-24','2025-12-26','Richiesta colazione','Giulia Verdi','2025-12-10',234567,'2018-05-10','Passaporto'),
('2025-11-22','2025-12-23','2025-12-28','Animali ammessi','Luca Bianchi','2025-12-15',345678,'2019-09-20','Carta Identità');

-- Inserimento associazioni
INSERT INTO Associato_a(CF,NumeroCamera,PrezzoAcquisto)VALUES
('RSSMRA85M01H501X',101,120.0),
('VRDLGI90F45F205Y',102,150.0),
('BNCLRD75D23B123Z',201,300.0);

-- Inserimento ricevute fiscali
INSERT INTO RicevutaFiscale(IDPrenotazione,DataEmissione,PrezzoTrattamento,PrezzoServizio,PrezzoCamera)VALUES
(1,'2025-12-01',50.0,10.0,120.0),
(2,'2025-12-02',70.0,15.0,150.0),
(3,'2025-12-03',100.0,50.0,null);

-- Inserimento servizi
INSERT INTO Servizio (nome,Prezzo)VALUES
('WiFi',10.0),
('Colazione',15.0),
('Spa',50.0);

-- Inserimento trattamenti
INSERT INTO Trattamento(nome,Prezzo,PrezzoAcquisto)VALUES
('Massaggio',50.0,50.0),
('Trattamento viso',70.0,70.0),
('Percorso benessere',100.0,100.0);

-- Inserimento impiegati
INSERT INTO Impiegato VALUES
('PLLMRA85M01H501A','Marco','Pallini','00100','2020-01-15','345678910','Italiana','marco.pallini@hotel.com','M','Receptionist','2015-05-10','Carta Identità','Via Roma','RM','Roma',10,111111,'2025-12-31','PLLMRA85M01H501A'),
('FRNGNN90F45F205B','Anna','Ferragni','20100','2019-06-20','345678911','Italiana','anna.ferragni@hotel.com','F','Cameriera','2010-03-15','Passaporto','Corso Milano','MI','Milano',5,222222,'2025-11-30','FRNGNN90F45F205B');
