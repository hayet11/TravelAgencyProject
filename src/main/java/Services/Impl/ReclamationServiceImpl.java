package service;

import Entities.Reclamation;
import Entities.Reponse;
import Entities.TypeReclamation; // Import the Enum
import Services.IService;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationServiceImpl implements IService<Reclamation> {
    private final Connection conn;

    public ReclamationServiceImpl() {
        conn = DataSource.getInstance().getConn();
    }

    @Override
    public void ajouter(Reclamation r) throws SQLException {
        String query = "INSERT INTO reclamation (email, type_reclamation, description, etat, reponse_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, r.getEmail());
            stmt.setString(2, r.getTypeReclamation().toString()); // Save Enum as String
            stmt.setString(3, r.getDescription());
            stmt.setString(4, r.getEtat());
            stmt.setObject(5, r.getReponse() != null ? r.getReponse().getId() : null, Types.INTEGER);
            stmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Reclamation reclamation) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reclamation.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET email = ?, type_reclamation = ?, description = ?, etat = ?, reponse_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, reclamation.getEmail());
            stmt.setString(2, reclamation.getTypeReclamation().toString()); // Save Enum as String
            stmt.setString(3, reclamation.getDescription());
            stmt.setString(4, reclamation.getEtat());
            stmt.setObject(5, reclamation.getReponse() != null ? reclamation.getReponse().getId() : null, Types.INTEGER);
            stmt.setInt(6, reclamation.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Reclamation> getAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        try (Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int reponseId = resultSet.getInt("reponse_id");
                Reponse reponse = (reponseId != 0) ? new Reponse(reponseId, null) : null;

                // Convert the String type_reclamation to the Enum
                TypeReclamation typeReclamation = TypeReclamation.fromString(resultSet.getString("type_reclamation"));

                reclamations.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        typeReclamation, // Use the Enum
                        resultSet.getString("description"),
                        resultSet.getString("etat"),
                        reponse
                ));
            }
        }
        return reclamations;
    }

    @Override
    public Reclamation getById(int id) throws SQLException {
        String query = "SELECT r.*, rp.content as reponse_content " +
                "FROM reclamation r " +
                "LEFT JOIN reponse rp ON r.reponse_id = rp.id " +
                "WHERE r.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int reponseId = resultSet.getInt("reponse_id");
                String reponseContent = resultSet.getString("reponse_content");
                Reponse reponse = (reponseId != 0) ? new Reponse(reponseId, reponseContent) : null;

                // Convert the String type_reclamation to the Enum
                TypeReclamation typeReclamation = TypeReclamation.fromString(resultSet.getString("type_reclamation"));

                return new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        typeReclamation, // Use the Enum
                        resultSet.getString("description"),
                        resultSet.getString("etat"),
                        reponse
                );
            }
        }
        return null;
    }
    public Reclamation getReclamationWithReponse(int id) throws SQLException {
        String query = "SELECT r.id AS reclamation_id, r.email, r.type_reclamation, r.description, r.etat, " +
                "rep.id AS reponse_id, rep.content " +
                "FROM reclamation r LEFT JOIN reponse rep ON r.reponse_id = rep.id " +
                "WHERE r.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Retrieve Reclamation data
                int reclamationId = resultSet.getInt("reclamation_id");
                String email = resultSet.getString("email");
                TypeReclamation typeReclamation = TypeReclamation.fromString(resultSet.getString("type_reclamation"));
                String description = resultSet.getString("description");
                String etat = resultSet.getString("etat");

                // Retrieve Reponse data
                int reponseId = resultSet.getInt("reponse_id");
                String content = resultSet.getString("content");
                Reponse reponse = null;
                if (reponseId != 0) {
                    reponse = new Reponse(reponseId, content);
                }

                // Create and return Reclamation object with Reponse
                return new Reclamation(reclamationId, email, typeReclamation, description, etat, reponse);
            }
        }
        return null;
    }

}