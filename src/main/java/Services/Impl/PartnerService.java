package Services.Impl;

import Entities.Partner;
import Services.IPartnerService;
import Utils.DataSource;
import enums.PartnerCategory;
import enums.PartnerType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerService implements IPartnerService {
    private final Connection con = DataSource.getInstance().getConn();

    @Override
    public void ajouter(Partner partner) throws SQLException {
        String query = "INSERT INTO partners (name, category, type, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, partner.getName());
        pst.setString(2, partner.getCategory().name());
        pst.setString(3, partner.getType().name());
        pst.setString(4, partner.getEmail());
        pst.setString(5, partner.getPhone());
        pst.setString(6, partner.getAddress());
        pst.executeUpdate();
    }

    @Override
    public void supprimer(Partner partner) throws SQLException {
        String query = "DELETE FROM partners WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, partner.getId());
        pst.executeUpdate();
    }

    @Override
    public void update(Partner partner) throws SQLException {
        String query = "UPDATE partners SET name = ?, category = ?, type = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, partner.getName());
        pst.setString(2, partner.getCategory().name());
        pst.setString(3, partner.getType().name());
        pst.setString(4, partner.getEmail());
        pst.setString(5, partner.getPhone());
        pst.setString(6, partner.getAddress());
        pst.setInt(7, partner.getId());
        pst.executeUpdate();
    }

    @Override
    public Partner getById(int id) throws SQLException {
        String query = "SELECT * FROM partners WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Partner(
                    rs.getString("name"),
                    rs.getInt("id"),
                    PartnerCategory.valueOf(rs.getString("category")),
                    PartnerType.valueOf(rs.getString("type")),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            );
        }
        return null;
    }

    @Override
    public List<Partner> getAll() throws SQLException {
        List<Partner> partners = new ArrayList<>();
        String query = "SELECT * FROM partners";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            partners.add(new Partner(
                    rs.getString("name"),
                    rs.getInt("id"),
                    PartnerCategory.valueOf(rs.getString("category")),
                    PartnerType.valueOf(rs.getString("type")),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            ));
        }
        return partners;
    }

    public List<String> getOffresByPartner(int partnerId) throws SQLException {
        List<String> offres = new ArrayList<>();
        String query = """
                    SELECT op.offre_id, op.offre_type, 
                           CASE 
                               WHEN op.offre_type = 'VOL' THEN (SELECT v.numeroVol FROM vol v WHERE v.numeroVol = op.offre_id)
                               WHEN op.offre_type = 'SEJOUR' THEN (SELECT s.nomHotel FROM sejourhotel s WHERE s.id = op.offre_id)
                               WHEN op.offre_type = 'VOYAGE_ORGANISE' THEN (SELECT vg.id FROM voyageorganise vg WHERE vg.id = op.offre_id)
                           END AS offre
                    FROM offres_partners op
                    WHERE op.partner_id = ?
                """;
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, partnerId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String offre = rs.getString("offre");
            String type = rs.getString("offre_type");
            offres.add(type.toUpperCase() + " : " + offre);
        }

        return offres;
    }


    public List<Partner> getByFilter(String category, String type) throws SQLException {
        List<Partner> partners = new ArrayList<>();
        String query = "SELECT * FROM partners WHERE category = ? OR type = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, category);
        pst.setString(2, type);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            partners.add(new Partner(
                    rs.getString("name"),
                    rs.getInt("id"),
                    PartnerCategory.valueOf(rs.getString("category")),
                    PartnerType.valueOf(rs.getString("type")),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            ));
        }
        return partners;
    }

    public List<Partner> getByOffer(int offerId) throws SQLException {
        List<Partner> partners = new ArrayList<>();
        String query = "SELECT p.* FROM partners p " +
                "JOIN partner_offer po ON p.id = po.partner_id " +
                "WHERE po.offer_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, offerId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            partners.add(new Partner(
                    rs.getString("name"),
                    rs.getInt("id"),
                    PartnerCategory.valueOf(rs.getString("category")),
                    PartnerType.valueOf(rs.getString("type")),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            ));
        }
        return partners;
    }

    public void associatePartnerToOffer(int partnerId, int offreId, String offreType) throws SQLException {
        String query = "INSERT INTO offres_partners (offre_id, partner_id, offre_type) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, offreId);
        stmt.setInt(2, partnerId);
        stmt.setString(3, offreType);
        stmt.executeUpdate();

    }


    public void changeStatus(int partnerId, String status) throws SQLException {
        String query = "UPDATE partners SET status = ? WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, status);
        pst.setInt(2, partnerId);
        pst.executeUpdate();
    }

//    public List<Partner> getTopRatedPartners() throws SQLException {
//        List<Partner> partners = new ArrayList<>();
//        String query = "SELECT p.* FROM partners p " +
//                "JOIN reviews r ON p.id = r.partner_id " +
//                "GROUP BY p.id " +
//                "ORDER BY AVG(r.rating) DESC LIMIT 5";
//        Statement st = con.createStatement();
//        ResultSet rs = st.executeQuery(query);
//        while (rs.next()) {
//            partners.add(new Partner(
//                    rs.getString("name"),
//                    rs.getInt("id"),
//                    PartnerCategory.valueOf(rs.getString("category")),
//                    PartnerType.valueOf(rs.getString("type")),
//                    rs.getString("contact"),
//                    rs.getString("email"),
//                    rs.getString("phone"),
//                    rs.getString("address")
//            ));
//        }
//        return partners;
//    }
}
