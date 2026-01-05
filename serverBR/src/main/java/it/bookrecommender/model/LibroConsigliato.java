package it.bookrecommender.model;

/**
 * Rappresenta l'aggregazione di un consiglio di lettura.
 * Memorizza il titolo del libro e quante volte è stato raccomandato.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class LibroConsigliato {

    private String titolo;
    private int volte;
    private String suggeritori;

    /**
     * Costruttore per creare un'istanza di consiglio aggregato.
     * @param titolo Il titolo del libro consigliato.
     * @param volte Il numero totale di raccomandazioni ricevute.
     */
    public LibroConsigliato(String titolo, int volte) {
        this.titolo = titolo;
        this.volte = volte;
    }
/**
     * Restituisce l'elenco dei soggetti che hanno suggerito questo libro.
     * Utile per visualizzare rapidamente i riferimenti o i nomi dei suggeritori 
     * in una lista o in un'area di dettaglio.
     * @return Una stringa contenente i nomi o i riferimenti dei suggeritori.
     */
    public String getSuggeritori() { return suggeritori; }
    /**
     * Imposta o aggiorna l'elenco dei suggeritori per questo libro.
     * @param s La stringa formattata contenente i nuovi riferimenti dei suggeritori.
     */
    public void setSuggeritori(String s) { this.suggeritori = s; }

    /** Restituisce il titolo del libro.
     * @return titolo Restituisce il titolo del libro.
     *  */
    public String getTitolo() { return titolo; }
    
    /** Restituisce il conteggio delle raccomandazioni. 
     * @return Il numero di raccomandazioni. 
     */
    public int getVolte() { return volte; }
}