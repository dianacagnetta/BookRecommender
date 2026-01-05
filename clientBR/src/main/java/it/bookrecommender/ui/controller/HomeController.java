package it.bookrecommender.ui.controller;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import it.bookrecommender.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Controller della dashboard principale dell'applicazione.
 * Gestisce l'instradamento dell'utente verso le funzionalità di ricerca, catalogo e gestione librerie.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class HomeController {
	
	/**
	 * Costruttore predefinito della classe.
     */
	public HomeController() {}
	

    @FXML private Label welcomeLabel;

    private Utente utente;
    private boolean guest = false;

    /**
     * Configura lo stato della sessione nell'interfaccia.
     * Se l'oggetto utente è nullo, abilita restrizioni funzionali per la modalità ospite.
     * @param u L'oggetto utente autenticato o null per accesso ospite.
     */
    public void setUtente(Utente u) {
        this.utente = u;
        if (u == null) {
            guest = true;
            welcomeLabel.setText("Modalità OSPITE");
        } else {
            guest = false;
            welcomeLabel.setText("Benvenuto, " + u.getNome() + " " + u.getCognome());
        }
    }

    /**
     * Termina la sessione corrente chiudendo la finestra principale.
     */
    @FXML
    public void logout() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Metodo di navigazione per passare alla vista Catalogo.
     * Inizializza il CatalogoController passando le informazioni sulla sessione.
     * @throws Exception Se si verifica un errore durante il caricamento del file FXML o l'inizializzazione della scena.
     */
    @FXML
    public void openCatalogo() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/catalogo.fxml"));
        Scene scene = new Scene(loader.load());

        CatalogoController controller = loader.getController();
        controller.setGuest(guest);
        controller.setUtente(utente);
        controller.loadBooks();

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Metodo per la ricerca di libri.
     */

@FXML
private void openRicerca() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/ricerca.fxml"));
        Scene scene = new Scene(loader.load());
        RicercaController controller = loader.getController();

        // Passa i dati: se utente è null, l'app sa che è un guest
        controller.setUtente(this.utente); 
        controller.setGuest(this.utente == null); 

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(scene);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * Metodo di navigazione per la sezione delle librerie personali.
     * Impedisce l'accesso se l'utente è loggato come ospite tramite un avviso pop-up.
     * @throws Exception Se si verifica un errore durante il caricamento del file FXML o l'inizializzazione della scena.
     */
    @FXML
    public void openLibrerie() throws Exception {
        if (guest) {
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING,
                    "Solo gli utenti registrati possono gestire le proprie librerie."
            ).showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/librerie.fxml"));
        Scene scene = new Scene(loader.load());

        LibrerieController controller = loader.getController();
        controller.setUtente(utente);

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}