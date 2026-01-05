package it.bookrecommender;

/**
 * Classe di avvio ausiliaria per l'applicazione.
 * Necessaria per il caricamento dei moduli JavaFX.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class Launcher {

    /**
     * Costruttore di default richiesto da Javadoc.
     */
    public Launcher() {}

    /**
     * Metodo di ingresso che avvia l'applicazione principale.
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        ClientMain.main(args);
    }
}