use hotelcolossus;

create view Storico 
as Select cliente.CF,nome,cognome,Cap,comune,civico,provincia,via,Email,Sesso,telefono,MetodoDiPagamento,Cittadinanza,IsBackListed,Camera.NumeroCamera,NoteCamera,Stato,Prezzo,Prenotazione.IDPrenotazione,DataPrenotazione,DataArrivoCliente,DataPartenzaCliente,NoteAggiuntive,Intestatario,dataScadenza,numeroDocumento,DataRilascio,Tipo
From associato_a,camera,cliente,prenotazione
where associato_a.CF = cliente.CF and associato_a.IDPrenotazione = Prenotazione.IDPrenotazione and associato_a.NumeroCamera = Camera.NumeroCamera