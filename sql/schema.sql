CREATE SCHEMA IF NOT EXISTS br;

SET search_path TO br;

CREATE TABLE IF NOT EXISTS UtentiRegistrati (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    cognome VARCHAR(50),
    codice_fiscale VARCHAR(16) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    userid VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS Libri (
    id SERIAL PRIMARY KEY,
    titolo VARCHAR(255) NOT NULL,
    autori VARCHAR(255),
    anno_pubblicazione INT,
    editore VARCHAR(255),
    categoria VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Librerie (
    id SERIAL PRIMARY KEY,
    utente_id INT REFERENCES UtentiRegistrati(id),
    nome VARCHAR(100),
    UNIQUE(utente_id, nome)
);

CREATE TABLE IF NOT EXISTS LibrerieLibri (
    libreria_id INT REFERENCES Librerie(id),
    libro_id INT REFERENCES Libri(id),
    PRIMARY KEY(libreria_id, libro_id)
);

CREATE TABLE IF NOT EXISTS ValutazioniLibri (
    id SERIAL PRIMARY KEY,
    utente_id INT REFERENCES UtentiRegistrati(id),
    libro_id INT REFERENCES Libri(id),
    stile INT CHECK(stile BETWEEN 1 AND 5),
    contenuto INT CHECK(contenuto BETWEEN 1 AND 5),
    gradevolezza INT CHECK(gradevolezza BETWEEN 1 AND 5),
    originalita INT CHECK(originalita BETWEEN 1 AND 5),
    edizione INT CHECK(edizione BETWEEN 1 AND 5),
    commento TEXT,
    UNIQUE(utente_id, libro_id)
);

CREATE TABLE IF NOT EXISTS ConsigliLibri (
    id SERIAL PRIMARY KEY,
    utente_id INT REFERENCES UtentiRegistrati(id),
    libro_id INT REFERENCES Libri(id),
    suggerito_libro_id INT REFERENCES Libri(id)
);


