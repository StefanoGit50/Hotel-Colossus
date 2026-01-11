use hotelcolossus;
drop view PrenotaIS;
CREATE view PrenotaIS as
SELECT  p.*, t.Nome as TrattamentoNome, t.Prezzo as TrattamentoPrezzo,
        c.CF, c.nome as ClienteNome, c.cognome as ClienteCognome,   c.Email, c.telefono, c.Sesso, c.DataDiNascita, c.Cittadinanza,
        c.via, c.civico, c.comune, c.provincia, c.Cap,  c.MetodoDiPagamento, c.IsBackListed,
        cam.NumeroCamera, cam.NumeroMaxOcc, cam.NoteCamera, cam.Stato as CameraStato, cam.Prezzo
            as CameraPrezzo,s.Nome as ServizioNome, s.Prezzo as ServizioPrezzo
From  Prenotazione p
          join Trattamento t ON p.NomeTrattamento = t.Nome
          join Associato_a a ON p.IDPrenotazione = a.IDPrenotazione
          JOIN Cliente c ON a.CF = c.CF
          JOIN Camera cam ON a.NumeroCamera = cam.NumeroCamera
          LEFT JOIN Servizio s ON s.IDPrenotazione = p.IDPrenotazione

