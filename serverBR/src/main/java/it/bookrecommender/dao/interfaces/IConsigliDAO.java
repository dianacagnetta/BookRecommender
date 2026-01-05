package it.bookrecommender.dao.interfaces;

import it.bookrecommender.model.LibroConsigliato;
import java.util.List;

/**
 * Interfaccia per la gestione della persistenza dei consigli di lettura.
 * <p>
 * Definisce i metodi necessari per associare libri suggeriti a un libro target,
 * permettendo agli utenti di condividere raccomandazioni basate sulla loro esperienza.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public interface IConsigliDAO {

    /**
     * Registra un nuovo consiglio di lettura nel sistema.
     * @param userId     L'ID dell'utente che sta fornendo il consiglio.
     * @param libroId    L'ID del libro principale (target) a cui si riferisce il consiglio.
     * @param suggeritoId L'ID del libro che viene suggerito in associazione al principale.
     * @throws Exception Se si verifica un errore durante il salvataggio su database (es. violazione di vincoli).
     */
    void addConsiglio(String userId, int libroId, int suggeritoId) throws Exception;

    /**
     * Recupera la lista di tutti i libri consigliati associati a un determinato libro.
     * @param libroId L'ID del libro per il quale si vogliono ottenere i suggerimenti.
     * @return Una {@link List} di oggetti {@link LibroConsigliato} contenente i dati dei libri suggeriti.
     * @throws Exception Se si verifica un errore durante la query di recupero dati.
     */
    List<LibroConsigliato> getConsigli(int libroId) throws Exception;

    /**
     * Rimuove tutti i consigli inseriti da un utente specifico per un determinato libro.
     * @param userId  L'ID dell'utente che ha creato i consigli da eliminare.
     * @param libroId L'ID del libro target da cui rimuovere le associazioni.
     * @throws Exception Se si verifica un errore durante la cancellazione dei record.
     */
    void clearConsigli(String userId, int libroId) throws Exception;
}