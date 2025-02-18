package Services.Impl;

import Entities.Vol;
import Services.IVolServices;
import Utils.DataSource;
import enums.TypeVol;
import java.sql.*;
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
        String req = "INSERT INTO `vol` (`description`, `tarif`, `type`, `compagnieDepart`, `aeroportDepart`, `aeroportArrivee`, `heureDepart`, `heureArrivee`, `dateArrivee`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, vol.getDescription());
        pre.setFloat(2, vol.getTarif());
        pre.setString(3, vol.getType().getValue());
        pre.setString(4, vol.getCompagnie());
        pre.setString(5, vol.getAeroportDepart());
        pre.setString(6, vol.getAeroportArrivee());
        pre.setTimestamp(7, new Timestamp(vol.getHeureDepart().getTime()));
        pre.setTimestamp(8, new Timestamp(vol.getHeureArrivee().getTime()));
        pre.setDate(9, (Date) vol.getDateArrivee());
        pre.executeUpdate();
    }


    public void supprimer(Vol vol) throws SQLException {
        String req = "DELETE FROM `vol` WHERE `numeroVol` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, vol.getId());
        pre.executeUpdate();
    }

    public void update(Vol vol) throws SQLException {
        System.out.println(
                "Valeurs envoyées : " + vol.getDescription() + ", " + vol.getTarif() + ", " + vol.getType().toString()
                        + ", " + vol.getCompagnie() + ", " + vol.getAeroportDepart() + ", " + vol.getAeroportArrivee()
                        + ", " + vol.getHeureDepart() + ", " + vol.getHeureArrivee() + ", " + vol.getDateArrivee());

        String req = "UPDATE `vol` SET `description`=?, `tarif`=?, `type`=?, `compagnieDepart`=?, `aeroportDepart`=?, `aeroportArrivee`=?, `heureDepart`=?, `heureArrivee`=?, `dateArrivee`=? WHERE `numeroVol`= ?";

        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, vol.getDescription());
            pre.setFloat(2, vol.getTarif());
            pre.setString(3, vol.getType().toString());
            pre.setString(4, vol.getCompagnie());
            pre.setString(5, vol.getAeroportDepart());
            pre.setString(6, vol.getAeroportArrivee());
            pre.setTimestamp(7, new Timestamp(vol.getHeureDepart().getTime()));
            pre.setTimestamp(8, new Timestamp(vol.getHeureArrivee().getTime()));

            if (vol.getDateArrivee() != null) {
                pre.setDate(9, new Date(vol.getDateArrivee().getTime()));
            } else {
                pre.setNull(9, Types.DATE);
            }

            pre.setInt(10, vol.getId());

            int rowsUpdated = pre.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Vol mis à jour avec succès !");
            } else {
                System.out.println("Aucune ligne mise à jour (ID introuvable ?)");
            }
        }
    }

    @Override
    public Vol getById(int id) throws SQLException {
        return null;
    }

    public List<Vol> getByAeroportArrivee(String aeroportArrivee) throws SQLException {
        String req = "SELECT * FROM `vol` WHERE `aeroportArrivee` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, aeroportArrivee);  // On passe l'aéroport d'arrivée comme paramètre
        ResultSet rs = pre.executeQuery();

        List<Vol> vols = new ArrayList<>();
        while (rs.next()) {
            Vol v = new Vol();
            v.setId(rs.getInt("numeroVol"));
            v.setDescription(rs.getString("description"));
            v.setTarif(rs.getFloat("tarif"));
            v.setType(TypeVol.valueOf(rs.getString("type").toLowerCase()));
            v.setCompagnie(rs.getString("compagnieDepart"));
            v.setAeroportDepart(rs.getString("aeroportDepart"));
            v.setAeroportArrivee(rs.getString("aeroportArrivee"));
            v.setHeureDepart(rs.getTime("heureDepart"));
            v.setHeureArrivee(rs.getTime("heureArrivee"));
            v.setDateArrivee(rs.getDate("dateArrivee"));

            vols.add(v);
        }

        return vols;
    }


    public List<Vol> getAll() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `vol`");
        while (rs.next()) {
            Vol v = new Vol();
            v.setId(rs.getInt("numeroVol"));
            v.setDescription(rs.getString("description"));
            v.setTarif(rs.getFloat("tarif"));
            v.setType(TypeVol.valueOf(rs.getString("type").toLowerCase()));
            v.setCompagnie(rs.getString("compagnieDepart"));
            v.setAeroportDepart(rs.getString("aeroportDepart"));
            v.setAeroportArrivee(rs.getString("aeroportArrivee"));
            v.setHeureDepart(rs.getTime("heureDepart"));
            v.setHeureArrivee(rs.getTime("heureArrivee"));
            v.setDateArrivee((rs.getDate("dateArrivee")));
            ;

            vols.add(v);
        }
        return vols;
    }

    @Override
    public int StatsFlightToDay() throws SQLException {
        return 0;
    }

    @Override
    public int StatsFlightGneral() throws SQLException {
        return 0;
    }

    @Override
    public float SumFlights() throws SQLException {
        return 0;
    }
}