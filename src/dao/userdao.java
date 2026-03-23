package dao;

import model.User;
import java.sql.*;

   // la partie userdao pour la Gestion des utilisateurs dans la base de données
  
public class UserDAO {

  // Attribut qui représente la connexion à la base de données. Privé car personne d'autre ne doit y accéder directement.
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // save new user in the DB method
  
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, email, password, role, adresse, telephone, actif) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole() != null ? user.getRole() : "CLIENT");
            stmt.setString(5, user.getAdresse());
            stmt.setString(6, user.getTelephone());
            stmt.setBoolean(7, user.isActif());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur save: " + e.getMessage());
        }
        return false;
    }

    // update user method 

    public boolean update(User user) {
        String sql = "UPDATE users SET username=?, email=?, password=?, role=?, adresse=?, telephone=?, actif=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getAdresse());
            stmt.setString(6, user.getTelephone());
            stmt.setBoolean(7, user.isActif());
            stmt.setInt(8, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur update: " + e.getMessage());
        }
        return false;
    }

    // delete user by his id 
  
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur delete: " + e.getMessage());
        }
        return false;
    }

    // find user by id
  
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur findById: " + e.getMessage());
        }
        return null;
    }

    // find user bu mail 
  
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur findByEmail: " + e.getMessage());
        }
        return null;
    }

    // Authentifie un utilisateur avec email + mot de passe,  Retourne l'objet User si succès, null sinon
     
    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM users WHERE email=? AND password=? AND actif=1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur authenticate: " + e.getMessage());
        }
        return null;
    }

    // Mappe un ResultSet vers un objet User
     
    private User mapResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setAdresse(rs.getString("adresse"));
        user.setTelephone(rs.getString("telephone"));
        user.setActif(rs.getBoolean("actif"));
        return user;
    }

    // Crée la table users si elle n'existe pas
    
    public void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT DEFAULT 'CLIENT',
                adresse TEXT,
                telephone TEXT,
                actif INTEGER DEFAULT 1
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur createTable: " + e.getMessage());
        }
    }
}
