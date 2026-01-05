package it.bookrecommender.dao.interfaces;

import it.bookrecommender.model.Valutazione;
import java.util.List;

/**
 * Interfaccia per la gestione della persistenza delle valutazioni e recensioni.
 * Questo DAO definisce le operazioni necessarie per memorizzare i feedback dettagliati
 * degli utenti sui libri, permettendo la gestione di punteggi multi-criterio 
 * e il calcolo delle medie statistiche di gradimento.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public interface IValutazioniDAO {

    /**
     * Salva una valutazione completa e dettagliata per un libro nel sistema.
     * @param userId             L'identificativo dell'utente che rilascia la valutazione.
     * @param libroId            L'ID del libro valutato.
     * @param stile              Voto numerico per lo stile (es. da 1 a 5).
     * @param stileNote          Commento testuale specifico per lo stile.
     * @param contenuto          Voto numerico per il contenuto.
     * @param contenutoNote      Commento testuale specifico per il contenuto.
     * @param gradevolezza       Voto numerico per la gradevolezza complessiva.
     * @param gradevolezzaNote   Commento testuale specifico per la gradevolezza.
     * @param originalita        Voto numerico per l'originalità dell'opera.
     * @param originalitaNote    Commento testuale specifico per l'originalità.
     * @param edizione           Voto numerico per la qualità dell'edizione.
     * @param edizioneNote       Commento testuale specifico per l'edizione.
     * @param votoFinale         Media ponderata o voto complessivo calcolato.
     * @param noteGenerali       Commento riassuntivo finale dell'utente.
     * @return {@code true} se il salvataggio è riuscito, {@code false} altrimenti.
     * @throws Exception Se l'utente ha già valutato questo libro o in caso di errore SQL.
     */
    boolean salvaValutazione(String userId, int libroId, int stile, String stileNote, int contenuto, String contenutoNote,
                             int gradevolezza, String gradevolezzaNote, int originalita, String originalitaNote,
                             int edizione, String edizioneNote, int votoFinale, String noteGenerali) throws Exception;

    /**
     * Recupera l'elenco di tutte le valutazioni (voti e commenti) associate a un libro.
     * @param libroId L'ID del libro di cui si vogliono leggere le recensioni.
     * @return Una {@link List} di oggetti {@link Valutazione} contenente i feedback degli utenti.
     * @throws Exception Se si verifica un errore durante l'interrogazione del database.
     */
    List<Valutazione> getValutazioniLibro(int libroId) throws Exception;

    /**
     * Calcola la media aritmetica di tutti i voti finali ricevuti da un determinato libro.
     * @param libroId L'ID del libro per il quale calcolare il rating medio.
     * @return Il valore medio calcolato come {@code double}.
     * @throws Exception In caso di errori durante il calcolo aggregato sul database.
     */
    double calcolaMediaValutazioni(int libroId) throws Exception;
}