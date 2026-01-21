-- CLIENTE
INSERT INTO Cliente VALUES
                        ('MSCMRA00T04H703M','Mario','Masceri','84085','Mercato San Severino',12,'Salerno','Corso Armando Diaz','mario.masceri@gmail.com','Maschio','3249552034','Bancomat','Italiana','1899-12-04',false),
                        ('MSCMRA00T04H703H','Mario','Masceri','84085','Mercato San Severino',12,'Salerno','Corso Armando Diaz','mario.masceri@gmail.com','Maschio','3249552034','Carta','Italiana','1899-12-04',false),
                        ('RSSMRA80A01H501Z','Mario','Rossi','00100','Roma',10,'Roma','Via Roma','rossi@gmail.com','Maschio','3331112222','Contanti','Italiana','1980-01-01',false),
                        ('BNCLGI85B10F205X','Luigi','Bianchi','20100','Milano',5,'Milano','Via Milano','bianchi@gmail.com','Maschio','3334445555','Carta','Italiana','1985-02-10',false),
                        ('VRDLRA90C20D612Y','Laura','Verdi','80100','Napoli',7,'Napoli','Via Napoli','verdi@gmail.com','Femmina','3337778888','Bonifico','Italiana','1990-03-20',false);

-- CAMERA
INSERT INTO Camera VALUES
                       (101,2,'Non ce la faccio piu','Libera',120),
                       (102,3,'Non ce la faccio piu','Libera',110),
                       (103,2,'Vista mare','Libera',130),
                       (104,4,'Suite','Libera',200),
                       (105,1,'Singola','Libera',80);

-- PRENOTAZIONE (AUTO_INCREMENT)
INSERT INTO Prenotazione
(DataPrenotazione,DataArrivoCliente,DataPartenzaCliente,NomeTrattamento,NoteAggiuntive,Intestatario,
 dataScadenza,numeroDocumento,DataRilascio,TipoDocumento,Stato,ChekIn)
VALUES
    ('1992-12-20','1994-11-30','1990-09-20','asporto','Note test','Mario Masceri',
     '2004-12-02',112,'2000-12-02','Carta didentita ',true,false),
    ('2024-01-10','2024-02-01','2024-02-10','Pensione Completa','---','Rossi',
     '2024-01-20',201,'2019-05-10','Passaporto',true,false),
    ('2024-03-01','2024-03-10','2024-03-15','Mezza Pensione','---','Bianchi',
     '2024-03-05',301,'2018-04-10','CI',true,false),
    ('2024-04-01','2024-04-05','2024-04-07','Solo Pernotto','---','Verdi',
     '2024-04-02',401,'2020-07-01','CI',true,false),
    ('2024-05-01','2024-05-10','2024-05-20','All Inclusive','---','Neri',
     '2024-05-05',501,'2021-06-01','CI',true,false);

-- TRATTAMENTO
INSERT INTO Trattamento VALUES
                            ('Dasporto',22,1),
                            ('Pensione Completa',40,2),
                            ('Mezza Pensione',30,3),
                            ('Solo Pernotto',15,4),
                            ('All Inclusive',60,5);

-- SERVIZIO
INSERT INTO Servizio VALUES
                         ('Mensa',11,1),
                         ('Piscina',12,1),
                         ('ijjknk',15,1),
                         ('Spa',25,2),
                         ('Parcheggio',10,3);

-- ASSOCIATO_A
INSERT INTO Associato_a VALUES
                            ('MSCMRA00T04H703M',101,1,120),
                            ('MSCMRA00T04H703H',102,1,110),
                            ('RSSMRA80A01H501Z',103,2,130),
                            ('BNCLGI85B10F205X',104,3,200),
                            ('VRDLRA90C20D612Y',105,4,80);

-- RICEVUTAFISCALE
INSERT INTO RicevutaFiscale VALUES
                                (1,1,'1992-12-21','Bancomat','1992-12-20',22,'Dasporto'),
                                (2,2,'2024-01-11','Contanti','2024-01-10',40,'Pensione Completa'),
                                (3,3,'2024-03-02','Carta','2024-03-01',30,'Mezza Pensione'),
                                (4,4,'2024-04-02','Bonifico','2024-04-01',15,'Solo Pernotto'),
                                (5,5,'2024-05-02','Carta','2024-05-01',60,'All Inclusive');

-- CLIENTIRICEVUTA
INSERT INTO ClientiRicevuta VALUES
                                ('MSCMRA00T04H703M',1,'Mario','Masceri',true),
                                ('RSSMRA80A01H501Z',2,'Mario','Rossi',true),
                                ('BNCLGI85B10F205X',3,'Luigi','Bianchi',true),
                                ('VRDLRA90C20D612Y',4,'Laura','Verdi',true),
                                ('MSCMRA00T04H703H',1,'Mario','Masceri',false);

-- CAMERARICEVUTA
INSERT INTO CameraRicevuta VALUES
                               (1,101,120),
                               (1,102,110),
                               (2,103,130),
                               (3,104,200),
                               (4,105,80);

-- SERVIZIRICEVUTA
INSERT INTO ServiziRicevuta VALUES
                                (1,'Mensa',1,11),
                                (1,'Piscina',1,12),
                                (1,'ijjknk',1,15),
                                (2,'Spa',1,25),
                                (3,'Parcheggio',1,10);

-- IMPIEGATO
INSERT INTO Impiegato VALUES
                          ('AAAABBBBCCCCDDDD',1500,'Luigi','Neri','00100','2020-01-10','3331110000','Italiana','luigi@hotel.it','Maschio','Manager','2018-01-01','CI','Via Roma','Roma','Roma',10,'AA1234567','2028-01-01',NULL),
                          ('EEEEFFFFGGGGHHHH',1200,'Anna','Blu','20100','2021-02-10','3332220000','Italiana','anna@hotel.it','Femmina','FDclient','2019-02-02','CI','Via Milano','Milano','Milano',5,'BB1234567','2029-02-02',NULL),
                          ('IIIIJJJJKKKKLLLL',1300,'Paolo','Verdi','80100','2019-03-10','3333330000','Italiana','paolo@hotel.it','Maschio','Governante','2017-03-03','CI','Via Napoli','Napoli','Napoli',7,'CC1234567','2027-03-03',NULL),
                          ('MMMMNNNNOOOOPPPP',1400,'Sara','Rosa','50100','2022-04-10','3334440000','Italiana','sara@hotel.it','Femmina','FDclient','2020-04-04','CI','Via Firenze','Firenze','Firenze',8,'DD1234567','2030-04-04',NULL),
                          ('QQQQRRRRSSSSTTTT',2000,'Carlo','Gialli','10100','2018-05-10','3335550000','Italiana','carlo@hotel.it','Maschio','Manager','2016-05-05','CI','Via Torino','Torino','Torino',9,'EE1234567','2026-05-05','AAAABBBBCCCCDDDD');

INSERT INTO Cliente
(CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, MetodoDiPagamento, Cittadinanza, DataDiNascita, IsBackListed)
VALUES
    ('SMIJHN75D15Z114B','John','Smith','00121','Londra',45,'Estero','Baker Street',
     'john.smith@uk.com','Maschio','447123456','Carta di Credito','Inglese','1975-04-15',false);

INSERT INTO Camera
(NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (202,1,'Singola business','InPulizia',85);

INSERT INTO Servizio
(Nome, Prezzo, IDPrenotazione)
VALUES
    ('Transfer Aeroporto',40,NULL);


INSERT INTO Trattamento
(Nome, Prezzo, IDPrenotazione)
VALUES
    ('Massaggio Sportivo',70,NULL);

INSERT INTO Prenotazione
(DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento,
 NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio,
 TipoDocumento, Stato, ChekIn)
VALUES
    ('2023-11-15','2024-02-14','2024-02-16','Massaggio Sportivo',
     'Anniversario','John Smith','2032-12-10',901234,'2022-12-10',
     'Passaporto',true,false);



INSERT INTO Associato_a
(CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto)
VALUES
    ('SMIJHN75D15Z114B',202,6,85);

UPDATE Servizio
SET IDPrenotazione = 6
WHERE Nome = 'Transfer Aeroporto';

UPDATE Trattamento
SET IDPrenotazione = 6
WHERE Nome = 'Massaggio Sportivo';
