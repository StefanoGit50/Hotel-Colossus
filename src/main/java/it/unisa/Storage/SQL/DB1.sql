drop database IF EXISTS hotelcolossus;
create database hotelColossus;
use hotelColossus;

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
                        Nazionalita varchar(50) not null,
                        DataDiNascita date not null,
                        IsBlackListed boolean not null,
                        primary key(CF)
);

create table Camera(
                       NumeroCamera int not null,
                       NomeCamera varchar(50) not null,
                       NumeroMaxOcc int not null,
                       NoteCamera varchar(1000),
                       Stato enum('OutOfOrder','InPulizia','Occupata','Libera','Prenotata') not null,
                       Prezzo double not null,
                       primary key(NumeroCamera)
);

create table Servizio(
                         IDServizio int auto_increment,
                         Nome varchar(50)UNIQUE,
                         Prezzo double not null,
                         primary key(IDServizio)
);



create table Trattamento(
                            Nome varchar(50) not null,
                            Prezzo double not null,
                            primary key(Nome)
);

create table Prenotazione(
                             IDPrenotazione int not null auto_increment,
                             NomeIntestatario varchar(50) not null,
                             DataCreazionePrenotazione date not null,
                             DataArrivoCliente date not null,
                             DataPartenzaCliente date not null,
                             DataEmissioneRicevuta date,
                             NumeroDocumento char(9) not null,
                             DataRilascioDocumento date not null,
                             DataScadenzaDocumento date not null,
                             NomeTrattamento varchar(50),
                             NoteAggiuntive varchar(1000),
                             TipoDocumento varchar(50) not null,
                             Stato boolean not null Default true,
                             CheckIn boolean not null Default false,
                             PrezzoAcquistoTrattamento double,
                             MetodoPagamento varchar(50) ,
                             Cittadinanza varchar(50),
                             primary key(IDPrenotazione),
                             foreign key(NomeTrattamento) references Trattamento(Nome) on update cascade
);

create table Associato_a(
                            IDAssociazione int auto_increment ,
                            CF char(16) ,
                            NumeroCamera int,
                            NominativoCliente varchar(50) not null,
                            NumeroCameraStorico int not null,
                            IDPrenotazione int not null,
                            PrezzoAcquisto double not null,
                            foreign key(CF) references Cliente(CF)on delete set null on update cascade,
                            foreign key(NumeroCamera) references Camera(NumeroCamera) on delete set null on update cascade,
                            foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione) on delete cascade on update cascade,
                            primary key(IDAssociazione)
);

create table Ha(
                   IDDettaglio int AUTO_INCREMENT PRIMARY KEY,
                   IDPrenotazione int not null,
                   IDServizio int NULL,
                   quantità int NULL,
                   NomeServizioAcquistato varchar(50) not null,
                   PrezzoAcquistoServizio double not null,
                   UNIQUE(IDServizio,IDPrenotazione),

                   foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione)
                       on delete cascade on update cascade,
                   foreign key(IDServizio) references Servizio(IDServizio)
                       on delete set null on update cascade);

create table Impiegato(
                          IDImpiegato int not null auto_increment,
                          CF char(16) not null UNIQUE,
                          Stipendio double not null,
                          UserName varchar(50) not null,
                          HashPasword varchar(100) not null,
                          isTemporary boolean Default false,
                          dataScadenzaToken TIMESTAMP default null,
                          Nome varchar(50) not null,
                          Cognome varchar(50) not null,
                          Cap char(5) not null,
                          DataAssunzione date not null,
                          Telefono char(15) not null,
                          EmailAziendale varchar(100) not null,
                          Sesso varchar(20) not null,
                          Ruolo enum('FrontDesk','Manager','Governante') not null,
                          Via varchar(40) not null,
                          Provincia varchar(50) not null,
                          Comune varchar(50) not null,
                          Civico int not null,
                          NumeroDocumento char(9) not null,
                          TipoDocumento varchar(50) not null,
                          DataScadenzaDocumento date not null,
                          DataRilascioDocumento date not null,
                          Cittadinanza varchar(50) not null,
                          IDManager int null DEFAULT 0,
                          CFManager char(16) null DEFAULT null,
                          UNIQUE(CF),
                          UNIQUE(IDImpiegato),
                          primary key(IDImpiegato,CF),
                          foreign key(IDManager,CFManager) references Impiegato(IDImpiegato,CF) on delete cascade on update cascade
);
