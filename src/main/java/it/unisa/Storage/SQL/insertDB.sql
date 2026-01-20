INSERT INTO Cliente (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, MetodoDiPagamento, Cittadinanza, DataDiNascita, IsBackListed) VALUES
                                                                                                                                                                       ('RSSMRA80A01H501U', 'Mario', 'Rossi', '00100', 'Roma', 10, 'RM', 'Via Roma', 'mario.rossi@email.it', 'Maschio', '3331234567', 'Carta di Credito', 'Italiana', '1980-01-01', false),
                                                                                                                                                                       ('BNCGNN85B10L219Z', 'Gianni', 'Bianchi', '20100', 'Milano', 5, 'MI', 'Corso Milano', 'gianni.b@email.it', 'Maschio', '3339876543', 'Contanti', 'Italiana', '1985-02-10', false),
                                                                                                                                                                       ('VERGVT90C20F205K', 'Giovanna', 'Verdi', '50100', 'Firenze', 22, 'FI', 'Via dei Fiori', 'gio.verdi@email.it', 'Femmina', '3401122334', 'Bonifico', 'Italiana', '1990-03-20', false),
                                                                                                                                                                       ('SMIJHN75D15Z114B', 'John', 'Smith', '00121', 'Londra', 45, 'Estero', 'Baker Street', 'john.smith@uk.com', 'Maschio', '447123456', 'Carta di Credito', 'Inglese', '1975-04-15', false),
                                                                                                                                                                       ('ESPFRN88E25F839O', 'Francesca', 'Esposito', '80100', 'Napoli', 1, 'NA', 'Via Toledo', 'f.esposito@email.it', 'Femmina', '3285566778', 'PayPal', 'Italiana', '1988-05-25', true);

INSERT INTO Camera (NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo) VALUES
                                                                               (101, 2, 'Vista mare', 'Libera', 120.00),
                                                                               (102, 2, 'Letto matrimoniale king', 'Occupata', 135.00),
                                                                               (201, 4, 'Suite familiare', 'Prenotata', 250.00),
                                                                               (202, 1, 'Singola business', 'InPulizia', 85.00),
                                                                               (301, 2, 'Accesso disabili', 'OutOfOrder', 110.00);

INSERT INTO Prenotazione (DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato) VALUES
                                                                                                                                                                                                          ('2023-10-01', '2023-12-20', '2023-12-27', 'Mezza Pensione', 'Nessuna', 'Mario Rossi', '2030-01-01', 123456, '2020-01-01', 'Carta d\'identità', true),
                                                                                                                                                                                                          ('2023-10-05', '2023-12-24', '2023-12-26', 'Pensione Completa', 'Allergia alle arachidi', 'Gianni Bianchi', '2031-05-15', 789012, '2021-05-15', 'Passaporto', true),
                                                                                                                                                                                                          ('2023-11-10', '2024-01-05', '2024-01-10', 'Bed & Breakfast', 'Piano alto', 'Giovanna Verdi', '2029-08-20', 345678, '2019-08-20', 'Patente', true),
                                                                                                                                                                                                          ('2023-11-15', '2024-02-14', '2024-02-16', 'All Inclusive', 'Anniversario', 'John Smith', '2032-12-10', 901234, '2022-12-10', 'Passaporto', true),
                                                                                                                                                                                                          ('2023-12-01', '2024-03-01', '2024-03-03', 'Solo Pernottamento', 'Arrivo tardi', 'Francesca Esposito', '2028-03-03', 567890, '2018-03-03', 'Carta d\'identità', true);

INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) VALUES
                                                                               ('RSSMRA80A01H501U', 101, 1, 120.00),
                                                                               ('BNCGNN85B10L219Z', 102, 2, 135.00),
                                                                               ('VERGVT90C20F205K', 201, 3, 250.00),
                                                                               ('SMIJHN75D15Z114B', 202, 4, 85.00),
                                                                               ('ESPFRN88E25F839O', 101, 5, 120.00);

INSERT INTO RicevutaFiscale (IDRicevutaFiscale, IDPrenotazione, DataEmissione, metodoPagamento, DataPrenotazione, PrezzoTrattamento, TipoTrattamento) VALUES
                                                                                                                                                          (1, 1, '2023-12-27', 'Carta di Credito', '2023-10-01', 150.00, 'Mezza Pensione'),
                                                                                                                                                          (2, 2, '2023-12-26', 'Contanti', '2023-10-05', 200.00, 'Pensione Completa'),
                                                                                                                                                          (3, 3, '2024-01-10', 'Bonifico', '2023-11-10', 80.00, 'Bed & Breakfast'),
                                                                                                                                                          (4, 4, '2024-02-16', 'Carta di Credito', '2023-11-15', 300.00, 'All Inclusive'),
                                                                                                                                                          (5, 5, '2024-03-03', 'PayPal', '2023-12-01', 0.00, 'Solo Pernottamento');

INSERT INTO ClientiRicevuta (CFCliente, IDRicevutaFiscale, NomeCliente, CognomeCliente, isIntestatario) VALUES
                                                                                                            ('RSSMRA80A01H501U', 1, 'Mario', 'Rossi', true),
                                                                                                            ('BNCGNN85B10L219Z', 2, 'Gianni', 'Bianchi', true),
                                                                                                            ('VERGVT90C20F205K', 3, 'Giovanna', 'Verdi', true),
                                                                                                            ('SMIJHN75D15Z114B', 4, 'John', 'Smith', true),
                                                                                                            ('ESPFRN88E25F839O', 5, 'Francesca', 'Esposito', true);

INSERT INTO CameraRicevuta (IDRicevutaFiscale, NumeroCamera, PrezzoCamera) VALUES
                                                                               (1, 101, 840.00),
                                                                               (2, 102, 270.00),
                                                                               (3, 201, 1250.00),
                                                                               (4, 202, 170.00),
                                                                               (5, 101, 240.00);

INSERT INTO ServiziRicevuta (IDRicevutaFiscale, NomeServizio, Quantità, PrezzoServizio) VALUES
                                                                                            (1, 'Minibar', 2, 15.00),
                                                                                            (2, 'Spa', 1, 50.00),
                                                                                            (3, 'Lavanderia', 3, 30.00),
                                                                                            (4, 'Parcheggio', 2, 20.00),
                                                                                            (5, 'Room Service', 1, 12.00);

INSERT INTO Servizio (Nome, Prezzo, IDPrenotazione) VALUES
                                                        ('Massaggio Relax', 60.00, 1),
                                                        ('Escursione Guidata', 45.00, 2),
                                                        ('Noleggio Bici', 15.00, 3),
                                                        ('Transfer Aeroporto', 40.00, 4),
                                                        ('Champagne in camera', 90.00, 5);

INSERT INTO Trattamento (Nome, Prezzo, IDPrenotazione) VALUES
                                                           ('Spa Gold', 100.00, 1),
                                                           ('Detox Day', 80.00, 2),
                                                           ('Percorso Kneipp', 40.00, 3),
                                                           ('Massaggio Sportivo', 70.00, 4),
                                                           ('Trattamento Viso', 55.00, 5);

INSERT INTO Impiegato (CF, Stipedio, Nome, Cognome, Cap, DataAssunzione, Telefono, Cittadinanza, EmailAziendale, Sesso, Ruolo, DataRilascio, TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento, DataScadenza, CF1) VALUES
                                                                                                                                                                                                                                    ('DIRMRA70A01H501W', 5000.00, 'Roberto', 'Direttore', '00100', '2010-01-01', '06123456', 'Italiana', 'direttore@hotel.it', 'Maschio', 'Manager', '2020-01-01', 'Passaporto', 'Via dei Boss', 'RM', 'Roma', 1, 'AA0000001', '2030-01-01', NULL),
                                                                                                                                                                                                                                    ('FNDMAR85A01H501Z', 1800.00, 'Marco', 'Front', '00100', '2020-05-10', '06789012', 'Italiana', 'm.front@hotel.it', 'Maschio', 'FDclient', '2021-02-02', 'Carta d\'identità', 'Via Ricevimento', 'RM', 'Roma', 15, 'AB1234567', '2031-02-02', 'DIRMRA70A01H501W'),
                                                                                                                                                                                                                                    ('GOVSRA92A01H501X', 1600.00, 'Sara', 'Governi', '00100', '2021-03-15', '06112233', 'Italiana', 's.governi@hotel.it', 'Femmina', 'Governante', '2022-06-06', 'Patente', 'Via Pulizia', 'RM', 'Roma', 8, 'CD9876543', '2032-06-06', 'DIRMRA70A01H501W'),
                                                                                                                                                                                                                                    ('MNGGIO77A01H501Y', 3500.00, 'Giovanni', 'Gestore', '00100', '2015-09-20', '06445566', 'Italiana', 'g.gestore@hotel.it', 'Maschio', 'Manager', '2019-11-11', 'Passaporto', 'Via Centro', 'RM', 'Roma', 50, 'EF5554443', '2029-11-11', 'DIRMRA70A01H501W'),
                                                                                                                                                                                                                                    ('FNDLLN95A01H501K', 1750.00, 'Elena', 'Lanzi', '00100', '2022-01-10', '06998877', 'Italiana', 'e.lanzi@hotel.it', 'Femmina', 'FDclient', '2023-08-08', 'Carta d\'identità', 'Via Mare', 'RM', 'Roma', 23, 'GH1112223', '2033-08-08', 'MNGGIO77A01H501Y');
