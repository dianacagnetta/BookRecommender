package it.bookrecommender.dto;

import it.bookrecommender.model.*;
import java.util.List;
import java.io.Serializable;

/**
 * Rappresenta la risposta standard inviata dal server al client.
 * Implementa Serializable per permettere il passaggio dell'oggetto attraverso il socket.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Response implements Serializable {
    /** ID di versione per la serializzazione garantisce la compatibilità tra client e server. */
    private static final long serialVersionUID = 1L;

    /** Indica se l'operazione sul server è andata a buon fine (usato in RegisterController e ValutazioneFormController). */
    public boolean ok;
    
    /** Messaggio di feedback da mostrare all'utente (es. messaggi di errore o conferma). */
    public String message;

    // --- Campi Dati (incapsulano le entità del pacchetto model) ---
    
    /** Utente restituito dopo un login di successo. */
    public Utente utente;             
    
    /** Lista di libri risultanti da una ricerca o dal catalogo. */
    public List<Libro> libri;         
    
    /** Singolo libro richiesto per visualizzarne i dettagli. */
    public Libro libro;               

    /** Elenco delle valutazioni associate a un libro. */
    public List<Valutazione> valutazioni;   
    
    /** Elenco dei libri suggeriti basato sui consigli degli utenti. */
    public List<LibroConsigliato> consigli; 
    
    /** Elenco delle librerie create dall'utente loggato. */
    public List<Libreria> librerie;   
    
    /** Dettagli di una singola libreria selezionata. */
    public Libreria libreria; 


    /**
     * Costruttore predefinito richiesto per la deserializzazione Java.
     */
    public Response() {}

    /**
     * Costruttore per risposte semplici (esito e messaggio).
     * @param ok Esito dell'operazione.
     * @param message Testo descrittivo dell'esito.
     */
    public Response(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    /**
     * Costruttore per il login: restituisce l'utente autenticato.
     * @param ok Esito dell'operazione.
     * @param message Messaggio di benvenuto o errore.
     * @param utente Oggetto Utente da salvare nella Session.
     */
    public Response(boolean ok, String message, Utente utente) {
        this.ok = ok;
        this.message = message;
        this.utente = utente;
    }

    /**
     * Costruttore per liste di libri (ricerca/catalogo).
     * @param ok Esito dell'operazione.
     * @param message Messaggio di feedback.
     * @param libri Lista di oggetti Libro.
     */
    public Response(boolean ok, String message, List<Libro> libri) {
        this.ok = ok;
        this.message = message;
        this.libri = libri;
    }

    /**
     * Costruttore per il dettaglio di un singolo libro.
     * @param ok Esito dell'operazione.
     * @param message Messaggio di feedback.
     * @param libro Oggetto Libro richiesto.
     */
    public Response(boolean ok, String message, Libro libro) {
        this.ok = ok;
        this.message = message;
        this.libro = libro;
    }
}