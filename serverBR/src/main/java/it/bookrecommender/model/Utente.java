package it.bookrecommender.model;
/**
 * Rappresenta l'entità Utente all'interno del sistema.
 * <p>
 * Questa classe modella i dati anagrafici e le credenziali di accesso di un utente registrato.
 * Viene utilizzata per la gestione delle sessioni, la registrazione e l'associazione
 * delle librerie e delle valutazioni ai singoli profili.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Utente {

    private int id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String userid;
    private String passwordHash;
/**
     * Costruttore completo per inizializzare un oggetto Utente con tutti i suoi attributi.
     * Tipicamente utilizzato durante il caricamento dei dati dal database o dopo una registrazione.
     *
     * @param id             L'identificativo univoco numerico.
     * @param nome           Il nome di battesimo dell'utente.
     * @param cognome        Il cognome dell'utente.
     * @param codiceFiscale  Il codice fiscale (identificatore alfanumerico univoco).
     * @param email          L'indirizzo di posta elettronica.
     * @param userid         Lo username scelto per l'autenticazione.
     * @param passwordHash   L'hash della password (mai memorizzata in chiaro per sicurezza).
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
     * @return L'identificativo univoco dell'utente (ID).
     */
    public int getId() { return id; }

    /**
     * Restituisce il nome dell'utente.
     * @return Il nome di battesimo.
     */
    public String getNome() { return nome; }

    /**
     * Restituisce il cognome dell'utente.
     * @return Il cognome.
     */
    public String getCognome() { return cognome; }

    /**
     * Restituisce il codice fiscale dell'utente.
     * @return Il codice fiscale (identificativo alfanumerico).
     */
    public String getCodiceFiscale() { return codiceFiscale; }

    /**
     * Restituisce l'indirizzo email dell'utente.
     * @return L'indirizzo email di contatto.
     */
    public String getEmail() { return email; }

    /**
     * Restituisce lo username dell'utente.
     * @return Lo username utilizzato per l'autenticazione.
     */
    public String getUserid() { return userid; }
    
    /** * Restituisce l'hash della password.
     * <b>Nota:</b> Per ragioni di sicurezza, non esporre mai questo valore nell'interfaccia utente.
     * @return La stringa contenente l'hash della password. 
     */
    public String getPasswordHash() { return passwordHash; }
}
