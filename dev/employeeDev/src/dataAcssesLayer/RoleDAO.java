package employeeDev.src.dataAcssesLayer;


import employeeDev.src.dtos.RoleDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private final String dbPath = DBConstants.DB_PATH;
    private final String TABLE_NAME = DBConstants.ROLE_TABLE;

    public void addOrReplaceRole(RoleDTO role) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT OR REPLACE INTO " + TABLE_NAME + " (roleName) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, role.getName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRole(String roleName) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE roleName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("roleName");
                roles.add(new RoleDTO(roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}