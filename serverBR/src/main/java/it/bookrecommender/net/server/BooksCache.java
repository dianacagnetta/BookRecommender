package it.bookrecommender.net.server;

import java.util.List;
import java.util.ArrayList;
import it.bookrecommender.model.Libro; 
import it.bookrecommender.dao.interfaces.ILibroDAO; 
import it.bookrecommender.dao.jdbc.LibroDAOImpl; 

/**
 * Semplice cache in-memory per l'elenco dei libri del catalogo.
 * <p>
 * Caricata all'avvio del server per velocizzare le operazioni di ricerca e visualizzazione
 * generale, riducendo il carico di query ripetitive sul database.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class BooksCache {
    /** Lista interna sincronizzata che contiene l'intero catalogo libri. */
    private static final List<Libro> cache = new ArrayList<>();

    /**
     * Costruttore privato per impedire l'istanziazione della classe.
     * Trattandosi di una utility class con soli metodi statici, non deve essere creata come oggetto.
     */
    private BooksCache() {}

    /**
     * Inizializza la cache caricando tutti i libri tramite il DAO.
     * Svuota la memoria esistente e la ripopola chiamando il metodo {@link ILibroDAO#getAllBooks()}.
     * Se il database non è raggiungibile, stampa l'errore nel log del server senza arrestare il sistema.
     */
    public static synchronized void init() {
        try {
            ILibroDAO libroDao = new LibroDAOImpl();
            List<Libro> libri = libroDao.getAllBooks();
            
            cache.clear();
            if (libri != null) {
                cache.addAll(libri);
            }
            
            // Messaggio fondamentale per il debug: controlla questo log nel terminale del Server
            System.out.println(">>> BooksCache: caricati " + cache.size() + " libri con successo dal DB.");
        } catch (Exception e) {
            System.err.println(">>> BooksCache: ERRORE CRITICO nel caricamento libri: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Restituisce una copia dell'elenco completo dei libri.
     * <b>Nota tecnica:</b> Viene restituita una "Defensive Copy" (nuova istanza di ArrayList)
     * per garantire che manipolazioni della lista da parte del ClientHandler non alterino
     * la sorgente dati originale in cache.
     * @return Una lista (copia) di oggetti {@link Libro}.
     */
    public static synchronized List<Libro> getAll() {
        return new ArrayList<>(cache);
    }

    /**
     * Sincronizza manualmente la cache con lo stato attuale del database.
     * Da chiamare tipicamente dopo l'aggiunta massiva di nuovi libri.
     */
    public static synchronized void reload() {
        init();
    }
}