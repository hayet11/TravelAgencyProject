package Services.Impl;

import Entities.VoyageOrganise;
import Services.IVoyageOrganiseService;
import Utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoyageOrganiseImpl implements IVoyageOrganiseService {

    private final Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public VoyageOrganiseImpl() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du Statement : " + e.getMessage());
        }
    }

    @Override
    public void ajouterReservation(VoyageOrganise voyageOrganise) throws SQLException {

    }


    @Override
    public void ajouter(VoyageOrganise voyageOrganise) throws SQLException {
        StringBuilder pointInteret = new StringBuilder();
        StringBuilder itineraire = new StringBuilder();
        if (voyageOrganise.getItineraires() != null && voyageOrganise.getItineraires().size() > 0)
            for (String iteniraire : voyageOrganise.getItineraires()) {
                itineraire.append(iteniraire).append("/");
            }
        if (voyageOrganise.getPointsIneret() != null && voyageOrganise.getPointsIneret().size() > 0)
            for (String point : voyageOrganise.getPointsIneret()) {
                pointInteret.append(point).append("/");
            }
        String req = "INSERT INTO `voyageorganise` (`itineraires`, `dateDepart`, `pointsInteret`, `guideDisponible`,`description`,`Tarif`,`NBPlace`) " +
                "VALUES ( ?, ?, ?, ?,?,?,?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, itineraire.toString());
        if (voyageOrganise.getDateDepart() != null) {
            pre.setDate(2, new java.sql.Date(voyageOrganise.getDateDepart().getTime()));
        } else {
            pre.setDate(2, null);
        }
        pre.setString(3, pointInteret.toString());
        pre.setBoolean(4, voyageOrganise.isGuideDisponible());
        pre.setString(5, voyageOrganise.getDescription());
        pre.setFloat(6, voyageOrganise.getTarif());
        pre.setFloat(7, voyageOrganise.getNBPlacDisponible());
        pre.executeUpdate();
        System.out.println("Voyage ajouté avec succès !");
    }

    @Override
    public void supprimer(VoyageOrganise voyageOrganise) throws SQLException {
        String req = "DELETE FROM `voyageorganise` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, voyageOrganise.getId());
        pre.executeUpdate();
        System.out.println("Voyage supprimé avec succès !");
    }

    @Override
    public void update(VoyageOrganise voyageOrganise) throws SQLException {
        StringBuilder pointInteret = new StringBuilder();
        StringBuilder itineraire = new StringBuilder();

        if (voyageOrganise.getItineraires() != null && voyageOrganise.getItineraires().size() > 0)
            for (String iteniraire : voyageOrganise.getItineraires()) {
                itineraire.append(iteniraire).append("/");
            }

        if (voyageOrganise.getPointsIneret() != null && voyageOrganise.getPointsIneret().size() > 0)
            for (String point : voyageOrganise.getPointsIneret()) {
                pointInteret.append(point).append("/");
            }

        System.out.println("voyage" + voyageOrganise);

        String req = "UPDATE `voyageorganise` SET `itineraires` = ?, `dateDepart` = ?, `pointsInteret` = ?, `guideDisponible` = ?, `description` = ?, `Tarif` = ?, `NBPlace` = ? WHERE `id` = ?";

        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, itineraire.toString());

        if (voyageOrganise.getDateDepart() != null) {
            pre.setDate(2, new java.sql.Date(voyageOrganise.getDateDepart().getTime()));
        } else {
            pre.setDate(2, null);
        }

        pre.setString(3, pointInteret.toString());
        pre.setBoolean(4, voyageOrganise.isGuideDisponible());
        pre.setString(5, voyageOrganise.getDescription());
        pre.setFloat(6, voyageOrganise.getTarif());
        pre.setInt(7, voyageOrganise.getNBPlacDisponible());
        pre.setInt(8, voyageOrganise.getId()); // This was moved to the last parameter to match the SQL query

        pre.executeUpdate();
        System.out.println("Voyage mis à jour avec succès !");
    }

    @Override
    public VoyageOrganise getById(int id) throws SQLException {
        String req = "SELECT * FROM `voyageorganise` WHERE `id` = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();
        if (rs.next()) {
            VoyageOrganise v = new VoyageOrganise();
            v.setId(rs.getInt("id"));
            v.setDateDepart(rs.getDate("dateDepart"));
            v.setGuideDisponible(rs.getBoolean("guideDisponible"));
            v.setItineraires(Arrays.stream(rs.getString("itineraires").split("/")).toList());
            v.setPointsIneret(Arrays.stream(rs.getString("pointsInteret").split("/")).toList());
            v.setDescription(rs.getString("description"));
            v.setTarif(rs.getFloat("Tarif"));
            v.setNBPlacDisponible(rs.getInt("NBPlace"));
            return v;
        }
        return null;
    }

    @Override
    public List<VoyageOrganise> getAll() throws SQLException {
        List<VoyageOrganise> vols = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `voyageorganise` where `NBPlace`>0 ");
        while (rs.next()) {
            VoyageOrganise v = new VoyageOrganise();
            v.setId(rs.getInt("id"));
            v.setDateDepart(rs.getDate("dateDepart"));
            v.setGuideDisponible(rs.getBoolean("guideDisponible"));
            v.setItineraires(Arrays.stream(rs.getString("itineraires").split("/")).toList());
            v.setPointsIneret(Arrays.stream(rs.getString("pointsInteret").split("/")).toList());
            v.setDescription(rs.getString("description"));
            v.setTarif(rs.getFloat("Tarif"));
            v.setNBPlacDisponible(rs.getInt("NBPlace"));
            vols.add(v);
        }
        return vols;
    }

    @Override
    public List<VoyageOrganise> getByDate(LocalDate date) throws SQLException {
        System.out.println(date);
        List<VoyageOrganise> voyages = new ArrayList<>();
        if (date == null) {
            return getAll();
        } else {
            String req = "SELECT * FROM `voyageorganise` WHERE `dateDepart` = ?";
            PreparedStatement pre = con.prepareStatement(req);
            pre.setDate(1, Date.valueOf(date));
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                VoyageOrganise v = new VoyageOrganise();
                v.setId(rs.getInt("id"));
                v.setDateDepart(rs.getDate("dateDepart"));
                v.setGuideDisponible(rs.getBoolean("guideDisponible"));
                v.setItineraires(Arrays.stream(rs.getString("itineraires").split("/")).toList());
                v.setPointsIneret(Arrays.stream(rs.getString("pointsInteret").split("/")).toList());
                v.setDescription(rs.getString("description"));
                v.setTarif(rs.getFloat("Tarif"));
                v.setNBPlacDisponible(rs.getInt("NBPlace"));
                voyages.add(v);
            }
        }
        return voyages;
    }

    @Override
    public int StatsOrganisedTripToDay() throws SQLException {
        LocalDate today = LocalDate.now();
        String req = "SELECT COUNT(*) AS NB FROM `reservation` WHERE `date`=? and `typeOffre` = ?";
        int nbTrips = 0;

        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setDate(1, Date.valueOf(today));
            pre.setString(2, "VoyageOrganise");

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    nbTrips = rs.getInt("NB");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du nombre de voyages organisés", e);
        }

        return nbTrips;
    }


    @Override
    public int StatsOrganisedTripGeneral() throws SQLException {
        String req = "SELECT COUNT(*) AS NB FROM `reservation` WHERE `typeOffre` = ?";
        int nbTrips = 0;

        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, "VoyageOrganise");

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    nbTrips = rs.getInt("NB");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du nombre de voyages organisés", e);
        }

        return nbTrips;
    }

    @Override
    public float SumOrganisedTrip() throws SQLException {
        String req = "SELECT SUM(tarif) AS SUM FROM `voyageorganise`";
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

        return sum += sum * 0.5;
    }


    public int ajouterEtRecupererId(VoyageOrganise voyageOrganise) throws SQLException {
        StringBuilder pointInteret = new StringBuilder();
        StringBuilder itineraire = new StringBuilder();

        if (voyageOrganise.getItineraires() != null && !voyageOrganise.getItineraires().isEmpty()) {
            for (String itineraireStr : voyageOrganise.getItineraires()) {
                itineraire.append(itineraireStr).append("/");
            }
        }
        if (voyageOrganise.getPointsIneret() != null && !voyageOrganise.getPointsIneret().isEmpty()) {
            for (String point : voyageOrganise.getPointsIneret()) {
                pointInteret.append(point).append("/");
            }
        }

        String req = "INSERT INTO `voyageorganise` (`itineraires`, `dateDepart`, `pointsInteret`, `guideDisponible`, `description`, `Tarif`, `NBPlace`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Demande à JDBC de retourner les clés générées
        PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        pre.setString(1, itineraire.toString());
        if (voyageOrganise.getDateDepart() != null) {
            pre.setDate(2, new java.sql.Date(voyageOrganise.getDateDepart().getTime()));
        } else {
            pre.setDate(2, null);
        }
        pre.setString(3, pointInteret.toString());
        pre.setBoolean(4, voyageOrganise.isGuideDisponible());
        pre.setString(5, voyageOrganise.getDescription());
        pre.setFloat(6, voyageOrganise.getTarif());
        pre.setFloat(7, voyageOrganise.getNBPlacDisponible());

        pre.executeUpdate();

        // Récupère l'ID généré
        ResultSet rs = pre.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        System.out.println("Voyage ajouté avec succès, ID : " + id);
        return id;
    }


}
