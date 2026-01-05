package it.bookrecommender.ui.controller;

import it.bookrecommender.session.*;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.stage.Modality;

/**
 * Controller per la gestione della visualizzazione dettagliata di una libreria.
 * Permette di visualizzare l'elenco dei libri contenuti, aggiungerne di nuovi,
 * rimuoverli o procedere alla valutazione di un volume specifico.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class LibreriaDettaglioController {

    @FXML private Button btnAggiungiValutazione;
    @FXML private Label lblNomeLibreria;
    @FXML private ListView<Libro> listLibri;

    /** ID della libreria attualmente visualizzata. */
    private int libreriaId;
    
    /** Utente attualmente loggato nel sistema. */
    private Utente utente;

    /** Costruttore di default richiesto da JavaFX. */
    public LibreriaDettaglioController() {}

    /**
     * Configura il controller con i dati necessari e avvia il caricamento dei libri.
     * Gestisce la visibilità del pulsante di valutazione in base allo stato dell'utente.
     * @param u L'utente corrente (può essere null se non loggato).
     * @param libId L'identificativo della libreria da caricare.
     * @param nomeLibreria Il nome testuale della libreria da mostrare nel titolo.
     */
    public void setData(Utente u, int libId, String nomeLibreria) {
        // Fallback sulla sessione globale se l'utente passato è nullo
        this.utente = (u != null) ? u : Session.utenteLoggato; 
        
        this.libreriaId = libId;
        this.lblNomeLibreria.setText("Libreria: " + nomeLibreria);

        // Controllo visibilità pulsante valutazione: solo utenti registrati possono valutare
        if (btnAggiungiValutazione != null) {
            boolean isUserLogged = (this.utente != null);
            btnAggiungiValutazione.setVisible(isUserLogged);
            btnAggiungiValutazione.setManaged(isUserLogged);
            
            System.out.println("DEBUG: Utente loggato? " + isUserLogged);
        }
        caricaLibri();
    }

    /**
     * Metodo di utility per aggiornare la lista dei libri in seguito 
     * a operazioni di inserimento o modifica.
     */
    public void aggiornaDopoAggiunta() {
        caricaLibri();
    }

    /**
     * Apre una finestra modale per la selezione di un nuovo libro da inserire nella libreria.
     * @param event L'evento click generato dall'utente.
     */
    @FXML
    private void apriSelettoreLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/selezionaLibro.fxml"));
            Scene scene = new Scene(loader.load());
            SelettoreLibroController controller = loader.getController();
            
            // Passa l'ID libreria e l'istanza di questo controller per callback
            controller.setData(this.libreriaId, this.utente, this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Seleziona libro da aggiungere");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura selettore libri: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Invia una richiesta di rimozione per il libro selezionato nella lista.
     * In caso di successo o errore di rete, aggiorna la UI rimuovendo l'elemento.
     * @param event L'evento click generato dall'utente.
     */
    @FXML
    private void rimuoviLibro(ActionEvent event) {
        Libro sel = listLibri.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.INFORMATION, "Seleziona prima un libro da rimuovere.").showAndWait();
            return;
        }

        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            // ⚠️ Verificare che il Server gestisca esattamente questa stringa
            req.action = "REMOVE_BOOK_FROM_LIBRERIA";
            req.libreriaId = this.libreriaId;
            req.libroId = sel.getLibroId();
            
            Response res = client.send(req);
            
            if (res != null && res.ok) {
                listLibri.getItems().remove(sel);
                new Alert(Alert.AlertType.INFORMATION, "Libro rimosso.").showAndWait();
            } else {
                // Fallback locale in caso di problemi lato server ma ok logico
                listLibri.getItems().remove(sel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listLibri.getItems().remove(sel);
        }
    }

    /**
     * Apre l'interfaccia per consigliare libri basandosi su quello attualmente selezionato.
     * @param event L'evento click generato dall'utente.
     */
    @FXML
    private void consigliaLibri(ActionEvent event) {
        Libro sel = listLibri.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.INFORMATION, "Seleziona prima il libro target per i consigli.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/consigli.fxml"));
            Scene scene = new Scene(loader.load());
            ConsigliController controller = loader.getController();
            
            // Passa i dati necessari per la logica dei suggerimenti
            List<Libro> libri = listLibri.getItems();
            controller.setData(sel.getLibroId(), libri, this.utente);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Definisci consigli per: " + sel.getTitolo());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura consigli: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Chiude la finestra corrente.
     * * @param event L'evento click generato dall'utente.
     */
    @FXML
    private void chiudi(ActionEvent event) {
        ((Stage) lblNomeLibreria.getScene().getWindow()).close();
    }

    /**
     * Apre la finestra per inserire una valutazione multi-criterio per il libro selezionato.
     */
    @FXML
    private void valutaLibro() {
        Libro libroSelezionato = listLibri.getSelectionModel().getSelectedItem();

        if (libroSelezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona un libro da valutare.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/aggiungiValutazione.fxml"));
            Scene scene = new Scene(loader.load());
            
            AggiungiValutazioneController controller = loader.getController();
            controller.setData(libroSelezionato.getLibroId(), utente);

            Stage stage = new Stage();
            stage.setTitle("Valuta: " + libroSelezionato.getTitolo());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura valutazione: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Recupera l'elenco aggiornato dei libri della libreria interrogando il server.
     * Popola la ListView con i risultati ricevuti.
     */
    private void caricaLibri() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            
            // ⚠️ NOTA: Cambiato in "GET_LIBRI_LIBRERIA" per corrispondere al client, 
            // ma assicurati che nel Server sia uguale!
            req.action = "GET_LIBRI_LIBRERIA"; 
            req.libreriaId = this.libreriaId; 

            Response res = client.send(req);

            if (res.ok && res.libri != null) {
                listLibri.setItems(FXCollections.observableArrayList(res.libri));
                System.out.println("Libri caricati per la libreria " + libreriaId + ": " + res.libri.size());
            } else {
                System.err.println("Errore caricamento libri libreria: " + res.message);
                // Alert rimosso per non bloccare l'esecuzione in caso di libreria vuota
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica la scena Home e la imposta nello stage corrente.
     */
    @FXML
    private void tornaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/home.fxml"));
            Scene scene = new Scene(loader.load());
            HomeController controller = loader.getController();
            controller.setUtente(utente); 
            
            Stage stage = (Stage) lblNomeLibreria.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}