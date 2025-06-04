package transportDev.src.main.dataAccessLayer;

import transportDev.src.main.dtos.SiteDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiteDAO {

    private final String DBPath = TransportDBConstants.DB_PATH;
    private final String siteTableName = TransportDBConstants.SITE_TABLE;

    public SiteDTO getSiteByName(String name) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + siteTableName + " WHERE name = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, name);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String address = rs.getString("address");
                        String phoneNumber = rs.getString("phone_number");
                        String contactPerson = rs.getString("contact_person");
                        String shippingZone = rs.getString("shipping_zone");

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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + siteTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phone_number");
                    String contactPerson = rs.getString("contact_person");
                    String shippingZone = rs.getString("shipping_zone");

                    sites.add(new SiteDTO(name, address, phoneNumber, contactPerson, shippingZone));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }

    public void insertSite(SiteDTO site) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + siteTableName + 
                          " (name, address, phone_number, contact_person, shipping_zone) VALUES (?, ?, ?, ?, ?)";
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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "UPDATE " + siteTableName + 
                          " SET address = ?, phone_number = ?, contact_person = ?, shipping_zone = ? WHERE name = ?";
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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "DELETE FROM " + siteTableName + " WHERE name = ?";
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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
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
} 