drop view ricevu;
create view Ricevu
as select c.IDRicevutaFiscale,IDPrenotazione,DataEmissione,metodoPagamento,DataPrenotazione,PrezzoTrattamento,TipoTrattamento,c.PrezzoCamera,c.NumeroCamera,c2.CFCliente,c2.NomeCliente,c2.CognomeCliente,c2.isIntestatario,s.NomeServizio,s.PrezzoServizio,s.Quantità
   FROM ( ricevutafiscale JOIN hotelcolossus.cameraricevuta c on ricevutafiscale.IDRicevutaFiscale = c.IDRicevutaFiscale
    Join hotelcolossus.serviziricevuta s on ricevutafiscale.IDRicevutaFiscale = s.IDRicevutaFiscale
    JOIN hotelcolossus.clientiricevuta c2 on ricevutafiscale.IDRicevutaFiscale = c2.IDRicevutaFiscale)
