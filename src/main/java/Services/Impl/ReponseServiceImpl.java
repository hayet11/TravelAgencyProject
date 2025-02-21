package service;

import Entities.Reponse;
import Entities.Reclamation;
import Services.IReponseService;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseServiceImpl implements IReponseService {
    private final Connection conn;

    public ReponseServiceImpl() {
        conn = DataSource.getInstance().getConn();
    }

    @Override
    public void ajouter(Reponse reponse) throws SQLException {
        String query = "INSERT INTO reponse (content) VALUES (?)";
        String updateReclamationQuery = "UPDATE reclamations SET reponse_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reponse.getContent());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                reponse.setId(generatedId);

                if (reponse.getReclamation() != null) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateReclamationQuery)) {
                        updateStmt.setInt(1, generatedId);
                        updateStmt.setInt(2, reponse.getReclamation().getId());
                        updateStmt.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void supprimer(Reponse reponse) throws SQLException {
        String query = "DELETE FROM reponse WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reponse.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Reponse reponse) throws SQLException {
        String query = "UPDATE reponse SET content = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, reponse.getContent());
            stmt.setInt(2, reponse.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Reponse> getAll() throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse";
        try (Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                reponses.add(new Reponse(resultSet.getInt("id"), resultSet.getString("content")));
            }
        }
        return reponses;
    }

    @Override
    public Reponse getById(int id) throws SQLException {
        String query = "SELECT * FROM reponse WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new Reponse(resultSet.getInt("id"), resultSet.getString("content"));
            }
        }
        return null;
    }

    @Override
    public Reponse readByReclamationId(int reclamationId) throws SQLException {
        String query = "SELECT * FROM reponse WHERE id = (SELECT reponse_id FROM reclamations WHERE id = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reclamationId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new Reponse(resultSet.getInt("id"), resultSet.getString("content"));
            }
        }
        return null;
    }
}