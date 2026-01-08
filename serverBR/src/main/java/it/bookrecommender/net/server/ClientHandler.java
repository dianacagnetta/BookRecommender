package it.bookrecommender.net.server;


import com.google.gson.Gson;
import it.bookrecommender.dao.interfaces.*;
import it.bookrecommender.dao.jdbc.*;
import it.bookrecommender.dto.*;
import it.bookrecommender.model.*;
import java.io.*;
import java.net.Socket;

/**
 * Gestore dei thread lato server. Riceve JSON, usa i DAO e risponde al client.
 */
public class ClientHandler extends Thread {
    private final Socket socket;
    private final Gson gson = new Gson();

    // Inizializzazione dei DAO tramite le interfacce
    private final IUtenteDAO utenteDao = new UtenteDAOImpl();
    private final ILibroDAO libroDao = new LibroDAOImpl();
    private final ILibrerieDAO librerieDao = new LibrerieDAOImpl();
    private final IValutazioniDAO valutazioniDao = new ValutazioniDAOImpl();
    private final IConsigliDAO consigliDao = new ConsigliDAOImpl();

    /**
     * Costruttore della classe ClientHandler.
     * Inizializza un nuovo gestore per la connessione di un client specifico, 
     * ricevendo la socket generata dal ServerSocket.
     * @param socket L'oggetto {@link Socket} che rappresenta la connessione attiva con il client.
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    /**
     * Esegue il ciclo principale di ascolto e gestione delle richieste del client.
     * <p>
     * Il metodo apre gli stream di input/output sulla socket e resta in attesa di messaggi JSON.
     * Per ogni richiesta ricevuta:
     * <ul>
     * <li>Deserializza la stringa JSON in un oggetto {@link Request}.</li>
     * <li>Esegue uno switch sulla proprietà 'action' per determinare l'operazione da compiere.</li>
     * <li>Interroga i vari DAO ({@code utenteDao}, {@code libroDao}, ecc.) per elaborare i dati.</li>
     * <li>Serializza l'oggetto {@link Response} risultante e lo invia al client.</li>
     * </ul>
     * Il ciclo termina quando il client chiude la connessione o si verifica un errore di rete.
     */
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("📩 Ricevuto: " + message);
                Request req = gson.fromJson(message, Request.class); //
                Response res = new Response();

                try {
                    switch (req.action) { //
                        case "GET_LIBRI_LIBRERIA":
    // Recupera la lista dei libri associati a una specifica libreria
    res.libri = librerieDao.getLibriInLibreria(req.libreriaId);
    res.ok = true;
    break;

case "REMOVE_BOOK_FROM_LIBRERIA":
    // Se hai anche la funzione di rimozione
    res.ok = librerieDao.rimuoviLibroDaLibreria(req.libreriaId, req.libroId);
    break;
                        case "LOGIN":
                            Utente u = utenteDao.login(req.userid, req.passwordHash); //
                            res = (u != null) ? new Response(true, "Login OK", u) : new Response(false, "Credenziali errate");
                            break;

  case "ADD_CONSIGLIO":
    System.out.println("🔹 ADD_CONSIGLIO: userid=" + req.userid + 
                       ", libroId=" + req.libroId + 
                       ", suggerito=" + req.suggerito);
    consigliDao.addConsiglio(req.userid, req.libroId, req.suggerito);
    res.ok = true;
    res.message = "Consiglio aggiunto";
    break;

       case "REGISTER":
                            Utente newUser = new Utente(0, req.nome, req.cognome, req.codiceFiscale, req.email, req.userid, req.passwordHash);
                            res.ok = utenteDao.registra(newUser); //
                            res.message = res.ok ? "Registrazione completata" : "Errore registrazione";
                            break;

                        case "GET_ALL_BOOKS":
                            res.libri = BooksCache.getAll(); //
                            if (res.libri == null || res.libri.isEmpty()) {
                                res.libri = libroDao.getAllBooks(); //
                            }
                            res.ok = true;
                            break;

                        case "SEARCH_BOOKS":
                            res.libri = libroDao.searchBooks(req.titolo, req.autore, req.anno); //
                            res.ok = true;
                            break;

                        case "GET_BOOK_DETAILS":
                            res.libro = libroDao.getBookById(req.libroId); //
                            if (res.libro != null) {
                                res.valutazioni = valutazioniDao.getValutazioniLibro(req.libroId); //
                                res.consigli = consigliDao.getConsigli(req.libroId); //
                            }
                            res.ok = (res.libro != null);
                            break;

                        case "GET_LIBRERIE_UTENTE":
                            res.librerie = librerieDao.getLibrerieByUtenteId(req.utenteId); //
                            res.ok = true;
                            break;

                        case "CREATE_LIBRERIA":
                            librerieDao.creaLibreria(req.utenteId, req.nomeLibreria); //
                            res.librerie = librerieDao.getLibrerieByUtenteId(req.utenteId);
                            res.ok = true;
                            break;

                        case "ADD_BOOK_TO_LIBRERIA":
                            res.ok = librerieDao.aggiungiLibroALibreria(req.libreriaId, req.libroId); //
                            break;

                        case "ADD_VALUTAZIONE":
                            res.ok = valutazioniDao.salvaValutazione(req.userid, req.libroId, req.stile, req.commentoStile, 
                                req.contenuto, req.commentoContenuto, req.gradevolezza, req.commentoGradevolezza, 
                                req.originalita, req.commentoOriginalita, req.edizione, req.commentoEdizione, 
                                req.votoFinale, req.commentoGenerale); //
                            break;

                   

                        default:
                            res = new Response(false, "Azione non riconosciuta");
                    }
                } catch (Exception e) {
                    res = new Response(false, "Errore Server: " + e.getMessage());
                }
                out.println(gson.toJson(res)); //
            }
        } catch (Exception e) {
            System.err.println("⚠️ Connessione chiusa: " + e.getMessage());
        }
    }
}