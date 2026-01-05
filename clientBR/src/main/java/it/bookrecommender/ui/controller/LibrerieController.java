package it.bookrecommender.ui.controller;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.FXCollections; 

/**
 * Classe che si occupa della gestione della libreria e i suoi comportamenti.
* @author Cagnetta Diana, 757000, VA
* @author Romano Aurora Lindangela, 757787, VA
* @author Caliaro Fabio, 756230, VA
* @author Fedeli Riccardo, 758019, VA
*/
public class LibrerieController {

	/**
	 * Costruttore predefinito della classe.
	 */
	public LibrerieController() {}
    @FXML private ListView<Libreria> listLibrerie; // Assicurati che sia Libreria
    @FXML private TextField fieldNomeLibreria;
    @FXML private Button btnTornaHome;

    private Utente utente;
/**
     * Inizializza il controller e configura il comportamento della lista delle librerie.
     * Imposta un listener per intercettare il doppio click sugli elementi della lista,
     * permettendo una navigazione rapida verso il dettaglio della libreria selezionata.
     */
    @FXML
    public void initialize() {
        // Gestione doppio click: serve per passare l'utente alla schermata successiva
        listLibrerie.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Libreria selezionata = listLibrerie.getSelectionModel().getSelectedItem();
                if (selezionata != null) {
                    apriDettaglio(selezionata);
                }
            }
        });
    }
/**
     * Configura l'utente corrente per questa vista e avvia automaticamente 
     * il caricamento dei dati dal server.
     * @param u L'oggetto {@link Utente} che sta utilizzando l'applicazione.
     */
    public void setUtente(Utente u) {
        this.utente = u;
        caricaLibrerie();
    }


    /**
     * Recupera l'elenco delle librerie associate all'utente loggato interrogando il server.
     * In caso di risposta positiva, aggiorna la {@link ListView} con i nuovi dati.
     * Gestisce eventuali eccezioni di rete stampando lo stack trace.
     */
    public void caricaLibrerie() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "GET_LIBRERIE_UTENTE";
            req.utenteId = utente.getId();
            Response res = client.send(req);
            
            if (res.ok && res.librerie != null) {
                // CORREZIONE: Aggiungi direttamente la lista di oggetti Libreria
                listLibrerie.setItems(FXCollections.observableArrayList(res.librerie));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }


    /**
     * Esegue il passaggio alla vista di dettaglio di una specifica libreria.
     * Carica il file FXML dedicato e inietta nel controller di destinazione i dati 
     * necessari (utente, ID libreria e nome) per il corretto funzionamento dei permessi.
     *
     * @param lib L'oggetto {@link Libreria} da visualizzare.
     */
    private void apriDettaglio(Libreria lib) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/libreriaDettaglio.fxml"));
            Scene scene = new Scene(loader.load());

            LibreriaDettaglioController controller = loader.getController();

            
            // FONDAMENTALE: Passiamo l'utente! Senza questo, il bottone non apparirà mai
            controller.setData(this.utente, lib.getId(), lib.getNome());

            Stage stage = (Stage) listLibrerie.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }


    /**
     * Gestisce la navigazione verso la schermata Home, assicurandosi di passare 
     * l'oggetto utente al controller di destinazione per mantenere la sessione attiva.
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
     * Crea una nuova libreria per l'utente corrente.
     * Valida il nome inserito e tenta l'operazione sul server. Se il server non è 
     * raggiungibile o l'operazione fallisce, procede con un inserimento "fittizio" 
     * locale per non interrompere il flusso di lavoro dell'utente.
     *
     * @param event L'evento di pressione del tasto di creazione.
     */
    @FXML
    private void creaLibreria(ActionEvent event) {
        try {
            String nome = fieldNomeLibreria.getText();
            if (nome == null || nome.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Inserisci un nome per la libreria.").showAndWait();
                return;
            }

            // Tentativo semplice di invio al server; se fallisce aggiungiamo localmente
            try {
                NetworkClient client = NetworkClient.getInstance();
                Request req = new Request();
                req.action = "CREATE_LIBRERIA";
                req.utenteId = this.utente.getId();
                req.nomeLibreria = nome;
                Response res = client.send(req);
                if (res != null && res.ok) {
                    caricaLibrerie();
                } else {
                    // fallback: aggiungi localmente
                    listLibrerie.getItems().add(new Libreria(-1, this.utente.getId(), nome));
                }
            } catch (Exception e) {
                listLibrerie.getItems().add(new Libreria(-1, this.utente.getId(), nome));
            }

            fieldNomeLibreria.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Verifica se è stata effettuata una selezione nella lista e richiama 
     * la funzione di apertura dettaglio. Mostra un avviso se non è selezionato nulla.
     *
     * @param event L'evento di click sul pulsante "Apri".
     */
    @FXML
    private void apriLibreria(ActionEvent event) {
        Libreria sel = listLibrerie.getSelectionModel().getSelectedItem();
        if (sel != null) apriDettaglio(sel);
        else new Alert(Alert.AlertType.INFORMATION, "Seleziona prima una libreria.").showAndWait();
    }
}