package it.bookrecommender.model;

import java.io.Serializable;

/**
 * Entità Libro.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;


    /** Identificativo univoco del libro nel database (Primary Key). */
    private int libroId;

    /** Titolo dell'opera. */
    private String titolo;

    /** Nome dell'autore o degli autori del libro. */
    private String autore;

    /** Anno in cui il libro è stato pubblicato. */
    private int annoPubblicazione;

    /** Casa editrice che ha pubblicato il libro. */
    private String editore;

    /** Genere letterario o categoria di appartenenza (es. "Romanzo", "Thriller"). */
    private String categoria;

/**
     * Costruttore per la creazione di un oggetto Libro completo.
     * @param libroId Identificativo univoco del libro.
     * @param titolo Titolo dell'opera.
     * @param autore Nome dell'autore.
     * @param annoPubblicazione Anno di uscita.
     * @param editore Casa editrice.
     * @param categoria Genere letterario.
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
     * Metodo toString: sovrascritto per fornire una rappresentazione leggibile del libro
     * all'interno di ListView e altri componenti grafici.
     * @return Stringa formattata come "Titolo (Autore)".
     */
    @Override
    public String toString() {
        return titolo + " (" + autore + ")";
    }
    /**
     * Restituisce l'identificativo univoco del libro.
     * @return L'identificativo del libro.
     */
    public int getLibroId() { return libroId; }

    /**
     * Restituisce il titolo del libro.
     * @return Il titolo del libro.
     */
    public String getTitolo() { return titolo; }

    /**
     * Restituisce l'autore del libro.
     * @return L'autore del libro.
     */
    public String getAutore() { return autore; }

    /**
     * Restituisce l'anno di pubblicazione del libro.
     * @return L'anno di pubblicazione.
     */
    public int getAnnoPubblicazione() { return annoPubblicazione; }

    /**
     * Restituisce l'editore del libro.
     * @return L'editore del libro.
     */
    public String getEditore() { return editore; }

    /**
     * Restituisce la categoria o il genere del libro.
     * @return La categoria del libro.
     */
    public String getCategoria() { return categoria; }
}