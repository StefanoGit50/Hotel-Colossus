
-- INSERT per HotelColossusDB
-- Rispettando tutti i vincoli di integrità referenziale e vincoli di dominio

USE hotelcolossus;

-- =============================================
-- INSERT TRATTAMENTO (deve essere inserito prima di Prenotazione)
-- =============================================
INSERT INTO Trattamento (Nome, Prezzo) VALUES
                                           ('Solo Pernottamento', 0.00),
                                           ('Bed & Breakfast', 15.00),
                                           ('Mezza Pensione', 35.00),
                                           ('Pensione Completa', 55.00),
                                           ('All Inclusive', 80.00);

-- =============================================
-- INSERT SERVIZIO
-- =============================================
INSERT INTO Servizio (Nome, Prezzo) VALUES
                                        ('Parcheggio', 10.00),
                                        ('WiFi Premium', 5.00),
                                        ('Colazione in Camera', 12.00),
                                        ('Late CheckOut', 25.00),
                                        ('Early CheckIn', 20.00),
                                        ('Servizio Lavanderia', 15.00),
                                        ('Minibar', 8.00),
                                        ('Spa e Benessere', 45.00),
                                        ('Transfer Aeroporto', 35.00),
                                        ('Noleggio Auto', 50.00);

-- =============================================
-- INSERT IMPIEGATO
-- =============================================
-- Prima inseriamo i Manager (che non hanno manager superiore)
INSERT INTO Impiegato (CF, stipendio, UserName, HashPasword, isTemporary, dataScadenzaToken,
                       Nome, Cognome, Cap, DataAssunzione, Telefono, EmailAziendale, Sesso,
                       Ruolo, Via, Provincia, Comune, Civico, NumeroDocumento, TipoDocumento,
                       DataScadenzaDocumento, DataRilascioDocumento, Cittadinanza, IDManager, CFManager)
VALUES
    ('RSSMRA75H15F839K', 3500.00, 'mrossi', 'hash123abc', false, '2025-12-31 23:59:59',
     'Mario', 'Rossi', '80100', '2015-03-15', '+393201234567', 'mario.rossi@hotelcolossus.it', 'M',
     'Manager', 'Via Roma', 'Napoli', 'Napoli', 45, 'AA1234567', 'Carta Identità',
     '2028-06-15', '2018-06-15', 'Italiana', 0, NULL);

-- Poi inseriamo gli altri impiegati che riportano al Manager
INSERT INTO Impiegato (CF, stipendio, UserName, HashPasword, isTemporary, dataScadenzaToken,
                       Nome, Cognome, Cap, DataAssunzione, Telefono, EmailAziendale, Sesso,
                       Ruolo, Via, Provincia, Comune, Civico, NumeroDocumento, TipoDocumento,
                       DataScadenzaDocumento, DataRilascioDocumento, Cittadinanza, IDManager, CFManager)
VALUES
    ('BNCGVN85M20H703Z', 2200.00, 'gbianchi', 'hash456def', false, '2025-12-31 23:59:59',
     'Giovanna', 'Bianchi', '80121', '2018-09-01', '+393339876543', 'giovanna.bianchi@hotelcolossus.it', 'F',
     'FrontDesk', 'Via Caracciolo', 'Napoli', 'Napoli', 12, 'AB9876543', 'Carta Identità',
     '2029-03-20', '2019-03-20', 'Italiana', 1, 'RSSMRA75H15F839K'),

    ('VRDLCA90A15F839M', 2000.00, 'lverdi', 'hash789ghi', false, '2025-12-31 23:59:59',
     'Luca', 'Verdi', '80133', '2020-01-10', '+393487654321', 'luca.verdi@hotelcolossus.it', 'M',
     'FrontDesk', 'Corso Umberto', 'Napoli', 'Napoli', 88, 'AC5432198', 'Carta Identità',
     '2030-01-15', '2020-01-15', 'Italiana', 1, 'RSSMRA75H15F839K'),

    ('GLLMRA88D45H703W', 1900.00, 'mgiallo', 'hashabcxyz', false, '2025-12-31 23:59:59',
     'Maria', 'Giallo', '80134', '2019-06-20', '+393356781234', 'maria.giallo@hotelcolossus.it', 'F',
     'Governante', 'Via Foria', 'Napoli', 'Napoli', 156, 'AD8765432', 'Carta Identità',
     '2027-12-10', '2017-12-10', 'Italiana', 1, 'RSSMRA75H15F839K'),

    ('NRIGPP92L30F839Y', 1950.00, 'gneri', 'hashdefuvw', false, '2025-12-31 23:59:59',
     'Giuseppe', 'Neri', '80126', '2021-03-15', '+393298765432', 'giuseppe.neri@hotelcolossus.it', 'M',
     'Governante', 'Via Toledo', 'Napoli', 'Napoli', 234, 'AE3456789', 'Carta Identità',
     '2031-07-30', '2021-07-30', 'Italiana', 1, 'RSSMRA75H15F839K');

-- =============================================
-- INSERT CLIENTE
-- =============================================
INSERT INTO Cliente (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso,
                     telefono, Nazionalita, DataDiNascita, IsBlackListed)
VALUES
    ('FRRLRD80A01H501Z', 'Leonardo', 'Ferrari', '20100', 'Milano', 15, 'Milano', 'Via Dante',
     'leonardo.ferrari@email.it', 'M', '+393331234567', 'Italiana', '1980-01-01', false),

    ('MRTANA85B42F205W', 'Anna', 'Moretti', '50100', 'Firenze', 23, 'Firenze', 'Via Tornabuoni',
     'anna.moretti@email.it', 'F', '+393349876543', 'Italiana', '1985-02-02', false),

    ('RCCPTR90C15L736Y', 'Pietro', 'Ricci', '00100', 'Roma', 78, 'Roma', 'Via Veneto',
     'pietro.ricci@email.it', 'M', '+393387654321', 'Italiana', '1990-03-15', false),

    ('CSTGLT88D20H501M', 'Giulia', 'Costa', '20121', 'Milano', 45, 'Milano', 'Corso Buenos Aires',
     'giulia.costa@email.it', 'F', '+393356789012', 'Italiana', '1988-04-20', false),

    ('GRCFNC95E10F839K', 'Francesco', 'Greco', '80100', 'Napoli', 67, 'Napoli', 'Via Chiaia',
     'francesco.greco@email.it', 'M', '+393298761234', 'Italiana', '1995-05-10', false),

    ('BRNLRA92F25L219P', 'Laura', 'Bruno', '10100', 'Torino', 34, 'Torino', 'Via Roma',
     'laura.bruno@email.it', 'F', '+393367890123', 'Italiana', '1992-06-25', false),

    ('SNTMRC87G30G273N', 'Marco', 'Santi', '16100', 'Genova', 89, 'Genova', 'Via Garibaldi',
     'marco.santi@email.it', 'M', '+393345678901', 'Italiana', '1987-07-30', true),

    ('LMBSRA93H12F205Q', 'Sara', 'Lombardi', '50122', 'Firenze', 12, 'Firenze', 'Borgo Pinti',
     'sara.lombardi@email.it', 'F', '+393323456789', 'Italiana', '1993-06-12', false),

    ('MRNAND91L20D612R', 'Andrea', 'Marini', '35100', 'Padova', 56, 'Padova', 'Via del Santo',
     'andrea.marini@email.it', 'M', '+393312345678', 'Italiana', '1991-07-20', false),

    ('FNTCLD89M15H501S', 'Claudia', 'Fontana', '20122', 'Milano', 91, 'Milano', 'Viale Monza',
     'claudia.fontana@email.it', 'F', '+393390123456', 'Italiana', '1989-08-15', false);

-- =============================================
-- INSERT CAMERA
-- =============================================
INSERT INTO Camera (NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (101, 'Camera Standard', 2, 'Camera con vista cortile interno, letto matrimoniale', 'Libera', 80.00),
    (102, 'Camera Standard Plus', 2, 'Camera con vista strada, letto matrimoniale o due singoli', 'Libera', 90.00),
    (103, 'Camera Deluxe', 2, 'Camera spaziosa con balcone, letto king size', 'Prenotata', 120.00),
    (104, 'Camera Family', 4, 'Camera con letto matrimoniale e due letti singoli', 'Libera', 150.00),
    (201, 'Junior Suite', 3, 'Suite con zona living separata e vista mare', 'Occupata', 180.00),
    (202, 'Camera Superior', 2, 'Camera elegante con vista panoramica', 'Libera', 110.00),
    (203, 'Camera Economy', 2, 'Camera essenziale ma confortevole', 'InPulizia', 70.00),
    (204, 'Camera Comfort', 2, 'Camera rinnovata con tutti i comfort', 'Libera', 95.00),
    (301, 'Suite Presidenziale', 4, 'Suite di lusso con terrazza privata e jacuzzi', 'Libera', 350.00),
    (302, 'Camera Executive', 2, 'Camera business con scrivania e WiFi veloce', 'Prenotata', 130.00),
    (303, 'Camera Standard', 2, 'Camera standard al terzo piano', 'Libera', 80.00),
    (304, 'Camera Tripla', 3, 'Camera con tre letti singoli', 'Libera', 115.00),
    (401, 'Camera Accessible', 2, 'Camera attrezzata per disabili', 'Libera', 85.00),
    (402, 'Camera Romantica', 2, 'Camera con decorazioni romantiche e vasca idromassaggio', 'OutOfOrder', 140.00),
    (403, 'Camera Standard', 2, 'Camera standard al quarto piano', 'Libera', 80.00);

-- =============================================
-- INSERT PRENOTAZIONE
-- =============================================
INSERT INTO Prenotazione (NomeIntestatario, DataCreazionePrenotazione, DataArrivoCliente,
                          DataPartenzaCliente, DataEmissioneRicevuta, NumeroDocumento,
                          DataRilascioDocumento, DataScadenzaDocumento, NomeTrattamento,
                          NoteAggiuntive, TipoDocumento, Stato, CheckIn, PrezzoAcquistoTrattamento,
                          MetodoPagamento)
VALUES
-- Prenotazioni passate con check-in effettuato
('Leonardo Ferrari', '2025-01-10', '2025-01-15', '2025-01-20', '2025-01-20',
 'AA1111111', '2020-01-01', '2030-01-01', 'Bed & Breakfast', 'Cliente abituale, preferisce piano alto',
 'Carta Identità', true, true, 15.00, 'Carta di Credito'),

('Anna Moretti', '2025-01-12', '2025-01-18', '2025-01-22', '2025-01-22',
 'BB2222222', '2019-05-15', '2029-05-15', 'Mezza Pensione', 'Richiesta camera silenziosa',
 'Passaporto', true, true, 35.00, 'Contanti'),

-- Prenotazioni attive (check-in effettuato, ancora in hotel)
('Pietro Ricci', '2025-01-20', '2025-01-28', '2025-02-03', NULL,
 'CC3333333', '2021-03-10', '2031-03-10', 'Pensione Completa', 'Intolleranza al glutine',
 'Carta Identità', true, true, 55.00, 'Carta di Credito'),

-- Prenotazioni future (non ancora check-in)
('Giulia Costa', '2025-01-25', '2025-02-05', '2025-02-10', NULL,
 'DD4444444', '2020-08-20', '2030-08-20', 'Bed & Breakfast', 'Viaggio di lavoro',
 'Carta Identità', true, false, 15.00, 'Bonifico'),

('Francesco Greco', '2025-01-26', '2025-02-08', '2025-02-12', NULL,
 'EE5555555', '2022-01-15', '2032-01-15', 'All Inclusive', 'Viaggio di nozze',
 'Passaporto', true, false, 80.00, 'Carta di Credito'),

('Laura Bruno', '2025-01-27', '2025-02-15', '2025-02-18', NULL,
 'FF6666666', '2021-06-30', '2031-06-30', 'Solo Pernottamento', 'Gruppo per conferenza',
 'Carta Identità', true, false, 0.00, 'Bonifico'),

('Sara Lombardi', '2025-01-28', '2025-02-20', '2025-02-25', NULL,
 'GG7777777', '2020-11-12', '2030-11-12', 'Mezza Pensione', 'Richiesta culla per bambino',
 'Carta Identità', true, false, 35.00, 'Carta di Credito'),

('Andrea Marini', '2025-01-29', '2025-03-01', '2025-03-05', NULL,
 'HH8888888', '2019-09-25', '2029-09-25', 'Bed & Breakfast', 'Cliente VIP',
 'Passaporto', true, false, 15.00, 'Contanti'),

-- Prenotazione cancellata
('Marco Santi', '2025-01-15', '2025-02-01', '2025-02-04', NULL,
 'II9999999', '2018-12-10', '2028-12-10', 'Pensione Completa', 'Cliente in blacklist',
 'Carta Identità', false, false, 55.00, NULL),

('Claudia Fontana', '2025-01-30', '2025-03-10', '2025-03-15', NULL,
 'JJ0000000', '2021-07-18', '2031-07-18', 'All Inclusive', 'Weekend relax',
 'Carta Identità', true, false, 80.00, 'Carta di Credito');

-- =============================================
-- INSERT ASSOCIATO_A (Collegamento Cliente-Camera-Prenotazione)
-- =============================================
INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto)
VALUES
-- Prenotazione 1 - Leonardo Ferrari
('FRRLRD80A01H501Z', 202, 1, 110.00),

-- Prenotazione 2 - Anna Moretti
('MRTANA85B42F205W', 103, 2, 120.00),

-- Prenotazione 3 - Pietro Ricci (attualmente in hotel)
('RCCPTR90C15L736Y', 201, 3, 180.00),

-- Prenotazione 4 - Giulia Costa
('CSTGLT88D20H501M', 302, 4, 130.00),

-- Prenotazione 5 - Francesco Greco
('GRCFNC95E10F839K', 103, 5, 120.00),

-- Prenotazione 6 - Laura Bruno
('BRNLRA92F25L219P', 204, 6, 95.00),

-- Prenotazione 7 - Sara Lombardi
('LMBSRA93H12F205Q', 104, 7, 150.00),

-- Prenotazione 8 - Andrea Marini
('MRNAND91L20D612R', 301, 8, 350.00),

-- Prenotazione 9 - Marco Santi (cancellata)
('SNTMRC87G30G273N', 101, 9, 80.00),

-- Prenotazione 10 - Claudia Fontana
('FNTCLD89M15H501S', 302, 10, 130.00);

-- =============================================
-- INSERT HA (Servizi associati alle prenotazioni)
-- =============================================
INSERT INTO Ha (IDPrenotazione, NomeServizio, PrezzoAcquistoServizio)
VALUES
-- Prenotazione 1
(1, 'Parcheggio', 10.00),
(1, 'WiFi Premium', 5.00),

-- Prenotazione 2
(2, 'Colazione in Camera', 12.00),
(2, 'Late CheckOut', 25.00),

-- Prenotazione 3 (cliente attuale)
(3, 'Parcheggio', 10.00),
(3, 'Spa e Benessere', 45.00),
(3, 'Minibar', 8.00),

-- Prenotazione 4
(4, 'Early CheckIn', 20.00),
(4, 'WiFi Premium', 5.00),

-- Prenotazione 5
(5, 'Transfer Aeroporto', 35.00),
(5, 'Spa e Benessere', 45.00),
(5, 'Late CheckOut', 25.00),

-- Prenotazione 6
(6, 'Parcheggio', 10.00),

-- Prenotazione 7
(7, 'Servizio Lavanderia', 15.00),
(7, 'Colazione in Camera', 12.00),

-- Prenotazione 8
(8, 'Transfer Aeroporto', 35.00),
(8, 'Noleggio Auto', 50.00),
(8, 'Spa e Benessere', 45.00),

-- Prenotazione 10
(10, 'Spa e Benessere', 45.00),
(10, 'WiFi Premium', 5.00);

-- =============================================
-- FINE INSERT
-- =============================================
INSERT INTO Camera
(NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (101, 'Camera 101', 2, 'Camera doppia con vista mare', 'Libera', 120);

INSERT INTO Camera
(NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (102, 'Camera 102', 4, 'Camera familiare con balcone', 'Libera', 180);

INSERT INTO Camera
(NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (103, 'Camera 103', 2, 'Camera prova', 'Libera', 150);

INSERT INTO Camera
(NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
    (104, 'Camera 104', 2, 'Camera prova', 'Libera', 150);



SELECT 'Database popolato con successo!' AS Messaggio;
