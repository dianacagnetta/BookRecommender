package it.bookrecommender.ui.controller;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller per la ricerca avanzata dei libri.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class RicercaController {
	/**
	 * Costruttore predefinito della classe.
	 */
	public RicercaController() {}
    @FXML private TextField titoloField;
    @FXML private TextField autoreField;
    @FXML private TextField annoField;
    @FXML private TableView<Libro> tableRisultati;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutore;
    @FXML private TableColumn<Libro, Integer> colAnno;
    @FXML private TableColumn<Libro, String> colEditore;
    @FXML private TableColumn<Libro, String> colCategoria;
    @FXML private Hyperlink backLink;

    private boolean guest = false;
    private Utente utente = null;
/**
     * Configura la modalità di accesso dell'utente.
     * @param g true se l'utente sta navigando come ospite (funzionalità limitate), 
     * false se l'utente è autenticato.
     */
    public void setGuest(boolean g) { this.guest = g; }

    /**
     * Imposta l'utente attualmente loggato nel sistema.
     * Questo metodo è essenziale per mantenere la sessione durante la navigazione 
     * tra le diverse schermate dell'applicazione.
     * @param u L'oggetto {@link Utente} che rappresenta il profilo attivo.
     */
    public void setUtente(Utente u) { this.utente = u; }

    /**
     * Gestisce il ritorno alla schermata principale (Home).
     * Carica il file FXML della Home, recupera il relativo controller e vi inietta 
     * l'oggetto utente corrente per preservare lo stato della sessione. 
     * Infine, sostituisce la scena corrente nello stage principale.
     */
    @FXML
    public void tornaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/home.fxml"));
            Scene scene = new Scene(loader.load());

            HomeController controller = loader.getController();
            controller.setUtente(this.utente);

            Stage stage = (Stage) tableRisultati.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza le colonne della tabella e imposta il listener per il doppio click
     * su una riga per aprire i dettagli del libro.
     */
   @FXML
public void initialize() {
    // I nomi tra virgolette devono corrispondere esattamente ai campi in Libro.java
    colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
    colAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
    colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
    colEditore.setCellValueFactory(new PropertyValueFactory<>("editore"));
    colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

    // Listener per il doppio click per aprire i dettagli
    tableRisultati.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {
            Libro selected = tableRisultati.getSelectionModel().getSelectedItem();
            if (selected != null) apriDettagliLibro(selected.getLibroId());
        }
    });
}
    /**
     * Invia la richiesta di ricerca al server basata su titolo, autore e anno.
     */
    @FXML
    public void cercaLibri() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "SEARCH_BOOKS";
            String titolo = titoloField.getText() != null ? titoloField.getText().trim().toLowerCase() : "";
            String autore = autoreField.getText() != null ? autoreField.getText().trim().toLowerCase() : "";
            req.titolo = titolo;
            req.autore = autore;
            Integer anno = null;
            if (!annoField.getText().isEmpty()) {
                try {
                    anno = Integer.parseInt(annoField.getText());
                    req.anno = anno;
                } catch (NumberFormatException ex) {
                    // Ignora input non numerico
                }
            }

            Response res = client.send(req);
            if (res.ok && res.libri != null) {
                // Filtro locale per titolo, autore e anno
                ObservableList<Libro> filtered = FXCollections.observableArrayList();
                for (Libro l : res.libri) {
                    boolean match = true;
                    if (!titolo.isEmpty() && (l.getTitolo() == null || !l.getTitolo().toLowerCase().contains(titolo))) match = false;
                    if (!autore.isEmpty() && (l.getAutore() == null || !l.getAutore().toLowerCase().contains(autore))) match = false;
                    if (anno != null && (l.getAnnoPubblicazione() != anno)) match = false;
                    if (match) filtered.add(l);
                }
                tableRisultati.setItems(filtered);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica e visualizza la finestra di dettaglio per un libro specifico.
     * @param libroId ID del libro da visualizzare.
     */
    private void apriDettagliLibro(int libroId) {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "GET_BOOK_DETAILS";
            req.libroId = libroId;


            Response res = client.send(req);
            // DEBUG: stampa la risposta ricevuta dal server
            System.out.println("[DEBUG] res.ok=" + res.ok + ", res.libro=" + res.libro);
            if (!res.ok || res.libro == null) {
                System.out.println("[DEBUG] Dettagli libro non disponibili: la finestra non verrà aperta.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/visualizzalibro.fxml"));
            Scene scene = new Scene(loader.load());
            VisualizzaLibroController controller = loader.getController();

            controller.setGuest(guest);
            controller.setUtente(utente);
            // Visualizzazione da ricerca/catalogo: non permettere aggiunta di valutazioni
            controller.setAllowAddValutazione(false);
            controller.setData(res.libro, res.valutazioni, res.consigli);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dettagli libro");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}