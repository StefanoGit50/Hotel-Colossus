
INSERT INTO Cliente (
    CF, nome, cognome, Cap, comune, civico, provincia, via,
    Email, Sesso, telefono, Cittadinanza, Nazionalità,
    DataDiNascita, IsBackListed
) VALUES (
             'RSSMRA85M01H501Z', 'Mario', 'Rossi', '80100', 'Napoli', 15,
             'Napoli', 'Via Roma', 'mario.rossi@email.it',
             'Maschio', '3331234567', 'Italiana', 'Italiana',
             '1985-08-01', false
         ),
         ('VRDLRA90D45F839Y', 'Laura', 'Verdi', '00100', 'Roma', 23, 'Roma', 'Via Milano', 'laura.verdi@email.it', 'Femmina', '3339876543', 'Italiana', 'Italiana', '1990-04-05', FALSE)
;

INSERT INTO Camera (
    NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo
) VALUES (
             101, 2, 'Camera matrimoniale vista mare', 'Occupata', 120.00
         ),
      (102 , 3 , 'Camera per due persone','Libera',130.0);

INSERT INTO Prenotazione (
    DataPrenotazione, DataArrivoCliente, DataPartenzaCliente,
    NomeTrattamento, NoteAggiuntive, Intestatario,
    dataScadenza, numeroDocumento, DataRilascio,
    TipoDocumento, Stato, ChekIn
) VALUES (
             '2026-02-01', '2026-02-10', '2026-02-15',
             'Pensione Completa', 'Nessuna nota',
             'Mario Rossi', '2026-02-09',
             'AA123456', '2020-01-01',
             'CartaIdentità', true, false
         ),
         ('2026-01-20', '2026-02-10', '2026-02-15', 'Mezza Pensione', 'Nessuna nota particolare', 'Laura Verdi', '2029-05-10', 'BC789012', '2019-05-10', 'Passaporto', TRUE, FALSE);


INSERT INTO Associato_a (
    CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto
) VALUES (
             'RSSMRA85M01H501Z', 101, 1, 600.00
         ),
      ('VRDLRA90D45F839Y',102,2,180.00);

INSERT INTO RicevutaFiscale (
    IDRicevutaFiscale, IDPrenotazione, DataEmissione,
    metodoPagamento, DataPrenotazione,
    PrezzoTrattamento, TipoTrattamento
) VALUES (
             1, 1, '2026-02-01', 'Carta di Credito',
             '2026-02-01', 300.00, 'Pensione Completa'
         );

INSERT INTO ClientiRicevuta (
    CFCliente, IDRicevutaFiscale,
    NomeCliente, CognomeCliente, isIntestatario
) VALUES (
             'RSSMRA85M01H501Z', 1, 'Mario', 'Rossi', true
         );


INSERT INTO CameraRicevuta (
    IDRicevutaFiscale, NumeroCamera, PrezzoCamera
) VALUES (
             1, 101, 120.00
         );

INSERT INTO ServiziRicevuta (
    IDRicevutaFiscale, NomeServizio, Quantità, PrezzoServizio
) VALUES (
             1, 'Spa', 2, 50.00
         );

INSERT INTO Servizio (
    Nome, Prezzo, IDPrenotazione
) VALUES (
             'Spa', 50.00, 1
         );

INSERT INTO Trattamento (
    Nome, Prezzo, IDPrenotazione
) VALUES (
             'Pensione Completa', 300.00, 1
         );

INSERT INTO Impiegato (
    CF, Stipedio, UserName, HashPasword,
    isTempurali, dataScadenzaToken,
    Nome, Cognome, Cap, DataAssunzione,
    Telefono, Cittadinanza, EmailAziendale,
    Sesso, Ruolo, DataRilascio, TipoDocumento,
    Via, Provincia, Comune, Civico,
    NumeroDocumento, DataScadenza
) VALUES (
             'VRDLGI90A01H501X', 1800.00, 'gverdi', 'hashedpwd',
             false, CURRENT_TIMESTAMP,
             'Luigi', 'Verdi', '80100', '2024-01-10',
             '3339998888', 'Italiana', 'luigi.verdi@hotel.it',
             'Maschio', 'Manager', '2018-05-01', 'CartaIdentità',
             'Via Milano', 'Napoli', 'Napoli', 20,
             'BB987654', '2030-05-01'
         );

