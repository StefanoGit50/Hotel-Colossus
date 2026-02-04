use hotelcolossus;


INSERT INTO Impiegato (
    CF, Stipendio, UserName, HashPasword, isTemporary, dataScadenzaToken,
    Nome, Cognome, Cap, DataAssunzione, Telefono, EmailAziendale, Sesso, Ruolo,
    Via, Provincia, Comune, Civico, NumeroDocumento, TipoDocumento,
    DataScadenzaDocumento, DataRilascioDocumento, Cittadinanza, IDManager, CFManager
) VALUES


    -- Marco Esposito (FrontDesk)
    (
     'SPSMRC88T12F839A', 1450.00, 'FrontDesk6', '', 0, NULL,
     'Marco', 'Esposito', '80132', '2021-11-05', '3331112233', 'marco.esposito@hotelcolossus.com', 'M', 'FrontDesk',
     'Via Toledo', 'NA', 'Napoli', 45, 'PP5566778', 'Passaporto',
     '2031-11-05', '2021-11-05', 'Italiana',
     1, 'DIRGEN70A01H501X'
        ),

-- Elena Rizzo (Governante)
    (
     'RZZLNE75P45A662J', 1250.00, 'Governante7', '', 0, NULL,
     'Elena', 'Rizzo', '70121', '2019-06-20', '3339988776', 'elena.rizzo@hotelcolossus.com', 'F', 'Governante',
     'Corso Cavour', 'BA', 'Bari', 8, 'PT9988776', 'Patente',
     '2029-06-20', '2019-06-20', 'Italiana',
     1, 'DIRGEN70A01H501X'
        ),

    -- Giulia Conti (FrontDesk)
    (
     'CNTGLI92C54H501Z', 1350.00, 'FrontDesk5', '', 0, NULL,
     'Giulia', 'Conti', '50123', '2023-01-10', '3334445566', 'giulia.conti@hotelcolossus.com', 'F', 'FrontDesk',
     'Via dei Calzaiuoli', 'FI', 'Firenze', 12, 'CI2233445', 'Carta Identità',
     '2033-01-10', '2023-01-10', 'Italiana',
     1, 'DIRGEN70A01H501X'
        );
