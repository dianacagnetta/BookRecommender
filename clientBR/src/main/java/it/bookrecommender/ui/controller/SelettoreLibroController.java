package it.bookrecommender.ui.controller;

import it.bookrecommender.net.NetworkClient;
import it.bookrecommender.dto.Request;
import it.bookrecommender.dto.Response;
import it.bookrecommender.model.Libro;
import it.bookrecommender.model.Utente;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controller per la selezione di un libro da aggiungere a una libreria specifica.
 * Include un filtro di ricerca dinamico.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class SelettoreLibroController {
	/**
	 * Costruttore predefinito della classe.
	 */
	
	public SelettoreLibroController() {}
    @FXML private javafx.scene.control.TextField txtFiltro;
    @FXML private javafx.scene.control.ListView<Libro> listaLibri;
    
    private int libreriaId;
    private Utente utente;
    private LibreriaDettaglioController caller;

    /**
     * Inizializza il controller e prepara l'interfaccia per la ricerca manuale.
     * @param libreriaId L'identificativo della libreria su cui si sta operando.
     * @param utente     L'oggetto {@link Utente} attualmente loggato nel sistema.
     * @param caller     Riferimento al controller chiamante ({@link LibreriaDettaglioController}) per la gestione dei callback.
     */
    public void setData(int libreriaId, Utente utente, LibreriaDettaglioController caller) {
        this.libreriaId = libreriaId;
        this.utente = utente;
        this.caller = caller;
    }

    /**
     * Esegue la ricerca dei libri in base al filtro inserito nel campo di testo.
     * Viene richiamato quando l'utente clicca il pulsante "Cerca".
     */
    @FXML
    private void cercaLibri() {
        String filtro = txtFiltro.getText().trim();
        caricaLibri(filtro);
    }

    /**
     * Carica i libri dal catalogo, opzionalmente filtrati per titolo.
     * @param filtro La stringa utilizzata per filtrare i titoli dei libri. Se vuota, recupera tutti i libri.
     */
    private void caricaLibri(String filtro) {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = filtro.isEmpty() ? "GET_ALL_BOOKS" : "SEARCH_BOOKS";
            req.titolo = filtro;
            req.autore = "";  // Necessario per SEARCH_BOOKS
            req.anno = null;  // Necessario per SEARCH_BOOKS

            Response res = client.send(req);
            listaLibri.getItems().clear();
            
            if (res.ok && res.libri != null) {
                listaLibri.getItems().addAll(res.libri);
                System.out.println("✅ Caricati " + res.libri.size() + " libri");
            } else {
                System.out.println("⚠️ Nessun libro ricevuto dal server");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                "Errore caricamento libri: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Conferma l'aggiunta del libro selezionato e notifica il controller chiamante.
     */
    @FXML
    private void aggiungiLibro() {
        Libro selezionato = listaLibri.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING,
                "Seleziona un libro dalla lista!").showAndWait();
            return;
        }

        if (libreriaId <= 0) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, 
                "ID libreria non valido. Assicurati di aver salvato la libreria sul server e ricarica la lista.").showAndWait();
            return;
        }

        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "ADD_BOOK_TO_LIBRERIA";
            req.libreriaId = libreriaId;
            req.libroId = selezionato.getLibroId();
            
            System.out.println("🔹 Invio richiesta ADD_BOOK_TO_LIBRERIA libreriaId=" + req.libreriaId + 
                             " libroId=" + req.libroId);
            
            Response res = client.send(req);
            
            if (res == null) {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, 
                    "Nessuna risposta dal server. Controlla che il server sia avviato.").showAndWait();
                return;
            }
            
            if (res.ok) {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, 
                    "Libro aggiunto correttamente.").showAndWait();
                try { 
                    caller.aggiornaDopoAggiunta(); 
                } catch (Exception ex) { 
                    ex.printStackTrace(); 
                }
                chiudi();
            } else {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, 
                    "Impossibile aggiungere il libro: " + res.message).showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, 
                "Errore durante l'aggiunta: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Metodo delegato alla gestione dell'evento "Annulla".
     * Interrompe l'operazione in corso e richiama il metodo {@link #chiudi()} 
     * per chiudere la finestra senza apportare modifiche.
     */
    @FXML 
    private void annulla() { 
        chiudi(); 
    }

    /**
     * Chiude la finestra (Stage) corrente.
     * Recupera il riferimento alla finestra risalendo la gerarchia dei nodi a partire 
     * dalla scena contenente la 'listaLibri'.
     */
    private void chiudi() {
        ((Stage) listaLibri.getScene().getWindow()).close();
    }
}