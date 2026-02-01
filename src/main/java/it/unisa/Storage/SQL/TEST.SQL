drop database hotelcolossus;
create database HotelColossus;
use HotelColossus;

create table Cliente(
                        CF char(16) not null,
                        nome varchar(50) not null,
                        cognome varchar(50) not null,
                        Cap char(5) not null,
                        comune varchar(50) not null,
                        civico int not null,
                        provincia varchar(50) not null,
                        via varchar(50) not null,
                        Email varchar(100) not null,
                        Sesso varchar(40) not null,
                        telefono char(15) not null,
                        Cittadinanza varchar(50) not null,
                        Nazionalità varchar(50) not null,
                        DataDiNascita date not null,
                        IsBackListed boolean not null,
                        primary key(CF)
);

create table Camera(
                       NumeroCamera int not null,
                       NumeroMaxOcc int not null,
                       NoteCamera varchar(1000) not null,
                       Stato enum('OutOfOrder','InPulizia','Occupata','Prenotata','Libera') not null,
                       Prezzo double not null,
                       primary key(NumeroCamera)
);

create table Prenotazione(
                             IDPrenotazione int not null auto_increment,
                             DataPrenotazione date not null,
                             DataArrivoCliente date not null,
                             DataPartenzaCliente date not null,
                             NomeTrattamento varchar(50) null,
                             NoteAggiuntive varchar(1000) not null,
                             Intestatario varchar(50) not null,
                             dataScadenza date not null,
                             numeroDocumento char(9) not null,
                             DataRilascio date not null,
                             TipoDocumento varchar(50) not null,
                             Stato boolean not null,
                             CheckIn boolean not null,
                             primary key(IDPrenotazione)
);

create table Associato_a(
                            CF char(16) not null,
                            NumeroCamera int not null,
                            IDPrenotazione int not null,
                            PrezzoAcquisto double not null,
                            foreign key(CF) references Cliente(CF) on delete cascade on update cascade,
                            foreign key(NumeroCamera) references Camera(NumeroCamera) on delete cascade on update cascade,
                            foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione) on delete cascade on update cascade,
                            primary key(CF, NumeroCamera, IDPrenotazione)
);

create table RicevutaFiscale(
                                IDRicevutaFiscale int not null,
                                IDPrenotazione int not null,
                                DataEmissione date not null,
                                metodoPagamento varchar(50) not null,
                                DataPrenotazione Date not null,
                                PrezzoTrattamento double not null,
                                TipoTrattamento varchar(50) not null,
                                foreign key(IDPrenotazione)references Prenotazione(IDPrenotazione) on delete cascade on update cascade,
                                primary key(IDRicevutaFiscale)
);

create table ClientiRicevuta(
                                CFCliente varchar(50) not null,
                                IDRicevutaFiscale int not null,
                                NomeCliente varchar(50) not null,
                                CognomeCliente varchar(50) not null,
                                isIntestatario bool not null,
                                foreign key(IDRicevutaFiscale) references RicevutaFiscale(IDRicevutaFiscale),
                                primary key(CFCliente , IDRicevutaFiscale)
);

create table CameraRicevuta(
                               IDRicevutaFiscale int not null ,
                               NumeroCamera int not null ,
                               PrezzoCamera double not null,
                               foreign key(IDRicevutaFiscale) references RicevutaFiscale(IDRicevutaFiscale),
                               primary key (IDRicevutaFiscale , NumeroCamera)
);

create table ServiziRicevuta(
                                IDRicevutaFiscale int not null,
                                NomeServizio varchar(50) not null,
                                Quantità int not null,
                                PrezzoServizio double not null,
                                foreign key(IDRicevutaFiscale) references RicevutaFiscale(IDRicevutaFiscale),
                                primary key(NomeServizio,IDRicevutaFiscale )
);

create table Servizio(
                         Nome varchar(50) not null,
                         Prezzo double not null,
                         IDPrenotazione int null DEFAULT null,
                         primary key(Nome),
                         foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione) on delete cascade on update cascade
);

create table Trattamento(
                            Nome varchar(50) not null,
                            Prezzo double not null,
                            IDPrenotazione int null default null,
                            primary key(Nome),
                            foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione) on delete cascade on update cascade
);

create table Impiegato(
                          IDImpiegato int not null auto_increment,
                          CF char(16) not null UNIQUE,
                          Stipedio double not null,
                          UserName varchar(50) not null,
                          HashPasword varchar(50) not null,
                          isTempurali boolean not null Default false,
                          dataScadenzaToken TIMESTAMP not null,
                          Nome varchar(50) not null,
                          Cognome varchar(50) not null,
                          Cap char(5) not null,
                          DataAssunzione date not null,
                          Telefono char(15) not null,
                          Cittadinanza varchar(50) not null,
                          EmailAziendale varchar(100) not null,
                          Sesso varchar(20) not null,
                          Ruolo enum('FrontDesk','Manager','Governante') not null,
                          DataRilascio date not null,
                          TipoDocumento varchar(50) not null,
                          Via varchar(40) not null,
                          Provincia varchar(50) not null,
                          Comune varchar(50) not null,
                          Civico int not null,
                          NumeroDocumento char(9) not null,
                          DataScadenza date not null,
                          IDImpiegato1 int null DEFAULT 0,
                          CF1 char(16) null DEFAULT null,
                          UNIQUE(CF),
                          primary key(IDImpiegato,CF),
                          foreign key(IDImpiegato1,CF1) references Impiegato(IDImpiegato,CF) on delete cascade on update cascade
);


INSERT INTO Cliente (
    CF, nome, cognome, Cap, comune, civico, provincia, via,
    Email, Sesso, telefono, Cittadinanza, Nazionalità,
    DataDiNascita, IsBackListed
) VALUES (
             'RSSMRA85M01H501Z', 'Mario', 'Rossi', '80100', 'Napoli', 15,
             'Napoli', 'Via Roma', 'mario.rossi@email.it',
             'Maschio', '3331234567', 'Italiana', 'Italiana',
             '1985-08-01', false
         );

INSERT INTO Camera (
    NumeroCamera, NumeroMaxOcc, NoteCamera, Stato, Prezzo
) VALUES (101, 2, 'Camera matrimoniale vista mare', 'Occupata', 120.00 ),
         (102 , 3 , 'Camera per due persone','Libera',130.0),
         (404 , 1, 'Suite Familiare, due stanze comunicanti, balcone ampio','Libera',250.0);

INSERT INTO Prenotazione (
    DataPrenotazione, DataArrivoCliente, DataPartenzaCliente,
    NomeTrattamento, NoteAggiuntive, Intestatario,
    dataScadenza, numeroDocumento, DataRilascio,
    TipoDocumento, Stato, CheckIn
) VALUES (
             '2026-02-01', '2026-02-10', '2026-02-15',
             'Pensione Completa', 'Nessuna nota',
             'Mario Rossi', '2026-02-09',
             'AA123456', '2020-01-01',
             'CartaIdentità', true, false
         );

INSERT INTO Associato_a (
    CF, NumeroCamera, IDPrenotazione, PrezzoAcquisto
) VALUES (
             'RSSMRA85M01H501Z', 101, 1, 600.00
         );

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

