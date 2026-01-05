package it.bookrecommender.model;

import java.io.Serializable;

/**
 * Modello lato client per una valutazione dettagliata.
 * Include i punteggi numerici per diverse categorie e i relativi commenti testuali.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Valutazione implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * Costruttore predefinito.
     * Necessario per i framework di serializzazione (come Gson) 
     * e per istanziare oggetti vuoti da popolare tramite setter.
     */
    public Valutazione() {}

    // --- METODI GETTER ---

    /**
     * Restituisce il punteggio assegnato allo stile.
     * @return stile Il punteggio numerico assegnato allo stile del libro.
     */
    public int getStile() { return stile; }

    /**
     * Restituisce il punteggio assegnato al contenuto.
     * @return contenuto Il punteggio numerico assegnato al valore del contenuto.
     */
    public int getContenuto() { return contenuto; }

    /**
     * Restituisce il punteggio assegnato alla gradevolezza.
     * @return gradevolezza Il punteggio relativo alla piacevolezza della lettura.
     */
    public int getGradevolezza() { return gradevolezza; }

    /**
     * Restituisce il punteggio assegnato all'originalità.
     * @return originalita Il punteggio assegnato all'originalità dell'opera.
     */
    public int getOriginalita() { return originalita; }

    /**
     * Restituisce il punteggio assegnato all'edizione.
     * @return edizione Il punteggio relativo alla qualità dell'edizione/formato.
     */
    public int getEdizione() { return edizione; }

    /**
     * Restituisce il voto finale complessivo.
     * @return votoFinale La media calcolata o il voto complessivo della valutazione.
     */
    public int getVotoFinale() { return votoFinale; }

    /**
     * Restituisce il commento specifico per lo stile.
     * @return commentoStile Il testo descrittivo relativo allo stile.
     */
    public String getCommentoStile() { return commentoStile; }

    /**
     * Restituisce il commento specifico per il contenuto.
     * @return commentoContenuto Il testo descrittivo relativo al contenuto.
     */
    public String getCommentoContenuto() { return commentoContenuto; }

    /**
     * Restituisce il commento specifico sulla gradevolezza.
     * @return commentoGradevolezza Il testo descrittivo sulla piacevolezza della lettura.
     */
    public String getCommentoGradevolezza() { return commentoGradevolezza; }

    /**
     * Restituisce il commento specifico sull'originalità.
     * @return commentoOriginalita Il testo descrittivo sull'originalità dell'opera.
     */
    public String getCommentoOriginalita() { return commentoOriginalita; }

    /**
     * Restituisce il commento specifico sull'edizione.
     * @return commentoEdizione Il testo descrittivo sulla qualità editoriale.
     */
    public String getCommentoEdizione() { return commentoEdizione; }

    /**
     * Restituisce il commento generale sulla valutazione.
     * @return commentoGenerale Il testo della recensione globale.
     */
    public String getCommentoGenerale() { return commentoGenerale; }

    /**
     * Restituisce il nome dell'utente autore della valutazione.
     * @return utenteNome Il nome dell'utente da visualizzare.
     */
    public String getUtenteNome() { return utenteNome; }


    // --- METODI SETTER ---

    /**
     * Imposta il punteggio per lo stile.
     * @param stile Il punteggio numerico (1-5) da assegnare allo stile.
     */
    public void setStile(int stile) { this.stile = stile; }

    /**
     * Imposta il punteggio per il contenuto.
     * @param contenuto Il punteggio numerico (1-5) relativo al contenuto.
     */
    public void setContenuto(int contenuto) { this.contenuto = contenuto; }

    /**
     * Imposta il punteggio per la gradevolezza.
     * @param gradevolezza Il punteggio numerico (1-5) sulla piacevolezza.
     */
    public void setGradevolezza(int gradevolezza) { this.gradevolezza = gradevolezza; }

    /**
     * Imposta il punteggio per l'originalità.
     * @param originalita Il punteggio numerico (1-5) sull'originalità.
     */
    public void setOriginalita(int originalita) { this.originalita = originalita; }

    /**
     * Imposta il punteggio per l'edizione.
     * @param edizione Il punteggio numerico (1-5) sulla qualità dell'edizione.
     */
    public void setEdizione(int edizione) { this.edizione = edizione; }

    /**
     * Imposta il valore del voto finale complessivo.
     * @param votoFinale Il punteggio medio o finale della valutazione.
     */
    public void setVotoFinale(int votoFinale) { this.votoFinale = votoFinale; }

    /**
     * Imposta la nota testuale specifica per lo stile.
     * @param s Il testo descrittivo riguardante lo stile.
     */
    public void setCommentoStile(String s) { this.commentoStile = s; }

    /**
     * Imposta la nota testuale specifica per il contenuto.
     * @param s Il testo descrittivo riguardante il contenuto.
     */
    public void setCommentoContenuto(String s) { this.commentoContenuto = s; }

    /**
     * Imposta la nota testuale specifica per la gradevolezza.
     * @param s Il testo descrittivo riguardante l'esperienza di lettura.
     */
    public void setCommentoGradevolezza(String s) { this.commentoGradevolezza = s; }

    /**
     * Imposta la nota testuale specifica per l'originalità.
     * @param s Il testo descrittivo riguardante l'originalità dell'opera.
     */
    public void setCommentoOriginalita(String s) { this.commentoOriginalita = s; }

    /**
     * Imposta la nota testuale specifica per la qualità editoriale.
     * @param s Il testo descrittivo riguardante l'edizione.
     */
    public void setCommentoEdizione(String s) { this.commentoEdizione = s; }

    /**
     * Imposta il commento riassuntivo e globale dell'utente.
     * @param s Il testo della recensione generale.
     */
    public void setCommentoGenerale(String s) { this.commentoGenerale = s; }

    /**
     * Imposta il nome dell'autore della valutazione.
     * @param n Il nome visualizzato dell'utente.
     */
    public void setUtenteNome(String n) { this.utenteNome = n; }
}