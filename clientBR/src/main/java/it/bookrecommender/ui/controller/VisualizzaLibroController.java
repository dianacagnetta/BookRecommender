package it.bookrecommender.ui.controller;

    
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;
/**
 * Controller per la visualizzazione dettagliata di un libro.
 * * Questa classe gestisce la logica della vista 'visualizzalibro.fxml', occupandosi di:
 * <ul>
 * <li>Visualizzare i metadati del libro (Titolo, Autore, etc.).</li>
 * <li>Calcolare e mostrare la media delle valutazioni degli utenti.</li>
 * <li>Mostrare la lista dei consigli correlati.</li>
 * <li>Gestire i permessi di interfaccia (Guest vs Utente autenticato).</li>
 * </ul>
 * * Utilizza la Reflection per estrarre dinamicamente i commenti dalle valutazioni
 * e implementa un sistema di visualizzazione condizionale per i pulsanti di azione.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class VisualizzaLibroController {

    @FXML private Label lblTitolo;
    @FXML private Label lblAutore;
    @FXML private Label lblAnno;
    @FXML private Label lblEditore;
    @FXML private Label lblCategoria;

    @FXML private ListView<String> listValutazioni;
    @FXML private ListView<String> listConsigli;
    @FXML private javafx.scene.control.TextArea txtCommentoDettaglio;

    @FXML private Button btnAggiungiValutazione;
    @FXML private Button btnAggiungiLibreria;

    private boolean guest = true;
    private Utente utente;
    private Libro libro;
    private java.util.List<Valutazione> valutazioniList;
    private boolean allowAddValutazione = false;

    /**
     * Costruttore predefinito della classe.
     */
    
    public VisualizzaLibroController() {}
/**
     * Imposta lo stato "Ospite" per la visualizzazione attuale.
     * @param g true se l'utente non è loggato.
     */
    public void setGuest(boolean g) { this.guest = g; }
    /**
     * Passa i dati dell'utente loggato al controller.
     * @param u L'oggetto {@link Utente} corrente.
     */
    public void setUtente(Utente u) { this.utente = u; }
    /**
     * Imposta il permesso per l'utente di aggiungere nuove valutazioni.
     * Questo flag viene utilizzato per abilitare o disabilitare i componenti 
     * grafici (pulsanti) relativi all'inserimento di recensioni.
     * @param allow true se l'utente è autorizzato ad aggiungere valutazioni, false altrimenti.
     */
    public void setAllowAddValutazione(boolean allow) { this.allowAddValutazione = allow; }

    /** * Popola la view con i dati del libro, le valutazioni (con media calcolata) 
     * e il commento generale ben visibile. 
     * @param libro       L'oggetto {@link Libro} da visualizzare. Se null, i campi restano vuoti.
     * @param valutazioni La lista di {@link Valutazione} ricevute dal server per questo libro.
     * @param consigli    La lista di {@link LibroConsigliato} che rappresentano i suggerimenti correlati.
     */
    public void setData(Libro libro, List<Valutazione> valutazioni, List<LibroConsigliato> consigli) {
        this.libro = libro;

        if (libro != null) {
            lblTitolo.setText(libro.getTitolo());
            lblAutore.setText("Autore: " + (libro.getAutore() == null ? "" : libro.getAutore()));
            lblAnno.setText("Anno: " + libro.getAnnoPubblicazione());
            lblEditore.setText("Editore: " + (libro.getEditore() == null ? "" : libro.getEditore()));
            lblCategoria.setText("Categoria: " + (libro.getCategoria() == null ? "" : libro.getCategoria()));
        }

        if (listValutazioni != null) {
            this.valutazioniList = valutazioni == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(valutazioni);
            listValutazioni.getItems().clear();
            
            for (Valutazione v : this.valutazioniList) {
                String user = v.getUtenteNome() == null ? "Anonimo" : v.getUtenteNome();
                
                // 1. CALCOLO MEDIA: Sostituiamo il voto finale con la media dei 5 parametri
                double media = (v.getStile() + v.getContenuto() + v.getGradevolezza() + v.getOriginalita() + v.getEdizione()) / 5.0;

                // 2. RECUPERO COMMENTO: Assicuriamoci che il commento generale sia estratto correttamente
                String gen = v.getCommentoGenerale();

                String snippet = "";
                if (gen != null && !gen.trim().isEmpty()) {
                    snippet = gen.length() > 60 ? gen.substring(0, 60) + "..." : gen;
                }

                // Costruzione stringa: Media (Parametri) - Commento
                String summary = String.format("%s — Media: %.1f (St:%d Co:%d Gr:%d Or:%d Ed:%d)%s",
                    user,
                    media, // Ora mostra la media calcolata invece di v.getVotoFinale()
                    v.getStile(), v.getContenuto(), v.getGradevolezza(), v.getOriginalita(), v.getEdizione(),
                    (snippet.isEmpty() ? "" : "\n\"" + snippet + "\"")
                );
                
                listValutazioni.getItems().add(summary);
            }

            listValutazioni.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
                int idx = newIdx.intValue();
                if (idx >= 0 && idx < this.valutazioniList.size()) {
                    Valutazione sel = this.valutazioniList.get(idx);
                    String header = (sel.getUtenteNome() == null ? "" : "Utente: " + sel.getUtenteNome() + "\n\n");
                    txtCommentoDettaglio.setText(header + buildCommentText(sel));
                } else {
                    txtCommentoDettaglio.clear();
                }
            });
        }

        if (listConsigli != null) {
            listConsigli.getItems().clear();
            if (consigli != null) {
                for (LibroConsigliato c : consigli) {
                    listConsigli.getItems().add(c.getTitolo() + " (" + c.getVolte() + ")");
                }
            }
        }

        boolean isUser = (this.utente != null) && this.allowAddValutazione;
        if (btnAggiungiValutazione != null) {
            btnAggiungiValutazione.setVisible(isUser);
            btnAggiungiValutazione.setManaged(isUser);
        }
        if (btnAggiungiLibreria != null) {
            btnAggiungiLibreria.setVisible(isUser);
            btnAggiungiLibreria.setManaged(isUser);
        }
    }
/**
     * Costruisce una stringa formattata contenente tutti i commenti testuali 
     * presenti in una valutazione, separando il commento generale dai dettagli.
     * @param v La valutazione da analizzare.
     * @return Una stringa pronta per essere visualizzata nella TextArea.
     */
    private String buildCommentText(Valutazione v) {
        if (v == null) return "";
        StringBuilder sb = new StringBuilder();
        
        // Priorità al commento generale nel dettaglio
        String gen = v.getCommentoGenerale();
        if (gen == null || gen.isEmpty()) gen = safeGetString(v, "getCommento");
        
        if (gen != null && !gen.trim().isEmpty()) {
        sb.append("COMMENTO GENERALE:\n").append(gen).append("\n\n--- DETTAGLI ---\n");
    }
    
    appendIfPresent(sb, v, "getCommentoStile", "Stile: ");
    appendIfPresent(sb, v, "getCommentoContenuto", "Contenuto: ");
    appendIfPresent(sb, v, "getCommentoGradevolezza", "Gradevolezza: ");
    appendIfPresent(sb, v, "getCommentoOriginalita", "Originalità: ");
    appendIfPresent(sb, v, "getCommentoEdizione", "Edizione: ");
    
    return sb.toString().trim();
}
    private void appendIfPresent(StringBuilder sb, Valutazione v, String getter, String label) {
        String s = safeGetString(v, getter);
        if (s != null && !s.trim().isEmpty()) {
            sb.append(label).append(s).append("\n");
        }
    }
/**
     * Costruisce una stringa formattata contenente tutti i commenti testuali 
     * presenti in una valutazione, separando il commento generale dai dettagli.
     * @param v La valutazione da analizzare.
     * @return Una stringa pronta per essere visualizzata nella TextArea.
     */
    private String safeGetString(Valutazione v, String methodName) {
        try {
            java.lang.reflect.Method m = v.getClass().getMethod(methodName);
            Object o = m.invoke(v);
            return o == null ? null : o.toString();
        } catch (Exception e) { return null; }
    }
/**
     * Apre la finestra modale per l'inserimento di una nuova valutazione.
     */
    @FXML
    private void valutaLibro() {
        if (libro == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/aggiungiValutazione.fxml"));
            Scene scene = new Scene(loader.load());
            AggiungiValutazioneController controller = loader.getController();
            controller.setData(libro.getLibroId(), utente);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Valuta: " + libro.getTitolo());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura valutazione: " + e.getMessage()).showAndWait();
        }
    }
/**
     * Apre la finestra modale per selezionare in quale libreria dell'utente 
     * aggiungere il libro corrente.
     */
    @FXML
    private void apriSceltaLibreria() {
        if (libro == null || utente == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/selezionaLibreria.fxml"));
            Scene scene = new Scene(loader.load());
            SelezionaLibreriaController controller = loader.getController();
            controller.setUtente(utente);
            controller.setLibro(libro);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Aggiungi a libreria: " + libro.getTitolo());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura selezione libreria: " + e.getMessage()).showAndWait();
        }
    }
/**
     * Chiude la finestra di dettaglio.
     */
    @FXML
    private void chiudi() {
        Stage stage = (Stage) lblTitolo.getScene().getWindow();
        stage.close();
    }
}