package it.bookrecommender.dao.jdbc;

import it.bookrecommender.dao.interfaces.IValutazioniDAO;
import it.bookrecommender.model.Valutazione;
import it.bookrecommender.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione JDBC dell'interfaccia {@link IValutazioniDAO}.
 * Gestisce la persistenza dei feedback degli utenti, inclusi i voti dettagliati per categoria
 * e i relativi commenti testuali. Include funzionalità per il calcolo delle medie aggregate.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class ValutazioniDAOImpl implements IValutazioniDAO {

	/**
	 * Costruttore predefinito della classe.
	 */
	public ValutazioniDAOImpl() {}
    /**
     * Salva una nuova valutazione nel database. 
     * Il metodo esegue prima una query per convertire lo userid testuale nell'ID numerico interno.
     */
    @Override
    public boolean salvaValutazione(String userId, int libroId, int stile, String stileNote, int contenuto, String contenutoNote,
                                    int gradevolezza, String gradevolezzaNote, int originalita, String originalitaNote,
                                    int edizione, String edizioneNote, int votoFinale, String noteGenerali) throws Exception {
        
        // Query per risolvere l'ID utente dalla stringa userid
        String sqlUser = "SELECT id FROM br.UtentiRegistrati WHERE userid = ?";
        // Query di inserimento massivo di tutti i parametri della valutazione
        String sqlInsert = "INSERT INTO br.valutazionilibri (utente_id, libro_id, stile, contenuto, gradevolezza, originalita, edizione, voto_finale, " +
            "commento_stile, commento_contenuto, commento_gradevolezza, commento_originalita, commento_edizione, commento_generale) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            int uId = -1;
            // 1. Fase di recupero ID Utente
            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setString(1, userId);
                ResultSet rs = psUser.executeQuery();
                if (rs.next()) {
                    uId = rs.getInt("id");
                } else {
                    return false; // Utente non trovato, operazione annullata
                }
            }

            // 2. Fase di inserimento dei dati
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, uId); 
                ps.setInt(2, libroId); 
                ps.setInt(3, stile); 
                ps.setInt(4, contenuto);
                ps.setInt(5, gradevolezza); 
                ps.setInt(6, originalita); 
                ps.setInt(7, edizione); 
                ps.setInt(8, votoFinale);
                ps.setString(9, stileNote); 
                ps.setString(10, contenutoNote); 
                ps.setString(11, gradevolezzaNote);
                ps.setString(12, originalitaNote); 
                ps.setString(13, edizioneNote); 
                ps.setString(14, noteGenerali);
                
                // Restituisce true se almeno una riga è stata inserita
                return ps.executeUpdate() > 0;
            }
        }
    }

    /**
     * Recupera tutte le valutazioni di un libro specifico.
     * Utilizza un LEFT JOIN per ottenere anche i dati anagrafici del recensore.
     */
    @Override
    public List<Valutazione> getValutazioniLibro(int libroId) throws Exception {
        List<Valutazione> lista = new ArrayList<>();
        // Query che unisce la tabella Valutazioni con quella degli Utenti per mostrare chi ha scritto cosa
        String sql = "SELECT v.*, u.nome, u.cognome FROM br.ValutazioniLibri v " +
                     "LEFT JOIN br.utentiregistrati u ON v.utente_id = u.id WHERE v.libro_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Costruzione dell'oggetto Valutazione mappando le colonne del DB
                Valutazione v = new Valutazione(
                    rs.getInt("stile"), rs.getInt("contenuto"), rs.getInt("gradevolezza"),
                    rs.getInt("originalita"), rs.getInt("edizione"), rs.getInt("voto_finale"),
                    rs.getString("commento_stile"), rs.getString("commento_contenuto"), 
                    rs.getString("commento_gradevolezza"), rs.getString("commento_originalita"), 
                    rs.getString("commento_edizione"), rs.getString("commento_generale")
                );
                
                // Concatenazione nome e cognome per l'autore della recensione
                v.setUtenteNome(rs.getString("nome") + " " + rs.getString("cognome"));
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Calcola la media matematica del campo 'voto_finale' per un libro.
     * Sfrutta la funzione aggregata AVG del database per efficienza.
     */
    @Override
    public double calcolaMediaValutazioni(int libroId) throws Exception {
        String sql = "SELECT AVG(voto_finale) as media FROM br.ValutazioniLibri WHERE libro_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();
            
            // Se non ci sono valutazioni, AVG restituisce null/0
            return rs.next() ? rs.getDouble("media") : 0.0;
        }
    }
}