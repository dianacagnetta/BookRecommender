package it.bookrecommender.ui.controller;


import it.bookrecommender.session.*;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller per la gestione della schermata di autenticazione.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class LoginController {

	/**
	 * Costruttore predefinito della classe.
	 */
	public LoginController() {}
    @FXML private TextField useridField;
    @FXML private PasswordField passwordField;

    /**
     * Esegue la procedura di login inviando le credenziali al server.
     * In caso di successo, aggiorna la Session e naviga verso la Home.
     */
    @FXML
    public void loginAction() {
        try {
            NetworkClient client = NetworkClient.getInstance();

            Request req = new Request();
            req.action = "LOGIN";
            req.userid = useridField.getText();
            req.passwordHash = passwordField.getText();

            Response res = client.send(req);

            if (res.ok) {
                Session.login(res.utente);
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/home.fxml"));
                Scene scene = new Scene(loader.load());
                HomeController controller = loader.getController();
                controller.setUtente(res.utente);

                Stage stage = (Stage) useridField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                new Alert(Alert.AlertType.ERROR, res.message).showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore connessione al server").showAndWait();
        }
    }

    /**
     * Consente l'accesso all'applicazione senza credenziali.
     * Naviga alla Home impostando l'utente a null nel HomeController.
     */
    @FXML
    public void enterAsGuest() {
        try {
            Session.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/home.fxml"));
            Scene scene = new Scene(loader.load());

            HomeController controller = loader.getController();
            controller.setUtente(null);

            Stage stage = (Stage) useridField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Errore nel caricamento della home").showAndWait();
        }
    }

    /**
     * Metodo di navigazione verso la schermata di registrazione.
     */
    @FXML
    public void goToRegister() {
        try {
            Stage stage = (Stage) useridField.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/bookrecommender/register.fxml")));
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore apertura pagina di registrazione").showAndWait();
        }
    }
}