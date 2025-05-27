package DataAccessLayer;

import DataAccessLayer.DTO.SupplierDTO;
import DataAccessLayer.DTO.DeliverySupplierDTO;
import DataAccessLayer.DTO.PickupSupplierDTO;
import DataAccessLayer.interfacesDAO.SupplierDAO;
import DomainLayer.Supplier.Enums.DaysOfTheWeek;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    public SupplierDAOImpl() throws SQLException {
    }

    @Override
    public void create(SupplierDTO dto) {
        String sql = "INSERT INTO Suppliers (supplier_id, name, bank_account, supplier_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, dto.getSupplierId());
            stmt.setString(2, dto.getName());
            stmt.setString(3, dto.getBankAccount());
            stmt.setString(4, dto.getType());
            stmt.executeUpdate();

            if (dto instanceof DeliverySupplierDTO del) {
                insertDeliveryDays(del.getSupplierId(), del.getDeliveryDays());
            } else if (dto instanceof PickupSupplierDTO p) {
                insertPickupAddress(p.getSupplierId(), p.getPickupAddress());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SupplierDTO read(String supplierId) {
        String sql = "SELECT name, bank_account, supplier_type FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name        = rs.getString("name");
                    String bankAccount = rs.getString("bank_account");
                    String type        = rs.getString("supplier_type");

                    if ("delivery_days".equals(type)) {
                        List<String> days = new ArrayList<>();
                        for (DaysOfTheWeek d : getDeliveryDays(supplierId)) {
                            days.add(d.name());
                        }
                        return new DeliverySupplierDTO(name, supplierId, bankAccount, days);

                    } else if ("needs_pickup".equals(type)) {
                        String address = getPickupAddress(supplierId);
                        return new PickupSupplierDTO(name, supplierId, bankAccount, address);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SupplierDTO> readAll() {
        List<SupplierDTO> all = new ArrayList<>();

        String sql = "SELECT supplier_id FROM Suppliers";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("supplier_id");
                SupplierDTO dto = read(id);
                if (dto != null) all.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }


    @Override
    public void update(SupplierDTO dto) {
        String sql = "UPDATE Suppliers SET name = ?, bank_account = ?, supplier_type = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, dto.getName());
            stmt.setString(2, dto.getBankAccount());
            stmt.setString(3, dto.getType());
            stmt.setString(4, dto.getSupplierId());
            stmt.executeUpdate();

            deleteDeliveryDays(dto.getSupplierId());
            deletePickupAddress(dto.getSupplierId());

            if (dto instanceof DeliverySupplierDTO del) {
                insertDeliveryDays(del.getSupplierId(), del.getDeliveryDays());
            } else if (dto instanceof PickupSupplierDTO p) {
                insertPickupAddress(p.getSupplierId(), p.getPickupAddress());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String supplierId) {
        deleteDeliveryDays(supplierId);
        deletePickupAddress(supplierId);

        String sql = "DELETE FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === helper methods ===

    private void insertDeliveryDays(String supplierId, List<String> days) throws SQLException {
        String sql = "INSERT INTO SupplierDeliveryDays (supplier_id, day_of_week) VALUES (?, ?)";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            for (String d : days) {
                stmt.setString(1, supplierId);
                stmt.setString(2, d);
                stmt.executeUpdate();
            }
        }
    }

    private List<DaysOfTheWeek> getDeliveryDays(String supplierId) throws SQLException {
        List<DaysOfTheWeek> days = new ArrayList<>();
        String sql = "SELECT day_of_week FROM SupplierDeliveryDays WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    days.add(DaysOfTheWeek.valueOf(rs.getString("day_of_week")));
                }
            }
        }
        return days;
    }

    private void deleteDeliveryDays(String supplierId) {
        String sql = "DELETE FROM SupplierDeliveryDays WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertPickupAddress(String supplierId, String address) throws SQLException {
        String sql = "INSERT INTO SupplierPickupDetails (supplier_id, address) VALUES (?, ?)";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.setString(2, address);
            stmt.executeUpdate();
        }
    }

    private String getPickupAddress(String supplierId) throws SQLException {
        String sql = "SELECT address FROM SupplierPickupDetails WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("address");
                }
            }
        }
        return null;
    }

    private void deletePickupAddress(String supplierId) {
        String sql = "DELETE FROM SupplierPickupDetails WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateName(String supplierId, String name) {
        String sql = "UPDATE Suppliers SET name = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBankAccount(String supplierId, String bankAccount) {
        String sql = "UPDATE Suppliers SET bank_account = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt =DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, bankAccount);
            stmt.setString(2, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSupplierId(String oldId, String newId) {
        try {
            DatabaseConnection.getValidConnection().setAutoCommit(false);
            try (PreparedStatement p =DatabaseConnection.getValidConnection().prepareStatement(
                    "UPDATE Suppliers SET supplier_id = ? WHERE supplier_id = ?")) {
                p.setString(1, newId);
                p.setString(2, oldId);
                p.executeUpdate();
            }
            try (PreparedStatement p =DatabaseConnection.getValidConnection().prepareStatement(
                    "UPDATE SupplierDeliveryDays SET supplier_id = ? WHERE supplier_id = ?")) {
                p.setString(1, newId);
                p.setString(2, oldId);
                p.executeUpdate();
            }
            try (PreparedStatement p =DatabaseConnection.getValidConnection().prepareStatement(
                    "UPDATE SupplierPickupDetails SET supplier_id = ? WHERE supplier_id = ?")) {
                p.setString(1, newId);
                p.setString(2, oldId);
                p.executeUpdate();
            }
            DatabaseConnection.getValidConnection().commit();
        } catch (SQLException e) {
            try {DatabaseConnection.getValidConnection().rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try {DatabaseConnection.getValidConnection().setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    @Override
    public void updateDeliveryDays(String supplierId, List<String> days) {
        try {
            deleteDeliveryDays(supplierId);
            insertDeliveryDays(supplierId, days);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePickupAddress(String supplierId, String address) {
        try {
            deletePickupAddress(supplierId);
            insertPickupAddress(supplierId, address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
