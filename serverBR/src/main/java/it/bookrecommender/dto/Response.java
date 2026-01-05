package it.bookrecommender.dto;

import it.bookrecommender.model.*;
import java.util.List;

/**
 * Oggetto di trasferimento dati (DTO) inviato dal Server al Client.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Response {
	
	/** Indica se l'operazione richiesta al server è andata a buon fine. */
    public boolean ok;

    /** Messaggio testuale di feedback (es. descrizione dell'errore o conferma di successo). */
    public String message;
    
    /** L'oggetto {@link Utente} restituito dal server (popolato tipicamente dopo il login). */
    public Utente utente;

    /** Lista di oggetti {@link Libro} (es. risultati di una ricerca o l'intero catalogo). */
    public List<Libro> libri;

    /** Un singolo oggetto {@link Libro} contenente i dettagli completi. */
    public Libro libro;
    /** Elenco delle {@link Valutazione} associate a un determinato libro. */
    public List<Valutazione> valutazioni;

    /** Elenco di libri suggeriti ({@link LibroConsigliato}) basati sulle preferenze o correlazioni. */
    public List<LibroConsigliato> consigli;
    
    /** Elenco delle {@link Libreria} appartenenti a un utente specifico. */
    public List<Libreria> librerie;

    /** Costruttore di default. */
    public Response() {}

   /**
     * Costruttore per messaggi di esito semplice (senza dati allegati).
     * Utilizzato solitamente per conferme di operazioni (es. cancellazione riuscita)
     * o per segnalare errori generici.
     *
     * @param ok      Stato dell'operazione (true se riuscita).
     * @param message Messaggio descrittivo o di errore.
     */
    public Response(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    /**
     * Costruttore per risposte contenenti i dati di un utente (es. dopo il Login).
     * Sfrutta il concatenamento dei costruttori tramite {@code this()}.
     *
     * @param ok      Stato dell'operazione.
     * @param message Messaggio descrittivo.
     * @param utente  L'oggetto {@link Utente} da restituire al client.
     */
    public Response(boolean ok, String message, Utente utente) {
        this(ok, message);
        this.utente = utente;
    }

    /**
     * Costruttore per risposte contenenti liste di libri (es. risultati di una Ricerca).
     *
     * @param ok      Stato dell'operazione.
     * @param message Messaggio descrittivo.
     * @param libri   La lista di oggetti {@link Libro} trovati.
     */
    public Response(boolean ok, String message, List<Libro> libri) {
        this(ok, message);
        this.libri = libri;
    }

    /**
     * Costruttore per i dettagli di un singolo libro specifico.
     *
     * @param ok      Stato dell'operazione.
     * @param message Messaggio descrittivo.
     * @param libro   L'istanza dell'oggetto {@link Libro} richiesto.
     */
    public Response(boolean ok, String message, Libro libro) {
        this(ok, message);
        this.libro = libro;
    }
}