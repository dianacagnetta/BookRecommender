package it.bookrecommender.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) per le richieste client-to-server.
 * Contiene i parametri necessari per autenticazione, ricerca, valutazioni e gestione librerie.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Request {

	/**
	 * Costruttore predefinito della classe.
	 */
	public Request() {}
	/** Identifica l'operazione da eseguire sul server (es. "LOGIN", "SEARCH_BOOKS"). */
    public String action;

    /** Lo username univoco dell'utente per il login. */
    public String userid;

    /** L'hash della password per l'autenticazione sicura. */
    public String passwordHash;

    /** Il nome di battesimo dell'utente. */
    public String nome;

    /** Il cognome dell'utente. */
    public String cognome;

    /** Il codice fiscale dell'utente. */
    public String codiceFiscale;

    /** L'indirizzo email dell'utente. */
    public String email;

    /** Il titolo (o parte di esso) del libro da ricercare. */
    public String titolo;

    /** L'autore del libro da ricercare. */
    public String autore;

    /** L'anno di pubblicazione del libro per filtrare la ricerca. */
    public Integer anno;

    /** L'identificativo univoco del libro interessato dalla richiesta. */
    public int libroId;

    /** L'identificativo univoco dell'utente interessato dalla richiesta. */
    public int utenteId;

    /** L'identificativo della libreria specifica dell'utente. */
    public int libreriaId;

    /** Il nome della nuova libreria da creare. */
    public String nomeLibreria;

    /** Voto numerico assegnato allo stile (1-5). */
    public int stile;

    /** Voto numerico assegnato al contenuto (1-5). */
    public int contenuto;

    /** Voto numerico assegnato alla gradevolezza (1-5). */
    public int gradevolezza;

    /** Voto numerico assegnato all'originalità (1-5). */
    public int originalita;

    /** Voto numerico assegnato all'edizione (1-5). */
    public int edizione;

    /** Media calcolata o voto totale della valutazione. */
    public int votoFinale;

    /** Commento testuale specifico per lo stile. */
    public String commentoStile;

    /** Commento testuale specifico per il contenuto. */
    public String commentoContenuto;

    /** Commento testuale specifico per la gradevolezza. */
    public String commentoGradevolezza;

    /** Commento testuale specifico per l'originalità. */
    public String commentoOriginalita;

    /** Commento testuale specifico per l'edizione. */
    public String commentoEdizione;

    /** Opinione globale e riassuntiva sul libro. */
    public String commentoGenerale;

   
	
    /** Lista di ID dei libri suggeriti come correlati. */
    public List<Integer> suggeriti;

    /** Singolo ID del libro suggerito in una specifica operazione. */
    public int suggerito;

    /** ID del libro "fulcro" a cui associare i suggerimenti. */
    public int libroTarget;
}