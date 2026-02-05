
USE hotelColossus;

-- =============================================
-- 1. INSERT TRATTAMENTO
-- =============================================
INSERT INTO Trattamento (Nome, Prezzo) VALUES
                                           ('Solo Pernottamento', 0.00),
                                           ('Bed & Breakfast', 15.00),
                                           ('Mezza Pensione', 35.00),
                                           ('Pensione Completa', 55.00),
                                           ('All Inclusive', 80.00);

-- =============================================
-- 2. INSERT SERVIZIO
-- =============================================
INSERT INTO Servizio (Nome, Prezzo) VALUES
                                        ('Parcheggio', 10.00),          -- ID 1
                                        ('WiFi Premium', 5.00),         -- ID 2
                                        ('Colazione in Camera', 12.00), -- ID 3
                                        ('Late CheckOut', 25.00),       -- ID 4
                                        ('Spa e Benessere', 45.00),     -- ID 5
                                        ('Transfer Aeroporto', 35.00);  -- ID 6

-- =============================================
-- 3. INSERT CAMERA
-- =============================================
INSERT INTO Camera (NumeroCamera, NomeCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo) VALUES
                                                                                           (101, 'Camera Standard', 2, 'Vista interna', 'Libera', 80.00),
                                                                                           (102, 'Camera Standard', 2, 'Vista strada', 'Occupata', 80.00),
                                                                                           (201, 'Junior Suite', 3, 'Vista mare laterale', 'Prenotata', 180.00),
                                                                                           (202, 'Suite Presidenziale', 2, 'Jacuzzi privata', 'Occupata', 350.00),
                                                                                           (301, 'Camera Singola', 1, 'Piccola ma accogliente', 'InPulizia', 60.00);

-- =============================================
-- 4. INSERT CLIENTE
-- =============================================
INSERT INTO Cliente (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, Nazionalita, DataDiNascita, IsBackListed) VALUES
                                                                                                                                                   ('RSSMRA80A01H501U', 'Mario', 'Rossi', '10000', 'Roma', 10, 'Roma', 'Via del Corso', 'mario.rossi@email.com', 'M', '3331234567', 'Italiana', '1980-01-01', false),
                                                                                                                                                   ('VRDLGI90B02F205K', 'Luigi', 'Verdi', '20100', 'Milano', 20, 'Milano', 'Corso Buenos Aires', 'luigi.verdi@email.com', 'M', '3339876543', 'Italiana', '1990-02-02', false),
                                                                                                                                                   ('BNCLCU85C03G273Z', 'Lucia', 'Bianchi', '90100', 'Palermo', 5, 'Palermo', 'Via Roma', 'lucia.bianchi@email.com', 'F', '3381122334', 'Italiana', '1985-03-03', true), -- Blacklisted
                                                                                                                                                   ('MULLER88E05Z112K', 'Hans', 'Muller', '10115', 'Berlino', 1, 'Berlin', 'Alexanderplatz', 'hans.muller@de-mail.de', 'M', '+4915123456', 'Tedesca', '1988-05-05', false);

-- =============================================
-- 5. INSERT IMPIEGATO
-- =============================================
-- 5.1 Inseriamo il Manager (Capo) che non ha manager sopra di lui
INSERT INTO Impiegato (CF, Stipendio, UserName, HashPasword, isTemporary, dataScadenzaToken, Nome, Cognome, Cap, DataAssunzione, Telefono, EmailAziendale, Sesso, Ruolo, Via, Provincia, Comune, Civico, NumeroDocumento, TipoDocumento, DataScadenzaDocumento, DataRilascioDocumento, Cittadinanza, IDManager, CFManager)
VALUES
    ('DIRGEN70A01H501X', 5000.00, 'admin', 'hash_super_sicura', false, '2030-12-31 23:59:59', 'Giacomo', 'Direttori', '10000', '2010-01-01', '3330000000', 'direzione@colossus.it', 'M', 'Manager', 'Via Veneto', 'Roma', 'Roma', 1, 'AX1234567', 'Carta Identita', '2028-01-01', '2018-01-01', 'Italiana', NULL, NULL);

-- 5.2 Inseriamo un Receptionist (assume che il Manager sopra abbia ID=1)
INSERT INTO Impiegato (CF, Stipendio, UserName, HashPasword, isTemporary, dataScadenzaToken, Nome, Cognome, Cap, DataAssunzione, Telefono, EmailAziendale, Sesso, Ruolo, Via, Provincia, Comune, Civico, NumeroDocumento, TipoDocumento, DataScadenzaDocumento, DataRilascioDocumento, Cittadinanza, IDManager, CFManager)
VALUES
    ('RECPT0180A01H501', 1800.00, 'reception1', 'hash_rec_1', false, '2026-12-31 23:59:59', 'Elena', 'Neri', '10000', '2020-05-01', '3331111111', 'reception@colossus.it', 'F', 'FrontDesk', 'Via Nazionale', 'Roma', 'Roma', 20, 'BX9876543', 'Patente', '2029-05-01', '2019-05-01', 'Italiana', 1, 'DIRGEN70A01H501X');

-- =============================================
-- 6. INSERT PRENOTAZIONE
-- =============================================
INSERT INTO Prenotazione (NomeIntestatario, DataCreazionePrenotazione, DataArrivoCliente, DataPartenzaCliente, DataEmissioneRicevuta, NumeroDocumento, DataRilascioDocumento, DataScadenzaDocumento, NomeTrattamento, NoteAggiuntive, TipoDocumento, Stato, CheckIn, PrezzoAcquistoTrattamento, MetodoPagamento, Cittadinanza)
VALUES
-- Prenotazione 1: Mario Rossi
('Mario Rossi', '2023-12-01', '2024-01-10', '2024-01-15', '2024-01-15', 'ID888777', '2020-01-01', '2030-01-01', 'Bed & Breakfast', 'Nessuna nota', 'Carta Identita', true, true, 15.00, 'Carta di Credito', 'italiana'),

-- Prenotazione 2: Luigi Verdi
('Luigi Verdi', '2024-02-01', '2024-02-20', '2024-02-25', NULL, 'PT123456', '2019-05-05', '2029-05-05', 'Pensione Completa', 'Intolleranza Lattosio', 'Patente', true, true, 55.00, 'Contanti', 'italiana'),

-- Prenotazione 3: Hans Muller
('Hans Muller', '2024-02-15', '2024-03-01', '2024-03-07', NULL, 'PASSDE99', '2021-01-01', '2031-01-01', 'All Inclusive', 'Late arrival expected', 'Passaporto', true, false, 80.00, 'Bonifico', 'italiana');

-- =============================================
-- 7. INSERT ASSOCIATO_A
-- Importante: Inseriamo manualmente NomeOspiteStorico e CognomeOspiteStorico
-- =============================================
INSERT INTO Associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto, NominativoCliente, NumeroCameraStorico ) VALUES
-- Mario Rossi -> Camera 101 -> Prenotazione 1
('RSSMRA80A01H501U', 101, 1, 350.00, 'Mario',101),

-- Luigi Verdi -> Suite 202 -> Prenotazione 2
('VRDLGI90B02F205K', 202, 2, 350.00, 'Luigi Verdi',202),

-- Hans Muller -> Camera 201 -> Prenotazione 3
('MULLER88E05Z112K', 201, 3, 180.00, 'Hans Muller',201);

-- =============================================
-- 8. INSERT HA (Dettaglio Servizi)
-- Importante: Inseriamo manualmente NomeServizioAcquistato
-- =============================================
INSERT INTO Ha (IDPrenotazione, IDServizio, quantità, NomeServizioAcquistato, PrezzoAcquistoServizio) VALUES
-- Prenotazione 1 (Mario) prende Parcheggio(1)
(1, 1, 2,'Parcheggio', 10.00),

-- Prenotazione 2 (Luigi) prende Spa(5) e Colazione(3)
(2, 5, 3,'Spa e Benessere', 45.00),
(2, 3, 2,'Colazione in Camera', 12.00),

-- Prenotazione 3 (Hans) prende Transfer(6)
(3, 6, 4,'Transfer Aeroporto', 35.00);
SELECT 'Database popolato con successo!' AS Messaggio;
