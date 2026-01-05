package it.bookrecommender.ui.controller;

import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller per la gestione della finestra di inserimento valutazioni.
 * Permette all'utente di inserire voti numerici (1-5) e commenti testuali per diverse categorie
 * di analisi del libro, calcolando automaticamente una media finale.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class AggiungiValutazioneController {

	/**
     * Costruttore predefinito della classe AggiungiValutazioneController.
     */
    public AggiungiValutazioneController() {
    }
    // Componenti UI per l'inserimento dei voti numerici
    @FXML private TextField fieldStile;
    @FXML private TextField fieldContenuto;
    @FXML private TextField fieldGradevolezza;
    @FXML private TextField fieldOriginalita;
    @FXML private TextField fieldEdizione;

    // Componenti UI per l'inserimento dei commenti testuali specifici
    @FXML private TextField fieldCommentoStile;
    @FXML private TextField fieldCommentoContenuto;
    @FXML private TextField fieldCommentoGradevolezza;
    @FXML private TextField fieldCommentoOriginalita;
    @FXML private TextField fieldCommentoEdizione;

    // Area di testo per un commento riassuntivo finale
    @FXML private TextArea fieldCommentoGenerale;

    private int libroId; // ID univoco del libro nel database
    private Utente utente; // Utente che sta effettuando l'operazione

    /**
     * Inizializza il controller con i riferimenti necessari all'identificazione del libro e dell'autore.
     * @param libroId Identificativo del libro selezionato.
     * @param utente Oggetto Utente loggato.
     */
    public void setData(int libroId, Utente utente) {
        this.libroId = libroId;
        this.utente = utente;
    }

/**
     * Inizializza il controller dopo che il file FXML è stato caricato.
     * Configura i vincoli di input per i campi di testo, limitando la lunghezza 
     * massima dei commenti a 256 caratteri.
     */
    @FXML
    private void initialize() {
        // Enforce max 256 characters on all comment fields
        enforceMax(fieldCommentoGenerale, 256);
        enforceMax(fieldCommentoStile, 256);
        enforceMax(fieldCommentoContenuto, 256);
        enforceMax(fieldCommentoGradevolezza, 256);
        enforceMax(fieldCommentoOriginalita, 256);
        enforceMax(fieldCommentoEdizione, 256);
    }

    /**
     * Aggiunge un listener a un controllo di testo per limitare in tempo reale 
     * il numero di caratteri inseribili dall'utente.
     * @param ctl Il controllo UI (es. TextField o TextArea) da monitorare.
     * @param max Il numero massimo di caratteri consentiti.
     */

    private void enforceMax(javafx.scene.control.TextInputControl ctl, int max) {
        if (ctl == null) return;
        ctl.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > max) {
                ctl.setText(newVal.substring(0, max));
            }
        });
    }

    /**
     * Raccoglie i dati dai campi di input, calcola il voto finale come media aritmetica
     * e invia la richiesta di salvataggio al server tramite NetworkClient.
     */
    @FXML
    private void salvaValutazione() {
        try {
            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "ADD_VALUTAZIONE";

            // Associazione ID libro e ID utente alla richiesta
            req.libroId = libroId;
           req.userid = utente.getUserid();

            // Parsing dei valori numerici dai campi di testo
            req.stile = Integer.parseInt(fieldStile.getText());
            req.commentoStile = trimTo(fieldCommentoStile.getText(), 256);
            req.contenuto = Integer.parseInt(fieldContenuto.getText());
            req.commentoContenuto = trimTo(fieldCommentoContenuto.getText(), 256);
            req.gradevolezza = Integer.parseInt(fieldGradevolezza.getText());
            req.commentoGradevolezza = trimTo(fieldCommentoGradevolezza.getText(), 256);
            req.originalita = Integer.parseInt(fieldOriginalita.getText());
            req.commentoOriginalita = trimTo(fieldCommentoOriginalita.getText(), 256);
            req.edizione = Integer.parseInt(fieldEdizione.getText());
            req.commentoEdizione = trimTo(fieldCommentoEdizione.getText(), 256);

            // Calcolo del voto finale (media aritmetica semplice)
            req.votoFinale = (req.stile + req.contenuto + req.gradevolezza + req.originalita + req.edizione) / 5;
            req.commentoGenerale = trimTo(fieldCommentoGenerale.getText(), 256);

            Response res = client.send(req);

            if (res.ok) {
                new Alert(Alert.AlertType.INFORMATION, "Valutazione salvata").showAndWait();
                fieldStile.getScene().getWindow().hide(); // Chiude il pop-up in caso di successo
            } else {
                new Alert(Alert.AlertType.ERROR, res.message).showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tronca una stringa alla lunghezza specificata se supera il limite massimo.
     * Metodo di sicurezza per garantire che i dati inviati al server rispettino i limiti del database.
     *
     * @param s   La stringa di input da controllare.
     * @param max La lunghezza massima consentita.
     * @return La stringa troncata, oppure la stringa originale se rientra nel limite. 
     * Ritorna null se l'input è null.
     */
    
    private String trimTo(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}