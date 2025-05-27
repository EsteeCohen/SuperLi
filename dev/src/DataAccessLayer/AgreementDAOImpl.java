package DataAccessLayer;

import DataAccessLayer.DTO.AgreementDTO;
import DataAccessLayer.interfacesDAO.AgreementDAO;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AgreementDAOImpl implements AgreementDAO {

    public AgreementDAOImpl(){
    }

    @Override
    public void create(AgreementDTO dto) {
        String sql = "INSERT INTO Agreements (supplier_id, agreement_id, payment_method, payment_timing, valid_from, valid_to) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dto.getSupplierId());
            stmt.setInt(2, dto.getAgreementId());
            stmt.setString(3, dto.getPaymentMethod());
            stmt.setString(4, dto.getPaymentTiming());
            stmt.setDate(5, Date.valueOf(dto.getValidFrom()));
            stmt.setDate(6, Date.valueOf(dto.getValidTo()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            int generatedId = dto.getAgreementId();
            if (rs.next()) generatedId = rs.getInt(1);
            insertProductDiscounts(dto.getSupplierId(), generatedId, dto.getProductDiscounts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertProductDiscounts(String supplierId, int agreementId, Map<String, Map<Integer, Integer>> discounts) throws SQLException {
        String sql = "INSERT INTO AgreementProductDiscounts (agreement_id, catalog_number, supplier_id, quantity_threshold, discount_percent) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            for (Map.Entry<String, Map<Integer, Integer>> prodEntry : discounts.entrySet()) {
                String catalog = prodEntry.getKey();
                for (Map.Entry<Integer, Integer> qd : prodEntry.getValue().entrySet()) {
                    stmt.setInt(1, agreementId);
                    stmt.setString(2, catalog);
                    stmt.setString(3, supplierId);
                    stmt.setInt(4, qd.getKey());
                    stmt.setInt(5, qd.getValue());
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
        }
    }

    @Override
    public AgreementDTO read(int agreementId) {
        String sql = "SELECT * FROM Agreements WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setInt(1, agreementId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String supplierId = rs.getString("supplier_id");
                String pm = rs.getString("payment_method");
                String pt = rs.getString("payment_timing");
                LocalDate from = rs.getDate("valid_from").toLocalDate();
                LocalDate to   = rs.getDate("valid_to").toLocalDate();
                Map<String, Map<Integer, Integer>> discounts = getProductDiscounts(agreementId);
                return new AgreementDTO(supplierId, agreementId, pm, pt, from, to, discounts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Map<Integer, Integer>> getProductDiscounts(int agreementId) throws SQLException {
        Map<String, Map<Integer, Integer>> discounts = new HashMap<>();
        String sql = "SELECT catalog_number, quantity_threshold, discount_percent FROM AgreementProductDiscounts WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setInt(1, agreementId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("catalog_number");
                int qty  = rs.getInt("quantity_threshold");
                int disc = rs.getInt("discount_percent");
                discounts.computeIfAbsent(cat, k -> new HashMap<>()).put(qty, disc);
            }
        }
        return discounts;
    }

    @Override
    public List<AgreementDTO> readAllBySupplier(String supplierId) {
        List<AgreementDTO> list = new ArrayList<>();
        String sql = "SELECT agreement_id FROM Agreements WHERE supplier_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AgreementDTO dto = read(rs.getInt("agreement_id"));
                if (dto != null) list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(AgreementDTO dto) {
        String sql = "UPDATE Agreements SET payment_method=?, payment_timing=?, valid_from=?, valid_to=? WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, dto.getPaymentMethod());
            stmt.setString(2, dto.getPaymentTiming());
            stmt.setDate(3, Date.valueOf(dto.getValidFrom()));
            stmt.setDate(4, Date.valueOf(dto.getValidTo()));
            stmt.setInt(5, dto.getAgreementId());
            stmt.executeUpdate();

            deleteProductDiscounts(dto.getAgreementId());
            insertProductDiscounts(dto.getSupplierId(), dto.getAgreementId(), dto.getProductDiscounts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProductDiscounts(int agreementId) throws SQLException {
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(
                "DELETE FROM AgreementProductDiscounts WHERE agreement_id=?")) {
            stmt.setInt(1, agreementId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int agreementId) {
        try {
            deleteProductDiscounts(agreementId);
            try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(
                    "DELETE FROM Agreements WHERE agreement_id=?")) {
                stmt.setInt(1, agreementId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBySupplier(String supplierId) {
        String sql = "DELETE FROM Agreements WHERE supplier_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePaymentMethod(int agreementId, String paymentMethod) {
        String sql = "UPDATE Agreements SET payment_method=? WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, paymentMethod);
            stmt.setInt(2, agreementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePaymentTiming(int agreementId, String paymentTiming) {
        String sql = "UPDATE Agreements SET payment_timing=? WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, paymentTiming);
            stmt.setInt(2, agreementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateValidFrom(int agreementId, LocalDate validFrom) {
        String sql = "UPDATE Agreements SET valid_from=? WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(validFrom));
            stmt.setInt(2, agreementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateValidTo(int agreementId, LocalDate validTo) {
        String sql = "UPDATE Agreements SET valid_to=? WHERE agreement_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(validTo));
            stmt.setInt(2, agreementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProductDiscounts(String supplierId, int agreementId, Map<String, Map<Integer,Integer>> discounts) {
        System.out.println("check connection:" + DatabaseConnection.isConnectionActive());
        try {
            try (PreparedStatement del = DatabaseConnection.getValidConnection().prepareStatement(
                    "DELETE FROM AgreementProductDiscounts WHERE agreement_id=?")) {
                del.setInt(1, agreementId);
                del.executeUpdate();
            }
            String sql = "INSERT INTO AgreementProductDiscounts " +
                    "(agreement_id, catalog_number, supplier_id, quantity_threshold, discount_percent) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ins =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
                for (Map.Entry<String, Map<Integer,Integer>> prod : discounts.entrySet()) {
                    String catalog = prod.getKey();
                    for (Map.Entry<Integer,Integer> qd : prod.getValue().entrySet()) {
                        ins.setInt(1, agreementId);
                        ins.setString(2, catalog);
                        ins.setString(3, supplierId);
                        ins.setInt(4, qd.getKey());
                        ins.setInt(5, qd.getValue());
                        ins.addBatch();
                    }
                }
                ins.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

