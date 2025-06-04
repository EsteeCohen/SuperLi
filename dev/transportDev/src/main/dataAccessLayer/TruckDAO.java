package src.main.dataAccessLayer;

import src.main.dtos.TruckDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TruckDAO {

    private final String DBPath = TransportDBConstants.DB_PATH;
    private final String truckTableName = TransportDBConstants.TRUCK_TABLE;
    private final String truckLicenseTypesTableName = TransportDBConstants.TRUCK_LICENSE_TYPES_TABLE;

    public TruckDTO getTruckByLicensePlate(String licensePlate) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + truckTableName + " WHERE license_plate = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, licensePlate);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String model = rs.getString("model");
                        double emptyWeight = rs.getDouble("empty_weight");
                        double maxWeight = rs.getDouble("max_weight");
                        boolean available = rs.getBoolean("available");
                        List<String> requiredLicenseTypes = getLicenseTypesForTruck(licensePlate);

                        return new TruckDTO(licensePlate, model, emptyWeight, maxWeight, 
                                          requiredLicenseTypes, available);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TruckDTO> getAllTrucks() {
        List<TruckDTO> trucks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + truckTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String licensePlate = rs.getString("license_plate");
                    String model = rs.getString("model");
                    double emptyWeight = rs.getDouble("empty_weight");
                    double maxWeight = rs.getDouble("max_weight");
                    boolean available = rs.getBoolean("available");
                    List<String> requiredLicenseTypes = getLicenseTypesForTruck(licensePlate);

                    trucks.add(new TruckDTO(licensePlate, model, emptyWeight, maxWeight, 
                                          requiredLicenseTypes, available));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trucks;
    }

    public void insertTruck(TruckDTO truck) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "INSERT INTO " + truckTableName + 
                          " (license_plate, model, empty_weight, max_weight, available) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, truck.getLicensePlate());
                statement.setString(2, truck.getModel());
                statement.setDouble(3, truck.getEmptyWeight());
                statement.setDouble(4, truck.getMaxWeight());
                statement.setBoolean(5, truck.isAvailable());
                statement.executeUpdate();
            }

            // Insert license types
            insertLicenseTypesForTruck(conn, truck.getLicensePlate(), truck.getRequiredLicenseTypes());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTruck(TruckDTO truck) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "UPDATE " + truckTableName + 
                          " SET model = ?, empty_weight = ?, max_weight = ?, available = ? WHERE license_plate = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, truck.getModel());
                statement.setDouble(2, truck.getEmptyWeight());
                statement.setDouble(3, truck.getMaxWeight());
                statement.setBoolean(4, truck.isAvailable());
                statement.setString(5, truck.getLicensePlate());
                statement.executeUpdate();
            }

            // Update license types
            deleteLicenseTypesForTruck(conn, truck.getLicensePlate());
            insertLicenseTypesForTruck(conn, truck.getLicensePlate(), truck.getRequiredLicenseTypes());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTruck(String licensePlate) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            // Delete license types first
            deleteLicenseTypesForTruck(conn, licensePlate);
            
            String query = "DELETE FROM " + truckTableName + " WHERE license_plate = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, licensePlate);
                statement.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TruckDTO> getAvailableTrucks() {
        List<TruckDTO> trucks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + truckTableName + " WHERE available = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setBoolean(1, true);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String licensePlate = rs.getString("license_plate");
                        String model = rs.getString("model");
                        double emptyWeight = rs.getDouble("empty_weight");
                        double maxWeight = rs.getDouble("max_weight");
                        boolean available = rs.getBoolean("available");
                        List<String> requiredLicenseTypes = getLicenseTypesForTruck(licensePlate);

                        trucks.add(new TruckDTO(licensePlate, model, emptyWeight, maxWeight, 
                                              requiredLicenseTypes, available));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trucks;
    }

    private List<String> getLicenseTypesForTruck(String licensePlate) {
        List<String> licenseTypes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT license_type FROM " + truckLicenseTypesTableName + " WHERE truck_license_plate = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, licensePlate);
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

    private void insertLicenseTypesForTruck(Connection conn, String licensePlate, List<String> licenseTypes) throws SQLException {
        String query = "INSERT INTO " + truckLicenseTypesTableName + " (truck_license_plate, license_type) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (String licenseType : licenseTypes) {
                statement.setString(1, licensePlate);
                statement.setString(2, licenseType);
                statement.executeUpdate();
            }
        }
    }

    private void deleteLicenseTypesForTruck(Connection conn, String licensePlate) throws SQLException {
        String query = "DELETE FROM " + truckLicenseTypesTableName + " WHERE truck_license_plate = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, licensePlate);
            statement.executeUpdate();
        }
    }
} 