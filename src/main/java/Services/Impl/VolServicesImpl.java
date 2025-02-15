package Services.Impl;

import Entities.Vol;
import Services.IVolServices;
import Utils.DataSource;
import enums.TypeVol;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VolServicesImpl implements IVolServices {

    private final Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public VolServicesImpl() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du Statement : " + e.getMessage());
        }
    }

    public void ajouter(Vol vol) throws SQLException {
        String req = "INSERT INTO `vol` (`id`, `description`, `tarif`, `type`, `compagnieDepart`, `aeroportDepart`, `aeroportArrivee`,`heureDepart`,`heureArrivee`,`dateArrivee`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, vol.getId());
        pre.setString(2, vol.getDescription());
        pre.setFloat(3, vol.getTarif());
        pre.setString(4, vol.getType().toString());
        pre.setString(5, vol.getCompagnie());
        pre.setString(6, vol.getAeroportDepart());
        pre.setString(7, vol.getAeroportArrivee());
        pre.setTimestamp(8, new java.sql.Timestamp(vol.getHeureDepart().getTime()));
        pre.setTimestamp(9, new java.sql.Timestamp(vol.getHeureArrivee().getTime()));
        pre.setDate(10, new java.sql.Date(vol.getHeureArrivee().getTime()));
        pre.executeUpdate();
        System.out.println("Vol ajouté avec succès !");
    }

    public void supprimer(Vol vol) throws SQLException {
        String req = "DELETE FROM `vol` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, vol.getId());
        pre.executeUpdate();
        System.out.println("Vol supprimé avec succès !");
    }

    public void update(Vol vol) throws SQLException {
        String req = "UPDATE `vol` SET `description`=?, `tarif`=?, `type`=?, `compagnieDepart`=?, `aeroportDepart`=?, `aeroportArrivee`=?,`heureDepart`=?,`heureArrivee`=?,`dateArrivee`=?" +
                "WHERE `id`= ?;" ;

        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, vol.getDescription());
        pre.setFloat(2, vol.getTarif());
        pre.setString(3, vol.getType().toString());
        pre.setString(4, vol.getCompagnie());
        pre.setString(5, vol.getAeroportDepart());
        pre.setString(6, vol.getAeroportArrivee());
        pre.setTimestamp(7, new java.sql.Timestamp(vol.getHeureDepart().getTime()));
        pre.setTimestamp(8, new java.sql.Timestamp(vol.getHeureArrivee().getTime()));
        pre.setDate(9, new java.sql.Date(vol.getHeureArrivee().getTime()));
        pre.setInt(10, vol.getId());

        pre.executeUpdate();
        System.out.println("Vol mis à jour avec succès !");
    }

    @Override
    public Vol getById(int id) throws SQLException {
        String req = "SELECT * FROM `vol` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();
        if (rs.next()) {
            Vol v= new Vol();
                    v.setId(rs.getInt("id"));
                    v.setDescription(rs.getString("description"));
                    v.setTarif(rs.getFloat("tarif"));
                    v.setType(TypeVol.valueOf(rs.getString("type").toLowerCase()));
                    v.setCompagnie(rs.getString("compagnieDepart"));
                    v.setAeroportDepart(rs.getString("aeroportDepart"));
                    v.setAeroportArrivee(rs.getString("aeroportArrivee"));
                    v.setHeureDepart(rs.getDate("heureDepart"));
                    v.setHeureArrivee(rs.getDate("heureArrivee"));
            v.setHeureArrivee(rs.getDate("heureArrivee"));
            v.setDateArrivee(rs.getDate("dateArrivee"));

            return  v;
        }
        return null;
    }

    @Override
    public List<Vol> getAll() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `vol`");
        while (rs.next()) {
            Vol v= new Vol();
            v.setId(rs.getInt("id"));
            v.setDescription(rs.getString("description"));
            v.setTarif(rs.getFloat("tarif"));
            v.setType(TypeVol.valueOf(rs.getString("type").toLowerCase()));
            v.setCompagnie(rs.getString("compagnieDepart"));
            v.setAeroportDepart(rs.getString("aeroportDepart"));
            v.setAeroportArrivee(rs.getString("aeroportArrivee"));
            v.setHeureDepart(rs.getDate("heureDepart"));
            v.setHeureArrivee(rs.getDate("heureArrivee"));
            vols.add(v);
        }
        return vols;
    }
    public int StatsFlightToDay() throws SQLException {
        LocalDate today = LocalDate.now();
        String req = "SELECT count(*) AS NB FROM `reservation` where `date`=? and `typeOffre`=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setDate(1,Date.valueOf(today));
        pre.setString(2,"Vol");
        ResultSet rs = pre.executeQuery();
        if (rs.next()) {
            return rs.getInt("NB");
        }
        return 0;    }

    @Override
    public int StatsFlightGneral() throws SQLException {
        String req = "SELECT count(*) AS NB FROM `reservation` WHERE `typeOffre` = ?";
        int nbFlights = 0;

        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, "Vol");

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    nbFlights = rs.getInt("NB");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du nombre de vols", e);
        }

        return nbFlights;
    }

    @Override
    public float SumFlights() throws SQLException {
        String req = "SELECT SUM(tarif) AS SUM FROM `vol`";
        int sum = 0;

        try (PreparedStatement pre = con.prepareStatement(req)) {
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    sum = rs.getInt("SUM");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du nombre de voyages organisés", e);
        }

        return sum+=sum*0.5;    }
}
