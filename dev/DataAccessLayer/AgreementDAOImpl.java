package DataAccessLayer;


import DataAccessLayer.interfacesDAO.AgreementDAO;
import DomainLayer.Supplier.Agreement;
import DataAccessLayer.DatabaseConnection;
import DomainLayer.Supplier.Enums.*;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public class AgreementDAOImpl implements AgreementDAO {
    private final Connection connection;

    public AgreementDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(Agreement agreement) {
        String sql = "INSERT INTO Agreements (supplier_id, payment_method, payment_timing, valid_from, valid_to) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, agreement.getSupplierId());
            stmt.setString(2, agreement.getPaymentMethod().toString());
            stmt.setString(3, agreement.getPaymentTiming().toString());
            stmt.setDate(4, Date.valueOf(agreement.getValidFrom()));
            stmt.setDate(5, Date.valueOf(agreement.getValidTo()));
            stmt.executeUpdate();
            // set agreement id (if needed)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Agreement read(int agreementId) {
        String sql = "SELECT * FROM Agreements WHERE agreement_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agreementId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Agreement(
                        rs.getString("supplier_id"),
                        rs.getInt("agreement_id"),
                        PaymentMethod.valueOf(rs.getString("payment_method")),
                        PaymentTiming.valueOf(rs.getString("payment_timing")),
                        rs.getDate("valid_from").toLocalDate(),
                        rs.getDate("valid_to").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Agreement> readAllBySupplier(String supplierId) {
        List<Agreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM Agreements WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                agreements.add(new Agreement(
                        rs.getString("supplier_id"),
                        rs.getInt("agreement_id"),
                        PaymentMethod.valueOf(rs.getString("payment_method")),
                        PaymentTiming.valueOf(rs.getString("payment_timing")),
                        rs.getDate("valid_from").toLocalDate(),
                        rs.getDate("valid_to").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agreements;
    }

    @Override
    public void update(Agreement agreement) {
        String sql = "UPDATE Agreements SET payment_method=?, payment_timing=?, valid_from=?, valid_to=? WHERE agreement_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreement.getPaymentMethod().toString());
            stmt.setString(2, agreement.getPaymentTiming().toString());
            stmt.setDate(3, Date.valueOf(agreement.getValidFrom()));
            stmt.setDate(4, Date.valueOf(agreement.getValidTo()));
            stmt.setInt(5, agreement.getAgreementId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int agreementId) {
        String sql = "DELETE FROM Agreements WHERE agreement_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agreementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBySupplier(String supplierId) {
        String sql = "DELETE FROM agreements WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
