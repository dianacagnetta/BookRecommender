package it.bookrecommender.dao.jdbc;


import it.bookrecommender.dao.interfaces.ILibroDAO;
import it.bookrecommender.model.Libro;
import it.bookrecommender.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione JDBC per la gestione del catalogo libri.
 * Utilizza query SQL per interagire con la tabella br.libri.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
*/
public class LibroDAOImpl implements ILibroDAO {

	/**
	 * Costruttore predefinito della classe.
	 */
	public LibroDAOImpl() {}
    /**
     * Recupera l'elenco completo dei libri presenti nel database.
     * Mappa la logica precedentemente presente in LibriDAO.findAll().
     * @return Lista di tutti i libri nel catalogo.
     * @throws Exception In caso di errore di connessione o query SQL.
     */
    @Override
    public List<Libro> getAllBooks() throws Exception {
        // Riutilizza la logica di ricerca senza filtri per ottenere tutto il catalogo
        return searchBooks("", "", null);
    }

    /**
     * Esegue una ricerca flessibile filtrando per titolo, autore e anno.
     * Implementa la logica di filtraggio parziale (LIKE) definita in LibriDAO.
     * @param titolo Stringa di ricerca per il titolo.
     * @param autore Stringa di ricerca per l'autore.
     * @param anno Anno di pubblicazione specifico (opzionale).
     * @return Lista di libri filtrati.
     * @throws Exception In caso di errore SQL.
     */
    @Override
    public List<Libro> searchBooks(String titolo, String autore, Integer anno) throws Exception {
        List<Libro> lista = new ArrayList<>();
        // Query SQL con gestione dei parametri vuoti tramite clausole OR
        String sql = "SELECT id, titolo, autori, anno_pubblicazione, editore, categoria " +
                     "FROM br.libri WHERE " +
                     "(LOWER(titolo) LIKE LOWER(?) OR ? = '') " +
                     "AND (LOWER(autori) LIKE LOWER(?) OR ? = '') " +
                     "AND (anno_pubblicazione = ? OR ? IS NULL)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + titolo + "%");
            ps.setString(2, titolo);
            ps.setString(3, "%" + autore + "%");
            ps.setString(4, autore);
            
            if (anno != null) {
                ps.setInt(5, anno);
                ps.setInt(6, anno);
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Creazione dell'oggetto Libro dai dati del ResultSet
                lista.add(new Libro(
                    rs.getInt("id"), 
                    rs.getString("titolo"), 
                    rs.getString("autori"),
                    rs.getInt("anno_pubblicazione"), 
                    rs.getString("editore"), 
                    rs.getString("categoria")
                ));
            }
        }
        return lista;
    }

    /**
     * Recupera i dettagli di un singolo libro tramite ID.
     * Implementa la logica di ricerca per chiave primaria.
     * @param id Identificativo univoco del libro.
     * @return L'oggetto Libro trovato, o null se inesistente.
     * @throws Exception In caso di errore SQL.
     */
    @Override
    public Libro getBookById(int id) throws Exception {
        String sql = "SELECT * FROM br.libri WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Libro(
                    rs.getInt("id"), 
                    rs.getString("titolo"), 
                    rs.getString("autori"),
                    rs.getInt("anno_pubblicazione"), 
                    rs.getString("editore"), 
                    rs.getString("categoria")
                );
            }
        }
        return null;
    }
}