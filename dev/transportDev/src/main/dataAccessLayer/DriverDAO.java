package transportDev.src.main.dataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.dtos.DriverDTO;
import transportDev.src.main.entities.DatabaseConnection;

public class DriverDAO {

    private final String driverTableName = TransportDBConstants.DRIVER_TABLE;
    private final String driverLicenseTypesTableName = TransportDBConstants.DRIVER_LICENSE_TYPES_TABLE;

    public DriverDTO getDriverById(String id) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + driverTableName + " WHERE employee_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String licenseType = rs.getString("licenseType");
                        boolean available = rs.getBoolean("availableToDrive");
                        
                        // For transportDev compatibility, create a simple driver
                        List<String> licenseTypes = new ArrayList<>();
                        licenseTypes.add(licenseType);

                        return new DriverDTO(id, "Driver " + id, "000-000-0000", licenseTypes, available);
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
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + driverTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("employee_id");
                    String licenseType = rs.getString("licenseType");
                    boolean available = rs.getBoolean("availableToDrive");
                    
                    // For transportDev compatibility, create a simple driver
                    List<String> licenseTypes = new ArrayList<>();
                    licenseTypes.add(licenseType);

                    drivers.add(new DriverDTO(id, "Driver " + id, "000-000-0000", licenseTypes, available));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public void insertDriver(DriverDTO driver) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "INSERT OR REPLACE INTO " + driverTableName + 
                          " (employee_id, licenseType, availableToDrive) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, driver.getId());
                // Use first license type if available
                String licenseType = !driver.getLicenseTypes().isEmpty() ? 
                                   driver.getLicenseTypes().get(0) : "C";
                statement.setString(2, licenseType);
                statement.setBoolean(3, driver.isAvailable());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDriver(DriverDTO driver) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "UPDATE " + driverTableName + 
                          " SET licenseType = ?, availableToDrive = ? WHERE employee_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Use first license type if available
                String licenseType = !driver.getLicenseTypes().isEmpty() ? 
                                   driver.getLicenseTypes().get(0) : "C";
                statement.setString(1, licenseType);
                statement.setBoolean(2, driver.isAvailable());
                statement.setString(3, driver.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(String id) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "DELETE FROM " + driverTableName + " WHERE employee_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DriverDTO> getAvailableDrivers() {
        List<DriverDTO> drivers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + driverTableName + " WHERE availableToDrive = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setBoolean(1, true);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String id = rs.getString("employee_id");
                        String licenseType = rs.getString("licenseType");
                        boolean available = rs.getBoolean("availableToDrive");
                        
                        // For transportDev compatibility, create a simple driver
                        List<String> licenseTypes = new ArrayList<>();
                        licenseTypes.add(licenseType);

                        drivers.add(new DriverDTO(id, "Driver " + id, "000-000-0000", licenseTypes, available));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // Legacy methods for compatibility - simplified since main driver info is in the Drivers table
    private List<String> getLicenseTypesForDriver(String driverId) {
        List<String> licenseTypes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT licenseType FROM " + driverTableName + " WHERE employee_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, driverId);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        licenseTypes.add(rs.getString("licenseType"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licenseTypes;
    }

    private void insertLicenseTypesForDriver(Connection conn, String driverId, List<String> licenseTypes) throws SQLException {
        // This is handled in the main driver insert since the schema stores one license type per driver
    }

    private void deleteLicenseTypesForDriver(Connection conn, String driverId) throws SQLException {
        // This is handled in the main driver update since the schema stores one license type per driver
    }
}