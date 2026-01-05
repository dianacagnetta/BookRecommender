package it.bookrecommender;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione Client che estende Application di JavaFX.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class ClientMain extends Application {

    /**
     * Costruttore di default richiesto da Javadoc.
     */
    public ClientMain() {}

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookrecommender/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Book Recommender - Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Punto di avvio standard dell'applicazione Java.
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}