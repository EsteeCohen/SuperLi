package employeeDev.src.dataAcssesLayer;

import employeeDev.src.dtos.SiteDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SiteDAO {

    private final String dbPath = DBConstants.DB_PATH;
    private final String TABLE_NAME = DBConstants.SITE_TABLE;

    public void addSite(SiteDTO site) throws SQLException{
         try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT OR REPLACE INTO " + TABLE_NAME + " (SiteName, address, contactPhone, contactName, ShippingZone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, site.getName());
            stmt.setString(2, site.getAddress());
            stmt.setString(3, site.getContactPhone());
            stmt.setString(4, site.getContactName());
            stmt.setString(5, site.getShippingZone());
            stmt.executeUpdate();
           } 
    }

    public void updateSite(SiteDTO site) throws SQLException{
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "UPDATE " + TABLE_NAME + " SET address = ?, contactPhone = ?, contactName = ?, ShippingZone = ? WHERE SiteName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, site.getAddress());
            stmt.setString(2, site.getContactPhone());
            stmt.setString(3, site.getContactName());
            stmt.setString(4, site.getShippingZone());
            stmt.setString(5, site.getName());
            stmt.executeUpdate();
        }
    }

    public void deleteSite(String siteName) throws SQLException{
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE SiteName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, siteName);
            stmt.executeUpdate();
        }
    }

    public SiteDTO getSite(String siteName) throws SQLException{
    try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE SiteName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, siteName);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                String address = rs.getString("address");
                String contactPhone = rs.getString("contactPhone");
                String contactName = rs.getString("contactName");
                String shippingZone = rs.getString("ShippingZone");
                return new SiteDTO(siteName, address, contactPhone, contactName, shippingZone);
            }
        }
        return null;
    }

    public List<SiteDTO> getAllSites() throws SQLException{
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            PreparedStatement stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery();
            List<SiteDTO> sites = new ArrayList<>();
            while (rs.next()) {
                String siteName = rs.getString("SiteName");
                String address = rs.getString("address");
                String contactPhone = rs.getString("contactPhone");
                String contactName = rs.getString("contactName");
                String shippingZone = rs.getString("ShippingZone");
                sites.add(new SiteDTO(siteName, address, contactPhone, contactName, shippingZone));
            }
            return sites;
        }
    }

    public List<SiteDTO> getSitesByShippingZone(String shippingZone) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ShippingZone = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, shippingZone);
            var rs = stmt.executeQuery();
            List<SiteDTO> sites = new ArrayList<>();
            while (rs.next()) {
                String siteName = rs.getString("SiteName");
                String address = rs.getString("address");
                String contactPhone = rs.getString("contactPhone");
                String contactName = rs.getString("contactName");
                sites.add(new SiteDTO(siteName, address, contactPhone, contactName, shippingZone));
            }
            return sites;
        }
    }


    
}
