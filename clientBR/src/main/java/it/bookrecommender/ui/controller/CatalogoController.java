package it.bookrecommender.ui.controller;

import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.stage.Modality;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * Controller per la gestione della visualizzazione del catalogo libri.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class CatalogoController {

    @FXML private TableView<Libro> tableLibri;
    @FXML private TextField fieldTitolo;
    @FXML private TextField fieldAutore;
    @FXML private TextField fieldAnno;
    @FXML private ComboBox<String> comboTipoRicerca;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutore;
    @FXML private TableColumn<Libro, Integer> colAnno;
    @FXML private TableColumn<Libro, String> colEditore;
    @FXML private TableColumn<Libro, String> colCategoria;
    @FXML private Button btnTornaHome;

    private boolean guest = false;
    private Utente utente;
    
    /** Cache locale dei libri per evitare richieste ripetute al server. */
    private ObservableList<Libro> libriCache;

    /** Costruttore di default. */
    public CatalogoController() {}

    /**
     * Imposta lo stato di ospite per limitare le funzionalità.
     * @param g true se l'utente è guest.
     */
    public void setGuest(boolean g) { this.guest = g; }

    /**
     * Imposta l'utente loggato.
     * @param u L'oggetto utente della sessione.
     */
    public void setUtente(Utente u) { this.utente = u; }

    /**
     * Inizializza i componenti della UI e definisce la logica di visualizzazione.
     */
    @FXML
    public void initialize() {
        // Popola la ComboBox dei tipi di ricerca
        comboTipoRicerca.getItems().setAll("Titolo", "Autore", "Autore e Anno");
        comboTipoRicerca.getSelectionModel().selectFirst();
        aggiornaVisibilitaCampi();
        comboTipoRicerca.setOnAction(e -> aggiornaVisibilitaCampi());

        // Collegamento colonne
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colEditore.setCellValueFactory(new PropertyValueFactory<>("editore"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Carica i libri all'apertura
        loadBooks();

        // Doppio click su riga apre i dettagli del libro
        tableLibri.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Libro selected = tableLibri.getSelectionModel().getSelectedItem();
                if (selected != null) apriDettagliLibro(selected.getLibroId());
            }
        });
    }

    /**
     * Mostra o nasconde i campi di ricerca e le label associate in base al tipo selezionato.
     */
    private void aggiornaVisibilitaCampi() {
        String tipo = comboTipoRicerca.getSelectionModel().getSelectedItem();
        boolean showTitolo = "Titolo".equals(tipo);
        boolean showAutore = "Autore".equals(tipo) || "Autore e Anno".equals(tipo);
        boolean showAnno = "Autore e Anno".equals(tipo);
        fieldTitolo.setVisible(showTitolo);
        fieldTitolo.setManaged(showTitolo);
        fieldAutore.setVisible(showAutore);
        fieldAutore.setManaged(showAutore);
        fieldAnno.setVisible(showAnno);
        fieldAnno.setManaged(showAnno);
        // Mostra/nasconde anche le label associate
        if (fieldTitolo.getParent() instanceof javafx.scene.layout.HBox) {
            javafx.scene.layout.HBox hbox = (javafx.scene.layout.HBox) fieldTitolo.getParent();
            for (javafx.scene.Node node : hbox.getChildren()) {
                if (node instanceof Label) {
                    Label lbl = (Label) node;
                    if ("Titolo:".equals(lbl.getText())) { lbl.setVisible(showTitolo); lbl.setManaged(showTitolo); }
                    if ("Autore:".equals(lbl.getText())) { lbl.setVisible(showAutore); lbl.setManaged(showAutore); }
                    if ("Anno:".equals(lbl.getText())) { lbl.setVisible(showAnno); lbl.setManaged(showAnno); }
                }
            }
        }
    }

    /**
     * Apre la finestra di dettaglio per un libro.
     * @param libroId L'identificativo univoco del libro da visualizzare.
     */
    private void apriDettagliLibro(int libroId) {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "GET_BOOK_DETAILS";
            req.libroId = libroId;

            Response res = client.send(req);
            if (!res.ok || res.libro == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/visualizzalibro.fxml"));
            javafx.scene.Parent root = loader.load();
            VisualizzaLibroController controller = loader.getController();

            controller.setGuest(this.guest);
            controller.setUtente(this.utente);
            controller.setAllowAddValutazione(false);
            controller.setData(res.libro, res.valutazioni, res.consigli);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dettagli libro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.err.println("Errore caricamento dettagli libro:");
            e.printStackTrace();
        }
    }

    /**
     * Carica tutti i libri dal server e li memorizza nella cache locale.
     * <p>
     * Questo metodo viene invocato una sola volta all'apertura del catalogo.
     * I libri vengono salvati in {@code libriCache} per permettere ricerche
     * successive senza ulteriori richieste di rete, migliorando significativamente
     * le prestazioni dell'applicazione.
     * </p>
     */
    public void loadBooks() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "GET_ALL_BOOKS";

            Response res = client.send(req);

            if (res != null && res.libri != null) {
                libriCache = FXCollections.observableArrayList(res.libri);
                tableLibri.setItems(FXCollections.observableArrayList(libriCache));
                System.out.println("Caricati " + res.libri.size() + " libri nel catalogo.");
            } else {
                System.err.println("Nessun dato ricevuto dal server.");
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento libri:");
            e.printStackTrace();
        }
    }

    /**
     * Torna alla Home.
     */
    @FXML
    private void tornaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/home.fxml"));
            Scene scene = new Scene(loader.load());
            HomeController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) btnTornaHome.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Esegue il filtraggio dei libri in base al tipo di ricerca selezionato.
     * <p>
     * Il filtraggio viene eseguito sulla cache locale {@code libriCache} invece di
     * effettuare una nuova richiesta al server, garantendo tempi di risposta immediati.
     * Se la cache è vuota, viene prima popolata tramite {@link #loadBooks()}.
     * </p>
     * <p>
     * Tipi di ricerca supportati:
     * <ul>
     *   <li><b>Titolo</b>: ricerca parziale case-insensitive sul titolo del libro</li>
     *   <li><b>Autore</b>: ricerca parziale case-insensitive sul nome dell'autore</li>
     *   <li><b>Autore e Anno</b>: combinazione di ricerca su autore e anno di pubblicazione</li>
     * </ul>
     * </p>
     * @param event L'evento di azione generato dal click sul pulsante di ricerca.
     */
    @FXML
    private void searchBooks(ActionEvent event) {
        try {
            // Usa la cache locale invece di ricaricare dal server
            if (libriCache == null || libriCache.isEmpty()) {
                loadBooks();
                return;
            }
            
            String tipo = comboTipoRicerca.getSelectionModel().getSelectedItem();
            
            // Validazione: controlla che i campi di ricerca non siano vuoti
            boolean campiVuoti = false;
            String messaggio = "";
            
            if ("Titolo".equals(tipo)) {
                if (fieldTitolo.getText() == null || fieldTitolo.getText().trim().isEmpty()) {
                    campiVuoti = true;
                    messaggio = "Inserire un titolo per effettuare la ricerca.";
                }
            } else if ("Autore".equals(tipo)) {
                if (fieldAutore.getText() == null || fieldAutore.getText().trim().isEmpty()) {
                    campiVuoti = true;
                    messaggio = "Inserire un autore per effettuare la ricerca.";
                }
            } else if ("Autore e Anno".equals(tipo)) {
                boolean autoreVuoto = fieldAutore.getText() == null || fieldAutore.getText().trim().isEmpty();
                boolean annoVuoto = fieldAnno.getText() == null || fieldAnno.getText().trim().isEmpty();
                if (autoreVuoto || annoVuoto) {
                    campiVuoti = true;
                    messaggio = "Inserire sia l'autore che l'anno per effettuare la ricerca.";
                }
            }
            
            if (campiVuoti) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Parametri mancanti");
                alert.setHeaderText("Campi di ricerca vuoti");
                alert.setContentText(messaggio);
                alert.showAndWait();
                return;
            }
            
            ObservableList<Libro> filtered = FXCollections.observableArrayList();
            
            if ("Titolo".equals(tipo)) {
                String t = fieldTitolo.getText().trim().toLowerCase();
                for (Libro l : libriCache) {
                    if (l.getTitolo() != null && l.getTitolo().toLowerCase().contains(t)) {
                        filtered.add(l);
                    }
                }
            } else if ("Autore".equals(tipo)) {
                String a = fieldAutore.getText().trim().toLowerCase();
                for (Libro l : libriCache) {
                    if (l.getAutore() != null && l.getAutore().toLowerCase().contains(a)) {
                        filtered.add(l);
                    }
                }
            } else if ("Autore e Anno".equals(tipo)) {
                String a = fieldAutore.getText().trim().toLowerCase();
                int anno = Integer.parseInt(fieldAnno.getText().trim());
                for (Libro l : libriCache) {
                    boolean matchAutore = l.getAutore() != null && l.getAutore().toLowerCase().contains(a);
                    boolean matchAnno = l.getAnnoPubblicazione() == anno;
                    if (matchAutore && matchAnno) {
                        filtered.add(l);
                    }
                }
            }
            tableLibri.setItems(filtered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Resetta i filtri di ricerca e ripristina la visualizzazione completa del catalogo.
     * <p>
     * Questo metodo svuota tutti i campi di input (titolo, autore, anno) e
     * ripopola la tabella con l'elenco completo dei libri dalla cache locale,
     * senza effettuare alcuna richiesta al server.
     * </p>
     */
    @FXML
    private void resetSearch() {
        if (libriCache != null) {
            tableLibri.setItems(FXCollections.observableArrayList(libriCache));
        }
        fieldTitolo.clear();
        fieldAutore.clear();
        fieldAnno.clear();
    }
}
