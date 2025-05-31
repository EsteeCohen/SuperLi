package dataAcssesLayer;

import dtos.RoleDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private final String dbPath = DBConstants.DB_PATH;
    private final String tableName = DBConstants.ROLE_TABLE;
    private final String employeeRolesTable = DBConstants.EMPLOYEE_ROLES_TABLE;

    public List<RoleDTO> getRolesForEmployee(String employeeId) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            String query = "SELECT r.* FROM " + tableName + " r " +
                    "JOIN " + employeeRolesTable + " er ON r.id = er.roleId " +
                    "WHERE er.employeeId = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, employeeId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<RoleDTO> roles = new ArrayList<>();
                    while (resultSet.next()) {
                        String roleName = resultSet.getString("name");
                        roles.add(new RoleDTO(roleName));
                    }
                    return roles;
                }
            }
        }
    }

    public List<RoleDTO> getAllRoles() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            String query = "SELECT * FROM " + tableName;
            try (PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery()) {
                List<RoleDTO> roles = new ArrayList<>();
                while (resultSet.next()) {
                    String roleName = resultSet.getString("name");
                    roles.add(new RoleDTO(roleName));
                }
                return roles;
            }
        }
    }
}
