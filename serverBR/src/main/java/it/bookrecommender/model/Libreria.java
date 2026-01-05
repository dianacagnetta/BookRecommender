package it.bookrecommender.model;

/**
 * Rappresenta la collezione personale di un utente.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Libreria {
    private int id;
    private int utenteId;
    private String nome;

    /** Costruttore di default per la serializzazione Gson. */
    public Libreria() {}



    /**
     * Crea un'istanza di Libreria con tutti i parametri identificativi.
     * @param id       L'identificativo univoco (ID) della libreria nel database.
     * @param utenteId L'ID dell'utente proprietario della libreria.
     * @param nome     Il nome assegnato alla libreria.
     */
    public Libreria(int id, int utenteId, String nome) {
        this.id = id;
        this.utenteId = utenteId;
        this.nome = nome;
    }

   
    /**
     * Restituisce l'ID della libreria.
     * @return L'identificativo univoco (ID) dell'istanza nel database.
     */
    public int getId() { return id; }

    /**
     * Imposta l'ID della libreria.
     * @param id L'identificativo univoco da assegnare a questa istanza.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce l'ID dell'utente proprietario.
     * @return L'ID dell'utente proprietario di questo elemento (Foreign Key).
     */
    public int getUtenteId() { return utenteId; }

    /**
     * Associa la libreria a un utente specifico.
     * @param utenteId L'ID dell'utente a cui associare questo elemento.
     */
    public void setUtenteId(int utenteId) { this.utenteId = utenteId; }

    /**
     * Restituisce il nome della libreria.
     * @return Il nome descrittivo assegnato (es. il titolo della libreria).
     */
    public String getNome() { return nome; }

    /**
     * Imposta il nome della libreria.
     * @param nome Il valore testuale da assegnare come nome.
     */
    public void setNome(String nome) { this.nome = nome; }
}