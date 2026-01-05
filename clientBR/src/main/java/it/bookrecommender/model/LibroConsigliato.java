package it.bookrecommender.model;

import java.io.Serializable;

/**
 * Rappresenta un libro consigliato con il conteggio delle raccomandazioni ricevute.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class LibroConsigliato implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Il titolo del libro che è stato consigliato. */
    private String titolo;

    /** * Il numero di volte (frequenza) in cui questo libro è stato suggerito 
     * in relazione al libro principale. 
     */
    private int volte;

    /** * Una stringa che elenca i nomi degli utenti che hanno suggerito questo libro, 
     * solitamente separati da virgole. 
     */
    private String suggeritori;

    /**
     * Costruttore della classe LibroConsigliato.
     * @param titolo Il titolo del libro suggerito.
     * @param volte Il numero di volte che il libro è stato consigliato.
     */
    public LibroConsigliato(String titolo, int volte) {
        this.titolo = titolo;
        this.volte = volte;
    }

    /**
     * Restituisce i nomi degli utenti che hanno suggerito questo libro.
     * @return Una stringa contenente i suggeritori del libro.
     */
    public String getSuggeritori() { return suggeritori; }

    /**
     * Imposta i nomi degli utenti che hanno suggerito questo libro.
     * @param s La stringa contenente l'elenco dei nuovi suggeritori.
     */
    public void setSuggeritori(String s) { this.suggeritori = s; }

    /**
     * Restituisce il titolo del libro consigliato.
     * @return String titolo.
     */
    public String getTitolo() { return titolo; }

    /**
     * Restituisce il numero di raccomandazioni ricevute.
     * @return int conteggio.
     */
    public int getVolte() { return volte; }
}