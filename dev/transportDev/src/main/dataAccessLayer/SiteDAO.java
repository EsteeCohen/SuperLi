package transportDev.src.main.dataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.dtos.SiteDTO;
import transportDev.src.main.entities.DatabaseConnection;

public class SiteDAO {

    private final String siteTableName = TransportDBConstants.SITE_TABLE;

    public SiteDTO getSiteByName(String name) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + siteTableName + " WHERE SiteName = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, name);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String address = rs.getString("address");
                        String phoneNumber = rs.getString("contactPhone");
                        String contactPerson = rs.getString("contactName");
                        String shippingZone = rs.getString("ShippingZone");

                        return new SiteDTO(name, address, phoneNumber, contactPerson, shippingZone);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SiteDTO> getAllSites() {
        List<SiteDTO> sites = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + siteTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("SiteName");
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("contactPhone");
                    String contactPerson = rs.getString("contactName");
                    String shippingZone = rs.getString("ShippingZone");

                    sites.add(new SiteDTO(name, address, phoneNumber, contactPerson, shippingZone));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }

    public void insertSite(SiteDTO site) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "INSERT OR REPLACE INTO " + siteTableName + 
                          " (SiteName, address, contactPhone, contactName, ShippingZone) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, site.getName());
                statement.setString(2, site.getAddress());
                statement.setString(3, site.getPhoneNumber());
                statement.setString(4, site.getContactPerson());
                statement.setString(5, site.getShippingZone());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSite(SiteDTO site) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "UPDATE " + siteTableName + 
                          " SET address = ?, contactPhone = ?, contactName = ?, ShippingZone = ? WHERE SiteName = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, site.getAddress());
                statement.setString(2, site.getPhoneNumber());
                statement.setString(3, site.getContactPerson());
                statement.setString(4, site.getShippingZone());
                statement.setString(5, site.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSite(String name) {
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "DELETE FROM " + siteTableName + " WHERE SiteName = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SiteDTO> getSitesByShippingZone(String shippingZone) {
        List<SiteDTO> sites = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getValidConnection()) {
            String query = "SELECT * FROM " + siteTableName + " WHERE shipping_zone = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, shippingZone);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String address = rs.getString("address");
                        String phoneNumber = rs.getString("phone_number");
                        String contactPerson = rs.getString("contact_person");

                        sites.add(new SiteDTO(name, address, phoneNumber, contactPerson, shippingZone));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }
} // :)