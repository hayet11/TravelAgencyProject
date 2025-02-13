package Services.Impl;

import Entities.Aide;
import Services.IAideService;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AideServiceImpl implements IAideService {

    private Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public AideServiceImpl() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du Statement : " + e.getMessage());
        }
    }

    @Override
    public void ajouter(Aide aide) throws SQLException {
        String req = "INSERT INTO `aide` (`question`, `response`) VALUES (?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, aide.getQuestion());
        pre.setString(2, aide.getReponse());
        pre.executeUpdate();
        System.out.println("Aide ajoutée avec succès !");
    }

    @Override
    public void supprimer(Aide aide) throws SQLException {
        String req = "DELETE FROM `aide` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, aide.getId());
        pre.executeUpdate();
        System.out.println("Aide supprimée avec succès !");
    }

    @Override
    public void update(Aide aide) throws SQLException {
        String req = "UPDATE `aide` SET `question` = ?, `response` = ? WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, aide.getQuestion());
        pre.setString(2, aide.getReponse());
        pre.setInt(3, aide.getId());
        pre.executeUpdate();
        System.out.println("Aide mise à jour avec succès !");
    }

    @Override
    public Aide getById(int id) throws SQLException {
        String req = "SELECT * FROM `aide` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();
        if (rs.next()) {
            return new Aide(
                    rs.getInt("id"),
                    rs.getString("question"),
                    rs.getString("response")
            );
        }
        return null;
    }

    @Override
    public List<Aide> getAll() throws SQLException {
        List<Aide> aides = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `aide`");
        while (rs.next()) {
            aides.add(new Aide(
                    rs.getInt("id"),
                    rs.getString("question"),
                    rs.getString("response")
            ));
        }
        return aides;
    }

    @Override
    public List<Aide> searchAideByQuestion(String keyword) throws SQLException {
        List<Aide> aides = new ArrayList<>();
        String req = "SELECT * FROM `aide` WHERE `question` LIKE ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, "%" + keyword + "%");
        ResultSet rs = pre.executeQuery();
        while (rs.next()) {
            aides.add(new Aide(
                    rs.getInt("id"),
                    rs.getString("question"),
                    rs.getString("response")
            ));
        }
        return aides;
    }
}