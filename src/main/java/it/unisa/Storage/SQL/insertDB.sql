INSERT INTO Cliente VALUES
('RSSMRA90A01H501Z','Mario','Rossi','00100','Roma',10,'RM','Via Roma','mario.rossi@email.it','M','3331111111','Carta','Italiana','1990-01-01',false),
('VRDLGI85B12F205X','Luigi','Verdi','20100','Milano',22,'MI','Via Milano','luigi.verdi@email.it','M','3332222222','Contanti','Italiana','1985-02-12',false),
('BNCLRA92C23D612Y','Laura','Bianchi','80100','Napoli',5,'NA','Via Napoli','laura.bianchi@email.it','F','3333333333','Bonifico','Italiana','1992-03-23',false);

INSERT INTO Camera VALUES
(101,2,'Camera matrimoniale','Libera',120.00),
(102,3,'Camera tripla','Prenotata',150.00),
(103,1,'Camera singola','Occupata',80.00);


INSERT INTO Prenotazione
(DataPrenotazione,DataArrivoCliente,DataPartenzaCliente,NomeTrattamento,NoteAggiuntive,
 Intestatario,dataScadenza,numeroDocumento,DataRilascio,TipoDocumento,Stato)
VALUES
('2026-01-01','2026-01-10','2026-01-15','Pensione Completa','Nessuna','Mario Rossi','2030-01-01',123456789,'2020-01-01','CartaIdentità',true),
('2026-01-02','2026-01-12','2026-01-18','Mezza Pensione','Vista mare','Luigi Verdi','2031-02-02',987654321,'2021-02-02','Passaporto',true),
('2026-01-03','2026-01-20','2026-01-25','Solo Pernotto','Letto aggiuntivo','Laura Bianchi','2032-03-03',456789123,'2022-03-03','CartaIdentità',false);


INSERT INTO Associato_a VALUES
('RSSMRA90A01H501Z',101,1,600.00),
('VRDLGI85B12F205X',102,2,900.00),
('BNCLRA92C23D612Y',103,3,400.00);


INSERT INTO RicevutaFiscale VALUES
(1,1,'2026-01-15','Carta','2026-01-01',300.00,'Pensione Completa'),
(2,2,'2026-01-18','Contanti','2026-01-02',250.00,'Mezza Pensione'),
(3,3,'2026-01-25','Bonifico','2026-01-03',150.00,'Solo Pernotto');


INSERT INTO ClientiRicevuta VALUES
('RSSMRA90A01H501Z',1,'Mario','Rossi',true),
('VRDLGI85B12F205X',2,'Luigi','Verdi',true),
('BNCLRA92C23D612Y',3,'Laura','Bianchi',true);


INSERT INTO CameraRicevuta VALUES
(1,101,120.00),
(2,102,150.00),
(3,103,80.00);


INSERT INTO ServiziRicevuta VALUES
(1,'Spa',2,50.00),
(2,'Colazione',3,10.00),
(3,'Parcheggio',1,15.00);


INSERT INTO Servizio VALUES
('Spa',50.00,1),
('Colazione',10.00,2),
('Parcheggio',15.00,3);


INSERT INTO Trattamento VALUES
('Pensione Completa',300.00,1),
('Mezza Pensione',250.00,2),
('Solo Pernotto',150.00,3);


INSERT INTO Impiegato VALUES
('RSSMRA90A01H501Z',1800.00,'Mario','Rossi','00100','2020-01-01','3331111111','Italiana',
 'm.rossi@hotel.it','M','Manager','2015-01-01','CartaIdentità',
 'Via Roma','RM','Roma',10,'AA1234567','2030-01-01',NULL),

('VRDLGI85B12F205X',1400.00,'Luigi','Verdi','20100','2021-02-02','3332222222','Italiana',
 'l.verdi@hotel.it','M','FrontDesk','2016-02-02','Passaporto',
 'Via Milano','MI','Milano',22,'BB2345678','2031-02-02','RSSMRA90A01H501Z'),

('BNCLRA92C23D612Y',1300.00,'Laura','Bianchi','80100','2022-03-03','3333333333','Italiana',
 'l.bianchi@hotel.it','F','Governante','2017-03-03','CartaIdentità',
 'Via Napoli','NA','Napoli',5,'CC3456789','2032-03-03','RSSMRA90A01H501Z');


