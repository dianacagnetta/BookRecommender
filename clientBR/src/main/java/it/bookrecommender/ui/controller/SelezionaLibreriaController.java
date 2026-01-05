package it.bookrecommender.ui.controller;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller per la finestra di selezione libreria durante l'aggiunta di un libro.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class SelezionaLibreriaController {
	/**
	 * Costruttore predefinito della classe.
	 */
	public SelezionaLibreriaController() {}
    @FXML private ComboBox<Libreria> comboLibrerie;
    private Utente utente;
    private Libro libro;


    /**
     * Configura l'utente attualmente loggato e avvia immediatamente la procedura 
     * di recupero delle sue librerie dal server.
     * Questo metodo funge da punto di ingresso per i dati della sessione.
     *
     * @param u L'oggetto {@link Utente} che rappresenta il profilo attivo.
     */
    public void setUtente(Utente u) {
        this.utente = u;
        caricaLibrerie();
    }

    /**
     * Associa un oggetto libro specifico a questo controller.
     * Utilizzato solitamente quando si apre la vista di dettaglio o di modifica 
     * per un singolo volume.
     *
     * @param l L'oggetto {@link Libro} da visualizzare o gestire.
     */
    public void setLibro(Libro l) {
        this.libro = l;
    }

    /**
     * Popola la ComboBox con le librerie possedute dall'utente.
     */
    private void caricaLibrerie() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "GET_LIBRERIE_UTENTE";
            req.utenteId = utente.getId();

            Response res = client.send(req);
            comboLibrerie.getItems().clear();
            if (res.ok && res.librerie != null) comboLibrerie.getItems().addAll(res.librerie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invia il comando al server per inserire il libro nella libreria selezionata.
     */
    @FXML
    private void aggiungiLibro() {
        Libreria sel = comboLibrerie.getValue();
        if (sel == null) return;

        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "ADD_BOOK_TO_LIBRERIA";
            req.libreriaId = sel.getId();
            req.libroId = libro.getLibroId();

            Response res = client.send(req);
            new Alert(res.ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, res.message).showAndWait();
            if (res.ok) chiudi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo delegato alla gestione del pulsante di annullamento.
     * Interrompe la procedura corrente e invoca la chiusura della finestra 
     * chiamando il metodo {@link #chiudi()}.
     */
    @FXML private void annulla() { chiudi(); }

    /**
     * Esegue la chiusura dello Stage corrente.
     * Utilizza il riferimento al componente 'comboLibrerie' per risalire alla 
     * scena e alla finestra (Stage) di appartenenza, terminando il ciclo di vita del popup.
     */
    private void chiudi() {
        ((Stage) comboLibrerie.getScene().getWindow()).close();
    }
}