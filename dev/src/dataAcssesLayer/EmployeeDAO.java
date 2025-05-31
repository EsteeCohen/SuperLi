package dataAcssesLayer;

import dtos.EmployeeDTO;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDAO {
    private final String dbPath = "..//..//db.db";
    private final String tableName = "Employees";

    public EmployeeDTO getEmployeeById(String id) throws SQLException {
        EmployeeDTO employee = null;
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            conn = java.sql.DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                employee = new EmployeeDTO(
                    rs.getString("id"),
                    rs.getString("fullName"),
                    rs.getString("password"),
                    rs.getString("startDate"),
                    rs.getInt("wage"),
                    rs.getString("wageType"),
                    rs.getInt("yearlySickDays"),
                    rs.getInt("yearlyDaysOff")
                );
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return employee;
    }

    public List<EmployeeDTO> getAllEmployees() throws SQLException{
        // Implementation to retrieve all employees
        return null; // Placeholder return statement
    }

    public void instertEmployee(EmployeeDAO employee) throws SQLException{
        // Implementation to insert a new employee
    }

    public void updateEmployee(EmployeeDAO employee) throws SQLException{
        // Implementation to update an existing employee
    }

    public EmployeeDTO deleteEmployee(String id) throws SQLException{
        // Implementation to delete an employee by ID
        return null; // Placeholder return statement
    }

    public void assignRoleToEmployee(String employeeId, String roleId) throws SQLException {
        // Implementation to assign a role to an employee
    }

    public void removeRoleFromEmployee(String employeeId, String roleId) throws SQLException {
        // Implementation to remove a role from an employee
    }
}
