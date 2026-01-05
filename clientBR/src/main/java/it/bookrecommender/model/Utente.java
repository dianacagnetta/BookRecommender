package it.bookrecommender.model;
import java.io.Serializable;
/**
 * Modello dati che rappresenta un utente registrato nel sistema.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */




public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco dell'utente nel database (Primary Key). */
    private int id;

    /** Nome di battesimo dell'utente. */
    private String nome;

    /** Cognome dell'utente. */
    private String cognome;

    /** Codice Fiscale dell'utente (identificatore unico per la normativa italiana). */
    private String codiceFiscale;

    /** Indirizzo di posta elettronica dell'utente. */
    private String email;

    /** Nome utente (ID alfanumerico) utilizzato per l'autenticazione. */
    private String userid;

    /** La password dell'utente memorizzata in formato hash per motivi di sicurezza. */
    private String passwordHash;

    /**
     * Costruttore completo per l'oggetto Utente.
     * @param id Identificativo univoco.
     * @param nome Nome dell'utente.
     * @param cognome Cognome dell'utente.
     * @param codiceFiscale Codice Fiscale (16 caratteri).
     * @param email Indirizzo email.
     * @param userid Nome utente per il login.
     * @param passwordHash Password (solitamente hashata).
     */
    public Utente(int id, String nome, String cognome, String codiceFiscale,
                  String email, String userid, String passwordHash) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userid = userid;
        this.passwordHash = passwordHash;
    }

    
    /**
     * Restituisce l'identificativo univoco dell'utente.
     * @return id L'identificativo univoco dell'utente registrato nel sistema.
     */
    public int getId() { return id; }

    /**
     * Restituisce il nome dell'utente.
     * @return nome Il nome di battesimo dell'utente.
     */
    public String getNome() { return nome; }

    /**
     * Restituisce il cognome dell'utente.
     * @return cognome Il cognome dell'utente.
     */
    public String getCognome() { return cognome; }

    /**
     * Restituisce il codice fiscale dell'utente.
     * @return codiceFiscale Il codice fiscale dell'utente (identificativo alfanumerico).
     */
    public String getCodiceFiscale() { return codiceFiscale; }

    /**
     * Restituisce l'indirizzo email dell'utente.
     * @return L'indirizzo email di contatto dell'utente.
     */
    public String getEmail() { return email; }

    /**
     * Restituisce lo username scelto per l'autenticazione.
     * @return userId Lo username (ID alfanumerico) scelto dall'utente per effettuare l'accesso.
     */
    public String getUserid() { return userid; }
    /**
     * Restituisce l'impronta della password.
     * Nota: Per motivi di sicurezza, viene restituito l'hash e non la password in chiaro.
     * @return passwordhash L'hash della password archiviato nel database.
     */
    public String getPasswordHash() { return passwordHash; }
}