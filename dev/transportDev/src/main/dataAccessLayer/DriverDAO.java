package src.main.dataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.dtos.DriverDTO;

public class DriverDAO {

    private final String DBPath = TransportDBConstants.DB_PATH;
    private final String driverTableName = TransportDBConstants.DRIVER_TABLE;
    private final String driverLicenseTypesTableName = TransportDBConstants.DRIVER_LICENSE_TYPES_TABLE;

    public DriverDTO getDriverById(String id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + driverTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String phoneNumber = rs.getString("phone_number");
                        boolean available = rs.getBoolean("available");
                        List<String> licenseTypes = getLicenseTypesForDriver(id);

                        return new DriverDTO(id, name, phoneNumber, licenseTypes, available);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DriverDTO> getAllDrivers() {
        List<DriverDTO> drivers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + driverTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String phoneNumber = rs.getString("phone_number");
                    boolean available = rs.getBoolean("available");
                    List<String> licenseTypes = getLicenseTypesForDriver(id);

                    drivers.add(new DriverDTO(id, name, phoneNumber, licenseTypes, available));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public void insertDriver(DriverDTO driver) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "INSERT INTO " + driverTableName + 
                          " (id, name, phone_number, available) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, driver.getId());
                statement.setString(2, driver.getName());
                statement.setString(3, driver.getPhoneNumber());
                statement.setBoolean(4, driver.isAvailable());
                statement.executeUpdate();
            }

            // Insert license types
            insertLicenseTypesForDriver(conn, driver.getId(), driver.getLicenseTypes());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDriver(DriverDTO driver) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "UPDATE " + driverTableName + 
                          " SET name = ?, phone_number = ?, available = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, driver.getName());
                statement.setString(2, driver.getPhoneNumber());
                statement.setBoolean(3, driver.isAvailable());
                statement.setString(4, driver.getId());
                statement.executeUpdate();
            }

            // Update license types
            deleteLicenseTypesForDriver(conn, driver.getId());
            insertLicenseTypesForDriver(conn, driver.getId(), driver.getLicenseTypes());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(String id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            // Delete license types first
            deleteLicenseTypesForDriver(conn, id);
            
            String query = "DELETE FROM " + driverTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, id);
                statement.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DriverDTO> getAvailableDrivers() {
        List<DriverDTO> drivers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + driverTableName + " WHERE available = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setBoolean(1, true);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("name");
                        String phoneNumber = rs.getString("phone_number");
                        boolean available = rs.getBoolean("available");
                        List<String> licenseTypes = getLicenseTypesForDriver(id);

                        drivers.add(new DriverDTO(id, name, phoneNumber, licenseTypes, available));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    private List<String> getLicenseTypesForDriver(String driverId) {
        List<String> licenseTypes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT license_type FROM " + driverLicenseTypesTableName + " WHERE driver_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, driverId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        licenseTypes.add(rs.getString("license_type"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licenseTypes;
    }

    private void insertLicenseTypesForDriver(Connection conn, String driverId, List<String> licenseTypes) throws SQLException {
        String query = "INSERT INTO " + driverLicenseTypesTableName + " (driver_id, license_type) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (String licenseType : licenseTypes) {
                statement.setString(1, driverId);
                statement.setString(2, licenseType);
                statement.executeUpdate();
            }
        }
    }

    private void deleteLicenseTypesForDriver(Connection conn, String driverId) throws SQLException {
        String query = "DELETE FROM " + driverLicenseTypesTableName + " WHERE driver_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, driverId);
            statement.executeUpdate();
        }
    }
} // :)