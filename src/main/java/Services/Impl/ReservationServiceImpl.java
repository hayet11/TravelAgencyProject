package Services.Impl;

import Entities.Reservation;
import Utils.DataSource;
import enums.ModePaiement;
import enums.TypeOffre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl {

    private final Connection con;
    private Statement st;

    public ReservationServiceImpl() {
        con = DataSource.getInstance().getConn();
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du Statement : " + e.getMessage());
        }
    }

    // Annuler une réservation
    public void annulerReservation(int reservationId) throws SQLException {
        String req = "DELETE FROM `reservation` WHERE `id` = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, reservationId);
            pre.executeUpdate();
            System.out.println("Réservation annulée avec succès !");
        }
    }

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM `reservation`";
        try (ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getDate("date_reservation"),
                        rs.getInt("nbre_participant"),
                        rs.getString("mode_paiement")
                ));
            }
        }
        return reservations;
    }

    public void ajouter(Reservation reservation) throws SQLException {
        String req = "INSERT INTO `Reservation` (`date`, `nbrParticipants`, `modePaiement`, `clientId`, `offreId`, `typeOffre`) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pre.setDate(1, new Date(reservation.getDate().getTime()));
            pre.setInt(2, reservation.getNbreParticipant());
            pre.setString(3, reservation.getModePaiement());
            pre.setInt(4, reservation.getClientId());
            pre.setInt(5, reservation.getId());
            pre.setString(6, reservation.getTypeOffre().toString());

            int rowsAffected = pre.executeUpdate();
            System.out.println("Réservation ajoutée avec succès ! Nombre de lignes affectées : " + rowsAffected);

            try (ResultSet generatedKeys = pre.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                    System.out.println("ID de la réservation générée : " + reservation.getId());
                }
            }
        }
    }

//    public List<Reservation> getByClientId(int id) throws SQLException {
//        String req = "SELECT * FROM `Reservation` WHERE `clientId` = ?";
//        List<Reservation> allReservations = new ArrayList<>();
//        try (PreparedStatement pre = con.prepareStatement(req)) {
//            pre.setInt(1, id);
//            try (ResultSet rs = pre.executeQuery()) {
//                while (rs.next()) {
//                    allReservations.add(new Reservation(
//                            rs.getInt("id"),
//                            rs.getDate("date"),
//                            rs.getInt("nbrParticipants"),
//                            rs.getString("modePaiement"),
//                            rs.getString("clientId"),
//                            rs.getInt("offreId"),
//                            rs.getString("typeOffre")
//                    );
//                }
//            }
//        }
//        return allReservations;
//    }
//
//    public void supprimer(Reservation reservation) throws SQLException {
//        String req = "DELETE FROM `Reservation` WHERE `id` = ?";
//        try (PreparedStatement pre = con.prepareStatement(req)) {
//            pre.setInt(1, reservation.getId());
//            int rowsAffected = pre.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Réservation supprimée avec succès !");
//            } else {
//                System.out.println("Aucune réservation trouvée avec cet ID.");
//            }
//        }
//    }
//
//    public void update(Reservation reservation) throws SQLException {
//        String req = "UPDATE `Reservation` SET `date` = ?, `nbrParticipants` = ?, `modePaiement` = ?, `clientId` = ?, `offreId` = ?, `typeOffre` = ? WHERE `id` = ?";
//        try (PreparedStatement pre = con.prepareStatement(req)) {
//            pre.setDate(1, new Date(reservation.getDate().getTime()));
//            pre.setInt(2, reservation.getNbrParticipants());
//            pre.setString(3, reservation.getModePaiement().getValue());
//            pre.setString(4, reservation.getClientId());
//            pre.setInt(5, reservation.getOffreId());
//            pre.setString(6, reservation.getTypeOffre().toString());
//            pre.setInt(7, reservation.getId());
//
//            int rowsAffected = pre.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Mise à jour réussie.");
//            } else {
//                System.out.println("Aucune réservation trouvée.");
//            }
//        }
//    }
//
//    public Reservation getById(int id) throws SQLException {
//        String req = "SELECT * FROM `Reservation` WHERE `id` = ?";
//        try (PreparedStatement pre = con.prepareStatement(req)) {
//            pre.setInt(1, id);
//            try (ResultSet rs = pre.executeQuery()) {
//                if (rs.next()) {
//                    return new Reservation(
//                            rs.getInt("id"),
//                            rs.getDate("date"),
//                            rs.getInt("nbrParticipants"),
//                            ModePaiement.fromString(rs.getString("modePaiement")),
//                            rs.getString("clientId"),
//                            rs.getInt("offreId"),
//                            TypeOffre.fromString(rs.getString("typeOffre"))
//                    );
//                }
//            }
//        }
//        return null;
//    }
}
