package it.bookrecommender.model;

/**
 * Classe modello per l'entità Libro.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Libro {

    private int libroId;
    private String titolo;
    private String autore;
    private int annoPubblicazione;
    private String editore;
    private String categoria;

/**
     * Costruttore completo per la creazione di un oggetto Libro.
     * Utilizzato solitamente per istanziare oggetti i cui dati provengono 
     * direttamente da una query del database.
     *
     * @param libroId           L'ID univoco del libro.
     * @param titolo            Il titolo dell'opera.
     * @param autore            L'autore o gli autori del volume.
     * @param annoPubblicazione L'anno di rilascio del libro.
     * @param editore           La casa editrice.
     * @param categoria         Il genere letterario di appartenenza.
     */
    public Libro(int libroId, String titolo, String autore,
                 int annoPubblicazione, String editore, String categoria) {
        this.libroId = libroId;
        this.titolo = titolo;
        this.autore = autore;
        this.annoPubblicazione = annoPubblicazione;
        this.editore = editore;
        this.categoria = categoria;
    }

 
    /**
     * Restituisce l'ID del libro.
     * @return L'identificativo univoco del libro nel database (Primary Key).
     */
    public int getLibroId() { return libroId; }

    /**
     * Restituisce il titolo del libro.
     * @return Il titolo completo dell'opera.
     */
    public String getTitolo() { return titolo; }

    /**
     * Restituisce l'autore del libro.
     * @return Il nome dell'autore o degli autori del libro.
     */
    public String getAutore() { return autore; }

    /**
     * Restituisce l'anno di pubblicazione.
     * @return L'anno in cui il libro è stato pubblicato.
     */
    public int getAnnoPubblicazione() { return annoPubblicazione; }

    /**
     * Restituisce l'editore del libro.
     * @return La casa editrice che ha pubblicato l'opera.
     */
    public String getEditore() { return editore; }

    /**
     * Restituisce la categoria del libro.
     * @return Il genere o la categoria tematica del libro (es. Narrativa, Thriller, Saggistica).
     */
    public String getCategoria() { return categoria; }

    /**
     * Fornisce una rappresentazione testuale del libro (Titolo e Autore).
     */
    @Override
    public String toString() {
        return titolo + " (" + autore + ")";
    }
}