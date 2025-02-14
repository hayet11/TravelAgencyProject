package Services.Impl;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Entities.Utilisateur;
import Utils.DataSource;
import Entities.Role;



public class UtilisateurServiceImpl {
    private Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public UtilisateurServiceImpl() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du Statement : " + e.getMessage());
        }
    }
    public boolean ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO users (nom, prenom, mot_de_passe, date_naissance, telephone, email, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getMotDePasse()); // ⚠ Pense à sécuriser le mot de passe
            pstmt.setDate(4, Date.valueOf(utilisateur.getDateNaissance()));
            pstmt.setString(5, utilisateur.getTelephone());
            pstmt.setString(6, utilisateur.getEmail());
            pstmt.setString(7, utilisateur.getRole().name()); // Enum en String

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Retourne true si l'insertion a réussi
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false si une erreur s'est produite
        }
    }


    // ➤ Supprimer un utilisateur par ID
    public void supprimerUtilisateur(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Utilisateur supprimé !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Mettre à jour un utilisateur
    public void modifierUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE users SET nom = ?, prenom = ?, mot_de_passe = ?, date_naissance = ?, telephone = ?, email = ?, role = ? WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getMotDePasse()); // À sécuriser avec BCrypt
            pstmt.setDate(4, Date.valueOf(utilisateur.getDateNaissance()));
            pstmt.setString(5, utilisateur.getTelephone());
            pstmt.setString(6, utilisateur.getEmail());
            pstmt.setString(7, utilisateur.getRole().name());
            pstmt.setInt(8, utilisateur.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Utilisateur mis à jour !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Récupérer un utilisateur par ID
    public Utilisateur getUtilisateurById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Utilisateur utilisateur = null;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mot_de_passe"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    // ➤ Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mot_de_passe"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    // ➤ Fonction Se Connecter
    public Utilisateur seConnecter(String email, String motDePasse) {
        String sql = "SELECT * FROM users WHERE email = ? AND mot_de_passe = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, motDePasse);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mot_de_passe"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si l'utilisateur n'existe pas
    }


    // ➤ Vérifier si un email existe déjà
    public  boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si > 0, l'email existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ➤ Fonction S'inscrire
    public boolean sInscrire(Utilisateur utilisateur) {
        if (emailExiste(utilisateur.getEmail())) {
            System.out.println("⚠️ Cet email est déjà utilisé !");
            return false;
        }
        return ajouterUtilisateur(utilisateur); // Réutilisation de la méthode ajouter()
    }





}

