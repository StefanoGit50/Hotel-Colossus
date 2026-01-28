drop database Hot;
create database Hot;
use Hot;

create table Cliente2(
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

create table Camera2(
	NumeroCamera int not null,
	NumeroMaxOcc int not null,
	NoteCamera varchar(1000) not null,
	Stato enum('OutOfOrder','InPulizia','Occupata','Prenotata','Libera') not null,
    Prezzo double not null,
    primary key(NumeroCamera)
);

create table Prenotazione2(
	IDPrenotazione int not null auto_increment,
    DataPrenotazione date not null,
    DataArrivoCliente date not null,
    DataPartenzaCliente date not null,
    NomeTrattamento varchar(50) not null,
    NoteAggiuntive varchar(1000) not null,
    Intestatario varchar(50) not null,
    dataScadenza date not null,
    numeroDocumento char(8) not null,
    DataRilascio date not null,
    TipoDocumento varchar(50) not null,
    Stato boolean not null,
    ChekIn boolean not null,
    primary key(IDPrenotazione)
);

create table Associato_a2(
	CF char(16) not null,
    NumeroCamera int not null,
    IDPrenotazione int not null,
    PrezzoAcquisto double not null,
	foreign key(CF) references Cliente2(CF) on delete cascade on update cascade,
    foreign key(NumeroCamera) references Camera2(NumeroCamera) on delete cascade on update cascade,
    foreign key(IDPrenotazione) references Prenotazione2(IDPrenotazione) on delete cascade on update cascade,
    primary key(CF,NumeroCamera , IDPrenotazione)
);

create table RicevutaFiscale2(
	IDRicevutaFiscale int not null,
    IDPrenotazione int not null,
	DataEmissione date not null,
    metodoPagamento varchar(50) not null,
    DataPrenotazione Date not null,
    PrezzoTrattamento double not null,
	TipoTrattamento varchar(50) not null,
    foreign key(IDPrenotazione)references Prenotazione2(IDPrenotazione) on delete cascade on update cascade,
    primary key(IDRicevutaFiscale)
);

create table ClientiRicevuta2(
    CFCliente varchar(50) not null,
    IDRicevutaFiscale int not null,
    NomeCliente varchar(50) not null,
    CognomeCliente varchar(50) not null,
    isIntestatario bool not null,
    foreign key(IDRicevutaFiscale) references RicevutaFiscale2(IDRicevutaFiscale),
    primary key(CFCliente , IDRicevutaFiscale)
);

create table CameraRicevuta2(
    IDRicevutaFiscale int not null ,
    NumeroCamera int not null ,
    PrezzoCamera double not null,
    foreign key(IDRicevutaFiscale) references RicevutaFiscale2(IDRicevutaFiscale),
    primary key (IDRicevutaFiscale , NumeroCamera)
);

create table ServiziRicevuta2(
    IDRicevutaFiscale int not null,
    NomeServizio varchar(50) not null,
    Quantità int not null,
    PrezzoServizio double not null,
    foreign key(IDRicevutaFiscale) references RicevutaFiscale2(IDRicevutaFiscale),
    primary key(NomeServizio,IDRicevutaFiscale )
);

create table Servizio2(
	Nome varchar(50) not null,
    Prezzo double not null,
    IDPrenotazione int null DEFAULT null,
    primary key(Nome),
	foreign key(IDPrenotazione) references Prenotazione2(IDPrenotazione) on delete cascade on update cascade
);

create table Trattamento2(
     Nome varchar(50) not null,
     Prezzo double not null,
     IDPrenotazione int null default null,
     primary key(Nome),
     foreign key(IDPrenotazione) references Prenotazione2(IDPrenotazione) on delete cascade on update cascade
);

create table Impiegato2(
    IDImpiegato int not null auto_increment,
	CF char(16) not null,
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
    Ruolo enum('FDclient','Manager','Governante') not null,
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
    primary key(IDImpiegato,CF),
   foreign key(IDImpiegato1,CF1) references Impiegato2(IDImpiegato,CF) on delete cascade on update cascade
);
