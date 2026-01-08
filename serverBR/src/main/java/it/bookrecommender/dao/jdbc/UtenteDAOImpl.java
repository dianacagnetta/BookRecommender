package it.bookrecommender.dao.jdbc;

import it.bookrecommender.dao.interfaces.IUtenteDAO;
import it.bookrecommender.model.*;
import it.bookrecommender.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione JDBC dell'interfaccia {@link IUtenteDAO}.
 * Questa classe funge da hub principale per tutte le operazioni di persistenza
 * del sistema BookRecommender, gestendo l'autenticazione, il catalogo libri,
 * le valutazioni e le librerie personali.
 * @author Cagnetta Diana, 757000, VA
 * @author Romano Aurora Lindangela, 757787, VA
 * @author Caliaro Fabio, 756230, VA
 * @author Fedeli Riccardo, 758019, VA
 */
public class UtenteDAOImpl implements IUtenteDAO {
	 /**
	  * Costruttore predefinito della classe.
	  */
	public UtenteDAOImpl() {}
    /**
     * Esegue l'autenticazione dell'utente verificando lo userid e l'hash della password.
     * @return Oggetto {@link Utente} se le credenziali sono corrette, {@code null} altrimenti.
     */
    @Override
    public Utente login(String userid, String passwordHash) throws Exception {
        String sql = "SELECT * FROM br.UtentiRegistrati WHERE userid = ? AND password_hash = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            // Se troviamo una corrispondenza, costruiamo l'oggetto Utente con i dati del DB
            if (rs.next()) {
                return new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codice_fiscale"),
                        rs.getString("email"),
                        rs.getString("userid"),
                        rs.getString("password_hash")
                );
            }
            return null; // Credenziali errate
        }
    }

    /**
     * Inserisce un nuovo utente nel database (Registrazione).
     * @return {@code true} se l'operazione ha successo.
     */
    @Override
    public boolean registra(Utente u) throws Exception {
        String sql = "INSERT INTO br.UtentiRegistrati " +
                     "(nome, cognome, codice_fiscale, email, userid, password_hash) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getCognome());
            ps.setString(3, u.getCodiceFiscale());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getUserid());
            ps.setString(6, u.getPasswordHash());
            ps.executeUpdate();

            return true;
        }
    }

    /**
     * Esegue una ricerca flessibile nel catalogo libri.
     * Gestisce ricerche parziali (LIKE) e parametri opzionali (titolo, autore, anno).
     */
    @Override
    public List<Libro> searchBooks(String titolo, String autore, Integer anno) throws Exception {
        List<Libro> lista = new ArrayList<>();

        // La query usa LOWER per rendere la ricerca case-insensitive
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

            // Gestione dell'anno come intero opzionale
            if (anno != null) {
                ps.setInt(5, anno);
                ps.setInt(6, anno);
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ResultSet rs = ps.executeQuery();
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

    /**
     * Recupera l'elenco completo dei libri presenti nel catalogo.
     */
    @Override
    public List<Libro> getBooks() throws Exception {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT id, titolo, autori, anno_pubblicazione, editore, categoria FROM br.libri";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    /**
     * Ottiene tutte le valutazioni per un libro, includendo il nome dell'utente autore.
     */
    @Override
    public List<Valutazione> getValutazioniLibro(int libroId) throws Exception {
        List<Valutazione> lista = new ArrayList<>();
        // LEFT JOIN per associare il nome dell'utente alla valutazione
        String sql = "SELECT v.*, u.nome, u.cognome FROM br.ValutazioniLibri v " +
                     "LEFT JOIN br.utentiregistrati u ON v.utente_id = u.id WHERE v.libro_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Valutazione v = new Valutazione(
                    rs.getInt("stile"), rs.getInt("contenuto"), rs.getInt("gradevolezza"),
                    rs.getInt("originalita"), rs.getInt("edizione"), rs.getInt("voto_finale"),
                    rs.getString("commento_stile"), rs.getString("commento_contenuto"),
                    rs.getString("commento_gradevolezza"), rs.getString("commento_originalita"),
                    rs.getString("commento_edizione"), rs.getString("commento_generale")
                );
                
                // Formattazione del nome del recensore
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                if (nome != null || cognome != null) {
                    v.setUtenteNome(((nome==null)?"":nome) + " " + ((cognome==null)?"":cognome));
                }
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Recupera i libri consigliati per un libro target, aggregando i nomi di chi li ha suggeriti.
     */
    @Override
    public List<LibroConsigliato> getConsigli(int libroId) throws Exception {
        List<LibroConsigliato> lista = new ArrayList<>();
        // STRING_AGG concatena i nomi degli utenti suggeritori in un'unica stringa
        String sql = "SELECT l2.titolo, COUNT(*) AS volte, STRING_AGG(u.nome || ' ' || u.cognome, ', ') AS suggeritori " +
                     "FROM br.ConsigliLibri c JOIN br.Libri l2 ON c.suggerito_libro_id = l2.id " +
                     "LEFT JOIN br.utentiregistrati u ON c.utente_id = u.id WHERE c.libro_id = ? GROUP BY l2.titolo";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LibroConsigliato lc = new LibroConsigliato(rs.getString("titolo"), rs.getInt("volte"));
                lc.setSuggeritori(rs.getString("suggeritori"));
                lista.add(lc);
            }
        }
        return lista;
    }

    /**
     * Ricerca un singolo libro tramite il suo ID.
     */
    @Override
    public Libro getBookById(int libroId) throws Exception {
        String sql = "SELECT * FROM br.libri WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Libro(rs.getInt("id"), rs.getString("titolo"), rs.getString("autori"),
                        rs.getInt("anno_pubblicazione"), rs.getString("editore"), rs.getString("categoria"));
            }
        }
        return null;
    }

    /**
     * Inserisce una valutazione dettagliata (voti e commenti) per un libro.
     */
    @Override
    public void aggiungiValutazione(int utenteId, int libroId, int stile, int contenuto, int gradevolezza, 
                                    int originalita, int edizione, int votoFinale, String commentoStile, 
                                    String commentoContenuto, String commentoGradevolezza, String commentoOriginalita, 
                                    String commentoEdizione, String commentoGenerale) throws Exception {
        String sql = "INSERT INTO br.valutazionilibri (utente_id, libro_id, stile, contenuto, gradevolezza, originalita, edizione, voto_finale, " +
                     "commento_stile, commento_contenuto, commento_gradevolezza, commento_originalita, commento_edizione, commento_generale) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utenteId); ps.setInt(2, libroId); ps.setInt(3, stile); ps.setInt(4, contenuto);
            ps.setInt(5, gradevolezza); ps.setInt(6, originalita); ps.setInt(7, edizione); ps.setInt(8, votoFinale);
            ps.setString(9, commentoStile); ps.setString(10, commentoContenuto); ps.setString(11, commentoGradevolezza);
            ps.setString(12, commentoOriginalita); ps.setString(13, commentoEdizione); ps.setString(14, commentoGenerale);
            ps.executeUpdate();
        }
    }

    /**
     * Recupera l'elenco delle librerie create da un utente specifico.
     */
    @Override
    public List<Libreria> getLibrerieUtente(int utenteId) throws Exception {
        List<Libreria> lista = new ArrayList<>();
        String sql = "SELECT * FROM br.librerie WHERE utente_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utenteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Libreria(rs.getInt("id"), rs.getInt("utente_id"), rs.getString("nome")));
            }
        }
        return lista;
    }

    /**
     * Crea una nuova libreria (collezione) per un utente.
     */
    @Override
    public void creaLibreria(int utenteId, String nome) throws Exception {
        String sql = "INSERT INTO br.librerie(utente_id, nome) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utenteId); ps.setString(2, nome);
            ps.executeUpdate();
        }
    }

    /**
     * Aggiunge un libro a una libreria specifica (tabella di giunzione).
     */
    @Override
    public void addLibroAllaLibreria(int libreriaId, int libroId) throws Exception {
        String sql = "INSERT INTO br.librerielibri(libreria_id, libro_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, libreriaId); ps.setInt(2, libroId);
            ps.executeUpdate();
        }
    }

    /**
     * Recupera tutti i libri contenuti in una determinata libreria personale.
     */
    @Override
    public List<Libro> getLibriInLibreria(int libreriaId) throws Exception {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT l.* FROM br.librerielibri ll JOIN br.libri l ON ll.libro_id = l.id WHERE ll.libreria_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, libreriaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Libro(rs.getInt("id"), rs.getString("titolo"), rs.getString("autori"),
                        rs.getInt("anno_pubblicazione"), rs.getString("editore"), rs.getString("categoria")));
            }
        }
        return lista;
    }

    /**
     * Registra un consiglio (associazione tra due libri) fornito da un utente.
     * LIMITE: Un utente può consigliare massimo 3 libri per ogni libro.
     */
    @Override
    public void addConsiglio(int utenteId, int libroId, int suggeritoId) throws Exception {
        // Query per contare i consigli già inseriti dall'utente per questo libro
        String sqlCount = "SELECT COUNT(*) as conteggio FROM br.ConsigliLibri WHERE utente_id = ? AND libro_id = ?";
        // Query per recuperare il titolo del libro target
        String sqlTitolo = "SELECT titolo FROM br.Libri WHERE id = ?";
        String sql = "INSERT INTO br.ConsigliLibri(utente_id, libro_id, suggerito_libro_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Verifico il limite di 3 consigli
            int conteggioConsigli = 0;
            try (PreparedStatement psCount = conn.prepareStatement(sqlCount)) {
                psCount.setInt(1, utenteId);
                psCount.setInt(2, libroId);
                ResultSet rsCount = psCount.executeQuery();
                if (rsCount.next()) {
                    conteggioConsigli = rsCount.getInt("conteggio");
                }
            }
            
            // 2. Controllo: se ha già 3 consigli, blocco l'inserimento
            if (conteggioConsigli >= 3) {
                String titoloLibro = "";
                try (PreparedStatement psTitolo = conn.prepareStatement(sqlTitolo)) {
                    psTitolo.setInt(1, libroId);
                    ResultSet rsTitolo = psTitolo.executeQuery();
                    if (rsTitolo.next()) {
                        titoloLibro = rsTitolo.getString("titolo");
                    }
                }
                throw new Exception("Hai raggiunto il limite di 3 consigli per: " + titoloLibro);
            }
            
            // 3. Se il limite non è stato raggiunto, inserisco il consiglio
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, utenteId); ps.setInt(2, libroId); ps.setInt(3, suggeritoId);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Rimuove i consigli inseriti da un utente per un determinato libro.
     */
    @Override
    public void clearConsigli(int utenteId, int libroId) throws Exception {
        String sql = "DELETE FROM br.ConsigliLibri WHERE utente_id = ? AND libro_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utenteId); ps.setInt(2, libroId);
            ps.executeUpdate();
        }
    }
}