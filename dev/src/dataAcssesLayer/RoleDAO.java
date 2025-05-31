package dataAcssesLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dtos.RoleDTO;

public class RoleDAO {
    private final String dbPath = DBConstants.DB_PATH;
    private final String tableName = DBConstants.ROLE_TABLE;

    public List<RoleDTO> getRolesForEmployee(String employeeId, Connection connection) throws SQLException {
        String query = "SELECT r.* FROM " + tableName + " r " +
                       "JOIN EmployeeRoles er ON r.id = er.roleId " +
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
