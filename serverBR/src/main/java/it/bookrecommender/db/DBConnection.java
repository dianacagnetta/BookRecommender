package it.bookrecommender.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al database PostgreSQL remoto su Render.
 * Le credenziali vengono inizializzate dal ServerMain all'avvio.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class DBConnection {

	 /**
	  * Costruttore predefinito della classe.
	  */
	public DBConnection() {}
	
	/** 
	 * L'indirizzo del server database (URL JDBC).
     * Include il protocollo, l'host, la porta e il nome del database.
     */
    private static String host;

    /** 
     * Lo username utilizzato per l'autenticazione al sistema database. 
     */
    private static String user;

    /** * La password associata all'utente per l'accesso al database.
     * Deve essere gestita con attenzione per motivi di sicurezza.
     */
    private static String password;

    /**
     * Inizializza le credenziali di connessione al database.
     * Deve essere richiamato all'avvio del server prima di qualsiasi query.
     * @param h Host del database.
     * @param u Nome utente.
     * @param p Password di accesso.
     */
    public static void init(String h, String u, String p) {
host = (h != null) ? h.trim() : null;
        user = (u != null) ? u.trim() : null;
        password = (p != null) ? p.trim() : null;
    }

    /**
     * Crea e restituisce una nuova connessione attiva al database PostgreSQL.
     * Implementa la connessione SSL obbligatoria per i servizi cloud come Render.
     * @return Connection Oggetto connessione JDBC.
     * @throws SQLException Se le credenziali mancano o la connessione fallisce.
     */
public static Connection getConnection() throws SQLException {
if (host == null || user == null || password == null) {
            throw new SQLException("Credenziali non inizializzate correttamente.");
        }

        // 1. Pulizia dell'host (rimuove jdbc:postgresql:// se già presente per errore)
        String cleanHost = host.replace("jdbc:postgresql://", "");

        // 2. Costruzione URL corretta: 
        // jdbc:postgresql://HOST:PORTA/NOMEDB?sslmode=require
        // Nota lo slash '/' prima del punto interrogativo
        String url = "jdbc:postgresql://" + cleanHost;
        
        if (!cleanHost.contains(":5432")) {
            url += ":5432";
        }
        
        if (!cleanHost.contains("/bookrecommender_qt9c")) {
            url += "/bookrecommender_qt9c";
        }

        url += "?sslmode=require";

        // Rimuove eventuali spazi rimasti all'interno della stringa finale
        url = url.replace(" ", "");

        System.out.println("🔗 Tentativo di connessione a: " + url);
        
        return DriverManager.getConnection(url, user, password);
    }
}