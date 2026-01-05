package it.bookrecommender.net.server;

import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import it.bookrecommender.db.DBConnection;

/**
 * Punto di ingresso principale per l'applicazione Server.
 * Si occupa di inizializzare la connessione al database tramite input da console
 * e di avviare il servizio di ascolto socket per i client.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class ServerMain {

	 /**
	  * Costruttore predefinito della classe.
	  */
	public ServerMain() {}
    /**
     * Metodo main: configura i parametri del DB, verifica la connessione e avvia il server.
     * Implementa un ciclo infinito per accettare connessioni multiple, delegandole a ClientHandler.
     * @param args Argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        int port = 7777;
        Scanner sc = new Scanner(System.in);

try {
            // 1. Richiesta Host (Specifico per PostgreSQL su Render)
            String hostInput = JOptionPane.showInputDialog(null, 
                "Inserisci host del DB (es. dpg-xxx.render.com):", 
                "Configurazione PostgreSQL - Host", JOptionPane.QUESTION_MESSAGE);
            
            // Se preme X o Annulla, chiudiamo subito
            if (hostInput == null) {
                System.out.println("❌ Operazione annullata (Host).");
                System.exit(0);
            }

            // 2. Richiesta Username
            String user = JOptionPane.showInputDialog(null, 
                "Inserisci username database:", 
                "Configurazione PostgreSQL - User", JOptionPane.QUESTION_MESSAGE);
            
            if (user == null) {
                System.out.println("❌ Operazione annullata (User).");
                System.exit(0);
            }

            // 3. Richiesta Password
            JPasswordField pf = new JPasswordField();
            int result = JOptionPane.showConfirmDialog(null, pf, 
                "Inserisci password database:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                System.out.println("❌ Operazione annullata (Password).");
                System.exit(0);
            }

            // Costruiamo la stringa per PostgreSQL
            String password = new String(pf.getPassword());
            String host = "jdbc:postgresql://" + hostInput;

            System.out.println("⏳ Connessione a PostgreSQL in corso...");
            
            // Inizializzazione e Test
            DBConnection.init(host, user, password);
            DBConnection.getConnection().close(); 
            
            JOptionPane.showMessageDialog(null, "✅ Connessione a PostgreSQL riuscita!", 
                "Successo", JOptionPane.INFORMATION_MESSAGE);

            // 4. Avvio server
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("✅ Server in ascolto sulla porta " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("🔗 Nuova connessione da " + clientSocket.getInetAddress());
                    new ClientHandler(clientSocket).start();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Errore fatale: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Impossibile avviare il server:\n" + e.getMessage(), 
                "Errore di Connessione", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}