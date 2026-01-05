package it.bookrecommender.model;

import java.io.Serializable;

/**
 * Entità Libreria.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Libreria implements Serializable {
	/** Identificatore univoco per la serializzazione della classe. */
    private static final long serialVersionUID = 1L;
    /** Identificativo univoco della libreria nel database (Primary Key). */
    private int id;
    /** Identificativo dell'utente proprietario della libreria (Foreign Key). */
    private int utenteId;
    /** Nome assegnato alla libreria dall'utente. */
    private String nome;

    /**
     * Costruttore vuoto necessario per i processi di serializzazione/deserializzazione.
     */
    public Libreria() {}

    /**
     * Costruttore parametrizzato per inizializzare una libreria esistente con ID e proprietario.
     * @param id       L'identificativo univoco della libreria nel database.
     * @param utenteId L'ID dell'utente proprietario che ha creato la libreria.
     * @param nome     Il nome descrittivo assegnato alla libreria (es. "Preferiti").
     */
    public Libreria(int id, int utenteId, String nome) {
        this.id = id;
        this.utenteId = utenteId;
        this.nome = nome;
    }
/**
     * Restituisce una rappresentazione testuale dell'oggetto.
     * Viene utilizzato principalmente dai componenti della UI (come le ComboBox o le List)
     * per visualizzare il nome dell'elemento.
     *
     * @return Il nome dell'oggetto se presente, altrimenti una stringa vuota.
     */
    @Override
    public String toString() {
        return nome == null ? "" : nome;
    }
    /**
     * Restituisce l'identificativo della libreria.
     * @return L'identificativo univoco (ID) dell'istanza presente nel database.
     */
    public int getId() { return id; }
    /**
     * Imposta il nome della libreria.
     * @return nome Il nuovo nome da assegnare.
     */
    public String getNome() { return nome; }
    // Getters e Setters standard per l'accesso ai campi
}