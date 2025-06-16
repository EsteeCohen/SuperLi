package transportDev.src.main.dataAccessLayer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.dtos.*;
import transportDev.src.main.entities.DatabaseConnection;

public class TransportDAO {

    private final String transportTableName = TransportDBConstants.TRANSPORT_TABLE;
    private final String transportDestinationsTableName = TransportDBConstants.TRANSPORT_DESTINATIONS_TABLE;
    private final TruckDAO truckDAO;
    private final DriverDAO driverDAO;
    private final SiteDAO siteDAO;

    public TransportDAO() {
        this.truckDAO = new TruckDAO();
        this.driverDAO = new DriverDAO();
        this.siteDAO = new SiteDAO();
    }

    public TransportDTO getTransportById(int id) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + transportTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        LocalDate date = LocalDate.parse(rs.getString("date"));
                        LocalTime time = LocalTime.parse(rs.getString("time"));
                        String truckLicensePlate = rs.getString("truck_license_plate");
                        String driverId = rs.getString("driver_id");
                        String sourceSiteName = rs.getString("source_site_name");
                        double currentWeight = rs.getDouble("current_weight");
                        String status = rs.getString("status");

                        TruckDTO truck = truckDAO.getTruckByLicensePlate(truckLicensePlate);
                        DriverDTO driver = driverDAO.getDriverById(driverId);
                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        List<SiteDTO> destinations = getDestinationsForTransport(id);

                        return new TransportDTO(id, date, time, truck, driver, sourceSite, 
                                              destinations, currentWeight, status);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TransportDTO> getAllTransports() {
        List<TransportDTO> transports = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + transportTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    LocalTime time = LocalTime.parse(rs.getString("time"));
                    String truckLicensePlate = rs.getString("truck_license_plate");
                    String driverId = rs.getString("driver_id");
                    String sourceSiteName = rs.getString("source_site_name");
                    double currentWeight = rs.getDouble("current_weight");
                    String status = rs.getString("status");

                    TruckDTO truck = truckDAO.getTruckByLicensePlate(truckLicensePlate);
                    DriverDTO driver = driverDAO.getDriverById(driverId);
                    SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                    List<SiteDTO> destinations = getDestinationsForTransport(id);

                    transports.add(new TransportDTO(id, date, time, truck, driver, sourceSite, 
                                                  destinations, currentWeight, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public void insertTransport(TransportDTO transport) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            conn.setAutoCommit(false);
            
            String query = "INSERT INTO " + transportTableName + 
                          " (id, date, time, truck_license_plate, driver_id, source_site_name, current_weight, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, transport.getId());
                statement.setString(2, transport.getDate().toString());
                statement.setString(3, transport.getTime().toString());
                statement.setString(4, transport.getTruck().getLicensePlate());
                statement.setString(5, transport.getDriver().getId());
                statement.setString(6, transport.getSourceSite().getName());
                statement.setDouble(7, transport.getCurrentWeight());
                statement.setString(8, transport.getStatus());
                statement.executeUpdate();
            }

            // Insert destinations
            insertDestinationsForTransport(conn, transport.getId(), transport.getDestinations());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransport(TransportDTO transport) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            conn.setAutoCommit(false);
            
            String query = "UPDATE " + transportTableName + 
                          " SET date = ?, time = ?, truck_license_plate = ?, driver_id = ?, source_site_name = ?, current_weight = ?, status = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, transport.getDate().toString());
                statement.setString(2, transport.getTime().toString());
                statement.setString(3, transport.getTruck().getLicensePlate());
                statement.setString(4, transport.getDriver().getId());
                statement.setString(5, transport.getSourceSite().getName());
                statement.setDouble(6, transport.getCurrentWeight());
                statement.setString(7, transport.getStatus());
                statement.setInt(8, transport.getId());
                statement.executeUpdate();
            }

            // Update destinations
            deleteDestinationsForTransport(conn, transport.getId());
            insertDestinationsForTransport(conn, transport.getId(), transport.getDestinations());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransport(int id) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            conn.setAutoCommit(false);
            
            // Delete destinations first
            deleteDestinationsForTransport(conn, id);
            
            String query = "DELETE FROM " + transportTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TransportDTO> getTransportsByDate(LocalDate date) {
        List<TransportDTO> transports = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + transportTableName + " WHERE date = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, date.toString());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        LocalTime time = LocalTime.parse(rs.getString("time"));
                        String truckLicensePlate = rs.getString("truck_license_plate");
                        String driverId = rs.getString("driver_id");
                        String sourceSiteName = rs.getString("source_site_name");
                        double currentWeight = rs.getDouble("current_weight");
                        String status = rs.getString("status");

                        TruckDTO truck = truckDAO.getTruckByLicensePlate(truckLicensePlate);
                        DriverDTO driver = driverDAO.getDriverById(driverId);
                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        List<SiteDTO> destinations = getDestinationsForTransport(id);

                        transports.add(new TransportDTO(id, date, time, truck, driver, sourceSite, 
                                                      destinations, currentWeight, status));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public List<TransportDTO> getTransportsByStatus(String status) {
        List<TransportDTO> transports = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + transportTableName + " WHERE status = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, status);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        LocalDate date = LocalDate.parse(rs.getString("date"));
                        LocalTime time = LocalTime.parse(rs.getString("time"));
                        String truckLicensePlate = rs.getString("truck_license_plate");
                        String driverId = rs.getString("driver_id");
                        String sourceSiteName = rs.getString("source_site_name");
                        double currentWeight = rs.getDouble("current_weight");

                        TruckDTO truck = truckDAO.getTruckByLicensePlate(truckLicensePlate);
                        DriverDTO driver = driverDAO.getDriverById(driverId);
                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        List<SiteDTO> destinations = getDestinationsForTransport(id);

                        transports.add(new TransportDTO(id, date, time, truck, driver, sourceSite, 
                                                      destinations, currentWeight, status));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    private List<SiteDTO> getDestinationsForTransport(int transportId) {
        List<SiteDTO> destinations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT site_name FROM " + transportDestinationsTableName + " WHERE transport_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, transportId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String siteName = rs.getString("site_name");
                        // Create new SiteDAO instance to avoid connection conflicts
                        SiteDAO freshSiteDAO = new SiteDAO();
                        SiteDTO site = freshSiteDAO.getSiteByName(siteName);
                        if (site != null) {
                            destinations.add(site);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinations;
    }

    private void insertDestinationsForTransport(Connection conn, int transportId, List<SiteDTO> destinations) throws SQLException {
        String query = "INSERT INTO " + transportDestinationsTableName + " (transport_id, site_name) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (SiteDTO destination : destinations) {
                statement.setInt(1, transportId);
                statement.setString(2, destination.getName());
                statement.executeUpdate();
            }
        }
    }

    private void deleteDestinationsForTransport(Connection conn, int transportId) throws SQLException {
        String query = "DELETE FROM " + transportDestinationsTableName + " WHERE transport_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, transportId);
            statement.executeUpdate();
        }
    }
} // :)