package it.bookrecommender.dao.jdbc;

import it.bookrecommender.dao.interfaces.IConsigliDAO;
import it.bookrecommender.model.LibroConsigliato;
import it.bookrecommender.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link IConsigliDAO} tramite tecnologia JDBC.
 * Questa classe gestisce la persistenza dei consigli di lettura nel database, 
 * occupandosi della traduzione degli oggetti Java in query SQL e viceversa.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class ConsigliDAOImpl implements IConsigliDAO {

	  /**
	   * Costruttore predefinito della classe.
	   */
	public ConsigliDAOImpl() {}
    /**
     * Aggiunge un suggerimento di lettura al database.
     * Effettua prima una ricerca per convertire lo 'userid' (stringa) nell'ID numerico interno.
     */
    @Override
    public void addConsiglio(String userId, int libroId, int suggeritoId) throws Exception {
        // Query per recuperare l'ID numerico dell'utente
        String sqlUser = "SELECT id FROM br.UtentiRegistrati WHERE userid = ?";
        // Query per inserire l'associazione del consiglio
        String sqlInsert = "INSERT INTO br.ConsigliLibri(utente_id, libro_id, suggerito_libro_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            int uId = -1;
            // 1. Ricerca dell'utente
            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setString(1, userId);
                ResultSet rs = psUser.executeQuery();
                if (rs.next()) uId = rs.getInt("id");
            }
            
            // 2. Se l'utente esiste, inserisco il consiglio
            if (uId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setInt(1, uId); 
                    ps.setInt(2, libroId); 
                    ps.setInt(3, suggeritoId);
                    ps.executeUpdate();
                }
            }
        }
    }

    /**
     * Recupera la lista dei libri consigliati per un libro specifico, 
     * contando quante volte ogni libro è stato suggerito dagli utenti.
     */
    @Override
    public List<LibroConsigliato> getConsigli(int libroId) throws Exception {
        List<LibroConsigliato> lista = new ArrayList<>();
        // Query con JOIN tra ConsigliLibri e Libri per ottenere il titolo del libro suggerito
        // Raggruppa per titolo e conta le occorrenze (frequenza del consiglio)
        String sql = "SELECT l2.titolo, COUNT(*) AS volte FROM br.ConsigliLibri c " +
                     "JOIN br.Libri l2 ON c.suggerito_libro_id = l2.id " +
                     "WHERE c.libro_id = ? GROUP BY l2.titolo";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();
            
            // Popolamento della lista con gli oggetti LibroConsigliato
            while (rs.next()) {
                lista.add(new LibroConsigliato(rs.getString("titolo"), rs.getInt("volte")));
            }
        }
        return lista;
    }

    /**
     * Rimuove i consigli inseriti da un utente specifico per un determinato libro.
     */
    @Override
    public void clearConsigli(String userId, int libroId) throws Exception {
        // Query per recuperare l'ID numerico dell'utente
        String sqlUser = "SELECT id FROM br.UtentiRegistrati WHERE userid = ?";
        // Query per eliminare i record corrispondenti
        String sqlDelete = "DELETE FROM br.ConsigliLibri WHERE utente_id = ? AND libro_id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            int uId = -1;
            // 1. Ricerca dell'utente tramite stringa userid
            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setString(1, userId);
                ResultSet rs = psUser.executeQuery();
                if (rs.next()) uId = rs.getInt("id");
            }
            
            // 2. Se l'utente è stato trovato, procedo alla cancellazione
            if (uId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                    ps.setInt(1, uId); 
                    ps.setInt(2, libroId);
                    ps.executeUpdate();
                }
            }
        }
    }
}