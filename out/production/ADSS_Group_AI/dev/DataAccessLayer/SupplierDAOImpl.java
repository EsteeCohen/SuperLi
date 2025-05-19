package DataAccessLayer;

import DataAccessLayer.interfacesDAO.SupplierDAO;
import DomainLayer.Supplier.*;
import DomainLayer.Supplier.Enums.DaysOfTheWeek;
import DataAccessLayer.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class SupplierDAOImpl implements SupplierDAO {
    private final Connection connection;

    public SupplierDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(Supplier supplier) {
        String type = (supplier instanceof SupplierWithDeliveryDays) ? "delivery_days" : "needs_pickup";
        String sql = "INSERT INTO Suppliers (supplier_id, name, bank_account, supplier_type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplier.getSupplierId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getBankAccount());
            stmt.setString(4, type);
            stmt.executeUpdate();

            if (supplier instanceof SupplierWithDeliveryDays) {
                SupplierWithDeliveryDays supp = (SupplierWithDeliveryDays) supplier;
                insertDeliveryDays(supp.getSupplierId(), supp.getDeliveryDays());
            } else if (supplier instanceof SupplierNeedsPickup) {
                SupplierNeedsPickup supp = (SupplierNeedsPickup) supplier;
                insertPickupAddress(supp.getSupplierId(), supp.getAddress());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDeliveryDays(String supplierId, List<DaysOfTheWeek> days) throws SQLException {
        String sql = "INSERT INTO SupplierDeliveryDays (supplier_id, day_of_week) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (DaysOfTheWeek day : days) {
                stmt.setString(1, supplierId);
                stmt.setInt(2, day.ordinal());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertPickupAddress(String supplierId, String address) throws SQLException {
        String sql = "INSERT INTO SupplierPickupDetails (supplier_id, address) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.setString(2, address);
            stmt.executeUpdate();
        }
    }

    @Override
    public Supplier read(String supplierId) {
        String sql = "SELECT * FROM Suppliers WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("supplier_type");
                String name = rs.getString("name");
                String bankAccount = rs.getString("bank_account");

                if ("delivery_days".equals(type)) {
                    List<DaysOfTheWeek> days = getDeliveryDays(supplierId);
                    return new SupplierWithDeliveryDays(name, supplierId, bankAccount, days);
                } else if ("needs_pickup".equals(type)) {
                    String address = getPickupAddress(supplierId);
                    return new SupplierNeedsPickup(name, supplierId, bankAccount, address);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DaysOfTheWeek> getDeliveryDays(String supplierId) throws SQLException {
        List<DaysOfTheWeek> days = new ArrayList<>();
        String sql = "SELECT day_of_week FROM SupplierDeliveryDays WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int ordinal = rs.getInt("day_of_week");
                days.add(DaysOfTheWeek.values()[ordinal]);
            }
        }
        return days;
    }

    private String getPickupAddress(String supplierId) throws SQLException {
        String sql = "SELECT address FROM SupplierPickupDetails WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("address");
            }
        }
        return null;
    }

    @Override
    public List<Supplier> readAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT supplier_id FROM Suppliers";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String supplierId = rs.getString("supplier_id");
                Supplier supplier = read(supplierId);
                if (supplier != null) suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    @Override
    public void update(Supplier supplier) {
        String sql = "UPDATE Suppliers SET name=?, bank_account=? WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getBankAccount());
            stmt.setString(3, supplier.getSupplierId());
            stmt.executeUpdate();

            // עדכון מידע בטבלאות המשנה
            if (supplier instanceof SupplierWithDeliveryDays) {
                // מחיקת ימים ישנים והוספת החדשים
                deleteDeliveryDays(supplier.getSupplierId());
                insertDeliveryDays(supplier.getSupplierId(), ((SupplierWithDeliveryDays) supplier).getDeliveryDays());
            } else if (supplier instanceof SupplierNeedsPickup) {
                // עדכון כתובת איסוף
                updatePickupAddress(supplier.getSupplierId(), ((SupplierNeedsPickup) supplier).getAddress());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDeliveryDays(String supplierId) throws SQLException {
        String sql = "DELETE FROM SupplierDeliveryDays WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        }
    }

    private void updatePickupAddress(String supplierId, String newAddress) throws SQLException {
        String sql = "UPDATE SupplierPickupDetails SET address=? WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newAddress);
            stmt.setString(2, supplierId);
            int rows = stmt.executeUpdate();
            if (rows == 0) { // כתובת לא קיימת - ניצור חדשה
                insertPickupAddress(supplierId, newAddress);
            }
        }
    }

    @Override
    public void delete(String supplierId) {
        // מחיקה מהטבלאות המשניות
        try {
            deleteDeliveryDays(supplierId);
            deletePickupAddress(supplierId);
            deleteSupplierContacts(supplierId);
            // מחיקה מהטבלה הראשית
            String sql = "DELETE FROM Suppliers WHERE supplier_id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, supplierId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePickupAddress(String supplierId) throws SQLException {
        String sql = "DELETE FROM SupplierPickupDetails WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        }
    }

    private void deleteSupplierContacts(String supplierId) throws SQLException {
        String sql = "DELETE FROM SupplierContacts WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        }
    }
}
