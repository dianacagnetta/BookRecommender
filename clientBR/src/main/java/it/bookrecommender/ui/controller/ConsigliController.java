package it.bookrecommender.ui.controller;

import it.bookrecommender.net.NetworkClient;
import it.bookrecommender.dto.Request;
import it.bookrecommender.dto.Response;
import it.bookrecommender.model.Libro;
import it.bookrecommender.model.Utente;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.List;

/**
 * Controller per la definizione dei consigli tra libri.
 * Permette all'utente di associare a un libro "target" altri libri della sua collezione
 * che ritiene simili o raccomandabili.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class ConsigliController {
	
	/**
     * Costruttore predefinito della classe AggiungiValutazioneController.
     */
    public ConsigliController() {
    }
	
    @FXML private javafx.scene.control.Label lblTitoloLibro;
    @FXML private javafx.scene.control.ListView<Libro> listLibri;

    private int libroId;               
    private Utente utente;

    /**
     * Configura la lista dei libri consigliabili, escludendo il libro sorgente stesso.
     * Abilita la selezione multipla per agevolare l'inserimento di più consigli contemporaneamente.
     * @param libroId L'identificativo univoco del libro per il quale si vuole inserire una valutazione o un consiglio.
     * @param libri   La lista completa dei libri disponibili nel sistema.
     * @param u       L'oggetto {@link Utente} che sta effettuando l'operazione.
     */
    public void setData(int libroId, List<Libro> libri, Utente u) {
        this.libroId = libroId;
        this.utente = u;

        listLibri.getItems().clear();
        for (Libro l : libri) {
            if (l.getLibroId() != libroId) {
                listLibri.getItems().add(l);
            } else {
                lblTitoloLibro.setText("Consiglia libri correlati per: " + l.getTitolo());
            }
        }
        listLibri.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    /**
     * Salva i consigli selezionati dall'utente sul database.
     */
    @FXML
    private void salvaConsigli() {
        List<Libro> selezionati = listLibri.getSelectionModel().getSelectedItems();
        if (selezionati.isEmpty()) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, 
                "Seleziona almeno un libro!").showAndWait();
            return;
        }

        try {
            NetworkClient client = NetworkClient.getInstance();

            // Inserimento nuovi consigli
            for (Libro l : selezionati) {
                Request req = new Request();
                req.action = "ADD_CONSIGLIO";
                req.userid = utente.getUserid();
                req.libroId = libroId;
                req.suggerito = l.getLibroId();
                
                Response res = client.send(req);
                if (!res.ok) {
                    throw new Exception("Errore durante il salvataggio del consiglio per: " + l.getTitolo());
                }
            }

            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, 
                "Consigli salvati!").showAndWait();
            chiudi();
            
        } catch (Exception e) {
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, 
                "Errore: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Chiude la finestra corrente.
     * Recupera lo {@link Stage} di riferimento partendo dalla scena in cui si trova 
     * l'elemento 'listLibri' e invoca il metodo di chiusura del sistema.
     */
    @FXML
    private void chiudi() {
        ((Stage) listLibri.getScene().getWindow()).close();
    }
}