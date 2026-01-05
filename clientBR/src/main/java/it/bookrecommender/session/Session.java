package it.bookrecommender.session;
import it.bookrecommender.model.*;

/**
 * Classe per la gestione della sessione dell'utente loggato nel client.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Session {
    
	/**
	 * Costruttore predefinito della classe.
	 */
	public Session() {}
    /** Riferimento statico all'utente attualmente autenticato. */
    public static Utente utenteLoggato = null;

    /**
     * Verifica se un utente è attualmente loggato.
     * @return true se loggato, false altrimenti.
     */
    public static boolean isLogged() {
        return utenteLoggato != null;
    }

    /**
     * Registra l'utente nella sessione corrente dopo un login di successo.
     * @param u L'oggetto Utente autenticato.
     */
    public static void login(Utente u) {
        utenteLoggato = u;
    }

    /**
     * Rimuove l'utente dalla sessione (Logout).
     */
    public static void logout() {
        utenteLoggato = null;
    }
}