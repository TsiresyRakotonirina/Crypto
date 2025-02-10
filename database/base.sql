create database crypto;
\c crypto

create sequence seq_Utilisateur;
create sequence seq_Crypto;
create sequence seq_Historique_fond;
create sequence seq_Portefeuille;

create table utilisateur(
    id_utilisateur varchar(10) primary key, ----UTI00
    email varchar(50),
    login varchar(50),
    mdp varchar(50),
    admin int ---0 si admin 1 sinon
);
create table crypto(
    id_crypto varchar(10) primary key, ----UTI00
    nom varchar(100),
    valeur_actuelle double precision
);
create table valeur_crypto(
    id_crypto varchar(10) references crypto(id_crypto),
    valeur double precision,
    date_heure timestamp
);
create table fond (
    id_utilisateur VARCHAR(10) primary key references utilisateur(id_utilisateur),
    fond double precision
);
create table historique_fond(
    id_histofond varchar(10) primary key, ----HIS00
    id_utilisateur varchar(10) references utilisateur(id_utilisateur),
    types int,--- (-1 retrait , 1 depot)
    montant double precision,
    date_heure timestamp,
    motif text,
    etat int --- (-1 non valide ,0 refuse, 1 valide, 2 a confirmer par mail)
);
create table portefeuille(
    id_porte varchar(10) primary key ,
    id_utilisateur varchar(10) references utilisateur(id_utilisateur)
);
create table portefeuille_crypto(
    id_porte varchar(10) references portefeuille(id_porte),
    id_crypto varchar(10) references crypto(id_crypto),
    nb int
);
create table action(
    id_utilisateur varchar(10) references utilisateur(id_utilisateur),
    type varchar(10),
    montant double precision,
    date_heure timestamp,
    id_crypto varchar(10) references crypto(id_crypto),
    nombre int,
    commission double precision
);
create table commission(
    type varchar(10) primary key, ---vente ,  achat
    pourcentage double precision
);