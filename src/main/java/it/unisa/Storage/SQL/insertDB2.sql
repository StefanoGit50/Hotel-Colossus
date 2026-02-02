-- INSERT per Cliente2 (2 clienti)
INSERT INTO cliente (CF, nome, cognome, Cap, comune, civico, provincia, via, Email, Sesso, telefono, Cittadinanza, Nazionalità, DataDiNascita, IsBackListed)
VALUES
('VRDLRA90D45F839Y', 'Laura', 'Verdi', '00100', 'Roma', 23, 'Roma', 'Via Milano', 'laura.verdi@email.it', 'Femmina', '3339876543', 'Italiana', 'Italiana', '1990-04-05', FALSE);

-- INSERT per Camera2 (2 camere)
INSERT INTO camera (NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo)
VALUES
(101, 2, 'Camera doppia con vista mare', 'Libera', 120.00),
(102, 4, 'Camera familiare con balcone', 'Libera', 180.00);

-- INSERT per Prenotazione2 (2 prenotazioni)
INSERT INTO prenotazione (DataCreazionePrenotazione, DataArrivoCliente, DataPartenzaCliente, NomeTrattamento, NoteAggiuntive, Intestatario, dataScadenza, numeroDocumento, DataRilascio, TipoDocumento, Stato, ChekIn)
VALUES 
('2026-01-15', '2026-02-01', '2026-02-05', 'Pensione Completa', 'Richiesta camera silenziosa', 'Mario Rossi', '2030-01-01', 'AX123456', '2020-01-15', 'Carta Identità', TRUE, FALSE),
('2026-01-20', '2026-02-10', '2026-02-15', 'Mezza Pensione', 'Nessuna nota particolare', 'Laura Verdi', '2029-05-10', 'BC789012', '2019-05-10', 'Passaporto', TRUE, FALSE);

-- INSERT per Trattamento2 (2 trattamenti) - PRIMA delle associazioni
INSERT INTO trattamento (Nome, Prezzo)
VALUES
('Pensione Completa', 50.00),
('Mezza Pensione', 30.00);

-- INSERT per Servizio2 (2 servizi)
INSERT INTO servizio (Nome, Prezzo)
VALUES
('Lavanderia', 15.00),
('Spa e Benessere', 40.00);

-- INSERT per Associato_a2 (2 associazioni)
INSERT INTO associato_a (CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto)
VALUES
('VRDLRA90D45F839Y', 102, 2, 180.00);

-- INSERT per RicevutaFiscale2 (2 ricevute)

-- INSERT per Impiegato2 (2 impiegati)
INSERT INTO impiegato (CF, Stipedio, UserName, HashPasword, isTempurali, dataScadenzaToken, Nome, Cognome, Cap, DataAssunzione, Telefono, Cittadinanza, EmailAziendale, Sesso, Ruolo, DataRilascio, TipoDocumento, Via, Provincia, Comune, Civico, NumeroDocumento, DataScadenza, IDImpiegato1, CF1)
VALUES 
('BNCLGI80A01H703X', 1800.00, 'l.bianchi', 'hash123abc', FALSE, '2026-12-31 23:59:59', 'Luigi', 'Bianchi', '80100', '2020-03-01', '3337654321', 'Italiana', 'l.bianchi@hotelcolossus.it', 'Maschio', 'Manager', '2015-01-10', 'Carta Identità', 'Via Napoli', 'Napoli', 'Napoli', 42, 'AB1234567', '2030-01-10', 0, NULL),
('GLLMRA88B42F205W', 1500.00, 'a.gialli', 'hash456def', FALSE, '2026-12-31 23:59:59', 'Maria', 'Gialli', '80100', '2021-06-15', '3338765432', 'Italiana', 'm.gialli@hotelcolossus.it', 'Femmina', 'FDclient', '2016-03-20', 'Passaporto', 'Via Vesuvio', 'Napoli', 'Napoli', 18, 'CD8901234', '2028-03-20', 1, 'BNCLGI80A01H703X');