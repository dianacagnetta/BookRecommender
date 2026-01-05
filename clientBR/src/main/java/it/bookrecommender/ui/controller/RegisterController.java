package it.bookrecommender.ui.controller;
import it.bookrecommender.net.*;
import it.bookrecommender.dto.*;    
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controller per la registrazione di un nuovo utente.
 *
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class RegisterController {
	/**
	 * Costruttore predefinito della classe.
	 */
	public RegisterController() {}

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField cfField;
    @FXML private TextField emailField;
    @FXML private TextField useridField;
    @FXML private PasswordField passwordField;

    /**
     * Raccoglie i dati dai campi, verifica la completezza e la lunghezza del Codice Fiscale,
     * quindi invia la richiesta "REGISTER" al server.
     */
    @FXML
    public void registerAction() {
        try {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String cf = cfField.getText().trim();
            String email = emailField.getText().trim();
            String userid = useridField.getText().trim();
            String pwd = passwordField.getText().trim();

            if (nome.isEmpty() || cognome.isEmpty() || cf.isEmpty() || email.isEmpty() || userid.isEmpty() || pwd.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Compila tutti i campi!").showAndWait();
                return;
            }

            if (cf.length() != 16) {
                new Alert(Alert.AlertType.ERROR, "Il Codice Fiscale deve avere 16 caratteri.").showAndWait();
                return;
            }

            NetworkClient client = NetworkClient.getInstance();
            Request req = new Request();
            req.action = "REGISTER";
            req.nome = nome;
            req.cognome = cognome;
            req.codiceFiscale = cf;
            req.email = email;
            req.userid = userid;
            req.passwordHash = pwd;

            Response res = client.send(req);

            if (res.ok) {
                new Alert(Alert.AlertType.INFORMATION, "Registrazione completata!").showAndWait();
                goToLogin(null);
            } else {
                new Alert(Alert.AlertType.ERROR, res.message).showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore connessione al server").showAndWait();
        }
    }

    /**
     * Naviga verso la schermata di login.
     * @param event L'evento che ha scatenato la navigazione (se presente).
     */
    @FXML
    public void goToLogin(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                stage = (Stage) nomeField.getScene().getWindow();
            }
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}