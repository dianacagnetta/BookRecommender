package it.bookrecommender.model;

/**
 * Modello dati che rappresenta una valutazione dettagliata di un libro.
 * Include i punteggi numerici per diverse categorie e i relativi commenti testuali.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Valutazione {

    private int stile;
    private int contenuto;
    private int gradevolezza;
    private int originalita;
    private int edizione;
    private int votoFinale;

    private String commentoStile;
    private String commentoContenuto;
    private String commentoGradevolezza;
    private String commentoOriginalita;
    private String commentoEdizione;
    private String commentoGenerale;
    private String utenteNome;

    /**
     * Costruttore vuoto necessario per i processi di serializzazione e deserializzazione JSON.
     */
    public Valutazione() {}

    /**
     * Costruttore parametrizzato per inizializzare una valutazione completa.
     * @param stile Voto per lo stile (1-5).
     * @param contenuto Voto per il contenuto (1-5).
     * @param gradevolezza Voto per la gradevolezza (1-5).
     * @param originalita Voto per l'originalità (1-5).
     * @param edizione Voto per l'edizione (1-5).
     * @param votoFinale Media calcolata dei voti assegnati.
     * @param commentoStile Nota testuale sullo stile.
     * @param commentoContenuto Nota testuale sul contenuto.
     * @param commentoGradevolezza Nota testuale sulla gradevolezza.
     * @param commentoOriginalita Nota testuale sull'originalità.
     * @param commentoEdizione Nota testuale sull'edizione.
     * @param commentoGenerale Commento riassuntivo finale.
     */
    public Valutazione(
            int stile, int contenuto, int gradevolezza, int originalita, int edizione, int votoFinale,
            String commentoStile, String commentoContenuto, String commentoGradevolezza,
            String commentoOriginalita, String commentoEdizione, String commentoGenerale
    ) {
        this.stile = stile;
        this.contenuto = contenuto;
        this.gradevolezza = gradevolezza;
        this.originalita = originalita;
        this.edizione = edizione;
        this.votoFinale = votoFinale;
        this.commentoStile = commentoStile;
        this.commentoContenuto = commentoContenuto;
        this.commentoGradevolezza = commentoGradevolezza;
        this.commentoOriginalita = commentoOriginalita;
        this.commentoEdizione = commentoEdizione;
        this.commentoGenerale = commentoGenerale;
        this.utenteNome = null;
    }

    /**
     * Restituisce il nome dell'utente autore della valutazione.
     * @return Il nome dell'utente autore della valutazione.
     */
    public String getUtenteNome() { return utenteNome; }

    /**
     * Imposta il nome dell'utente autore della valutazione.
     * @param n Il nome dell'utente da visualizzare come autore della recensione.
     */
    public void setUtenteNome(String n) { this.utenteNome = n; }
    /**
     * Restituisce il punteggio dello stile.
     * @return Il punteggio assegnato alla qualità della scrittura e dello stile.
     */
    public int getStile() { return stile; }

    /**
     * Restituisce il punteggio del contenuto.
     * @return Il punteggio relativo alla validità e profondità dei contenuti.
     */
    public int getContenuto() { return contenuto; }

    /**
     * Restituisce il punteggio della gradevolezza.
     * @return Il punteggio sulla piacevolezza complessiva della lettura.
     */
    public int getGradevolezza() { return gradevolezza; }

    /**
     * Restituisce il punteggio dell'originalità.
     * @return Il punteggio relativo all'originalità dell'opera o del tema trattato.
     */
    public int getOriginalita() { return originalita; }

    /**
     * Restituisce il punteggio dell'edizione.
     * @return Il punteggio sulla qualità dell'edizione fisica o del formato digitale.
     */
    public int getEdizione() { return edizione; }

    /**
     * Restituisce il voto finale complessivo.
     * @return La media calcolata o il voto finale riassuntivo.
     */
    public int getVotoFinale() { return votoFinale; }

    /**
     * Restituisce il commento specifico per lo stile.
     * @return Il commento specifico relativo allo stile.
     */
    public String getCommentoStile() { return commentoStile; }

    /**
     * Restituisce il commento specifico per il contenuto.
     * @return Il commento specifico relativo al contenuto.
     */
    public String getCommentoContenuto() { return commentoContenuto; }

    /**
     * Restituisce il commento specifico sulla gradevolezza.
     * @return Il commento specifico sulla gradevolezza.
     */
    public String getCommentoGradevolezza() { return commentoGradevolezza; }

    /**
     * Restituisce il commento specifico sull'originalità.
     * @return Il commento specifico sull'originalità.
     */
    public String getCommentoOriginalita() { return commentoOriginalita; }

    /**
     * Restituisce il commento specifico sull'edizione.
     * @return Il commento specifico sull'edizione.
     */
    public String getCommentoEdizione() { return commentoEdizione; }

    /**
     * Restituisce il commento generale della valutazione.
     * @return La recensione testuale completa o di sintesi.
     */
    public String getCommentoGenerale() { return commentoGenerale; }

    // --- Metodi Setter (Voti Numerici) ---

    /**
     * Imposta il punteggio per lo stile.
     * @param s Punteggio stile (tipicamente da 1 a 5).
     */
    public void setStile(int s) { stile = s; }

    /**
     * Imposta il punteggio per il contenuto.
     * @param c Punteggio contenuto (tipicamente da 1 a 5).
     */
    public void setContenuto(int c) { contenuto = c; }

    /**
     * Imposta il punteggio per la gradevolezza.
     * @param g Punteggio gradevolezza (tipicamente da 1 a 5).
     */
    public void setGradevolezza(int g) { gradevolezza = g; }

    /**
     * Imposta il punteggio per l'originalità.
     * @param o Punteggio originalità (tipicamente da 1 a 5).
     */
    public void setOriginalita(int o) { originalita = o; }

    /**
     * Imposta il punteggio per l'edizione.
     * @param e Punteggio edizione (tipicamente da 1 a 5).
     */
    public void setEdizione(int e) { edizione = e; }

    /**
     * Imposta il valore del voto finale.
     * @param v Voto finale calcolato o assegnato.
     */
    public void setVotoFinale(int v) { votoFinale = v; }

    // --- Metodi Setter (Commenti Testuali) ---

    /**
     * Imposta il commento testuale per lo stile.
     * @param s Testo descrittivo per lo stile.
     */
    public void setCommentoStile(String s) { commentoStile = s; }

    /**
     * Imposta il commento testuale per il contenuto.
     * @param s Testo descrittivo per il contenuto.
     */
    public void setCommentoContenuto(String s) { commentoContenuto = s; }

    /**
     * Imposta il commento testuale per la gradevolezza.
     * @param s Testo descrittivo per la gradevolezza.
     */
    public void setCommentoGradevolezza(String s) { commentoGradevolezza = s; }

    /**
     * Imposta il commento testuale per l'originalità.
     * @param s Testo descrittivo per l'originalità.
     */
    public void setCommentoOriginalita(String s) { commentoOriginalita = s; }

    /**
     * Imposta il commento testuale per l'edizione.
     * @param s Testo descrittivo per l'edizione.
     */
    public void setCommentoEdizione(String s) { commentoEdizione = s; }

    /**
     * Imposta il commento testuale generale.
     * @param s Testo della recensione generale.
     */
    public void setCommentoGenerale(String s) { commentoGenerale = s; }
}