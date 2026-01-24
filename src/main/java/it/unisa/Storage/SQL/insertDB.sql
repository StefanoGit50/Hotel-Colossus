-- CLIENTI
INSERT INTO Cliente VALUES
                        ('RSSMRA90A01H501Z','Mario','Rossi','80100','Napoli',10,'NA','Via Roma','mario.rossi@mail.it','M','3331234567','Italiana','1990-01-01',false),
                        ('VRDLGI85B22F839X','Luigi','Verdi','84013','Cava',20,'SA','Via Milano','luigi.verdi@mail.it','M','3399876543','Italiana','1985-02-22',false);

-- CAMERE
INSERT INTO Camera VALUES
                       (101,2,'Camera matrimoniale','Libera',80.00),
                       (102,4,'Camera familiare','Prenotata',150.00);

-- PRENOTAZIONI
INSERT INTO Prenotazione VALUES
                             (1,'2026-01-10','2026-02-01','2026-02-05','Pensione Completa','Nessuna','Mario','2030-01-01',123456,'2020-01-01','CI',true,false),
                             (2,'2026-01-15','2026-03-10','2026-03-15','Mezza Pensione','Vista mare','Luigi','2031-01-01',654321,'2021-01-01','Passaporto',true,false);

-- ASSOCIAZIONI
INSERT INTO Associato_a VALUES
                            ('RSSMRA90A01H501Z',101,1,320.00),
                            ('VRDLGI85B22F839X',102,2,750.00);

-- TRATTAMENTI
INSERT INTO Trattamento VALUES
                            ('Pensione Completa',40.00,1),
                            ('Mezza Pensione',25.00,2);

-- SERVIZI
INSERT INTO Servizio VALUES
                         ('Spa',50.00,1),
                         ('Navetta',20.00,2);

-- RICEVUTE
INSERT INTO RicevutaFiscale VALUES
                                (1,1,'2026-02-01','Carta','2026-01-10',160.00,'Pensione Completa'),
                                (2,2,'2026-03-10','Contanti','2026-01-15',125.00,'Mezza Pensione');

-- CLIENTI RICEVUTA
INSERT INTO ClientiRicevuta VALUES
                                ('RSSMRA90A01H501Z',1,'Mario','Rossi',true),
                                ('VRDLGI85B22F839X',2,'Luigi','Verdi',true);

-- CAMERE RICEVUTA
INSERT INTO CameraRicevuta VALUES
                               (1,101,320.00),
                               (2,102,750.00);

-- SERVIZI RICEVUTA
INSERT INTO ServiziRicevuta VALUES
                                (1,'Spa',1,50.00),
                                (2,'Navetta',1,20.00);

-- IMPIEGATI
INSERT INTO Impiegato VALUES
                          ('BNCLRA80A01F839T',1500,'Laura','Bianchi','80100','2020-01-01','3332221111','Italiana','l.bianchi@hotel.it','F','FDclient',
                           '2019-01-01','CI','Via Toledo','NA','Napoli',5,'AA1234567','2030-01-01',NULL),
                          ('RSSLNZ75C10H501Y',2500,'Lorenzo','Russo','80100','2018-01-01','3345556666','Italiana','l.russo@hotel.it','M','Manager',
                           '2018-01-01','CI','Via Chiaia','NA','Napoli',12,'BB7654321','2030-01-01','BNCLRA80A01F839T');
