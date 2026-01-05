package it.bookrecommender.dao.interfaces;

import it.bookrecommender.model.Libreria;
import it.bookrecommender.model.Libro;
import java.util.List;

/**
 * Interfaccia per la gestione della persistenza delle librerie personali degli utenti.
 * <p>
 * Questo DAO definisce i metodi per creare collezioni personalizzate e gestire
 * l'associazione molti-a-molti tra le librerie e i libri in esse contenuti.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public interface ILibrerieDAO {

    /**
     * Recupera tutte le librerie create da un determinato utente.
     * @param utenteId L'ID numerico dell'utente proprietario.
     * @return Una {@link List} di oggetti {@link Libreria} appartenenti all'utente.
     * @throws Exception Se si verifica un errore durante l'accesso ai dati.
     */
    List<Libreria> getLibrerieByUtenteId(int utenteId) throws Exception;

    /**
     * Crea una nuova libreria (collezione) per un utente.
     * @param utenteId L'ID dell'utente che crea la libreria.
     * @param nome     Il nome da assegnare alla nuova libreria (es. "Da leggere", "Preferiti").
     * @throws Exception Se il nome è già esistente per quell'utente o in caso di errore DB.
     */
    void creaLibreria(int utenteId, String nome) throws Exception;

    /**
     * Aggiunge un libro esistente a una specifica libreria dell'utente.
     * @param libreriaId L'ID della libreria di destinazione.
     * @param libroId    L'ID del libro da aggiungere.
     * @return {@code true} se l'inserimento è avvenuto con successo, {@code false} altrimenti.
     * @throws Exception Se il libro è già presente nella libreria o per errori di connessione.
     */
    boolean aggiungiLibroALibreria(int libreriaId, int libroId) throws Exception;

 /**
     * Rimuove un libro da una specifica libreria.
     * @param libreriaId L'ID della libreria da cui rimuovere il libro.
     * @param libroId    L'ID del libro da rimuovere.
     * @return {@code true} se la rimozione è avvenuta con successo, {@code false} altrimenti.
     * @throws Exception In caso di errore durante l'esecuzione della query SQL.
     */
    boolean rimuoviLibroDaLibreria(int libreriaId, int libroId) throws Exception;

    /**
     * Restituisce l'elenco dei libri contenuti in una determinata libreria.
     * @param libreriaId L'ID della libreria da consultare.
     * @return Una {@link List} di oggetti {@link Libro} presenti nella libreria specificata.
     * @throws Exception Se la libreria non esiste o non è possibile recuperare i dati.
     */
    List<Libro> getLibriInLibreria(int libreriaId) throws Exception;
}