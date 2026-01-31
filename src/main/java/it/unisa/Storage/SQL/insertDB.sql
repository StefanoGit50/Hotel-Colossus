-- ===========================
-- INSERT PER TABELLA Cliente
-- ===========================

INSERT INTO Cliente (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, Cittadinanza, DataDiNascita, IsBackListed, Nazionalità) VALUES
                                                                                                                                                    ('RSSMRA85M01H501Z', 'Mario', 'Rossi', '00100', 'Roma', 15, 'Roma', 'Via Nazionale', 'mario.rossi@email.it', 'M', '3331234567', 'Italiana', '1985-08-01', false, 'Italiana'),
                                                                                                                                                    ('VRDGVN90D42F205X', 'Giovanna', 'Verdi', '20100', 'Milano', 22, 'Milano', 'Corso Buenos Aires', 'g.verdi@email.it', 'F', '3487654321', 'Italiana', '1990-04-02', false, 'Italiana'),
                                                                                                                                                    ('BNCLCU88R15L219K', 'Luca', 'Bianchi', '50100', 'Firenze', 8, 'Firenze', 'Via dei Calzaiuoli', 'luca.bianchi@email.it', 'M', '3401122334', 'Italiana', '1988-10-15', false, 'Italiana'),
                                                                                                                                                    ('FRRSFN92C50D612W', 'Stefania', 'Ferrari', '10100', 'Torino', 45, 'Torino', 'Via Roma', 's.ferrari@email.it', 'F', '3332211445', 'Italiana', '1992-03-10', false, 'Italiana'),
                                                                                                                                                    ('MRTNDR80A12H501Y', 'Andrea', 'Martini', '00185', 'Roma', 33, 'Roma', 'Via Merulana', 'andrea.martini@email.it', 'M', '3498877665', 'Italiana', '1980-01-12', true, 'Italiana'),
                                                                                                                                                    ('GLLCLA95L63F839B', 'Chiara', 'Gallo', '80100', 'Napoli', 12, 'Napoli', 'Via Toledo', 'chiara.gallo@email.it', 'F', '3471234890', 'Italiana', '1995-07-23', false, 'Italiana'),
                                                                                                                                                    ('CSTMRC87H09A944V', 'Marco', 'Costa', '16100', 'Genova', 7, 'Genova', 'Via XX Settembre', 'marco.costa@email.it', 'M', '3389876543', 'Italiana', '1987-06-09', false, 'Italiana'),
                                                                                                                                                    ('RMNLRA93E55H501T', 'Laura', 'Romani', '00153', 'Roma', 28, 'Roma', 'Via Ostiense', 'laura.romani@email.it', 'F', '3456789012', 'Italiana', '1993-05-15', false, 'Italiana'),
                                                                                                                                                    ('SNTMTT91B18L736P', 'Matteo', 'Santi', '90100', 'Palermo', 19, 'Palermo', 'Via Maqueda', 'matteo.santi@email.it', 'M', '3423456789', 'Italiana', '1991-02-18', false, 'Italiana'),
                                                                                                                                                    ('LMBELS89T60D969H', 'Elisa', 'Lombardi', '40100', 'Bologna', 41, 'Bologna', 'Via Indipendenza', 'elisa.lombardi@email.it', 'F', '3312345678', 'Italiana', '1989-12-20', false, 'Cinese');

-- ===========================
-- INSERT PER TABELLA Camera
-- ===========================

INSERT INTO Camera (NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo) VALUES
                                                                               (101, 2, 'Camera matrimoniale con vista giardino', 'Libera', 80.00),
                                                                               (102, 2, 'Camera doppia standard', 'Libera', 75.00),
                                                                               (103, 3, 'Camera tripla con balcone', 'Libera', 110.00),
                                                                               (104, 2, 'Camera matrimoniale deluxe', 'Prenotata', 120.00),
                                                                               (105, 4, 'Suite familiare con soggiorno', 'Libera', 180.00),
                                                                               (201, 2, 'Camera matrimoniale vista mare', 'Occupata', 150.00),
                                                                               (202, 2, 'Camera doppia con bagno rinnovato', 'Libera', 85.00),
                                                                               (203, 1, 'Camera singola economica', 'Libera', 50.00),
                                                                               (204, 2, 'Camera matrimoniale con vasca idromassaggio', 'InPulizia', 140.00),
                                                                               (205, 3, 'Camera tripla con terrazzo', 'Prenotata', 130.00),
                                                                               (301, 2, 'Camera matrimoniale superior', 'Libera', 95.00),
                                                                               (302, 2, 'Camera doppia panoramica', 'OutOfOrder', 90.00),
                                                                               (303, 4, 'Suite presidenziale', 'Libera', 250.00),
                                                                               (304, 2, 'Camera matrimoniale comfort', 'Libera', 100.00),
                                                                               (305, 2, 'Camera doppia con aria condizionata', 'Libera', 80.00);

-- ===========================
-- INSERT PER TABELLA Impiegato
-- ===========================

INSERT INTO Impiegato (CF, Stipedio, UserName, HashPasword, isTempurali, dataScadenzaToken, Nome, Cognome, Cap, DataAssunzione, Telefono, Cittadinanza, EmailAziendale, Sesso, Ruolo, DataRilascio, TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento, DataScadenza, IDImpiegato1, CF1) VALUES
                                                                                                                                                                                                                                                                                                         ('MNCGPP75D15H501A', 2500.00, 'g.mancini', 'hash123abc', false, '2026-02-27 23:59:59', 'Giuseppe', 'Mancini', '00100', '2015-03-01', '3351234567', 'Italiana', 'g.mancini@hotelcolossus.it', 'M', 'Manager', '2010-05-20', 'Carta Identità', 'Via Cavour', 'Roma', 'Roma', 10, 'AB1234567', '2030-05-20', 0, NULL),
                                                                                                                                                                                                                                                                                                         ('PZZFNC82T42F205B', 1800.00, 'f.pizzo', 'hash456def', false, '2026-02-27 23:59:59', 'Francesca', 'Pizzo', '00185', '2018-06-15', '3487654321', 'Italiana', 'f.pizzo@hotelcolossus.it', 'F', 'FrontDesk', '2015-03-10', 'Carta Identità', 'Via Nazionale', 'Roma', 'Roma', 25, 'CD7890123', '2028-03-10', 1, 'MNCGPP75D15H501A'),
                                                                                                                                                                                                                                                                                                         ('GRSSMN88P11L219C', 1600.00, 's.grassi', 'hash789ghi', false, '2026-02-27 23:59:59', 'Simona', 'Grassi', '00153', '2019-09-10', '3401122334', 'Italiana', 's.grassi@hotelcolossus.it', 'F', 'Governante', '2016-07-15', 'Carta Identità', 'Via Ostiense', 'Roma', 'Roma', 18, 'EF4567890', '2029-07-15', 1, 'MNCGPP75D15H501A'),
                                                                                                                                                                                                                                                                                                         ('NTLCRL86S03D612D', 1900.00, 'c.natalini', 'hash012jkl', false, '2026-02-27 23:59:59', 'Carla', 'Natalini', '00176', '2017-11-20', '3332211445', 'Italiana', 'c.natalini@hotelcolossus.it', 'F', 'FrontDesk', '2014-09-25', 'Carta Identità', 'Via Tuscolana', 'Roma', 'Roma', 52, 'GH1234567', '2027-09-25', 1, 'MNCGPP75D15H501A'),
                                                                                                                                                                                                                                                                                                         ('BRTNDR90L25H501E', 1700.00, 'a.berti', 'hash345mno', true, '2026-03-15 23:59:59', 'Andrea', 'Berti', '00165', '2025-01-10', '3498877665', 'Italiana', 'a.berti@hotelcolossus.it', 'M', 'FrontDesk', '2020-04-12', 'Carta Identità', 'Via Gregorio VII', 'Roma', 'Roma', 33, 'IJ7890123', '2031-04-12', 1, 'MNCGPP75D15H501A');

-- ===========================
-- INSERT PER TABELLA Servizio
-- ===========================

INSERT INTO Servizio (Nome, Prezzo, IDPrenotazione) VALUES
                                                        ('Colazione in camera', 15.00, NULL),
                                                        ('Lavanderia', 20.00, NULL),
                                                        ('Transfer aeroporto', 50.00, NULL),
                                                        ('Spa e massaggi', 80.00, NULL),
                                                        ('Noleggio biciclette', 10.00, NULL),
                                                        ('Minibar', 25.00, NULL),
                                                        ('Parcheggio giornaliero', 15.00, NULL),
                                                        ('Late check-out', 30.00, NULL);

-- ===========================
-- INSERT PER TABELLA Trattamento
-- ===========================

INSERT INTO Trattamento (Nome, Prezzo, IDPrenotazione) VALUES
                                                           ('Pernottamento', 0.00, NULL),
                                                           ('Bed & Breakfast', 15.00, NULL),
                                                           ('Mezza Pensione', 35.00, NULL),
                                                           ('Pensione Completa', 50.00, NULL),
                                                           ('All Inclusive', 75.00, NULL);

-- ===========================
-- INSERT PER TABELLA Prenotazione
-- ===========================

INSERT INTO Prenotazione (DataPrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn) VALUES
                                                                                                                                                                                                                  ('2026-01-15', '2026-02-01', '2026-02-05', 'Bed & Breakfast', 'Camera con vista mare richiesta', 'Mario Rossi', '2035-12-31', 'AB123456', '2020-01-10', 'Carta Identità', true, true),
                                                                                                                                                                                                                  ('2026-01-20', '2026-02-10', '2026-02-14', 'Mezza Pensione', 'Anniversario di matrimonio', 'Giovanna Verdi', '2033-05-15', 'CD789012', '2018-05-15', 'Passaporto', true, false),
                                                                                                                                                                                                                  ('2026-01-22', '2026-03-01', '2026-03-07', 'Pensione Completa', 'Famiglia con bambini', 'Luca Bianchi', '2032-08-20', 'EF345678', '2017-08-20', 'Carta Identità', true, false),
                                                                                                                                                                                                                  ('2026-01-10', '2026-01-25', '2026-01-28', 'Bed & Breakfast', 'Viaggio di lavoro', 'Stefania Ferrari', '2031-11-10', 'GH901234', '2021-11-10', 'Carta Identità', true, true),
                                                                                                                                                                                                                  ('2025-12-20', '2026-01-15', '2026-01-20', 'All Inclusive', 'Gruppo di amici', 'Chiara Gallo', '2030-03-05', 'IJ567890', '2019-03-05', 'Passaporto', false, false),
                                                                                                                                                                                                                  ('2026-01-18', '2026-02-20', '2026-02-23', 'Pernottamento', 'Solo pernottamento', 'Marco Costa', '2029-07-18', 'KL123456', '2016-07-18', 'Carta Identità', true, false);

-- ===========================
-- INSERT PER TABELLA Associato_a
-- ===========================

INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto) VALUES
                                                                               ('RSSMRA85M01H501Z', 201, 1, 150.00),
                                                                               ('VRDGVN90D42F205X', 104, 2, 120.00),
                                                                               ('FRRSFN92C50D612W', 104, 2, 120.00),
                                                                               ('BNCLCU88R15L219K', 205, 3, 130.00),
                                                                               ('GLLCLA95L63F839B', 205, 3, 130.00),
                                                                               ('FRRSFN92C50D612W', 203, 4, 50.00),
                                                                               ('CSTMRC87H09A944V', 303, 6, 250.00);

-- ===========================
-- INSERT PER TABELLA RicevutaFiscale
-- ===========================

INSERT INTO RicevutaFiscale (IDRicevutaFiscale, IDPrenotazione, DataEmissione, metodoPagamento, DataPrenotazione, PrezzoTrattamento, TipoTrattamento) VALUES
                                                                                                                                                          (1001, 1, '2026-02-05', 'Carta di Credito', '2026-01-15', 15.00, 'Bed & Breakfast'),
                                                                                                                                                          (1002, 4, '2026-01-28', 'Contanti', '2026-01-10', 15.00, 'Bed & Breakfast');

-- ===========================
-- INSERT PER TABELLA ClientiRicevuta
-- ===========================

INSERT INTO ClientiRicevuta (CFCliente, IDRicevutaFiscale, NomeCliente, CognomeCliente, isIntestatario) VALUES
                                                                                                            ('RSSMRA85M01H501Z', 1001, 'Mario', 'Rossi', true),
                                                                                                            ('FRRSFN92C50D612W', 1002, 'Stefania', 'Ferrari', true);

-- ===========================
-- INSERT PER TABELLA CameraRicevuta
-- ===========================

INSERT INTO CameraRicevuta (IDRicevutaFiscale, NumeroCamera, PrezzoCamera) VALUES
                                                                               (1001, 201, 150.00),
                                                                               (1002, 203, 50.00);

-- ===========================
-- INSERT PER TABELLA ServiziRicevuta
-- ===========================

INSERT INTO ServiziRicevuta (IDRicevutaFiscale, NomeServizio, Quantità, PrezzoServizio) VALUES
                                                                                            (1001, 'Colazione in camera', 4, 15.00),
                                                                                            (1001, 'Transfer aeroporto', 1, 50.00),
                                                                                            (1002, 'Parcheggio giornaliero', 3, 15.00);