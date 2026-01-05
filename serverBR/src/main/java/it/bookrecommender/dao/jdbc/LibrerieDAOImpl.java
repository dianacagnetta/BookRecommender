package it.bookrecommender.dao.jdbc;

import it.bookrecommender.dao.interfaces.ILibrerieDAO;
import it.bookrecommender.model.*;
import it.bookrecommender.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione JDBC dell'interfaccia {@link ILibrerieDAO}.
 * Gestisce la persistenza delle librerie personali degli utenti e le relazioni 
 * molti-a-molti con i libri contenuti, utilizzando il database relazionale.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class LibrerieDAOImpl implements ILibrerieDAO {
	/**
	 * Costruttore predefinito della classe.
	 */
	public LibrerieDAOImpl() {}
    /**
     * Recupera l'elenco delle librerie associate a un utente specifico.
     */
    @Override
    public List<Libreria> getLibrerieByUtenteId(int utenteId) throws Exception {
        List<Libreria> lista = new ArrayList<>();
        String sql = "SELECT * FROM br.librerie WHERE utente_id = ?";
        
        // Uso del try-with-resources per garantire la chiusura di Connection e PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, utenteId);
            ResultSet rs = ps.executeQuery();
            
            // Itera sui risultati e crea oggetti Libreria
            while (rs.next()) {
                lista.add(new Libreria(
                    rs.getInt("id"), 
                    rs.getInt("utente_id"), 
                    rs.getString("nome")
                ));
            }
        }
        return lista;
    }

    /**
     * Crea una nuova entry nella tabella delle librerie.
     */
    @Override
    public void creaLibreria(int utenteId, String nome) throws Exception {
        String sql = "INSERT INTO br.librerie(utente_id, nome) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, utenteId);
            ps.setString(2, nome);
            // Esegue l'inserimento
            ps.executeUpdate();
        }
    }

    /**
     * Associa un libro a una libreria nella tabella di giunzione 'librerielibri'.
     * @return true se l'inserimento è andato a buon fine.
     */
    @Override
    public boolean aggiungiLibroALibreria(int libreriaId, int libroId) throws Exception {
        String sql = "INSERT INTO br.librerielibri(libreria_id, libro_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libreriaId);
            ps.setInt(2, libroId);
            // executeUpdate ritorna il numero di righe modificate
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Rimuove l'associazione tra un libro e una libreria.
     * @return true se la riga è stata effettivamente eliminata.
     */
    @Override
    public boolean rimuoviLibroDaLibreria(int libreriaId, int libroId) throws Exception {
        String sql = "DELETE FROM br.librerielibri WHERE libreria_id = ? AND libro_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libreriaId);
            ps.setInt(2, libroId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Recupera i dettagli di tutti i libri contenuti in una specifica libreria 
     * tramite una query di JOIN.
     */
    @Override
    public List<Libro> getLibriInLibreria(int libreriaId) throws Exception {
        List<Libro> lista = new ArrayList<>();
        // Query che unisce la tabella di giunzione con la tabella Libri per ottenere i metadati
        String sql = "SELECT l.* FROM br.librerielibri ll JOIN br.libri l ON ll.libro_id = l.id WHERE ll.libreria_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libreriaId);
            ResultSet rs = ps.executeQuery();
            
            // Mapping del ResultSet sull'oggetto del modello Libro
            while (rs.next()) {
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
}