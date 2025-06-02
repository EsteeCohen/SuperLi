package employeeDev.src.dataAcssesLayer;


import employeeDev.src.dtos.RoleDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoleDAO {
    private final String dbPath = DBConstants.DB_PATH;
    private final String TABLE_NAME = DBConstants.ROLE_TABLE;

    public void addOrReplaceRole(RoleDTO role) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT OR REPLACE INTO " + TABLE_NAME + " (roleName) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, role.getName());
            stmt.executeUpdate();
        }
    }

    public void deleteRole(String roleName) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE roleName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
    }
}