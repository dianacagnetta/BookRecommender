package it.bookrecommender.dao.interfaces;

import it.bookrecommender.model.Libro;
import java.util.List;

/**
 * Interfaccia per il Data Access Object (DAO) dei libri.
 * Definisce i metodi necessari per la gestione e la ricerca del catalogo.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
*/
public interface ILibroDAO {

    /**
     * Recupera l'elenco completo dei libri presenti nel sistema.
     * @return Una lista di oggetti Libro.
     * @throws Exception In caso di errori di accesso ai dati.
     */
    List<Libro> getAllBooks() throws Exception;

    /**
     * Esegue una ricerca filtrata nel catalogo.
     * @param titolo Parte del titolo da cercare.
     * @param autore Parte del nome dell'autore.
     * @param anno Anno di pubblicazione (può essere null per ignorare il filtro).
     * @return Una lista di libri che corrispondono ai criteri.
     * @throws Exception In caso di errori durante la query.
     */
    List<Libro> searchBooks(String titolo, String autore, Integer anno) throws Exception;

    /**
     * Cerca un libro specifico tramite il suo identificativo univoco.
     * @param id L'ID del libro nel database.
     * @return L'oggetto Libro trovato, o null se non esiste.
     * @throws Exception In caso di errori SQL.
     */
    Libro getBookById(int id) throws Exception;
}