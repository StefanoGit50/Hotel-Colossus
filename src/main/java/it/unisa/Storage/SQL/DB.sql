drop database hotelcolossus;
create database hotelcolossus;
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
    telefono char(9) not null,
    MetodoDiPagamento varchar(50) not null,
    Cittadinanza varchar(50) not null,
    IsBackListed boolean not null,
	primary key(CF)
);

create table Camera(
	NumeroCamera int not null,
	NumeroMaxOcc int not null,
	NoteCamera varchar(1000) not null,
	Stato varchar(20) not null,
    Piano int not null,
    TipologiaCamera varchar(100) not null,
    Prezzo double not null,
    primary key(NumeroCamera)
);

create table Prenotazione(
	IDPrenotazione int not null auto_increment,
    DataPrenotazione date not null,
    DataArrivoCliente date not null,
    DataPartenzaCliente date not null,
    NoteAggiuntive varchar(1000) not null,
    Intestatario varchar(50) not null,
    dataScadenza date not null,
    numeroDocumento int not null,
    DataRilascio date not null,
    Tipo varchar(50) not null,
    primary key(IDPrenotazione)
);

create table Associato_a(
	CF char(16) not null,
    NumeroCamera int not null,
    IDPrenotazione int not null auto_increment,
    PrezzoAcquisto double not null,
    foreign key(CF) references Cliente(CF),
    foreign key(NumeroCamera) references Camera(NumeroCamera),
    foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione)
);

create table RicevutaFiscale(
	IDRicevutaFiscale int not null auto_increment,
    IDPrenotazione int not null,
	DataEmissione date not null,
    PrezzoCamera double null,
    PrezzoTrattamento double null,
    PrezzoServizio double null,
    foreign key(IDPrenotazione)references Prenotazione(IDPrenotazione),
    primary key(IDRicevutaFiscale , IDPrenotazione)
);

create table Servizio(
	Nome varchar(50) not null,
    Prezzo double not null,
    IDPrenotazione int not null auto_increment,
    primary key(Nome),
    foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione)
);

create table Trattamento(
	Nome varchar(50) not null,
	Prezzo double not null,
	IDPrenotazione int not null auto_increment,
	PrezzoAcquisto double not null,
    primary key(Nome),
    foreign key(IDPrenotazione) references Prenotazione(IDPrenotazione)
);

create table Impiegato(
	CF char(16) not null,
	Nome varchar(50) not null,
    Cognome varchar(50) not null,
    Cap char(5) not null,
    DataAssunzione date not null,
    Telefono char(9) not null,
    Cittadinanza varchar(50) not null,
    EmailAziendale varchar(100) not null,
    Sesso varchar(20) not null,
    Ruolo varchar(50) not null,
    DataRilascio date not null,
    TipoDocumento varchar(50) not null,
    Via varchar(40) not null,
    Provincia varchar(50) not null,
    Comune varchar(50) not null,
    Civico int not null,
    NumeroDocumento int not null,
    DataScadenza date not null,
    CF1 char(16) not null,
    primary key(CF),
    foreign key(CF1) references Impiegato(CF)
);