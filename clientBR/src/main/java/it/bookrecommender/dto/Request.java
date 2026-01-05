
package it.bookrecommender.dto;

import it.bookrecommender.model.Libreria;
import java.util.List;
import java.io.Serializable; 

/**
 * Oggetto che incapsula le richieste inviate dal client al server.
 * Implementa Serializable per permettere l'invio su Socket. 
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Request implements Serializable { // 2. Aggiungi l'interfaccia

    // 3. È buona norma aggiungere un ID di versione per la serializzazione
    private static final long serialVersionUID = 1L;

    /** Azione da eseguire sul server. */
    public String action;

    /** UserID per login o registrazione. */
    public String userid;
    /** Password (hash) per l'autenticazione. */
    public String passwordHash;
    /** Nome dell'utente. */
    public String nome;
    /** Cognome dell'utente. */
    public String cognome;
    /** Codice fiscale dell'utente. */
    public String codiceFiscale;
    /** Email dell'utente. */
    public String email;
    /** Titolo del libro per ricerca. */
    public String titolo;
    /** Autore per ricerca. */
    public String autore;
    /** Anno per ricerca. */
    public Integer anno;
    /** ID del libro interessato. */
    public int libroId;
    /** ID dell'utente interessato. */
    public int utenteId;
    /** Voto per lo stile. */
    public int stile;
    /** Voto per il contenuto. */
    public int contenuto;
    /** Voto per la gradevolezza. */
    public int gradevolezza;
    /** Voto per l'originalità. */
    public int originalita;
    /** Voto per l'edizione. */
    public int edizione;
    /** Media finale della valutazione. */
    public int votoFinale;
    /** Commento generale. */
    public String commentoGenerale;
    /** Commento specifico sullo stile. */
    public String commentoStile;
    /** Commento specifico sul contenuto. */
    public String commentoContenuto;
    /** Commento specifico sulla gradevolezza. */
    public String commentoGradevolezza;
    /** Commento specifico sull'originalità. */
    public String commentoOriginalita;
    /** Commento specifico sull'edizione. */
    public String commentoEdizione;
    /** ID della libreria. */
    public int libreriaId;
    /** Lista di librerie. */
    public List<Libreria> librerie;
    /** Nome della libreria (per creazione). */
    public String nomeLibreria;
    /** Lista di ID di libri suggeriti. */
    public List<Integer> suggeriti; 
    /** ID del libro suggerito singolarmente. */
    public int suggerito;
    /** ID del libro target per i consigli. */
    public int libroTarget;

    /** Costruttore di default. */
    public Request() {}
}