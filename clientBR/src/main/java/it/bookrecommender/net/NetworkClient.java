package it.bookrecommender.net;

import it.bookrecommender.dto.Request;
import it.bookrecommender.dto.Response;
import com.google.gson.Gson;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

/**
 * Gestore della comunicazione di rete lato client.
 * Questa classe agisce come un ponte tra l'interfaccia utente (Controller) e il Server.
 * Utilizza il protocollo TCP per una connessione affidabile e JSON per lo scambio dati.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */

public class NetworkClient {

    /** Istanza unica della classe (Pattern Singleton). */
    private static NetworkClient instance;
    
    /** Oggetto GSON per trasformare gli oggetti Java in stringhe JSON e viceversa. */
    private final Gson gson = new Gson();
    
    /** Il socket per la connessione fisica con il server. */
    private Socket socket;
    
    /** Stream per leggere i messaggi in arrivo dal server. */
    private BufferedReader in;
    
    /** Stream per inviare i messaggi al server. */
    private PrintWriter out;

    /**
     * Costruttore privato: impedisce la creazione di più istanze della classe.
     * La connessione viene gestita centralmente tramite il metodo getInstance().
     */
    private NetworkClient() {}

    /**
     * Restituisce l'istanza unica di NetworkClient. 
     * Se è la prima volta che viene chiamata, crea l'oggetto e stabilisce la connessione.
     * @return L'istanza singleton di NetworkClient.
     * @throws RuntimeException se il server non è raggiungibile all'avvio.
     */
    public static NetworkClient getInstance() {
        if (instance == null) {
            instance = new NetworkClient();
            try {
                instance.connect();
            } catch (Exception e) {
                // Se la connessione fallisce, l'app non può funzionare correttamente.
                throw new RuntimeException("Impossibile connettersi al server: " + e.getMessage());
            }
        }
        return instance;
    }

    /**
     * Apre il socket verso l'indirizzo IP del server (localhost) sulla porta 7777.
     * Inizializza i canali di input (in) e output (out).
     * @throws Exception In caso di errore di rete o se il server è spento.
     */
    public void connect() throws Exception {
        // Connessione all'indirizzo locale (127.0.0.1) sulla porta concordata col server.
        socket = new Socket("127.0.0.1", 7777);
        
        // BufferedReader legge il testo ricevuto dal socket linea per linea.
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // PrintWriter invia il testo al server. 'true' abilita l'auto-flush del buffer.
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Esegue lo scambio di dati con il server: invia una richiesta e attende una risposta.
     * @param req L'oggetto Request che contiene l'azione (es. LOGIN, REGISTER) e i dati.
     * @return L'oggetto Response inviato dal server, ricostruito dal formato JSON.
     * @throws Exception In caso di crash del socket o errore di lettura/scrittura.
     */
    public Response send(Request req) throws Exception {
        // 1. Converte l'oggetto Request in una stringa JSON e la invia.
        out.println(gson.toJson(req));
        
        // 2. Rimane in attesa di una linea di testo dal server (il JSON della risposta).
        String jsonResponse = in.readLine();
        
        // 3. Converte la stringa JSON ricevuta in un oggetto Response e lo restituisce al Controller.
        return gson.fromJson(jsonResponse, Response.class);
    }
}