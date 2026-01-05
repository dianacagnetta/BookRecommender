package it.bookrecommender.dao.interfaces;

import it.bookrecommender.model.Utente;
import it.bookrecommender.model.Libro;
import it.bookrecommender.model.Valutazione;
import it.bookrecommender.model.LibroConsigliato;
import it.bookrecommender.model.Libreria;
import java.util.List;

/**
 * Interfaccia principale per la gestione delle operazioni dell'utente e della logica di business.
 * <p>
 * Definisce i contratti per l'autenticazione, la ricerca di libri, la gestione delle 
 * valutazioni e il coordinamento delle attività sulle librerie personali e i consigli.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public interface IUtenteDAO {

    /**
     * Esegue l'autenticazione di un utente nel sistema.
     * @param userid L'identificativo univoco dell'utente.
     * @param passwordHash L'hash della password per il confronto di sicurezza.
     * @return L'oggetto {@link Utente} se le credenziali sono corrette.
     * @throws Exception Se le credenziali sono errate o in caso di errore di connessione.
     */
    Utente login(String userid, String passwordHash) throws Exception;

    /**
     * Registra un nuovo utente nel database.
     * @param u L'oggetto {@link Utente} contenente i dati anagrafici e le credenziali.
     * @return {@code true} se la registrazione è andata a buon fine.
     * @throws Exception Se lo userid è già presente o i dati non sono validi.
     */
    boolean registra(Utente u) throws Exception;

    /**
     * Cerca libri nel catalogo in base a criteri specifici (filtri opzionali).
     * @param titolo Titolo o parte del titolo del libro.
     * @param autore Nome o cognome dell'autore.
     * @param anno Anno di pubblicazione.
     * @return Una {@link List} di {@link Libro} che soddisfano i criteri di ricerca.
     * @throws Exception In caso di errore nella query SQL.
     */
    List<Libro> searchBooks(String titolo, String autore, Integer anno) throws Exception;

    /**
     * Recupera l'intero catalogo dei libri disponibili.
     * @return Lista completa di tutti i libri.
     * @throws Exception Se il recupero dei dati fallisce.
     */
    List<Libro> getBooks() throws Exception;

    /**
     * Recupera tutte le valutazioni e i commenti associati a un libro.
     * @param libroId ID del libro target.
     * @return Lista di oggetti {@link Valutazione}.
     * @throws Exception In caso di errore nel recupero.
     */
    List<Valutazione> getValutazioniLibro(int libroId) throws Exception;

    /**
     * Ottiene i suggerimenti correlati a un libro specifico.
     * @param libroId ID del libro target.
     * @return Lista di libri consigliati dagli utenti.
     * @throws Exception In caso di errore nel database.
     */
    List<LibroConsigliato> getConsigli(int libroId) throws Exception;

    /**
     * Recupera i dettagli completi di un singolo libro tramite il suo ID.
     * @param libroId Identificativo del libro.
     * @return L'oggetto {@link Libro} corrispondente.
     * @throws Exception Se il libro non viene trovato.
     */
    Libro getBookById(int libroId) throws Exception;

    /**
     * Inserisce una valutazione dettagliata per un libro, includendo voti e commenti per ogni categoria.
     * @param utenteId ID dell'utente che valuta.
     * @param libroId ID del libro da valutare.
     * @param stile Voto per lo stile (es. 1-5).
     * @param contenuto Voto per il contenuto.
     * @param gradevolezza Voto per la gradevolezza.
     * @param originalita Voto per l'originalità.
     * @param edizione Voto per l'edizione.
     * @param votoFinale Media o voto complessivo.
     * @param commentoStile Testo del commento sullo stile.
     * @param commentoContenuto Testo del commento sul contenuto.
     * @param commentoGradevolezza Testo del commento sulla gradevolezza.
     * @param commentoOriginalita Testo del commento sull'originalità.
     * @param commentoEdizione Testo del commento sull'edizione.
     * @param commentoGenerale Testo del commento riassuntivo.
     * @throws Exception Se l'utente ha già valutato il libro o per errori di persistenza.
     */
    void aggiungiValutazione(int utenteId, int libroId, int stile, int contenuto, int gradevolezza, 
                            int originalita, int edizione, int votoFinale, String commentoStile, 
                            String commentoContenuto, String commentoGradevolezza, String commentoOriginalita, 
                            String commentoEdizione, String commentoGenerale) throws Exception;

    /**
     * Recupera le librerie personali di un utente.
     * @param utenteId ID dell'utente.
     * @return Lista di {@link Libreria}.
     * @throws Exception In caso di errore di sistema.
     */
    List<Libreria> getLibrerieUtente(int utenteId) throws Exception;

    /**
     * Crea una nuova libreria vuota per l'utente.
     * @param utenteId ID del proprietario.
     * @param nome Nome della libreria.
     * @throws Exception Se il nome è duplicato per lo stesso utente.
     */
    void creaLibreria(int utenteId, String nome) throws Exception;

    /**
     * Inserisce un libro in una libreria esistente.
     * @param libreriaId ID della libreria.
     * @param libroId ID del libro.
     * @throws Exception Se il libro è già presente o la libreria non esiste.
     */
    void addLibroAllaLibreria(int libreriaId, int libroId) throws Exception;

    /**
     * Ottiene l'elenco dei libri contenuti in una specifica libreria.
     * @param libreriaId ID della libreria.
     * @return Lista di {@link Libro}.
     * @throws Exception In caso di errore nel recupero.
     */
    List<Libro> getLibriInLibreria(int libreriaId) throws Exception;

    /**
     * Aggiunge un consiglio di lettura (associazione tra due libri).
     * @param utenteId ID dell'utente che fornisce il consiglio.
     * @param libroId ID del libro base.
     * @param suggeritoId ID del libro suggerito.
     * @throws Exception In caso di errore nel database.
     */
    void addConsiglio(int utenteId, int libroId, int suggeritoId) throws Exception;

    /**
     * Rimuove tutti i consigli forniti da un utente per un determinato libro.
     * @param utenteId ID dell'utente.
     * @param libroId ID del libro base.
     * @throws Exception In caso di errore durante la cancellazione.
     */
    void clearConsigli(int utenteId, int libroId) throws Exception;
}